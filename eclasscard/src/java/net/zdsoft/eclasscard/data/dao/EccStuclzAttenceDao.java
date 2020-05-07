package net.zdsoft.eclasscard.data.dao;

import java.util.List;
import java.util.Set;

import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EccStuclzAttenceDao extends BaseJpaRepositoryDao<EccStuclzAttence, String>{

	@Query("From EccStuclzAttence Where classAttId = ?2 and studentId = ?1 ")
	public EccStuclzAttence findByStuIdClzAttId(String studentId,String classAttId);
	
	@Query("From EccStuclzAttence Where classAttId = ?1 order by status")
	public List<EccStuclzAttence> findByClzAttId(String classAttId);

	@Modifying
    @Query("update EccStuclzAttence set status = ?1 Where id in (?1)")
	public void updateStatus(int status, String[] ids);

	@Query("From EccStuclzAttence Where status != 4 and studentId in (?1) and ((to_char(clockDate,'yyyy-MM-dd') >= ?2 and to_char(clockDate,'yyyy-MM-dd') <= ?3) or clockDate is null)")
	public List<EccStuclzAttence> findByClassIdSum(String[] studentIds, String bDate, String eDate);
	
	@Query("From EccStuclzAttence Where status != 4 and studentId = ?1 and ((to_char(clockDate,'yyyy-MM-dd') >= ?2 and to_char(clockDate,'yyyy-MM-dd') <= ?3)or clockDate is null)")
	public List<EccStuclzAttence> findByStudentIdSum(String studentId, String bDate, String eDate);

	@Query("From EccStuclzAttence Where classAttId in (?1) and status = 1")
	public List<EccStuclzAttence> findByClassAttIdsNeedPush(String[] attIds);
	
	@Query("From EccStuclzAttence Where classAttId in (?1) and studentId in (?2) and status = 1")
	public List<EccStuclzAttence> findListLeaveStudent(
			String[] classAttenceIds, String[] studentIds);

	@Query("Select studentId From EccStuclzAttence Where classAttId in (?1) ")
	public Set<String> findListStuByAttIds(String[] attIds);
}
