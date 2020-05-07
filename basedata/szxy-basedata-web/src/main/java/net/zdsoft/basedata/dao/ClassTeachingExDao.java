package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ClassTeachingExDao extends BaseJpaRepositoryDao<ClassTeachingEx, String>{

	List<ClassTeachingEx> findByClassTeachingIdIn(String[] classTeachingIds);

	@Modifying
	@Query("delete from ClassTeachingEx where classTeachingId = ?1 and teacherId in (?2)")
	void deleteByClassTeachingIdAndTeacherIdIn(String classTeachingId,
			String[] teacherIds);
	
	@Modifying
	@Query("delete from ClassTeachingEx where classTeachingId in (?1)")
	void deleteByClassTeachingIdIn(String[] classTeachingId);

	@Modifying
	@Query("delete from ClassTeachingEx where teacherId= ?1")
	void deleteByTeacherIds(String... teacherIds);

	@Modifying
	@Query("delete from ClassTeachingEx c where exists(select 1 from ClassTeaching ct where ct.id=c.classTeachingId and ct.classId in (?1) )")
	void deleteByClassIds(String... classIds);

	@Modifying
	@Query("delete from ClassTeachingEx c where exists(select 1 from ClassTeaching ct where ct.id=c.classTeachingId and ct.subjectId in (?1) )")
	void deleteBySubjectIds(String... subjectIds);

}
