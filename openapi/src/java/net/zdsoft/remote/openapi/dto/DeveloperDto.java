/* 
 * @(#)DeveloperDto.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午02:07:36 $
 */
public class DeveloperDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String ticketKey;
    private String unitName;
    // private String description;
    private String ips;
    private String username;
    private String realName;
    // private String password;
    private String mobilePhone;
    private String email;
    private String address;
    private String homepage;
    private List<String> interfaceNames;
    // private List<InterfaceDto> interfaces;// 接口信息
    Map<String, List<InterfaceDto>> interfaces;
    private Date creationTime;
    private String serverNames;// </br>分隔
    private int serverNum;// 应用数
    private int isCheck;// 是否有数据需要审核 1：是 0：否
    private int inVerifyNum;// 审核中接口数
    private int passVerifyNum;// 已订阅接口数
    private int unpassVerifyNum;// 没通过接口数
    private List<InterfaceDto> passInterfaceDtos;
    private String inVerifyTypes;// 在审核的类型，用逗号分隔

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<InterfaceDto> getPassInterfaceDtos() {
        return passInterfaceDtos;
    }

    public void setPassInterfaceDtos(List<InterfaceDto> passInterfaceDtos) {
        this.passInterfaceDtos = passInterfaceDtos;
    }

    public List<String> getInterfaceNames() {
        return interfaceNames;
    }

    public void setInterfaceNames(List<String> interfaceNames) {
        this.interfaceNames = interfaceNames;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }

    public int getServerNum() {
        return serverNum;
    }

    public void setServerNum(int serverNum) {
        this.serverNum = serverNum;
    }

    public String getServerNames() {
        return serverNames;
    }

    public void setServerNames(String serverNames) {
        this.serverNames = serverNames;
    }

    public int getInVerifyNum() {
        return inVerifyNum;
    }

    public void setInVerifyNum(int inVerifyNum) {
        this.inVerifyNum = inVerifyNum;
    }

    public int getPassVerifyNum() {
        return passVerifyNum;
    }

    public void setPassVerifyNum(int passVerifyNum) {
        this.passVerifyNum = passVerifyNum;
    }

    public int getUnpassVerifyNum() {
        return unpassVerifyNum;
    }

    public void setUnpassVerifyNum(int unpassVerifyNum) {
        this.unpassVerifyNum = unpassVerifyNum;
    }

    public Map<String, List<InterfaceDto>> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Map<String, List<InterfaceDto>> interfaces) {
        this.interfaces = interfaces;
    }

    public String getInVerifyTypes() {
        return inVerifyTypes;
    }

    public void setInVerifyTypes(String inVerifyTypes) {
        this.inVerifyTypes = inVerifyTypes;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

}
