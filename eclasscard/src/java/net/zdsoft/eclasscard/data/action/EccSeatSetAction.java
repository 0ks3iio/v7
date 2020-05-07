package net.zdsoft.eclasscard.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.EccSeatSetDto;
import net.zdsoft.eclasscard.data.entity.EccSeatItem;
import net.zdsoft.eclasscard.data.service.EccSeatItemService;
import net.zdsoft.eclasscard.data.service.EccSeatSetService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/12/2 10:47
 */
@RequestMapping("/eclasscard/standard")
@Controller
public class EccSeatSetAction extends BaseAction {

    @Resource
    private ClassRemoteService classRemoteService;
    @Resource
    private TeachClassRemoteService teachClassRemoteService;
    @Resource
    private CourseRemoteService courseRemoteService;
    @Resource
    private SemesterRemoteService semesterRemoteService;
    @Resource
    private EccSeatSetService eccSeatSetService;
    @Resource
    private EccSeatItemService eccSeatItemService;
    @Resource
    private GradeRemoteService gradeRemoteService;
    @Resource
    private StudentRemoteService studentRemoteService;
//    @Resource
//    private TeachClassStuRemoteService teachClassStuRemoteService;

    @GetMapping("/classSeating/page")
    public String showIndex(ModelMap modelMap) {
        return "/eclasscard/classSeating/seatSet.ftl";
    }

    /**
     * 座位设置表首页
     * @param modelMap
     * @return
     */
    @GetMapping("/classSeating/index/page")
    public String index(ModelMap modelMap,String gradeId,String classType,String subjectId,String classId) {
        modelMap.put("gradeId",gradeId);
        modelMap.put("classType",classType);
        modelMap.put("subjectId",subjectId);
        modelMap.put("classId",classId);

        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);
        if (CollectionUtils.isNotEmpty(gradeList)) {
            modelMap.put("gradeList", EntityUtils.getList(gradeList, x -> getJsonObj(x.getId(),x.getGradeName(),"grade")));
        }
        return "/eclasscard/classSeating/seatSetIndex.ftl";
    }

