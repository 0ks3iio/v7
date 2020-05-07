/* 
 * @(#)DeveloperDto.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.api.base.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午02:07:36 $
 */
public class DeveloperDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String ticketKey;
    private String unitName;
    private String ips;
    private String username;
    private String realName;
    private String mobilePhone;
    private String email;
    private String address;
    private List<String> interfaceNames;
    private Date creationTime;
    private String apKey;
    private int isCheck;// 是否有数据需要审核 1：是 0：否
    private boolean isPower; //是否有权限授权
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

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

	public boolean getIsPower() {
		return isPower;
	}

	public void setPower(boolean isPower) {
		this.isPower = isPower;
	}

	public String getApKey() {
		return apKey;
	}

	public void setApKey(String apKey) {
		this.apKey = apKey;
	}
}
