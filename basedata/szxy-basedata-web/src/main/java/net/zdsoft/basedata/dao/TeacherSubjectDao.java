package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeacherSubject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeacherSubjectDao extends BaseJpaRepositoryDao<TeacherSubject, String>{

	@Query("From TeacherSubject where isDeleted=0 and  teacherId in (?1) ")
	List<TeacherSubject> findListByTeacherIdIn(String[] teacherIds);

}
