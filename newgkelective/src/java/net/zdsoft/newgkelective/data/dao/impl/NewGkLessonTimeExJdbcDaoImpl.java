package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.dao.NewGkLessonTimeExJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;

@Repository
public class NewGkLessonTimeExJdbcDaoImpl extends BaseDao<NewGkLessonTimeEx> implements NewGkLessonTimeExJdbcDao {

	@Override
	public NewGkLessonTimeEx setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void insertBatch(List<NewGkLessonTimeEx> list) {
		saveAll(list.toArray(new NewGkLessonTimeEx[] {}));
	}
	
}
