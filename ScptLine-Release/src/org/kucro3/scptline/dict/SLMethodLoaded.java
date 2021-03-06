package org.kucro3.scptline.dict;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.kucro3.exception.Untraced.UntracedException;
import org.kucro3.scptline.SLEnvironment;
import org.kucro3.scptline.dict.SLDictionaryLoader.SLExportedInfo;
import org.kucro3.scptline.dict.SLMethodParam.SLResolvedParam;
import org.kucro3.scptline.exception.SLException;

public class SLMethodLoaded extends SLExported implements SLDictionaryObject {
	SLMethodLoaded(SLEnvironment env, SLDictionaryLoaded dict, SLDictionary reference,
			SLExportedInfo metadata, Method method)
	{
		super(metadata);
		this.env = env;
		this.dict = dict;
		this.name = metadata.name();
		this.method = method;
		this.reference = new DefaultGetter(reference);
		this.owner = reference;
		this.params = params(env, metadata);
		this.paramList = Arrays.asList(params);
		this.essence = SLDictionaryLoader.TYPE_METHOD;
	}
	
	SLMethodLoaded(SLEnvironment env, SLDictionaryLoaded dict, SLDictionary owner,
			Object reference, Field field, SLExportedInfo metadata, Method method)
	{
		super(metadata);
		this.env = env;
		this.dict = dict;
		this.method = method;
		this.name = metadata.name();
		this.reference = new ReflectGetter(reference, field);
		this.owner = owner;
		this.params = params(env, metadata);
		this.paramList = Arrays.asList(params);
		this.essence = SLDictionaryLoader.TYPE_FIELD;
	}
	
	static SLResolvedParam[] params(SLEnvironment env, SLExportedInfo metadata)
	{
		String[] targs = metadata.targs();
		SLResolvedParam[] params = new SLResolvedParam[targs.length];
		for(int i = 0; i < params.length; i++)
			if((params[i] = SLMethodParam.resolveWithVariable(targs[i])) == null)
				throw SLMethodException.newUnknownParamType(env, targs[i]);
		return params;
	}
	
	@Override
	public final SLEnvironment getEnv() 
	{
		return env;
	}

	@Override
	public final SLDictionaryLoaded getDict() 
	{
		return dict;
	}
	
	public Object getReference()
	{
		return reference;
	}
	
	@Override
	public SLDictionary getOwner()
	{
		return owner;
	}
	
	public Method getMethod()
	{
		return method;
	}
	
	public int getParameterCount()
	{
		return method.getParameterCount();
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public String getRealName()
	{
		return method.getName();
	}
	
	public Class<?>[] getParameterTypes()
	{
		return method.getParameterTypes();
	}
	
	public Class<?> getReturnType()
	{
		return method.getReturnType();
	}
	
	public SLResolvedParam[] getResolvedParams()
	{
		return params;
	}
	
	public List<SLResolvedParam> getResolvedParamList()
	{
		return paramList;
	}
	
	public Object invoke(Object... args)
	{
		try {
			return method.invoke(reference.get(), args);
		} catch (IllegalAccessException e) {
			throw SLMethodException.newIllegalAccess(env, e);
		} catch (IllegalArgumentException e) {
			throw SLMethodException.newIllegalArgument(env, e);
		} catch (InvocationTargetException e) {
			throw new SLInvokingException(env, e);
		}
	}
	
	public int getEssentialTypeID()
	{
		return essence;
	}
	
	private final String name;
	
	private final SLResolvedParam[] params;
	
	private final List<SLResolvedParam> paramList;
	
	Method method;
	
	final Getter reference;
	
	private final SLDictionary owner;
	
	private final SLDictionaryLoaded dict;
	
	private final SLEnvironment env;
	
	private final int essence;
	
	public static interface Getter
	{
		public abstract Object get();
	}
	
	public class DefaultGetter implements Getter
	{
		public DefaultGetter(Object obj)
		{
			this.obj = obj;
		}
		
		@Override
		public Object get()
		{
			return obj;
		}
		
		private final Object obj;
	}
	
	public class ReflectGetter implements Getter
	{
		public ReflectGetter(Object ref, Field field)
		{
			this.ref = ref;
			this.field = field;
		}
		
		@Override
		public Object get()
		{
			try {
				return field.get(ref);
			} catch (IllegalArgumentException e) {
				throw SLMethodException.newIllegalArgument(env, e);
			} catch (IllegalAccessException e) {
				throw SLMethodException.newIllegalAccess(env, e);
			}
		}
		
		private final Object ref;
		
		private final Field field;
	}

	public class NullGetter implements Getter
	{
		@Override
		public Object get()
		{
			return null;
		}
	}
	
	public static class SLInvokingException extends SLException implements UntracedException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6224592490603126527L;
		
		public SLInvokingException(SLEnvironment env, 
				InvocationTargetException e)
		{
			super(env, DESCRIPTION, MESSAGE_);
			super.initCause(e.getCause());
		}

		public static final String DESCRIPTION = "SL_INVOKING_EXCEPTION";
		
		public static final String MESSAGE_ = "MESSAGE_SL_INVOKING_EXCEPTION";
	}
	
	public static class SLMethodException extends SLException implements UntracedException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2832676163836170957L;
		
		public SLMethodException(SLEnvironment env, String stub)
		{
			super(env, DESCRIPTION, stub);
		}
		
		public SLMethodException(SLEnvironment env, String stub,
				String message)
		{
			super(env, DESCRIPTION, stub, message);
		}
		
		public static SLMethodException newIllegalAccess(SLEnvironment env,
				IllegalAccessException e)
		{
			return new SLMethodException(env,
					MESSAGE_ILLEGAL_ACCESS,
					String.format(MESSAGE_ILLEGAL_ACCESS, e.getMessage()));
		}
		
		public static SLMethodException newIllegalArgument(SLEnvironment env,
				IllegalArgumentException e)
		{
			return new SLMethodException(env,
					MESSAGE_ILLEGAL_ARGUMENT,
					String.format(MESSAGE_ILLEGAL_ARGUMENT, e.getMessage()));
		}
		
		public static SLMethodException newUnknownParamType(SLEnvironment env,
				String name)
		{
			return new SLMethodException(env,
					MESSAGE_UNKNOWN_PARAM_TYPE,
					String.format(MESSAGE_UNKNOWN_PARAM_TYPE, name));
		}
		
		public static final String DESCRIPTION = "A exception caused by method in dictionary";
	
		public static final String MESSAGE_ILLEGAL_ACCESS = "Illegal Access: %s";
		
		public static final String MESSAGE_ILLEGAL_ARGUMENT = "Illegal Argument: %s";
	
		public static final String MESSAGE_UNKNOWN_PARAM_TYPE = "Unknown param type: %s";
	}
}
