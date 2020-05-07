package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DyStudentRewardSettingDao extends BaseJpaRepositoryDao<DyStudentRewardSetting,String>{
	
	@Query("FROM DyStudentRewardSetting where unitId = ?1 and projectId in (?2) order by rewardGrade,rewardLevelOrder")
	List<DyStudentRewardSetting> findListByProjectIdsOrderByRewardGradeAndRewardLevelOrder(
			String unitId,String[] projectIds );

	@Query("FROM DyStudentRewardSetting where unitId = ?1 and projectId in (?2) order by rewardPeriod,rewardLevelOrder")
	List<DyStudentRewardSetting> findListByProjectIdsOrderByRewardPeriodAndRewardLevelOrder(
			String unitId, String[] projectIds);
	@Modifying
    @Query("delete from DyStudentRewardSetting where id in (?1)")
	void deleteByIds(String[] ids);
	@Modifying
	@Query("delete from DyStudentRewardSetting where unitId = ?1 and projectId in (?2)")
	void deleteByProIds(String unitId,String[] projectIds);
	
	@Query("FROM DyStudentRewardSetting where unitId = ?1 ")
	List<DyStudentRewardSetting> findByUnitId(String unitId);
	
	@Query("FROM DyStudentRewardSetting where projectId = ?1 ")
	List<DyStudentRewardSetting> findByProjectId(String projectId);
	
	@Query("FROM DyStudentRewardSetting where projectId in ?1 order by rewardPeriod,projectId desc,rewardLevelOrder asc " )
	List<DyStudentRewardSetting> findInProjectId(String[] projectIds,
			Pageable pageable);
}
