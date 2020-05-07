package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccAttenceNoticeSetDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("eccAttenceNoticeSetService")
public class EccAttenceNoticeSetServiceImpl  extends BaseServiceImpl<EccAttenceNoticeSet, String> implements EccAttenceNoticeSetService{

	@Autowired
	private EccAttenceNoticeSetDao eccAttenceNoticeSetDao;

	@Override
	protected BaseJpaRepositoryDao<EccAttenceNoticeSet, String> getJpaDao() {
		return eccAttenceNoticeSetDao;
	}

	@Override
	protected Class<EccAttenceNoticeSet> getEntityClass() {
		return EccAttenceNoticeSet.class;
	}
	//定时发送，给相应的人员

	@Override
	public EccAttenceNoticeSet findByUnitIdAndType(String unitId, String type) {
		return eccAttenceNoticeSetDao.findByUnitIdAndType(unitId,type);
	}

}
