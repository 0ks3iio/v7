package net.zdsoft.desktop.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.dao.UserSetDao;
import net.zdsoft.desktop.entity.UserSet;
import net.zdsoft.desktop.service.UserSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shenke on 2017/4/6.
 */
@Service("userSetService")
public class UserSetServiceImpl extends BaseServiceImpl<UserSet, String> implements UserSetService {

    @Autowired private UserSetDao userSetDao;

    @Override
    public UserSet findByUserId(String userId) {
        return userSetDao.findByUserId(userId);
    }

    @Override
    public String findLayoutByUserId(String userId) {
        UserSet userSet = findByUserId(userId);
        return userSet != null?userSet.getLayout():UserSet.LAYOUT_TWO2ONE;
    }

    @Override
    protected BaseJpaRepositoryDao<UserSet, String> getJpaDao() {
        return userSetDao;
    }

    @Override
    protected Class<UserSet> getEntityClass() {
        return getTClass();
    }
}
