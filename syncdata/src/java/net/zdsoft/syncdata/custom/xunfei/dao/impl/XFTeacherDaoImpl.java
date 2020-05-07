package net.zdsoft.syncdata.custom.xunfei.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.syncdata.custom.xunfei.dao.XFTeacherDao;
import net.zdsoft.syncdata.entity.XFTeacher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository 
@Lazy(true)
public class XFTeacherDaoImpl extends BaseDao<XFTeacher> implements XFTeacherDao {

    @Override
    public List<XFTeacher> findAll(String unitId) {
        return query("select * from tianchang_teacher where school_ic = ?", unitId, new MultiRow());
    }

    @Override
    public XFTeacher setField(ResultSet rs) throws SQLException {
        XFTeacher t = new XFTeacher();
        t.setId(StringUtils.leftPad(rs.getString("idcard").replaceAll("X", "A"), 32, "0"));
        t.setTeacherName(rs.getString("name"));
        t.setTeacherCode(StringUtils.leftPad(rs.getString("teacher_id"), 6, "0"));
        t.setIdentityCard(rs.getString("idcard"));
        t.setSex(NumberUtils.toInt(StringUtils.substring(rs.getString("gender"), 0, 1)));
        t.setUnitId(StringUtils.leftPad(rs.getString("school_ic"), 32, "0"));
        t.setDeptId("D" + StringUtils.leftPad(rs.getString("school_ic"), 31, "0"));
        t.setNation(StringUtils.leftPad(rs.getString("nation_code"), 2, "0"));
        t.setCreationTime(new Date());
        t.setIsDeleted(0);
        t.setWeaveUnitId(t.getUnitId());
        t.setIncumbencySign("11");
        t.setModifyTime(new Date());
        t.setIsLeaveSchool("0");
        t.setEventSource(1);
        return t;
    }

    @Override
    public List<XFTeacher> findLgTime(Date time) {
        return null;
    }

}
