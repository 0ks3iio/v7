package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.ExamSubjectDto;
import net.zdsoft.exammanage.data.dto.ScoreInputDto;
import net.zdsoft.exammanage.data.dto.ScoreInputSearchDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 成绩处理录入
 */
@Controller
@RequestMapping("/exammanage")
public class EmScoreInputAction extends EmExamCommonAction {
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmLimitService emLimitService;
    @Autowired
    private EmLimitDetailService emLimitDetailService;
    @Autowired
    private EmNotLimitService emNotLimitService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmClassLockService emClassLockService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private TeachClassRemoteService teachClassService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private TeacherRemoteService teachService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;


    @RequestMapping("/scoreNewInput/index/page")
    @ControllerInfo(value = "录入成绩")
    public String showIndex(ModelMap map, ScoreInputSearchDto searchDto, String noLimit) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        if (sem == null) {
            return errorFtl(map, "学年学期不存在");
        }
        map.put("type", "1");
        if (StringUtils.isBlank(noLimit)) {
            Set<String> teacherIds = emLimitService.findByUnitIdAndExamIdNot(unitId, BaseConstants.ZERO_GUID);
            List<String> noLimitTeacherIds = emNotLimitService.findTeacherIdByUnitId(unitId);
            boolean flagLimit = teacherIds.contains(loginInfo.getOwnerId());
            boolean flagNoLimit = noLimitTeacherIds.contains(loginInfo.getOwnerId());
            if (flagLimit && flagNoLimit) {//有两个录分角色，需要选择一个
                return "/exammanage/scoreNewInput/examSelect.ftl";
            } else if (flagNoLimit) {//只有管理员权限
                noLimit = "1";
            } else if (flagLimit) {//只有普通权限
                noLimit = "0";
            } else {
                return promptFlt(map, "无录入权限");
            }
        }
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        //页面参数(初始值)
        if (StringUtils.isBlank(searchDto.getAcadyear())) {
            searchDto.setAcadyear(sem.getAcadyear());
            searchDto.setSemester(sem.getSemester() + "");
			/*searchDto.setAcadyear("2017-2018");
			searchDto.setSemester("2");*/
        }
        map.put("acadyearList", acadyearList);
        map.put("gradeList", gradeList);
        map.put("searchDto", searchDto);
        map.put("unitId", unitId);
        map.put("noLimit", noLimit);
        return "/exammanage/scoreNewInput/examManage.ftl";
    }

    @ResponseBody
    @RequestMapping("/scoreNewInput/setNumSpan")
    @ControllerInfo(value = "显示录入人数")
    public String setNumSpan(ScoreInputSearchDto searchDto, String noLimit) {
        JSONArray array = new JSONArray();
        String examId = searchDto.getExamId();
        String subjectId = searchDto.getSubjectId();
        try {
            LoginInfo loginInfo = getLoginInfo();
            List<Clazz> xzbList = new ArrayList<>();
            List<TeachClass> jxbList = new ArrayList<>();
            setList(xzbList, jxbList, searchDto, loginInfo, noLimit);
            //获取行政班以及教学班的学生
            List<Student> studentList = SUtils.dt(studentService.findByClassIds(xzbList.stream().map(Clazz::getId).collect(Collectors.toSet())
                    .toArray(new String[]{})), new TR<List<Student>>() {
            });

            List<TeachClassStu> teachStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(jxbList.stream().map(TeachClass::getId).collect(Collectors.toSet())
                    .toArray(new String[]{})), new TR<List<TeachClassStu>>() {
            });
            //不排考学生
            Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, loginInfo.getUnitId(), ExammanageConstants.FILTER_TYPE1);
            //行政班对应的学生map
            Map<String, List<Student>> studentListMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(studentList)) {
                for (Student stu : studentList) {
                    if (!studentListMap.containsKey(stu.getClassId())) {
                        studentListMap.put(stu.getClassId(), new ArrayList<>());
                    }
                    studentListMap.get(stu.getClassId()).add(stu);
                }
            }
            //教学班对应的学生map
            Map<String, List<TeachClassStu>> teachStuListMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(teachStuList)) {
                for (TeachClassStu teachStu : teachStuList) {
                    if (!teachStuListMap.containsKey(teachStu.getClassId())) {
                        teachStuListMap.put(teachStu.getClassId(), new ArrayList<>());
                    }
                    teachStuListMap.get(teachStu.getClassId()).add(teachStu);
                }
            }
            Set<String> studentIds = emScoreInfoService.findByUnitIdAndExamIdAndSubjectId(loginInfo.getUnitId(), examId, subjectId)
                    .stream().map(EmScoreInfo::getStudentId).collect(Collectors.toSet());
            JSONObject json = null;
            int allNum = 0;
            int scoreNum = 0;
            if (CollectionUtils.isNotEmpty(xzbList)) {
                for (Clazz clazz : xzbList) {
                    allNum = 0;
                    scoreNum = 0;
                    json = new JSONObject();
                    List<Student> stuList = studentListMap.get(clazz.getId());
                    if (CollectionUtils.isNotEmpty(stuList)) {
//	    				allNum=stuList.size();
                        for (Student stu : stuList) {
                            if (!filterMap.containsKey(stu.getId())) {
                                allNum++;
                            }
                            if (studentIds.contains(stu.getId())) {
                                scoreNum++;
                            }
                        }
                    }
                    json.put("allNum", allNum);
                    json.put("scoreNum", scoreNum);
                    array.add(json);
                }
            }
            if (CollectionUtils.isNotEmpty(jxbList)) {
                for (TeachClass teachClass : jxbList) {
                    allNum = 0;
                    scoreNum = 0;
                    json = new JSONObject();
                    List<TeachClassStu> inteachStuList = teachStuListMap.get(teachClass.getId());
                    if (CollectionUtils.isNotEmpty(inteachStuList)) {
//	    				allNum=inteachStuList.size();
                        for (TeachClassStu inteach : inteachStuList) {
                            if (!filterMap.containsKey(inteach.getStudentId())) {
                                allNum++;
                            }
                            if (studentIds.contains(inteach.getStudentId())) {
                                scoreNum++;
                            }
                        }
                    }
                    json.put("allNum", allNum);
                    json.put("scoreNum", scoreNum);
                    array.add(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array.toJSONString();

    }

    public List<ScoreInputDto> getInputList(List<Clazz> xzbList, List<TeachClass> jxbList, ScoreInputSearchDto searchDto, LoginInfo loginInfo) {
        //获取锁定科目对应的班级
        String subjectId = searchDto.getSubjectId();
        Map<String, EmClassLock> classClockMap = emClassLockService.findBySchoolIdAndExamIdAndSubjectId(loginInfo.getUnitId(), searchDto.getExamId(), subjectId)
                .stream().collect(Collectors.toMap(EmClassLock::getClassId, Function.identity()));
        List<ScoreInputDto> inputDtoList = new ArrayList<>();
        ScoreInputDto inputDto = null;
        Set<String> classIds = xzbList.stream().map(Clazz::getId).collect(Collectors.toSet());
        Set<String> teachClassIds = jxbList.stream().map(TeachClass::getId).collect(Collectors.toSet());
        Map<String, Set<String>> classIdOfTeaIdMap = emSubjectInfoService.findTeacher(searchDto.getExamId(), subjectId, loginInfo.getUnitId(), classIds, teachClassIds);
        Map<String, String> teacherNameMap = SUtils.dt(teachService.findByUnitId(loginInfo.getUnitId()), new TR<List<Teacher>>() {
        }).stream().collect(Collectors.toMap(Teacher::getId, Teacher::getTeacherName));


        if (CollectionUtils.isNotEmpty(xzbList)) {//行政班是否没有区别
            for (Clazz clazz : xzbList) {
                inputDto = new ScoreInputDto();
                inputDto.setClassId(clazz.getId());
                inputDto.setClassName(clazz.getClassNameDynamic());
                inputDto.setClassType("1");//行政班
                EmClassLock classClock = classClockMap.get(clazz.getId());
                if (classClock == null || "0".equals(classClock.getLockState())) {
                    inputDto.setLock(false);
                } else
                    inputDto.setLock(true);
                inputDto.setSubjectId(subjectId);
                //inputDto.setSubjectName(subjectName);
                //inputDto.setSubmit(isSubmit);
                Set<String> teacherIds = classIdOfTeaIdMap.get(clazz.getId());
                if (CollectionUtils.isNotEmpty(teacherIds)) {
                    StringBuilder teacherNames = new StringBuilder();
                    int i = 0;
                    for (String teacherId : teacherIds) {
                        if (teacherNameMap.containsKey(teacherId)) {
                            if (i != 0) {
                                teacherNames.append(",");
                            }
                            teacherNames.append(teacherNameMap.get(teacherId));
                            i++;
                        }
                    }
                    inputDto.setTeacherNames(teacherNames.toString());
                }
                inputDtoList.add(inputDto);
            }
        }
        if (CollectionUtils.isNotEmpty(jxbList)) {//教学班  存在该7选3科目全部学校是选考或学考的情况   此时需根据考试范围来判断
            for (TeachClass teachClass : jxbList) {
                inputDto = new ScoreInputDto();
                inputDto.setClassId(teachClass.getId());
                inputDto.setClassName(teachClass.getName());
                inputDto.setClassType("2");//教学班
                EmClassLock classClock = classClockMap.get(teachClass.getId());
                if (classClock == null || "0".equals(classClock.getLockState())) {
                    inputDto.setLock(false);
                } else
                    inputDto.setLock(true);
                inputDto.setSubjectId(subjectId);
                //inputDto.setSubjectName(subjectName);
                Set<String> teacherIds = classIdOfTeaIdMap.get(teachClass.getId());
                if (CollectionUtils.isNotEmpty(teacherIds)) {
                    StringBuilder teacherNames = new StringBuilder();
                    int i = 0;
                    for (String teacherId : teacherIds) {
                        if (teacherNameMap.containsKey(teacherId)) {
                            if (i != 0) {
                                teacherNames.append(",");
                            }
                            teacherNames.append(teacherNameMap.get(teacherId));
                            i++;
                        }
                    }
                    inputDto.setTeacherNames(teacherNames.toString());
                }
                inputDtoList.add(inputDto);
            }
        }
        return inputDtoList;
    }

    @RequestMapping("/scoreNewInput/manageList/page")
    @ControllerInfo(value = "查看成绩锁定页面")
    public String showManageList(ModelMap map, ScoreInputSearchDto searchDto, String noLimit) {
        LoginInfo loginInfo = getLoginInfo();
        String subjectId = searchDto.getSubjectId();
        if (StringUtils.isBlank(subjectId)) {
            return "/exammanage/scoreNewInput/examManageList.ftl";
        }

        List<Clazz> xzbList = new ArrayList<>();
        List<TeachClass> jxbList = new ArrayList<>();
        setList(xzbList, jxbList, searchDto, loginInfo, noLimit);
        //获取锁定科目对应的班级
        List<ScoreInputDto> inputDtoList = getInputList(xzbList, jxbList, searchDto, loginInfo);

        map.put("inputDtoList", inputDtoList);
        map.put("searchDto", searchDto);
        map.put("noLimit", noLimit);
        return "/exammanage/scoreNewInput/examManageList.ftl";
    }

    @ResponseBody
    @ControllerInfo(value = "修改锁定")
    @RequestMapping("/scoreNewInput/changeLockStatus")
    public String lockTheExamScore(String examId, String subjectId, String classIdType, String status) {
        try {
            LoginInfo loginInfo = getLoginInfo();
            String[] idType = classIdType.split("_");
            String classId = idType[0];
            String classType = idType[1];
            EmClassLock classLock = emClassLockService.findBySchoolIdAndExamIdAndSubjectIdAndClassId
                    (loginInfo.getUnitId(), examId, subjectId, classId);
            if (classLock == null || StringUtils.isBlank(classLock.getId())) {
                classLock = new EmClassLock();
                classLock.setId(UuidUtils.generateUuid());
                classLock.setClassId(classId);
                classLock.setClassType(classType);
                classLock.setCreationTime(new Date());
                classLock.setExamId(examId);
                classLock.setLockState(status);
                classLock.setSchoolId(loginInfo.getUnitId());
                classLock.setSubjectId(subjectId);
                emClassLockService.save(classLock);
            } else {
                classLock.setLockState(status);
                classLock.setModifyTime(new Date());
                emClassLockService.save(classLock);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改失败");
        }
        return success("修改成功");
    }

    @RequestMapping("/scoreNewInput/scoreIndex/page")
    @ControllerInfo(value = "录入编辑成绩index")
    public String showScoreIndex(ModelMap map, ScoreInputSearchDto searchDto, String noLimit) {

        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        if (sem == null) {
            return errorFtl(map, "学年学期不存在");
        }
        map.put("type", 1);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {
        });
        map.put("acadyearList", acadyearList);
        map.put("gradeList", gradeList);
        map.put("searchDto", searchDto);
        map.put("noLimit", noLimit);
        map.put("unitId", getLoginInfo().getUnitId());
        return "/exammanage/scoreNewInput/examScoreIndex.ftl";
    }

    @RequestMapping("/scoreNewInput/tablist/page")
    @ControllerInfo(value = "科目tab")
    public String showTablist(ModelMap map, ScoreInputSearchDto searchDto, String noLimit) {
        LoginInfo loginInfo = getLoginInfo();
        Set<String> subjectIds = new HashSet<>();
        EmExamInfo exam = emExamInfoService.findOne(searchDto.getExamId());
        List<EmSubjectInfo> infoList = null;
        Map<String, String> typeMap = null;
        Map<String, String> course73Map = new HashMap<>();
        if (exam != null && "1".equals(exam.getIsgkExamType())) {//新高考模式 分选考学考
            typeMap = new HashMap<>();
            infoList = emSubjectInfoService.findByExamId(searchDto.getExamId());
            if (CollectionUtils.isNotEmpty(infoList)) {
                for (EmSubjectInfo subjectInfo : infoList) {
                    typeMap.put(subjectInfo.getSubjectId(), subjectInfo.getGkSubType());
                }
            }
            //map.put("typeMap", typeMap);

            List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(loginInfo.getUnitId()), new TR<List<Course>>() {
            });
            course73Map = courseList73.stream().collect(Collectors.toMap(Course::getId, Course::getSubjectCode));
            //map.put("course73Map", course73Map);
        }
        if ("1".equals(searchDto.getClassType())) {
            if (!"1".equals(noLimit)) {//普通
                if (StringUtils.isNotBlank(searchDto.getClassId()))
                    subjectIds = EntityUtils.getSet(emLimitService.findByExamIdST(searchDto.getExamId(), loginInfo.getOwnerId(), null, searchDto.getClassId()), EmLimit::getSubjectId);
            } else {
                //List<EmSubjectInfo> infoList=emSubjectInfoService.findByExamId(searchDto.getExamId());
                if (infoList == null)
                    infoList = emSubjectInfoService.findByExamId(searchDto.getExamId());
                Set<String> subIds = EntityUtils.getSet(infoList, EmSubjectInfo::getSubjectId);

                List<ClassTeaching> list = SUtils.dt(classTeachingRemoteService.findBySearch(loginInfo.getUnitId(), searchDto.getAcadyear(),
                        searchDto.getSemester(), new String[]{searchDto.getClassId()}, 0, 0), new TR<List<ClassTeaching>>() {
                });
                if (CollectionUtils.isNotEmpty(list)) {
                    for (ClassTeaching item : list) {
                        if (subIds.contains(item.getSubjectId())) {
                            subjectIds.add(item.getSubjectId());
                        }
                    }
                }
            }
        } else {
            TeachClass teachClass = SUtils.dc(teachClassService.findOneById(searchDto.getClassId()), TeachClass.class);
            subjectIds.add(teachClass == null ? "" : teachClass.getCourseId());
        }
        if (subjectIds == null)
            subjectIds = new HashSet<>();
        if (!subjectIds.contains(searchDto.getSubjectId()))
            searchDto.setSubjectId(null);
//    	List<Course> courseList=new ArrayList<>();
        if (exam != null) {
            searchDto.setAcadyear(exam.getAcadyear());
            searchDto.setSemester(exam.getSemester());
            getSubjectList(map, typeMap, course73Map, subjectIds, searchDto);
        }
//		map.put("courseList", courseList);
        map.put("searchDto", searchDto);
        map.put("subjectId", searchDto.getSubjectId());
        return "/exammanage/scoreNewInput/examScoreTablist.ftl";
    }

    @RequestMapping("/scoreNewInput/onelist/page")
    @ControllerInfo(value = "科目onelist")
    public String showOnelist(ModelMap map, ScoreInputSearchDto searchDto, String noLimit, String subType) {
        LoginInfo loginInfo = getLoginInfo();
        String examId = searchDto.getExamId();
        String classId = searchDto.getClassId();
        String classType = searchDto.getClassType();
        String subjectId = searchDto.getSubjectId();
        String unitId = loginInfo.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);

        if (StringUtils.isBlank(classId))
            return promptFlt(map, "请选择班级");
        //获取科目信息
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
        if (CollectionUtils.isNotEmpty(subjectInfoList))
            map.put("subjectInfo", subjectInfoList.get(0));
        //是否锁定
        EmClassLock classLock = emClassLockService.findBySchoolIdAndExamIdAndSubjectIdAndClassId
                (unitId, examId, subjectId, classId);
        String lockState = "0";
        if (classLock != null && StringUtils.isNotBlank(classLock.getId()))
            lockState = classLock.getLockState();

        //班级下学生
        List<Student> stuList = new ArrayList<Student>();
        //不排考学生
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        if (StringUtils.isNotBlank(subType)) {
            Set<String> stuIds = null;
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, examInfo.getGradeCodes()), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isEmpty(gradeList))
                return promptFlt(map, "考试选择的年级不存在");
            Grade grade = gradeList.get(0);
            if ("1".equals(subType)) {//选考
                Map<String, Set<String>> xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(examInfo.getAcadyear(), examInfo.getSemester(), grade.getId(), true, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                });
                if (xuankaoStuIdMap == null)
                    xuankaoStuIdMap = new HashMap<>();
                stuIds = xuankaoStuIdMap.get(subjectId);
            } else {//学考
                Map<String, Set<String>> xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(examInfo.getAcadyear(), examInfo.getSemester(), grade.getId(), false, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                });
                if (xuekaoStuIdMap == null)
                    xuekaoStuIdMap = new HashMap<>();
                stuIds = xuekaoStuIdMap.get(subjectId);
            }
            List<Student> stuList1 = new ArrayList<Student>();
            if (ExammanageConstants.CLASS_TYPE1.equals(classType)) {
                stuList1 = SUtils.dt(studentService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
                });
            } else {
                stuList1 = SUtils.dt(studentService.findByTeachClassIds(new String[]{classId}), new TR<List<Student>>() {
                });
            }
            if (CollectionUtils.isNotEmpty(stuIds)) {
                for (Student stu : stuList1) {
                    if (stuIds.contains(stu.getId())) {
                        stuList.add(stu);
                    }
                }
            }
