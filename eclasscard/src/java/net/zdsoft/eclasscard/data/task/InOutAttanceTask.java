package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.service.EccInOutAttanceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.log4j.Logger;
public class InOutAttanceTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(InOutAttanceTask.class);
	protected String unitId;
	public InOutAttanceTask(String periodId,String unitId,boolean isEnd) {
		this.bizId = periodId;
		this.unitId = unitId;
		this.isEnd = isEnd;
	}

	@Override
	public void run() {
		EccInOutAttanceService eccInOutAttanceService = Evn.getBean("eccInOutAttanceService");
		if(RedisUtils.hasLocked(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+bizId+"."+isEnd)){//并发加锁
			try{
				logger.info("上下学考勤任务执行，id="+bizId);
				eccInOutAttanceService.InOutTaskRun(bizId,unitId, isEnd);
				if(isEnd){
					logger.info("上下学考勤时间段结束，id="+bizId);
				}else{
					logger.info("上下学考勤时间段开始，id="+bizId);
				}
			}finally{
				RedisUtils.unLock(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+bizId+"."+isEnd);
			}
		}
		
	}

}
