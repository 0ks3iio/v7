package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.ScoreInputDto;
import net.zdsoft.exammanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.exammanage.data.entity.EmClassLock;
import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 选修成绩处理录入
 */
@Controller
@RequestMapping("/exammanage/scoreOptional")
public class EmScoreOptioinalAction extends BaseAction {

    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseService;
    @Autowired
    private EmClassLockService emClassLockService;
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private TeachClassRemoteService teachClassService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private TeachClassStuRemoteService teachClassStuService;
    @Autowired
    private SemesterRemoteService semesterService;
    @Autowired
    private EmLimitService emLimitService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private EmNotLimitService emNotLimitService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;


    @RequestMapping("/index/page")
    public String showIndex(ModelMap map, String noLimit, String acadyearSearch, String semesterSearch) {
        List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        Semester semester = SUtils.dc(semesterService.getCurrentSemester(2, unitId), Semester.class);
        if (semester == null) {
            return errorFtl(map, "学年学期不存在");
        }
        //判断录分角色 无 普通 管理员 2者都有
        if (noLimit == null) {

            Set<String> teacherIds = emLimitService.findByUnitIdAndExamId(unitId, BaseConstants.ZERO_GUID);
            List<String> noLimitTeacherIds = emNotLimitService.findTeacherIdByUnitId(unitId);
            if (teacherIds.contains(loginInfo.getOwnerId()) && noLimitTeacherIds.contains(loginInfo.getOwnerId())) {
                //有两个录分角色，需要选择一个
                return "/exammanage/scoreNewInput/examSelect.ftl";
            }
            if (StringUtils.isBlank(noLimit) && noLimitTeacherIds.contains(getLoginInfo().getOwnerId())) {
                noLimit = "1";
            }
        }
        //页面参数(初始值)
        if (StringUtils.isBlank(acadyearSearch) || StringUtils.isBlank(semesterSearch)) {
            acadyearSearch = semester.getAcadyear();
            semesterSearch = semester.getSemester() + "";

        }

        //获取所有选修课程
//		String courseJsons = courseRemoteService.findByUnitIdIn(new String[]{unitId}, BaseConstants.TYPE_COURSE_OPTIONAL, new String[]{});
//		List<Course> courseList = SUtils.dt(courseJsons, Course.class);
        //获得该学期下已开设的课程
        List<Course> courseList = null;
        Set<String> courseIdSet = null;
        if ("1".equals(noLimit)) {
            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findByUnitIdAndAcadyearAndSemesterAndSubjectType(unitId, acadyearSearch, semesterSearch, Integer.parseInt(BaseConstants.SUBJECT_TYPE_XX)), ClassTeaching.class);
            courseIdSet = EntityUtils.getSet(classTeachingList, "subjectId");
        } else {
            List<TeachClass> classList = SUtils.dt(teachClassService.findBySearch(unitId, acadyearSearch, semesterSearch, null, null, null), TeachClass.class);
            List<String> classIdList = new ArrayList<String>();
            for (TeachClass teachClass : classList) {
                if (Constant.IS_TRUE_Str.equals(teachClass.getIsUsing()) && !Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())) {
                    classIdList.add(teachClass.getId());
                }
            }
            ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
            searchDto.setExamId(Constant.GUID_ZERO);
            searchDto.setAcadyear(acadyearSearch);
            searchDto.setSemester(semesterSearch);
            searchDto.setUnitId(unitId);
            searchDto.setTeacherId(getLoginInfo().getOwnerId());
            searchDto.setClassIds(classIdList.toArray(new String[0]));
            List<EmLimit> limitList = emLimitService.findBySearchDto(searchDto);
            courseIdSet = EntityUtils.getSet(limitList, "subjectId");
        }
        if (CollectionUtils.isNotEmpty(courseIdSet)) {
            courseList = SUtils.dt(courseRemoteService.findListByIds(courseIdSet.toArray(new String[0])), Course.class);
        }

        map.put("acadyearList", acadyearList);
        map.put("acadyearSearch", acadyearSearch);
        map.put("semesterSearch", semesterSearch);

        map.put("unitId", unitId);
