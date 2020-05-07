package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;

import java.util.List;

/**
 * studevelop_month_performance
 * @author 
 * 
 */
public interface StudevelopMonthPerformanceService extends BaseService<StudevelopMonthPerformance, String>{

	/**
	 * 根据ids数组删除studevelop_month_performance数据
	 * @param ids
	 * @return
	 */
	public Integer delete(String[] ids);

	public void deleteByStudentIds(String unitId,String acadyear ,int semester, int performanceMouth ,String itemId ,String[] studentIds );

	public List<StudevelopMonthPerformance> getMonthPermanceByStuId(String unitId,String acadyear ,int semester, int performanceMouth ,String studentId ,String itemId );

	public void saveMonthPermance(StudevelopMonthPerformance studevelopMonthPerformance ,String classId,String unitId);

	public List<StudevelopMonthPerformance> getMonthPermanceListByStuIds(String unitId, String acadyear , int semester, String performanceMouth  , String itemId , String[] studentIds);

	public String saveCopyLastMouth(StudevelopMonthPerformance studevelopMonthPerformance ,String unitId);

	public void deleteByItemCodeId(String unitId,String itemcodeId);

	public void deleteByItemId(String unitId ,String itemId);

	public List<StudevelopMonthPerformance> getStudevelopMonthPerformanceByItemIds(String unitId, String acadyear , int semester, int performanceMouth  , String[] itemIds );

	public List<StudevelopMonthPerformance>  getMonthPermanceListByStuId(String unitId,String acadyear ,int semester, String studentId  );
}