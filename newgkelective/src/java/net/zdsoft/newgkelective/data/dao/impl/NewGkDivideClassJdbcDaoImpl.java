package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.dao.NewGkDivideClassJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

@Repository
public class NewGkDivideClassJdbcDaoImpl extends BaseDao<NewGkDivideClass> implements NewGkDivideClassJdbcDao{

	@Override
	public void insertBatch(List<NewGkDivideClass> newGkDivideClassList) {
		saveAll(newGkDivideClassList.toArray(new NewGkDivideClass[] {}));
	}
	@Override
	public NewGkDivideClass setField(ResultSet rs) throws SQLException {
		NewGkDivideClass dc = new NewGkDivideClass();
		dc.setId(rs.getString("id"));
		dc.setDivideId(rs.getString("divide_id"));
		dc.setClassName(rs.getString("class_name"));
		dc.setClassType(rs.getString("class_type"));
		dc.setRelateId(rs.getString("relate_id"));
		dc.setSubjectIds(rs.getString("subject_ids"));
		dc.setSubjectIdsB(rs.getString("subject_ids_b"));
		dc.setSubjectType(rs.getString("subject_type"));
		dc.setBestType(rs.getString("best_type"));
		dc.setCreationTime(rs.getDate("creation_time"));
		dc.setModifyTime(rs.getDate("modify_time"));
		dc.setIsHand(rs.getString("is_hand"));
		dc.setOldClassId(rs.getString("old_class_id"));
		dc.setOldDivideClassId(rs.getString("old_divide_class_id"));
		dc.setSourceType(rs.getString("source_type"));
		dc.setBatch(rs.getString("batch"));
		dc.setOrderId(rs.getInt("order_id"));
		dc.setParentId(rs.getString("parent_id"));
		return dc;
	}

	@Override
	public void deleteByIdInOrDivideId(String[] ids, String divideId) {
		if(ids!=null){
			this.updateForInSQL("delete from newgkelective_divide_class where id in", null, ids);
		}
		if(StringUtils.isNotBlank(divideId)){
			this.update("delete from newgkelective_divide_class where divide_id=?", divideId);
		}
	}

	@Override
	public List<NewGkDivideClass> findByDivideIdAndClassType(String divideId, String sourceType, String[] classTypes,
			boolean containChildren) {
		StringBuilder sql = new StringBuilder("select * from newgkelective_divide_class where divide_id =? and source_type = ? ");
		List<Object> args = new ArrayList<>();
		args.add(divideId);
		args.add(sourceType);
		//不包括拆分的教学班
		if(!containChildren) {
			sql.append(" and ( class_type<> '2' or class_type='2' and parent_id is null )");
		}
		String postfix = (" order by class_type,subject_type desc,subject_ids,order_id,class_name");
		if(classTypes != null && classTypes.length>0) {
			sql.append(" and class_type in ");
			return queryForInSQL(sql.toString(), new Object[] {divideId,sourceType}, classTypes, new MultiRow(), postfix);
		}else {
			sql.append(postfix);
			return query(sql.toString(), new Object[] {divideId,sourceType}, new MultiRow());
		}
	}
}
