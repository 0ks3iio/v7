package net.zdsoft.syncdata.custom.dongguan.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.dongguan.service.DongGuanSyncService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class DongGuanDataJob implements Job{
	private Logger log = Logger.getLogger(DongGuanDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
        log.info("-----------东莞机构数据同步");
        DongGuanSyncService dongGuanSyncService = (DongGuanSyncService) Evn.getBean("dongGuanSyncService");
        try {
        	long time1 = System.currentTimeMillis();
        	dongGuanSyncService.saveUnitAndAdmin();
    		log.info("---------更新数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}
}
