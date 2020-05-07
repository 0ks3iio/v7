package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccStuLeaveInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccStuLeaveInfoDao extends BaseJpaRepositoryDao<EccStuLeaveInfo,String>{

	public List<EccStuLeaveInfo> findByStuDormAttIdIn(String[] stuDormAttIds);

	public List<EccStuLeaveInfo> findByLeaveIdIn(String[] leaveIds);

}
