package net.zdsoft.eclasscard.data.scheduler;

import net.zdsoft.eclasscard.data.service.EccFaceLowerLogService;
import net.zdsoft.framework.config.Evn;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 每20分钟检测是否需要下发人脸的班牌，进行人脸下发
 * @author user
 *
 */
public class EccFaceModifyLowerJob implements Job{
	private static final Logger log = LoggerFactory.getLogger(EccFaceModifyLowerJob.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		EccFaceLowerLogService eccFaceLowerLogService = Evn.getBean("eccFaceLowerLogService");
		//1.上课考勤
		log.info("检测是否需要下发人脸的班牌开始");
		eccFaceLowerLogService.faceLowerCheckAll();
		log.info("检测是否需要下发人脸的班牌结束");
	}

}
