package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.user.Role;

public interface RoleRemoteService extends BaseRemoteService<Role,String> {

    /**
     * 获取单位下的所有角色
     * 
     * @param unitId
     * @return List&lt;Role&gt;
     */
    public String findByUnitId(String unitId);

    /**
     * 数组形式entitys参数，返回list的json数据
     * 
     * @param entitys
     * @return
     */
    public String saveAllEntitys(String entitys);

	/**
	 * 查找默认的角色 type = 0 
	 * @param unitId
	 * @param roleTypeOper
	 * @return
	 */
	public String findByUnitIdAndRoleType(String unitId, int roleTypeOper);

}
