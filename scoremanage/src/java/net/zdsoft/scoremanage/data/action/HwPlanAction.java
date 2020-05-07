package net.zdsoft.scoremanage.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.remote.service.StudentAbnormalFlowRemoteService;
import net.zdsoft.scoremanage.data.constant.HwConstants;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.HwReinstateDto;
import net.zdsoft.scoremanage.data.dto.StudentFlowDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.HwPlan;
import net.zdsoft.scoremanage.data.entity.HwReinstate;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;
import net.zdsoft.scoremanage.data.entity.ResitScore;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.HwPlanService;
import net.zdsoft.scoremanage.data.service.HwReinstateService;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import net.zdsoft.scoremanage.data.service.ResitScoreService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author niuchao
 * @date 2019/11/5 13:42
 */
@Controller
@RequestMapping("/scoremanage/plan")
public class HwPlanAction extends BaseAction {

    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private HwPlanService hwPlanService;
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ScoreInfoService scoreInfoService;
    @Autowired
    private HwStatisService hwStatisService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private SubjectInfoService subjectInfoService;
    @Autowired
    private ResitScoreService resitScoreService;
    @Autowired
    private StudentAbnormalFlowRemoteService studentAbnormalFlowRemoteService;
    @Autowired
    private HwReinstateService hwReinstateService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;

    @RequestMapping("/index/page")
    @ControllerInfo("成绩方案首页")
    public String showIndex(ModelMap map){
        return "/scoremanage/plan/planIndex.ftl";
    }

    @RequestMapping("/head/page")
    @ControllerInfo("成绩方案头部")
    public String showHead(String acadyear, Integer semester, ModelMap map){
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        String unitId = getLoginInfo().getUnitId();
        Semester sem;
        if(StringUtils.isBlank(acadyear) && semester==null){
            sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        }else{
            sem = new Semester();
            sem.setAcadyear(acadyear);
            sem.setSemester(semester);
        }
        map.put("acadyearList", acadyearList);
        map.put("semester", sem);
        return "/scoremanage/plan/planHead.ftl";
    }

    @RequestMapping("/list/page")
    @ControllerInfo("成绩方案列表")
    public String showList(String acadyear, String semester, ModelMap map, HttpServletRequest request){
        Pagination page = createPagination();
        String unitId = getLoginInfo().getUnitId();
        List<HwPlan> hwPlanList = hwPlanService.findListByAcadyearAndSemester(unitId, acadyear, semester, page);
        map.put("planList", hwPlanList);
        sendPagination(request, map, page);
        return "/scoremanage/plan/planList.ftl";
    }

    @RequestMapping("/add/page")
    @ControllerInfo("添加成绩方案")
    public String addPlan(String acadyear, String semester, ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        List<ExamInfo> examInfoList = examInfoService.findExamInfoList(unitId, acadyear, semester);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("examList", examInfoList);
        return "/scoremanage/plan/planAdd.ftl";
    }

