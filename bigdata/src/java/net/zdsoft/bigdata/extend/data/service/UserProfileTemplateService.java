package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.UserProfileTemplate;

public interface UserProfileTemplateService extends BaseService<UserProfileTemplate, String> {
	public List<UserProfileTemplate> findByUserProfileCode(String userProfileCode);
	
	public void deleteBytagId(String tagId);
}
