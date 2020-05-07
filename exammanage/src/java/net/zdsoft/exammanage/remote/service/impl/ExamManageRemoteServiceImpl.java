package net.zdsoft.exammanage.remote.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.ExamInfoDto;
import net.zdsoft.exammanage.data.dto.ScoreInfoDto;
import net.zdsoft.exammanage.data.dto.SubjectInfoDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("examManageRemoteService")
public class ExamManageRemoteServiceImpl implements ExamManageRemoteService {
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmPlaceGroupService emPlaceGroupService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;

    @Override
    public Set<String> getExamSubjectPlaceIds(String examId, String subjectInfoId, String subType, String unitId) {
        Set<String> placeIdSet = new HashSet<>();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
//		if(StringUtils.isBlank(subType)||StringUtils.equals(subType, "0")) {
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            if (StringUtils.isBlank(subType)) {
                subType = "0";
            }
            EmSubjectInfo subInfo = emSubjectInfoService.findOne(subjectInfoId);
            String subjectId = subInfo.getSubjectId();
            List<EmSubGroup> groups = emSubGroupService.findByExamIdAndSubIdAndSubType(unitId, examId, subjectId, subType);
            if (CollectionUtils.isEmpty(groups)) {
                return placeIdSet;
            }
            List<EmPlaceGroup> placeGroups = emPlaceGroupService.findByGroupIdAndSchoolId(groups.get(0).getId(), unitId);
            if (CollectionUtils.isNotEmpty(placeGroups)) {
                Set<String> examPlaceIds = EntityUtils.getSet(placeGroups, EmPlaceGroup::getExamPlaceId);
                List<EmPlace> emPlaces = emPlaceService.findListByIds(examPlaceIds.toArray(new String[0]));
                for (EmPlace emPlace : emPlaces) {
                    placeIdSet.add(emPlace.getPlaceId());
                }
            }
        } else {
            List<EmPlace> emPlaces = emPlaceService.findByExamId(examId);
            if (CollectionUtils.isNotEmpty(emPlaces)) {
                for (EmPlace emPlace : emPlaces) {
                    placeIdSet.add(emPlace.getPlaceId());
                }
            }
        }
        return placeIdSet;
    }

    @Override//考场名称、考试科目、考试时间段、监考老师、考生名单（姓名、座位号）
    public String getDoorSticker(String examId, String subjectInfoId, String unitId, String placeId, String subType) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        JSONObject js = new JSONObject();
        EmSubjectInfo emSubjectInfo = emSubjectInfoService.findOne(subjectInfoId);
        String subjectId = emSubjectInfo.getSubjectId();
        Course course = SUtils.dc(courseRemoteService.findOneById(emSubjectInfo.getSubjectId()), Course.class);
        TeachPlace p = SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(placeId), new TR<TeachPlace>() {
        });
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            if (StringUtils.equals(subType, "2")) {
                js.put("examTime", DateUtils.date2String(emSubjectInfo.getStartDate(), "HH:mm") + "~" + DateUtils.date2String(emSubjectInfo.getGkEndDate(), "HH:mm"));
            } else {
                js.put("examTime", DateUtils.date2String(emSubjectInfo.getStartDate(), "HH:mm") + "~" + DateUtils.date2String(emSubjectInfo.getEndDate(), "HH:mm"));
            }
        } else {
            js.put("examTime", DateUtils.date2String(emSubjectInfo.getStartDate(), "HH:mm") + "~" + DateUtils.date2String(emSubjectInfo.getEndDate(), "HH:mm"));
        }
        js.put("examSubject", course != null ? course.getSubjectName() : "");
        js.put("examPlace", p != null ? p.getPlaceName() : "");
        EmPlace emPlace = emPlaceService.findByExamIdAndSchoolIdAndPlaceId(unitId, examId, placeId);
        List<EmPlaceTeacher> emPlaceTeachers = emPlaceTeacherService.findByExamIdAndPlaceIdAndSubjectId(examId, emPlace != null ? emPlace.getId() : "", emSubjectInfo != null ? emSubjectInfo.getSubjectId() : "", "1");
        Set<String> teachIdInSet = new HashSet<String>();
        Set<String> teachIdOutSet = new HashSet<String>();
        for (EmPlaceTeacher emPlaceTeacher : emPlaceTeachers) {
            String teacherIn = emPlaceTeacher.getTeacherIdsIn();
            String teacherOut = emPlaceTeacher.getTeacherIdsOut();
            if (StringUtils.isNotBlank(teacherIn)) {
                String[] teacherArr = teacherIn.split(",");
                for (String string : teacherArr) {
                    teachIdInSet.add(string);
                }
            }
            if (StringUtils.isNotBlank(teacherOut)) {
                String[] teacherArr = teacherOut.split(",");
                for (String string : teacherArr) {
                    teachIdOutSet.add(string);
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        boolean isFull = false;
        if (CollectionUtils.isNotEmpty(teachIdInSet)) {
            List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teachIdInSet.toArray(new String[0])), new TR<List<Teacher>>() {
            });
            for (Teacher teacher : teachers) {
                if (i == 0) {
                    sb.append(teacher.getTeacherName());
                } else {
                    if (sb.length() + (teacher.getTeacherName() == null ? 0 : teacher.getTeacherName().length()) > 8) {
                        sb.append("等");
                        isFull = true;
                        break;
                    }
                    sb.append("，" + teacher.getTeacherName());
                }
                i++;
            }
        }
        if (CollectionUtils.isNotEmpty(teachIdOutSet) && !isFull) {
            List<EmOutTeacher> emOutTeachers = emOutTeacherService.findListByIds(teachIdOutSet.toArray(new String[0]));
            if (CollectionUtils.isNotEmpty(emOutTeachers)) {
                for (EmOutTeacher emOutTeacher : emOutTeachers) {
                    if (i == 0) {
                        sb.append(emOutTeacher.getTeacherName());
                    } else {
                        if (sb.length() + (emOutTeacher.getTeacherName() == null ? 0 : emOutTeacher.getTeacherName().length()) > 8) {
                            sb.append("等");
                            break;
                        }
                        sb.append("，" + emOutTeacher.getTeacherName());
                    }
                    i++;
                }
            }
        }
        js.put("teacherNames", sb.toString());
        List<EmPlaceStudent> emPlaceStudents = new ArrayList<>();
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            List<EmSubGroup> groups = emSubGroupService.findByExamIdAndSubIdAndSubType(unitId, examId, subjectId, subType);
//			List<EmPlaceGroup> placeGroups = emPlaceGroupService.findByGroupIdAndSchoolId(groups.get(0).getId(), unitId);
            emPlaceStudents = emPlaceStudentService.findByExamIdAndPlaceIdAndGroupId(examId, emPlace.getId(), groups.get(0).getId());
        } else {
            emPlaceStudents = emPlaceStudentService.findByExamIdAndPlaceId(examId, emPlace != null ? emPlace.getId() : "");
        }
        Set<String> stuSet = new HashSet<String>();
        for (EmPlaceStudent emPlaceStudent : emPlaceStudents) {
            stuSet.add(emPlaceStudent.getStudentId());
        }
        JSONArray arr = new JSONArray();
        if (CollectionUtils.isNotEmpty(stuSet)) {
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuSet.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(s -> {
                return s.getId();
            }, s -> {
                return s;
            }));
            for (EmPlaceStudent emPlaceStudent : emPlaceStudents) {
                JSONObject jso = new JSONObject();
                if (studentMap.containsKey(emPlaceStudent.getStudentId())) {
                    Student student = studentMap.get(emPlaceStudent.getStudentId());
                    jso.put("studentName", student != null ? student.getStudentName() : "");
                } else {
                    jso.put("studentName", "学生不存在");
                }
                jso.put("studentNo", emPlaceStudent.getSeatNum());
                arr.add(jso);
            }
        }
        js.put("arr", arr);
        return js.toJSONString();
    }

    @Override
    public String doSynch(String jsonString) {
        JSONObject json = JSONObject.parseObject(jsonString);
        ExamInfoDto examDto = SUtils.dc(json.getString("exam"), ExamInfoDto.class);
        List<ScoreInfoDto> scoreDtoList = SUtils.dt(json.getString("scoreList"), new TR<List<ScoreInfoDto>>() {
        });
        List<SubjectInfoDto> subjectDtoList = SUtils.dt(json.getString("subInfoList"), new TR<List<SubjectInfoDto>>() {
        });
        emSubjectInfoService.synch(examDto, scoreDtoList, subjectDtoList);
        return null;
    }


    @Override
    public String getExamList(String unitId, String gradeCode, String acadyear, String semester) {
        System.out.println("unitId:" + unitId);
        System.out.println("gradeCode:" + gradeCode);
        System.out.println("acadyear:" + acadyear);
        System.out.println("semester:" + semester);
        JSONObject js = new JSONObject();
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        List<EmExamInfo> examInfoList = new ArrayList<>();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        searchDto.setSearchGradeCode(gradeCode);
        examInfoList = emExamInfoService.findExamList(null, unitId, searchDto, false);
        JSONArray arr = new JSONArray();
        for (EmExamInfo exam : examInfoList) {
            JSONObject jso = new JSONObject();
            jso.put("examId", exam.getId());
            jso.put("examCode", exam.getExamCode());
            jso.put("examName", exam.getExamName());
            arr.add(jso);
        }
        js.put("examlist", arr);
        System.out.println("返回json:" + js.toJSONString());
        return js.toJSONString();
    }

    @Override
    public String getExamArrange(String examId, String unitId) {
        System.out.println("examId:" + examId);
        System.out.println("unitId:" + unitId);
        JSONObject js = new JSONObject();
        EmExamInfo emExamInfo = emExamInfoService.findExamInfoOne(examId);
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        Map<String, String> teacherNamesMap = new LinkedHashMap<>();
        //所有在校老师
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        for (Teacher teacher : teacherInLists) {
            teacherNamesMap.put(teacher.getId(), teacher.getTeacherName());
        }
        //所有校外老师
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            teacherNamesMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
        }
        Map<String, String> emplaceTeacherMap = new LinkedHashMap<>();
        Map<String, String> emplaceTeacherIdMap = new LinkedHashMap<>();
        List<EmPlaceTeacher> emplaceTeacherList = emPlaceTeacherService.findByExamId(examId);
        for (EmPlaceTeacher emPlaceTeacher : emplaceTeacherList) {
            String teacherNames = "";
            String teacherIds = "";
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                String[] teacherIdsIn = emPlaceTeacher.getTeacherIdsIn().split(",");
                for (int i = 0; i < teacherIdsIn.length; i++) {
                    teacherNames += teacherNamesMap.get(teacherIdsIn[i]) + ",";
                    teacherIds += teacherIdsIn[i] + ",";
                }
            }
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                String[] teacherIdsOut = emPlaceTeacher.getTeacherIdsOut().split(",");
                for (int i = 0; i < teacherIdsOut.length; i++) {
                    teacherNames += teacherNamesMap.get(teacherIdsOut[i]) + ",";
                    teacherIds += teacherIdsOut[i] + ",";
                }
            }
            if (StringUtils.isBlank(teacherNames)) {
                continue;
            }
            teacherNames = teacherNames.substring(0, teacherNames.length() - 1);
            teacherIds = teacherIds.substring(0, teacherIds.length() - 1);
            if ("1".equals(emPlaceTeacher.getType())) {
                emplaceTeacherMap.put(emPlaceTeacher.getExamPlaceId() + emPlaceTeacher.getSubjectId(), teacherNames);
                emplaceTeacherIdMap.put(emPlaceTeacher.getExamPlaceId() + emPlaceTeacher.getSubjectId(), teacherIds);
            } else {
                emplaceTeacherMap.put(emPlaceTeacher.getSubjectId(), teacherNames);
                emplaceTeacherIdMap.put(emPlaceTeacher.getSubjectId(), teacherIds);
            }
        }

        Map<String, Set<String>> subPlaceMap = new HashMap<>();
        if (StringUtils.equals(emExamInfo.getIsgkExamType(), "1")) {
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            Map<String, Set<String>> subGroupMap = new HashMap<>();
            Set<String> subIds = new HashSet<>();
            for (EmSubGroup g : groupList) {
                String[] subIdArr = g.getSubjectId().split(",");
                for (String subId : subIdArr) {
                    if (!subGroupMap.containsKey(subId)) {
                        subGroupMap.put(subId, new HashSet<>());
                    }
                    subGroupMap.get(subId).add(g.getId());
                    subIds.add(subId);
                }
            }
            List<EmPlaceGroup> placeGlist = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
            for (String subId : subIds) {
                Set<String> groupIds = subGroupMap.get(subId);
                subPlaceMap.put(subId, new HashSet<>());
                for (EmPlaceGroup pg : placeGlist) {
                    if (groupIds.contains(pg.getGroupId())) {
                        subPlaceMap.get(subId).add(pg.getExamPlaceId());
                    }
                }
            }
        }
        Date dayTime = null;
        int sizeMax = 0;
        List<String> dayArr = new ArrayList<>();
        Map<String, List<EmSubjectInfo>> daySubMap = new HashMap<>();
        for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
            if (emSubjectInfo.getStartDate() == null) {
                break;
            }
            if (dayTime == null) {
                dayTime = emSubjectInfo.getStartDate();
            } else {
                String time1 = new SimpleDateFormat("yyyy-MM-dd").format(dayTime);
                String time2 = new SimpleDateFormat("yyyy-MM-dd").format(emSubjectInfo.getStartDate());
                if (time1.equals(time2)) {
                } else {
//					String time = new SimpleDateFormat("MM").format(dayTime) + "月" + new SimpleDateFormat("dd").format(dayTime) + "日";
                    String time = new SimpleDateFormat("yyyy-MM-dd").format(dayTime);
                    dayTime = emSubjectInfo.getStartDate();
                    dayArr.add(time);
                }
            }
            sizeMax++;
            if (sizeMax == findSByExamIdIn.size()) {
//				String time = new SimpleDateFormat("MM").format(dayTime) + "月" + new SimpleDateFormat("dd").format(dayTime) + "日";
                String time = new SimpleDateFormat("yyyy-MM-dd").format(dayTime);
                dayArr.add(time);
            }
