package net.zdsoft.szxy.operation.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 处理不用用户的数据权限
 *
 * @author shenke
 * @since 2019/3/30 下午3:36
 */
public class UserRegionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
          Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof String) {
            if (org.apache.commons.lang3.StringUtils.startsWith((String) principal, "anonymous")) {
                chain.doFilter(request, response);
                return;
            }
        }
        if (principal instanceof User) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (principal instanceof SecurityUser) {
                UserDataRegionHolder.setRegion(((SecurityUser) principal).getAuthRegions());
            }
            chain.doFilter(request, response);
        } finally {
            UserDataRegionHolder.remove();
        }
    }

    @Override
    public void destroy() {

    }
}
