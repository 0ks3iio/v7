/* 
 * @(#)AppManageRemoteService.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.service;

import java.util.List;

import net.zdsoft.system.remote.dto.ServerRemoteDto;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:50:11 $
 */
public interface AppManageRemoteService {

    /**
     * 根据开发者ID获取应用信息
     * 
     * @param devId
     * @return
     */
    List<ServerRemoteDto> getAppsByDevId(String devId);

    /**
     * 删除应用
     * 
     * @param systemId
     */
    void removeApp(String systemId);

    /**
     * 获取应用信息
     * 
     * @param systemId
     * @return
     */
    ServerRemoteDto getAppBySystemId(String systemId);

    /**
     * 更改应用状态
     * 
     * @param status
     * @param systemId
     */
    void updateAppStatus(int status, String systemId);

    /**
     * 根据应用名称查找应用是否存在
     * 
     * @param appName
     * @return
     */
    int getAppCountByName(String appName);

    /**
     * 添加应用信息
     * 
     * @param dto
     */
    void addApp(ServerRemoteDto dto);

    /**
     * 更新应用信息
     * 
     * @param dto
     */
    void updateAppInfo(ServerRemoteDto dto);
}
