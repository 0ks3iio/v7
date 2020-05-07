package net.zdsoft.szxy.operation.security;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 在Freemarker渲染过程中获取当前的regionCodes的集合
 * @author shenke
 * @since 2019/4/12 上午9:58
 */
@Component
public final class UserDataRegionFreemarkerConfiguration {

    @Resource
    private Configuration configuration;

    @PostConstruct
    public void init() throws TemplateModelException {
        configuration.setSharedVariable("regionHolder", UserDataRegionHolder.holder);
    }
}
