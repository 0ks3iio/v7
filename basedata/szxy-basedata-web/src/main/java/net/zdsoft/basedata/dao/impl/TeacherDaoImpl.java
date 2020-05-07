package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.dao.TeacherExtDao;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.framework.dao.BaseDao;

public class TeacherDaoImpl extends BaseDao<Teacher> implements TeacherExtDao {

	@Override
	public Teacher setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public int[] updateCardNumber(List<String[]> techerCardList) {
		String sql = "update base_teacher set card_number=? where identity_card=?";
		List<Object[]> objList = new ArrayList<Object[]>();
		for (String[] tea : techerCardList) {
			Object[] obj = new Object[] { tea[1], tea[0] };
			objList.add(obj);
		}
		return batchUpdate(sql, objList, new int[] { Types.VARCHAR,
				Types.VARCHAR });
	}

}
