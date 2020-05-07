package net.zdsoft.activity.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.activity.dao.FamilyDearRegisterJdbcDao;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FamilyDearRegisterDaoImpl extends BaseDao<FamilyDearRegister> implements FamilyDearRegisterJdbcDao{

	@Override
	public List<FamilyDearRegister> getListByUnitIdAndTeacherIds(String unitId,String year,
			String[] activityIds, Pagination page) {
		StringBuffer sql=new StringBuffer("select * from famdear_register where status=2 and unit_Id=? ");
		List<Object> args=new ArrayList<Object>();
		args.add(unitId);
		if(StringUtils.isNotBlank(year)){
			sql.append(" and to_char(apply_time,'yyyy')=?");
			args.add(year);
		}
		if(ArrayUtils.isNotEmpty(activityIds)){
			sql.append(" and activity_id in ");
			if(page!=null){
				return queryForInSQL(sql.toString(), args.toArray(), activityIds, new MultiRow(), " order by teacher_id",page);
			}else{
				return queryForInSQL(sql.toString(), args.toArray(), activityIds, new MultiRow(), " order by teacher_id");
			}
		}else{
			sql.append(" order by teacher_id");
			if(page!=null){
				return query(sql.toString(), args.toArray(), new MultiRow(), page);
			}else{
				return query(sql.toString(), args.toArray(), new MultiRow());
			}
		}
	}

	@Override
	public FamilyDearRegister setField(ResultSet rs) throws SQLException {
		FamilyDearRegister famDearRegister=new FamilyDearRegister();
		famDearRegister.setId(rs.getString("id"));
		famDearRegister.setActivityId(rs.getString("activity_id"));
		famDearRegister.setArrangeId(rs.getString("arrange_id"));
		famDearRegister.setTeacherId(rs.getString("teacher_id"));
		famDearRegister.setTeaUserId(rs.getString("tea_user_id"));
		famDearRegister.setUnitId(rs.getString("unit_id"));
		famDearRegister.setStatus(rs.getInt("status"));
		famDearRegister.setRemark(rs.getString("remark"));
		famDearRegister.setAuditUserId(rs.getString("audit_user_id"));
		famDearRegister.setApplyTime(rs.getTimestamp("apply_time"));
		famDearRegister.setAuditTime(rs.getTimestamp("audit_time"));
		return famDearRegister;
	}

}
