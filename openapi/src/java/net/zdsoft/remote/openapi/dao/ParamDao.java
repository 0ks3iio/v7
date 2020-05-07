/* 
 * @(#)ParamDao.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.Parameter;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-28 下午06:02:38 $
 */
public interface ParamDao extends BaseJpaRepositoryDao<Parameter, String> {

    public List<Parameter> findByUriOrderByDisplayOrder(String uri);
}
