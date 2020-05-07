package net.zdsoft.szxy.operation.security;

import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author shenke
 * @since 2019/1/11 下午4:49
 */
@Component
public class OpPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return PasswordUtils.encodeIfNot(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordUtils.decode(encodedPassword).equals(rawPassword.toString());
    }
}
