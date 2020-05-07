package net.zdsoft.officework.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.officework.dto.ClassRoomDto;
import net.zdsoft.officework.entity.OfficeClassroomRelevance;

public interface OfficeClassroomRelevanceService extends BaseService<OfficeClassroomRelevance, String> {
	/**
	 * 根据数字校园场地，查找华三对应教室
	 * @param placeId
	 * @return
	 */
	public OfficeClassroomRelevance findByPlaceId(String placeId);

	public List<OfficeClassroomRelevance> findByUnitId(String unitId);

	public void deleteByPlaceId(String placeId);
	
	public List<ClassRoomDto> getClassRoomList(String unitId);
}
