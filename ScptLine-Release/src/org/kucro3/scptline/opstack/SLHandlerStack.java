package org.kucro3.scptline.opstack;

import org.kucro3.scptline.SLObject;
import org.kucro3.scptline.SLEnvironment;
import org.kucro3.scptline.exception.SLException;
import org.kucro3.scptline.exception.SLExternalException;

public class SLHandlerStack implements SLObject {
	public SLHandlerStack(SLEnvironment owner)
	{
		this.owner = owner;
		this.size = owner.getProperties().getHandlerStackSize();
		this.stack = new SLHandler[size];
	}

	public final int size()
	{
		return pointer;
	}
	
	@Override
	public SLEnvironment getEnv() 
	{
		return owner;
	}
	
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	public int depth()
	{
		return size;
	}
	
	public void add(SLHandler handler)
	{
		checkOverflow();
		stack[pointer++] = handler;
		update();
	}
	
	public void remove()
	{
		checkUnderflow();
		stack[--pointer] = null;
		update();
	}
	
	public SLHandler peek()
	{
		checkUnderflow();
		return stack[pointer - 1];
	}
	
	public SLHandler poll()
	{
		checkUnderflow();
		SLHandler handler = stack[--pointer];
		stack[pointer] = null;
		update();
		return handler;
	}
	
	public void update()
	{
		if(isEmpty())
			underConsole = underScript = false;
		else
		{
			SLHandler ctx = stack[pointer - 1];
			if(ctx instanceof ConsoleHandler)
				underConsole = true;
			else
				underConsole = false;
			if(ctx instanceof ScriptHandler)
				underScript = true;
			else
				underScript = false;
		}
	}
	
	public boolean underConsole()
	{
		return underConsole;
	}
	
	public boolean underScript()
	{
		return underScript;
	}
	
	private final void checkOverflow()
	{
		if(pointer + 1 > size)
			throw SLHandlerStackException.newStackOverflow(owner);
	}
	
	private final void checkUnderflow()
	{
		if(pointer < 1)
			throw SLHandlerStackException.newStackUnderflow(owner);
	}
	
	public final void internalException(SLException e)
	{
		requireHandler().internalException(this.getEnv(), e);
	}
	
	public final void externalException(SLExternalException e)
	{
		requireHandler().externalException(this.getEnv(), e);
	}
	
	public final void intpoint()
	{
		requireHandler().intpoint(this.getEnv());
	}
	
	public final boolean process(String line)
	{
		return requireHandler().process(this.getEnv(), line);
	}
	
	public final void println(String msg)
	{
		requireHandler().println(msg);
	}
	
	public final void print(String msg)
	{
		requireHandler().print(msg);
	}
	
	final SLHandler requireHandler()
	{
		SLHandler handler;
		if((handler = peek()) != null)
			return handler;
		throw SLHandlerStackException.newStackUnderflow(owner);
	}
	
	private volatile boolean underConsole;
	
	private volatile boolean underScript;
	
	private int pointer = 0;
	
	private final SLHandler[] stack;
	
	private final int size;
	
	private final SLEnvironment owner;
	
	public static class SLHandlerStackException extends SLException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2854181267941856346L;
		
		public SLHandlerStackException(SLEnvironment env,
				String stub)
		{
			super(env, DESCRIPTION, stub);
		}
		
		public SLHandlerStackException(SLEnvironment env,
				String stub, String message)
		{
			super(env, DESCRIPTION, stub, message);
		}
		
		public static SLHandlerStackException newStackOverflow(SLEnvironment env)
		{
			return new SLHandlerStackException(env,
					MESSAGE_STACK_OVERFLOW,
					MESSAGE_STACK_OVERFLOW);
		}
		
		public static SLHandlerStackException newStackUnderflow(SLEnvironment env)
		{
			return new SLHandlerStackException(env,
					MESSAGE_STACK_UNDERFLOW,
					MESSAGE_STACK_UNDERFLOW);
		}
		
		public static final String MESSAGE_STACK_OVERFLOW = "Handler stack overflow";
		
		public static final String MESSAGE_STACK_UNDERFLOW = "Handler stack underflow";
		
		public static final String DESCRIPTION = "A exception caused by handler stack";
	}
}