//			if(CollectionUtils.isNotEmpty(stuIds)){
//				stuList = SUtils.dt(studentService.findListByIds(stuIds.toArray(new String[]{})),new TR<List<Student>>(){});
//			}
        } else if (ExammanageConstants.CLASS_TYPE1.equals(classType)) {
            stuList = SUtils.dt(studentService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
            });
        } else {
            stuList = SUtils.dt(studentService.findByTeachClassIds(new String[]{classId}), new TR<List<Student>>() {
            });
        }
		/*if(stuList!=null && stuList.size()>2){
			Student stu1=stuList.get(0);
			Student stu2=stuList.get(1);
			stuList=new ArrayList<>();
			stuList.add(stu1);
			stuList.add(stu2);
		}*/

        //学生考号
        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        //获取班级名称
        Map<String, String> classNameMap = toMakeClassMap(classType, new String[]{classId});
        String className = classNameMap.get(classId);

        List<ScoreInputDto> dtoList = new ArrayList<>();
        Set<String> stuIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(stuList)) {
            stuIds = stuList.stream().map(Student::getId).collect(Collectors.toSet());
        }
        //已有的成绩 一个科目 一个学生 一个成绩
        Map<String, EmScoreInfo> infoMap = emScoreInfoService.findByStudent(examId, subjectId, stuIds.toArray(new String[0]));
        stuList.forEach(item -> {
            if (!filterMap.containsKey(item.getId())) {
                ScoreInputDto dto = new ScoreInputDto();
                dto.setStuId(item.getId());
                dto.setStuName(item.getStudentName());
                dto.setStuCode(item.getStudentCode());
                dto.setClassId(item.getClassId());
                dto.setClassName(className);
                if (examNumMap.containsKey(item.getId())) {
                    dto.setStuExamNum(examNumMap.get(item.getId()));
                }
                if (ExammanageConstants.CLASS_TYPE2.equals(classType)) {
                    dto.setTeachClassId(classId);
                }
                EmScoreInfo scoreInfo = infoMap.get(item.getId());
                if (scoreInfo != null) {
                    dto.setScore(scoreInfo.getScore());
                    dto.setScoreStatus(scoreInfo.getScoreStatus());
                    dto.setToScore(scoreInfo.getToScore());
                    dto.setScoreId(scoreInfo.getId());
                }
                dtoList.add(dto);
            }
        });

        //是否需要总评成绩
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectCode = null;
        if (course == null) {
            //logger.error("根据课程id："+ courseId +",找不到对应课程");
        } else {
            subjectCode = course.getSubjectCode();
            if (ExammanageConstants.EXAM_TYPE_FINAL.equals(examInfo.getExamType())
                    && !BaseConstants.HW_CODE_KS.equals(subjectCode) && !BaseConstants.HW_CODE_XZ.equals(subjectCode)) {
                map.put("needGeneral", true);
            }
        }
        //如果成绩已经锁定，需要打印成绩表 信息
        if (Constant.IS_TRUE_Str.equals(classLock.getLockState())) {
            int rowCount = (dtoList.size() + 1) / 2;
            map.put("rowCount", rowCount);
            map.put("stuCount", dtoList.size());
            //语文需要绑定 写作成绩
            if (BaseConstants.HW_CODES_YU.contains(subjectCode)) {
                List<Course> xzCourse = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, BaseConstants.HW_CODE_XZ), Course.class);
                if (CollectionUtils.isNotEmpty(xzCourse)) {
                    String xzCourseId = xzCourse.get(0).getId();
                    Map<String, EmScoreInfo> xzInfoMap = emScoreInfoService.findByStudent(examId, xzCourseId, stuIds.toArray(new String[0]));
                    //判断写作成绩是否已经录入
                    if (xzInfoMap.size() > 0) {
                        for (ScoreInputDto dto2 : dtoList) {
                            EmScoreInfo scoreInfo = xzInfoMap.get(dto2.getStuId());
                            if (scoreInfo != null) {
                                dto2.setWritingScore(scoreInfo.getScore());
                            }
                        }
                    }
                    map.put("bindWriting", true);
                } else {
                    //logger.error("根据课程码："+BaseConstants.HW_CODE_XZ+" 找不到写作课程");
                }
            }
            //外文需要绑定 口试成绩
            if (BaseConstants.HW_CODES_WW.contains(subjectCode)) {
                List<Course> ksCourse = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, BaseConstants.HW_CODE_KS), Course.class);
                if (CollectionUtils.isNotEmpty(ksCourse)) {
                    String ksCourseId = ksCourse.get(0).getId();
                    Map<String, EmScoreInfo> ksInfoMap = emScoreInfoService.findByStudent(examId, ksCourseId, stuIds.toArray(new String[0]));
                    //判断口试成绩是否已经录入
                    if (ksInfoMap.size() > 0) {
                        for (ScoreInputDto dto2 : dtoList) {
                            EmScoreInfo scoreInfo = ksInfoMap.get(dto2.getStuId());
                            if (scoreInfo != null) {
                                dto2.setSpeehScore(scoreInfo.getScore());
                            }
                        }
                    }
                    map.put("bindSpeeh", true);
                } else {
                    //logger.error("根据课程码：" + BaseConstants.HW_CODE_KS + " 找不到口试课程");
                }
            }
        }
        //获取学校名
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        String acadyear = examInfo.getAcadyear();
        String semester = examInfo.getSemester();
        String semesterName = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", semester), McodeDetail.class).getMcodeContent();
        String examName = examInfo.getExamName();

        String classAllName = null;
        if ("1".equals(classType)) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            String gradeName = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class).getGradeName();
            classAllName = gradeName + clazz.getClassName();
        } else {
            TeachClass teachClass = SUtils.dc(teachClassService.findOneById(classId), TeachClass.class);
            String gradeName = SUtils.dc(gradeRemoteService.findOneById(teachClass.getGradeId()), Grade.class).getGradeName();
            //行政班暂时不需要加上年级名称
            gradeName = "";
            classAllName = gradeName + teachClass.getName();
        }
        map.put("unitName", unit.getUnitName());
        map.put("acadyear", acadyear);
        map.put("semesterName", semesterName);
        map.put("examName", examName);
        map.put("classAllName", classAllName);
        map.put("subjectName", course.getSubjectName());

        map.put("lockState", lockState);
        map.put("subjectId", subjectId);
        map.put("dtoList", dtoList);
        map.put("searchDto", searchDto);
        return "/exammanage/scoreNewInput/examScoreOnelist.ftl";
    }

    @ResponseBody
    @RequestMapping("/scoreNewInput/clearScores")
    @ControllerInfo(value = "清除某考试的一个科目成绩")
    public String clearScores(String examId, String subjectId) {
        try {
            emScoreInfoService.deleteBy(examId, subjectId);
            emClassLockService.deleteBy(examId, subjectId);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping("/scoreNewInput/saveAll")
    @ControllerInfo(value = "保存成绩")
    public String lockTheExamScore(ScoreInputDto dto, String state, String subType) {
        try {
            LoginInfo loginInfo = getLoginInfo();
            String examId = dto.getExamId();
            String subjectId = dto.getSubjectId();
            List<EmScoreInfo> dtoList = dto.getDtoList();
            if (dto == null || CollectionUtils.isEmpty(dtoList)) {
                return error("没有能添加的数据");
            }
            if (!"1".equals(subType) && !"2".equals(subType)) {
                subType = "0";
            }
            EmExamInfo exam = emExamInfoService.findOne(examId);
            //获取科目信息
            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
            if (CollectionUtils.isEmpty(subjectInfoList))
                return error("该考试科目已删除");
            EmSubjectInfo subjectInfo = subjectInfoList.get(0);
            if (!subjectInfo.getInputType().equals(dto.getInputType())) {
                return error("该考试科目信息已经改变");
            }

            Set<String> stuIds = dtoList.stream().map(EmScoreInfo::getStudentId).collect(Collectors.toSet());
            Set<String> subjectIds = new HashSet<String>();
            subjectIds.add(subjectInfo.getSubjectId());

            //Map<String, EmScoreInfo> infoMap=emScoreInfoService.findByStudent(examId, subjectId,subType, stuIds.toArray(new String[0]));
            Map<String, EmScoreInfo> infoMap = emScoreInfoService.findByStudent(examId, subjectId, stuIds.toArray(new String[0]));
            List<EmScoreInfo> insertOrupdateList = new ArrayList<>();
            EmScoreInfo newScoreInfo = null;
            String opoUserId = loginInfo.getOwnerId();
            if (ExammanageConstants.ACHI_GRADE.equals(subjectInfo.getInputType())) {
                //等第
                if (!subjectInfo.getGradeType().equals(dto.getGradeType())) {
                    return error("该考试科目信息已经改变");
                }
                for (EmScoreInfo scoreInfo : dtoList) {
                    if ("1".equals(scoreInfo.getScoreStatus())) {
                        scoreInfo.setScore("");
                    }
                    if (infoMap.containsKey(scoreInfo.getStudentId())) {
                        //修改
                        newScoreInfo = infoMap.get(scoreInfo.getStudentId());
                        newScoreInfo.setOperatorId(opoUserId);
                        newScoreInfo.setModifyTime(new Date());
                    } else {
                        newScoreInfo = makeScoreInfo(exam, subjectInfo, subjectId, opoUserId, loginInfo.getUnitId());
                    }
                    newScoreInfo.setStudentId(scoreInfo.getStudentId());
                    newScoreInfo.setClassId(scoreInfo.getClassId());
                    newScoreInfo.setTeachClassId(scoreInfo.getTeachClassId());
                    newScoreInfo.setScore(scoreInfo.getScore());
                    newScoreInfo.setScoreStatus(scoreInfo.getScoreStatus());
                    //wyy.设置总评成绩
                    newScoreInfo.setToScore(scoreInfo.getToScore());
                    newScoreInfo.setGkSubType(subType);
                    insertOrupdateList.add(newScoreInfo);
                }
            } else {
                //分数
                for (EmScoreInfo scoreInfo : dtoList) {
                    if ("1".equals(scoreInfo.getScoreStatus())) {
                        scoreInfo.setScore("0");
                    }
                    if (infoMap.containsKey(scoreInfo.getStudentId())) {
                        //修改
                        newScoreInfo = infoMap.get(scoreInfo.getStudentId());
                        newScoreInfo.setOperatorId(opoUserId);
                        newScoreInfo.setModifyTime(new Date());
                        //分数--防止之前的数据是等第
                        newScoreInfo.setInputType(subjectInfo.getInputType());
                    } else {
                        newScoreInfo = makeScoreInfo(exam, subjectInfo, subjectId, opoUserId, loginInfo.getUnitId());
                    }
                    newScoreInfo.setStudentId(scoreInfo.getStudentId());
                    newScoreInfo.setClassId(scoreInfo.getClassId());
                    newScoreInfo.setTeachClassId(scoreInfo.getTeachClassId());
                    newScoreInfo.setScore(scoreInfo.getScore());
                    newScoreInfo.setScoreStatus(scoreInfo.getScoreStatus());
                    //wyy.设置总评成绩
                    newScoreInfo.setToScore(scoreInfo.getToScore());
                    newScoreInfo.setGkSubType(subType);
                    insertOrupdateList.add(newScoreInfo);
                }
            }
            if (CollectionUtils.isNotEmpty(insertOrupdateList)) {
                emScoreInfoService.saveAll(insertOrupdateList.toArray(new EmScoreInfo[]{}));
            }
            String classId = dto.getClassId();
            String classType = dto.getClassType();
            EmClassLock classLock = emClassLockService.findBySchoolIdAndExamIdAndSubjectIdAndClassId
                    (loginInfo.getUnitId(), examId, subjectId, classId);
            if (classLock == null || StringUtils.isBlank(classLock.getId())) {
                classLock = new EmClassLock();
                classLock.setId(UuidUtils.generateUuid());
                classLock.setClassId(classId);
                classLock.setClassType(classType);
                classLock.setCreationTime(new Date());
                classLock.setExamId(examId);
                classLock.setLockState(state);
                classLock.setSchoolId(loginInfo.getUnitId());
                classLock.setSubjectId(subjectId);
                emClassLockService.save(classLock);
            } else {
                classLock.setLockState(state);
                classLock.setModifyTime(new Date());
                emClassLockService.save(classLock);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping(value = "/scoreNewInput/checkInputPower")
    public String checkPower(ScoreInputSearchDto searchDto, String noLimit) {
        //是否锁定
        if (StringUtils.isNotBlank(searchDto.getClassId())) {
            EmClassLock classLock = emClassLockService.findBySchoolIdAndExamIdAndSubjectIdAndClassId
                    (getLoginInfo().getUnitId(), searchDto.getExamId(), searchDto.getSubjectId(), searchDto.getClassId());
            if (classLock != null && "1".equals(classLock.getLockState())) {
                return error("该科目已有成绩提交，不可导入！");
            }
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/scoreNewInput/classList")
    @ControllerInfo(value = "班级列")
    public String showClassList(ModelMap map, ScoreInputSearchDto searchDto, String noLimit) {
        List<Clazz> xzbList = new ArrayList<>();
        List<TeachClass> jxbList = new ArrayList<>();
        setList(xzbList, jxbList, searchDto, getLoginInfo(), noLimit);
        JSONArray array = new JSONArray();
        if ("1".equals(searchDto.getClassType())) {
            if (CollectionUtils.isNotEmpty(xzbList)) {
                xzbList.forEach(item -> {
                    JSONObject json = new JSONObject();
                    json.put("classId", item.getId());
                    json.put("className", item.getClassNameDynamic());
                    array.add(json);
                });
            }
        } else {
            if (CollectionUtils.isNotEmpty(jxbList)) {
                jxbList.forEach(item -> {
                    JSONObject json = new JSONObject();
                    json.put("classId", item.getId());
                    json.put("className", item.getName());
                    array.add(json);
                });
            }
        }
        return array.toJSONString();
    }

    /**
     * 且排除高考模式下 选考和学考导致的某班没有对应的学生数的班级
     *
     * @param xzbList
     * @param jxbList
     * @param searchDto
     * @param loginInfo
     * @param noLimit
     */
    public void setList(List<Clazz> xzbList, List<TeachClass> jxbList, ScoreInputSearchDto searchDto, LoginInfo loginInfo, String noLimit) {
        String acadyear = searchDto.getAcadyear();
        String semester = searchDto.getSemester();
        String gradeCode = searchDto.getGradeCode();
        String subjectId = searchDto.getSubjectId();
        String examId = searchDto.getExamId();
        String unitId = loginInfo.getUnitId();
        List<Clazz> myxzbList = new ArrayList<>();
        List<TeachClass> myjxbList = new ArrayList<>();
        if (!"1".equals(noLimit)) {//普通

            Set<String> limitIds = EntityUtils.getSet(emLimitService.findByExamIdST(searchDto.getExamId(), loginInfo.getOwnerId(), subjectId, null), EmLimit::getId);
            if (CollectionUtils.isNotEmpty(limitIds)) {
                Set<String> xzbIds = new HashSet<>();
                Set<String> yxbIds = new HashSet<>();
                List<EmLimitDetail> limitDetailList = emLimitDetailService.findByLimitIdIn(limitIds.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(limitDetailList)) {
                    for (EmLimitDetail limitDetail : limitDetailList) {
                        if ("1".equals(limitDetail.getClassType())) {
                            xzbIds.add(limitDetail.getClassId());
                        } else {
                            yxbIds.add(limitDetail.getClassId());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(xzbIds)) {
                    myxzbList = SUtils.dt(classRemoteService.findByIdsSort(xzbIds.toArray(new String[0])), new TR<List<Clazz>>() {
                    });
                }
                if (CollectionUtils.isNotEmpty(yxbIds)) {
                    myjxbList = SUtils.dt(teachClassService.findListByIds(yxbIds.toArray(new String[0])), new TR<List<TeachClass>>() {
                    });
                }
            }
        } else {
            int section = NumberUtils.toInt(gradeCode.substring(0, 1));
            int afterGradeCode = NumberUtils.toInt(gradeCode.substring(1, 2));
            int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
            String openAcadyear = (beforeSelectAcadyear - afterGradeCode + 1) + "-" + (beforeSelectAcadyear - afterGradeCode + 2);

            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
            });
            myxzbList = SUtils.dt(classRemoteService.findClassList(unitId, subjectId, openAcadyear, acadyear, semester, section, 0), new TR<List<Clazz>>() {
            });
            if (StringUtils.isBlank(subjectId)) {
                List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(searchDto.getExamId());
                Set<String> subjectIds = EntityUtils.getSet(infoList, EmSubjectInfo::getSubjectId);
                List<TeachClass> teachClassList = SUtils.dt(teachClassService.findTeachClassList(unitId, acadyear, semester, null,
                        EntityUtils.getSet(gradeList, Grade::getId).toArray(new String[0]), false), new TR<List<TeachClass>>() {
                });
                for (TeachClass teachClass : teachClassList) {
                    if (subjectIds.contains(teachClass.getCourseId())) {
                        myjxbList.add(teachClass);
                    }
                }
            } else {
                myjxbList = SUtils.dt(teachClassService.findTeachClassList(unitId, acadyear, semester, subjectId,
                        EntityUtils.getSet(gradeList, Grade::getId).toArray(new String[0]), false), new TR<List<TeachClass>>() {
                });
            }
        }
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam != null && "1".equals(exam.getIsgkExamType())) {//新高考模式 分选考学考
            String gkSubjectType = null;//该科目在 此考试的范围
            boolean haveSubId = StringUtils.isNotBlank(subjectId);
            Map<String, String> filterMap = new HashMap<>();
            Map<String, List<Student>> studentListMap = new HashMap<>();
            Map<String, List<TeachClassStu>> teachStuListMap = new HashMap<>();
            Map<String, Set<String>> xuankaoStuIdMap = new HashMap<>();
            Map<String, Set<String>> xuekaoStuIdMap = new HashMap<>();
            //获取行政班以及教学班的学生
            List<Student> studentList = SUtils.dt(studentService.findByClassIds(myxzbList.stream().map(Clazz::getId).collect(Collectors.toSet())
                    .toArray(new String[]{})), new TR<List<Student>>() {
            });

            List<TeachClassStu> teachStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(myjxbList.stream().map(TeachClass::getId).collect(Collectors.toSet())
                    .toArray(new String[]{})), new TR<List<TeachClassStu>>() {
            });
            //不排考学生
            filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, loginInfo.getUnitId(), ExammanageConstants.FILTER_TYPE1);
            //行政班对应的学生map
            if (CollectionUtils.isNotEmpty(studentList)) {
                for (Student stu : studentList) {
                    if (!studentListMap.containsKey(stu.getClassId())) {
                        studentListMap.put(stu.getClassId(), new ArrayList<>());
                    }
                    studentListMap.get(stu.getClassId()).add(stu);
                }
            }
            //教学班对应的学生map
            if (CollectionUtils.isNotEmpty(teachStuList)) {
                for (TeachClassStu teachStu : teachStuList) {
                    if (!teachStuListMap.containsKey(teachStu.getClassId())) {
                        teachStuListMap.put(teachStu.getClassId(), new ArrayList<>());
                    }
                    teachStuListMap.get(teachStu.getClassId()).add(teachStu);
                }
            }
            //获取高考模式下的该科目类型
//			List<EmSubjectInfo> infoList=emSubjectInfoService.findByExamIdAndSubjectId(examId,subjectId);
            List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(examId);
            Map<String, String> gkSubjectTypeMap = new HashMap<>();
            Set<String> subIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(infoList)) {
                for (EmSubjectInfo info : infoList) {
                    subIds.add(info.getSubjectId());
                    if (gkSubjectTypeMap.containsKey(info.getSubjectId())) {
                        gkSubjectTypeMap.put(info.getSubjectId(), "0");//同时有选考和学考   0的时候不需要判断
                    } else
                        gkSubjectTypeMap.put(info.getSubjectId(), info.getGkSubType());//1:选考   2：学考    0:非7选3
                }
                if (haveSubId) {
                    gkSubjectType = gkSubjectTypeMap.get(subjectId);
                }
				/*if(infoList.size()>1){
					gkSubjectType="0";//同时有选考和学考
				}else{
					gkSubjectType=infoList.get(0).getGkSubType();//1:选考   2：学考    0:非7选3
				}*/
            }
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(loginInfo.getUnitId(), searchDto.getGradeCode()), new TR<List<Grade>>() {
            });
            Grade grade = gradeList.get(0);
            if (haveSubId) {
//				xuankaoStuIdMap=SUtils.dt(newGkChoResultRemoteService.findStuIdListBySubjectId(grade.getId(),true, new String[]{subjectId}),new TR<Map<String,Set<String>>>(){});
//				xuekaoStuIdMap=SUtils.dt(newGkChoResultRemoteService.findStuIdListBySubjectId(grade.getId(),false, new String[]{subjectId}),new TR<Map<String,Set<String>>>(){});
                xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, grade.getId(), true, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                });
                xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, grade.getId(), false, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                });
            } else {
//				String[] subIds=myjxbList.stream().map(TeachClass::getCourseId).collect(Collectors.toSet()).toArray(new String[]{});
                xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, grade.getId(), true, subIds.toArray(new String[]{})), new TR<Map<String, Set<String>>>() {
                });
                xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, grade.getId(), false, subIds.toArray(new String[]{})), new TR<Map<String, Set<String>>>() {
                });
