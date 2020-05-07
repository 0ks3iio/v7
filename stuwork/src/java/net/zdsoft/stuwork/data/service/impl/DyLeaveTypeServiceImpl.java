package net.zdsoft.stuwork.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.dao.DyLeaveTypeDao;
import net.zdsoft.stuwork.data.entity.DyLeaveType;
import net.zdsoft.stuwork.data.service.DyLeaveTypeService;
@Service("dyLeaveTypeService")
public class DyLeaveTypeServiceImpl extends BaseServiceImpl<DyLeaveType, String> implements DyLeaveTypeService{
    @Autowired
	private DyLeaveTypeDao dyLeaveTypeDao;
	
	@Override
	protected BaseJpaRepositoryDao<DyLeaveType, String> getJpaDao() {
		return dyLeaveTypeDao;
	}

	@Override
	protected Class<DyLeaveType> getEntityClass() {
		return DyLeaveType.class;
	}

	@Override
	public List<DyLeaveType> findLeaveTypeListByState(String unitId, int state) {
		return dyLeaveTypeDao.findLeaveTypeListByState(unitId, state);
	}

}
