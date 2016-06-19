package org.kucro3.scptline;

import org.kucro3.exception.Untraced.UntracedException;

public class SLExternalUntracedException extends SLExternalException implements UntracedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850949267877432952L;
	
	public SLExternalUntracedException()
	{
		super();
	}
	
	public SLExternalUntracedException(String msg)
	{
		super(msg);
	}
	
	public SLExternalUntracedException(Throwable t)
	{
		super(t);
	}
	
	public SLExternalUntracedException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
