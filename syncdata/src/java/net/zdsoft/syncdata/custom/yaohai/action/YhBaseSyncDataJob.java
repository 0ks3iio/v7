package net.zdsoft.syncdata.custom.yaohai.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.yaohai.service.YhSyncService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class YhBaseSyncDataJob implements Job{

	private Logger log = Logger.getLogger(YhBaseSyncDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
		log.info("-----------瑶海数据同步");
		YhSyncService yhSyncService = (YhSyncService) Evn.getBean("yhSyncService");
	        try {
	        	//小初高
	        	long time1 = System.currentTimeMillis();
	        	yhSyncService.saveSchool();
	        	yhSyncService.saveTeacher();
//	        	yhSyncService.saveGrade();
	        	yhSyncService.saveClass();
	        	yhSyncService.saveStudent();
	        	yhSyncService.saveUser();
//	        	yhSyncService.saveCourse();
//	        	yhSyncService.saveClassTeaching();
	    		log.info("---------拉取数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        log.info(">>>>任务结束！");
	}
}
