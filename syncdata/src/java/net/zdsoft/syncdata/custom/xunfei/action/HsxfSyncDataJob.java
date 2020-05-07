package net.zdsoft.syncdata.custom.xunfei.action;

import java.io.IOException;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.UrlUtils;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class HsxfSyncDataJob implements Job {
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("任务启动！");
        try {
            UrlUtils.readContent(Evn.getWebUrl() + "/syncdata/hsxf/index?force=true");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(">>>>任务结束！");
    }

}
