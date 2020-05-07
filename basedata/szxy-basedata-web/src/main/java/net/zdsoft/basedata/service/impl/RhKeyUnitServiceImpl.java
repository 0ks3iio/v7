package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.RhKeyUnitDao;
import net.zdsoft.basedata.entity.RhKeyUnit;
import net.zdsoft.basedata.service.RhKeyUnitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("rhKeyUnitService")
public class RhKeyUnitServiceImpl extends BaseServiceImpl<RhKeyUnit, String> implements RhKeyUnitService{

	@Autowired
	private RhKeyUnitDao rhKeyUnitDao;
	@Override
	public List<RhKeyUnit> findByUkey(String ukey) {
		return rhKeyUnitDao.findByUkey(ukey);
	}
	@Override
	public List<RhKeyUnit> findByUnitId(String unitId) {
		return rhKeyUnitDao.findByUnitId(unitId);
	}
	@Override
	public List<RhKeyUnit> findByUnitIdAnduKeyId(String unitId, String uKeyId) {
		return rhKeyUnitDao.findByUnitIdAndUkey(unitId,uKeyId);
	}
	@Override
	protected BaseJpaRepositoryDao<RhKeyUnit, String> getJpaDao() {
		return rhKeyUnitDao;
	}
	@Override
	protected Class<RhKeyUnit> getEntityClass() {
		return RhKeyUnit.class;
	}

}
