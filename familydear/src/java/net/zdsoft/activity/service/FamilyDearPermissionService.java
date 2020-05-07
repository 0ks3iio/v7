package net.zdsoft.activity.service;

import java.util.List;

import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.basedata.service.BaseService;

public interface FamilyDearPermissionService extends BaseService<FamilyDearPermission,String>{

	public List<FamilyDearPermission> getFamilyDearPermissionList(String unitId , String permissionType);

	public List<FamilyDearPermission> getFamilyDearPermissionListByPermissionType(String unitId,String userId,String permissionType);
}
