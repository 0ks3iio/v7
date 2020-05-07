/* 
 * @(#)ServerAuthorizeJdbcDaoImpl.java    Created on 2017年3月17日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.server.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.system.dao.server.ServerAuthorizeJdbcDao;
import net.zdsoft.system.entity.server.ServerAuthorize;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月17日 下午4:07:21 $
 */
@Repository
public class ServerAuthorizeJdbcDaoImpl extends BaseDao<ServerAuthorize> implements ServerAuthorizeJdbcDao {

    @Override
    public ServerAuthorize setField(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void deleteByServerIdAndUnitCondition(Integer serverId, String region, Integer[] unitClass) {
        List<Object> args = new ArrayList<Object>();
        args.add(serverId);
        StringBuffer sql = new StringBuffer("delete from base_server_authorize where  server_id=?");

        sql.append(" and unit_id in(select id from base_unit where is_deleted=0 ");

        if (StringUtils.isNotEmpty(region)) {
            args.add(region + "%");
            sql.append(" and region_code like ?");
        }
        sql.append(andIn(args, Arrays.asList(unitClass), "unit_class"));

        args.add(serverId);
        sql.append(" and id in(select unit_id from base_server_authorize where server_id=?))");

        update(sql.toString(), args.toArray(new Object[0]));
    }
}
