package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.service.user.RoleService;

@Service("roleRemoteService")
public class RoleRemoteServiceImpl extends BaseRemoteServiceImpl<Role, String> implements RoleRemoteService {

    @Autowired
    private RoleService roleService;

    @Override
    protected BaseService<Role, String> getBaseService() {
        return roleService;
    }

    @Override
    public String findByUnitId(String unitId) {
        return SUtils.s(roleService.findByUnitId(unitId));
    }

    @Override
    public String saveAllEntitys(String entitys) {
        Role[] dt = SUtils.dt(entitys, new TR<Role[]>() {
        });
        return SUtils.s(roleService.saveAllEntitys(dt));
    }

	@Override
	public String findByUnitIdAndRoleType(String unitId, int roleTypeOper) {
		return SUtils.s(roleService.findByUnitIdAndRoleType(unitId,roleTypeOper));
	}

}
