package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;

public interface NewGKStudentRangeExService extends BaseService<NewGKStudentRangeEx, String>{

	List<NewGKStudentRangeEx> findByDivideIdAndSubjectType(String divideId, String subjectType);

	public List<NewGKStudentRangeEx> findByDivideId(String divideId);

	void saveAndDelete(List<NewGKStudentRangeEx> exList, String divideId);

	void saveAndDelete(List<NewGKStudentRangeEx> exList, String subjectType, String divideId);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subIds);

	void deleteByDivideIdAndSubjectType(String divideId, String subjectType);
}
