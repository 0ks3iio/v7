package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.entity.DyDormRoom;

public interface DyDormRoomService extends BaseService<DyDormRoom, String>{
	/**
	 * 根据unit_id buildingId roomType 查找stuwork_dorm_room list
	 * @param unit_id buildingId roomType
	 * @param 
	 * @return
	 */
	public List<DyDormRoom> getDormRoomByUnitId(String unitId,
			String buildingId,String roomType,String acadyear,String semester,String floor,String roomName,String roomState,String roomProperty,Pagination page);
	/**
	 * 根据unitId查找stuwork_dorm_room list
	 * @param unit_id
	 * @param 
	 * @return
	 */
	public List<DyDormRoom> getDormRoomByUnitId(String unitId,String roomProperty);
	
	public List<DyDormRoom> getDormRoomByProUnitId(String unitId,String roomProperty);
	/**
	 * 根据unitId buildingId roomType查找stuwork_dorm_room list
	 * @param unitId buildingId roomType
	 * @param 
	 * @return
	 */
	public List<DyDormRoom> getDormRoomByCon(String unitId,String buildingId,String roomType,String roomProperty,Pagination page);
	/**
	 * 根据unitId buildingId roomFloor roomType roomName查找stuwork_dorm_room list
	 * @param unitId buildingId roomType
	 * @param
	 * @return
	 */
	public List<DyDormRoom> getDormRoomByCon(String unitId,String buildingId,String roomFloor,String roomName,String roomType,String roomProperty,Pagination page);
	
	/**
	 * 根据roomName查找stuwork_dorm_room list
	 * @param roomName
	 * @param 
	 * @return
	 */
	public List<DyDormRoom>	findByName(String unitId,String roomName,String buildingId,String roomProperty);
	/**
	 * 根据id删除stuwork_dorm_room数据
	 * @param id
	 * @param
	 * @return
	 */
	public void deletedById(String id);
	/**
	 * 根据id删除stuwork_dorm_room数据
	 * @param id
	 * @param
	 * @return
	 */
	public void deletedByBuildingId(String buildingId);
	/**
	 * 排序
	 * @param roomList
	 * @param
	 * @return
	 */
	public List<DyDormRoom> getListOrderBy(List<DyDormRoom> roomList);

	/**
	 * 排序
	 * @param buildingId
	 * @param roomType
	 * @return String
	 */
	public String doDyDormRoomImport(String unitId,String buildingId,String roomType,List<String[]> datas);
	
}
