package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.user.RolePermission;
import net.zdsoft.system.remote.service.RolePermissionRemoteService;
import net.zdsoft.system.service.user.RolePermissionService;

/**
 * created by shenke 2017/2/28 15:23
 */
@Service("rolePermissionRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class RolePermissionRemoteServiceImpl extends BaseRemoteServiceImpl<RolePermission, String> implements
        RolePermissionRemoteService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    protected BaseService<RolePermission, String> getBaseService() {

        return this.rolePermissionService;
    }
}
