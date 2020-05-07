package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmLimitSaveDto;
import net.zdsoft.exammanage.data.dto.EmScoreInfoDto;
import net.zdsoft.exammanage.data.dto.EmScoreSaveDto;
import net.zdsoft.exammanage.data.dto.EmSearchScoreDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
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
@RequestMapping("/exammanage")
public class EmScoreAction extends EmExamCommonAction {
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmLimitService emLimitService;
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private EmLimitDetailService emLimitDetailService;


    @RequestMapping("/scoreProcessing/index/page")
    @ControllerInfo(value = "成绩处理")
    public String showIndex(ModelMap map) {
        return "/exammanage/scoreProcessing/scoreProcessIndex.ftl";
    }

    @RequestMapping("/scoreProcessing/head/page")
    @ControllerInfo(value = "成绩处理设置")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreProcessing/scoreProcessHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/scoreProcessing/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreProcessing/scoreProcessList.ftl";
        return showExamInfoIn(map, httpSession, url, true);
    }

    @RequestMapping("/scoreProcessing/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreProcessing/scoreProcessList.ftl";
        return showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
    }

    @RequestMapping("/scoreProcessing/scoreItemIndex/page")
    @ControllerInfo("tab")
    public String showArrangeTab(String examId, String type, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreProcessing/scoreProcessTabIndex.ftl";
        return showTabIndex(examId, type, map, httpSession, url);
    }

    @RequestMapping("/scorePermission/index/page")
    @ControllerInfo("录分权限")
    public String scorePermissionIndex(String examId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamId(examId);
        map.put("emSubjectList", emSubjectList);
        map.put("examId", examId);
        //教师
        List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        map.put("teacherList", teacherList);
        return "/exammanage/scoreProcessing/scorePermissionIndex.ftl";
    }

    @RequestMapping("/scorePermission/list/page")
    @ControllerInfo("录分权限")
    public String showPermissionList(String examId, String subjectId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        List<EmClassInfo> emClassList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);

        //行政班
        List<Clazz> clazzList = new ArrayList<Clazz>();
        if (CollectionUtils.isNotEmpty(emClassList)) {
            Set<String> classIds = EntityUtils.getSet(emClassList, "classId");
            clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
        }
        map.put("clazzList", clazzList);
        //教师
        List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        map.put("teacherList", teacherList);

        List<EmLimit> limitList = emLimitService.findLimitList(examId, unitId, subjectId);

        map.put("limitList", limitList);
        map.put("examId", examId);
        map.put("subjectId", subjectId);

        return "/exammanage/scoreProcessing/scorePermissionList.ftl";
    }

    @ResponseBody
    @RequestMapping("/scorePermission/autoArrange")
    @ControllerInfo(value = "自动安排")
    public String autoArrange(String examId, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            List<EmClassInfo> emClassInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
            List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamId(examId);
            //key1:teacher key2:subjectId  value:classIds
            Map<String, Map<String, List<String>>> classIdsByTs = new HashMap<String, Map<String, List<String>>>();
            if (CollectionUtils.isNotEmpty(emClassInfoList) && CollectionUtils.isNotEmpty(emSubjectList)) {
                Set<String> classIds = EntityUtils.getSet(emClassInfoList, "classId");
                Set<String> subjectIds = EntityUtils.getSet(emSubjectList, "subjectId");
                //行政班课程
                List<ClassTeaching> classTeachList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(examInfo.getAcadyear(), examInfo.getSemester(), classIds.toArray(new String[]{})), new TR<List<ClassTeaching>>() {
                });
                if (CollectionUtils.isNotEmpty(classTeachList)) {
                    for (ClassTeaching c : classTeachList) {
                        if (!subjectIds.contains(c.getSubjectId())) {
                            continue;
                        }
                        if (StringUtils.isNotBlank(c.getTeacherId()) && !BaseConstants.ZERO_GUID.equals(c.getTeacherId())) {
                            if (!classIdsByTs.containsKey(c.getTeacherId())) {
                                classIdsByTs.put(c.getTeacherId(), new HashMap<String, List<String>>());
                            }
                            if (!classIdsByTs.get(c.getTeacherId()).containsKey(c.getSubjectId())) {
                                classIdsByTs.get(c.getTeacherId()).put(c.getSubjectId(), new ArrayList<String>());
                            }
                            classIdsByTs.get(c.getTeacherId()).get(c.getSubjectId()).add(c.getClassId());

                        }
                    }
                }

                //班主任
                List<Clazz> clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                if (CollectionUtils.isNotEmpty(clazzList)) {
                    for (Clazz z : clazzList) {
                        if (StringUtils.isNotBlank(z.getTeacherId())) {
                            for (String s : subjectIds) {
                                if (!classIdsByTs.containsKey(z.getTeacherId())) {
                                    classIdsByTs.put(z.getTeacherId(), new HashMap<String, List<String>>());
                                }
                                if (!classIdsByTs.get(z.getTeacherId()).containsKey(s)) {
                                    classIdsByTs.get(z.getTeacherId()).put(s, new ArrayList<String>());
                                }
                                classIdsByTs.get(z.getTeacherId()).get(s).add(z.getId());
                            }
                        }
                    }
                }
                if (classIdsByTs.size() > 0 && CollectionUtils.isNotEmpty(clazzList)) {
                    Set<String> teacherIds = classIdsByTs.keySet();
                    //查询已有的
                    List<EmLimit> list = emLimitService.findByExamIdTeacher(examId, teacherIds);
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (EmLimit e : list) {
                            if (!classIdsByTs.get(e.getTeacherId()).containsKey(e.getSubjectId())) {
                                classIdsByTs.get(e.getTeacherId()).put(e.getSubjectId(), new ArrayList<String>());
                            }
                            List<String> eelist = classIdsByTs.get(e.getTeacherId()).get(e.getSubjectId());
                            if (e.getClassIds() != null && e.getClassIds().length > 0) {
                                for (String s : e.getClassIds()) {
                                    if (!eelist.contains(s)) {
                                        eelist.add(s);
                                    }
                                }
                            }

                        }
                    }
                    List<EmLimit> saveList = new ArrayList<EmLimit>();
                    EmLimit emLimit = null;
                    for (String tt : classIdsByTs.keySet()) {
                        Map<String, List<String>> map1 = classIdsByTs.get(tt);
                        if (map1 == null || map1.size() <= 0) {
                            continue;
                        }
                        for (String su : map1.keySet()) {
                            emLimit = new EmLimit();
                            emLimit.setId(UuidUtils.generateUuid());
                            emLimit.setExamId(examId);
                            emLimit.setUnitId(unitId);
                            emLimit.setTeacherId(tt);
                            emLimit.setSubjectId(su);
                            if (CollectionUtils.isNotEmpty(map1.get(su))) {
                                emLimit.setClassIds(map1.get(su).toArray(new String[]{}));
                            }
                            saveList.add(emLimit);
                        }
                    }
                    if (CollectionUtils.isEmpty(saveList)) {
                        return error("没有数据可保存！");
                    }
                    emLimitService.saveByTeacher(saveList, teacherIds, examId);
                } else {
                    //没有数据
                    return error("没有数据可保存！");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }


    @ResponseBody
    @RequestMapping("/scorePermission/delete")
    @ControllerInfo(value = "删除录入")
    public String deleteByLimitId(String id, HttpSession httpSession) {
        try {
            EmLimit limit = emLimitService.findOne(id);
            if (limit == null) {
                return error("这条记录不存在！");
            }
            emLimitService.deleteByIds(new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/scorePermission/save")
    @ControllerInfo(value = "保存")
    public String saveLimits(EmLimitSaveDto emLimitSaveDto, HttpSession httpSession) {
        try {
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            if (emLimitSaveDto == null) {
                return error("没有需要保存的数据！");
            }
            List<EmLimit> emLimitList = emLimitSaveDto.getEmLimitList();
            if (CollectionUtils.isEmpty(emLimitList)) {
                return error("没有需要保存的数据！");
            }
            EmExamInfo examInfo = emExamInfoService.findOne(emLimitSaveDto.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            List<EmSubjectInfo> sublist = emSubjectInfoService.findByExamId(emLimitSaveDto.getExamId());
            if (CollectionUtils.isEmpty(sublist)) {
                return error("该考试下还没有科目设置");
            }
            Set<String> subjectIds = EntityUtils.getSet(sublist, "subjectId");
            if (!subjectIds.contains(emLimitSaveDto.getSubjectId())) {
                return error("该考试下没有设置该科目");
            }
            List<EmLimit> saveList = new ArrayList<EmLimit>();
            for (EmLimit limit : emLimitList) {
                if (limit != null && StringUtils.isNotBlank(limit.getTeacherId())) {
                    limit.setSubjectId(emLimitSaveDto.getSubjectId());
                    limit.setUnitId(unitId);
                    limit.setExamId(emLimitSaveDto.getExamId());
                    saveList.add(limit);
                }
            }
            if (CollectionUtils.isNotEmpty(saveList)) {
                emLimitService.saveLimitAll(saveList, true);
            } else {
                return error("没有需要保存的数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return returnSuccess();
    }


    @RequestMapping("/scorePermission/findLimit/page")
    @ControllerInfo("成绩录入")
    public String findLimit(String examId, String teacherId, ModelMap map, HttpSession httpSession) {
        Set<String> teacherIds = new HashSet<String>();
        teacherIds.add(teacherId);

        Teacher teacher = SUtils.dt(teacherRemoteService.findOneById(teacherId), new TypeReference<Teacher>() {
        });
        map.put("teacher", teacher);
        List<EmLimit> emLimitList = emLimitService.findByExamIdTeacher(examId, teacherIds);
        makeSubjectClassName(emLimitList);
        map.put("emLimitList", emLimitList);

        return "/exammanage/scoreProcessing/teacherPermission.ftl";

    }

    private void makeSubjectClassName(List<EmLimit> emLimitList) {
        if (CollectionUtils.isNotEmpty(emLimitList)) {
            Set<String> subjectIds = new HashSet<String>();
            Set<String> classIds = new HashSet<String>();
            for (EmLimit e : emLimitList) {
                subjectIds.add(e.getSubjectId());
                String[] arr = e.getClassIds();
                if (arr != null && arr.length > 0) {
                    for (String s : arr) {
                        classIds.add(s);
                    }
                }
            }
            Map<String, Course> courseMap = new HashMap<String, Course>();
            if (subjectIds.size() > 0) {
                List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
                });
                courseMap = EntityUtils.getMap(courseList, "id");
            }
            Map<String, Clazz> clazzMap = new HashMap<String, Clazz>();
            if (classIds.size() > 0) {
                List<Clazz> clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                clazzMap = EntityUtils.getMap(clazzList, "id");
            }
            for (EmLimit e : emLimitList) {
                if (courseMap.containsKey(e.getSubjectId())) {
                    e.setSubjectName(courseMap.get(e.getSubjectId()).getSubjectName());
                }
                String[] arr = e.getClassIds();
                if (arr != null && arr.length > 0) {
                    String ss = "";
                    for (String s : arr) {
                        if (clazzMap.containsKey(s)) {
                            ss = ss + "," + clazzMap.get(s).getClassNameDynamic();
                        }
                    }
                    if (StringUtils.isNotBlank(ss)) {
                        ss = ss.substring(1);
                    }
                    e.setClassNames(ss);
                }
            }
        }
    }

    @RequestMapping("/scoreInput/index/page")
    @ControllerInfo("成绩录入")
    public String scoreInputIndex(String examId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examId", examId);
        List<EmClassInfo> emClassList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
        //班级
        List<Clazz> clazzList = new ArrayList<Clazz>();
        if (CollectionUtils.isNotEmpty(emClassList)) {
            Set<String> classIds = EntityUtils.getSet(emClassList, "classId");
            clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
        }
        map.put("clazzList", clazzList);
        //考场
        List<EmPlace> examPlanList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        map.put("examPlanList", examPlanList);

        //科目
        List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamId(examId);
        map.put("emSubjectList", emSubjectList);

        return "/exammanage/scoreProcessing/scoreInputIndex.ftl";
    }

    @RequestMapping("/scoreInput/list/page")
    @ControllerInfo("成绩录入")
    public String scoreInputList(EmSearchScoreDto dto, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        String ownerId = info.getOwnerId();
        EmExamInfo examInfo = emExamInfoService.findOne(dto.getExamId());
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        EmSubjectInfo emSubject = emSubjectInfoService.findOne(dto.getEmSubjectId());
        if (emSubject == null || !dto.getSubjectId().equals(emSubject.getSubjectId())) {
            return promptFlt(map, "考试科目不存在或者科目有调整，请刷新后再次操作");
        }
        map.put("emSubject", emSubject);
        map.put("isLock", emSubject.getIsLock());
        List<EmScoreInfoDto> dtoList = new ArrayList<EmScoreInfoDto>();

        if (StringUtils.isNotBlank(dto.getSubjectId())) {
            List<Student> studentList = new ArrayList<Student>();
            //key:studentId value:考场
            Map<String, String> stuPlaceCode = new HashMap<String, String>();
            if ("1".equals(dto.getSearchInputType())) {
                List<EmLimitDetail> emLimitDetailList = emLimitDetailService.findBySubjectTeacher(dto.getExamId(), ownerId, dto.getSubjectId());
                if (CollectionUtils.isNotEmpty(emLimitDetailList)) {
                    Set<String> set = EntityUtils.getSet(emLimitDetailList, "classId");
                    if (set.contains(dto.getSearchClassId())) {
                        map.put("isCanScoreInfo", true);
                    } else {
                        map.put("isCanScoreInfo", false);
                    }
                } else {
                    map.put("isCanScoreInfo", false);
                }
                //按班级
                if (StringUtils.isNotBlank(dto.getSearchClassId())) {
                    studentList = SUtils.dt(studentRemoteService.findByClassIds(dto.getSearchClassId()), new TR<List<Student>>() {
                    });
                    if (CollectionUtils.isNotEmpty(studentList)) {
                        Set<String> stuIds = EntityUtils.getSet(studentList, "id");
                        List<EmPlaceStudent> pStuList = emPlaceStudentService.findByExamIdStuIds(dto.getExamId(), stuIds.toArray(new String[]{}));
                        if (CollectionUtils.isNotEmpty(pStuList)) {
                            Set<String> examPlaceIds = EntityUtils.getSet(pStuList, "examPlaceId");
                            List<EmPlace> emPlaceList = emPlaceService.findListByIds(examPlaceIds.toArray(new String[]{}));
                            Map<String, EmPlace> emPlaceMap = EntityUtils.getMap(emPlaceList, "id");
                            for (EmPlaceStudent s : pStuList) {
                                if (emPlaceMap.containsKey(s.getExamPlaceId())) {
                                    stuPlaceCode.put(s.getStudentId(), emPlaceMap.get(s.getExamPlaceId()).getExamPlaceCode());
                                }
                            }
                        }
                    }
                }
            } else {
                //没有权限之分
                map.put("isCanScoreInfo", true);
                //按考场
                if (StringUtils.isNotBlank(dto.getSearchExamPlanId())) {
                    EmPlace emPlace = emPlaceService.findByEmPlaceId(dto.getSearchExamPlanId());
                    List<EmPlaceStudent> pStuList = emPlaceStudentService.findByExamPlaceIdAndGroupId(null, dto.getSearchExamPlanId());
                    if (CollectionUtils.isNotEmpty(pStuList)) {
                        Set<String> stuIds = EntityUtils.getSet(pStuList, "studentId");
                        studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
                        });
                        if (emPlace != null) {
                            for (EmPlaceStudent s : pStuList) {
                                stuPlaceCode.put(s.getStudentId(), emPlace.getExamPlaceCode());
                            }
                        }
                    }
                }
            }
            //拿到成绩 key:student
            Map<String, EmScoreInfo> stuScoreMap = new HashMap<String, EmScoreInfo>();
            Map<String, String> classMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(studentList)) {
                Set<String> studentIds = EntityUtils.getSet(studentList, "id");
                Set<String> classIds = EntityUtils.getSet(studentList, "classId");
                stuScoreMap = emScoreInfoService.findByStudent(dto.getExamId(), dto.getSubjectId(), studentIds.toArray(new String[]{}));
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }

            //不排考学生
            Map<String, String> filteationMap = emFiltrationService.findByExamIdAndSchoolIdAndType(dto.getExamId(), unitId, ExammanageConstants.FILTER_TYPE1);
            //考号
            Map<String, String> stuNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, dto.getExamId());
            EmScoreInfoDto scoreDto = null;

            for (Student s : studentList) {
                if (filteationMap.containsKey(s.getId())) {
                    continue;
                }
                scoreDto = new EmScoreInfoDto();
                if (classMap.containsKey(s.getClassId())) {
                    scoreDto.setClassName(classMap.get(s.getClassId()));
                }
                if (stuNumMap.containsKey(s.getId())) {
                    scoreDto.setExamNum(stuNumMap.get(s.getId()));
                }
                if (stuPlaceCode.containsKey(s.getId())) {
                    scoreDto.setPlaceCode(stuPlaceCode.get(s.getId()));
                }
                if (stuScoreMap.containsKey(s.getId())) {
                    scoreDto.setScoreInfo(stuScoreMap.get(s.getId()));
                }
                scoreDto.setStuCode(s.getStudentCode());
                scoreDto.setStuName(s.getStudentName());
                scoreDto.setClassId(s.getClassId());
                scoreDto.setStudentId(s.getId());
                dtoList.add(scoreDto);
            }
        }
        map.put("dtoList", dtoList);

        map.put("examId", dto.getExamId());
        map.put("subjectId", dto.getSubjectId());
        return "/exammanage/scoreProcessing/scoreInputList.ftl";
    }

    private String checkEmSubject(EmSubjectInfo oldEmSubject, EmSubjectInfo nowEmSubject) {
        if (nowEmSubject == null) {
            return "科目设置有调整";
        }
        if (!oldEmSubject.getSubjectId().equals(nowEmSubject.getSubjectId())) {
            return "科目设置有调整";
        }
        if (!oldEmSubject.getInputType().equals(nowEmSubject.getInputType())) {
            return "科目设置中录入方式有调整，目前是" + (nowEmSubject.getInputType().equals("G") ? "等第" : "分数");
        }
        if (nowEmSubject.getInputType().equals("G")) {
            if (!nowEmSubject.getGradeType().equals(oldEmSubject.getGradeType())) {
                return "科目设置中录入方式为等第，等第显示分等改变";
            }
        }
        return null;
    }


    @ResponseBody
    @RequestMapping("/scoreProcessing/save")
    @ControllerInfo(value = "保存")
    public String saveScores(EmScoreSaveDto emScoreSaveDto, HttpSession httpSession) {
        try {
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            String ownerId = info.getOwnerId();
            if (emScoreSaveDto == null) {
                return error("没有需要保存的数据！");
            }
            EmSubjectInfo oldEmSubject = emScoreSaveDto.getEmSubjectInfo();
            EmExamInfo examInfo = emExamInfoService.findOne(oldEmSubject.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            EmSubjectInfo nowEmSubject = emSubjectInfoService.findOne(oldEmSubject.getId());
            //比较考试类型
            String errorMess = checkEmSubject(oldEmSubject, nowEmSubject);
            if (StringUtils.isNotBlank(errorMess)) {
                return error(errorMess);
            }
            List<EmScoreInfo> scoreInfoList = emScoreSaveDto.getScoreInfoList();
            if (CollectionUtils.isEmpty(scoreInfoList)) {
                return error("没有需要保存的数据！");
            }
            //保存成绩  先删除学生成绩

            List<EmScoreInfo> saveList = new ArrayList<EmScoreInfo>();
            //原来成绩
            Set<String> stuId = new HashSet<String>();
            float fullScore = 0;
            String fullScoreError = "";
            if (nowEmSubject.getInputType().equals("S")) {
                //分数
                fullScore = nowEmSubject.getFullScore() == null ? 0.0f : nowEmSubject.getFullScore();
                float oldfullScore = oldEmSubject.getFullScore() == null ? 0.0f : oldEmSubject.getFullScore();
                if (fullScore != oldfullScore) {
                    fullScoreError = "科目设置满分值有变动，目前满分值：" + fullScore;
                } else {
                    fullScoreError = "科目设置满分值：" + fullScore;
                }
            }
            for (EmScoreInfo dto : scoreInfoList) {
                if (dto == null) {
                    continue;
                }
                if (StringUtils.isBlank(dto.getId())) {
                    dto.setId(UuidUtils.generateUuid());
                }
                dto.setAcadyear(examInfo.getAcadyear());
                dto.setCreationTime(new Date());
                dto.setModifyTime(new Date());
                dto.setExamId(examInfo.getId());
                dto.setInputType(oldEmSubject.getInputType());
                dto.setOperatorId(ownerId);
                dto.setSemester(examInfo.getSemester());
                dto.setSubjectId(oldEmSubject.getSubjectId());
                dto.setUnitId(unitId);

                if (nowEmSubject.getInputType().equals("S")) {
                    //分数
                    float score = Float.parseFloat(dto.getScore());
                    if (score > fullScore) {
                        return error(fullScoreError + ",存在成绩大于满分值，请验证后提交。");
                    }
                }


                saveList.add(dto);
                stuId.add(dto.getStudentId());
            }
            if (CollectionUtils.isNotEmpty(saveList)) {
                emScoreInfoService.saveAll(saveList, stuId, oldEmSubject.getSubjectId(), examInfo.getId());
            } else {
                return error("没有需要保存的数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return returnSuccess();
    }

}
