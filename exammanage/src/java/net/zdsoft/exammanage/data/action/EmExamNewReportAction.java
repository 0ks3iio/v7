package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmLineStatDto;
import net.zdsoft.exammanage.data.dto.EmSubDto;
import net.zdsoft.exammanage.data.dto.ExamSubjectDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.exammanage.data.constant.ExammanageConstants.ZERO32;

@Controller
@RequestMapping("/examanalysis")
public class EmExamNewReportAction extends EmExamCommonAction {
    @Autowired
    TeachClassRemoteService teachClassRemoteService;
    @Autowired
    TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private EmAbilitySetService emAbilitySetService;
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmConversionService emConversionService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private EmStatLineService emStatLineService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;

    @RequestMapping("/examNewStudent/index/page")
    @ControllerInfo("学生成绩分析Index")
    public String examStudentIndex(HttpServletRequest request, ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        if (semester == null) {
            return errorFtl(map, "学年学期不存在");
        }
        LoginInfo loginInfo = getLoginInfo();
        //1代表学生
        if (loginInfo.getOwnerType() == User.OWNER_TYPE_STUDENT) {
            map.put("studentId", loginInfo.getOwnerId());
            map.put("isStu", true);
        }
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/examanalysis/examNewStudent/examNewStudentIndex.ftl";
    }

    @RequestMapping("/examNewStudent/radar/page")
    @ControllerInfo("学生成绩分析radar 雷达表")
    public String examStudentRadar(HttpServletRequest request, ModelMap map) {
        String examId = request.getParameter("examId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String studentId = request.getParameter("studentId");
        String unitId = getLoginInfo().getUnitId();
        List<EmExamInfo> examList = reportService.queryExamsByStudentId(studentId, acadyear, semester, null);
        if (CollectionUtils.isNotEmpty(examList)) {
            if (StringUtils.isBlank(examId)) {
                examId = examList.get(0).getId();
            }
            String jsonString = reportService.getScoreDetailByStudentId(unitId, acadyear, semester, null, examId, studentId);
            JSONObject json = new JSONObject();
            List<EmStat> statList = SUtils.dt(JSONObject.parseObject(jsonString).getString("infolist"), new TR<List<EmStat>>() {
            });
            String[] legendData = new String[]{"各学科分析"};
            JSONArray loadingData = new JSONArray();
            JSONArray polarIndicator = new JSONArray();
            JSONObject json1 = new JSONObject();
            json1.put("name", "各学科分析");
            int i = 0;
            if (CollectionUtils.isNotEmpty(statList)) {
                String[] values = new String[statList.size()];
                for (EmStat emStat : statList) {
                    JSONObject json2 = new JSONObject();
                    json2.put("text", emStat.getSubjectName());
                    json2.put("max", 100);
                    polarIndicator.add(json2);
                    BigDecimal b = new BigDecimal(emStat.getScoreT());
                    values[i] = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    i++;
                }
                json1.put("value", values);
            }
            loadingData.add(json1);
            json.put("legendData", legendData);
            json.put("loadingData", loadingData);
            json.put("polarIndicator", polarIndicator);
            map.put("statList", statList);
            map.put("jsonStringData", json.toString());
        }
        map.put("examList", examList);
        map.put("examId", examId);
        return "/examanalysis/examNewStudent/examNewStudentRadar.ftl";
    }

    @RequestMapping("/examNewStudent/List/page")
    @ControllerInfo("学生成绩分析List")
    public String examStudentList(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String studentId = request.getParameter("studentId");
        String conType = request.getParameter("conType");
        String showAll = request.getParameter("showAll");
        String jsonString = reportService.getAllExamStuDetailBySelf(getLoginInfo().getUnitId(), acadyear, semester,
                null, null, studentId, conType, showAll);
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            JSONArray array = json.getJSONArray("infolist");
            List<EmExamInfo> examList = new ArrayList<>();
            EmExamInfo examInfo = null;
            Map<String, EmStat> statMap = new HashMap<>();
            for (int i = 0; i < array.size(); i++) {
                examInfo = new EmExamInfo();
                JSONObject info = array.getJSONObject(i);
                String examId = info.getString("examId");
                examInfo.setId(examId);
                examInfo.setExamName(info.getString("examName"));
                examList.add(examInfo);
                List<EmStat> inStatList = SUtils.dt(info.getString("inStatList"), new TR<List<EmStat>>() {
                });
                if (CollectionUtils.isNotEmpty(inStatList)) {
                    for (EmStat emStat : inStatList) {
                        statMap.put(examId + emStat.getSubjectId() + emStat.getSubType(), emStat);
                    }
                }
            }
            List<ExamSubjectDto> subjectDtoList = SUtils.dt(json.getString("subjectDtoList"), new TR<List<ExamSubjectDto>>() {
            });
            //组装图表数据
            JSONObject json1 = new JSONObject();
            if (CollectionUtils.isNotEmpty(subjectDtoList)) {
                String[] legendData = new String[subjectDtoList.size()];
                String[] xAxisData = new String[examList.size()];
                String[][] loadingData = new String[subjectDtoList.size()][examList.size()];
                EmStat inStat = null;
                for (int j = 0; j < examList.size(); j++) {
                    xAxisData[j] = examList.get(j).getExamName();
                }
                for (int i = 0; i < subjectDtoList.size(); i++) {
                    legendData[i] = subjectDtoList.get(i).getSubjectName();
                    for (int j = 0; j < examList.size(); j++) {
                        inStat = statMap.get(examList.get(j).getId() + subjectDtoList.get(i).getSubjectId());
                        if (inStat != null) {
                            if ("1".equals(conType)) {
                                loadingData[i][j] = inStat.getScore() + "";
                            } else if ("3".equals(conType)) {
                                BigDecimal b = new BigDecimal(inStat.getScoreT());
                                loadingData[i][j] = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                            } else {
                                loadingData[i][j] = inStat.getSubjectScore();
                            }
                        }
                    }
                }
                json1.put("legendData", legendData);
                json1.put("xAxisData", xAxisData);
                json1.put("loadingData", loadingData);
            }
            map.put("statMap", statMap);
            map.put("examList", examList);
            map.put("type", request.getParameter("type"));
            map.put("conType", conType);
            map.put("subjectDtoList", subjectDtoList);
            map.put("jsonStringData", json1.toString());
        }
        return "/examanalysis/examNewStudent/examNewStudentList.ftl";
    }

    @RequestMapping("/examNewGrade/index/page")
    @ControllerInfo("年级成绩分析index")
    public String examNewGradeIndex(HttpServletRequest request, ModelMap map) {
        return "/examanalysis/examNewGrade/examNewGradeIndex.ftl";
    }

