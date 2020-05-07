package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.NewGKCourseFeatureDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;

public interface NewGkSubjectTimeService extends BaseService<NewGkSubjectTime, String>{

	void saveWithChoRelation(List<NewGkSubjectTime> subjectTimeList, List<NewGkChoRelation> saveList, String[] choiceIds, String unitId);
	
	String saveList(String divideId, List<NewGkSubjectTime> subjectTimeList);

	void updateList(List<NewGkSubjectTime> subjectTimeList);
	/**
	 * 删除扩展信息
	 * @param unitId TODO
	 * @param itemId
	 */
	void deleteByArrayItemId(String unitId, String itemId);

	List<NewGkSubjectTime> findByArrayItemId(String subjectArrangeId);
	
	List<NewGkSubjectTime> findByArrayItemIdAndSubjectId(String itemId, String[] subjectIds,String[] subjectTypes);
	
	List<NewGkSubjectTime> findByArrayItemIdAndGroupType(String itemId, String groupType);

	List<NewGkSubjectTime> findByArrayItemIdAndSubjectTypeIn(String subjectArrangeId, String[] subjectTypes);

	void updateByArrayItem(String unitId, List<NewGkSubjectTime> subjectTimeList);

	void saveAllSubjectTimeItem(String unitId, String itemId,
                                List<NewGkSubjectTime> subjectTimeList, List<NewGkLessonTime> lessonTimeList,
                                List<NewGkLessonTimeEx> lessonTimeExList, List<NewGkRelateSubtime> relateSubtimeList, String arrayId);

	/**
	 * 删除某个科目 包括关联
     * @param list
     * @param arrayId
     */
	void deleteOneSubject(String unitId, String itemId, String subjectId, String subjectType, String arrayId);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subids);
    
    List<NewGKCourseFeatureDto> findCourseFeatures(String arrayItemId, Map<String,Object> map, Grade grade,
			String unitId);
    List<NewGKCourseFeatureDto> findCourseFeaturesWithMaster(String arrayItemId, Map<String,Object> map, Grade grade,
    		String unitId);
}
