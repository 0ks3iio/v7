package net.zdsoft.gkelective.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.remote.service.GkResultRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkResultRemoteService")
public class GkResultRemoteServiceImpl extends BaseRemoteServiceImpl<GkResult, String> implements GkResultRemoteService{

	@Autowired
	private GkResultService gkResultService;
	
	@Override
	protected BaseService<GkResult, String> getBaseService() {
		return gkResultService;
	}

	@Override
	public String getGkResultByGradeId(String gradeId) {
		return SUtils.s(gkResultService.getGkResultByGradeId(gradeId));
	}

}
