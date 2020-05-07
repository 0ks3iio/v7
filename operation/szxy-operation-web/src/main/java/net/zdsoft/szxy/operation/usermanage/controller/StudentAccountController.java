package net.zdsoft.szxy.operation.usermanage.controller;

import net.zdsoft.szxy.base.api.ClassRemoteService;
import net.zdsoft.szxy.base.api.GradeRemoteService;
import net.zdsoft.szxy.base.api.StudentRemoteService;
import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.entity.Clazz;
import net.zdsoft.szxy.base.entity.Grade;
import net.zdsoft.szxy.base.entity.Student;
import net.zdsoft.szxy.base.query.StudentQuery;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.operation.usermanage.controller.vo.ClazzVo;
import net.zdsoft.szxy.operation.usermanage.controller.vo.StudentVo;
import net.zdsoft.szxy.operation.usermanage.dto.StudentAccountQuery;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/16 上午9:46
 */
@Controller
@RequestMapping("/operation/user/manage/student")
public class StudentAccountController {

    @Resource
    private StudentRemoteService studentRemoteService;
    @Resource
    private UnitRemoteService unitRemoteService;
    @Resource
    private GradeRemoteService gradeRemoteService;
    @Resource
    private ClassRemoteService classRemoteService;
    @Resource
    private UserRemoteService userRemoteService;

    @RequestMapping(
            value = "/index",
            method = RequestMethod.GET
    )
    public String execute(String unitId, Model model) {
        model.addAttribute("unitId", unitId);
        if (StringUtils.isNotBlank(unitId)) {
            List<Grade> grades = gradeRemoteService.getGradesBySchoolId(unitId);
            model.addAttribute("gradeList", grades);
        }
        return "/usermanage/usermanage-studentIndex.ftl";
    }

    @ResponseBody
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{gradeId}/classList"
    )
    public Response doGetClassList(@PathVariable("gradeId") String gradeId) {
        List<Clazz> clazzList = classRemoteService.getClazzesByGradeId(gradeId);
        return Response.ok().data("classList", clazzList.stream().map(e -> {
            ClazzVo clazzVo = new ClazzVo();
            clazzVo.setClassName(e.getClassName());
            clazzVo.setId(e.getId());
            return clazzVo;
        }).collect(Collectors.toList())).build();
    }

    @Record(type = RecordType.URL)
    @RequestMapping(
            value = "list",
            method = RequestMethod.GET
    )
    public String doList(String unitId, StudentAccountQuery studentAccountQuery,
                         Pageable page, Model model) {

        StudentQuery studentQuery = new StudentQuery();
        studentQuery.setClassId(studentAccountQuery.getClassId());
        studentQuery.setStudentName(studentAccountQuery.getRealName());
        studentQuery.setUsername(studentAccountQuery.getUsername());
        studentQuery.setUnitId(unitId);
        if (StringUtils.isBlank(unitId)) {
            Set<String> regions = UserDataRegionHolder.getRegions();
            if (!regions.isEmpty()) {
                studentQuery.setRegions(regions);
            }
        }

        Page<Student> studentPage = studentRemoteService.queryStudents(studentQuery, page);
        Page<StudentVo> pages = new PageImpl<>(convertStudentVos(studentPage.getContent()), page, studentPage.getTotalElements());
        model.addAttribute("students", pages);
        return "/usermanage/usermanage-studentList.ftl";
    }

    private List<StudentVo> convertStudentVos(List<Student> studentList) {
        String[] schoolIds = studentList.stream().map(Student::getSchoolId).toArray(String[]::new);
        Map<String, String> unitNameMap =
                ArrayUtils.isEmpty(schoolIds) ? Collections.emptyMap() : unitRemoteService.getUnitNameMap(schoolIds);

        String[] ownerIds = studentList.stream().map(Student::getId).toArray(String[]::new);
        Map<String, String> usernameMap =
                ArrayUtils.isEmpty(schoolIds) ? Collections.emptyMap() : userRemoteService.getUsernameMapByOwnerIds(ownerIds);
        return studentList.stream().map(e->{
            StudentVo studentVo = new StudentVo();
            studentVo.setCreationTime(e.getCreationTime());
            studentVo.setStudentName(e.getStudentName());
            studentVo.setId(e.getId());
            studentVo.setSchoolName(unitNameMap.get(e.getSchoolId()));
            studentVo.setUsername(usernameMap.get(e.getId()));
            return studentVo;
        }).collect(Collectors.toList());
    }
}
