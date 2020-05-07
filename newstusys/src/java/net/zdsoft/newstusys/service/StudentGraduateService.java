package net.zdsoft.newstusys.service;

import java.util.List;

import net.zdsoft.newstusys.entity.StudentGraduate;

public interface StudentGraduateService{

	public List<StudentGraduate> findByClassIds(String[] classIds);
}
