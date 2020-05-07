package net.zdsoft.desktop.dao;

import net.zdsoft.desktop.entity.UserApp;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author shenke
 * @since 2017.05.08
 */
public interface UserAppDao extends BaseJpaRepositoryDao<UserApp, String>{

    @Modifying
    @Query("delete from UserApp where userId=?1")
    void deleteByUserId(String userId);
}
