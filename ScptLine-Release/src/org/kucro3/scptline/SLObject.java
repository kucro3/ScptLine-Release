package org.kucro3.scptline;

/**
 * �κ�һ���ᱻ���л�����ӵ�еĻ�������<strong>��Ӧʵ��</strong>�˽ӿ�. 
 * ����ʵ�ִ˽ӿڵĻ�����������з��ϲ�����ʽ�Ĺ��췽��. 
 * ������ʽΪ: (SLEnvironment, ...)
 * @author Kumonda221
 * 
 */
public interface SLObject {
	/**
	 * ����ӵ�д˶�������л���.
	 * @return ���л���
	 */
	public SLEnvironment getEnv();
}
