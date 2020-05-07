package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessSet;

public interface TeacherasessSetDao extends BaseJpaRepositoryDao<TeacherAsessSet, String>{
	
	@Query("From TeacherAsessSet where unitId = ?1 and assessId = ?2 and subjectId =?3 order by className")
	public List<TeacherAsessSet> findByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);

	public List<TeacherAsessSet> findByUnitIdAndAssessId(String unitId, String teacherAsessId);

}
