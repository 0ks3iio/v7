package net.zdsoft.system.remote.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.enums.UnitClassEnum;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.dao.server.ServerDao;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.OrderTypeEnum;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.service.server.ServerAuthorizeService;
import net.zdsoft.system.service.server.ServerService;

@Service("serverRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class ServerRemoteServiceImpl extends BaseRemoteServiceImpl<Server, Integer> implements ServerRemoteService {

	@Autowired
	private ServerService serverService;
	@Autowired
	private ServerDao serverDao;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ServerAuthorizeService serverAuthorizeService;
	@Autowired
	private UserRemoteService userRmoteService;
	
	
	@Override
	public List<Server> findByOrderTypeAndStatus(Integer[] orderTypes, int status) {
		return serverService.findByOrderType(orderTypes, status);
	}

	@Override
	public List<Server> findServerList(Integer[] orderTypes, Integer unitClass, int status, String sections,
			int userType, Integer[] serverIds, Integer isVisible, String subIdCondition) {
		return serverService.findServerList(orderTypes, unitClass, status, sections, userType, serverIds, isVisible, subIdCondition);
	}

	@Override
	public int getAppCountByName(String appName) {
		return serverService.getAppCountByName(appName);
	}

	@Override
	public List<Server> getAllApps(String appName, Integer status, Integer source, Date startTime, Date endTime,
			Pagination page) {
		return serverService.getAllApps(appName, status, source, startTime, endTime, page);
	}

	@Override
	public Server addAppAndRegisterPassPort(Server server) {
		return serverService.addAppAndRegisterPassPort(server);
	}

	@Override
	public void updateAppInfoByAppId(Server server) {
		serverService.updateAppInfoByAppId(server);
	}

	@Override
	public void updateAppAndRegisterPassPort(Server server) {
		serverService.updateAppAndRegisterPassPort(server);
	}

	@Override
	public List<Server> findAppsByDevIds(String[] devIds) {
		return serverService.findAppsByDevIds(devIds);
	}

	@Override
	protected BaseService<Server, Integer> getBaseService() {
		return serverService;
	}

	@Override
	public String findByServerCode(String serverCode) {
		return SUtils.s(serverDao.findByServerCode(serverCode));
	}

	@Override
	public void removeAppByAppId(int appId) {
		serverService.removeAppByAppId(appId);
	}

	@Override
	public void updateAppStatusByAppId(int status, int appId) {
		serverService.updateAppStatusByAppId(status, appId);
	}

	@Override
	public String findByOwnerTypeAndUnitIdAndUnitClass(Integer ownerType, String unitId, Integer unitClass) {
		List<Server> serverList = new ArrayList<Server>();

		String sections = "";
		if (null != unitClass && unitClass == UnitClassEnum.SCHOOL.getValue()) {
			sections = schoolRemoteService.findSectionsById(unitId);
		}
		// 系统订阅无需授权
		serverList.addAll(serverService.findServerList(new Integer[] { OrderTypeEnum.SYSTEM_NO_AUTH.getValue() },
				unitClass, AppStatusEnum.ONLINE.getValue(), sections, ownerType, null, null, "isNull"));

		// 单位订阅无需授权应用
		serverList.addAll(serverAuthorizeService.listServers(unitId, unitClass, ownerType,
				new Integer[] { OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue() }, sections, null, "isNull"));

		return SUtils.s(serverList);
	}

	@Override
	public String findNonModelsServer() {
		return SUtils.s(serverDao.findNonModelsServer());
	}

	@Override
	public int countFreeOrderNum(Integer serverId, Integer[] userTypes) {

		List<String> unitIds = null;
		if (null != serverId) {
			unitIds = serverAuthorizeService.listUnitIds(serverId);
		}

		return userRmoteService.countUserNum(unitIds == null ? null : unitIds.toArray(new String[unitIds.size()]),
				userTypes);
	}

	@Override
	public Server getAppByAppId(int appId) {
		return serverService.getAppByAppId(appId);
	}

	@Override
	public String getAppsByDevId(String devId) {
		return SUtils.s(serverService.getAppsByDevId(devId));
	}

	@Override
	public String findBySubId(Integer subId) {
		return SUtils.s(serverService.findBySubId(subId));
	}
	
	@Override
	public String findBySubIds(Integer[] subIds) {
		return SUtils.s(serverService.findBySubIds(subIds));
	}

	@Override
	public String findByIndexUrl(String url) {
		return SUtils.s(serverService.findByIndexUrl(url));
	}
}
