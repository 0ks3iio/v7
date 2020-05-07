/* 
 * @(#)ProblemServerDao.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.commonProblem;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.commonProblem.ProblemServer;

public interface ProblemServerDao extends BaseJpaRepositoryDao<ProblemServer, Integer> {
    @Override
    List<ProblemServer> findAll();
}
