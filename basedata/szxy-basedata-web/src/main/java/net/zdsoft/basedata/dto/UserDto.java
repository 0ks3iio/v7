/*
 * @(#)UserDto.java    Created on 2017年3月2日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dto;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月2日 下午5:38:28 $
 */
public class UserDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private String id;//
    private String realName;
    private String deptName;
    private String unitName;
    private String avatarUrl;

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the realName.
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     *            The realName to set.
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return Returns the deptName.
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * @param deptName
     *            The deptName to set.
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * @return Returns the unitName.
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName
     *            The unitName to set.
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * @return Returns the avatarUrl.
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * @param avatarUrl
     *            The avatarUrl to set.
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
