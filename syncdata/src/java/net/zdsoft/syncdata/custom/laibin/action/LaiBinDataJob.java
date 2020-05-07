package net.zdsoft.syncdata.custom.laibin.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.laibin.service.LaiBinSyncService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class LaiBinDataJob implements Job{
	private Logger log = Logger.getLogger(LaiBinDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
        log.info("-----------来宾卡号数据同步");
        LaiBinSyncService laiBinSyncService = (LaiBinSyncService) Evn.getBean("laiBinSyncService");
        try {
        	long time1 = System.currentTimeMillis();
        	laiBinSyncService.updateStuCardNum();
    		log.info("---------更新数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}
}
