package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEduExamResultAction extends BaseAction {

    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private RegionRemoteService regionRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private EmExamRegionService emExamRegionService;
    @Autowired
    private EmOptionService emOptionService;
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmArrangeService emArrangeService;

    @RequestMapping("/examResult/detailList/page")
    @ControllerInfo(value = "查看结果")
    public String showDetailList(ModelMap map, HttpServletRequest request) {
        String url = "/exammanage/edu/examResult/examResultDetailList.ftl";
        return showExamDetailList(map, request, url);
    }

    @RequestMapping("/examResult/studentList/page")
    @ControllerInfo(value = "考生名单")
    public String showStudentList(ModelMap map, HttpServletRequest request) {
        String examPlaceId = request.getParameter("examPlaceId");
        List<EmPlaceStudent> emPlaceStudentList = emPlaceStudentService.findByExamPlaceIdAndGroupId(null, examPlaceId);
        EmPlace emPlace = emPlaceService.findOne(examPlaceId);
        if (emPlace != null) {
            EmOption emOption = emOptionService.findOne(emPlace.getOptionId());
            if (emOption != null) {
                EmRegion emRegion = emExamRegionService.findOne(emOption.getExamRegionId());
                map.put("regionName", emRegion == null ? "" : emRegion.getRegionName());
                map.put("optionName", emOption.getOptionName());
            }
            map.put("placeCode", emPlace.getExamPlaceCode());
        }
        map.put("emPlaceStudentList", emPlaceStudentList);
        map.put("cityRegionCode", request.getParameter("cityRegionCode"));
        map.put("examId", request.getParameter("examId"));
        map.put("regionId", request.getParameter("regionId"));
        map.put("optionId", request.getParameter("optionId"));
        map.put("noArrangeNum", request.getParameter("noArrangeNum"));
        return "/exammanage/edu/examResult/examResultStudentList.ftl";
    }

    @RequestMapping("/examResult/index/page")
    @ControllerInfo(value = "编排结果")
    public String showIndex(ModelMap map) {
        return "/exammanage/edu/examResult/examResultIndex.ftl";
    }

    @RequestMapping("/examResult/head/page")
    @ControllerInfo(value = "编排结果主列表")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examResult/examResultHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examResult/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examResult/examResultList.ftl";
        return showExamInfoIn(map, httpSession, url, true);
    }

    @RequestMapping("/examResult/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(EmExamInfoSearchDto searchDto, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examResult/examResultList.ftl";
        return showExamInfoBefore(searchDto, map, httpSession, url, true);
    }

    /**
     * 编排结果 包括头部
     *
     * @param map
     * @param request
     * @param url
     * @return
     */
    public String showExamDetailList(ModelMap map, HttpServletRequest request, String url) {
        String examId = request.getParameter("examId");
        String cityRegionCode = request.getParameter("cityRegionCode");
        String regionId = request.getParameter("regionId");
        String optionId = request.getParameter("optionId");
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        Unit unit = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()), Unit.class);
        if (unit != null) {
            //获取省级单位下的市级
            Region region = SUtils.dc(regionRemoteService.findByFullCode(unit.getRegionCode()), Region.class);
            if (region != null && region.getRegionCode().length() == 2) {
                List<Region> regionList = SUtils.dt(regionRemoteService.findSameRegion(region.getRegionCode(), 4), new TR<List<Region>>() {
                });
                List<EmRegion> emRegionList = emExamRegionService.findByExamIdAndLikeCode(examId, null, getLoginInfo().getUnitId());
                Map<String, Region> regionMap = EntityUtils.getMap(regionList, "regionCode");
                List<Region> lastRegionList = new ArrayList<Region>();
                //过滤没有考点的市级
                if (CollectionUtils.isNotEmpty(emRegionList)) {
                    Map<String, String> testMap = new HashMap<String, String>();
                    for (EmRegion emRegion : emRegionList) {
                        String subRegionCode = emRegion.getRegionCode().substring(0, 4);
                        if (testMap.get(subRegionCode) == null && regionMap.containsKey(subRegionCode)) {
                            lastRegionList.add(regionMap.get(subRegionCode));
                        }
                        testMap.put(subRegionCode, "one");
                    }
                }
                map.put("regionList", lastRegionList);
            }
        }
        //TODO
        List<EmRegion> emRegionList = emExamRegionService.findByExamIdAndLikeCode(examId, cityRegionCode, getLoginInfo().getUnitId());
        List<EmOption> emOptionList = emOptionService.findByExamIdAndExamRegionIdIn(examId, regionId, EntityUtils.getSet(emRegionList, "id").toArray(new String[0]));
        Pagination page = createPagination();
        if (CollectionUtils.isNotEmpty(emOptionList)) {
            Set<String> optionIds = new HashSet<String>();
            if (StringUtils.isNotBlank(optionId)) {
                optionIds.add(optionId);
            } else {
                optionIds = EntityUtils.getSet(emOptionList, "id");
            }
            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndOptionIds(examId, optionIds.toArray(new String[0]), page);
            setRegionOptionName(emRegionList, emOptionList, emPlaceList);
            map.put("emPlaceList", emPlaceList);
        }
        map.put("examName", examInfo == null ? "" : examInfo.getExamName());
        map.put("emRegionList", emRegionList);
        map.put("emOptionList", emOptionList);
        map.put("examId", examId);
        map.put("regionId", regionId);
        map.put("optionId", optionId);
        map.put("cityRegionCode", cityRegionCode);
        map.put("regionPick", request.getParameter("regionPick"));
        map.put("optionPick", request.getParameter("optionPick"));
        map.put("noArrangeNum", request.getParameter("noArrangeNum"));
        map.put("pagination", page);
        sendPagination(request, map, page);
        return url;
    }

    /**
     * 判断是否省级单位
     *
     * @return
     */
    public boolean isProUnit(Unit unit) {
        if (unit != null) {
            Region region = SUtils.dc(regionRemoteService.findByFullCode(unit.getRegionCode()), Region.class);
            if (region != null && region.getRegionCode().length() == 2) {
                return true;
            }
        }
        return false;
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
    public String showExamInfoBefore(EmExamInfoSearchDto searchDto, ModelMap map, HttpSession httpSession, String url, boolean isClear) {
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        examInfoList = emExamInfoService.findExamList(ExammanageConstants.DAY, unitId, searchDto, false);
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            setStudentNum(examInfoList, unitId, isClear);

            Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
            List<EmArrange> arranges = emArrangeService.findByExamIdIn(examIds.toArray(new String[0]));
            Set<String> aExamIds = EntityUtils.getSet(arranges, "examId");
            for (EmExamInfo emExamInfo : examInfoList) {
                if (!aExamIds.contains(emExamInfo.getId())) {
                    emExamInfo.setArrangeResult(-1);
                    continue;
                }
                String hasStat = RedisUtils.get("arrangeExam_" + emExamInfo.getId());
                if (StringUtils.isNotBlank(hasStat)) {
                    emExamInfo.setArrangeResult(1);
                } else {
                    emExamInfo.setArrangeResult(0);
                }
            }
        }
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "2");
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
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        examInfoList = emExamInfoService.findExamList(ExammanageConstants.DAY, unitId, new EmExamInfoSearchDto(), true);
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            setStudentNum(examInfoList, unitId, isClear);

            Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
            List<EmArrange> arranges = emArrangeService.findByExamIdIn(examIds.toArray(new String[0]));
            Set<String> aExamIds = EntityUtils.getSet(arranges, "examId");
            for (EmExamInfo emExamInfo : examInfoList) {
                if (!aExamIds.contains(emExamInfo.getId())) {
                    emExamInfo.setArrangeResult(-1);
                    continue;
                }
                String hasStat = RedisUtils.get("arrangeExam_" + emExamInfo.getId());
                if (StringUtils.isNotBlank(hasStat)) {
                    emExamInfo.setArrangeResult(1);
                } else {
                    emExamInfo.setArrangeResult(0);
                }
            }
        }
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "1");
        return url;
    }

    /**
     * 冗余报考编排学生数
     *
     * @param examInfoList
     * @return
     */
    public void setStudentNum(List<EmExamInfo> examInfoList, String unitId, boolean isClear) {
        if (isClear) {
            clearNoExam(examInfoList, unitId);
        }
        Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
        //报考人数
        //Map<String,List<EmEnrollStudent>> map=emEnrollStudentService.findMapByExamIds(examIds.toArray(new String[0]));
        Map<String, Integer> allMap = emEnrollStudentService.getStudentAllCount(examIds.toArray(new String[0]));
        Map<String, Integer> passMap = emEnrollStudentService.getStudentPassNum(examIds.toArray(new String[0]));
        Map<String, Integer> auditMap = emEnrollStudentService.getStudentAuditNum(examIds.toArray(new String[0]));
        //编排人数
        //Map<String,List<EmPlaceStudent>> placeMap=emPlaceStudentService.findMapByExamIds(examIds.toArray(new String[0]));
        Map<String, Integer> studentMap = emPlaceStudentService.getAllCountMap(examIds.toArray(new String[0]));
        List<EmEnrollStudent> emstudentList = null;
        List<EmPlaceStudent> placestudentList = null;
        for (EmExamInfo emExamInfo : examInfoList) {
            int countNum = 0;
            int passNum = 0;
            int auditNum = 0;
            int arrangeNum = 0;
            //emstudentList=map.get(emExamInfo.getId());
            //if(CollectionUtils.isNotEmpty(emstudentList)){
            //	countNum=emstudentList.size();
            //	for(EmEnrollStudent emstudent:emstudentList){
            //		if(emstudent.getHasPass().equals("1")){//审核通过
            //			passNum++;
            //		}else if(emstudent.getHasPass().equals("0")){//待审核
            //			auditNum++;
            //		}
            //	}
            //	emExamInfo.setCountNum(countNum);
            //	emExamInfo.setPassNum(passNum);
            //	emExamInfo.setAuditNum(auditNum);
            //}
            if (MapUtils.isNotEmpty(allMap) && allMap.containsKey(emExamInfo.getId())) {
                countNum = allMap.get(emExamInfo.getId());
            }
            emExamInfo.setCountNum(countNum);
            if (MapUtils.isNotEmpty(passMap) && passMap.containsKey(emExamInfo.getId())) {
                passNum = passMap.get(emExamInfo.getId());
            }
            emExamInfo.setPassNum(passNum);
            if (MapUtils.isNotEmpty(auditMap) && auditMap.containsKey(emExamInfo.getId())) {
                auditNum = auditMap.get(emExamInfo.getId());
            }
            emExamInfo.setAuditNum(auditNum);
            //placestudentList=placeMap.get(emExamInfo.getId());
            //if(CollectionUtils.isNotEmpty(placestudentList)){
            //	arrangeNum=placestudentList.size();
            //	emExamInfo.setArrangeNum(arrangeNum);
            //	emExamInfo.setNoArrangeNum(passNum-arrangeNum);
            //}
            if (MapUtils.isNotEmpty(studentMap) && studentMap.containsKey(emExamInfo.getId())) {
                arrangeNum = studentMap.get(emExamInfo.getId());
            }
            emExamInfo.setArrangeNum(arrangeNum);
            emExamInfo.setNoArrangeNum(passNum - arrangeNum);
        }

    }

    /**
     * 编排主列表
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
     * 设置考区考点名称
     *
     * @param emRegionList
     * @param emOptionList
     * @param emPlaceList
     */
    public void setRegionOptionName(List<EmRegion> emRegionList, List<EmOption> emOptionList, List<EmPlace> emPlaceList) {
        //List<EmPlace> lastPlaceList=new ArrayList<EmPlace>();
        Map<String, EmRegion> regionMap = EntityUtils.getMap(emRegionList, "id");
        Map<String, EmOption> optionMap = EntityUtils.getMap(emOptionList, "id");
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            EmOption emOption = null;
            EmRegion emRegion = null;
            for (EmPlace emPlace : emPlaceList) {
                emOption = optionMap.get(emPlace.getOptionId());
                if (emOption != null) {
                    emPlace.setOptionName(emOption.getOptionName());
                    emPlace.setOptionCode(emOption.getOptionCode());
                    emRegion = regionMap.get(emOption.getExamRegionId());
                    if (emRegion != null) {
                        emPlace.setRegionName(emRegion.getRegionName());
                        emPlace.setExamRegionCode(emRegion.getExamRegionCode());
                    }
                }
                //lastPlaceList.add(emPlace);
            }
			/*Collections.sort(lastPlaceList, new Comparator<EmPlace>(){
				@Override
				public int compare(EmPlace o1, EmPlace o2) {
					if(StringUtils.isNotBlank(o1.getExamRegionCode()) && StringUtils.isNotBlank(o2.getExamRegionCode()) && 
							!o1.getExamRegionCode().equals(o2.getExamRegionCode())){
						return o1.getExamRegionCode().compareTo(o2.getExamRegionCode());
					}else if(StringUtils.isNotBlank(o1.getOptionCode()) && StringUtils.isNotBlank(o2.getOptionCode()) &&
							!o1.getOptionCode().equals(o2.getOptionCode())){
						return o1.getOptionCode().compareTo(o2.getOptionCode());
					}else if(StringUtils.isNotBlank(o1.getExamPlaceCode()) && StringUtils.isNotBlank(o2.getExamPlaceCode())){
						return o1.getExamPlaceCode().compareTo(o2.getExamPlaceCode());
					}
					return 0;
				}
				
			});*/
        }
        //return lastPlaceList;
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
