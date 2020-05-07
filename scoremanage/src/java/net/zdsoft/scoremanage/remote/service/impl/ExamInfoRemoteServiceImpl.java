package net.zdsoft.scoremanage.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.remote.service.ExamInfoRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("examInfoRemoteService")
public class ExamInfoRemoteServiceImpl extends BaseRemoteServiceImpl<ExamInfo, String> implements ExamInfoRemoteService{

	@Autowired
	private ExamInfoService examInfoService;
	
	@Override
	protected BaseService<ExamInfo, String> getBaseService() {
		return examInfoService;
	}

	@Override
	public String findExamInfoListAll(String unitId, String acadyear, String semester, String gradeId) {
		return SUtils.s(examInfoService.findExamInfoListAll(unitId,acadyear,semester,gradeId));
	}

}
