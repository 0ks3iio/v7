package net.zdsoft.officework.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.officework.dto.HkDataDto;
import net.zdsoft.officework.entity.OfficeDataLogHk;


public interface OfficeDataLogHkService extends BaseService<OfficeDataLogHk, String> {

	public void saveData(HkDataDto dataDto);
	
}