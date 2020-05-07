package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;

public interface SubjectInfoService extends BaseService<SubjectInfo, String>{
	
	/**
	 * 
	 * @param schoolId
	 * @param examId
	 * @param gradeCode
	 * @return
	 */
	List<SubjectInfo> findByUnitIdExamId(String schoolId,String examId,String... gradeCode);


	/**
	 * 
	 * @param examInfo
	 * @param unitId
	 * @param courseInfoList
	 * @param isCanEditSubject
	 * @param isCanEditClass
	 * @throws Exception
	 */
	void saveCourseInfoAll(ExamInfo examInfo,String unitId, List<SubjectInfo> courseInfoList,boolean isCanEditSubject,boolean isCanEditClass) throws Exception;
	
	/**
	 * 
	 * @param unitId
	 * @param courseInfoList
	 * @param searchType
	 * @param isDeleteClass
	 * @throws Exception
	 */
	void saveCourseInfoAll(String unitId, List<SubjectInfo> courseInfoList, String searchType, boolean isDeleteClass) throws Exception;

	/**
	 * 同时会删除 班级设置和分数线设置
	 * @param cifs
	 */
	void deleteCourseInfoAll(SubjectInfo... cifs);
	
	/**
	 * 
	 * @param schoolId 用来找这个学校的班级数据 可为null
	 * @param examIds
	 * @return
	 */
	public List<SubjectInfo> findByExamIdIn(String schoolId, String... examIds);

	int saveCopy(Unit unit, ExamInfo sourceExamInfo, ExamInfo oldExamInfo);
	/**
	 * 取得某次考试，某个科目的考试科目信息（包括各个年级）
	 * @param examId
	 * @param courseId
	 * @return
	 */
	public List<SubjectInfo> findByExamIdAndCourseId(String examId, String courseId);

	/**
	 * 查询某个班级在某个考试下的考试科目
	 * @param examId
	 * @param classIdSearch
	 * @return
	 */
	public List<SubjectInfo> findByExamIdClassId(String examId, String classId);
	/**
	 * 拷贝班级数据	适用直属教育局考试和参与的校校
	 * @param unit
	 * @param sourceExamInfo
	 * @param oldExamInfo
	 * @return
	 */
	int saveCopyClass(Unit unit, ExamInfo sourceExamInfo, ExamInfo oldExamInfo);

	List<SubjectInfo> saveAllEntitys(SubjectInfo... courseInfo);

	int saveGradeCourseCopy(String examId, String gradeCode, String[] toGradeCode);

	/**
	 * 查询某个年级在某个考试下的考试科目
	 * @param examId
	 * @param gradeId
	 * @return
	 */
	List<SubjectInfo> findByExamIdGradeId(String examId, String gradeId);
	
	List<SubjectInfo> findByExamId(String examId);
	
	public List<SubjectInfo> findByExamIdAndCourseIdAndUnitId(String unitId, String examId, String courseId);

	
	/**
	 * 查询某个考试某个单位下的科目信息(不组装其他辅助参数)
	 * @param examId
	 * @param unitId
	 * @param classId 可为空
	 * @return
	 */
	public List<SubjectInfo> findBySubjectInfoList(String examId, String unitId,String classId);

	/**
	 * 
	 * @param examId
	 * @param courseId
	 * @param gradeCode
	 * @return
	 */
	SubjectInfo findByExamIdAndCourseIdAndGradeCode(String examId, String courseId, String gradeCode);
}
