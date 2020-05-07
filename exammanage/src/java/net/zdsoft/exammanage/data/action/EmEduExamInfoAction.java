package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.SubjectClassInfoSaveDto;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.remote.service.UnitIniRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEduExamInfoAction extends BaseAction {
    @Autowired
    EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    EmExamInfoService emExamInfoService;
    @Autowired
    EmClassInfoService emClassInfoService;
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    McodeRemoteService mcodeRemoteService;
    @Autowired
    SchoolRemoteService schoolRemoteService;
    @Autowired
    EmSubjectInfoService emSubjectInfoService;
    @Autowired
    CourseRemoteService courseRemoteService;
    @Autowired
    EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    UnitIniRemoteService unitIniRemoteService;
    @Autowired
    SystemIniRemoteService systemIniRemoteService;

    /**
     * 字符串转换成日期
     *
     * @param str gs:"yyyy-MM-dd HH:mm:ss"
     * @return date
     */
    public static Date StrToDate(String str, String gs) {

        SimpleDateFormat format = new SimpleDateFormat(gs);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @RequestMapping("/examInfo/index/page")
    @ControllerInfo(value = "考试信息设置")
    public String showExamHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examInfo/examInfoHead.ftl";
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
            for (McodeDetail mcodeDetail : mcodelist) {
                UnitIni unitIni = SUtils.dc(unitIniRemoteService.getUnitIni(unitId, "UNIT.SECTION.LENGTH_" + mcodeDetail.getThisId()), UnitIni.class);
                String valueTest = "";
                if (unitIni != null && StringUtils.isNotBlank(unitIni.getNowvalue())) {
                    valueTest = unitIni.getNowvalue();
                }
                if (StringUtils.isNotBlank(valueTest)) {
                    mcodeDetail.setMcodeContent(valueTest);
                }
            }
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            map.put("gradeList", gradeList);
        }
        return url;
    }

    @RequestMapping("/examInfo/list/page")
    @ControllerInfo("考试列表")
    public String showList(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examInfo/examInfoList.ftl";
        showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, false);
        List<EmExamInfo> examInfoList = (List<EmExamInfo>) map.get("examInfoList");
        map.put("examInfoList", examInfoList);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        map.put("unitClass", unit.getUnitClass());
        return url;
    }

    @RequestMapping("/examInfo/edit/page")
    @ControllerInfo(value = "新增或修改考试")
    public String showExamInfo(String id, ModelMap map, HttpSession httpSession) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);

        EmExamInfo examInfo = null;
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);

        if (StringUtils.isNotBlank(id)) {
            examInfo = emExamInfoService.findExamInfoOne(id);
        } else {
            examInfo = new EmExamInfo();
            examInfo.setUnitId(unitId);
            Map<String, String> hMap = new HashMap<String, String>();
            hMap.put(unitId, unitId);
            examInfo.setLkxzSelectMap(hMap);
        }
        List<Unit> findAll = null;
        //年级Code列表
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        List<Grade> gradeList = new ArrayList<Grade>();
        if (unit.getUnitClass() != Unit.UNIT_CLASS_SCHOOL) {
            //教育局
            map.put("tklxMap", ExammanageConstants.eduTklxNew);
            findAll = SUtils.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {
            });
            List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
            });
            for (McodeDetail mcodeDetail : mcodelist) {
                UnitIni unitIni = SUtils.dc(unitIniRemoteService.getUnitIni(unitId, "UNIT.SECTION.LENGTH_" + mcodeDetail.getThisId()), UnitIni.class);
                String valueTest = "";
                if (unitIni != null && StringUtils.isNotBlank(unitIni.getNowvalue())) {
                    valueTest = unitIni.getNowvalue();
                }
                if (StringUtils.isNotBlank(valueTest)) {
                    mcodeDetail.setMcodeContent(valueTest);
                }
            }
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            examInfo.setExamUeType("4");
            map.put("gradeList", gradeList);
        } else {
            //学校
            map.put("tklxMap", ExammanageConstants.schTklx);
            findAll = SUtils.dt(unitRemoteService.findDirectUnits(unit.getParentId(), Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {
            });
            //学校
            School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
            gradeList = getSchGradeList(findMapMapByMcodeIds, school);
            map.put("gradeList", gradeList);
        }

        String region = systemIniRemoteService.findValue("SYSTEM.DEPLOY.SCHOOL");
        map.put("region",false);
        if("shuangyashan".equals(region)){
            map.put("region",true);
        }
        map.put("examInfo", examInfo);
        map.put("unitClass", unit.getUnitClass());
        map.put("unitList", findAll);

        List<McodeDetail> findByMcodeId = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-KSLB"), new TR<List<McodeDetail>>() {
        });

        map.put("kslbList", findByMcodeId);

        return "/exammanage/edu/examInfo/examInfoAdd.ftl";
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
                    case 9:
                        yearLength = school.getSeniorYear();
                        map = findMapMapByMcodeIds.get("DM-RKXD-9");
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

    @ResponseBody
    @RequestMapping("/examInfo/save")
    @ControllerInfo(value = "保存考试")
    public String doSaveExam(EmExamInfo examInfo) {
        try {
            if (StringUtils.isBlank(examInfo.getId())) {
                examInfo.setId(UuidUtils.generateUuid());
                //考试编号 当前年度+6位流水号---修改当前年度+学期+6位流水号
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                List<String> findExamCodeMax = emExamInfoService.findExamCodeMax();
                if (findExamCodeMax.size() > 0) {
                    String string = findExamCodeMax.get(0);
                    if (string.substring(0, 4).equals(String.valueOf(year))) {
                        Integer intValue = Integer.valueOf(string.substring(4, string.length())) + 1;
                        DecimalFormat countFormat = new DecimalFormat("000000");
                        String strValue = countFormat.format(intValue);
                        examInfo.setExamCode(year + strValue);
                    } else {
                        examInfo.setExamCode(year + "000001");
                    }
                } else {
                    examInfo.setExamCode(year + "000001");
                }
            } else {
                EmExamInfo examInfoOld = emExamInfoService.findOne(examInfo.getId());
                EntityUtils.copyProperties(examInfo, examInfoOld, true);
                examInfo = examInfoOld;
            }
            //处理校校联考
            List<EmJoinexamschInfo> joinexamschInfoAddList = new ArrayList<EmJoinexamschInfo>();
            if (ExammanageConstants.TKLX_4.equals(examInfo.getExamUeType())) {
                EmJoinexamschInfo joinexamschInfo = null;
                for (String item : examInfo.getLkxzSelect()) {
                    joinexamschInfo = new EmJoinexamschInfo();
                    joinexamschInfo.setExamId(examInfo.getId());
                    joinexamschInfo.setSchoolId(item);
                    joinexamschInfoAddList.add(joinexamschInfo);
                }
            }
            examInfo.setExamUeType("4");
            emExamInfoService.saveExamInfoOne(examInfo, joinexamschInfoAddList);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/examInfo/delete")
    @ControllerInfo("删除考试")
    public String doDeleteExamInfo(String id) {
        try {
            List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(id);
            List<EmClassInfo> findCByExamIdIn = emClassInfoService.findByExamIdAndSchoolId(id, null);

            if (CollectionUtils.isNotEmpty(findSByExamIdIn) || CollectionUtils.isNotEmpty(findCByExamIdIn)) {
                return error("已被考试科目引用或者考试下有班级存在无法删除！");
            }
            emExamInfoService.deleteAllIsDeleted(id);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return returnSuccess();
    }

    @RequestMapping("/subjectClassIndex/index/page")
    @ControllerInfo("考试科目")
    public String showSubjectClassIndex(String examId, String searchType, String searchGradeCode, String isView, ModelMap map, HttpSession httpSession) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }

        //结束时间默认+1--为了页面时间控制
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(examInfo.getExamEndDate());//当天00:00
        theCa.set(Calendar.MINUTE, 59);
        theCa.set(Calendar.HOUR_OF_DAY, 23);
        Date date = theCa.getTime();
        examInfo.setExamEndDate(date);

        map.put("examInfo", examInfo);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        boolean isEditSubject = false;//是否可以修改科目信息
        boolean isEdu = false;
        if (unit.getUnitClass() != Unit.UNIT_CLASS_SCHOOL) {
            //教育局
            isEdu = true;
            if (examInfo.getUnitId().equals(unitId)) {
                //可以修改
                isEditSubject = true;
            } else {
                isEditSubject = false;
            }
        }
        //科目列表
        List<Course> courseList = showFindSubject(unit, examInfo);
        map.put("courseList", courseList);
        map.put("isEditSubject", isEditSubject);
        map.put("isEdu", isEdu);
        //以保存的科目信息
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        map.put("subjectInfoList", subjectInfoList);

        //如果这是校校联考 但是创建的学校没有参与（这个问题实际中出现可能性小 但是可能也有误操作）
        map.put("isJoin", true);
        if ((!isEdu) && ExammanageConstants.TKLX_3.equals(examInfo.getExamUeType())) {
            //判断改单位是否参与
            List<EmJoinexamschInfo> joinList = emJoinexamschInfoService.findByExamId(examId);
            if (CollectionUtils.isNotEmpty(joinList)) {
                Set<String> schoolIds = EntityUtils.getSet(joinList, "schoolId");
                if (schoolIds.contains(unitId)) {
                    map.put("isJoin", true);
                } else {
                    map.put("isJoin", false);
                }

            } else {
                map.put("isJoin", false);
            }
        }

        if ("1".equals(isView)) {
            map.put("isEditSubject", false);
            return "/exammanage/edu/subjectClassInfo/subjectClassShow.ftl";
        }

        return "/exammanage/edu/subjectClassInfo/subjectClassIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/subjectClassInfo/list/save")
    @ControllerInfo(value = "保存考试科目")
    public String doSaveCourseInfoList(SubjectClassInfoSaveDto subjectClassInfoSaveDto, HttpSession httpSession) {
        try {
            LoginInfo loginInfo = getLoginInfo(httpSession);
            String unitId = loginInfo.getUnitId();
            List<EmSubjectInfo> saveSubjectList = new ArrayList<EmSubjectInfo>();

            EmExamInfo examInfo = emExamInfoService.findOne(subjectClassInfoSaveDto.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            List<EmSubjectInfo> subjectInfoList = subjectClassInfoSaveDto.getEmSubjectInfoList();

            if (examInfo.getUnitId().equals(unitId)) {
                //可以修改科目

            } else {
                //没有权利修改科目
                subjectInfoList = new ArrayList<EmSubjectInfo>();
            }


            String[] classIds = null;
            ;
            if (StringUtils.isNotBlank(subjectClassInfoSaveDto.getClassIdStr())) {
                classIds = subjectClassInfoSaveDto.getClassIdStr().split(",");
            }
//			if(CollectionUtils.isEmpty(subjectInfoList) && (classIds==null || classIds.length<=0)){
//				return error("没有需要保存的数据！");
//			}
            if (CollectionUtils.isNotEmpty(subjectInfoList)) {
                for (EmSubjectInfo item : subjectInfoList) {
                    if (item == null) {
                        continue;
                    }
                    //判断subjectInfoList 是否正确--忽略
                    if (StringUtils.isNotBlank(item.getSubjectId())) {
                        if (StringUtils.isBlank(item.getId())) {
                            item.setId(UuidUtils.generateUuid());
                        }
                        if (StringUtils.isNotEmpty(item.getStrStartDate())) {
                            item.setStartDate(StrToDate(item.getStrStartDate(), "yyyy-MM-dd HH:mm"));
                        }
                        if (StringUtils.isNotEmpty(item.getStrEndDate())) {
                            item.setEndDate(StrToDate(item.getStrEndDate(), "yyyy-MM-dd HH:mm"));
                        }
                        saveSubjectList.add(item);
                    }
                }
            }
            emSubjectInfoService.saveSubjectClass(examInfo, unitId, saveSubjectList, classIds);
//			if(CollectionUtils.isNotEmpty(saveSubjectList) || (classIds!=null && classIds.length>0)){
//
//			}else{
//				return error("没有需要保存的数据！");
//			}
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/subjectInfo/delete")
    @ControllerInfo("删除考试科目")
    public String doDeleteInfo(String id, String examId, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo loginInfo = getLoginInfo(httpSession);
            String unitId = loginInfo.getUnitId();
            if (!examInfo.getUnitId().equals(unitId)) {
                return error("无权限操作！");
            }
//			List<ScoreInfo> findByExamId = scoreInfoService.findByExamId(examId);
//			if(findByExamId.size()>0){
//				return error("考试下已存在成绩，不能删除科目！");
//			}
            EmSubjectInfo emSubjectInfo = emSubjectInfoService.findOne(id);
            emPlaceTeacherService.deleteAllPlaceTeacher(emSubjectInfo.getSubjectId(), examId, unitId);
            emSubjectInfoService.deleteByIdIn(id);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    private List<Course> showFindSubject(Unit unit, EmExamInfo examInfo) {
        List<Course> courseList = new ArrayList<Course>();
        Set<String> unitIds = new HashSet<String>();
        Unit djunit = SUtils.dc(unitRemoteService.findTopUnit(unit == null ? null : unit.getId()), Unit.class);
        if (unit.getUnitClass() == 2 && !ExammanageConstants.TKLX_3.equals(examInfo.getExamUeType())) {
            //学校  //教育局学科+学校
            unitIds.add(unit.getId());
            if (djunit != null) {
                unitIds.add(djunit.getId());
            }
        } else {
            //教育局学科
            if (djunit != null) {
                unitIds.add(djunit.getId());
            }

        }
        courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIds.toArray(new String[]{}), examInfo.getGradeCodes().substring(0, 1)), new TR<List<Course>>() {
        });

        return courseList;
    }

    /**
     * 考试列表
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
        examInfoList = emExamInfoService.findExamList(null, unitId, searchDto, false);
        if (isClear) {
            clearNoExam(examInfoList, unitId);
        }
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "2");
        return url;
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
