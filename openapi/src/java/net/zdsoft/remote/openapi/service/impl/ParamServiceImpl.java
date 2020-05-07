/* 
 * @(#)ParamServiceImpl.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.zdsoft.remote.openapi.dao.ParamDao;
import net.zdsoft.remote.openapi.entity.Parameter;
import net.zdsoft.remote.openapi.service.ParamService;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-28 下午05:57:34 $
 */
@Service("paramService")
public class ParamServiceImpl implements ParamService {
    @Resource
    private ParamDao paramDao;

    @Override
    public List<Parameter> getParams(String uri) {
        return paramDao.findByUriOrderByDisplayOrder(uri);
    }

}
