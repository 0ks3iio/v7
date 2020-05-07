package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Date: 2019/04/02
 * 综合素质
 */
@RestController
@RequestMapping("/diathesis/comprehensive/quality")
public class DiathesisComprehensiveAction extends DataImportAction {

    @Autowired
    private DiathesisMutualGroupService diathesisMutualGroupService;
    @Autowired
    private DiathesisMutualGroupStuService diathesisMutualGroupStuService;
    @Autowired
    private DiathesisProjectExService diathesisProjectExService;
    @Autowired
    private DiathesisScoreTypeService diathesisScoreTypeService;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private FamilyRemoteService familyRemoteService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;
    @Autowired
    private DiathesisRoleService diathesisRoleService;

    //加入导师的学生

    /**
     * 返回,这个年级下的所有学生
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/class/student")
    @ResponseBody
    public String classStudent(String gradeId) {
        if (StringUtils.isBlank(gradeId)) {
            return error("参数丢失");
        }
        JSONObject jsonObject = new JSONObject();
        String unitId = getLoginInfo().getUnitId();
        List<Clazz> classList = new ArrayList<>();
        if (StringUtils.isBlank(gradeId)) {
            classList = SUtils.dt(classRemoteService.findBySchoolId(unitId), Clazz.class);
            if(CollectionUtils.isNotEmpty(classList)){
                classList = EntityUtils.filter2(classList, x -> Clazz.NOT_GRADUATED == x.getIsGraduate());
            }
        } else {
            classList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), Clazz.class);
        }

        Set<String> classIdSet = classList.stream().map(e -> e.getId()).collect(Collectors.toSet());
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIdSet.toArray(new String[0])), Student.class);

        //包含导师
        Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(getLoginInfo().getUnitId(), getLoginInfo().getUserId());
        if (roleMap.keySet().contains(DiathesisConstant.ROLE_4)) {

            List<String> tutorStudentIds = roleMap.get(DiathesisConstant.ROLE_4);
            List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(tutorStudentIds.toArray(new String[0])), Student.class);
            Set<String> classIds = EntityUtils.getSet(stuList, x -> x.getClassId());

            List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), Clazz.class);
            if (CollectionUtils.isNotEmpty(classes)) {
                for (Clazz clazz : classes) {
                    if (!classIdSet.contains(clazz.getId())) {
                        classList.add(clazz);
                    }
                }
            }
            Set<String> stuIds = EntityUtils.getSet(studentList, x -> x.getId());
            if (CollectionUtils.isNotEmpty(stuList)) {
                for (Student student : stuList) {
                    if (!stuIds.contains(student.getId())) {
                        studentList.add(student);
                    }
                }
            }
        }
        Map<String, List<DiathesisStudentDto>> studentMap = studentList.stream()
                .collect(Collectors.groupingBy(Student::getClassId, Collectors.mapping(e -> {
                    DiathesisStudentDto tmp = new DiathesisStudentDto();
                    tmp.setStudentId(e.getId());
                    tmp.setStudentCode(e.getStudentCode());
                    tmp.setStudentName(e.getStudentName());
                    return tmp;
                }, Collectors.toList())));
        List<DiathesisClassDto> classDtos = classList.stream().sorted((y, x) -> x.getAcadyear().compareTo(y.getAcadyear())).map(e -> {
            DiathesisClassDto tmp = new DiathesisClassDto();
            tmp.setClassId(e.getId());
            tmp.setClassName(e.getClassNameDynamic() == null ? e.getClassName() : e.getClassNameDynamic());
            tmp.setStudentList(studentMap.get(e.getId()));
            return tmp;
        }).collect(Collectors.toList());
        jsonObject.put("classList", classDtos);
        return jsonObject.toJSONString();
    }

    private List<Student> getStudents(String gradeId) {
        List<Clazz> classList = new ArrayList<>();
        if (StringUtils.isBlank(gradeId)) {
            classList = SUtils.dt(classRemoteService.findBySchoolId(getLoginInfo().getUnitId()), Clazz.class);
            if (CollectionUtils.isNotEmpty(classList)){
                classList=EntityUtils.filter2( classList,x->Clazz.NOT_GRADUATED==x.getIsGraduate());
            }
        } else {
            classList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), Clazz.class);
        }
        Set<String> classIdSet = classList.stream().map(e -> e.getId()).collect(Collectors.toSet());
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIdSet.toArray(new String[0])), Student.class);

        //包含导师
        Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(getLoginInfo().getUnitId(), getLoginInfo().getUserId());
        if (roleMap.keySet().contains(DiathesisConstant.ROLE_4)) {

            List<String> tutorStudentIds = roleMap.get(DiathesisConstant.ROLE_4);
            List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(tutorStudentIds.toArray(new String[0])), Student.class);
            Set<String> classIds = EntityUtils.getSet(stuList, x -> x.getClassId());

            List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), Clazz.class);
            if (CollectionUtils.isNotEmpty(classes)) {
                for (Clazz clazz : classes) {
                    if (!classIdSet.contains(clazz.getId())) {
                        classList.add(clazz);
                    }
                }
            }
            Set<String> stuIds = EntityUtils.getSet(studentList, x -> x.getId());
            if (CollectionUtils.isNotEmpty(stuList)) {
                for (Student student : stuList) {
                    if (!stuIds.contains(student.getId())) {
                        studentList.add(student);
                    }
                }
            }
        }
        return studentList;
    }

    //综合素质录入首页
    @RequestMapping("/gradeList")
    @ResponseBody
    public String getGradeList(@RequestParam(required = false) String studentId) {

        List<Grade> gradeList;
        String unitId = getLoginInfo().getUnitId();

        if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_STUDENT) {
            //学生端
            studentId = getLoginInfo().getOwnerId();
            Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            String classId = student.getClassId();
            String gradeId = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getGradeId();
            gradeList = SUtils.dt(gradeRemoteService.findListByIds(new String[]{gradeId}), Grade.class);
        } else if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_FAMILY) {
            //家长端
            String familyId = getLoginInfo().getOwnerId();
            Family family = SUtils.dc(familyRemoteService.findOneById(familyId), Family.class);
            studentId = family.getStudentId();
            // 一个家长暂时对应一个学生
            Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            String classId = student.getClassId();
            String gradeId = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getGradeId();
            gradeList = SUtils.dt(gradeRemoteService.findListByIds(new String[]{gradeId}), Grade.class);
        } else {
            //管理端
            gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY, BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);
        }

        //每一種评价的数量
        Integer[] perNum = new Integer[]{0, 0, 0, 0, 0};

//        String num = RedisUtils.get("diathesis_project_num" + unitId + "78,");
//        if(StringUtils.isBlank(num)){
        List<DiathesisProject> projectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_CHILD});
        List<String> projectIds = EntityUtils.getList(projectList, x -> x.getId());
        List<String> parentIds = EntityUtils.getList(projectList, x -> x.getParentId());
        projectIds.removeAll(parentIds);
        List<DiathesisProjectEx> exList = diathesisProjectExService.findByUnitIdAndProjectIdIn(unitId, projectIds);
        for (DiathesisProjectEx ex : exList) {
            String types = ex.getEvaluationTypes();
            if (StringUtils.isNotBlank(types)) {
                for (String s : types.split(",")) {
                    perNum[Integer.parseInt(s) - 1]++;
                }
            }
        }

        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);

        List<DiathesisScoreType> diathesisScoreTypeList = diathesisScoreTypeService.findByUnitIdAndGradeIdAndType(unitId, null, DiathesisConstant.INPUT_TYPE_3);
        //  String[] gradeIds = EntityUtils.getSet(diathesisScoreTypeList, x -> x.getGradeId()).toArray(new String[0]);
//        List<Grade> gradeScoreList = new ArrayList<>();
//        if (gradeIds != null && gradeIds.length > 0) {
//            gradeScoreList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds), Grade.class);
//        }

        Map<String, String> gradeCodeMap = EntityUtils.getMap(gradeList, x -> x.getId(), x -> x.getGradeCode());
        List<String> typeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(diathesisScoreTypeList)) {
            //过滤出当前学年学期的scoreType
            List<String> collect = diathesisScoreTypeList.stream()
                    .filter(x -> x.getSemester() == semester.getSemester()
                            && x.getGradeCode().equals(gradeCodeMap.get(x.getGradeId())))
                    .map(x -> x.getId()).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(collect)) typeList.addAll(collect);
        }


        List<DiathesisScoreInfo> info = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(typeList)) {
            if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_STUDENT) {
                info = diathesisScoreInfoService.findByUnitIdAndProjectIdInAndScoreTypeIdInAndStudentId(unitId, projectIds, typeList, studentId);
                List<DiathesisScoreInfo> evaluateList = diathesisScoreInfoService.findByUnitIdAndProjectIdInAndScoreTypeIdInAndEvaluateId(unitId, projectIds, typeList, studentId);
                if (CollectionUtils.isNotEmpty(evaluateList)) {
                    info.addAll(evaluateList);
                }
            } else {
                info = diathesisScoreInfoService.findByUnitIdAndProjectIdInAndScoreTypeIdIn(unitId, projectIds, typeList);
                if (CollectionUtils.isNotEmpty(info))
                    info = info.stream().filter(x -> !x.getType().equals(DiathesisConstant.SCORE_TYPE_7)).collect(Collectors.toList());
            }
        }


        Map<String, Long> hasScoredMap = info.stream().collect(Collectors.groupingBy(x -> x.getScoreTypeId(), Collectors.counting()));

        Map<String, DiathesisScoreTypeDto> map = new HashMap<>();
        for (DiathesisScoreType one : diathesisScoreTypeList) {
            DiathesisScoreTypeDto tmp = new DiathesisScoreTypeDto();
            tmp.setScoreId(one.getId());
            tmp.setScoreType(one.getScoreType());
            tmp.setScoreName(one.getExamName());
            tmp.setImportTime(one.getLimitedTime());
            //已经打分的百分比
            Long hasScored = hasScoredMap.get(one.getId());

            Integer allStu;
            if (getLoginInfo().getOwnerType().equals(User.OWNER_TYPE_STUDENT)) {
                allStu = getStuNum(one.getScoreType(), getLoginInfo().getOwnerId());
            } else {
                allStu = getStudents(one.getGradeId()).size();
            }

            if (hasScored != null && allStu != 0 && perNum[Integer.parseInt(one.getScoreType()) - 1] != 0) {
                Integer t = perNum[Integer.parseInt(one.getScoreType()) - 1];
                String format = String.format("%.2f", (hasScored * 100.0f) / ((allStu * t) * 1.0f));
                tmp.setPercentage(format);
            }
            map.put(one.getGradeId() + "_" + one.getGradeCode() + "_" + one.getSemester() + "_" + one.getScoreType(), tmp);
        }
        List<DiathesisGradeDto> diathesisGradeDtoList = new ArrayList<>();
        for (Grade one : gradeList) {
            DiathesisGradeDto tmp = new DiathesisGradeDto();
            tmp.setGradeId(one.getId());
            tmp.setYear(one.getOpenAcadyear().substring(0, 4) + "级");
            tmp.setSemesterName(Integer.valueOf(1).equals(semester.getSemester()) ? "第一学期" : "第二学期");
            tmp.setSemester(semester.getSemester().toString());
            tmp.setGradeCode(one.getGradeCode());
            tmp.setGradeName(one.getGradeName());
            List<DiathesisScoreTypeDto> sub = new ArrayList<>();
            for (String key : DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.keySet()) {
                DiathesisScoreTypeDto subTmp = map.get(one.getId() + "_" + one.getGradeCode() + "_" + semester.getSemester() + "_" + key);
                // sub 中设置比例
                if (subTmp == null) {
                    subTmp = new DiathesisScoreTypeDto();
                    subTmp.setScoreId(BaseConstants.ZERO_GUID);
                    subTmp.setScoreType(key);
                    subTmp.setScoreName(DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.get(key));
                }
                sub.add(subTmp);
            }
            tmp.setScoreList(sub);
            diathesisGradeDtoList.add(tmp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gradeList", diathesisGradeDtoList);
        return jsonObject.toJSONString();
    }

    private Integer getStuNum(String scoreType, String stuId) {
        if (DiathesisConstant.INPUT_TYPE_1.equals(scoreType)) return 1;

        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId()), Semester.class);
        DiathesisSet set = diathesisSetService.findByUnitId(getLoginInfo().getUnitId());
        List<DiathesisMutualGroupStu> stuList;
        if (DiathesisConstant.MUTUAL_EVALUATE_LEADER.equals(set.getMutualType())) {
            //直接找组员就行了
            List<DiathesisMutualGroup> group = diathesisMutualGroupService.findByLeadIdAndSemester(getLoginInfo().getOwnerId(), semester.getAcadyear(), semester.getSemester());
            stuList = diathesisMutualGroupStuService.findListByMutualGroupIdIn(EntityUtils.getList(group, x -> x.getId()));

        } else {
            String groupId = diathesisMutualGroupStuService.findMutualGroupIdByStudentId(getLoginInfo().getOwnerId(), semester.getAcadyear(), semester.getSemester());
            stuList = diathesisMutualGroupStuService.findListByMutualGroupId(groupId);
        }
        if (CollectionUtils.isEmpty(stuList)) {
            return 0;
        } else {
            for (DiathesisMutualGroupStu stu : stuList) {
                if (stu.getStudentId().equals(stuId)) return stuList.size() - 1;
            }
            return stuList.size();
        }

    }

    @RequestMapping("/student/detail")
    @ResponseBody
    public String getStudentDetail(String gradeId, String studentId, String gradeCode, String semester) {

        //int type = getLoginInfo().getOwnerType();

        if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(studentId)) {
            return error("参数丢失");
        }
        JSONObject jsonObject = new JSONObject();
        String unitId = getLoginInfo().getUnitId();
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET);

        DiathesisGlobalSettingDto usingSet = diathesisSetService.findGlobalByUnitId(usingUnitId);
        DiathesisGlobalSettingDto myset = diathesisSetService.findGlobalByUnitId(unitId);


        //todo 互評

        boolean isMutual = false;
        Predicate<DiathesisScoreInfo> filter = x -> !DiathesisConstant.SCORE_TYPE_7.equals(x.getType());
        Integer type = getLoginInfo().getOwnerType();
        if (type == User.OWNER_TYPE_STUDENT && DiathesisConstant.MUTUAL_EVALUATE_MEMBER.equals(myset.getMutualType()) && !getLoginInfo().getOwnerId().equals(studentId)) {
            //学生之间的互评
            isMutual = true;
            filter = x -> DiathesisConstant.SCORE_TYPE_7.equals(x.getType()) && getLoginInfo().getOwnerId().equals(x.getEvaluateStuId());
        }

        jsonObject.put("inputValueType", usingSet.getInputValueType());
        if (DiathesisConstant.INPUT_RANK.equals(usingSet.getInputValueType())) {
            jsonObject.put("rankItems", usingSet.getRankItems());
        }

        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);

        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        if (type == User.OWNER_TYPE_STUDENT) {
            jsonObject.put("acadyearName", acadyearName(grade.getGradeCode(), semesterObj.getSemester()).get(semesterObj.getSemester() - 1));
        } else {
            jsonObject.put("acadyearName", acadyearName(grade.getGradeCode(), semesterObj.getSemester()));
        }

        // 若为空取当前学年学期
        if (StringUtils.isBlank(gradeCode) || StringUtils.isBlank(semester)) {
            semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, unitId), Semester.class).getSemester().toString();
            gradeCode = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class).getGradeCode();
        }
        //总评成绩type
        DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findTotalScoreType(gradeId, gradeCode, semester);
        List<DiathesisProject> topList = diathesisProjectService.findByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP});
        List<String> topIds = EntityUtils.getList(topList, x -> x.getId());
        HashMap<String, DiathesisScoreInfo> proIdAndTotalScoreMap = new HashMap<>();
        //不为null 说明 已经统计过了
        if (diathesisScoreType != null) {
            List<DiathesisScoreInfo> totalList = diathesisScoreInfoService.findByScoreTypeIdAndStuIdAndProjectIdIn(diathesisScoreType.getId(), studentId, topIds);
            if (CollectionUtils.isNotEmpty(totalList)) {
                for (DiathesisScoreInfo info : totalList) {
                    proIdAndTotalScoreMap.put(info.getObjId(), info);
                }
            }
        }
        List<DiathesisScoreType> diathesisScoreTypeList = diathesisScoreTypeService.findComprehensiveList(gradeId, gradeCode, semester);
        if (CollectionUtils.isEmpty(diathesisScoreTypeList)) {
            return error("录入时间范围没有设置");
        }
        Map<String, DiathesisScoreType> scoreTypeMap = diathesisScoreTypeList.stream().collect(Collectors.toMap(e -> e.getScoreType(), e -> e));
        List<String> scoreTypes = new ArrayList<>();
        List<String> scoreTimes = new ArrayList<>();
        Map<String, String> scoreMap = new HashMap<>();
        if (type == User.OWNER_TYPE_STUDENT) {
            //学生端
            scoreMap.put(DiathesisConstant.SCORE_TYPE_1, "学生自评");
            scoreMap.put(DiathesisConstant.SCORE_TYPE_3, "学生互评");
        } else if (type == User.OWNER_TYPE_FAMILY) {
            //家长端
            scoreMap.put(DiathesisConstant.SCORE_TYPE_2, "家长评价");
        } else {
            //管理员端

            scoreMap = DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP;

        }
        //仅仅是导师

        boolean onlyTutor = true;
        Map<String, List<String>> role = diathesisRoleService.findRoleByUserId(unitId, getLoginInfo().getUserId());
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);

        if (role.keySet().contains("1")) {
            onlyTutor = false;
        }
        if (role.keySet().contains("2")) {
            if (role.get("2").contains(clazz.getGradeId())) {
                onlyTutor = false;
            }
        }
        if (role.keySet().contains("3")) {
            if (role.get("3").contains(student.getClassId())) {
                onlyTutor = false;
            }
        }
        if (!role.keySet().contains("4")) {
            onlyTutor = false;
        }

        for (String one : scoreMap.keySet()) {
            String tmp;
            String timeTmp;
            if (onlyTutor && !one.equals(DiathesisConstant.SCORE_TYPE_4)) {
                tmp = BaseConstants.ZERO_GUID;
                timeTmp = "null";

            } else if (scoreTypeMap.get(one) == null) {
                tmp = BaseConstants.ZERO_GUID;
                timeTmp = "null";
            } else {
                tmp = scoreTypeMap.get(one).getId();
                timeTmp = scoreTypeMap.get(one).getLimitedTime();
                if (StringUtils.isBlank(tmp)) {
                    tmp = BaseConstants.ZERO_GUID;
                }
                if (StringUtils.isBlank(timeTmp)) {
                    timeTmp = "null";
                }
            }
            scoreTypes.add(tmp);
            scoreTimes.add(timeTmp);
        }
        jsonObject.put("scoreTypeIds", scoreTypes);
        jsonObject.put("scoreTypeTimes", scoreTimes);
        List<String> scoreTypeIds = diathesisScoreTypeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByStudentIdAndScoreTypeIdIn(studentId, scoreTypeIds.toArray(new String[0]));
        List<DiathesisScoreInfo> collect = diathesisScoreInfoList.stream().filter(filter).collect(Collectors.toList());
        Map<String, String> diathesisScoreInfoMap = diathesisScoreInfoList.stream().filter(filter)
                .collect(Collectors.toMap(e -> (e.getObjId() + "_" + e.getType()), e -> e.getScore()));

        // 组装三级列表
        List<DiathesisProject> diathesisProjectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD});
        diathesisProjectList.forEach(e -> {
            if (e.getParentId() == null) {
                e.setParentId(BaseConstants.ZERO_GUID);
            }
        });

        Map<String, List<DiathesisComprehensiveDto>> map = diathesisProjectList.stream()
                .collect(Collectors.groupingBy(DiathesisProject::getParentId, Collectors.mapping(e -> {
                    DiathesisComprehensiveDto tmp = new DiathesisComprehensiveDto();
                    tmp.setDiathesisId(e.getId());
                    tmp.setDiathesisName(e.getProjectName());
                    tmp.setEvaluationTypes(e.getEvaluationTypes());
                    tmp.setRemark(e.getRemark());
                    return tmp;
                }, Collectors.toList())));
        if (map == null) {
            return error("未维护综合素质指标");
        }
        List<DiathesisComprehensiveDto> resultList = new ArrayList<>();
        for (DiathesisComprehensiveDto one : map.get(BaseConstants.ZERO_GUID)) {
            List<DiathesisComprehensiveDto> oneSubTmp = new ArrayList<>();
            if (map.get(one.getDiathesisId()) != null) {
                for (DiathesisComprehensiveDto two : map.get(one.getDiathesisId())) {
                    if (map.get(two.getDiathesisId()) == null) {
                        // 该二级列表无子列表
                        // 直接填充成绩
                        int index = 0;
                        // 控制只返回  学生自评 或者  家长评价
                        String[] scoreList = new String[scoreMap.size()];
                        for (String key : scoreMap.keySet()) {
                            scoreList[index++] = (diathesisScoreInfoMap.get(two.getDiathesisId() + "_" + key) == null ? "null" : diathesisScoreInfoMap.get(two.getDiathesisId() + "_" + key));
                        }
                        two.setScoreList(scoreList);
                    } else {
                        // 填充二级列表子列表
                        List<DiathesisComprehensiveDto> twoSubTmp = new ArrayList<>();
                        for (DiathesisComprehensiveDto three : map.get(two.getDiathesisId())) {
                            int index = 0;
                            // 控制只返回  学生自评 或者  家长评价
                            String[] scoreList = new String[scoreMap.size()];
                            for (String key : scoreMap.keySet()) {
                                if (three.getEvaluationTypes().contains(key)) {
                                    if (isMutual) key = DiathesisConstant.SCORE_TYPE_7;
                                    scoreList[index++] = (diathesisScoreInfoMap.get(three.getDiathesisId() + "_" + key) == null ? "null" : diathesisScoreInfoMap.get(three.getDiathesisId() + "_" + key));
                                } else {
                                    scoreList[index++] = "/";
                                }
                            }
                            three.setScoreList(scoreList);
                            twoSubTmp.add(three);
                        }
                        two.setSubList(twoSubTmp);
                    }
                    oneSubTmp.add(two);
                }
            }
            if (CollectionUtils.isNotEmpty(oneSubTmp)) {
                DiathesisScoreInfo info = proIdAndTotalScoreMap.get(one.getDiathesisId());
                if (info == null || onlyTutor) {
                    one.setTotalScore("");
                    one.setTotalRemark("");
                    one.setTotalScoreId("");
                } else {
                    one.setTotalScore(info.getScore());
                    one.setTotalRemark(info.getRemark());
                    one.setTotalScoreId(info.getId());
                }
                one.setSubList(oneSubTmp);
                resultList.add(one);
            }
        }

        jsonObject.put("diathesisList", resultList);
        return jsonObject.toJSONString();
    }

    /**
     * 总评等第修改
     *
     * @param dto
     * @param errors
     * @return
     */
    @PostMapping("/student/setOverallScore")
    @ResponseBody
    public String setOverallScore(@RequestBody @Valid DiathesisOverallChangeDto dto, Errors errors) {
        if (errors.hasFieldErrors()) return error(errors.getFieldError().getDefaultMessage());
        DiathesisScoreInfo scoreInfo = diathesisScoreInfoService.findOne(dto.getTotalScoreId());
        if (scoreInfo == null) return error("总评生成后才能修改");
        DiathesisScoreInfo info = EntityUtils.copyProperties(scoreInfo, new DiathesisScoreInfo());
        info.setModifyTime(new Date());
        info.setScore(dto.getScore());
        info.setRemark(dto.getRemark());
        diathesisScoreInfoService.save(info);
        return success("修改成功");
    }


