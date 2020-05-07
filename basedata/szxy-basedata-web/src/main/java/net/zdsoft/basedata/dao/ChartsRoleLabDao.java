package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsRoleLab;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ChartsRoleLabDao extends BaseJpaRepositoryDao<ChartsRoleLab, String>{

	List<ChartsRoleLab> findByChartsRoleIdIn(Integer... chartsRoleId);

}
