package net.zdsoft.framework.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author shenke
 * @since 2019/5/22 下午2:09
 */
@Configuration
public class JavaMailSenderConfiguration {

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(sender);
        return sender;
    }

    /**
     * 登录用户名
     */
    private final static String ACCOUNT = "szxy@zdsoft.net";

    /**
     * 登录密码
     */
    private final static String PASSWORD = "zds6..JK";

    /**
     * 邮件服务器地址
     */
    private final static String HOST = "smtp.exmail.qq.com";

    /**
     * 发信端口
     */
    private final static Integer PORT = 587;


    private void applyProperties(JavaMailSenderImpl sender) {

        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(ACCOUNT);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("UTF-8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", Boolean.TRUE.toString());
        javaMailProperties.setProperty("mail.smtp.starttls.enable", Boolean.TRUE.toString());
        javaMailProperties.setProperty("mail.smtp.starttls.required", Boolean.TRUE.toString());

        sender.setJavaMailProperties(javaMailProperties);
    }

}