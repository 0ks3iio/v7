package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.eclasscard.data.entity.EccDormAttence;

public interface EccDingMsgPushService {

	public void pushDingMsgTaskRun(String[] attIds,String type,String unitId,Integer sectionNumber);
	public void dormAttPushDingMsg(List<EccDormAttence> dormAttences,String unitId,Set<String> otherUserIds,EccAttenceNoticeSet noticeSet,String periodStr,Set<String> studentIds);

}
