package net.zdsoft.api.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.api.openapi.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.framework.utils.UrlUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class GlobalInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(GlobalInterceptor.class);
    private String[] whiteList;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
    	HttpSession httpSession = request.getSession();
        String requestURI = request.getRequestURI();

        String loginUrl = UrlUtils.getPrefix(request) + OpenApiConstants.BG_API_LOGIN_URL; 
        if ("/".equals(requestURI)) {
            response.sendRedirect(loginUrl);
            return false;
        }
        if (null == request.getSession().getAttribute(OpenApiConstants.DEVELOPER_SESSION)) {
//            if (null == request.getHeader("X-Requested-With")) {
//                response.sendRedirect(loginUrl);
//            } else {
//                ServletUtils.print(response,
//                        Json.toJSONString(new ResultDto().setSuccess(false).setCode("-2").setMsg(loginUrl)));
//            }
            httpSession.invalidate();
            response.getWriter().write(getGoHomeScript(request));
            return false;
        }
        return true;
    }
    
    /**
	 * 返回首页的脚本
	 *
	 * @return
	 */
	private String getGoHomeScript(HttpServletRequest req) {
		if (log.isDebugEnabled()) {
			log.debug("Show Evn map in getGoHomeScript");	
		}
		String apiLoginUrl = createHomepageUrl(req);
		return ("<script>top.location.href = \"" + apiLoginUrl + "\";</script>");
	}
	
	
    private String createHomepageUrl(HttpServletRequest req) {
		String loginUrl = UrlUtils.getPrefix(req) + OpenApiConstants.BG_API_LOGIN_URL;
		if (!StringUtils.startsWithIgnoreCase(loginUrl, "http")) {
			loginUrl = UrlUtils.getPrefix(req)
					+ (StringUtils.startsWith(loginUrl, "/") ? loginUrl
							: ("/" + loginUrl));
		}
		return loginUrl;
	}

    @Override
    public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj,
                           ModelAndView modelandview) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception exception)
            throws Exception {

    }

    public String[] getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String[] whiteList) {
        this.whiteList = whiteList;
    }

}
