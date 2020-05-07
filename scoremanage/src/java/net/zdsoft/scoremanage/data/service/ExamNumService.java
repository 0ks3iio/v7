package net.zdsoft.scoremanage.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.StudentDto;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ExamNum;

public interface ExamNumService extends BaseService<ExamNum,String>{

	/**
	 * 某次考试下 某个单位下<学生id,学生考号>
	 * @param examId
	 * @param schoolId(可为空)
	 * @return
	 */
	public Map<String, String> findByExamId(String examId,String schoolId);

	/**
	 *删除某次考试部分学生的考号
	 * @param examId
	 * @param stuIds
	 */
	public void deleteByexamIdStudent(String examId, String[] stuIds);

	/**
	 * 删除末次考试某个学校的所有学生考号
	 * @param schoolId
	 * @param examId
	 */
	public void deleteBySchoolIdExamId(String schoolId, String examId);

	public void insertList(List<ExamNum> insertList,String schoolId,String examId);

	public List<ExamNum> saveAllEntitys(ExamNum... examNum);

	/**
	 * 批量按保存某次考试学生考号
	 * @param studentDtoList
	 */
	public void save(String examId,List<StudentDto> studentDtoList);

	public List<ExamNum> findByExamIdList(String examId,String schoolId);
}
