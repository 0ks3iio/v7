package net.zdsoft.scoremanage.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.scoremanage.remote.service.SubjectInfoRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subjectInfoRemoteService")
public class SubjectInfoRemoteServiceImpl extends BaseRemoteServiceImpl<SubjectInfo, String> implements SubjectInfoRemoteService {

	@Autowired
	private SubjectInfoService subjectInfoService;
	
	@Override
	protected BaseService<SubjectInfo, String> getBaseService() {
		return subjectInfoService;
	}

	@Override
	public String findByExamIdGradeId(String examId, String gradeId) {
		return SUtils.s(subjectInfoService.findByExamIdGradeId(examId,gradeId));
	}

}
