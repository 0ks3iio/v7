package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.UserAhSync;

public interface UserAhSyncService extends BaseService<UserAhSync, String> {

	List<UserAhSync> findByObjectType(String objcetType);
	
	List<UserAhSync> findByObjectTypeAndAhUnitId(String objcetType,String ahUnitId);

	List<UserAhSync> findByAhObjectId(String ahUserId);

	List<UserAhSync> findByAhUnitId(String ahSyncUnitId);

	List<UserAhSync> findByObjectId(String objectId);
	
}
