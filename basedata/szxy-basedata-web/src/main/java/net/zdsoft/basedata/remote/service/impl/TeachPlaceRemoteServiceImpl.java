/* 
 * @(#)TeacherPlaceRemoteServiceImpl.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.framework.utils.SUtils;

@Service("teachPlaceRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachPlaceRemoteServiceImpl extends BaseRemoteServiceImpl<TeachPlace,String> implements TeachPlaceRemoteService {

    @Autowired
    private TeachPlaceService teachPlaceService;

    @Override
    public String findTeachPlaceMap(String unitId, String type) {
        return SUtils.s(teachPlaceService.findTeachPlaceMap(unitId, type));
    }

    @Override
    public String findTeachPlaceMap(String[] ids) {
        return SUtils.s(teachPlaceService.findTeachPlaceMap(ids));
    }
    
    @Override
    public String findTeachPlaceMapByAttr(String[] ids, String attrName) {
    	return SUtils.s(teachPlaceService.findTeachPlaceMapByAttr(ids, attrName));
    }

    @Override
    public String findTeachPlaceById(String id) {
        return findOneById(id);
    }

    @Override
    protected BaseService<TeachPlace, String> getBaseService() {
        return teachPlaceService;
    }

	@Override
	public String findTeachPlaceListByType(String unitId, String type) {
		return SUtils.s(teachPlaceService.findTeachPlaceListByType(unitId, type));
	}

	@Override
	public String findTeachPlaceList(String[] ids) {
		return SUtils.s(teachPlaceService.findListByIds(ids));
	}

	@Override
	public String findByUnitIdIn(String[] unitIds) {
		return SUtils.s(teachPlaceService.findByUnitIdIn(unitIds));
	}

}
