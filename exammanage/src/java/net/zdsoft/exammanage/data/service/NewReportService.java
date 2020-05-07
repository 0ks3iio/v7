package net.zdsoft.exammanage.data.service;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmStat;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.entity.EmStatRange;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

public interface NewReportService {
    /**
     * type：3  4 班主任/ 任课老师報告
     *
     * @param map
     * @param unitId
     * @param examId
     * @param classId
     * @param subjectId
     * @param subType
     */
    public void getClassReport(ModelMap map, String unitId, String examId, String classId, String subjectId, String subType);

    /**
     * type:1 2
     * 获取前几部分
     *
     * @param map
     * @param unitId
     * @param examId
     * @param subjectId
     * @param subType
     */
    public void getReport(ModelMap map, String unitId, String examId, String subjectId, String subType);

    /**
     * 获取前几部分
     *
     * @param map
     * @param unitId
     * @param examId
     * @param studentId
     */
    public void getStuReport(ModelMap map, String unitId, String examId, String studentId);

    /**
     * type:1 2
     * 根据考试id 获取 学科总体概况
     *
     * @param examId
     * @return
     */
    public List<EmStatRange> getTotalList(String unitId, String examId, String subjectId, String subType, EmStatObject emStatObject);

    /**
     * type:1 2
     * 获取总分 以及赋分的年级排名信息
     *
     * @param unitId
     * @param examId
     * @param gradeId
     * @param emStatObject
     * @param examScoreList
     * @param subScoreList
     */
    public void getExamAllSubjectBySelf(String unitId, String examId, String gradeId, String subjectId, String subType, String classId, EmStatObject emStatObject
            , List<EmStat> examScoreList, List<EmStat> subScoreList);

    /**
     * type:1 2
     * 总分或某科目进步度分析
     *
     * @param jsonData
     * @param examId
     * @param unitId
     */
    public void getZeroScoreComJson(JSONObject jsonData, String examId, String subjectId, String subType, String unitId);

    /**
     * type:1 2
     * 班级总分或某科目进步度分析
     *
     * @param jsonData
     * @param examId
     * @param unitId
     */
    public void getClassZreoScoreCom(ModelMap map, String examId, String classId, String subjectId, String subType, String unitId);

    /**
     * type:1 2
     * 总分进步度分析 -进步度排名
     *
     * @param jsonData
     * @param examId
     * @param compareId
     * @param unitId
     */
    public void getZeroProCom(JSONObject jsonData, String examId, String compareId, String unitId, String subjectId, String subType);

    /**
     * type:1 2 3
     * 科目均衡度分析
     *
     * @param jsonData
     * @param examId
     * @param unitId
     */
    public void getSubjectComJson(JSONObject jsonData, String examId, String unitId, String classId);

    /**
     * 学生科目均衡度分析
     *
     * @param jsonData
     * @param examId
     * @param unitId
     */
    public void getStuSubjectComJson(JSONObject jsonData, String examId, String unitId, String studentId);

    /**
     * type:1 2
     * 报告分数段
     *
     * @param jsonData
     * @param jsonString
     * @param subjectFlag
     * @param flagRank
     */
    public void getClassScore(JSONObject jsonData, String jsonString, boolean subjectFlag, boolean flagRank);

    /**
     * type:1 2
     * 报告 -平均分
     *
     * @param jsonData
     * @param jsonString
     */
    public void getAvgScore(JSONObject jsonData, String jsonString, boolean flag);

    /**
     * type:1 2
     * key examId
     *
     * @param examIds
     * @param unitId
     * @param examList
     * @return
     */
    public Map<String, List<Course>> getMapCoursrByExamIds(String[] examIds, String unitId, List<EmExamInfo> examList);

    /**
     * type:1 2
     * 计算学生的进步度
     *
     * @param statList
     * @param referList
     */
    public void getDegreeStatList(List<EmStat> statList, List<EmStat> referList);
}
