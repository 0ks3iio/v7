package net.zdsoft.framework.config;

import freemarker.template.Configuration;

import java.util.Map;

/**
 * Created by shenke on 2017/4/17.
 */
public abstract class FreemarkerConfigureEx {

    public abstract Map<String, Object> getExtendFreemarkerVariables(Configuration freemarkerConfiguration);

}
