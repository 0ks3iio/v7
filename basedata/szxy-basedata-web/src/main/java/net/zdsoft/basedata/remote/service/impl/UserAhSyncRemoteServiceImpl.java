package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.UserAhSync;
import net.zdsoft.basedata.remote.service.UserAhSyncRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.UserAhSyncService;
import net.zdsoft.framework.utils.SUtils;

@Service("userAhSyncRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class UserAhSyncRemoteServiceImpl extends BaseRemoteServiceImpl<UserAhSync, String> implements UserAhSyncRemoteService  {
	
	@Autowired
    private UserAhSyncService userAhSyncService;
	
	@Override
	public String findByObjectType(String objcetType) {
		return SUtils.s(userAhSyncService.findByObjectType(objcetType));
	}

	@Override
	public String findByObjectTypeAndAhUnitId(String objcetType, String ahUnitId) {
		// TODO Auto-generated method stub
		return SUtils.s(userAhSyncService.findByObjectTypeAndAhUnitId(objcetType,ahUnitId));
	}

	@Override
	public String findByAhObjectId(String ahUserId) {
		return SUtils.s(userAhSyncService.findByAhObjectId(ahUserId));
	}

	@Override
	public String findByAhUnitId(String ahSyncUnitId) {
		// TODO Auto-generated method stub
		return SUtils.s(userAhSyncService.findByAhUnitId(ahSyncUnitId));
	}
	@Override
	public String findByObjectId(String objectId) {
		// TODO Auto-generated method stub
		return SUtils.s(userAhSyncService.findByObjectId(objectId));
	}
	@Override
	protected BaseService<UserAhSync, String> getBaseService() {
		return userAhSyncService;
	}

}
