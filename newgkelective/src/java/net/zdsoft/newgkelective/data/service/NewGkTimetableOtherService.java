package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.SimpleClassroomUseageDto;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;

public interface NewGkTimetableOtherService extends BaseService<NewGkTimetableOther, String>{

	void deleteByTimetableIdIn(String[] timeTableIds);
	void saveAllEntity(List<NewGkTimetableOther> insertOtherList);
	
	List<NewGkTimetableOther> findByArrayId(String unitId, String arrayId);
	/**
	 * @param unitId
	 * @param arrayId
	 * @param placeId
	 * @return
	 */
	List<NewGkTimetableOther> findByArrayIdAndPlaceId(String unitId, String arrayId,String placeId);
	/**
	 * 根据arrayId查出教室的使用情况
	 * @param unitId
	 * @param arrayId 走缓存10秒
	 * @return
	 */
	List<SimpleClassroomUseageDto> seachClassroomUseage(String unitId, String arrayId);

	void saveOne(String unitId, String otherStr,String placeId,String timeId,String otherId);
	/**
	 * 获取老师上课时间点
	 * @param unitId
	 * @param teacherIds1
	 * @param arrayId
	 * @return
	 */
	Map<String, List<NewGkTimetableOther>> findByTeacherIds(String unitId, Set<String> teacherIds, String arrayId);
	
	List<String> findPlaceIds(String unitId,String arrayId);
	
	void deleteByIdIn(String[] otherIds);
}
