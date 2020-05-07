package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.LessonTimeDtoPack;
import net.zdsoft.newgkelective.data.dto.SubjectLessonTimeDto;
import net.zdsoft.newgkelective.data.dto.TimeInfDto;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;

public interface NewGkLessonTimeExService extends BaseService<NewGkLessonTimeEx, String>{

	List<NewGkLessonTimeEx> findByObjectId(String[] scourceTypeIds,
			String[] scourceTypes);



    List<NewGkLessonTimeEx> findByObjectIdWithMaster(String[] scourceTypeIds, String[] scourceTypes);

	/**
	 * 非文理总课表保存和单科数据保存时调用到
	 * 根据以下参数更新lessonTimeEx表。实际是先删除newgkelective_lesson_time_ex所有对应的记录再添加新的记录。
	 * 既可以更新总课表，也可以更新科目课时安排
	 * @param scourceTypeId 非空时删除对应的的ex记录
	 * @param arrayItemId 非空时，lessonTimeExs和系统已有的单科课表时间判断，删除冲突的单科数据
	 * @param scourceType
	 * @param timeType
	 * @param lessonTimeExs 要新增的ex记录
	 */
	void updateGradeLessonTimeEx(String scourceTypeId, String arrayItemId, String scourceType,
			String timeType, List<NewGkLessonTimeEx> lessonTimeExs);
	
	public List<NewGkLessonTimeEx> assembleLessonTimeExFromSubjectDto(
			List<TimeInfDto> timeInf, String lessonTimeId,String arrayItemId);
	
	void addSubjectLessonTime(SubjectLessonTimeDto subjectLessonTimeDto,
			String arrayItemId);
	
	/**
	 * 新增总课表
	 * @param lessonTimeDtoPack
	 * @param divide_id
	 * @return
	 */
	String addLessonTimeTable(LessonTimeDtoPack lessonTimeDtoPack,
			String divide_id);
	
	/**
	 * 总课表用
	 * @param arrayItemId TODO
	 * @param lessonTimeDtoPack
	 * @param objScMap <key=objId，01/05时objId+levelType+groupType>
	 * @return
	 */
	public List<NewGkLessonTimeEx> assembleLessonTimeExFromDtoForGrade(String arrayItemId, 
			LessonTimeDtoPack lessonTimeDtoPack, Map<String, String> objScMap);
	
	/**
	 * 非文理模式单科课表时间保存
	 * @param array_item_id
	 * @param subjectLessonTimeDto
	 */
	void updateSubjectLessonTimes(String array_item_id,
			SubjectLessonTimeDto subjectLessonTimeDto);
	
	/**
	 * 文理总课表修改、文理保存、教师时间保存
	 * @param arrayId 仅在排课特征中保存课表设置时用来 更新预排课表
	 * @param itemId
	 * @param objectType
	 * @param lessonTimeDtoPack
	 */
	public void updateLessonTime(String arrayId, String itemId, String objectType, LessonTimeDtoPack lessonTimeDtoPack);
	
	/**
	 * 保存基础年级设置
	 * @param arrayId
	 * @param itemId
	 * @param objectType
	 * @param lessonTimeDtoPack
	 */
	public void saveBasicGradeTime(String arrayId, String itemId, String objectType, LessonTimeDtoPack lessonTimeDtoPack);
	
	void deleteByScourceTypeIdIn(String[] scourceTypeIds);
	
	void deleteByArrayItemId(String arrayItemId);
	
	void saveBatch(List<NewGkLessonTimeEx> list);
	
	void deleteByItemIdAndType(String arrayItemId, String objectType, String timeType);
}
