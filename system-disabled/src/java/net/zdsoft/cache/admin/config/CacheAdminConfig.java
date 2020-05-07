package net.zdsoft.cache.admin.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

/**
 * @author shenke
 * @since 2017.07.11
 */
@Configuration
//@ComponentScan(basePackages = {"net.zdsoft.cache.admin","net.zdsoft.cache.admin.service"})
@Import({WebConfig.class})
//@PropertySources({@PropertySource("classpath:/conf/redis.properties")})
public class CacheAdminConfig {

    @Bean
    protected PropertiesFactoryBean propertiesFactoryBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        try {
            propertiesFactoryBean.setLocation(new ClassPathResource("/conf/redis.properties"));
            propertiesFactoryBean.afterPropertiesSet();
        } catch (Exception e){
            //
        }
        return propertiesFactoryBean;
    }
}
