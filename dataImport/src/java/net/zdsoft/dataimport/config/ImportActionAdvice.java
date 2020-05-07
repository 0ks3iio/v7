package net.zdsoft.dataimport.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shenke
 * @since 17-8-10 下午11:06
 */
@Aspect
@Component
public class ImportActionAdvice {

    public static final String ACTION_NAME = "actionName";

    private static final String ACTION = "action"; //page

    @Pointcut(value = "execution(* net.zdsoft.dataimport.biz.AbstractImportAction.import*(..))")
    public void exeuteActionNamePointcut() {

    }

    @Around(value = "exeuteActionNamePointcut()")
    public Object exeuteActionName(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class<?> action = proceedingJoinPoint.getTarget().getClass();
        getCurrentRequest().setAttribute(ACTION_NAME, getActionName(action));
        Object returnVal = proceedingJoinPoint.proceed();
        if ( returnVal != null && returnVal instanceof ModelAndView ) {
            ((ModelAndView)returnVal).addObject(ACTION, getActionName(action) + "/");
        }
        return returnVal;
    }

    private String getActionName(Class<?> action) {
        RequestMapping requestMapping = action.getAnnotation(RequestMapping.class);
        if ( requestMapping != null ) {
            String[] values = requestMapping.value();
            if ( values[0].startsWith("/")) {
                return values[0].replaceFirst("/", "");
            }
            return values[0];
        }
        return "";
    }


    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ( requestAttributes == null ) {
            return null;
        }
        return requestAttributes.getRequest();
    }
}
