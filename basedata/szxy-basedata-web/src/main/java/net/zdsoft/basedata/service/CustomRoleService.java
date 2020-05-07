/*
 * Project: v7
 * Author : shenke
 * @(#) CustomRoleService.java Created on 2016-10-13
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.CustomRole;

/**
 * @description: TODO <b>其中方法全部没走缓存，需重构 by shenke</b>
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-13下午7:02:29
 */
public interface CustomRoleService extends BaseService<CustomRole, String> {

    /**
     * <b>未使用缓存</b>
     * 
     * @param subsystem
     * @return
     */
    List<CustomRole> findBySubsystem(String subsystem);

    public void saveAllEntitys(CustomRole... customRole);
    /**
     * 某个子系统下单位人员权限角色(不检查)
     * 缓存一周
     * @param unitId
     * @param subsystem
     * @return
     */
    public List<CustomRole> findByUnitIdAndSubsystem(String unitId, String subsystem);

    /**
     * 查询某个子系统下单位人员权限角色(检查系统给出的角色，没有会进行新增)
     * @param unitId
     * @param subsystem
     * @param isMakeUser 是否组装用户数据
     * @return
     */
	List<CustomRole> findByUnitIdAndSubsystem(String unitId, String subsystem, boolean isMakeUser);

	/**
	 * 判断userId是不是roleCode角色
	 * @param unitId
	 * @param subsystem
	 * @param roleCode
	 * @param userId
	 * @return 
	 */
	public boolean checkUserRole(String unitId, String subsystem, String roleCode,String userId);
	/**
	 * 查询某个单位某种角色用户ids
	 * @param unitId
	 * @param subsystem
	 * @param roleCode
	 * @return 缓存1小时
	 */
	public List<String> findUserIdListByUserRole(String unitId, String subsystem, String roleCode);

    void deleteById(String id);
}
