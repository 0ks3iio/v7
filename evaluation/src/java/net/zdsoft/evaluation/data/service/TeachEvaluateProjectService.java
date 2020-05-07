package net.zdsoft.evaluation.data.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.framework.entity.Pagination;

public interface TeachEvaluateProjectService extends BaseService<TeachEvaluateProject, String> {

	public List<TeachEvaluateProject> findByUnitId(String unitId, String acadyear, String semester);
	
	public List<TeachEvaluateProject> findExist(String unitId, String acadyear,
			String semester, String evaluateType, Date beginTime, Date endTime);

	public void saveProject(TeachEvaluateProject project);
	
	public void updatePorject(TeachEvaluateProject project);
	
	public List<TeachEvaluateProject> findByCon(String unitId, String acadyear, String semester);

	public void deleteById(String projectId);
	/**
	 * 查找已经提交的学生
	 * @param selectType 1 学号 2姓名
	 * @param selectObj
	 * @param page 
	 * @return
	 */
	public List<Student> findByStuSubList(String projectId, Set<String> classIds,
			String selectType, String selectObj, Pagination page);

	public TeachEvaluateProject getSubNum(String projectId);

	public List<Student> findByStuNoSubList(String projectId,
			Set<String> clsIds, String selectType, String selectObj,
			Pagination page);

	public List<TeachEvaluateProject> findByUnitIdAndType(String unitId,
			String acadyear, String semester, String evaluateType);
	
}
