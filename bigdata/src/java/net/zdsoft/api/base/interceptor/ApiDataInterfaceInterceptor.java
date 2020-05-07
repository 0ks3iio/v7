package net.zdsoft.api.base.interceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.ServletUtils;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 所有的请求接口，需要先进行验证，再进行访问
 */
public class ApiDataInterfaceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String ticketKey = request.getParameter("ticketKey");
        if (StringUtils.isEmpty(ticketKey)) {
            ticketKey = request.getHeader("ticketKey");
            if (StringUtils.isEmpty(ticketKey)) {
                ServletUtils.print(response, "无效的ticketKey");
                return false;
            }
        }
        ApiDeveloperService bean = Evn.getBean("apiDeveloperService");
        ApiDeveloper developer = bean.findByTicketKey(ticketKey);
        if(developer==null){
        	ServletUtils.print(response, "您传递的参数ticketKey不存在，有需要请联系管理员。");
            return false;
        }
        String ips = developer.getIps();// 允许访问的IP
        if (!StringUtils.isEmpty(ips)) {
            // 当前访问的IP
            String ip = ServletUtils.getRemoteAddr(request);
            System.out.println("dataInterface access ip:" + ip);
            Set<String> ipsSet = new HashSet<String>();
            String[] ipArray = ips.split(",");
            ipsSet.addAll(Arrays.asList(ipArray));
            if (!ipsSet.contains(ip)) {
                ServletUtils.print(response, "您所在IP已被限制访问，不能读取数据，有需要请联系管理员。");
                return false;
            }
        }
        return true;
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
