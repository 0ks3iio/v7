package net.zdsoft.eclasscard.data.task;

import java.util.Set;

import net.zdsoft.eclasscard.data.service.EccExamTimeService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class UnitDoorTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(UnitDoorTask.class);
	private Set<String> infoIds;
	public UnitDoorTask(String bizId,Set<String> infoIds) {
		this.bizId = bizId;
		this.infoIds = infoIds;
	}


	@Override
	public void run() {
		EccExamTimeService eccExamTimeService = Evn.getBean("eccExamTimeService");
		logger.info("按单位推送考场门贴开始，id="+bizId);
		eccExamTimeService.examTimeUnitTaskRun(bizId,infoIds);
		logger.info("按单位推送考场门贴结束，id="+bizId);
	}


}
