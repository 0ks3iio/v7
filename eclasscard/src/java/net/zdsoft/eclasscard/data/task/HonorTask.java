package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.service.EccHonorService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class HonorTask extends EccTask{

	private static final Logger logger = Logger.getLogger(HonorTask.class);
	
	public HonorTask(String honorId,boolean isEnd) {
		this.bizId = honorId;
		this.isEnd = isEnd;
	}
	
	@Override
	public void run() {
		EccHonorService eccHonorService = Evn.getBean("eccHonorService");
		logger.info("推送通知公告开始，id="+bizId);
		eccHonorService.honorTaskRun(bizId,isEnd);
		logger.info("推送通知公告结束，id="+bizId);
	}
}
