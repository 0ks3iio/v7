package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ChartsUserDao extends BaseJpaRepositoryDao<ChartsUser, String>{

	List<ChartsUser> findByUserIdIn(String... userId);

}
