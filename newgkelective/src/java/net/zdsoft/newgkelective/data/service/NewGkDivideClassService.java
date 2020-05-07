package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.*;

public interface NewGkDivideClassService extends BaseService<NewGkDivideClass,String>{
	
	/**
	 * 
	 * @param unitId
	 * @param divideId
	 * @param classTypes 可为null
	 * @param isMakeStudent 是否组装学生数据
	 * @param sourceType 
	 * @param containChildren 是否包含拆分科目产生的班级
	 * @return
	 */
	List<NewGkDivideClass> findByDivideIdAndClassType(String unitId,
			String divideId,String[] classTypes,boolean isMakeStudent, String sourceType, boolean containChildren);
	
	List<NewGkDivideClass> findByDivideIdAndClassTypeWithMaster(String unitId,
			String divideId,String[] classTypes,boolean isMakeStudent, String sourceType, boolean containChildren);
	
	List<NewGkDivideClass> findByDivideIdAndClassTypeSubjectType(String unitId,
			String divideId, String[] classTypes, boolean isMakeStudent, String sourceType, String subjectType);
	List<NewGkDivideClass> findByDivideIdAndClassTypeSubjectTypeWithMaster(String unitId,
			String divideId, String[] classTypes, boolean isMakeStudent, String sourceType, String subjectType);
	
	List<NewGkDivideClass> findByRelateIdsWithMaster(String[] relateIds);

	List<NewGkDivideClass> findByDivideIdAndClassTypeAndSubjectIds(String unitId, String divideId, String classType, String subjectIds, boolean isMakeStudent);
	
	List<NewGkDivideClass> findByDivideIdAndClassTypeAndSubjectIdsWithMaster(String unitId, String divideId, String classType, String subjectIds, boolean isMakeStudent);

	/**
	 * 改走缓存10秒 多表关联走索引
	 * @param divideIds
	 * @return
	 */
	public List<NewGkDivideClass> findByGradeId(String gradeId,String sourceType);
	/**
	 * 改走缓存10秒 多表关联走索引  
	 * @param divideId
	 * @param isRedis 是否走缓存
	 * @return
	 */
	public List<NewGkDivideClass> findByDivideIdAndSourceType(String divideId,String sourceType,boolean isRedis);
	/**
	 * 
	 * @param unitId TODO
	 * @param divideId 不为空 修改分班方案状态  为空 不做处理 改成下面isStat
	 * @param delClassId
	 * @param insertClassList
	 * @param insertStudentList
	 * @param isStat
	 */
	void saveAllList(String unitId,String divideId,String[] delClassId,List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList, boolean isStat);

    /**
     * 课程拆分
     * @param unitId
     * @param divideId
     * @param insertClassList
     * @param insertStudentList
     * @param newGkChoRelationList
     */
	void saveAllSplit(String unitId, String divideId, String solitSubjectId, List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList, List<NewGkChoRelation> newGkChoRelationList);

	/**
	 * 删除班级同时删除班级下学生 扩展表newgkelective_class_bacth
	 * @param unitId TODO
	 * @param delClassId
	 */
	void deleteByClassIdIn(String unitId,String divideId, String[] delClassId);
	/**
	 * 
	 * @param unitId TODO
	 * @param newGkDivideClass
	 */
	String saveByHand(String unitId, NewGkDivideClass newGkDivideClass);
	
	void deleteById(String unitId, String divideId, String divideClassId);
	/**
	 * 计算人数
	 * @param unitId TODO
	 * @param divideClassList
	 */
	void makeStuNum(String unitId,String arrayId, List<NewGkDivideClass> divideClassList);
	/**
	 * 改走缓存10秒 多表关联走索引
	 * @param arrayId
	 * @return
	 */
	Map<String, String> findByArrayIdMap(String arrayId);
	
	List<NewGkDivideClass> findClassBySubjectIds(String unitId,String divideId,String sourceType,String classType,String subjectIds, boolean isMakeStu);
	
	List<NewGkDivideClass> findClassBySubjectIdsWithMaster(String unitId,String divideId,String sourceType,String classType,String subjectIds, boolean isMakeStu);
	
	NewGkDivideClass findById(String unitId, String divideClassId, boolean isMakeStu);
	NewGkDivideClass findByIdWithMaster(String unitId, String divideClassId, boolean isMakeStu);
	/**
	 * 
	 * @param divideId TODO
	 * @param divideClassIds 删除班级及学生
	 * @param updateList 修改班级下学生
	 */
	void updateStu(String unitId, String divideId,String[] divideClassIds, List<NewGkDivideClass> updateList);
	
	public List<NewGkDivideClass> findByDivideIdIn(String[] divideIds);
	
