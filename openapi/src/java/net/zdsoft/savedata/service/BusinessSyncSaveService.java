package net.zdsoft.savedata.service;

import java.util.Map;

import net.zdsoft.savedata.entity.CheckWorkData;

public interface BusinessSyncSaveService {
	
	/**
	 * @param array
	 * @param returnMsg
	 */
	void saveCheckWork(CheckWorkData[] array, Map<String, String> returnMsg) throws Exception;

}
