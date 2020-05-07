/* 
 * @(#)ProblemType.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.entity.commonProblem;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_problem_type")
public class ProblemType extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String name;
    private String serverCode;
    private int isDeleted;
    private Date modifyTime;

    public String getName() {
        return name;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    @Override
    public String fetchCacheEntitName() {
        return "problemType";
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
