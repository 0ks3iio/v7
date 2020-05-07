package net.zdsoft.exammanage.data.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.exammanage.data.constant.ExammanageConstants.ZERO32;

@Service("newReportService")
public class NewReportServiceImpl implements NewReportService {
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private TeachGroupRemoteService teachGroupRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private EmConversionService emConversionService;
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmStatService emStatService;

    @Override
    public void getReport(ModelMap map, String unitId, String examId, String subjectId, String subType) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, examInfo.getGradeCodes()), new TR<List<Grade>>() {
        });
        String gradeId = null;
        if (CollectionUtils.isNotEmpty(gradeList)) {
            gradeId = gradeList.get(0).getId();
            map.put("gradeName", gradeList.get(0).getGradeName());
        }
        //根据考试id 获取 学科总体概况(或者单科)
        List<EmStatRange> totalList = getTotalList(unitId, examId, subjectId, subType, emStatObject);
        //获取年级前10统计
        List<EmStat> examScoreList = new ArrayList<>();//总分排名
        List<EmStat> subScoreList = null;//赋分排名  高考模式有
        if ((StringUtils.isNotBlank(subType) && "1".equals(subType)) ||
                (StringUtils.isBlank(subjectId) && "1".equals(examInfo.getIsgkExamType()))) {
            map.put("gkType", true);
            subScoreList = new ArrayList<>();
        }
        //获取总分 以及赋分的年级排名信息
        getExamAllSubjectBySelf(unitId, examId, gradeId, subjectId, subType, null, emStatObject, examScoreList, subScoreList);
        //获取前后10名
        setOneFiveList(map, examScoreList, subScoreList, false);

        // 异步获取班級具体信息
        if (StringUtils.isNotBlank(subjectId)) {
            map.put("subjectId", subjectId + "," + subType);
        }
        map.put("totalList", totalList);
        map.put("examInfo", examInfo == null ? new EmExamInfo() : examInfo);
    }

    @Override
    public void getStuReport(ModelMap map, String unitId, String examId, String studentId) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String compareId = getCompareId(null, examId, unitId);
        EmExamInfo comExamInfo = emExamInfoService.findOne(compareId);
        //根据考试id 获取学生 学科总体概况
        List<EmStat> statList = getStuTotalList(map, unitId, examId, compareId, studentId, emStatObject);

        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, examInfo.getGradeCodes()), new TR<List<Grade>>() {
        });
        if (CollectionUtils.isNotEmpty(gradeList)) {
            map.put("gradeName", gradeList.get(0).getGradeName());
        }
        map.put("statList", statList);
        map.put("examInfo", examInfo == null ? new EmExamInfo() : examInfo);
        map.put("comExamInfo", comExamInfo == null ? new EmExamInfo() : comExamInfo);
    }

    @Override
    public void getClassReport(ModelMap map, String unitId, String examId, String classId, String subjectId, String subType) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String compareId = getCompareId(null, examId, unitId);
        EmExamInfo comExamInfo = emExamInfoService.findOne(compareId);
        //根据考试id 获取 学科总体概况(或者单科)
        getClassTotalList(map, unitId, examId, compareId, classId, subjectId, subType, emStatObject);
        //获取班级前10统计
        List<EmStat> examScoreList = new ArrayList<>();//总分排名
        List<EmStat> subScoreList = null;//赋分排名  高考模式有
        if ((StringUtils.isNotBlank(subType) && "1".equals(subType)) ||
                (StringUtils.isBlank(subjectId) && "1".equals(examInfo.getIsgkExamType()))) {
            map.put("gkType", true);
            subScoreList = new ArrayList<>();
        }
        //获取总分 以及赋分的班级排名信息
        getExamAllSubjectBySelf(unitId, examId, null, subjectId, subType, classId, emStatObject, examScoreList, subScoreList);
        //获取前后10名
        setOneFiveList(map, examScoreList, subScoreList, true);
        map.put("examInfo", examInfo == null ? new EmExamInfo() : examInfo);
        map.put("comExamInfo", comExamInfo == null ? new EmExamInfo() : comExamInfo);
    }

    public List<EmStat> getStuTotalList(ModelMap map, String unitId, String examId, String compareId, String studentId, EmStatObject emStatObject) {
        EmStatObject comStatObject = emStatObjectService.findByUnitIdExamId(unitId, compareId);
        //取出基础数据
        Student stu = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        String classId = stu == null ? "0" : stu.getClassId();
        List<EmStat> statList = emStatService.findByExamIdAndStudentId(emStatObject.getId(), examId, studentId);
        //本次考试的数据
        Map<String, EmStat> statMap = statList.stream().collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
        Map<String, EmStat> comMap = null;//上次考试数据
        if (StringUtils.isNotBlank(compareId)) {
            List<EmStat> comList = emStatService.findByExamIdAndStudentId(comStatObject.getId(), compareId, studentId);
            //取出上一次考试的学生情况
            comMap = comList.stream().collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
        }

        List<EmStatRange> schoolRanageList = emStatRangeService.findByObjIdAndRangeId(emStatObject.getId(), examId, unitId, EmStatRange.RANGE_SCHOOL);
        List<EmStatRange> classRanageList = emStatRangeService.findByObjIdAndRangeId(emStatObject.getId(), examId, classId, EmStatRange.RANGE_CLASS);
        //examId  rangeId  rangeType  subjectId  subType 可确定唯一
        Map<String, EmStatRange> schoolRanageMap = schoolRanageList.stream()
                .collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
        Map<String, EmStatRange> classRanageMap = classRanageList.stream()
                .collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
        //取出当前考试的所有科目  通过学生的统计情况过滤不是自己的科目
        List<Course> courseList = getMapCoursrByExamIds(new String[]{examId}, unitId, null).get(examId);
        //取满分值
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        Map<String, EmSubjectInfo> subInfoMap = subjectInfoList.stream()
                .collect(Collectors.toMap(k -> k.getSubjectId(), Function.identity(), (k1, k2) -> k1));
        Map<String, EmStatParm> parmMap = emStatParmService.findMapByUnitId(unitId, examId);
        float conFullScore = 0.0f;
        List<EmConversion> conlist = emConversionService.findByUnitId(unitId);
        if (CollectionUtils.isNotEmpty(conlist)) {
            conFullScore = conlist.get(0).getEndScore();
        }
        float zeroFullScore = 0.0f;
        float sumFullScore = 0.0f;
        List<EmStat> lastList = new ArrayList<>();
        EmSubjectInfo subInfo = null;
        EmStat inStat = null;
        EmStat comStat = null;
        String subIdType = null;//是科目id和科目类型的组装string
//		EmStatRange classRange=null;//每个科目的班级情况
//		EmStatRange schoolRange=null;//每个科目的年级情况
        if (CollectionUtils.isNotEmpty(courseList)) {
            for (Course course : courseList) {
                String typeName = "";
                if (StringUtils.equals(course.getCourseTypeId(), "1")) {
                    typeName = "选考";
                } else if (StringUtils.equals(course.getCourseTypeId(), "2")) {
                    typeName = "学考";
                }
                course.setSubjectName(course.getSubjectName() + typeName);
                subIdType = course.getId() + "_" + course.getCourseTypeId();
                boolean isJoinSum = parmMap.containsKey(course.getId()) && !"0".equals(parmMap.get(course.getId()).getJoinSum());
                if (isJoinSum && "1".equals(course.getCourseTypeId())) {
                    sumFullScore += conFullScore;
                }
                if (statMap.containsKey(subIdType)) {
                    inStat = statMap.get(subIdType);
                    subInfo = subInfoMap.get(course.getId());
                    if (subInfo != null) {
                        if (isJoinSum) {
                            if ("0".equals(course.getCourseTypeId())) {
                                sumFullScore += subInfo.getFullScore();
                            }
                            zeroFullScore += subInfo.getFullScore();
                        }
                        inStat.setFullScore(subInfo.getFullScore());
                    }
                    inStat.setSubjectName(course.getSubjectName());
                    //班级 年级情况
                    inStat.setClassRange(classRanageMap.containsKey(subIdType) ? classRanageMap.get(subIdType) : new EmStatRange());
                    inStat.setSchoolRange(schoolRanageMap.containsKey(subIdType) ? schoolRanageMap.get(subIdType) : new EmStatRange());
                    if (comMap != null && comMap.containsKey(subIdType)) {
                        comStat = comMap.get(subIdType);
                        inStat.setProgressDegree(inStat.getScoreT() - comStat.getScoreT());
                        inStat.setCompareExamRank(comStat.getClassRank());
                        inStat.setCompareGradeRank(comStat.getGradeRank());
                    }
                    lastList.add(inStat);
                }
            }
            subIdType = ExammanageConstants.ZERO32 + "_0";
            inStat = statMap.get(subIdType);
            if (inStat != null) {
                inStat.setSubjectName("总分");
                inStat.setFullScore(zeroFullScore);
                //班级 年级情况
                inStat.setClassRange(classRanageMap.containsKey(subIdType) ? classRanageMap.get(subIdType) : new EmStatRange());
                inStat.setSchoolRange(schoolRanageMap.containsKey(subIdType) ? schoolRanageMap.get(subIdType) : new EmStatRange());
                if (comMap != null && comMap.containsKey(subIdType)) {
                    comStat = comMap.get(subIdType);
                    inStat.setProgressDegree(inStat.getScoreT() - comStat.getScoreT());
                    inStat.setCompareExamRank(comStat.getClassRank());
                    inStat.setCompareGradeRank(comStat.getGradeRank());
                }
                lastList.add(inStat);
            }
            subIdType = ExammanageConstants.CON_SUM_ID + "_0";
            inStat = statMap.get(subIdType);
            if (inStat != null) {
                inStat.setSubjectName("赋分总分");
                inStat.setFullScore(sumFullScore);
                //班级 年级情况
                inStat.setClassRange(classRanageMap.containsKey(subIdType) ? classRanageMap.get(subIdType) : new EmStatRange());
                inStat.setSchoolRange(schoolRanageMap.containsKey(subIdType) ? schoolRanageMap.get(subIdType) : new EmStatRange());
                if (comMap != null && comMap.containsKey(subIdType)) {
                    comStat = comMap.get(subIdType);
                    inStat.setProgressDegree(inStat.getScoreT() - comStat.getScoreT());
                    inStat.setCompareExamRank(comStat.getClassRank());
                    inStat.setCompareGradeRank(comStat.getGradeRank());
                }
                lastList.add(inStat);
            }
        }
        return lastList;

    }

    /**
     * 班级获取总分概况
     *
     * @param map
     * @param unitId
     * @param examId
     * @param compareId
     * @param classId
     * @param emStatObject
     * @return
     */
    public List<EmStatRange> getClassTotalList(ModelMap map, String unitId, String examId, String compareId, String classId, String subjectId, String subType, EmStatObject emStatObject) {

        EmStatObject comStatObject = emStatObjectService.findByUnitIdExamId(unitId, compareId);
        List<EmStatRange> statRanageList = null;
        List<EmStatRange> comRanageList = null;
        boolean noSubId = StringUtils.isBlank(subjectId);
        String zero = ExammanageConstants.ZERO32 + "_0";
        String sum = ExammanageConstants.CON_SUM_ID + "_0";
        if (noSubId) {
            statRanageList = emStatRangeService.findByExamIdAndSubIds(emStatObject.getId(), examId, new String[]{ExammanageConstants.ZERO32, ExammanageConstants.CON_SUM_ID}, null);
            if (comStatObject != null)
                comRanageList = emStatRangeService.findByExamIdAndSubIds(comStatObject.getId(), compareId, new String[]{ExammanageConstants.ZERO32, ExammanageConstants.CON_SUM_ID}, null);
        } else {
            statRanageList = emStatRangeService.findByExamIdAndSubIds(emStatObject.getId(), examId, new String[]{subjectId}, null);
            if (comStatObject != null)
                comRanageList = emStatRangeService.findByExamIdAndSubIds(comStatObject.getId(), compareId, new String[]{subjectId}, null);
            zero = subjectId + "_" + subType;
            if ("1".equals(subType))
                sum = subjectId + "_3";
        }
        if (CollectionUtils.isNotEmpty(statRanageList)) {
            if (CollectionUtils.isEmpty(comRanageList))
                comRanageList = new ArrayList<>();
            //总分和赋分总分的情况下 不需要subType是0  7选3有1 2 3之分
            Map<String, EmStatRange> statRanageMap = statRanageList.stream()
                    .collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType() + "_" + k.getRangeId() + "_" + k.getRangeType(), Function.identity(), (k1, k2) -> k1));
            Map<String, EmStatRange> comRanageMap = comRanageList.stream()
                    .collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType() + "_" + k.getRangeId() + "_" + k.getRangeType(), Function.identity(), (k1, k2) -> k1));

            EmStatRange zeroClassRange = statRanageMap.get(zero + "_" + classId + "_" + EmStatRange.RANGE_CLASS);//考试分总分 本班
            EmStatRange zeroGradeRange = statRanageMap.get(zero + "_" + unitId + "_" + EmStatRange.RANGE_SCHOOL);//考试分总分 年级
            EmStatRange conClassRange = statRanageMap.get(sum + "_" + classId + "_" + EmStatRange.RANGE_CLASS);//赋分总分 本班
            EmStatRange conGradeRange = statRanageMap.get(sum + "_" + unitId + "_" + EmStatRange.RANGE_SCHOOL);//赋分总分 年级

            List<EmStatRange> zeroList = new ArrayList<>();
            List<EmStatRange> conList = new ArrayList<>();
            int zeroRank = 0;
            int conRank = 0;
            EmStatRange inStat = null;
            for (EmStatRange emStatRange : statRanageList) {
                if (EmStatRange.RANGE_CLASS.equals(emStatRange.getRangeType())) {
                    if (zero.equals(emStatRange.getSubjectId() + "_" + emStatRange.getSubType())) {//考试分0
                        inStat = comRanageMap.get(zero + "_" + emStatRange.getRangeId() + "_" + EmStatRange.RANGE_CLASS);
                        if (inStat != null) {
                            emStatRange.setCompareExamAvgScoreT(inStat.getAvgScoreT());
                            emStatRange.setProgressDegree(emStatRange.getAvgScoreT() - inStat.getAvgScoreT());
                        }
                        zeroList.add(emStatRange);
                    } else if (sum.equals(emStatRange.getSubjectId() + "_" + emStatRange.getSubType())) {//赋分
                        inStat = comRanageMap.get(sum + "_" + emStatRange.getRangeId() + "_" + EmStatRange.RANGE_CLASS);
                        if (inStat != null) {
                            emStatRange.setCompareExamAvgScoreT(inStat.getAvgScoreT());
                            emStatRange.setProgressDegree(emStatRange.getAvgScoreT() - inStat.getAvgScoreT());
                        }
                        conList.add(emStatRange);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(zeroList)) {
                zeroRank = getProRank(zeroList, classId);
            }
            if (CollectionUtils.isNotEmpty(conList)) {
                conRank = getProRank(conList, classId);
            }
            map.put("zeroRank", zeroRank);
            map.put("conRank", conRank);
            map.put("zeroClassRange", zeroClassRange);
            map.put("zeroGradeRange", zeroGradeRange);
            map.put("conClassRange", conClassRange);
            map.put("conGradeRange", conGradeRange);
        }
        return statRanageList;
    }

    @Override
    public List<EmStatRange> getTotalList(String unitId, String examId, String subjectId, String subType, EmStatObject emStatObject) {
        List<EmStatRange> lastList = new ArrayList<>();
        boolean noSubId = StringUtils.isBlank(subjectId);
        if (emStatObject != null) {
            List<EmStatRange> statRanageList = null;
            List<Course> courses = null;
            if (noSubId) {
                statRanageList = emStatRangeService.findByObjIdAndRangeId(emStatObject.getId(), examId, unitId, EmStatRange.RANGE_SCHOOL);
                courses = reportService.getSubList(unitId, examId, "0");
            } else {
                statRanageList = new ArrayList<>();
                statRanageList.add(emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeSubType(emStatObject.getId()
                        , examId, subjectId, unitId, EmStatRange.RANGE_SCHOOL, subType));
                Course c = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
                c.setCourseTypeId(subType);
                courses = new ArrayList<>();
                courses.add(c);
            }
            Map<String, EmStatRange> statRanageMap = statRanageList.stream()
                    .collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));

            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
            Map<String, EmSubjectInfo> subInfoMap = subjectInfoList.stream()
                    .collect(Collectors.toMap(k -> k.getSubjectId(), Function.identity(), (k1, k2) -> k1));
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
            if (noSubId) {
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
            }
        }
        return lastList;
    }

    @Override
    public void getExamAllSubjectBySelf(String unitId, String examId, String gradeId, String subjectId, String subType, String classId, EmStatObject emStatObject
            , List<EmStat> examScoreList, List<EmStat> subScoreList) {
        boolean noSubId = StringUtils.isBlank(subjectId);
        //exammanage_stat 学生统计
        Set<String> classIds = new HashSet<>();
        if (StringUtils.isNotBlank(classId)) {
            classIds.add(classId);
        } else {
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
            });
            classIds.addAll(clazzList.stream().map(Clazz::getId).collect(Collectors.toSet()));
        }

        //获取年级或班级学生数据
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
        });
        if (CollectionUtils.isEmpty(studentList)) {//空的情况是教学班的情况
            Set<String> stuIds = EntityUtils.getSet(SUtils.dt(teachClassStuRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<TeachClassStu>>() {
            }), TeachClassStu::getStudentId);
            if (CollectionUtils.isNotEmpty(stuIds)) {
                studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
                });
            }
        }
        //获取
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        List<EmStat> statList = null;
        if (StringUtils.isNotBlank(subjectId)) {
            statList = emStatService.findByExamIdAndObjectSubIds(examId, emStatObject.getId(), new String[]{ExammanageConstants.ZERO32, ExammanageConstants.CON_SUM_ID, subjectId});
        } else {
            statList = emStatService.findByExamIdAndObjectSubIds(examId, emStatObject.getId(), new String[]{ExammanageConstants.ZERO32, ExammanageConstants.CON_SUM_ID});
        }
        statList.forEach(item -> {
            if (noSubId) {
                if (ExammanageConstants.ZERO32.equals(item.getSubjectId())) {
                    if (studentMap.containsKey(item.getStudentId())) {
                        item.setStudentName(studentMap.get(item.getStudentId()).getStudentName());
                        examScoreList.add(item);
                    }
                }
            } else if (subjectId.equals(item.getSubjectId()) && subType.equals(item.getSubType())) {//1代表选考，即代表会有赋分， 0 2都是没有赋分成绩的
                if (studentMap.containsKey(item.getStudentId())) {
                    item.setStudentName(studentMap.get(item.getStudentId()).getStudentName());
                    examScoreList.add(item);
                }
            }
        });
        if (StringUtils.isNotBlank(classId)) {
            Collections.sort(examScoreList, (o1, o2) -> {
                return o1.getClassRank() - o2.getClassRank();
            });
        } else {
            Collections.sort(examScoreList, (o1, o2) -> {
                return o1.getGradeRank() - o2.getGradeRank();
            });
        }
        if (subScoreList != null) {
            statList.forEach(item -> {
                if (noSubId) {
                    if (ExammanageConstants.CON_SUM_ID.equals(item.getSubjectId())) {
                        if (studentMap.containsKey(item.getStudentId())) {
                            item.setStudentName(studentMap.get(item.getStudentId()).getStudentName());
                            subScoreList.add(item);
                        }
                    }
                } else if (subjectId.equals(item.getSubjectId()) && "3".equals(item.getSubType())) {//1是选考 对应的赋分成绩是3。 1是选考的考试成绩
                    if (studentMap.containsKey(item.getStudentId())) {
                        item.setStudentName(studentMap.get(item.getStudentId()).getStudentName());
                        subScoreList.add(item);
                    }
                }
            });
            if (StringUtils.isNotBlank(classId)) {
                Collections.sort(subScoreList, (o1, o2) -> {
                    return o1.getClassRank() - o2.getClassRank();
                });
            } else {
                Collections.sort(subScoreList, (o1, o2) -> {
                    return o1.getGradeRank() - o2.getGradeRank();
                });
            }
        }
    }

    @Override
    public Map<String, List<Course>> getMapCoursrByExamIds(String[] examIds, String unitId, List<EmExamInfo> examList) {
        Map<String, List<Course>> courseListMap = new HashMap<>();
        //获取考试对应的科目
        List<EmSubjectInfo> infoList = emSubjectInfoService.findByUnitIdAndExamId(unitId, examIds);
        Set<String> subjectIds = EntityUtils.getSet(infoList, EmSubjectInfo::getSubjectId);
        Map<String, List<EmSubjectInfo>> subInfoListMap = infoList.stream().collect(Collectors.groupingBy(EmSubjectInfo::getExamId));
        if (CollectionUtils.isEmpty(examList))
            examList = emExamInfoService.findListByIds(examIds);
        Map<String, EmExamInfo> examMap = EntityUtils.getMap(examList, EmExamInfo::getId);
        //组装考试对应的科目 分选考学考
        List<Course> courseList = null;
        Map<String, Course> courseMap = EntityUtils.getMap(SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {
        }), Course::getId);
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
        });
        Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);

        List<EmSubjectInfo> inList = null;
        EmExamInfo inExamInfo = null;
        for (Entry<String, List<EmSubjectInfo>> entry : subInfoListMap.entrySet()) {
            courseList = new ArrayList<>();
            inList = entry.getValue();
            inExamInfo = examMap.get(entry.getKey());
            if (CollectionUtils.isNotEmpty(inList)) {
                for (EmSubjectInfo inSub : inList) {
                    Course c = courseMap.get(inSub.getSubjectId());
                    if (c == null)
                        continue;
                    if (id73Set.contains(inSub.getSubjectId())) {
                        if (StringUtils.equals(inSub.getGkSubType(), "0")) {
                            if (StringUtils.equals(inExamInfo.getIsgkExamType(), "1")) {
                                Course c1 = new Course();
                                c1.setCourseTypeId("1");
                                c1.setId(c.getId());
                                c1.setSubjectName(c.getSubjectName());
                                courseList.add(c1);
                                Course c2 = new Course();
                                c2.setCourseTypeId("2");
                                c2.setId(c.getId());
                                c2.setSubjectName(c.getSubjectName());
                                courseList.add(c2);
                            } else {
                                c.setCourseTypeId(inSub.getGkSubType() == null ? "0" : inSub.getGkSubType());
                                courseList.add(c);
                            }
                        } else {
                            c.setCourseTypeId(inSub.getGkSubType() == null ? "0" : inSub.getGkSubType());
                            courseList.add(c);
                        }
                    } else {
                        c.setCourseTypeId("0");
                        courseList.add(c);
                    }
                }
            }
            courseListMap.put(entry.getKey(), courseList);
        }
        return courseListMap;
    }

    @Override
    public void getZeroScoreComJson(JSONObject jsonData, String examId, String subjectId, String subType, String unitId) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //获取上一场考试id
        String compareId = getCompareId(examInfo, examId, unitId);

        List<EmStatRange> statArrangeList = null;
        if (StringUtils.isBlank(subjectId)) {
            statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, ZERO32, EmStatRange.RANGE_CLASS, "0");
        } else {
            statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, subType);
        }
        //总分的班级标准分t
        EmExamInfo comExamInfo = emExamInfoService.findOne(compareId);
        List<EmStatRange> comArrangeList = null;
        if (StringUtils.isNotBlank(compareId)) {
            //获取进步度排名
            getZeroProCom(jsonData, examId, compareId, unitId, subjectId, subType);

            EmStatObject comStatObject = emStatObjectService.findByUnitIdExamId(unitId, compareId);
            //后续标准分t的图表
            if (StringUtils.isBlank(subjectId)) {
                comArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(comStatObject.getId(), compareId, ZERO32, EmStatRange.RANGE_CLASS, "0");
            } else {
                comArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(comStatObject.getId(), compareId, subjectId, EmStatRange.RANGE_CLASS, subType);
            }
        }
        boolean haveComflag = CollectionUtils.isNotEmpty(comArrangeList);
        if (CollectionUtils.isNotEmpty(statArrangeList)) {
            //组装数据
            Set<String> rangeIds = statArrangeList.stream().map(EmStatRange::getRangeId).collect(Collectors.toSet());
            Map<String, EmStatRange> statMap = EntityUtils.getMap(statArrangeList, EmStatRange::getRangeId);
            Map<String, EmStatRange> statComMap = EntityUtils.getMap(comArrangeList, EmStatRange::getRangeId);
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(rangeIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(clazzList)) {
                //将标准分T存入json中
                String[] legendData = haveComflag ? new String[2] : new String[1];
                String[] xAxisData = new String[clazzList.size()];
                String[][] loadingData = haveComflag ? new String[2][clazzList.size()] : new String[1][clazzList.size()];
                int i = 0;
                legendData[0] = examInfo.getExamName();
                if (haveComflag) {
                    legendData[1] = comExamInfo.getExamName();
                }
                for (Clazz clazz : clazzList) {
                    xAxisData[i] = clazz.getClassNameDynamic();
                    loadingData[0][i] = statMap.containsKey(clazz.getId()) ? statMap.get(clazz.getId()).getAvgScoreT() + "" : "0.0";
                    if (haveComflag) {
                        loadingData[1][i] = statComMap.containsKey(clazz.getId()) ? statComMap.get(clazz.getId()).getAvgScoreT() + "" : "0.0";
                    }
                    i++;
                }
                jsonData.put("legendData", legendData);
                jsonData.put("xAxisData", xAxisData);
                jsonData.put("loadingData", loadingData);
            }
        }
    }

    @Override
    public void getClassZreoScoreCom(ModelMap map, String examId, String classId, String subjectId, String subType, String unitId) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String gradeCode = examInfo.getGradeCodes();
        String compareExamId = getCompareId(examInfo, examId, unitId);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
        });
        List<EmStat> statListClass1 = new ArrayList<>();
        List<EmStat> statListClass2 = new ArrayList<>();
        List<EmStat> statListGrade1 = new ArrayList<>();
        List<EmStat> statListGrade2 = new ArrayList<>();

        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmStatObject emStatObject1 = emStatObjectService.findByUnitIdExamId(unitId, compareExamId);
        if (StringUtils.isNotBlank(subjectId)) {
            String jsonStringClass1 = null;
            String jsonStringClass2 = null;
            String jsonStringGrade1 = null;
            String jsonStringGrade2 = null;
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(classId)) {
                jsonStringClass1 = reportService.getClassScoreRankBySelf(unitId, null, null, examId, gradeCode, subjectId, classId, subType);
                if (StringUtils.isNotBlank(compareExamId)) {
                    jsonStringClass2 = reportService.getClassScoreRankBySelf(unitId, null, null, compareExamId, gradeCode, subjectId, classId, subType);
                }
            }
            if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(examId)) {
                jsonStringGrade1 = reportService.getClassScoreRankBySelf(unitId, null, null, examId, gradeCode, subjectId, "", subType);
                if (StringUtils.isNotBlank(compareExamId)) {
                    jsonStringGrade2 = reportService.getClassScoreRankBySelf(unitId, null, null, compareExamId, gradeCode, subjectId, "", subType);
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
        } else {
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
        }
        setEmstatInfo(statListClass1, unitId, examId, statListClass2, statListGrade1, statListGrade2);
        if (CollectionUtils.isNotEmpty(statListClass2) && CollectionUtils.isNotEmpty(statListClass1)) {
            Map<String, EmStat> emStatMap = statListClass1.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
            map.put("xName", "标准分T");
            map.put("yName", "进步度");

//			String [][] strings11 = new String[statListClass1.size()][2];
//			String [][] strings12 = new String[statListClass1.size()][2];
//			String [][] strings13 = new String[statListClass1.size()][2];
//			String [][] strings14= new String[statListClass1.size()][2];
//			String [][] strings = new String[statListClass1.size()][2];
//			String [][] strings1= new String[statListClass1.size()][2];
            List<String[]> string1 = new ArrayList<>();
            List<String[]> string2 = new ArrayList<>();
            List<String[]> string3 = new ArrayList<>();
            List<String[]> string4 = new ArrayList<>();
            for (int i = 0; i < statListClass1.size(); i++) {
                BigDecimal b = new BigDecimal(statListClass1.get(i).getProgressDegree());
                float progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                BigDecimal b1 = new BigDecimal(Float.toString(statListClass1.get(i).getScoreT()));
                BigDecimal b2 = new BigDecimal("50");
                float scoreTSub = b1.subtract(b2).floatValue();
                if (scoreTSub <= 0 && progressDegree <= 0) {
                    string1.add(new String[]{scoreTSub + "", progressDegree + ""});
                } else if (scoreTSub >= 0 && progressDegree >= 0) {
                    string2.add(new String[]{scoreTSub + "", progressDegree + ""});
                } else if (scoreTSub <= 0 && progressDegree >= 0) {
                    string3.add(new String[]{scoreTSub + "", progressDegree + ""});
                } else {
                    string4.add(new String[]{scoreTSub + "", progressDegree + ""});
                }
            }
            map.put("string1", string1);
            map.put("string2", string2);
            map.put("string3", string3);
            map.put("string4", string4);
            string1 = new ArrayList<>();
            string2 = new ArrayList<>();
            string3 = new ArrayList<>();
            string4 = new ArrayList<>();
            for (int i = 0; i < statListClass2.size(); i++) {
                EmStat emStat = emStatMap.get(statListClass2.get(i).getStudentId());
                if (emStat != null) {
                    BigDecimal b = new BigDecimal(emStat.getProgressDegree());
                    float progressDegree = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                    BigDecimal b1 = new BigDecimal(Float.toString(statListClass2.get(i).getScoreT()));
                    BigDecimal b2 = new BigDecimal("50");
//					strings1[i] = new String[]{b1.subtract(b2).toString(), progressDegree};
//					string2.add(new String[]{b1.subtract(b2).toString(), progressDegree});
                    float scoreTSub = b1.subtract(b2).floatValue();
                    if (scoreTSub <= 0 && progressDegree <= 0) {
                        string1.add(new String[]{scoreTSub + "", progressDegree + ""});
                    } else if (scoreTSub >= 0 && progressDegree >= 0) {
                        string2.add(new String[]{scoreTSub + "", progressDegree + ""});
                    } else if (scoreTSub <= 0 && progressDegree >= 0) {
                        string3.add(new String[]{scoreTSub + "", progressDegree + ""});
                    } else {
                        string4.add(new String[]{scoreTSub + "", progressDegree + ""});
                    }
                }
            }
//			map.put("loadingData1", strings);
//			map.put("loadingData2",strings1);
            map.put("string11", string1);
            map.put("string12", string2);
            map.put("string13", string3);
            map.put("string14", string4);
        }
    }

    @Override
    public void getZeroProCom(JSONObject jsonData, String examId, String compareId, String unitId, String subjectId, String subType) {
        List<EmStat> statList = null;
        List<EmStat> referList = null;
        String jsonString = null;
        String referJsonString = null;
        if (StringUtils.isNotBlank(subjectId)) {
            jsonString = reportService.getClassScoreRankBySelf(unitId, null, null, examId, null, subjectId, null, subType);
            referJsonString = reportService.getClassScoreRankBySelf(unitId, null, null, compareId, null, subjectId, null, subType);
            if (StringUtils.isNotBlank(jsonString)) {
                statList = SUtils.dt(JSONObject.parseObject(jsonString).getString("statList"), new TR<List<EmStat>>() {
                });
            }
            if (StringUtils.isNotBlank(referJsonString)) {
                referList = SUtils.dt(JSONObject.parseObject(referJsonString).getString("statList"), new TR<List<EmStat>>() {
                });
            }
        } else {
            jsonString = reportService.getExamAllSubjectBySelf(unitId, null, null, null, examId, "1");
            referJsonString = reportService.getExamAllSubjectBySelf(unitId, null, null, null, compareId, "1");
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
            List<EmStat> tenStatList = new ArrayList<>();//进步度1-3名的数据
            for (EmStat emStat : statList) {
                if (emStat.getProgressDegreeRankGrade() <= 10) {
                    tenStatList.add(emStat);
                }
            }
            jsonData.put("statList", tenStatList);
        }
    }

    @Override
    public void getStuSubjectComJson(JSONObject jsonData, String examId, String unitId, String studentId) {
        //统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        List<EmStat> statList = emStatService.findByExamIdAndStudentId(emStatObject.getId(), examId, studentId);
        //本次考试的数据
        Map<String, EmStat> statMap = statList.stream().collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
        //取出当前考试的所有科目  通过学生的统计情况过滤不是自己的科目
        List<Course> courseList = getMapCoursrByExamIds(new String[]{examId}, unitId, null).get(examId);
        List<EmStat> lastList = new ArrayList<>();
        EmStat inStat = null;
        for (Course course : courseList) {
            String subIdType = course.getId() + "_" + course.getCourseTypeId();
            if (statMap.containsKey(subIdType)) {
                inStat = statMap.get(subIdType);
                inStat.setSubjectName(course.getSubjectName());
                lastList.add(inStat);
            }
        }
        Double[][] loadingData = new Double[1][lastList.size()];
        int i = 0;
        BigDecimal b = null;
        List<EmStat> subList = new ArrayList<>();
        JSONArray array = new JSONArray();
        JSONObject json = null;
        for (EmStat stat : lastList) {
            json = new JSONObject();
            json.put("subjectName", stat.getSubjectName());
            b = new BigDecimal(stat.getScoreT());
            stat.setScoreT(b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            loadingData[0][i] = NumberUtils.toDouble(stat.getScoreT() + "");
            subList.add(stat);
            array.add(json);
            i++;
        }
        jsonData.put("loadingData", loadingData);
        jsonData.put("array", array);
//		jsonData.put("lastList", lastList);
        int l = lastList.size();
        int oneL = l % 2 == 0 ? l / 2 : l / 2 + 1;
        List<EmStat> oneList = subList.subList(0, oneL);//实际0是开始，oneL-1的位置是结束
        List<EmStat> otherList = subList.subList(oneL, l);
        jsonData.put("oneList", oneList);
        jsonData.put("otherList", otherList);
//		jsonData.put("jsonA", json);
    }

    @Override
    public void getSubjectComJson(JSONObject jsonData, String examId, String unitId, String classId) {
        //统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //考试
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        Set<String> classIds = new HashSet<>();
        List<Clazz> clazzList = null;
        if (StringUtils.isBlank(classId)) {
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, examInfo.getGradeCodes()), new TR<List<Grade>>() {
            });
            String gradeId = CollectionUtils.isNotEmpty(gradeList) ? gradeList.get(0).getId() : "";//获取年级id
            clazzList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
            });
            classIds.addAll(clazzList.stream().map(Clazz::getId).collect(Collectors.toSet()));
        } else {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            clazzList = new ArrayList<>();
            clazzList.add(clazz);
            classIds.add(classId);
        }
        //获取该考试的所有班级数据
        List<EmStatRange> statArrangeList = emStatRangeService.findByExamId(emStatObject.getId(), examId, EmStatRange.RANGE_CLASS, classIds.toArray(new String[0]));
        Map<String, List<EmStatRange>> statRangeListMap = statArrangeList.stream().collect(Collectors.groupingBy(EmStatRange::getRangeId));
        //得到科目信息
        Map<String, List<Course>> courseListMap = getMapCoursrByExamIds(new String[]{examId}, unitId, null);
        List<Course> courseList = courseListMap.get(examId);
        for (Course course : courseList) {
            if (StringUtils.equals(course.getCourseTypeId(), "1")) {
                course.setSubjectName(course.getSubjectName() + "选考");
            } else if (StringUtils.equals(course.getCourseTypeId(), "2")) {
                course.setSubjectName(course.getSubjectName() + "学考");
            }
        }
        //取该行政班 未统计进的教学班科目
        Set<String> noStatSubIds=new HashSet<>();
        Set<String> allSubIds=statArrangeList.stream().map(EmStatRange::getSubjectId).collect(Collectors.toSet());
        courseList.forEach(course -> {
            if(!allSubIds.contains(course.getId())){
                noStatSubIds.add(course.getId());
            }
        });
        List<EmStat> emStats=null;
        List<Student> studentList =null;
        if (StringUtils.isNotBlank(classId)) {
            studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>(){});
            Set<String> stuIds=EntityUtils.getSet(studentList,Student::getId);
            emStats=emStatService.findByStudentIds(emStatObject.getId(),examId,null,stuIds.toArray(new String[0]));
        }else{
            studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>(){});
            if(CollectionUtils.isNotEmpty(noStatSubIds)){
                emStats=emStatService.findByExamIdAndObjectSubIds(examId,emStatObject.getId(),noStatSubIds.toArray(new String[0]));
            }
        }
        //组装出key是班级id对应学生列
        Map<String, List<Student>> claIdStusMap = studentList.stream().collect(Collectors.groupingBy(Student::getClassId));
        Map<String,EmStat> stuStatMap=null;
        if(CollectionUtils.isNotEmpty(emStats)){
            stuStatMap=emStats.stream().collect(Collectors.toMap(k->k.getStudentId()+"_"+k.getSubjectId()+"_"+k.getSubType(),Function.identity(),(k1,k2)->k1));
        }else{
            stuStatMap=new HashMap<>();
        }
       /* clazzList.forEach(c->{
            if(statRangeListMap.containsKey(c.getId())) {
                Map<String, EmStatRange> statMap = statRangeListMap.get(c.getId()).stream().collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
                courseList.forEach(course -> {
                    if(!statMap.containsKey(course.getId() + "_" + course.getCourseTypeId())){
                        noStatSubIds.add()
                    }
                });
            }
        });*/
        JSONArray array = new JSONArray();
        JSONObject json1 = null;
        Double[][] loadingData = null;
        Map<String, EmStatRange> statMap = null;
        for (Clazz clazz : clazzList) {
            json1 = new JSONObject();
//            statRangeListMap.get(clazz.getId());
            EmStatRange stat = null;
            if (statRangeListMap.containsKey(clazz.getId())) {
                List<EmStatRange> statList=statRangeListMap.get(clazz.getId());
                statMap = statRangeListMap.get(clazz.getId()).stream().collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
                loadingData = new Double[1][courseList.size()];
                json1.put("className", clazz.getClassNameDynamic());
                int i = 0;
                for (Course course : courseList) {
                    stat = statMap.get(course.getId() + "_" + course.getCourseTypeId());
                    if(stat==null){
                        stat = new EmStatRange();
                        List<Student> inStuList=claIdStusMap.get(clazz.getId());
                        if(CollectionUtils.isNotEmpty(inStuList)){
                            int l=0;
                            float allAvgT=0.0f;
                            for(Student inStu:inStuList){
                                if(stuStatMap.containsKey(inStu.getId()+"_"+course.getId()+"_"+course.getCourseTypeId())){
                                    allAvgT+=stuStatMap.get(inStu.getId()+"_"+course.getId()+"_"+course.getCourseTypeId()).getScoreT();
                                    l++;
                                }
                            }
                            if(l!=0){
                                BigDecimal b1=new BigDecimal(Float.toString(allAvgT));
                                BigDecimal b2 = new BigDecimal(l+"");
                                stat.setSubjectId(course.getId());
                                stat.setSubType(course.getCourseTypeId());
                                stat.setAvgScoreT(b1.divide(b2, 1,BigDecimal.ROUND_HALF_UP).floatValue());
                            }else{
                                stat.setAvgScoreT(0.0f);
                            }
                        }
                        statList.add(stat);
                    }
                    loadingData[0][i] = NumberUtils.toDouble(stat.getAvgScoreT() + "");
                    i++;
                }
                //把新加的标准分t算上
                statRangeListMap.put(clazz.getId(),statList);
                json1.put("loadingData", loadingData);
                array.add(json1);
            }
        }
        if (StringUtils.isNotBlank(classId)) {
            List<EmStatRange> subjectTList = new ArrayList<>();
            statMap = statRangeListMap.get(classId).stream().collect(Collectors.toMap(k -> k.getSubjectId() + "_" + k.getSubType(), Function.identity(), (k1, k2) -> k1));
            EmStatRange stat = null;
            if (statMap != null) {
                for (Course course : courseList) {
                    stat = statMap.get(course.getId() + "_" + course.getCourseTypeId());
                    if (stat == null) {
                        stat = new EmStatRange();
                        stat.setAvgScoreT(0.0f);
                    }
                    stat.setSubjectName(course.getSubjectName());
                    subjectTList.add(stat);
                }
                int l = subjectTList.size();
                int oneL = l % 2 == 0 ? l / 2 : l / 2 + 1;
                List<EmStatRange> oneList = subjectTList.subList(0, oneL);//实际0是开始，oneL-1的位置是结束
                List<EmStatRange> otherList = subjectTList.subList(oneL, l);
                jsonData.put("oneList", oneList);
                jsonData.put("otherList", otherList);
            }
        }
        jsonData.put("array", array);
        jsonData.put("courseList", courseList);
    }

    @Override
    public void getClassScore(JSONObject jsonData, String jsonString, boolean subjectFlag, boolean flagRank) {
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            if (subjectFlag) {
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
                    for (String scoreItem : scoreItemList) {
                        int num = 0;
                        num += Integer.parseInt(valueMap.get(scoreItem + "年级全体" + "num"));
                        scoreItemNumMap.put(scoreItem, num);
                    }
                }
                if (flagRank) {//名次段 升序排序名称
                    Collections.sort(scoreItemList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return Integer.valueOf(o1.split("-")[0]) - Integer.valueOf(o2.split("-")[0]);
                        }
                    });
                }
                String[] legendData = scoreItemList.toArray(new String[0]);
                String[] stackData = new String[scoreItemList.size()];
                String[] xAxisData = rangeNameListLast.toArray(new String[0]);
                String[] xAxisData2 = scoreItemList.toArray(new String[0]);
                Double[][] loadingData = new Double[scoreItemList.size()][rangeNameListLast.size()];
                Double[][] loadingData2 = new Double[1][scoreItemList.size()];
                int i = 0;
                for (String socreItem : scoreItemList) {
                    legendData[i] = socreItem;
                    stackData[i] = flagRank ? "名次段" : "分数段";
                    loadingData2[0][i] = NumberUtils.toDouble(scoreItemNumMap.get(socreItem) + "");
                    int j = 0;
                    for (String rangeName : rangeNameListLast) {
                        loadingData[i][j] = NumberUtils.toDouble(valueMap.get(socreItem + rangeName + "num"));
                        j++;
                    }
                    i++;
                }
                jsonData.put("legendData2", legendData);
                jsonData.put("stackData2", stackData);
                jsonData.put("xAxisData2", xAxisData);
                jsonData.put("loadingData2", loadingData);

                jsonData.put("legendData22", new String[]{"人数"});
                jsonData.put("xAxisData22", xAxisData2);
                jsonData.put("loadingData22", loadingData2);
            } else {
                List<Clazz> clazzList = SUtils.dt(json.getString("clazzList"), new TR<List<Clazz>>() {
                });
                List<EmSpaceItem> spaceList = SUtils.dt(json.getString("spaceList"), new TR<List<EmSpaceItem>>() {
                });
                Map<String, String> emStatNumMap = SUtils.dt(json.getString("emStatNumMap"), new TR<Map<String, String>>() {
                });
                if (CollectionUtils.isNotEmpty(spaceList)) {
                    List<EmSpaceItem> spaceList1 = new ArrayList<>();
                    for (EmSpaceItem emSpaceItem : spaceList) {
                        if (emStatNumMap.containsKey(emSpaceItem.getId()) && NumberUtils.toDouble(emStatNumMap.get(emSpaceItem.getId())) > 0) {
                            spaceList1.add(emSpaceItem);
                        }
                    }
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
                    jsonData.put("legendData2", legendData);
                    jsonData.put("stackData2", stackData);
                    jsonData.put("xAxisData2", xAxisData);
                    jsonData.put("loadingData2", loadingData);

                    jsonData.put("legendData22", new String[]{"人数"});
                    jsonData.put("xAxisData22", xAxisData2);
                    jsonData.put("loadingData22", loadingData2);
                }
            }
        }
    }

    @Override
    public void getAvgScore(JSONObject jsonData, String jsonString, boolean flag) {
        List<EmStatRange> emStatRangeList = new ArrayList<>();
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject json = JSONObject.parseObject(jsonString);
            emStatRangeList = SUtils.dt(json.getString("statArrangeList"), new TR<List<EmStatRange>>() {
            });
            if (CollectionUtils.isNotEmpty(emStatRangeList)) {
                Collections.sort(emStatRangeList, new Comparator<EmStatRange>() {
                    @Override
                    public int compare(EmStatRange o1, EmStatRange o2) {
						/*if(StringUtils.isBlank(o1.getClassNameOrder())) {
							return 1;
						}
						if(StringUtils.isBlank(o2.getClassNameOrder())) {
							return -1;
						}*/
                        if (StringUtils.isBlank(o1.getClassNameOrder()) || StringUtils.isBlank(o2.getClassNameOrder())) {
                            return o1.getRangeName().compareTo(o2.getRangeName());
                        }
                        return o1.getClassNameOrder().compareTo(o2.getClassNameOrder());
                    }
                });
                String[] xAxisData = new String[emStatRangeList.size()];
                Double[][] loadingData = new Double[1][emStatRangeList.size()];
                Double[][] loadingData11 = new Double[1][emStatRangeList.size()];
                int i = 0;
                DecimalFormat df = new DecimalFormat("#.0");
                for (EmStatRange e : emStatRangeList) {
                    xAxisData[i] = e.getClassName();
                    loadingData[0][i] = NumberUtils.toDouble(df.format(e.getAvgScore()));
                    if (flag) {
                        loadingData11[0][i] = NumberUtils.toDouble(df.format(e.getConAvgScore()));
                    }
                    i++;
                }
                jsonData.put("legendData", new String[]{"平均分"});
                jsonData.put("xAxisData", xAxisData);
                jsonData.put("loadingData", loadingData);
                if (flag)
                    jsonData.put("loadingData11", loadingData11);
            }
        }
    }

    @Override
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

    public Integer getProRank(List<EmStatRange> statList, String classId) {
        int classRank = 0;
        Collections.sort(statList, (o1, o2) -> {
            if (o1.getProgressDegree() != null && o2.getProgressDegree() != null) {
                return o2.getProgressDegree().compareTo(o1.getProgressDegree());
            }
            return 0;
        });
        Float score = 0.0f;
        int i = 1;
        int j = 1;
        BigDecimal b = null;
        for (EmStatRange emStat : statList) {
            if (emStat.getProgressDegree() == null) {
                continue;
            }
            b = new BigDecimal(emStat.getProgressDegree());
            Float nowProScore = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if (i == 1) {
                score = nowProScore;
            }
            if (score > nowProScore) {
                emStat.setProgressDegreeRank(i);
                j = i;
                score = nowProScore;
            } else {
                emStat.setProgressDegreeRank(j);
            }
            if (StringUtils.equals(classId, emStat.getRangeId())) {
                classRank = emStat.getProgressDegreeRank();
            }
            i++;
        }
        return classRank;
    }

    /**
     * 获取该考试的上一场考试 在当前学期
     *
     * @param examInfo
     * @param examId   不能为空
     * @param unitId   不能为空
     * @return
     */
    public String getCompareId(EmExamInfo examInfo, String examId, String unitId) {
        if (examInfo == null)
            examInfo = emExamInfoService.findOne(examId);
        Map<String, EmStatObject> objectMap = EntityUtils.getMap(emStatObjectService.findByUnitId(unitId), EmStatObject::getExamId);
        EmStatObject object = null;
        //获取本学期的所有考试
        List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadAndGradeId(unitId, examInfo.getAcadyear(), examInfo.getSemester(), examInfo.getGradeCodes());
        String compareId = null;
        if (CollectionUtils.isNotEmpty(examList)) {
            List<EmExamInfo> lastExamList = new ArrayList<>();
            for (EmExamInfo emExamInfo : examList) {
                object = objectMap.get(emExamInfo.getId());
                if (object != null && "1".equals(object.getIsStat())) {
                    lastExamList.add(emExamInfo);
                }
            }
            Collections.sort(lastExamList, (o1, o2) -> {
                if (o1.getExamStartDate().compareTo(o2.getExamStartDate()) == 0) {
                    return o1.getCreationTime().compareTo(o2.getCreationTime());
                }
                return o1.getExamStartDate().compareTo(o2.getExamStartDate());
            });
            //获取上一场考试
            for (int i = 0; i < lastExamList.size(); i++) {
                if (examId.equals(lastExamList.get(i).getId())) {
                    if (i != 0) {
                        compareId = lastExamList.get(i - 1).getId();
                    }
                }
            }
        }
        return compareId;
    }

    public void setEmstatInfo(List<EmStat> statListClass1, String unitId, String examId, List<EmStat> statListClass2, List<EmStat> statListGrade1, List<EmStat> statListGrade2) {
        String[] stuIds = null;
        stuIds = statListClass1.stream().map(EmStat::getStudentId).collect(Collectors.toSet()).toArray(new String[]{});
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
//					if(o2.getProgressDegree()==null || o1.getProgressDegree()==null){
//						return 0;
//					}
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

    /**
     * 获取前后10名
     *
     * @param map
     * @param examScoreList
     * @param subScoreList
     */
    public void setOneFiveList(ModelMap map, List<EmStat> examScoreList, List<EmStat> subScoreList, boolean isClass) {
        //取出前10名即可
        EmStat stat = null;
        int rank = 0;
        if (CollectionUtils.isNotEmpty(examScoreList)) {
//			List<EmStat> oneExamScoreList=new ArrayList<>();
//			List<EmStat> fiveExamScoreList=new ArrayList<>();
//			List<EmStat> lastOneScoreList=new ArrayList<>();
//			List<EmStat> lastFiveScoreList=new ArrayList<>();
            List<EmStat> tenList = new ArrayList<>();
            List<EmStat> lastTenList = new ArrayList<>();
            int length = examScoreList.size();
            for (int i = 0; i < length; i++) {
                stat = examScoreList.get(i);
                rank = isClass ? stat.getClassRank() : stat.getGradeRank();
                if (rank <= 10) {
                    tenList.add(stat);
                } else if (i >= length - 10) {
                    lastTenList.add(stat);
                }
				/*if(i<=4){
					oneExamScoreList.add(stat);
				}else if(rank<=10){
					fiveExamScoreList.add(stat);
				}else if(i>=l-10 && i<l-5){
					lastOneScoreList.add(stat);
				}else if(i>=l-5){
					lastFiveScoreList.add(stat);
				}*/
            }
            int l = tenList.size();
            int oneL = l % 2 == 0 ? l / 2 : l / 2 + 1;
            int lastL = lastTenList.size();
            int lastOneL = lastL % 2 == 0 ? lastL / 2 : lastL / 2 + 1;
            map.put("oneExamScoreList", tenList.subList(0, oneL));
            map.put("fiveExamScoreList", tenList.subList(oneL, l));
            map.put("lastOneScoreList", lastTenList.subList(0, lastOneL));
            map.put("lastFiveScoreList", lastTenList.subList(lastOneL, lastL));
        }
        if (CollectionUtils.isNotEmpty(subScoreList)) {
//			List<EmStat> oneSubScoreList=new ArrayList<>();
//			List<EmStat> fiveSubScoreList=new ArrayList<>();
//			List<EmStat> lastOneSubScoreList=new ArrayList<>();
//			List<EmStat> lastFiveSubScoreList=new ArrayList<>();
            List<EmStat> tenList = new ArrayList<>();
            List<EmStat> lastTenList = new ArrayList<>();
            int length = subScoreList.size();
            for (int i = 0; i < length; i++) {
                stat = subScoreList.get(i);
                rank = isClass ? stat.getClassRank() : stat.getGradeRank();
                if (rank <= 10) {
                    tenList.add(stat);
                } else if (i >= length - 10) {
                    lastTenList.add(stat);
                }
				/*if(i<=4){
					oneSubScoreList.add(stat);
				}else if(rank<=10){
					fiveSubScoreList.add(stat);
				}else if(i>=l-10 && i<l-5){
					lastOneSubScoreList.add(stat);
				}else if(i>=l-5){
					lastFiveSubScoreList.add(stat);
				}*/
            }
            int l = tenList.size();
            int oneL = l % 2 == 0 ? l / 2 : l / 2 + 1;
            int lastL = lastTenList.size();
            int lastOneL = lastL % 2 == 0 ? lastL / 2 : lastL / 2 + 1;
            map.put("oneSubScoreList", tenList.subList(0, oneL));
            map.put("fiveSubScoreList", tenList.subList(oneL, l));
            map.put("lastOneSubScoreList", lastTenList.subList(0, lastOneL));
            map.put("lastFiveSubScoreList", lastTenList.subList(lastOneL, lastL));
        }
    }
}
