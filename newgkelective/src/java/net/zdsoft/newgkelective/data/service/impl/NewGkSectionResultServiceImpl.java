package net.zdsoft.newgkelective.data.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkSectionResultDao;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;
import net.zdsoft.newgkelective.data.service.NewGkSectionResultService;
@Service("newGkSectionResultService")
public class NewGkSectionResultServiceImpl extends BaseServiceImpl<NewGkSectionResult, String>  implements NewGkSectionResultService   {
	@Autowired
	private NewGkSectionResultDao  newGkSectionResultDao;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkSectionResult, String> getJpaDao() {
		return newGkSectionResultDao;
	}

	@Override
	protected Class<NewGkSectionResult> getEntityClass() {
		return NewGkSectionResult.class;
	}

	@Override
	public void deleteBySectionIdAndArrangeType(String sectionId, String arrangeType) {
		if(StringUtils.isNotBlank(arrangeType)) {
			newGkSectionResultDao.deleteBySectionIdAndArrangeType(sectionId,arrangeType);
		}else {
			newGkSectionResultDao.deleteBySectionId(sectionId);
		}
	}

	@Override
	public void deleteById(String[] ids) {
		newGkSectionResultDao.deleteByIdIn(ids);;
	}

}
