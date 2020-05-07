package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;

public interface NewGkSubjectGroupColorDao extends BaseJpaRepositoryDao<NewGkSubjectGroupColor, String> {
	
	List<NewGkSubjectGroupColor> findByUnitId(String unitId);
	
	List<NewGkSubjectGroupColor> findByUnitIdAndGroupTypeIn(String unitId, String[] groupTypes);
}
