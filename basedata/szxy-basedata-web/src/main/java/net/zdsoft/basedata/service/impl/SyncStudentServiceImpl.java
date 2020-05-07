package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentSelectSubjectService;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.basedata.service.TeachClassStuService;
@Service("syncStudentService")
public class SyncStudentServiceImpl extends SyncBasedataService<Student, String> {
	
	@Autowired
	private StudentSelectSubjectService studentSelectSubjectService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private SemesterService semesterService;
	@Override
	protected void add(Student... t) {
		
	}

	@Override
	protected void update(Student... t) {
		
	}

	@Override
	protected void delete(String... studentId) {
		//删除选课信息
		studentSelectSubjectService.deleteByStudentIds(studentId);
		//删除教学班学生
		teachClassStuService.deleteByStudentIds(studentId);
	}

	@Override
	protected String preDelete(String... ids) {
		Semester semester = semesterService.getCurrentSemester(2);
		for(String k :ids){
			if(semester!=null){
				List<StudentSelectSubject> list = studentSelectSubjectService.findListBy(new String[]{"semester","acadyear","studentId","isDeleted"}, new Object[]{semester.getAcadyear(),semester.getSemester(),k,0});
				if(CollectionUtils.isNotEmpty(list)){
					return "该学生在教务基础信息学生选课中不能删除！";
				}
			}
		}
		
		return null;
	}

	@Override
	protected String preUpdate(Student t) {
		return null;
	}

	@Override
	protected String preAdd(Student t) {
		return null;
	}

}
