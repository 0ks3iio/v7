package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;

import org.springframework.data.jpa.repository.Query;

public interface GkSubjectArrangeDao extends BaseJpaRepositoryDao<GkSubjectArrange, String> {

	@Query("From GkSubjectArrange where isDeleted=0 and id =?1")
	public GkSubjectArrange findByIdWithoutDeleted(String id);
	
	@Query("From GkSubjectArrange where isDeleted=0 and gradeId=?1 ")
	public GkSubjectArrange findByGradeId(String gradeId);
	
	@Query("From GkSubjectArrange where isDeleted=0 and unitId=?1 ")
	public List<GkSubjectArrange> findByUnitId(String unitId);
	
	@Query("From GkSubjectArrange where isDeleted=0 and unitId=?1 and isUsing=?2")
	public List<GkSubjectArrange> findByUnitIdIsUsing(String unitId, Integer isUsing);
}
