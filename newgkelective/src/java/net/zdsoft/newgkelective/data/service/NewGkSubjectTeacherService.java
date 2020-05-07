package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTeacher;

public interface NewGkSubjectTeacherService extends BaseService<NewGkSubjectTeacher, String>{

	List<NewGkSubjectTeacher> findByArrayId(String arrayId);

	void deleteAndSave(String arrayId,
			List<NewGkSubjectTeacher> newGkSubjectTeacherList);

	List<NewGkSubjectTeacher> findByArrayIdAndSubjectId(String arrayId,
			String subjectId);

    // Basedata Sync Method
    void deleteByTeacherIds(String... teacherids);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subjectIds);
}
