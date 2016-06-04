package org.kucro3.scptline;

import java.util.LinkedList;

public class SLDefinitionStack implements SLObject {
	public SLDefinitionStack(SLEnvironment env)
	{
		this.env = env;
	}
	
	public SLDefinitionMap peek()
	{
		return stack.getFirst();
	}
	
	public SLDefinitionMap poll()
	{
		SLDefinitionMap obj = peek();
		pop();
		return obj;
	}
	
	public void pop()
	{
		stack.removeFirst();
	}
	
	public void push(SLDefinitionMap map)
	{
		stack.addFirst(map);
	}
	
	public void pushNew()
	{
		push(new SLDefinitionMap(env));
	}
	
	public void dup()
	{
		push(peek());
	}
	
	public void _checkDeclaraction(String name)
	{
		peek().checkDeclaration(name);
	}
	
	public void _declare(String name)
	{
		peek().declare(name);
	}
	
	public boolean _declared(String name)
	{
		return peek().declared(name);
	}
	
	public Object _get(String name)
	{
		return peek().get(name);
	}
	
	public void _put(String name, Object obj)
	{
		peek().put(name, obj);
	}
	
	public Object _require(String name)
	{
		return peek().require(name);
	}
	
	public void _set(String name, Object obj)
	{
		peek().set(name, obj);
	}
	
	@Override
	public SLEnvironment getEnv()
	{
		return env;
	}
	
	private final LinkedList<SLDefinitionMap> stack = new LinkedList<>();
	
	private final SLEnvironment env;
}
