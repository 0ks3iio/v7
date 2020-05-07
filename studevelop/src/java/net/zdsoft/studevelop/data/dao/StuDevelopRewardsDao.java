package net.zdsoft.studevelop.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface StuDevelopRewardsDao extends BaseJpaRepositoryDao<StuDevelopRewards, String>{
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and schid = ?3")
	public List<StuDevelopRewards> findListByAcaAndSemAndUnitId(String acadyear, String semester, String unitId, Pageable page);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and schid = ?3")
	public List<StuDevelopRewards> findListByAcaAndSemAndUnitId(String acadyear, String semester, String unitId);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid in (?3)")
	public List<StuDevelopRewards> findByStuIds(String acadyear, String semester, String[] stuids, Pageable page);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid in (?3)")
	public List<StuDevelopRewards> findByStuIds(String acadyear, String semester, String[] stuids);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid = ?3")
	public List<StuDevelopRewards> findByStuId(String acadyear, String semester, String stuid, Pageable page);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid = ?3")
	public List<StuDevelopRewards> findByStuId(String acadyear, String semester, String stuid);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and schid = ?3 and rewardslevel = ?4")
	public List<StuDevelopRewards> findByUnitIdAndLevel(String acadyear, String semester, String unitId, String rewardslevel, Pageable page);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and schid = ?3 and rewardslevel = ?4")
	public List<StuDevelopRewards> findByUnitIdAndLevel(String acadyear, String semester, String unitId, String rewardslevel);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and rewardslevel = ?3 and stuid in (?4)")
	public List<StuDevelopRewards> findByStuidsAndLevel(String acadyear, String semester, String rewardslevel, String[] stuids, Pageable page);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and rewardslevel = ?3 and stuid in (?4)")
	public List<StuDevelopRewards> findByStuidsAndLevel(String acadyear, String semester, String rewardslevel, String[] stuids);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid = ?3 and rewardslevel = ?4")
	public List<StuDevelopRewards> findByStuidAndLevel(String acadyear, String semester, String stuid, String rewardslevel, Pageable page);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid = ?3 and rewardslevel = ?4")
	public List<StuDevelopRewards> findByStuidAndLevel(String acadyear, String semester, String stuid, String rewardslevel);
    
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and stuid = ?3")
	public List<StuDevelopRewards> findByAcaAndSemAndStuId(String acadyear,String semester, String studentId);
	
	@Query("From StuDevelopRewards where acadyear = ?1 and semester = ?2 and schid = ?3")
	public List<StuDevelopRewards> findListAll(String acadyear, String semester, String unitId);
}
