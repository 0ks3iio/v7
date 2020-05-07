package net.zdsoft.eclasscard.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;

import java.util.List;

public interface EccFaceActivateService extends BaseService<EccFaceActivate,String>{

	public EccFaceActivate findByInfoId(String unitId, String infoId);

	public List<EccFaceActivate> findListByUnitId(String unitId);
	
	public void updateNeedLowerUnitId(String unitId);

	public void deleteByInfoId(String unitId, String infoId);
	
	public List<EccFaceActivate> findAllNeedLower();
}
