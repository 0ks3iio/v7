package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.dao.NewGkChoRelationJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;

@Repository
public class NewGkChoRelationJdbcDaoImpl extends BaseDao<NewGkChoRelation> implements NewGkChoRelationJdbcDao{

	@Override
	public void insertBatch(List<NewGkChoRelation> list) {
		saveAll(list.toArray(new NewGkChoRelation[] {}));
	}

	@Override
	public NewGkChoRelation setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void deleteByChoiceIdAndObjectTypeIn(String unitId, String choiceId, String[] objectTypes) {
		String sql="delete newgkelective_choice_relation where unit_id=? and choice_id=? and object_type in ";
		this.updateForInSQL(sql, new Object[]{unitId,choiceId}, objectTypes);
	}

	@Override
	public void deleteByChoiceIdTypeStu(String unitId, String choiceId, String objectType, String[] studentIds) {
		String sql="delete newgkelective_choice_relation where unit_id=? and choice_id=? and object_type =? and object_value in ";
		this.updateForInSQL(sql, new Object[]{unitId,choiceId,objectType}, studentIds);
	}

	@Override
	public void deleteByTypeChoiceIds(String unitId, String objectType, String[] choiceIds) {
		StringBuffer sql=new StringBuffer("delete newgkelective_choice_relation where unit_id=? and object_type =?");
		if(choiceIds == null  || choiceIds.length == 0){
			this.update(sql.toString(),new Object[] {unitId,objectType});
		}else{
			sql.append(" and choice_id in ");
			this.updateForInSQL(sql.toString(), new Object[]{unitId,objectType}, choiceIds);
		}
	}

}
