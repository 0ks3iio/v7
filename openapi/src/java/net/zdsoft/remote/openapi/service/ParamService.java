/* 
 * @(#)ParamService.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.remote.openapi.entity.Parameter;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-28 下午05:57:06 $
 */
public interface ParamService {
    List<Parameter> getParams(String uri);
}
