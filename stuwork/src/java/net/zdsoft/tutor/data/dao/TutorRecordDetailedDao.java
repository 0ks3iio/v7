package net.zdsoft.tutor.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorRecordDetailed;

/**
 * @author yangsj  2017年11月20日上午10:17:11
 */
public interface TutorRecordDetailedDao extends BaseJpaRepositoryDao<TutorRecordDetailed,String>{
    
	String SQL_AFTER=" Order By creationTime desc";
	
	@Query("From TutorRecordDetailed  Where unitId = ?1 and acadyear = ?2 and semester = ?3 " + SQL_AFTER)
	List<TutorRecordDetailed> findBySIdAndSemester(String uid, String acadyear, String semester);

	/**
	 * @param teacherIds
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	@Query("From TutorRecordDetailed  Where teacherId in (?1) and acadyear = ?2 and semester = ?3 ")
	List<TutorRecordDetailed> findByTIdsAndSemester(String[] teacherIds, String acadyear, String semester);
	
	
}
