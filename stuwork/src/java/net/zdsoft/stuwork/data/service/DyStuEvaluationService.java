package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.dto.DyStuEvaluationDto;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;

public interface DyStuEvaluationService extends BaseService<DyStuEvaluation, String>{
	/**
	 * 查询list
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public List<DyStuEvaluation> findListByUidAndDto(String unitId,DyBusinessOptionDto dto);
	/**
	 * 查询DyStuEvaluation
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public DyStuEvaluation findOneByUidAndDto(String unitId,DyBusinessOptionDto dto);
	/**
	 * 查询statlist
	 * @param unitId
	 * @param stuList
	 * @return
	 */
	public List<DyStuEvaluationDto> findListByUidAndCid(String unitId,Clazz clazz);
	//public List<DyStuEvaluation> findListByUidAndStuList(String unitId,List<Student> stuList);
	/**
	 * 查询statlist
	 * @param unitId
	 * @param stuList
	 * @return
	 */
	//public List<DyStuEvaluation> setProperty(List<DyStuEvaluation> stuEvaluationList,List<Student> stuList);
	/**
	 * 导入学生评语信息
	 * @param unitId
	 * @param dto
	 * @param datas
	 * @return
	 */
	public String doImport(String unitId, DyBusinessOptionDto dto, List<String[]> datas);
	
	public List<DyStuEvaluation> findByStudentId(String studentId);
	
	public List<DyStuEvaluation> findByStudentIdIn(String[] studentIds);
	/**
	 * 
	 * @param studentIds 可为空
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<DyStuEvaluation> findByStudentIdIn(String[] studentIds,String unitId,String acadyear,String semester);
	
	public List<DyStuEvaluation> findByUnitIdAndGradeIds(String unitId, String[] gradeIds);
}
