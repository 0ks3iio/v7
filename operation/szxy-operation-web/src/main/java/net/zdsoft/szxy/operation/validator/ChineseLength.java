package net.zdsoft.szxy.operation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author shenke
 * @since 2019/4/10 下午1:58
 */
@Documented
@Constraint(validatedBy = MobileValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ChineseLength {

    int max() default Integer.MAX_VALUE;

    String message() default "长度不合法";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