//			String times = new SimpleDateFormat("MM").format(emSubjectInfo.getStartDate()) + "月" + new SimpleDateFormat("dd").format(emSubjectInfo.getStartDate()) + "日";
            String times = new SimpleDateFormat("yyyy-MM-dd").format(emSubjectInfo.getStartDate());
            if (!daySubMap.containsKey(times)) {
                daySubMap.put(times, new ArrayList<>());
            }
            daySubMap.get(times).add(emSubjectInfo);
        }
        JSONArray dayList = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (CollectionUtils.isNotEmpty(dayArr)) {
            for (String time : dayArr) {
                JSONObject dayJson = new JSONObject();
                dayJson.put("daytime", time);
                JSONArray subInfo = new JSONArray();
                List<EmSubjectInfo> subList = daySubMap.get(time);
                if (CollectionUtils.isNotEmpty(subList)) {
                    for (EmSubjectInfo sub : subList) {
                        JSONObject subJson = new JSONObject();
                        subJson.put("courseName", sub.getCourseName());
                        String dateStr = "";
                        if (StringUtils.equals(emExamInfo.getIsgkExamType(), "1")) {
                            if (StringUtils.equals(sub.getGkSubType(), "0")) {
                                if (sub.getGkEndDate() != null) {
                                    dateStr = "（学考：" + sdf.format(sub.getStartDate()) + "-" + sdf.format(sub.getGkEndDate())
                                            + "  选考：" + sdf.format(sub.getStartDate()) + "-" + sdf.format(sub.getEndDate()) + "）";
                                } else {
                                    dateStr = "（" + sdf.format(sub.getStartDate()) + "-" + sdf.format(sub.getEndDate()) + "）";
                                }
                            } else if (StringUtils.equals(sub.getGkSubType(), "1")) {
                                dateStr = "（选考：" + sdf.format(sub.getStartDate()) + "-" + sdf.format(sub.getEndDate()) + "）";
                            } else if (StringUtils.equals(sub.getGkSubType(), "2")) {
                                dateStr = "（学考：" + sdf.format(sub.getStartDate()) + "-" + sdf.format(sub.getGkEndDate()) + "）";
                            }
                        } else {
                            dateStr = "（" + sdf.format(sub.getStartDate()) + "-" + sdf.format(sub.getEndDate()) + "）";
                        }
                        subJson.put("startDate", sdf1.format(sub.getStartDate()));
                        if (sub.getGkEndDate() != null) {
                            subJson.put("xuekEndDate", sdf1.format(sub.getGkEndDate()));
                        }
                        if (sub.getEndDate() != null) {
                            subJson.put("endDate", sdf1.format(sub.getEndDate()));
                        }
                        subJson.put("date", dateStr);
                        JSONArray placeInfo = new JSONArray();
                        for (EmPlace emPlace : emPlaceList) {
                            JSONObject placeJson = new JSONObject();
                            placeJson.put("placeCode", emPlace.getExamPlaceCode());
                            placeJson.put("placeName", emPlace.getPlaceName());
                            placeJson.put("placeId", emPlace.getPlaceId());
                            if (StringUtils.equals(emExamInfo.getIsgkExamType(), "1")) {
                                Set<String> pIds = subPlaceMap.get(sub.getSubjectId());
                                if (pIds.contains(emPlace.getId())) {
                                    placeJson.put("jkTea", emplaceTeacherMap.get(emPlace.getId() + sub.getSubjectId()));
                                    placeJson.put("jkTeaId", emplaceTeacherIdMap.get(emPlace.getId() + sub.getSubjectId()));
                                } else {
                                    placeJson.put("jkTea", "该科目无该考场");
                                    placeJson.put("jkTeaId", "");
                                }
                            } else {
                                placeJson.put("jkTea", emplaceTeacherMap.get(emPlace.getId() + sub.getSubjectId()));
                                placeJson.put("jkTeaId", emplaceTeacherIdMap.get(emPlace.getId() + sub.getSubjectId()));
                            }
                            placeInfo.add(placeJson);
                        }
                        subJson.put("placeInfo", placeInfo);
                        if (emplaceTeacherMap.containsKey(sub.getSubjectId())) {
                            subJson.put("xkTea", emplaceTeacherMap.get(sub.getSubjectId()));
                            subJson.put("xkTeaId", emplaceTeacherIdMap.get(sub.getSubjectId()));
                        } else {
                            subJson.put("xkTea", "");
                            subJson.put("xkTeaId", "");
                        }
                        subInfo.add(subJson);
                    }
                }
                dayJson.put("subInfo", subInfo);
                dayList.add(dayJson);
            }
        }
        js.put("dayList", dayList);
        System.out.println("返回json：" + js.toJSONString());
        return js.toJSONString();
    }

    @Override
    public String getExamSubByUnitIdAndExamId(String unitId, String... examId) {
        return Json.toJSONString(emSubjectInfoService.findByUnitIdAndExamId(unitId, examId));
    }

    @Override
    public String getExamScoreInfoByExamIdAndUnitId(String unitId, String examId) {
        return Json.toJSONString(emScoreInfoService.findByExamIdAndUnitId(examId, unitId));
    }
}
