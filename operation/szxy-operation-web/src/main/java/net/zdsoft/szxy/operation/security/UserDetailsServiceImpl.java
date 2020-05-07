package net.zdsoft.szxy.operation.security;

import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.permission.dto.OperateAuth;
import net.zdsoft.szxy.operation.inner.permission.service.InnerPermissionService;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/1/11 下午1:44
 */
@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private OpUserService opUserService;
    @Resource
    private InnerPermissionService innerPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<OpUser> optionalOpUser = opUserService.getOpUserByUsername(username);
        OpUser user = optionalOpUser.orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));
        Set<OperateAuth> operateAuths = innerPermissionService.getOperateByUserId(user.getId());
        Set<GrantedAuthority> authorities = operateAuths.stream().map(e -> new SpecialGrantedAuthority(e.getOperateCode(), e.getUrlPrefix()))
                .collect(Collectors.toSet());
        SpecialGrantedAuthority authority = new SpecialGrantedAuthority(SecurityUser.LOGINED, "/");
        authorities.add(authority);

        Set<String> regionCodes = innerPermissionService.getAuthRegionCodes(user.getId());
        return new OpSecurityUser(user, authorities, regionCodes);
    }

    static class OpSecurityUser implements SecurityUser, UserDetails {

        private OpUser opUser;
        private Collection<GrantedAuthority> authorities;
        private Set<String> regionCodes;

        OpSecurityUser(OpUser opUser, Collection<GrantedAuthority> authorities, Set<String> regionCodes) {
            this.opUser = opUser;
            this.authorities = authorities;
            this.regionCodes = regionCodes;
        }

        @Override
        public String getId() {
            return opUser.getId();
        }

        @Override
        public String getPhone() {
            return opUser.getPhone();
        }

        @Override
        public String getRealName() {
            return opUser.getRealName();
        }

        @Override
        public String getEmail() {
            return opUser.getEmail();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return opUser.getPassword();
        }

        @Override
        public String getUsername() {
            return opUser.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public Integer getSex() {
            return opUser.getSex();
        }

        @Override
        public Set<String> getAuthRegions() {
            return this.regionCodes;
        }
    }
}
