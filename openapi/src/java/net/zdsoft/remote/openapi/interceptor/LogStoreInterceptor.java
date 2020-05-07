package net.zdsoft.remote.openapi.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Session;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;

/**
 * 日志
 * @author yangsj  2017年10月30日下午2:25:36
 * 进行openapi接口的统计
 */
public class LogStoreInterceptor implements HandlerInterceptor {

//	@Override
//	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception arg3)
//			throws Exception {
//		// 采集操作日志------------------
//				String domain = req.getRemoteHost();
//				HttpSession httpSession = req.getSession(false);
//				String connectIp = ServletUtils.getRemoteAddr(req);
//				Session session = null;
//				if (httpSession != null) {
//					String sessionId = httpSession.getId();
//					session = Session.get(sessionId);
//				}
//				if (session == null) {
//					return;
//				}
//				OperationLog ol = new OperationLog();
//				ol.setDomain(domain);
//				ol.setClientVersion("eis7");
//				ol.setIp(connectIp);
//				ol.setLogTime(new Date());
//				LoginInfo loginInfo = session.getLoginInfo();
//				if (loginInfo != null) {
//					ol.setUnitId(loginInfo.getUnitId());
//					ol.setUserId(loginInfo.getUserId());
//					ol.setOwnerId(loginInfo.getOwnerId());
//					ol.setOwnerType(loginInfo.getOwnerType());
//				}
//				String url = (String) req.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
//				if (!StringUtils.startsWith(url, "/")) {
//					url = "/" + url;
//				}
//
//				ol.setUrl(url);
//				ol.setClientVersion(req.getHeader("user-agent"));
//				long markTime = NumberUtils.toLong((String) req.getAttribute("_markTime"));
//				ol.setSpendTime(System.currentTimeMillis() - markTime);
//
//				if (arg2 instanceof HandlerMethod) {
//					Method m = ((HandlerMethod) arg2).getMethod();
//					ControllerInfo info = m.getAnnotation(ControllerInfo.class);
//					boolean goOut = false;
//					if (info != null) {
//						String logStr = info.value();
//						if (info.ignoreLog() == 1) {
//							goOut = true;
//						} else if (info.ignoreLog() == 0) {
//							if (StringUtils.containsIgnoreCase(logStr, "/show") || StringUtils.contains(logStr, "/find")
//									|| StringUtils.contains(logStr, "/query") || StringUtils.contains(logStr, "/fetch")
//									|| StringUtils.contains(logStr, "/get") || StringUtils.contains(logStr, "/list")
//									|| StringUtils.endsWith(logStr, "/page")) {
//								goOut = true;
//							}
//						}
//						if (!goOut) {
//							@SuppressWarnings("unchecked")
//							Map<String, Object> attrMap = (Map<String, Object>) req
//									.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
//
//							Pattern pattern = Pattern.compile("\\{\\w*\\}");
//							Matcher macher = pattern.matcher(logStr);
//
//							int num = 0;
//							while (macher.find()) {
//								String value = StringUtils.substring(logStr, macher.start() + 1 - num, macher.end() - 1- num);
//								String oldValue = value;
//								Object o = attrMap.get(value);
//								if (o == null) {
//									value = req.getParameter(value);
//								} else {
//									value = o.toString();
//								}
//								logStr = StringUtils.substring(logStr, 0, macher.start()- num) + value
//										+ StringUtils.substring(logStr, macher.end()- num);
//								num = num + 2 + (oldValue.length() - value.length());
//							}
//							ol.setDescription(logStr);
//
//							String logParameter = info.parameter();
//							if (StringUtils.isNotBlank(logParameter)) {
//								Matcher macherParameter = pattern.matcher(logParameter);
//								while (macherParameter.find()) {
//									String value = StringUtils.substring(logParameter, macherParameter.start() + 1,
//											macherParameter.end() - 1);
//									Object o = attrMap.get(value);
//									if (o == null) {
//									    value = value.equals("ticketKey")? req.getHeader(value) : req.getParameter(value);
//									} else {
//										value = o.toString();
//									}
//									logParameter = StringUtils.substring(logParameter, 0, macherParameter.start()) + value
//											+ StringUtils.substring(logParameter, macherParameter.end());
//								}
//								ol.setParameter(logParameter);
//							}
//							RedisUtils.lpush("operationLog", SUtils.s(ol));
//						}
//					}
//				}
//				// -----------操作日志采集结束
//
//				// 删除当前线程上下文的passportClient
////				PassportClientUtils.remove();
//		
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
//			throws Exception {
//		
//	}
//
//	@Override
//	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
//		return true;
//	}
}