    @RequestMapping("/save")
    @ResponseBody
    @ControllerInfo("保存成绩方案")
    public String savePlan(String acadyear, String semester, String examId, String gradeId, String copyLast, ModelMap map){
        try {
            LoginInfo loginInfo = getLoginInfo();
            String unitId = loginInfo.getUnitId();
            List<String> studentIds;
            HwPlan hwPlan = null;
            if(Constant.IS_TRUE_Str.equals(copyLast)) {
                hwPlan = hwPlanService.findLastOne(unitId, acadyear, semester, gradeId, examId);
            }
            List<HwStatis> hwStatisList = null;
            List<Student> studentList = null;
            if(hwPlan!=null){
                hwStatisList = hwStatisService.findListByPlanId(unitId, hwPlan.getId(), HwConstants.PLAN_TYPE_1, true, null);
                studentIds = EntityUtils.getList(hwStatisList, HwStatis::getStudentId);
            }else{
                studentList = SUtils.dt(studentRemoteService.findByGradeId(gradeId), Student.class);
                studentIds = EntityUtils.getList(studentList, Student::getId);
            }
            //复学学生信息
            List<HwReinstate> reinstateList = hwReinstateService.findListByGradeIdAndExamId(unitId, acadyear, semester, gradeId, examId);
            List<HwStatis> oldStatisList = new ArrayList<>();
            List<String> oldStudentIds = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(reinstateList)){
                oldStudentIds = EntityUtils.getList(reinstateList, HwReinstate::getStudentId);
                Map<String, List<String>> oldPlanStuMap = EntityUtils.getListMap(reinstateList, e -> e.getUnitId() + e.getOldAcadyear() + e.getSemester() + e.getOldGradeId() + e.getOldExamId(), e->e.getStudentId());
                List<HwPlan> oldPlanList = hwPlanService.findLastListByOldConditions(oldPlanStuMap.keySet().toArray(new String[oldPlanStuMap.keySet().size()]));
                if(CollectionUtils.isNotEmpty(oldPlanList)){
                    List<String> statisConditions = new ArrayList<>();
                    for (HwPlan oldPlan : oldPlanList) {
                        List<String> stuIds = oldPlanStuMap.get(oldPlan.getUnitId() + oldPlan.getAcadyear() + oldPlan.getSemester() + oldPlan.getGradeId() + oldPlan.getExamId());
                        stuIds.forEach(e->{
                            statisConditions.add(oldPlan.getId()+e);
                        });
                    }
                    oldStatisList = hwStatisService.findListByConditions(unitId, HwConstants.PLAN_TYPE_1, statisConditions);
                }
            }

            //考试信息
            ExamInfo exam = examInfoService.findOne(examId);
            boolean isFinal = ScoreDataConstants.EXAM_TYPE_FINAL.equals(exam.getExamType());
            //年级信息
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
            //考试科目信息
            List<SubjectInfo> subjectInfoList = subjectInfoService.findByExamIdGradeId(examId, gradeId);
            //等第科目
            List<SubjectInfo> gradeSubject = subjectInfoList.stream()
                    .filter(e->ScoreDataConstants.ACHI_GRADE.equals(e.getInputType())).collect(Collectors.toList());
            Map<String, String> gradeTypeMap = gradeSubject.stream().collect(Collectors.toMap(e -> e.getSubjectId(), e -> e.getGradeType()));
            Map<String, Map<String, McodeDetail>> typeNameMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(gradeSubject)){
                Set<String> mcodeIds = gradeSubject.stream().map(e->e.getGradeType()).collect(Collectors.toSet());
                typeNameMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(mcodeIds.toArray(new String[mcodeIds.size()])),
                        new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                        });
            }
            List<String> gradeSubjectIds = gradeSubject.stream().map(SubjectInfo::getSubjectId).collect(Collectors.toList());
            //是否全是等第
            boolean isGrade  = gradeSubjectIds.size()==subjectInfoList.size();
            //成绩信息
            List<ScoreInfo> scoreInfoList = scoreInfoService.findBystudentIds(unitId, acadyear, semester, studentIds.toArray(new String[studentIds.size()]), examId);
            Map<String, ScoreInfo> scoreInfoMap = EntityUtils.getMap(scoreInfoList, e -> e.getStudentId() + e.getSubjectId());
            //7选3选考科目
            List<Course> xkList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, HwConstants.SUBJECT_73), Course.class);
            List<String> xkIds = EntityUtils.getList(xkList, Course::getId);
            //副课科目
            List<Course> minorList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, HwConstants.SUBJECT_MINOR), Course.class);
            List<String> minorIds = EntityUtils.getList(minorList, Course::getId);
            //补考分数
            Map<String, String> resitScoreMap = new HashMap<>();
            if(isFinal){
                List<ResitScore> resitScoreList = resitScoreService.listResitScoreBy(unitId, examId);
                resitScoreList = resitScoreList.stream().filter(e->StringUtils.isNotBlank(e.getScore())).collect(Collectors.toList());
                resitScoreMap = EntityUtils.getMap(resitScoreList, e -> e.getStudentId() + e.getSubjectId(), e -> e.getScore());
            }
            //原始平均分和总评平均分
            Map<String, Double> scoreAveMap = new HashMap<>();
            Map<String, Double> toScoreAveMap = new HashMap<>();
            if(!isGrade){
                Map<String, List<ScoreInfo>> scoreInfoListMap = scoreInfoList.stream()
                        .filter(e -> xkIds.contains(e.getSubjectId()) && !gradeSubjectIds.contains(e.getSubjectId()))
                        .collect(Collectors.groupingBy(ScoreInfo::getSubjectId));
                scoreInfoListMap.entrySet().forEach(x-> {
                    Double ave = x.getValue().stream().filter(y -> StringUtils.isNotBlank(y.getScore())).collect(Collectors.averagingDouble(y -> Double.parseDouble(y.getScore())));
                    scoreAveMap.put(x.getKey(), ave);
                });
            }
            if(isFinal){
                Map<String, List<ScoreInfo>> scoreInfoListMap = scoreInfoList.stream()
                        .filter(e -> xkIds.contains(e.getSubjectId()))
                        .collect(Collectors.groupingBy(ScoreInfo::getSubjectId));
                scoreInfoListMap.entrySet().forEach(x-> {
                    Double ave = x.getValue().stream().filter(y -> StringUtils.isNotBlank(y.getToScore())).collect(Collectors.averagingDouble(y -> Double.parseDouble(y.getToScore())));
                    toScoreAveMap.put(x.getKey(), ave);
                });
            }

            HwPlan addPlan = new HwPlan();
            addPlan.setId(UuidUtils.generateUuid());
            addPlan.setUnitId(unitId);
            addPlan.setAcadyear(acadyear);
            addPlan.setSemester(semester);
            addPlan.setGradeId(gradeId);
            addPlan.setGradeName(grade.getGradeName());
            addPlan.setExamId(examId);
            addPlan.setExamCode(exam.getExamCode());
            addPlan.setExamName(exam.getExamName());
            addPlan.setCreationTime(new Date());
            addPlan.setModifyTime(new Date());
            addPlan.setOperator(loginInfo.getUserId());
            addPlan.setIsDeleted(Constant.IS_DELETED_FALSE);
            List<HwStatis> addStatisList = new ArrayList<>();
            List<HwStatisEx> addStatisExList = new ArrayList<>();
            List<HwStatisEx> scoreRankList = new ArrayList<>();//用于原始总分排名
            List<HwStatisEx> toScoreRankList = new ArrayList<>();//用于总评总分排名
            if(hwPlan!=null){
                List<String> currentSubjectIds = hwStatisList.get(0).getExList1().stream().map(HwStatisEx::getObjectId).collect(Collectors.toList());
                if(!gradeSubjectIds.containsAll(currentSubjectIds)){
                    isGrade = false;
                }
                for (HwStatis hwStatis : hwStatisList) {
                    if(oldStudentIds.contains(hwStatis.getStudentId())){
                        continue;
                    }
                    hwStatis.setId(UuidUtils.generateUuid());
                    hwStatis.setHwPlanId(addPlan.getId());
                    for (HwStatisEx hwStatisEx : hwStatis.getExList1()) {
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setHwStatisId(hwStatis.getId());
                        hwStatisEx.setScore(null);//重置原始分
                        hwStatisEx.setConvertScore(null);//重置原始转换分
                        hwStatisEx.setMakeScore(null);//重置补考分
                        hwStatisEx.setTotalScore(null);//重置总评分
                        hwStatisEx.setTotalConvertScore(null);//重置总评转换分
                        String key = hwStatis.getStudentId()+hwStatisEx.getObjectId();
                        makeData(isFinal, scoreInfoMap, xkIds, resitScoreMap, scoreAveMap, toScoreAveMap, hwStatisEx, key, gradeTypeMap, typeNameMap);
                        addStatisExList.add(hwStatisEx);
                    }
                    for (HwStatisEx hwStatisEx : hwStatis.getExList2()) {
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setHwStatisId(hwStatis.getId());
                        hwStatisEx.setClassId(hwStatis.getClassId());
                        if(HwConstants.OBJECT_ID_1.equals(hwStatisEx.getObjectId())){
                            if(!isGrade){
                                Double totalScore = hwStatis.getExList1().stream().filter(e -> !minorIds.contains(e.getObjectId()) && StringUtils.isNotBlank(e.getConvertScore()))
                                        .collect(Collectors.summingDouble(e -> Double.parseDouble(e.getConvertScore())));
                                hwStatisEx.setObjectVal(String.format("%.2f", totalScore));
                                scoreRankList.add(hwStatisEx);
                                addStatisExList.add(hwStatisEx);
                            }
                        }else if(HwConstants.OBJECT_ID_2.equals(hwStatisEx.getObjectId())){
                            if(isFinal) {
                                Double totalScore = hwStatis.getExList1().stream().filter(e -> !minorIds.contains(e.getObjectId()) && StringUtils.isNotBlank(e.getTotalConvertScore()))
                                        .collect(Collectors.summingDouble(e -> Double.parseDouble(e.getTotalConvertScore())));
                                hwStatisEx.setObjectVal(String.format("%.2f", totalScore));
                                toScoreRankList.add(hwStatisEx);
                                addStatisExList.add(hwStatisEx);
                            }
                        }else if(HwConstants.OBJECT_ID_7.equals(hwStatisEx.getObjectId())
                                || HwConstants.OBJECT_ID_8.equals(hwStatisEx.getObjectId())){
                            addStatisExList.add(hwStatisEx);
                        }
                    }
                    addStatisList.add(hwStatis);
                }
            }else{
                List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getList(subjectInfoList, SubjectInfo::getSubjectId).toArray(new String[0])),Course.class);
                Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
                List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
                Map<String, String> classNameMap = EntityUtils.getMap(classList, Clazz::getId, Clazz::getClassNameDynamic);
                Map<String, String> classCodeMap = EntityUtils.getMap(classList, Clazz::getId, Clazz::getClassCode);
                Map<String, Long> claStuNumMap = studentList.stream().collect(Collectors.groupingBy(e -> e.getClassId(), Collectors.counting()));
                HwStatis hwStatis = null;
                HwStatisEx hwStatisEx = null;
                for (Student student : studentList) {
                    if(oldStudentIds.contains(student.getId())){
                        continue;
                    }
                    hwStatis = new HwStatis();
                    hwStatis.setId(UuidUtils.generateUuid());
                    hwStatis.setUnitId(unitId);
                    hwStatis.setHwPlanId(addPlan.getId());
                    hwStatis.setPlanType(HwConstants.PLAN_TYPE_1);
                    hwStatis.setStudentId(student.getId());
                    hwStatis.setStuName(student.getStudentName());
                    hwStatis.setStuCode(student.getStudentCode());
                    hwStatis.setClassId(student.getClassId());
                    hwStatis.setClaCode(classCodeMap.get(student.getClassId()));
                    hwStatis.setClaName(classNameMap.get(student.getClassId()));
                    hwStatis.setGradeId(gradeId);
                    hwStatis.setGradeName(grade.getGradeName());
                    for (SubjectInfo subjectInfo : subjectInfoList) {
                        hwStatisEx = new HwStatisEx();
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setUnitId(unitId);
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setPlanType(HwConstants.PLAN_TYPE_1);
                        hwStatisEx.setHwStatisId(hwStatis.getId());
                        hwStatisEx.setObjectType(HwConstants.OBJECT_TYPE_1);
                        hwStatisEx.setObjectId(subjectInfo.getSubjectId());
                        hwStatisEx.setObjectName(courseNameMap.get(subjectInfo.getSubjectId()));
                        String key = hwStatis.getStudentId()+hwStatisEx.getObjectId();
                        makeData(isFinal, scoreInfoMap, xkIds, resitScoreMap, scoreAveMap, toScoreAveMap, hwStatisEx, key, gradeTypeMap, typeNameMap);
                        hwStatis.getExList1().add(hwStatisEx);
                        addStatisExList.add(hwStatisEx);
                    }
                    for (Map.Entry<String, String> entry : HwConstants.idNameMap.entrySet()) {
                        hwStatisEx = new HwStatisEx();
                        if(HwConstants.OBJECT_ID_1.equals(entry.getKey())){
                            if(!isGrade) {
                                Double totalScore = hwStatis.getExList1().stream().filter(e -> !minorIds.contains(e.getObjectId()) && StringUtils.isNotBlank(e.getConvertScore()))
                                        .collect(Collectors.summingDouble(e -> Double.parseDouble(e.getConvertScore())));
                                hwStatisEx.setObjectVal(String.format("%.2f", totalScore));
                                scoreRankList.add(hwStatisEx);
                            }
                        }else if(HwConstants.OBJECT_ID_2.equals(entry.getKey())){
                            if(isFinal) {
                                Double totalScore = hwStatis.getExList1().stream().filter(e -> !minorIds.contains(e.getObjectId()) && StringUtils.isNotBlank(e.getTotalConvertScore()))
                                        .collect(Collectors.summingDouble(e -> Double.parseDouble(e.getTotalConvertScore())));
                                hwStatisEx.setObjectVal(String.format("%.2f", totalScore));
                                toScoreRankList.add(hwStatisEx);
                            }
                        }else if(HwConstants.OBJECT_ID_7.equals(entry.getKey())){
                            hwStatisEx.setObjectVal(String.valueOf(claStuNumMap.get(student.getClassId())));
                        }else if(HwConstants.OBJECT_ID_8.equals(entry.getKey())){
                            hwStatisEx.setObjectVal(String.valueOf(studentList.size()));
                        }else{
                            continue;
                        }
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setUnitId(unitId);
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setPlanType(HwConstants.PLAN_TYPE_1);
                        hwStatisEx.setHwStatisId(hwStatis.getId());
                        hwStatisEx.setObjectType(HwConstants.OBJECT_TYPE_2);
                        hwStatisEx.setObjectId(entry.getKey());
                        hwStatisEx.setObjectName(entry.getValue());
                        hwStatisEx.setClassId(hwStatis.getClassId());
                        addStatisExList.add(hwStatisEx);
                    }
                    addStatisList.add(hwStatis);
                }
            }

            if(!isGrade){
                //原始总分年级排名
                List<Map.Entry<String, List<HwStatisEx>>> tempList = scoreRankList.stream()
                        .collect(Collectors.groupingBy(HwStatisEx::getObjectVal)).entrySet()
                        .stream().sorted((s1, s2) -> -Float.compare(Float.parseFloat(s1.getKey()), Float.parseFloat(s2.getKey())))
                        .collect(Collectors.toList());
                int index = 1;
                for (Map.Entry<String, List<HwStatisEx>> entry : tempList) {
                    final int i = index;
                    entry.getValue().forEach(e->{
                        HwStatisEx hwStatisEx = new HwStatisEx();
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setUnitId(unitId);
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setPlanType(HwConstants.PLAN_TYPE_1);
                        hwStatisEx.setHwStatisId(e.getHwStatisId());
                        hwStatisEx.setObjectType(HwConstants.OBJECT_TYPE_2);
                        hwStatisEx.setObjectId(HwConstants.OBJECT_ID_4);
                        hwStatisEx.setObjectName(HwConstants.idNameMap.get(HwConstants.OBJECT_ID_4));
                        hwStatisEx.setObjectVal(String.valueOf(i));
                        addStatisExList.add(hwStatisEx);
                    });
                    index = index+entry.getValue().size();
                }
                //原始总分班级排名
                Map<String, List<HwStatisEx>> tempMap = scoreRankList.stream().collect(Collectors.groupingBy(HwStatisEx::getClassId));
                for (List<HwStatisEx> item2 : tempMap.values()) {
                    tempList = item2.stream()
                            .collect(Collectors.groupingBy(HwStatisEx::getObjectVal)).entrySet()
                            .stream().sorted((s1, s2) -> -Float.compare(Float.parseFloat(s1.getKey()), Float.parseFloat(s2.getKey())))
                            .collect(Collectors.toList());
                    index = 1;
                    for (Map.Entry<String, List<HwStatisEx>> entry : tempList) {
                        final int i = index;
                        entry.getValue().forEach(e->{
                            HwStatisEx hwStatisEx = new HwStatisEx();
                            hwStatisEx.setId(UuidUtils.generateUuid());
                            hwStatisEx.setUnitId(unitId);
                            hwStatisEx.setHwPlanId(addPlan.getId());
                            hwStatisEx.setPlanType(HwConstants.PLAN_TYPE_1);
                            hwStatisEx.setHwStatisId(e.getHwStatisId());
                            hwStatisEx.setObjectType(HwConstants.OBJECT_TYPE_2);
                            hwStatisEx.setObjectId(HwConstants.OBJECT_ID_3);
                            hwStatisEx.setObjectName(HwConstants.idNameMap.get(HwConstants.OBJECT_ID_3));
                            hwStatisEx.setObjectVal(String.valueOf(i));
                            addStatisExList.add(hwStatisEx);
                        });
                        index = index+entry.getValue().size();
                    }
                }
            }
            if(isFinal){
                //总评总分年级排名
                List<Map.Entry<String, List<HwStatisEx>>> tempList = toScoreRankList.stream()
                        .collect(Collectors.groupingBy(HwStatisEx::getObjectVal)).entrySet()
                        .stream().sorted((s1, s2) -> -Float.compare(Float.parseFloat(s1.getKey()), Float.parseFloat(s2.getKey())))
                        .collect(Collectors.toList());
                int index = 1;
                for (Map.Entry<String, List<HwStatisEx>> entry : tempList) {
                    final int i = index;
                    entry.getValue().forEach(e->{
                        HwStatisEx hwStatisEx = new HwStatisEx();
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setUnitId(unitId);
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setPlanType(HwConstants.PLAN_TYPE_1);
                        hwStatisEx.setHwStatisId(e.getHwStatisId());
                        hwStatisEx.setObjectType(HwConstants.OBJECT_TYPE_2);
                        hwStatisEx.setObjectId(HwConstants.OBJECT_ID_6);
                        hwStatisEx.setObjectName(HwConstants.idNameMap.get(HwConstants.OBJECT_ID_6));
                        hwStatisEx.setObjectVal(String.valueOf(i));
                        addStatisExList.add(hwStatisEx);
                    });
                    index = index+entry.getValue().size();
                }
                //总评总分班级排名
                Map<String, List<HwStatisEx>> tempMap = toScoreRankList.stream().collect(Collectors.groupingBy(HwStatisEx::getClassId));
                for (List<HwStatisEx> item2 : tempMap.values()) {
                    tempList = item2.stream()
                            .filter(e-> StringUtils.isNotBlank(e.getObjectVal()))
                            .collect(Collectors.groupingBy(HwStatisEx::getObjectVal)).entrySet()
                            .stream().sorted((s1, s2) -> -Float.compare(Float.parseFloat(s1.getKey()), Float.parseFloat(s2.getKey())))
                            .collect(Collectors.toList());
                    index = 1;
                    for (Map.Entry<String, List<HwStatisEx>> entry : tempList) {
                        final int i = index;
                        entry.getValue().forEach(e->{
                            HwStatisEx hwStatisEx = new HwStatisEx();
                            hwStatisEx.setId(UuidUtils.generateUuid());
                            hwStatisEx.setUnitId(unitId);
                            hwStatisEx.setHwPlanId(addPlan.getId());
                            hwStatisEx.setPlanType(HwConstants.PLAN_TYPE_1);
                            hwStatisEx.setHwStatisId(e.getHwStatisId());
                            hwStatisEx.setObjectType(HwConstants.OBJECT_TYPE_2);
                            hwStatisEx.setObjectId(HwConstants.OBJECT_ID_5);
                            hwStatisEx.setObjectName(HwConstants.idNameMap.get(HwConstants.OBJECT_ID_5));
                            hwStatisEx.setObjectVal(String.valueOf(i));
                            addStatisExList.add(hwStatisEx);
                        });
                        index = index+entry.getValue().size();
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(oldStatisList)){
                for (HwStatis statis : oldStatisList) {
                    statis.setId(UuidUtils.generateUuid());
                    statis.setHwPlanId(addPlan.getId());
                    statis.setStuName(statis.getStuName()+"(复学)");
                    for (HwStatisEx hwStatisEx : statis.getExList1()) {
                        hwStatisEx.setId(UuidUtils.generateUuid());
                        hwStatisEx.setHwPlanId(addPlan.getId());
                        hwStatisEx.setHwStatisId(statis.getId());
                        addStatisExList.add(hwStatisEx);
                    }
                    addStatisList.add(statis);
                }
            }
            hwPlanService.savePlan(Arrays.asList(addPlan), addStatisList, addStatisExList);
        }catch (Exception e){
            e.printStackTrace();
            return error("保存失败");
        }
        return success("保存并统计成功");
    }

    /**
     * 组装成绩数据(用于保存)
     * @param isFinal
     * @param scoreInfoMap
     * @param xkIds
     * @param resitScoreMap
     * @param scoreAveMap
     * @param toScoreAveMap
     * @param hwStatisEx
     * @param key
     */
    private void makeData(boolean isFinal, Map<String, ScoreInfo> scoreInfoMap, List<String> xkIds,
                          Map<String, String> resitScoreMap, Map<String, Double> scoreAveMap,
                          Map<String, Double> toScoreAveMap, HwStatisEx hwStatisEx, String key,
                          Map<String, String> gradeTypeMap, Map<String, Map<String, McodeDetail>> typeNameMap) {
        if(scoreInfoMap.containsKey(key)){
            ScoreInfo scoreInfo = scoreInfoMap.get(key);
            hwStatisEx.setScore(scoreInfo.getScore());//原始分
            //如果是分数类型且原始分不为空则补充转换分
            if(StringUtils.isNotBlank(scoreInfo.getScore())){
                if(ScoreDataConstants.ACHI_SCORE.equals(scoreInfo.getInputType())){
                    if(xkIds.contains(hwStatisEx.getObjectId())){
                        hwStatisEx.setConvertScore(convertScore(scoreInfo.getScore(), scoreAveMap.get(hwStatisEx.getObjectId())));
                    }else{
                        hwStatisEx.setConvertScore(scoreInfo.getScore());
                    }
                }else{
                    if(gradeTypeMap.containsKey(scoreInfo.getSubjectId())){
                        String gradeType = gradeTypeMap.get(scoreInfo.getSubjectId());
                        if(typeNameMap.containsKey(gradeType)){
                            Map<String, McodeDetail> detailMap = typeNameMap.get(gradeType);
                            if(detailMap.containsKey(scoreInfo.getScore())){
                                hwStatisEx.setScore(detailMap.get(scoreInfo.getScore()).getMcodeContent());
                            }
                        }
                    }
                }
            }
            //如果是期末考试且总评分不为空补充总评分
            if( isFinal && StringUtils.isNotBlank(scoreInfo.getToScore())){
                hwStatisEx.setTotalScore(scoreInfo.getToScore());
                if(xkIds.contains(hwStatisEx.getObjectId())) {
                    hwStatisEx.setTotalConvertScore(convertScore(scoreInfo.getToScore(), toScoreAveMap.get(hwStatisEx.getObjectId())));
                }else{
                    hwStatisEx.setTotalConvertScore(scoreInfo.getToScore());
                }
            }
        }
        if(isFinal && resitScoreMap.containsKey(key)){
            hwStatisEx.setMakeScore(resitScoreMap.get(key));
        }
    }

    /**
     * 转换分数
     * @param score
     * @param ave
     * @return
     */
    private String convertScore(String score, Double ave){
        double convertScore = (Float.parseFloat(score)-ave)*25/(100-ave)+75;
        return String.format("%.2f", convertScore);
    }

    @RequestMapping("/getGradeList")
    @ResponseBody
    @ControllerInfo("根据考试获取年级")
    public List<Grade> getGradeList(String examId){
        ExamInfo exam = examInfoService.findOne(examId);
        List<String> gradeCodeList = Arrays.asList(exam.getRanges().split(","));
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(exam.getUnitId(), exam.getAcadyear()), Grade.class);
        Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1,exam.getUnitId()), Semester.class);
        gradeList = gradeList.stream().filter(e->gradeCodeList.contains(e.getGradeCode())).collect(Collectors.toList());
        if(sem.getAcadyear().equals(exam.getAcadyear())){//防止当前学年未升学，取真实年级
            List<String> gradeIdList = EntityUtils.getList(gradeList, Grade::getId);
            gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIdList.toArray(new String[gradeIdList.size()])), Grade.class);
        }
        return gradeList;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ControllerInfo("删除成绩方案")
    public String deletePlan(String planId, ModelMap map){
        try {
            LoginInfo loginInfo = getLoginInfo();
            hwPlanService.deletePlan(loginInfo.getUnitId(), planId, loginInfo.getUserId());
        }catch (Exception e){
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/detail/page")
    @ControllerInfo("查看成绩详情")
    public String showDetail(String planId, ModelMap map, HttpServletRequest request){
        Pagination page = createPagination();
        HwPlan plan = hwPlanService.findOne(planId);
        List<String> nameList = new ArrayList<>();
        List<Map<String, String>> detailList = new ArrayList<>();
        getData(plan, nameList, detailList, page);
        map.put("nameList", nameList);
        map.put("detailList", detailList);
        map.put("planId", planId);
        map.put("acadyear", plan.getAcadyear());
        map.put("semester", plan.getSemester());
        sendPagination(request, map, page);
        return "/scoremanage/plan/planDetail.ftl";
    }

    /**
     * 组装成绩数据(用于展示和导出)
     * @param plan
     * @param nameList
     * @param detailList
     * @param page
     */
    private void getData(HwPlan plan,List<String> nameList, List<Map<String, String>> detailList, Pagination page) {
        String unitId = getLoginInfo().getUnitId();
        //7选3选考科目
        List<Course> xkList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, HwConstants.SUBJECT_73), Course.class);
        List<String> xkIds = EntityUtils.getList(xkList, Course::getId);
        ExamInfo exam = examInfoService.findOne(plan.getExamId());
        boolean isFinal = ScoreDataConstants.EXAM_TYPE_FINAL.equals(exam.getExamType());
        nameList.add("序号");
        nameList.add("班级");
        nameList.add("学生");
        nameList.add("学号");

        List<HwStatis> hwStatisList = hwStatisService.findListByPlanId(unitId, plan.getId(), HwConstants.PLAN_TYPE_1, true, page);
        LinkedHashMap<String, String> subjNameMap = new LinkedHashMap<>();
        for (HwStatis statis : hwStatisList) {
            for (HwStatisEx statisEx : statis.getExList1()) {
                if(!subjNameMap.containsKey(statisEx.getObjectId())){
                    subjNameMap.put(statisEx.getObjectId(), statisEx.getObjectName());
                }
            }
        }
        subjNameMap.entrySet().forEach(e->{
            nameList.add(e.getValue()+"(考试)");
            if(xkIds.contains(e.getKey())){
                nameList.add(e.getValue()+"(考试0.75)");
            }
            if(isFinal){
                nameList.add(e.getValue()+"(总评)");
                if(xkIds.contains(e.getKey())){
                    nameList.add(e.getValue()+"(总评0.75)");
                }
                nameList.add(e.getValue()+"(补考)");
            }
        });
        if(isFinal){
            nameList.addAll(HwConstants.idNameMap.values());
        }else{
            nameList.addAll(HwConstants.idNameMap2.values());
        }

        int i = 0;
        Map<String, String> detailMap = null;
        for (HwStatis hwStatis : hwStatisList) {
            i++;
            detailMap = new HashMap<>();
            detailMap.put(nameList.get(0), String.valueOf(i));
            detailMap.put(nameList.get(1), hwStatis.getClaName());
            detailMap.put(nameList.get(2), hwStatis.getStuName());
            detailMap.put(nameList.get(3), hwStatis.getStuCode());

            for (HwStatisEx hwStatisEx : hwStatis.getExList1()) {
                detailMap.put(hwStatisEx.getObjectName() + "(考试)", hwStatisEx.getScore());
                if(xkIds.contains(hwStatisEx.getObjectId())){
                    detailMap.put(hwStatisEx.getObjectName() + "(考试0.75)", hwStatisEx.getConvertScore());
                }
                if (isFinal){
                    detailMap.put(hwStatisEx.getObjectName() + "(总评)", hwStatisEx.getTotalScore());
                    if(xkIds.contains(hwStatisEx.getObjectId())){
                        detailMap.put(hwStatisEx.getObjectName() + "(总评0.75)", hwStatisEx.getTotalConvertScore());
                    }
                    detailMap.put(hwStatisEx.getObjectName()+"(补考)", hwStatisEx.getMakeScore());
                }
            }

            for (HwStatisEx hwStatisEx : hwStatis.getExList2()) {
                detailMap.put(hwStatisEx.getObjectName(), hwStatisEx.getObjectVal());
            }
            detailList.add(detailMap);
        }
    }

    @RequestMapping("/export")
    @ResponseBody
    @ControllerInfo("导出成绩Excel")
    public String doExport(String planId, ModelMap map, HttpServletResponse response){
        try {
            HwPlan plan = hwPlanService.findOne(planId);
            String name = plan.getExamName()+"-"+plan.getGradeName()+"("+
                    DateUtils.date2String(plan.getCreationTime(), "yyyy年MM月dd日HH时mm分ss秒")+")";
            List<String> nameList = new ArrayList<>();
            List<Map<String, String>> detailList = new ArrayList<>();
            getData(plan, nameList, detailList, null);
            Map<String, List<String>> fieldTitleMap = new HashMap<>();
            fieldTitleMap.put("成绩方案详情表", nameList);
            Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String,List<Map<String,String>>>();
            sheetName2RecordListMap.put("成绩方案详情表", detailList);
            ExportUtils exportUtils = ExportUtils.newInstance();
            exportUtils.exportXLSFile(name, fieldTitleMap, sheetName2RecordListMap, response);
        } catch (Exception e) {
            e.printStackTrace();
            return error("导出失败");
        }
        return success("导出完成");
    }

    @RequestMapping("/makeScore")
    @ResponseBody
    @ControllerInfo("获取补考成绩")
    public String makeScore(String acadyear, String semester, ModelMap map){
        try {
            LoginInfo loginInfo = getLoginInfo();
            String unitId = loginInfo.getUnitId();
            List<ExamInfo> examInfoList = examInfoService.findExamInfoList(unitId, acadyear, semester);
            examInfoList = examInfoList.stream().filter(e->ScoreDataConstants.EXAM_TYPE_FINAL.equals(e.getExamType())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(examInfoList)){
                List<String> examIds = EntityUtils.getList(examInfoList, ExamInfo::getId);
                List<HwPlan> planList = hwPlanService.findListByExamIds(unitId, acadyear, semester, examIds);
                if(CollectionUtils.isNotEmpty(planList)){
                    Set<String> existsIds = EntityUtils.getSet(planList, HwPlan::getExamId);
                    examIds = examIds.stream().filter(e -> existsIds.contains(e)).collect(Collectors.toList());
                    List<ResitScore> resitScoreList = resitScoreService.findListByExamIds(unitId, examIds.toArray(new String[examIds.size()]));
                    if(CollectionUtils.isNotEmpty(resitScoreList)){
                        Map<String, String> resitScoreMap = resitScoreList.stream().filter(e->StringUtils.isNotBlank(e.getScore()))
                                .collect(Collectors.toMap(e -> e.getExamId() + e.getStudentId() + e.getSubjectId(), e -> e.getScore()));
                        Set<String> existsIds2 = EntityUtils.getSet(resitScoreList, ResitScore::getExamId);
                        planList = planList.stream().filter(e->existsIds2.contains(e.getExamId())).collect(Collectors.toList());

                        List<String> planIdList = EntityUtils.getList(planList, HwPlan::getId);
                        List<HwStatis> statisList = hwStatisService.findListByPlanIds(unitId, planIdList.toArray(new String[0]), HwConstants.PLAN_TYPE_1);
                        Map<String, List<HwStatis>> statisListMap = statisList.stream().collect(Collectors.groupingBy(HwStatis::getHwPlanId));

                        List<HwPlan> addPlanList = new ArrayList<>();
                        List<HwStatis> addStatisList = new ArrayList<>();
                        List<HwStatisEx> addStatisExList = new ArrayList<>();
                        for (HwPlan hwPlan : planList) {
                            String oldPlanId = hwPlan.getId();
                            hwPlan.setId(UuidUtils.generateUuid());
                            hwPlan.setCreationTime(new Date());
                            hwPlan.setModifyTime(new Date());
                            hwPlan.setOperator(loginInfo.getUserId());
                            List<HwStatis> inStatisList = statisListMap.get(oldPlanId);
                            for (HwStatis hwStatis : inStatisList) {
                                hwStatis.setId(UuidUtils.generateUuid());
                                hwStatis.setHwPlanId(hwPlan.getId());
                                for (HwStatisEx hwStatisEx : hwStatis.getExList1()) {
                                    hwStatisEx.setId(UuidUtils.generateUuid());
                                    hwStatisEx.setHwPlanId(hwPlan.getId());
                                    hwStatisEx.setHwStatisId(hwStatis.getId());
                                    if(resitScoreMap.containsKey(hwPlan.getExamId()+hwStatis.getStudentId()+hwStatisEx.getObjectId())){
                                        hwStatisEx.setMakeScore(resitScoreMap.get(hwPlan.getExamId()+hwStatis.getStudentId()+hwStatisEx.getObjectId()));
                                    }else{
                                        hwStatisEx.setMakeScore(null);
                                    }
                                    addStatisExList.add(hwStatisEx);
                                }
                                for (HwStatisEx hwStatisEx : hwStatis.getExList2()) {
                                    hwStatisEx.setId(UuidUtils.generateUuid());
                                    hwStatisEx.setHwPlanId(hwPlan.getId());
                                    hwStatisEx.setHwStatisId(hwStatis.getId());
                                    addStatisExList.add(hwStatisEx);
                                }
                                addStatisList.add(hwStatis);
                            }
                            addPlanList.add(hwPlan);
                        }
                        hwPlanService.savePlan(addPlanList, addStatisList, addStatisExList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/reStudent/list/page")
    @ControllerInfo("复学学生列表")
    public String reStudentList(String acadyear, String semester, ModelMap map, HttpServletResponse response){
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<StudentFlowDto> dtoList = SUtils.dt(studentAbnormalFlowRemoteService.findAbnormalFlowStudent(unitId, acadyear, semester, HwConstants.FLOW_TYPES),StudentFlowDto.class);
        dtoList = dtoList.stream().
                filter(e->StringUtils.isNotBlank(e.getCurrentGradeId())&&StringUtils.isNotBlank(e.getSourceGradeId())
                        &&!e.getSourceGradeId().equals(e.getCurrentGradeId()))
                .collect(Collectors.toList());
        List<String> studentIdList = new ArrayList<>();
        Set<String> gradeIdSet = new HashSet<>();
        dtoList.forEach(e->{
            studentIdList.add(e.getStudentId());
            gradeIdSet.add(e.getCurrentGradeId());
        });
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdList.toArray(new String[studentIdList.size()])), Student.class);
        Map<String, Student> studentMap = EntityUtils.getMap(studentList, Student::getId, Function.identity());
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIdSet.toArray(new String[gradeIdSet.size()])), Grade.class);
        Map<String, String> gradeNameMap = EntityUtils.getMap(gradeList, Grade::getId, Grade::getGradeName);

        for (StudentFlowDto dto : dtoList) {
            dto.setOldGradeId(dto.getSourceGradeId());
            dto.setGradeId(dto.getCurrentGradeId());
            if(studentMap.containsKey(dto.getStudentId())){
                dto.setStudentName(studentMap.get(dto.getStudentId()).getStudentName());
                dto.setIdentityCard(studentMap.get(dto.getStudentId()).getIdentityCard());
            }
            if(gradeNameMap.containsKey(dto.getGradeId())){
                dto.setGradeName(gradeNameMap.get(dto.getGradeId()));
            }
        }

        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("dtoList", dtoList);
        return "/scoremanage/plan/reStudentList.ftl";
    }

    @RequestMapping("/reinstate/list/page")
    @ControllerInfo("复学学生考试插入方案列表")
    public String reinstateList(ModelMap map, HttpServletRequest request, HttpServletResponse response){
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String studentId = request.getParameter("studentId");
        String studentName = request.getParameter("studentName");
        String identityCard = request.getParameter("identityCard");
        String oldGradeId = request.getParameter("oldGradeId");
        String gradeId = request.getParameter("gradeId");
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<HwReinstate> reinstateList = hwReinstateService.findListByStudentId(unitId, acadyear, semester, studentId);
        if(CollectionUtils.isNotEmpty(reinstateList)){
            Set<String> examIds = new HashSet<>();
            reinstateList.forEach(e->{
                examIds.add(e.getOldExamId());
                examIds.add(e.getExamId());
            });
            List<ExamInfo> examInfoList = examInfoService.findListByIdIn(examIds.toArray(new String[examIds.size()]));
            Map<String, String> examNameMap = EntityUtils.getMap(examInfoList, ExamInfo::getId, ExamInfo::getExamName);
            List<HwReinstateDto> dtoList = new ArrayList<>();
            HwReinstateDto dto = null;
            for (HwReinstate hwReinstate : reinstateList) {
                dto = new HwReinstateDto();
                dto.setReinstateId(hwReinstate.getId());
                dto.setExamId(hwReinstate.getExamId());
                dto.setExamName(examNameMap.get(hwReinstate.getExamId()));
                dto.setOldExamId(hwReinstate.getOldExamId());
                dto.setOldExamName(examNameMap.get(hwReinstate.getOldExamId()));
                dtoList.add(dto);
            }
            map.put("dtoList", dtoList);
        }
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("studentId", studentId);
        map.put("studentName", studentName);
        map.put("identityCard", identityCard);
        map.put("oldGradeId", oldGradeId);
        map.put("gradeId", gradeId);
        return "/scoremanage/plan/reinstateList.ftl";
    }

    @RequestMapping("/reinstate/add/page")
    @ControllerInfo("新增考试插入方案")
    public String addReinstate(ModelMap map, HttpServletRequest request, HttpServletResponse response){
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String studentId = request.getParameter("studentId");
        String studentName = request.getParameter("studentName");
        String identityCard = request.getParameter("identityCard");
        String oldGradeId = request.getParameter("oldGradeId");
        String gradeId = request.getParameter("gradeId");

        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();

        List<ExamInfo> examInfoList = examInfoService.findExamInfoList(unitId, acadyear, semester);

        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(new String[]{oldGradeId, gradeId}), Grade.class);
        Map<String, Grade> gradeMap = EntityUtils.getMap(gradeList, Grade::getId, Function.identity());
        Grade grade = gradeMap.get(gradeId);
        int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));//2018
        int examAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
        int difCode = examAcadyear-openAcadyear;

        Grade oldGrade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
        int oldOpenAcadyear = NumberUtils.toInt(StringUtils.substringBefore(oldGrade.getOpenAcadyear(), "-"));
        int oldAcadyear1 = oldOpenAcadyear+difCode;
        int oldAcadyear2 = oldAcadyear1+1;
        String oldAcadyear = oldAcadyear1+"-"+oldAcadyear2;
        List<ExamInfo> oldExamList = examInfoService.findExamInfoListAll(unitId, oldAcadyear, semester, oldGradeId);
        map.put("oldExamList", oldExamList);
        List<ExamInfo> examList = examInfoService.findExamInfoListAll(unitId, acadyear, semester, gradeId);
        map.put("examList", examList);
        map.put("acadyear", acadyear);
        map.put("oldAcadyear", oldAcadyear);
        map.put("semester", semester);
        map.put("studentId", studentId);
        map.put("studentName", studentName);
        map.put("identityCard", identityCard);
        map.put("oldGradeId", oldGradeId);
        map.put("gradeId", gradeId);
        return "/scoremanage/plan/reinstateAdd.ftl";
    }

    @RequestMapping("/reinstate/save")
    @ResponseBody
    @ControllerInfo("考试插入方案保存")
    public String saveReinstate( ModelMap map, HttpServletRequest request, HttpServletResponse response){
        String acadyear = request.getParameter("acadyear");
        String oldAcadyear = request.getParameter("oldAcadyear");
        String semester = request.getParameter("semester");
        String studentId = request.getParameter("studentId");
        String oldGradeId = request.getParameter("oldGradeId");
        String gradeId = request.getParameter("gradeId");
        String oldExamId = request.getParameter("oldExamId");
        String examId = request.getParameter("examId");
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();

        HwReinstate hwReinstate = hwReinstateService.findOneByStudentId(unitId, acadyear, semester, studentId, examId);
        if(hwReinstate!=null){
            return error("已存在相同的考试插入方案");
        }

        HwPlan oldPlan = hwPlanService.findLastOne(unitId, oldAcadyear, semester, oldGradeId, oldExamId);
        if(oldPlan==null){
            return error("所选的休学前年级考试不存在成绩方案");
        }

        HwStatis statis = hwStatisService.findOneByPlanIdAndStudentId(unitId, oldPlan.getId(), HwConstants.PLAN_TYPE_1, studentId);
        if(statis==null){
            return error("所选的休学前年级考试成绩方案中不存在该学生");
        }

        HwPlan hwPlan = hwPlanService.findLastOne(unitId, acadyear, semester, gradeId, examId);
        if(hwPlan==null) {
            String result = savePlan(acadyear, semester, examId, gradeId, null, map);
            JSONObject json = JSONObject.parseObject(result);
            if(!json.getBooleanValue("success")){
                return error("当前年级新增方案失败");
            }
            hwPlan = hwPlanService.findLastOne(unitId, acadyear, semester, gradeId, examId);
        }

        statis.setId(UuidUtils.generateUuid());
        statis.setHwPlanId(hwPlan.getId());
        statis.setStuName(statis.getStuName()+"(复学)");
        for (HwStatisEx hwStatisEx : statis.getExList1()) {
            hwStatisEx.setId(UuidUtils.generateUuid());
            hwStatisEx.setHwPlanId(hwPlan.getId());
            hwStatisEx.setHwStatisId(statis.getId());
        }
        hwReinstate = new HwReinstate();
        hwReinstate.setId(UuidUtils.generateUuid());
        hwReinstate.setUnitId(unitId);
        hwReinstate.setAcadyear(acadyear);
        hwReinstate.setSemester(semester);
        hwReinstate.setStudentId(studentId);
        hwReinstate.setOldAcadyear(oldAcadyear);
        hwReinstate.setOldGradeId(oldGradeId);
        hwReinstate.setGradeId(gradeId);
        hwReinstate.setOldExamId(oldExamId);
        hwReinstate.setExamId(examId);
        hwReinstate.setCreationTime(new Date());
        hwReinstate.setModifyTime(new Date());
        hwReinstate.setOperator(loginInfo.getUserId());

        HwStatis oldStatis = hwStatisService.findOneByPlanIdAndStudentId(unitId, hwPlan.getId(), HwConstants.PLAN_TYPE_1, studentId);
        hwReinstateService.saveReinstate(hwReinstate,oldStatis,statis);
        return returnSuccess();
    }

    @RequestMapping("/reinstate/delete")
    @ResponseBody
    @ControllerInfo("考试插入方案删除")
    public String deleteReinstate(String reinstateId, ModelMap map, HttpServletResponse response){
        HwReinstate reinstate = hwReinstateService.findOne(reinstateId);
        HwStatis statis = null;
        List<HwStatisEx> statisExList = new ArrayList<>();
        HwPlan hwPlan = hwPlanService.findLastOne(reinstate.getUnitId(), reinstate.getAcadyear(), reinstate.getSemester(), reinstate.getGradeId(), reinstate.getExamId());
        if(hwPlan!=null){
            statis = hwStatisService.findOneByPlanIdAndStudentId(reinstate.getUnitId(), hwPlan.getId(), HwConstants.PLAN_TYPE_1, reinstate.getStudentId());
            if(statis!=null){
                statisExList.addAll(statis.getExList1());
            }
        }
        hwReinstateService.deleteReinstate(reinstate, statis, statisExList);
        return returnSuccess();
    }


}
