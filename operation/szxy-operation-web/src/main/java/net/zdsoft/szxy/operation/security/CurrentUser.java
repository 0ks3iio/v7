package net.zdsoft.szxy.operation.security;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shenke
 * @since 2019/1/11 下午2:04
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = CurrentUser.CURRENT_USER)
public @interface CurrentUser {

    String CURRENT_USER = "CURRENT_USER";

    /**
     *
     */
    @AliasFor("expression")
    String value() default CURRENT_USER;

    /**
     * 可指定OpUser字段的名字
     * @see AuthenticationPrincipal#expression()
     */
    @AliasFor("value")
    String expression() default CURRENT_USER;

    /**
     * @see AuthenticationPrincipal#errorOnInvalidType()
     */
    boolean errorOnInvalidType() default true;
}
