package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.bigdata.system.entity.BgModule;


public interface BgAuthorityService {
	
	/**
	 * 获取用户有权限的模块列表
	 * @param userId
	 * @param userType
	 * @param isAdmin
	 * @return
	 */
	public List<BgModule> getAuthorityModuleList(String userId,String userType,boolean isAdmin);

}
