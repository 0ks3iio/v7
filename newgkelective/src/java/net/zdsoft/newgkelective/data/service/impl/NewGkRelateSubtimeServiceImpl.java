package net.zdsoft.newgkelective.data.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkRelateSubtimeDao;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.service.NewGkRelateSubtimeService;

@Service("newGkRelateSubtimeService")
public class NewGkRelateSubtimeServiceImpl extends BaseServiceImpl<NewGkRelateSubtime, String> implements NewGkRelateSubtimeService{

	@Autowired
	private NewGkRelateSubtimeDao newGkRelateSubtimeDao;
	@Override
	protected BaseJpaRepositoryDao<NewGkRelateSubtime, String> getJpaDao() {
		return newGkRelateSubtimeDao;
	}

	@Override
	protected Class<NewGkRelateSubtime> getEntityClass() {
		return NewGkRelateSubtime.class;
	}

	@Override
	public void saveAllNewGkRelateSubtime(String itemId, List<NewGkRelateSubtime> subtimeList) {
		newGkRelateSubtimeDao.deleteByItemId(itemId);
		if(CollectionUtils.isNotEmpty(subtimeList)) {
			newGkRelateSubtimeDao.saveAll(checkSave(subtimeList.toArray(new NewGkRelateSubtime[] {})));
		}
		
	}

	@Override
	public List<NewGkRelateSubtime> findListByItemId(String itemId) {
		return newGkRelateSubtimeDao.findByItemId(itemId);
	}

	@Override
	public void deletedByItemId(String itemId) {
		newGkRelateSubtimeDao.deleteByItemId(itemId);
	}

	@Override
	public void deleteLikeSubjectIdType(String gradeId, String subjectIdType) {
		if(StringUtils.isNotBlank(subjectIdType)) {
			newGkRelateSubtimeDao.deleteLikeSubjectIdType(gradeId,"%"+subjectIdType+"%");
		}
	}
}
