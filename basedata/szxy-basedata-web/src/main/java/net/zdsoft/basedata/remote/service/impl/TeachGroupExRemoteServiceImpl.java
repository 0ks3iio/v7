package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.remote.service.TeachGroupExRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.framework.utils.SUtils;

@Service("teachGroupExRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachGroupExRemoteServiceImpl extends BaseRemoteServiceImpl<TeachGroupEx, String> implements TeachGroupExRemoteService {

	@Autowired
	TeachGroupExService teachGroupExService;
	
	@Override
	public String findByTeachGroupId(String[] teachGroupIds) {
		return SUtils.s(teachGroupExService.findByTeachGroupId(teachGroupIds));
	}

	@Override
	protected BaseService<TeachGroupEx, String> getBaseService() {
		return teachGroupExService;
	}

}
