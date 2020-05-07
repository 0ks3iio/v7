package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;

public interface NewGkOpenSubjectService extends BaseService<NewGkOpenSubject, String>{

	void deleteByDivideId(String divideId);

	void saveAllEntity(List<NewGkOpenSubject> openSubjectList);

	List<NewGkOpenSubject> findByDivideId(String divideId);
	
	List<NewGkOpenSubject> findByDivideIdAndGroupType(String divideId, String groupType);

	List<NewGkOpenSubject> findByDivideIdAndSubjectTypeIn(String divideId, String[] subjectTypes);
	
	List<NewGkOpenSubject> findByDivideIdAndSubjectTypeInWithMaster(String divideId, String[] subjectTypes);

	List<NewGkOpenSubject> findByDivideIdIn(String[] divideIds);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subIds);
}
