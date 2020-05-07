package net.zdsoft.basedata.remote.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CustomRoleUserService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

@Service("customRoleUserRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CustomRoleUserRemoteServiceImpl extends BaseRemoteServiceImpl<CustomRoleUser,String> implements
        CustomRoleUserRemoteService {

    @Autowired
    private CustomRoleUserService customRoleUserService;

    @Override
    public boolean containRole(String userId, String roleCode) {
        return customRoleUserService.containRole(userId, roleCode);
    }

//    @Override
//    public void saveCustomRoleUsers(String[] userIds, String customRoleId) {
//        customRoleUserService.saveCustomRoleUsers(userIds, customRoleId);
//    }

    @Override
    protected BaseService<CustomRoleUser, String> getBaseService() {
        return customRoleUserService;
    }

	@Override
	public String findUserIdsByCustomRoleIdIn(String[] customRoleIds) {
		List<String> userIdsList=new ArrayList<String>();
		if(ArrayUtils.isNotEmpty(customRoleIds)) {
			List<CustomRoleUser> list = customRoleUserService.findListByIn("roleId", customRoleIds);
			if(CollectionUtils.isNotEmpty(list)) {
				Set<String> useIds = EntityUtils.getSet(list, "userId");
				userIdsList.addAll(useIds);
			}
		}
		return SUtils.s(userIdsList);
	}

	@Override
	public void deleteByRoleId(String roleId) {
		customRoleUserService.deleteByRoleId(roleId);
	}

}
