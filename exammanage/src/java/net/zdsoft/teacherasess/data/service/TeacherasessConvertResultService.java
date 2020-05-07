package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertResult;

public interface TeacherasessConvertResultService extends BaseService<TeacherasessConvertResult, String>{

	public void statResult(String convertId, String acadyear, String semester);

	public List<TeacherasessConvertResult> findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(String unitId,String convertId,String subjectId,String...studentIds);

	public List<TeacherasessConvertResult> findListByConvertId(String convertId, String subjectId, Pagination page);
	
	public void deleteByConvertId(String convertId, String subjectId);
}