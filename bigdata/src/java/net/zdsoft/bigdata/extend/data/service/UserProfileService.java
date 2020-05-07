package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;

/**
 * Created by wangdongdong on 2018/7/9 13:35.
 */
public interface UserProfileService extends BaseService<UserProfile, String> {
	
	List<MetadataTableColumn> findMainCols(String userProfileCode);
}
