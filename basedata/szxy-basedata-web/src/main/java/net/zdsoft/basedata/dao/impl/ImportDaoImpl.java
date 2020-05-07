/*
* Project: v7
* Author : shenke
* @(#) ImportDaoImpl.java Created on 2016-8-18
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.zdsoft.basedata.dao.ImportJdbcDao;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.framework.dao.BaseDao;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-18下午3:41:40
 */
public class ImportDaoImpl extends BaseDao<ImportEntity> implements ImportJdbcDao {

	@Override
	public ImportEntity setField(ResultSet rs) throws SQLException {
		return null;
	}

}
