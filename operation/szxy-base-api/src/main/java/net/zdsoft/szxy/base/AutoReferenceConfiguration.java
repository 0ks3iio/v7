package net.zdsoft.szxy.base;

import net.zdsoft.szxy.dubbo.autoreference.AutoReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author shenke
 * @since 2019/3/18 下午5:34
 */
@Configuration
@AutoReference(path = "classpath*:/dubbo/dubbo-base.xml")
@ImportResource(locations = "classpath*:/dubbo/dubbo-base.xml")
public class AutoReferenceConfiguration {

}