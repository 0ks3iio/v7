package net.zdsoft.career.data.service.impl;

import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.career.data.dao.CarTypeResultDao;
import net.zdsoft.career.data.entity.CarTypeResult;
import net.zdsoft.career.data.service.CarTypeResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("carTypeResultService")
public class CarTypeResultServiceImpl extends BaseServiceImpl<CarTypeResult,String>implements CarTypeResultService{

	@Autowired
	private CarTypeResultDao carPlanResultDao;
	
	@Override
	protected BaseJpaRepositoryDao<CarTypeResult, String> getJpaDao() {
		return carPlanResultDao;
	}

	@Override
	protected Class<CarTypeResult> getEntityClass() {
		return CarTypeResult.class;
	}

	@Override
	public CarTypeResult findByResultType(String resultType) {
		return carPlanResultDao.findByResultType(resultType);
	}

	@Override
	public Set<String> findAllTypes() {
		return carPlanResultDao.findAllTypes();
	}

}
