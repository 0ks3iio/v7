package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;

public interface DyBusinessOptionService extends BaseService<DyBusinessOption, String>{

	public List<DyBusinessOption> findListByUnitIdAndType(String unitId, String businessType);
	
	public List<DyBusinessOption> findListByUnitId(String unitId);
	
	public List<DyBusinessOption> findListByUidBtypeAndOname(String unitId, String businessType,String optionName);
	
	public List<DyBusinessOption> findListByUidBtypeAndOrderId(String unitId, String businessType,int orderId);
	
	public void deleteByUIdBType(String unitId,String businessType);
	
	public void save(List<DyBusinessOption> dyBusinessOptionList, String unitId, String businessType);
	
	public void deleteByUnitIdAndType(String unitId, String businessType);
	
	public void deleteAndOrder(String id,String unitId, String businessType);
	
	public void saveAndOrder(String unitId, String businessType, List<DyBusinessOption> dyBusinessOptionList);
}
