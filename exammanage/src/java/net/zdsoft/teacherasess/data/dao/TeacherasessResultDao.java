package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessResult;

public interface TeacherasessResultDao extends BaseJpaRepositoryDao<TeacherAsessResult, String>{

	@Query("From TeacherAsessResult where unitId = ?1 and assessId = ?2 and classType=?3 and subjcetId =?4 order by rank")
	public List<TeacherAsessResult> findByUnitIdAndAsessIdAndClassTypeAndSubjectId(String unitId,String assessId,String classType,String subjectId);
	
	@Query("From TeacherAsessResult where unitId = ?1 and assessId = ?2 and subjcetId =?3 order by rank")
	public List<TeacherAsessResult> findByUnitIdAndAsessIdAndSubjectId(String unitId,String assessId,String subjectId);
	
	@Query("select distinct(subjcetId) from TeacherAsessResult where unitId = ?1 and assessId = ?2")
	public List<String> findByUnitIdAndAsessId(String unitId,String asessId);
	
	@Modifying
	@Query("delete from TeacherAsessResult where unitId=?1 and assessId in (?2)")
	void deleteByAssessId(String unitId,String... assessId);
}
