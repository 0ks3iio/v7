package net.zdsoft.szxy.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author shenke
 * @since 2019/3/19 下午3:03
 */
@SpringBootApplication(
        scanBasePackages = "net.zdsoft.szxy.base"
)
@EnableJpaRepositories(
        basePackages = {
                "net.zdsoft.szxy.base.**.dao"
        }
)
@EntityScan(
        basePackages = {
                "net.zdsoft.szxy.operation.**.entity",
                "net.zdsoft.basedata.**.entity",
                "net.zdsoft.system.**.entity.**",
                "net.zdsoft.szxy.base.**.entity.**"
        }
)
public class BaseTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseTestApplication.class, args);
    }
}
