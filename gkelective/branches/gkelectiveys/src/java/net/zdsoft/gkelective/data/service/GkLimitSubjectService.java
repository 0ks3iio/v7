package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;

public interface GkLimitSubjectService extends BaseService<GkLimitSubject, String>{
	
	public List<GkLimitSubject> findBySubjectArrangeId(String subjectArrangeId);
	
	public void deleteBySubjectArrangeId(String arrangeSubjectId);
	/**
	 * 同时清除选课结果数据
	 * @param ent
	 */
	public void saveLimitSubject(GkLimitSubject ent);
}
