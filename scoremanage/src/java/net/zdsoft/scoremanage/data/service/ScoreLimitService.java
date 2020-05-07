package net.zdsoft.scoremanage.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;

public interface ScoreLimitService extends BaseService<ScoreLimit, String>{

	/**
	 * 某次考试下的录分权限(searchDto:学年，学期，单位，考试id不能为空)
	 *  subjectId与subjectIds 优先subjectId 如果 subjectId==null 根据subjectIds查询
	 * @return
	 */
	public List<ScoreLimit> findBySearchDto(ScoreLimitSearchDto searchDto);
	
	
	public List<ScoreLimit> saveAllEntitys(ScoreLimit... scoreLimit);
	/**
	 * 
	 * @param dto
	 *  acadyear semester unitId examId  不能为空
	 *  subjectId classId teacherId 暂时不能为空
	 *   后续根据需求是否扩充
	 */
	public int deleteByClassIdAndTeacherId(ScoreLimitSearchDto dto);
	
	/**
	 * 某次考试下的老师的录分权限(不包括全局老师)
	 * @param searchDto  
	 *   acadyear semester unitId examId  teacherId 不能为空
	 *   subjectId与subjectIds 优先subjectId 如果 subjectId==null 根据subjectIds查询
	 * @return key:subjectId value:classIds
	 */
	public Map<String,Set<String>> findByTeacherId(ScoreLimitSearchDto searchDto);
	
	public List<String> findTeacherIdByUnitId(String unitId,String type);

}
