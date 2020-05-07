package net.zdsoft.framework.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.config.OpenApiSession;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.OpenApiLoginInfo;
import net.zdsoft.framework.utils.ServletUtils;

public class OpenApiHandlerInterceptor implements HandlerInterceptor {

	private static final Logger log = Logger.getLogger(OpenApiHandlerInterceptor.class);
	
	private String[] excludeUrls;
	private String[] excludePaths;
	private String[] includeUrls;

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse arg1, Object arg2, ModelAndView view)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		String connectIp = ServletUtils.getRemoteAddr(req);
		HttpSession httpSession = req.getSession();
		OpenApiSession session = null;
		if (httpSession != null) {
			String sessionId = httpSession.getId();
			session = OpenApiSession.get(sessionId);
			if (session != null)
				session.refresh();
			else {
				log.info("OpenApiSession 初始化");
				session = new OpenApiSession(sessionId);
			}
		}
		String uri = req.getRequestURI();
		boolean forceCheck = false;
		if (!forceCheck) {
			for (String s : excludePaths) {
				if (StringUtils.contains(uri, s)) {
					forceCheck = true;
					break;
				}
			}
		}
		if (!forceCheck) {
			for (String s : excludeUrls) {
				if (StringUtils.endsWith(uri, s)) {
					forceCheck = true;
					break;
				}
			}
		}
		if (forceCheck) {
			if (Evn.isDevModel()) {
				System.out.println();
				System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				System.out.println("访问ip："+connectIp);
				System.out.println("无需登录："+uri);
			}
			return true;
		}
		
		OpenApiLoginInfo loginInfo = session.getLoginInfo();
		if (loginInfo == null) {
			String loginUrl = Evn.getString(Constant.OPENAPI_LOGIN_URL);
			res.sendRedirect(loginUrl);
			return false;
		}
		
		return true;
	}

	public String[] getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public String[] getExcludePaths() {
		return excludePaths;
	}

	public void setExcludePaths(String[] excludePaths) {
		this.excludePaths = excludePaths;
	}

	public String[] getIncludeUrls() {
		return includeUrls;
	}

	public void setIncludeUrls(String[] includeUrls) {
		this.includeUrls = includeUrls;
	}

}
