package net.zdsoft.stuwork.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;
import net.zdsoft.stuwork.data.entity.DyDormForm;

public interface DyDormCheckResultService extends BaseService<DyDormCheckResult,String>{
	/**
	 * 根据schoolId, buildingId,week,day查找dyDormCheckResult map
	 * @param schoolId, buildingId,week,day
	 * @param 
	 * @return
	 */
	public Map<String,DyDormCheckResult> getResultMap(String schoolId,String buildingId,String inputDate);
	
	public void saveResultAndRemindList(DormSearchDto dormDto,String unitId,String userId);
	
	public List<DyDormForm> getResRemForm(DormSearchDto dormDto);
	/**
	 * 根据schoolId buildingId inputDate考核list封装成 key-roomId  value-扣分情况 map
	 * @param 
	 * @param 
	 * @return
	 */
	public Map<String,String> getResultListForMap(String schoolId,String buildingId,String inputDate);
	/**
	 * 导入寝室考核信息
	 * @param schoolId TODO
	 * @param acadyear TODO
	 * @param semester TODO
	 * @param searchDate
	 * @param datas [week,时间范围,section[0],section[1]...]
	 * @return
	 */
	public String doCheckImport(String schoolId, String acadyear, String semester,String userId,String buildingId, String searchDate, List<String[]> datas);
	/**
	 * 根据classId inputDate schoolId acadyear semesterStr获取 score remark
	 * @param 
	 * @param 
	 * @return
	 */
	public Map<String,String> getClassResult(String classId,String inputDate,String schoolId,String acadyear,String semesterStr);
	
	public List<DyDormForm> getResRemFormWeek(DormSearchDto dormDto);
	
	
	public Map<String,DyDormCheckResult> getResultMapWeek(String schoolId,String acadyear, String semester, int week, String[] roomIds);
}
