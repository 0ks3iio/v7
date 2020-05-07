package net.zdsoft.bigdata.datax.utils;

import com.jcraft.jsch.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.ExecJobConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangdongdong on 2019/4/29 14:01.
 */
@Component
@Lazy(false)
public class DataxUtil implements ApplicationContextAware {

    private static ExecutorService pool = Executors.newFixedThreadPool(100);

    private static ApplicationContext applicationContext = null;

    public static void execJob(ExecJobConfig execJobConfig) {
        pool.submit(new DataxRunnable(execJobConfig));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataxUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static String viewLog(ExecJobConfig execJobConfig) throws BigDataBusinessException {
        Session session = null;
        try {
            if (session == null || !session.isConnected()) {
                JSch jsch = new JSch();
                // Create and connect session.
                session = jsch.getSession(execJobConfig.getUsername(), execJobConfig.getHost(), execJobConfig.getPort());
                session.setPassword(execJobConfig.getPassword());
                //去掉连接确认的
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(30000);

                ChannelExec channel = (ChannelExec) session.openChannel("exec");

                StringBuilder command = new StringBuilder("cat ");
                command.append(execJobConfig.getJobLogPath() + "/" + execJobConfig.getJobInsLogId() + ".log")
                        .append("\n");
                channel.setCommand(command.toString());

                channel.setInputStream(null);
                BufferedReader input = new BufferedReader(new InputStreamReader(channel.getInputStream(), "UTF-8"));

                channel.connect();

                StringBuilder result = new StringBuilder();

                String line = "";
                while ((line = input.readLine()) != null) {
                    result.append(line + "\n");
                }

                input.close();
                channel.disconnect();
                session.disconnect();

                return result.toString();
            }
        } catch (JSchException e) {
            throw new BigDataBusinessException(e.getMessage());
        } catch (IOException e) {
            throw new BigDataBusinessException(e.getMessage());
        }
        return "";
    }

}
