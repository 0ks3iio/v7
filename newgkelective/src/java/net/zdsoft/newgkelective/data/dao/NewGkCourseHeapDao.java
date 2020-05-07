package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkCourseHeap;

public interface NewGkCourseHeapDao extends BaseJpaRepositoryDao<NewGkCourseHeap, String>{

	void deleteByArrayId(String arrayId);

	List<NewGkCourseHeap> findByArrayId(String arrayId);

	List<NewGkCourseHeap> findByArrayIdAndSubjectId(String arrayId,
			String subjectId);

	NewGkCourseHeap findByArrayIdAndTimetableId(String arrayId,
			String timetibleId);

	List<NewGkCourseHeap> findByArrayIdAndTimetableIdIn(String arrayId,
			String[] array);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subIds);
}
