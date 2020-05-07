package net.zdsoft.api.base.config;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ValidatorConfig {
    @Bean("messageSource")
    public ResourceBundleMessageSource getMessageSource() {
    	ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
    	resource.setBasename("exceptions");
        return resource;
    }

    @Bean
    public MethodInvokingFactoryBean validatorFactory(){
    	MethodInvokingFactoryBean validatorFactory = new MethodInvokingFactoryBean();
    	validatorFactory.setArguments(getMessageSource());
    	validatorFactory.setStaticMethod("net.zdsoft.api.base.utils.CheckUtil.setResources");
        return validatorFactory;
    }

}