    @RequestMapping("/examNewGrade/tab/page")
    @ControllerInfo("年级成绩分析tab")
    public String examNewGradeTab(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        if (semester == null) {
            return errorFtl(map, "学年学期不存在");
        }
        LoginInfo loginInfo = getLoginInfo();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(loginInfo.getUnitId()), new TR<List<Grade>>() {
        });
        map.put("unitId", loginInfo.getUnitId());
        map.put("gradeList", gradeList);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("type", type);
        if ("1".equals(type) || "2".equals(type)) {
            return "/examanalysis/examNewGrade/examNewGradeTab.ftl";
        } else if ("3".equals(type)) {
            return "/examanalysis/examNewGrade/examNewGradeTab2.ftl";
        } else {
            return "/examanalysis/examNewGrade/examNewGradeTab3.ftl";
        }
    }

    @RequestMapping("/examNewGrade/subTotalList/page")
    @ControllerInfo("年级成绩分析totalList-学考情况总体概况")
    public String examNewGradeSubTotalList(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String gradeCode = request.getParameter("gradeCode");
        if (StringUtils.isBlank(gradeCode) || StringUtils.isBlank(examId)) {
            return "/examanalysis/examNewGrade/examNewGradeSubTotalList.ftl";
        }
        String unitId = getLoginInfo().getUnitId();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject != null) {
            List<EmStatRange> statRanageList = emStatRangeService.findByObjIdAndRangeId(emStatObject.getId(), examId, unitId, "2");
            Map<String, EmStatRange> statRanageMap = statRanageList.stream()
                    .collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
            List<Course> courses = reportService.getSubList(unitId, examId, "0");
            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
            Map<String, EmSubjectInfo> subInfoMap = subjectInfoList.stream()
                    .collect(Collectors.toMap(k -> k.getSubjectId(), Function.identity(), (k1, k2) -> k1));
            List<EmStatRange> lastList = new ArrayList<>();
            EmSubjectInfo subInfo = null;
            EmStatRange statRange = null;
            float conFullScore = 0.0f;
            List<EmConversion> conlist = emConversionService.findByUnitId(unitId);
            if (CollectionUtils.isNotEmpty(conlist)) {
                conFullScore = conlist.get(0).getEndScore();
            }
            Map<String, EmStatParm> parmMap = emStatParmService.findMapByUnitId(unitId, examId);
            float zeroFullScore = 0.0f;
            float sumFullScore = 0.0f;
            for (Course course : courses) {
                boolean isJoinSum = parmMap.containsKey(course.getId()) && !"0".equals(parmMap.get(course.getId()).getJoinSum());
                statRange = statRanageMap.get(course.getId() + "_" + course.getCourseTypeId());
                if (isJoinSum && "1".equals(course.getCourseTypeId())) {
                    sumFullScore += conFullScore;
                }
                if (statRange != null) {
                    subInfo = subInfoMap.get(course.getId());
                    if (subInfo != null) {
                        if (isJoinSum) {
                            if ("0".equals(course.getCourseTypeId())) {
                                sumFullScore += subInfo.getFullScore();
                            }
                            zeroFullScore += subInfo.getFullScore();
                        }
                        statRange.setFullScore(subInfo.getFullScore());
                    }
                    if ("1".equals(course.getCourseTypeId())) {
                        statRange.setSubjectName(course.getSubjectName() + "选考");
                    } else if ("2".equals(course.getCourseTypeId())) {
                        statRange.setSubjectName(course.getSubjectName() + "学考");
                    } else {
                        statRange.setSubjectName(course.getSubjectName());
                    }
                    lastList.add(statRange);
                }
            }
            statRange = statRanageMap.get(ExammanageConstants.CON_SUM_ID + "_0");
            if (statRange != null) {
                statRange.setSubjectName("非7选3考试分+选考赋分总分");
                statRange.setFullScore(sumFullScore);
                lastList.add(statRange);
            }
            statRange = statRanageMap.get(ExammanageConstants.ZERO32 + "_0");
            if (statRange != null) {
                statRange.setSubjectName("总分");
                statRange.setFullScore(zeroFullScore);
                lastList.add(statRange);
            }
            map.put("lastList", lastList);
        }
        StringBuffer s = new StringBuffer(acadyear + "学年");
        if ("1".equals(semester)) {
            s.append("第一学期");
        } else
            s.append("第二学期");
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        s.append(examInfo.getExamName());
        s.append("学科情况总体概况");
        map.put("title", s.toString());
        return "/examanalysis/examNewGrade/examNewGradeSubTotalList.ftl";
    }

    @RequestMapping("/examNewGrade/proList/page")
    @ControllerInfo("年级成绩分析proList-进步度分")
    public String examNewGradeProList(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String referExamId = request.getParameter("referExamId");
        String gradeCode = request.getParameter("gradeCode");
        if (StringUtils.isBlank(referExamId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(subjectId) || StringUtils.isBlank(examId)) {
            return "/examanalysis/examNewGrade/examNewGradeProList.ftl";
        }
        String unitId = getLoginInfo().getUnitId();
        String[] ss = subjectId.split(",");
        List<EmStat> statList = null;
        List<EmStat> referList = null;
        String jsonString = null;
        String referJsonString = null;
        if (ss != null && ss.length > 1) {
            jsonString = reportService.getClassScoreRankBySelf(unitId, acadyear, semester, examId, gradeCode, ss[0], null, ss[1]);
            referJsonString = reportService.getClassScoreRankBySelf(unitId, acadyear, semester, referExamId, gradeCode, ss[0], null, ss[1]);
            if (StringUtils.isNotBlank(jsonString)) {
                statList = SUtils.dt(JSONObject.parseObject(jsonString).getString("statList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(referJsonString)) {
                referList = SUtils.dt(JSONObject.parseObject(referJsonString).getString("statList"), new TR<List<EmStat>>() {
                });
            }
        } else {
            if (ExammanageConstants.ZERO32.equals(ss[0])) {
                jsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, examId, "1");
                referJsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, referExamId, "1");
            } else if (ExammanageConstants.CON_SUM_ID.equals(ss[0])) {
                jsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, examId, "2");
                referJsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, referExamId, "2");
            }
            if (StringUtils.isNotBlank(jsonString)) {
                statList = SUtils.dt(JSONObject.parseObject(jsonString).getString("totalList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(referJsonString)) {
                referList = SUtils.dt(JSONObject.parseObject(referJsonString).getString("totalList"), new TR<List<EmStat>>() {
                });
            }
        }
        if (CollectionUtils.isEmpty(statList)) {
            map.put("examPro", "本次考试暂无数据");
            return "/examanalysis/examNewGrade/examNewGradeProList.ftl";
        } else if (CollectionUtils.isEmpty(referList)) {
            map.put("examPro", "参照考试暂无数据");
            return "/examanalysis/examNewGrade/examNewGradeProList.ftl";
        }
        getDegreeStatList(statList, referList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", "进步度/本次考试标准分T(年级)象限图");
        jsonObject.put("xName", "标准分T");
        jsonObject.put("yName", "进步度");
        jsonObject.put("legendData", new String[]{"本次考试"});
        JSONArray jsonArr = new JSONArray();
        JSONArray jsonArr2 = new JSONArray();
        String[][] strings = new String[statList.size()][2];
        for (int i = 0; i < statList.size(); i++) {
            BigDecimal b = new BigDecimal(statList.get(i).getScoreT());
            BigDecimal b1 = new BigDecimal(statList.get(i).getProgressDegree());
            strings[i] = new String[]{b.setScale(1, BigDecimal.ROUND_HALF_UP).toString(), b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString()};
        }
        jsonArr2.add(strings);
        jsonArr.add(jsonArr2);
        jsonObject.put("loadingData", jsonArr);
        map.put("jsonStringData", jsonObject.toString());
        jsonObject = new JSONObject();
        jsonObject.put("text", "进步度/参照考试标准分T(年级)象限图");
        jsonObject.put("xName", "标准分T");
        jsonObject.put("yName", "进步度");
        jsonObject.put("legendData", new String[]{"参照考试"});
        jsonArr = new JSONArray();
        jsonArr2 = new JSONArray();
        strings = new String[referList.size()][2];
        Map<String, Float> proDegreeMap = statList.stream().collect(Collectors.toMap(EmStat::getStudentId, EmStat::getProgressDegree, (k1, k2) -> k1));
        EmStat stat = null;
        for (int i = 0; i < referList.size(); i++) {
            stat = referList.get(i);
            BigDecimal b = new BigDecimal(stat.getScoreT());
            BigDecimal b1 = new BigDecimal(0f);
            if (proDegreeMap.containsKey(stat.getStudentId())) {
                b1 = new BigDecimal(proDegreeMap.get(stat.getStudentId()));
            }
            strings[i] = new String[]{b.setScale(1, BigDecimal.ROUND_HALF_UP).toString(), b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString()};
        }
        jsonArr2.add(strings);
        jsonArr.add(jsonArr2);
        jsonObject.put("loadingData", jsonArr);
        map.put("jsonStringData1", jsonObject.toString());
        map.put("statList", statList);
        return "/examanalysis/examNewGrade/examNewGradeProList.ftl";
    }

    public void getDegreeStatList(List<EmStat> statList, List<EmStat> referList) {
        Map<String, List<EmStat>> statListMap = new HashMap<>();
        Map<String, EmStat> referMap = referList.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
        for (EmStat emStat : statList) {
            EmStat referStat = referMap.get(emStat.getStudentId());
            if (referStat != null) {
                emStat.setCompareExamScore(referStat.getScore());
                emStat.setCompareExamRank(referStat.getClassRank());
                emStat.setCompareExamScoreT(referStat.getScoreT());
                if (emStat.getScoreT() == null)
                    emStat.setScoreT(0.0f);
                if (referStat.getScoreT() == null)
                    referStat.setScoreT(0.0f);
                emStat.setProgressDegree(emStat.getScoreT() - referStat.getScoreT());
            } else {
                emStat.setProgressDegree(0.0f);
            }
            if (!statListMap.containsKey(emStat.getClassId())) {
                statListMap.put(emStat.getClassId(), new ArrayList<>());
            }
            statListMap.get(emStat.getClassId()).add(emStat);
        }
        Map<String, Integer> stuGraMap = new HashMap<>();
        getProRank(statList, stuGraMap);
        Map<String, Integer> stuClaMap = new HashMap<>();
        for (Entry<String, List<EmStat>> entry : statListMap.entrySet()) {
            getProRank(entry.getValue(), stuClaMap);
        }
        for (EmStat emStat : statList) {
            emStat.setProgressDegreeRankClass(stuClaMap.get(emStat.getStudentId()));
            emStat.setProgressDegreeRankGrade(stuGraMap.get(emStat.getStudentId()));
        }
    }

    public void getProRank(List<EmStat> statList, Map<String, Integer> stuMap) {
        Collections.sort(statList, (o1, o2) -> {
            return o2.getProgressDegree().compareTo(o1.getProgressDegree());
        });
        Float score = 0.0f;
        int i = 1;
        int j = 1;
        BigDecimal b = null;
        for (EmStat emStat : statList) {
            b = new BigDecimal(emStat.getProgressDegree());
            Float nowProScore = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if (i == 1) {
                score = nowProScore;
            }
            if (score > nowProScore) {
                stuMap.put(emStat.getStudentId(), i);
                j = i;
                score = nowProScore;
            } else {
                stuMap.put(emStat.getStudentId(), j);
            }
            i++;
        }
    }

    @RequestMapping("/examNewGrade/abilityList/page")
    @ControllerInfo("年级成绩分析abilityList-综合能力分")
    public String examNewGradeAbilityList(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String gradeCode = request.getParameter("gradeCode");
        String unitId = getLoginInfo().getUnitId();
        if (StringUtils.isBlank(examId) || StringUtils.isBlank(gradeCode)) {
            return "/examanalysis/examNewGrade/examNewGradeAbilityList.ftl";
        }
        String jsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, examId, "3");
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            List<ExamSubjectDto> subjectDtoList = SUtils.dt(json.getString("subjectDtoList"), new TR<List<ExamSubjectDto>>() {
            });
            List<EmStat> statList = SUtils.dt(json.getString("totalList"), new TR<List<EmStat>>() {
            });
            Map<String, EmStat> emStatMap = SUtils.dt(json.getString("emStatMap"), new TR<Map<String, EmStat>>() {
            });
            map.put("subjectDtoList", subjectDtoList);
            map.put("title", json.getString("title"));
            map.put("summary", json.getString("summary"));
            map.put("statList", statList);
            map.put("emStatMap", emStatMap);
        }
        return "/examanalysis/examNewGrade/examNewGradeAbilityList.ftl";
    }

    @ResponseBody
    @RequestMapping("/examNewGrade/abiEdit/page")
    @ControllerInfo("年级成绩分析abiEdit-更改科目权重")
    public String examNewGradeAbiEdit(HttpServletRequest request, ModelMap map) {
        String examId = request.getParameter("examId");
        String unitId = getLoginInfo().getUnitId();
        JSONObject json = new JSONObject();
        List<Course> courses1 = reportService.getSubList(unitId, examId, "0");
        List<Course> courses = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(courses1)) {
            Map<String, String> map1 = new HashMap<>();
            for (Course course : courses1) {
                if (!map1.containsKey(course.getId())) {
                    courses.add(course);
                    map1.put(course.getId(), "one");
                }
            }
        }
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject != null) {
            List<EmAbilitySet> abiList = emAbilitySetService.findListByObjAndExamId(emStatObject.getId(), examId);
            Map<String, EmAbilitySet> abiMap = abiList.stream().collect(Collectors.toMap(EmAbilitySet::getSubjectId, Function.identity()));
            json.put("courses", courses);
            json.put("abiMap", abiMap);
            json.put("statObjectId", emStatObject.getId());
        }
        json.put("examId", examId);
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/examNewGrade/abiSave")
    @ControllerInfo("年级成绩分析abiSave-保存科目权重")
    public String examNewGradeAbiSave(HttpServletRequest request, EmSubDto subDto, ModelMap map) {
        try {
            List<EmAbilitySet> abiList = subDto.getAbiList();
            String examId = request.getParameter("examId");
            String statObjectId = request.getParameter("statObjectId");
            if (CollectionUtils.isNotEmpty(abiList)) {
                emAbilitySetService.deleteByObjAndExamId(statObjectId, examId);
                for (EmAbilitySet abi : abiList) {
                    abi.setId(UuidUtils.generateUuid());
                    abi.setStatObjectId(statObjectId);
                    abi.setExamId(examId);
                }
                emAbilitySetService.saveAll(abiList.toArray(new EmAbilitySet[]{}));
            }
            Map<String, EmAbilitySet> abiMap = abiList.stream().collect(Collectors.toMap(EmAbilitySet::getSubjectId, Function.identity()));
            emStatService.saveAbiEmStatList(abiMap, examId, statObjectId, null);
        } catch (Exception e) {
            e.printStackTrace();
            return success("操作失败");
        }
        return success("操作成功");
    }

    @RequestMapping("/examNewGrade/List/page")
    @ControllerInfo("年级成绩分析List-考试分")
    public String examNewGradeList(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String gradeCode = request.getParameter("gradeCode");
        String subjectId = request.getParameter("subjectId");
        if (StringUtils.isBlank(examId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(subjectId)) {
            return "/examanalysis/examNewGrade/examNewGradeList1.ftl";
        }
        LoginInfo loginInfo = getLoginInfo();
        String[] ss = subjectId.split(",");
        if (ss != null && ss.length > 1) {
            String jsonString = reportService.getClassScoreRankBySelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], null, ss[1]);
            if (StringUtils.isNotBlank(jsonString)) {
                JSONObject json = JSONObject.parseObject(jsonString);
                List<EmStat> statList = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                });
                getAllRank(statList, examId, loginInfo.getUnitId());
                map.put("is73Sub", json.getString("is73Sub"));
                map.put("title", json.getString("title"));
                map.put("summary", json.getString("summary"));
                map.put("statList", statList);
            }
            return "/examanalysis/examNewGrade/examNewGradeList1.ftl";
        } else {
            String jsonString = "";
            if (ExammanageConstants.ZERO32.equals(ss[0])) {
                jsonString = reportService.getExamAllSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, examId, "1");
                map.put("tableType", "1");
            } else if (ExammanageConstants.CON_SUM_ID.equals(ss[0])) {
                jsonString = reportService.getExamAllSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, examId, "2");
                map.put("tableType", "2");
            }
            if (StringUtils.isNotBlank(jsonString)) {
                JSONObject json = JSONObject.parseObject(jsonString);
                List<ExamSubjectDto> subjectDtoList = SUtils.dt(json.getString("subjectDtoList"), new TR<List<ExamSubjectDto>>() {
                });
                List<EmStat> statList = SUtils.dt(json.getString("totalList"), new TR<List<EmStat>>() {
                });
                Map<String, EmStat> emStatMap = SUtils.dt(json.getString("emStatMap"), new TR<Map<String, EmStat>>() {
                });
                if (ExammanageConstants.ZERO32.equals(ss[0])) {
                    map.put("subjectDtoList", subjectDtoList);
                    map.put("tableType", "1");
                } else if (ExammanageConstants.CON_SUM_ID.equals(ss[0])) {
                    List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(loginInfo.getUnitId()), new TR<List<Course>>() {
                    });
                    Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
                    List<ExamSubjectDto> subjectLastList = new ArrayList<>();
                    int length = 0;
                    if (CollectionUtils.isNotEmpty(subjectDtoList)) {
                        boolean flag = false;
                        for (ExamSubjectDto subjectDto : subjectDtoList) {
                            if (StringUtils.equals(subjectDto.getSubType(), "1")) {
                                length += 4;
                            } else
                                length += 3;
                            if (!flag && id73Set.contains(subjectDto.getSubjectId().split(",")[0])) {
                                ExamSubjectDto ysyDto = new ExamSubjectDto();
                                ysyDto.setSubjectId(ExammanageConstants.CON_YSY_ID + ",0");
                                ysyDto.setSubType("0");
                                ysyDto.setSubjectName("非7选3科目总分");
                                subjectLastList.add(ysyDto);
                                flag = true;
                                length += 3;
                            }
                            subjectLastList.add(subjectDto);
                        }
                        ExamSubjectDto sumDto = new ExamSubjectDto();
                        sumDto.setSubjectId(ExammanageConstants.CON_SUM_ID + ",0");
                        sumDto.setSubjectName("总分（非7选3+选考赋分）");
                        sumDto.setSubType("0");
                        subjectLastList.add(sumDto);
                        length += 3;
                    }
                    map.put("length", length += 4);
                    map.put("subjectDtoList", subjectLastList);
                    map.put("tableType", "2");
                }
                map.put("title", json.getString("title"));
                map.put("summary", json.getString("summary"));
                map.put("statList", statList);
                map.put("emStatMap", emStatMap);
            }
            return "/examanalysis/examNewGrade/examNewGradeList2.ftl";
        }
    }

    @RequestMapping("/examNewClass/index/page")
    @ControllerInfo("班级成绩分析Index")
    public String examNewClassIndex(HttpServletRequest request, ModelMap map) {
        return "/examanalysis/examNewClass/examNewClassIndex.ftl";
    }

    @RequestMapping("/examNewClass/teachTab/page")
    @ControllerInfo("教学班级成绩分析tab")
    public String examNewTeachTab(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        if (semester == null) {
            return errorFtl(map, "学年学期不存在");
        }
        LoginInfo loginInfo = getLoginInfo();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(loginInfo.getUnitId()), new TR<List<Grade>>() {
        });
        map.put("unitId", loginInfo.getUnitId());
        map.put("gradeList", gradeList);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("type", type);
        if ("5".equals(type)) {
            return "/examanalysis/examNewClass/examNewTeachTab2.ftl";
        } else if ("10".equals(type) || "12".equals(type)) {
            return "/examanalysis/examNewClass/examNewTeachTab3.ftl";
        } else if ("13".equals(type)) {
            return "/examanalysis/examNewClass/examNewTeachTab4.ftl";
        } else if ("14".equals(type)) {
            return "/examanalysis/examNewClass/examNewTeachTab5.ftl";
        }
        return "/examanalysis/examNewClass/examNewTeachTab.ftl";
    }

    @RequestMapping("/examNewClass/teachList/page")
    @ControllerInfo("教学班级成绩分析List")
    public String examNewTeachList(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String classId = request.getParameter("classId");
        String type = request.getParameter("type");
        String reportType = request.getParameter("reportType");
        LoginInfo loginInfo = getLoginInfo();
        String[] ss = subjectId.split(",");
        String jsonString = null;
        if ("1".equals(type)) {
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(subjectId) && StringUtils.isNotBlank(classId)) {
                jsonString = reportService.getClassScoreRankBySelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], classId, ss.length > 1 ? ss[1] : "0");
            }
            if (StringUtils.isNotBlank(jsonString)) {
                JSONObject json = JSONObject.parseObject(jsonString);
                List<EmStat> statList = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                });
                map.put("is73Sub", json.getString("is73Sub"));
                map.put("title", json.getString("title"));
                map.put("summary", json.getString("summary"));
                map.put("statList", statList);
            }
            return "/examanalysis/examNewClass/examNewTeachList1.ftl";
        } else if ("2".equals(type) || "3".equals(type) || "20".equals(type)) {//2:各班分数段占比分析  3:各班赋分等级占比分析  20：各班名次段占比分析
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(subjectId)) {
                if ("2".equals(type)) {
                    jsonString = reportService.getClassScore(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], ss.length > 1 ? ss[1] : "0");
                } else if ("20".equals(type)) {
                    jsonString = reportService.getClassRank(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], ss.length > 1 ? ss[1] : "0");
                } else if ("3".equals(type)) {
                    jsonString = reportService.getIsConScore(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], ss.length > 1 ? ss[1] : "0");
                }
            }
            if (StringUtils.isNotBlank(jsonString)) {
                JSONObject json = JSONObject.parseObject(jsonString);
                List<String> scoreItemList = SUtils.dt(json.getString("scoreItemList"), new TR<List<String>>() {
                });
                List<String> rangeNameList = SUtils.dt(json.getString("rangeNameList"), new TR<List<String>>() {
                });
                Map<String, String> valueMap = SUtils.dt(json.getString("valueMap"), new TR<Map<String, String>>() {
                });
                Map<String, Integer> scoreItemNumMap = new HashMap<>();
                List<String> rangeNameListLast = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(scoreItemList) && CollectionUtils.isNotEmpty(rangeNameList)) {
                    Collections.sort(rangeNameList);
                    for (String rangeName : rangeNameList) {
                        if (!"年级全体".equals(rangeName.trim())) {
                            rangeNameListLast.add(rangeName);
                        }
                    }
//					if(!"20".equals(type)){
//					rangeNameListLast.add("年级全体");
                    for (String scoreItem : scoreItemList) {
                        int num = 0;
                        num += Integer.parseInt(valueMap.get(scoreItem + "年级全体" + "num"));
                        scoreItemNumMap.put(scoreItem, num);
                    }
                    map.put("haveExsit", "true");
                }
                if ("20".equals(type)) {//名次段 升序排序名称
                    Collections.sort(scoreItemList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return Integer.valueOf(o1.split("-")[0]) - Integer.valueOf(o2.split("-")[0]);
                        }
                    });
                }
                EmStatHeadEntity head = SUtils.dc(json.getString("params"), EmStatHeadEntity.class);
                map.put("title", head.getTitle());
                map.put("scoreItemList", scoreItemList);
                map.put("rangeNameList", rangeNameListLast);
                map.put("valueMap", valueMap);
                map.put("scoreItemNumMap", scoreItemNumMap);
            }
            map.put("type", type);
            map.put("reportType", reportType);
            return "/examanalysis/examNewClass/examNewTeachList2.ftl";
        } else if ("4".equals(type)) {
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(subjectId)) {
                jsonString = reportService.getClassSubRankBySelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], ss.length > 1 ? ss[1] : "0");
            }
            if (StringUtils.isNotBlank(jsonString)) {
                JSONObject json = JSONObject.parseObject(jsonString);
                List<EmStatRange> statArrangeList = SUtils.dt(json.getString("statArrangeList"), new TR<List<EmStatRange>>() {
                });
                EmStatHeadEntity head = SUtils.dc(json.getString("params"), EmStatHeadEntity.class);
                map.put("title", head.getTitle());
                map.put("is73Sub", json.getString("is73Sub"));
                map.put("statArrangeList", statArrangeList);
            }
            map.put("reportType", reportType);
            return "/examanalysis/examNewClass/examNewTeachList3.ftl";
        } else if ("5".equals(type)) {
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(subjectId) && StringUtils.isNotBlank(classId)) {
                String subType = request.getParameter("subType");
                jsonString = reportService.getAllExamSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, subjectId, subType, classId);
            }
            if (StringUtils.isNotBlank(jsonString)) {
                JSONObject json = JSONObject.parseObject(jsonString);
                List<EmStatRange> statArrangeList = SUtils.dt(json.getString("statArrangeList"), new TR<List<EmStatRange>>() {
                });
                EmStatHeadEntity head = SUtils.dc(json.getString("params"), EmStatHeadEntity.class);
                map.put("title", head.getTitle());
                map.put("is73Sub", json.getString("is73Sub"));
                map.put("statArrangeList", statArrangeList);
            }
            map.put("reportType", reportType);
            return "/examanalysis/examNewClass/examNewTeachList4.ftl";
        } else if ("10".equals(type) || "11".equals(type)) {
            String unitId = loginInfo.getUnitId();
            String referExamIds = request.getParameter("referExamIds");
            Set<String> referIds = new HashSet<>();
            if (StringUtils.isBlank(subjectId)) {
                return "/examanalysis/examNewClass/examNewTeachList10.ftl";
            }
            map.put("infoList", new ArrayList<>());
            if (StringUtils.isNotBlank(referExamIds)) {
                List<EmExamInfo> infoList = emExamInfoService.findListByIdIn(referExamIds.split(","));
                if (CollectionUtils.isNotEmpty(infoList)) {
                    for (EmExamInfo emExamInfo : infoList) {
                        referIds.add(emExamInfo.getId());
                    }
                    map.put("infoList", infoList);
                }
            }
            List<EmStatLine> statLines = emStatLineService.findByUnitIdAndExamId(unitId, examId, "0", ss[0], ss[1]);
            if (CollectionUtils.isEmpty(statLines)) {
                return "/examanalysis/examNewClass/examNewTeachList10.ftl";
            }
            String subjectName = "";
            EmExamInfo info = emExamInfoService.findOne(examId);
            if (StringUtils.equals(ss[0], ExammanageConstants.ZERO32)) {
                subjectName = "总分";
            } else if (StringUtils.equals(ss[0], ExammanageConstants.CON_SUM_ID)) {
                subjectName = "非7选3+选考赋分总分";
            } else {
                Course course = SUtils.dc(courseRemoteService.findOneById(ss[0]), Course.class);
                subjectName = course.getSubjectName();
                if ("1".equals(ss[1])) {
                    subjectName += "(选考)";
                } else if ("2".equals(ss[1])) {
                    subjectName += "(学考)";
                }
            }
            StringBuilder title = new StringBuilder();
            title.append(info.getExamName() + " ");
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                if (StringUtils.equals(ss[0], ExammanageConstants.ZERO32) || StringUtils.equals(ss[0], ExammanageConstants.CON_SUM_ID)) {
                    title.append(gradeList.get(0).getGradeName() + "年级  各行政班");
                } else {
                    title.append(gradeList.get(0).getGradeName() + "年级  各教学班");
                }
            }
            title.append(subjectName + " 考试分单上线对比表");
            map.put("title", title.toString());
            List<Clazz> clslist = getClsList(statLines);
            List<String> linelist = getLineList(statLines);
            map.put("clslist", clslist);
            map.put("linelist", linelist);
            Map<String, EmLineStatDto> dtoMap1 = getLineDtoMap1(statLines);
            map.put("dtoMap1", dtoMap1);
            Map<String, EmLineStatDto> dtoMap2 = new HashMap<>();
            if (referIds.size() > 0) {
                dtoMap2 = emStatLineService.findByDtoMap2(unitId, ss[0], ss[1], referIds, statLines);
            }
            map.put("dtoMap2", dtoMap2);
            return "/examanalysis/examNewClass/examNewTeachList10.ftl";
        } else if ("12".equals(type)) {
            String unitId = loginInfo.getUnitId();
            String referExamIds = request.getParameter("referExamIds");
            Set<String> referIds = new HashSet<>();
            if (StringUtils.isBlank(subjectId)) {
                return "/examanalysis/examNewClass/examNewTeachList12.ftl";
            }
            map.put("infoList", new ArrayList<>());
            if (StringUtils.isNotBlank(referExamIds)) {
                List<EmExamInfo> infoList = emExamInfoService.findListByIdIn(referExamIds.split(","));
                if (CollectionUtils.isNotEmpty(infoList)) {
                    for (EmExamInfo emExamInfo : infoList) {
                        referIds.add(emExamInfo.getId());
                    }
                    map.put("infoList", infoList);
                }
            }
            String sumId = request.getParameter("sumType");
            List<EmStatLine> statLines = emStatLineService.findByUnitIdAndExamId(unitId, examId, "1", ss[0], ss[1], sumId);
            if (CollectionUtils.isEmpty(statLines)) {
                return "/examanalysis/examNewClass/examNewTeachList12.ftl";
            }
            String subjectName = "";
            EmExamInfo info = emExamInfoService.findOne(examId);
            Course course = SUtils.dc(courseRemoteService.findOneById(ss[0]), Course.class);
            subjectName = course.getSubjectName();
            if ("1".equals(ss[1])) {
                subjectName += "(选考)";
            } else if ("2".equals(ss[1])) {
                subjectName += "(学考)";
            }
            StringBuilder title = new StringBuilder();
            title.append(info.getExamName() + " ");
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                if (StringUtils.equals(sumId, ExammanageConstants.ZERO32)) {
                    title.append(gradeList.get(0).getGradeName() + "年级各教学班  " + subjectName + "与总分的双上线对比表");
                } else {
                    title.append(gradeList.get(0).getGradeName() + "年级各教学班  " + subjectName + "与非7选3+选考赋分总分的双上线对比表");
                }
            }
            map.put("title", title.toString());
            List<Clazz> clslist = getClsList(statLines);
            List<String> linelist = getLineList(statLines);
            map.put("clslist", clslist);
            map.put("linelist", linelist);
            Map<String, EmLineStatDto> dtoMap1 = getLineDtoMap1(statLines);
            map.put("dtoMap1", dtoMap1);
            Map<String, EmLineStatDto> dtoMap2 = new HashMap<>();
            if (referIds.size() > 0) {
                dtoMap2 = emStatLineService.findByDoubleDtoMap2(unitId, ss[0], ss[1], sumId, referIds, statLines);
            }
            map.put("dtoMap2", dtoMap2);
            return "/examanalysis/examNewClass/examNewTeachList12.ftl";
        }
        return "/examanalysis/examNewClass/examNewTeachList1.ftl";
    }

    @RequestMapping("/examNewClass/getExamNewTeachList13/page")
    @ControllerInfo("学生进步度排名分析")
    public String getExamNewTeachList13(HttpServletRequest request, ModelMap map) {
        String examId = request.getParameter("examId");
        String compareExamId = request.getParameter("compareExamId");
        String subjectId = request.getParameter("subjectId");
        String classId = request.getParameter("classId");
        String gradeCode = request.getParameter("gradeCode");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String reportType = request.getParameter("reportType");
        String[] ss = subjectId.split(",");
        String unitId = getLoginInfo().getUnitId();
        String subjectName = "";
        String className = "";
        String jsonStringClass1 = null;
        String jsonStringClass2 = null;
        String jsonStringGrade1 = null;
        String jsonStringGrade2 = null;
        List<EmStat> statListClass1 = new ArrayList<>();
        List<EmStat> statListClass2 = new ArrayList<>();
        List<EmStat> statListGrade1 = new ArrayList<>();
        List<EmStat> statListGrade2 = new ArrayList<>();
        if (StringUtils.isNotBlank(classId)) {
            List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(new String[]{classId}), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(clsList)) {
                className = clsList.get(0).getClassNameDynamic();
            } else {
                List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findListByIds(new String[]{classId}), new TR<List<TeachClass>>() {
                });
                if (CollectionUtils.isNotEmpty(teaClsList)) {
                    className = teaClsList.get(0).getName();
                }
            }
        }
        StringBuilder title = new StringBuilder();
        if (StringUtils.isNotBlank(examId)) {
            EmExamInfo info = emExamInfoService.findOne(examId);
            if (info != null) {
                map.put("examName", info.getExamName());
            }
            if (StringUtils.isNotBlank(compareExamId)) {
                EmExamInfo info1 = emExamInfoService.findOne(compareExamId);
                if (info1 != null) {
                    map.put("compareExamName", info1.getExamName());
                }
            }
            title.append(info.getExamName() + " ");
        }
        if (StringUtils.isNotBlank(subjectId)) {
            Course course = SUtils.dc(courseRemoteService.findOneById(ss[0]), Course.class);
            subjectName = course.getSubjectName();
            if ("1".equals(ss[1])) {
                subjectName += "(选考)";
            } else if ("2".equals(ss[1])) {
                subjectName += "(学考)";
            }
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                title.append(className + " " + subjectName + "进步度排名");
            }
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(classId)) {
                jsonStringClass1 = reportService.getClassScoreRankBySelf(getLoginInfo().getUnitId(), acadyear, semester, examId, gradeCode, ss[0], classId, ss.length > 1 ? ss[1] : "0");
                if (StringUtils.isNotBlank(compareExamId)) {
                    jsonStringClass2 = reportService.getClassScoreRankBySelf(getLoginInfo().getUnitId(), acadyear, semester, compareExamId, gradeCode, ss[0], classId, ss.length > 1 ? ss[1] : "0");
                }
            }
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId)) {
                jsonStringGrade1 = reportService.getClassScoreRankBySelf(getLoginInfo().getUnitId(), acadyear, semester, examId, gradeCode, ss[0], "", ss.length > 1 ? ss[1] : "0");
                if (StringUtils.isNotBlank(compareExamId)) {
                    jsonStringGrade2 = reportService.getClassScoreRankBySelf(getLoginInfo().getUnitId(), acadyear, semester, compareExamId, gradeCode, ss[0], "", ss.length > 1 ? ss[1] : "0");
                }
            }
            if (StringUtils.isNotBlank(jsonStringClass1)) {
                JSONObject json = JSONObject.parseObject(jsonStringClass1);
                statListClass1 = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(jsonStringClass2)) {
                JSONObject json = JSONObject.parseObject(jsonStringClass2);
                statListClass2 = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(jsonStringGrade1)) {
                JSONObject json = JSONObject.parseObject(jsonStringGrade1);
                statListGrade1 = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(jsonStringGrade2)) {
                JSONObject json = JSONObject.parseObject(jsonStringGrade2);
                statListGrade2 = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                });
            }
            setEmstatInfo(statListClass1, getLoginInfo().getUnitId(), examId, statListClass2, statListGrade1, statListGrade2);
        }
        ;
        map.put("jsonStringData1", "");
        if (CollectionUtils.isNotEmpty(statListClass2) && CollectionUtils.isNotEmpty(statListClass1)) {
            Map<String, EmStat> emStatMap = statListClass1.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", "进步度/本次考试标准分T(年级)象限图");
            jsonObject.put("xName", "标准分T");
            jsonObject.put("yName", "进步度");
            jsonObject.put("legendData", new String[]{"本次考试"});
            JSONArray jsonArr = new JSONArray();
            JSONArray jsonArr3 = new JSONArray();
            JSONArray jsonArray4 = new JSONArray();
            JSONArray jsonArray5 = new JSONArray();
            String[][] strings = new String[statListClass1.size()][2];
            String[][] strings1 = new String[statListClass2.size()][2];
            for (int i = 0; i < statListClass1.size(); i++) {
                EmStat emStat = statListClass1.get(i);
                if (emStat != null) {
                    BigDecimal b = new BigDecimal(emStat.getProgressDegree());
                    String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    strings[i] = new String[]{statListClass1.get(i).getScoreT().toString(), progressDegree};
                }
            }
            for (int i = 0; i < statListClass2.size(); i++) {
                EmStat emStat = emStatMap.get(statListClass2.get(i).getStudentId());
                if (emStat != null) {
                    BigDecimal b = new BigDecimal(emStat.getProgressDegree());
                    String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    strings1[i] = new String[]{statListClass2.get(i).getScoreT().toString(), progressDegree};
                }
            }
            jsonArray4.add(strings1);
            jsonArray5.add(jsonArray4);
            jsonArr3.add(strings);
            jsonArr.add(jsonArr3);
            jsonObject.put("loadingData", jsonArr);
            map.put("jsonStringData1", jsonObject.toString());
            jsonObject.put("text", "进步度/参照考试标准分T(年级)象限图");
            jsonObject.put("legendData", new String[]{"参照考试"});
            jsonObject.put("loadingData", jsonArray5);
            map.put("jsonStringData2", jsonObject.toString());
        }
        map.put("reportType", reportType);
        map.put("statList", statListClass1);
        map.put("title", title.toString());
        return "/examanalysis/examNewClass/examNewTeachList13.ftl";
    }

    @RequestMapping("/examNewClass/getExamNewTeachList14/page")
    @ControllerInfo("学生进步度排名分析")
    public String getExamNewTeachList14(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        String examId = request.getParameter("examId");
        String compareExamId = request.getParameter("compareExamId");
        String subjectId = request.getParameter("subjectId");
        String type = request.getParameter("type");
        String reportType = request.getParameter("reportType");
        LoginInfo loginInfo = getLoginInfo();
        String[] ss = subjectId.split(",");
        String jsonString = null;
        String jsonString1 = null;
        if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(subjectId)) {
            jsonString = reportService.getClassSubRankBySelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], ss.length > 1 ? ss[1] : "0");
        }
        if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(compareExamId) && StringUtils.isNotBlank(subjectId)) {
            jsonString1 = reportService.getClassSubRankBySelf(loginInfo.getUnitId(), acadyear, semester, compareExamId, gradeCode, ss[0], ss.length > 1 ? ss[1] : "0");
        }
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            List<EmStatRange> statArrangeList = SUtils.dt(json.getString("statArrangeList"), new TR<List<EmStatRange>>() {
            });
            Map<String, EmStatRange> emStatMap = new HashMap<>();
            if (StringUtils.isNotBlank(jsonString1)) {
                JSONObject json1 = JSONObject.parseObject(jsonString1);
                List<EmStatRange> statRangeListCompare = SUtils.dt(json1.getString("statArrangeList"), new TR<List<EmStatRange>>() {
                });
                emStatMap = statRangeListCompare.stream().collect(Collectors.toMap(EmStatRange::getRangeId, Function.identity()));
            }
            String subjectName = "";
            if (StringUtils.isNotBlank(subjectId)) {
                Course course = SUtils.dc(courseRemoteService.findOneById(ss[0]), Course.class);
                subjectName = course.getSubjectName();
                if ("1".equals(ss[1])) {
                    subjectName += "(选考)";
                } else if ("2".equals(ss[1])) {
                    subjectName += "(学考)";
                }
            }
            if (MapUtils.isNotEmpty(emStatMap)) {
                for (EmStatRange emStatRange : statArrangeList) {
                    emStatRange.setSubjectName(subjectName);
                    EmStatRange emStatRange1 = emStatMap.get(emStatRange.getRangeId());
                    if (emStatRange1 != null) {
                        emStatRange.setCompareExamAvgScoreT(emStatRange1.getAvgScoreT());
                        emStatRange.setProgressDegree(emStatRange.getAvgScoreT() - emStatRange1.getAvgScoreT());
                    }
                }
                statArrangeList.sort(new Comparator<EmStatRange>() {
                    @Override
                    public int compare(EmStatRange o1, EmStatRange o2) {
                        if (o1.getProgressDegree() != null && o2.getProgressDegree() != null) {
                            if (o2.getProgressDegree() > o1.getProgressDegree()) {
                                return 1;
                            } else if (o2.getProgressDegree() < o1.getProgressDegree()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } else {
                            return 0;
                        }
                    }
                });
                for (int i = 0; i < statArrangeList.size(); i++) {
                    if (statArrangeList.get(i).getAvgScoreT() != null) {
                        BigDecimal b1 = new BigDecimal(statArrangeList.get(i).getAvgScoreT());
                        statArrangeList.get(i).setAvgScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
                    }
                    statArrangeList.get(i).setProgressDegreeRank(i + 1);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", "进步度/本次考试标准分T(年级)象限图");
                jsonObject.put("xName", "标准分T");
                jsonObject.put("yName", "进步度");
                jsonObject.put("legendData", new String[]{"本次考试"});
                JSONArray jsonArr = new JSONArray();
                JSONArray jsonArr3 = new JSONArray();
                JSONArray jsonArray4 = new JSONArray();
                JSONArray jsonArray5 = new JSONArray();
                String[][] strings = new String[statArrangeList.size()][2];
                String[][] strings1 = new String[statArrangeList.size()][2];
                for (int i = 0; i < statArrangeList.size(); i++) {
                    EmStatRange emStatRange = statArrangeList.get(i);
                    if (emStatRange != null) {
                        BigDecimal b = new BigDecimal(emStatRange.getProgressDegree()==null?0.0f:emStatRange.getProgressDegree());
                        String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                        strings[i] = new String[]{emStatRange.getAvgScoreT() == null ? "0" : emStatRange.getAvgScoreT().toString(), progressDegree == null ? "0" : progressDegree};
                    }
                }
                for (int i = 0; i < statArrangeList.size(); i++) {
                    EmStatRange emStatRange = statArrangeList.get(i);
                    if (emStatRange != null) {
                        BigDecimal b = new BigDecimal(emStatRange.getProgressDegree()==null?0.0f:emStatRange.getProgressDegree());
                        String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                        strings1[i] = new String[]{emStatRange.getCompareExamAvgScoreT() == null ? "0" : emStatRange.getCompareExamAvgScoreT().toString(), progressDegree == null ? "0" : progressDegree};
                    }
                }
                jsonArray4.add(strings1);
                jsonArray5.add(jsonArray4);
                jsonArr3.add(strings);
                jsonArr.add(jsonArr3);
                jsonObject.put("loadingData", jsonArr);
                map.put("jsonStringData1", jsonObject.toString());
                jsonObject.put("text", "进步度/参照考试标准分T(年级)象限图");
                jsonObject.put("legendData", new String[]{"参照考试"});
                jsonObject.put("loadingData", jsonArray5);
                map.put("jsonStringData2", jsonObject.toString());
            }
            EmStatHeadEntity head = SUtils.dc(json.getString("params"), EmStatHeadEntity.class);
            map.put("title", head.getTitle());
            map.put("is73Sub", json.getString("is73Sub"));
            map.put("statArrangeList", statArrangeList);
        }
        map.put("reportType", reportType);
        return "/examanalysis/examNewClass/examNewTeachList14.ftl";
    }

    private Map<String, EmLineStatDto> getLineDtoMap1(List<EmStatLine> statLines) {
        Map<String, EmLineStatDto> dtoMap = new HashMap<>();
        EmLineStatDto dto = null;
        for (EmStatLine e : statLines) {
            dto = new EmLineStatDto();
            dto.setBlance(e.getBlance());
            dto.setRank(e.getRank());
            dto.setScoreNum(NumberUtils.toInt(e.getScoreNum()));
            dtoMap.put(e.getLine() + "_" + e.getClassId(), dto);
        }
        return dtoMap;
    }

    private List<String> getLineList(List<EmStatLine> statLines) {
        List<String> linelist = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (EmStatLine e : statLines) {
            if (set.contains(e.getLine())) {
                continue;
            }
            set.add(e.getLine());
            linelist.add(e.getLine());
        }
        Collections.sort(linelist, new Comparator<String>() {
            @Override
            public int compare(String arg0, String arg1) {
                return NumberUtils.toInt(arg0) - NumberUtils.toInt(arg1);
            }
        });
        return linelist;
    }

    private List<Clazz> getClsList(List<EmStatLine> statLines) {
        Collections.sort(statLines, new Comparator<EmStatLine>() {
            @Override
            public int compare(EmStatLine arg0, EmStatLine arg1) {
                int a = arg0.getClassType().compareTo(arg1.getClassType());
                if (a != 0) {
                    return a;
                } else {
                    return arg0.getClassName().compareTo(arg1.getClassName());
                }
            }
        });
        List<Clazz> clslist = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (EmStatLine e : statLines) {
            if (set.contains(e.getClassId())) {
                continue;
            }
            set.add(e.getClassId());
            Clazz c = new Clazz();
            c.setId(e.getClassId());
            c.setClassName(e.getClassName());
            clslist.add(c);
        }
        return clslist;
    }

    @RequestMapping("/examNewClass/tab/page")
    @ControllerInfo("行政班级成绩分析tab")
    public String examNewClassTab(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        map.put("type", type);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
        });
        List<Grade> gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
        map.put("gradeList", gradeList);
        map.put("unitId", getLoginInfo().getUnitId());
        if (StringUtils.equals(type, "11")) {
            return "/examanalysis/examNewClass/examNewClassTab2.ftl";
        } else if (StringUtils.equals(type, "15")) {
            return "/examanalysis/examNewClass/examNewClassTab3.ftl";
        } else if (StringUtils.equals(type, "16")) {
            return "/examanalysis/examNewClass/examNewClassTab4.ftl";
        }
        return "/examanalysis/examNewClass/examNewClassTab.ftl";
    }

    @RequestMapping("/examNewClass/showData4/page")
    @ControllerInfo("行政班级成绩分析报表-type4")
    public String showClassData4(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        String sumType = request.getParameter("sumType");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String classId = request.getParameter("classId");
        String showType = request.getParameter("showType");
        LoginInfo loginInfo = getLoginInfo();
        String jsonString = "";
        if ("0".equals(sumType)) {
            jsonString = reportService.getScoreCountByClassIdSelf(loginInfo.getUnitId(), acadyear, semester, classId, "0");
            map.put("tableType", "1");
        } else if ("9".equals(sumType)) {
            jsonString = reportService.getScoreCountByClassIdSelf(loginInfo.getUnitId(), acadyear, semester, classId, "1");
            map.put("tableType", "2");
        }
        List<EmStatRange> emStatRangeList = new ArrayList<>();
        List<EmExamInfo> emExamInfoList = emExamInfoService.findByUnitIdAndAcadyear(loginInfo.getUnitId(), acadyear, semester);
        if (emExamInfoList.size() > 0) {
            map.put("emExamInfoList", emExamInfoList);
        }
        if (StringUtils.isNotBlank(classId)) {
            map.put("classId", classId);
        }
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            emStatRangeList = SUtils.dt(json.getString("infolist"), new TR<List<EmStatRange>>() {
            });
            map.put("title", json.getString("title"));
            map.put("emStatRangeList", emStatRangeList);
        }
        map.put("type", type);
        map.put("showType", showType);
        if (StringUtils.equals(showType, "1") && CollectionUtils.isNotEmpty(emStatRangeList)) {
            JSONObject json1 = new JSONObject();
            String[] xAxisData = new String[emStatRangeList.size()];
            Float[][] loadingData = new Float[1][emStatRangeList.size()];
            Float[][] loadingData1 = new Float[1][emStatRangeList.size()];
            int i = 0;
            for (EmStatRange e : emStatRangeList) {
                xAxisData[i] = e.getExamName();
                loadingData[0][i] = e.getAvgScore();
                i++;
            }
            json1.put("legendData", new String[]{"总分平均分"});
            json1.put("xAxisData", xAxisData);
            json1.put("loadingData", loadingData);
            JSONObject json2 = new JSONObject();
            int j = 0;
            for (EmStatRange e : emStatRangeList) {
                loadingData1[0][j] = e.getAvgScoreT();
                j++;
            }
            json2.put("legendData", new String[]{"班级平均标准分T"});
            json2.put("xAxisData", xAxisData);
            json2.put("loadingData", loadingData1);
            map.put("jsonStringData2", json2.toString());
            map.put("jsonStringData1", json1.toString());
        } else {
            map.put("jsonStringData", "");
        }
        return "/examanalysis/examNewClass/showClassData4.ftl";
    }

    @RequestMapping("/examNewClass/showClassData5")
    @ControllerInfo("显示雷达图")
    public String showClassData5(HttpServletRequest request, ModelMap modelMap) {
        JSONArray jsonArr = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject json3 = new JSONObject();
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        List<EmSubjectInfo> emSubjectInfoList = emSubjectInfoService.findByExamId(examId);
        List<Course> list = reportService.getSubList(getLoginInfo().getUnitId(), examId, "0");
        Map<String, Course> courseMap = EntityUtils.getMap(list, Course::getId);
        List<Float> doubleList = new ArrayList<>();
        Double[] doubles = new Double[emSubjectInfoList.size()];
        int i = 0;
        if (CollectionUtils.isNotEmpty(emSubjectInfoList)) {
            for (EmSubjectInfo emSubjectInfo : emSubjectInfoList) {
                EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(getLoginInfo().getUnitId(), examId);
                if (emStatObject != null) {
                    List<EmStatRange> emStatRanges = emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeList(emStatObject.getId(), examId, emSubjectInfo.getSubjectId(), classId, "1");
                    String subjectName = "";
                    if (courseMap.containsKey(emSubjectInfo.getSubjectId())) {
                        subjectName = courseMap.get(emSubjectInfo.getSubjectId()).getSubjectName();
                    }
                    if (CollectionUtils.isNotEmpty(emStatRanges)) {
                        json3 = new JSONObject();
                        for (EmStatRange emStatRange : emStatRanges) {
                            JSONObject jsonObject1 = new JSONObject();
                            if (emStatRange.getSubType().equals("1")) {
                                jsonObject1.put("text", subjectName + "(选考)");
                                if (emStatRange.getAvgScoreT() != null) {
                                    doubleList.add(emStatRange.getAvgScoreT());
                                } else {
                                    doubleList.add(0f);
                                }
                                jsonObject1.put("max", emSubjectInfo.getFullScore());
                                jsonArr.add(jsonObject1);
                            } else if (emStatRange.getSubType().equals("2")) {
                                jsonObject1.put("text", subjectName + "(学考)");
                                if (emStatRange.getAvgScoreT() != null) {
                                    doubleList.add(emStatRange.getAvgScoreT());
                                } else {
                                    doubleList.add(0f);
                                }
                                jsonObject1.put("max", emSubjectInfo.getFullScore());
                                jsonArr.add(jsonObject1);
                            } else if (emStatRange.getSubType().equals("0")) {
                                jsonObject1.put("text", subjectName);
                                if (emStatRange.getAvgScoreT() != null) {
                                    doubleList.add(emStatRange.getAvgScoreT());
                                } else {
                                    doubleList.add(0f);
                                }
                                jsonObject1.put("max", emSubjectInfo.getFullScore());
                                jsonArr.add(jsonObject1);
                            }
                        }
                    } else {
                        json3 = new JSONObject();
                        if (courseMap.containsKey(emSubjectInfo.getSubjectId())) {
                            json3.put("text", courseMap.get(emSubjectInfo.getSubjectId()).getSubjectName());
                        }
                        json3.put("max", emSubjectInfo.getFullScore());
                        jsonArr.add(json3);
                        doubleList.add(0f);
                    }
                }
                i++;
            }
            json.put("polarIndicator", jsonArr);
            jsonArr = new JSONArray();
            json3 = new JSONObject();
            json3.put("name", "各科均衡分析");
            if (CollectionUtils.isNotEmpty(doubleList)) {
                json3.put("value", doubleList.toArray(new Float[0]));
                jsonArr.add(json3);
                json.put("loadingData", jsonArr);
                json.put("legendData", new String[]{"各科均衡分析"});
                modelMap.put("jsonStringData3", json.toString());
            } else {
                modelMap.put("jsonStringData3", "");
            }
        } else {
            modelMap.put("jsonStringData3", "");
        }
        return "/examanalysis/examNewClass/showClassData5.ftl";
    }

    @RequestMapping("/examNewClass/showClassData6/page")
    @ControllerInfo("显示行政班进步度排名")
    public String showClassData6(HttpServletRequest request, ModelMap map) {
        String examId = request.getParameter("examId");
        String compareExamId = request.getParameter("compareExamId");
        String classId = request.getParameter("classId");
        String gradeCode = request.getParameter("gradeCode");
        String reportType = request.getParameter("reportType");
        String unitId = getLoginInfo().getUnitId();
        String subjectName = "";
        String className = "";
        if (StringUtils.isNotBlank(classId)) {
            List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(new String[]{classId}), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(clsList)) {
                className = clsList.get(0).getClassNameDynamic();
            } else {
                List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findListByIds(new String[]{classId}), new TR<List<TeachClass>>() {
                });
                if (CollectionUtils.isNotEmpty(teaClsList)) {
                    className = teaClsList.get(0).getName();
                }
            }
        }
        StringBuilder title = new StringBuilder();
        if (StringUtils.isNotBlank(examId)) {
            EmExamInfo info = emExamInfoService.findOne(examId);
            if (info != null) {
                map.put("examName", info.getExamName());
            }
            if (StringUtils.isNotBlank(compareExamId)) {
                EmExamInfo info1 = emExamInfoService.findOne(compareExamId);
                if (info1 != null) {
                    map.put("compareExamName", info1.getExamName());
                }
            }
            title.append(info.getExamName() + " ");
        }
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
        });
        if (CollectionUtils.isNotEmpty(gradeList)) {
            title.append(className + " " + subjectName + "进步度排名");
        }
        List<EmStat> statListClass1 = new ArrayList<>();
        List<EmStat> statListClass2 = new ArrayList<>();
        List<EmStat> statListGrade1 = new ArrayList<>();
        List<EmStat> statListGrade2 = new ArrayList<>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmStatObject emStatObject1 = emStatObjectService.findByUnitIdExamId(unitId, compareExamId);
        if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(classId) && emStatObject != null) {
            statListClass1 = emStatService.findByExamIdAndObjectsAndSubjectIdAndClassId(examId, ZERO32, emStatObject.getId(), classId);
            if (StringUtils.isNotBlank(compareExamId) && emStatObject1 != null) {
                statListClass2 = emStatService.findByExamIdAndObjectsAndSubjectIdAndClassId(compareExamId, ZERO32, emStatObject1.getId(), classId);
            }
        }
        if (CollectionUtils.isNotEmpty(gradeList) && emStatObject != null) {
            statListGrade1 = emStatService.findByExamIdAndObjectsAndSubjectId(examId, ZERO32, emStatObject.getId());
            if (StringUtils.isNotBlank(compareExamId) && emStatObject1 != null) {
                statListGrade2 = emStatService.findByExamIdAndObjectsAndSubjectId(compareExamId, ZERO32, emStatObject1.getId());
            }
        }
        setEmstatInfo(statListClass1, getLoginInfo().getUnitId(), examId, statListClass2, statListGrade1, statListGrade2);
        map.put("jsonStringData1", "");
        if (CollectionUtils.isNotEmpty(statListClass2) && CollectionUtils.isNotEmpty(statListClass1)) {
            Map<String, EmStat> emStatMap = statListClass1.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", "进步度/本次考试标准分(年级)象限图");
            jsonObject.put("xName", "标准分T");
            jsonObject.put("yName", "进步度");
            jsonObject.put("legendData", new String[]{"本次考试"});
            JSONArray jsonArr = new JSONArray();
            JSONArray jsonArr3 = new JSONArray();
            JSONArray jsonArr4 = new JSONArray();
            JSONArray jsonArr5 = new JSONArray();
            String[][] strings = new String[statListClass1.size()][2];
            String[][] strings1 = new String[statListClass1.size()][2];
            for (int i = 0; i < statListClass1.size(); i++) {
                BigDecimal b = new BigDecimal(statListClass1.get(i).getProgressDegree());
                String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                strings[i] = new String[]{statListClass1.get(i).getScoreT().toString(), progressDegree};
            }
            for (int i = 0; i < statListClass2.size(); i++) {
                EmStat emStat = emStatMap.get(statListClass2.get(i).getStudentId());
                if (emStat != null) {
                    BigDecimal b = new BigDecimal(emStat.getProgressDegree());
                    String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    strings1[i] = new String[]{statListClass2.get(i).getScoreT().toString(), progressDegree};
                }
            }
            jsonArr4.add(strings1);
            jsonArr5.add(jsonArr4);
            jsonArr3.add(strings);
            jsonArr.add(jsonArr3);
            jsonObject.put("loadingData", jsonArr);
            map.put("jsonStringData1", jsonObject.toString());
            jsonObject.put("text", "进步度/参照考试标准分T(年级)象限图");
            jsonObject.put("legendData", new String[]{"参照考试"});
            jsonObject.put("loadingData", jsonArr5);
            map.put("jsonStringData2", jsonObject.toString());
        }
        map.put("reportType", reportType);
        map.put("statList", statListClass1);
        map.put("title", title.toString());
        return "/examanalysis/examNewClass/showClassData6.ftl";
    }

    @RequestMapping("/examNewClass/showClassData7/page")
    @ControllerInfo("学生进步度排名分析")
    public String showClassData7(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        String examId = request.getParameter("examId");
        String compareExamId = request.getParameter("compareExamId");
        String type = request.getParameter("type");
        String reportType = request.getParameter("reportType");
        LoginInfo loginInfo = getLoginInfo();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(getLoginInfo().getUnitId(), examId);
        EmStatObject emStatObject1 = emStatObjectService.findByUnitIdExamId(getLoginInfo().getUnitId(), compareExamId);
        map.put("jsonStringData1", "");
        List<EmStatRange> statArrangeList = new ArrayList<>();
        Map<String, EmStatRange> emStatMap = new HashMap<>();
        if (StringUtils.isNotBlank(examId) && emStatObject != null) {
            statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, ZERO32, EmStatRange.RANGE_CLASS, "0");
            if (StringUtils.isNotBlank(compareExamId) && emStatObject1 != null) {
                List<EmStatRange> statRangeListCompare = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject1.getId(), compareExamId, ZERO32, EmStatRange.RANGE_CLASS, "0");
                emStatMap = statRangeListCompare.stream().collect(Collectors.toMap(EmStatRange::getRangeId, Function.identity()));
            }
        }
        if (MapUtils.isNotEmpty(emStatMap) && CollectionUtils.isNotEmpty(statArrangeList)) {
            for (EmStatRange emStatRange : statArrangeList) {
                EmStatRange emStatRange1 = emStatMap.get(emStatRange.getRangeId());
                if (emStatRange1 != null) {
                    emStatRange.setCompareExamAvgScoreT(emStatRange1.getAvgScoreT());
                    emStatRange.setProgressDegree(emStatRange.getAvgScoreT() - emStatRange1.getAvgScoreT());
                }
            }
            statArrangeList.sort(new Comparator<EmStatRange>() {
                @Override
                public int compare(EmStatRange o1, EmStatRange o2) {
                    if (o1.getProgressDegree() != null && o2.getProgressDegree() != null) {
                        if (o2.getProgressDegree() > o1.getProgressDegree()) {
                            return 1;
                        } else if (o2.getProgressDegree() < o1.getProgressDegree()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            });
            for (int i = 0; i < statArrangeList.size(); i++) {
                statArrangeList.get(i).setProgressDegreeRank(i + 1);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", "进步度/本次考试标准分T(年级)象限图");
            jsonObject.put("xName", "标准分T");
            jsonObject.put("yName", "进步度");
            jsonObject.put("legendData", new String[]{"本次考试"});
            JSONArray jsonArr = new JSONArray();
            JSONArray jsonArr3 = new JSONArray();
            JSONArray jsonArr4 = new JSONArray();
            JSONArray jsonArr5 = new JSONArray();
            String[][] strings = new String[statArrangeList.size()][2];
            String[][] strings1 = new String[statArrangeList.size()][2];
            for (int i = 0; i < statArrangeList.size(); i++) {
                EmStatRange emStatRange = statArrangeList.get(i);
                if (emStatRange != null) {
                    BigDecimal b = new BigDecimal(emStatRange.getProgressDegree());
                    String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    strings[i] = new String[]{emStatRange.getAvgScoreT() == null ? "0" : emStatRange.getAvgScoreT().toString(), progressDegree == null ? "0" : progressDegree};
                }
            }
            for (int i = 0; i < statArrangeList.size(); i++) {
                EmStatRange emStatRange = statArrangeList.get(i);
                if (emStatRange != null) {
                    BigDecimal b = new BigDecimal(emStatRange.getProgressDegree());
                    String progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    strings1[i] = new String[]{emStatRange.getCompareExamAvgScoreT() == null ? "0" : emStatRange.getCompareExamAvgScoreT().toString(), progressDegree == null ? "0" : progressDegree};
                }
            }
            jsonArr4.add(strings1);
            jsonArr5.add(jsonArr4);
            jsonArr3.add(strings);
            jsonArr.add(jsonArr3);
            jsonObject.put("loadingData", jsonArr);
            map.put("jsonStringData1", jsonObject.toString());
            jsonObject.put("text", "进步度/参照考试标准分T(年级)象限图");
            jsonObject.put("legendData", new String[]{"参照考试"});
            jsonObject.put("loadingData", jsonArr5);
            map.put("jsonStringData2", jsonObject.toString());
        }
        setEmstatRangeInfo(statArrangeList, getLoginInfo().getUnitId(), examId, ZERO32);
        StringBuilder title = new StringBuilder();
        if (StringUtils.isNotBlank(examId)) {
            EmExamInfo info = emExamInfoService.findOne(examId);
            title.append(info.getExamName() + " ");
        }
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(getLoginInfo().getUnitId(), gradeCode), new TR<List<Grade>>() {
        });
        if (CollectionUtils.isNotEmpty(gradeList)) {
            title.append(gradeList.get(0).getGradeName() + "进步度排名");
        }
        map.put("title", title);
        map.put("statArrangeList", statArrangeList);
        map.put("reportType", reportType);
        return "/examanalysis/examNewClass/showClassData7.ftl";
    }

    public void setEmstatInfo(List<EmStat> statListClass1, String unitId, String examId, List<EmStat> statListClass2, List<EmStat> statListGrade1, List<EmStat> statListGrade2) {
        String[] stuIds = null;
        stuIds = statListClass1.stream().map(EmStat::getStudentId).collect(Collectors.toSet()).toArray(new String[]{});
        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds), new TR<List<Student>>() {
        });
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        Student stu = null;
        JSONObject json = new JSONObject();
        List<EmStat> lastList = new ArrayList<>();
        for (EmStat inStat : statListClass1) {
            stu = studentMap.get(inStat.getStudentId());
            if (stu != null) {
                inStat.setStudentName(stu.getStudentName());
                inStat.setExamNum(examNumMap.get(stu.getId()));
                inStat.setStudentCode(stu.getStudentCode());
            }
            lastList.add(inStat);
        }
        if (CollectionUtils.isNotEmpty(statListClass2) && CollectionUtils.isNotEmpty(statListClass1)) {
            Map<String, EmStat> emStatMap = statListClass2.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
            for (EmStat emStat : statListClass1) {
                if (emStatMap.containsKey(emStat.getStudentId())) {
                    EmStat stat = emStatMap.get(emStat.getStudentId());
                    emStat.setCompareExamScore(stat.getScore());
                    emStat.setCompareExamRank(stat.getGradeRank());
                    emStat.setCompareExamScoreT(stat.getScoreT());
                    if (emStat.getScoreT() != null && stat.getScoreT() != null) {
                        emStat.setProgressDegree(emStat.getScoreT() - stat.getScoreT());
                    }
                }
            }
            statListClass1.sort(new Comparator<EmStat>() {
                @Override
                public int compare(EmStat o1, EmStat o2) {
                    if (o2.getProgressDegree() != null && o1.getProgressDegree() != null) {
                        if (o2.getProgressDegree() > o1.getProgressDegree()) {
                            return 1;
                        } else if (o2.getProgressDegree() < o1.getProgressDegree()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                    return 0;
                }
            });
            for (int i = 0; i < statListClass1.size(); i++) {
                statListClass1.get(i).setProgressDegreeRankClass(i + 1);
            }
        }
        if (CollectionUtils.isNotEmpty(statListGrade2) && CollectionUtils.isNotEmpty(statListGrade1)) {
            Map<String, EmStat> emStatMap = statListGrade2.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
            for (EmStat emStat : statListGrade1) {
                if (emStatMap.containsKey(emStat.getStudentId())) {
                    EmStat stat = emStatMap.get(emStat.getStudentId());
                    emStat.setCompareExamScore(stat.getScore());
                    emStat.setCompareExamRank(stat.getGradeRank());
                    emStat.setCompareExamScoreT(stat.getScoreT());
                    ;
                    if (emStat.getScoreT() != null && stat.getScoreT() != null) {
                        emStat.setProgressDegree(emStat.getScoreT() - stat.getScoreT());
                    }
                }
            }
            statListGrade1.sort(new Comparator<EmStat>() {
                @Override
                public int compare(EmStat o1, EmStat o2) {
                    if (o2.getProgressDegree() > o1.getProgressDegree()) {
                        return 1;
                    } else if (o2.getProgressDegree() < o1.getProgressDegree()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            for (int i = 0; i < statListGrade1.size(); i++) {
                statListGrade1.get(i).setProgressDegreeRankGrade(i + 1);
            }
            Map<String, EmStat> emStatMapGrade = statListGrade1.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
            if (CollectionUtils.isNotEmpty(statListClass1)) {
                for (EmStat emStat : statListClass1) {
                    if (emStatMapGrade.containsKey(emStat.getStudentId())) {
                        EmStat emStat1 = emStatMapGrade.get(emStat.getStudentId());
                        if (emStat.getScoreT() != null) {
                            BigDecimal b1 = new BigDecimal(emStat.getScoreT());
                            emStat.setScoreT(Float.parseFloat(b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString()));
                        }
                        emStat.setProgressDegreeRankGrade(emStat1.getProgressDegreeRankGrade());
                    }
                }
            }
        }
    }

    void setEmstatRangeInfo(List<EmStatRange> statRangeList, String unitId, String examId, String subjectId) {
        Set<String> rangeIds = statRangeList.stream().map(EmStatRange::getRangeId).collect(Collectors.toSet());
        Map<String, Clazz> clazzMap = SUtils.dt(classRemoteService.findListByIds(rangeIds.toArray(new String[]{})), new TR<List<Clazz>>() {
        }).stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
        Map<String, TeachClass> teachClassMap = SUtils.dt(teachClassRemoteService.findListByIds(rangeIds.toArray(new String[]{})), new TR<List<TeachClass>>() {
        }).stream().collect(Collectors.toMap(TeachClass::getId, Function.identity()));
        Map<String, Teacher> teacherMap = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        }).stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
        Map<String, Set<String>> teacherIdsMap = emSubjectInfoService.findTeacher(examId, subjectId, unitId, rangeIds, rangeIds);
        EmStatRange statRange = null;
        for (int i = 0; i < statRangeList.size(); i++) {
            statRange = statRangeList.get(i);
            String rangeName = "";
            String teacherId = "";
            if (EmStatRange.RANGE_SCHOOL.equals(statRange.getRangeType())) {
                rangeName = "年级全体";
            } else if (clazzMap.containsKey(statRange.getRangeId())) {
                rangeName = clazzMap.get(statRange.getRangeId()).getClassNameDynamic();
                teacherId = clazzMap.get(statRange.getRangeId()).getTeacherId();
            } else if (teachClassMap.containsKey(statRange.getRangeId())) {
                rangeName = teachClassMap.get(statRange.getRangeId()).getName();
            }
            StringBuilder teacherNames = new StringBuilder();
            if (StringUtils.isNotEmpty(teacherId)) {
                Teacher teacher = teacherMap.get(teacherId);
                if (teacher != null) {
                    teacherNames.append(teacher.getTeacherName());
                }
            }
            statRange.setRangeName(rangeName);
            statRange.setTeacherName(teacherNames.toString());
        }
    }

    @RequestMapping("/examNewClass/showData3/page")
    @ControllerInfo("行政班级成绩分析报表-type3")
    public String showClassData3(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        String examId = request.getParameter("examId");
        String sumType = request.getParameter("sumType");
        String acadyear = request.getParameter("acadyear");
        String gradeCode = request.getParameter("gradeCode");
        String semester = request.getParameter("semester");
        String showType = request.getParameter("showType");
        LoginInfo loginInfo = getLoginInfo();
        String jsonString = "";
        if ("0".equals(sumType)) {
            jsonString = reportService.getScoreRankingByClassSelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, examId, "0");
            map.put("tableType", "1");
        } else if ("9".equals(sumType)) {
            jsonString = reportService.getScoreRankingByClassSelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, examId, "1");
            map.put("tableType", "2");
        }
        List<EmStatRange> emStatRangeList = new ArrayList<>();
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            emStatRangeList = SUtils.dt(json.getString("statArrangeList"), new TR<List<EmStatRange>>() {
            });
            map.put("title", json.getString("title"));
            map.put("emStatRangeList", emStatRangeList);
        }
        map.put("type", type);
        map.put("showType", showType);
        if (StringUtils.equals(showType, "1") && CollectionUtils.isNotEmpty(emStatRangeList)) {
            Collections.sort(emStatRangeList, new Comparator<EmStatRange>() {
                @Override
                public int compare(EmStatRange o1, EmStatRange o2) {
                    if (StringUtils.isBlank(o1.getClassNameOrder())) {
                        return 1;
                    }
                    if (StringUtils.isBlank(o2.getClassNameOrder())) {
                        return -1;
                    }
                    return o1.getClassNameOrder().compareTo(o2.getClassNameOrder());
                }
            });
            JSONObject json = new JSONObject();
            String[] xAxisData = new String[emStatRangeList.size()];
            Double[][] loadingData = new Double[1][emStatRangeList.size()];
            Float[][] loadingData1 = new Float[1][emStatRangeList.size()];
            int i = 0;
            DecimalFormat df = new DecimalFormat("#.0");
            for (EmStatRange e : emStatRangeList) {
                xAxisData[i] = e.getClassName();
                loadingData[0][i] = NumberUtils.toDouble(df.format(e.getAvgScore()));
                i++;
            }
            json.put("legendData", new String[]{"总分平均分"});
            json.put("xAxisData", xAxisData);
            json.put("loadingData", loadingData);
            JSONObject json2 = new JSONObject();
            int j = 0;
            for (EmStatRange e : emStatRangeList) {
                loadingData1[0][j] = e.getAvgScoreT();
                j++;
            }
            json2.put("legendData", new String[]{"班级标准分T"});
            json2.put("xAxisData", xAxisData);
            json2.put("loadingData", loadingData1);
            map.put("jsonStringData1", json.toString());
            map.put("jsonStringData2", json2.toString());
        } else {
            map.put("jsonStringData", "");
        }
        return "/examanalysis/examNewClass/showClassData3.ftl";
    }

    @RequestMapping("/examNewClass/showData2/page")
    @ControllerInfo("行政班级成绩分析报表-type2")
    public String showClassData2(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        String examId = request.getParameter("examId");
        String sumType = request.getParameter("sumType");
        String acadyear = request.getParameter("acadyear");
        String gradeCode = request.getParameter("gradeCode");
        String semester = request.getParameter("semester");
        String showType = request.getParameter("showType");
        LoginInfo loginInfo = getLoginInfo();
        String jsonString = "";
        boolean flagRank = "21".equals(type);
        if (flagRank) {
            if ("0".equals(sumType)) {
                jsonString = reportService.getRankDistributeByClassSelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, "0");
                map.put("tableType", "1");
            } else if ("9".equals(sumType)) {
                jsonString = reportService.getRankDistributeByClassSelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, "1");
                map.put("tableType", "2");
            }
        } else {
            if ("0".equals(sumType)) {
                jsonString = reportService.getScoreDistributeByClassSelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, "0");
                map.put("tableType", "1");
            } else if ("9".equals(sumType)) {
                jsonString = reportService.getScoreDistributeByClassSelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, "1");
                map.put("tableType", "2");
            }
        }
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            List<Clazz> clazzList = SUtils.dt(json.getString("clazzList"), new TR<List<Clazz>>() {
            });
            List<EmSpaceItem> spaceList = SUtils.dt(json.getString("spaceList"), new TR<List<EmSpaceItem>>() {
            });
            Map<String, String> emStatMap = SUtils.dt(json.getString("emStatMap"), new TR<Map<String, String>>() {
            });
            Map<String, String> emStatNumMap = SUtils.dt(json.getString("emStatNumMap"), new TR<Map<String, String>>() {
            });
            map.put("title", json.getString("title"));
            map.put("clazzList", clazzList);
            map.put("spaceList", spaceList);
            map.put("emStatMap", emStatMap);
            map.put("emStatNumMap", emStatNumMap);
            if (StringUtils.equals(showType, "1") && CollectionUtils.isNotEmpty(spaceList)) {
                List<EmSpaceItem> spaceList1 = new ArrayList<>();
                for (EmSpaceItem emSpaceItem : spaceList) {
                    if (emStatNumMap.containsKey(emSpaceItem.getId()) && NumberUtils.toDouble(emStatNumMap.get(emSpaceItem.getId())) > 0) {
                        spaceList1.add(emSpaceItem);
                    }
                }
                JSONObject json1 = new JSONObject();
                String[] legendData = new String[spaceList1.size()];
                String[] stackData = new String[spaceList1.size()];
                String[] xAxisData = new String[clazzList.size()];
                String[] xAxisData2 = new String[spaceList1.size()];
                int i = 0;
                for (Clazz e : clazzList) {
                    xAxisData[i++] = e.getClassNameDynamic();
                }
                i = 0;
                Double[][] loadingData = new Double[spaceList1.size()][clazzList.size()];
                Double[][] loadingData2 = new Double[1][spaceList1.size()];
                for (EmSpaceItem e : spaceList1) {
                    legendData[i] = e.getName();
                    stackData[i] = flagRank ? "名次段" : "分数段";
                    xAxisData2[i] = e.getName();
                    loadingData2[0][i] = NumberUtils.toDouble(emStatNumMap.get(e.getId()));
                    int j = 0;
                    for (Clazz c : clazzList) {
                        loadingData[i][j] = NumberUtils.toDouble(emStatNumMap.get(c.getId() + e.getId()));
                        j++;
                    }
                    i++;
                }
                json1.put("legendData", legendData);
                json1.put("stackData", stackData);
                json1.put("xAxisData", xAxisData);
                json1.put("loadingData", loadingData);
                map.put("jsonStringData1", json1.toString());
                JSONObject json2 = new JSONObject();
                json2.put("legendData", new String[]{"人数"});
                json2.put("xAxisData", xAxisData2);
                json2.put("loadingData", loadingData2);
                map.put("jsonStringData2", json2.toString());
            } else {
                map.put("jsonStringData1", "");
                map.put("jsonStringData2", "");
            }
        }
        map.put("type", type);
        map.put("showType", showType);
        return "/examanalysis/examNewClass/showClassData2.ftl";
    }

    @RequestMapping("/examNewClass/showData/page")
    @ControllerInfo("行政班级成绩分析报表-type1")
    public String showClassData(HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        String sumType = request.getParameter("sumType");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        LoginInfo loginInfo = getLoginInfo();
        String jsonString = "";
        if ("0".equals(sumType)) {
            jsonString = reportService.getExamClassAllSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, classId, examId, "1");
            map.put("tableType", "1");
        } else if ("9".equals(sumType)) {
            jsonString = reportService.getExamClassAllSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, classId, examId, "2");
            map.put("tableType", "2");
        }
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            List<ExamSubjectDto> subjectDtoList = SUtils.dt(json.getString("subjectDtoList"), new TR<List<ExamSubjectDto>>() {
            });
            List<EmStat> statList = SUtils.dt(json.getString("totalList"), new TR<List<EmStat>>() {
            });
            Map<String, EmStat> emStatMap = SUtils.dt(json.getString("emStatMap"), new TR<Map<String, EmStat>>() {
            });
            if ("0".equals(sumType)) {
                map.put("subjectDtoList", subjectDtoList);
                map.put("tableType", "1");
            } else if ("9".equals(sumType)) {
                List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(loginInfo.getUnitId()), new TR<List<Course>>() {
                });
                Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
                List<ExamSubjectDto> subjectLastList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(subjectDtoList)) {
                    boolean flag = false;
                    for (ExamSubjectDto subjectDto : subjectDtoList) {
                        if (!flag && id73Set.contains(subjectDto.getSubjectId().split(",")[0])) {
                            ExamSubjectDto ysyDto = new ExamSubjectDto();
                            ysyDto.setSubjectId(ExammanageConstants.CON_YSY_ID + ",0");
                            ysyDto.setSubType("0");
                            ysyDto.setSubjectName("非7选3科目总分");
                            subjectLastList.add(ysyDto);
                            flag = true;
                        }
                        subjectLastList.add(subjectDto);
                    }
                    ExamSubjectDto sumDto = new ExamSubjectDto();
                    sumDto.setSubjectId(ExammanageConstants.CON_SUM_ID + ",0");
                    sumDto.setSubjectName("总分(非7选3+选考赋分)");
                    sumDto.setSubType("0");
                    subjectLastList.add(sumDto);
                }
                map.put("subjectDtoList", subjectLastList);
                map.put("tableType", "2");
            }
            map.put("title", json.getString("title"));
            map.put("summary", json.getString("summary"));
            map.put("statList", statList);
            map.put("emStatMap", emStatMap);
        }
        map.put("type", type);
        return "/examanalysis/examNewClass/showClassData.ftl";
    }

    private List<Grade> getEduGradeList(List<McodeDetail> mcodelist, Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds) {
        List<Grade> gradeList = new ArrayList<Grade>();
        for (int i = 0; i < mcodelist.size(); i++) {
            McodeDetail detail = mcodelist.get(i);
            int section = Integer.parseInt(detail.getThisId());
            String thisId = detail.getThisId();
            Map<String, McodeDetail> mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-" + thisId);
            if (mcodeMap == null || mcodeMap.size() <= 0) {
                continue;
            }
            int nz = Integer.parseInt(detail.getMcodeContent());
            for (int j = 0; j < nz; j++) {
                int grade = j + 1;
                Grade dto = new Grade();
                dto.setGradeCode(section + "" + grade);
                if (mcodeMap.containsKey(grade + "")) {
                    dto.setGradeName(mcodeMap.get(grade + "").getMcodeContent());
                }
                gradeList.add(dto);
            }
        }
        Collections.sort(gradeList, new Comparator<Grade>() {
            public int compare(Grade o1, Grade o2) {
                return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
            }
        });
        return gradeList;
    }

    @RequestMapping("/examNewGrade/exportPro")
    @ControllerInfo("年级成绩分析exportPro")
    public void exportPro(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String referExamId = request.getParameter("referExamId");
        String gradeCode = request.getParameter("gradeCode");
        String unitId = getLoginInfo().getUnitId();
        String[] ss = subjectId.split(",");
        List<EmStat> statList = null;
        List<EmStat> referList = null;
        String jsonString = null;
        String referJsonString = null;
        if (ss != null && ss.length > 1) {
            jsonString = reportService.getClassScoreRankBySelf(unitId, acadyear, semester, examId, gradeCode, ss[0], null, ss[1]);
            referJsonString = reportService.getClassScoreRankBySelf(unitId, acadyear, semester, referExamId, gradeCode, ss[0], null, ss[1]);
            if (StringUtils.isNotBlank(jsonString)) {
                statList = SUtils.dt(JSONObject.parseObject(jsonString).getString("statList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(referJsonString)) {
                referList = SUtils.dt(JSONObject.parseObject(referJsonString).getString("statList"), new TR<List<EmStat>>() {
                });
            }
        } else {
            if (ExammanageConstants.ZERO32.equals(ss[0])) {
                jsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, examId, "1");
                referJsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, referExamId, "1");
            } else if (ExammanageConstants.CON_SUM_ID.equals(ss[0])) {
                jsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, examId, "2");
                referJsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, referExamId, "2");
            }
            if (StringUtils.isNotBlank(jsonString)) {
                statList = SUtils.dt(JSONObject.parseObject(jsonString).getString("totalList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(referJsonString)) {
                referList = SUtils.dt(JSONObject.parseObject(referJsonString).getString("totalList"), new TR<List<EmStat>>() {
                });
            }
        }
        if (CollectionUtils.isNotEmpty(statList) && CollectionUtils.isNotEmpty(referList)) {
            getDegreeStatList(statList, referList);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("总课表");
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString("学号"));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            cell = row.createCell(1);
            cell.setCellValue(new HSSFRichTextString("姓名"));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
            cell = row.createCell(2);
            cell.setCellValue(new HSSFRichTextString("行政班级"));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
            cell = row.createCell(3);
            cell.setCellValue(new HSSFRichTextString("本次考试"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
            cell = row.createCell(6);
            cell.setCellValue(new HSSFRichTextString("参照考试"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 8));
            cell = row.createCell(9);
            cell.setCellValue(new HSSFRichTextString("进步度"));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
            cell = row.createCell(10);
            cell.setCellValue(new HSSFRichTextString("进步度年级排名"));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
            cell = row.createCell(11);
            cell.setCellValue(new HSSFRichTextString("进步度班级排名"));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
            row = sheet.createRow(1);
            cell = row.createCell(3);
            cell.setCellValue(new HSSFRichTextString("考试分"));
            cell = row.createCell(4);
            cell.setCellValue(new HSSFRichTextString("年级排名"));
            cell = row.createCell(5);
            cell.setCellValue(new HSSFRichTextString("标准分T（年级）"));
            cell = row.createCell(6);
            cell.setCellValue(new HSSFRichTextString("考试分"));
            cell = row.createCell(7);
            cell.setCellValue(new HSSFRichTextString("年级排名"));
            cell = row.createCell(8);
            cell.setCellValue(new HSSFRichTextString("标准分T（年级）"));
            EmStat stat = null;
            if (CollectionUtils.isNotEmpty(statList)) {
                for (int l = 0; l < statList.size(); l++) {
                    stat = statList.get(l);
                    row = sheet.createRow(l + 2);
                    cell = row.createCell(0);
                    cell.setCellValue(new HSSFRichTextString(stat.getStudentCode()));
                    cell = row.createCell(1);
                    cell.setCellValue(new HSSFRichTextString(stat.getStudentName()));
                    cell = row.createCell(2);
                    cell.setCellValue(new HSSFRichTextString(stat.getClassName()));
                    cell = row.createCell(3);
                    cell.setCellValue(new HSSFRichTextString(stat.getScore() + ""));
                    cell = row.createCell(4);
                    cell.setCellValue(new HSSFRichTextString(stat.getGradeRank() + ""));
                    cell = row.createCell(5);
                    cell.setCellValue(new HSSFRichTextString(stat.getScoreT() + ""));
                    cell = row.createCell(6);
                    cell.setCellValue(new HSSFRichTextString(stat.getCompareExamScore() + ""));
                    cell = row.createCell(7);
                    cell.setCellValue(new HSSFRichTextString(stat.getCompareExamRank() + ""));
                    cell = row.createCell(8);
                    cell.setCellValue(new HSSFRichTextString(stat.getCompareExamScoreT() + ""));
                    cell = row.createCell(9);
                    cell.setCellValue(new HSSFRichTextString(stat.getProgressDegree() + ""));
                    cell = row.createCell(10);
                    cell.setCellValue(new HSSFRichTextString(stat.getProgressDegreeRankGrade() + ""));
                    cell = row.createCell(11);
                    cell.setCellValue(new HSSFRichTextString(stat.getProgressDegreeRankClass() + ""));
                }
            }
            try {
                ExportUtils.outputData(workbook, "进步度分析", getResponse());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/examNewGrade/exportAbi")
    @ControllerInfo("年级成绩分析exportAbi")
    public void exportAbi(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String gradeCode = request.getParameter("gradeCode");
        String unitId = getLoginInfo().getUnitId();
        String jsonString = reportService.getExamAllSubjectBySelf(unitId, acadyear, semester, gradeCode, examId, "3");
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            List<ExamSubjectDto> subjectDtoList = SUtils.dt(json.getString("subjectDtoList"), new TR<List<ExamSubjectDto>>() {
            });
            List<EmStat> statList = SUtils.dt(json.getString("totalList"), new TR<List<EmStat>>() {
            });
            Map<String, EmStat> emStatMap = SUtils.dt(json.getString("emStatMap"), new TR<Map<String, EmStat>>() {
            });
            map.put("subjectDtoList", subjectDtoList);
            map.put("title", json.getString("title"));
            map.put("summary", json.getString("summary"));
            map.put("statList", statList);
            map.put("emStatMap", emStatMap);
            String fileName = json.getString("title");
            List<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
            List<String> tis = new ArrayList<String>();
            tis.add("学号");
            tis.add("姓名");
            tis.add("行政班");
            for (ExamSubjectDto dto : subjectDtoList) {
                tis.add(dto.getSubjectName() + "标准分T（年级）");
            }
            tis.add("综合能力分");
            tis.add("考试总分年级排名");
            tis.add("综合能力分年级排名");
            if (CollectionUtils.isNotEmpty(statList)) {
                Map<String, String> valueMap = null;
                for (EmStat emStat : statList) {
                    valueMap = new HashMap<String, String>();
                    valueMap.put("学号", emStat.getStudentCode());
                    valueMap.put("姓名", emStat.getStudentName());
                    valueMap.put("行政班", emStat.getClassName());
                    for (ExamSubjectDto dto : subjectDtoList) {
                        EmStat inStat = emStatMap.get(emStat.getStudentId() + dto.getSubjectId());
                        if (inStat != null) {
                            BigDecimal b = new BigDecimal(inStat.getScoreT());
                            valueMap.put(dto.getSubjectName() + "标准分T（年级）", b.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                        }
                    }
                    BigDecimal b1 = new BigDecimal(emStat.getAbilityScore());
                    valueMap.put("综合能力分", b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                    valueMap.put("考试总分年级排名", emStat.getGradeRank() + "");
                    valueMap.put("综合能力分年级排名", emStat.getAbilityRank() + "");
                    recordList.add(valueMap);
                }
            }
            Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
            sheetName2RecordListMap.put("exportGrade", recordList);
            Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
            titleMap.put("exportGrade", tis);
            ExportUtils exportUtils = ExportUtils.newInstance();
            fileName = fileName.replaceAll(" ", "").replaceAll("考试分总分", "综合能力分");
            exportUtils.exportXLSFile(fileName, titleMap, sheetName2RecordListMap, getResponse());
        }
    }

    @RequestMapping("/examNewGrade/export")
    @ControllerInfo("年级成绩分析export")
    public void exportNewGrade(HttpServletRequest request, ModelMap map) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String examId = request.getParameter("examId");
        String gradeCode = request.getParameter("gradeCode");
        String subjectId = request.getParameter("subjectId");
        List<String> tis = new ArrayList<String>();
        String fileName = "";
        String tableType = "";
        List<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
        if (StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(subjectId)) {
            LoginInfo loginInfo = getLoginInfo();
            String[] ss = subjectId.split(",");
            if (ss != null && ss.length > 1) {
                String jsonString = reportService.getClassScoreRankBySelf(loginInfo.getUnitId(), acadyear, semester, examId, gradeCode, ss[0], null, ss[1]);
                if (StringUtils.isNotBlank(jsonString)) {
                    JSONObject json = JSONObject.parseObject(jsonString);
                    List<EmStat> statList = SUtils.dt(json.getString("statList"), new TR<List<EmStat>>() {
                    });
                    fileName = json.getString("title");
                    boolean is73Sub = "true".equals(json.getString("is73Sub"));
                    tis.add("学号");
                    tis.add("行政班");
                    tis.add("学生");
                    tis.add("考试分");
                    tis.add("标准分T（年级）");
                    tis.add("百分等级分数（年级）");
                    if (is73Sub) {
                        tis.add("赋分");
                    }
                    tis.add("班级排名");
                    tis.add("年级排名");
                    if (CollectionUtils.isNotEmpty(statList)) {
                        Map<String, String> valueMap = null;
                        for (EmStat emStat : statList) {
                            valueMap = new HashMap<String, String>();
                            valueMap.put("学号", emStat.getStudentCode());
                            valueMap.put("行政班", emStat.getClassName());
                            valueMap.put("学生", emStat.getStudentName());
                            BigDecimal b1 = new BigDecimal(emStat.getScore());
                            valueMap.put("考试分", b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                            b1 = new BigDecimal(emStat.getScoreT());
                            valueMap.put("标准分T（年级）", b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                            b1 = new BigDecimal(emStat.getScoreLevel());
                            valueMap.put("百分等级分数（年级）", b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                            if (is73Sub) {
                                BigDecimal b2 = new BigDecimal(emStat.getConScore());
                                valueMap.put("赋分", b2.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                            }
                            valueMap.put("班级排名", emStat.getClassRank() + "");
                            valueMap.put("年级排名", emStat.getGradeRank() + "");
                            recordList.add(valueMap);
                        }
                    }
                }
            } else {
                String jsonString = "";
                if (ExammanageConstants.ZERO32.equals(ss[0])) {
                    jsonString = reportService.getExamAllSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, examId, "1");
                    tableType = "1";
                } else if (ExammanageConstants.CON_SUM_ID.equals(ss[0])) {
                    jsonString = reportService.getExamAllSubjectBySelf(loginInfo.getUnitId(), acadyear, semester, gradeCode, examId, "2");
                    tableType = "2";
                }
                if (StringUtils.isNotBlank(jsonString)) {
                    JSONObject json = JSONObject.parseObject(jsonString);
                    fileName = json.getString("title");
                    List<ExamSubjectDto> subjectDtoList = SUtils.dt(json.getString("subjectDtoList"), new TR<List<ExamSubjectDto>>() {
                    });
                    boolean haveSub = CollectionUtils.isNotEmpty(subjectDtoList);
                    List<EmStat> statList = SUtils.dt(json.getString("totalList"), new TR<List<EmStat>>() {
                    });
                    Map<String, EmStat> emStatMap = SUtils.dt(json.getString("emStatMap"), new TR<Map<String, EmStat>>() {
                    });
                    if ("1".equals(tableType)) {
                        tis.add("学号");
                        tis.add("行政班");
                        tis.add("学生");
                        tis.add("总分");
                        tis.add("标准分T（年级）");
                        tis.add("百分等级分数（年级）");
                        if (haveSub) {
                            for (ExamSubjectDto subjectDto : subjectDtoList) {
                                tis.add(subjectDto.getSubjectName());
                            }
                        }
                        tis.add("班级排名");
                        tis.add("年级排名");
                        if (CollectionUtils.isNotEmpty(statList)) {
                            Map<String, String> valueMap = null;
                            for (EmStat emStat : statList) {
                                valueMap = new HashMap<String, String>();
                                valueMap.put("学号", emStat.getStudentCode());
                                valueMap.put("行政班", emStat.getClassName());
                                valueMap.put("学生", emStat.getStudentName());
                                if (haveSub) {
                                    for (ExamSubjectDto subjectDto : subjectDtoList) {
                                        if (emStatMap.containsKey(emStat.getStudentId() + subjectDto.getSubjectId())) {
                                            EmStat inStat = emStatMap.get(emStat.getStudentId() + subjectDto.getSubjectId());
                                            BigDecimal b = new BigDecimal(inStat.getScore());
                                            valueMap.put(subjectDto.getSubjectName(), b.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                                        } else {
                                            valueMap.put(subjectDto.getSubjectName(), "-");
                                        }
                                    }
                                }
                                BigDecimal b = new BigDecimal(emStat.getScore());
                                valueMap.put("总分", b.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                                b = new BigDecimal(emStat.getScoreT());
                                valueMap.put("标准分T（年级）", b.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                                b = new BigDecimal(emStat.getScoreLevel());
                                valueMap.put("百分等级分数（年级）", b.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                                valueMap.put("班级排名", emStat.getClassRank() + "");
                                valueMap.put("年级排名", emStat.getGradeRank() + "");
                                recordList.add(valueMap);
                            }
                        }
                    } else if ("2".equals(tableType)) {
                        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(loginInfo.getUnitId()), new TR<List<Course>>() {
                        });
                        Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
                        List<ExamSubjectDto> subjectLastList = new ArrayList<>();
                        int length = 3;
                        if (CollectionUtils.isNotEmpty(subjectDtoList)) {
                            boolean flag = false;
                            for (ExamSubjectDto subjectDto : subjectDtoList) {
                                if (StringUtils.equals(subjectDto.getSubType(), "1")) {
                                    length += 4;
                                } else
                                    length += 3;
                                if (!flag && id73Set.contains(subjectDto.getSubjectId().split(",")[0])) {
                                    ExamSubjectDto ysyDto = new ExamSubjectDto();
                                    ysyDto.setSubjectId(ExammanageConstants.CON_YSY_ID + ",0");
                                    ysyDto.setSubType("0");
                                    ysyDto.setSubjectName("非7选3科目总分");
                                    subjectLastList.add(ysyDto);
                                    flag = true;
                                    length += 3;
                                }
                                subjectLastList.add(subjectDto);
                            }
                            ExamSubjectDto sumDto = new ExamSubjectDto();
                            sumDto.setSubjectId(ExammanageConstants.CON_SUM_ID + ",0");
                            sumDto.setSubjectName("总分（非7选3+选考赋分）");
                            sumDto.setSubType("0");
                            subjectLastList.add(sumDto);
                            length += 3;
                        }
                        tis.add("学号");
                        tis.add("行政班");
                        tis.add("学生");
                        if (haveSub) {
                            for (ExamSubjectDto subjectDto : subjectLastList) {
                                tis.add(subjectDto.getSubjectName());
                            }
                        }
                        HSSFWorkbook workbook = new HSSFWorkbook();
                        HSSFSheet sheet = workbook.createSheet("总课表");
                        HSSFRow row = sheet.createRow(0);
                        HSSFCell cell = row.createCell(0);
                        cell.setCellValue(new HSSFRichTextString("学号"));
                        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                        cell = row.createCell(1);
                        cell.setCellValue(new HSSFRichTextString("行政班级"));
                        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                        cell = row.createCell(2);
                        cell.setCellValue(new HSSFRichTextString("学生"));
                        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
                        ExamSubjectDto subjectDto = null;
                        int j = 3;
                        for (int i = 0; i < subjectLastList.size(); i++) {
                            subjectDto = subjectLastList.get(i);
                            cell = row.createCell(j);
                            if ("1".equals(subjectDto.getSubType())) {
                                sheet.addMergedRegion(new CellRangeAddress(0, 0, j, j + 3));
                                j += 4;
                            } else {
                                sheet.addMergedRegion(new CellRangeAddress(0, 0, j, j + 2));
                                j += 3;
                            }
                            cell.setCellValue(new HSSFRichTextString(subjectDto.getSubjectName()));
                        }
                        row = sheet.createRow(1);
                        j = 3;
                        for (int i = 0; i < subjectLastList.size(); i++) {
                            subjectDto = subjectLastList.get(i);
                            cell = row.createCell(j);
                            cell.setCellValue(new HSSFRichTextString("考试分"));
                            cell = row.createCell(j + 1);
                            cell.setCellValue(new HSSFRichTextString("班级排名"));
                            cell = row.createCell(j + 2);
                            cell.setCellValue(new HSSFRichTextString("年级排名"));
                            if ("1".equals(subjectDto.getSubType())) {
                                cell = row.createCell(j + 3);
                                cell.setCellValue(new HSSFRichTextString("赋分"));
                                j += 4;
                            } else {
                                j += 3;
                            }
                        }
                        EmStat stat = null;
                        if (CollectionUtils.isNotEmpty(statList)) {
                            for (int l = 0; l < statList.size(); l++) {
                                stat = statList.get(l);
                                row = sheet.createRow(l + 2);
                                cell = row.createCell(0);
                                cell.setCellValue(new HSSFRichTextString(stat.getStudentCode()));
                                cell = row.createCell(1);
                                cell.setCellValue(new HSSFRichTextString(stat.getClassName()));
                                cell = row.createCell(2);
                                cell.setCellValue(new HSSFRichTextString(stat.getStudentName()));
                                j = 3;
                                for (int i = 0; i < subjectLastList.size(); i++) {
                                    subjectDto = subjectLastList.get(i);
                                    EmStat inStat = emStatMap.get(stat.getStudentId() + subjectDto.getSubjectId());
                                    if (inStat != null) {
                                        cell = row.createCell(j);
                                        cell.setCellValue(inStat.getScore());
                                        cell = row.createCell(j + 1);
                                        cell.setCellValue(inStat.getClassRank());
                                        cell = row.createCell(j + 2);
                                        cell.setCellValue(inStat.getGradeRank());
                                    }
                                    if ("1".equals(subjectDto.getSubType())) {
                                        cell = row.createCell(j + 3);
                                        if (inStat != null) {
                                            cell.setCellValue(inStat.getConScore());
                                        } else {
                                            cell.setCellValue("");
                                        }
                                        j += 4;
                                    } else {
                                        j += 3;
                                    }
                                }
                            }
                        }
                        try {
                            fileName = fileName.replaceAll(" ", "").replaceAll("\\+", "与");
                            ExportUtils.outputData(workbook, fileName, getResponse());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (!"2".equals(tableType)) {
            Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
            sheetName2RecordListMap.put("exportGrade", recordList);
            Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
            titleMap.put("exportGrade", tis);
            ExportUtils exportUtils = ExportUtils.newInstance();
            fileName = fileName.replaceAll(" ", "");
            exportUtils.exportXLSFile(fileName, titleMap, sheetName2RecordListMap, getResponse());
        }
    }

    public void getAllRank(List<EmStat> statList, String examId, String unitId) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject != null) {
            List<EmStat> statListSum = emStatService.findByAllExamIdAndObjIdAndSubjectId(examId, ExammanageConstants.CON_SUM_ID, emStatObject.getId());
            if (CollectionUtils.isNotEmpty(statListSum)) {
                Collections.sort(statListSum, (o1, o2) -> {
                    return o2.getScore().compareTo(o1.getScore());
                });
                float score = 0.0f;
                int i = 1;
                int j = 1;
                for (EmStat emStat : statListSum) {
                    if (score == emStat.getScore()) {
                        emStat.setGradeRank(j);
                    } else {
                        emStat.setGradeRank(i);
                        j = i;
                        score = emStat.getScore();
                    }
                    i++;
                }
                Map<String, EmStat> map1 = EntityUtils.getMap(statListSum, EmStat::getStudentId);
                for (EmStat emStat : statList) {
                    EmStat inStat = map1.get(emStat.getStudentId());
                    if (inStat != null) {
                        emStat.setAllScore(inStat.getScore());
                        emStat.setAllRank(inStat.getGradeRank());
                    } else {
                        emStat.setAllRank(statListSum.get(statListSum.size() - 1).getGradeRank() + 1);
                    }
                }
            }
        }
    }
}
