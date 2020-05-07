package net.zdsoft.gkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkArrangeGroupResultDto;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkStuRemark;

public interface GkResultService extends BaseService<GkResult, String> {

    //public void saveGkResultDto(String arrangeId, StudentSubjectDto studentSubjectDto);
    
    /**
     * 更新学生选课班级
     * @param oldIds 原来的teachClassStu.id
     * @param groupId 调整后的组合班
     * @param stuId
     * @param roundsId TODO
     */
//    public void updateTeaClassStu(String oldIds, String groupId, String stuId, String roundsId);
    
    /**
     * 删除教学班学生选课记录
     * @param oldGroupId 组合时为groupclass.id，单科时为教学班id
     * @param stuId 
     * @param type 单科还是组合
     * @param roundId 
     */
//    public void deleteTeaClassStu(String oldGroupId, String stuId, String type, String roundId);
    
    public List<GkResult> findByArrangeId(String arrangeId);
    
    /**
     * 查询 已选课 学生学生选课结果
     * 
     * @param arrangeId
     * @param searchDto
     * @param page
     * @param notStudentIds 排除的stuIds
     * @return
     */
    public List<GkResult> findGkResult(String arrangeId, ChosenSubjectSearchDto searchDto, Pagination page, Set<String> notStudentIds);

    /**
     * 查询 未选课 学生学生选课结果
     * @param arrangeId
     * @param searchDto TODO
     * @param page
     * @param classId
     * 
     * @return
     */
    public List<GkResult> findUnChosenGkResult(String arrangeId, ChosenSubjectSearchDto searchDto, Pagination page, boolean selectAll,
            String... classIds);

    /**
     * 已选课学生总数统计
     * 
     * @param ArrangeId
     * @return
     */
    public Integer findisChosenStudentCountByArrangeId(String ArrangeId, String[] classIds);

    /**
     * 统计每门科目的选择人数
     * 
     * @param courseIds
     * @return
     */
    Map<String, Integer> findResultCountByArrangeIdAndCourseIds(String arrangeId, String[] courseIds,String[] stuids);

    /**
     * 根据学生id查询所选的课程
     * 
     * @param stuId
     * @return
     */
    public List<String> findCoursesByStuId(String stuId, String arrangeId);

    /**
     * 根据学生id查询选课结果
     * 
     * @param stuIds
     * @return
     */
    public List<GkResult> findGkByStuId(String[] stuIds, String arrangeId);

    /**
     * @param arrangeId
     * @param id
     */
    public void removeByArrangeIdAndStudentId(String arrangeId, String id);

    /**
     * 批量保存选课结果（id已存在则update）
     * 
     * @param gkResultList
     * @param stuRemarkList TODO
     * @param studentIds TODO
     */
    public void saveAll(List<GkResult> gkResultList, List<GkStuRemark> stuRemarkList, Set<String> studentIds);

    /**
     * 查询组合班内学生及科目
     * 
     * @param roundsId
     * @return <studentId_subjectId,教学班id_批次>
     */
    public Map<String,String> findCombineStudentMap(String roundsId);

    public List<GkArrangeGroupResultDto> findClassDtoList(String roundId, String classId);
 
    public void saveGkResultList(List<GkResult> gkResultList, String studentId, String arrangeId);

	public void updateStatus(List<GkResult> gkResults);

	public List<GkResult> findBySubjectId(String unitId, String arrangeId, String courseId, String classId, Pagination page);

	public List<GkResult> findByGroupSubjectId(String unitId, String arrangeId,
			String courseId, String classId, Pagination page);

	//public void saveNewGkResultDto(String arrangeId, GkResultDto gkResultDto);

	 /**
     * 查询学生选课情况
     * 
     * @param subjectArrangeId
     * @param subjectIds 可为空 某个组合
     * @return
     */
    public List<StudentSubjectDto> findAllStudentSubjectDto(String subjectArrangeId,Set<String> subjectIds);

	public List<StudentSubjectDto> findStudentSubjectDtoByStuId(
			String subjectArrangeId, String[] stuId);

	/**
	 * 查询学生选课数据
	 * @param subjectArrangeId
	 * @param stuIds
	 * @return
	 */
	public List<StudentSubjectDto> findStudentSubjectDtoByStudent(
			String subjectArrangeId, List<String> stuIds);

	public List<GkResult> findResultByArrangeIdAndCourseIds(String arrangeId, String[] courseIds, String[] stuids);
	/**
     * 特定优化查下不走in方法
     * @param arrangeId
     * @param gradeId
     * @param dto TODO
     * @return
     */
    public List<GkResult> findResultByChosenSubjectSearchDto(String arrangeId,String gradeId, ChosenSubjectSearchDto dto);
    
    /**
     * 
     * @param arrangeId
     * @param subjectIds String[]leng =3 学生学的三个科目
     */
    public void deleteBySubjectArrangeIdAndSubjectId(String arrangeId,
			String[] subjectIds);

	public void deleteByArrangeIdAndStudentIds(String arrangeId, String[] studentIds);
	/**
	 * 根据年级id获取选考科目列表
	 * @param gradeId
	 * @return
	 */
	public Map<String, Set<String>> getGkResultByGradeId(String gradeId);
	
	public List<StudentSubjectDto> findStudentSubjectDto(
			String subjectArrangeId,Set<String> subjectIds,String[] stuids);
}
