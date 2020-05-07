package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.stutotality.data.entity.StutotalityStuResult;

import java.util.List;
import java.util.Set;

public interface StutotalityStuResultService extends BaseService<StutotalityStuResult,String>{
    List<StutotalityStuResult> findListByOptionIds(String year,String semester,String[] studentIds,String[] optionIds,String resultType);

    List<StutotalityStuResult> findListByOptionIdsWithMaster(String year,String semester,String[] studentIds,String[] optionIds,String resultType);

    List<StutotalityStuResult> findListByItemIds(String year,String semester,String studentId,String[] itemIds,String resultType);

    List<StutotalityStuResult> findListByItemIdsWithMaster(String year,String semester,String studentId,String[] itemIds,String resultType);

    List<StutotalityStuResult> findByAcadyearAndSemesterAndUnitIdAndTypeWithMaster(String year,String semester,String unitId,String type,String studentId);

    List<StutotalityStuResult> findItemHealthIdAndOptionId(String year,String semester,String unitId,String type,String itemHealthId,String optionId);

    List<StutotalityStuResult> findItemHealthIdAndOptionIdWithMaster(String year,String semester,String unitId,String type,String itemHealthId,String optionId);

    List<StutotalityStuResult> findItemHealthId(String year,String semester,String unitId,String type,String itemHealthId);

    List<StutotalityStuResult> findItemHealthIdWithMaster(String year,String semester,String unitId,String type,String itemHealthId);

    /**
     * 获取该学生成绩
     * @param unitId
     * @param acadyear
     * @param semester
     * @param studentId
     * @param itemIds 可为空
     * @return
     */
    List<StutotalityStuResult> findListByParms(String unitId,String acadyear,String semester,String studentId,String[] itemIds);

    /**
     * 定制接口 获取某些学生的所有非身体素质结果数据   默认去除type是3的数据
     * @param acadyear
     * @param semester
     * @param studentIds
     * @return
     */
    List<StutotalityStuResult> findListByStudentIds(String acadyear,String semester,String[] studentIds);

    List<StutotalityStuResult> findListByStudentIds(String acadyear,String semester,String[] studentIds,String[] itemHealthIds);

    void saveData(String unitId, String acadyear, String semester, Set<String> studentId, String Type, List<StutotalityStuResult> list);

    void deleteByStudentIds(String unitId,String acadyear,String semester,String[] studentId,String Type);

    String saveImportData (String classId,List<String[]> list, LoginInfo loginInfo);

    String saveImportData1 (String classId,List<String[]> list, LoginInfo loginInfo);
}
