/*
* Project: v7
* Author : shenke
* @(#) StudentFlowDao.java Created on 2016-8-5
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.StudentFlow;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-5上午11:43:35
 */
public interface StudentFlowDao extends BaseJpaRepositoryDao<StudentFlow, String> , StudentFlowJdbcDao{


}
