package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ClassHourDao extends BaseJpaRepositoryDao<ClassHour, String>{


	@Query("From ClassHour where acadyear=?1 and semester=?2 and unit_id=?3 and isDeleted=0 ")
	List<ClassHour> findListByUnitId(String acadyear, String semester, String unitId);
	@Query("From ClassHour where acadyear=?1 and semester=?2 and unit_id=?3 and grade_id=?4  and isDeleted=0 ")
	List<ClassHour> findListByGraded(String acadyear, String semester, String unitId, String gradeId);


	@Query("from ClassHour where acadyear=?1 and semester=?2 and unit_id=?3 and grade_id=?4 and subjectId in (?5) and isDeleted=0 ")
	List<ClassHour> findBySubjectIds(String acadyear, String semester,String unitId, String gradeId,String[] subjectIds);

	@Modifying
	@Query("update ClassHour set isDeleted=1,modifyTime=sysdate where id in (?1) ")
	public void deleteUpdateByIdIn(String[] id);
}
