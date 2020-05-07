package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgHelp;

public interface BgHelpService extends BaseService<BgHelp, String>{

	/**
	 * 获取所有的帮助list
	 * @return
	 */
	public List<BgHelp> findAllHelpList();
	
	
	public List<BgHelp> findHelpListByModuleId(String moduleId);
	
	/**
	 * 根据是否核心模块获取帮助list
	 * @param core
	 * @return
	 */
	public List<BgHelp> findHelpListByCore(Integer core);
	
	/**
	 * 获取最大排序号
	 * @return
	 */
	 public Integer getMaxOrderId();
}

