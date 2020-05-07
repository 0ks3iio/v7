package net.zdsoft.exammanage.remote.service;

import java.util.Set;

public interface ExamManageRemoteService {

    /**
     * 查找考试列表
     *
     * @param gradeCode 年级代码 31、32、33。。。
     */
    public String getExamList(String unitId, String gradeCode, String acadyear, String semester);

    /**
     * 查考学校考试的考试安排信息
     */
    public String getExamArrange(String examId, String unitId);

    /**
     * 查找examId对应考试，subjectId科目，所对应的场地集合
     * 请求参数：考试id,科目id
     * 返回参数：Set<场地id>
     *
     * @param examId
     * @param subjectId
     * @param unitId    TODO
     * @return
     */
    public Set<String> getExamSubjectPlaceIds(String examId, String subjectId, String subType, String unitId);

    /**
     * 获取门贴信息
     * 请求参数：考试id,科目id，场地id
     * 返回参数：考场名称、考试科目、考试时间段、监考老师、考生名单（姓名、座位号）
     *
     * @param examId
     * @param subjectId
     * @param placeId
     * @return
     */
    public String getDoorSticker(String examId, String subjectId, String unitId, String placeId, String subType);

    /**
     * 同步 scomanange数据
     * ExamInfo examInfo
     * List<ScoreInfo> scoreList
     * List<SubjectInfo> subInfoList
     *
     * @param jsonString
     * @return
     */
    public String doSynch(String jsonString);

    public String getExamSubByUnitIdAndExamId(String unitId, String... examId);

    public String getExamScoreInfoByExamIdAndUnitId(String unitId, String examId);
}
