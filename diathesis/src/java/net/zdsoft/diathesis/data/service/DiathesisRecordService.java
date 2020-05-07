package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisRecord;
import net.zdsoft.diathesis.data.entity.DiathesisRecordInfo;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:41
 */
public interface DiathesisRecordService extends BaseService<DiathesisRecord, String> {

	/**
	 * 查询项目下还有多少记录未审核key-projectId，value—未审核数
	 * @param unitId
	 * @param projectIds
	 * @return
	 */
	Map<String, Integer> findMapByProjectIdIn(String unitId, String[] projectIds);

	/**
	 * 查询项目下写实记录
	 * @param unitId
	 * @param projectId必填
	 * @param acadyear
	 * @param semester
	 * @param classId
	 * @param classIds
	 * @param studentId
	 * @param status
	 * @param page
	 * @return
	 */
	List<DiathesisRecord> findListByProjectId(String unitId, String projectId, String acadyear, String semester,
											  String classId, String[] classIds, String studentId, String[] status, Pagination page);

	/**
	 *
	 * @param unitId
	 * @param recordId
	 * @param saveRecordList
	 * @param saveInfoList
	 */
	void deleteAndSave(String unitId, String recordId, List<DiathesisRecord> saveRecordList, List<DiathesisRecordInfo> saveInfoList);

	List<DiathesisRecord> findListByStuId(String unitId,String stuId,String status);
	List<DiathesisRecord> findListByGradeCodeAndStuId(String unitId,String stuId,String gradeCode,String status);

	List<String> findStuIdByProjectIdInAndUnitId(List<String> projectIds, String unitId);

	List<DiathesisRecord> findListByUnitIdAndStuIdInAndProjectIdIn(String unitId, String acadyear, Integer semester, List<String> stuIds, List<String> proIds);

	List<DiathesisRecord> findListByUnitIdAndStuId(String unitId, String studentId);

	List<DiathesisRecord> findListByUnitIdAndProjectIdInAndStuId(String unitId, String acadyear, Integer semester, List<String> projectIds, String studentId);

	List<DiathesisRecord> findListByUnitIdAndProjectIdIn(String unitId, String acadyear, Integer semester, String[] projectIds);

	void deleteRecord(String unitId, String[] ids);

	void deleteByProjectIds(String[] projectIds);

	List<String> findIdsByProjectIdIn(String[] projectIds);

	List<DiathesisRecord> findListByProjectIdAndStuIdIn(String unitId, String projectId, String acadyear, String semester,String[] statu, String[] stuIds);

	List<DiathesisRecord> findListByAcadyearAndSemesterAndStuId(String unitId, String acadyear, Integer semester, String studentId);
}
