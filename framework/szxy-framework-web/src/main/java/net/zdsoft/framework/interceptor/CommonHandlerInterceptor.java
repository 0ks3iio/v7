package net.zdsoft.framework.interceptor;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public class CommonHandlerInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory
			.getLogger(CommonHandlerInterceptor.class);

	private String ignore;

	private String auth;
	private String loginUrl;
	private String indexUrl;
	private String[] excludeUrls;
	private String[] excludePaths;
	private String[] includeUrls;
	private volatile RedisTemplate<String, String> redisTemplate;

	@Override
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object arg2, Exception arg3) {
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj, ModelAndView view) {
		initViewObject(req, view);
	}

	private void initViewObject(HttpServletRequest req, ModelAndView view) {
		if (view != null) {
			String showFramework2 = req.getParameter("showFramework");
			String desktopIndex = req.getParameter("desktopIndex");
			if (StringUtils.isNotBlank(showFramework2)) {
				view.addObject("showFramework", "true".equals(showFramework2));
			}
			if (StringUtils.isNotBlank(desktopIndex)) {
				view.addObject("desktopIndex", "true".equals(desktopIndex));
			}
		}
	}

	private static DateTimeFormatter requestLoggerTimeFormatter = DateTimeFormatter
			.ofPattern("H:m:s:S");
	private static ModelAndView emptyModel = new ModelAndView("");

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object arg2) throws Exception {

		//refresh session
		clusterSessionRefreshBean.refresh(req);

		HttpSession httpSession = req.getSession();
		LoginInfo loginInfo = (LoginInfo) httpSession.getAttribute("loginInfo");
		String uri = req.getRequestURI().trim();
		if (StringUtils.isNotBlank(uri)) {
			Object domainName = httpSession.getAttribute("domainName");
			String[] domainNames = uri.split(String
					.valueOf(UrlUtils.SEPARATOR_SIGN));
			if ((domainNames.length == 2 && loginInfo == null)
					|| domainName == null) {
				for (String name : domainNames) {
					if (StringUtils.isNotBlank(name)) {
						httpSession.setAttribute("domainName", name);
						break;
					}
				}
			}
		}

		String domainName = (String) httpSession.getAttribute("domainName");

		if (arg2 instanceof DefaultServletHttpRequestHandler
				&& loginInfo != null) {
			String s = StringUtils.replace(req.getRequestURI(),
					req.getContextPath(), "");
			Boolean isNormal = false;
			if (StringUtils.isBlank(s) || StringUtils.equals("/", s)) {
				isNormal = Boolean.TRUE;
			} else {
				s = StringUtils.replace(s, "/", "");
				if (s.equals(domainName)) {
					isNormal = Boolean.TRUE;
				}
			}
			if (isNormal) {
				res.getOutputStream().write(getGoHomeScript(req).getBytes());
				return false;
			}

			res.setHeader("Content-type", "text/html;charset=UTF-8");
			res.setCharacterEncoding("UTF-8");
			String openType = req.getParameter("openType");
			res.getWriter().write(getGo404PageScript(req, res, openType));
			return false;
		}

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
			return true;
		}
		if (loginInfo == null) {
			String eisLoginUrl = createHomepageUrl(req);
			if (arg2 instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) arg2;
				if (handlerMethod.hasMethodAnnotation(ResponseBody.class)) { // ajax方法
					PrintWriter printWriter = res.getWriter();
					res.setStatus(499);
					String url = req.getHeader("referer");
					if (org.springframework.util.StringUtils.isEmpty(url)) {
						url = eisLoginUrl;
					}
					printWriter.print(url);
				} else {
					res.getOutputStream()
							.write(getGoHomeScript(req).getBytes());
				}
			} else {
				res.getOutputStream().write(getGoHomeScript(req).getBytes());
			}
			return false;
		}
		try {
			RedisTemplate<String, String> cache = getRedisTemplate();
			if (cache != null && Evn.isPassport()) {
				String passportSessionId = (String) httpSession
						.getAttribute("passportSessionId");
				String copySessionId = cache.opsForValue().get(
						Constant.PASSPORT_TICKET_KEY + passportSessionId);
				if (StringUtils.isBlank(copySessionId)
						&& StringUtils.isNotBlank(passportSessionId)) {
					httpSession.invalidate();
					res.getWriter().write(getGoHomeScript(req));
					return false;
				}


			}
		} catch (Exception e) {
			httpSession.invalidate();
			return false;
		}
		return true;
	}

	private String createHomepageUrl(HttpServletRequest req) {
		String eisLoginUrl = UrlUtils.getPrefix(req);
		//生涯规划首页
		if(StringUtils.isNotBlank(req.getRequestURI()) && req.getRequestURI().indexOf("/careerPlanning")>0){
			eisLoginUrl+="/homepage/register/syghLogin/page";
		}else{
			eisLoginUrl +="/homepage/loginPage/page";
			if (!StringUtils.startsWithIgnoreCase(eisLoginUrl, "http")) {
				eisLoginUrl = UrlUtils.getPrefix(req)
						+ (StringUtils.startsWith(eisLoginUrl, "/") ? eisLoginUrl
								: ("/" + eisLoginUrl));
			}
		}
		return eisLoginUrl;
	}

	private RedisTemplate<String, String> getRedisTemplate() {
		if (redisTemplate == null) {
			redisTemplate = Evn.getBean("redisTemplate");
		}
		return redisTemplate;
	}

	/**
	 * 返回首页的脚本
	 *
	 * @return
	 */
	private String getGoHomeScript(HttpServletRequest req) {
		if (log.isDebugEnabled()) {
			log.debug("Show Evn map in getGoHomeScript");
			log.debug(SUtils.s(Evn.getMap()));
		}
		String eisLoginUrl = createHomepageUrl(req);

		//判断是否是大数据首页地址，如果是则最终请求passport之后应该重定向到大数据首页上去
		if (req.getRequestURI().contains("bigdata/v3")) {
			eisLoginUrl = UrlUtils.addQueryString(eisLoginUrl, "url", UrlUtils.getPrefix(req) + "/bigdata/v3");
		}
		else {
			//eisLoginUrl = UrlUtils.addQueryString(eisLoginUrl, "url", req.getRequestURL().toString());
		}
		return ("<script>top.location.href = \"" + eisLoginUrl + "\";</script>");
	}

	private String getGo404PageScript(HttpServletRequest req,
			HttpServletResponse res, String openType) {
		log.error("找不到地址：" + req.getRequestURI());
		String $404Html = null;
		try {
			String $404path;
			if (req.getRequestURI().indexOf("/bigdata/") >= 0) {
				$404path = Evn.getRequest().getRealPath(
						"/bigdata/v3/common/404.ftl");
			} else {
				if (req.getRequestURI().indexOf("/desktop/app/") >= 0
						|| req.getRequestURI().indexOf("/homepage/template/") >= 0) {
					$404path = Evn.getRequest().getRealPath(
							"/" + "desktop/functionArea404.ftl");
				} else {
					$404path = Evn.getRequest().getRealPath(
							"/" + "desktop/404.ftl");
				}
			}
			$404Html = FileUtils.readFileToString(new File($404path), "utf-8");
			if (StringUtils.isBlank($404Html)) {
				return getGoHomeScript(req);
			}
		} catch (IOException e) {
			return getGoHomeScript(req);
		}

		return $404Html.replaceAll("\\r\\n", "");
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getIndexUrl() {
		return indexUrl;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
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

	private PseudoClusterSessionRefreshBean clusterSessionRefreshBean;

	@Autowired
	public void setClusterSessionRefreshBean(PseudoClusterSessionRefreshBean clusterSessionRefreshBean) {
		this.clusterSessionRefreshBean = clusterSessionRefreshBean;
	}
}
