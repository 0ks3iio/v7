/* 
 * @(#)ServerAuthorize.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.entity.server;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午10:20:09 $
 */
@Entity
@Table(name = "base_server_authorize")
public class ServerAuthorize extends BaseEntity<String> {

    private static final long serialVersionUID = 9057014414011364893L;

    @Override
    public String fetchCacheEntitName() {
        return "serverAuthorize";
    }

    private Integer serverId;// 应用服务id
    private String userId;// 用户id
    private Date creationTime;// 创建时间
    private int isDeleted;// 软删除标记 0.正常 1.删除
    private int eventSource;// 事件来源 0.本地 1.订阅消息
    private String unitId;// 单位id

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getEventSource() {
        return eventSource;
    }

    public void setEventSource(int eventSource) {
        this.eventSource = eventSource;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

}
