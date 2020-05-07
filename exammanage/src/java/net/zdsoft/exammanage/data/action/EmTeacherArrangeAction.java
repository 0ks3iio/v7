package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.TeacherArrangeDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmOutTeacher;
import net.zdsoft.exammanage.data.entity.EmPlaceTeacher;
import net.zdsoft.exammanage.data.service.EmOutTeacherService;
import net.zdsoft.exammanage.data.service.EmPlaceTeacherService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/exammanage")
public class EmTeacherArrangeAction extends EmExamCommonAction {

    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;

    @RequestMapping("/teacherStat/index/page")
    @ControllerInfo(value = "教师安排统计")
    public String showIndex(ModelMap map) {
        return "/exammanage/teacherArrange/teacherArrangeIndex.ftl";
    }

    @RequestMapping("/teacherStat/inspectorsArrangeIndex/page")
    @ControllerInfo(value = "巡考老师安排Index")
    public String inspectorsArrange(ModelMap map, HttpSession httpSession) {
        Date nowTime = new Date();
        map.put("startTime", nowTime);
        map.put("endTime", nowTime);
        return "/exammanage/teacherArrange/inspectorsArrangeIndex.ftl";
    }

    @RequestMapping("/teacherStat/inspectorsArrangeList/page")
    @ControllerInfo(value = "巡考老师安排List")
    public String inspectorsArrangeList(String startTime, String endTime, String queryTeacherName, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Map<String, Map<String, String>> teacherMap = new HashMap<String, Map<String, String>>();
        Set<String> examIdsSet = new HashSet<String>();
        List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByUnitIdAndStartTimeAndType(unitId, startTime, endTime, ExammanageConstants.TEACHER_TYPE2);
        Map<String, String> teacherNamesMap = new LinkedHashMap<String, String>();
        Map<String, String> teacherIdsMap = new LinkedHashMap<String, String>();
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findBySchoolId(unitId);
        for (Teacher teacher : teacherInLists) {
            teacherNamesMap.put(teacher.getId(), teacher.getTeacherName());
            teacherIdsMap.put(teacher.getTeacherName(), teacher.getId());
        }
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            teacherNamesMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
            teacherIdsMap.put(emOutTeacher.getTeacherName(), emOutTeacher.getId());
        }
        List<String> queryTeacherIds = new ArrayList<String>();
        if (StringUtils.isNotBlank(queryTeacherName)) {
            try {
                queryTeacherName = URLDecoder.decode(queryTeacherName, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pattern pattern = Pattern.compile(queryTeacherName);
            for (Map.Entry<String, String> entry : teacherIdsMap.entrySet()) {
                Matcher matcher = pattern.matcher(entry.getKey());
                if (matcher.find()) {
                    queryTeacherIds.add(entry.getValue());
                }
            }
            if (CollectionUtils.isEmpty(queryTeacherIds)) {
                return "/exammanage/teacherArrange/inspectorsArrangeList.ftl";
            }
        }
        for (EmPlaceTeacher emPlaceTeacher : teacherList) {
            examIdsSet.add(emPlaceTeacher.getExamId());
            String teachIds = "";
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                teachIds += emPlaceTeacher.getTeacherIdsIn() + ",";
            }
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                teachIds += emPlaceTeacher.getTeacherIdsOut() + ",";
            }
            teachIds = teachIds.substring(0, teachIds.length() - 1);
            String[] teachInAndOutIds = teachIds.split(",");
            for (int i = 0; i < teachInAndOutIds.length; i++) {
                if (StringUtils.isNotBlank(queryTeacherName) && (!queryTeacherIds.contains(teachInAndOutIds[i]))) {
                    continue;
                }
                Map<String, String> examAll = teacherMap.get(teachInAndOutIds[i]);
                if (MapUtils.isEmpty(examAll)) {
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.put(emPlaceTeacher.getExamId(), "1");
                    teacherMap.put(teachInAndOutIds[i], newMap);
                } else {
                    String size = examAll.get(emPlaceTeacher.getExamId());
                    if (StringUtils.isBlank(size)) {
                        examAll.put(emPlaceTeacher.getExamId(), "1");
                        teacherMap.put(teachInAndOutIds[i], examAll);
                    } else {
                        examAll.put(emPlaceTeacher.getExamId(), String.valueOf((Integer.parseInt(size) + 1)));
                        teacherMap.put(teachInAndOutIds[i], examAll);
                    }
                }
            }
        }
        Map<String, EmExamInfo> examInfoMap = emExamInfoService.findMapByIdIn(examIdsSet.toArray(new String[]{}));
        List<TeacherArrangeDto> teacherDtoList = new ArrayList<TeacherArrangeDto>();
        TeacherArrangeDto teacherArrangeDto = null;
        for (Map.Entry<String, Map<String, String>> entrys : teacherMap.entrySet()) {
            String examName = "";
            String maxSize = "";
            for (Map.Entry<String, String> entry : entrys.getValue().entrySet()) {
                examName += examInfoMap.get(entry.getKey()).getExamName() + "(" + entry.getValue() + "),";
                if (StringUtils.isBlank(maxSize)) {
                    maxSize = entry.getValue();
                } else {
                    maxSize = String.valueOf(Integer.parseInt(maxSize) + Integer.parseInt(entry.getValue()));
                }
            }
            examName = examName.substring(0, examName.length() - 1);
            teacherArrangeDto = new TeacherArrangeDto();
            teacherArrangeDto.setTeacherName(teacherNamesMap.get(entrys.getKey()));
            teacherArrangeDto.setExamNames(examName);
            teacherArrangeDto.setExamSize(maxSize);
            teacherDtoList.add(teacherArrangeDto);
        }
        map.put("teacherDtoList", teacherDtoList);
        return "/exammanage/teacherArrange/inspectorsArrangeList.ftl";
    }

    @RequestMapping("/teacherStat/invigilateArrangeIndex/page")
    @ControllerInfo(value = "监考老师安排Index")
    public String invigilateArrangeIndex(ModelMap map, HttpSession httpSession) {
        Date nowTime = new Date();
        map.put("startTime", nowTime);
        map.put("endTime", nowTime);
        return "/exammanage/teacherArrange/invigilateArrangeIndex.ftl";
    }

    @RequestMapping("/teacherStat/invigilateArrangeList/page")
    @ControllerInfo(value = "监考老师安排List")
    public String invigilateArrangeList(String startTime, String endTime, String queryTeacherName, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Map<String, Map<String, String>> teacherMap = new HashMap<String, Map<String, String>>();
        Set<String> examIdsSet = new HashSet<String>();
        List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByUnitIdAndStartTimeAndType(unitId, startTime, endTime, ExammanageConstants.TEACHER_TYPE1);
        Map<String, String> teacherNamesMap = new LinkedHashMap<String, String>();
        Map<String, String> teacherIdsMap = new LinkedHashMap<String, String>();
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findBySchoolId(unitId);
        for (Teacher teacher : teacherInLists) {
            teacherNamesMap.put(teacher.getId(), teacher.getTeacherName());
            teacherIdsMap.put(teacher.getTeacherName(), teacher.getId());
        }
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            teacherNamesMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
            teacherIdsMap.put(emOutTeacher.getTeacherName(), emOutTeacher.getId());
        }
        List<String> queryTeacherIds = new ArrayList<String>();
        if (StringUtils.isNotBlank(queryTeacherName)) {
            try {
                queryTeacherName = URLDecoder.decode(queryTeacherName, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pattern pattern = Pattern.compile(queryTeacherName);
            for (Map.Entry<String, String> entry : teacherIdsMap.entrySet()) {
                Matcher matcher = pattern.matcher(entry.getKey());
                if (matcher.find()) {
                    queryTeacherIds.add(entry.getValue());
                }
            }
            if (CollectionUtils.isEmpty(queryTeacherIds)) {
                return "/exammanage/teacherArrange/inspectorsArrangeList.ftl";
            }
        }
        for (EmPlaceTeacher emPlaceTeacher : teacherList) {
            examIdsSet.add(emPlaceTeacher.getExamId());
            String teachIds = "";
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                teachIds += emPlaceTeacher.getTeacherIdsIn() + ",";
            }
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                teachIds += emPlaceTeacher.getTeacherIdsOut() + ",";
            }
            if (StringUtils.isBlank(teachIds)) {
                continue;
            }
            teachIds = teachIds.substring(0, teachIds.length() - 1);
            String[] teachInAndOutIds = teachIds.split(",");
            for (int i = 0; i < teachInAndOutIds.length; i++) {
                if (StringUtils.isNotBlank(queryTeacherName) && (!queryTeacherIds.contains(teachInAndOutIds[i]))) {
                    continue;
                }
                Map<String, String> examAll = teacherMap.get(teachInAndOutIds[i]);
                if (MapUtils.isEmpty(examAll)) {
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.put(emPlaceTeacher.getExamId(), "1");
                    teacherMap.put(teachInAndOutIds[i], newMap);
                } else {
                    String size = examAll.get(emPlaceTeacher.getExamId());
                    if (StringUtils.isBlank(size)) {
                        examAll.put(emPlaceTeacher.getExamId(), "1");
                        teacherMap.put(teachInAndOutIds[i], examAll);
                    } else {
                        examAll.put(emPlaceTeacher.getExamId(), String.valueOf((Integer.parseInt(size) + 1)));
                        teacherMap.put(teachInAndOutIds[i], examAll);
                    }
                }
            }
        }
        Map<String, EmExamInfo> examInfoMap = emExamInfoService.findMapByIdIn(examIdsSet.toArray(new String[]{}));
        List<TeacherArrangeDto> teacherDtoList = new ArrayList<TeacherArrangeDto>();
        TeacherArrangeDto teacherArrangeDto = null;
        for (Map.Entry<String, Map<String, String>> entrys : teacherMap.entrySet()) {
            String examName = "";
            String maxSize = "";
            for (Map.Entry<String, String> entry : entrys.getValue().entrySet()) {
                examName += examInfoMap.get(entry.getKey()).getExamName() + "(" + entry.getValue() + "),";
                if (StringUtils.isBlank(maxSize)) {
                    maxSize = entry.getValue();
                } else {
                    maxSize = String.valueOf(Integer.parseInt(maxSize) + Integer.parseInt(entry.getValue()));
                }
            }
            examName = examName.substring(0, examName.length() - 1);
            teacherArrangeDto = new TeacherArrangeDto();
            teacherArrangeDto.setTeacherName(teacherNamesMap.get(entrys.getKey()));
            teacherArrangeDto.setExamNames(examName);
            teacherArrangeDto.setExamSize(maxSize);
            teacherDtoList.add(teacherArrangeDto);
        }
        map.put("teacherDtoList", teacherDtoList);
        return "/exammanage/teacherArrange/invigilateArrangeList.ftl";
    }
}
