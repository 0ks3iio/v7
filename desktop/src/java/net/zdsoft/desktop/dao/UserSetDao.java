package net.zdsoft.desktop.dao;

import net.zdsoft.desktop.entity.UserSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * Created by shenke on 2017/4/6.
 */
public interface UserSetDao extends BaseJpaRepositoryDao<UserSet, String> {

    UserSet findByUserId(String userId);
}
