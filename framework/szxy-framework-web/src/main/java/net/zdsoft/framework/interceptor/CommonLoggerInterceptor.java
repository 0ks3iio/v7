package net.zdsoft.framework.interceptor;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;

public class CommonLoggerInterceptor implements HandlerInterceptor {

	private static final Pattern pattern = Pattern.compile("\\{\\w*\\}");
	private static final String METHOD_NAME_FOR_LOG_REGEX = "^[\\w_/]*(add|save|update|delete|insert|remove|modify|init|set|register)+[\\w_/]*";
	private static final String METHOD_NAME_FOR_WITHOUT_LOG_REGEX = "^(show|list|get|take|fetch|find)+[\\w_/]*";

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception arg3) {

	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object obj, ModelAndView view) {
		JSONObject ol = logOperation(req, obj);
		showMethodInfo(req, obj, view, ol);
	}

	private static DateTimeFormatter requestLoggerTimeFormatter = DateTimeFormatter.ofPattern("H:m:s:S");
	private static ModelAndView emptyModel = new ModelAndView("");

	private void showMethodInfo(HttpServletRequest req, Object obj, ModelAndView view, JSONObject ol) {
		if (!Evn.isDevModel() || !(obj instanceof HandlerMethod)) {
			return;
		}
		HandlerMethod hm = (HandlerMethod) obj;
		String msg = assemblyRequestLogger(req, view, ol, hm);
	}

	private String assemblyRequestLogger(HttpServletRequest req, ModelAndView view, JSONObject ol, HandlerMethod hm) {
		StringBuilder requestLoggerBuilder = new StringBuilder();
		requestLoggerBuilder.append("------>>>\n");

		String currentFormatterTime = LocalDateTime.now().format(requestLoggerTimeFormatter);
		requestLoggerBuilder.append(currentFormatterTime).append(".-").append("IP.").append(req.getRemoteHost())
				.append("\n").append(currentFormatterTime).append(".-")
				.append(SystemUtils.IS_OS_WINDOWS ? "地址." : "\033[35m地址\033[0m.").append(req.getRequestURI())
				.append("\n").append(currentFormatterTime).append(".-")
				.append(SystemUtils.IS_OS_WINDOWS ? "接口." : "\033[32m接口\033[0m.")
				.append(hm.getMethod().getDeclaringClass().getName()).append("!").append(hm.getMethod().getName())
				.append("\n").append(currentFormatterTime).append(".-")
				.append(SystemUtils.IS_OS_WINDOWS ? "页面." : "\033[36m页面\033[0m.")
				.append(Optional.ofNullable(view).orElse(emptyModel).getViewName()).append("\n")
				.append(currentFormatterTime).append(".-")
				.append(SystemUtils.IS_OS_WINDOWS ? "耗时." : "\033[31m耗时\033[0m.")
				.append((System.currentTimeMillis() - (Long) req.getAttribute("_timeMark")) / 1000d).append("s\n");
		return requestLoggerBuilder.toString();
	}

	private JSONObject logOperation(HttpServletRequest req, Object arg2) {
		boolean ignoreLog = true;
		JSONObject ol = new JSONObject();
		String url = (String) req.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
		if (!StringUtils.startsWith(url, "/")) {
			url = "/" + url;
		}
		ignoreLog = iniIgnoreLog(arg2, ignoreLog, url);
		if (ignoreLog)
			return ol;

		String domain = req.getRemoteHost();
		HttpSession httpSession = req.getSession(false);
		String connectIp = ServletUtils.getRemoteAddrAll(req);
		if (httpSession == null) {
			return ol;
		}
		ol.put("domain", domain);
		ol.put("ip", connectIp);
		ol.put("logTime", new Date());
		LoginInfo loginInfo = (LoginInfo) httpSession.getAttribute("loginInfo");
		if (loginInfo != null) {
			ol.put("unitId", loginInfo.getUnitId());
			ol.put("userId", loginInfo.getUserId());
			ol.put("ownerId", loginInfo.getOwnerId());
			ol.put("ownerType", loginInfo.getOwnerType());
		}

		ol.put("url", url);
		ol.put("clientVersion", "eis7|" + req.getHeader("user-agent"));

		Method m = ((HandlerMethod) arg2).getMethod();
		ControllerInfo info = m.getAnnotation(ControllerInfo.class);
		String classMethod = m.getDeclaringClass().getSimpleName() + "." + m.getName();
		String logStr = classMethod;
		if (info != null) {
			ol.put("operationName", info.operationName());
			logStr = dealLogStr(req, loginInfo, info);
			if(m.getName().contains("lockTheExamScore")){
				// 杭外锁定成绩
				String isLock = req.getParameter("isLock");
				if(Objects.equals(isLock,"0")){
					logStr += ",解锁";
				}else{
					logStr += ",锁定";
				}
			}
			String logParameter = info.parameter();
			logParameter = dealParameter(req, logParameter);
			ol.put("parameter", logParameter);
		}
		String unitInfo = "";

		if (loginInfo != null) {
			unitInfo = (loginInfo.getRealName() == null ? "" : "," + loginInfo.getRealName());
			if (StringUtils.isNotBlank(unitInfo)) {
				unitInfo += "," + (loginInfo.getUnitName() == null ? "" : "," + loginInfo.getUnitName());
			} else {
				unitInfo = (loginInfo.getUnitName() == null ? "" : "," + loginInfo.getUnitName());
			}
		}
		if (StringUtils.equals(logStr, classMethod)) {
			logStr = "";
		}
		ol.put("description", "(" + domain + "-" + classMethod + ")" + logStr + unitInfo);
		RedisUtils.lpush("operationLog", SUtils.s(ol));
		return ol;
	}

	private String dealParameter(HttpServletRequest req, String logParameter) {
		if (StringUtils.isNotBlank(logParameter) && StringUtils.contains(logParameter, "{")) {
			Map<String, Object> attrMap = (Map<String, Object>) req
					.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
			Matcher macherParameter = pattern.matcher(logParameter);
			int count = 10;
			while (macherParameter.find() && count > 0) {
				String value = StringUtils.substring(logParameter, macherParameter.start() + 1,
						macherParameter.end() - 1);
				Object o = attrMap.get(value);
				if (o == null) {
					value = req.getParameter(value);
				} else {
					value = o.toString();
				}
				logParameter = StringUtils.substring(logParameter, 0, macherParameter.start()) + value
						+ StringUtils.substring(logParameter, macherParameter.end());
				count--;
			}
		}
		return logParameter;
	}

	private String dealLogStr(HttpServletRequest req, LoginInfo loginInfo, ControllerInfo info) {
		String logStr = info.value();
		if (StringUtils.contains(logStr, "{")) {
			Map<String, Object> attrMap = (Map<String, Object>) req
					.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
			Matcher matcher = pattern.matcher(logStr);
			if (StringUtils.contains(logStr, "{_username}")) {
				logStr = StringUtils.replace(logStr, "{_username}", loginInfo.getUserName());
			}
			// 设置count，为了避免一些特殊的情况下，进入死循环
			int count = 10;
			while (matcher.find() && count > 0) {
				int start = matcher.start();
				int end = matcher.end();
				String value = StringUtils.substring(logStr, start + 1, end - 1);
				Object o = attrMap.get(value);
				if (o == null) {
					value = req.getParameter(value);
				} else {
					value = o.toString();
				}
				logStr = StringUtils.substring(logStr, 0, start) + value + StringUtils.substring(logStr, end);
				count--;
				matcher = pattern.matcher(logStr);
			}
		}
		return logStr;
	}

	private boolean iniIgnoreLog(Object arg2, boolean ignoreLog, String url) {
		if (arg2 instanceof HandlerMethod) {
			Method m = ((HandlerMethod) arg2).getMethod();
			ControllerInfo info = m.getAnnotation(ControllerInfo.class);
			if (info != null) {
				if (info.ignoreLog() == ControllerInfo.LOG_FORCE_WRITE) {
					ignoreLog = false;
				} else if (info.ignoreLog() == ControllerInfo.LOG_FORCE_DEFAULT) {
					ignoreLog = false;
				} else if (info.ignoreLog() == ControllerInfo.LOG_FORCE_IGNORE)
					ignoreLog = true;
			} else {
				ignoreLog = false;
			}
		}
		return ignoreLog;
	}

	private boolean checkIgnoreLog(String url, Method m, ControllerInfo info) {
		boolean ignoreLog;
		if (info != null && StringUtils.isNotBlank(info.operationName())) {
			ignoreLog = false;
		} else {
			ignoreLog = Pattern.compile(METHOD_NAME_FOR_WITHOUT_LOG_REGEX, Pattern.CASE_INSENSITIVE).matcher(url)
					.find();
			ignoreLog = ignoreLog
					|| !Pattern.compile(METHOD_NAME_FOR_LOG_REGEX, Pattern.CASE_INSENSITIVE).matcher(url).find();
			ignoreLog = ignoreLog || !Pattern.compile(METHOD_NAME_FOR_LOG_REGEX, Pattern.CASE_INSENSITIVE)
					.matcher(m.getName()).find();
		}
		return ignoreLog;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		if (Evn.isDevModel()) {
			long time = System.currentTimeMillis();
			req.setAttribute("_timeMark", time);
		}
		return true;
	}
}
