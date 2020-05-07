/* 
 * @(#)ProblemService.java    Created on 2017-5-9
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.commonProblem;

import java.util.Date;
import java.util.List;

import net.zdsoft.system.entity.commonProblem.Problem;

public interface ProblemService {
    List<Problem> findByServerCode(String serverCode);

    /**
     * 根据问题条件查询
     * 
     * @param question
     * @return
     */
    List<Problem> findByServerAndQuestion(String serverCode, String question);

    Problem saveProblem(Problem problem);

    void updateProblem(String question, String answer, Date date, String id);

    void delProblemById(String id);

    List<Problem> findOrderByModifyTime(String serverCode);

    List<Problem> findOrderByViewNum(String serverCode);

    /**
     * 根据问题id查找问题类型名称、问题详情
     * 
     * @param id
     * @return
     */
    Problem findById(String id);

    /**
     * 修改viewNum的值+1
     * 
     * @param id
     */
    void updatViewNum(String id);

    /**
     * 根据问题类型删除问题
     * 
     * @param typeId
     */
    void delProblemByTypeId(String typeId);
}
