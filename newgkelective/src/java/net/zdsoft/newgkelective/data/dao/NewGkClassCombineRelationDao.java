package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;

public interface NewGkClassCombineRelationDao extends BaseJpaRepositoryDao<NewGkClassCombineRelation, String> {
	
	public List<NewGkClassCombineRelation> findByUnitIdAndArrayItemId(String unitId, String arrayItemId);

	@Modifying
	@Query("delete from NewGkClassCombineRelation where arrayItemId=?1")
	public void deleteByArrayItemId(String arrayItemId);

    void deleteByClassSubjectIdsLike(String subjectId);
}
