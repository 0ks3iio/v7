package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsRoleUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ChartsRoleUserDao extends BaseJpaRepositoryDao<ChartsRoleUser, String>{

	List<ChartsRoleUser> findByUserId(String userId);

}
