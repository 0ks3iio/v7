package net.zdsoft.szxy.operation.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * @author shenke
 * @since 2019/4/4 上午11:06
 */
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
public class FixedGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    protected ApplicationContext context;
    private final String ROLE_PREFIX = "ROLE_";

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();
        accessDecisionManager.getDecisionVoters().stream()
                .filter(RoleVoter.class::isInstance)
                .map(RoleVoter.class::cast)
                .forEach(it -> it.setRolePrefix(getRolePrefix()));
        return accessDecisionManager;
    }

    private String getRolePrefix() {
        String[] grantedAuthorityDefaultsBeanNames = context.getBeanNamesForType(GrantedAuthorityDefaults.class);
        if(grantedAuthorityDefaultsBeanNames.length == 1) {
            GrantedAuthorityDefaults grantedAuthorityDefaults = context.getBean(grantedAuthorityDefaultsBeanNames[0], GrantedAuthorityDefaults.class);
            return grantedAuthorityDefaults.getRolePrefix();
        }
        return ROLE_PREFIX;
    }

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext context) {
        super.setApplicationContext(context);
        this.context = context;
    }
}
