package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class FullObjTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(FullObjTask.class);

	public FullObjTask(String bulletinId,boolean isEnd) {
		this.bizId = bulletinId;
		this.isEnd = isEnd;
	}

	@Override
	public void run() {
		EccFullObjService eccFullObjService = Evn.getBean("eccFullObjService");
		logger.info("全屏展示内容开始，id="+bizId);
		eccFullObjService.fullObjTaskRun(bizId,isEnd);
		logger.info("全屏展示内容结束，id="+bizId);
	}


}
