package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.ExammanageAudition;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.ExammanageAuditionService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage/edu")
public class ExammanageAuditionAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ExammanageAuditionService exammanageAuditionService;

    @RequestMapping("/audition/page")
    public String exammanageAuditionIndex(ModelMap map) {
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
        map.put("unitId", getLoginInfo().getUnitId());
        return "/exammanage/audition/exammanageAuditionIndex.ftl";
    }

    @RequestMapping("/audition/findSchoolList")
    @ResponseBody
    public List<Unit> findSchoolList(String year,String semester,String examId) {
        List<ExammanageAudition> list = exammanageAuditionService.findListBy(new String[]{"acadyear","semester","examId"},new String []{year,semester,examId});
        List<Unit> unitList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> unitIds = list.stream().map(ExammanageAudition::getSchoolId).collect(Collectors.toSet());
            unitList = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[0])), new TR<List<Unit>>() {
            });
        }
        return unitList;
    }

    @RequestMapping("/audition/exammanageAuditionList")
    public String exammanageAuditionList(String schoolId, String acadyear, String semester, String examId, ModelMap map) {
        List<ExammanageAudition> list = exammanageAuditionService.findListBy(new String[]{"schoolId", "acadyear", "semester", "examId"}, new String[]{schoolId, acadyear, semester, examId});
        int passNum = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> studentIds = list.stream().map(ExammanageAudition::getStudentId).collect(Collectors.toSet());
            Set<String> schoolIds = list.stream().map(ExammanageAudition::getSchoolId).collect(Collectors.toSet());
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
            List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<Unit>>() {
            });
            Map<String, Unit> stringUnitMap = unitList.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));
            for (ExammanageAudition exammanageAudition : list) {
                Student student = studentMap.get(exammanageAudition.getStudentId());
                if (student != null) {
                    exammanageAudition.setStudentName(student.getStudentName());
                    exammanageAudition.setStuCode(student.getStudentCode());
                    exammanageAudition.setIdentityCard(student.getIdentityCard());
                    exammanageAudition.setSex(student.getSex());
                }
                if(exammanageAudition.getIsPass()!=null) {
                    if (exammanageAudition.getIsPass().equals("1")) {
                        passNum = passNum + 1;
                    }
                }
                Unit unit = stringUnitMap.get(exammanageAudition.getSchoolId());
                if (unit != null) {
                    exammanageAudition.setSchoolName(unit.getUnitName());
                }
            }
        }
        map.put("passNum",passNum);
        map.put("auditionList", list);
        return "/exammanage/audition/exammanageAuditionList.ftl";
    }

    @RequestMapping("/audition/auditionDel")
    @ResponseBody
    public String auditionDel(String id) {
        try {
            exammanageAuditionService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnSuccess();
    }

    @RequestMapping("/audition/updateState")
    @ResponseBody
    public String updateState(String id, String isPass) {
        ExammanageAudition exammanageAudition = exammanageAuditionService.findOne(id);
        try {
            exammanageAudition.setIsPass(isPass);
            exammanageAuditionService.save(exammanageAudition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnSuccess();
    }

    @RequestMapping("/audition/auditionSet")
    public String auditionSet(String examId, ModelMap modelMap) {
        List<EmEnrollStudent> list = emEnrollStudentService.findByExamId(examId);
        List<Student> students = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            list.removeIf(student -> !student.getHasPass().equals("1"));
            Set<String> studentIds = list.stream().map(EmEnrollStudent::getStudentId).collect(Collectors.toSet());
            students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
        }
        modelMap.put("studentList", students);
        return "/exammanage/audition/auditionSet.ftl";
    }

    @RequestMapping("/audition/auditionSetSave")
    @ResponseBody
    public String auditionSetSave(String schoolId, String acadyear, String semester, String examId, String studentId) {
        List<ExammanageAudition> list = exammanageAuditionService.findListBy(new String[]{ "acadyear", "semester", "examId", "studentId"}, new String[]{acadyear, semester, examId, studentId});
        if (CollectionUtils.isNotEmpty(list)) {
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该学生已加入面试列表，请勿重复操作！"));
        }
        ExammanageAudition exammanageAudition = new ExammanageAudition();
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
        exammanageAudition.setStudentId(studentId);
        exammanageAudition.setAcadyear(acadyear);
        exammanageAudition.setSemester(semester);
        if(StringUtils.isNotBlank(student.getSchoolId())) {
            exammanageAudition.setSchoolId(student.getSchoolId());
        }
        exammanageAudition.setExamId(examId);
        exammanageAudition.setId(UuidUtils.generateUuid());
        try {
            exammanageAuditionService.save(exammanageAudition);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
}
