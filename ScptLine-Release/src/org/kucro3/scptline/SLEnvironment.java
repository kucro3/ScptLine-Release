package org.kucro3.scptline;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import org.kucro3.decl.Decl;
import org.kucro3.decl.DeclObject;
import org.kucro3.scptline.dict.SLDictionaryCollection;
import org.kucro3.scptline.dict.SLDictionaryFactory;
import org.kucro3.scptline.dict.SLDictionaryLoaded;
import org.kucro3.scptline.dict.SLDictionaryLoader;
import org.kucro3.scptline.opstack.SLHandler;
import org.kucro3.scptline.opstack.SLHandlerStack;

public class SLEnvironment implements SLExceptionHandler {
	public SLEnvironment(String... config)
	{
		this(Decl.toDecl(config));
	}
	
	public SLEnvironment(File config) throws IOException
	{
		this(Decl.toDecl(config));
	}
	
	SLEnvironment(DeclObject obj)
	{
		_booting();
		this.reg = new SLRegister(this);
		this.property = new SLProperty(this, obj);
		Decl.toObject(property, obj);
		this.opstack = new SLHandlerStack(this);
		this.collection = SLDictionaryFactory.newCollection(this);
		this.intpointEnabled = property.intpointEnabled();
		this.initHandlers();
		_idle();
	}
	
	public final boolean isBooting()
	{
		return state == SLEnvState.BOOTING;
	}
	
	public final boolean isLoadingInline()
	{
		return (state == SLEnvState.LOADING) && (lastState == SLEnvState.BOOTING);
	}
	
	public final SLProperty getProperties()
	{
		return property;
	}
	
	public final SLHandlerStack getHandlerStack()
	{
		return opstack;
	}
	
	public final SLDictionaryCollection getDictionaries()
	{
		return collection;
	}
	
	public final SLRegister getRegister()
	{
		return reg;
	}
	
	@Override
	public final boolean handle(SLEnvironment env, SLException e)
	{
		if(env != this)
			InternalError.IntersectedFunctionCall();
		e.printStackTrace();
		return true;
	}
	
	final void __state(SLEnvState state)
	{
		this.lastState = this.state;
		this.state = state;
	}
	
	final void _booting()
	{
		__state(SLEnvState.BOOTING);
	}
	
	final void _idle()
	{
		__state(SLEnvState.IDLE);
	}
	
	final void __state_last()
	{
		__state(lastState);
	}
	
	final void _executing()
	{
		__state(SLEnvState.EXECUTING);
	}
	
	final void _intpoint()
	{
		__state(SLEnvState.INTPOINT_CALLBACK);
	}
	
	final void _exception()
	{
		__state(SLEnvState.FAILURE_CALLBACK);
	}
	
	final void _loading()
	{
		__state(SLEnvState.LOADING);
	}
	
	public final SLEnvState getState()
	{
		return state;
	}
	
	public final SLEnvState getLastState()
	{
		return lastState;
	}
	
	public SLDictionaryLoaded load(File file)
	{
		return load0(SLDictionaryFactory::load, file);
	}
	
	public SLDictionaryLoaded load(URL url)
	{
		return load0(SLDictionaryFactory::load, url);
	}
	
	public SLDictionaryLoaded load(DeclObject profile)
	{
		return load0(SLDictionaryFactory::load, profile);
	}
	
	public SLDictionaryLoaded load(Class<?> clz)
	{
		DeclObject profile = new DeclObject();
		profile.putString(SLDictionaryLoader.FIELD_MAIN_CLASS, clz.getTypeName());
		SLDictionaryLoaded loaded = load0(SLDictionaryFactory::load, profile);
		if(loaded == null)
			return null;
		SLDictionaryFactory.bind(collection, loaded);
		return loaded;
	}
	
	public void execute(String... lines)
	{
		for(int i = 0; i < lines.length; i++)
			execute(lines[i], i + 1);
	}
	
	public boolean execute(String line)
	{
		return execute(line, 1);
	}
	
	public boolean execute(String line, int linenumber)
	{
		try {
			try {
				_executing();
				String[] lines = opstack.preprocess(line);
				if(lines == null)
					return false;
				boolean r = opstack.process(lines);
				_intpoint();
				if(this.intpointEnabled)
					opstack.intpoint();
				return r;
			} catch (SLException e) {
				this.exception(e);
			} catch (Exception e) {
				this.uncaughtException(e);
			}
		} finally {
			_idle();
		}
		return false;
	}
	
	public final void exception(SLException e)
	{
		try {
			_exception();
			SLExceptionHandler handler;
			if(exceptionHandlers == null
					|| (handler = exceptionHandlers.get(getLastState())) == null)
				uncaughtException(e);
			else if(!handler.handle(this, e))
				uncaughtException(e);
		} catch (Exception e0) {
			uncaughtException(e0);
		} finally {
			__state_last();
		}
	}
	
	final void uncaughtException(Exception e)
	{
		// TODO handle all the uncaught exceptions
		e.printStackTrace();
	}
	
	private final void initHandlers()
	{
		this.exceptionHandlers.put(SLEnvState.IDLE, this);
		this.exceptionHandlers.put(SLEnvState.BOOTING, this);
		this.exceptionHandlers.put(SLEnvState.EXECUTING, new SLOpStackExceptionHandler());
	}
	
	private final <T> SLDictionaryLoaded load0(LambdaLoading<T> lambda, T v)
	{
		SLEnvState last = state;
		try {
			try {
				_loading();
				return lambda.function(this, v);
			} catch (SLException e) {
				this.exception(e);
			} catch (Exception e) {
				this.uncaughtException(e);
			}
		} finally {
			__state(last);
		}
		return null;
	}
	
	final Map<SLEnvState, SLExceptionHandler> exceptionHandlers = new HashMap<>();
	
	private final SLDictionaryCollection collection;
	
	private final SLHandlerStack opstack;
	
	private final SLProperty property;
	
	private final SLRegister reg;
	
	volatile SLEnvState lastState;
	
	volatile SLEnvState state;
	
	private final boolean intpointEnabled;
	
	public static enum SLEnvState
	{
		BOOTING,
		LOADING,
		IDLE,
		EXECUTING,
		INTPOINT_CALLBACK,
		FAILURE_CALLBACK;
	}
	
	interface LambdaLoading<T>
	{
		public abstract SLDictionaryLoaded function(SLEnvironment env, T t);
	}
	
	private final class SLOpStackExceptionHandler implements SLExceptionHandler
	{
		private SLOpStackExceptionHandler()
		{
		}
		
		@Override
		public boolean handle(SLEnvironment env, SLException e) 
		{
			SLHandler handler = opstack.peek();
			if(handler == null)
				return false;
			return handler.handle(SLEnvironment.this, e);
		}
	}
}
