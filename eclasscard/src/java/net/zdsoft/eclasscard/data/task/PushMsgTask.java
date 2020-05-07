package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.service.EccDingMsgPushService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class PushMsgTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(PushMsgTask.class);
	private String[] attIds;
	private String type;
	private Integer sectionNumber;
	
	public PushMsgTask(String[] attIds,String type,Integer sectionNumber,String unitId) {
		this.attIds = attIds;
		this.type = type;
		this.sectionNumber = sectionNumber;
		this.bizId = unitId;
	}

	@Override
	public void run() {
		String typeStr = "";
		if(EccConstants.CLASS_ATTENCE_SET_TYPE1.equals(type)){
			typeStr = "第"+sectionNumber+"节上课考勤";
		}else if(EccConstants.DORM_ATTENCE_SET_TYPE2.equals(type)){
			typeStr = "寝室考勤";
		}
		EccDingMsgPushService eccDingMsgPushService = Evn.getBean("eccDingMsgPushService");
		logger.info(typeStr+"推送钉钉消息开始");
		eccDingMsgPushService.pushDingMsgTaskRun(attIds,type,bizId,sectionNumber);
		logger.info(typeStr+"推送钉钉消息结束");
		
	}


}
