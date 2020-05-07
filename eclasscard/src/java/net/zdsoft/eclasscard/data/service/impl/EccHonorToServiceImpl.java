package net.zdsoft.eclasscard.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccHonorToDao;
import net.zdsoft.eclasscard.data.entity.EccHonorTo;
import net.zdsoft.eclasscard.data.service.EccHonorToService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("eccHonorToService")
public class EccHonorToServiceImpl extends BaseServiceImpl<EccHonorTo,String> implements EccHonorToService{

	@Autowired
	private EccHonorToDao eccHonorToDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccHonorTo, String> getJpaDao() {
		return eccHonorToDao;
	}

	@Override
	protected Class<EccHonorTo> getEntityClass() {
		return EccHonorTo.class;
	}

	@Override
	public List<EccHonorTo> findByObjectIdIn(String[] objectIds) {
		return eccHonorToDao.findByObjectIdIn(objectIds);
	}

	@Override
	public List<EccHonorTo> findByHonorIdIn(String[] honorIds) {
		return eccHonorToDao.findByHonorIdIn(honorIds);
	}

	@Override
	public void deleteByHonorId(String honorId) {
		eccHonorToDao.deleteByHonorId(honorId);
	}

}
