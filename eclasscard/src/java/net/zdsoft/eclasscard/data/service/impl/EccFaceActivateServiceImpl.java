package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccFaceActivateDao;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("eccFaceActivateService")
public class EccFaceActivateServiceImpl extends BaseServiceImpl<EccFaceActivate,String> implements EccFaceActivateService{

	@Autowired
	private EccFaceActivateDao eccFaceActivateDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccFaceActivate, String> getJpaDao() {
		return eccFaceActivateDao;
	}
	
	@Override
	protected Class<EccFaceActivate> getEntityClass() {
		return EccFaceActivate.class;
	}
	
	@Override
	public EccFaceActivate findByInfoId(String unitId, String infoId) {
		return eccFaceActivateDao.findByInfoId(unitId,infoId);
	}

	@Override
	public List<EccFaceActivate> findListByUnitId(String unitId) {
		return eccFaceActivateDao.findListByUnitId(unitId);
	}

	@Override
	public void updateNeedLowerUnitId(String unitId) {
		eccFaceActivateDao.updateNeedLowerUnitId(unitId);
		
	}

	@Override
	public List<EccFaceActivate> findAllNeedLower() {
		return eccFaceActivateDao.findAllNeedLower();
	}

	@Override
	public void deleteByInfoId(String unitId, String infoId) {
		 eccFaceActivateDao.deleteByInfoId(unitId,infoId);
	}
}
