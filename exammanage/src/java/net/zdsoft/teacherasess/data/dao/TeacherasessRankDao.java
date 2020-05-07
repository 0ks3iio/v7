package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;

public interface TeacherasessRankDao extends BaseJpaRepositoryDao<TeacherAsessRank, String>{

	@Query("From TeacherAsessRank where unitId = ?1 and assessId = ?2 and subjectId =?3 order by name")
	public List<TeacherAsessRank> findByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);

	public List<TeacherAsessRank> findByUnitIdAndAssessId(String unitId, String asessId);
}
