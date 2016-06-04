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
		this.reg = new SLRegister(this);
		this.property = new SLProperty(this, obj);
		Decl.toObject(property, obj);
		this.opstack = new SLHandlerStack(this);
		this.collection = SLDictionaryFactory.newCollection(this);
		this.intpointEnabled = property.intpointEnabled();
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
				String[] lines = opstack.preprocess(line);
				if(lines == null)
					return false;
				boolean r = opstack.process(lines);
				
				if(this.intpointEnabled)
					opstack.intpoint();
				return r;
			} catch (SLException e) {
				this.uncaughtException(e);
			}
		return false;
	}
	
	final void uncaughtException(Exception e)
	{
		// TODO handle all the uncaught exceptions
		e.printStackTrace();
	}
	
	private final <T> SLDictionaryLoaded load0(LambdaLoading<T> lambda, T v)
	{
		try {
			return lambda.function(this, v);
		} catch (SLException e) {
			this.uncaughtException(e);
		}
		return null;
	}
	
	private final SLDictionaryCollection collection;
	
	private final SLHandlerStack opstack;
	
	private final SLProperty property;
	
	private final SLRegister reg;
	
	private final boolean intpointEnabled;
	
	interface LambdaLoading<T>
	{
		public abstract SLDictionaryLoaded function(SLEnvironment env, T t);
	}
}
