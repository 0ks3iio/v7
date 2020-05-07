package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.log4j.Logger;
public class DormAttanceTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(DormAttanceTask.class);
	
	public DormAttanceTask(String periodId,boolean isEnd) {
		this.bizId = periodId;
		this.isEnd = isEnd;
	}

	@Override
	public void run() {
		EccDormAttenceService eccDormAttenceService = Evn.getBean("eccDormAttenceService");
		if(RedisUtils.hasLocked(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+bizId+"."+isEnd)){//并发加锁
			try{
				logger.info("寝室考勤任务执行，id="+bizId);
				eccDormAttenceService.dormTaskRun(bizId, isEnd);
				if(isEnd){
					logger.info("寝室考勤时间段结束，id="+bizId);
				}else{
					logger.info("寝室考勤时间段开始，id="+bizId);
				}
			}finally{
				RedisUtils.unLock(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+bizId+"."+isEnd);
			}
		}
		
	}

}
