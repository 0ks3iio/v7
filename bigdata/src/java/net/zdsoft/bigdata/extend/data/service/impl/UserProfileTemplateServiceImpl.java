package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.UserProfileTemplateDao;
import net.zdsoft.bigdata.extend.data.entity.UserProfileTemplate;
import net.zdsoft.bigdata.extend.data.service.UserProfileTemplateService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userProfileTemplateService")
public class UserProfileTemplateServiceImpl extends BaseServiceImpl<UserProfileTemplate, String>
		implements UserProfileTemplateService {

	@Autowired
	private UserProfileTemplateDao userProfileTemplateDao;
	@Override
	protected BaseJpaRepositoryDao<UserProfileTemplate, String> getJpaDao() {
		return userProfileTemplateDao;
	}

	@Override
	protected Class<UserProfileTemplate> getEntityClass() {
		return UserProfileTemplate.class;
	}

	@Override
	public List<UserProfileTemplate> findByUserProfileCode(
			String userProfileCode) {
		return userProfileTemplateDao.findByProfileCode(userProfileCode);
	}

	@Override
	public void deleteBytagId(String tagId) {
		userProfileTemplateDao.deleteBytagId(tagId);
	}


}
