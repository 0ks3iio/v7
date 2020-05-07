package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.dto.StuDevelopSubjectDto;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;

public interface StuDevelopSubjectService extends BaseService<StuDevelopSubject, String>{

	public void saveStuDevelopSubject(StuDevelopSubjectDto stuDevelopSubjectDto);
	
	public List<StuDevelopSubject> stuDevelopSubjectList(String unitId, String acadyear, String semester, String gradeId);
	
	public void deleteByIds(String[] ids);
	
	public void doCopy(String oldAcadyear, String oldSemester, String[] gradeIds, String acadyear, String smester, String unitId);
	
	public void copySubject(String oldAcadyear, String oldSemester, String[] gradeIds, String acadyear, String semester, String unitId);
}
