package net.zdsoft.dataimport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shenke on 2017-7-31.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ImportAnnotation
public @interface Valid {


    boolean notNull() default false;

    /**
     * 字符串最大长度
     * @return
     */
    int length() default Integer.MAX_VALUE;

    /**
     * 非负
     * @return
     */
    boolean nonNegative() default false;

    /**
     * 整数位长度
     * @return
     */
    int precision() default Integer.MAX_VALUE;

    /**
     * 小数位长度
     * @return
     */
    int decimal() default Integer.MAX_VALUE;


    String[] format() default "";
}
