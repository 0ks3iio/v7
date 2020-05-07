package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.ChosenSearchDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

public interface NewGkChoResultService extends BaseService<NewGkChoResult, String>{

	/**
	 * 查询
	 * @param uintId
	 * @param kindType
	 * @param choiceId
	 * @param studentId
	 * @return
	 */
	List<NewGkChoResult> findByChoiceIdAndStudentIdAndKindType(String unitId,String kindType, String choiceId, String studentId);
	
	/**
	 * 
	 * @param unitId
	 * @param kindType
	 * @param choiceId
	 * @param studentId
	 * @return
	 */
	List<NewGkChoResult> findByKindTypeAndChoiceIdAndStudentIds(String unitId,String kindType, String choiceId, String[] studentId);
	
	/**
	 * 查询学生Ids
	 * @param unitId
	 * @param kindType
	 * @param choiceId
	 * @return
	 */
	Set<String> findSetByChoiceIdAndKindType(String unitId,String kindType,String choiceId);
	
	/**
	 * 查询
	 * @param unitId
	 * @param kindType
	 * @param choiceId
	 * @return
	 */
	Map<String,Set<String>>  findMapByChoiceIdAndKindType(String unitId,String kindType,String choiceId);

	/**
	 * 保存选课
	 * @param unitId
	 * @param choiceId
	 * @param studentId
	 * @param subjectIds
	 * @param subjectTypes 
	 * @param noWantToIds 
	 * @param wantToIds 
	 */
	void saveNewGkChoResult(String unitId,String choiceId, String studentId,
			List<String> subjectIds, String[] subjectTypes, String[] wantToIds, String[] noWantToIds);

	List<StudentResultDto> findNotChosenList(String unitId,String choiceId, ChosenSearchDto dto);
	
	List<StudentResultDto> findNotChosenListWithMaster(String unitId, String choiceId, ChosenSearchDto dto);

	List<StudentResultDto> findChosenList(String unitId, String choiceId, ChosenSearchDto dto);

	List<StudentResultDto> findChosenListWithMaster(String unitId, String choiceId, ChosenSearchDto dto);
	
	List<StudentResultDto> findGradeIdList(String unitId,String gradeId,String choiceId, String subids);
	
	/**
	 * 批量保存选课结果
	 * @param unitId
	 * @param choiceId
	 * @param resultList 保存的选课结果
	 * @param stuIds 需要先删除的学生选课结果
	 * @param noJoinStuIds 增加不参与选课的学生id
	 * @param moveNoJoinStuIds 去除参与选课的学生id
	 */
	void saveAllResult(String unitId,String choiceId,List<NewGkChoResult> resultList, String[] stuIds,
			String[] noJoinStuIds,String[] moveNoJoinStuIds);

	void saveLock(String unitId,String choiceId, boolean isLock, String[] studentIds);
	/**
	 * 各次选课人数
	 * 改走缓存10秒   多表关联走索引
	 * @param gradeId
	 * @param kindType
	 * @return key:choiceId
	 */
	Map<String, Integer> getCountMapByGradeIdAndKindType(String gradeId,String kindType);
	
	/**
	 * 删除
	 * @param unitId
	 * @param choiceId
	 * @param studentIds
	 */
	void deleteByStudentIds(String unitId, String choiceId, String[] studentIds);

	
//	String doImport(String unitId, String choiceId, List<String[]> datas);
	/**
	 * 改走缓存10秒   多表关联走索引
	 * @deprecated 不建议用，数据量太大
	 * @param gradeId
	 * @param kindType
	 * @return
	 */
	List<NewGkChoResult> findByGradeIdAndKindType(String gradeId,String kindType);
	/**
	 * 
	 * @param unitId
	 * @param kindType
	 * @param choiceIds
	 * @return
	 */
	List<NewGkChoResult> findByGradeIdAndKindType(String unitId,String kindType,String[] choiceIds);
	
	/**
	 * 
	 * @param unitId
	 * @param kindType
	 * @param choiceIds
	 * @return
	 */
	List<Object[]> findCountByChoiceIdAndKindType(String unitId, String kindType, String[] choiceIds);
	
	List<Object[]> findCountByChoiceIdAndKindTypeAndStudentIds(String unitId, String kindType, String[] choiceIds, String[] studentIds);
	/**
	 * 
	 * @param unitId
	 * @param kindType
	 * @param choiceId
	 * @return
	 */
	List<NewGkChoResult> findByChoiceIdAndKindType(String unitId, String kindType,String choiceId);
	
	/**
	 * 
	 * @param unitId
	 * @param kindTypes
	 * @param choiceId
	 * @return
	 */
	Map<String,List<NewGkChoResult>> findByChoiceId(String unitId,String[] kindTypes,String choiceId);

	/**
	 * 查询选课学生数
	 * @param unitId
	 * @param kindType
	 * @param choiceId
	 * @return
	 */
	int findStudentNumByChoiceIdAndKindType(String unitId, String kindType,String choiceId);
	
	/**
	 * 查询 选择某科目的学生
	 * @param unitId
	 * @param kindType
	 * @param choiceId
	 * @param subjectId
	 * @return
	 */
	List<String> findByChoiceIdAndSubjectIdAndKindType(String unitId, String kindType,String choiceId, String subjectId);

	/**
	 * key-kindType
	 * @param unitId
	 * @param kindTypes
	 * @param choiceId
	 * @param studentId
	 * @return
	 */
	Map<String, List<NewGkChoResult>> findMapByChoiceIdAndStudentId(String unitId,String[] kindTypes, String choiceId, String studentId);

	Map<String, List<NewGkChoResult>> findMapByChoiceIdAndStudentIdWithMaster(String unitId,String[] kindTypes, String choiceId, String studentId);

	/**
	 * 
	 * @param unitId
	 * @param choiceId
	 * @param subjectId
	 * @param isXuankao 是否选择
	 * @return key:subjectId
	 */
	Map<String,Set<String>> findStuIdListByChoiceIdAndSubjectId(String unitId,String choiceId, String[] subjectId,boolean isXuankao);

	/**
	 * 走缓存   获取三科选课结果
	 * @param unitId
	 * @param choiceId
	 * @param courseIds
	 * @param isReverse
	 * @return
	 */
	String getCount(String unitId,String choiceId,List<String> courseIds, boolean isReverse);

    // Basedata Sync Method
    void deleteByStudentIds(String... stuids);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subids);

    /**
     * key1-choiceId,key2-kindType
     * @param studentId
     * @param choiceIds
     * @return
     */
	Map<String,Map<String,List<NewGkChoResult>>> findByStudentIdAndChoiceIds(String unitId, String studentId, String[] choiceIds);


}
