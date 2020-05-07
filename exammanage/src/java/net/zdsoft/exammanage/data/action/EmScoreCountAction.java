package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmSpaceItem;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.entity.EmStatParm;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.exammanage.data.action
 * @ClassName: EmStatRankingService
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2018/8/20 10:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/20 10:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/exammanage/scoreRanking")
public class EmScoreCountAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmStatSpaceService emStatSpaceService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmSpaceItemService emSpaceItemService;
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ReportService reportService;

    @ControllerInfo("获取语数外+选考赋分 排名表(1)")
    @ResponseBody
    @RequestMapping("/queryStuExamList")
    public String queryGKList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        String json = reportService.getJsonByUnitIdAndExamId(unitId, examId, acadyear, semester, gradeCode);
        return json;
    }

    @ControllerInfo("获取语数外+选考赋分 排名表(2)")
    @ResponseBody
    @RequestMapping("/queryStuAllExamList")
    public String queryStuAllExamList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        String json = reportService.getJsonByUnitIdAndExamGradeRank(unitId, examId, acadyear, semester, gradeCode);
        return json;
    }

    @ControllerInfo("获取语数外+选考赋分 排名表(3)")
    @ResponseBody
    @RequestMapping("/queryStuSubExamList")
    public String queryStuSubExamList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String grade = request.getParameter("gradeCode");
        String subjectId = request.getParameter("subjectId");
        String subType = "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subjectId) && subjectId.contains(",")) {
            subType = subjectId.split(",")[1];
            subjectId = subjectId.split(",")[0];
        }
        String json = reportService.getJsonByUnitIdAndExamSubjectRank(unitId, examId, acadyear, semester, grade, subjectId, subType);
        return json;
    }

    @ControllerInfo("获取教学班内某科目成绩排名报表(4)")
    @RequestMapping("/getClassScoreRank")
    @ResponseBody
    public String getClassScoreRank(String unitId, String acadyear, String semester, String examId,
                                    String gradeCode, String subjectId, String classId, String subType) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subjectId) && subjectId.contains(",")) {
            subType = subjectId.split(",")[1];
            subjectId = subjectId.split(",")[0];
        }
        return reportService.getClassScoreRank(unitId, acadyear, semester, examId, gradeCode, subjectId, classId, subType);
    }

    @ControllerInfo("获取各教学班平均分排名表(5)")
    @RequestMapping("/getClassSubRank")
    @ResponseBody
    public String getClassSubRank(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subjectId) && subjectId.contains(",")) {
            subType = subjectId.split(",")[1];
            subjectId = subjectId.split(",")[0];
        }
        return reportService.getClassSubRank(unitId, acadyear, semester, examId, gradeCode, subjectId, subType);
    }

    @ControllerInfo("人数占比对比表   (6)")
    @RequestMapping("/getClassScore")
    @ResponseBody
    public String getClassScore(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subjectId) && subjectId.contains(",")) {
            subType = subjectId.split(",")[1];
            subjectId = subjectId.split(",")[0];
        }
        return reportService.getClassScore(unitId, acadyear, semester, examId, gradeCode, subjectId, subType);
    }

    @ControllerInfo("获取赋分等级对比表(7)")
    @RequestMapping("/getIsConScore")
    @ResponseBody
    public String getIsConScore(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subjectId) && subjectId.contains(",")) {
            subType = subjectId.split(",")[1];
            subjectId = subjectId.split(",")[0];
        }
        return reportService.getIsConScore(unitId, acadyear, semester, examId, gradeCode, subjectId, subType);
    }

    @ControllerInfo("获取历次考试对比表 (8)")
    @RequestMapping("/getAllExamSubject")
    @ResponseBody
    public String getAllExamSubject(String unitId, String acadyear, String semester, String gradeCode, String subjectId, String subType, String classId) {
        return reportService.getAllExamSubject(unitId, acadyear, semester, gradeCode, subjectId, subType, classId);
    }

    @ControllerInfo("获取历次考试对比表 (9)")
    @RequestMapping("/getAllExamSubjectClassId")
    @ResponseBody
    public String getAllExamSubjectClassId(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String classId = request.getParameter("classId");
        String json = reportService.getJsonByUnitIdAndExamIdAndClassId(unitId, examId, acadyear, semester, classId);
        return json;
    }

    @ControllerInfo("获取某考试下所有科目的排名表(10)")
    @RequestMapping("/getExamAllSubject")
    @ResponseBody
    public String getExamAllSubject(String unitId, String acadyear, String semester, String gradeCode, String examId, String classId) {
        return reportService.getExamAllSubject(unitId, acadyear, semester, gradeCode, examId, classId);
    }

    @ControllerInfo("同一考试各行政班排名 11,12")
    @RequestMapping("/getScoreRankingByClass")
    @ResponseBody
    public String getScoreRankingByClass(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam, ModelMap map) {
        return reportService.getScoreRankingByClass(unitId, acadyear, semester, gradeCode, examId, subjectParam);
    }

    @ControllerInfo("获取某个分数段各个行政部的占比人数 13,14")
    @RequestMapping("/getScoreDistributeByClass")
    @ResponseBody
    public String getScoreDistributeByClass(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam, ModelMap map) {
        return reportService.getScoreDistributeByClass(unitId, acadyear, semester, gradeCode, examId, subjectParam);
    }

    @ControllerInfo("获取某个行政班历次考试的对比表 15,16")
    @RequestMapping("/getScoreCountByClass")
    @ResponseBody
    public String getScoreCountByClassId(String unitId, String acadyear, String semester, String classId, String subjectParam, ModelMap map) {
        return reportService.getScoreCountByClassId(unitId, acadyear, semester, classId, subjectParam);
    }

    @ControllerInfo("获取某个学生某次考试的各科目的详情 17")
    @RequestMapping("/getScoreDetailByStudentId")
    @ResponseBody
    public String getScoreDetailByStudentId(String unitId, String acadyear, String semester, String classId, String examId, String studentId, ModelMap map) {
        return reportService.getScoreDetailByStudentId(unitId, acadyear, semester, classId, examId, studentId);
    }

    @ControllerInfo("学生成绩")
    @RequestMapping("/getAllScoreByStudentId")
    @ResponseBody
    public String getAllScoreByStudentId(String studentId, String unitId, String examId) {
        return reportService.getAllScoreByStudentId(studentId, unitId, examId);
    }

    @ControllerInfo("学生历次考试成绩排名")
    @RequestMapping("/getStuAllExamScore")
    @ResponseBody
    public String getStuAllExamScore(String unitId, String acadyear, String semester, String studentId) {
        return reportService.getStuAllExamScore(unitId, acadyear, semester, studentId);
    }

    @ControllerInfo("学生历次考试科目排名")
    @RequestMapping("/getStuAllExamRank")
    @ResponseBody
    public String getStuAllExamRank(String unitId, String acadyear, String semester, String studentId) {
        return reportService.getStuAllExamRank(unitId, acadyear, semester, studentId);
    }


    @RequestMapping("/index")
    @ResponseBody
    public String scoreRankingByClassIndex() {
        ModelMap map = new ModelMap();
        Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        map.put("semester", sem);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (null != sem) {
            List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadyear(getLoginInfo().getUnitId(), sem.getAcadyear(), String.valueOf(sem.getSemester()));
            map.put("acadyearList", acadyearList);
        } else {
            return errorFtl(map, "当前时间不在学年学期内，无法维护！");
        }
        String json = JSONObject.toJSONString(map);
        return json;
    }

    @RequestMapping("/examList")
    @ResponseBody
    public String examNameList(String unitId, String searchAcadyear, String searchSemester, String searchGradeCode, ModelMap map) {
        //根据年级id查询班级id
        //List<EmExamInfo> emExamInfoList = emExamInfoService.findByUnitIdAndAcadyear(getLoginInfo().getUnitId(),searchAcadyear,searchSemester);
        List<EmExamInfo> infoList = new ArrayList<EmExamInfo>();
        String searchGradeId = "";
        if (StringUtils.isNotBlank(searchGradeCode)) {
            String grades = gradeRemoteService.findByUnitIdAndGradeCode(unitId, searchGradeCode);
            List<Grade> gradeList = SUtils.dt(grades, new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                searchGradeId = gradeList.get(0).getId();
            }
        }
        if (StringUtils.isNotBlank(searchGradeId)) {
            List<Clazz> classIds = SUtils.dt(classRemoteService.findByGradeIdSortAll(searchGradeId), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classIds)) {
                Set<String> clsId = EntityUtils.getSet(classIds, "id");
                List<String> examList = emClassInfoService.findExamIdByClassIds(clsId.toArray(new String[]{}));
                infoList = emExamInfoService.findListByIdIn(examList.toArray(new String[]{}));
            }
        }
        map.put("examInfoList", infoList);
        String json = JSONObject.toJSONString(map);
        return json;
    }

    @RequestMapping("/classList")
    @ResponseBody
    public String examClassList(String unitId, String searchGradeCode, ModelMap map) {
        //根据年级id查询班级id
        //List<EmExamInfo> emExamInfoList = emExamInfoService.findByUnitIdAndAcadyear(getLoginInfo().getUnitId(),searchAcadyear,searchSemester);
        String searchGradeId = "";
        if (StringUtils.isNotBlank(searchGradeCode)) {
            String grades = gradeRemoteService.findByUnitIdAndGradeCode(unitId, searchGradeCode);
            List<Grade> gradeList = SUtils.dt(grades, new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                searchGradeId = gradeList.get(0).getId();
            }
        }
        if (StringUtils.isNotBlank(searchGradeId)) {
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), searchGradeId), new TR<List<Clazz>>() {
            });

            map.put("classList", clazzList);
        }
        String json = JSONObject.toJSONString(map);
        return json;
    }

    @ControllerInfo("获取分数段")
    @RequestMapping("/getEmSpaceItemList")
    @ResponseBody
    public String getEmSpaceItemList(String unitId, String examId, ModelMap map) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        List<EmStatParm> parmList = new ArrayList<EmStatParm>();
        if (emStatObject != null) {
            parmList = emStatParmService.findByStatObjectIdAndExamId(emStatObject.getId(), examId);
        }
        if (CollectionUtils.isNotEmpty(parmList)) {
            Set<String> parmIds = EntityUtils.getSet(parmList, "id");
            List<EmSpaceItem> spaceList = emSpaceItemService.findByStatParmIdIn(parmIds.toArray(new String[]{}));
            List<String> emSpaceNameList = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(spaceList)) {
                for (EmSpaceItem emSpaceItem : spaceList) {
                    emSpaceNameList.add(emSpaceItem.getName());
                }
            }
            map.put("emSpaceNameList", emSpaceNameList);
            map.put("emSpaceList", spaceList);
        }
        String json = JSONObject.toJSONString(map);
        return json;
    }
}
