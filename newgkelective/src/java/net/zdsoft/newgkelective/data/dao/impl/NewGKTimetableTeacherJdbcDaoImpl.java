package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableTeacherJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;

import org.springframework.stereotype.Repository;

@Repository
public class NewGKTimetableTeacherJdbcDaoImpl extends BaseDao<NewGkTimetableTeacher> implements NewGkTimetableTeacherJdbcDao {
	
    @Override
    public NewGkTimetableTeacher setField(ResultSet rs) throws SQLException {
        return null;
    }

	@Override
	public void insertBatch(List<NewGkTimetableTeacher> timetableTeacherList) {
		saveAll(timetableTeacherList.toArray(new NewGkTimetableTeacher[] {}));
	}

	@Override
	public void deleteByIdInOrTimetableIdIn(String[] ids, String[] timetableIds) {
		if(ids!=null){
			this.updateForInSQL("delete from newgkelective_timetable_teach where id in",null,ids);
		}
			
		if(timetableIds!=null){
			this.updateForInSQL("delete from newgkelective_timetable_teach where timetable_id in",null,timetableIds);
		}
		
	}
    
}
