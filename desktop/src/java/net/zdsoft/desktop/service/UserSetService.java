package net.zdsoft.desktop.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.UserSet;

/**
 * Created by shenke on 2017/4/6.
 */
public interface UserSetService extends BaseService<UserSet, String> {

    UserSet findByUserId(String userId);

    /**
     * 根据userId查找，若userSet为null则返回默认值{@code UserSet.LAYOUT_DEFAULT}
     * @param userId
     * @return
     */
    String findLayoutByUserId(String userId);
}
