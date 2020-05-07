package net.zdsoft.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public abstract @interface ControllerInfo {
	
	public static final int LOG_FORCE_WRITE = -1;
	public static final int LOG_FORCE_IGNORE = 1;
	public static final int LOG_FORCE_DEFAULT = 0;
	public String value() default "";

	/**
	 * 是否记录日志，0=按照规则默认，1=强制忽略，-1=强制不忽略
	 * @return
	 */
	public int ignoreLog() default 0;

	public String parameter() default "";
	
	public String operationName() default "";
}
