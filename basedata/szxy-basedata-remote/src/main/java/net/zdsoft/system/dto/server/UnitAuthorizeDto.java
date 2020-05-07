/* 
 * @(#)Unit.java    Created on 2017年2月28日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dto.server;

import java.io.Serializable;
import java.util.List;

import net.zdsoft.basedata.dto.UnitDto;
import net.zdsoft.system.entity.server.Server;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月28日 上午9:48:59 $
 */
public class UnitAuthorizeDto implements Serializable {

    private static final long serialVersionUID = 7830584577804745001L;

    private String unitId;// 单位id
    private String unitName;// 单位名称
    private String unionCode;// 单位编号
    private Integer unitClass;// 单位类型 1.教育局 2.学校
    private String regionName;// 行政区划名称
    private int authorizeStatus;// 应用授权状态 0.未授权 1.已授权
    private List<Server> authorizeServerList;// 订阅应用列表

    public UnitAuthorizeDto() {

    }

    public UnitAuthorizeDto(UnitDto unit, List<Server> serverList) {
        this.unitId = unit.getUnitId();
        this.unitName = unit.getUnitName();
        this.unionCode = unit.getUnionCode();
        this.unitClass = unit.getUnitClass();
        this.authorizeServerList = serverList;
        this.regionName = unit.getRegionName();
    }

    public UnitAuthorizeDto(UnitDto unit, int authorizeStatus) {
        this.unitId = unit.getUnitId();
        this.unitName = unit.getUnitName();
        this.unionCode = unit.getUnionCode();
        this.unitClass = unit.getUnitClass();
        this.authorizeStatus = authorizeStatus;
        this.regionName = unit.getRegionName();
    }

    public String getServerStr() {
        String serverStr = "";
        if (authorizeServerList != null && !authorizeServerList.isEmpty()) {
            int index = 1;
            for (Server serverObj : authorizeServerList) {
                serverStr += serverObj.getName();
                if (index < authorizeServerList.size()) {
                    serverStr += "</br>";
                }
            }
        }
        return serverStr;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Integer getUnitClass() {
        return unitClass;
    }

    public void setUnitClass(Integer unitClass) {
        this.unitClass = unitClass;
    }

    public List<Server> getAuthorizeServerList() {
        return authorizeServerList;
    }

    public void setAuthorizeServerList(List<Server> authorizeServerList) {
        this.authorizeServerList = authorizeServerList;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(int authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

}
