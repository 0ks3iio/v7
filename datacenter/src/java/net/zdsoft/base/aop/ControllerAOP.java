package net.zdsoft.base.aop;

import net.zdsoft.base.dto.ResultBean;
import net.zdsoft.base.exception.CheckException;
import net.zdsoft.base.exception.NoPermissionException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



/**
 * 处理和包装异常
 * 
 */
@Aspect    //该标签把LoggerAspect类声明为一个切面
@Order(1)  //设置切面的优先级：如果有多个切面，可通过设置优先级控制切面的执行顺序（数值越小，优先级越高）
@Component("controllerAopApi") //该标签把LoggerAspect类放到IOC容器中
public class ControllerAOP {
	private static final Logger logger = LoggerFactory.getLogger(ControllerAOP.class);
	
	 /**
     * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码
     * 使用@Pointcut声明切入点表达式
     * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
     */
    @Pointcut("execution(public net.zdsoft.base.dto.ResultBean *(..))")
    public void declearJoinPointExpression(){}


	@Around(value="declearJoinPointExpression()")
	public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
		long startTime = System.currentTimeMillis();

		ResultBean<?> result;

		try {
			result = (ResultBean<?>) pjp.proceed();
			
			// 如果需要打印入参，可以从这里取出打印
			// Object[] args = pjp.getArgs();

			// 本次操作用时（毫秒）
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.info("[{}]use time: {}", pjp.getSignature(), elapsedTime);
		} catch (Throwable e) {
			result = handlerException(pjp, e);
		}

		return result;
	}

	private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
		ResultBean<?> result = new ResultBean();

		// 已知异常【注意：已知异常不要打印堆栈，否则会干扰日志】
		// 校验出错，参数非法
		if (e instanceof CheckException) {
			result.setMsg(e.getLocalizedMessage());
			result.setCode(ResultBean.CHECK_FAIL);
		}
		else if (e instanceof IllegalArgumentException) {
			result.setMsg("参数错误");
			result.setCode(ResultBean.CHECK_FAIL);
		}
		// 没有权限
		else if (e instanceof NoPermissionException) {
			result.setMsg("没有权限");
			result.setCode(ResultBean.NO_PERMISSION);
		} else {
			logger.error(pjp.getSignature() + " error ", e);

			// TODO 未知的异常，应该格外注意，可以发送邮件通知等
			result.setMsg(e.toString());
			result.setCode(ResultBean.UNKNOWN_EXCEPTION);
		}

		return result;
	}
}
