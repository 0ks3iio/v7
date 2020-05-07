package net.zdsoft.eclasscard.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.eclasscard.data.entity.EccFaceLowerLog;

public interface EccFaceLowerLogService extends BaseService<EccFaceLowerLog, String>{

	public void faceLowerCheckAll();

	public boolean lowerFaceByCard(EccFaceActivate face, EccFaceLowerLog lowerLog); 
}
