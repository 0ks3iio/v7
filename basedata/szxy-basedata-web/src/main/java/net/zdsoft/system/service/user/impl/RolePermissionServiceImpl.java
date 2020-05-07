package net.zdsoft.system.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.user.RolePermissionDao;
import net.zdsoft.system.entity.user.RolePermission;
import net.zdsoft.system.service.user.RolePermissionService;

@Service("rolePermissionService")
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission, String> implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermDao;

    @Override
    protected BaseJpaRepositoryDao<RolePermission, String> getJpaDao() {
        return rolePermDao;
    }

    @Override
    protected Class<RolePermission> getEntityClass() {
        return RolePermission.class;
    }
}
