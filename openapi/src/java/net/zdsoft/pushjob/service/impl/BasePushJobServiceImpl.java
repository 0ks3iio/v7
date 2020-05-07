package net.zdsoft.pushjob.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.pushjob.dao.BasePushJobDao;
import net.zdsoft.pushjob.entity.BasePushJob;
import net.zdsoft.pushjob.service.BasePushJobService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("basePushJobService")
public class BasePushJobServiceImpl extends BaseServiceImpl<BasePushJob, String> implements BasePushJobService {

	@Autowired
	private BasePushJobDao  basePushJobDao;
	
	@Override
	protected BaseJpaRepositoryDao<BasePushJob, String> getJpaDao() {
		return basePushJobDao;
	}

	@Override
	protected Class<BasePushJob> getEntityClass() {
		return BasePushJob.class;
	}
	
    @Override
	public List<BasePushJob> getBasePushJobAll() {
    	List<BasePushJob> allJobs = basePushJobDao.findAll();
    	if(CollectionUtils.isNotEmpty(allJobs)){
    		allJobs = EntityUtils.filter2(allJobs, t->{
    			return t.getIsDeleted() == 0;
    		});
    	}
		return allJobs;
	}
}
