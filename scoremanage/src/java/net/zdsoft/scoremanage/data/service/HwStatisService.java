package net.zdsoft.scoremanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.scoremanage.data.entity.HwStatis;

import java.util.List;
import java.util.Map;

/**
 * @author niuchao
 * @date 2019/11/5 11:29
 */
public interface HwStatisService extends BaseService<HwStatis, String>{

    /**
     * 查询方案成绩
     * @param unitId
     * @param planId
     * @param planType
     * @param makeScore 是否组装成绩数据
     * @param page 分页
     * @return
     */
    List<HwStatis> findListByPlanId(String unitId, String planId, String planType, boolean makeScore, Pagination page);

    /**
     * 查询方案成绩(包含组装成绩)
     * @param unitId
     * @param planIds
     * @param planType
     * @return
     */
    List<HwStatis> findListByPlanIds(String unitId, String[] planIds, String planType);

    /**
     *根据学生id查询方案成绩(包含组装成绩)
     * @param unitId
     * @param planId
     * @param planType
     * @param studentId
     * @return
     */
    HwStatis findOneByPlanIdAndStudentId(String unitId, String planId, String planType, String studentId);

    /**
     * 根据条件查询方案成绩(包含组装成绩)
     * @param unitId
     * @param planType
     * @param conditions
     * @return
     */
    List<HwStatis> findListByConditions(String unitId, String planType, List<String> conditions);

    /**
     * 按照汇总方案的设置,统计该年级下所有学生的的成绩信息
     * @param unitId
     * @param gradeId
     */
    void saveStatisticGrade(String unitId, String gradeId);

    List<List<String>> getReportByStudentId(String unitId, String studentId);

    List<HwStatis> findListByUnitIdAndPlanTypeAndStudentId(String unitId, String planType, String studentId);

    Map<String,List<List<String>>> getReportByClassId(String unitId, String classId);

    /**
     * 统计个人汇总 年级数据
     * @param unitId
     * @param gradeId
     */
    void savePersonalGrade(String unitId, String gradeId);

    /**
     * 个人成绩汇总获取
     * @param unitId
     * @param studentId
     * @return
     */
    List<List<String>> getPersonalReportByStudentId(String unitId, String studentId);

    Map<String, List<List<String>>> getPersonalReportByClassId(String unitId, String classId);

    Integer countDataByPlanType(String unitId, String planType, String classId, String studentId);
}
