package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;

public interface NewGkTeacherPlanDao extends BaseJpaRepositoryDao<NewGkTeacherPlan, String>{

	List<NewGkTeacherPlan> findByArrayItemIdIn(String[] teacherArrangeIds);
	
	void deleteByArrayItemId(String arrayItemId);
	
	void deleteByIdIn(String[] ids);

	List<NewGkTeacherPlan> findByArrayItemIdAndSubjectIdIn(String arrayItemId, String[] subjectIds);
    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subids);
}
