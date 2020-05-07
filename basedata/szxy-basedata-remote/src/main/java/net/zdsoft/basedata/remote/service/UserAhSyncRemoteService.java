package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.UserAhSync;

public interface UserAhSyncRemoteService extends BaseRemoteService<UserAhSync, String> {
	
	String findByObjectType(String objcetType);
	
	String findByObjectTypeAndAhUnitId(String objcetType,String ahUnitId);

	String findByAhObjectId(String ahUserId);

	String findByAhUnitId(String ahSyncUnitId);
	/**
	 * 根据eis的id查到对应的第三方id
	 * @param objectId
	 * @return
	 */
	String findByObjectId(String  objectId);
}
