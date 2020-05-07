package net.zdsoft.syncdata.custom.changzhi.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.changzhi.service.CzSyncService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class ChangZhiSyncDataJob implements Job {
	private Logger log = Logger.getLogger(ChangZhiSyncDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
        log.info("-----------长治基础数据的同步");
        CzSyncService czSyncService = (CzSyncService) Evn.getBean("czSyncService");
        try {
        	long time1 = System.currentTimeMillis();
//        	ysService.saveEdu();
        	czSyncService.saveEdu();
        	czSyncService.saveSchool();
        	czSyncService.saveSyncTeacher();
    		log.info("---------保存数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}


}
