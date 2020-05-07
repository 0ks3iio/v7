package net.zdsoft.newgkelective.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;

public interface NewGkSectionEndDao extends BaseJpaRepositoryDao<NewGkSectionEnd, String> {

	void deleteBySectionId(String sectionId);
}
