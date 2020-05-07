package net.zdsoft.stuwork.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DyStudentRewardProjectDao extends BaseJpaRepositoryDao<DyStudentRewardProject,String>{
	
	
	@Query("From DyStudentRewardProject where classesType = ?1 and unitId=?2 order by rewardClasses,projectName desc")
	List<DyStudentRewardProject> findListByClassesType(String classesType,
			String unitId);
	
	@Query("From DyStudentRewardProject where classesType = ?1 and unitId=?2 order by projectType asc")
	List<DyStudentRewardProject> findListByClassesTypeOrderByProjectType(String classesType,
			String unitId);
	
	@Query("From DyStudentRewardProject where classesType = ?1 and unitId=?2 and rewardPeriod=?3 order by rewardPeriod,projectType asc")
	List<DyStudentRewardProject> findListByClassesTypeOrderByProjectType(String classesType,
			String unitId,int rewardPeriod);
	@Query("From DyStudentRewardProject where rewardPeriod = ?1 and rewardClasses=?2 and unitId=?3 order by rewardPeriod,projectType asc")
	List<DyStudentRewardProject> findListByRewardPeriodAndRewardClasses(
			int rewardPeriod, String rewardClasses, String unitId);
	
	@Query("From DyStudentRewardProject where rewardClasses = ?1 and projectName=?2 and unitId=?3 order by  rewardClasses,projectName asc")
	DyStudentRewardProject findByClassesAndName(String rewardClasses,
			String projectName, String unitId);
	@Query("From DyStudentRewardProject where rewardClasses = ?1 and classesType =?2  and unitId=?3 order by  projectType asc")
	List<DyStudentRewardProject> findByTypeClassesAndName(String rewardClasses, String classType, String unitId);
	
	@Query("From DyStudentRewardProject where unitId = ?1 ")
	List<DyStudentRewardProject> findByUnitId(String unitId);
	
	@Query("From DyStudentRewardProject where  classesType =?1 and unitId = ?2 and rewardPeriod<>0 ")
	List<DyStudentRewardProject> findListByFestivalWithOutZero(
			String classesType, String unitId);
	
	@Modifying
	@Transactional
	@Query("delete from DyStudentRewardProject where id in (?1)")
	void deletByIds(String[] ids);
}
