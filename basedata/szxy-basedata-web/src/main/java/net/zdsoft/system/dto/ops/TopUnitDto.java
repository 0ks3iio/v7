/* 
 * @(#)TopUnitDto.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dto.ops;

public class TopUnitDto {

    private String unitName;// 顶级单位名称
    private String regionCode;// 顶级单位行政区划
    private String username;// 顶级单位管理员账号
    private String password;// 顶级单位管理员密码
    private String licenseTxt;// 序列号

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLicenseTxt() {
        return licenseTxt;
    }

    public void setLicenseTxt(String licenseTxt) {
        this.licenseTxt = licenseTxt;
    }
}