	public void saveChangeByStuId(String arrayId, String stuId, String[] chooseClass,String unitId);
	
	List<NewGkDivideClass> findByBatch(String divideClassId);
	
	/**
	 * 查询各组合班以及行政班数量
	 * @param divideIds
	 * @param classTypes
	 * @return key:divideId+"_"+classType+"_"+subjectType(当其中属性为空 则"")
	 */
	Map<String,Integer> findCountByDivideIdAndClassType(String[] divideIds,String[] classTypes);
	/**
	 * 查询各科目教学班数量
	 * @param divideIds
	 * @return key:divideId+"_"+subjectId+"_"+subjectType+"_"+bestType(当其中属性为空 则"")
	 */
	Map<String,Integer> findCountByDivideIdAndSubjectType(String[] divideIds);

	void updateMoveStudents(String oldClassId, String[] stuIds, List<NewGkDivideClass> list, List<NewGkClassStudent> makeStudents);
	/**
	 * 组装学生数据
	 * @param unitId TODO
	 * @param list
	 */
	public void toMakeStudentList(String unitId,String divideId, List<NewGkDivideClass> list);
	
	List<NewGkDivideClass> findListByDivideId(String divideId,String sourceType, String classType, String subjectId,
			String subjectType );
	
	List<NewGkDivideClass> findListByDivideIdWithMaster(String divideId,String sourceType, String classType, String subjectId,
			String subjectType );
	/**
	 * 删除相关所有信息
	 * @param unitId TODO
	 * @param divideId
	 */
	void deleteByDivideId(String unitId, String divideId);

	List<NewGkDivideClass> findListByRelateId(String divideId, String sourceType,String classType, String[] relateIds);
	
	/**
	 * 从排课结果中 取出在某个时间点 上课的学生
	 * @param arrayId
	 * @param classTypes 不能为空
	 * @return
	 */
	List<String> findByClassTypeAndTime(String arrayId,NewGkTimetableOther timeInf, String[] classTypes);

	void saveAllList(List<NewGkDivide> insertDivideList, List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subjectIds);

    // Basedata Sync Method
    void deleteByClassIds(String... classIds);

    List<NewGkDivideClass> findByParentIds(String[] parentIds);

    /**
     * 统计数量
     * @param divideId
     * @param classType
     * @param classSourceType
     * @return
     */
    long countByDivideId(String divideId, String classType, String classSourceType);

	void saveStuList(String groupClassId, List<NewGkClassStudent> insertStudentList);

	void saveClassOrDel(String unitId,String divideId,List<NewGkDivideClass> updateList, String[] ids,String[] delClasstuIds);

	void deleteByClassIdIn2(String unitId, String divideId, String[] classIds);
	
	public void savejxbBySubjectType(String unitId,String divideId,String subjectType,List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList);

	/**
	 * 获取 行政班 实际要上课的 走班科目(包括行政班科目),不支持伪行政班
	 * @param unitId 必填
	 * @param divideId 必填
	 * @param sourceType 必填
	 * @param xzbIds
	 * @return  key:xzbId  v:上课 科目 arr[0]:subjectId arr[1]:subjectType
	 */
	Map<String, List<String[]>> findXzbSubjects(String unitId, String divideId, String lessonArrangeId, String sourceType, List<String> xzbIds);

	/**
	 * 只是获取伪 行政班 实际上要上课的 走班科目 ；除物理 历史以外的 4门
	 * @param unitId
	 * @param divideId
	 * @param lessonArrangeId
	 * @param sourceType
	 * @param fakeXzbIds
	 * @return
	 */
	Map<String, List<String[]>> findFakeXzbSubjects(String unitId, String divideId, String lessonArrangeId,
			String sourceType, List<String> fakeXzbIds);

	/**
	 * 查找classType =3 的 班级中，对应的 行政班 有两个 组合班的班级
	 * @param unitId
	 * @param divideId
	 * @param sourceType
	 * @return
	 */
	List<NewGkDivideClass> findNoMoveZhbs(String unitId, String divideId,String sourceType);
	/**
	 * 
	 * @param divideId
	 * @param studentId
	 * @param subjectIds
	 * @param classIds
	 * @return
	 */
	String saveChangeByStudentId(String divideId, String studentId, String subjectIds, String classIds,String oppoName);

	void deleteByStudentId(NewGkDivide divide, String studentId);
	
	public String checkChoose(NewGkDivide divide,String subjectIds);

	/**
	 * 获取排课方案中 classId 对应的 oldDivideClassId
	 * @param arrayId
	 * @return
	 */
	Map<String, String> findOldDivideClassIdMap(String arrayId);
}
