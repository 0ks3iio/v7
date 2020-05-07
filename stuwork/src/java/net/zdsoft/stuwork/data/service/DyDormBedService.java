package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyDormBed;

public interface DyDormBedService extends BaseService<DyDormBed, String>{
	/**
	 * 根据studentId查找stuwork_dorm_bed 
	 * @param studentId
	 * @param 
	 * @return
	 */
	public DyDormBed getbedByStudentId(String studentId,String unitId,String acadyear,String semesterStr);
	/**
	 * 根据unitId查找stuwork_dorm_bed list
	 * @param unitId acadyear semester roomIds
	 * @param 
	 * @return
	 */
	public List<DyDormBed> getDormBedsByUnitId(String unitId,
			String acadyear,String semester,String[] roomIds,String[] ids,String roomProperty);
	/**
	 * 根据roomIds查找stuwork_dorm_bed list
	 * @param roomIds
	 * @param 
	 * @return
	 */
	public List<DyDormBed> getDormBedsByRoomIds(String unitId,
			String[] roomIds,String acadyear,String semesterStr);
	/**
	 * 根据unitId  acadyear semesterStr查找stuwork_dorm_bed list
	 * @param unitId  acadyear semesterStr
	 * @param 
	 * @return
	 */
	public List<DyDormBed> getDormBedsByProCon(String unitId,String acadyear,String semesterStr,String roomProperty);
	
	public List<DyDormBed> getDormBedsByCon(String classId,String unitId,String acadyear,String semesterStr,String roomProperty);
	
	public String doBedImport(String unitId, String acadyear,String semester,String roomProperty, List<String[]> datas);
	
	public String saveAllBed(List<DyDormBed> bedList,String roomProperty);
	/**
	 * 根据id删除stuwork_dorm_bed数据
	 * @param id
	 * @param
	 * @return
	 */
	public void deletedById(String id);
	/**
	 * 根据unitId acadyear roomId删除stuwork_dorm_bed数据
	 * @param unitId acadyear roomId
	 * @param roomId可为空
	 * @return
	 */
	public void deletedByUARoomId(String unitId,String acadyear,String semester,String roomId,String roomProperty);
	
	public List<DyDormBed> findByClassIds(String unitId, String acadyear,
			String semester, String[] classIds);
	
	public List<DyDormBed> findStudentByRoomIds(String unitId, String acadyear,
			String semester, String[] roomIds);
	
	public List<DyDormBed> findDyDormBedByUnitId(String unitId, String acadyear, String semester, String[] studentIds);
}