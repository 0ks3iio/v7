package net.zdsoft.comprehensive.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.comprehensive.entity.CompreStatistics;

import java.util.List;

public interface CompreStatisticsService extends BaseService<CompreStatistics, String>{

	public void saveAllStatistics(List<CompreStatistics> insertList);

	public List<CompreStatistics> findByStudentIdAcadyear(String studentId,
			String[] acadyear);
	
	public List<CompreStatistics> findByStudentIdsAcadyear(String[] studentIds,
			String[] acadyear);

	public List<CompreStatistics> findByStudentIds(String[] studentIds);

	public List<CompreStatistics> findByUnitIdAcadyear(String unitId,
			String[] acadyear);

//分割线，以上为原有方法，方便后续删除
	public List<CompreStatistics> findByTimeStudentId(String unitId,
			String acadyear, String semester,String type, String[] studentId);

	public void saveAll(String unitId, String acadyear, String semester,
			String[] delStuIds,String[] delType, List<CompreStatistics> insertList);

	public void deleteByStudentIds(String unitId, String acadyear,
			String semester,String[] delType, String[] studentId);

	public List<CompreStatistics> findByAcaSemStudentIds(String acadyear, String semester, String[] studentIds);

    List<CompreStatistics> findByTypeStudentIdsAcadyear(String type, String[] studentIds, String[] acadyear);
}
