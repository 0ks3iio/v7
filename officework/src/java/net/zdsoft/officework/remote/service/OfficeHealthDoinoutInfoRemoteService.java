package net.zdsoft.officework.remote.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthDoinoutInfo;


public interface OfficeHealthDoinoutInfoRemoteService{
	
	/**
	 * 进出校(华三)
	 * @param dataStr
	 */
	public String doInOut(String dataStr);
	/**
	 * 进出校(天波)
	 * @param dataStr
	 */
	public String doTBInOut(String dataStr);
	
	public void sendInOutDataToWeiKe(List<OfficeHealthDoinoutInfo> list,
			Map<String, OfficeHealthInfoDto> stepMap);

	public void dealInOutData(List<OfficeHealthDoinoutInfo> list,Map<String, OfficeHealthInfoDto> stepMap);
}