package net.zdsoft.comprehensive.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.comprehensive.dto.CompreScoreMapDto;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.comprehensive.entity.CompreSetup;
import net.zdsoft.framework.entity.Pagination;

public interface CompreScoreService extends BaseService<CompreScore, String>{

	/**
	 * 查询总评成绩
	 * @param unitId
	 * @param examId
	 * @param subjectIds
	 * @param page 
	 * @return
	 */
	public List<CompreScore> findByExamIdSubjectIdIn(String unitId, String examId,String type,
			String[] subjectIds, Pagination page);

	/**
	 * 保存数据
	 * @param unitId
	 * @param gradeId
	 * @param userId
	 * @return
	 */
	public void  saveAll(String unitId,String acadyear,String semester,String gradeId,String classId,String userId,List<CompreScore> scoreList);
	

	/**
	 * 查询学考原始成绩 未封装
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classIds scoreType
	 * @return
	 */
	public List<CompreScore> getFirstList(String unitId,String acadyear,String semester,String scoreType,String[] classIds);
	/**
	 * 导入原始成绩数据
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param params
	 * @param datas
	 * @return
	 */
	public String doImport(String unitId,String acadyear,String semester,String params,List<String[]> datas);
	/**
	 * 换算学考原始成绩
	 * @param unitId
	 * @param acadyear
	 * @param string
	 * @param gradeId
	 * @param typeXk
	 * @param userId
	 */
	public void saveConvertFirst(String unitId, String acadyear, String string,
			String gradeId, String userId);
	
	public List<CompreScore> findByStudentId(String unitId,String acadyear,String semester,String type,String[] studentIds);
	
	
	public List<CompreScore> findByExamIdIn(String unitId, String scoremanageType, String[] examIds);
	
	public void saveAllScores(String unitId, String searchAcadyear,String searchSemester, String examId, String userId, List<CompreScore> compreScores);
	
	public List<CompreScore> findExamAllScores(String searchAcadyear,String searchSemester, String unitId,String inputType, String scoresType, String[] examIds);

	public void updateParamInfoList(String unitId, String infoKey);
	
	public void deleteAndSaveScoreSave(String unitId, String comInfoId,
			String searchAcadyear, String searchSemester, String userId,
			List<CompreScore> compreScores, String[] studentIds);
	public void deleteAndSaveScoreRank(String englishId , String englishOralTestId,String unitId, String comInfoId,
			String searchAcadyear, String searchSemester, String userId,
			List<CompreScore> compreScores, List<CompreScore> englishScoreList, List<CompreScore> englishOralTestScoreList);
	
	public String zHSZImport(String unitId, String examId, String gradeId, String operatorId,
			List<String[]> datas);
	
	public List<CompreScore> queryByIdIn(String[] ids);
	
	
//分割线，以上为原有方法，方便后续删除
	/**
	 * 查询综合素质计算所需要的总评成绩
	 * @param compreInfoId
	 * @param isAll true:学科只取总分 false:所有总评成绩
	 * @return
	 */
	public List<CompreScore> findByCompreInfoId(String compreInfoId, boolean isAll);
	

    /**
     * 查询学考换算或原始成绩
     * @param unitId
     * @param acadyear
     * @param semester
     * @param classIds
     * @return
     */
    List<CompreScoreMapDto> getFirstOrFinallyList(String unitId, String scoreManageType, String compreInfoId, String[] classIds, boolean isFirst, Pagination page);

    /**
     * 同步数据
     * @param unitId
     * @param gradeId
     * @param examType
     * @return
     */
    void saveSynFirst(String unitId, String acadyear, String semester,
                             String gradeId, String examType);

    List<CompreScore> findByStudentIdAndType(String studentId, String type);

    List<CompreScore> findByUnitIdAndType(String unitId, String typeXk);
    
    /**
     * 保存设置
     * @param infoId
     * @param type
     * @param saveSetupList
     */
	void saveAndDelete(String infoId, String type, List<CompreSetup> saveSetupList);

	/**
	 * 根据类型查询成绩
	 * @param unitId
	 * @param infoId
	 * @param type
	 * @return
	 */
	List<CompreScore> findByInfoIdAndType(String unitId, String infoId, String type);

	/**
	 * 统计保存
	 * @param info
	 * @param scoreList
	 */
	void saveAll(CompreInfo info, List<CompreScore> scoreList);

	public List<CompreScore> findByStudentIdsAndType(String[] studentIds, String type);
	
}
