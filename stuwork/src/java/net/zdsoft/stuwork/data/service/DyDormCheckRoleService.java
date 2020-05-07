package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormCheckRole;

public interface DyDormCheckRoleService extends BaseService<DyDormCheckRole,String>{
	public List<DyDormCheckRole> getCheckRolesBy(String schoolId,String acadyear,String semester);
	
	public void deleteBy(DormSearchDto dormDto);
	
}
