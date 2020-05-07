package net.zdsoft.newgkelective.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkSectionBegin;

public interface NewGkSectionBeginDao extends BaseJpaRepositoryDao<NewGkSectionBegin, String> {

	void deleteBySectionId(String sectionId);
}
