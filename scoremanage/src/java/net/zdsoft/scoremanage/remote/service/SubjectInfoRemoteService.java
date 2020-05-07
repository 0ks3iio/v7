package net.zdsoft.scoremanage.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;

public interface SubjectInfoRemoteService extends BaseRemoteService<SubjectInfo,String>{
	
	/**
	 * 查询某个年级在某个考试下的考试科目
	 * @param examId
	 * @param gradeId
	 * @return List<SubjectInfo>
	 */
	public String findByExamIdGradeId(String examId, String gradeId);
	
}
