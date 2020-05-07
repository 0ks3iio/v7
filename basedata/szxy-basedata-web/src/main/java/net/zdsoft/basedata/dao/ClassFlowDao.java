/*
* Project: v7
* Author : shenke
* @(#) ClassFlowDao.java Created on 2016-9-27
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import org.springframework.stereotype.Repository;

import net.zdsoft.basedata.entity.ClassFlow;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-27下午4:23:29
 */
@Repository
public interface ClassFlowDao extends BaseJpaRepositoryDao<ClassFlow, String> {

}
