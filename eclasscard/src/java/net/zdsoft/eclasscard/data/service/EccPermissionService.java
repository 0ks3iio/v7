package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccPermission;


public interface EccPermissionService extends BaseService<EccPermission, String>{

	public void savePermission(String eccName,String eccNames,String[] userIds,Boolean isAll,String unitId);

	public Map<String,List<EccPermission>> findbyEccInfoMap(String[] infoNames,String unitId);
	
	public void deleteByEccName(String eccName,String unitId);

}
