package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.dao.NewGkChoResultJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

@Repository
public class NewGkChoResultJdbcDaoImpl extends BaseDao<NewGkChoResult> implements NewGkChoResultJdbcDao{

	@Override
	public void insertBatch(List<NewGkChoResult> list) {
		saveAll(list.toArray(new NewGkChoResult[] {}));
	}

	@Override
	public NewGkChoResult setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void deleteByChoiceIdAndStudentIdIn(String unitId, String choiceId, String[] stuIds) {
		StringBuffer sql=new StringBuffer("delete newgkelective_choice_result where unit_id=? and choice_id =?");
		if(stuIds==null){
			this.update(sql.toString(),new Object[]{unitId,choiceId});
		}else{
			sql.append(" and student_id in ");
			this.updateForInSQL(sql.toString(), new Object[]{unitId,choiceId}, stuIds);
		}
	}

}
