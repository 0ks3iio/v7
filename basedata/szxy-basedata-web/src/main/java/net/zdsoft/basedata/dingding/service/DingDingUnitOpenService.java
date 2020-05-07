package net.zdsoft.basedata.dingding.service;

import java.util.List;

import net.zdsoft.basedata.dingding.entity.DdUnitOpen;

public interface DingDingUnitOpenService {

	/**
	 * 根据状态获取数据
	 * 
	 * @param state
	 * @return
	 */

	public List<DdUnitOpen> findByState(Integer state);

	/**
	 * 根据单位id和状态获取数据
	 * 
	 * @param unitId
	 * @param state
	 * @return
	 */
	public List<DdUnitOpen> findByUnitIdAndState(String unitId, Integer state);
}
