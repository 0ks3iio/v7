/* 
 * @(#)TeachPlaceRemoteService.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachPlace;

public interface TeachPlaceRemoteService extends BaseRemoteService<TeachPlace,String>{

    /**
     * 查询指定机构的场地信息
     * 
     * @author dingw
     * @param unitId
     * @param type
     *            不传值则返回所有场地
     * @return JSON(Map<id,name>)
     */
    String findTeachPlaceMap(String unitId, String type);

    /**
     * 批量查询场地信息
     * 
     * @author dingw
     * @param ids
     * @return JSON(Map<id,name>)
     */
    String findTeachPlaceMap(String[] ids);

    /**
     * 查询指定场地信息
     * 
     * @author dingw
     * @param id
     * @return JSON(TeachPlace Object)
     */
    String findTeachPlaceById(String id);
    /**
     * 查询指定单位某一类型的场地信息
     * 
     * @param unitId
     * @param type
     * @return
     */
    String findTeachPlaceListByType(final String unitId, final String type);
    
    /**
     * 批量查询场地信息
     * 
     * @param ids
     * @return 
     */
    String findTeachPlaceList(String[] ids);

    /**
     * 
     * @param ids 查询的id集合
     * @param attrName Map中value在TeachPlace中的属性名
     * @return
     */
	String findTeachPlaceMapByAttr(String[] ids, String attrName);

	/**
	 * 查找单位下的所有场地
	 * @param unitIds
	 * @return
	 */
	String findByUnitIdIn(String[] unitIds);
    
}
