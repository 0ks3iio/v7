package net.zdsoft.system.service.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.system.entity.server.Server;

public interface ServerService extends BaseService<Server, Integer> {

	List<Server> getServersByCodes(String[] codes);

	List<Server> findByOrderType(Integer[] orderTypes, int status);

	/**
	 * 根据应用id获取上线应用
	 *
	 * @author cuimq
	 * @param appIds
	 * @return
	 */
	Map<Integer, Server> getOnLineServerMap(Integer[] ids);

	/**
	 * 根据条件查询相关应用
	 *
	 * @author cuimq
	 * @param orderTypes 订阅类型
	 * @param unitClass  单位类型
	 * @param status     应用状态
	 * @param sections   学段，可为空
	 * @param userType   用户身份，为0是过滤身份条件
	 * @param serverIds  应用ID，可空
	 * @param isVisible  是否可见
	 *
	 * @return
	 */
	List<Server> findServerList(final Integer[] orderTypes, final Integer unitClass, final int status,
			final String sections, final int userType, final Integer[] serverIds, final Integer isVisible,
			final String subIdCondition);

	/**
	 * 根据订阅类型和应用状态查找应用
	 *
	 * @author cuimq
	 * @param orderTypes
	 * @param status
	 * @return
	 */
	List<Server> findByOrderTypeAndStatus(Integer[] orderTypes, int status);

	/**
	 * 获取所有应用
	 *
	 * @author wulinhao
	 * @return
	 */
	List<Server> getAllApps(final String appName, final Integer status, final Integer source, final Date startTime,
			final Date endTime, Pagination page);

	/**
	 * 根据开发者ID获取对应的应用名称
	 *
	 * @param devIds
	 * @return
	 */
	Map<String, List<String>> getAppNamesByDevIds(String[] devIds);

	List<Server> findAppsByDevIds(String[] devIds);

	/**
	 * 根据开发者ID获取应用信息
	 *
	 * @param devId
	 * @return
	 */
	List<Server> getAppsByDevId(String devId);

	/**
	 * 删除应用
	 *
	 * @param systemId
	 */
	void removeApp(String systemId);

	/**
	 * 删除应用
	 *
	 * @param appId
	 */
	void removeAppByAppId(int appId);

	/**
	 * 获取应用
	 *
	 * @param systemId
	 * @return
	 */
	Server getAppBySystemId(String systemId);

	/**
	 * 获取应用
	 *
	 * @param appId
	 * @return
	 */
	Server getAppByAppId(int appId);

	/**
	 * 更改应用状态
	 *
	 * @param systemId
	 */
	void updateAppStatusBySystemId(int status, String systemId);

	/**
	 * 更改应用状态
	 *
	 * @param status
	 * @param appId
	 */
	void updateAppStatusByAppId(int status, int appId);

	/**
	 * 更新应用通过时的状态
	 *
	 * @param server
	 */
	// void updateAppPassStatus(Server server);

	/**
	 * 根据应用名判断应用是否存在
	 *
	 * @param appName
	 * @return
	 */
	int getAppCountByName(String appName);

	/**
	 * 添加应用
	 *
	 * @param server
	 */
	Server addApp(Server server);

	/**
	 * 更新应用信息
	 *
	 * @param server
	 */
	void updateAppInfoForOpenapi(Server server);

	/**
	 * 更新应用信息
	 *
	 * @param server
	 */
	void updateAppInfoByAppId(Server server);

	/**
	 * 更新当前启用的应用
	 *
	 * @author dingw
	 * @param codes 序列号关联的应用code列表
	 */
	void updateEnableServer(String[] codes);

	/**
	 * 更新应用且注册passPort
	 *
	 * @param server
	 */
	void updateAppAndRegisterPassPort(Server server);

	/**
	 * 新增应用且注册passPort
	 *
	 * @param server
	 */
	Server addAppAndRegisterPassPort(Server server);

	/**
	 * deploy 使用，会级联更新passport sys_server
	 * 
	 * @param server
	 */
	void updateInnerAp(Server server);

	/**
	 * 缓存5分钟
	 * @param subId
	 * @return
	 */
	Server findBySubId(Integer subId);
	
	List<Server> findBySubIds(Integer[] subIds);

	Server findByIndexUrl(String url);
}
