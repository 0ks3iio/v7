package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkSection;
import net.zdsoft.newgkelective.data.entity.NewGkSectionBegin;
import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;

public interface NewGkSectionService extends BaseService<NewGkSection, String> {

	public void saveSection(NewGkSection newGkSection, List<NewGkSectionBegin> beginList, List<NewGkSectionResult> resultList);

	public void updateSection(NewGkSection newGkSection, String[] noJoinId);
	
	/**
	 * 删除轮次下所有数据
	 * @param sectionId
	 */
	public void deleteBySectionId(String sectionId);

	/**
	 * 删除算法后产生的数据
	 * @param sectionId
	 */
	public void deleteResultBySectionId(String sectionId);

	public void updateSection(String[] delResultId, List<NewGkSectionBegin> beginList, List<NewGkSectionEnd> endList,
			List<NewGkSectionResult> resultList);

}
