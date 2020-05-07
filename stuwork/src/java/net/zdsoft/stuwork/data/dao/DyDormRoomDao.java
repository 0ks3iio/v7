package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.entity.Pagination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormRoom;

public interface DyDormRoomDao extends BaseJpaRepositoryDao<DyDormRoom,String>,DyDormRoomJdbcDao{
	@Query("FROM DyDormRoom WHERE unit_id = ?1 and room_name = ?2 and building_id = ?3 and room_property = ?4 order by roomType,roomName")
	public List<DyDormRoom>	findByName(String unitId,String roomName,String buildingId,String roomProperty);
	@Query("FROM DyDormRoom WHERE unit_id = ?1 and room_name = ?2 and building_id = ?3  order by roomType,roomName")
	
	public List<DyDormRoom>	findByName(String unitId,String roomName,String buildingId);
	@Query("FROM DyDormRoom WHERE unit_id = ?1  and building_id = ?2 order by roomType,roomName")
	public List<DyDormRoom>	findByBuildingId(String unitId,String buildingId);
	@Query("FROM DyDormRoom WHERE unit_id = ?1  and building_id = ?2 and room_property = ?3 order by roomType,roomName")
	public List<DyDormRoom>	findByBuildingId(String unitId,String buildingId,String roomProperty);
	
	@Query("FROM DyDormRoom WHERE unit_id = ?1 ")
	public List<DyDormRoom>	findByUnitId(String unitId);
	@Query("FROM DyDormRoom WHERE unit_id = ?1 and room_property = ?2 ")
	public List<DyDormRoom>	findByUnitId(String unitId,String roomProperty);
	@Modifying
	@Query("Delete FROM DyDormRoom WHERE building_id = ?1 ")
	public void deletedByBuildingId(String buildingId);
}
