package net.zdsoft.desktop.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.dao.UserAppDao;
import net.zdsoft.desktop.entity.UserApp;
import net.zdsoft.desktop.service.UserAppService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenke
 * @since 2017.05.08
 */
@Service("userAppService")
public class UserAppServiceImpl extends BaseServiceImpl<UserApp, String> implements UserAppService {

    @Autowired private UserAppDao userAppDao;

    @Override
    protected BaseJpaRepositoryDao<UserApp, String> getJpaDao() {
        return this.userAppDao;
    }

    @Override
    protected Class<UserApp> getEntityClass() {
        return getTClass();
    }

    @Override
    public void updateUserApps(List<UserApp> updateUserApps, List<UserApp> rmfUserApps) {
        deleteAll(EntityUtils.toArray(rmfUserApps, UserApp.class));
        saveAll(EntityUtils.toArray(updateUserApps, UserApp.class));
    }

    @Override
    public void deleteByUserId(String userId) {
        userAppDao.deleteByUserId(userId);
    }
}
