package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;

public interface NewGkReferScoreService extends BaseService<NewGkReferScore, String>{

	public List<NewGkReferScore> findByNames(String unitId, String gradeId, String[] names);
	/**
	 * 根据年级id查询成绩整体信息
	 * @param unitId
	 * @param gradeId
	 * @param isMakeData(是否需要设置辅助字段)
	 * @param isAll(是否包括没有成绩的数据)
	 * @return
	 */
	public List<NewGkReferScore> findListByGradeId(String unitId,String gradeId,boolean isMakeData,boolean isAll);
	public List<NewGkReferScore> findListByGradeIdWithMaster(String unitId,String gradeId,boolean isMakeData,boolean isAll);
	
	public List<NewGkReferScore> findByUnitId(String unitId);
	/**
	 * 软删
	 * @param id
	 */
	public void deleteById(String id);
	/**
	 * 如果isMakeData为true,则unitId必填，否则可为空
	 * @param referScoreId
	 * @param isMakeData
	 * @param unitId
	 * @return
	 */
	public NewGkReferScore findById(String referScoreId, boolean isMakeData, String unitId);
	public String findDefaultIdByGradeId(String unitId, String gradeId);
	public String findDefaultIdByGradeId(String gradeId);

    // Basedata Sync Method
    void deleteByGradeIds(String... gradeIds);
	
}
