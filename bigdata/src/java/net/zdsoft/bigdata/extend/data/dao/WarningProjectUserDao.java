package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.WarningProjectUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * @author duhuachao
 * @date 2019/6/12
 */
public interface WarningProjectUserDao extends BaseJpaRepositoryDao<WarningProjectUser,String> {

    List<WarningProjectUser> findByProjectId(String id);

    @Modifying
    void deleteByProjectId(String projectId);

    List<WarningProjectUser> findByUsersId(String ownerId);
}
