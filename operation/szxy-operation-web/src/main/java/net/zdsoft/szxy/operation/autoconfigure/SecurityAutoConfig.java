package net.zdsoft.szxy.operation.autoconfigure;

import net.zdsoft.szxy.operation.security.OpAccessDeniedHandler;
import net.zdsoft.szxy.operation.security.OpPasswordEncoder;
import net.zdsoft.szxy.operation.security.SzxyExceptionFilter;
import net.zdsoft.szxy.operation.security.SzxyLoginUrlRedirectHandler;
import net.zdsoft.szxy.operation.security.UserRegionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2019/1/11 下午1:42
 */
@Configuration
public class SecurityAutoConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(daoAuthenticationProvider).csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/operation/login")
                .loginProcessingUrl("/operation/doLogin")
                .defaultSuccessUrl("/")
                .failureForwardUrl("/operation/login")
                .permitAll()
                .and()
                .logout().logoutUrl("/operation/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/")
                .and().headers().frameOptions().sameOrigin()
                .and().addFilterBefore(new SzxyExceptionFilter(), ExceptionTranslationFilter.class)
                .addFilterBefore(new UserRegionFilter(), ExceptionTranslationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new SzxyLoginUrlRedirectHandler("/operation/login"));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               OpPasswordEncoder opPasswordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setHideUserNotFoundExceptions(true);
        daoAuthenticationProvider.setPasswordEncoder(opPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }


    @Configuration
    public static class SecurityUserWebMvcAutoConfig implements WebMvcConfigurer {

        @Resource
        private SecurityUserAutoResolver securityUserAutoResolver;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(securityUserAutoResolver);
        }
    }

    @Bean
    public OpAccessDeniedHandler accessDeniedHandler() {
        return new OpAccessDeniedHandler();
    }

    @Bean
    public ExceptionTranslationFilter exceptionTranslationFilter(LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint,
                                                                 OpAccessDeniedHandler opAccessDeniedHandler) {

        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(loginUrlAuthenticationEntryPoint);
        exceptionTranslationFilter.setAccessDeniedHandler(opAccessDeniedHandler);
        return exceptionTranslationFilter;
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/operation/login");
    }

    @Bean
    public GrantedAuthorityDefaults rolePrefix() {
        return new GrantedAuthorityDefaults("");
    }
}
