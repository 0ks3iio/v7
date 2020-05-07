package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.ScoreLimitSaveDto;
import net.zdsoft.exammanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.Map.Entry;

/**
 * 权限
 */
@Controller
@RequestMapping("/exammanage")
public class EmExamLimitAction extends BaseAction {

    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private TeachClassRemoteService teachClassService;
    @Autowired
    private EmLimitService emLimitService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private EmNotLimitService emNotLimitService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;


    @RequestMapping("/scoreLimit/index/page")
    @ControllerInfo(value = "录入权限设置index")
    public String showList(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<String> teacherIdsList = emNotLimitService.findTeacherIdByUnitId(unitId);
        //teacherIds teacherNames
        if (CollectionUtils.isNotEmpty(teacherIdsList)) {
            List<Teacher> tList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdsList.toArray(new String[]{})), new TR<List<Teacher>>() {
            });
            if (CollectionUtils.isNotEmpty(tList)) {
                String teacherIds = "";
                String teacherNames = "";
                for (Teacher t : tList) {
                    teacherIds = teacherIds + "," + t.getId();
                    teacherNames = teacherNames + "," + t.getTeacherName();
                }
                map.put("tea", tList);
                if (StringUtils.isNotBlank(teacherIds)) {
                    teacherIds = teacherIds.substring(1);
                    teacherNames = teacherNames.substring(1);
                }
                map.put("teacherIds", teacherIds);
                map.put("teacherNames", teacherNames);
            }
        }
        return "/exammanage/scoreLimit/scoreLimitIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/scoreLimit/saveNotLimit")
    @ControllerInfo(value = "保存总权限录分人员")
    public String saveNotLimit(String teacherIds, String delId, HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();

        if (StringUtils.isNotBlank(teacherIds) || StringUtils.isNotBlank(delId)) {
            List<String> newIds = new ArrayList<>();
            String[] teacherArr = null;
            if (StringUtils.isNotBlank(teacherIds)) {
                for (String id : teacherIds.split(",")) {
                    if (!StringUtils.equals(id, delId)) {
                        newIds.add(id);
                    }
                }
            } else {
                List<String> teacherIdsList = emNotLimitService.findTeacherIdByUnitId(unitId);
                for (String id : teacherIdsList) {
                    if (!StringUtils.equals(id, delId)) {
                        newIds.add(id);
                    }
                }

            }
            if (CollectionUtils.isNotEmpty(newIds)) {
                teacherArr = newIds.toArray(new String[0]);
            }
            try {
                emNotLimitService.saveTeacherIds(teacherArr, unitId);
            } catch (Exception e) {
                e.printStackTrace();
                returnError();
            }
        }
        return success("更新录分总管理员成功！");
    }

    @RequestMapping("/scoreLimit/tabHead/page")
    @ControllerInfo(value = "录入权限设置index")
    public String showTabHead(ModelMap map, String type) {
        //学年学期
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        LoginInfo loginInfo = getLoginInfo();
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("unitId", loginInfo.getUnitId());

        if ("1".equals(type)) {
            //必修课
            Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
            });
            School school = SUtils.dc(schoolRemoteService.findOneById(loginInfo.getUnitId()), School.class);
            List<Grade> gradeList = getSchGradeList(findMapMapByMcodeIds, school);
            map.put("gradeList", gradeList);
            map.put("type", "1");
        } else if ("2".equals(type)) {
            //选修课
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(loginInfo.getUnitId()), new TR<List<Grade>>() {
            });
            map.put("gradeList", gradeList);
            map.put("examId", BaseConstants.ZERO_GUID);
            map.put("type", "2");
        } else {
            return errorFtl(map, "参数丢失，请重新加载！");
        }
        return "/exammanage/scoreLimit/scoreLimitHead.ftl";
    }

    @RequestMapping("/scoreLimit/limitList/page")
    @ControllerInfo(value = "录入权限列表")
    public String showLimitList1(ModelMap map, ScoreLimitSearchDto dto) {
        dto.setUnitId(getLoginInfo().getUnitId());
        //返回数据
        List<ScoreLimitSaveDto> dtoList = new ArrayList<>();
        //行政班
        List<Clazz> xzbList = new ArrayList<>();
        //教学班
        List<TeachClass> jxbList = new ArrayList<>();
        if ("1".equals(dto.getType())) {
            //必修课
            //根据考试科目id 拿到考试下所有考试科目信息
            //根据学年学期考试获得班级id
            String gradeCode = dto.getGradeCode();
            String acadyear = dto.getAcadyear();
            String semester = dto.getSemester();
            int section = NumberUtils.toInt(gradeCode.substring(0, 1));
            int afterGradeCode = NumberUtils.toInt(gradeCode.substring(1, 2));
            int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
            String openAcadyear = (beforeSelectAcadyear - afterGradeCode + 1) + "-" + (beforeSelectAcadyear - afterGradeCode + 2);
            //班级开设课程 不走班的
            xzbList = SUtils.dt(classService.findClassList(dto.getUnitId(), dto.getSubjectId(), openAcadyear, acadyear, semester, section, 0), new TR<List<Clazz>>() {
            });
            List<Grade> gradeList = makeGradeIdList(gradeCode, acadyear, dto.getUnitId());
            Set<String> gradeIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(gradeList)) {
                gradeIds = EntityUtils.getSet(gradeList, Grade::getId);
            }
            //TeachClass 年级id不可能为空
            if (CollectionUtils.isNotEmpty(gradeIds)) {
                jxbList = SUtils.dt(teachClassService.findTeachClassList(dto.getUnitId(), acadyear, semester, dto.getSubjectId(), gradeIds.toArray(new String[]{}), false), new TR<List<TeachClass>>() {
                });
            }
        } else if ("2".equals(dto.getType())) {
            //选修课
            //取得教学班id
            jxbList = SUtils.dt(teachClassService.findBySearch(dto.getUnitId(), dto.getAcadyear(), dto.getSemester(), TeachClass.CLASS_TYPE_ELECTIVE, dto.getGradeId(), dto.getSubjectId()), new TR<List<TeachClass>>() {
            });
            Iterator<TeachClass> iterator = jxbList.iterator();
            while (iterator.hasNext()) {
                TeachClass teachClass = iterator.next();
                if (Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge()) || Constant.IS_FALSE_Str.equals(teachClass.getIsUsing())) {
                    iterator.remove();
                }
            }
        } else {
            return errorFtl(map, "参数丢失，请重新加载！");
        }
        Map<String, Set<String>> teacherIdsByClassId = new HashMap<>();
        //根据学年学期单位科目
        List<EmLimit> limitList = emLimitService.findBySearchDto(dto);
        Set<String> teacherIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(limitList)) {
            for (EmLimit sl : limitList) {
                String[] classIds = sl.getClassIds();
                for (String clsId : classIds) {
                    if (!teacherIdsByClassId.containsKey(clsId)) {
                        teacherIdsByClassId.put(clsId, new HashSet<String>());
                    }
                    teacherIdsByClassId.get(clsId).add(sl.getTeacherId());
                }
                teacherIds.add(sl.getTeacherId());
            }
        }
        Map<String, Teacher> teacherMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(teacherIds)) {
            List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{})), new TR<List<Teacher>>() {
            });
            if (CollectionUtils.isNotEmpty(teacherList)) {
                teacherMap = EntityUtils.getMap(teacherList, Teacher::getId);
            }
        }
        //组装数据
        ScoreLimitSaveDto scoreLimitSaveDto = null;
        Set<String> set = null;
        if (CollectionUtils.isNotEmpty(xzbList)) {
            for (Clazz zz : xzbList) {
                scoreLimitSaveDto = new ScoreLimitSaveDto();
                scoreLimitSaveDto.setClassId(zz.getId());
                scoreLimitSaveDto.setClassName(zz.getClassNameDynamic());
                scoreLimitSaveDto.setClassType(ExammanageConstants.CLASS_TYPE1);
                set = teacherIdsByClassId.get(zz.getId());
                Map<String, String> tMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(set)) {
                    for (String s : set) {
                        if (teacherMap.containsKey(s)) {
                            tMap.put(teacherMap.get(s).getId(), teacherMap.get(s).getTeacherName());
                        }
                    }
                }
                scoreLimitSaveDto.setTeacherMap(tMap);
                dtoList.add(scoreLimitSaveDto);
            }
        }
        if (CollectionUtils.isNotEmpty(jxbList)) {
            for (TeachClass zz : jxbList) {
                scoreLimitSaveDto = new ScoreLimitSaveDto();
                scoreLimitSaveDto.setClassId(zz.getId());
                scoreLimitSaveDto.setClassName(zz.getName());
                scoreLimitSaveDto.setClassType(ExammanageConstants.CLASS_TYPE2);
                set = teacherIdsByClassId.get(zz.getId());
                Map<String, String> tMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(set)) {
                    for (String s : set) {
                        if (teacherMap.containsKey(s)) {
                            tMap.put(teacherMap.get(s).getId(), teacherMap.get(s).getTeacherName());
                        }
                    }
                }
                scoreLimitSaveDto.setTeacherMap(tMap);
                dtoList.add(scoreLimitSaveDto);
            }
        }
        map.put("dtoList", dtoList);
        map.put("searchSubjectId", dto.getSubjectId());
        return "/exammanage/scoreLimit/scoreLimitList.ftl";
    }

    @ResponseBody
    @RequestMapping("/scoreLimit/saveBySubject")
    @ControllerInfo(value = "保存录入权限")
    public String doSaveCourseInfoList(ScoreLimitSearchDto dto, String teaId) {
        if (StringUtils.isBlank(dto.getAcadyear()) || StringUtils.isBlank(dto.getSubjectId()) ||
                StringUtils.isBlank(dto.getSemester()) || StringUtils.isBlank(dto.getExamId())) {
            return error("参数丢失，请刷新后操作");
        }
        if (ArrayUtils.isEmpty(dto.getClassIds())) {
            return error("没有选中班级进行赋权");
        }
        if (ArrayUtils.isEmpty(dto.getTeacherIds())) {
            return error("没有选中老师进行赋权");
        }
        if (ArrayUtils.isEmpty(dto.getClassTypes())) {
            return error("参数丢失，请刷新后操作");
        }
        if (dto.getClassIds().length != dto.getClassTypes().length) {
            return error("参数丢失，请刷新后操作");
        }

        dto.setUnitId(getLoginInfo().getUnitId());
        //根据班级 科目 考试 学年  已有

        if (StringUtils.isNotBlank(teaId)) {
            //保存单个班级
            dto.setTeacherId(teaId);
            dto.setClassId(dto.getClassIds()[0]);
            emLimitService.deleteByClassIdAndTeacherId(dto);
        }

        List<EmLimit> limitList = emLimitService.findBySearchDto(dto);
        Map<String, Set<String>> teacherIdsByClassId = new HashMap<>();
        Map<String, String> teacherIds = new HashMap<>();
        if (CollectionUtils.isNotEmpty(limitList)) {
            for (EmLimit sl : limitList) {
                for (String clsId : sl.getClassIds()) {
                    if (!teacherIdsByClassId.containsKey(clsId)) {
                        teacherIdsByClassId.put(clsId, new HashSet<String>());
                    }
                    teacherIdsByClassId.get(clsId).add(sl.getTeacherId());
                }
                teacherIds.put(sl.getTeacherId(), sl.getId());
            }
        }
        List<EmLimit> insertList = new ArrayList<>();
        List<EmLimitDetail> detailList = new ArrayList<>();
        EmLimit scoreLimit = null;
        EmLimitDetail limitDetail = null;
        Set<String> tset = null;
        String[] classArr = dto.getClassIds();
        String[] classTypeArr = dto.getClassTypes();
        for (String teacherId : dto.getTeacherIds()) {
            String id = "";
            if (!teacherIds.containsKey(teacherId)) {
                scoreLimit = tomakeNewLimit(dto);
                scoreLimit.setTeacherId(teacherId);
                id = scoreLimit.getId();
                insertList.add(scoreLimit);
            } else {
                id = teacherIds.get(teacherId);
            }

            for (int i = 0; i < classArr.length; i++) {
                String classId = classArr[i];
                String classType = classTypeArr[i];
                tset = teacherIdsByClassId.get(classId);
                if (tset == null) {
                    tset = new HashSet<>();
                }
                if (!tset.contains(teacherId)) {
                    limitDetail = new EmLimitDetail();
                    limitDetail.setClassId(classId);
                    limitDetail.setClassType(classType);
                    limitDetail.setLimitId(id);
                    limitDetail.setId(UuidUtils.generateUuid());
                    detailList.add(limitDetail);
                }
            }
        }
        if (CollectionUtils.isEmpty(insertList) && CollectionUtils.isEmpty(detailList)) {
            return success("没有需要新增的权限数据！");
        }
        try {
            emLimitService.saveAllEntitys(insertList.toArray(new EmLimit[]{}), detailList.toArray(new EmLimitDetail[]{}));
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }

        return success("保存成功！");
    }

    private EmLimit tomakeNewLimit(ScoreLimitSearchDto dto) {
        EmLimit scoreLimit = new EmLimit();
        scoreLimit.setUnitId(dto.getUnitId());
        scoreLimit.setSubjectId(dto.getSubjectId());
        scoreLimit.setId(UuidUtils.generateUuid());
        scoreLimit.setExamId(dto.getExamId());
        return scoreLimit;
    }

    @ResponseBody
    @RequestMapping("/scoreLimit/deleteOne")
    @ControllerInfo(value = "保存录入权限")
    public String deleteOne(ScoreLimitSearchDto dto) {
        if (StringUtils.isBlank(dto.getAcadyear()) || StringUtils.isBlank(dto.getSubjectId()) ||
                StringUtils.isBlank(dto.getSemester()) || StringUtils.isBlank(dto.getExamId())) {
            return error("参数丢失，请刷新后操作");
        }
        if (StringUtils.isBlank(dto.getClassId())) {
            return error("班级参数丢失，请刷新后操作");
        }
        if (StringUtils.isBlank(dto.getTeacherId())) {
            return error("教师参数丢失，请刷新后操作");
        }
        dto.setUnitId(getLoginInfo().getUnitId());

        try {
            emLimitService.deleteByClassIdAndTeacherId(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return success("删除成功！");
    }


    @ResponseBody
    @RequestMapping("/scoreLimit/limitInit")
    @ControllerInfo(value = "初始化")
    public String limitInit(ScoreLimitSearchDto dto) {
        if (StringUtils.isBlank(dto.getAcadyear()) ||
                StringUtils.isBlank(dto.getSemester()) || StringUtils.isBlank(dto.getExamId())) {
            return error("参数丢失，请刷新后操作");
        }
        if (StringUtils.isBlank(dto.getType())) {
            return error("参数丢失，请刷新后操作");
        }

        dto.setUnitId(getLoginInfo().getUnitId());

        List<EmLimit> limitList = emLimitService.findBySearchDto(dto);
        Set<String> deleteIds = EntityUtils.getSet(limitList, EmLimit::getId);

        //key:subjectId key:classId value:teacherIds
        Map<String, Map<String, Set<String>>> teacherIdsBysubjectClass = new HashMap<>();
        List<TeachClass> jxbList = new ArrayList<>();

        Set<String> xzbClassId = new HashSet<>();//行政班id
        //教学班 --一个科目一个班
        Set<String> jxbClassId = new HashSet<>();//教学班id
        if ("1".equals(dto.getType())) {
            //必修课
            String acadyear = dto.getAcadyear();
            String semester = dto.getSemester();
            EmExamInfo examInfo = emExamInfoService.findOne(dto.getExamId());
            List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(dto.getExamId());
            String gradeCode = examInfo.getGradeCodes();
            int section = NumberUtils.toInt(gradeCode.substring(0, 1));
            int afterGradeCode = NumberUtils.toInt(gradeCode.substring(1, 2));
            int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
            String openAcadyear = (beforeSelectAcadyear - afterGradeCode + 1) + "-" + (beforeSelectAcadyear - afterGradeCode + 2);
            List<Grade> gradeList = makeGradeIdList(gradeCode, acadyear, dto.getUnitId());
            Set<String> gradeIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(gradeList)) {
                gradeIds = EntityUtils.getSet(gradeList, Grade::getId);
            }
            //key:classId value:subjectIds  行政班 可以是多个科目
            Map<String, Set<String>> xzbsubjectIdByClassId = new HashMap<>();
            Set<String> xzbSujectId = new HashSet<>();
            Map<String, Clazz> xzbClsMap = new HashMap<>();
            Map<String, TeachClass> jxbClsMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(infoList)) {
                Set<String> subjectIds = EntityUtils.getSet(infoList, EmSubjectInfo::getSubjectId);
                for (String subId : subjectIds) {
                    List<Clazz> xzbList = SUtils.dt(classService.findClassList(dto.getUnitId(), subId,
                            openAcadyear, acadyear, semester, section, 0), new TR<List<Clazz>>() {
                    });
                    for (Clazz c : xzbList) {
                        xzbClassId.add(c.getId());
                        if (!xzbsubjectIdByClassId.containsKey(c.getId())) {
                            xzbsubjectIdByClassId.put(c.getId(), new HashSet<String>());
                        }
                        xzbsubjectIdByClassId.get(c.getId()).add(subId);
                        xzbSujectId.add(subId);
                        xzbClsMap.put(c.getId(), c);
                    }
                    //TeachClass 年级id不可能为空
                    if (CollectionUtils.isNotEmpty(gradeIds)) {
                        jxbList = SUtils.dt(teachClassService.findTeachClassList(dto.getUnitId(), acadyear, semester, subId, gradeIds.toArray(new String[]{}), false), new TR<List<TeachClass>>() {
                        });
                        for (TeachClass c : jxbList) {
                            jxbClassId.add(c.getId());
                            jxbClsMap.put(c.getId(), c);
                        }
                    }
                }
            }
            //获取该学年学期行政班教师上课情况TeacherIdsBysubjectClass
            /**
             * xzbClassId  xzbSujectId行政班参与的科目
             * List<ClassTeaching>
             */
            if (CollectionUtils.isNotEmpty(xzbClassId)) {
                List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(getLoginInfo().getUnitId(), examInfo.getAcadyear(), examInfo.getSemester(), xzbClassId.toArray(new String[]{}), xzbSujectId.toArray(new String[]{}), true), new TR<List<ClassTeaching>>() {
                });
                if (CollectionUtils.isNotEmpty(classTeachingList)) {
                    for (ClassTeaching c : classTeachingList) {
                        if (CollectionUtils.isEmpty(xzbsubjectIdByClassId.get(c.getClassId()))) {
                            continue;
                        }
                        if (!xzbsubjectIdByClassId.get(c.getClassId()).contains(c.getSubjectId())) {
                            continue;
                        }
                        Set<String> teacherSet = new HashSet<>();
                        if (CollectionUtils.isNotEmpty(c.getTeacherIds())) {
                            teacherSet.addAll(c.getTeacherIds());
                        }
                        if (StringUtils.isNotBlank(c.getTeacherId())) {
                            teacherSet.add(c.getTeacherId());
                        }
                        if (CollectionUtils.isEmpty(teacherSet)) {
                            continue;
                        }
                        if (!teacherIdsBysubjectClass.containsKey(c.getSubjectId())) {
                            teacherIdsBysubjectClass.put(c.getSubjectId(), new HashMap<String, Set<String>>());
                        }

                        if (!teacherIdsBysubjectClass.get(c.getSubjectId()).containsKey(c.getClassId())) {
                            teacherIdsBysubjectClass.get(c.getSubjectId()).put(c.getClassId(), new HashSet<String>());
                        }
                        //取得教师
                        teacherIdsBysubjectClass.get(c.getSubjectId()).get(c.getClassId()).addAll(teacherSet);
                    }
                }

            }
            //获取教学班
            if (CollectionUtils.isNotEmpty(jxbClassId)) {
                jxbList = SUtils.dt(teachClassService.findListByIds(jxbClassId.toArray(new String[]{})), new TR<List<TeachClass>>() {
                });
            }
        } else if ("2".equals(dto.getType())) {
            //选修课
            //取得教学班id
            jxbList = SUtils.dt(teachClassService.findBySearch(dto.getUnitId(), dto.getAcadyear(), dto.getSemester(), TeachClass.CLASS_TYPE_ELECTIVE, null, null), new TR<List<TeachClass>>() {
            });
        } else {
            return error("参数丢失，请刷新后操作");
        }

        if (CollectionUtils.isNotEmpty(jxbList)) {
            for (TeachClass t : jxbList) {
                if (!teacherIdsBysubjectClass.containsKey(t.getCourseId())) {
                    teacherIdsBysubjectClass.put(t.getCourseId(), new HashMap<String, Set<String>>());
                }

                if (!teacherIdsBysubjectClass.get(t.getCourseId()).containsKey(t.getId())) {
                    teacherIdsBysubjectClass.get(t.getCourseId()).put(t.getId(), new HashSet<String>());
                }
                //取得教师
                Set<String> tSet = new HashSet<>();
                if (StringUtils.isNotBlank(t.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(t.getTeacherId()))) {
                    tSet.add(t.getTeacherId());
                }
                if (StringUtils.isNotBlank(t.getAssistantTeachers())) {
                    if (t.getAssistantTeachers().indexOf(",") >= 0) {
                        String[] arr = t.getAssistantTeachers().split(",");
                        tSet.addAll(Arrays.asList(arr));
                    } else {
                        tSet.add(t.getAssistantTeachers());
                    }
                }
                if (CollectionUtils.isNotEmpty(tSet)) {
                    teacherIdsBysubjectClass.get(t.getCourseId()).get(t.getId()).addAll(tSet);
                }
            }
        }
        Map<String, String> entityIdMap = new HashMap<>();
        EmLimit em = null;
        EmLimitDetail emDetail = null;
        List<EmLimit> emlist = new ArrayList<>();
        List<EmLimitDetail> emDetaills = new ArrayList<>();
        if (teacherIdsBysubjectClass.size() > 0) {
            for (Entry<String, Map<String, Set<String>>> item : teacherIdsBysubjectClass.entrySet()) {
                String subId = item.getKey();
                Map<String, Set<String>> value = item.getValue();
                if (value != null && value.size() > 0) {
                    for (Entry<String, Set<String>> item1 : value.entrySet()) {
                        String classId = item1.getKey();
                        Set<String> teaIds = item1.getValue();
                        if (CollectionUtils.isNotEmpty(teaIds)) {
                            for (String teaId : teaIds) {
                                emDetail = new EmLimitDetail();
                                if (xzbClassId.contains(classId)) {
                                    emDetail.setClassType(ExammanageConstants.CLASS_TYPE1);
                                } else {
                                    emDetail.setClassType(ExammanageConstants.CLASS_TYPE2);
                                }
                                emDetail.setClassId(classId);
                                emDetail.setId(UuidUtils.generateUuid());
                                if (entityIdMap.containsKey(teaId + "," + subId)) {
                                    emDetail.setLimitId(entityIdMap.get(teaId + "," + subId));
                                } else {
                                    em = new EmLimit();
                                    em.setExamId(dto.getExamId());
                                    em.setSubjectId(subId);
                                    em.setUnitId(dto.getUnitId());
                                    em.setId(UuidUtils.generateUuid());
                                    em.setTeacherId(teaId);
                                    emlist.add(em);
                                    emDetail.setLimitId(em.getId());
                                    entityIdMap.put(teaId + "," + subId, em.getId());
                                }
                                emDetaills.add(emDetail);
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(emDetaills) && CollectionUtils.isEmpty(emlist)) {
            return success("没有需要新增的权限数据！");
        }
        try {
            emLimitService.initAllEntitys(deleteIds, emlist, emDetaills);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return success("操作成功！");
    }

    @ResponseBody
    @RequestMapping("/common/edu/examList")
    public List<EmExamInfo> queryExamList(String acadyear, String semester, String unitId) {
        List<EmExamInfo> examList = new ArrayList<>();
        if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
            return examList;
        }
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        searchDto.setSearchType("1");
        examList = emExamInfoService.findExamList(null, unitId, searchDto, false);
        return examList;
    }

    @ResponseBody
    @RequestMapping("/common/edu/schoolList")
    public List<School> querySchoolList(String examId) {
        List<School> schList = new ArrayList<>();
        if (StringUtils.isBlank(examId)) {
            return schList;
        }
        List<EmJoinexamschInfo> joinSchs = emJoinexamschInfoService.findByExamId(examId);
        Set<String> schIds = EntityUtils.getSet(joinSchs, EmJoinexamschInfo::getSchoolId);
        schList = SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
        });
        return schList;
    }


    @ResponseBody
    @RequestMapping("/common/examList")
    public List<EmExamInfo> examList(String acadyear, String semester, String searchType, String unitId, String noLimit, String fromType, String gradeCode) {
        List<EmExamInfo> examList = new ArrayList<>();
        if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
            return examList;
        }
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        searchDto.setSearchGradeCode(gradeCode);
        if (StringUtils.isNotBlank(searchType)) {
            searchDto.setSearchType(searchType);
        }
        examList = emExamInfoService.findExamList(null, unitId, searchDto, false);
        //普通录分老师考试列表控制
        if ("1".equals(fromType) && CollectionUtils.isNotEmpty(examList) && !"1".equals(noLimit)) {
            ScoreLimitSearchDto dto = new ScoreLimitSearchDto();
            dto.setAcadyear(acadyear);
            dto.setSemester(semester);
            dto.setUnitId(unitId);
            dto.setTeacherId(getLoginInfo().getOwnerId());

            List<EmLimit> limitList = emLimitService.findBySearchDto(dto);
            if (CollectionUtils.isNotEmpty(limitList)) {
                Set<String> examIdSet = EntityUtils.getSet(limitList, EmLimit::getExamId);
                examIdSet.remove(Constant.GUID_ZERO);
                Iterator<EmExamInfo> iterator = examList.iterator();
                while (iterator.hasNext()) {
                    if (!examIdSet.contains(iterator.next().getId())) {
                        iterator.remove();
                    }
                }
            } else {
                examList = new ArrayList<>();
            }
        }
        return examList;
    }

    @ResponseBody
    @RequestMapping("/common/examList1")
    public List<EmExamInfo> examList1(String acadyear, String semester, String searchType, String unitId, String noLimit, String fromType, String gradeCode) {
        List<EmExamInfo> examList = new ArrayList<>();
        if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
            return examList;
        }
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        searchDto.setSearchGradeCode(gradeCode);
        searchDto.setSearchGk("1");
        if (StringUtils.isNotBlank(searchType)) {
            searchDto.setSearchType(searchType);
        }
        examList = emExamInfoService.findExamList(null, unitId, searchDto, false);
        return examList;
    }

    /**
     * 某个学校某次考试下科目信息
     *
     * @param examId
     * @param schoolId
     * @return
     */
    @ResponseBody
    @RequestMapping("/common/subjectList")
    public List<Course> subjectList(String examId, String unitId, String noLimit, String fromType) {
        Set<String> subjectIds = null;
        if ("0".equals(noLimit)) {//普通
            subjectIds = EntityUtils.getSet(emLimitService.findByExamIdST(examId, getLoginInfo().getOwnerId(), null, null), EmLimit::getSubjectId);
        } else {
            List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(examId);
            subjectIds = EntityUtils.getSet(infoList, EmSubjectInfo::getSubjectId);
        }
        List<Course> courseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subjectIds)) {
            courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            Collections.sort(courseList, (o1, o2) -> {
                return o1.getOrderId() - o2.getOrderId();
            });
        }
        return courseList;
    }

    @ResponseBody
    @RequestMapping("/common/subjectList1")
    public List<Course> subjectList1(String examId, String unitId) {
        List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(examId);
        List<EmSubjectInfo> elist = new ArrayList<>();
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
        });
        Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
        for (EmSubjectInfo sub : infoList) {
            if (StringUtils.isNotBlank(sub.getGkSubType()) && !StringUtils.equals(sub.getGkSubType(), "2")
                    && id73Set.contains(sub.getSubjectId())
                    && StringUtils.equals(sub.getInputType(), "S")) {
                elist.add(sub);
            }
        }
        List<Course> courseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(elist)) {
            Set<String> subjectIds = EntityUtils.getSet(elist, EmSubjectInfo::getSubjectId);
            courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
        }
        return courseList;
    }

    private List<Grade> getSchGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds, School school) {
        List<Grade> gradeList = new ArrayList<>();
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
                    if (map != null && map.containsKey(grade + "")) {
                        dto.setGradeName(map.get(grade + "").getMcodeContent());
                    }
                    gradeList.add(dto);
                }
            }
        }
//		Collections.sort(gradeList, new Comparator<Grade>() {
//			public int compare(Grade o1, Grade o2) {
//				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
//			}
//		});
        return gradeList;
    }


    private List<Grade> makeGradeIdList(String gradeCode, String acadyear, String schoolId) {
        int section = NumberUtils.toInt(gradeCode.substring(0, 1));
        int afterGradeCode = NumberUtils.toInt(gradeCode.substring(1, 2));
        int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
        String openAcadyear = (beforeSelectAcadyear - afterGradeCode + 1) + "-" + (beforeSelectAcadyear - afterGradeCode + 2);//开设学年
        //理论上一个学校一个年级默认只有一个  这个gradeList只有一个
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchidSectionAcadyear(schoolId, openAcadyear, new Integer[]{section}), new TR<List<Grade>>() {
        });
        return gradeList;
    }

}
