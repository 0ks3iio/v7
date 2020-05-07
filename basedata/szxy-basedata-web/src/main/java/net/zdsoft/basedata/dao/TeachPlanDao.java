package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.TeachPlan;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachPlanDao extends BaseJpaRepositoryDao<TeachPlan, String> {

	void deleteByUnitIdAndAcadyearAndSemesterAndClassIdIn(String unitId, String acadyear, int semester, String[] classIds);

	List<TeachPlan> findByAcadyearAndSemesterAndClassIdInAndSubjectIdIn(String acadyear, int semester, String[] classIds, String[] subjectIds);
	
}
