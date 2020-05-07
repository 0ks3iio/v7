package net.zdsoft.api.base.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.api.base.dao.ApiCensusCountDao;
import net.zdsoft.api.base.entity.eis.ApiCensusCount;
import net.zdsoft.api.base.service.ApiCensusCountService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiCensusCountService")
public class ApiCensusCountServiceImpl extends BaseServiceImpl<ApiCensusCount, String> implements ApiCensusCountService {

	@Autowired
	private ApiCensusCountDao apiCensusCountDao;
	@Override
	protected BaseJpaRepositoryDao<ApiCensusCount, String> getJpaDao() {
		return apiCensusCountDao;
	}

	@Override
	protected Class<ApiCensusCount> getEntityClass() {
		return ApiCensusCount.class;
	}

	@Override
	public List<ApiCensusCount> findByKeyAndDate(String key, Date startDate,
			Date endDate) {
		return apiCensusCountDao.findByKeyAndDate(key,startDate,endDate);
	}

	@Override
	public List<ApiCensusCount> findByDate(Date dayFirst, Date dayEnd) {
		return apiCensusCountDao.findByDate(dayFirst,dayEnd);
	}

	@Override
	public List<ApiCensusCount> getInterfaceCounts(String key,
			String resultType, Date dayFirst, Date dayEnd) {
		return apiCensusCountDao.getInterfaceCounts(key,resultType,dayFirst,dayEnd);
	}

	
}
