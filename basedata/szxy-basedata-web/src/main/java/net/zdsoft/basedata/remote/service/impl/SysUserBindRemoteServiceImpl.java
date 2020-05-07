package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.SysUserBind;
import net.zdsoft.basedata.remote.service.SysUserBindRemoteService;
import net.zdsoft.basedata.service.SysUserBindService;
import net.zdsoft.framework.utils.SUtils;

@Service("sysUserBindRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SysUserBindRemoteServiceImpl implements SysUserBindRemoteService {

	@Autowired
	private SysUserBindService sysUserBindService;
	
	@Override
	public void save(SysUserBind sysUserBind) {
		sysUserBindService.save(sysUserBind);
	}

	@Override
	public void deleteByRemoteUserIdIn(String[] remoteUserIds) {
		sysUserBindService.deleteByRemoteUserIdIn(remoteUserIds);
	}

	@Override
	public void deleteByUserIdIn(String[] userids) {
		sysUserBindService.deleteByUserIdIn(userids);
	}

	@Override
	public String getSysUserBindById(String remoteUserId) {
		return SUtils.s(sysUserBindService.getSysUserBindById(remoteUserId));
	}

	@Override
	public String findByRemoteUsername(String remoteUsername) {
		return SUtils.s(sysUserBindService.findByRemoteUsername(remoteUsername));
	}

	@Override
	public String findByUserId(String userId) {
		return SUtils.s(sysUserBindService.findByUserId(userId));
	}

}
