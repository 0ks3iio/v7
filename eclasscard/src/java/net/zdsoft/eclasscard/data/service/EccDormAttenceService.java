package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccDormAttence;


public interface EccDormAttenceService extends BaseService<EccDormAttence, String>{

	public List<EccDormAttence> findDormAttenceByPeriodId(String periodId, Date date);
	public List<EccDormAttence> findListByPlaceIdNotOver(String placeId,String unitId);
	/**
	 * 定时器加入寝室考勤时间到delayedqueue
	 */
	public void addDormAttenceQueue();
	public void dormTaskRun(String periodId, boolean isEnd);
	/**
	 * 返回当天年级对应的考勤类型，0为不需要考勤
	 * @param gradeIds
	 * @param date
	 * @return
	 */
	public Map<String,Integer> findDormAttType(String unitId,String[] gradeIds, Date date);
	
	/**
	 * 获取list
	 * @param periodId
	 * @param date
	 * @param periodIds
	 * @return
	 */
	public List<EccDormAttence> findListByCon(String unitId,String periodId,String date,String buildingId,String[] periodIds);
	/**
	 * 获取list for 汇总
	 * @param startTime
	 * @param endTime
	 * @param 
	 * @return
	 */
	public List<EccDormAttence> findStatByCon(String unitId,String startTime,String endTime);
	/**
	 * 班牌考勤获取当前时间段未初始化的数据
	 * @param unitId
	 * @param periodId
	 * @param date
	 * @param placeIds
	 * @return
	 */
	public List<EccDormAttence> findListByEccNotInit(String unitId,
			String periodId, String date, String[] placeIds);
}
