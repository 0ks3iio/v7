package net.zdsoft.newstusys.dao;

import java.util.List;

import net.zdsoft.newstusys.entity.StudentGraduate;

public interface StudentGraduateJdbcDao{

	public List<StudentGraduate> findByClassIds(String[] classIds);
}