//    @GetMapping("/checkHasSetSeat")
//    @ResponseBody
//    public String checkHasSetSeat(String classId){
//        if (StringUtils.isBlank(classId)){
//            return error("classId不能为空!");
//        }
//        EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(getLoginInfo().getUnitId(), classId);
//        if (seatSet==null){
//            return error("没有设置座位");
//        }else{
//            return success("已经设置");
//        }
//    }

    /**
     * 返回年级id下的下列数据
     * {
     *  行政班 : 返回所有下面班级
     *  教学班: 返回所有开的科目
     * }
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/getClassListByGradeId")
    @ResponseBody
    public String getClassListByGradeId(String gradeId) {
        Json result = new Json();
        String unitId = getLoginInfo().getUnitId();
        //行政班
        List<Clazz> classList = SUtils.dt(classRemoteService.findByGradeId(unitId, gradeId, null), Clazz.class);
        if (CollectionUtils.isNotEmpty(classList)) {
            result.put("classList", EntityUtils.getList(classList, x -> getJsonObj(x.getId(), x.getClassName(),"class")));
        }
        Semester currentSemester = SUtils.dc(semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_NEXT), Semester.class);
        //教学班
        List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findListBy(
                new String[]{"unitId", "acadyear", "semester", "gradeId", "isUsing","isDeleted"},
                new String[]{unitId, currentSemester.getAcadyear(), String.valueOf(currentSemester.getSemester()),
                    gradeId, Constant.IS_TRUE_Str,String.valueOf(Constant.IS_DELETED_FALSE)}), TeachClass.class);

        if (CollectionUtils.isNotEmpty(teachClassList)) {
            result.put("subList", getSubList(teachClassList));
        }
        result.put("code", "00");
        return result.toJSONString();
    }


    /**
     * 用科目 和gradeId 找 教学班级
     *
     * @param gradeId
     * @param subjectId
     * @return
     */

    @RequestMapping("/getTeachClass")
    @ResponseBody
    public String getTeacherClass(String gradeId, String subjectId) {
        String unitId = getLoginInfo().getUnitId();
        Semester currentSemester = SUtils.dc(semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_NEXT), Semester.class);
        Json result = new Json();
        List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId, currentSemester.getAcadyear(),
                String.valueOf(currentSemester.getSemester()), subjectId, new String[]{gradeId}, true), TeachClass.class);
        if (CollectionUtils.isNotEmpty(teachClassList)) {
            result.put("classList", EntityUtils.getList(teachClassList, x -> getJsonObj(x.getId(), x.getName(),"class")));
        }
        return result.toJSONString();
    }


    /**
     * 保存一个座位表设置
     *
     * @param seatSetDto
     * @param errors
     * @return
     */
    @PostMapping("/saveSeatSet")
    @ResponseBody
    public String saveSeatSet(@Valid @RequestBody EccSeatSetDto seatSetDto, Errors errors) {
        if (errors.hasFieldErrors()) {
            return error(errors.getFieldError().getDefaultMessage());
        }
        try {
            seatSetDto.setUnitId(getLoginInfo().getUnitId());
            eccSeatSetService.saveSeatSet(seatSetDto);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("设置成功");
    }


    /**
     * 座位表设置 行 列 过道 页面
     * @param modelMap
     * @param classId
     * @return
     */
    @GetMapping("/seatSet")
    public String seatSet(ModelMap modelMap, @RequestParam("classId") String classId) {
        EccSeatSetDto dto = eccSeatSetService.findDtoByUnitIdAndClassId(getLoginInfo().getUnitId(), classId,true);
        modelMap.put("seatSet", dto);
        return "/eclasscard/classSeating/seatSetSpecific.ftl";
    }

    /**
     * 座位表学生分布的页面
     * @param modelMap
     * @param classId
     * @return
     */
    @GetMapping("/seatTable")
    public String seatTable(ModelMap modelMap, @RequestParam("classId") String classId) {
        String unitId = getLoginInfo().getUnitId();
        //座位表设置
        EccSeatSetDto dto = eccSeatSetService.findDtoByUnitIdAndClassId(unitId, classId,false);
        if(dto!=null) {
        	//学生分布图  raw_col:name    的map形式
            Map<String, String> itemMap = new HashMap<>();
            if (StringUtils.isNotBlank(dto.getId())) {
                List<EccSeatItem> itemList = eccSeatItemService.findListBySeatId(unitId, dto.getId());
                if (CollectionUtils.isNotEmpty(itemList)) {
                    List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(EntityUtils.getArray(itemList, x -> x.getStudentId(), String[]::new)), Student.class);

                    Set<String> stuIdSet = new HashSet<>();
                    if (CollectionUtils.isNotEmpty(studentList)){
                        for (Student student : studentList) {
                            if (Constant.IS_DELETED_FALSE==student.getIsDeleted() && Student.STUDENT_NORMAL==student.getIsLeaveSchool()){
                                stuIdSet.add(student.getId());
                            }
                        }
                    }
                    itemMap = itemList.stream().filter(x->stuIdSet.contains(x.getStudentId())).collect(Collectors.toMap(x -> x.getRowNum() + "_" + x.getColNum(), x -> x.getStudentName()));
                }
            }
            modelMap.put("showSeat", true);
            modelMap.put("stuSetdataMap", itemMap);
            modelMap.put("seatSet", dto);
        }else {
        	modelMap.put("showSeat", false);
        }
        
        
        return "/eclasscard/classSeating/seatSetTable.ftl";
    }
//
//    private Set<String> getStuIdSet(String classId, String classType) {
//        TreeSet<String> stuIdSet = new TreeSet<>();
//        if (EccConstants.ECC_CLASS_TYPE_1.equals(classType)){
//            //行政班
//            List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
//            if (CollectionUtils.isNotEmpty(stuList)){
//                stuIdSet.addAll(EntityUtils.getSet(stuList, x->x.getId()));
//            }
//        }else{
//            //教学班
//            List<TeachClassStu> stuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[]{classId}), TeachClassStu.class);
//            if (CollectionUtils.isNotEmpty(stuList)){
//                SUtils.dt(studentRemoteService.findListByIds(classId), Student.class);
//
//                stuIdSet.addAll(EntityUtils.getSet(stuList,x->x.getStudentId()));
//            }
//        }
//        return stuIdSet;
//    }

    /**
     * 删除班级的座位设置
     * @param classId
     * @return
     */
    @RequestMapping("/clearClassSeatSet")
    @ResponseBody
    public String clearClassSeatSet(String classId) {
        try {
            eccSeatSetService.deleteByClassId(classId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return error("清除失败");
        }
        return success("清除成功");
    }

    private List<Json> getSubList(List<TeachClass> teachClassList) {
        Set<String> subIdSet = EntityUtils.getSet(teachClassList, x -> x.getCourseId());
        return SUtils.dt(courseRemoteService.findListByIds(subIdSet.toArray(new String[0])), Course.class).stream().sorted((x, y) -> {
            if (x.getOrderId() == null) {
                return -1;
            }
            if (y.getOrderId() == null) {
                return 1;
            }
            return x.getOrderId() - y.getOrderId();
        }).map(x -> getJsonObj(x.getId(),x.getSubjectName(),"sub")).collect(Collectors.toList());
    }

    private Json getJsonObj(String id, String name,String prefix) {
        Json json = new Json();
        json.put(prefix+"Id", id);
        json.put(prefix+"Name", name);
        return json;
    }

}
