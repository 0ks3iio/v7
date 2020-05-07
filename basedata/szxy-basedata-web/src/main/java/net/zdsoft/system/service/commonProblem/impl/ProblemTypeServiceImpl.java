/* 
 * @(#)ProblemTypeServiceImpl.java    Created on 2017-5-9
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.commonProblem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.dao.commonProblem.ProblemTypeDao;
import net.zdsoft.system.entity.commonProblem.ProblemType;
import net.zdsoft.system.service.commonProblem.ProblemTypeService;

@Service
public class ProblemTypeServiceImpl implements ProblemTypeService {
    @Autowired
    private ProblemTypeDao problemTypeDao;

    @Override
    public List<ProblemType> findByServerCode(String serverCode) {
        return problemTypeDao.findByServerCode(serverCode);
    }

    @Override
    public ProblemType saveOne(ProblemType type) {
        return problemTypeDao.save(type);
    }

    @Override
    public void updateTypeName(String name, String id) {
        problemTypeDao.updateTypeName(name, id);
    }

    @Override
    public ProblemType findById(String id) {
        return problemTypeDao.findById(id);
    }

    @Override
    public void removeProblemTypeById(String id) {
        problemTypeDao.removeProblemTypeById(id);
    }
}
