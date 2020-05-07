package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeachPlan;

public interface TeachPlanService extends
		BaseService<TeachPlan, String> {

	public void deleteByAcadyearAndSemesterAndClassIds(String unitId, String acadyear, int semester, String[] classIds);

	public List<TeachPlan> findTeachPlanListByClassIds(String acadyear, int semester, String[] classIds);

	public List<TeachPlan> findTeachPlanListByClassIdsAndSubjectIds(String acadyear, int semester, String[] classIds, String[] subjectIds);
}
