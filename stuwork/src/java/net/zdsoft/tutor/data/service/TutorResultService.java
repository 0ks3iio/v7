package net.zdsoft.tutor.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.tutor.data.entity.TutorResult;

/**
 * @author yangsj  2017年9月11日下午8:18:12
 */
public interface TutorResultService extends BaseService<TutorResult, String> {

	/**
	 * @param unitId
	 * @return
	 */
	List<TutorResult> findByUnitId(String unitId);

	/**
	 * @param tutorRoundId
	 */
	void deleteByRoundId(String tutorRoundId);

	/**
	 * @param tid
	 */
	List<TutorResult> findByTeacherId(String tid);

	/**
	 * @param tutorRoundId
	 * @return
	 */
	List<TutorResult> findByRoundId(String tutorRoundId);

	/**
	 * @param studentId
	 * @return
	 */
	TutorResult findByStudentId(String studentId);
	/**
	 * 被导师设置毕业的学生选导结果
	 * @param studentId
	 * @return
	 */
	public List<TutorResult> findByStudentIdDel(String studentId);
	/**
	 * 根据教师ids取导师结果
	 * @param tids
	 * @return
	 */
	public List<TutorResult> findByTeacherIds(String[] tids);
	/**
	 * @param studentIds
	 * @return
	 */
	public List<TutorResult> findByStudentIds(String[] studentIds);
	/**
	 * 更换导师，导师记录也一起更换
	 * @param updateResults
	 */
	public void updateTutor(String[] updateStuIds, String teacherId,String tutorId);

    
	String save (TutorResult tutorResult,String param,String teacherId);
	/**
	 * 导入学生导师
	 * @param unitId
	 * @param datas
	 * @return
	 */
	public String doImport(String unitId, List<String[]> datas);
	/**
	 * 批量换老师，更新结果表和记录表
	 * @param updateList
	 */
	public void updateAll(List<TutorResult> updateList);

	/**
	 * 根据轮次更新state
	 * @param updateList
	 */
	void updateStateByRoundId(Integer state, String... roundIds);

	/**
	 * 获取当前学段的导师
	 * @param studentId
	 * @param section
	 * @return
	 */
//	TutorResult findByStudentIdAndSection(String studentId, Integer section);

	/**
	 * 判断当前的学生是否已经毕业
	 * @param studentId
	 * @param section
	 * @return
	 */
//	List<TutorResult> findByStudentIdDelAndSection(String studentId,Integer section);

	/**
	 * 获取当前单位学段的导师结果
	 * @param unitId
	 * @param section
	 * @return
	 */
//	List<TutorResult> findByUnitIdAndSection(String unitId, Integer section);


}
