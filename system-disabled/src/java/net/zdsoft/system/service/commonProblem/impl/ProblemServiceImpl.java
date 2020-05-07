/* 
 * @(#)ProblemServiceImpl.java    Created on 2017-5-9
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.commonProblem.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.dao.commonProblem.ProblemDao;
import net.zdsoft.system.entity.commonProblem.Problem;
import net.zdsoft.system.service.commonProblem.ProblemService;

/**
 * @author gzjsd
 * @version $Revision: 1.0 $, $Date: 2017-5-9 下午03:09:27 $
 */
@Service("problemService")
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ProblemDao problemDao;

    @Override
    public List<Problem> findByServerCode(String serverCode) {
        return problemDao.findByServerCode(serverCode);
    }

    @Override
    public Problem saveProblem(Problem problem) {

        return problemDao.save(problem);
    }

    @Override
    public void updateProblem(String question, String answer, Date date, String id) {
        problemDao.updateProblem(question, answer, date, id);
    }

    @Override
    public void delProblemById(String id) {
        problemDao.deleProblemById(id);
    }

    @Override
    public List<Problem> findByServerAndQuestion(String serverCode, String question) {
        if (StringUtils.isEmpty(serverCode) || StringUtils.isEmpty(question)) {
            return Collections.emptyList();
        }
        return problemDao.findByServerAndQuestion(serverCode, question);
    }

    @Override
    public List<Problem> findOrderByModifyTime(String serverCode) {
        return problemDao.findOrderByModifyTime(serverCode);
    }

    @Override
    public List<Problem> findOrderByViewNum(String serverCode) {
        return problemDao.findOrderByViewNum(serverCode);
    }

    @Override
    public Problem findById(String id) {
        return problemDao.findById(id);
    }

    @Override
    public void updatViewNum(String id) {
        problemDao.updatViewNum(id);
    }

    @Override
    public void delProblemByTypeId(String typeId) {
        problemDao.delProblemByTypeId(typeId);
    }
}
