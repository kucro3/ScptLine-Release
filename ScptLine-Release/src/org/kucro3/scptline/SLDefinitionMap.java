package org.kucro3.scptline;

import java.util.Map;

import org.kucro3.scptline.exception.SLException;

import java.util.HashMap;

public class SLDefinitionMap implements SLObject {
	public SLDefinitionMap(SLEnvironment env)
	{
		this.env = env;
	}
	
	@Override
	public SLEnvironment getEnv()
	{
		return env;
	}
	
	public boolean declared(String name)
	{
		return map.containsKey(name);
	}
	
	public void declare(String name)
	{
		if(declared(name))
			throw SLDefinitionMapException.newRedeclaration(env, name);
		set(name, NULL);
	}
	
	public void checkDeclaration(String name)
	{
		if(!declared(name))
			throw SLDefinitionMapException.newVarNotDeclared(env, name);
	}
	
	public void set(String name, Object obj)
	{
		if(obj == null)
			obj = NULL;
		this.map.put(name, obj);
	}
	
	public void put(String name, Object obj)
	{
		checkDeclaration(name);
		set(name, obj);
	}
	
	public void putIfDeclared(String name, Object obj)
	{
		if(declared(name))
			set(name, obj);
	}
	
	public Object get(String name)
	{
		Object obj = map.get(name);
		if(obj == null || obj == NULL)
			return null;
		return obj;
	}
	
	public Object require(String name)
	{
		checkDeclaration(name);
		return get(name);
	}
	
	private static final Object NULL = new Object();
	
	private final Map<String, Object> map = new HashMap<>();
	
	private final SLEnvironment env;
	
	public static class SLDefinitionMapException extends SLException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5904755072313415819L;
		
		public SLDefinitionMapException(SLEnvironment env,
				String message)
		{
			super(env, DESCRIPTION, message, message);
		}
		
		public SLDefinitionMapException(SLEnvironment env,
				String stub, String message)
		{
			super(env, DESCRIPTION, stub, message);
		}
		
		public static SLDefinitionMapException newVarNotDeclared(SLEnvironment env, String var)
		{
			return new SLDefinitionMapException(env,
					MESSAGE_VAR_NOT_DECLARED,
					String.format(MESSAGE_VAR_NOT_DECLARED, var));
		}
		
		public static SLDefinitionMapException newRedeclaration(SLEnvironment env, String var)
		{
			return new SLDefinitionMapException(env,
					MESSAGE_VAR_REDECLARATION,
					String.format(MESSAGE_VAR_REDECLARATION, var));
		}
		
		public static final String MESSAGE_VAR_NOT_DECLARED = "Variable not declared: %s";
		
		public static final String MESSAGE_VAR_REDECLARATION = "Redeclaration: %s";
		
		public static final String DESCRIPTION = "An exception occurred in definition map";
	}
}
