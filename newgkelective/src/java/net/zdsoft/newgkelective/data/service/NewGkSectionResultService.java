package net.zdsoft.newgkelective.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;

public interface NewGkSectionResultService extends BaseService<NewGkSectionResult, String> {


	void deleteBySectionIdAndArrangeType(String sectionId, String arrangeType);

	void deleteById(String[] ids);
}
