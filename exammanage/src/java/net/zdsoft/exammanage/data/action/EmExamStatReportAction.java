package net.zdsoft.exammanage.data.action;


import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.ExamInfoDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/examanalysis")
public class EmExamStatReportAction extends BaseAction {
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private TeachGroupRemoteService teachGroupRemoteService;
    @Autowired
    private TeachGroupExRemoteService teachGroupExRemoteService;
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
    private StorageDirRemoteService storageDirRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
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
    @Autowired
    private NewReportService newReportService;

    @RequestMapping("/permission/index/page")
    @ControllerInfo(value = "人员权限index")
    public String permissionIndex(ModelMap map, HttpServletRequest request) {
        map.put("subsystem", ExammanageConstants.EXAM_SUBSYSTEM);
        return "/examanalysis/permission/emPermissionIndex.ftl";
    }

    public List<TeachGroup> getMainGroupIds(String unitId) {
        List<TeachGroup> list = SUtils.dt(teachGroupRemoteService.findBySchoolId(unitId), new TR<List<TeachGroup>>() {
        });
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> ids = EntityUtils.getSet(list, e -> e.getId());
            List<TeachGroupEx> exlist = SUtils.dt(teachGroupExRemoteService.findByTeachGroupId(ids.toArray(new String[]{})), new TR<List<TeachGroupEx>>() {
            });
            Map<String, Set<String>> tIdByGroupId = new HashMap<String, Set<String>>();
            if (CollectionUtils.isNotEmpty(exlist)) {
                for (TeachGroupEx ee : exlist) {
                    if (ee.getType() == 1) {//负责人
                        Set<String> set = tIdByGroupId.get(ee.getTeachGroupId());
                        if (CollectionUtils.isEmpty(set)) {
                            set = new HashSet<String>();
                            tIdByGroupId.put(ee.getTeachGroupId(), set);
                        }
                        set.add(ee.getTeacherId());
                    }
                }
                for (TeachGroup gg : list) {
                    if (tIdByGroupId.containsKey(gg.getId())) {
                        gg.setTeacherIdSet(tIdByGroupId.get(gg.getId()));
                    }
                }
            }
        }
        return list;
    }

    @RequestMapping("/emReport/index/page")
    @ControllerInfo(value = "报告index")
    public String emReportIndex(ModelMap map, HttpServletRequest request) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        String userId = loginInfo.getUserId();
        String ownerId = loginInfo.getOwnerId();
        List<String> nameList = new ArrayList<>();
        Map<String, String> typeMap = new HashMap<>();
        //判断校级报告权限
        List<Grade> myGradeList = SUtils.dt(gradeRemoteService.findByTeacherId(ownerId), new TR<List<Grade>>() {
        });
        if (CollectionUtils.isNotEmpty(myGradeList) || customRoleRemoteService.checkUserRole(unitId, ExammanageConstants.EXAM_SUBSYSTEM, ExammanageConstants.REPORT_SCHOOL_LEVEL, userId)) {
            nameList.add("校级报告");
            typeMap.put("校级报告", "1");
        }
        //教研组长报告
        //List<TeachGroup> teachGroupList=SUtils.dt(teachGroupRemoteService.findBySchoolId(unitId, true),new TR<List<TeachGroup>>(){});
        List<TeachGroup> teachGroupList = getMainGroupIds(unitId);
        if (CollectionUtils.isNotEmpty(teachGroupList)) {
            for (TeachGroup teachGroup : teachGroupList) {
                Set<String> teacherIds = teachGroup.getTeacherIdSet();
                if (CollectionUtils.isNotEmpty(teacherIds) && teacherIds.contains(ownerId)) {
                    nameList.add("教研组长报告");
                    typeMap.put("教研组长报告", "2");
                    break;
                }
            }
        }
        //班主任报告
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findByTeacherId(ownerId), new TR<List<Clazz>>() {
        });
        if (CollectionUtils.isNotEmpty(clazzList)) {
            nameList.add("班主任报告");
            typeMap.put("班主任报告", "3");
        }
        //任课老师报告
        List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByTeacherId(unitId, ownerId), new TR<List<ClassTeaching>>() {
        });
        if (CollectionUtils.isNotEmpty(classTeachingList)) {
            nameList.add("任课老师报告");
            typeMap.put("任课老师报告", "4");
        }

        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        if (CollectionUtils.isNotEmpty(nameList))
            map.put("type", typeMap.get(nameList.get(0)));
        map.put("nameList", nameList);
        map.put("typeMap", typeMap);
        return "/examanalysis/myReport/myReportExamIndex.ftl";
    }

    @RequestMapping("/emReport/stuIndex/page")
    @ControllerInfo(value = "学生报告index")
    public String emStuReportIndex(ModelMap map, HttpServletRequest request) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);

        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/examanalysis/myReport/stuReportExamIndex.ftl";
    }

    @RequestMapping("/emReport/examList/page")
    @ControllerInfo(value = "报告examList")
    public String showExamList(ModelMap map, HttpServletRequest request) {
        String type = request.getParameter("type");//1 校级  2教研组长   3班主任  4任课老师
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        String ownerId = loginInfo.getOwnerId();
        String userId = loginInfo.getUserId();
        boolean haveGradeCode = StringUtils.isNotBlank(gradeCode);
        List<ExamInfoDto> examDtoList = new ArrayList<>();
        ExamInfoDto examDto = null;
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        //获取年级名次 考试存的是年级code
        Map<String, String> gradeNameMap = EntityUtils.getMap(gradeList, Grade::getGradeCode, Grade::getGradeName);
        Map<String, String> gradeCodeMap = EntityUtils.getMap(gradeList, Grade::getId, Grade::getGradeCode);
        map.put("type", type);
        Map<String, EmStatObject> objectMap = EntityUtils.getMap(emStatObjectService.findByUnitId(unitId), EmStatObject::getExamId);
        EmStatObject object = null;
        if ("1".equals(type) || "2".equals(type)) {
            List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadAndGradeId(unitId, acadyear, semester, gradeCode);
            if (CollectionUtils.isEmpty(examList)) {
                return "/examanalysis/myReport/myReportExamList.ftl";
            }
            //校长报告的所有考试
            if ("1".equals(type)) {
                //校长权限是全校 年级组长是该年级
                boolean isSchool = customRoleRemoteService.checkUserRole(unitId, ExammanageConstants.EXAM_SUBSYSTEM, ExammanageConstants.REPORT_SCHOOL_LEVEL, userId);
                List<Grade> teachGrades = SUtils.dt(gradeRemoteService.findByTeacherId(ownerId), new TR<List<Grade>>() {
                });
                Set<String> gradeCodes = EntityUtils.getSet(teachGrades, Grade::getGradeCode);
                for (EmExamInfo emExamInfo : examList) {
                    if (!isSchool && !gradeCodes.contains(emExamInfo.getGradeCodes())) {
                        continue;
                    }
                    object = objectMap.get(emExamInfo.getId());
                    if (object != null && "1".equals(object.getIsStat())) {
                        examDto = new ExamInfoDto();
                        examDto.setId(emExamInfo.getId());
                        examDto.setExamName(emExamInfo.getExamName());
                        examDto.setGradeName(gradeNameMap.get(emExamInfo.getGradeCodes()));
                        examDtoList.add(examDto);
                    }
                }
            } else {
                //教研组长报告
//				List<TeachGroup> teachGroupList=SUtils.dt(teachGroupRemoteService.findBySchoolId(unitId, true),new TR<List<TeachGroup>>(){});
                List<TeachGroup> teachGroupList = getMainGroupIds(unitId);
                Set<String> subjectIds = new HashSet<>();
                if (CollectionUtils.isNotEmpty(teachGroupList)) {
                    for (TeachGroup teachGroup : teachGroupList) {
                        Set<String> teacherIds = teachGroup.getTeacherIdSet();
                        if (CollectionUtils.isNotEmpty(teacherIds) && teacherIds.contains(ownerId)) {
                            subjectIds.add(teachGroup.getSubjectId());
                        }
                    }
                }
                //获取一个考试对应的科目 已分选考学考
                Map<String, List<Course>> courseListMap = newReportService.getMapCoursrByExamIds(EntityUtils.getSet(examList, EmExamInfo::getId).toArray(new String[0]), unitId, examList);
                for (EmExamInfo emExamInfo : examList) {
                    object = objectMap.get(emExamInfo.getId());
                    if (object != null && "1".equals(object.getIsStat())) {
                        List<Course> courseList = courseListMap.get(emExamInfo.getId());
                        if (CollectionUtils.isNotEmpty(courseList)) {
                            for (Course course : courseList) {
                                if (subjectIds.contains(course.getId())) {
                                    examDto = new ExamInfoDto();
                                    examDto.setId(emExamInfo.getId());
                                    examDto.setExamName(emExamInfo.getExamName());
                                    examDto.setGradeName(gradeNameMap.get(emExamInfo.getGradeCodes()));
                                    examDto.setSubjectId(course.getId());
                                    examDto.setSubType(course.getCourseTypeId());
                                    if (StringUtils.equals(course.getCourseTypeId(), "1")) {
                                        examDto.setSubjectName(course.getSubjectName() + "(选考)");
                                    } else if (StringUtils.equals(course.getCourseTypeId(), "2")) {
                                        examDto.setSubjectName(course.getSubjectName() + "(学考)");
                                    } else {
                                        examDto.setSubjectName(course.getSubjectName());
                                    }
                                    examDtoList.add(examDto);
                                }
                            }
                        }
                    }
                }
            }
        } else if ("3".equals(type)) {//班主任报告
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findByTeacherId(ownerId), new TR<List<Clazz>>() {
            });
            //班主任的班级数量不多 一般1到2个
            for (Clazz clazz : clazzList) {
                List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadAndGradeId(unitId, acadyear, semester, gradeCodeMap.get(clazz.getGradeId()));
                if (CollectionUtils.isNotEmpty(examList)) {
                    for (EmExamInfo emExamInfo : examList) {
                        if (haveGradeCode && !emExamInfo.getGradeCodes().equals(gradeCode)) {
                            continue;
                        }
                        object = objectMap.get(emExamInfo.getId());
                        if (object != null && "1".equals(object.getIsStat())) {
                            examDto = new ExamInfoDto();
                            examDto.setId(emExamInfo.getId());
                            examDto.setExamName(emExamInfo.getExamName());
                            examDto.setGradeName(gradeNameMap.get(emExamInfo.getGradeCodes()));
                            examDto.setClassName(clazz.getClassNameDynamic());
                            examDto.setClassId(clazz.getId());
                            examDtoList.add(examDto);
                        }
                    }
                }
            }
        } else {//任课老师报告
            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList
                    (unitId, acadyear, semester, ownerId), new TR<List<ClassTeaching>>() {
            });
            //查行政班数据
            Map<String, List<ClassTeaching>> classTeachingMap = classTeachingList.stream().collect(Collectors.groupingBy(ClassTeaching::getClassId));
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(EntityUtils.getSet(classTeachingList, ClassTeaching::getClassId).toArray(new String[0])), new TR<List<Clazz>>() {
            });
            //教学班数据
            List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findListByTeacherId(ownerId, acadyear, semester), new TR<List<TeachClass>>() {
            });
            //查考试数据
            List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadAndGradeId(unitId, acadyear, semester, gradeCode);
            //获取一个考试对应的科目 已分选考学考
            if (CollectionUtils.isNotEmpty(examList)) {
                Map<String, List<Course>> courseListMap = newReportService.getMapCoursrByExamIds(EntityUtils.getSet(examList, EmExamInfo::getId).toArray(new String[0]), unitId, examList);
                Map<String, List<EmExamInfo>> codeExamMap = examList.stream().collect(Collectors.groupingBy(EmExamInfo::getGradeCodes));
                List<EmExamInfo> inList = null;
                List<ClassTeaching> classInList = null;
                List<Course> courseInList = null;
                if (CollectionUtils.isNotEmpty(teachClassList)) {
                    for (TeachClass teachClass : teachClassList) {
                        inList = codeExamMap.get(teachClass.getGradeId());//每个教学班的年级对应的考试数据
                        if (CollectionUtils.isNotEmpty(inList)) {
                            for (EmExamInfo emExamInfo : inList) {//
                                object = objectMap.get(emExamInfo.getId());
                                if (object != null && "1".equals(object.getIsStat())) {
                                    courseInList = courseListMap.get(emExamInfo.getId());
                                    if (CollectionUtils.isNotEmpty(courseInList)) {
                                        for (Course course : courseInList) {
                                            if (course.getId().equals(teachClass.getCourseId())) {
                                                examDto = new ExamInfoDto();
                                                examDto.setId(emExamInfo.getId());
                                                examDto.setExamName(emExamInfo.getExamName());
                                                examDto.setGradeName(gradeNameMap.get(emExamInfo.getGradeCodes()));
                                                examDto.setClassName(teachClass.getName());
                                                examDto.setClassId(teachClass.getId());
                                                examDto.setSubjectId(course.getId());
                                                examDto.setSubType(course.getCourseTypeId());
                                                examDto.setTeachClassType("1");//代表教学班
                                                if (StringUtils.equals(course.getCourseTypeId(), "1")) {
                                                    examDto.setSubjectName(course.getSubjectName() + "(选考)");
                                                } else if (StringUtils.equals(course.getCourseTypeId(), "2")) {
                                                    examDto.setSubjectName(course.getSubjectName() + "(学考)");
                                                } else {
                                                    examDto.setSubjectName(course.getSubjectName());
                                                }
                                                examDtoList.add(examDto);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(clazzList)) {
                    for (Clazz clazz : clazzList) {
                        classInList = classTeachingMap.get(clazz.getId());//每个班级 对应的课程信息
                        inList = codeExamMap.get(gradeCodeMap.get(clazz.getGradeId()));//每个班级的年级对应的考试数据
                        if (CollectionUtils.isNotEmpty(classInList) && CollectionUtils.isNotEmpty(inList)) {
                            for (ClassTeaching classTeaching : classInList) {//每个课程信息有一个科目数据
                                for (EmExamInfo emExamInfo : inList) {//
                                    object = objectMap.get(emExamInfo.getId());
                                    if (object != null && "1".equals(object.getIsStat())) {
                                        courseInList = courseListMap.get(emExamInfo.getId());
                                        if (CollectionUtils.isNotEmpty(courseInList)) {
                                            for (Course course : courseInList) {
                                                if (course.getId().equals(classTeaching.getSubjectId())) {
                                                    examDto = new ExamInfoDto();
                                                    examDto.setId(emExamInfo.getId());
                                                    examDto.setExamName(emExamInfo.getExamName());
                                                    examDto.setGradeName(gradeNameMap.get(emExamInfo.getGradeCodes()));
                                                    examDto.setClassName(clazz.getClassNameDynamic());
                                                    examDto.setClassId(clazz.getId());
                                                    examDto.setSubjectId(course.getId());
                                                    examDto.setSubType(course.getCourseTypeId());
                                                    if (StringUtils.equals(course.getCourseTypeId(), "1")) {
                                                        examDto.setSubjectName(course.getSubjectName() + "(选考)");
                                                    } else if (StringUtils.equals(course.getCourseTypeId(), "2")) {
                                                        examDto.setSubjectName(course.getSubjectName() + "(学考)");
                                                    } else {
                                                        examDto.setSubjectName(course.getSubjectName());
                                                    }
                                                    examDtoList.add(examDto);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        map.put("unitId", unitId);
        map.put("examDtoList", examDtoList);
        return "/examanalysis/myReport/myReportExamList.ftl";
    }

    @RequestMapping("/emReport/stuExamList/page")
    @ControllerInfo(value = "学生报告examList")
    public String showStuExamList(ModelMap map, HttpServletRequest request) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        String ownerId = loginInfo.getOwnerId();
        //取出学生已有统计结果的考试
        List<EmExamInfo> examList = reportService.queryExamsByStudentId(ownerId, acadyear, semester, null);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        //获取年级名次 考试存的是年级code
        Map<String, String> gradeNameMap = EntityUtils.getMap(gradeList, Grade::getGradeCode, Grade::getGradeName);
        List<ExamInfoDto> examDtoList = new ArrayList<>();
        ExamInfoDto examDto = null;
        for (EmExamInfo emExamInfo : examList) {
            examDto = new ExamInfoDto();
            examDto.setId(emExamInfo.getId());
            examDto.setExamName(emExamInfo.getExamName());
            examDto.setGradeName(gradeNameMap.get(emExamInfo.getGradeCodes()));
            examDtoList.add(examDto);
        }
        map.put("unitId", unitId);
        map.put("studentId", ownerId);
        map.put("examDtoList", examDtoList);
        return "/examanalysis/myReport/stuReportExamList.ftl";
    }

    @RequestMapping("/emReport/goStuReport")
    @ControllerInfo(value = "学生报告")
    public String goStuReport(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String unitId = request.getParameter("unitId");
        String studentId = request.getParameter("studentId");
        String isExport = request.getParameter("isExport");
        map.put("isExport", isExport);
        newReportService.getStuReport(map, unitId, examId, studentId);
        map.put("unitId", unitId);
        map.put("type", "5");//代表学生报告
        map.put("studentId", studentId);
        return "/examanalysis/myReport/stuReport.ftl";
    }

    @RequestMapping("/emReport/goSchoolReport")
    @ControllerInfo(value = "校级报告")
    public String goSchoolReport(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String unitId = request.getParameter("unitId");
        String isExport = request.getParameter("isExport");
        map.put("isExport", isExport);
        newReportService.getReport(map, unitId, examId, null, null);
        map.put("type", "1");
        map.put("unitId", unitId);
        return "/examanalysis/myReport/myReportSchool.ftl";
    }

    @RequestMapping("/emReport/goSchoolRepPdf")
    @ControllerInfo(value = "校级报告pdf")
    public String goSchoolReportPdf(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String unitId = request.getParameter("unitId");
        newReportService.getReport(map, unitId, examId, null, null);

        List<Course> list = reportService.getSubList(unitId, examId, "0");
        List<Course> courseList = new ArrayList<>();
        EmExamInfo exam = emExamInfoService.findOne(examId);
        Course c = new Course();
        c.setSubjectName("考试总分");
        c.setId(ExammanageConstants.ZERO32);
        courseList.add(c);
        if (exam != null && "1".equals(exam.getIsgkExamType())) {
            c = new Course();
            c.setSubjectName("赋分总分");
            c.setId(ExammanageConstants.CON_SUM_ID);
            courseList.add(c);
        }
        for (Course e : list) {
            c = new Course();
            if (StringUtils.isBlank(e.getCourseTypeId())) {//不让null 传到页面
                e.setCourseTypeId("0");
            }
            c.setId(e.getId() + "," + e.getCourseTypeId());
            String typeName = "";
            if (StringUtils.equals(e.getCourseTypeId(), "1")) {
                typeName = "选考";
            } else if (StringUtils.equals(e.getCourseTypeId(), "2")) {
                typeName = "学考";
            }
            c.setSubjectName(e.getSubjectName() + typeName);
            courseList.add(c);
        }
        map.put("courseList", courseList);
        map.put("type", "1");
        map.put("unitId", unitId);
        return "/examanalysis/myReport/myReportSchoolPdf.ftl";
    }

    @RequestMapping("/emReport/goGradeReport")
    @ControllerInfo(value = "教研组长报告")
    public String goGradeReport(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String subType = request.getParameter("subType");
        String unitId = request.getParameter("unitId");
        String isExport = request.getParameter("isExport");
        map.put("isExport", isExport);
        newReportService.getReport(map, unitId, examId, subjectId, subType);
        if (StringUtils.isNotBlank(subjectId)) {
            Course c = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
            if (c != null) {
                map.put("subjectName", c.getSubjectName());
            }
        }
        map.put("type", "2");
        map.put("unitId", unitId);
        if (StringUtils.isNotBlank(subType)) {
            map.put("subjectId", subjectId + "," + subType);
        } else {
            map.put("subjectId", subjectId);
        }
        return "/examanalysis/myReport/myReportSchool.ftl";
    }

    @RequestMapping("/emReport/goClassReport")
    @ControllerInfo(value = "班主任报告")
    public String goClassReport(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        String unitId = request.getParameter("unitId");
        String isExport = request.getParameter("isExport");
        map.put("isExport", isExport);
        map.put("type", "3");
        newReportService.getClassReport(map, unitId, examId, classId, null, null);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        if (map.get("zeroClassRange") == null) {
            return errorFtl(map, "暂无统计数据");
        }
        map.put("classId", classId);
        map.put("unitId", unitId);
        map.put("className", clazz == null ? "" : clazz.getClassNameDynamic());
        return "/examanalysis/myReport/myReportClass.ftl";
    }

    @RequestMapping("/emReport/goTeachClassReport")
    @ControllerInfo(value = "任课老师报告")
    public String goTeachClassReport(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        String subjectId = request.getParameter("subjectId");
        String subType = request.getParameter("subType");
        String teachClassType = request.getParameter("teachClassType");
        String unitId = request.getParameter("unitId");
        String isExport = request.getParameter("isExport");
        map.put("isExport", isExport);
        newReportService.getClassReport(map, unitId, examId, classId, subjectId, subType);
        if ("1".equals(teachClassType)) {//教学班
            TeachClass teachClass = SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
            map.put("className", teachClass == null ? "" : teachClass.getName());
        } else {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            map.put("className", clazz == null ? "" : clazz.getClassNameDynamic());
        }
        if (StringUtils.isNotBlank(subjectId)) {
            Course c = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
            map.put("subjectName", c == null ? "" : c.getSubjectName());
        }
        if (map.get("zeroClassRange") == null) {
            return errorFtl(map, "暂无统计数据");
        }
        map.put("type", "4");
        map.put("classId", classId);
        map.put("unitId", unitId);
        map.put("subType", subType);
        map.put("subjectId", subjectId);

        map.put("teachClassType", teachClassType);
        return "/examanalysis/myReport/myReportClass.ftl";
    }

    @ResponseBody
    @RequestMapping("/emReport/schoolClassRankCom")
    @ControllerInfo(value = "班级对比分析-名次段")
    public String getClassRankCom(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String unitId = request.getParameter("unitId");
        String jsonString = "";
        boolean subjectFlag = false;//判断是否是科目类型
        JSONObject jsonData = new JSONObject();
        try {
            if (ExammanageConstants.ZERO32.equals(subjectId)) {//考试总分
                jsonString = reportService.getRankDistributeByClassSelf(unitId, null, null, examId, null, "0");
            } else if (ExammanageConstants.CON_SUM_ID.equals(subjectId)) {//赋分总分
                jsonString = reportService.getRankDistributeByClassSelf(unitId, null, null, examId, null, "1");
            } else {
                subjectFlag = true;
                String[] ss = subjectId.split(",");
                String sub = ss.length > 1 ? ss[1] : "0";
                jsonString = reportService.getClassRank(unitId, null, null, examId, null, ss[0], sub);
            }
            //报告 -名次段 bvg
            newReportService.getClassScore(jsonData, jsonString, subjectFlag, true);//false代表分数段
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }

        return jsonData.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/emReport/schoolClassCom")
    @ControllerInfo(value = "班级对比分析-平均分/分数段")
    public String getClassCompare(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String unitId = request.getParameter("unitId");
        String jsonString = "";
        String jsonString2 = "";
        boolean flag = false;//判断学考或者是选考
        boolean subjectFlag = false;//判断是否是科目类型
        JSONObject jsonData = new JSONObject();
        try {
            if (ExammanageConstants.ZERO32.equals(subjectId)) {//考试总分
                //平均分
                jsonString = reportService.getScoreRankingByClassSelf(unitId, null, null, null, examId, "0");
                jsonString2 = reportService.getScoreDistributeByClassSelf(unitId, null, null, examId, null, "0");
            } else if (ExammanageConstants.CON_SUM_ID.equals(subjectId)) {//赋分总分
                jsonString = reportService.getScoreRankingByClassSelf(unitId, null, null, null, examId, "1");

                jsonString2 = reportService.getScoreDistributeByClassSelf(unitId, null, null, examId, null, "1");
            } else {
                subjectFlag = true;
                String[] ss = subjectId.split(",");
                String sub = ss.length > 1 ? ss[1] : "0";
                if ("1".equals(sub))
                    flag = true;
                jsonString = reportService.getClassSubRankBySelf(unitId, null, null, examId, null, ss[0], sub);

                jsonString2 = reportService.getClassScore(unitId, null, null, examId, null, ss[0], sub);
            }
            //报告 -平均分
            newReportService.getAvgScore(jsonData, jsonString, flag);
            //报告 -分数段
            newReportService.getClassScore(jsonData, jsonString2, subjectFlag, false);//false代表分数段
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }
        return jsonData.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/emReport/schoolSubjectCom")
    @ControllerInfo(value = "科目均衡度分析")
    public String getSubjectCompare(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String unitId = request.getParameter("unitId");
        JSONObject jsonData = new JSONObject();
        try {
            newReportService.getSubjectComJson(jsonData, examId, unitId, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }
        return jsonData.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/emReport/classSubjectCom")
    @ControllerInfo(value = "班级科目均衡度分析")
    public String getClassSubjectCom(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        String unitId = request.getParameter("unitId");
        JSONObject jsonData = new JSONObject();
        try {
            newReportService.getSubjectComJson(jsonData, examId, unitId, classId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }
        return jsonData.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/emReport/stuSubjectCom")
    @ControllerInfo(value = "学生科目均衡度分析")
    public String getStuSubjectCom(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String studentId = request.getParameter("studentId");
        String unitId = request.getParameter("unitId");
        JSONObject jsonData = new JSONObject();
        try {
            newReportService.getStuSubjectComJson(jsonData, examId, unitId, studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }
        return jsonData.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/emReport/schoolZeroScoreCom")
    @ControllerInfo(value = "总分进步度分析")
    public String getZeroScoreCompare(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String unitId = request.getParameter("unitId");
        JSONObject jsonData = new JSONObject();
        try {
            newReportService.getZeroScoreComJson(jsonData, examId, null, null, unitId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }
        return jsonData.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/emReport/schoolSubScoreCom")
    @ControllerInfo(value = "科目进步度分析")
    public String getSubScoreCompare(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String subjectId = request.getParameter("subjectId");
        String unitId = request.getParameter("unitId");
        JSONObject jsonData = new JSONObject();
        try {
            String[] ss = subjectId.split(",");
            String sub = ss.length > 1 ? ss[1] : "0";
            newReportService.getZeroScoreComJson(jsonData, examId, ss[0], sub, unitId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().toJSONString();
        }
        return jsonData.toJSONString();
    }

    @RequestMapping("/emReport/classZreoScoreCom")
    @ControllerInfo(value = "班级总分进步度分析")
    public String getClassZreoScoreCom(ModelMap map, HttpServletRequest request) {
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        String subjectId = request.getParameter("subjectId");
        String subType = request.getParameter("subType");
        String unitId = request.getParameter("unitId");
        try {
            newReportService.getClassZreoScoreCom(map, examId, classId, subjectId, subType, unitId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/examanalysis/myReport/myReportClassOther.ftl";
    }

    @RequestMapping("/emReport/exprotPdf/page")
    @ControllerInfo(value = "导出pdf")
    public String exprotPdf(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        String classId = request.getParameter("classId");
        String subjectId = request.getParameter("subjectId");
        String subType = request.getParameter("subType");
        String teachClassType = request.getParameter("teachClassType");
        String studentId = request.getParameter("studentId");
        String type = request.getParameter("type");
        boolean flag = "true".equals(request.getParameter("flag")) ? true : false;
        StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
        String fileName = null;
        String uu = dir.getDir() + File.separator + "emReport" + File.separator + unitId + File.separator + examId + File.separator + "report" +
                type + File.separator;
        EmExamInfo exam = emExamInfoService.findOne(examId);
        String examName = exam == null ? "" : exam.getExamName();
        if ("1".equals(type)) {
            fileName = "校级报告-" + examName;
            uu += examId + ".pdf";
        } else if ("2".equals(type)) {
            String[] ss = subjectId.split(",");
            subjectId = ss[0];
            subType = ss.length > 1 ? ss[1] : "0";
            Course c = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
            String courseName = c == null ? "" : c.getSubjectName();
            if ("1".equals(subType)) {
                courseName += "(选考)";
            } else if ("2".equals(subType)) {
                courseName += "(学考)";
            }
            fileName = "教研组长报告-" + examName + "-" + courseName;
            uu += subjectId + "_" + subType + ".pdf";
        } else if ("3".equals(type)) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            String className = clazz == null ? "" : clazz.getClassNameDynamic();
            fileName = "班主任报告-" + examName + "-" + className;
            uu += classId + ".pdf";
        } else if ("4".equals(type)) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            String className = clazz == null ? "" : clazz.getClassNameDynamic();
            Course c = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
            String courseName = c == null ? "" : c.getSubjectName();
            if ("1".equals(subType)) {
                courseName += "(选考)";
            } else if ("2".equals(subType)) {
                courseName += "(学考)";
            }
            fileName = "任课老师报告-" + examName + "-" + courseName + className;
            uu += classId + File.separator + subjectId + "_" + subType + ".pdf";
        } else if ("5".equals(type)) {
            fileName = "我的报告-";
            uu += studentId + ".pdf";
            Student stu = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            fileName += stu == null ? "" : stu.getStudentName();
        }
        MyThread myThread = new MyThread();
        myThread.setServerUrl(UrlUtils.getPrefix(request));
        myThread.setUu(uu);
        myThread.setUnitId(unitId);
        myThread.setExamId(examId);
        myThread.setClassId(classId);
        myThread.setSubjectId(subjectId);
        myThread.setSubType(subType);
        myThread.setTeachClassType(teachClassType);
        myThread.setStudentId(studentId);
        myThread.setType(type);
        myThread.setFlag(flag);
        Thread thread = new Thread(myThread);
        thread.start();
        try {
            thread.join();
            ExportUtils.outputFile(uu, fileName + ".pdf", getResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void downPdf(HttpServletResponse response, String url, String fileName) throws IOException {

    }

    class MyThread implements Runnable {
        private String serverUrl;
        private String uu;
        private String unitId;
        private String examId;
        private String classId;
        private String subjectId;
        private String subType;
        private String teachClassType;
        private String studentId;
        private String type;
        private boolean flag = false;//是否覆盖已有的pdf

        @Override
        public void run() {
//			StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
            String urlStr = null;
            if ("1".equals(type)) {
                urlStr = "/examanalysis/emReport/goSchoolRepPdf?examId=" + examId + "&unitId=" + unitId + "&isExport=true";
            } else if ("2".equals(type)) {
                urlStr = "/examanalysis/emReport/goGradeReport?examId=" + examId + "&unitId=" + unitId + "&subjectId=" + subjectId + "&subType=" + subType + "&isExport=true";
            } else if ("3".equals(type)) {
                urlStr = "/examanalysis/emReport/goClassReport?examId=" + examId + "&unitId=" + unitId + "&classId=" + classId + "&isExport=true";
            } else if ("4".equals(type)) {
                urlStr = "/examanalysis/emReport/goTeachClassReport?examId=" + examId + "&unitId=" + unitId + "&classId=" + classId + "&teachClassType=" + teachClassType + "&subjectId=" + subjectId + "&subType=" + subType + "&isExport=true";
            } else if ("5".equals(type)) {
                urlStr = "/examanalysis/emReport/goStuReport?examId=" + examId + "&unitId=" + unitId + "&studentId=" + studentId + "&isExport=true";
            }
            File f = new File(uu);
            if (f.exists()) {
                if (flag) {
                    f.delete();//删除
                    HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, "2000", null, 0, null);
                }
            } else {
                HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, null, 0, null);
            }
			/*try {
				ExportUtils.outputFile(uu, "我的报告", getResponse());
			} catch (IOException e) {
				e.printStackTrace();
			}*/
        }

        public void setUu(String uu) {
            this.uu = uu;
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getTeachClassType() {
            return teachClassType;
        }

        public void setTeachClassType(String teachClassType) {
            this.teachClassType = teachClassType;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }

}
