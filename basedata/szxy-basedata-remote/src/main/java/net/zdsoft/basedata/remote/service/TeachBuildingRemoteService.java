package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachBuilding;

public interface TeachBuildingRemoteService extends BaseRemoteService<TeachBuilding,String>{
	
	 /**
     * 批量查询楼层信息
     * 
     * @param ids
     * @return JSON(Map<id,name>)
     */
	public String findTeachBuildMap(String[] ids);
	/**
	 * 查询某单位楼层
	 * @param unitId
	 * @return
	 */
	public String findTeachBuildListByUnitId(String unitId);
	/**
	 * 批量查询楼层
	 * @param uidList
	 * @return
	 */
	public String findByUnitIdIn(String[] uidList);

}
