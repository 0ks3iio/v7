package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormBed;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DyDormBedDao extends BaseJpaRepositoryDao<DyDormBed,String>{
	@Query("From DyDormBed where unit_id = ?1 ")
	public List<DyDormBed> findByUnitId(String unitId);
	
	@Modifying
	@Query("DELETE FROM DyDormBed WHERE unit_id =?1 and acadyear=?2 and semester =?3 and room_id=?4")
	public void deletedByUARoomId(String unitId,String acadyear,String semester,String roomId);
	@Modifying
	@Query("DELETE FROM DyDormBed WHERE unit_id =?1 and acadyear=?2 and semester =?3 and ownerType= 1")
	public void deletedStuByUA(String unitId,String acadyear,String semester);
	@Modifying
	@Query("DELETE FROM DyDormBed WHERE unit_id =?1 and acadyear=?2 and semester =?3 and ownerType =2 ")
	public void deletedTeachByUA(String unitId,String acadyear,String semester);
	@Modifying
	@Query("DELETE FROM DyDormBed WHERE unit_id =?1 and acadyear=?2 and semester =?3 and owner_id in (?4)")
	public void deletedByUAStuIds(String unitId,String acadyear,String semester,String[] ownerIds);
	
	@Query("From DyDormBed where unit_id = ?1 and room_id in (?2)")
	public List<DyDormBed> getDormBedsByRoomIds(String unitId,String[] roomIds);
	
	@Query("From DyDormBed where owner_id= ?1 and unit_id = ?2 and acadyear = ?3 and semester = ?4")
	public DyDormBed getbedByStudentId(String ownerId,String unitId,String acadyear,String semesterStr);
	
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester =?3 and room_id in (?4)")
	public List<DyDormBed> getDormBedsByRoomIds(String unitId,String acadyear,String semesterStr,String[] roomIds);
	
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester =?3 and owner_id in (?4)")
	public List<DyDormBed> getDormBedsByStuIds(String unitId,String acadyear,String semester,String[] ownerIds);
	
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester =?3 ")
	public List<DyDormBed> getDormBedsByCon(String unitId,String acadyear,String semesterStr);
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester =?3 and ownerType= ?4 ")
	public List<DyDormBed> getDormBedsByCon(String unitId,String acadyear,String semesterStr,String ownerType);
	
	@Query("From DyDormBed where class_id =?1 and unit_id = ?2 and acadyear= ?3 and semester =?4 ")
	public List<DyDormBed> getDormBedsByClaCon(String classId,String unitId,String acadyear,String semesterStr);
	@Query("From DyDormBed where class_id =?1 and unit_id = ?2 and acadyear= ?3 and semester =?4 and ownerType= ?5 ")
	public List<DyDormBed> getDormBedsByClaCon(String classId,String unitId,String acadyear,String semesterStr,String ownerType);
	
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 ")
	public List<DyDormBed> getDormBedsByProCon(String unitId,String acadyear);
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and ownerType= ?3 ")
	public List<DyDormBed> getDormBedsByProCon(String unitId,String acadyear,String ownerType);
	
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester = ?3 and classId in ?4")
	public List<DyDormBed> findByClassIds(String unitId, String acadyear,String semester, String[] classIds);
	
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester = ?3 and ownerId is not null and roomId in ?4")
	public List<DyDormBed> findStudentByRoomIds(String unitId, String acadyear,String semester, String[] roomIds);
	@Query("From DyDormBed where unit_id = ?1 and acadyear= ?2 and semester = ?3 and ownerId in (?4)")
	public List<DyDormBed> findDyDormBedByUnitId(String unitId, String acadyear, String semester, String[] ownerIds);
}
