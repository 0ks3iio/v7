package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfoEx;
import net.zdsoft.diathesis.data.entity.DiathesisScoreType;

import java.util.List;

public interface DiathesisScoreTypeService
        extends BaseService<DiathesisScoreType, String> {

    /**
     * gradeId为空则返回全部年级数据
     * @param unitId
     * @param gradeId
     * @param type
     * @return
     */
    List<DiathesisScoreType> findByUnitIdAndGradeIdAndType(String unitId, String gradeId, String type);
    
    List<DiathesisScoreType> findByUnitIdAndGradeIdAndTypeIn(String unitId, String gradeId, String[] type);

    /**
     * examName可为空
     * @param gradeId
     * @param type
     * @param scoreType
     * @param gradeCode
     * @param semester
     * @return
     */
    DiathesisScoreType findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(String gradeId, String type, String scoreType, String gradeCode, String semester, String examName);

    List<DiathesisScoreType> findComprehensiveList(String gradeId, String gradeCode, String semester);

    /**
     * 一并删除scoreInfo相关联的数据
     * @param id
     */
    void deleteById(String id);
    
    List<DiathesisScoreType> findListByGradeId(String unitId, String gradeId,String gradeCode,String semester,String type);
    

	void saveSate(List<DiathesisScoreInfo> scoreInfoList, DiathesisScoreType scoreType, String[] delScoreTypeIds);

    void save(DiathesisScoreType diathesisScoreType, List<DiathesisScoreInfo> diathesisScoreInfoList);

    List<DiathesisScoreType> findListByUnitIdsAndGradeCodeAndSemester(List<String> unitIds, String gradeCode, String year, Integer semester);


    List<DiathesisScoreType> findListByGradeIdAndSemesterAndTypeAndScoreType(String gradeId, String semester, String type, String scoreType);

    /**
     * 把教务管理里的成绩导入到综合素质
     * @param unitId
     * @param gradeId
     * @param semester
     * @param scoreInfoList
     */
    //void saveScoreToDiathesis(String unitId, String gradeId, String semester,String scoreType, List<ScoreInfo> scoreInfoList,String realName);

    DiathesisScoreType findTotalScoreType(String gradeId, String gradeCode, String semester);

    List<DiathesisScoreType> findListByUnitIdsAndSemesterAndYear(List<String> unitIds, Integer semester, String year);

    void deleteByIds(String[] typeIds);

    Integer countByUnitIdAndTypeAndScoreTypeIn(String unitId, String type, String[] scoreTypes);

    /**
     * 从教务管理中获取成绩 学分,负责人,审核人
     * @return
     */
    List<String> saveValueToDiathesis(String unitId, String gradeId, String gradeCode, String semester, String scoreType, String realName,String examId);

    String getAcadyearBy(String gradeId,String gradeCode);

    List<DiathesisScoreType> findListByUnitIdAndGradeIdAndType(String unitId, String gradeId, String type);

    void save(DiathesisScoreType diathesisScoreType, List<DiathesisScoreInfo> insertList, List<DiathesisScoreInfoEx> insertExList);

    List<DiathesisScoreType> findByUnitIdAndTypeAndGradeIdAndGradeCodeAndSemester(String unitId, String type, String gradeId, String gradeCode, Integer semester);
}
