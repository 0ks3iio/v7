package net.zdsoft.szxy.operation.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shenke
 * @since 2019/2/25 下午2:18
 */
public final class ServletUtils {

    /**
     * 是否是Ajax请求
     * @param request httpServletRequest
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxHeader);
    }
}
