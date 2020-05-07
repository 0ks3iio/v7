/*
 * Project: v7
 * Author : shenke
 * @(#) StudentNormalFlowService.java Created on 2016-8-5
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.dto.StudentFlowDto;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentFlow;
import net.zdsoft.framework.entity.Pagination;

/**
 * @description: 学生调动
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-5上午10:43:59
 */
public interface StudentFlowService extends BaseService<StudentFlow, String> {

    /**
     * 学生调入或者调出保存
     * 
     * @param studentFlow
     */
    public StudentFlow save(Student student, StudentFlow studentFlow);

    /**
     * 学生调动历史记录查询
     * 
     * @param studentIds
     *            学生Ids
     * @param startDate
     *            时间节点 开始
     * @param endDate
     *            时间节点 结束
     * @param unitId
     *            (schoolId) unitId不为空时 查询当前学校的学生,否则查询全部学生
     * @param studentFlowType
     *            调动类型 {@code StudentFlow.STUDENT_FLOW_LEAVE StudentFlow.STUDENT_FLOW_IN}
     * @param page
     *            page 可为空 不分页
     */
    public List<StudentFlow> searchFlowHistory(final String[] studentIds, final Date startDate, final Date endDate,
            final String studentFlowType, String unitId, Pagination page);

    /**
     * 学生调动历史记录查询
     * 
     * @param studentname
     *            学生姓名
     * @param startDate
     *            时间节点 开始
     * @param endDate
     *            时间节点 结束
     * @param page
     *            unitId不为空时 查询当前学校的学生,否则查询全部学生
     * @param studentFlowType
     *            调动类型 {@code StudentFlow.STUDENT_FLOW_LEAVE StudentFlow.STUDENT_FLOW_IN}
     * @param unitId
     * @param isEdu
     * @return
     */
    public List<StudentFlowDto> searchFlowLogs(final String studentname, final Date startDate, final Date endDate,
            final String studentFlowType, Pagination page, String unitId, boolean isEdu);

    /**
     * 调动学生详细查询
     * 
     * @param studentname
     * @param identityCard
     * @param pin
     * @param flowType
     *            调动类型
     * @param page
     *            page is null query all
     * @return
     */
    public List<StudentFlow> searchFlows(String studentname, String identityCard, String pin, String flowType,
            Pagination page);

    public void batchSave(List<StudentFlow> studentFlows, List<Student> students, String type);

    public void saveImport(MultipartFile uploadFile, String userId, String unitId, String type)
            throws IllegalStateException, IOException;

    public void batchImport(ImportEntity importEntit, String flowType) throws IOException;

    void importStudentFlow(ImportEntity importEntity, String flowType) throws Exception;

    public ImportEntity checkStudentFlow(String importTypeIn);

    public void remoteOutSchool(String unitId, String[] studentIds);

    public List<StudentFlow> saveAllEntitys(StudentFlow... studentFlow);

}
