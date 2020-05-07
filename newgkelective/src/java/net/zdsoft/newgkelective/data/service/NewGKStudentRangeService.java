package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.NewGKStudentRangeDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;

public interface NewGKStudentRangeService extends BaseService<NewGKStudentRange, String>{
	/**
	 * 
	 * @param divideId
	 * @param subjectId 可为空，这时查询某次分班的所有科目的分层情况
	 * @param subjectType 
	 * @param range 
	 * @return
	 */
	public List<NewGKStudentRange> findByDivideIdSubjectIdAndSubjectType(String divideId, String subjectId, String subjectType, String range);

	/**
	 * 
	 * @param unitId TODO
	 * @param divideId
	 * @param subjectId
	 * @param subjectType
	 * @param range  可为null,此时删除所有等级的指定条目
	 */
	public void deleteByDivideIdSubjectIdAndRange(String unitId, String divideId, String subjectId, String subjectType, String[] range);

	public void updateStudentRange(String unitId, String divideId, String subjectId, String subjectType,
			String[] range, List<NewGKStudentRange> stuRangeList);
	/**
	 * 查询分层学生集合
	 * @param unitId TODO
	 * @param divideId
	 * @param subjectType
	 * @return
	 */
	public List<NewGKStudentRangeDto> findDtoListByDivideIdAndSubjectType(String unitId,String divideId, String subjectType);

	/**
	 * 查询分层学生集合
	 * @param divideId
	 * @param subjectType
	 * @return
	 */
	public List<NewGKStudentRangeEx> findExListByDivideIdAndSubjectType(String divideId, String subjectType);

	public List<NewGKStudentRange> findByDivideId(String unitId, String divideId);
	
	public Map<String, Integer> findStuRangeCount(String unitId, String divideId, String subjectId, String subjectType);

	/**
	 * 返回各科目分的层级
	 * @param unitId
	 * @param divideId
	 * @param subjectType
	 * @return
	 */
	public Map<String, Set<String>> findSubjectRangeSet(String unitId, String divideId, String subjectType);

	public void deleteByDivideId(String unitId, String divideId);

    // Basedata Sync Method
    void deleteByStudentIds(String... stuids);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subids);
    
    public List<NewGKStudentRange> findByDivideIdAndStudentId(String unitId, String divideId,String studentId);
}
