package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.TimingSetDto;
import net.zdsoft.eclasscard.data.entity.EccTimingSet;


public interface EccTimingSetService extends BaseService<EccTimingSet,String>{

	/**
	 * 根据单位Id获取数据
	 * @param unitId
	 * @return
	 */
	List<EccTimingSet> findByUnitId(String unitId);

	/**
	 * 保存设置
	 * @param timingSetDto
	 */
	void saveTimingSet(TimingSetDto timingSetDto);

}
