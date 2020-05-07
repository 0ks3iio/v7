package net.zdsoft.teacherasess.data.service;

import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.entity.TeacherAsessCheck;

public interface TeacherasessCheckService extends BaseService<TeacherAsessCheck, String>{

	void deleteByAssessId(String teacherAsessId);
	
	public Map<String,TeacherAsessCheck> getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(String unitId,String assessId,String subjectId);

}
