package org.kucro3.scptline;

import org.kucro3.decl.DeclObject;

public class SLProperty implements SLObject {
	public SLProperty(SLEnvironment env, DeclObject obj)
	{
		this.env = env;
		this.SizeOfOpStack = obj.getInt("SizeOfOpStack");
		this.IntPointEnabled = obj.getBoolean("IntPointEnabled");
	}
	
	public int getHandlerStackSize()
	{
		return SizeOfOpStack;
	}
	
	public boolean intpointEnabled()
	{
		return IntPointEnabled;
	}
	
	@Override
	public final SLEnvironment getEnv()
	{
		return env;
	}
	
	private final int SizeOfOpStack;
	
	private final boolean IntPointEnabled;
	
	private transient final SLEnvironment env;
}
