package org.kucro3.exception;

public class Util {
	public static void throwRuntime(Throwable e)
	{
		throw new RuntimeException(e);
	}
	
	public static void throwUntracedRuntime(Throwable e)
	{
		RuntimeException re = new RuntimeException(e);
		re.setStackTrace(Untraced.EMPTY_STACK_TRACE);
		throw re;
	}
}
