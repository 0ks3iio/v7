package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachClassExDao extends BaseJpaRepositoryDao<TeachClassEx, String>{
	
	@Query(" from TeachClassEx where teachClassId in (?1) order by dayOfWeek,periodInterval,period")
	List<TeachClassEx> findByClassId(String[] classIds);

	@Modifying
	@Query("delete TeachClassEx where teachClassId in (?1) ")
	void deleteByTeachClassId(String[] teachClassId);

	@Modifying
	@Query("delete TeachClassEx ex where exists(select 1 from TeachClass tc where tc.id=ex.teachClassId and tc.gradeId in (?1))")
	void deleteByGradeIds(String... gradeIds);

	@Modifying
	@Query("delete TeachClassEx ex where exists(select 1 from TeachClass tc where tc.id=ex.teachClassId and tc.courseId in (?1))")
	void deleteBySubjectIds(String... subjectIds);

}
