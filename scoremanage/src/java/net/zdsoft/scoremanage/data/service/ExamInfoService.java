package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;

public interface ExamInfoService extends BaseService<ExamInfo, String>{

	List<ExamInfo> findByUnitId(String unitId, Pagination page);
	
	List<ExamInfo> findExamInfoList(String unitId,ExamInfoSearchDto searchDto,Pagination page);

	List<String> findExamCodeMax();
	
	void deleteAllIsDeleted(String... ids);

	void saveExamInfoOne(ExamInfo examInfo, List<JoinexamschInfo> joinexamschInfoAddList);

	ExamInfo findExamInfoOne(String id);

	List<ExamInfo> findNotDeletedByIdIn(String... ids);
	
	List<ExamInfo> findExamInfoList(String unitId, String acadyear, String semester);

	/**
	 * 小于等于学校学期未删除的本单位设置的
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<ExamInfo> findExamInfoListRange(String unitId, String acadyear, String semester);
	
	/**
	 * 小于等于学校学期未删除的直属教育局设置的或自己单位参与的校校
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<ExamInfo> findExamInfoListRange(String unitId, String acadyear, String semester, String searchType);

	List<ExamInfo> saveAllEntitys(ExamInfo... examInfo);
	
	/**
	 * 根据条件获取本校参与过的考试
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeCode 学段
	 * @return List<ExamInfo>
	 */
	List<ExamInfo> findExamInfoListAll(String unitId, String acadyear, String semester, String gradeCode);
	/**
	 * 根据gradeId 以及 examType查看数据
	 */
	List<ExamInfo> findExamInfoByGradeId(String unitId,String gradeId,String examType);
	
	List<ExamInfo> findExamInfoByDayTBefore(String unitId);
	
	List<ExamInfo> findExamInfoByDayTMoreBefore(String unitId, String acadyear, String semester, String examType);

	String findJsonExamList(String unitId);

	String findJsonGradeList(String unitId, String examId);
}
