package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.NewGkClassFeatureDto;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;

public interface NewGkClassSubjectTimeService extends BaseService<NewGkClassSubjectTime, String> {
	/**
	 * 
	 * @param unitId 单位ID 必填
	 * @param arrayItemId 必填
	 * @param classIds
	 * @param subjectId
	 * @param subjectType
	 * @return
	 */
	public List<NewGkClassSubjectTime> findByArrayItemIdAndClassIdIn(String unitId,  String arrayItemId, String[] classIds,
			String[] subjectId, String[] subjectType);

	List<NewGkClassFeatureDto> makeExistsClassSubjectInfo(String unitId, String arrayItemId, String classId);
	
	List<NewGkSubjectTime> findClassSubjectList(String arrayItemId, String classId, String unitId);
	
	public void saveResult(NewGkClassFeatureDto dto); 
	
	public void updateBySubjectTimeChange( String unitId, String itemId, List<NewGkSubjectTime> subjectTimeList,
			List<NewGkLessonTime> lessonTimeList, List<NewGkLessonTimeEx> lessonTimeExList);
	
	public void deleteBySubjectCode(String unitId, String arrayItemId, Set<String> delSubjectCodes, List<String> delRelaCodeList,
                                    Map<String, Integer> periodUpMap, Map<String, Set<String>> noTimeUpMap, Set<String> resetWeekTypes);
	
	public void deleteByArrayItemId(String arrayItemId);
	
	/**
	 * 
	 * @param unitId
	 * @param arrayItemId
	 * @param classSubjectTypeIds List. e -> new String[]{classId,subjectId,subjectType}
	 * @return
	 */
	List<NewGkClassFeatureDto> findSubjectInfo(String unitId,String arrayItemId, List<String[]> classSubjectTypeIds);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subjectIds);

    // Basedata Sync Method
    void deleteByClassIds(String... classIds);
	/**
	 * 课程特征删除某一门科目时 删除对应的班级特征数据
	 * @param unitId
	 * @param stList
	 */
	void deleteClassFeatureBySubjectTime(String unitId, List<NewGkSubjectTime> stList);
}
