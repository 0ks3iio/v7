package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.service.EccFullObjAllService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class FullObjAllTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(FullObjAllTask.class);

	public FullObjAllTask(String fullObjId,boolean isEnd) {
		this.bizId = fullObjId;
		this.isEnd = isEnd;
	}

	@Override
	public void run() {
		EccFullObjAllService eccFullObjAllService = Evn.getBean("eccFullObjAllService");
		logger.info("全屏展示内容开始，id="+bizId);
		eccFullObjAllService.fullObjAllTaskRun(bizId,isEnd);
		logger.info("全屏展示内容结束，id="+bizId);
	}


}
