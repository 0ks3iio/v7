package net.zdsoft.dataimport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shenke
 * @since 2017.08.08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ImportAnnotation
public @interface Exporter {

    String HEADER = "header";
    String EXAMPLE = "example";
    String CHECKED = "checked";
    String DESCRIPTION = "description";


    String example() default "";

    /**
     * 微代码和selectItems 二者选其一
     * @return
     */
    String mcode() default "";

    int displayOrder() default 0;

    String[] selectItems() default "";

    /**
     * 导出模板时默认选中
     * @return
     */
    boolean defaultChecked() default true;

    String description() default "";
}
