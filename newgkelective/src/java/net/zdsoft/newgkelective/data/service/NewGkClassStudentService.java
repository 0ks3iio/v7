package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;

public interface NewGkClassStudentService extends BaseService<NewGkClassStudent, String>{

	/**
	 * key:classId value studentIds
	 * @param unitId TODO
	 * @param classIds
	 * @return
	 */
	Map<String, List<String>> findMapByClassIds(String unitId,String divideId, String[] classIds);
	
	/**
	 * 获取班级详情列表
	 * @param divideClassId
	 * @return
	 */
	List<NewGkConditionDto> findClassDetail(String divideClassId,String divideId);

	Set<String> findSetByClassIds(String unitId,String divideId, String[] classIds);
	
	List<NewGkClassStudent> findListByClassIds(String unitId,String divideId,String[] classIds);
	/**
	 * 
	 * @param unitId
	 * @param arrayId
	 * @param studentId
	 * @return
	 */
	List<NewGkClassStudent> findListByStudentId(String unitId,String divideId,String studentId);
	/**
	 * 
	 * @param unitId
	 * @param arrayId
	 * @param studentIds
	 * @return
	 */
	List<NewGkClassStudent> findListByStudentIds(String unitId,String divideId,String[] studentIds);

	void saveAllList(List<NewGkClassStudent> insertStudentList);

	void deleteByClassIdIn(String unitId, String divideId, String[] delClassId);

	void saveOrSaveList(String unitId,
			String divideId, String[] divideClassIds, List<NewGkClassStudent> classStudentList);
	/**
	 * 查询某一次分班结果 学生的所在班级列表
	 * @param divideId
	 * @param studentId
	 * @param classType
	 * @return
	 */
	List<NewGkClassStudent> findListByDivideStudentId(String divideId, String[] classType,String[] studentId,String scourceType);

	void saveAndDel(String[] ids, NewGkClassStudent[] list);

	void saveChangeClass(String leftClassSelect, String rightClassSelect, String leftAddStu, String rightAddStu);
	
	void deleteByClassIdAndStuIdIn(String classId, String[] stuIds);
	
	void deleteByDivideId(String unitId, String divideId);

    // Basedata Sync Method
    void deleteByStudentIds(String... subjectIds);

    // Basedata Sync Method
    void deleteByClassIds(String... classIds);
    
    List<String> findArrangeStudentIdWithMaster(String divideId,String classType);
    //根据ids删除
	void deleteByIds(String[] ids);
}
