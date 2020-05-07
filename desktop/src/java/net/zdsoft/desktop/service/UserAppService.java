package net.zdsoft.desktop.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.UserApp;

import java.util.List;

/**
 * @author shenke
 * @since 2017.05.08
 */
public interface UserAppService extends BaseService<UserApp, String> {

    void updateUserApps(List<UserApp> updateUserApps, List<UserApp> rmfUserApps);

    void deleteByUserId(String userId);
}
