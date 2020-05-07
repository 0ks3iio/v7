package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.SubjectClassInfoSaveDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage")
public class EmExamInfoAction extends EmExamCommonAction {
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private GradeTeachingRemoteService gradeTeachingRemoteService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    //	@Autowired
//	private NewGkChoiceRemoteService newGkChoiceRemoteService;
    @Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmPlaceService emPlaceService;

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
    public String showIndex(ModelMap map) {
        return "/exammanage/examInfo/examInfoIndex.ftl";
    }

    @RequestMapping("/examInfo/head/page")
    @ControllerInfo(value = "考试信息设置")
    public String showExamHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examInfo/examInfoHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examInfo/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examInfo/examInfoList.ftl";
        showExamInfoIn(map, httpSession, url, false);
        List<EmExamInfo> examInfoList = (List<EmExamInfo>) map.get("examInfoList");
        makeIsEdit(examInfoList);
        map.put("examInfoList", examInfoList);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        map.put("unitClass", unit.getUnitClass());
        return url;
    }

    @RequestMapping("/examInfo/list2/page")
    @ControllerInfo("30天前考试列表")
//	@Scheduled(fixedDelay = 2400000, initialDelay = 10000)
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examInfo/examInfoList.ftl";
        showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, false);
        List<EmExamInfo> examInfoList = (List<EmExamInfo>) map.get("examInfoList");
        makeIsEdit(examInfoList);
        map.put("examInfoList", examInfoList);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        map.put("unitClass", unit.getUnitClass());
        return url;
    }

    private void makeIsEdit(List<EmExamInfo> examInfoList) {
        if (CollectionUtils.isEmpty(examInfoList)) {
            return;
        }
        Set<String> ids = EntityUtils.getSet(examInfoList, "id");
        List<String> examIds = emScoreInfoService.findExamIds(ids.toArray(new String[]{}));
        for (EmExamInfo em : examInfoList) {
            if (examIds.contains(em.getId())) {
                em.setIsEdit("1");
            } else {
                em.setIsEdit("0");
            }
        }

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
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        map.put("goGrade", "0");
        if (StringUtils.isBlank(id)) {
            String acadyear = semester.getAcadyear();
            List<Grade> list = SUtils.dt(gradeRemoteService.findBySchoolIdAndOpenAcadyear(unitId, acadyear), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isEmpty(list)) {
                map.put("goGrade", "1");
            }
        }
        EmExamInfo examInfo = null;
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);

        if (StringUtils.isNotBlank(id)) {
            examInfo = emExamInfoService.findExamInfoOne(id);
            map.put("haveExamSub", CollectionUtils.isNotEmpty(emSubjectInfoService.findByExamId(id)));
        } else {
            examInfo = new EmExamInfo();
            examInfo.setAcadyear(semester.getAcadyear());
            examInfo.setSemester(semester.getSemester() + "");
            examInfo.setUnitId(unitId);
            Map<String, String> hMap = new HashMap<String, String>();
            hMap.put(unitId, unitId);
            examInfo.setLkxzSelectMap(hMap);
        }
        map.put("examInfo", examInfo);
        List<Unit> findAll = null;
        //年级Code列表
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        List<Grade> gradeList = new ArrayList<Grade>();
        if (unit.getUnitClass() != Unit.UNIT_CLASS_SCHOOL) {
            //教育局
            map.put("tklxMap", ExammanageConstants.eduTklx);
            findAll = SUtils.dt(unitRemoteService.findDirectUnits(unit.getId(), Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {
            });
            List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
            });
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            map.put("gradeList", gradeList);
        } else {
            //学校
            map.put("tklxMap", ExammanageConstants.schTklx);
            findAll = SUtils.dt(unitRemoteService.findDirectUnits(unit.getParentId(), Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {
            });
            //学校
            List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
            });
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            //School school = SUtils.dc(schoolRemoteService.findById(unitId), School.class);
            //gradeList = getSchGradeList(findMapMapByMcodeIds,school);
            map.put("gradeList", gradeList);
        }

        map.put("unitClass", unit.getUnitClass());
        map.put("unitList", findAll);

        List<McodeDetail> findByMcodeId = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-KSLB"), new TR<List<McodeDetail>>() {
        });

        map.put("kslbList", findByMcodeId);

        return "/exammanage/examInfo/examInfoAdd.ftl";
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
            String isGk = examInfo.getIsgkExamType();
            if (isGk.equals("1")) {
                String schoolId = getLoginInfo().getUnitId();
                String gradeCodes = examInfo.getGradeCodes();
                List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(schoolId, gradeCodes), new TR<List<Grade>>() {
                });
                Grade grade = null;
                if (CollectionUtils.isNotEmpty(gradeList)) {
                    grade = gradeList.get(0);
                }
                if (grade == null)
                    return returnError("-1", "找不到年级！");
                if (grade.getSection() < 3) {
                    return returnError("-1", "只有高中才可设置高考模式！");
                } else {
                    Map<String, Set<String>> stuSubIdsMap = SUtils.dt(studentSelectSubjectRemoteService.findStuSelectMapByGradeId(examInfo.getAcadyear(), examInfo.getSemester(), grade.getId()), new TR<Map<String, Set<String>>>() {
                    });
                    if (stuSubIdsMap == null || stuSubIdsMap.values().size() == 0) {
                        return returnError("-5", "");
                    }
                }
            }
            //处理校校联考
            List<EmJoinexamschInfo> joinexamschInfoAddList = new ArrayList<EmJoinexamschInfo>();
            if (ExammanageConstants.TKLX_3.equals(examInfo.getExamUeType())) {
                EmJoinexamschInfo joinexamschInfo = null;
                for (String item : examInfo.getLkxzSelect()) {
                    joinexamschInfo = new EmJoinexamschInfo();
                    joinexamschInfo.setExamId(examInfo.getId());
                    joinexamschInfo.setSchoolId(item);
                    joinexamschInfoAddList.add(joinexamschInfo);
                }
            }
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
    public String doDeleteExamInfo(String id, HttpSession httpSession) {
        try {
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            Set<String> placeIds = new HashSet<>();
            List<EmPlace> placeList = emPlaceService.findByExamIdAndSchoolIdWithMaster(id, unitId, false);
            if (CollectionUtils.isNotEmpty(placeList)) {
                placeIds = EntityUtils.getSet(placeList, "id");
            }
//			List<EmSubGroup> grouplist = emSubGroupService.findListByExamId(id);
//			if(CollectionUtils.isNotEmpty(grouplist)) {
//				Set<String> groupIds = EntityUtils.getSet(grouplist, "id");
//				Map<String, Set<String>> placeGroupMap = emPlaceGroupService.findGroupMap(unitId, groupIds.toArray(new String[0]));
//
//				if(placeGroupMap.size()>0) {
//					for (EmSubGroup g : grouplist) {
//						placeIds = placeGroupMap.get(g.getId());
//					}
//				}
//			}
            if (CollectionUtils.isNotEmpty(placeIds)) {
                String[] placeids = new String[placeIds.size()];
                int i = 0;
                for (String placeid : placeIds) {
                    placeids[i] = placeid;
                    i++;
                }
                List<EmPlaceStudent> emPlaceStudentList = emPlaceStudentService.findByExamPlaceIds(placeids);
                if (CollectionUtils.isNotEmpty(emPlaceStudentList)) {
                    return error("已编排过学生的考试无法删除 ！");
                }
            }
            //if()
            emExamInfoService.deleteAllIsDeleted(id);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return returnSuccess();
    }

    @RequestMapping("/subjectClassIndex/index/page")
    @ControllerInfo("考试科目班级设置")
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
        } else {
            //学校
            isEdu = false;
            if (examInfo.getUnitId().equals(unitId)) {
                //可以修改
                isEditSubject = true;
            } else {
                isEditSubject = false;
            }
			/*//已选择班级
			List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
			map.put("classInfoList", classInfoList);*/
        }
        map.put("isEditSubject", isEditSubject);
        map.put("isEdu", isEdu);
        //以保存的科目信息
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);

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

        if ("1".equals(examInfo.getIsgkExamType())) {//是否新高考模式，1:新高考
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, examInfo.getGradeCodes()), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                List<GradeTeaching> gradeTeachingList = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, examInfo.getAcadyear(),
                        examInfo.getSemester(), gradeList.get(0).getId(), 1), new TR<List<GradeTeaching>>() {
                });
                if (CollectionUtils.isNotEmpty(gradeTeachingList)) {
                    Set<String> subjectIds = gradeTeachingList.stream().map(GradeTeaching::getSubjectId).collect(Collectors.toSet());
                    List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
                    });
                    setYsyFlag(subjectInfoList, courseList73);
                    List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {
                    }).stream().collect(Collectors.toList());
                    Collections.sort(courseList, new Comparator<Course>() {
                        @Override
                        public int compare(Course o1, Course o2) {
                            if (o1.getOrderId() != o2.getOrderId()) {
                                return o1.getOrderId() - o2.getOrderId();
                            } else {
                                return o1.getSubjectCode().compareTo(o2.getSubjectCode());
                            }
                        }
                    });
					/*map.put("ysy1", BaseConstants.SUBJECT_TYPES_YSY[0]);
					map.put("ysy2", BaseConstants.SUBJECT_TYPES_YSY[1]);
					map.put("ysy3", BaseConstants.SUBJECT_TYPES_YSY[2]);*/
                    map.put("courseList73", courseList73);
                    map.put("courseList", courseList);
                }
            }
            map.put("subjectInfoList", subjectInfoList);
            if ("1".equals(isView)) {
                map.put("isEditSubject", false);
                return "/exammanage/subjectClassInfo/subjectGroupShow.ftl";
            }
            return "/exammanage/subjectClassInfo/subjectGroupIndex.ftl";
        }
        if ("1".equals(isView)) {
            //科目列表
            map.put("courseList", showFindSubject(unit, examInfo));
            map.put("isEditSubject", false);
            map.put("subjectInfoList", subjectInfoList);
            return "/exammanage/subjectClassInfo/subjectClassShow.ftl";
        }
        //科目列表
        map.put("courseList", showFindSubject(unit, examInfo));
        map.put("subjectInfoList", subjectInfoList);
        return "/exammanage/subjectClassInfo/subjectClassIndex.ftl";
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
            String[] classIds = showFindClass(unitId, examInfo).stream().map(Clazz::getId).collect(Collectors.toSet()).toArray(new String[0]);
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
                        if (StringUtils.isNotEmpty(item.getStrGkStartDate())) {
                            item.setGkStartDate(StrToDate(item.getStrGkStartDate(), "yyyy-MM-dd HH:mm"));
                        }
                        if (StringUtils.isNotEmpty(item.getStrGkEndDate())) {
                            item.setGkEndDate(StrToDate(item.getStrGkEndDate(), "yyyy-MM-dd HH:mm"));
                        }
                        saveSubjectList.add(item);
                    }
                }
            }
            emSubjectInfoService.saveSubjectClass(examInfo, unitId, saveSubjectList, classIds);
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

    @RequestMapping("/subjectInfo/sameExamList/page")
    @ControllerInfo("考试科目班级设置复用")
    public String showBeforeIndex(String examId, String type, ModelMap map, HttpSession httpSession) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        if ("1".equals(type)) {
            if (!examInfo.getUnitId().equals(unitId)) {
                return errorFtl(map, "无权限操作！");
            }
        }

        map.put("examId", examId);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);

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
        map.put("type", type);
        return "/exammanage/subjectClassInfo/subjectClassCopy.ftl";
    }

    @ResponseBody
    @RequestMapping("/examInfo/findExam")
    @ControllerInfo("查询考试")
    public String findExam(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        searchDto.setSearchAcadyear(searchAcadyear);
        searchDto.setSearchSemester(searchSemester);
        searchDto.setSearchType(searchType);
        searchDto.setSearchGradeCode(searchGradeCode);
        examInfoList = emExamInfoService.findExamList(null, unitId, searchDto, false);
        JSONArray jsonArr = new JSONArray();
        JSONObject job = null;
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            for (EmExamInfo item : examInfoList) {
                job = new JSONObject();
                job.put("id", item.getId());
                job.put("name", item.getExamName());
                jsonArr.add(job);
            }
        }
        return jsonArr.toJSONString();

    }

    @ResponseBody
    @RequestMapping("/examInfo/saveCopySubjectClass")
    @ControllerInfo("复制考试")
    public String saveCopySubjectClass(String examId, String copyExamId, String type, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null) {
                return error("考试已不存在！");
            }
            EmExamInfo copyExamInfo = emExamInfoService.findOne(copyExamId);
            if (copyExamInfo == null) {
                return error("复用的考试已不存在！");
            }

            LoginInfo loginInfo = getLoginInfo(httpSession);
            String unitId = loginInfo.getUnitId();
            if ("1".equals(type)) {
                //科目复制
                String section1 = examInfo.getGradeCodes().substring(0, 1);
                String section2 = copyExamInfo.getGradeCodes().substring(0, 1);
                if (!section1.equals(section2)) {
                    return error("考试学段不一致，不能复用！");
                }

                List<EmSubjectInfo> copyList = emSubjectInfoService.findByExamId(copyExamId);
                if (CollectionUtils.isEmpty(copyList)) {
                    return error("没有数据可以复用！");
                }
                List<EmSubjectInfo> list = emSubjectInfoService.findByExamId(examId);
                Set<String> oldSubjectIds = new HashSet<String>();
                if (CollectionUtils.isNotEmpty(list)) {
                    oldSubjectIds = EntityUtils.getSet(list, "subjectId");
                }
                List<EmSubjectInfo> insertList = new ArrayList<EmSubjectInfo>();
                EmSubjectInfo e = null;
                for (EmSubjectInfo em : copyList) {
                    if (oldSubjectIds.contains(em.getSubjectId())) {
                        continue;
                    }
                    e = new EmSubjectInfo();
                    e.setId(UuidUtils.generateUuid());
                    e.setUnitId(unitId);
                    e.setExamId(examId);
                    e.setSubjectId(em.getSubjectId());
                    e.setFullScore(em.getFullScore());
                    e.setGradeType(em.getGradeType());
                    e.setInputType(em.getInputType());
                    e.setIsLock("0");
                    insertList.add(e);
                }
                if (CollectionUtils.isNotEmpty(insertList)) {
                    emSubjectInfoService.saveAllEntitys(insertList.toArray(new EmSubjectInfo[]{}));
                }

            } else if ("2".equals(type)) {

                int section1 = NumberUtils.toInt(examInfo.getGradeCodes().substring(0, 1));
                int afterGradeCode1 = NumberUtils.toInt(examInfo.getGradeCodes().substring(1, 2));
                int beforeSelectAcadyear1 = NumberUtils.toInt(StringUtils.substringBefore(examInfo.getAcadyear(), "-"));
                String openAcadyear1 = (beforeSelectAcadyear1 - afterGradeCode1 + 1) + "-" + (beforeSelectAcadyear1 - afterGradeCode1 + 2);

                int section2 = NumberUtils.toInt(copyExamInfo.getGradeCodes().substring(0, 1));
                int afterGradeCode2 = NumberUtils.toInt(copyExamInfo.getGradeCodes().substring(1, 2));
                int beforeSelectAcadyear2 = NumberUtils.toInt(StringUtils.substringBefore(copyExamInfo.getAcadyear(), "-"));
                String openAcadyear2 = (beforeSelectAcadyear2 - afterGradeCode2 + 1) + "-" + (beforeSelectAcadyear2 - afterGradeCode2 + 2);
                if (section1 != section2) {
                    return error("跨学段不能复用！");
                }
                if (!openAcadyear1.equals(openAcadyear2)) {
                    return error("跨年级不能复用！");
                }
                List<Clazz> clazzList = showFindClass(unitId, examInfo);//班级范围
                if (CollectionUtils.isEmpty(clazzList)) {
                    return error("该学校无需参加此次考试！");
                }
                Set<String> classids = EntityUtils.getSet(clazzList, "id");
                //已选择班级
                List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
                Set<String> oldClassIds = new HashSet<String>();
                if (CollectionUtils.isNotEmpty(classInfoList)) {
                    oldClassIds = EntityUtils.getSet(classInfoList, "classId");
                }
                //班级复制
                List<EmClassInfo> copyList = emClassInfoService.findByExamIdAndSchoolId(copyExamId, unitId);
                if (CollectionUtils.isEmpty(copyList)) {
                    return error("没有数据可以复用！");
                }
                List<EmClassInfo> insertList = new ArrayList<EmClassInfo>();
                EmClassInfo e = null;
                boolean ff = true;
                for (EmClassInfo em : copyList) {
                    if (!classids.contains(em.getClassId())) {
                        continue;
                    }
                    if (oldClassIds.contains(em.getClassId())) {
                        if (ff) {
                            ff = false;
                        }
                        continue;
                    }
                    e = new EmClassInfo();
                    e.setId(UuidUtils.generateUuid());
                    e.setSchoolId(unitId);
                    e.setExamId(examId);
                    e.setClassId(em.getClassId());
                    e.setClassType(em.getClassType());
                    insertList.add(e);
                }
                if (CollectionUtils.isNotEmpty(insertList)) {
                    emClassInfoService.saveAllEntitys(insertList.toArray(new EmClassInfo[]{}));
                } else {
                    if (ff) {
                        return error("没有数据可复用！");
                    } else {
                        return error("已有复用的班级存在，没有多余的数据可以复用！");
                    }

                }
            }


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
        courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unit.getId(), "1", examInfo.getGradeCodes().substring(0, 1)), new TR<List<Course>>() {
        });
        return courseList;
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
     * 判断科目是否是语数英等非7选3科目
     *
     * @param subjectInfoList
     * @param courseList
     */
    public void setYsyFlag(List<EmSubjectInfo> subjectInfoList, List<Course> courseList73) {
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            //Map<String,String> courseCodeMap=EntityUtils.getMap(courseList, "id", "subjectCode");
            Map<String, Course> course73Map = courseList73.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
            for (EmSubjectInfo subjectInfo : subjectInfoList) {
                Course course = course73Map.get(subjectInfo.getSubjectId());
                if (course == null) {
                    subjectInfo.setYsy(true);
                }
				/*if(BaseConstants.SUBJECT_TYPES_YSY[0].equals(subjectCode)||BaseConstants.SUBJECT_TYPES_YSY[1].equals(subjectCode)
							||BaseConstants.SUBJECT_TYPES_YSY[2].equals(subjectCode)){
					subjectInfo.setYsy(true);
				}*/
            }
        }
    }
}
