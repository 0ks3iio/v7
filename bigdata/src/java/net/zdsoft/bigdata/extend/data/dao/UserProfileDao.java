package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * Created by wangdongdong on 2018/7/9 13:39.
 */
public interface UserProfileDao extends BaseJpaRepositoryDao<UserProfile, String> {

	UserProfile findByCode(String userProfileCode);
}
