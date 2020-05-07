package net.zdsoft.newgkelective.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;

public interface NewGkSectionResultDao extends BaseJpaRepositoryDao<NewGkSectionResult, String> {

	void deleteBySectionIdAndArrangeType(String sectionId, String arrangeType);

	void deleteBySectionId(String sectionId);

	void deleteByIdIn(String[] ids);
}
