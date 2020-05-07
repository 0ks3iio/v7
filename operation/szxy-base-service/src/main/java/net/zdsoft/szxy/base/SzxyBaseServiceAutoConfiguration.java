package net.zdsoft.szxy.base;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author shenke
 * @since 2019/3/23 上午11:36
 */
@ComponentScan(
        basePackages = {
                "net.zdsoft.szxy.base.service",
                "net.zdsoft.szxy.base.dao"
        }
)
@ImportResource(locations = "classpath*:/dubbo-base-provider.xml")
@Configuration
public class SzxyBaseServiceAutoConfiguration {


}
