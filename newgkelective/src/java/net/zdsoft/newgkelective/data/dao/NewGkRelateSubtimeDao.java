package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;

public interface NewGkRelateSubtimeDao extends BaseJpaRepositoryDao<NewGkRelateSubtime, String>{

	public void deleteByItemId(String itemId);
	
	public List<NewGkRelateSubtime> findByItemId(String itemId);

	@Modifying
	@Query("delete from NewGkRelateSubtime where itemId =?1 and subjectIds like ?2 ")
	public void deleteLikeSubjectIdType(String gradeId, String subjectIdType);
}
