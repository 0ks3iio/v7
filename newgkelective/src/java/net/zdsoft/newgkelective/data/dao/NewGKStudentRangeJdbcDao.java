package net.zdsoft.newgkelective.data.dao;

import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;

public interface NewGKStudentRangeJdbcDao {

    /**
     * 查询分层学生数map(key-subjectId_range,value-studentNum)
     * @param unitId TODO
     * @param divideId
     * @param subjectType
     * @return
     */
	public Map<String, Integer> findCountMapByDivideIdAndSubjectType(String unitId, String divideId, String subjectType);

	public void insertBatch(List<NewGKStudentRange> studentRangeList);
	
	void deleteByDivideIdAnd(String unitId, String divideId, String subjectId, String subjectType, String[] range);

	void deleteByDivideIdAnd(String unitId, String divideId, String subjectId, String subjectType);

	void deleteByDivideIdAnd(String unitId, String divideId, String subjectType);
	
	void deleteByDivideId(String unitId, String divideId);
}
