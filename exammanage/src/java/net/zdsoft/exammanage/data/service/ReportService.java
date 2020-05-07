package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.exammanage.data.entity.EmExamInfo;

import java.util.List;


public interface ReportService {

    /**
     * 获取教学班内某科目成绩排名报表(1)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param classId
     * @param subType
     * @return
     */
    String getJsonByUnitIdAndExamId(String unitId, String examId, String acadyear, String semter, String grade);

    /**
     * 获取教学班内某科目成绩排名报表(2)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param classId
     * @param subType
     * @return
     */
    String getJsonByUnitIdAndExamGradeRank(String unitId, String examId, String acadyear, String semter, String gradeCode);

    /**
     * 获取教学班内某科目成绩排名报表(3)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param classId
     * @param subType
     * @return
     */
    String getJsonByUnitIdAndExamSubjectRank(String unitId, String examId, String acadyear, String semter, String gradeCode, String subjectId, String subType);

    /**
     * 获取教学班内某科目成绩排名报表(4)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param classId
     * @param subType
     * @return
     */
    public String getClassScoreRank(String unitId, String acadyear, String semester, String examId,
                                    String gradeCode, String subjectId, String classId, String subType);

    /**
     * 考务自己的获取(年级)教学班内某科目成绩排名报表(4)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param classId
     * @param subType
     * @return
     */
    public String getClassScoreRankBySelf(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String classId, String subType);

    /**
     * 获取各教学班平均分排名表(5)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param subType
     * @return
     */
    public String getClassSubRank(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType);

    /**
     * 考务自身获取各教学班平均分排名表(5)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param subType
     * @return
     */
    public String getClassSubRankBySelf(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType);

    /**
     * 人数占比对比表   (6)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param subType
     * @return
     */
    public String getClassScore(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType);

    /**
     * 名次段占比对比表   (20)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param subType
     * @return
     */
    public String getClassRank(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType);

    /**
     * 获取赋分等级对比表(7)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param subType
     * @return
     */
    public String getIsConScore(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType);

    /**
     * 获取历次考试对比表 (8)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeCode
     * @param subjectId
     * @param subType
     * @param classId   教学或行政班id
     * @return
     */
    public String getAllExamSubject(String unitId, String acadyear, String semester, String gradeCode, String subjectId, String subType, String classId);

    /**
     * 考务自身获取历次考试对比表 (8)
     */
    public String getAllExamSubjectBySelf(String unitId, String acadyear, String semester, String gradeCode, String subjectId, String subType, String classId);

    /**
     * 获取教学班内某科目成绩排名报表(9)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param subjectId
     * @param classId
     * @param subType
     * @return
     */
    String getJsonByUnitIdAndExamIdAndClassId(String unitId, String examId, String acadyear, String semter, String classId);

    /**
     * 获取某考试下所有科目的排名表(10)
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeCode
     * @param examId
     * @param classId
     * @return
     */
    public String getExamAllSubject(String unitId, String acadyear, String semester, String gradeCode, String examId, String classId);

    /**
     * 考务自己年级获取某考试下所有科目的排名表(10)
     * tableType :1考试分总分排名表     tableType:2 赋分    3代表综合能力分报表，考试分数据，以综合能力分排序
     */
    public String getExamAllSubjectBySelf(String unitId, String acadyear, String semester, String gradeCode, String examId, String tableType);

    /**
     * 考务自己行政班获取某考试下所有科目的排名表(9)
     * tableType :1考试分总分排名表     tableType:2 赋分
     */
    public String getExamClassAllSubjectBySelf(String unitId, String acadyear, String semester, String classId, String examId, String tableType);

    /**
     * 同一考试各行政班排名 11,12
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeCode
     * @param examId
     * @param subjectParam
     * @return
     */
    public String getScoreRankingByClass(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam);

    /**
     * 考务自己行政班同一考试各行政班排名 11,12
     */
    public String getScoreRankingByClassSelf(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam);

    /**
     * 获取某个分数段各个行政部的占比人数 13,14
     *
     * @param unitId
     * @param searchAcadyear
     * @param searchSemester
     * @param searchGradeCode
     * @param examId
     * @param subjectParam
     * @return
     */
    public String getScoreDistributeByClass(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam);

    /**
     * 考务自己获取某个分数段各个行政部的占比人数 13,14
     * tableType :1考试分总分排名表     tableType:2 赋分
     *
     * @param gradeCode TODO
     */
    public String getScoreDistributeByClassSelf(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectParam);

    /**
     * 考务自己获取某个名次段各个行政部的占比人数
     * tableType :1考试分总分排名表     tableType:2 赋分
     *
     * @param gradeCode TODO
     */
    public String getRankDistributeByClassSelf(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectParam);

    /**
     * 获取某个行政班历次考试的对比表 15,16
     *
     * @param unitId
     * @param searchAcadyear
     * @param searchSemester
     * @param classId
     * @param subjectParam
     * @return
     */
    public String getScoreCountByClassId(String unitId, String acadyear, String semester, String classId, String subjectParam);

    /**
     * 考务自己获取某个行政班历次考试的对比表 15,16
     * tableType :1考试分总分排名表     tableType:2 赋分
     *
     * @param gradeCode TODO
     */
    public String getScoreCountByClassIdSelf(String unitId, String acadyear, String semester, String classId, String subjectParam);

    /**
     * 获取某个学生某次考试的各科目的详情 17
     *
     * @param unitId
     * @param searchAcadyear
     * @param searchSemester
     * @param classId
     * @param examId
     * @param studentId
     * @return
     */
    public String getScoreDetailByStudentId(String unitId, String acadyear, String semester, String classId, String examId, String studentId);

    /**
     * 考务自己获取某个学生历次考试的各科目的详情 17
     *
     * @param unitId
     * @param acadyear
     * @param semester  特殊3代表显示该学年下 两学期的考试
     * @param conType   1是图表考试分  2是图表赋分，此只获取高考模式
     * @param gradeCode
     * @param classId
     * @param studentId
     * @param showAll   1代表显示所有学年学期的考试
     * @return
     */
    public String getAllExamStuDetailBySelf(String unitId, String acadyear, String semester
            , String gradeCode, String classId, String studentId, String conType, String showAll);

    /**
     * 获取该学生 某学年学期下的所有考试
     *
     * @param studentId
     * @param acadyear
     * @param semester
     * @param conType
     * @return
     */
    public List<EmExamInfo> queryExamsByStudentId(String studentId, String acadyear, String semester, String conType);

    /**
     * 学生成绩
     *
     * @param studentId
     * @param unitId
     * @param examId
     * @return
     */
    public String getAllScoreByStudentId(String studentId, String unitId, String examId);

    /**
     * 学生历次考试成绩排名
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param studentId
     * @return
     */
    public String getStuAllExamScore(String unitId, String acadyear, String semester, String studentId);

    /**
     * 学生历次考试科目排名
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param studentId
     * @return
     */
    public String getStuAllExamRank(String unitId, String acadyear, String semester, String studentId);

    /**
     * 获取改考试下所有的科目列表
     *
     * @param unitId
     * @param examId
     * @param gkType
     * @return
     */
    public List<Course> getSubList(String unitId, String examId, String gkType);

    /**
     * 获取报表的标题
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examId
     * @param gradeCode
     * @param gradeName
     * @param subjectName
     * @param classId
     * @param className
     * @param name
     * @return
     */
    public String getTitle(String unitId, String acadyear, String semester, String examId, String gradeCode,
                           String gradeName, String subjectName, String classId, String className, String name);
}