//		map.put("type", 1);
        map.put("courseList", courseList);
        map.put("noLimit", noLimit);
        return "/exammanage/optionalScoreInfo/optionScoreIndex.ftl";
    }

    @RequestMapping("/updateClass")
    @ResponseBody
    public String updateClass(TeachClass teachClass, String noLimit) {
        String teachClassJsons = teachClassService.findTeachClassList(teachClass.getUnitId(), teachClass.getAcadyear(),
                teachClass.getSemester(), teachClass.getCourseId());
        List<TeachClass> teachClassList = SUtils.dt(teachClassJsons, TeachClass.class);

        Iterator<TeachClass> iterator = teachClassList.iterator();
        while (iterator.hasNext()) {
            if (Constant.IS_TRUE_Str.equals(iterator.next().getIsUsingMerge())) {
                iterator.remove();
            }
        }
        List<String> noLimitTeacherIds = emNotLimitService.findTeacherIdByUnitId(teachClass.getUnitId());
        if (StringUtils.isBlank(noLimit) && noLimitTeacherIds.contains(getLoginInfo().getOwnerId())) {
            noLimit = "1";
        }
        if (CollectionUtils.isNotEmpty(teachClassList) && !"1".equals(noLimit)) {
            String[] teachClassIds = EntityUtils.getSet(teachClassList, "id").toArray(new String[0]);
            ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
            searchDto.setExamId(Constant.GUID_ZERO);
            searchDto.setAcadyear(teachClass.getAcadyear());
            searchDto.setSemester(teachClass.getSemester());
            searchDto.setUnitId(teachClass.getUnitId());
            searchDto.setSubjectId(teachClass.getCourseId());
            searchDto.setClassIds(teachClassIds);
            searchDto.setTeacherId(getLoginInfo().getOwnerId());

            List<EmLimit> limitList = emLimitService.findBySearchDto(searchDto);
            if (CollectionUtils.isNotEmpty(limitList)) {
                Set<String> limitClassIds = new HashSet<>();
                for (EmLimit emLimit : limitList) {
                    String[] classIds = emLimit.getClassIds();
                    if (classIds != null && classIds.length > 0) {
                        limitClassIds.addAll(Arrays.asList(classIds));
                    }
                }
                teachClassList = SUtils.dt(teachClassService.findListByIds(limitClassIds.toArray(new String[0])), TeachClass.class);
            } else {
                teachClassList = new ArrayList<TeachClass>();
            }
        }

        teachClassJsons = SUtils.s(teachClassList);
        boolean success = false;
        String mess = "";
        String businessValue = "\"\"";
        if (CollectionUtils.isNotEmpty(teachClassList)) {
            success = true;
            mess = "成功";
            businessValue = teachClassJsons;
        } else {
            mess = "暂无班级";
        }
        String result = "{\"success\":" + success + ",\"message\":\"" + mess + "\",\"businessValue\":" + businessValue + "}";
        return result;
    }

    @RequestMapping("/tablist")
    public String showList(EmScoreInfo scoreInfo, String noLimit, ModelMap map) {
        TeachClass parentClass = SUtils.dc(teachClassService.findOneById(scoreInfo.getTeachClassId()), TeachClass.class);
        String[] classIds = null;
        List<EmScoreInfo> scoreInfList = null;
        Map<String, TeachClass> teachClassMap = new HashMap<String, TeachClass>();
        if (Constant.IS_TRUE_Str.equals(parentClass.getIsMerge())) {
            List<TeachClass> childTeachClassList = SUtils.dt(teachClassService.findByParentIds(new String[]{scoreInfo.getTeachClassId()}), TeachClass.class);
            teachClassMap = EntityUtils.getMap(childTeachClassList, "id");
            Set<Object> classIdSet = EntityUtils.getSet(childTeachClassList, "id");
            classIds = classIdSet.toArray(new String[0]);
            scoreInfList = emScoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(), classIds);
        } else {
            teachClassMap.put(parentClass.getId(), parentClass);
            scoreInfList = emScoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(), scoreInfo.getSubjectId(), scoreInfo.getTeachClassId());
            classIds = new String[]{scoreInfo.getTeachClassId()};
        }

        Map<String, EmScoreInfo> stuIdScoreMap = EntityUtils.getMap(scoreInfList, "studentId");

        LoginInfo loginInfo = getLoginInfo();
        List<ScoreInputDto> dtoList = new ArrayList<>();
        String viewName = "/exammanage/optionalScoreInfo/optionalScoreInfoList.ftl";
        //此班级成绩是否已经被提交 锁定
        EmClassLock classLock = emClassLockService.findBySchoolIdAndExamIdAndSubjectIdAndClassId
                (loginInfo.getUnitId(), BaseConstants.ZERO_GUID, scoreInfo.getSubjectId(), scoreInfo.getTeachClassId());
        if (classLock == null || StringUtils.isBlank(classLock.getId())) {
            classLock = new EmClassLock();
            classLock.setId(UuidUtils.generateUuid());
            classLock.setClassId(scoreInfo.getTeachClassId());
            classLock.setClassType(ExammanageConstants.CLASS_TYPE2);
            classLock.setCreationTime(new Date());
            classLock.setExamId(BaseConstants.ZERO_GUID);
            classLock.setLockState(Constant.IS_FALSE_Str);
            classLock.setSchoolId(loginInfo.getUnitId());
            classLock.setSubjectId(scoreInfo.getSubjectId());
            emClassLockService.save(classLock);
        }

        List<Student> realStudentList = null;
        //已经提交
        if (Constant.IS_TRUE_Str.equals(classLock.getLockState())) {
            String[] stuIds = EntityUtils.getSet(scoreInfList, "studentId").toArray(new String[0]);
            realStudentList = SUtils.dt(studentService.findListByIds(stuIds), Student.class);

        } else {
            //教学班的真实学生
            List<TeachClassStu> teachStuList = SUtils.dt(teachClassStuService.findByClassIds(classIds), TeachClassStu.class);
            String[] realStuIds = EntityUtils.getSet(teachStuList, "studentId").toArray(new String[0]);
            realStudentList = SUtils.dt(studentService.findListByIds(realStuIds), Student.class);
            Map<String, Student> realStuMap = EntityUtils.getMap(realStudentList, "id");
            //未提交
            List<EmScoreInfo> toSaveList = new ArrayList<>();
            for (TeachClassStu teachStu : teachStuList) {
                if (stuIdScoreMap.get(teachStu.getStudentId()) == null) {
                    //数据库中无数据
                    EmScoreInfo scoreInfo2 = new EmScoreInfo();
                    scoreInfo2.setId(UuidUtils.generateUuid());
                    scoreInfo2.setAcadyear(scoreInfo.getAcadyear());
                    scoreInfo2.setSemester(scoreInfo.getSemester());
                    scoreInfo2.setUnitId(scoreInfo.getUnitId());
                    scoreInfo2.setExamId(BaseConstants.ZERO_GUID);
                    if (Constant.IS_TRUE_Str.equals(parentClass.getIsMerge())) {
                        scoreInfo2.setTeachClassId(teachStu.getClassId());
                        scoreInfo2.setSubjectId(teachClassMap.get(teachStu.getClassId()) == null ? "" : teachClassMap.get(teachStu.getClassId()).getCourseId());
                    } else {
                        scoreInfo2.setTeachClassId(scoreInfo.getTeachClassId());
                        scoreInfo2.setSubjectId(scoreInfo.getSubjectId());
                    }
                    scoreInfo2.setStudentId(teachStu.getStudentId());
                    scoreInfo2.setOperatorId(loginInfo.getUserId());
                    scoreInfo2.setInputType(ExammanageConstants.ACHI_SCORE);
                    scoreInfo2.setClassId(realStuMap.get(teachStu.getStudentId()).getClassId());
                    scoreInfo2.setScoreStatus("0");
                    //scoreInfo2.setToScore("0");

                    toSaveList.add(scoreInfo2);
                }
            }

            boolean doubleRequest = false;  //判断是否需要二次查询成绩信息
            //保存新增的学生成绩
            if (CollectionUtils.isNotEmpty(toSaveList)) {
                doubleRequest = true;
                emScoreInfoService.saveAll(toSaveList.toArray(new EmScoreInfo[0]));
            }
            //删除已经不在的学生成绩
            List<EmScoreInfo> toDeleteList = new ArrayList<>();
            for (EmScoreInfo scoreInfo2 : scoreInfList) {
                if (realStuMap.get(scoreInfo2.getStudentId()) == null) {
                    //已经被教学班级删除
                    toDeleteList.add(scoreInfo2);
                }
            }
            if (CollectionUtils.isNotEmpty(toDeleteList)) {
                doubleRequest = true;
                emScoreInfoService.deleteAll(toDeleteList.toArray(new EmScoreInfo[0]));
            }

            //如果此班级的学生有变动，再查询一次
            if (doubleRequest) {
                if (Constant.IS_TRUE_Str.equals(parentClass.getIsMerge())) {
                    scoreInfList = emScoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(), classIds);
                } else {
                    scoreInfList = emScoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(),
                            scoreInfo.getSubjectId(), scoreInfo.getTeachClassId());
                }

            }
        }
        //若还是没有学生，则直接返回
        if (CollectionUtils.isEmpty(scoreInfList)) {
            return viewName;
        }

        //已经存在成绩表里的学生数据
        Map<String, Student> stuMap = EntityUtils.getMap(realStudentList, "id");
        //从成绩表里获取班级数据 --> 修改为根据学生来获取班级信息，因为行政班学生会调整，成绩表里的信息会失效
        List<Clazz> clazzList = SUtils.dt(classService.findListByIds(EntityUtils.getSet(realStudentList, "classId").toArray(new String[]{})), Clazz.class);
        Map<String, Clazz> clazMap = EntityUtils.getMap(clazzList, "id");
        List<EmScoreInfo> toUpdateScoreList = new ArrayList<>();
        for (EmScoreInfo scoreinfo : scoreInfList) {
            ScoreInputDto dto = new ScoreInputDto();
            if (teachClassMap.containsKey(scoreinfo.getTeachClassId()) && teachClassMap.get(scoreinfo.getTeachClassId()).getFullMark() != null) {
                dto.setFullMark(teachClassMap.get(scoreinfo.getTeachClassId()).getFullMark());
            } else
                dto.setFullMark(100);
            dto.setScoreId(scoreinfo.getId());
            dto.setStuId(scoreinfo.getStudentId());
            dto.setScoreStatus(scoreinfo.getScoreStatus());
            dto.setScore(scoreinfo.getScore());
            //显示学分 打印成绩表时使用
            dto.setToScore(scoreinfo.getToScore());
            //页面显示信息
            dto.setStuCode(stuMap.get(scoreinfo.getStudentId()).getStudentCode());
            //获取 年级信息 和 行政班 的信息
            Clazz clazz = clazMap.get(stuMap.get(scoreinfo.getStudentId()).getClassId());
            if (clazz == null)
                continue;
            dto.setClassName(clazz.getClassNameDynamic());
            dto.setStuName(stuMap.get(scoreinfo.getStudentId()).getStudentName());
            dto.setUnitiveCode(stuMap.get(scoreinfo.getStudentId()).getUnitiveCode());
            dtoList.add(dto);

            if (!clazz.getId().equals(scoreinfo.getClassId())) {
                //学生所在行政班发生了变化（比如发生行政班重组）
                scoreinfo.setClassId(clazz.getId());
                toUpdateScoreList.add(scoreinfo);
            }
            sortDtoList(dtoList);
        }
        if (CollectionUtils.isNotEmpty(toUpdateScoreList)) {
            emScoreInfoService.saveAllEntitys(toUpdateScoreList.toArray(new EmScoreInfo[0]));
        }

        //此人是否有录分权限
        int hasEditRole = emScoreInfoService.getEditRole(null, scoreInfo.getTeachClassId(), scoreInfo.getSubjectId(), scoreInfo.getUnitId(), scoreInfo.getAcadyear(),
                scoreInfo.getSemester(), loginInfo.getOwnerId());
        if (hasEditRole == 2) {
            if ("1".equals(noLimit)) {
                //无限制 为录分管理员
                hasEditRole = 0;
            }
            if ("0".equals(noLimit)) {
                //有限制 为普通录分人员
                hasEditRole = 1;
            }
        }
        // 考虑此人有 普通 和管理员两个角色，但是 1.在这个班级上没有普通录分角色，2.同时 他又选择以普通录分权限的角色进入，所以他没有这个班级的录分权限。
        if (hasEditRole == 0 && "0".equals(noLimit)) {
            hasEditRole = -1;
        }

        map.put("hasEditRole", hasEditRole);

        //如果成绩已经锁定，需要打印成绩表 信息
        TeachClass teachClass = SUtils.dc(teachClassService.findOneById(scoreInfo.getTeachClassId()), TeachClass.class);
        if (Constant.IS_TRUE_Str.equals(classLock.getLockState())) {
            int rowCount = (dtoList.size() + 1) / 2;
            map.put("rowCount", rowCount);
            map.put("stuCount", dtoList.size());

            //获取学校名
            Unit unit = SUtils.dc(unitRemoteService.findOneById(scoreInfo.getUnitId()), Unit.class);
            String acadyear = teachClass.getAcadyear();
            String semester = teachClass.getSemester();
            String semesterName = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", semester), McodeDetail.class).getMcodeContent();

            String className = null;
            //String gradeName = SUtils.dc(gradeRemoteService.findById(teachClass.getGradeId()),Grade.class).getGradeName();
            className = teachClass.getName();

            Course course = SUtils.dc(courseService.findOneById(scoreInfo.getSubjectId()), Course.class);

            map.put("unitName", unit.getUnitName());
            map.put("acadyear", acadyear);
            map.put("semesterName", semesterName);
            map.put("subjectName", course.getSubjectName());
            map.put("className", className);
        }

        map.put("classLock", classLock);
        //获取满分值

        map.put("dtoList", dtoList);
        map.put("acadyear", scoreInfo.getAcadyear());
        map.put("semester", scoreInfo.getSemester());
        map.put("unitId", scoreInfo.getUnitId());
        map.put("subjectId", scoreInfo.getSubjectId());
        map.put("teachClassId", scoreInfo.getTeachClassId());

        map.put("fullMark", teachClass.getFullMark());

        return viewName;
    }

    @ResponseBody
    @ControllerInfo(value = "修改锁定")
    @RequestMapping("/lock")
    public String lockTheExamScore(String classLockId, String isLock) {
        try {
            EmClassLock classLock = emClassLockService.findOne(classLockId);
            classLock.setLockState(isLock);
            emClassLockService.save(classLock);
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改失败");
        }
        return success("修改成功");
    }

    private void sortDtoList(List<ScoreInputDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        Collections.sort(dtoList, new Comparator<ScoreInputDto>() {
            @Override
            public int compare(ScoreInputDto o1, ScoreInputDto o2) {
                if (StringUtils.isNotBlank(o1.getUnitiveCode()) && StringUtils.isNotBlank(o2.getUnitiveCode())) {
                    return o1.getUnitiveCode().compareTo(o2.getUnitiveCode());
                }
                return 0;
            }
        });
    }

    @RequestMapping("/saveAll")
    @ResponseBody
    public String saveAll(ScoreInputDto dto) {
        String subjectId = dto.getSubjectId();
        String unitId = getLoginInfo().getUnitId();
        String teachClassId = dto.getTeachClassId();
        TeachClass teachClass = SUtils.dc(teachClassService.findOneById(teachClassId), TeachClass.class);
        List<EmScoreInfo> dtoList = dto.getDtoList();
        Map<String, EmScoreInfo> scoreinfoMap = EntityUtils.getMap(dtoList, "id");

        //先检查是否已经被锁定
        EmClassLock classLock = emClassLockService.findBySchoolIdAndExamIdAndSubjectIdAndClassId
                (unitId, BaseConstants.ZERO_GUID, subjectId, teachClassId);
        if (Constant.IS_TRUE_Str.equals(classLock.getLockState())) {
            return error("此班级数据已经锁定，无法再修改");
        }

        //有没有数据要保存
        if (dto == null || CollectionUtils.isEmpty(dtoList)) {
            return error("没有能添加的数据");
        }

        Map<String, String> teachStuClassMap = null;
        Map<String, TeachClass> teachClassMap = null;
        List<EmScoreInfo> scoreInfList = null;
        if (Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())) {
            List<TeachClass> childTeachClassList = SUtils.dt(teachClassService.findByParentIds(new String[]{teachClassId}), TeachClass.class);
            for (TeachClass childTeachClass : childTeachClassList) {
                if (childTeachClass.getPassMark() == null) childTeachClass.setPassMark(60);
            }
            teachClassMap = EntityUtils.getMap(childTeachClassList, "id");
            List<TeachClassStu> teachClassStuList = SUtils.dt(teachClassStuService.findByClassIds(EntityUtils.getSet(childTeachClassList, "id").toArray(new String[0])), TeachClassStu.class);
            teachStuClassMap = EntityUtils.getMap(teachClassStuList, "studentId", "classId");
            scoreInfList = emScoreInfoService.findOptionalCourseScore(unitId, EntityUtils.getSet(childTeachClassList, "id").toArray(new String[0]));
        } else {
            scoreInfList = emScoreInfoService.findOptionalCourseScore(unitId, subjectId, teachClassId);
        }

        try {
            if (CollectionUtils.isEmpty(scoreInfList)) {
                //数据库中没有数据 执行添加操作
                List<Student> studentList = SUtils.dt(studentService.findListByIds(EntityUtils.getSet(dtoList, "studentId").toArray(new String[0])), Student.class);
                Map<String, Student> stuMap = EntityUtils.getMap(studentList, "id");
                String acadyear = teachClass.getAcadyear();
                String semester = teachClass.getSemester();
                for (EmScoreInfo scoreInfo : dtoList) {
                    if (StringUtils.isBlank(scoreInfo.getId())) {
                        scoreInfo.setId(UuidUtils.generateUuid());
                    }
                    scoreInfo.setAcadyear(acadyear);
                    scoreInfo.setSemester(semester);
                    scoreInfo.setUnitId(unitId);
                    scoreInfo.setExamId(BaseConstants.ZERO_GUID);
                    if (Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())) {
                        scoreInfo.setTeachClassId(teachStuClassMap.get(scoreInfo.getStudentId()));
                        scoreInfo.setSubjectId(teachClassMap.get(scoreInfo.getTeachClassId()) == null ? "" : teachClassMap.get(scoreInfo.getTeachClassId()).getCourseId());
                    } else {
                        scoreInfo.setTeachClassId(teachClassId);
                        scoreInfo.setSubjectId(subjectId);
                    }
                    scoreInfo.setStudentId(scoreInfo.getStudentId());
                    scoreInfo.setOperatorId(getLoginInfo().getUserId());
                    scoreInfo.setInputType(ExammanageConstants.ACHI_SCORE);
                    scoreInfo.setClassId(stuMap.get(scoreInfo.getStudentId()).getClassId());
                }
                scoreInfList = dtoList;
            } else {
                //有数据执行修改操作
                for (EmScoreInfo scoreInfo : scoreInfList) {
                    EmScoreInfo scoreInfo2 = scoreinfoMap.get(scoreInfo.getId());
                    scoreInfo.setScore(scoreInfo2.getScore());
                    scoreInfo.setScoreStatus(scoreInfo2.getScoreStatus());
                    scoreInfo.setOperatorId(getLoginInfo().getUserId());
                }
            }
            //赋予学分
            if (Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())) {
                for (EmScoreInfo scoreInfo : scoreInfList) {
                    TeachClass inTeachClass = teachClassMap.get(scoreInfo.getTeachClassId());
                    if (inTeachClass != null) {
                        if (Float.parseFloat(scoreInfo.getScore()) >= inTeachClass.getPassMark()) {
                            scoreInfo.setToScore(inTeachClass.getCredit() == null ? null : (inTeachClass.getCredit() + ""));
                        } else {
                            scoreInfo.setToScore("0");
                        }
                    }
                }
            } else {
                // 学分 及格分将要改动 有教学班获取
                Integer passMark = teachClass.getPassMark();
                if (passMark == null) {
                    passMark = 60;
                }
                Integer credit = teachClass.getCredit();
                for (EmScoreInfo scoreInfo : scoreInfList) {
                    if (Float.parseFloat(scoreInfo.getScore()) >= passMark) {
                        scoreInfo.setToScore(credit == null ? null : (credit + ""));
                    } else {
                        scoreInfo.setToScore("0");
                    }
                }
            }
            emScoreInfoService.saveAllEntitys(scoreInfList.toArray(new EmScoreInfo[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("-1", "操作失败", e.getMessage());
        }

        return success("操作成功");
    }
}
