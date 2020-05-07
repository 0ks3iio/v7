package net.zdsoft.syncdata.custom.xunfei.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.syncdata.custom.xunfei.dao.XFStudentDao;
import net.zdsoft.syncdata.entity.XFStudent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@Lazy(true)
public class XFStudentDaoImpl extends BaseDao<XFStudent> implements XFStudentDao {

    @Override 
    public XFStudent setField(ResultSet rs) throws SQLException {
        XFStudent student = new XFStudent();
        student.setId(rs.getString("student_ic").replaceAll("-", ""));
        student.setSchoolId(StringUtils.leftPad(rs.getString("school_ic"), 32, "0"));
        student.setStudentName(rs.getString("name"));
        student.setSex("女".equals(rs.getString("gender")) ? 2 : 1);
        try {
            student.setBirthday(DateUtils.parseDate(rs.getString("birthday"), "yyyy-MM-dd"));
            student.setModifyTime(DateUtils.parseDate(rs.getString("timestamp"),
                    "yyyy-MM-dd HH:mm:ss"));
            student.setCreationTime(student.getModifyTime());
        }
        catch (ParseException e) {
        }
        student.setClassName(rs.getString("class"));
        student.setNowState("40");
        student.setIsLeaveSchool(0);
        student.setIsDeleted(0);
        student.setNation(rs.getString("nation_code"));
        String code = rs.getString("birthplace_code");
        student.setNativePlace(NumberUtils.isNumber(code) ? code : null);
        code = rs.getString("hkplace_code");
        student.setRegisterPlace(NumberUtils.isNumber(code) ? code : null);
        student.setGradeName(rs.getString("grade"));
        student.setPin(rs.getString("student_ip"));
        student.setIdentityCard(rs.getString("idcard"));
        student.setUpdatestamp(rs.getString("timestamp"));
        student.setEventSource(0);
        return student;
    }

    @Override
    public List<XFStudent> findLgTime(Date time, Pagination page) {
        String sql = "select * from tianchang_student where timestamp > ?";
        return query(sql, new Object[] { time }, new MultiRow(), page);
    }

    @Override
    public List<XFStudent> findAll(String unitId, String time) {
        String sql = "select * from tianchang_student where school_ic = ? and timestamp > ?";
        return query(sql, new Object[] { unitId, time }, new MultiRow());
    }

}
