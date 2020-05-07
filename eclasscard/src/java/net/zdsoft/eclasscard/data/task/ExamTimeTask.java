package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.service.EccExamTimeService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class ExamTimeTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(ExamTimeTask.class);
	
	public ExamTimeTask(String id,boolean isEnd) {
		this.bizId = id;
		this.isEnd = isEnd;
	}

	@Override
	public void run() {
		EccExamTimeService eccExamTimeService = Evn.getBean("eccExamTimeService");
		logger.info("推送考场门贴开始，id="+bizId);
		eccExamTimeService.examTimeTaskRun(bizId,isEnd);
		logger.info("推送考场门贴结束，id="+bizId);
	}


}
