/* 
 * @(#)RolePerm.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:31:11 $
 */
@Entity
@Table(name = "sys_role_perm")
public class RolePerm extends BaseEntity<String> {

    private static final long serialVersionUID = 2248582539389165140L;

    @Override
    public String fetchCacheEntitName() {
        return "rolePerm";
    }

    @Column(name = "roleid")
    private String roleId;// 角色id
    @Column(name = "moduleid")
    private Integer modelId;// 模块id
    @Column(name = "operids")
    private String operIds;
    private Integer type;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getOperIds() {
        return operIds;
    }

    public void setOperIds(String operIds) {
        this.operIds = operIds;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
