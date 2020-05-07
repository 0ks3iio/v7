package net.zdsoft.szxy.operation.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author shenke
 * @since 2019/3/30 下午4:07
 */
public class SpecialGrantedAuthority implements GrantedAuthority {

    private String authority;
    @Getter
    private String urlPrefix;

    public SpecialGrantedAuthority(String authority, String urlPrefix) {
        this.authority = authority;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof SpecialGrantedAuthority) {
            return authority.equals(((SpecialGrantedAuthority) obj).authority);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }
}
