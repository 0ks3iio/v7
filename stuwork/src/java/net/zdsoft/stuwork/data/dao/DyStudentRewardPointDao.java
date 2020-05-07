package net.zdsoft.stuwork.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStudentRewardPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DyStudentRewardPointDao extends BaseJpaRepositoryDao<DyStudentRewardPoint,String>{
	
	
	@Query("from DyStudentRewardPoint where student_id=?1 and acadyear=?2 and semester=?3 and isDeleted= 0 order by modifyTime desc")
	List<DyStudentRewardPoint> getStudentRewardPointByStudentId(
			String studentId, String acadyear, String semester,
			Pageable pageable);
	
	@Query("from DyStudentRewardPoint where settingId=?1  and semester=?2 and acadyear=?3 and isDeleted= 0 order by modifyTime desc")
	List<DyStudentRewardPoint> findBySettingId(String settingId, String semester, String acadyear);
	
	
	@Modifying
	@Query("delete from DyStudentRewardPoint where settingId=?1  and acadyear=?2  and semester=?3 and isDeleted= 0")
	void deletePoints(String settingId, String acadyear, String semester);
	
	
	@Modifying
	@Query("delete from DyStudentRewardPoint where settingId=?1 and isDeleted= 0  and acadyear=?2 and semester=?3 and id not in (?4) ")
	void deletePoints(String settingId, String acadyear, String semester,
			String[] pointIds);
	
	
	@Query("from DyStudentRewardPoint where unitId=?1  and studentId=?2 and isDeleted= 0 and acadyear in (?3)")
	List<DyStudentRewardPoint> findByAcadyear(String unitId, String studentId, String[] acadyears);

	@Query("from DyStudentRewardPoint where unitId=?1 and isDeleted= 0")
	List<DyStudentRewardPoint> findByUnitId(String unitId);
	
	@Modifying
	@Query("delete from DyStudentRewardPoint where settingId=?1")
	void deleteBySettingId(String settingId);
	
	@Modifying
	@Query("delete from DyStudentRewardPoint where projectId=?1")
	void deleteByProjectId(String projectId);

	@Modifying
	@Query("delete from DyStudentRewardPoint where acadyear = ?1  and semester = ?2 and projectId in (?3)")
	void deleteByAcadyearSemesterProjectIds(String acadyear, String semester, String[] projectIds);

	@Modifying
	@Query("delete from DyStudentRewardPoint where projectId in (?1)")
	void deleteByProjectIds(String[] projectIds);

	@Modifying
	@Query("update  DyStudentRewardPoint set isDeleted =1 where projectId in (?1)")
	void updateIsDeteletd(String[] projectIds);
	
	@Query("from DyStudentRewardPoint where unitId=?1  and studentId in (?2) and isDeleted= 0 and acadyear in (?3)")
	List<DyStudentRewardPoint> findByAcadyear(String unitId, String[] studentIds, String[] acadyears);

	@Query("from DyStudentRewardPoint where studentId in (?1) ")
	List<DyStudentRewardPoint> findByStudentIdIn(String[] studentIds);

	@Query("from DyStudentRewardPoint where unitId=?1 and acadyear=?2 and semester=?3 and isDeleted= 0 and studentId in (?4) and projectId in (?5)")
	List<DyStudentRewardPoint> findByStudentIds(String unitId, String acadyear, String semester, String[] studentIds, String[] projectIds);
}
