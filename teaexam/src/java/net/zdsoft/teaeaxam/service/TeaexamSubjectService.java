package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;

public interface TeaexamSubjectService extends BaseService<TeaexamSubject, String>{
	public List<TeaexamSubject> findByExamIds(String[] examIds);  
}
