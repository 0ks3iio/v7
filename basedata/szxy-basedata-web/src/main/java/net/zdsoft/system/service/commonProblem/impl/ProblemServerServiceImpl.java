/* 
 * @(#)ProblemServerServiceImpl.java    Created on 2017-5-9
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.commonProblem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.dao.commonProblem.ProblemServerDao;
import net.zdsoft.system.entity.commonProblem.ProblemServer;
import net.zdsoft.system.service.commonProblem.ProblemServerService;

/**
 * @author xuxiyu
 * @version $Revision: 1.0 $, $Date: 2017-5-9 上午11:53:41 $
 */
@Service("problemServerService")
public class ProblemServerServiceImpl implements ProblemServerService {
    @Autowired
    private ProblemServerDao problemServerDao;

    @Override
    public List<ProblemServer> findAll() {
        return problemServerDao.findAll();
    }

}
