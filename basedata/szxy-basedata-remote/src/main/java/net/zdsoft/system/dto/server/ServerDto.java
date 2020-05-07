/* 
 * @(#)ServerDto.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dto.server;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午7:06:30 $
 */
public class ServerDto {

    private Integer id;
    private String name;// 应用名称
    private int hasAuthorized;// 是否已订阅

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHasAuthorized() {
        return hasAuthorized;
    }

    public void setHasAuthorized(int hasAuthorized) {
        this.hasAuthorized = hasAuthorized;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
