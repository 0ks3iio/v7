package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.StudentSelectSubjectService;
import net.zdsoft.framework.utils.SUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studentSelectSubjectRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class StudentSelectSubjectRemoteServiceImpl extends BaseRemoteServiceImpl<StudentSelectSubject, String> implements StudentSelectSubjectRemoteService {

    @Autowired
    private StudentSelectSubjectService studentSelectSubjectService;

    @Override
    public String findStuSelectMapByGradeId(String acadyear, String semester, String gradeId) {
        return SUtils.s(studentSelectSubjectService.findStuSelectMapByGradeId(acadyear, Integer.valueOf(semester), gradeId));
    }

    @Override
    protected BaseService<StudentSelectSubject, String> getBaseService() {
        return studentSelectSubjectService;
    }

	@Override
	public void updateStudentSelectsByGradeId(String unitId, String acadyear, Integer semester, String gradeId,
			List<StudentSelectSubject> resultList) {
		studentSelectSubjectService.updateStudentSelectsByGradeId(unitId, acadyear, semester, gradeId, resultList);
	}

	@Override
	public String getStuSelectByGradeId(String acadyear, String semester, String gradeId,boolean isXuankao,String[] subjectIds) {
		 return SUtils.s(studentSelectSubjectService.findStuBySubjectMap(acadyear, Integer.valueOf(semester), gradeId,isXuankao,subjectIds));
	}
}
