package net.zdsoft.exammanage.data.dao.impl;

import net.zdsoft.exammanage.data.dao.EmEnrollStudentJdbcDao;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class EmEnrollStudentDaoImpl extends BaseDao<EmEnrollStudent> implements EmEnrollStudentJdbcDao {

    @Override
    public Map<String, Integer> getStudentAllCount(String[] examIds) {
        String sql = "select count(*),exam_id from exammanage_apply_student where exam_id in";
        return queryForInSQL(sql, null, examIds, new MapRowMapper<String, Integer>() {

            @Override
            public String mapRowKey(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString("exam_id");
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getInt("count(*)");
            }
        }, " group by exam_id");
    }

    @Override
    public Map<String, Integer> getStudentPassNum(String[] examIds) {
        String sql = "select count(*),exam_id from exammanage_apply_student where has_pass=1 and exam_id in";
        return queryForInSQL(sql, null, examIds, new MapRowMapper<String, Integer>() {

            @Override
            public String mapRowKey(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString("exam_id");
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getInt("count(*)");
            }
        }, " group by exam_id");
    }

    @Override
    public Map<String, Integer> getStudentAuditNum(String[] examIds) {
        String sql = "select count(*),exam_id from exammanage_apply_student where has_pass=0 and exam_id in";
        return queryForInSQL(sql, null, examIds, new MapRowMapper<String, Integer>() {

            @Override
            public String mapRowKey(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString("exam_id");
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getInt("count(*)");
            }
        }, " group by exam_id");
    }

    @Override
    public Map<String, Integer> findByExamIdAndHasPassAndHasGood(String examId,
                                                                 String hasPass, String hasGood) {
        String sql = "select count(*),school_id from exammanage_apply_student where has_pass=1 and has_good=1 and exam_id in";
        return queryForInSQL(sql, null, new String[]{examId}, new MapRowMapper<String, Integer>() {

            @Override
            public String mapRowKey(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString("school_id");
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getInt("count(*)");
            }
        }, " group by school_id");
    }

    @Override
    public EmEnrollStudent setField(ResultSet rs) throws SQLException {
        return null;
    }

}
