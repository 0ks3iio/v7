package net.zdsoft.officework.service;


import java.util.List;
import java.util.Map;

import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthDoinoutInfo;
public interface OfficeHealthDoinoutInfoService{

	/**
	 * 新增office_health_doinout_info
	 * @param list
	 */
	public void saveBatch(List<OfficeHealthDoinoutInfo> list);
	
}