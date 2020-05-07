package net.zdsoft.basedata.dingding.service.impl;

import java.util.List;

import net.zdsoft.basedata.dingding.dao.DingDingUnitOpenDao;
import net.zdsoft.basedata.dingding.entity.DdUnitOpen;
import net.zdsoft.basedata.dingding.service.DingDingUnitOpenService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dingDingUnitOpenService")
public class DingDingUnitOpenServiceImpl extends
		BaseServiceImpl<DdUnitOpen, String> implements DingDingUnitOpenService {

	@Autowired
	DingDingUnitOpenDao dingDingUnitOpenDao;

	@Override
	public List<DdUnitOpen> findByState(Integer state) {
		return dingDingUnitOpenDao.findByState(state);
	}

	public List<DdUnitOpen> findByUnitIdAndState(String unitId, Integer state) {
		return dingDingUnitOpenDao.findByUnitIdAndState(unitId, state);
	}

	@Override
	protected BaseJpaRepositoryDao<DdUnitOpen, String> getJpaDao() {
		// TODO Auto-generated method stub
		return dingDingUnitOpenDao;
	}

	@Override
	protected Class<DdUnitOpen> getEntityClass() {
		// TODO Auto-generated method stub
		return DdUnitOpen.class;
	}
}
