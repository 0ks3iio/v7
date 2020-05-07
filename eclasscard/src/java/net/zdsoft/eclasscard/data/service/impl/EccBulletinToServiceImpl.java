package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccBulletinToDao;
import net.zdsoft.eclasscard.data.entity.EccBulletinTo;
import net.zdsoft.eclasscard.data.service.EccBulletinToService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("eccBulletinToService")
public class EccBulletinToServiceImpl extends
		BaseServiceImpl<EccBulletinTo, String> implements EccBulletinToService {

	@Autowired
	private EccBulletinToDao eccBulletinToDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccBulletinTo, String> getJpaDao() {
		return eccBulletinToDao;
	}

	@Override
	protected Class<EccBulletinTo> getEntityClass() {
		return EccBulletinTo.class;
	}

	@Override
	public void deleteByBulletinId(String bulletinId) {
		eccBulletinToDao.deleteByBulletinId(bulletinId);
	}


}
