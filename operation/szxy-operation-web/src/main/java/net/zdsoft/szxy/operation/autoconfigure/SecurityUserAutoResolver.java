package net.zdsoft.szxy.operation.autoconfigure;

import lombok.Setter;
import net.zdsoft.szxy.base.enu.UserSexCode;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.security.SecurityUser;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * 在使用Spring-security-test时会有问题
 * WithMockUser创建的是User对象
 *
 * @author shenke
 * @since 2019/1/11 下午1:54
 */
@Component
public class SecurityUserAutoResolver implements HandlerMethodArgumentResolver {

    private ExpressionParser parser = new SpelExpressionParser();
    @Setter
    private BeanResolver beanResolver;
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return findMethodAnnotation(AuthenticationPrincipal.class, parameter) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        //Mock test
        if (principal instanceof User) {
            //convert to SecurityUser
            principal = userDetailsService.loadUserByUsername(((UserDetails) principal).getUsername());
        }
        //anonymous user
        if (principal instanceof String) {
            if (org.apache.commons.lang3.StringUtils.startsWith((String)principal, "anonymous")) {
                principal = new AnonymousUser();
            }
        }

        CurrentUser currentUser = findMethodAnnotation(CurrentUser.class, parameter);
        AuthenticationPrincipal authPrincipal = findMethodAnnotation(
                AuthenticationPrincipal.class, parameter);
        String expressionToParse = expression(authPrincipal, currentUser);
        if (StringUtils.hasLength(expressionToParse)) {
            StandardEvaluationContext context = new StandardEvaluationContext();
            if (!CurrentUser.CURRENT_USER.equals(expressionToParse)) {
                context.setRootObject(principal);
            } else {
                return principal;
            }
            context.setVariable("this", principal);
            context.setBeanResolver(beanResolver);

            Expression expression = this.parser.parseExpression(expressionToParse);
            principal = expression.getValue(context);
        }
        if (principal != null
                && !parameter.getParameterType().isAssignableFrom(principal.getClass())) {

            if (errorOnInvalidType(authPrincipal, currentUser)) {
                throw new ClassCastException(principal + " is not assignable to "
                        + parameter.getParameterType());
            } else {
                return null;
            }
        }
        return principal;
    }

    private String expression(AuthenticationPrincipal principal, CurrentUser currentUser) {
        if (currentUser != null) {
            return currentUser.expression();
        }
        return principal.expression();
    }

    private boolean errorOnInvalidType(AuthenticationPrincipal principal, CurrentUser currentUser) {
        if (currentUser != null) {
            return currentUser.errorOnInvalidType();
        }
        return principal.errorOnInvalidType();
    }

    private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
                                                          MethodParameter parameter) {
        T annotation = parameter.getParameterAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
        for (Annotation toSearch : annotationsToSearch) {
            annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(),
                    annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    private static class AnonymousUser implements SecurityUser {
        @Override
        public String getPhone() {
            return "anonymousPhone";
        }

        @Override
        public String getRealName() {
            return "anonymousRealName";
        }

        @Override
        public String getEmail() {
            return "anonymousEmail";
        }

        @Override
        public String getUsername() {
            return "anonymousUsername";
        }

        @Override
        public String getId() {
            return "anonymousUserId";
        }

        @Override
        public Integer getSex() {
            return UserSexCode.MALE;
        }
    }
}
