package org.kucro3.exception;

public final class Untraced {
	private Untraced()
	{
	}
	
	public static void untrace(UntracedException e)
	{
		int reserved = e.reserved();
		StackTraceElement[] elements = 
				reserved == 0 ? EMPTY_STACK_TRACE : new StackTraceElement[reserved],
						old = e.getStackTrace();
		for(int i = 0; i < reserved; i++)
			elements[i] = old[i];
		e.setStackTrace(elements);
	}
	
	public static void untrace(Throwable e)
	{
		if(e instanceof UntracedException)
			untrace((UntracedException)e);
	}
	
	public static void untraceAll(Throwable e)
	{
		do 
			untrace(e);
		while((e = e.getCause()) != null);
	}
	
	public static interface UntracedException
	{
		abstract StackTraceElement[] getStackTrace(); // implemented by java.lang.Throwable
		
		abstract void setStackTrace(StackTraceElement[] elements); // implemented by java.lang.Throwable
		
		default int reserved() {return 0;}
	}
	
	static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
}
