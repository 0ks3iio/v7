package net.zdsoft.syncdata.custom.gansu.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.gansu.service.GanSuUserService;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class GanSuSyncDataJob implements  Job{
	private Logger log = LoggerFactory.getLogger(GanSuSyncDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
			log.info("任务启动！");
        log.info("-----------甘肃用户删除数据的同步");
        GanSuUserService gsUserService = (GanSuUserService) Evn.getBean("ganSuUserService");
        try {
        	long time1 = System.currentTimeMillis();
        	gsUserService.deleteUser();
    		log.info("---------保存数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}

}
