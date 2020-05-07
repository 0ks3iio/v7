package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.InspectorsTeacherDto;
import net.zdsoft.exammanage.data.dto.InvigilateTeacherDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage")
public class EmExamTeacherAction extends EmExamCommonAction {
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmPlaceGroupService emPlaceGroupService;

    @RequestMapping("/examTeacher/index/page")
    @ControllerInfo(value = "监巡考")
    public String showIndex(ModelMap map) {
        return "/exammanage/examTeacher/examTeacherIndex.ftl";
    }

    @RequestMapping("/examTeacher/head/page")
    @ControllerInfo(value = "监巡考设置")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examTeacher/examTeacherHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examTeacher/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examTeacher/examTeacherList.ftl";
        return showExamInfoIn(map, httpSession, url, true);
    }

    @RequestMapping("/examTeacher/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examTeacher/examTeacherList.ftl";
        return showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
    }

    @RequestMapping("/examTeacher/examItemIndex/page")
    @ControllerInfo("tab")
    public String showteacherTab(String examId, String type, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examTeacher/teacherTabIndex.ftl";
        return showTabIndex(examId, type, map, httpSession, url);
    }

    @RequestMapping("/examTeacher/inspectorsTeacher/page")
    @ControllerInfo("巡考老师设置List")
    public String showInspectorsTeacher(String examId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
        } else {
            map.put("isgk", "0");
        }
        List<InspectorsTeacherDto> inspectorsList = new ArrayList<InspectorsTeacherDto>();
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            Map<String, String> courseMap = new LinkedHashMap<String, String>();
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
                InspectorsTeacherDto dto = new InspectorsTeacherDto();
                dto.setExamId(examId);
                dto.setSubjectInfoId(emSubjectInfo.getId());
                dto.setSubjectId(emSubjectInfo.getSubjectId());
                dto.setStartTime(emSubjectInfo.getStartDate());
                dto.setEndTime(emSubjectInfo.getEndDate());
                dto.setGkStartTime(emSubjectInfo.getGkStartDate());
                dto.setGkEndTime(emSubjectInfo.getGkEndDate());
                dto.setSubType(emSubjectInfo.getGkSubType());
                inspectorsList.add(dto);
            }
            List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE2);
            if (CollectionUtils.isNotEmpty(teacherList)) {
                List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
                Map<String, String> teacherOutsMap = new LinkedHashMap<String, String>();
                if (CollectionUtils.isNotEmpty(teacherOutLists)) {
                    for (EmOutTeacher emOutTeacher : teacherOutLists) {
                        teacherOutsMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
                    }
                }
                List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
                });
                Map<String, String> teacherInsMap = new LinkedHashMap<String, String>();
                if (CollectionUtils.isNotEmpty(teacherInLists)) {
                    for (Teacher teacher : teacherInLists) {
                        teacherInsMap.put(teacher.getId(), teacher.getTeacherName());
                    }
                }

                Map<String, String> teacherIdAndName = new LinkedHashMap<String, String>();
                Map<String, String> teacherIdsMap = new LinkedHashMap<String, String>();
                for (EmPlaceTeacher emPlaceTeacher : teacherList) {
                    String teachersInName = "";
                    String teachersOutName = "";
                    String teacherIds = "";
                    String teacherNames = "";
                    if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                        String[] teachersIn = emPlaceTeacher.getTeacherIdsIn().split(",");
                        for (int i = 0; i < teachersIn.length; i++) {
                            teachersInName = teachersInName + teacherInsMap.get(teachersIn[i]) + ",";
                        }
                        teachersInName = teachersInName.substring(0, teachersInName.length() - 1);
                    }
                    if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                        String[] teachersOut = emPlaceTeacher.getTeacherIdsOut().split(",");
                        for (int i = 0; i < teachersOut.length; i++) {
                            teachersOutName = teachersOutName + teacherOutsMap.get(teachersOut[i]) + ",";
                        }
                        teachersOutName = teachersOutName.substring(0, teachersOutName.length() - 1);
                    }
                    if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn()) && StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                        teacherIds = emPlaceTeacher.getTeacherIdsIn() + "," + emPlaceTeacher.getTeacherIdsOut();
                        teacherNames = teachersInName + "," + teachersOutName;
                    } else if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn()) && StringUtils.isBlank(emPlaceTeacher.getTeacherIdsOut())) {
                        teacherIds = emPlaceTeacher.getTeacherIdsIn();
                        teacherNames = teachersInName;
                    } else if (StringUtils.isBlank(emPlaceTeacher.getTeacherIdsIn()) && StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                        teacherIds = emPlaceTeacher.getTeacherIdsOut();
                        teacherNames = teachersOutName;
                    }
                    if (StringUtils.isNotBlank(teacherIds)) {
                        teacherIdAndName.put(emPlaceTeacher.getSubjectId(), teacherIds + "&" + teacherNames);
                        teacherIdsMap.put(emPlaceTeacher.getSubjectId(), emPlaceTeacher.getId());
                    }
                }

                for (InspectorsTeacherDto dto : inspectorsList) {
                    String idAndName = teacherIdAndName.get(dto.getSubjectId());
                    String teacherIds = teacherIdsMap.get(dto.getSubjectId());
                    if (StringUtils.isNotBlank(idAndName)) {
                        String[] idName = idAndName.split("&");
                        dto.setTeacherIds(idName[0]);
                        dto.setTeacherNames(idName[1]);
                    }
                    if (StringUtils.isNotBlank(teacherIds)) {
                        dto.setEmPlaceTeacherId(teacherIds);
                    }
                }
            }
            for (InspectorsTeacherDto dto : inspectorsList) {
                dto.setSubjectName(courseMap.get(dto.getSubjectId()));
            }
        }
        map.put("inspectorsList", inspectorsList);
        map.put("examId", examId);
        return "/exammanage/examTeacher/inspectorsTeacherList.ftl";
    }

    @RequestMapping("/examTeacher/inspectorsEdit")
    @ControllerInfo(value = "设置巡考老师")
    public String inspectorsEdit(String examId, String emPlaceTeacherId, String subjectInfoId, String teacherIds, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        String teachInIds = "";
        String teacherInNames = "";
        List<String> teacherOutIdsList = new ArrayList<String>();
        if (StringUtils.isNotBlank(teacherIds)) {
            List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
            });
            Map<String, String> teachInNameMap = new LinkedHashMap<String, String>();
            List<String> teacherIdList = new ArrayList<String>();
            for (Teacher teacher : teacherList) {
                teachInNameMap.put(teacher.getId(), teacher.getTeacherName());
                teacherIdList.add(teacher.getId());
            }
            List<String> teacherIdsList = Arrays.asList(teacherIds.split(","));
            String teacherOutIds = "";
            for (String str : teacherIdsList) {
                if (teacherIdList.contains(str)) {
                    teachInIds += str + ",";
                    teacherInNames += teachInNameMap.get(str) + ",";
                } else {
                    teacherOutIds += str + ",";
                }
            }
            if (StringUtils.isNotBlank(teacherOutIds)) {
                teacherOutIdsList = Arrays.asList(teacherOutIds.split(","));
            }
        }
        if (StringUtils.isNotBlank(teachInIds)) {
            teachInIds = teachInIds.substring(0, teachInIds.length() - 1);
            teacherInNames = teacherInNames.substring(0, teacherInNames.length() - 1);
        }
        map.put("examId", examId);
        map.put("teachInIds", teachInIds);
        map.put("teacherInNames", teacherInNames);
        map.put("teacherOutIdsList", teacherOutIdsList);
        map.put("subjectInfoId", subjectInfoId);
        map.put("teacherOutLists", teacherOutLists);
        map.put("emPlaceTeacherId", emPlaceTeacherId);
        return "/exammanage/examTeacher/inspectorsEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/examTeacher/inspectorsSave")
    @ControllerInfo(value = "巡考老师保存")
    public String inspectorsSave(String examId, String teacherInIds, String teacherOutIds, String emPlaceTeacherId, String subjectInfoId, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findListByIds(new String[]{subjectInfoId});
        EmSubjectInfo emSubjectInfo = findSByExamIdIn.get(0);
        String invigilateTeacherIds = "";
        List<EmPlaceTeacher> invigilateTeacherList = emPlaceTeacherService.findByExamIdAndSubjectIdAndType(examId, emSubjectInfo.getSubjectId(), ExammanageConstants.TEACHER_TYPE1);
        if (CollectionUtils.isNotEmpty(invigilateTeacherList)) {
            for (EmPlaceTeacher emPlaceTeacher : invigilateTeacherList) {
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                    invigilateTeacherIds += emPlaceTeacher.getTeacherIdsIn() + ",";
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                    invigilateTeacherIds += emPlaceTeacher.getTeacherIdsOut();
                }
            }
            invigilateTeacherIds = invigilateTeacherIds.substring(0, invigilateTeacherIds.length() - 1);
        }
        List<String> invigilateTeacherIdsList = new ArrayList<String>();
        if (StringUtils.isNotBlank(invigilateTeacherIds)) {
            invigilateTeacherIdsList = Arrays.asList(invigilateTeacherIds.split(","));
            if (StringUtils.isNotBlank(teacherInIds)) {
                String[] teacherInIdsList = teacherInIds.split(",");
                for (int i = 0; i < teacherInIdsList.length; i++) {
                    if (invigilateTeacherIdsList.contains(teacherInIdsList[i])) {
                        return returnError("操作失败！", "已设置在校老师为当前时间段的监考老师！");
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(teacherOutIds)) {
            String[] teacherOutIdsList = teacherOutIds.split(",");
            for (int i = 0; i < teacherOutIdsList.length; i++) {
                if (invigilateTeacherIdsList.contains(teacherOutIdsList[i])) {
                    return returnError("操作失败！", "已设置校外老师为当前时间段的监考老师！");
                }
            }
        }
        try {
            EmPlaceTeacher emPlaceTeacher = new EmPlaceTeacher();
            emPlaceTeacher.setExamId(examId);
            if (StringUtils.isNotBlank(teacherInIds)) {
                emPlaceTeacher.setTeacherIdsIn(teacherInIds);
            }
            if (StringUtils.isNotBlank(teacherOutIds)) {
                emPlaceTeacher.setTeacherIdsOut(teacherOutIds);
            }
            emPlaceTeacher.setUnitId(unitId);
            emPlaceTeacher.setSubjectId(emSubjectInfo.getSubjectId());
            emPlaceTeacher.setStartTime(emSubjectInfo.getStartDate());
            if (emSubjectInfo.getEndDate() == null) {
                emPlaceTeacher.setEndTime(emSubjectInfo.getGkEndDate());
            } else {
                emPlaceTeacher.setEndTime(emSubjectInfo.getEndDate());
            }
            emPlaceTeacher.setType(ExammanageConstants.TEACHER_TYPE2);
            emPlaceTeacherService.saveAndDel(emPlaceTeacher, emPlaceTeacherId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功！");
    }

    @RequestMapping("/examTeacher/invigilateTeacherIndex/page")
    @ControllerInfo("监考老师设置选择")
    public String invigilateTeacherIndex(String examId, ModelMap map, String status, String subjectId, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        List<Course> courseList = new ArrayList<Course>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            String[] subjectNames = new String[findSByExamIdIn.size()];
            int index = 0;
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                subjectNames[index] = emSubjectInfo.getSubjectId();
                index++;
            }
            if (subjectNames != null) {
                courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectNames), new TR<List<Course>>() {
                });
            }
			/*if(CollectionUtils.isNotEmpty(courseList)){
				subjectId=courseList.get(0).getId();
			}*/
        }
        String havedata = "true";
        //考场信息
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        if (CollectionUtils.isEmpty(findSByExamIdIn) || CollectionUtils.isEmpty(emPlaceList)) {
            havedata = "false";
        }
        map.put("examId", examId);
        map.put("havedata", havedata);
        map.put("courseList", courseList);
        map.put("status", status);
        map.put("subjectId", subjectId);
        return "/exammanage/examTeacher/invigilateTeacherIndex.ftl";
    }

    @RequestMapping("/examTeacher/invigilateTeacherList/page")
    @ControllerInfo("监考老师列表")
    public String invigilateTeacherList(String examId, String status, String subjectId, String teacherName, ModelMap map, HttpServletRequest request, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        //考场信息
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        //考试科目信息
        //List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        //学科名称Map
        Map<String, String> courseNamesMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseNamesMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
            }
        }
        //加载已经维护了的数据
        List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE1);
        Map<String, EmPlaceTeacher> teachersMap = new LinkedHashMap<>();
        Map<String, String> teacherIdMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(teacherList)) {
            Map<String, String> teacherInsMap = new LinkedHashMap<>();
            Map<String, String> teacherOutsMap = new LinkedHashMap<>();
            List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
            });
            List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
            if (CollectionUtils.isNotEmpty(teacherOutLists)) {
                for (EmOutTeacher emOutTeacher : teacherOutLists) {
                    teacherOutsMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
                }
            }
            if (CollectionUtils.isNotEmpty(teacherInLists)) {
                for (Teacher teacher : teacherInLists) {
                    teacherInsMap.put(teacher.getId(), teacher.getTeacherName());
                }
            }
            Iterator<EmPlaceTeacher> it = teacherList.iterator();
            while (it.hasNext()) {
                EmPlaceTeacher teacher = it.next();
                if (StringUtils.isNotBlank(teacher.getTeacherIdsIn())) {
                    String[] teacherIdIns = teacher.getTeacherIdsIn().split(",");
                    String teacherNameIns = "";
                    for (int i = 0; i < teacherIdIns.length; i++) {
                        teacherNameIns = teacherNameIns + teacherInsMap.get(teacherIdIns[i]) + ",";
                    }
                    teacherNameIns = teacherNameIns.substring(0, teacherNameIns.length() - 1);
                    teacher.setTeacherInNames(teacherNameIns);
                }
                if (StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                    String[] teacherIdOuts = teacher.getTeacherIdsOut().split(",");
                    String teacherNameOuts = "";
                    for (int i = 0; i < teacherIdOuts.length; i++) {
                        teacherNameOuts = teacherNameOuts + teacherOutsMap.get(teacherIdOuts[i]) + ",";
                    }
                    teacherNameOuts = teacherNameOuts.substring(0, teacherNameOuts.length() - 1);
                    teacher.setTeacherOutNames(teacherNameOuts);
                }
                if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) || StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                    teachersMap.put(teacher.getExamPlaceId() + teacher.getSubjectId(), teacher);
                    teacherIdMap.put(teacher.getExamPlaceId() + teacher.getSubjectId(), teacher.getId());
                }
            }
        }
        List<InvigilateTeacherDto> teacherDtosList = new ArrayList<InvigilateTeacherDto>();
        if (StringUtils.isNotBlank(teacherName)) {
            try {
                teacherName = URLDecoder.decode(teacherName, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            //新高考模式
            map.put("isgk", "1");
            Map<String, EmPlace> placeMap = emPlaceList.stream().collect(Collectors.toMap(EmPlace::getId, Function.identity()));
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            Map<String, Set<String>> subGroupMap = new HashMap<>();
            Map<String, EmSubGroup> groupMap = new HashMap<>();
            Set<String> emGroupIds = new HashSet<>();
            for (EmSubGroup g : groupList) {
                emGroupIds.add(g.getId());
                groupMap.put(g.getId(), g);
                String[] subIdArr = g.getSubjectId().split(",");
                for (String subId : subIdArr) {
                    if (!subGroupMap.containsKey(subId)) {
                        subGroupMap.put(subId, new HashSet<>());
                    }
                    subGroupMap.get(subId).add(g.getId());
                }
            }
            List<EmPlaceGroup> placeGlist = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
            Map<String, Set<String>> groupPlaceMap = new HashMap<>();
            for (EmPlaceGroup pg : placeGlist) {
                if (!groupPlaceMap.containsKey(pg.getGroupId())) {
                    groupPlaceMap.put(pg.getGroupId(), new HashSet<>());
                }
                groupPlaceMap.get(pg.getGroupId()).add(pg.getExamPlaceId());
            }
            //考号范围组装
            List<String> numberList = null;
            Map<String, String> numberMap = new LinkedHashMap<>();
            List<EmPlaceStudent> emStudentList = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupIdIn(examId, unitId, emGroupIds.toArray(new String[0]));
            Map<String, Map<String, List<EmPlaceStudent>>> groupOfMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(emStudentList)) {
                emStudentList.forEach(emStu -> {
                    Map<String, List<EmPlaceStudent>> placeOfStuMap = groupOfMap.get(emStu.getGroupId());
                    if (placeOfStuMap == null) {
                        placeOfStuMap = new HashMap<>();
                    }
                    if (!placeOfStuMap.containsKey(emStu.getExamPlaceId())) {
                        placeOfStuMap.put(emStu.getExamPlaceId(), new ArrayList<>());
                    }
                    placeOfStuMap.get(emStu.getExamPlaceId()).add(emStu);
                    groupOfMap.put(emStu.getGroupId(), placeOfStuMap);
                });
            }

            for (EmSubGroup g : groupList) {
                Map<String, List<EmPlaceStudent>> stuMap = groupOfMap.get(g.getId());
				/*for (EmPlaceStudent emPlaceStudent : studentList) {
					if(!stuMap.containsKey(emPlaceStudent.getExamPlaceId())) {
						stuMap.put(emPlaceStudent.getExamPlaceId(), new ArrayList<>());
					}
					stuMap.get(emPlaceStudent.getExamPlaceId()).add(emPlaceStudent);
				}*/
                if (stuMap != null) {
                    for (String key : stuMap.keySet()) {
                        List<EmPlaceStudent> stulist = stuMap.get(key);
                        if (CollectionUtils.isNotEmpty(stulist)) {
                            Collections.sort(stulist, new Comparator<EmPlaceStudent>() {
                                @Override
                                public int compare(EmPlaceStudent o1, EmPlaceStudent o2) {
                                    return o1.getExamNumber().compareTo(o2.getExamNumber());
                                }
                            });
                            /*for (EmPlaceStudent emPlaceStudent : stulist) {
                                numberList = new ArrayList<>();
                                numberList.add(emPlaceStudent.getExamNumber());
                            }
                            String number = numberList.get(0) + "-" + numberList.get(numberList.size() - 1);*/
                            String number = stulist.get(0).getExamNumber() + "-" + stulist.get(stulist.size() - 1).getExamNumber();
                            numberMap.put(key + g.getId(), number);
                        }
                    }
                }
            }
            //数据组装
            InvigilateTeacherDto dto = null;
            EmPlaceTeacher teacher = null;
            Set<String> groupIds = null;
            if (CollectionUtils.isNotEmpty(findSByExamIdIn) && CollectionUtils.isNotEmpty(emPlaceList)) {
                for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                    if (!"all".equals(subjectId) && !subjectId.equals(emSubjectInfo.getSubjectId())) {
                        continue;
                    }
                    groupIds = subGroupMap.get(emSubjectInfo.getSubjectId());
                    if (groupIds == null) {
                        continue;
                    }
                    for (String groupId : groupIds) {
                        EmSubGroup group = groupMap.get(groupId);
                        Set<String> placeIds = groupPlaceMap.get(groupId);
                        if (placeIds == null) {
                            continue;
                        }
                        for (String placeId : placeIds) {
                            EmPlace emPlace = placeMap.get(placeId);
                            teacher = teachersMap.get(emPlace.getId() + emSubjectInfo.getSubjectId());
                            if ("1".equals(status) && teacher == null) {
                                continue;
                            }
                            if ("0".equals(status) && teacher != null) {
                                continue;
                            }
                            dto = new InvigilateTeacherDto();
                            if (teacher != null) {
                                if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) && StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                                    dto.setTeacherIds(teacher.getTeacherIdsIn() + "," + teacher.getTeacherIdsOut());
                                    dto.setTeacherNames(teacher.getTeacherInNames() + "," + teacher.getTeacherOutNames());
                                } else if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) && StringUtils.isBlank(teacher.getTeacherIdsOut())) {
                                    dto.setTeacherIds(teacher.getTeacherIdsIn());
                                    dto.setTeacherNames(teacher.getTeacherInNames());
                                } else if (StringUtils.isBlank(teacher.getTeacherIdsIn()) && StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                                    dto.setTeacherIds(teacher.getTeacherIdsOut());
                                    dto.setTeacherNames(teacher.getTeacherOutNames());
                                }
                            }
                            if (StringUtils.isNotBlank(teacherName) && teacher != null) {
                                boolean haveTeacher = false;
                                Pattern pattern = Pattern.compile(teacherName);
                                if (StringUtils.isNotBlank(dto.getTeacherIds())) {
                                    List<String> teacherNamesList = Arrays.asList(dto.getTeacherNames().split(","));
                                    for (String str : teacherNamesList) {
                                        Matcher matcher = pattern.matcher(str);
                                        if (matcher.find()) {
                                            haveTeacher = true;
                                        }
                                    }
                                }
                                if (!haveTeacher) {
                                    continue;
                                }
                            }
                            dto.setEmPlaceTeacherId(teacherIdMap.get(emPlace.getId() + emSubjectInfo.getSubjectId()));
                            dto.setEmPlaceId(emPlace.getId());
                            dto.setExamPlaceCode(emPlace.getExamPlaceCode());
                            dto.setEmSubjectInfoId(emSubjectInfo.getId());
                            dto.setExamId(examId);
                            dto.setSubjectId(emSubjectInfo.getSubjectId());
                            dto.setSubjectName(courseNamesMap.get(emSubjectInfo.getSubjectId()) + "(" + group.getGroupName() + ")");
                            dto.setExamPlaceId(emPlace.getPlaceId());
                            dto.setExamPlaceName(emPlace.getPlaceName());
                            dto.setExamStartAndEndNumber(numberMap.get(emPlace.getId() + groupId));
                            dto.setStartTime(emSubjectInfo.getStartDate());
                            if (StringUtils.equals(group.getSubType(), "0")) {
                                dto.setEndTime(emSubjectInfo.getEndDate());
                            } else if (StringUtils.equals(group.getSubType(), "1")) {
                                dto.setEndTime(emSubjectInfo.getEndDate());
                            } else if (StringUtils.equals(group.getSubType(), "2")) {
                                dto.setStartTime(emSubjectInfo.getGkStartDate());
                                dto.setEndTime(emSubjectInfo.getGkEndDate());
                            }
//							dto.setSubType(emSubjectInfo.getGkSubType());
                            teacherDtosList.add(dto);
                        }
                    }
                }
            }
        } else {
            //一般模式
            map.put("isgk", "0");
            List<String> numberList = null;
            Map<String, String> numberMap = new LinkedHashMap<>();
            for (EmPlace emPlace : emPlaceList) {
                numberList = new ArrayList<>();
                List<EmPlaceStudent> studentList = emPlaceStudentService.findByExamPlaceId(emPlace.getId());
                if (CollectionUtils.isNotEmpty(studentList)) {
                    Collections.sort(studentList, new Comparator<EmPlaceStudent>() {
                        @Override
                        public int compare(EmPlaceStudent o1, EmPlaceStudent o2) {
                            return o1.getExamNumber().compareTo(o2.getExamNumber());
                        }
                    });
                    for (EmPlaceStudent emPlaceStudent : studentList) {
                        numberList.add(emPlaceStudent.getExamNumber());
                    }
                    String number = numberList.get(0) + "-" + numberList.get(numberList.size() - 1);
                    numberMap.put(emPlace.getId(), number);
                }
            }
            InvigilateTeacherDto dto = null;
            EmPlaceTeacher teacher = null;
            if (CollectionUtils.isNotEmpty(findSByExamIdIn) && CollectionUtils.isNotEmpty(emPlaceList)) {
                for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                    for (EmPlace emPlace : emPlaceList) {
                        if (!"all".equals(subjectId) && !subjectId.equals(emSubjectInfo.getSubjectId())) {
                            continue;
                        }
                        teacher = teachersMap.get(emPlace.getId() + emSubjectInfo.getSubjectId());
                        if ("1".equals(status) && teacher == null) {
                            continue;
                        }
                        if ("0".equals(status) && teacher != null) {
                            continue;
                        }
                        dto = new InvigilateTeacherDto();
                        if (teacher != null) {
                            if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) && StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                                dto.setTeacherIds(teacher.getTeacherIdsIn() + "," + teacher.getTeacherIdsOut());
                                dto.setTeacherNames(teacher.getTeacherInNames() + "," + teacher.getTeacherOutNames());
                            } else if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) && StringUtils.isBlank(teacher.getTeacherIdsOut())) {
                                dto.setTeacherIds(teacher.getTeacherIdsIn());
                                dto.setTeacherNames(teacher.getTeacherInNames());
                            } else if (StringUtils.isBlank(teacher.getTeacherIdsIn()) && StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                                dto.setTeacherIds(teacher.getTeacherIdsOut());
                                dto.setTeacherNames(teacher.getTeacherOutNames());
                            }
                        }
                        if (StringUtils.isNotBlank(teacherName) && teacher != null) {
                            boolean haveTeacher = false;
                            Pattern pattern = Pattern.compile(teacherName);
                            if (StringUtils.isNotBlank(dto.getTeacherIds())) {
                                List<String> teacherNamesList = Arrays.asList(dto.getTeacherNames().split(","));
                                for (String str : teacherNamesList) {
                                    Matcher matcher = pattern.matcher(str);
                                    if (matcher.find()) {
                                        haveTeacher = true;
                                    }
                                }
                            }
                            if (!haveTeacher) {
                                continue;
                            }
                        }
                        dto.setEmPlaceTeacherId(teacherIdMap.get(emPlace.getId() + emSubjectInfo.getSubjectId()));
                        dto.setEmPlaceId(emPlace.getId());
                        dto.setExamPlaceCode(emPlace.getExamPlaceCode());
                        dto.setEmSubjectInfoId(emSubjectInfo.getId());
                        dto.setExamId(examId);
                        dto.setSubjectId(emSubjectInfo.getSubjectId());
                        dto.setSubjectName(courseNamesMap.get(emSubjectInfo.getSubjectId()));
                        dto.setStartTime(emSubjectInfo.getStartDate());
                        dto.setEndTime(emSubjectInfo.getEndDate());
                        dto.setGkStartTime(emSubjectInfo.getGkStartDate());
                        dto.setGkEndTime(emSubjectInfo.getGkEndDate());
                        dto.setSubType(emSubjectInfo.getGkSubType());
                        dto.setExamPlaceId(emPlace.getPlaceId());
                        dto.setExamPlaceName(emPlace.getPlaceName());
                        dto.setExamStartAndEndNumber(numberMap.get(emPlace.getId()));
                        teacherDtosList.add(dto);
                    }
                }
            }
        }
        map.put("examId", examId);
        map.put("teacherName", teacherName);
        if (CollectionUtils.isNotEmpty(teacherDtosList)) {
            Collections.sort(teacherDtosList, new Comparator<InvigilateTeacherDto>() {
                @Override
                public int compare(InvigilateTeacherDto o1, InvigilateTeacherDto o2) {
                    String a = o1.getSubjectName() + o1.getExamPlaceCode();
                    String b = o2.getSubjectName() + o2.getExamPlaceCode();
                    return a.compareTo(b);
                }
            });
        }
        map.put("teacherDtosList", teacherDtosList);
        map.put("status", status);
        map.put("subjectId", subjectId);
        return "/exammanage/examTeacher/invigilateTeacherList.ftl";
    }

    @RequestMapping("/examTeacher/invigilateTeacherInfo/page")
    @ControllerInfo("设置监考老师")
    public String teacherInfo(String examId, String courseSize, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Integer placeSize = emPlaceService.getSizeByExamIdAndSchoolId(examId, unitId);
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        map.put("examId", examId);
        map.put("placeSize", placeSize);
        map.put("subjectSize", courseSize);
        map.put("teacherOutLists", teacherOutLists);
        return "/exammanage/examTeacher/invigilateTeacherInfo.ftl";
    }

    @RequestMapping("/examTeacher/invigilateEdit/page")
    @ControllerInfo("编辑监考老师")
    public String teacherEdit(String examId, String status, String subjectId, String emPlaceTeacherId, String teacherIds, String emSubjectInfoId, String emPlaceId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmSubjectInfo> subjectList = emSubjectInfoService.findListByIdIn(new String[]{emSubjectInfoId});
        List<EmPlace> placeLsit = emPlaceService.findListByIds(new String[]{emPlaceId});
        EmSubjectInfo emSubjectInfo = subjectList.get(0);
        Course course = SUtils.dc(courseRemoteService.findOneById(emSubjectInfo.getSubjectId()), Course.class);
        emSubjectInfo.setCourseName(course.getSubjectName());
        EmPlace emPlace = placeLsit.get(0);
        TeachPlace teachPlace = SUtils.dc(teachPlaceRemoteService.findTeachPlaceById(emPlace.getPlaceId()), TeachPlace.class);
        emPlace.setPlaceName(teachPlace.getPlaceName());

        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        String teachInIds = "";
        String teacherInNames = "";
        List<String> teacherOutIdsList = new ArrayList<String>();
        if (StringUtils.isNotBlank(teacherIds)) {
            List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
            });
            Map<String, String> teachInNameMap = new LinkedHashMap<String, String>();
            List<String> teacherIdList = new ArrayList<String>();
            for (Teacher teacher : teacherList) {
                teachInNameMap.put(teacher.getId(), teacher.getTeacherName());
                teacherIdList.add(teacher.getId());
            }
            List<String> teacherIdsList = Arrays.asList(teacherIds.split(","));
            String teacherOutIds = "";
            for (String str : teacherIdsList) {
                if (teacherIdList.contains(str)) {
                    teachInIds += str + ",";
                    teacherInNames += teachInNameMap.get(str) + ",";
                } else {
                    teacherOutIds += str + ",";
                }
            }
            if (StringUtils.isNotBlank(teacherOutIds)) {
                teacherOutIdsList = Arrays.asList(teacherOutIds.split(","));
            }
        }
        if (StringUtils.isNotBlank(teachInIds)) {
            teachInIds = teachInIds.substring(0, teachInIds.length() - 1);
            teacherInNames = teacherInNames.substring(0, teacherInNames.length() - 1);
        }
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            //新高考模式
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            Set<String> groupIds = new HashSet<>();
            Map<String, EmSubGroup> groupMap = new HashMap<>();
            for (EmSubGroup g : groupList) {
                groupMap.put(g.getId(), g);
                String[] subIdArr = g.getSubjectId().split(",");
                for (String subId : subIdArr) {
                    if (StringUtils.equals(subId, emSubjectInfo.getSubjectId())) {
                        groupIds.add(g.getId());
                    }
                }
            }
            List<EmPlaceGroup> placeGlist = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
            for (EmPlaceGroup pg : placeGlist) {
                if (groupIds.contains(pg.getGroupId()) && StringUtils.equals(pg.getExamPlaceId(), emPlaceId)) {
                    EmSubGroup g = groupMap.get(pg.getGroupId());
                    if (StringUtils.equals(g.getSubType(), "2")) {
                        emSubjectInfo.setStartDate(emSubjectInfo.getGkStartDate());
                        emSubjectInfo.setEndDate(emSubjectInfo.getGkEndDate());
                    }
                }
            }
        }
        map.put("examId", examId);
        map.put("teacherOutLists", teacherOutLists);
        map.put("teachInIds", teachInIds);
        map.put("teacherInNames", teacherInNames);
        map.put("teacherOutIdsList", teacherOutIdsList);
        map.put("emSubjectInfo", emSubjectInfo);
        map.put("emPlace", emPlace);
        map.put("placeId", emPlace.getId());
        map.put("status", status);
        map.put("subjectId", subjectId);
        map.put("emPlaceTeacherId", emPlaceTeacherId);
        return "/exammanage/examTeacher/invigilateEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/examTeacher/invigilateBatchSave")
    @ControllerInfo("批量保存监考老师")
    public String invigilateBatchSave(String examId, String teacherInIds, String needInspector,
                                      String teacherOutIds, String teacherSize, String invigilateMax, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Map<String, Set<String>> teaSubIdsMap = new HashMap<>();
        Map<String, Set<String>> subTeaIds = new HashMap<>();
        List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE2);
        Map<String, Integer> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(teacherList)) {
            for (EmPlaceTeacher e : teacherList) {
                if (!subTeaIds.containsKey(e.getSubjectId())) {
                    subTeaIds.put(e.getSubjectId(), new HashSet<>());
                }
                if (StringUtils.isNotBlank(e.getTeacherIdsIn())) {
                    String[] teaIds = e.getTeacherIdsIn().split(",");
                    for (String teaid : teaIds) {
                        if (StringUtils.equals(needInspector, "1")) {
                            if (!teaSubIdsMap.containsKey(teaid)) {
                                teaSubIdsMap.put(teaid, new HashSet<>());
                            }
                            teaSubIdsMap.get(teaid).add(e.getSubjectId());
                            if (!map.containsKey(teaid)) {
                                map.put(teaid, 0);
                            }
                            //TODO
                            map.put(teaid, getMinute(e.getEndTime(), e.getStartTime()) + map.get(teaid));
                        }
                        subTeaIds.get(e.getSubjectId()).add(teaid);
                    }

                }
                if (StringUtils.isNotBlank(e.getTeacherIdsOut())) {
                    String[] teaIds = e.getTeacherIdsOut().split(",");
                    for (String teaid : teaIds) {
                        if (StringUtils.equals(needInspector, "1")) {
                            if (!teaSubIdsMap.containsKey(teaid)) {
                                teaSubIdsMap.put(teaid, new HashSet<>());
                            }
                            teaSubIdsMap.get(teaid).add(e.getSubjectId());
                            if (!map.containsKey(teaid)) {
                                map.put(teaid, 0);
                            }
                            map.put(teaid, getMinute(e.getEndTime(), e.getStartTime()) + map.get(teaid));
                        }
                        subTeaIds.get(e.getSubjectId()).add(teaid);
                    }
                }
            }
        }
        //考试科目信息
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        //考场信息
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        //所有在校老师
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        List<String> teacherIdInLists = new ArrayList<>();
        for (Teacher teacher : teacherInLists) {
            teacherIdInLists.add(teacher.getId());
        }
        //所有校外老师
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        List<String> teacherIdOutLists = new ArrayList<>();
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            teacherIdOutLists.add(emOutTeacher.getId());
        }
        Map<String, Integer> teaMinuteMap = new HashMap<>();
        Map<String, Integer> teaNumMap = new HashMap<>();
        if (StringUtils.isNotBlank(teacherInIds)) {
            String[] ids = teacherInIds.split(",");
            for (String id : ids) {
                int i = 0;
                if (map.containsKey(id)) {
                    teaMinuteMap.put(id, map.get(id));
                } else {
                    teaMinuteMap.put(id, 0);
                }
                if (teaSubIdsMap.containsKey(id)) {
                    i = teaSubIdsMap.get(id).size();
                }
                teaNumMap.put(id, i);
            }
        }
        if (StringUtils.isNotBlank(teacherOutIds)) {
            String[] ids = teacherOutIds.split(",");
            for (String id : ids) {
                int i = 0;
                if (map.containsKey(id)) {
                    teaMinuteMap.put(id, map.get(id));
                } else {
                    teaMinuteMap.put(id, 0);
                }
                if (teaSubIdsMap.containsKey(id)) {
                    i = teaSubIdsMap.get(id).size();
                }
                teaNumMap.put(id, i);
            }
        }
        List<EmPlaceTeacher> invigilateList = new ArrayList<>();
        EmPlaceTeacher emPlaceTeacher = null;
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            //新高考模式
            Map<String, EmPlace> placeMap = EntityUtils.getMap(emPlaceList, EmPlace::getId);
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            Map<String, Set<String>> subGroupMap = new HashMap<>();
            Map<String, EmSubGroup> groupMap = new HashMap<>();
            for (EmSubGroup g : groupList) {
                groupMap.put(g.getId(), g);
                String[] subIdArr = g.getSubjectId().split(",");
                for (String subId : subIdArr) {
                    if (!subGroupMap.containsKey(subId)) {
                        subGroupMap.put(subId, new HashSet<>());
                    }
                    subGroupMap.get(subId).add(g.getId());
                }
            }
            List<EmPlaceGroup> placeGlist = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
            Map<String, Set<String>> groupPlaceMap = new HashMap<>();
            for (EmPlaceGroup pg : placeGlist) {
                if (!groupPlaceMap.containsKey(pg.getGroupId())) {
                    groupPlaceMap.put(pg.getGroupId(), new HashSet<>());
                }
                groupPlaceMap.get(pg.getGroupId()).add(pg.getExamPlaceId());
            }
            //数据组装
            if (CollectionUtils.isNotEmpty(findSByExamIdIn) && CollectionUtils.isNotEmpty(emPlaceList)) {
                for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                    Set<String> groupIds = subGroupMap.get(emSubjectInfo.getSubjectId());
                    if (groupIds == null) {
                        continue;
                    }
                    for (String groupId : groupIds) {
                        EmSubGroup group = groupMap.get(groupId);
                        Set<String> placeIds = groupPlaceMap.get(groupId);
                        if (CollectionUtils.isEmpty(placeIds)) {
                            continue;
                        }
                        for (String placeId : placeIds) {
                            EmPlace emPlace = placeMap.get(placeId);
                            emPlaceTeacher = new EmPlaceTeacher();
                            emPlaceTeacher.setUnitId(unitId);
                            emPlaceTeacher.setExamId(examId);
                            emPlaceTeacher.setExamPlaceId(emPlace.getId());
                            emPlaceTeacher.setSubjectId(emSubjectInfo.getSubjectId());
                            emPlaceTeacher.setStartTime(emSubjectInfo.getStartDate());
                            if (StringUtils.equals(group.getSubType(), "0")) {
                                emPlaceTeacher.setEndTime(emSubjectInfo.getEndDate());
                            } else if (StringUtils.equals(group.getSubType(), "1")) {
                                emPlaceTeacher.setEndTime(emSubjectInfo.getEndDate());
                            } else if (StringUtils.equals(group.getSubType(), "2")) {
                                emPlaceTeacher.setEndTime(emSubjectInfo.getGkEndDate());
                            }
                            emPlaceTeacher.setMinute(getMinute(emPlaceTeacher.getEndTime(), emPlaceTeacher.getStartTime()));
                            emPlaceTeacher.setType(ExammanageConstants.TEACHER_TYPE1);
                            invigilateList.add(emPlaceTeacher);
                        }
                    }
                }
            }

        } else {
            if (CollectionUtils.isNotEmpty(findSByExamIdIn) && CollectionUtils.isNotEmpty(emPlaceList)) {
                for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                    for (EmPlace emPlace : emPlaceList) {
                        emPlaceTeacher = new EmPlaceTeacher();
                        emPlaceTeacher.setUnitId(unitId);
                        emPlaceTeacher.setExamId(examId);
                        emPlaceTeacher.setExamPlaceId(emPlace.getId());
                        emPlaceTeacher.setSubjectId(emSubjectInfo.getSubjectId());
                        emPlaceTeacher.setStartTime(emSubjectInfo.getStartDate());
                        emPlaceTeacher.setEndTime(emSubjectInfo.getEndDate());
                        emPlaceTeacher.setMinute(getMinute(emSubjectInfo.getEndDate(), emSubjectInfo.getStartDate()));
                        emPlaceTeacher.setType(ExammanageConstants.TEACHER_TYPE1);
                        invigilateList.add(emPlaceTeacher);
                    }
                }
            }
        }
        SortUtils.DESC(invigilateList, "minute");
        int teacherMax = (int) NumberUtils.toFloat(teacherSize);
        for (EmPlaceTeacher placeTeacher : invigilateList) {
            if (!subTeaIds.containsKey(placeTeacher.getSubjectId())) {
                subTeaIds.put(placeTeacher.getSubjectId(), new HashSet<>());
            }
            Set<String> set = subTeaIds.get(placeTeacher.getSubjectId());
            String idIn = "";
            String idOut = "";
            for (int i = 0; i < teacherMax; i++) {
                String teaId = getTeaIdByList(set, (int) NumberUtils.toFloat(invigilateMax), teaMinuteMap, teaNumMap);
                if (StringUtils.isNotBlank(teaId)) {
                    set.add(teaId);
                    teaMinuteMap.put(teaId, teaMinuteMap.get(teaId) + placeTeacher.getMinute());
                    teaNumMap.put(teaId, teaNumMap.get(teaId) + 1);
                }
                if (teacherIdInLists.contains(teaId)) {
                    idIn += teaId + ",";
                }
                if (teacherIdOutLists.contains(teaId)) {
                    idOut += teaId + ",";
                }
            }
            if (StringUtils.isNotBlank(idIn)) {
                placeTeacher.setTeacherIdsIn(idIn.substring(0, idIn.length() - 1));
            }
            if (StringUtils.isNotBlank(idOut)) {
                placeTeacher.setTeacherIdsOut(idOut.substring(0, idOut.length() - 1));
            }
        }
        try {
            if (CollectionUtils.isNotEmpty(invigilateList)) {
                emPlaceTeacherService.saveAllAndDel(examId, invigilateList, ExammanageConstants.TEACHER_TYPE1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功！");
    }

    private int getMinute(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        int minute = (int) diff / (1000 * 60);
        return minute;
    }

    private String getTeaIdByList(Set<String> set, int invigilateMax, Map<String, Integer> teaMinuteMap, Map<String, Integer> teaNumMap) {
        //找到已安排时间最少的老师集合
        Set<String> minTeas = new HashSet<>();
        int min = Integer.MAX_VALUE;
        int minNum = invigilateMax;
        for (String teaId : teaMinuteMap.keySet()) {
            if (set.contains(teaId)) {
                continue;
            }
            if (teaNumMap.get(teaId) == invigilateMax) {
                continue;
            }
            if (teaMinuteMap.get(teaId) < min) {
                min = teaMinuteMap.get(teaId);
                minNum = teaNumMap.get(teaId);
                minTeas = new HashSet<>();
                minTeas.add(teaId);
            } else if (teaMinuteMap.get(teaId) == min) {
                minTeas.add(teaId);
                if (teaNumMap.get(teaId) < minNum) {
                    minNum = teaNumMap.get(teaId);
                }
            }
        }
        for (String key : minTeas) {
            if (minNum == teaNumMap.get(key)) {
                return key;
            }
        }
        return null;
    }

    public boolean opinion(String examId, String subjectId, String teacherId) {
        List<EmPlaceTeacher> inspectorsTeacherList = emPlaceTeacherService.
                findByExamIdAndSubjectIdAndType(examId, subjectId, ExammanageConstants.TEACHER_TYPE2);
        String teacherIds = "";
        for (EmPlaceTeacher emPlaceTeacher : inspectorsTeacherList) {
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                teacherIds += emPlaceTeacher.getTeacherIdsIn() + ",";
            }
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                teacherIds += emPlaceTeacher.getTeacherIdsOut() + ",";
            }
        }
        if (StringUtils.isNotBlank(teacherIds)) {
            teacherIds = teacherIds.substring(0, teacherIds.length() - 1);
            List<String> teacherIdsList = Arrays.asList(teacherIds.split(","));
            if (teacherIdsList.contains(teacherId)) {
                return true;
            }
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/examTeacher/invigilateSave")
    @ControllerInfo("保存监考老师")
    public String invigilateSave(String examId, EmSubjectInfo emSubjectInfo, String placeId, EmPlace emPlace, String emPlaceTeacherId, String teacherInIds, String teacherOutIds, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        String inspectorsTeacherIds = "";
        String invigilateTeacherIds = "";
        List<EmPlaceTeacher> inspectorsTeacherList = emPlaceTeacherService.findByExamIdAndSubjectIdAndType(examId, emSubjectInfo.getSubjectId(), ExammanageConstants.TEACHER_TYPE2);
        List<EmPlaceTeacher> invigilateTeacherList = emPlaceTeacherService.findByExamIdAndSubjectIdAndType(examId, emSubjectInfo.getSubjectId(), ExammanageConstants.TEACHER_TYPE1);
        if (CollectionUtils.isNotEmpty(invigilateTeacherList)) {
            for (EmPlaceTeacher emPlaceTeacher : invigilateTeacherList) {
                if (emPlaceTeacher.getId().equals(emPlaceTeacherId)) {
                    continue;
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                    invigilateTeacherIds += emPlaceTeacher.getTeacherIdsIn() + ",";
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                    invigilateTeacherIds += emPlaceTeacher.getTeacherIdsOut() + ",";
                }
            }
            if (StringUtils.isNotBlank(invigilateTeacherIds)) {
                invigilateTeacherIds = invigilateTeacherIds.substring(0, invigilateTeacherIds.length() - 1);
            }
        }
        List<String> invigilateTeacherIdsList = new ArrayList<String>();
        if (StringUtils.isNotBlank(invigilateTeacherIds)) {
            invigilateTeacherIdsList = Arrays.asList(invigilateTeacherIds.split(","));
        }
        if (CollectionUtils.isNotEmpty(inspectorsTeacherList)) {
            for (EmPlaceTeacher emPlaceTeacher : inspectorsTeacherList) {
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                    inspectorsTeacherIds += emPlaceTeacher.getTeacherIdsIn() + ",";
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                    inspectorsTeacherIds += emPlaceTeacher.getTeacherIdsOut() + ",";
                }
            }
            inspectorsTeacherIds = inspectorsTeacherIds.substring(0, inspectorsTeacherIds.length() - 1);
        }
        List<String> inspectorsTeacherIdsList = new ArrayList<String>();
        if (StringUtils.isNotBlank(inspectorsTeacherIds)) {
            inspectorsTeacherIdsList = Arrays.asList(inspectorsTeacherIds.split(","));
        }
        if (StringUtils.isNotBlank(teacherInIds)) {
            String[] teacherIdsIn = teacherInIds.split(",");
            for (int i = 0; i < teacherIdsIn.length; i++) {
                if (inspectorsTeacherIdsList.contains(teacherIdsIn[i])) {
                    return returnError("操作失败！", "已设置该在校老师为当前时间段的巡考老师！");
                }
                if (invigilateTeacherIdsList.contains(teacherIdsIn[i])) {
                    return returnError("操作失败！", "已设置该在校老师为当前时间段其他考场的监考老师！");
                }
            }
        }
        if (StringUtils.isNotBlank(teacherOutIds)) {
            String[] teacherIdsOut = teacherOutIds.split(",");
            for (int i = 0; i < teacherIdsOut.length; i++) {
                if (inspectorsTeacherIdsList.contains(teacherIdsOut[i])) {
                    return returnError("操作失败！", "已设置该校外老师为当前时间段的巡考老师！");
                }
                if (invigilateTeacherIdsList.contains(teacherIdsOut[i])) {
                    return returnError("操作失败！", "已设置该校外老师为当前时间段其他考场的监考老师！");
                }
            }
        }
        try {
            EmPlaceTeacher emPlaceTeacher = new EmPlaceTeacher();
            emPlaceTeacher.setUnitId(unitId);
            emPlaceTeacher.setExamId(examId);
            emPlaceTeacher.setExamPlaceId(placeId);
            if (StringUtils.isNotBlank(teacherInIds)) {
                emPlaceTeacher.setTeacherIdsIn(teacherInIds);
            }
            if (StringUtils.isNotBlank(teacherOutIds)) {
                emPlaceTeacher.setTeacherIdsOut(teacherOutIds);
            }
            emPlaceTeacher.setSubjectId(emSubjectInfo.getSubjectId());
            emPlaceTeacher.setStartTime(emSubjectInfo.getStartDate());
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
                //新高考模式
                List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
                Set<String> groupIds = new HashSet<>();
                Map<String, EmSubGroup> groupMap = new HashMap<>();
                for (EmSubGroup g : groupList) {
                    groupMap.put(g.getId(), g);
                    String[] subIdArr = g.getSubjectId().split(",");
                    for (String subId : subIdArr) {
                        if (StringUtils.equals(subId, emSubjectInfo.getSubjectId())) {
                            groupIds.add(g.getId());
                        }
                    }
                }
                List<EmPlaceGroup> placeGlist = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
                for (EmPlaceGroup pg : placeGlist) {
                    if (groupIds.contains(pg.getGroupId()) && StringUtils.equals(pg.getExamPlaceId(), placeId)) {
                        EmSubGroup g = groupMap.get(pg.getGroupId());
                        if (StringUtils.equals(g.getSubType(), "2")) {
                            emPlaceTeacher.setStartTime(emSubjectInfo.getGkStartDate());
                            emPlaceTeacher.setEndTime(emSubjectInfo.getGkEndDate());
                        } else {
                            emPlaceTeacher.setEndTime(emSubjectInfo.getEndDate());
                        }
                    }
                }
            } else {
                emPlaceTeacher.setEndTime(emSubjectInfo.getEndDate());
            }

            emPlaceTeacher.setType(ExammanageConstants.TEACHER_TYPE1);
            emPlaceTeacherService.saveAndDel(emPlaceTeacher, emPlaceTeacherId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功！");
    }

    @RequestMapping("/examTeacher/outTeacherIndex/page")
    @ControllerInfo(value = "设置校外老师")
    public String outTeacherIndex(String examId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        map.put("examId", examId);
        map.put("teacherOutLists", teacherOutLists);
        return "/exammanage/examTeacher/outTeacherList.ftl";
    }

    @RequestMapping("/examTeacher/outTeacherAdd")
    @ControllerInfo(value = "新增校外老师")
    public String outTeacherAdd(String examId, ModelMap map) {
        map.put("examId", examId);
        return "/exammanage/examTeacher/outTeacherEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/examTeacher/outTeacherSave")
    @ControllerInfo(value = "保存校外老师")
    public String outTeacherSave(String examId, String outTeacherName, String outTeacherTel, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            if (emOutTeacher.getTeacherName().equals(outTeacherName)) {
                return returnError("操作失败！", "该老师已存在");
            }
            if (emOutTeacher.getMobilePhone().equals(outTeacherTel)) {
                return returnError("操作失败！", "该手机号已存在");
            }
        }
        try {
            EmOutTeacher outTeacher = new EmOutTeacher();
            outTeacher.setExamId(examId);
            outTeacher.setSchoolId(unitId);
            outTeacher.setTeacherName(outTeacherName);
            outTeacher.setMobilePhone(outTeacherTel);
            emOutTeacherService.saveOne(outTeacher);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping("/examTeacher/outTeacherDelete")
    @ControllerInfo(value = "删除校外老师,examId[{examId}],outTeacherId[{id}]")
    public String outTeacherDelete(String id, String examId) {
        List<EmPlaceTeacher> emPlaceTeacherList = emPlaceTeacherService.findByExamId(examId);
        String teacherIds = "";
        for (EmPlaceTeacher emPlaceTeacher : emPlaceTeacherList) {
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                teacherIds += emPlaceTeacher.getTeacherIdsOut() + ",";
            }
        }
        if (StringUtils.isNotBlank(teacherIds)) {
            if (teacherIds.contains(id)) {
                return returnError("操作失败！", "已设置该校外老师为监考或巡考老师");
            }
        }
        try {
            emOutTeacherService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }
}