//				xuekaoStuIdMap=SUtils.dt(newGkChoResultRemoteService.findStuIdListBySubjectId(grade.getId(),false, subIds.toArray(new String[]{})),new TR<Map<String,Set<String>>>(){});
            }
            if (CollectionUtils.isNotEmpty(myxzbList)) {//行政班是否没有区别
                for (Clazz clazz : myxzbList) {
                    if (studentListMap.containsKey(clazz.getId())) {
                        Set<String> stuIds = studentListMap.get(clazz.getId()).stream().map(Student::getId).collect(Collectors.toSet());
                        if ("1".equals(gkSubjectType)) {//选考
                            Set<String> xuankaoStuIds = xuankaoStuIdMap.get(subjectId);
                            if (xuankaoStuIds == null) xuankaoStuIds = new HashSet<>();
                            stuIds.retainAll(xuankaoStuIds);
                        } else if ("2".equals(gkSubjectType)) {//学考
                            Set<String> xuekaoStuIds = xuekaoStuIdMap.get(subjectId);
                            if (xuekaoStuIds == null) xuekaoStuIds = new HashSet<>();
                            stuIds.retainAll(xuekaoStuIds);
                        }
                        Set<String> lastStuIds = new HashSet<>();
                        if (CollectionUtils.isNotEmpty(stuIds)) {//此时是两交集 去除不排考后为剩下的学生
                            for (String stuId : stuIds) {
                                if (!filterMap.containsKey(stuId)) {
                                    lastStuIds.add(stuId);
                                }
                            }
                        }
                        if (lastStuIds.size() == 0) {
                            continue;
                        }
                        xzbList.add(clazz);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(myjxbList)) {//教学班  存在该7选3科目全部学校是选考或学考的情况   此时需根据考试范围来判断
                for (TeachClass teachClass : myjxbList) {
                    if (teachStuListMap.containsKey(teachClass.getId())) {
                        Set<String> stuIds = teachStuListMap.get(teachClass.getId()).stream().map(TeachClassStu::getStudentId).collect(Collectors.toSet());
                        if (!haveSubId) {//没有subjectId 教学班的时候
                            subjectId = teachClass.getCourseId();
                            gkSubjectType = gkSubjectTypeMap.get(subjectId);
                        }
                        if ("1".equals(gkSubjectType)) {//选考
                            Set<String> xuankaoStuIds = xuankaoStuIdMap.get(subjectId);
                            if (xuankaoStuIds == null) xuankaoStuIds = new HashSet<>();
                            stuIds.retainAll(xuankaoStuIds);
                        } else if ("2".equals(gkSubjectType)) {//学考
                            Set<String> xuekaoStuIds = xuekaoStuIdMap.get(subjectId);
                            if (xuekaoStuIds == null) xuekaoStuIds = new HashSet<>();
                            stuIds.retainAll(xuekaoStuIds);
                        }
                        Set<String> lastStuIds = new HashSet<>();
                        if (CollectionUtils.isNotEmpty(stuIds)) {//此时是两交集 去除不排考后为剩下的学生
                            for (String stuId : stuIds) {
                                if (!filterMap.containsKey(stuId)) {
                                    lastStuIds.add(stuId);
                                }
                            }
                        }
                        if (lastStuIds.size() == 0) {
                            continue;
                        }
                        jxbList.add(teachClass);
                    }
                }
            }
        } else {
            xzbList.addAll(myxzbList);
            jxbList.addAll(myjxbList);
        }
    }

    private Map<String, String> toMakeClassMap(String classType, String[] classIds) {
        Map<String, String> classMap = new LinkedHashMap<String, String>();
        if (classIds == null || classIds.length <= 0) {
            return classMap;
        }
        if (ExammanageConstants.CLASS_TYPE1.equals(classType)) {
            //行政班
            List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classList)) {
                for (Clazz z : classList) {
                    if (z.getIsDeleted() == Constant.IS_DELETED_TRUE) {
                        continue;
                    }
                    classMap.put(z.getId(), z.getClassNameDynamic());
                }
            }
        } else {
            List<TeachClass> tcalssList = SUtils.dt(teachClassService.findListByIds(classIds), new TR<List<TeachClass>>() {
            });
            if (CollectionUtils.isNotEmpty(tcalssList)) {
                for (TeachClass t : tcalssList) {
                    if (t.getIsDeleted() == Constant.IS_DELETED_TRUE) {
                        continue;
                    }
                    classMap.put(t.getId(), t.getName());
                }
            }
        }
        return classMap;
    }

    private EmScoreInfo makeScoreInfo(EmExamInfo exam, EmSubjectInfo subjectInfo,
                                      String subjectId, String userId, String unitId) {
        EmScoreInfo info = new EmScoreInfo();
        info.setAcadyear(exam.getAcadyear());
        info.setSemester(exam.getSemester());
        info.setSubjectId(subjectId);
        info.setCreationTime(new Date());
        info.setExamId(exam.getId());
        info.setId(UuidUtils.generateUuid());
        info.setModifyTime(new Date());
        info.setOperatorId(userId);
        info.setUnitId(unitId);
        info.setInputType(subjectInfo.getInputType());
        return info;
    }

    /**
     * 转换成选考学考科目 且去除班级对应的学生没有选择该科目的科目
     *
     * @param map
     * @param typeMap
     * @param course73Map
     * @param subjectIds
     * @param searchDto
     */
    public void getSubjectList(ModelMap map, Map<String, String> typeMap, Map<String, String> course73Map,
                               Set<String> subjectIds, ScoreInputSearchDto searchDto) {
        if (CollectionUtils.isNotEmpty(subjectIds)) {
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            Collections.sort(courseList, (o1, o2) -> {
                return o1.getOrderId() - o2.getOrderId();
            });
            //获取该行政班的学生
            Set<String> stuIds = null;
            if ("1".equals(searchDto.getClassType())) {
                stuIds = SUtils.dt(studentService.findByClassIds(searchDto.getClassId()), new TR<List<Student>>() {
                }).stream().map(Student::getId).collect(Collectors.toSet());
            } else {
                stuIds = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[]{searchDto.getClassId()}), new TR<List<TeachClassStu>>() {
                }).stream().map(TeachClassStu::getStudentId).collect(Collectors.toSet());
            }
            if (stuIds == null)
                stuIds = new HashSet<>();
            //获取 科目对应的选考学考人数
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(getLoginInfo().getUnitId(), searchDto.getGradeCode()), new TR<List<Grade>>() {
            });
            Grade grade = gradeList.get(0);
            Map<String, Set<String>> xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(searchDto.getAcadyear(), searchDto.getSemester(), grade.getId(), true, subjectIds.toArray(new String[]{})), new TR<Map<String, Set<String>>>() {
            });
            Map<String, Set<String>> xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(searchDto.getAcadyear(), searchDto.getSemester(), grade.getId(), false, subjectIds.toArray(new String[]{})), new TR<Map<String, Set<String>>>() {
            });
