package net.zdsoft.bigdata.frame.data.canal.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.frame.data.canal.dao.CanalJobDao;
import net.zdsoft.bigdata.frame.data.canal.entity.CanalJob;
import net.zdsoft.bigdata.frame.data.canal.service.CanalJobService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("canalJobService")
public class CanalJobServiceImpl extends BaseServiceImpl<CanalJob, String>
		implements CanalJobService {

	@Autowired
	CanalJobDao canalJobDao;
	
	@Override
	public void updateJobById(String jobId,BigDecimal submitCount,Date lastCommitDate){
		canalJobDao.updateJobById(jobId, submitCount, lastCommitDate);
	}

	@Override
	protected BaseJpaRepositoryDao<CanalJob, String> getJpaDao() {
		return canalJobDao;
	}

	@Override
	protected Class<CanalJob> getEntityClass() {
		return CanalJob.class;
	}

}
