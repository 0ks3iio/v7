/*
 * @(#)GroupItems.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午4:46:56 $
 */
@Entity
@Table(name = "base_group_items")
public class GroupItems extends BaseEntity<String>{

    private static final long serialVersionUID = 1L;

    @Column
    private Integer type;
    @Column
    private String groupId;
    @Column
    private String itemId;

    @Override
    public String fetchCacheEntitName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return Returns the type.
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return Returns the groupId.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     *            The groupId to set.
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return Returns the itemId.
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId
     *            The itemId to set.
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
