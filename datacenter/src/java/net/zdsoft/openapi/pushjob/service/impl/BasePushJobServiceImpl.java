package net.zdsoft.openapi.pushjob.service.impl;

import java.util.List;

import net.zdsoft.base.dao.OpenApiPushJobDao;
import net.zdsoft.base.entity.eis.OpenApiPushJob;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.openapi.pushjob.service.BasePushJobService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("basePushJobService")
public class BasePushJobServiceImpl extends BaseServiceImpl<OpenApiPushJob, String> implements BasePushJobService {

	@Autowired
	private OpenApiPushJobDao  basePushJobDao;
	
	@Override
	protected BaseJpaRepositoryDao<OpenApiPushJob, String> getJpaDao() {
		return basePushJobDao;
	}

	@Override
	protected Class<OpenApiPushJob> getEntityClass() {
		return OpenApiPushJob.class;
	}
	
    @Override
	public List<OpenApiPushJob> getBasePushJobAll() {
    	List<OpenApiPushJob> allJobs = basePushJobDao.findAll();
    	if(CollectionUtils.isNotEmpty(allJobs)){
    		allJobs = EntityUtils.filter2(allJobs, t->{
    			return t.getIsDeleted() == 0;
    		});
    	}
		return allJobs;
	}
}
