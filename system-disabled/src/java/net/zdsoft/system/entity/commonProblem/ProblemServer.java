/* 
 * @(#)ProblemServer.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.entity.commonProblem;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_problem_server")
public class ProblemServer extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String code;// 应用code
    private String serverName;// 应用名称

    public String getCode() {
        return code;
    }

    public String getServerName() {
        return serverName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "problemServer";
    }

}
