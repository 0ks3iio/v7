package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTeacher;

public interface NewGkSubjectTeacherDao extends BaseJpaRepositoryDao<NewGkSubjectTeacher, String>{

	List<NewGkSubjectTeacher> findByArrayId(String arrayId);

	void deleteByArrayId(String arrayId);

	List<NewGkSubjectTeacher> findByArrayIdAndSubjectId(String arrayId,
			String subjectId);

    // Basedata Sync Method
    void deleteByTeacherIdIn(String... teacherIds);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subjectids);
}
