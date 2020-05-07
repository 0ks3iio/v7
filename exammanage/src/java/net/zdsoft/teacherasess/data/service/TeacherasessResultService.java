package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.entity.TeacherAsessResult;

public interface TeacherasessResultService extends BaseService<TeacherAsessResult, String>{

	public List<TeacherAsessResult> findByUnitIdAndAsessIdAndClassTypeAndSubjectId(String unitId,String assessId,String classType,String subjectId);
	
	public List<TeacherAsessResult> findByUnitIdAndAsessIdAndSubjectId(String unitId,String assessId,String subjectId);
	
	public List<Course> findByUnitIdAndAsessId(String unitId,String asessId);
	
	public void deleteByAssessId(String unitId,String... assessId);
}
