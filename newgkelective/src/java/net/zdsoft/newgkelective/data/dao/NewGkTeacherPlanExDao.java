package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;

public interface NewGkTeacherPlanExDao extends BaseJpaRepositoryDao<NewGkTeacherPlanEx, String>{
	
	public void deleteByTeacherPlanIdIn(String[] teacherPlanIds);

	public void deleteByIdIn(String[]id);

	@Query("select teacherId from NewGkTeacherPlanEx where teacherPlanId=?1")
	public List<String> findByTeacherPlanId(String teacherPlanId);

	@Query("select tpe from NewGkTeacherPlanEx tpe, NewGkTeacherPlan tp where tp.arrayItemId =?1 and tpe.teacherId in ?2 and tp.id = tpe.teacherPlanId")
	public List<NewGkTeacherPlanEx> findByTeacherId(String arrayItemId, String[] teacherIds);

    // Basedata Sync Method
    void deleteByTeacherIdIn(String... teacherIds);
    // Basedata Sync Method
    List<NewGkTeacherPlanEx> findByClassIdsLike(String classId);
}
