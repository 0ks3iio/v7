package net.zdsoft.syncdata.custom.xunfei.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.syncdata.custom.xunfei.dao.XFUnitDao;
import net.zdsoft.syncdata.entity.XFUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@Lazy(true)
public class XFUnitDaoImpl extends BaseDao<XFUnit> implements XFUnitDao {

    @Override
    public List<XFUnit> findAll() {
        List<XFUnit> xfunits = query("select * from tianchang_school",
                new MultiRow());
        Map<String, XFUnit> ur = new HashMap<String, XFUnit>();
        for (XFUnit u : xfunits) {
            String id = u.getId();
            XFUnit u2 = ur.get(id);
            if (u2 != null) {
                if (ArrayUtils.contains(new String[] { "119", "218", "368", "319", "518", "519" },
                        u2.getFunctionTypeCode())) {
                    ur.put(id, u);
                }
            }
            else {
                ur.put(id, u);
            }
        }
        List<XFUnit> us = new ArrayList<XFUnit>();
        for (XFUnit xfu : ur.values()) {
            us.add(xfu);
        }
        return us;
    }

    @Override 
    public XFUnit setField(ResultSet rs) throws SQLException {
        XFUnit u = new XFUnit();
        System.out.println("---------setField, " + StringUtils.leftPad(rs.getString("school_ic"), 32, "0"));
        u.setId(StringUtils.leftPad(rs.getString("school_ic"), 32, "0"));
        u.setUnitName(rs.getString("school_name"));
        u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
        String regionCode = rs.getString("location_code");
        regionCode = regionCode.replaceAll("\\.", "");
        u.setRegionCode(StringUtils.substring(regionCode, 0, 6));
        u.setIsDeleted(0);
        u.setRunSchoolType(NumberUtils.toInt(rs.getString("host_code")));
        u.setAreaTypeCode(rs.getString("area_type_code"));
        u.setFunctionTypeCode(rs.getString("function_type_code"));
        u.setModifyTime(new Date());
        u.setCreationTime(new Date());
        u.setEventSource(1);
        u.setSchoolIntId(rs.getInt("school_id"));
        u.setSchoolIc(rs.getString("school_ic"));
        return u;
    }

    @Override
    public List<XFUnit> findLgTime(Date time) {
        return null;
    }

}
