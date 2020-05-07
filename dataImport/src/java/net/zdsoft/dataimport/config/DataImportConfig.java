package net.zdsoft.dataimport.config;

import net.zdsoft.dataimport.freemarker.ViewDataFreemarkerUtils;
import net.zdsoft.framework.config.FreemarkerConfigureEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @see
 * @author shenke
 * @since 2017.08.10
 */
@Configuration
@PropertySource("classpath:/conf/redis.properties")
@ComponentScan(basePackages = {"net.zdsoft.dataimport","net.zdsoft.uimport"})
public class DataImportConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerApplication() {
        ImportApplicationContext.registerApplicationContext(applicationContext);
    }

    //此处不可直接注入freemarkerConfigurer  org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer#get
    @Bean(name = "importFreemarkerEx")
    public ImportFreemarkerEx registerEx() {
        return new ImportFreemarkerEx();
    }

    public static class ImportFreemarkerEx extends FreemarkerConfigureEx {

        @Override
        public Map<String, Object> getExtendFreemarkerVariables(freemarker.template.Configuration freemarkerConfiguration) {
            return new HashMap<String, Object>(){
                {
                    put("ViewDataFreemarkerUtils", new ViewDataFreemarkerUtils());
                }
            };
        }
    }
}
