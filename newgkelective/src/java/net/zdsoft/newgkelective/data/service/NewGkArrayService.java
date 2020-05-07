package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;

public interface NewGkArrayService extends BaseService<NewGkArray, String> {

	List<NewGkArray> findByDivideId(String divideId);
	/**
	 * 
	 * @param unitId
	 * @param gradeId 可为空
	 * @param stat 可为空
	 * @param arrangeType
	 * @return
	 */
	List<NewGkArray> findByGradeId(String unitId, String gradeId,String stat,String arrangeType);
	
	List<NewGkArray> findByGradeIdWithMaster(String unitId, String gradeId,String stat,String arrangeType);
	/**
	 * 
	 * @param unitId
	 * @param gradeId 可为空
	 * @param arrangeType
	 * @return
	 */
	int findMaxByGradeId(String unitId, String gradeId,String arrangeType);

	void updateStatById(String stat, String id);

	void deleteById(String unitId, String id);

	NewGkArray findById(String arrayId);

	/**
	 * 通知第三方
     * @param grade
     * @param pushHost
     * @return
     */
    ResultDto pushThirdParty(Grade grade, String pushHost);

    void saveToSchedule(NewGkArray array, DateInfo startDateInfo, DateInfo endDateInfo);

	/**
	 * 保存排课方案，同时复制分班的班级 学生信息给排课结果使用
	 * @param addDto
	 */
	void saveArray(NewGkArray addDto);

	boolean checkIsArrayIng(String arrayId);
	/**
	 * 自动分堆
	 * @param unitId
	 * @param arrayId
	 * @return
	 */
	String autoArraySameClass(String unitId, String arrayId);

	/**
	 * 检验课表中教师的冲突情况 返回null 或者 "" 表示没有冲突 ，其余为错误信息
	 * @param arrayId
	 * @return
	 */
	String checkAllTeacherConflict(String arrayId);

	/**
	 * 查询行政班排课
	 * @param unitId
	 * @param divideIds
	 * @return
	 */
	List<NewGkArray> findByDivideIds(String unitId, String[] divideIds);
	List<NewGkArray> findByDivideIdsWithMaster(String unitId, String[] divideIds);

    // Basedata Sync Method
    void deleteByGradeIds(String... gradeIds);
}
