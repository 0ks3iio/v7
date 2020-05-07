/* 
 * @(#)UnitDaoImpl.java    Created on 2017年3月17日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.UnitJdbcDao;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.MultiRowMapper;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月17日 下午3:36:50 $
 */

@Repository
public class UnitDaoImpl extends BaseDao<Unit> implements UnitJdbcDao {

    @Override
    public List<Unit> findAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page) {
        StringBuffer sql = new StringBuffer("select id, unit_name, region_code from base_unit where is_deleted=0");

        List<Object> args = Lists.newArrayList();

        sql.append(andIn(args, Arrays.asList(unitClass), "unit_class"));

        if (StringUtils.isNotEmpty(regionCode)) {
            args.add(regionCode + "%");
            sql.append(" and region_code like ?");
        }

        args.add(serverId);
        sql.append(" and id in(select unit_id from base_server_authorize where server_id=? and is_deleted=0 and unit_id is not null) order by unit_Name");

        if (page != null) {
            return query(sql.toString(), args.toArray(new Object[0]), new MultiRowMapper<Unit>() {

				@Override
				public Unit mapRow(ResultSet rs, int rowNum) throws SQLException {
					Unit unit = new Unit();
			        unit.setId(rs.getString("id"));
			        unit.setUnitName(rs.getString("unit_name"));
			        unit.setRegionCode(rs.getString("region_code"));
					return unit;
				}
            	
            }, page);
        }
        else {
            return query(sql.toString(), args.toArray(new Object[0]),  new MultiRowMapper<Unit>(){

				@Override
				public Unit mapRow(ResultSet rs, int rowNum) throws SQLException {
					Unit unit = new Unit();
			        unit.setId(rs.getString("id"));
			        unit.setUnitName(rs.getString("unit_name"));
			        unit.setRegionCode(rs.getString("region_code"));
					return unit;
				}
            	
            });
        }
    }

    @Override
    public List<Unit> findUnAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page) {
        StringBuffer sql = new StringBuffer("select id, unit_name, region_code from base_unit where is_deleted=0");

        List<Object> args = Lists.newArrayList();

        sql.append(andIn(args, Arrays.asList(unitClass), "unit_class"));

        if (StringUtils.isNotEmpty(regionCode)) {
            args.add(regionCode + "%");
            sql.append(" and region_code like ?");
        }

        args.add(serverId);
        sql.append(" and id not in(select unit_id from base_server_authorize where server_id=? and is_deleted=0 and unit_id is not null) order by unit_Name");

        if (page != null) {
            return query(sql.toString(), args.toArray(new Object[0]), new MultiRowMapper<Unit>(){

				@Override
				public Unit mapRow(ResultSet rs, int rowNum) throws SQLException {
					Unit unit = new Unit();
			        unit.setId(rs.getString("id"));
			        unit.setUnitName(rs.getString("unit_name"));
			        unit.setRegionCode(rs.getString("region_code"));
					return unit;
				}
            	
            }, page);
        }
        else {
            return query(sql.toString(), args.toArray(new Object[0]), new MultiRowMapper<Unit>(){

				@Override
				public Unit mapRow(ResultSet rs, int rowNum) throws SQLException {
					Unit unit = new Unit();
			        unit.setId(rs.getString("id"));
			        unit.setUnitName(rs.getString("unit_name"));
			        unit.setRegionCode(rs.getString("region_code"));
					return unit;
				}
            	
            });
        }
    }

    @Override
    public Unit setField(ResultSet rs) throws SQLException {
        Unit unit = new Unit();
        unit.setId(rs.getString("id"));
        unit.setUnitClass(rs.getInt("unit_class"));
        unit.setUnitName(rs.getString("unit_name"));
        unit.setRegionCode(rs.getString("region_code"));
        unit.setParentId(rs.getString("parent_id"));
        unit.setUnitHeader(rs.getString("unit_header"));
        unit.setMobilePhone(rs.getString("mobile_phone"));
        return unit;
    }

	@Override
	public List<Unit> findTopUnitList() {
		StringBuffer sql = new StringBuffer("select * from base_unit where is_deleted=0 and parent_Id in('00000000000000000000000000000000','11111111111111111111111111111111') ");
		return query(sql.toString(),  new MultiRow());
	}

	public List<Unit> findUnionCodeSectionList(String unionCode,String section, boolean isedu,boolean isSchool) {
		StringBuffer sql = new StringBuffer("select * FROM base_unit where union_Code like ? and is_deleted=0 ");
		String schoolQuery="";
		String EduQuery="";
		if(isSchool){
			if(StringUtils.isNotBlank(section)) {
				schoolQuery=" (unit_class=2 and exists(select 1 from base_school bs where bs.id=base_unit.id and bs.is_deleted=0 and bs.sections like '%"+section+"%' ) ) ";
			}else {
				schoolQuery=" (unit_class=2 and exists(select 1 from base_school bs where bs.id=base_unit.id and bs.is_deleted=0 ) ) ";
			}
		}
		if(isedu) {
			EduQuery=" ( unit_class<>2 ) ";
		}
		if(isedu && isSchool) {
			sql.append(" and ("+schoolQuery+" or "+EduQuery+")");
		}else if(isedu) {
			sql.append(" and "+EduQuery);
		}else if(isSchool){
			sql.append(" and "+schoolQuery);
		}else {
			return new ArrayList<Unit>();
		}
		return query(sql.toString(),new Object[]{unionCode+'%'},  new MultiRow());
	}
}
