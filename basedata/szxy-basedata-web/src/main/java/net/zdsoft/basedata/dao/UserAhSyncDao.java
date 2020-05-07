package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.UserAhSync;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 */
public interface UserAhSyncDao extends BaseJpaRepositoryDao<UserAhSync, String> {

	List<UserAhSync> findByObjectTypeAndAhUnitId(String objcetType, String ahUnitId);

	List<UserAhSync> findByObjectType(String objcetType);

	List<UserAhSync> findByAhObjectId(String ahUserId);

	List<UserAhSync> findByAhUnitId(String ahSyncUnitId);

	List<UserAhSync> findByObjectId(String objectId);

}
