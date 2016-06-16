package org.kucro3.scptline;

public class SLExternalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SLExternalException()
	{
		super();
	}
	
	public SLExternalException(String msg)
	{
		super(msg);
	}
	
	public SLExternalException(Throwable t)
	{
		super(t);
	}
	
	public SLExternalException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
