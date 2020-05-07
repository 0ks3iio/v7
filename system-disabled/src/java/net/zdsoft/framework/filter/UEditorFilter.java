/*
 * @Project: v7
 * @Author shenke
 * @(#)UeFilter.java  Created on 2016/11/14
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 * @Date 2016/11/14 create by intellij idea and don't die in 28 years old
 */
package net.zdsoft.framework.filter;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.ueditor.action.UeditorController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 处理UE富文本编辑器和SpringMVC上传冲突问题
 * @date: 2016/11/14 16:48
 * create by intellij idea
 */
public class UEditorFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger("UEditorFilter");
    /** ue富文本编辑器上传图片url*/
    private static final String UE_EDITOR_URL = "static/ueditor/jsp/controller/controller.jsp";

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;

        String url = request.getRequestURI();
        if(StringUtils.contains(url,UE_EDITOR_URL)){
            UeditorController ueditorController = null;
            try {
                ueditorController = (UeditorController) Evn.getApplication().getBean("ueditorController");
                if (ueditorController == null) {
                    ueditorController = new UeditorController();
                }
            }catch (Exception e) {
                LOG.error("[处理UE富文本url编辑器URL]异常！！",e);
                ueditorController = new UeditorController();
            }
            String action = request.getParameter("action");
            ueditorController.execute(action, request);
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
