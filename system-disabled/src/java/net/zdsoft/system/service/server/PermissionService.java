package net.zdsoft.system.service.server;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.Permission;

public interface PermissionService extends BaseService<Permission, String> {

    List<Permission> saveAllEntitys(Permission... permission);

    void deleteAllByIds(String... id);

}
