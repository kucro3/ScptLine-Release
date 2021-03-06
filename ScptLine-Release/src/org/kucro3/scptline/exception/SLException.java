package org.kucro3.scptline.exception;

import org.kucro3.scptline.SLEnvironment;
import org.kucro3.scptline.SLObject;

/**
 * 环境中的运行时错误.
 * 此错误可以在任何一个环境对象的任何一个方法中被抛出.
 * 并且此错误也符合环境对象的规范,并且被一个指定环境所拥有.
 * @author Administrator
 *
 */
public abstract class SLException extends RuntimeException implements SLObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3816066002568652275L;
	
	protected SLException(SLEnvironment environment)
	{
		this(environment, DEFUALT_DESCRIPTION);
	}
	
	protected SLException(SLEnvironment environment,
			String description)
	{
		this(environment, description, null);
	}
	
	protected SLException(SLEnvironment environment,
			String description, String message)
	{
		this(environment, description, message, message);
	}
	
	protected SLException(SLEnvironment envrionment,
			String description, String messageStub, String message)
	{
		super(message);
		this.owner = envrionment;
		this.description = description;
		this.descriptionStub = description;
		this.descriptionStubHash = description.hashCode();
		this.messageStub = messageStub;
		this.messageStubHash = messageStub.hashCode();
	}
	
	@Override
	public final SLEnvironment getEnv()
	{
		return owner;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public final String getDescriptionStub()
	{
		return descriptionStub;
	}
	
	public final String getMessageStub()
	{
		return messageStub;
	}
	
	public boolean equalsMessageStub(String s)
	{
		return s.hashCode() == messageStubHash;
	}
	
	public boolean equalsDescriptionStub(String s)
	{
		return s.hashCode() == descriptionStubHash;
	}
	
	protected String description;
	
	private final String descriptionStub;
	
	private final int descriptionStubHash;
	
	private final String messageStub;
	
	private final int messageStubHash;
	
	private final SLEnvironment owner;
	
	public static final String DEFUALT_DESCRIPTION = "A ScptLine Environment runtime exception";
}
