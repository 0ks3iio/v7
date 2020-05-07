package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.dao.NewGkClassStudentJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;

@Repository
public class NewGkClassStudentJdbcDaoImpl extends BaseDao<NewGkClassStudent> implements NewGkClassStudentJdbcDao{

	@Override
	public void insertBatch(List<NewGkClassStudent> newGkClassStudentList) {
		saveAll(newGkClassStudentList.toArray(new NewGkClassStudent[] {}));
	}

	@Override
	public NewGkClassStudent setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void deleteBydivideIdOrClassIds(String unitId, String divideId,String[] classIds, String[] ids) {
		if(classIds ==null && ids ==null &&  StringUtils.isNotBlank(divideId)){
			this.update("delete NEWGKELECTIVE_CLASS_STUDENT where unit_id=? and divide_id=?",new Object[]{unitId,divideId});
		}else if((classIds !=null && classIds.length>0) && ids ==null){
			this.updateForInSQL("delete NEWGKELECTIVE_CLASS_STUDENT where unit_id=? and divide_id=? and class_id in ",new Object[]{unitId,divideId},classIds);
		}else if((ids !=null && ids.length>0) && classIds ==null){
			this.updateForInSQL("delete NEWGKELECTIVE_CLASS_STUDENT where id in ",null,ids);
		}
	}

	@Override
	public void deleteByClassIdAndStuids(String classId, String[] stuids) {
		this.updateForInSQL("delete NEWGKELECTIVE_CLASS_STUDENT where class_id=? and student_id in", new Object[]{classId}, stuids);
	}

	@Override
	public List<String> findArrangeStudentIdWithMaster(String divideId, String classType) {
		StringBuffer sql=new StringBuffer("select distinct(b.student_id) student_id from NEWGKELECTIVE_CLASS_STUDENT b where b.divide_id=? ");
		List<Object> objList=new ArrayList<>();
		objList.add(divideId);
		sql.append(" and  exists(select 1 from newgkelective_divide_class t where t.divide_id=b.divide_id and t.id=b.class_id and t.class_type=? )");
		objList.add(classType);
		return query(sql.toString(), objList.toArray(new Object[0]),new MultiRowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("student_id");
			}
			
		});
	}

}
