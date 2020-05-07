/* 
 * @(#)ProblemJdbcDao.java    Created on 2017-5-16
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.commonProblem;

import java.util.List;

import net.zdsoft.system.remote.dto.ProblemRemoteDto;

/**
 * @author xuxiyu
 * @version $Revision: 1.0 $, $Date: 2017-5-16 上午09:45:48 $
 */
public interface ProblemJdbcDao {
    List<ProblemRemoteDto> findByCondition(ProblemRemoteDto contidtion);
}
