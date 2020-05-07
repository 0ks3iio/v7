package net.zdsoft.basedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SysUserBindDao;
import net.zdsoft.basedata.entity.SysUserBind;
import net.zdsoft.basedata.service.SysUserBindService;

@Service("sysUserBindService")
public class SysUserBindServiceImpl implements SysUserBindService {

	@Autowired
	private SysUserBindDao sysUserBindDao;
	
	@Override
	public void save(SysUserBind sysUserBind) {
		sysUserBindDao.save(sysUserBind);
	}

	@Override
	public void deleteByRemoteUserIdIn(String[] remoteUserIds) {
		sysUserBindDao.deleteByRemoteUserIdIn(remoteUserIds);
	}

	@Override
	public void deleteByUserIdIn(String[] userids) {
		sysUserBindDao.deleteByUserIdIn(userids);
	}

	@Override
	public SysUserBind getSysUserBindById(String remoteUserId) {
		return sysUserBindDao.findByRemoteUserId(remoteUserId);
	}

	@Override
	public SysUserBind findByRemoteUsername(String remoteUsername) {
		return sysUserBindDao.findByRemoteUsername(remoteUsername);
	}

	@Override
	public SysUserBind findByUserId(String userId) {
		return sysUserBindDao.findByUserId(userId);
	}

}
