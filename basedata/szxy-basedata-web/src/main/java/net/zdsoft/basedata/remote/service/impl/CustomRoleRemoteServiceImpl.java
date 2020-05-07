package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CustomRoleService;
import net.zdsoft.framework.utils.SUtils;

@Service("customRoleRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CustomRoleRemoteServiceImpl extends BaseRemoteServiceImpl<CustomRole,String> implements CustomRoleRemoteService {

    @Autowired
    private CustomRoleService customRoleService;

    // @Autowired
    // private CustomRoleDao customRoleDao;

    @Override
    protected BaseService<CustomRole, String> getBaseService() {
        return customRoleService;
    }

    /*
     * @Override public void deleteByUnitId(String unitId) { // TODO Auto-generated method stub
     * customRoleDao.deleteByUnitId(unitId); }
     */

    public String  findListByUnitAndSubsystem(String unitId,String subsystem) {
    	return SUtils.s(customRoleService.findByUnitIdAndSubsystem(unitId, subsystem, false));
    }

	@Override
	public boolean checkUserRole(String unitId, String subsystem, String roleCode,String userId) {
		return customRoleService.checkUserRole(unitId,subsystem,roleCode,userId);
	}

    @Override
    public void deleteById(String id) {
        customRoleService.deleteById(id);
    }
}
