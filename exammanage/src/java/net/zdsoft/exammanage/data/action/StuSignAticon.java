package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage")
public class StuSignAticon extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmEnrollStuCountService emEnrollStuCountService;

    @RequestMapping("/edu/stuSign/page")
    public String stuSignIndex(ModelMap map) {
        //学年学期
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        map.put("unitId", unitId);
        return "/exammanage/stuSign/stuSignIndex.ftl";
    }

    @RequestMapping("/stu/stuSignList/page")
    public String stuSignList(String year, String semester, ModelMap map) {
        List<EmExamInfo> emExamInfoList = new ArrayList<>();
        Set<String> joinExamIds = new HashSet<>();
        List<EmJoinexamschInfo> emJoinexamschInfos = emJoinexamschInfoService.findListBy(new String[]{"schoolId"}, new String[]{getLoginInfo().getUnitId()});
        if (CollectionUtils.isNotEmpty(emJoinexamschInfos)) {
            joinExamIds = emJoinexamschInfos.stream().map(EmJoinexamschInfo::getExamId).collect(Collectors.toSet());
        }
        emExamInfoList = emExamInfoService.findByUnitIdAndAcadAndGradeId("", year, semester, "");
        for (Iterator<EmExamInfo> it = emExamInfoList.iterator(); it.hasNext(); ) {
            EmExamInfo emExamInfo = it.next();
            if(emExamInfo.getIsStuSign()!=null) {
                if (!emExamInfo.getIsStuSign().equals("1")) {
                    it.remove();
                    continue;
                }
            }
            if (emExamInfo.getSignStartTime() != null && emExamInfo.getSignEndTime() != null) {
                if (emExamInfo.getSignStartTime().compareTo(new Date()) > 0) {
                    it.remove();
                    continue;
                } else if (emExamInfo.getSignEndTime().compareTo(new Date()) < 0) {
                    it.remove();
                    continue;
                }
                if (CollectionUtils.isNotEmpty(joinExamIds)) {
                    if (!joinExamIds.contains(emExamInfo.getId())) {
                        it.remove();
                        continue;
                    }
                }
                emExamInfo.setState("-1");
            } else {
                it.remove();
            }
        }
        if (CollectionUtils.isNotEmpty(emExamInfoList)) {
            Set<String> examIds = emExamInfoList.stream().map(EmExamInfo::getId).collect(Collectors.toSet());
            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamIds(examIds);
            List<EmEnrollStudent> emEnrollStudents = emEnrollStudentService.findMapByExamIdsAndStudentId(examIds.toArray(new String[0]), getLoginInfo().getOwnerId());
            Map<String, EmEnrollStudent> map1 = new HashMap<>();
            if (CollectionUtils.isNotEmpty(emEnrollStudents)) {
                map1 = emEnrollStudents.stream().collect(Collectors.toMap(EmEnrollStudent::getExamId, Function.identity()));
            }
            Map<String, List<EmSubjectInfo>> stringListMap = new HashMap<>();
            Map<String, Course> courseMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(subjectInfoList)) {
                stringListMap = subjectInfoList.stream().collect(Collectors.groupingBy(EmSubjectInfo::getExamId));
                Set<String> courseIds = subjectInfoList.stream().map(EmSubjectInfo::getSubjectId).collect(Collectors.toSet());
                List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>() {
                });
                courseMap = courseList.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
            }
            for (EmExamInfo emExamInfo : emExamInfoList) {
                String subjectName = "";
                if (MapUtils.isNotEmpty(map1)) {
                    EmEnrollStudent emEnrollStudent = map1.get(emExamInfo.getId());
                    if (emEnrollStudent != null) {
                        emExamInfo.setState(emEnrollStudent.getHasPass());
                    }
                }
                if (MapUtils.isNotEmpty(stringListMap)) {
                    List<EmSubjectInfo> emSubjectInfos = stringListMap.get(emExamInfo.getId());
                    if (CollectionUtils.isNotEmpty(emSubjectInfos)) {
                        int i = 0;
                        for (EmSubjectInfo emSubjectInfo : emSubjectInfos) {
                            Course course = courseMap.get(emSubjectInfo.getSubjectId());
                            if (i == 0) {
                                if (course != null) {
                                    subjectName = subjectName + course.getSubjectName();
                                }
                            } else {
                                subjectName = subjectName + "," + course.getSubjectName();
                            }
                            i++;
                        }
                    }
                }
                emExamInfo.setSubjectNames(subjectName);
            }
        }
        map.put("emExamInfoList", emExamInfoList);
        return "/exammanage/stuSign/stuSignList.ftl";
    }

    @RequestMapping("/stu/stuSign/stuSignConfirmInfo")
    @ControllerInfo(value = "学生报名信息确认")
    public String stuSignConfirmInfo(String id, String unitId, ModelMap map) {
        map.put("id", id);
        map.put("unitId", unitId);
        Unit unit = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()), Unit.class);
        Student student = SUtils.dc(studentRemoteService.findOneById(getLoginInfo().getOwnerId()), Student.class);
        map.put("className", "");
        if (StringUtils.isNotBlank(student.getClassId())) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
            map.put("className", clazz.getClassNameDynamic());
        }
        map.put("student", student);
        map.put("unit", unit);
        return "/exammanage/stuSign/stuSignConfirmInfo.ftl";
    }

    @RequestMapping("/stu/stuSign/doEnroll")
    @ControllerInfo(value = "学生报名")
    @ResponseBody
    public String doEnroll(String examId, String unitId) {
        List<EmEnrollStuCount> emEnrollStuCounts = emEnrollStuCountService.findByExamIdAndSchoolIdIn(examId,new String[]{getLoginInfo().getUnitId()});
        EmEnrollStuCount emEnrollStuCount = new EmEnrollStuCount();
        if(CollectionUtils.isNotEmpty(emEnrollStuCounts)){
            emEnrollStuCount = emEnrollStuCounts.get(0);
            emEnrollStuCount.setSumCount(emEnrollStuCount.getSumCount()+1);
        }else {
            emEnrollStuCount = new EmEnrollStuCount();
            emEnrollStuCount.setId(UuidUtils.generateUuid());
            emEnrollStuCount.setExamId(examId);
            emEnrollStuCount.setSchoolId(getLoginInfo().getUnitId());
            emEnrollStuCount.setSumCount(1);
            emEnrollStuCount.setNotPassNum(0);
            emEnrollStuCount.setPassNum(0);
        }
        synchronized (this) {
            emEnrollStuCountService.save(emEnrollStuCount);
        }

        Student student = SUtils.dc(studentRemoteService.findOneById(getLoginInfo().getOwnerId()), Student.class);
        EmEnrollStudent emEnrollStudent = new EmEnrollStudent();
        emEnrollStudent.setExamId(examId);
        emEnrollStudent.setHasPass("0");
        emEnrollStudent.setSchoolId(getLoginInfo().getUnitId());
        emEnrollStudent.setUnitId(unitId);
        emEnrollStudent.setStudentId(getLoginInfo().getOwnerId());
        emEnrollStudent.setId(UuidUtils.generateUuid());
        emEnrollStudent.setClassId(student == null ? "" : student.getClassId());
        try {
            List<EmEnrollStudent> list = emEnrollStudentService.findByExamIdAndSchoolIdAndStudentIdIn(examId, getLoginInfo().getUnitId(), new String[]{getLoginInfo().getOwnerId()});
            if (CollectionUtils.isNotEmpty(list)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该学生已有报名记录，请勿重复报名！"));
            }
            emEnrollStudentService.save(emEnrollStudent);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();

    }
}
