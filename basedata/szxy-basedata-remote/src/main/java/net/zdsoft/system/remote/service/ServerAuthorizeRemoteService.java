/*
 * @(#)ServerAuthorizeService.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.server.ServerAuthorize;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午10:17:23 $
 */
public interface ServerAuthorizeRemoteService {

	/**
	 * 查找单位订阅应用信息
	 *
	 * @author cuimq
	 * @param unitIds
	 */
	Map<String, List<Server>> getUnitIdAndAuthorizeServerMap(String[] unitIds);

	/**
	 * 查找单位已订阅应用id
	 *
	 * @author cuimq
	 * @param unitId
	 * @return
	 */
	List<Integer> listServerIds(String unitId);

	/**
	 * 更新单位应用订阅信息
	 *
	 * @author cuimq
	 * @param unitId
	 * @param serverAuthorizeList
	 */
	void updateUnitAuthorize(String unitId, List<ServerAuthorize> serverAuthorizeList);

	/**
	 * 批量新增订阅信息
	 *
	 * @author cuimq
	 * @param serverAuthorizeList
	 */
	void saveServerAuthorizes(List<ServerAuthorize> serverAuthorizeList);

	/**
	 * 批量删除订阅信息
	 *
	 * @author cuimq
	 * @param serverId
	 * @param uintIds
	 */
	void removeServerAuthorizes(Integer serverId, String[] unitIds);

	/**
	 * 查找已授权该应用的单位
	 *
	 * @author cuimq
	 * @param serverId
	 * @return
	 */
	List<String> listUnitIds(Integer serverId);

	/**
	 * 查找单位订阅应用列表
	 *
	 * @author cuimq
	 * @param unitId
	 * @param unitClass
	 * @param ownerType  用户身份
	 * @param orderTypes 订阅类型
	 * @param isVisible  是否可见
	 * @return
	 *
	 */
	List<Server> listServers(String unitId, Integer unitClass, Integer ownerType, Integer[] orderTypes, String sections,
			Integer isVisible, String subIdCondition);

	/**
	 * 批量删除订阅信息
	 *
	 * @author cuimq
	 * @param serverId
	 * @param region
	 * @param unitClass
	 */
	void removeByServerIdAndUnitCondition(Integer serverId, String region, Integer[] unitClass);

}
