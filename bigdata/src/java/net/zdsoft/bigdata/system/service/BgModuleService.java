package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgModule;

public interface BgModuleService extends BaseService<BgModule, String>{

	/**
	 * 获取所有的模块
	 * @return
	 */
	public List<BgModule> findAllModuleList();
	
	/**
	 * 根据parentId获取模块
	 * @param parentId
	 * @return
	 */
	public List<BgModule> findModuleListByParentId(String parentId);

	/**
	 * 更新mark值
	 * @param id
	 * @param mark
	 */
	public void updateMarkById(String id, Integer mark);
}
