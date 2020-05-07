package net.zdsoft.basedata.remote.service.impl;

import java.util.List;

import net.zdsoft.basedata.entity.ImportResult;
import net.zdsoft.basedata.remote.service.ImportResultRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.ImportResultService;
import net.zdsoft.framework.entity.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("importResultRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class ImportResultRemoteServiceImpl extends BaseRemoteServiceImpl<ImportResult,String> implements ImportResultRemoteService {

	@Autowired
    private ImportResultService importResultService;
	
	@Override
	public List<ImportResult> findListByUserIdAndBusinessId(String userId,
			String businessId, Pagination page) {
		return importResultService.findListByUserIdAndBusinessId(userId, businessId, page);
	}

	@Override
	public List<ImportResult> findListByBusinessId(String businessId,
			Pagination page) {
		return  importResultService.findListByBusinessId(businessId, page);
	}

	@Override
	protected BaseService<ImportResult, String> getBaseService() {
		return importResultService;
	}

}
