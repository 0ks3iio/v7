package net.zdsoft.newgkelective.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkSectionBegin;

public interface NewGkSectionBeginService extends BaseService<NewGkSectionBegin, String> {

	void updateJoinBySectionId(String[] noJoinId, String sectionId);

	void deleteBySectionId(String sectionId);
}
