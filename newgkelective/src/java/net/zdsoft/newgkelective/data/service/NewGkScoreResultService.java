package net.zdsoft.newgkelective.data.service;


import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;

public interface NewGkScoreResultService extends BaseService<NewGkScoreResult, String>{

	public String doImport(String unitId, String gradeId, String[] realTitles,List<String[]> datas,String filePath,boolean isNoTitle);
	public List<NewGkScoreResult> findByReferScoreIds(String unitId, String[] referScoreIds);

	
	List<NewGkScoreResult> findListByReferScoreId(String unitId, String referScoreId);
	/**
	 * 
	 * @param referScoreId
	 * @return key1:student_id key2:subject_id value2:score
	 */
	Map<String,Map<String, Float>> findMapByReferScoreId(String unitId, String referScoreId);
	
	public List<NewGkScoreResult> findByReferScoreIdAndSubjectId(String unitId,
			String referScoreId, String subjectId);
	
	List<NewGkScoreResult> findByReferScoreIdAndStudentIdIn(String unitId, String referScoreId, String[] studentIds);
	/**
	 * 查询某次学生参考成绩
	 * @param referScoreId
	 * @param byRedis 是否通过缓存获取
	 * @return
	 */
	List<NewGkScoreResult> findListByReferScoreId(String unitId, String referScoreId, boolean byRedis);
	
	/**
	 * 统计成绩结果表中学生数量
	 * @param referScoreIds
	 * @return
	 */
	public Map<String,Integer> findCountByReferId(String unitId, String[] referScoreIds);
	
	/**
	 * 统计成绩结果表 各科目平均分
	 * @param referScoreIds
	 * @return
	 */
	public Map<String,Map<String,Float>> findCountSubjectByReferId(String unitId, String[] referScoreIds);

    // Basedata Sync Method
    void deleteByStudentIds(String... stuids);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subids);
    
	public void deleteByReferScoreId(String referScoreId);
}
