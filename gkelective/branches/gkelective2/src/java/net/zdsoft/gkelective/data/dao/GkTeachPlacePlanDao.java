package net.zdsoft.gkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;

public interface GkTeachPlacePlanDao extends BaseJpaRepositoryDao<GkTeachPlacePlan, String>{
	@Query("From GkTeachPlacePlan where subjectArrangeId =?1 and isDeleted =0 order by  startTime desc")
	public List<GkTeachPlacePlan> findBySubjectArrangeId(String arrangeId);
	
	@Query("From GkTeachPlacePlan where id =?1 and isDeleted =0 ")
	public GkTeachPlacePlan findGkTeachPlacePlanById(String id);

	@Query("From GkTeachPlacePlan where subjectArrangeId =?1 ")
	public List<GkTeachPlacePlan> findBySubjectArrangeIdHasDelete(
			String subjectArrangeId);
	@Query("From GkTeachPlacePlan where isDeleted=0 and roundsId =?1 ")
	public List<GkTeachPlacePlan> findByRoundId(String roundsId);
}
