package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.newgkelective.data.dao.NewGKStudentRangeJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;

@Repository
public class NewGKStudentRangeJdbcDaoImpl extends BaseDao<NewGKStudentRange> implements NewGKStudentRangeJdbcDao {
    @Override
    public NewGKStudentRange setField(ResultSet rs) throws SQLException {
    	NewGKStudentRange range =new NewGKStudentRange();
    	range.setId(rs.getString("id"));
    	range.setDivideId(rs.getString("divide_id"));
    	range.setSubjectId(rs.getString("subject_id"));
    	range.setSubjectType(rs.getString("subject_type"));
    	range.setStudentId(rs.getString("student_id"));
    	range.setRange(rs.getString("range"));
    	range.setModifyTime(rs.getDate("modify_time"));
    	range.setUnitId(rs.getString("unit_id"));
        return range;
    }

    @Override
    public Map<String, Integer> findCountMapByDivideIdAndSubjectType(String unitId,String divideId, String subjectType) {
        String sql = "select subject_id,range,count(*) c  from newgkelective_stu_range where unit_id =? and divide_id=? and subject_type=? group by subject_id,range order by subject_id,range";
        return queryForMap(sql, new Object[] {unitId, divideId,subjectType}, new MapRowMapper<String, Integer>() {
            @Override
            public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("subject_id")+"_"+rs.getString("range");
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("c");
            }
        });
    }

	@Override
	public void insertBatch(List<NewGKStudentRange> studentRangeList) {
		saveAll(studentRangeList.toArray(new NewGKStudentRange[] {}));
	}

	@Override
	public void deleteByDivideIdAnd(String unitId, String divideId, String subjectId, String subjectType,
			String[] range) {
		this.updateForInSQL("delete newgkelective_stu_range where unit_id=? and divide_Id=? and subject_Id=? and subject_Type=? and range in", 
				new Object[]{unitId,divideId,subjectId,subjectType},range);
	}

	@Override
	public void deleteByDivideIdAnd(String unitId, String divideId, String subjectId, String subjectType) {
		this.update("delete newgkelective_stu_range where unit_id=? and divide_Id=? and subject_Id=? and subject_Type=? ",new Object[]{
				unitId,divideId,subjectId,subjectType
		});
	}

	@Override
	public void deleteByDivideIdAnd(String unitId, String divideId, String subjectType) {
		this.update("delete newgkelective_stu_range where unit_id=? and divide_Id=? and subject_Type=? ",new Object[]{
				unitId,divideId,subjectType
		});
	}

	@Override
	public void deleteByDivideId(String unitId, String divideId) {
		this.update("delete newgkelective_stu_range where unit_id=? and divide_Id=?",new Object[]{
				unitId,divideId
		});
	}
    
}
