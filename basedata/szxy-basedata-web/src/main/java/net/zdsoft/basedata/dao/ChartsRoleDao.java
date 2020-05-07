package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsRole;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ChartsRoleDao extends BaseJpaRepositoryDao<ChartsRole, String> {

    List<ChartsRole> findByIsUsingAndIdIn(int isUsing, Integer[] intId);

    List<ChartsRole> findByIsUsing(int isUsing);

}
