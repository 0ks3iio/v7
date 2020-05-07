package net.zdsoft.dataimport.config;

import org.springframework.context.ApplicationContext;

/**
 * @author shenke
 * @since 17-8-13 下午3:08
 */
public class ImportApplicationContext {


    public ImportApplicationContext(ApplicationContext applicationContext) {
        ImportApplicationContext.applicationContext = applicationContext;
    }

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplication() {
        return applicationContext;
    }

    static void registerApplicationContext(ApplicationContext applicationContext) {
        ImportApplicationContext.applicationContext = applicationContext;
    }
}
