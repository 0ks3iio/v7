package net.zdsoft.szxy.operation.security;

import net.zdsoft.szxy.operation.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author shenke
 * @since 2019/2/25 下午1:09
 */
public class SzxyExceptionFilter extends GenericFilterBean {

    private Logger logger = LoggerFactory.getLogger(SzxyExceptionFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            if (ServletUtils.isAjaxRequest((HttpServletRequest) request) && !response.isCommitted()) {
                logger.error("未知的错误", e);
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(SzxyResponseStatusCode.SERVER_ERROR.getCode());
                res.setHeader("Content-type", "application/html");
                res.getOutputStream().write(e.getMessage().getBytes());
            } else {
                throw e;
            }
        }

    }
}
