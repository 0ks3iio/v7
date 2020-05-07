package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.OperationUrl;
import net.zdsoft.basedata.remote.service.OperationUrlRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.OperationUrlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operationUrlRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class OperationUrlRmoteServiceImpl extends
		BaseRemoteServiceImpl<OperationUrl, String> implements
		OperationUrlRemoteService {

	@Autowired
	private OperationUrlService operationUrlService;

	@Override
	protected BaseService<OperationUrl, String> getBaseService() {
		return operationUrlService;
	}

}
