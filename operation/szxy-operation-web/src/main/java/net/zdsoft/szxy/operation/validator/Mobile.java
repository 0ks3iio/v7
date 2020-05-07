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
 * @since 2019/4/9 上午11:21
 */
@Documented
@Constraint(validatedBy = MobileValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface Mobile {

    String message() default "手机号格式不合法";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
