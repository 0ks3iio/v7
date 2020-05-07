package net.zdsoft.newgkelective.data.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkSectionEndDao;
import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;
import net.zdsoft.newgkelective.data.service.NewGkSectionEndService;
@Service("newGkSectionEndService")
public class NewGkSectionEndServiceImpl extends BaseServiceImpl<NewGkSectionEnd, String>
	implements NewGkSectionEndService{
	@Autowired
	private NewGkSectionEndDao newGkSectionEndDao;

	@Override
	protected BaseJpaRepositoryDao<NewGkSectionEnd, String> getJpaDao() {
		return newGkSectionEndDao;
	}

	@Override
	protected Class<NewGkSectionEnd> getEntityClass() {
		return NewGkSectionEnd.class;
	}

	@Override
	public void saveNewGkSectionEndList(List<NewGkSectionEnd> endList) {
		if(CollectionUtils.isNotEmpty(endList)) {
			saveAll(endList.toArray(new NewGkSectionEnd[] {}));
		}
		
	}

	@Override
	public void deleteBySectionId(String sectionId) {
		newGkSectionEndDao.deleteBySectionId(sectionId);
	}

}
