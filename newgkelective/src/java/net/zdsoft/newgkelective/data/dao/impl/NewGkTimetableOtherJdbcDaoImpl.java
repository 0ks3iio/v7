package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableOtherJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;

import org.springframework.stereotype.Repository;

@Repository
public class NewGkTimetableOtherJdbcDaoImpl extends BaseDao<NewGkTimetableOther> implements NewGkTimetableOtherJdbcDao{

	@Override
	public void insertBatch(List<NewGkTimetableOther> newGkTimetableOtherList) {
		saveAll(newGkTimetableOtherList.toArray(new NewGkTimetableOther[] {}));
	}

	@Override
	public NewGkTimetableOther setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void deleteByTimetableIdIn(String[] timeTableIds) {
		if(timeTableIds!=null){
			this.updateForInSQL("delete from newgkelective_timetable_other where timetable_id in", null, timeTableIds);
		}
	}

}
