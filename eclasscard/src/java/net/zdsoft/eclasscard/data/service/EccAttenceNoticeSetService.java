package net.zdsoft.eclasscard.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;

public interface EccAttenceNoticeSetService extends BaseService<EccAttenceNoticeSet, String> {
	
	public EccAttenceNoticeSet findByUnitIdAndType(String unitId,String type);

}
