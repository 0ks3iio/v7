package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;

public interface NewGKStudentRangeExDao extends BaseJpaRepositoryDao<NewGKStudentRangeEx, String>{

	List<NewGKStudentRangeEx> findByDivideId(String divideId);

	List<NewGKStudentRangeEx> findByDivideIdAndSubjectType(String divideId, String subjectType);

	@Modifying
	void deleteByDivideId(String divideId);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subIds);
    @Modifying
	void deleteByDivideIdAndSubjectType(String divideId, String subjectType);
}
