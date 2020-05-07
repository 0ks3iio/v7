package net.zdsoft.api.openapi.pushjob.service.impl;

import java.util.List;

import net.zdsoft.api.base.dao.ApiPushJobDao;
import net.zdsoft.api.base.entity.eis.ApiPushJob;
import net.zdsoft.api.openapi.pushjob.service.ApiPushJobService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiPushJobService")
public class ApiPushJobServiceImpl extends BaseServiceImpl<ApiPushJob, String> implements ApiPushJobService {

	@Autowired
	private ApiPushJobDao  basePushJobDao;
	
	@Override
	protected BaseJpaRepositoryDao<ApiPushJob, String> getJpaDao() {
		return basePushJobDao;
	}

	@Override
	protected Class<ApiPushJob> getEntityClass() {
		return ApiPushJob.class;
	}
	
    @Override
	public List<ApiPushJob> getBasePushJobAll() {
    	List<ApiPushJob> allJobs = basePushJobDao.findAll();
    	if(CollectionUtils.isNotEmpty(allJobs)){
    		allJobs = EntityUtils.filter2(allJobs, t->{
    			return t.getIsDeleted() == 0;
    		});
    	}
		return allJobs;
	}
}
