package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.remote.service.ClassHourExRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.framework.utils.SUtils;

@com.alibaba.dubbo.config.annotation.Service
@Service("classHourExRemoteService")
public class ClassHourExRemoteServiceImpl extends BaseRemoteServiceImpl<ClassHourEx,String>
	implements ClassHourExRemoteService {
	
	@Autowired
	private ClassHourExService classHourExService;
	
	@Override
	protected BaseService<ClassHourEx, String> getBaseService() {
		return classHourExService;
	}

	@Override
	public String findListByHourIdIn(String[] classHourIds) {
		return SUtils.s(classHourExService.findByClassHourIdIn(classHourIds));
	}


}
