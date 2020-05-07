package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.EmClassInfoService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmPlaceStudentService;
import net.zdsoft.exammanage.data.service.EmSubjectInfoService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmExamCommonAction extends BaseAction {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    StudentRemoteService studentRemoteService;
    @Autowired
    CourseRemoteService courseRemoteService;
    @Autowired
    SchoolRemoteService schoolRemoteService;
    @Autowired
    McodeRemoteService mcodeRemoteService;

    @Autowired
    EmSubjectInfoService emSubjectInfoService;
    @Autowired
    EmExamInfoService emExamInfoService;
    @Autowired
    EmClassInfoService emClassInfoService;
    @Autowired
    EmPlaceStudentService emPlaceStudentService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    RhKeyUnitRemoteService rhKeyUnitRemoteService;

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
        boolean isShowRh = false;
        if (unit.getUnitClass() == 2) {
            //学校
            School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
            gradeList = getSchGradeList(findMapMapByMcodeIds, school);
            map.put("gradeList", gradeList);
            List<RhKeyUnit> keyList = SUtils.dt(rhKeyUnitRemoteService.findByUnitIdAnduKeyId(unitId, null), new TR<List<RhKeyUnit>>() {
            });
            if (CollectionUtils.isNotEmpty(keyList)) {
                isShowRh = true;
            }
        } else {
            List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
            });
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            map.put("gradeList", gradeList);
        }
        map.put("isShowRh", isShowRh);
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

    /**
     * tab
     *
     * @param examId
     * @param type
     * @param map
     * @param httpSession
     * @return
     */
    public String showTabIndex(String examId, String type, ModelMap map, HttpSession httpSession, String url) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examId", examId);
        map.put("type", type);
        map.put("isgk", examInfo.getIsgkExamType());
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

    /**
     * 已安排考场学生人数
     *
     * @param examId
     * @param unitId
     * @param emPlaceList
     * @param groupId     TODO
     * @return
     */
    public int findArrangeStuNum(String examId, String unitId, List<EmPlace> emPlaceList, String groupId) {
        Set<String> emPlaceIds = EntityUtils.getSet(emPlaceList, EmPlace::getId);
        Map<String, Integer> sumMap = emPlaceStudentService.getCountMapByPlaceIds(examId, groupId, emPlaceIds);
        int num = 0;
        for (EmPlace pp : emPlaceList) {
            if (sumMap != null && sumMap.containsKey(pp.getId())) {
                pp.setStuNum(sumMap.get(pp.getId()));
                num += sumMap.get(pp.getId());
            } else {
                pp.setStuNum(0);
            }
        }
        return num;
    }

    public int findGkArrangeStuNum(String examId, String unitId, List<EmPlace> emPlaceList, String groupId) {
        List<EmPlaceStudent> list = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, groupId);
        Map<String, List<EmPlaceStudent>> map = new HashMap<String, List<EmPlaceStudent>>();
        if (CollectionUtils.isNotEmpty(list)) {
            if (CollectionUtils.isNotEmpty(emPlaceList)) {
                for (EmPlaceStudent ps : list) {
                    if (!map.containsKey(ps.getExamPlaceId())) {
                        map.put(ps.getExamPlaceId(), new ArrayList<EmPlaceStudent>());
                    }
                    map.get(ps.getExamPlaceId()).add(ps);
                }
                for (EmPlace pp : emPlaceList) {
                    if (map.containsKey(pp.getId())) {
                        pp.setStuNum(map.get(pp.getId()).size());
                    } else {
                        pp.setStuNum(0);
                    }
                }
            }
            return list.size();
        }
        return 0;
    }

    public List<EmSubjectInfo> takeCanStat(List<EmSubjectInfo> subjectInfoList) {
        List<EmSubjectInfo> returnList = new ArrayList<EmSubjectInfo>();
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            for (EmSubjectInfo e : subjectInfoList) {
                if ("S".equals(e.getInputType())) {
                    returnList.add(e);
                }
            }
        }
        return returnList;
    }

    /**
     * 判断科目是否是语数英等非7选3科目
     *
     * @param subjectInfoList
     * @param courseList
     */
    public void setYsyFlag(List<EmSubjectInfo> subjectInfoList, List<Course> courseList73) {
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            Map<String, Course> course73Map = courseList73.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
            for (EmSubjectInfo subjectInfo : subjectInfoList) {
                Course course = course73Map.get(subjectInfo.getSubjectId());
                if (course == null) {
                    subjectInfo.setYsy(true);
                }
            }
        }
    }

    public List<Clazz> showFindClass(String schoolId, EmExamInfo examInfo) {
        int section = NumberUtils.toInt(examInfo.getGradeCodes().substring(0, 1));
        int afterGradeCode = NumberUtils.toInt(examInfo.getGradeCodes().substring(1, 2));
        int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(examInfo.getAcadyear(), "-"));
        String openAcadyear = (beforeSelectAcadyear - afterGradeCode + 1) + "-" + (beforeSelectAcadyear - afterGradeCode + 2);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchidSectionAcadyear(schoolId, openAcadyear, new Integer[]{section}), new TR<List<Grade>>() {
        });
        //如果出现多个高一这种情况---这个问题在考务编排上--保存gradeId?
        List<Clazz> classList = new ArrayList<Clazz>();
        if (CollectionUtils.isNotEmpty(gradeList)) {
            //Set<String> ids = EntityUtils.getSet(gradeList, "id");
            Grade grade = gradeList.get(0);
            classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(grade.getSchoolId(), grade.getId()), new TR<List<Clazz>>() {
            });
        }
        return classList;
    }
}
