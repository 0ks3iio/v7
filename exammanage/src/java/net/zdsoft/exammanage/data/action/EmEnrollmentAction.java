package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.data.entity.ExammanageEnrollment;
import net.zdsoft.exammanage.data.service.ExammanageEnrollmentService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEnrollmentAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private ExammanageEnrollmentService exammanageEnrollmentService;

    @RequestMapping("/enrollment/page")
    public String enrollIndex(ModelMap map) {
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
        return "/exammanage/enrollment/enrollmentIndex.ftl";
    }

    @RequestMapping("/enrollment/addEnrollment")
    @ResponseBody
    public String addEnrollment(String acadyear, String semester, String examId, String unitId) {
        ExammanageEnrollment exammanageEnrollment = new ExammanageEnrollment();
        exammanageEnrollment.setAcadyear(acadyear);
        exammanageEnrollment.setSemester(semester);
        exammanageEnrollment.setExamId(examId);
        exammanageEnrollment.setUnitId(unitId);
        exammanageEnrollment.setId(UuidUtils.generateUuid());
        try {
            List<ExammanageEnrollment> list = exammanageEnrollmentService.findListByParams(acadyear, semester, examId, unitId);
            if (CollectionUtils.isNotEmpty(list)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该次考试已有招生计划，请勿重复操作！"));
            }
            exammanageEnrollmentService.save(exammanageEnrollment);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/enrollment/enrollmentList")
    public String enrollmentList(String acadyear, String semester, String examId, String parentUnitId, ModelMap map) {
        List<ExammanageEnrollment> enrollmentList = exammanageEnrollmentService.findListByParams(acadyear, semester, examId, "");
        if (CollectionUtils.isNotEmpty(enrollmentList)) {
            Set<String> unitIds = enrollmentList.stream().map(ExammanageEnrollment::getUnitId).collect(Collectors.toSet());
            List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[0])), new TR<List<Unit>>() {
            });
            Map<String, Unit> stringUnitMap = unitList.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));
            Set<String> parentUnitIds = unitList.stream().map(Unit::getParentId).collect(Collectors.toSet());
            List<Unit> list = SUtils.dt(unitRemoteService.findListByIds(parentUnitIds.toArray(new String[0])), new TR<List<Unit>>() {
            });
            Map<String, Unit> stringUnitMap1 = list.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));

            for (Iterator<ExammanageEnrollment> it = enrollmentList.iterator(); it.hasNext(); ) {
                ExammanageEnrollment exammanageEnrollment = it.next();
                Unit unit = stringUnitMap.get(exammanageEnrollment.getUnitId());
                if (unit != null) {
                    exammanageEnrollment.setSchoolName(unit.getUnitName());
                    String parentSchId = unit.getParentId();
                    if (!parentSchId.equals(parentUnitId)) {
                        it.remove();
                    }
                    Unit parentUnit = stringUnitMap1.get(parentSchId);
                    if (parentUnit != null) {
                        exammanageEnrollment.setParentSchName(parentUnit.getUnitName());
                    }
                }
            }
        }
        map.put("enrollmentList", enrollmentList);
        return "/exammanage/enrollment/enrollmentList.ftl";
    }

    @RequestMapping("/enrollment/enrollmentSet")
    public String enrollmentSet(ModelMap modelMap) {
        String unitId = getLoginInfo().getUnitId();
        List<Unit> unitList = SUtils.dt(unitRemoteService.findByParentId(unitId), new TR<List<Unit>>() {
        });
        if (CollectionUtils.isNotEmpty(unitList)) {
            unitList.removeIf(unit -> unit.getUnitClass() != 2);
        }
        modelMap.put("unitList", unitList);
        return "/exammanage/enrollment/enrollmentSet.ftl";
    }

    @RequestMapping("/enrollment/findParentUnit")
    @ResponseBody
    public List<Unit> findParentUnit(String acadyear, String semester, String examId) {
        List<ExammanageEnrollment> enrollmentList = exammanageEnrollmentService.findListByParams(acadyear, semester, examId, "");
        List<Unit> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(enrollmentList)) {
            Set<String> unitIds = enrollmentList.stream().map(ExammanageEnrollment::getUnitId).collect(Collectors.toSet());
            List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[0])), new TR<List<Unit>>() {
            });
            Set<String> parentUnitIds = unitList.stream().map(Unit::getParentId).collect(Collectors.toSet());
            list = SUtils.dt(unitRemoteService.findListByIds(parentUnitIds.toArray(new String[0])), new TR<List<Unit>>() {
            });
        }
        return list;
    }

    @RequestMapping("/enrollment/editEnrollmentNumSet")
    public String editEnrollmentNumSet(String id, ModelMap modelMap) {
        ExammanageEnrollment exammanageEnrollment = exammanageEnrollmentService.findOne(id);
        modelMap.put("exammanageEnrollment", exammanageEnrollment);
        return "/exammanage/enrollment/enrollmentNumSet.ftl";
    }

    @RequestMapping("/enrollment/editEnrollmentNumSave")
    @ResponseBody
    public String editEnrollmentNumSave(ExammanageEnrollment exammanageEnrollment) {
        ExammanageEnrollment enrollment = exammanageEnrollmentService.findOne(exammanageEnrollment.getId());
        enrollment.setSpecialNum(exammanageEnrollment.getSpecialNum());
        enrollment.setTrickNum(exammanageEnrollment.getTrickNum());
        enrollment.setUnifiedNum(exammanageEnrollment.getUnifiedNum());
        try {
            exammanageEnrollmentService.save(enrollment);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
}
