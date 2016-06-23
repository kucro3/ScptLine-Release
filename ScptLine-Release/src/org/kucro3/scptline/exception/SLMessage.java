package org.kucro3.scptline.exception;

import org.kucro3.exception.Untraced.UntracedException;

public class SLMessage extends RuntimeException implements UntracedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7251879477672608368L;
	
	public SLMessage(String msg)
	{
		super(msg);
	}
	
	public static SLMessage needMoreArguments()
	{
		return new SLMessage("Need more arguments");
	}
	
	public static SLMessage tooManyArguments()
	{
		return new SLMessage("Too many arguments");
	}
	
	public static void checkArgument(String[] args, int length)
	{
		checkArgument(args, length, length);
	}
	
	public static void checkArgument(String[] args, int min, int max)
	{
		if(min > 0 && args.length < min)
			throw SLMessage.needMoreArguments();
		if(max > 0 && args.length > max)
			throw SLMessage.tooManyArguments();
	}
	
	public final int reserved()
	{
		return 1;
	}
}
