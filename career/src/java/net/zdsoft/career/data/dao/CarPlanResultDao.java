package net.zdsoft.career.data.dao;

import java.util.List;

import net.zdsoft.career.data.entity.CarPlanResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CarPlanResultDao extends BaseJpaRepositoryDao<CarPlanResult,String>{

	@Query("From CarPlanResult where isDeleted = 0 and studentId in (?1)")
	public List<CarPlanResult> findByStudentIds(String[] studentIds);

	@Modifying
    @Query("update from CarPlanResult set isDeleted = 1 Where studentId in (?1)")
	public void deleteTest(String[] studentIds);

	@Query("select s1 from CarPlanResult as s1,Student as s2 where s1.studentId = s2.id and s1.schoolId = ?1 and s2.studentName like (?2) and s1.isDeleted = 0 and s2.isDeleted = 0 and s2.isLeaveSchool = 0 order by s1.creationTime desc")
	public List<CarPlanResult> findBystuName(String unitId,String studentName,Pageable pageable);

	@Query("select s1 from CarPlanResult as s1,Student as s2 where s1.studentId = s2.id and s1.schoolId = ?1 and s2.studentName like (?2) and s1.isDeleted = 0 and s2.isDeleted = 0 and s2.isLeaveSchool = 0 order by s1.creationTime desc")
	public List<CarPlanResult> findBystuName(String unitId,String studentName);
	
	@Query("select s1 from CarPlanResult as s1,Student as s2 where s1.studentId = s2.id and s1.schoolId = ?1 and s2.classId = ?2 and s2.studentName like (?3) and s1.isDeleted = 0 and s2.isDeleted = 0 and s2.isLeaveSchool = 0 order by s1.creationTime desc")
	public List<CarPlanResult> findByClassIdAndStuName(String unitId,
			String classId, String studentName, Pageable page);
	
	@Query("select s1 from CarPlanResult as s1,Student as s2 where s1.studentId = s2.id and s1.schoolId = ?1 and s2.classId = ?2 and s2.studentName like (?3) and s1.isDeleted = 0 and s2.isDeleted = 0 and s2.isLeaveSchool = 0 order by s1.creationTime desc")
	public List<CarPlanResult> findByClassIdAndStuName(String unitId,
			String classId, String studentName);
	
}
