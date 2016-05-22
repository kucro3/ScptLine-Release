package org.kucro3.scptline;

/**
 * �����е�����ʱ����.
 * �˴���������κ�һ������������κ�һ�������б��׳�.
 * ���Ҵ˴���Ҳ���ϻ�������Ĺ淶,���ұ�һ��ָ��������ӵ��.
 * @author Administrator
 *
 */
public abstract class SLException extends RuntimeException implements SLObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3816066002568652275L;
	
	protected SLException(SLEnvironment environment, SLExceptionLevel level)
	{
		this(environment, level, DEFUALT_DESCRIPTION);
	}
	
	protected SLException(SLEnvironment environment, SLExceptionLevel level,
			String description)
	{
		this(environment, level, description, null);
	}
	
	protected SLException(SLEnvironment environment, SLExceptionLevel level,
			String description, String message)
	{
		this(environment, level, description, message, message);
	}
	
	protected SLException(SLEnvironment envrionment, SLExceptionLevel level,
			String description, String messageStub, String message)
	{
		super(message);
		this.owner = envrionment;
		this.level = level;
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
	
	public final SLExceptionLevel getLevel()
	{
		return level;
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
	
	private final SLExceptionLevel level;
	
	private final SLEnvironment owner;
	
	public static final String DEFUALT_DESCRIPTION = "A ScptLine Environment runtime exception";

	public static enum SLExceptionLevel
	{
		CONTINUE,
		INTERRUPT,
		STOP,
		CRASH,
		SIGN;
	}
}
