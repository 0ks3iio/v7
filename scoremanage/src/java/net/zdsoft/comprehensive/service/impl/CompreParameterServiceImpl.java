package net.zdsoft.comprehensive.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.comprehensive.dao.CompreParameterDao;
import net.zdsoft.comprehensive.entity.CompreParameter;
import net.zdsoft.comprehensive.service.CompreParameterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("compreParameterService")
public class CompreParameterServiceImpl extends
		BaseServiceImpl<CompreParameter, String> implements
		CompreParameterService {

	@Autowired
	private CompreParameterDao compreParameterDao;

	@Override
	protected BaseJpaRepositoryDao<CompreParameter, String> getJpaDao() {
		return compreParameterDao;
	}

	@Override
	protected Class<CompreParameter> getEntityClass() {
		return CompreParameter.class;
	}

	
	@Override
	public List<CompreParameter> findCompreParameterByTypeAndUnitId(
			String type, String unitId) {
		if(StringUtils.isBlank(type)) {
			return findCompreParameterByUnitId(unitId);
		}
		return compreParameterDao.findCompreParameterList(type, unitId);
	}

	@Override
	public void saveAllCompreParameter(List<CompreParameter> compreParameterList) {
		compreParameterDao.saveAll(compreParameterList);
	}
	@Override
	public List<CompreParameter> findCompreParameterByUnitId(String unitId) {
		return compreParameterDao.findByUnitId(unitId);
	}



}
