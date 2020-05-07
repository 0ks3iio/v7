package net.zdsoft.framework.config;

import java.util.Collection;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

public class MethodTimeAdvice implements MethodInterceptor {

	public static final Logger log = Logger.getLogger(MethodTimeAdvice.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 用 commons-lang 提供的 StopWatch 计时
		StopWatch clock = new StopWatch();
		clock.start(); // 计时开始
		Object result = invocation.proceed();
		clock.stop(); // 计时结束

		Object[] args = invocation.getArguments();
		Object[] newArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg instanceof Collection<?>) {
				newArgs[i] = "List[" + ((List) arg).size() + "]";
			} else if (arg instanceof String[]) {
				newArgs[i] = "String[" + ArrayUtils.toArray(arg).length + "]";
			} else {
				newArgs[i] = arg;
			}
		}

		String logStr = "消耗:" + clock.getTime() + " 毫秒 [" + invocation.getThis().getClass().getSimpleName() + "."
				+ invocation.getMethod().getName() + "(" + StringUtils.join(newArgs, ",") + ")] ";
		if (clock.getTime() >= 300)
			log.warn(logStr);
		else if (clock.getTime() >= 100)
			log.info(logStr);
		else
			log.debug(logStr);

		return result;

	}

}
