package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.SysPlatformModel;
import net.zdsoft.system.remote.service.SysPlatformModelRemoteService;
import net.zdsoft.system.service.server.SysPlatformModelService;

@Service("sysPlatformModelRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SysPlatformModelRemoteServiceImpl extends BaseRemoteServiceImpl<SysPlatformModel, Integer> implements
		SysPlatformModelRemoteService {

	@Autowired
	private SysPlatformModelService sysPlatformModelService;
	
	@Override
	protected BaseService<SysPlatformModel, Integer> getBaseService() {
		return sysPlatformModelService;
	}

	

}
