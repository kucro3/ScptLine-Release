package org.kucro3.scptline.opstack;

import org.kucro3.scptline.*;

public abstract class SLHandler implements SLRuntimeObject, SLExceptionHandler {
	protected SLHandler()
	{
		this(null);
	}
	
	protected SLHandler(SLHandler parent)
	{
		this.parent = parent;
	}
	
	public final boolean isTop()
	{
		return parent() == null;
	}
	
	public final SLHandler getTop()
	{
		SLHandler temp = this;
		if(top == null)
			while(true)
			{
				if(temp.isTop())
					return this.top = temp;
				temp = temp.parent();
				
				assert temp.parent() != null;
			}
		else
			return top;
	}
	
	public void intpoint(SLEnvironment env)
	{
		if(parent != null)
			parent.intpoint(env);
	}
	
	public void internalException(SLEnvironment env, SLException e)
	{
		if(parent != null)
			parent.internalException(env, e);
	}
	
	public void externalException(SLEnvironment env, SLExternalException e) 
	{
		if(parent != null)
			parent.externalException(env, e);
	}
	
	public boolean process(SLEnvironment env, String line)
	{
		if(parent != null)
			return parent.process(env, line);
		return true;
	}
	
	protected final SLHandler parent()
	{
		return parent;
	}
	
	public final boolean handle(SLEnvironment env, SLException e)
	{
		this.internalException(env, e);
		return true;
	}
	
	private SLHandler parent;
	
	SLHandler top;
}
