package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.log4j.Logger;

public class ClassAttanceTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(ClassAttanceTask.class);
	private Integer sectionNumber;
	private String section;//学段
	
	public ClassAttanceTask(String unitId,Integer sectionNumber,String section,boolean isEnd) {
		this.sectionNumber = sectionNumber;
		this.isEnd = isEnd;
		this.bizId = unitId;
		this.section = section;
	}
	
	@Override
	public void run() {
		EccClassAttenceService eccClassAttenceService = Evn.getBean("eccClassAttenceService");
		if(RedisUtils.hasLocked(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+bizId+"."+sectionNumber+"."+section+"."+isEnd)){//并发加锁
			try{
				logger.info("单位："+bizId+",上课考勤任务执行");
				eccClassAttenceService.classTaskRun(bizId, sectionNumber,section, isEnd);
				if(isEnd){
					logger.info("单位："+bizId+",第"+sectionNumber+"节课考勤结束");
				}else{
					logger.info("单位："+bizId+",第"+sectionNumber+"节课考勤开始");
				}
			}finally{
				RedisUtils.unLock(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+bizId+"."+sectionNumber+"."+section+"."+isEnd);
			}
		}
	}

}