//   		 Map<String, Set<String>> xuankaoStuIdMap=SUtils.dt(newGkChoResultRemoteService.findStuIdListBySubjectId(grade.getId(),true, subjectIds.toArray(new String[]{})),new TR<Map<String,Set<String>>>(){});
//   		 Map<String, Set<String>> xuekaoStuIdMap=SUtils.dt(newGkChoResultRemoteService.findStuIdListBySubjectId(grade.getId(),false, subjectIds.toArray(new String[]{})),new TR<Map<String,Set<String>>>(){});
            List<ExamSubjectDto> subjectDtoList = new ArrayList<>();
            ExamSubjectDto subjectDto = null;
            for (Course course : courseList) {
                String courseId = course.getId();
                subjectDto = new ExamSubjectDto();
                if (typeMap != null && course73Map.containsKey(courseId)) {
                    String subType = typeMap.get(courseId);
                    Set<String> xuankaoStuIds = xuankaoStuIdMap.get(courseId);
                    Set<String> xuekaoStuIds = xuekaoStuIdMap.get(courseId);
                    //取该科目选考学考 与 该班级学生人数的交集  若交集为空，则没有人数
                    if (CollectionUtils.isNotEmpty(xuankaoStuIds)) {
                        xuankaoStuIds.retainAll(stuIds);
                    }
                    if (CollectionUtils.isNotEmpty(xuekaoStuIds)) {
                        xuekaoStuIds.retainAll(stuIds);
                    }
                    if ("0".equals(subType)) {
                        if (CollectionUtils.isNotEmpty(xuankaoStuIds)) {
                            subjectDto.setSubjectName(course.getSubjectName() + "选考");
                            subjectDto.setSubjectId(courseId);
                            subjectDto.setSubType("1");
                            subjectDtoList.add(subjectDto);
                        }
                        if (CollectionUtils.isNotEmpty(xuekaoStuIds)) {
                            subjectDto = new ExamSubjectDto();
                            subjectDto.setSubjectName(course.getSubjectName() + "学考");
                            subjectDto.setSubjectId(courseId);
                            subjectDto.setSubType("2");
                            subjectDtoList.add(subjectDto);
                        }
                    } else if ("1".equals(subType)) {
                        if (CollectionUtils.isNotEmpty(xuankaoStuIds)) {
                            subjectDto.setSubjectName(course.getSubjectName() + "选考");
                            subjectDto.setSubjectId(courseId);
                            subjectDto.setSubType("1");
                            subjectDtoList.add(subjectDto);
                        }
                    } else if (CollectionUtils.isNotEmpty(xuekaoStuIds)) {
                        subjectDto.setSubjectName(course.getSubjectName() + "学考");
                        subjectDto.setSubjectId(courseId);
                        subjectDto.setSubType("2");
                        subjectDtoList.add(subjectDto);
                    }
                } else {
                    subjectDto.setSubjectName(course.getSubjectName());
                    subjectDto.setSubjectId(courseId);
                    subjectDtoList.add(subjectDto);
                }
            }
            map.put("subjectDtoList", subjectDtoList);
        }
    }
}
