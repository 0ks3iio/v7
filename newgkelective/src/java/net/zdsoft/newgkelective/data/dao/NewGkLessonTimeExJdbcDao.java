package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;

public interface NewGkLessonTimeExJdbcDao{

	public void insertBatch(List<NewGkLessonTimeEx> list);
}
