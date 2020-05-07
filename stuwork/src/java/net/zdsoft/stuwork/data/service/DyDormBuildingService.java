package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;

public interface DyDormBuildingService extends BaseService<DyDormBuilding, String>{
	/**
	 * 根据id查找stuwork_dorm_building数据
	 * @param id
	 * @param
	 * @return
	 */
	public DyDormBuilding getDormBuildingById(String id);
	/**
	 * 根据id删除stuwork_dorm_building数据
	 * @param id
	 * @param
	 * @return
	 */
	public void deletedById(String id);
	/**
	 * 根据id 判断是否已安排学生
	 * @param id
	 * @param
	 * @return
	 */
	public boolean getIsHaveStu(String unitId,String id);
	/**
	 * 根据unitId 查找stuwork_dorm_building list
	 * @param unit_id
	 * @param 
	 * @return
	 */
	public List<DyDormBuilding> findByUnitId(String unitId);
	/**
	 * 根据unitId name查找stuwork_dorm_building list
	 * @param unit_id name
	 * @param 
	 * @return
	 */
	public List<DyDormBuilding> getBuildsByName(String unitId,String name);
	/**
	 * 根据unitId查找stuwork_dorm_building list
	 * @param unit_id
	 * @param 
	 * @return
	 */
	public List<DyDormBuilding> getDormBuildingsByUnitId(String unitId,String acadyearStr,String semesterStr);
}
