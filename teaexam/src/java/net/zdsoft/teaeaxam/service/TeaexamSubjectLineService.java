package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLine;

public interface TeaexamSubjectLineService extends BaseService<TeaexamSubjectLine, String>{

	public List<TeaexamSubjectLine> findBySubjectId(String subjectId);
	
	public void saveLine(List<TeaexamSubjectLine> lineList, Float fullScore, String subjectId);
	
	public List<TeaexamSubjectLine> findBySubjectIds(String[] subjectIds);
}
