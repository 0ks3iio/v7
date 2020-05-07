package net.zdsoft.stuwork.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;

public interface DyDormCheckRemindService extends BaseService<DyDormCheckRemind, String>{
	
	/**
	 * 根据schoolId, roomIds,checkDate查找dyDormCheckRemind map
	 * @param schoolId, roomIds,checkDate
	 * @param 
	 * @return
	 */
	public List<DyDormCheckRemind> getRemindByCon(String schoolId,String checkDate,String[] roomIds);
	/**
	 * 根据schoolId, buildingId,checkDate查找dyDormCheckRemind map
	 * @param schoolId, buildingId,checkDate
	 * @param 
	 * @return
	 */
	public Map<String,DyDormCheckRemind> getRemindMap(String schoolId,String buildingId,String checkDate);
	
	/**
	 * 根据ids删除dyDormCheckRemind
	 * @param ids
	 * @param 
	 * @return
	 */
	public void deleteByIds(String[] ids);
	/**
	 * 根据schoolId, buildingId,week,day查找dyDormCheckRemind map
	 * @param schoolId, buildingId,week,day
	 * @param 
	 * @return
	 */
	public Map<String,DyDormCheckRemind> getRemindMap(DormSearchDto dormDto);
	
	public Map<String,DyDormCheckRemind> getRemindWeekMap(DormSearchDto dormDto, String[] roomIds);
	
}
