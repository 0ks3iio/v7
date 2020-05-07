package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkCourseHeap;

public interface NewGkCourseHeapService extends BaseService<NewGkCourseHeap, String>{

	void saveOrDel(String arrayId, List<NewGkCourseHeap> insertList);

	List<NewGkCourseHeap> findByArrayId(String arrayId);

	List<NewGkCourseHeap> findByArrayIdAndSubjectId(String arrayId,
			String subjectId);

	NewGkCourseHeap findByArrayIdAndTimetableId(String arrayId,
			String timetibleId);

	List<NewGkCourseHeap> findByArrayIdAndTimetableIdIn(String arrayId,
			String[] array);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subIds);
}
