package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;

public interface StuDevelopProjectService extends BaseService<StuDevelopProject,String>{

	public void saveStuDevelopProject(StuDevelopProject stuDevelopProject);
	
	public void deleteStuDevelopProject(String[] projectIds);
	
	public List<StuDevelopProject> stuDevelopProjectList(String unitId,
			String acadyear, String semester, String... gradeId);
	
	public void copyProject(String oldAcadyear, String oldSemester, String[] gradeIds, String acadyear, String semester, String unitId);
}
