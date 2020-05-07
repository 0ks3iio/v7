package net.zdsoft.scoremanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.HwReinstate;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:29
 */
public interface HwReinstateService extends BaseService<HwReinstate, String>{

    /**
     * 查询复学学生考试插入方案
     * @param unitId
     * @param acadyear
     * @param semester
     * @param studentId
     * @return
     */
    List<HwReinstate> findListByStudentId(String unitId, String acadyear, String semester, String studentId);

    /**
     * 查询复学学生考试插入方案
     * @param unitId
     * @param acadyear
     * @param semester
     * @param studentId
     * @param examId
     * @return
     */
    HwReinstate findOneByStudentId(String unitId, String acadyear, String semester, String studentId, String examId);

    /**
     * 新增复学学生考试插入方案
     * @param hwReinstate
     * @param oldStatis
     * @param statis
     */
    void saveReinstate(HwReinstate hwReinstate, HwStatis oldStatis, HwStatis statis);

    /**
     * 删除复学学生考试插入方案
     * @param hwReinstate
     * @param statis
     * @param statisExList
     */
    void deleteReinstate(HwReinstate hwReinstate, HwStatis statis, List<HwStatisEx> statisExList);

    /**
     * 查找复学学生考试插入方案
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeId
     * @param examId
     * @return
     */
    List<HwReinstate> findListByGradeIdAndExamId(String unitId, String acadyear, String semester, String gradeId, String examId);
}
