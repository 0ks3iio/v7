/* 
 * @(#)UnitJdbcDao.java    Created on 2017年3月17日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.framework.entity.Pagination;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月17日 下午3:36:34 $
 */
public interface UnitJdbcDao {
    /**
     * 查找已订阅某应用的单位列表
     * 
     * @author cuimq
     * @param unitClass
     * @param regionCode
     * @param serverId
     * @param page
     * @return
     */
    List<Unit> findAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page);

    /**
     * 查找未订阅某应用的单位列表
     * 
     * @author cuimq
     * @param unitClass
     * @param regionCode
     * @param serverId
     * @param page
     * @return
     */
    List<Unit> findUnAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page);
    
    List<Unit> findTopUnitList();
    /**
     * 获取教育局下过滤学段单位数据
     * @param unionCode
     * @param section
     * @return
     */
    public List<Unit> findUnionCodeSectionList(String unionCode,String section, boolean isedu,boolean isSchool);
}
