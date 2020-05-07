package net.zdsoft.cache.admin;

import net.zdsoft.cache.admin.utils.TomcatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shenke
 * @since 2017.07.11
 */
public class BindRedisInterceptor extends RedisApplication implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String redisHost = request.getParameter(REQUEST_PARAMETER_REDIS_HOST);
        String redisPort = request.getParameter(REQUEST_PARAMETER_REDIS_PORT);
        String dbIndex   = request.getParameter(REQUEST_PARAMETER_REDIS_DB_INDEX);
        if ( StringUtils.isNotBlank(redisHost) || StringUtils.isNotBlank(redisPort) ) {
            bindRedis(redisHost + DEFAULT_SEPARATOR + redisPort);
        }
        if ( StringUtils.isNotBlank(dbIndex) ) {
            bindDBIndex(NumberUtils.toInt(dbIndex,0));
        }
        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if ( modelAndView != null ) {
            ModelMap modelMap = modelAndView.getModelMap();
            modelMap.put(REQUEST_PARAMETER_REDIS_HOST,request.getParameter(REQUEST_PARAMETER_REDIS_HOST));
            modelMap.put(REQUEST_PARAMETER_REDIS_PORT,request.getParameter(REQUEST_PARAMETER_REDIS_PORT));
            modelMap.put(REQUEST_PARAMETER_REDIS_DB_INDEX,request.getParameter(REQUEST_PARAMETER_REDIS_DB_INDEX));
            modelMap.put(REQUEST_PARAMETER_REDIS_DATA_TYPE,request.getParameter(REQUEST_PARAMETER_REDIS_DATA_TYPE));
            String uriEncoding = TomcatUtils.getURIEncoding(request.getServerPort());
            String key = request.getParameter(REQUEST_PARAMETER_REDIS_KEY);
            if ( key != null && StringUtils.isNotBlank(uriEncoding) && !"utf-8".equalsIgnoreCase(uriEncoding) ) {
                try {
                    key = new String(key.getBytes(uriEncoding),"utf-8");
                } catch (Exception e){
                    //log.error("转码失败");
                }
            }
            modelMap.put(REQUEST_PARAMETER_REDIS_KEY, key);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //remove
        unbindRedis();
        unbindDBIndex();
    }
}
