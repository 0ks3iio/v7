package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.WarningProjectUser;

import java.util.List;

/**
 * @author duhuachao
 * @date 2019/06/12
 */
public interface WarningProjectUserService extends BaseService<WarningProjectUser,String> {

    List<WarningProjectUser> findByProjectId(String id);

    void deleteByProjectId(String projectId);

    List<WarningProjectUser> findByUsersId(String ownerId);
}
