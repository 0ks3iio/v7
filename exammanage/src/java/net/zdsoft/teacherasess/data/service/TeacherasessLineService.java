package net.zdsoft.teacherasess.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.dto.ClassDto;
import net.zdsoft.teacherasess.data.entity.TeacherAsessLine;

public interface TeacherasessLineService extends BaseService<TeacherAsessLine, String>{
	
	public Map<String,TeacherAsessLine> getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(String unitId,String assessId,String subjectId);

	void deleteByAssessId(String teacherAsessId);
	
	public List<ClassDto> getClassDtoByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);

}
