package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.basedata.remote.service.TeachClassExRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachClassExService;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachClassExRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachClassExRemoteServiceImpl extends BaseRemoteServiceImpl<TeachClassEx, String> implements
TeachClassExRemoteService{
	
	@Autowired
	private TeachClassExService teachClassExService;

	@Override
	public String findByTeachClassIdIn(String[] teachClassIds, boolean isMake) {
		return SUtils.s(teachClassExService.findByTeachClassIdIn(teachClassIds, isMake));
	}

	@Override
	public void deleteByTeachClass(String[] teachClassId) {
		teachClassExService.deleteByTeachClass(teachClassId);
	}

	@Override
	public String findByTeachClassId(String teachClassId) {
		return SUtils.s(teachClassExService.findByTeachClassId(teachClassId));
	}

	@Override
	protected BaseService<TeachClassEx, String> getBaseService() {
		return teachClassExService;
	}

}
