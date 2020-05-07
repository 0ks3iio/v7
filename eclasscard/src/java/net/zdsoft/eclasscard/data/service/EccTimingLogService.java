package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccTimingLog;

public interface EccTimingLogService extends BaseService<EccTimingLog, String>{
	/**
	 * 获取每一台最新的时间设置
	 * @return
	 */
	public List<EccTimingLog> findNewTimeList(String[] unitId);
	
	public void saveTimeLog(EccTimingLog timeLog,EccTimingLog otherLog);


}
