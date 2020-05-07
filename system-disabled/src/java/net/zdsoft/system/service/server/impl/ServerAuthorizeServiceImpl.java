/* 
 * @(#)ServerAuthorizeServiceImpl.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.server.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.enums.UnitClassEnum;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.system.dao.server.ServerAuthorizeDao;
import net.zdsoft.system.dao.server.ServerAuthorizeJdbcDao;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.server.ServerAuthorize;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.service.server.ServerAuthorizeService;
import net.zdsoft.system.service.server.ServerService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午10:58:02 $
 */
@Service
public class ServerAuthorizeServiceImpl extends BaseServiceImpl<ServerAuthorize, String> implements
        ServerAuthorizeService {

    @Autowired
    private ServerAuthorizeDao serverAuthorizeDao;

    @Autowired
    private ServerService serverService;

    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private ServerAuthorizeJdbcDao serverAuthorizeJdbcDao;

    @Override
    protected BaseJpaRepositoryDao<ServerAuthorize, String> getJpaDao() {
        return serverAuthorizeDao;
    }

    @Override
    protected Class<ServerAuthorize> getEntityClass() {
        return ServerAuthorize.class;
    }

    @Override
    public Map<String, List<Server>> getUnitIdAndAuthorizeServerMap(String[] unitIds) {
        Map<String, List<Server>> unitIdAndAuthorizeServerMap = new HashMap<String, List<Server>>();

        // 单位订阅应用信息
        List<ServerAuthorize> serverAuthorizeList = serverAuthorizeDao.findByUnitIds(unitIds);

        if (CollectionUtils.isNotEmpty(serverAuthorizeList)) {
            Set<Integer> serverIdSet = EntityUtils.getSet(serverAuthorizeList, "serverId");

            // 查找应用信息
            Map<Integer, Server> serverMap = serverService.getOnLineServerMap(serverIdSet
                    .toArray(new Integer[serverIdSet.size()]));

            for (ServerAuthorize authorize : serverAuthorizeList) {
                List<Server> serverList = unitIdAndAuthorizeServerMap.get(authorize.getUnitId());
                if (CollectionUtils.isEmpty(serverList)) {
                    serverList = new ArrayList<Server>();
                }

                if (serverMap.containsKey(authorize.getServerId())) {
                    serverList.add(serverMap.get(authorize.getServerId()));
                }
                unitIdAndAuthorizeServerMap.put(authorize.getUnitId(), serverList);
            }

        }
        return unitIdAndAuthorizeServerMap;
    }

    @Override
    public List<Integer> listServerIds(String unitId) {
        return serverAuthorizeDao.findServerIds(unitId);
    }

    @Override
    public void updateUnitAuthorize(String unitId, List<ServerAuthorize> serverAuthorizeList) {
        serverAuthorizeDao.deleteByUnitId(unitId);
        if (CollectionUtils.isNotEmpty(serverAuthorizeList)) {
            serverAuthorizeDao.saveAll(serverAuthorizeList);
        }

    }

    @Override
    public void saveServerAuthorizes(List<ServerAuthorize> serverAuthorizeList) {
        serverAuthorizeDao.saveAll(serverAuthorizeList);
    }

    @Override
    public void removeServerAuthorizes(Integer serverId, String[] uintIds) {
        serverAuthorizeDao.deleteByServerIdAndUnitIds(serverId, uintIds);
    }

    @Override
    public List<String> listUnitIds(Integer serverId) {
        return serverAuthorizeDao.findUnitIds(serverId);
    }

    @Override
    public List<Server> listServers(String unitId, Integer unitClass, Integer ownerType, Integer[] orderTypes,
            String sections, Integer isVisible, String subIdCondition) {
        List<Server> serverList = new ArrayList<Server>();

        // 已订阅应用
        List<Integer> serverIdList = new ArrayList<Integer>();
        if (StringUtils.isNotEmpty(unitId)) {
            serverIdList = serverAuthorizeDao.findServerIds(unitId);
        }

        if (CollectionUtils.isNotEmpty(serverIdList)) {
            // 学段
            if (null != unitClass && unitClass == UnitClassEnum.SCHOOL.getValue() && StringUtils.isEmpty(sections)) {
                sections = schoolRemoteService.findSectionsById(unitId);
            }

            serverList = serverService.findServerList(orderTypes, unitClass, AppStatusEnum.ONLINE.getValue(), sections,
                    ownerType, serverIdList.toArray(new Integer[serverIdList.size()]), isVisible, subIdCondition);
        }

        return serverList;

    }

    @Override
    public void removeByServerIdAndUnitCondition(Integer serverId, String region, Integer[] unitClass) {
        serverAuthorizeJdbcDao.deleteByServerIdAndUnitCondition(serverId, region, unitClass);
    }

}
