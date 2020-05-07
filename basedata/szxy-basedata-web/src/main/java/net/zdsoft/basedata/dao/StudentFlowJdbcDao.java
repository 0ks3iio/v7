/*
* Project: v7
* Author : shenke
* @(#) StudentFlowJdbcDao.java Created on 2016-8-5
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.StudentFlow;
import net.zdsoft.framework.entity.Pagination;


/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-5上午11:44:15
 */
public interface StudentFlowJdbcDao {


	/**
	 * 学生调动详细信息查询
	 * @param studentname
	 * @param identityCard
	 * @param pin 调动类型为调出是不需要
	 * @param flowType 调动类型
	 * @return
	 */
	List<StudentFlow> searchFlows(String studentname, String identityCard, String pin, String flowType, Pagination page);
}
