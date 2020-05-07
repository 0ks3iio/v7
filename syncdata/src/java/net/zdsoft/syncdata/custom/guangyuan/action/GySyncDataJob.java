package net.zdsoft.syncdata.custom.guangyuan.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.guangyuan.constant.GyBaseConstant;
import net.zdsoft.syncdata.custom.guangyuan.service.GySyncService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class GySyncDataJob implements Job{
	private Logger log = Logger.getLogger(GySyncDataJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
        log.info("-----------数据同步到苍溪和朝天平台");
        GySyncService gySyncService = (GySyncService) Evn.getBean("gySyncService");
        try {
        	long time1 = System.currentTimeMillis();
        	//推送苍溪的数据
//        	gySyncService.pushUnit(GyBaseConstant.GYCX_PLATFORM_URL);
        	gySyncService.pushUser(GyBaseConstant.GYCX_PLATFORM_URL);
        	//推送朝天的数据
//        	gySyncService.pushUnit(GyBaseConstant.GYCT_PLATFORM_URL);
//        	gySyncService.pushUser(GyBaseConstant.GYCT_PLATFORM_URL);
//        	gySyncService.saveEdu(GyBaseConstant.GYCX_PLATFORM_URL);
//        	gySyncService.saveSchool(GyBaseConstant.GYCX_PLATFORM_URL);
//        	gySyncService.saveTeacher(GyBaseConstant.GYCX_PLATFORM_URL);
//        	gySyncService.saveEdu(GyBaseConstant.GYCT_PLATFORM_URL);
//        	gySyncService.saveSchool(GyBaseConstant.GYCT_PLATFORM_URL);
//        	gySyncService.saveTeacher(GyBaseConstant.GYCT_PLATFORM_URL);
//        	gySyncService.saveUserToPassport();
    		log.info("---------推送数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}

}
