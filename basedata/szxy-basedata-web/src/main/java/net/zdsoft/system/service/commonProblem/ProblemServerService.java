/* 
 * @(#)ProblemServerService.java    Created on 2017-5-9
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.commonProblem;

import java.util.List;

import net.zdsoft.system.entity.commonProblem.ProblemServer;

/**
 * @author xuxiyu
 * @version $Revision: 1.0 $, $Date: 2017-5-9 上午11:51:42 $
 */
public interface ProblemServerService {
    List<ProblemServer> findAll();
}
