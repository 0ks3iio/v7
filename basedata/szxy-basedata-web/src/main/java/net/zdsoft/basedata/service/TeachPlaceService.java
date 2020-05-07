/* 
 * @(#)TeachPlaceService.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.TeachPlace;

public interface TeachPlaceService extends BaseService<TeachPlace, String> {

    /**
     * 查询指定单位某一类型的场地信息
     * 
     * @param unitId
     * @param type
     * @return
     */
    List<TeachPlace> findTeachPlaceListByType(final String unitId, final String type);
    /**
     * 查询指定单位的场地信息
     * 
     * @author dingw
     * @param unitId
     * @param type
     * @return
     */
    Map<String, String> findTeachPlaceMap(final String unitId, final String type);

    /**
     * 批量查询场地名称
     * 
     * @author dingw
     * @param ids
     * @return
     */
    Map<String, String> findTeachPlaceMap(String[] ids);
    /**
     * 
     * @param ids
     * @param attrName  Map中value在TeachPlace中的属性名
     * @return
     */
	Map<String, Object> findTeachPlaceMapByAttr(String[] ids, String attrName);
	
	List<TeachPlace> findByUnitIdIn(String[] unitIds);

}
