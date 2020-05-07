package net.zdsoft.exammanage.data.dao.impl;

import net.zdsoft.exammanage.data.dao.EmPlaceStudentJdbcDao;
import net.zdsoft.exammanage.data.entity.EmPlaceStudent;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.MultiRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmPlaceStudentDaoImpl extends BaseDao<EmPlaceStudent> implements EmPlaceStudentJdbcDao {

    @Override
    public EmPlaceStudent setField(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public Set<String> getClsSchMap(String examId) {
        String sql = "select distinct b.class_id as clsId from exammanage_place_student a,base_student b where a.student_id = b.id and a.exam_id = ?";
        List<String> list = query(sql.toString(), new Object[]{examId}, new MultiRowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("clsId");
            }
        });
        Set<String> clsIds = new HashSet<String>();
        clsIds.addAll(list);
        return clsIds;
    }

    @Override
    public Set<String> getPlaceIdSet(String examId) {
        String sql = "select distinct exam_Place_id from exammanage_place_student where exam_id=?";
        List<String> list = query(sql.toString(), new Object[]{examId}, new MultiRowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("exam_Place_id");
            }
        });
        Set<String> clsIds = new HashSet<String>();
        clsIds.addAll(list);
        return clsIds;
    }

    @Override
    public Set<String> getExamIds(String[] examIds) {
        String sql = "select distinct exam_id from exammanage_place_student where student_id is not null and exam_id in";
        List<String> list = queryForInSQL(sql, null, examIds, new MultiRowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("exam_id");
            }
        }, null);
        Set<String> clsIds = new HashSet<String>();
        clsIds.addAll(list);
        return clsIds;
    }

    @Override
    public Map<String, Integer> getAllCountMap(String[] examIds) {
        String sql = "select count(*),exam_id from exammanage_place_student where exam_id in";
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
    public int getCountByPlaceIds(String... placeIds) {
        String sql = "select count(*) from exammanage_place_student where exam_place_id in";
        return queryForIntInSQL(sql, null, placeIds);
    }

    @Override
    public Map<String, Integer> getCountMapByPlaceIds(String examId, String groupId, String[] emPlaceIds) {
        String sql = "select count(*),exam_place_id from exammanage_place_student where exam_id =? ";
        Object[] arg = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(groupId)) {
            sql = sql + " and group_id = ? ";
            arg = new Object[]{examId, groupId};
        } else {
            arg = new Object[]{examId};
        }
        sql = sql + " and exam_place_id in";
        return queryForInSQL(sql, arg, emPlaceIds, new MapRowMapper<String, Integer>() {

            @Override
            public String mapRowKey(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString("exam_place_id");
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getInt("count(*)");
            }
        }, " group by exam_place_id");
    }

}
