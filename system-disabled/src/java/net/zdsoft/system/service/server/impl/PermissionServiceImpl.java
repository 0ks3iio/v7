package net.zdsoft.system.service.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.PermissionDao;
import net.zdsoft.system.entity.Permission;
import net.zdsoft.system.service.server.PermissionService;

@Service("permissionService")
public class PermissionServiceImpl extends BaseServiceImpl<Permission, String> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    protected BaseJpaRepositoryDao<Permission, String> getJpaDao() {
        return permissionDao;
    }

    @Override
    protected Class<Permission> getEntityClass() {
        return Permission.class;
    }

    @Override
    public List<Permission> saveAllEntitys(Permission... permission) {
        return permissionDao.saveAll(checkSave(permission));
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0)
            permissionDao.deleteAllByIds(id);
    }

}
