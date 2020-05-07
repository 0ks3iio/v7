package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;

public interface NewGkSectionEndService extends BaseService<NewGkSectionEnd, String> {

	public void saveNewGkSectionEndList(List<NewGkSectionEnd> endList);

	public void deleteBySectionId(String sectionId);
}
