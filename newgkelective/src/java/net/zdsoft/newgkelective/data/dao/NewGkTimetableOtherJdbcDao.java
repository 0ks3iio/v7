package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;

public interface NewGkTimetableOtherJdbcDao {
	
	public void insertBatch(List<NewGkTimetableOther> newGkTimetableOtherList);
	
	public void deleteByTimetableIdIn(String[] timeTableIds);
	
}
