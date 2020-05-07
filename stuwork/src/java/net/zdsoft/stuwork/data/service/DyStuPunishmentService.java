package net.zdsoft.stuwork.data.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;

public interface DyStuPunishmentService extends BaseService<DyStuPunishment, String>{
	
	public List<DyStuPunishment> findByAll(String unitId, String acadyear, String semester, String punishTypeId, Date startTime, Date endTime, String[] studentIds, Pagination page);

	public String dealImport(String unitId, String importType, List<String[]> datas);
	
	public List<DyStuPunishment> getScortByStudentId(String studentId);
	
	public List<DyStuPunishment> getScortByStudentIdIn(String[] studentIds);
	
	public Map<String,Float> findMapBy(String unitId,String acadyear,String semester,int week);

	public List<DyStuPunishment> findByUnitIdAndPunishTypeIds(String unitId, String[] punishTypeIds);

	public void deleteByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);
	/**
	 * 
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param studentIds 可为空
	 * @return
	 */
	public List<DyStuPunishment> findByUnitIdAndStuIdIn(String unitId, String acadyear, String semester,String[] studentIds);
}
