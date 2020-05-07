package net.zdsoft.dataimport.ex;

import net.zdsoft.dataimport.biz.QImportError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 注解校验扩展点
 * @author shenke
 * @since 2017.08.09
 */
public interface AnnotationVerify<A extends Annotation> {

    /**
     * @param value 待校验的值
     * @param annotation 该字段所需校验的注解
     * @return true 执行下一个，false return
     */
     boolean verify(Object value, A annotation, QImportError error, Field field);

    /**
     * 校验顺序
     * @return
     */
    int order();
}
