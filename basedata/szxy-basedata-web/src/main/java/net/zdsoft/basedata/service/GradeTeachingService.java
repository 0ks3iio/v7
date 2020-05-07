package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.GradeTeaching;

public interface GradeTeachingService extends BaseService<GradeTeaching, String>{

	List<GradeTeaching> findBySearch(OpenTeachingSearchDto dto);
	List<GradeTeaching> findBySearchWithMaster(OpenTeachingSearchDto gradeTeachingSearchDto);
	
	String deleteAndSave(String acadyear, String semester, String gradeId,
			String unitId, String subjectType, String operatorId, List<GradeTeaching> newGradeTeachingList, List<GradeTeaching> delGradeTeachingList);

	void saveAndInit(String acadyearVal, String semesterVal, String unitId, String gradeIdVal, String operatorId, Map<String, List<GradeTeaching>> gradeTeachingSaveMap);

	/**
	 * 获取年级开设计划
	 * @param acadyear 学年
	 * @param semester 学期
	 * @param gradeId 年级id集合
	 * @param unitId 单位id
	 * @param isDeleted 是否删除
	 * @param subjectType 科目类型
	 * @param id base_grade_teaching表id
	 * @return
	 */
	List<GradeTeaching> findGradeTeachingList(String acadyear, String semester, String[] gradeId, String unitId, Integer isDeleted, String subjectType,String id);

	
	void saveAndInit(String unitId,String acadyear,String semester, String operatorId, List<GradeTeaching> insertList);
	
	public Map<String, GradeTeaching> findBySearchMap(String unitId, String acadyear, String semester, String gradeId);

	public List<GradeTeaching> findBySearchList(String unitId, String acadyear, String semester,
			String gradeId,String subjectId);

	List<GradeTeaching> findGradeTeachingList(String acadyear, String semester, String gradeId, String unitId, String[] subjectTypes);
	List<GradeTeaching> findGradeTeachingListWithMaster(String acadyear, String semester, String gradeId, String unitId, String[] subjectTypes);
	/**
	 * 删除年级后删除相应年级计划
	 * @param gradeIds
	 */
	void deleteByGradeIds(String... gradeIds);

	/**
	 * 删除课程后删除相应年级计划
	 * @param subjectIds
	 */
	void deleteBySubjectIds(String... subjectIds);
	/**
	 * 更新单个年级计划
	 * @param gradeTeaching
	 * @param operatorId 
	 * @return
	 */
	String updateOne(GradeTeaching gradeTeaching, String operatorId);

}
