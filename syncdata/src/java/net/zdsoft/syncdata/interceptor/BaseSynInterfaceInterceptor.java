package net.zdsoft.syncdata.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yangsj  2018年7月30日下午3:33:17
 */
public class BaseSynInterfaceInterceptor implements HandlerInterceptor {

	private static final Logger log = Logger.getLogger(BaseSynInterfaceInterceptor.class);
	private String[] excludeUrls = {"/sync/remote/openapi/eduUnit","/sync/remote/openapi/school",
			"/sync/remote/openapi/teacher","/sync/remote/openapi/updateUserPwd","/sync/remote/openapi/user"};
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		SystemIniRemoteService systemIniRemoteService = Evn.getBean("systemIniRemoteService");
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		if (StringUtils.isNotBlank(Evn.getString(BaseSaveConstant.BASE_SYN_OPEN_KEY))
    			&& "true".equalsIgnoreCase(Evn.getString(BaseSaveConstant.BASE_SYN_OPEN_KEY))) {
			boolean forceCheck = false;
			if(StringUtils.isNotBlank(deployRegion) && deployRegion.equals("DongGuan")) {
				String uri = request.getRequestURI();
				if (!forceCheck) {
					for (String s : excludeUrls) {
						if (StringUtils.contains(uri, s)) {
							forceCheck = true;
							break;
						}
					}
				}
				if(forceCheck) {
					return true;
				}
			}else {
				return true;
			}
    	}
		ServletUtils.print(response, "基础数据同步的接口配置没有开启");
		return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }
}
