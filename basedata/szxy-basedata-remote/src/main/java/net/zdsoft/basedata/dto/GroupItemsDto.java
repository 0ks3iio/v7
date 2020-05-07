/*
 * @(#)GroupItemsDto.java    Created on 2017年3月2日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dto;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月2日 下午4:11:55 $
 */
public class GroupItemsDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private String id;// 成员id
    private String name;// 成员名称
    private String avatarUrl;// 头像

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
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
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
