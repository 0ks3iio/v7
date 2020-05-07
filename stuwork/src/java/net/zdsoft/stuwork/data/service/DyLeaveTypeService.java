package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyLeaveType;

public interface DyLeaveTypeService extends BaseService<DyLeaveType, String>{
	
	public List<DyLeaveType> findLeaveTypeListByState(String unitId, int state);

}
