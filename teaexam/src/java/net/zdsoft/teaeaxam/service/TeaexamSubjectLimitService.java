package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLimit;

public interface TeaexamSubjectLimitService extends BaseService<TeaexamSubjectLimit, String>{

	public void saveLimit(String examId, String subjectId, String teacherIds);
	
	public void deleteBySubjectId(String subjectId);
	
	public TeaexamSubjectLimit findByExamIdAndSubId(String examId, String subjectId);

	public List<TeaexamSubjectLimit> limitList(String[] examIds);
	
	public List<TeaexamSubjectLimit> findBySubjectIds(String[] subIds);
}
