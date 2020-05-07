package net.zdsoft.base.service;

import java.util.List;

import net.zdsoft.base.entity.base.BaseRelation;
import net.zdsoft.basedata.service.BaseService;

public interface BaseRelationService extends BaseService<BaseRelation, String> {

	public String findByRelationParm(String businessId,String ticketKey,String sourceAp,String model);
	
	public List<BaseRelation> findListByRelationParm(String[] businessIds,String ticketKey,String sourceAp,String model,int unitClass);
	
	public void savelists(List<BaseRelation> relations);
	
	public void deleteByDcIds(String[] dcIds);
}
