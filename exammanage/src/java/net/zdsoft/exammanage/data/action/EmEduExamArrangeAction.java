package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.EmOptionDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
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

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEduExamArrangeAction extends BaseAction {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    McodeRemoteService mcodeRemoteService;
    @Autowired
    SchoolRemoteService schoolRemoteService;
    @Autowired
    EmExamInfoService emExamInfoService;
    @Autowired
    EmClassInfoService emClassInfoService;
    @Autowired
    RegionRemoteService regionRemoteService;
    @Autowired
    EmExamRegionService emExamRegionService;
    @Autowired
    EmOptionService emOptionService;
    @Autowired
    EmOptionSchoolService emOptionSchoolService;
    @Autowired
    EmJoinexamschInfoService emJoinexamschInfoService;


    @RequestMapping("/examArrange/index/page")
    @ControllerInfo(value = "考试编排")
    public String showIndex(ModelMap map) {
        return "/exammanage/edu/examArrange/examArrangeIndex.ftl";
    }

    @RequestMapping("/examArrange/head/page")
    @ControllerInfo(value = "考试编排设置")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examArrange/examArrangeHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examArrange/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examArrange/examArrangeList.ftl";
        return showExamInfoIn(map, httpSession, url, true);
    }

    @RequestMapping("/examArrange/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examArrange/examArrangeList.ftl";
        return showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
    }

    @RequestMapping("/examArrange/examItemIndex/page")
    @ControllerInfo("tab")
    public String showArrangeTab(String examId, String type, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examArrange/arrangeTabIndex.ftl";
        return showTabIndex(examId, null, type, map, httpSession, url);
    }


    @RequestMapping("/examArrange/examItemIndex2/page")
    @ControllerInfo("tab")
    public String showArrangeTab2(String examId, String regionId, String type, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examArrange/arrangeTabIndex2.ftl";
        return showTabIndex(examId, regionId, type, map, httpSession, url);
    }

    @RequestMapping("/examArrange/filterIndex/page")
    @ControllerInfo("考区设置")
    public String showFilterIndex(String examId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        Region region = SUtils.dc(regionRemoteService.findByFullCode(unit.getRegionCode()), Region.class);
        List<Region> regions = SUtils.dt(regionRemoteService.findRegionsByRegionCode(region.getRegionCode()), Region.class);
        List<EmRegion> emRegions = emExamRegionService.findByExamIdAndUnitId(examId, unitId);
        if (CollectionUtils.isEmpty(emRegions)) {
            emRegions = new ArrayList<EmRegion>();
            if (CollectionUtils.isNotEmpty(regions)) {
                int i = 1;
                for (Region region2 : regions) {
                    if (StringUtils.equals("市辖区", region2.getRegionName())) {
                        continue;
                    }
                    EmRegion emRegion = new EmRegion();
                    emRegion.setExamId(examId);
                    emRegion.setExamOptionNum(0);
                    emRegion.setExamRegionCode(i < 10 ? "0" + i : i + "");
                    emRegion.setRegionCode(region2.getRegionCode());
                    emRegion.setRegionName(region2.getRegionName());
                    emRegion.setUnitId(unitId);
                    i++;
                    emRegions.add(emRegion);
                }
                emExamRegionService.saveAllEntitys(emRegions.toArray(new EmRegion[0]));
            }
        } else {
            List<EmRegion> emRegionsDes = new ArrayList<EmRegion>();
            for (EmRegion emRegion : emRegions) {
                if (StringUtils.equals("市辖区", emRegion.getRegionName())) {
                    continue;
                }
                emRegionsDes.add(emRegion);
            }
            emRegions = emRegionsDes;
        }
        map.put("emRegions", emRegions);
        map.put("examId", examId);

        return "/exammanage/edu/examArrange/filterIndex.ftl";
    }


    @RequestMapping("/examArrange/regionList/page")
    @ControllerInfo("考点设置")
    public String showRegionList(String examId, String regionId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        List<EmJoinexamschInfo> joinexamschInfoList = emJoinexamschInfoService
                .findByExamId(examId);
        Set<String> schoolIds = new HashSet<String>();
        for (EmJoinexamschInfo item : joinexamschInfoList) {
            schoolIds.add(item.getSchoolId());
        }
        EmRegion emRegion = emExamRegionService.findOne(regionId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        List<Unit> findAll = SUtils.dt(
                unitRemoteService.findListByIds(schoolIds.toArray(new String[0])),
                new TR<List<Unit>>() {
                });
        List<EmOption> emOptions = emOptionService.findByExamIdAndExamRegionId(examId, regionId);
        Set<String> opSet = new HashSet<String>();
        for (EmOption emOption : emOptions) {
            opSet.add(emOption.getId());
        }
        List<EmOptionSchool> emOptionSchools = emOptionSchoolService.findByOptionIdWithMaster(examId, opSet.toArray(new String[0]));
        boolean canShow = false;
        if (CollectionUtils.isNotEmpty(emOptionSchools)) {
            for (EmOptionSchool emOptionSchool : emOptionSchools) {
                if (emOptionSchool.getJoinStudentCount() > 0) {
                    canShow = true;
                    break;
                }
            }
        }
        map.put("canShow", canShow);
        map.put("emOptions", emOptions);
        map.put("emRegion", emRegion);
        map.put("examId", examId);
        map.put("regionId", regionId);
        map.put("unitList", findAll);

        return "/exammanage/edu/examArrange/regionList.ftl";
    }

    @ResponseBody
    @RequestMapping("/examArrange/optionsSave")
    @ControllerInfo(value = "考点保存")
    public String optionsSave(EmOptionDto emOptionDto, String examId, String regionId, HttpSession httpSession) {
        if (CollectionUtils.isNotEmpty(emOptionDto.getEmOptions())) {
            List<EmOption> emOptions = emOptionService.findByExamIdWithMaster(examId);
            List<EmOptionSchool> emOptionSchools = new ArrayList<EmOptionSchool>();
            int i = 0;
            if (CollectionUtils.isNotEmpty(emOptions)) {
                for (EmOption emOption : emOptionDto.getEmOptions()) {
                    if (emOption.getOptionCode() == null) {
                        continue;
                    }
                    for (EmOption em : emOptions) {
                        if (!StringUtils.equals(regionId, em.getExamRegionId())) {
                            if (StringUtils.equals(em.getOptionSchoolId(), emOption.getOptionSchoolId())) {
                                return error("其他考区已维护相同考点，请重新维护！");
                            }
                        }
                    }
                    i++;
                    emOption.setExamId(examId);
                    emOption.setExamRegionId(regionId);
                    if (StringUtils.isBlank(emOption.getId())) {
                        emOption.setId(UuidUtils.generateUuid());
                    }
                    for (String item : emOption.getLkxzSelect()) {
                        EmOptionSchool emOptionSchool = new EmOptionSchool();
                        emOptionSchool.setExamId(examId);
                        emOptionSchool.setOptionId(emOption.getId());
                        emOptionSchool.setJoinSchoolId(item);
                        emOptionSchools.add(emOptionSchool);
                    }
                }
            } else {
                for (EmOption emOption : emOptionDto.getEmOptions()) {
                    if (emOption.getOptionCode() == null) {
                        continue;
                    }
                    i++;
                    emOption.setExamId(examId);
                    emOption.setExamRegionId(regionId);
                    if (StringUtils.isBlank(emOption.getId())) {
                        emOption.setId(UuidUtils.generateUuid());
                    }
                    for (String item : emOption.getLkxzSelect()) {
                        EmOptionSchool emOptionSchool = new EmOptionSchool();
                        emOptionSchool.setExamId(examId);
                        emOptionSchool.setOptionId(emOption.getId());
                        emOptionSchool.setJoinSchoolId(item);
                        emOptionSchools.add(emOptionSchool);
                    }
                }
            }
            emOptionService.saveEmOptionsAndEmOptionSchools(emOptionDto.getEmOptions(), emOptionSchools, examId, regionId, i);
        }
        return success("操作成功！");
    }

    @ResponseBody
    @RequestMapping("/examArrange/optionDelete")
    @ControllerInfo("删除考点")
    public String doDeleteExamInfo(String optionId, String examId) {
        try {
            emOptionService.deleteByOptionIdAndExamId(optionId, examId);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return success("操作成功！");
    }

    @ResponseBody
    @RequestMapping("/examArrange/optionName")
    @ControllerInfo("取学校地址")
    public String doGetUnitName(String unitId) {
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        String unitName = unit != null ? unit.getAddress() : "";
        return unitName;
    }

    @ResponseBody
    @RequestMapping("/examArrange/regionSave")
    @ControllerInfo(value = "考区保存")
    public String inspectorsSave(String examId, String regionCode, String regionId, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        try {
            List<EmRegion> emRegions = emExamRegionService.findByExamIdAndRegionCode(regionCode, examId);
            if (CollectionUtils.isNotEmpty(emRegions)) {
                for (EmRegion emRegion : emRegions) {
                    if (!StringUtils.equals(emRegion.getId(), regionId)) {
                        return error("该考区编号跟其它考区编号相同，请重新输入！");
                    }
                }
            }
            EmRegion emRegionOld = emExamRegionService.findOne(regionId);
            emRegionOld.setExamRegionCode(regionCode);
            emExamRegionService.saveAllEntitys(emRegionOld);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功！");
    }

    /**
     * tab
     *
     * @param examId
     * @param type
     * @param map
     * @param httpSession
     * @return
     */
    public String showTabIndex(String examId, String regionId, String type, ModelMap map, HttpSession httpSession, String url) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examId", examId);
        map.put("regionId", regionId);
        map.put("type", type);
        return url;
    }

    /**
     * 考试主列表
     *
     * @param map
     * @param httpSession
     * @param url
     * @return
     */
    public String showHead(ModelMap map, HttpSession httpSession, String url) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        map.put("unitClass", unit.getUnitClass());
        //年级Code列表
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        List<Grade> gradeList = new ArrayList<Grade>();
        if (unit.getUnitClass() == 2) {
            //学校
            School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
            gradeList = getSchGradeList(findMapMapByMcodeIds, school);
            map.put("gradeList", gradeList);
        } else {
            List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
            });
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            map.put("gradeList", gradeList);
        }
        return url;
    }

    /**
     * 30天考试列表
     *
     * @param map
     * @param httpSession
     * @param isClear     是否去除没有设置班级的考试
     * @return
     */
    public String showExamInfoIn(ModelMap map, HttpSession httpSession, String url, boolean isClear) {
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        examInfoList = emExamInfoService.findExamList(ExammanageConstants.DAY, unitId, searchDto, true);
        if (isClear) {
            clearNoExam(examInfoList, unitId);
        }
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "1");
        return url;
    }

    /**
     * 30天前考试列表
     *
     * @param searchAcadyear
     * @param searchSemester
     * @param searchType
     * @param searchGradeCode
     * @param map
     * @param httpSession
     * @return
     */
    public String showExamInfoBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession, String url, boolean isClear) {
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        searchDto.setSearchAcadyear(searchAcadyear);
        searchDto.setSearchSemester(searchSemester);
        searchDto.setSearchType(searchType);
        searchDto.setSearchGradeCode(searchGradeCode);
        examInfoList = emExamInfoService.findExamList(ExammanageConstants.DAY, unitId, searchDto, false);
        if (isClear) {
            clearNoExam(examInfoList, unitId);
        }
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "2");
        return url;
    }

    private List<Grade> getSchGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds, School school) {
        List<Grade> gradeList = new ArrayList<Grade>();
        String sections = school.getSections();
        if (StringUtils.isNotBlank(sections)) {
            String[] sectionArr = sections.split(",");
            Integer yearLength = 0;
            Map<String, McodeDetail> map = null;
            for (String ss : sectionArr) {
                int section = Integer.parseInt(ss);
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
                if (yearLength == null || yearLength == 0) {
                    continue;
                }
                for (int j = 0; j < yearLength; j++) {
                    int grade = j + 1;
                    Grade dto = new Grade();
                    dto.setGradeCode(section + "" + grade);
                    if (map.containsKey(grade + "")) {
                        dto.setGradeName(map.get(grade + "").getMcodeContent());
                    }
                    gradeList.add(dto);
                }
            }
        }
        Collections.sort(gradeList, new Comparator<Grade>() {
            public int compare(Grade o1, Grade o2) {
                return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
            }
        });
        return gradeList;
    }

    private List<Grade> getEduGradeList(List<McodeDetail> mcodelist, Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds) {
        List<Grade> gradeList = new ArrayList<Grade>();
        // 取教育局学制微代码信息
        for (int i = 0; i < mcodelist.size(); i++) {
            McodeDetail detail = mcodelist.get(i);
            int section = Integer.parseInt(detail.getThisId());
            String thisId = detail.getThisId();
            Map<String, McodeDetail> mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-" + thisId);
            if (mcodeMap == null || mcodeMap.size() <= 0) {
                continue;
            }
            int nz = Integer.parseInt(detail.getMcodeContent());// 年制
            for (int j = 0; j < nz; j++) {
                int grade = j + 1;
                Grade dto = new Grade();
                dto.setGradeCode(section + "" + grade);
                if (mcodeMap.containsKey(grade + "")) {
                    dto.setGradeName(mcodeMap.get(grade + "").getMcodeContent());
                }
                gradeList.add(dto);
            }
        }
        Collections.sort(gradeList, new Comparator<Grade>() {
            public int compare(Grade o1, Grade o2) {
                return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
            }
        });
        return gradeList;
    }

    /**
     * 去除没有设置班级的
     *
     * @param examInfoList
     */
    private void clearNoExam(List<EmExamInfo> examInfoList, String schoolId) {
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
            List<EmClassInfo> classInfoList = emClassInfoService.findBySchoolIdAndExamIdIn(examIds.toArray(new String[]{}), schoolId);
            if (CollectionUtils.isNotEmpty(classInfoList)) {
                Set<String> userExamIds = EntityUtils.getSet(classInfoList, "examId");
                List<EmExamInfo> newExamInfoList = new ArrayList<EmExamInfo>();
                for (EmExamInfo item : examInfoList) {
                    if (userExamIds.contains(item.getId())) {
                        newExamInfoList.add(item);
                    }
                }
                examInfoList = newExamInfoList;
            } else {
                examInfoList = new ArrayList<EmExamInfo>();
            }
        }
    }
}