    /**
     * 学生分数保存
     *
     * @return
     */
    @PostMapping("/student/setScore")
    @ResponseBody
    public String setSingleScore(@RequestBody @Valid DiathesisSetScoreDto dto) {
        //分布式锁控制,防止重复点击
        try {
            RedisUtils.hasLocked("diathesis_comprehensive_Lock_" + dto.getStudentId() + "_78,");
            String studentId = dto.getStudentId();
            String params = dto.getParams();
            //params :按projectId_scoreTypeId_scoreType-score的形式组装，多组以~分号隔开
            String unitId = getLoginInfo().getUnitId();

            String[] infos = params.split("~");
            Map<String, String> location = new HashMap<>();
            Set<String> scoreTypeIdSet = new HashSet<>();
            for (String one : infos) {
                String[] tmp = one.split("-");
                location.put(tmp[0], tmp[1]);
                scoreTypeIdSet.add(tmp[0].split("_")[1]);
            }

            // 等第或者分值
            String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET);
            DiathesisGlobalSettingDto usingSet = diathesisSetService.findGlobalByUnitId(usingUnitId);
            DiathesisGlobalSettingDto mySet = diathesisSetService.findGlobalByUnitId(unitId);
            Pattern pattern = null;
            List<String> rank = null;
            if (DiathesisConstant.INPUT_RANK.equals(usingSet.getInputValueType())) {
                rank = usingSet.getRankItems();
            } else {
                pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
            }

            //学生之间的互评
            boolean isMutual = false;
            Predicate<DiathesisScoreInfo> filter = x -> !DiathesisConstant.SCORE_TYPE_7.equals(x.getType());
            if (getLoginInfo().getOwnerType().equals(User.OWNER_TYPE_STUDENT) && DiathesisConstant.MUTUAL_EVALUATE_MEMBER.equals(mySet.getMutualType()) && !getLoginInfo().getOwnerId().equals(studentId)) {
                //取到具体学生的学生互评
                isMutual = true;
                filter = x -> DiathesisConstant.SCORE_TYPE_7.equals(x.getType()) && getLoginInfo().getOwnerId().equals(x.getEvaluateStuId());
            }

            List<DiathesisScoreInfo> diathesisScoreInfoListAll = diathesisScoreInfoService.findByStudentIdAndScoreTypeIdIn(studentId, scoreTypeIdSet.toArray(new String[0]));
            List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoListAll.stream().filter(filter).collect(Collectors.toList());
            Map<String, DiathesisScoreInfo> oldDatas = diathesisScoreInfoList.stream().collect(Collectors.toMap(e -> e.getObjId() + "_" + e.getScoreTypeId() + "_" + e.getType(), e -> e));
            //  String ownerId = getLoginInfo().getOwnerId();
            List<DiathesisScoreInfo> saveList = new ArrayList<>();
            for (Map.Entry<String, String> one : location.entrySet()) {
                if (DiathesisConstant.INPUT_RANK.equals(usingSet.getInputValueType())) {
                    if (rank.indexOf(one.getValue().toUpperCase()) < 0) {
                        return error("指标等第有误");
                    }
                } else {
                    if (!pattern.matcher(one.getValue()).matches()) {
                        return error("指标分数有误");
                    }
                    if (0.0f > Float.valueOf(one.getValue()) || Float.valueOf(one.getValue()) > 100.0f) {
                        return error("指标分数超过范围");
                    }
                }
                String key = one.getKey();
                if (isMutual) {
                    key = one.getKey().substring(0, one.getKey().length() - 1) + DiathesisConstant.SCORE_TYPE_7;
                }
                DiathesisScoreInfo tmp = oldDatas.get(key);
                if (tmp == null) {
                    //projectId_scoreTypeId_scoreType
                    String[] locationTmp = one.getKey().split("_");
                    tmp = new DiathesisScoreInfo();
                    tmp.setId(UuidUtils.generateUuid());
                    tmp.setUnitId(unitId);
                    tmp.setObjId(locationTmp[0]);
                    //若是学生之间的互评类型为 7
                    tmp.setType(isMutual ? DiathesisConstant.SCORE_TYPE_7 : locationTmp[2]);
                    //学生互评要记录人id
                    if (DiathesisConstant.SCORE_TYPE_3.equals(locationTmp[2])) {
                        tmp.setEvaluateStuId(getLoginInfo().getOwnerId());
                    }
                    tmp.setScoreTypeId(locationTmp[1]);
                    tmp.setScore(one.getValue());
                    tmp.setModifyTime(new Date());
                    tmp.setStuId(studentId);
                } else {
                    tmp.setScore(one.getValue());
                    tmp.setModifyTime(new Date());
                }
                saveList.add(tmp);
            }

            //如果是互评
            if (isMutual) {
                List<String> rankValues = usingSet.getRankValues();
                HashMap<String, Integer> rankMap = new HashMap<>();
                int i = 0;
                for (String r : rank) {
                    rankMap.put(r, Integer.parseInt(rankValues.get(i++)));
                }
                String scoreTypeId = scoreTypeIdSet.iterator().next();
                Map<String, List<DiathesisScoreInfo>> map = Stream.concat(saveList.stream(), diathesisScoreInfoListAll.stream().filter(x -> !getLoginInfo().getOwnerId().equals(x.getEvaluateStuId()))).filter(x -> DiathesisConstant.SCORE_TYPE_7.equals(x.getType()))
                        .collect(Collectors.groupingBy(x -> x.getObjId()));
                HashMap<String, String> newAvgMap = new HashMap<>();
                //老成绩
                Map<String, DiathesisScoreInfo> oldInfoMap = diathesisScoreInfoListAll.stream().filter(x -> !DiathesisConstant.SCORE_TYPE_7.equals(x.getType())).collect(Collectors.toMap(x -> x.getObjId(), x -> x));


                //key objId_type
                for (String one : map.keySet()) {
                    List<DiathesisScoreInfo> infoList = map.get(one);
                    if (CollectionUtils.isEmpty(infoList)) continue;
                    float sum = 0;
                    for (DiathesisScoreInfo info : infoList) {
                        if (DiathesisConstant.INPUT_RANK.equals(usingSet.getInputValueType())) {
                            //等第
                            sum += rankMap.get(info.getScore());
                        } else {
                            //分值
                            sum += Float.parseFloat(info.getScore());
                        }
                    }
                    String score = getLastScore(rankMap, sum, infoList.size());
                    newAvgMap.put(one, score);
                }

                for (String x : newAvgMap.keySet()) {
                    DiathesisScoreInfo tmp = oldInfoMap.get(x);
                    if (tmp == null) {
                        //objId
                        tmp = new DiathesisScoreInfo();
                        tmp.setId(UuidUtils.generateUuid());
                        tmp.setUnitId(unitId);
                        tmp.setObjId(x);

                        tmp.setType(DiathesisConstant.SCORE_TYPE_3);
                        tmp.setScoreTypeId(scoreTypeId);
                        tmp.setScore(newAvgMap.get(x));
                        tmp.setModifyTime(new Date());
                        tmp.setStuId(studentId);

                    } else {
                        tmp.setScore(newAvgMap.get(x));
                        tmp.setModifyTime(new Date());
                    }
                    saveList.add(tmp);
                }
            }
            diathesisScoreInfoService.saveAll(saveList.toArray(new DiathesisScoreInfo[0]));

        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        } finally {
            RedisUtils.unLock("diathesis_comprehensive_Lock_" + dto.getStudentId() + "_78,");
        }
        return success("成功");
    }

    private String getLastScore(HashMap<String, Integer> rankMap, float sum, int size) {
        if (rankMap == null || rankMap.isEmpty()) return null;
        float avg = sum * 1.0f / size;
        float score = Float.MAX_VALUE;
        String result = "";
        for (String s : rankMap.keySet()) {
            if (Math.abs(rankMap.get(s) - avg) <= score) {
                result = s;
                score = Math.abs(rankMap.get(s) - avg);
            }
        }
        return result;
    }

    @RequestMapping("/timeset")
    @ResponseBody
    public String timeSet(String gradeId, String timeSet) {
        /**
         * 外网出现数据重复  , 暂时不知道什么问题,用redis锁控制
         */

        if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(timeSet)) {
            return error("参数丢失");
        }
        try {
            RedisUtils.hasLocked("diathesis_comprehensive_timeset_" + gradeId + "_78,");
            String unitId = getLoginInfo().getUnitId();
            String[] timeArr = timeSet.split(",");
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, unitId), Semester.class);
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            List<DiathesisScoreType> saveList = new ArrayList<>();
            List<DiathesisScoreType> diathesisScoreTypeList = diathesisScoreTypeService.findComprehensiveList(gradeId, grade.getGradeCode(), semester.getSemester().toString());
            diathesisScoreTypeList.stream().filter(e -> DiathesisConstant.INPUT_TYPE_3.equals(e.getType())).collect(Collectors.toList());
            for (String time : timeArr) {
                String[] subArr = time.split(":");
                DiathesisScoreType tmp = null;
                for (DiathesisScoreType diathesisScoreType : diathesisScoreTypeList) {
                    if (subArr[0].equals(diathesisScoreType.getScoreType())) {
                        tmp = diathesisScoreType;
                    }
                }
                if (tmp == null) {
                    tmp = new DiathesisScoreType();
                    tmp.setId(UuidUtils.generateUuid());
                    tmp.setUnitId(unitId);
                    tmp.setGradeId(gradeId);
                    tmp.setGradeCode(grade.getGradeCode());
                    tmp.setSemester(semester.getSemester());
                    tmp.setType(DiathesisConstant.INPUT_TYPE_3);
                    tmp.setScoreType(subArr[0]);
                    tmp.setExamName(DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.get(subArr[0]));
                    tmp.setLimitedTime(subArr[1]);
                    tmp.setModifyTime(new Date());
                    tmp.setYear(grade.getOpenAcadyear().substring(0, 4));
                    tmp.setOperator(getLoginInfo().getRealName());
                } else {
                    tmp.setModifyTime(new Date());
                    tmp.setLimitedTime(subArr[1]);
                }
                saveList.add(tmp);
            }

            diathesisScoreTypeService.saveAll(saveList.toArray(new DiathesisScoreType[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        } finally {
            RedisUtils.unLock("diathesis_comprehensive_timeset_" + gradeId + "_78,");
        }
        return success("设置成功");
    }

    // ==========================================================
    // 综合素质分导入逻辑

    @RequestMapping("/execute")
    @ResponseBody
    public String execute() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businessKey", UuidUtils.generateUuid());
        return jsonObject.toJSONString();
    }

    @Override
    public String getObjectName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getRowTitleList() {
        return null;
    }

    public List<DiathesisComprehensiveDto> getMultiRowTitleList(String scoreType) {
        String unitId = getLoginInfo().getUnitId();
        // 组装三级列表
        List<DiathesisProject> diathesisProjectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD});
        diathesisProjectList.forEach(e -> {
            if (e.getParentId() == null) {
                e.setParentId(BaseConstants.ZERO_GUID);
            }
        });
        Map<String, List<DiathesisComprehensiveDto>> map = diathesisProjectList.stream()
                .collect(Collectors.groupingBy(DiathesisProject::getParentId, Collectors.mapping(e -> {
                    DiathesisComprehensiveDto tmp = new DiathesisComprehensiveDto();
                    tmp.setDiathesisId(e.getId());
                    tmp.setDiathesisName(e.getProjectName());
                    tmp.setEvaluationTypes(e.getEvaluationTypes());
                    tmp.setRemark(e.getRemark());
                    return tmp;
                }, Collectors.toList())));
        List<DiathesisComprehensiveDto> resultList = new ArrayList<>();
        for (DiathesisComprehensiveDto one : map.get(BaseConstants.ZERO_GUID)) {
            List<DiathesisComprehensiveDto> oneSubTmp = new ArrayList<>();
            if (map.get(one.getDiathesisId()) != null) {
                for (DiathesisComprehensiveDto two : map.get(one.getDiathesisId())) {
                    List<DiathesisComprehensiveDto> twoSubTmp = new ArrayList<>();
                    for (DiathesisComprehensiveDto three : map.get(two.getDiathesisId())) {
                        if (three.getEvaluationTypes().indexOf(scoreType) > -1) {
                            twoSubTmp.add(three);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(twoSubTmp)) {
                        two.setSubList(twoSubTmp);
                        oneSubTmp.add(two);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(oneSubTmp)) {
                one.setSubList(oneSubTmp);
                resultList.add(one);
            }
        }
        return resultList;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        params = params.replace("&quot;", "\"");
        JSONObject jsStr = JSONObject.parseObject(params);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operateName", getLoginInfo().getRealName());
        String unitId = getLoginInfo().getUnitId();
        String gradeId = jsStr.getString("gradeId");
        String scoreType = jsStr.getString("scoreType");
        String gradeCode = jsStr.getString("gradeCode");
        String semesterStr = jsStr.getString("semester");
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        Semester semester;
        if (StringUtils.isNotBlank(gradeCode) && StringUtils.isNotBlank(semesterStr)) {
            semester = new Semester();
            String[] acadyearTmp = grade.getOpenAcadyear().split("-");
            String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
            semester.setAcadyear(acadyear);
            semester.setSemester(Integer.valueOf(semesterStr));
        } else {
            semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, unitId), Semester.class);
        }

        // 等第或者分值
        DiathesisGlobalSettingDto comprehensiveSetting = diathesisSetService.findGlobalByUnitId(unitId);
        Pattern pattern = null;
        List<String> rank = null;
        if (DiathesisConstant.INPUT_SCORE.equals(comprehensiveSetting.getInputValueType())) {
            pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
        } else {
            rank = comprehensiveSetting.getRankItems();
        }

        List<DiathesisComprehensiveDto> titleList = getMultiRowTitleList(scoreType);
        int totalCol = 2;
        List<DiathesisComprehensiveDto> subList = new ArrayList<>();
        List<DiathesisComprehensiveDto> bottomList = new ArrayList<>();
        for (DiathesisComprehensiveDto top : titleList) {
            for (DiathesisComprehensiveDto sub : top.getSubList()) {
                subList.add(sub);
                totalCol += sub.getSubList().size();
                for (DiathesisComprehensiveDto bottom : sub.getSubList()) {
                    bottomList.add(bottom);
                }
            }
        }

        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 0, totalCol);

        if (StringUtils.isBlank(datas.get(0)[1])) {
            datas.remove(0);
        }
        datas.remove(0);
        datas.remove(0);
        datas.remove(0);

        List<String[]> errorDataList = new ArrayList<>();
        if (CollectionUtils.isEmpty(datas)) {
            return error("没有导入数据");
        }

        List<DiathesisProject> diathesisProjectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD});
        diathesisProjectList.forEach(e -> {
            if (e.getParentId() == null) {
                e.setParentId(BaseConstants.ZERO_GUID);
            }
        });
        Map<String, String> comprehensiveNameToIdMap = EntityUtils.getMap(diathesisProjectList, DiathesisProject::getProjectName, DiathesisProject::getId);

        // 学生
        Set<String> studentCodeSet = new HashSet<>();
        for (String[] arr : datas) {
            if (StringUtils.isNotBlank(arr[0])) {
                studentCodeSet.add(arr[0]);
            }
        }
        List<Student> studentList;
        Map<String, Student> studentMap = new HashMap<String, Student>();
        // 考虑学号重复问题
        Map<String, Set<String>> studentCodeToIdMap = new HashMap<String, Set<String>>();
        Set<String> classIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(studentCodeSet)) {
            studentList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, studentCodeSet.toArray(new String[0])), new TR<List<Student>>() {
            });
            for (Student one : studentList) {
                studentMap.put(one.getId(), one);
                if (!studentCodeToIdMap.containsKey(one.getStudentCode())) {
                    studentCodeToIdMap.put(one.getStudentCode(), new HashSet<>());
                }
                studentCodeToIdMap.get(one.getStudentCode()).add(one.getId());
                classIdSet.add(one.getClassId());
            }
        }

        List<Clazz> classList;
        Map<String, String> classIdToGradeIdMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(classIdSet)) {
            classList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            for (Clazz one : classList) {
                classIdToGradeIdMap.put(one.getId(), one.getGradeId());
            }
        }

        // 原有成绩信息
        Map<String, DiathesisScoreInfo> oldDiathesisScoreInfoMap = null;
        DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(grade.getId(), DiathesisConstant.INPUT_TYPE_3, scoreType, grade.getGradeCode(), semester.getSemester().toString(), null);
        if (diathesisScoreType == null) {
            diathesisScoreType = new DiathesisScoreType();
            diathesisScoreType.setId(UuidUtils.generateUuid());
            diathesisScoreType.setUnitId(unitId);
            diathesisScoreType.setGradeId(grade.getId());
            diathesisScoreType.setGradeCode(grade.getGradeCode());
            diathesisScoreType.setYear(grade.getOpenAcadyear().substring(0, 4));
            diathesisScoreType.setExamName(DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.get(scoreType));
            diathesisScoreType.setType(DiathesisConstant.INPUT_TYPE_3);
            diathesisScoreType.setScoreType(scoreType);
            diathesisScoreType.setModifyTime(new Date());
            diathesisScoreType.setOperator(getLoginInfo().getOwnerId());
        } else {
            List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(scoreType, diathesisScoreType.getId());
            if (CollectionUtils.isNotEmpty(diathesisScoreInfoList)) {
                oldDiathesisScoreInfoMap = diathesisScoreInfoList.stream().collect(Collectors.toMap(e -> (e.getStuId() + "_" + e.getObjId()), e -> e));
            }
            diathesisScoreType.setModifyTime(new Date());
            diathesisScoreType.setOperator(getLoginInfo().getOwnerId());
        }

        // 错误数据序列号
        int successCount = 0;
        Set<String> arrangeStuId = new HashSet<>();
        List<DiathesisScoreInfo> insertList = new ArrayList<>();
        int index = 0;
        for (String[] arr : datas) {
            index++;
            String[] errorData = new String[4];
            String studentCodeData = arr[0] == null ? null : StringUtils.trim(arr[0]);
            String studentNameData = arr[1] == null ? null : StringUtils.trim(arr[1]);

            if (StringUtils.isBlank(studentCodeData)) {
                errorData[0] = index + "";
                errorData[1] = "学号";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(studentNameData)) {
                errorData[0] = index + "";
                errorData[1] = "姓名";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            //判断学生是否存在
            Set<String> studentIdSet = studentCodeToIdMap.get(studentCodeData);
            Student student;
            if (CollectionUtils.isEmpty(studentIdSet)) {
                errorData[0] = index + "";
                errorData[1] = "学号";
                errorData[2] = studentCodeData;
                errorData[3] = "学号不存在";
                errorDataList.add(errorData);
                continue;
            } else {
                List<Student> sameCodeAndNameList = new ArrayList<>();
                for (String one : studentIdSet) {
                    if (studentMap.get(one).getStudentName() != null && studentMap.get(one).getStudentName().equals(studentNameData)) {
                        sameCodeAndNameList.add(studentMap.get(one));
                    }
                }
                if (CollectionUtils.isEmpty(sameCodeAndNameList)) {
                    errorData[0] = index + "";
                    errorData[1] = "姓名";
                    errorData[2] = studentNameData;
                    errorData[3] = "学号所对应的学生姓名错误";
                    errorDataList.add(errorData);
                    continue;
                }
                // 判断是不是该年级下的
                List<Student> sameCodeAndNameInGrade = new ArrayList<>();
                for (Student one : sameCodeAndNameList) {
                    if (gradeId.equals(classIdToGradeIdMap.get(one.getClassId()))) {
                        sameCodeAndNameInGrade.add(one);
                    }
                }
                if (CollectionUtils.isEmpty(sameCodeAndNameInGrade)) {
                    errorData[0] = index + "";
                    errorData[1] = "姓名";
                    errorData[2] = studentNameData;
                    errorData[3] = "该学生不属于" + grade.getGradeName() + "年级";
                    errorDataList.add(errorData);
                    continue;
                }
                if (sameCodeAndNameInGrade.size() > 1) {
                    errorData[0] = index + "";
                    errorData[1] = "姓名";
                    errorData[2] = studentNameData;
                    errorData[3] = grade.getGradeName() + "年级下存在多个匹配学号姓名的学生";
                    errorDataList.add(errorData);
                    continue;
                }
                student = sameCodeAndNameInGrade.get(0);
            }
            if (student == null) {
                continue;
            }

            // 保存成绩
            boolean flag = true;
            Map<String, String> scoreMap = new HashMap<>();
            for (int i = 0; i < bottomList.size(); i++) {
                String scoreStr = arr[i + 2] == null ? null : StringUtils.trim(arr[i + 2]);
                if (StringUtils.isBlank(scoreStr)) {
                    continue;
                }
                // 判断输入成绩是否正确
                if (DiathesisConstant.INPUT_SCORE.equals(comprehensiveSetting.getInputValueType())) {
                    if (!pattern.matcher(scoreStr).matches()) {
                        errorData = new String[4];
                        errorData[0] = index + "";
                        errorData[1] = bottomList.get(i).getDiathesisName();
                        errorData[2] = scoreStr;
                        errorData[3] = "指标分数有误";
                        errorDataList.add(errorData);
                        flag = false;
                        break;
                    }
                    if (0.0f > Float.valueOf(scoreStr) || Float.valueOf(scoreStr) > 100.0f) {
                        errorData = new String[4];
                        errorData[0] = index + "";
                        errorData[1] = bottomList.get(i).getDiathesisName();
                        errorData[2] = scoreStr;
                        errorData[3] = "指标分数超过范围";
                        errorDataList.add(errorData);
                        flag = false;
                        break;
                    }
                } else {
                    if (rank.indexOf(scoreStr.toUpperCase()) < 0) {
                        errorData = new String[4];
                        errorData[0] = index + "";
                        errorData[1] = bottomList.get(i).getDiathesisName();
                        errorData[2] = scoreStr;
                        errorData[3] = "指标等第有误";
                        errorDataList.add(errorData);
                        flag = false;
                        break;
                    }
                }
                scoreMap.put(comprehensiveNameToIdMap.get(bottomList.get(i).getDiathesisName()), scoreStr);
            }
            if (!flag) {
                continue;
            }
            if (arrangeStuId.contains(student.getId())) {
                errorData[0] = index + "";
                errorData[1] = "姓名";
                errorData[2] = studentNameData;
                errorData[3] = "之前已有该学生成绩，重复录入";
                errorDataList.add(errorData);
                continue;
            }

            //学生科目成绩
            for (Map.Entry<String, String> item : scoreMap.entrySet()) {
                DiathesisScoreInfo tmp = null;
                if (oldDiathesisScoreInfoMap != null) {
                    tmp = oldDiathesisScoreInfoMap.get(student.getId() + "_" + item.getKey());
                }
                if (tmp != null) {
                    tmp.setModifyTime(new Date());
                    tmp.setScore(item.getValue());
                    insertList.add(tmp);
                } else {
                    tmp = new DiathesisScoreInfo();
                    tmp.setId(UuidUtils.generateUuid());
                    tmp.setUnitId(unitId);
                    tmp.setType(scoreType);
                    tmp.setScoreTypeId(diathesisScoreType.getId());
                    tmp.setStuId(student.getId());
                    tmp.setObjId(item.getKey());
                    tmp.setScore(item.getValue());
                    tmp.setModifyTime(new Date());
                    insertList.add(tmp);
                }
            }
            arrangeStuId.add(student.getId());
            successCount++;
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if (CollectionUtils.isNotEmpty(errorDataList)) {
            totalCol += 2;

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();

            // 顶部提示样式
            HSSFCellStyle remarkStyle = workbook.createCellStyle();
            HSSFFont headfont = workbook.createFont();
            headfont.setFontHeightInPoints((short) 9);
            headfont.setColor(HSSFFont.COLOR_RED);
            remarkStyle.setFont(headfont);
            remarkStyle.setAlignment(HorizontalAlignment.LEFT);
            remarkStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            remarkStyle.setWrapText(true);

            // 顶部提示
            CellRangeAddress car = new CellRangeAddress(0, 0, 0, totalCol - 1);
            sheet.addMergedRegion(car);
            HSSFRow remarkRow = sheet.createRow(0);
            remarkRow.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
            HSSFCell remarkCell = remarkRow.createCell(0);
            remarkCell.setCellStyle(remarkStyle);
            String remark = getRemark();
            remarkCell.setCellValue(new HSSFRichTextString(remark));
            remarkCell.setCellStyle(remarkStyle);

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            errorStyle.setAlignment(HorizontalAlignment.CENTER);
            errorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            errorStyle.setWrapText(true);
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setWrapText(true);

            // 表头行
            HSSFRow topRow = sheet.createRow(1);
            HSSFRow subRow = sheet.createRow(2);
            HSSFRow bottomRow = sheet.createRow(3);

            CellRangeAddress nameBlock = new CellRangeAddress(1, 3, 0, 0);
            CellRangeAddress codeBlock = new CellRangeAddress(1, 3, 1, 1);

            sheet.addMergedRegion(codeBlock);
            HSSFCell codeCell = topRow.createCell(0);
            codeCell.setCellStyle(style);
            codeCell.setCellValue(new HSSFRichTextString("学号"));

            sheet.addMergedRegion(nameBlock);
            HSSFCell nameCell = topRow.createCell(1);
            nameCell.setCellStyle(style);
            nameCell.setCellValue(new HSSFRichTextString("姓名"));

            int coordinate = 2;
            for (DiathesisComprehensiveDto top : titleList) {
                int topTotalCol = 0;
                for (DiathesisComprehensiveDto sub : top.getSubList()) {
                    topTotalCol += sub.getSubList().size();
                }
                if (topTotalCol > 1) {
                    CellRangeAddress block = new CellRangeAddress(1, 1, coordinate, coordinate + topTotalCol - 1);
                    sheet.addMergedRegion(block);
                }
                HSSFCell cell = topRow.createCell(coordinate);
                cell.setCellStyle(style);
                cell.setCellValue(top.getDiathesisName());
                coordinate += topTotalCol;
            }

            CellRangeAddress errorBlock = new CellRangeAddress(1, 3, coordinate, coordinate);
            sheet.addMergedRegion(errorBlock);
            HSSFCell errorCell = topRow.createCell(coordinate);
            errorCell.setCellStyle(errorStyle);
            errorCell.setCellValue(new HSSFRichTextString("错误数据"));
            coordinate++;

            CellRangeAddress causeBlock = new CellRangeAddress(1, 3, coordinate, coordinate);
            sheet.addMergedRegion(causeBlock);
            HSSFCell causeCell = topRow.createCell(coordinate);
            causeCell.setCellStyle(errorStyle);
            causeCell.setCellValue(new HSSFRichTextString("错误原因"));
            coordinate++;

            coordinate = 2;
            for (DiathesisComprehensiveDto sub : subList) {
                int subTotalCol = sub.getSubList().size();
                if (subTotalCol > 1) {
                    CellRangeAddress block = new CellRangeAddress(2, 2, coordinate, coordinate + subTotalCol - 1);
                    sheet.addMergedRegion(block);
                }
                HSSFCell cell = subRow.createCell(coordinate);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(sub.getDiathesisName()));
                coordinate += subTotalCol;
            }

            coordinate = 2;
            for (DiathesisComprehensiveDto bottom : bottomList) {
                HSSFCell cell = bottomRow.createCell(coordinate++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(bottom.getDiathesisName()));
            }

            for (int i = 0; i < errorDataList.size(); i++) {
                HSSFRow row = sheet.createRow(i + 4);
                String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(i)[0]) - 1);
                for (int j = 0; j < totalCol; j++) {
                    HSSFCell cell = row.createCell(j);
                    if (j < totalCol - 2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[j]));
                        cell.setCellStyle(style);
                    } else if (j == totalCol - 2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[1]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }

        try {
            if (CollectionUtils.isNotEmpty(insertList)) {
                diathesisScoreTypeService.save(diathesisScoreType, insertList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }

        jsonObject.put("totalCount", datas.size());
        jsonObject.put("successCount", successCount);
        jsonObject.put("errorCount", errorDataList.size());
        jsonObject.put("errorExcelPath", errorExcelPath);

        return jsonObject.toJSONString();
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        String gradeId = request.getParameter("gradeId");
        String scoreType = request.getParameter("scoreType");
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, unitId), Semester.class);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        List<DiathesisComprehensiveDto> titleList = getMultiRowTitleList(scoreType);

        int totalCol = 2;
        List<DiathesisComprehensiveDto> subList = new ArrayList<>();
        List<DiathesisComprehensiveDto> bottomList = new ArrayList<>();
        for (DiathesisComprehensiveDto top : titleList) {
            for (DiathesisComprehensiveDto sub : top.getSubList()) {
                subList.add(sub);
                totalCol += sub.getSubList().size();
                for (DiathesisComprehensiveDto bottom : sub.getSubList()) {
                    bottomList.add(bottom);
                }
            }
        }

        // 顶部提示样式
        HSSFCellStyle remarkStyle = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);
        headfont.setColor(HSSFFont.COLOR_RED);
        remarkStyle.setFont(headfont);
        remarkStyle.setAlignment(HorizontalAlignment.LEFT);
        remarkStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        remarkStyle.setWrapText(true);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        // 顶部提示
        CellRangeAddress car = new CellRangeAddress(0, 0, 0, totalCol - 1);
        sheet.addMergedRegion(car);
        HSSFRow remarkRow = sheet.createRow(0);
        remarkRow.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(remarkStyle);
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(remarkStyle);

        // 表头行
        HSSFRow topRow = sheet.createRow(1);
        HSSFRow subRow = sheet.createRow(2);
        HSSFRow bottomRow = sheet.createRow(3);

        CellRangeAddress nameBlock = new CellRangeAddress(1, 3, 0, 0);
        CellRangeAddress codeBlock = new CellRangeAddress(1, 3, 1, 1);

        sheet.addMergedRegion(codeBlock);
        HSSFCell codeCell = topRow.createCell(0);
        codeCell.setCellStyle(style);
        codeCell.setCellValue(new HSSFRichTextString("学号"));

        sheet.addMergedRegion(nameBlock);
        HSSFCell nameCell = topRow.createCell(1);
        nameCell.setCellStyle(style);
        nameCell.setCellValue(new HSSFRichTextString("姓名"));

        int coordinate = 2;
        for (DiathesisComprehensiveDto top : titleList) {
            int topTotalCol = 0;
            for (DiathesisComprehensiveDto sub : top.getSubList()) {
                topTotalCol += sub.getSubList().size();
            }
            if (topTotalCol > 1) {
                CellRangeAddress block = new CellRangeAddress(1, 1, coordinate, coordinate + topTotalCol - 1);
                sheet.addMergedRegion(block);
            }
            HSSFCell cell = topRow.createCell(coordinate);
            cell.setCellStyle(style);
            cell.setCellValue(top.getDiathesisName());
            coordinate += topTotalCol;
        }

        coordinate = 2;
        for (DiathesisComprehensiveDto sub : subList) {
            int subTotalCol = sub.getSubList().size();
            if (subTotalCol > 1) {
                CellRangeAddress block = new CellRangeAddress(2, 2, coordinate, coordinate + subTotalCol - 1);
                sheet.addMergedRegion(block);
            }
            HSSFCell cell = subRow.createCell(coordinate);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(sub.getDiathesisName()));
            coordinate += subTotalCol;
        }

        coordinate = 2;
        for (DiathesisComprehensiveDto bottom : bottomList) {
            HSSFCell cell = bottomRow.createCell(coordinate++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(bottom.getDiathesisName()));
        }

        String fileName = grade.getGradeName() + semester.getAcadyear() + "学年第" + semester.getSemester() + "学期综合素质" + DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.get(scoreType) + "导入";

        ExportUtils.outputData(workbook, fileName, response);
    }

    /**
     * 模板校验
     *
     * @return
     */
    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String scoreType) {
        try {
            if (StringUtils.isBlank(filePath) || StringUtils.isBlank(scoreType)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("参数缺失"));
            }
            List<DiathesisComprehensiveDto> titleList = getMultiRowTitleList(scoreType);
            int totalCol = 2;
            List<String> bottomTitle = new ArrayList<>();
            for (DiathesisComprehensiveDto top : titleList) {
                for (DiathesisComprehensiveDto sub : top.getSubList()) {
                    totalCol += sub.getSubList().size();
                    for (DiathesisComprehensiveDto bottom : sub.getSubList()) {
                        bottomTitle.add(bottom.getDiathesisName());
                    }
                }
            }
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 0, totalCol);
            if (StringUtils.isBlank(datas.get(0)[1])) {
                datas.remove(0);
            }
            int index = 2;
            String[] titleArr = datas.get(2);
            for (String one : bottomTitle) {
                if (!StringUtils.equals(one, titleArr[index++])) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("请勿修改指标行"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("上传文件不符合模板要求"));
        }
        return success(getLoginInfo().getRealName());
    }

    @RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {
        return "填写注意：\n"
                + "1、重复导入将会覆盖之前存在数据;\n"
                + "2、若指标值为分值，范围为0至100，可精确至两位小数";
    }

    private static List<String> acadyearName(String gradeCode, Integer semester) {
        Integer section = Integer.valueOf(gradeCode.substring(0, 1));
        Integer index = Integer.valueOf(gradeCode.substring(1));
        if (BaseConstants.SECTION_HIGH_SCHOOL.equals(section) || BaseConstants.SECTION_COLLEGE.equals(index)) {
            int end = (index - 1) * 2 + semester;
            if (end > 6) {
                end = 6;
            }
            return DiathesisConstant.HIGH_ACADYEAR_LIST.subList(0, end);
        } else if (BaseConstants.SECTION_JUNIOR.equals(section)) {
            int end = (index - 1) * 2 + semester;
            if (end > 6) {
                end = 6;
            }
            return DiathesisConstant.JUNIOR_ACADYEAR_LIST.subList(0, end);
        } else if (BaseConstants.SECTION_PRIMARY.equals(section)) {
            int end = (index - 1) * 2 + semester;
            if (end > 12) {
                end = 12;
            }
            return DiathesisConstant.PRIMARY_ACADYEAR_LIST.subList(0, end);
        }
        return new ArrayList<>();
    }
}
