package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.remote.service.UserRoleRemoteService;
import net.zdsoft.system.service.user.UserRoleService;

/**
 * created by shenke 2017/2/28 15:27
 */
@Service("userRoleRemoteService")
public class UserRoleRemoteServiceImpl extends BaseRemoteServiceImpl<UserRole, String> implements UserRoleRemoteService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    protected BaseService<UserRole, String> getBaseService() {
        return userRoleService;
    }

    @Override
    public String findByUserId(String userId) {
        return SUtils.s(userRoleService.findByUserId(userId));
    }
    
    @Override
	public void updateRemoteId(String id, String oldId) {
    	userRoleService.updateRemoteId(id,oldId);
	}

	@Override
	public String findByRoleIdAndUserIdIn(String roleId, String[] ids) {
		return SUtils.s(userRoleService.findByRoleIdAndUserIdIn(roleId,ids));
	}

	@Override
	public void deleteByRoleIdAndUserIdIn(String roleId, String[] ids) {
		userRoleService.deleteByRoleIdAndUserIdIn(roleId,ids);
	}
}
