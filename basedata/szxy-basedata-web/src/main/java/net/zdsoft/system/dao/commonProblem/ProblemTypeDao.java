/* 
 * @(#)ProblemTypeDao.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.commonProblem;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.commonProblem.ProblemType;

public interface ProblemTypeDao extends BaseJpaRepositoryDao<ProblemType, Integer> {

    @Query("from ProblemType where is_deleted=0 and serverCode=?1 order by modify_time asc")
    List<ProblemType> findByServerCode(String serverCode);

    @Modifying
    @Query("update ProblemType set name=?1 where id=?2")
    void updateTypeName(String name, String id);

    ProblemType findById(String id);

    @Modifying
    @Query("update ProblemType set is_deleted=1 where id=?1")
    void removeProblemTypeById(String id);
}
