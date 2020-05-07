package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsRoleLab;

public interface ChartsRoleLabService {

	/**
	 * 获取角色的图表
	 * 数据库
	 * @param chartsRoleId
	 * @return
	 */
	List<ChartsRoleLab> findByChartsRoleIdIn(Integer... chartsRoleId);

}
