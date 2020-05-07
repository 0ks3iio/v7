package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccStuLeaveInfo;

public interface EccStuLeaveInfoService extends BaseService<EccStuLeaveInfo,String>{

	public List<EccStuLeaveInfo> findByStuDormAttIdIn(String[] stuDormAttIds);

	public List<EccStuLeaveInfo> findByLeaveIdIn(String[] leaveIds);

}
