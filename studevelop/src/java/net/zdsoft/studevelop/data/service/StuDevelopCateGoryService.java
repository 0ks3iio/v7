package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;

public interface StuDevelopCateGoryService extends BaseService<StuDevelopCateGory,String>{

	public List<StuDevelopCateGory> findListBySubjectId(String subjectId);
	
	public List<StuDevelopCateGory> findListBySubjectIdIn(String[] subjectIds);
}
