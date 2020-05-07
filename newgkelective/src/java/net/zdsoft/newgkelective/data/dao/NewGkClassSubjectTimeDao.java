package net.zdsoft.newgkelective.data.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;

public interface NewGkClassSubjectTimeDao extends BaseJpaRepositoryDao<NewGkClassSubjectTime, String>{

	@Query("delete from NewGkClassSubjectTime where arrayItemId=?1")
	@Modifying
	void deleteByArrayItemId(String arrayItemId);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subjectIds);

    // Basedata Sync Method
    void deleteByClassIdIn(String... classIds);
}
