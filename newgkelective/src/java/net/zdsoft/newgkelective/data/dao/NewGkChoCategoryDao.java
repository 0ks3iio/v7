package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;

public interface NewGkChoCategoryDao extends BaseJpaRepositoryDao<NewGkChoCategory, String>{
	void deleteByIdIn(String[] ids);

	@Query("from NewGkChoCategory where unitId= ?1 and choiceId= ?2 and isDeleted=0 order by orderId")
	List<NewGkChoCategory> findByUnitIdAndChoiceId(String unitId, String choiceId);
	
}
