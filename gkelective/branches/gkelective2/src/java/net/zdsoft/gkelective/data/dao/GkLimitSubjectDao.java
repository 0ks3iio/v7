package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkLimitSubjectDao extends BaseJpaRepositoryDao<GkLimitSubject, String>{

	public List<GkLimitSubject> findBySubjectArrangeId(String subjectArrangeId);
	
	@Modifying
	@Query("DELETE FROM GkLimitSubject where subjectArrangeId = ?1 ")
	public void deleteBySubjectArrangeId(String arrangeSubjectId);
}
