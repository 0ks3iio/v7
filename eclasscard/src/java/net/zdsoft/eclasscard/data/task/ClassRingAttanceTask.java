package net.zdsoft.eclasscard.data.task;

import java.util.Set;

import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class ClassRingAttanceTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(ClassRingAttanceTask.class);
	private Set<String> attIds;
	
	public ClassRingAttanceTask(String unitId,Set<String> attIds,boolean isEnd) {
		this.attIds = attIds;
		this.isEnd = isEnd;
		this.bizId = unitId;
	}
	
	@Override
	public void run() {
		EccClassAttenceService eccClassAttenceService = Evn.getBean("eccClassAttenceService");
		logger.info("单位："+bizId+",上课手环考勤任务执行");
		System.out.println("获取手环考勤数据======================================");
		eccClassAttenceService.classRingTaskRun(bizId, attIds, isEnd);
	}

}
