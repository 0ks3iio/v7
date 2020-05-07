/* 
 * @(#)ServerRemoteDto.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:45:14 $
 */
public class ServerRemoteDto implements Serializable {

    private static final long serialVersionUID = 6705841205620132131L;

    private Integer id;
    private String systemId;

    private String name;// 应用名称
    private String serverKey;// appkey
    private Integer serverTypeId;
    private String description;// 应用描述
    private String icon;// 应用图标名称
    private String iconUrl;// 应用图标相对路径
    private String protocol;// 协议
    private String domain;// 域名
    private Integer port;// 端口号
    private String context;// 上下文
    private String indexUrl;// 首页地址
    private String verifyUrl;// 通知登录url
    private String invalidateUrl;// 退出登录url
    private String devId;// 开发者ID
    private Date creationTime;// 创建时间
    private Date auditTime;// 审核时间
    private Date onlineTime;// 上线时间
    private Date modifyTime;// 修改时间
    private Date applyTime;// 申请时间
    private Integer orderType;// 订阅类型
    private String unitType;// 单位类型
    private String userType;// 用户类型
    private Integer status;// 应用状态
    private Integer serverClass;// 应用来源
    private String sections;// 学段
    private Integer isDeleted;
    private Integer subId;//

    private String statusName;// 状态名
    private String timeStr;// 时间的字符串形式
    private String fullIcon;// 完整路径
    private String[] unitTypeArray;// 单位类型数组
    private String[] userTypeArray;// 用户类型数组
    private String[] sectionsArray;// 学段类型数组

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getInvalidateUrl() {
        return invalidateUrl;
    }

    public void setInvalidateUrl(String invalidateUrl) {
        this.invalidateUrl = invalidateUrl;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getFullIcon() {
        return fullIcon;
    }

    public void setFullIcon(String fullIcon) {
        this.fullIcon = fullIcon;
    }

    public String[] getUnitTypeArray() {
        return unitTypeArray;
    }

    public void setUnitTypeArray(String[] unitTypeArray) {
        this.unitTypeArray = unitTypeArray;
    }

    public String[] getUserTypeArray() {
        return userTypeArray;
    }

    public void setUserTypeArray(String[] userTypeArray) {
        this.userTypeArray = userTypeArray;
    }

    public String[] getSectionsArray() {
        return sectionsArray;
    }

    public void setSectionsArray(String[] sectionsArray) {
        this.sectionsArray = sectionsArray;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServerTypeId() {
        return serverTypeId;
    }

    public void setServerTypeId(Integer serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setServerClass(Integer serverClass) {
        this.serverClass = serverClass;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getServerClass() {
        return serverClass;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

}
