package net.zdsoft.exammanage.data.dao.impl;

import net.zdsoft.exammanage.data.dao.EmPlaceJdbcDao;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.MultiRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmPlaceDaoImpl extends BaseDao<EmPlace> implements EmPlaceJdbcDao {

    @Override
    public EmPlace setField(ResultSet rs) throws SQLException {
        EmPlace emPlace = new EmPlace();
        emPlace.setId(rs.getString("id"));
        emPlace.setExamId(rs.getString("exam_id"));
        emPlace.setSchoolId(rs.getString("school_id"));
        emPlace.setExamPlaceCode(rs.getString("exam_place_code"));
        emPlace.setPlaceId(rs.getString("place_id"));
        emPlace.setCount(rs.getInt("count"));
        emPlace.setOptionId(rs.getString("option_id"));
        return emPlace;
    }

    @Override
    public List<EmPlace> getEmPlaceOrder(String examId, String[] optionIds, Pagination page) {
        String sql = new String("select p.* from exammanage_place p,exammanage_region r,exammanage_option o where p.option_id=o.id and o.exam_region_id=r.id and p.exam_id =? and option_id in ");
        if (page != null) {
            return queryForInSQL(sql, new Object[]{examId}, optionIds, new MultiRow(), " order by r.exam_region_code, o.option_code,p.exam_place_code", page);
        } else {
            return queryForInSQL(sql, new Object[]{examId}, optionIds, new MultiRow(), " order by r.exam_region_code, o.option_code,p.exam_place_code");
        }
    }

    @Override
    public Set<String> getOptionId(String examId) {
        String sql = "select distinct b.option_id as optionId from exammanage_place_student a, exammanage_place b where a.exam_place_id=b.id and a.exam_id=?";
        List<String> list = query(sql, new Object[]{examId}, new MultiRowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("optionId");
            }
        });
        Set<String> clsIds = new HashSet<String>();
        clsIds.addAll(list);
        return clsIds;
    }

}
