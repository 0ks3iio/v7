package net.zdsoft.tutor.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.tutor.data.entity.TutorRecordDetailed;

/**
 * @author yangsj  2017年11月20日上午10:14:24
 */
public interface TutorRecordDetailedService extends BaseService<TutorRecordDetailed, String> {

	/**
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param recordType
	 * @return
	 */
	List<TutorRecordDetailed> findBySIdAndSemester(String unitId, String acadyear, String semester, String recordType ,Pagination pagination,String... teacherId);

	/**
	 * @param array
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<TutorRecordDetailed> findByTIdsAndSemester(String[] teacherIds, String acadyear, String semester);

	
	
}
