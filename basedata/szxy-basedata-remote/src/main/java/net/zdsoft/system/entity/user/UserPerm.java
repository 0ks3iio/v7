/* 
 * @(#)UserPerm.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.entity.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:33:28 $
 */
@Entity
@Table(name = "sys_user_perm")
public class UserPerm extends BaseEntity<String> {

    private static final long serialVersionUID = -5695359192459654336L;

    @Override
    public String fetchCacheEntitName() {
        return "userPerm";
    }

    private String userId;// 角色id
    private Integer modelId;// 模块id

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

}
