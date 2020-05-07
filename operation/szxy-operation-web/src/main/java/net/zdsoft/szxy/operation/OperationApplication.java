package net.zdsoft.szxy.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author shenke
 * @since 2019/1/9 下午3:03
 */
@SpringBootApplication
@EnableJpaRepositories(
        basePackages = {
                "net.zdsoft.szxy.operation.**.dao",
                //"net.zdsoft.szxy.base.**.dao"
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
@ImportResource(locations = "classpath*:/dubbo-consumer.xml")
public class OperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class, args);
    }
}
