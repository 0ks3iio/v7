package net.zdsoft.power.remote.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.power.entity.SysApRole;
import net.zdsoft.power.remote.service.SysApRoleRemoteService;
import net.zdsoft.power.service.SysApRoleService;

/**
 * @author yangsj  2018年6月21日上午9:33:50
 */
@Service("sysApRoleRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SysApRoleRemoteServiceImpl extends BaseRemoteServiceImpl<SysApRole, String> implements SysApRoleRemoteService{

	@Autowired
	private SysApRoleService sysApRoleService;
	
	@Override
	protected BaseService<SysApRole, String> getBaseService() {
		return sysApRoleService;
	}

	@Override
	public String findByServerIdIn(Integer[] serverIds) {
		List<SysApRole> sysApRoles = sysApRoleService.findByServerIdIn(serverIds);
		return SUtils.s(sysApRoles);
	}
	
	

}
