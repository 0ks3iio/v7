package net.zdsoft.szxy.base.app;

import net.zdsoft.szxy.monitor.RecordJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenke
 * @since 2019/3/23 上午11:46
 */
@SpringBootApplication(
        scanBasePackages =
                {"net.zdsoft.szxy.base.service"}
)
@EnableJpaRepositories(
        basePackages = {
                "net.zdsoft.szxy.base.**.dao"
        },
        repositoryFactoryBeanClass = RecordJpaRepositoryFactoryBean.class
)
@EntityScan(
        basePackages = {
                "net.zdsoft.szxy.base.**.entity.**"
        }
)
@ImportResource(locations = "classpath*:/dubbo-provider.xml")
public class SzxyBaseApp {

    @Bean
    public CountDownLatch countDownLatch() {
        return new CountDownLatch(2);
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(SzxyBaseApp.class, args);

        context.getBean(CountDownLatch.class).await();
    }
}
