package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessCheck;

public interface TeacherasessCheckDao extends BaseJpaRepositoryDao<TeacherAsessCheck, String>{
	
	@Transactional
	@Modifying
	@Query("delete From TeacherAsessCheck where assessId=?1")
	void deleteByAssessId(String teacherAsessId);
	
	@Query("From TeacherAsessCheck where unitId=?1 and assessId=?2 and subjectId=?3 order by classId")
	public List<TeacherAsessCheck> getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(String unitId,String assessId,String subjectId);

}
