package net.zdsoft.newgkelective.data.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;

public interface NewGkLessonTimeExDao extends BaseJpaRepositoryDao<NewGkLessonTimeEx, String>,NewGkLessonTimeExJdbcDao{

	void deleteByScourceTypeIdIn(String[] scourceTypeIds);

	@Query("delete from NewGkLessonTimeEx where arrayItemId=?1")
	@Modifying
	void deleteByArrayItemId(String arrayItemId);
	
	
	@Query("delete from NewGkLessonTimeEx where scourceTypeId in "
			+ "(select id from NewGkLessonTime where arrayItemId =?1 and objectType=?2) and timeType = ?3")
	@Modifying
	void deleteByItemIdAndType(String arrayItemId, String objectType, String timeType);
}
