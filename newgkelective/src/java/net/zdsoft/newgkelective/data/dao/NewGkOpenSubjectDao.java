package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;

public interface NewGkOpenSubjectDao extends BaseJpaRepositoryDao<NewGkOpenSubject, String>{
	
    void deleteByDivideId(String divideId);

	List<NewGkOpenSubject> findByDivideId(String divideId);
	
	List<NewGkOpenSubject> findByDivideIdAndGroupType(String divideId, String groupType);

	List<NewGkOpenSubject> findByDivideIdAndSubjectTypeIn(String divideId, String[] subjectTypes);

	@Query("From NewGkOpenSubject where divideId in (?1)")
	List<NewGkOpenSubject> findByDivideIdIn(String[] divideIds);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subids);
}
