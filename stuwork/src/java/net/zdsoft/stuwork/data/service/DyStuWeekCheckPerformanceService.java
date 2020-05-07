package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;

public interface DyStuWeekCheckPerformanceService extends BaseService<DyStuWeekCheckPerformance, String>{

	public List<DyStuWeekCheckPerformance> findByStudentIds(String unitId, String acadyear, String semester, String week, String[] studentIds);
	
	public void saveList(String unitId, String acadyear, String semester, String week, List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList);
    
	public String doImport(String unitId, List<String[]> datas, String acadyear, String semester, String week);
	
	public List<DyStuWeekCheckPerformance> findByStudentId(String studentId);
	
	public List<DyStuWeekCheckPerformance> findByStudentIdIn(String[] studentIds);

	public List<DyStuWeekCheckPerformance> findByUnitIdAndGradeIds(String unitId, String[] gradeIds);
	/**
	 * 
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param studentIds 可为空
	 * @return
	 */
	public List<DyStuWeekCheckPerformance> findByUnitIdAndStuIds(String unitId, String acadyear, String semester,String[] studentIds);
}
