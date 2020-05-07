package net.zdsoft.stuwork.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyPermission;


public interface DyPermissionService extends BaseService<DyPermission, String>{

	/**
	 * 单位下权限List
	 * @param unitId
	 * @return
	 */
	public List<DyPermission> findListByUnitId(String unitId,String classsType,String permissionType);

	/**
	 * 根据班级保存权限
	 * @param classId
	 * @param userIds
	 */
	public void savePermission(String[] classIds, String[] userIds,String unitId,boolean isAll,String classsType,String permissionType);
	/**
	 * 查询该用户的班级权限(班主任有班级权限，年级组长有年级下所有班级权限)
	 * @param userId
	 * @return  班级id的set
	 */
	public Set<String> findClassSetByUserId(String userId);

	/**
	 * 查询该用户的班级权限(班主任有班级权限，年级组长有年级下所有班级权限)
	 * @param userId
	 * @return  班级id的set
	 */
	public Set<String> findClassSetByUserIdClaType(String userId,String classsType,String permissionType);

}
