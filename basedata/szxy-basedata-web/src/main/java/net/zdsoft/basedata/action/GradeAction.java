package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.dto.GradeDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/basedata")
public class GradeAction extends BaseAction {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ClassService classService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private SemesterService semesterService;

    private static Map<String, LinkedHashMap<String, String>> sectionMap = new HashMap<String, LinkedHashMap<String, String>>();

    static {
        LinkedHashMap<String, String> linMap = new LinkedHashMap<String, String>();
        linMap.put("0", "幼儿园");
        sectionMap.put("0", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("1", "小学");
        sectionMap.put("1", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("2", "初中");
        sectionMap.put("2", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("3", "高中");
        sectionMap.put("3", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("1", "小学");
        linMap.put("2", "初中");
        sectionMap.put("1,2", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("2", "初中");
        linMap.put("3", "高中");
        sectionMap.put("2,3", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("1", "小学");
        linMap.put("2", "初中");
        linMap.put("3", "高中");
        sectionMap.put("1,2,3", linMap);
        linMap = new LinkedHashMap<String, String>();
        linMap.put("0", "幼儿园");
        linMap.put("1", "小学");
        linMap.put("2", "初中");
        linMap.put("3", "高中");
        sectionMap.put("0,1,2,3", linMap);

    }

    @RequestMapping("/grade/index/page")
    @ControllerInfo(value = "年级管理")
    public String showIndex(ModelMap map, HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        map.put("unitId", unitId);
        School school = schoolService.findOne(unitId);
        if (StringUtils.isNotBlank(school.getSections())) {
            LinkedHashMap<String, String> linMap = sectionMap.get(school.getSections());
            if (linMap != null) {
                map.put("xdMap", linMap);
            }
            // 生成年级
            try {
                List<Grade> grades = gradeService.findByUnitIdOrderByOpenAcadyear(unitId);
                createGrades(unitId, school, grades, false);
                Map<String, List<Grade>> gradeMap = new HashMap<String, List<Grade>>();
                if (CollectionUtils.isNotEmpty(grades)) {
                    for (Grade grade : grades) {
                        List<Grade> list = gradeMap.get(String.valueOf(grade.getSection()));
                        if (list == null) {
                            list = new ArrayList<Grade>();
                        }
                        list.add(grade);
                        gradeMap.put(String.valueOf(grade.getSection()), list);
                    }
                    boolean isEdit = false;
                    for (String section : school.getSections().split(",")) {
                        if (gradeMap.get(section) == null) {
                            isEdit = true;
                            break;
                        }
                    }
                    map.put("isEdit", isEdit);
                }
                else {
                    map.put("isEdit", true);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "/basedata/grade/gradeIndex.ftl";
    }

    /**
     * 生成年级
     * 
     * @param unitId
     * @param school
     * @param isInit
     *            是否初始化
     */
    private int createGrades(String unitId, School school, List<Grade> grades, boolean isInit) {
        Semester currentSemester = semesterService.getCurrentSemester(2);
        int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentSemester.getAcadyear(), "-"));
        int curAcadyearAfter = curAcadyear + 1;
        Map<Integer, List<Grade>> graMpa = new HashMap<Integer, List<Grade>>();// 按学段区分
        for (Grade item : grades) {
            List<Grade> list = graMpa.get(item.getSection());
            if (list == null) {
                list = new ArrayList<Grade>();
            }
            list.add(item);
            graMpa.put(item.getSection(), list);
        }
        String[] split = school.getSections().split(",");
        List<Grade> saveGrades = new ArrayList<Grade>();
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService
                .findMapMapByMcodeIds(new String[] { "DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3" }),
                new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                });
        for (String item : split) {
            if (item.equals("0")) {
                // 幼儿园
                handleGrade(findMapMapByMcodeIds, unitId, school, curAcadyear, curAcadyearAfter, graMpa, saveGrades, 0,
                        isInit);
            }
            else if (item.equals("1")) {
                // 小学
                handleGrade(findMapMapByMcodeIds, unitId, school, curAcadyear, curAcadyearAfter, graMpa, saveGrades, 1,
                        isInit);
            }
            else if (item.equals("2")) {
                // 初中
                handleGrade(findMapMapByMcodeIds, unitId, school, curAcadyear, curAcadyearAfter, graMpa, saveGrades, 2,
                        isInit);
            }
            else if (item.equals("3")) {
                // 高中
                handleGrade(findMapMapByMcodeIds, unitId, school, curAcadyear, curAcadyearAfter, graMpa, saveGrades, 3,
                        isInit);
            }
        }
        if (CollectionUtils.isNotEmpty(saveGrades)) {
            gradeService.saveAllEntitys(saveGrades.toArray(new Grade[0]));
        }
        return saveGrades.size();
    }

    /**
     * 
     * @param findMapMapByMcodeIds
     * @param unitId
     * @param school
     * @param curAcadyear
     * @param curAcadyearAfter
     * @param graMpa
     * @param saveGrades
     * @param section
     * @param isInit
     *            是否是初始化
     */
    private void handleGrade(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds, String unitId, School school,
            int curAcadyear, int curAcadyearAfter, Map<Integer, List<Grade>> graMpa, List<Grade> saveGrades,
            Integer section, boolean isInit) {
        Grade saveGrade;
        List<Grade> list = graMpa.get(section);
        Integer yearLength = 0;
        Map<String, McodeDetail> map = null;
        switch (section) {
        case 0:
            yearLength = school.getInfantYear();
            map = findMapMapByMcodeIds.get("DM-RKXD-0");
            break;
        case 1:
            yearLength = school.getGradeYear();
            map = findMapMapByMcodeIds.get("DM-RKXD-1");
            break;
        case 2:
            yearLength = school.getJuniorYear();
            map = findMapMapByMcodeIds.get("DM-RKXD-2");
            break;
        case 3:
            yearLength = school.getSeniorYear();
            map = findMapMapByMcodeIds.get("DM-RKXD-3");
            break;
        default:
            break;
        }
        if (map == null) {
            map = new HashMap<String, McodeDetail>();
        }
        if (isInit && list == null && yearLength != null && yearLength > 0) {
            for (int i = 1; i <= yearLength; i++) {
                saveGrade = new Grade();
                saveGrade.setSchoolId(unitId);
                saveGrade.setSection(section);
                String gradeName = map.get(String.valueOf(i)) == null ? "未设置" : StringUtils.replace(
                        map.get(String.valueOf(i)).getMcodeContent(), "{enrollyear}", "");
                saveGrade.setGradeName(gradeName);
                saveGrade.setOpenAcadyear(String.valueOf(curAcadyear - i + 1) + "-"
                        + String.valueOf(curAcadyearAfter - i + 1));
                saveGrade.setSchoolingLength(yearLength);// 学制
                saveGrade.setAmLessonCount(4);// 上午课时
                saveGrade.setPmLessonCount(4);// 下午课时
                saveGrade.setNightLessonCount(0);// 晚上课时
                saveGrade.setGradeCode(section + "" + i);
                saveGrades.add(saveGrade);
            }
        }
        else if (list != null && list.size() > 0 && yearLength != null && yearLength > 0) {
            Grade grade = list.get(0);
            int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
            if (openAcadyear < curAcadyear) {
                saveGrade = new Grade();
                saveGrade.setSchoolId(unitId);
                saveGrade.setSection(section);
                String gradeName = map.get("1") == null ? "未设置" : StringUtils.replace(map.get("1").getMcodeContent(),
                        "{enrollyear}", "");
                saveGrade.setGradeName(gradeName);
                saveGrade.setOpenAcadyear(String.valueOf(curAcadyear) + "-" + String.valueOf(curAcadyearAfter));
                saveGrade.setSchoolingLength(yearLength);// 学制
                saveGrade.setAmLessonCount(4);// 上午课时
                saveGrade.setPmLessonCount(4);// 下午课时
                saveGrade.setNightLessonCount(0);// 晚上课时
                saveGrade.setGradeCode(section + "" + 1);
                saveGrades.add(saveGrade);
            }
        }
    }

    @RequestMapping("/grade/unit/{unitId}/list/page")
    @ControllerInfo("年级列表")
    public String showGradeByUnitIdPage(@PathVariable String unitId, ModelMap map, HttpServletRequest request,
            HttpSession httpSession) {
        map.put("unitId", unitId);
        Pagination page = createPagination(request);
        map.put("grades", gradeService.findByUnitId(unitId, page));
        map.put("permissions", getPermission(httpSession));
        map.put("pagination", page);
        return "/basedata/grade/gradeList.ftl";
    }

    @ResponseBody
    @RequestMapping("/grade/unit/{unitId}/list")
    @ControllerInfo("显示年级列表")
    public String showGradeByUnitId(@PathVariable String unitId, String searchSection, ModelMap map,
            HttpServletRequest request, HttpServletResponse response, HttpSession sesion) {
        List<Grade> grades = null;
        if (StringUtils.isNotBlank(searchSection)) {
            grades = gradeService.findByUnitId(unitId, Integer.valueOf(searchSection));
        }
        else {
            grades = gradeService.findByUnitId(unitId);
        }
        List<String> gradeIds = EntityUtils.getList(grades, "id");
        Map<String, Integer> clazzCountMap = classService.countByGradeIds(gradeIds.toArray(new String[0]));
        List<GradeDto> dtos = new ArrayList<GradeDto>();
        Semester currentSemester = semesterService.getCurrentSemester(2);
        int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentSemester.getAcadyear(), "-"));
        for (Grade grade : grades) {
            GradeDto dto = new GradeDto();
            String id = grade.getId();
            int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
            if (curAcadyear - openAcadyear + 1 > grade.getSchoolingLength()) {
                grade.setGradeName(grade.getGradeName() + "(需毕业)");
                dto.setNeedGraduate(1);
            }
            dto.setGrade(grade);
            dto.setClazzCount(clazzCountMap.get(id) == null ? 0 : clazzCountMap.get(id));
            dtos.add(dto);
        }
        return Json.toJSONString(dtos);
    }

    @ResponseBody
    @RequestMapping("/grade/initGrade")
    @ControllerInfo(value = "初始化年级")
    public String initGrade(ModelMap map, HttpServletRequest request, HttpServletResponse response,
            HttpSession httpSession) {
        try {
            LoginInfo loginInfo = getLoginInfo(httpSession);
            String unitId = loginInfo.getUnitId();
            School school = schoolService.findOne(unitId);
            List<Grade> grades = gradeService.findByUnitIdOrderByOpenAcadyear(unitId);
            int createGradesSize = createGrades(unitId, school, grades, true);
            return success("操作成功！初始化" + createGradesSize + "个年级");
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
    }

    @ResponseBody
    @RequestMapping("/grade/synNmae")
    @ControllerInfo(value = "同步年级名称")
    public String doSynNmae(ModelMap map, HttpServletRequest request, HttpServletResponse response,
            HttpSession httpSession) {
        try {
            LoginInfo loginInfo = getLoginInfo(httpSession);
            String unitId = loginInfo.getUnitId();
            List<Grade> grades = gradeService.findByUnitIdOrderByOpenAcadyear(unitId);
            List<Grade> updateGrades = new ArrayList<Grade>();
            Semester currentSemester = semesterService.getCurrentSemester(2);
            int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentSemester.getAcadyear(), "-"));
            Integer section;
            Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(
                    mcodeRemoteService.findMapMapByMcodeIds(new String[] { "DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2",
                            "DM-RKXD-3" }), new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                    });
            Map<String, McodeDetail> mcodeMap = null;
            for (Grade item : grades) {
                mcodeMap = null;
                section = item.getSection();
                switch (section) {
                case 0:
                    mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-0");
                    break;
                case 1:
                    mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-1");
                    break;
                case 2:
                    mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-2");
                    break;
                case 3:
                    mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-3");
                    break;
                default:
                    break;
                }
                if (mcodeMap == null) {
                    mcodeMap = new HashMap<String, McodeDetail>();
                }
                int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(item.getOpenAcadyear(), "-"));
                if (curAcadyear - openAcadyear + 1 <= item.getSchoolingLength()) {
                    // 不需毕业的
                    String gradeName = mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)) == null ? "未设置"
                            : StringUtils.replace(mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1))
                                    .getMcodeContent(), "{enrollyear}", "");
                    item.setGradeName(gradeName);
                    item.setGradeCode(item.getSection() + "" + (curAcadyear - openAcadyear + 1));
                    updateGrades.add(item);
                }
                else {
                    // 需毕业的
                    String gradeName = mcodeMap.get(String.valueOf(item.getSchoolingLength())) == null ? "未设置"
                            : StringUtils.replace(mcodeMap.get(String.valueOf(item.getSchoolingLength()))
                                    .getMcodeContent(), "{enrollyear}", "");
                    item.setGradeName(gradeName);
                    item.setGradeCode(item.getSection() + "" + item.getSchoolingLength());
                    updateGrades.add(item);
                }
            }
            if (CollectionUtils.isNotEmpty(updateGrades)) {
                gradeService.saveAllEntitys(updateGrades.toArray(new Grade[0]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/grade/{id}/delete")
    @ControllerInfo(value = "删除年级:{id}")
    public String doDeleteGrade(@PathVariable String id, HttpSession httpSession) {
        try {
            List<Clazz> clazzes = classService.findByGradeIdIn(id);
            if (CollectionUtils.isNotEmpty(clazzes)) {
                return error("该年级下存在班级，不能删除！");
            }
            gradeService.deleteAllIsDeleted(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    // @ResponseBody
    // @RequestMapping("/grade/update")
    // @ControllerInfo(value = "修改年级", name = "修改")
    // public String doUpdateGrade(@RequestBody GradeDto dto) {
    // String gradeId = dto.getGrade().getId();
    // Grade grade = dto.getGrade();
    // if (StringUtils.isBlank(gradeId)) {
    // return error("找不到对应的年级信息！");
    // }
    // Grade gradeo = gradeService.findOne(gradeId);
    // String acadyear = grade.getOpenAcadyear();
    // int section = grade.getSection();
    // String schoolId = grade.getSchoolId();
    // List<Grade> grades = gradeService.findBySectionAndAcadyear(schoolId, section, acadyear);
    // boolean ok = false;
    // if (CollectionUtils.isEmpty(grades))
    // ok = true;
    // for (Grade g : grades) {
    // if (StringUtils.equals(g.getId(), gradeId)) {
    // ok = true;
    // break;
    // }
    // }
    // if (!ok) {
    // return error("已经存在相同学段和学年学期的年级，不能修改！");
    // }
    // EntityUtils.copyProperties(dto.getGrade(), gradeo, true);
    // gradeService.saveOne(gradeo);
    // return success("修改成功！");
    // }

    @ResponseBody
    @RequestMapping("/grade/save")
    @ControllerInfo(value = "保存年级")
    public String doSaveGrade(@RequestBody GradeDto dto) {
        try {
            Grade grade = dto.getGrade();
            String acadyear = grade.getOpenAcadyear();
            int section = grade.getSection();
            String schoolId = grade.getSchoolId();
            int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
            Semester currentSemester = semesterService.getCurrentSemester(2);
            int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentSemester.getAcadyear(), "-"));

            if (StringUtils.isBlank(grade.getId())) {
                List<Grade> grades = gradeService.findBySectionAndAcadyear(schoolId, section, acadyear);
                if (CollectionUtils.isNotEmpty(grades)) {
                    return error("操作失败，已经存在相同学段和入学学年的年级！");
                }
                School school = schoolService.findOne(schoolId);
                Map<String, McodeDetail> mcodeMap = new HashMap<String, McodeDetail>();
                Integer yearLength = 0;
                switch (section) {
                case 0:
                    yearLength = school.getInfantYear();
                    mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds("DM-RKXD-0"),
                            new TypeReference<Map<String, McodeDetail>>() {
                            });
                    break;
                case 1:
                    yearLength = school.getGradeYear();
                    mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds("DM-RKXD-1"),
                            new TypeReference<Map<String, McodeDetail>>() {
                            });
                    break;
                case 2:
                    yearLength = school.getJuniorYear();
                    mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds("DM-RKXD-2"),
                            new TypeReference<Map<String, McodeDetail>>() {
                            });
                    break;
                case 3:
                    yearLength = school.getSeniorYear();
                    mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds("DM-RKXD-3"),
                            new TypeReference<Map<String, McodeDetail>>() {
                            });
                    break;
                default:
                    break;
                }
                grade.setGradeName(mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)) == null ? "未设置"
                        : StringUtils.replace(mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1))
                                .getMcodeContent(), "{enrollyear}", ""));
                grade.setGradeCode(section + "" + (curAcadyear - openAcadyear + 1));
                grade.setSection(section);
                grade.setSchoolingLength(yearLength);
            }
            else {
                List<Grade> grades = gradeService.findBySectionAndAcadyear(schoolId, section, acadyear);
                boolean ok = false;
                if (CollectionUtils.isEmpty(grades)) {
                    ok = true;
                }
                for (Grade g : grades) {
                    if (StringUtils.equals(g.getId(), grade.getId())) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    return error("操作失败，已经存在相同学段和学年学期的年级！");
                }
                Grade gradeo = gradeService.findOne(grade.getId());
                EntityUtils.copyProperties(grade, gradeo, true);
                gradeo.setDisplayOrder(grade.getDisplayOrder());
                grade = gradeo;
            }
            if (grade.getSchoolingLength() >= (curAcadyear - openAcadyear + 1)) {
                gradeService.saveGradeOne(grade);
            }
            else {
                return error("操作失败，学制设置有误！");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    // @RequestMapping("/grade/{id}/detail/page")
    // @ControllerInfo(value = "显示{id}年级明细", permitUrl = "/gradeDetail")
    // public String showGrade(@PathVariable String id, ModelMap map) {
    // Grade grade = gradeService.findOne(id);
    // GradeDto dto = new GradeDto();
    // dto.setGrade(grade);
    // map.put("dto", dto);
    // School school = schoolService.findOne(grade.getSchoolId());
    // List<String> fields = ColumnInfoUtils.getEntityFiledNames(Grade.class, school);
    // Map<String, ColumnInfoEntity> columnInfo = ColumnInfoUtils.getColumnInfos(Grade.class);
    // ColumnInfoUtils.copyColumnInfo(columnInfo).get("openAcadyear").setReadonly(true);
    // map.put("fields", fields);
    // map.put("columnInfo", columnInfo);
    // return "/basedata/grade/gradeDetail.ftl";
    // }

    @RequestMapping("/grade/edit/page")
    @ControllerInfo(value = "新增修改年级")
    public String showGradeAdd(String id, ModelMap map, HttpSession httpSession) {
        Grade grade = new Grade();
        GradeDto dto = new GradeDto();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        School school = schoolService.findOne(unitId);
        LinkedHashMap<String, String> linMap = sectionMap.get(school.getSections());
        if (linMap != null) {
            map.put("xdMap", linMap);
        }
        if (StringUtils.isBlank(id)) {
        }
        else {
            grade = gradeService.findOne(id);
        }
        dto.setGrade(grade);
        grade.setSchoolId(unitId);
        map.put("dto", dto);
        List<Teacher> findByUnitId = teacherService.findByUnitId(unitId);
        map.put("teacherList", findByUnitId);
        Semester currentSemester = semesterService.getCurrentSemester(2);
        int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentSemester.getAcadyear(), "-"));
        List<String> acadyearList = semesterService.findAcadeyearList();
        List<String> removeAcadyear = new ArrayList<String>();
        for (String string : acadyearList) {
            int acadyear = NumberUtils.toInt(StringUtils.substringBefore(string, "-"));
            if (acadyear - curAcadyear > 0) {
                removeAcadyear.add(string);
            }
        }
        acadyearList.removeAll(removeAcadyear);
        map.put("acadyearList", acadyearList);
        return "/basedata/grade/gradeAdd.ftl";
    }
}
