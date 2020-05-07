/* 
 * @(#)AppManageRemoteServiceImpl.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.dto.ServerRemoteDto;
import net.zdsoft.system.remote.service.AppManageRemoteService;
import net.zdsoft.system.service.server.ServerService;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:51:22 $
 */
@Service("appManageRemoteService")
public class AppManageRemoteServiceImpl implements AppManageRemoteService {

    @Autowired
    private ServerService serverService;

    @Override
    public List<ServerRemoteDto> getAppsByDevId(String devId) {
        List<ServerRemoteDto> dtos = new ArrayList<ServerRemoteDto>();
        List<Server> apps = serverService.getAppsByDevId(devId);
        for (Server app : apps) {
            ServerRemoteDto dto = changeServerToRemoteDto(app);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public void removeApp(String systemId) {
        serverService.removeApp(systemId);
    }

    @Override
    public ServerRemoteDto getAppBySystemId(String systemId) {
        return changeServerToRemoteDto(serverService.getAppBySystemId(systemId));
    }

    @Override
    public void updateAppStatus(int status, String systemId) {
        serverService.updateAppStatusBySystemId(status, systemId);
    }

    @Override
    public int getAppCountByName(String appName) {
        return serverService.getAppCountByName(appName);
    }

    @Override
    public void addApp(ServerRemoteDto dto) {
        Server server = changeRemoteDtoToServer(dto);
        serverService.addApp(server);
    }

    @Override
    public void updateAppInfo(ServerRemoteDto dto) {
        Server server = changeRemoteDtoToServer(dto);
        serverService.updateAppInfoForOpenapi(server);
        //serverService.updateAppAndRegisterPassPort(server);
    }

    private Server changeRemoteDtoToServer(ServerRemoteDto dto) {
        Server server = new Server();
        server.setId(dto.getId());
        server.setSystemId(dto.getSystemId());
        server.setName(dto.getName());
        server.setServerKey(dto.getServerKey());
        server.setServerTypeId(dto.getServerTypeId());
        server.setDescription(dto.getDescription());
        server.setIcon(dto.getIcon());
        server.setIconUrl(dto.getIconUrl());
        server.setProtocol(dto.getProtocol());
        server.setDomain(dto.getDomain());
        server.setPort(dto.getPort());
        server.setContext(dto.getContext());
        server.setIndexUrl(dto.getIndexUrl());
        server.setVerifyUrl(dto.getVerifyUrl());
        server.setInvalidateUrl(dto.getInvalidateUrl());
        server.setDevId(dto.getDevId());
        server.setCreationTime(dto.getCreationTime());
        server.setAuditTime(dto.getAuditTime());
        server.setOnlineTime(dto.getOnlineTime());
        server.setModifyTime(dto.getModifyTime());
        server.setApplyTime(dto.getApplyTime());
        server.setOrderType(dto.getOrderType());
        server.setUnittype(dto.getUnitType());
        server.setUsertype(dto.getUserType());
        server.setStatus(dto.getStatus());
        server.setServerClass(dto.getServerClass());
        server.setSections(dto.getSections());
        server.setIsDeleted(dto.getIsDeleted());
        server.setStatusName(dto.getStatusName());
        server.setTimeStr(dto.getTimeStr());
        server.setFullIcon(dto.getFullIcon());
        server.setUnitTypeArray(dto.getUnitTypeArray());
        server.setUserTypeArray(dto.getUserTypeArray());
        server.setSectionsArray(dto.getSectionsArray());
        return server;
    }

    private ServerRemoteDto changeServerToRemoteDto(Server server) {
        ServerRemoteDto result = new ServerRemoteDto();
        result.setId(server.getId());
        result.setSystemId(server.getSystemId());
        result.setName(server.getName());
        result.setServerKey(server.getServerKey());
        result.setServerTypeId(server.getServerTypeId());
        result.setDescription(server.getDescription());
        result.setIcon(server.getIcon());
        result.setIconUrl(server.getIconUrl());
        result.setProtocol(server.getProtocol());
        result.setDomain(server.getDomain());
        result.setPort(server.getPort());
        result.setContext(server.getContext());
        result.setIndexUrl(server.getIndexUrl());
        result.setVerifyUrl(server.getVerifyUrl());
        result.setInvalidateUrl(server.getInvalidateUrl());
        result.setDevId(server.getDevId());
        result.setCreationTime(server.getCreationTime());
        result.setAuditTime(server.getAuditTime());
        result.setOnlineTime(server.getOnlineTime());
        result.setModifyTime(server.getModifyTime());
        result.setApplyTime(server.getApplyTime());
        result.setOrderType(server.getOrderType());
        result.setUnitType(server.getUnittype());
        result.setUserType(server.getUsertype());
        result.setStatus(server.getStatus());
        result.setServerClass(server.getServerClass());
        result.setSections(server.getSections());
        result.setIsDeleted(server.getIsDeleted());
        result.setStatusName(server.getStatusName());
        result.setTimeStr(server.getTimeStr());
        result.setFullIcon(server.getFullIcon());
        result.setUnitTypeArray(server.getUnitTypeArray());
        result.setUserTypeArray(server.getUserTypeArray());
        result.setSectionsArray(server.getSectionsArray());
        result.setSubId(server.getSubId());
        return result;
    }
}
