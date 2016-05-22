package org.kucro3.scptline.anno;

import java.lang.annotation.*;

/**
 * ����ע��ע�͵ķ������򽫱���Ϊ����Ԫ��.
 * ���ҵ����ĺ���������ϲ�������,�����������<code>org.kucro3.scptline.SLRuntimeObject</code>.
 * @see org.kucro3.scptline.SLRuntimeObject
 * @author Kumonda221
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(SLExport.SLExportCollection.class)
public @interface SLExport {
	String[] targs() default {};
	
	String name();
	
	String delegate() default "";
	
	Class<?>[] delegateParams() default {};
	
	boolean directInput() default false;
	
	int argCount() default -1;
	
	String type();
	
	@Target({ElementType.FIELD, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	public static @interface SLExportCollection
	{
		SLExport[] value();
	}
}
