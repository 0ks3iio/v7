package net.zdsoft.framework.xss;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.utils.FilterParamUtils;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/4/29 上午10:54
 */
public class XssFilter implements Filter {
	
	private  String[] notEscapeURI;
	private  String[] notEscapeParam;
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	notEscapeURI = FilterParamUtils.getParamValues(filterConfig,
				"notEscapeURI");
    	notEscapeParam = FilterParamUtils.getParamValues(filterConfig,
    			"notEscapeParam");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	boolean forceCheck = false;
    	HttpServletRequest req = (HttpServletRequest) servletRequest;
    	String uri = req.getRequestURI().trim();
    	for(String s:notEscapeURI){
    		if (StringUtils.contains(uri, s)) {
    			forceCheck = true;
    			break;
    		}
    	}
    	if(forceCheck){
    		filterChain.doFilter(servletRequest, servletResponse);
    	}else{
    		filterChain.doFilter(new XssHttpServletRequest(req,notEscapeParam), servletResponse);
    	}
    }

    @Override
    public void destroy() {

    }
}
