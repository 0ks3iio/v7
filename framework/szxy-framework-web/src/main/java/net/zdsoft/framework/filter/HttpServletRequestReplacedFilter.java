package net.zdsoft.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.entity.BodyReaderHttpServletRequestWrapper;

public class HttpServletRequestReplacedFilter implements Filter {    
    @Override    
    public void destroy() {    
    
    }    
    
    @Override    
    public void doFilter(ServletRequest request, ServletResponse response,    
            FilterChain chain) throws IOException, ServletException {    
    
        ServletRequest requestWrapper = null;    
        if (request instanceof HttpServletRequest) {    
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;    
            if ("POST".equals(httpServletRequest.getMethod().toUpperCase())    
                    ) {    
                //&& httpServletRequest.getContentType().equalsIgnoreCase(    
           // "application/json")  
                requestWrapper = new BodyReaderHttpServletRequestWrapper(    
                        (HttpServletRequest) request);    
            }    
        }    
    
        if (requestWrapper == null) {    
            chain.doFilter(request, response);    
        } else {    
            chain.doFilter(requestWrapper, response);     
        }    
    }    
    
    @Override    
    public void init(FilterConfig arg0) throws ServletException {    
    
    }    
    
}  