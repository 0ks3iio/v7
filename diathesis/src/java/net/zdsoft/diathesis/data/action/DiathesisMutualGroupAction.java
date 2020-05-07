package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroup;
import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupService;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupStuService;
import net.zdsoft.diathesis.data.service.DiathesisSetService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 学校端专用 接口
 * 设置学生的互评小组
 *
 * @Author: panlf
 * @Date: 2019/5/31 13:42
 */
@RestController
@RequestMapping("/diathesis/mutualGroup")
public class DiathesisMutualGroupAction extends BaseAction {
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private DiathesisMutualGroupStuService diathesisMutualGroupStuService;
    @Autowired
    private DiathesisMutualGroupService diathesisMutualGroupService;
    @Autowired
    private DiathesisSetService diathesisSetService;


    @GetMapping("/getClassTree")
    public String getClassTree() {
        return JSON.toJSONString(getClassTreeList());
    }


    /**
     * 班级学生树
     *
     * @return
     */
    @GetMapping("/getClassAndStudentTree")
    public String getClassAndStudentTree() {
        String unitId = getLoginInfo().getUnitId();
        List<MutualGroupTreeDto> classTreeList = getClassTreeList();
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolId(unitId), Clazz.class);
        if(CollectionUtils.isNotEmpty(clazzList)){
            clazzList=EntityUtils.filter2(clazzList,x->Clazz.NOT_GRADUATED==x.getIsGraduate());
        }
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getArray(clazzList, x -> x.getId(), String[]::new)), Student.class);
        Map<String, List<DiathesisIdAndNameDto>> classIdStudentMap = studentList.stream().collect(Collectors.groupingBy(x -> x.getClassId(), Collectors.mapping(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getId());
            dto.setName(x.getStudentName());
            return dto;
        }, Collectors.toList())));
        for (MutualGroupTreeDto grade : classTreeList) {
            if (CollectionUtils.isEmpty(grade.getClassList())) continue;
            for (DiathesisIdAndNameDto clazz : grade.getClassList()) {
                List<DiathesisIdAndNameDto> stuList = classIdStudentMap.get(clazz.getId());
                if (CollectionUtils.isEmpty(stuList)) {
                    clazz.setStudentList(new ArrayList<>());
                } else {
                    clazz.setStudentList(classIdStudentMap.get(clazz.getId()));
                }
            }
        }
        return JSON.toJSONString(classTreeList);

    }

    public List<MutualGroupTreeDto> getClassTreeList() {
        String unitId = getLoginInfo().getUnitId();
        //Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolId(unitId), Clazz.class);
        if(CollectionUtils.isNotEmpty(clazzList)){
            clazzList=EntityUtils.filter2(clazzList,x->Clazz.NOT_GRADUATED==x.getIsGraduate());
        }

        Map<String, List<DiathesisIdAndNameDto>> map = clazzList.stream().collect(Collectors.groupingBy(x -> x.getGradeId(), Collectors.mapping(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getId());
            dto.setName(x.getClassNameDynamic());
            return dto;
        }, Collectors.toList())));
        List<MutualGroupTreeDto> list = gradeList.stream().map(x -> {
            MutualGroupTreeDto dto = new MutualGroupTreeDto();
            dto.setGradeId(x.getId());
            dto.setGradeName(x.getGradeName() + "(" + x.getOpenAcadyear() + "级)");
            List<DiathesisIdAndNameDto> classList = map.get(x.getId());
            if (CollectionUtils.isEmpty(classList)) {
                dto.setClassList(new ArrayList<>());
            } else {
                dto.setClassList(classList);
            }
            return dto;
        }).collect(Collectors.toList());
        return list;
    }

    //用班级id 获得学生列表  --所有学生
    @GetMapping("/findAllStudentByClassId")
    public String fingAllStudentByClassId(String classId) {
        if (StringUtils.isBlank(classId)) return error("classId不能为空");
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
        List<DiathesisStudentDto> result = studentList.stream().map(x -> {
            DiathesisStudentDto dto = new DiathesisStudentDto();
            dto.setStudentId(x.getId());
            dto.setStudentCode(x.getStudentCode());
            dto.setStudentName(x.getStudentName());
            return dto;
        }).collect(Collectors.toList());
        return JSON.toJSONString(result);
    }

    /**
     * 根据 classId,获得当前班级下的分组信息
     */
    @GetMapping("/findMutualGroup")
    public String findMutualGroup(String classId) {
        if (StringUtils.isBlank(classId)) return error("classId不能为空");
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId()), Semester.class);
        if (semester == null) return error("学期开没开始");
        List<DiathesisMutualGroupDto> groupList = diathesisMutualGroupService.findGroupByInfo(classId, semester.getSemester(), semester.getAcadyear().trim());
        if (groupList == null || groupList.isEmpty()) {
            groupList = new ArrayList<>();
        }
        return JSON.toJSONString(groupList);
    }

    /**
     * 获取互评小组名单
     */
    @GetMapping("/findMutualStudent")
    public String findMutualStudent(@RequestParam(required = false) String studentId) {
        if (StringUtils.isBlank(studentId)) studentId = getLoginInfo().getOwnerId();
        String filterId = studentId;
        // 全局设置 用自己的设置
        DiathesisSet set = diathesisSetService.findByUnitId(getLoginInfo().getUnitId());
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId()), Semester.class);
        List<String> groupIds = new ArrayList<>();
        Predicate<Student> filter;
        if (set.getMutualType().equals(DiathesisConstant.MUTUAL_EVALUATE_LEADER)) {
            //组长评
            List<DiathesisMutualGroup> groupList = diathesisMutualGroupService.findByLeadIdAndSemester(studentId, semester.getAcadyear(), semester.getSemester());
            List<String> ids = groupList.stream().map(x -> x.getId()).collect(Collectors.toList());
            groupIds.addAll(ids);
            //groupIds.addAll();
            filter = x -> true;
        } else {
            //互评
            String groupId = diathesisMutualGroupStuService.findMutualGroupIdByStudentId(studentId, semester.getAcadyear(), semester.getSemester());
            groupIds.add(groupId);
            filter = x -> !x.getId().equals(filterId);
        }
        if (CollectionUtils.isEmpty(groupIds)) return new Json().toJSONString();
        String[] ids = EntityUtils.getArray(diathesisMutualGroupStuService.findListByMutualGroupIdIn(groupIds), x -> x.getStudentId(), String[]::new);
        List<DiathesisStudentDto> result = SUtils.dt(studentRemoteService.findListByIds(ids), Student.class).stream().filter(filter).map(x -> {
            DiathesisStudentDto stu = new DiathesisStudentDto();
            stu.setStudentId(x.getId());
            stu.setStudentName(x.getStudentName());
            stu.setStudentCode(x.getStudentCode());
            return stu;
        }).collect(Collectors.toList());
        return JSON.toJSONString(result);
    }

    /**
     * 保存分组信息
     *
     * @param group
     * @param errors
     * @return
     */
    @PostMapping("/saveGroups")
    public String saveGroup(@RequestBody @Valid DiathesisGroupList group, Errors errors) {

        if (errors.hasFieldErrors()) return error(errors.getFieldError().getDefaultMessage());

        try {
            group.setUnitId(getLoginInfo().getUnitId());
            group.setRealName(getLoginInfo().getRealName());
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId()), Semester.class);
            RedisUtils.hasLocked("diathesisInitLock_" + getLoginInfo().getUnitId() + "_78,");
            if (semester == null) return error("当前不在任何学期!");
            group.setAcadyear(semester.getAcadyear());
            group.setSemester(semester.getSemester());
            diathesisMutualGroupService.saveGroupList(group);
        } catch (Exception e) {
            return error(e.getMessage());
        } finally {
            RedisUtils.unLock("diathesisInitLock_" + getLoginInfo().getUnitId() + "_78,");
        }
        return success("保存成功");
    }

}
