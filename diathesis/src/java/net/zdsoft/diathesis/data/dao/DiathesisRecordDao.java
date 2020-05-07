package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisRecord;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:30
 */
public interface DiathesisRecordDao extends BaseJpaRepositoryDao<DiathesisRecord,String> {

	@Query("select projectId, count(*) from DiathesisRecord where unitId=?1 and projectId in (?2) and status=0 group by projectId")
	List<Object[]> findStatusMapByProjectIdIn(String unitId, String[] projectIds);

	@Modifying
	@Query("delete from DiathesisRecord where projectId in (?1)")
	void deleteByProjectId(String[] projectIds);

	@Query(value = "select id from newdiathesis_record where PROJECT_ID in (?1)" ,nativeQuery = true)
	List<String> findByProjectIdIn(String[] projectIds);

	@Query("From DiathesisRecord where unitId=?1 and stuId=?2 and status=?3 ")
	List<DiathesisRecord> findListByStuId(String unitId, String stuId, String status);

	@Query("From DiathesisRecord where unitId=?1 and stuId=?2 and gradeCode=?3 and status=?4 ")
	List<DiathesisRecord> findListByGradeCodeAndStuId(String unitId, String stuId, String gradeCode, String status);

	@Query(value = "SELECT distinct(STU_ID) FROM newdiathesis_record where UNIT_ID=?1 and PROJECT_ID in ?2" ,nativeQuery = true)
	List<String> findStuIdByProjectIdInAndUnitId(String unitId,List<String> projectIds);

	@Query("From DiathesisRecord where unitId=?1 and acadyear=?2 and semester=?3 and stuId in ?4 and projectId in ?5")
	List<DiathesisRecord> findListByUnitIdAndStuIdInAndProjectIdIn(String unitId, String acadyear, Integer semester, List<String> stuIds, List<String> proIds);

	@Query("From DiathesisRecord where unitId=?1 and stuId = ?2")
	List<DiathesisRecord> findListByUnitIdAndStuId(String unitId, String studentId);

	@Query("From DiathesisRecord where unitId=?1  and acadyear = ?2 and semester=?3 and stuId = ?5 and projectId in ?4")
	List<DiathesisRecord> findListByUnitIdAndProjectIdInAndStuId(String unitId,String acadyear,Integer semester, List<String> projectIds, String studentId);

	@Query("From DiathesisRecord where unitId=?1 and acadyear = ?2 and semester=?3 and projectId in ?4 ")
	List<DiathesisRecord> findListByUnitIdAndProjectIdIn(String unitId, String acadyear, Integer semester, String[] projectIds);

	@Modifying
	@Query("delete from DiathesisRecord where id in ?1")
	void deleteByIds(String[] ids);

	@Query("From DiathesisRecord where unitId=?1 and projectId =?2 and acadyear = ?3 and semester=?4 and status in ?5 and stuId in ?6")
	List<DiathesisRecord> findListByProjectIdAndStuIdIn(String unitId, String projectId, String acadyear, Integer semester,String[] statu, String[] stuIds);

	@Query("From DiathesisRecord where unitId=?1 and acadyear =?2 and semester = ?3 and stuId=?4")
    List<DiathesisRecord> findListByAcadyearAndSemesterAndStuId(String unitId, String acadyear, Integer semester, String studentId);
}
