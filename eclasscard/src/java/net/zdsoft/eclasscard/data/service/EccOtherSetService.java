package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.EccOtherSetDto;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;


public interface EccOtherSetService extends BaseService<EccOtherSet,String>{
	
	public void updateOtherSet(Integer nowvalue,String unitId,Integer type);
	
	public EccOtherSet findByUnitIdAndType(String unitId,Integer type);
	
	public List<EccOtherSet> findByOpenAndType(Integer nowvalue, Integer type);
	public List<EccOtherSet> findListByUnitId(String unitId);
	
	/**
	 * 适合返回yes  or  no 的属性
	 * @param unitId
	 * @param type
	 * @return
	 */
	public boolean openProperty(String unitId,Integer type);

	public void saveOtherSet(String unitId, Integer nowValue, Integer type);
	
	public void saveOtherSet(EccOtherSetDto otherSetDto,String unitId);
	
}
