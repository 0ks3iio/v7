package net.zdsoft.system.remote.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.system.entity.server.Server;

public interface ServerRemoteService extends BaseRemoteService<Server, Integer> {

	
	/**
	 * 根据订阅类型和应用状态查找应用
	 *
	 * @author cuimq
	 * @param orderTypes
	 * @param status
	 * @return
	 */
	List<Server> findByOrderTypeAndStatus(Integer[] orderTypes, int status);
	
	List<Server> findServerList(final Integer[] orderTypes, final Integer unitClass, final int status,
			final String sections, final int userType, final Integer[] serverIds, final Integer isVisible,
			final String subIdCondition);
	
	/**
	 * 根据应用名判断应用是否存在
	 *
	 * @param appName
	 * @return
	 */
	int getAppCountByName(String appName);

	/**
	 * 获取所有应用
	 *
	 * @author wulinhao
	 * @return
	 */
	List<Server> getAllApps(final String appName, final Integer status, final Integer source, final Date startTime,
			final Date endTime, Pagination page);

	/**
	 * 新增应用且注册passPort
	 *
	 * @param server
	 */
	Server addAppAndRegisterPassPort(Server server);

	/**
	 * 更新应用信息
	 *
	 * @param server
	 */
	void updateAppInfoByAppId(Server server);

	/**
	 * 更新应用且注册passPort
	 *
	 * @param server
	 */
	void updateAppAndRegisterPassPort(Server server);

	/**
	 * 删除应用
	 *
	 * @param appId
	 */
	void removeAppByAppId(int appId);

	/**
	 * @param serverCode
	 * @return
	 */
	String findByServerCode(String serverCode);

	/*
	 * * 更改应用状态
	 *
	 * @param status
	 * 
	 * @param appId
	 */
	void updateAppStatusByAppId(int status, int appId);

	/**
	 * 根据开发者ID获取应用信息
	 * 
	 * @param devId
	 * @return
	 */
	String getAppsByDevId(String devId);

	List<Server> findAppsByDevIds(String[] devIds);

	/**
	 * 获取应用
	 * 
	 * @param appId
	 * @return
	 */
	Server getAppByAppId(int appId);

	/**
	 * 查找推荐应用列表
	 * 
	 * @author cuimq
	 * @param ownerType
	 * @param unitId
	 * @param unitClass
	 * @return
	 */
	String findByOwnerTypeAndUnitIdAndUnitClass(Integer ownerType, String unitId, Integer unitClass);

	/**
	 * 查找没有模块的子系统
	 * 
	 * @return
	 */
	String findNonModelsServer();

	/**
	 * 统计应用免费订阅数
	 * 
	 * @param serverId
	 * @return
	 */
	int countFreeOrderNum(Integer serverId, Integer[] userTypes);

	String findBySubId(Integer subId);
	
	String findBySubIds(Integer[] subIds);

	String findByIndexUrl(String url);

}
