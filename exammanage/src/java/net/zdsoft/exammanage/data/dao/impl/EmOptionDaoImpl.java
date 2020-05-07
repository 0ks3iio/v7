package net.zdsoft.exammanage.data.dao.impl;

import net.zdsoft.exammanage.data.dao.EmOptionJdbcDao;
import net.zdsoft.exammanage.data.entity.EmOption;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmOptionDaoImpl extends BaseDao<EmOption> implements EmOptionJdbcDao {

    @Override
    public Set<String> getExamRegionId(String examId) {
        String sql = "select distinct c.exam_region_id from exammanage_place_student a, exammanage_place b, exammanage_option c where a.exam_place_id=b.id and b.option_id=c.id and a.exam_id=?";
        List<String> list = query(sql.toString(), new Object[]{examId}, new MultiRowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("exam_region_id");
            }
        });
        Set<String> clsIds = new HashSet<String>();
        clsIds.addAll(list);
        return clsIds;
    }

    @Override
    public EmOption setField(ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
