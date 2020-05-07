package net.zdsoft.framework.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.google.common.collect.Lists;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import net.zdsoft.framework.utils.FreemarkerUtils;

/**
 * Freemarker的全局参数放到这里
 * 
 * @author linqz
 */
public class EisFreeMarkerConfigurer extends FreeMarkerConfigurer {

	private static Logger log = Logger.getLogger(EisFreeMarkerConfigurer.class);
    private List<FreemarkerConfigureEx> freemarkerConfigureExList = Lists.newArrayList();
    @Override
    public void setFreemarkerVariables(Map<String, Object> variables) {
        Map<String, String> map = Evn.getMap();
        for (String key : map.keySet()) {
            variables.put(key, map.get(key));
        }
        try {
            variables.put("frameworkEnv", Evn.class.newInstance());
            variables.put("stack",EisStaticModelStack.class.newInstance());
        }
        catch (InstantiationException e) {
        	log.error(e);
        }
        catch (IllegalAccessException eq) {
        	log.error(eq);
        }
        super.setFreemarkerVariables(variables);
    }

    // 复写该方法，防止 lazy-init
    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        Map<String,FreemarkerConfigureEx> exMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(EisContextLoaderListener.getCurrentWebApplicationContext(),FreemarkerConfigureEx.class,true,true);
        if (exMap != null) {
            List<FreemarkerConfigureEx> exList = Lists.newArrayList(exMap.values());
            refreshSharedVariables(exList,getConfiguration());
        }
    }

    public void refreshSharedVariables(List<FreemarkerConfigureEx> freemarkerConfigureExes,Configuration configuration) {
        try {
            if (CollectionUtils.isNotEmpty(freemarkerConfigureExes) ) {
                for (FreemarkerConfigureEx configureEx : freemarkerConfigureExes) {
                    Map<String,Object> exMap = null;
                    if ((exMap = configureEx.getExtendFreemarkerVariables(configuration)) != null) {
                        configuration.setAllSharedVariables(FreemarkerUtils.convertToSimpleHash(exMap));
                    }
                }
            }
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
    }

    public void addConfigureEx(FreemarkerConfigureEx freemarkerConfigureEx) {
        if (freemarkerConfigureEx != null) {
            this.freemarkerConfigureExList.add(freemarkerConfigureEx);
        }
    }
}
