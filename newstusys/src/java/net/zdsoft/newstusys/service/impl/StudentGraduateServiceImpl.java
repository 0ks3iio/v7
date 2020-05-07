package net.zdsoft.newstusys.service.impl;

import java.util.List;

import net.zdsoft.newstusys.dao.StudentGraduateJdbcDao;
import net.zdsoft.newstusys.entity.StudentGraduate;
import net.zdsoft.newstusys.service.StudentGraduateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studentGraduateService")
public class StudentGraduateServiceImpl implements StudentGraduateService{
    @Autowired
	private StudentGraduateJdbcDao studentGraduateDao;
    
	@Override
	public List<StudentGraduate> findByClassIds(String[] classIds) {
		return studentGraduateDao.findByClassIds(classIds);
	}

}
