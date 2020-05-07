package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;

public interface NewGkLessonTimeDao extends BaseJpaRepositoryDao<NewGkLessonTime, String>{

	@Query("from NewGkLessonTime where objectType=?1 and arrayItemId in (?2) ")
	List<NewGkLessonTime> findByObjectTypeAndItemIn(String type,
			String[] itemIds);

	@Modifying
	@Query("delete from NewGkLessonTime where id in (?1) ")
	void deleteByIdIn(String[] ids);

	void deleteByArrayItemId(String arrayItemId);
}
