package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroup;

public interface NewGkTeacherGroupDao extends BaseJpaRepositoryDao<NewGkTeacherGroup, String> {
	
	List<NewGkTeacherGroup> findByObjectId(String objectId);
	
	void deleteByIdIn(String[] ids);
}
