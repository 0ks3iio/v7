package net.zdsoft.eclasscard.data.scheduler;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.eclasscard.data.service.EccInOutAttanceService;
import net.zdsoft.eclasscard.data.service.EccUserFaceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EccAddAttPeriodEverydayJob implements Job{
	private static final Logger log = LoggerFactory.getLogger(EccAddAttPeriodEverydayJob.class);
	public static final String SCHEDULER_EVERY_DAY = "scheduler-every-day";
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		EccClassAttenceService eccClassAttenceService = Evn.getBean("eccClassAttenceService");
		EccDormAttenceService eccDormAttenceService = Evn.getBean("eccDormAttenceService");
		EccUserFaceService eccUserFaceService = Evn.getBean("eccUserFaceService");
		EccInOutAttanceService eccInOutAttanceService = Evn.getBean("eccInOutAttanceService");
		if (RedisUtils.get(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+SCHEDULER_EVERY_DAY) == null) {
			RedisUtils.set(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+SCHEDULER_EVERY_DAY, EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+SCHEDULER_EVERY_DAY, 10*60);
			//1.上课考勤
			log.info("加入上课考勤队列开始");
			eccClassAttenceService.addClassAttenceQueue(true);
			log.info("加入上课考勤队列结束");
			//2.寝室考勤
			log.info("加入寝室考勤队列开始");
			eccDormAttenceService.addDormAttenceQueue();
			log.info("加入寝室考勤队列结束");
			//3.上下学考勤
			log.info("加入上下学时间开始");
			eccInOutAttanceService.addInOutAttenceQueue(null);
			log.info("加入上下学时间结束");
			//4.每日清除非正常状态的学生的人脸照片
			log.info("非在校生人脸清除开始");
			eccUserFaceService.deleteStuNotInSchool();
			log.info("非在校生人脸清除结束");
		}
	}

}
