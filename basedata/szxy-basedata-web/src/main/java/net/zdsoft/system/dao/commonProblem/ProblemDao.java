/* 
 * @(#)ProblemDao.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.commonProblem;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.commonProblem.Problem;

public interface ProblemDao extends BaseJpaRepositoryDao<Problem, Integer> {
    @Query("from  Problem  where serverCode =?1 and isDeleted=0 order by typeId desc,modifyTime desc")
    List<Problem> findByServerCode(String serverCode);

    @SuppressWarnings("unchecked")
    @Override
    Problem save(Problem problem);

    @Modifying
    @Query("update Problem set question =?1,answer =?2 ,modifyTime =?3 where id =?4")
    void updateProblem(String question, String answer, Date date, String id);

    @Modifying
    @Transactional
    @Query("update Problem set isDeleted=1 where id=?1")
    void deleProblemById(String id);

    /**
     * 根据问题条件查询
     * 
     * @param question
     * @return
     */
    @Query("from Problem where serverCode =?1 and question like '%'||?2||'%' and isDeleted=0 order by typeId")
    List<Problem> findByServerAndQuestion(String serverCode, String question);

    @Query("from Problem where serverCode=?1 and isDeleted=0 order by modifyTime desc")
    List<Problem> findOrderByModifyTime(String serverCode);

    @Query("from Problem where serverCode=?1 and isDeleted=0 and viewNum>0 order by viewNum desc")
    List<Problem> findOrderByViewNum(String serverCode);

    @Query("from Problem where id=?1 and isDeleted=0")
    Problem findById(String id);

    @Modifying
    @Transactional
    @Query("update Problem set viewNum=viewNum+1 where id=?1")
    void updatViewNum(String id);

    @Modifying
    @Transactional
    @Query("update Problem set isDeleted=1 where typeId=?1")
    void delProblemByTypeId(String typeId);

}
