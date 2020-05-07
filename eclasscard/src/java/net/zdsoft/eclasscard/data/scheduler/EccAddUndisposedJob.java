package net.zdsoft.eclasscard.data.scheduler;

import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.eclasscard.data.service.EccExamTimeService;
import net.zdsoft.eclasscard.data.service.EccFullObjAllService;
import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.eclasscard.data.service.EccHonorService;
import net.zdsoft.eclasscard.data.service.EccInOutAttanceService;
import net.zdsoft.framework.config.Evn;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EccAddUndisposedJob implements Job{
	private static final Logger log = LoggerFactory.getLogger(EccAddUndisposedJob.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		EccClassAttenceService eccClassAttenceService = Evn.getBean("eccClassAttenceService");
		EccDormAttenceService eccDormAttenceService = Evn.getBean("eccDormAttenceService");
		EccBulletinService eccBulletinService = Evn.getBean("eccBulletinService");
		EccHonorService eccHonorService = Evn.getBean("eccHonorService");
		EccExamTimeService eccExamTimeService = Evn.getBean("eccExamTimeService");
		EccFullObjService eccFullObjService = Evn.getBean("eccFullObjService");
		EccFullObjAllService eccFullObjAllService = Evn.getBean("eccFullObjAllService");
		EccInOutAttanceService eccInOutAttanceService = Evn.getBean("eccInOutAttanceService");
		//1.上课考勤
		log.info("加入上课考勤队列开始");
		try {
			eccClassAttenceService.addClassAttenceQueue(false);
			log.info("加入上课考勤队列结束");
		}catch (Exception e) {
			log.error("加入上课考勤队列失败"+e.getMessage());
		}
		
		//2.寝室考勤
		log.info("加入寝室考勤队列");
		try {
			eccDormAttenceService.addDormAttenceQueue();
			log.info("加入寝室考勤队列结束");
		}catch (Exception e) {
			log.error("加入寝室考勤队列失败"+e.getMessage());
		}
		
		//3.通知公告
		log.info("加入未展示的通知公告队列开始");
		try {
			eccBulletinService.addBulletinQueue();
			log.info("加入未展示的通知公告队列结束");
		}catch (Exception e) {
			log.error("加入未展示的通知公告队列失败"+e.getMessage());
		}
		//4.荣誉
		log.info("加入未展示的荣誉队列开始");
		try {
			eccHonorService.addHonorQueue();
			log.info("加入未展示的荣誉队列结束");
		}catch (Exception e) {
			log.error("加入未展示的荣誉队列失败"+e.getMessage());
		}
		
		//5.考试时间
		log.info("加入未展示的考试时间队列开始");
		try {
			eccExamTimeService.addExamTimeQueue();
			log.info("加入未展示的考试时间队列结束");
		}catch (Exception e) {
			log.info("加入未展示的考试时间队列失败"+e.getMessage());
		}
		
		//6.全校全屏展示内容
		log.info("加入未展示的全校全屏内容队列开始");
		try {
			eccFullObjAllService.addFullObjAllQueue();
			log.info("加入未展示的全校全屏内容队列结束");
		}catch (Exception e) {
			log.info("加入未展示的全校全屏内容队列失败"+e.getMessage());
		}
		
		//7.单个班牌全屏展示内容
		log.info("加入未展示的单个班牌全屏内容队列开始");
		try {
			eccFullObjService.addFullObjQueue();
			log.info("加入未展示的单个班牌全屏内容队列结束");
		}catch (Exception e) {
			log.info("加入未展示的单个班牌全屏内容队列失败"+e.getMessage());
		}
		
		//8.上下学考勤
		log.info("加入上下学时间开始");
		try {
			eccInOutAttanceService.addInOutAttenceQueue(null);
			log.info("加入上下学时间结束");
		}catch (Exception e) {
			log.info("加入上下学时间失败"+e.getMessage());
		}
	}

}
