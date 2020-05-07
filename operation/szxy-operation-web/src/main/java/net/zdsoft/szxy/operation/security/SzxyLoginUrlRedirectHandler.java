package net.zdsoft.szxy.operation.security;

import net.zdsoft.szxy.operation.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author shenke
 * @since 2019/2/25 下午5:43
 */
public class SzxyLoginUrlRedirectHandler extends LoginUrlAuthenticationEntryPoint {

    public SzxyLoginUrlRedirectHandler(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if (ServletUtils.isAjaxRequest(request)) {
            String redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);
            response.setStatus(SzxyResponseStatusCode.NO_LOGIN.getCode());
            response.setHeader("Content-type", "application/html");
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(redirectUrl.getBytes(StandardCharsets.ISO_8859_1));
            outputStream.flush();
        } else {
            super.commence(request, response, authException);
        }
    }
}
