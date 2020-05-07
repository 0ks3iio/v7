package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmStatRangeService;
import net.zdsoft.exammanage.data.service.EmSubjectInfoService;
import net.zdsoft.exammanage.data.service.ReportService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/exammanage")
public class EmRemoteAction extends BaseAction {

    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private ReportService reportService;

    @ResponseBody
    @RequestMapping("/common/queryExamsByStudentId")
    public String queryExamsByStudentId(HttpServletRequest request) {

//		String unitId= request.getParameter("unitId");//统计单位
        String studentId = request.getParameter("studentId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");

        List<EmExamInfo> examList = reportService.queryExamsByStudentId(studentId, acadyear, semester, null);
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examList)) {
            for (EmExamInfo exam : examList) {
                JSONObject info = new JSONObject();
                info.put("id", exam.getId());
                info.put("name", exam.getExamName());
                infolist.add(info);
            }
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/common/queryExamSubTypeList")
    public String querySubTypeList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");//统计单位
        String subjectId = request.getParameter("subjectId");
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
        });
        Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
        String[] types = null;
        if (id73Set.contains(subjectId)) {
            types = new String[]{"0-普通考试", "1-高考考试选考", "2-高考考试学考"};
        } else {
            types = new String[]{"0-所有考试"};
        }
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        for (String type : types) {
            JSONObject info = new JSONObject();
            info.put("id", type.split("-")[0]);
            info.put("name", type.split("-")[1]);
            infolist.add(info);
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //http://127.0.0.1:8080/exammanage/common/queryExamSubClassList?unitId=2960EAA66A354A9C96209A59E94DFEC2&examId=10620541660510910139293250214041&subjectId=48613BF8B33AAAE7E050A8C09B006340,0&classType=2
    //根据考试和考试科目取班级
    @ResponseBody
    @RequestMapping("/common/queryExamSubClassList")
    public String querySubClassList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");//统计单位
        String examId = request.getParameter("examId");
        String classType = request.getParameter("classType");//空：全部 1，行政班 2，教学班
        String subjectIdType = request.getParameter("subjectId");
        String subjectId = subjectIdType.split(",")[0];
        String subType = subjectIdType.split(",").length > 1 ? subjectIdType.split(",")[1] : "";
        List<String> statClsIds = emStatRangeService.getClassIdsBy(unitId, examId, subjectId, subType);
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        if (CollectionUtils.isEmpty(statClsIds)) {
            return json.toJSONString();
        }
        if (StringUtils.isBlank(classType) || StringUtils.equals(classType, "1")) {
            List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(statClsIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(clsList)) {
                for (Clazz clazz : clsList) {
                    JSONObject info = new JSONObject();
                    info.put("id", clazz.getId());
                    info.put("className", clazz.getClassNameDynamic());
                    infolist.add(info);
                }
            }
        }
        if (StringUtils.isBlank(classType) || StringUtils.equals(classType, "2")) {
            List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findListByIds(statClsIds.toArray(new String[0])), new TR<List<TeachClass>>() {
            });
            if (CollectionUtils.isNotEmpty(teaClsList)) {
                for (TeachClass teaCls : teaClsList) {
                    JSONObject info = new JSONObject();
                    info.put("id", teaCls.getId());
                    info.put("className", teaCls.getName());
                    infolist.add(info);
                }
            }
			/*JSONObject info = new JSONObject();
			info.put("id", "14363497549979346286875250605703");
			info.put("className", "教学班");
			infolist.add(info);*/
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //根据考试科目取考试
    @ResponseBody
    @RequestMapping("/common/queryReferExamList")
    public String queryReferExamList(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        String type = request.getParameter("type");
        examId = examId.split(",")[0];
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo != null) {
            EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
            searchDto.setSearchAcadyear(examInfo.getAcadyear());
            searchDto.setSearchSemester(examInfo.getSemester());
            searchDto.setSearchGradeCode(examInfo.getGradeCodes());
            if (StringUtils.isNotBlank(type) && StringUtils.equals(type, "11")) {
                String sumId = request.getParameter("sumId");
                if (StringUtils.equals(ExammanageConstants.CON_SUM_ID, sumId)) {
                    searchDto.setSearchGk("1");
                }
                List<EmExamInfo> examInfos = emExamInfoService.findExamList(null, unitId, searchDto, false);
                List<JSONObject> infolist = new ArrayList<>();
                for (EmExamInfo e : examInfos) {
                    if (StringUtils.equals(e.getId(), examId)) {
                        continue;
                    }
                    JSONObject info = new JSONObject();
                    info.put("id", e.getId());
                    info.put("name", e.getExamName());
                    infolist.add(info);
                }
                json.put("infolist", infolist);
            } else {
                String subjectIdType = request.getParameter("subjectId");
                if (StringUtils.isBlank(subjectIdType)) {
                    return json.toJSONString();
                }
                if (StringUtils.isNotBlank(type) && StringUtils.equals(type, "12")) {
                    String sumId = request.getParameter("sumId");
                    if (StringUtils.equals(ExammanageConstants.CON_SUM_ID, sumId)) {
                        searchDto.setSearchGk("1");
                    }
                }
                String subjectId = subjectIdType.split(",")[0];

                List<EmExamInfo> examInfos = emExamInfoService.findExamList(null, unitId, searchDto, false);
                Map<String, String> examMap = EntityUtils.getMap(examInfos, EmExamInfo::getId, EmExamInfo::getExamName);
                Set<String> examIds = examMap.keySet();
                Set<String> ids = new HashSet<>();
                if (examIds.size() > 1) {
                    if (subjectIdType.split(",").length == 1) {
                        if (ExammanageConstants.ZERO32.equals(subjectId)) {//总分除自己外的所有考试
                            ids.addAll(examIds);
                        } else if (ExammanageConstants.CON_SUM_ID.equals(subjectId)) {//只考虑高考模式
                            for (EmExamInfo exam : examInfos) {
                                if ("1".equals(exam.getIsgkExamType())) {
                                    ids.add(exam.getId());
                                }
                            }
                        }
                    } else {
                        String subType = subjectIdType.split(",")[1];
                        List<EmSubjectInfo> infolist = emSubjectInfoService.findByExamIds(examIds);
                        for (EmSubjectInfo emSubjectInfo : infolist) {
							/*if(StringUtils.equals(emSubjectInfo.getExamId(), examId)) {
								continue;
							}*/
                            if (StringUtils.equals(emSubjectInfo.getSubjectId(), subjectId)) {
                                if (StringUtils.isBlank(emSubjectInfo.getGkSubType())
                                        || StringUtils.equals(emSubjectInfo.getGkSubType(), "0")
                                        || StringUtils.equals(emSubjectInfo.getGkSubType(), subType)) {
                                    //为0时代表这个科目一定会参加考试
                                    ids.add(emSubjectInfo.getExamId());
                                }
                            }
                        }
                    }
                }

                List<JSONObject> infolist = new ArrayList<>();
                if (ids.size() > 0) {
                    for (EmExamInfo e : examInfos) {
                        if (ids.contains(e.getId()) && (!e.getId().contains(examId))) {
                            JSONObject info = new JSONObject();
                            info.put("id", e.getId());
                            info.put("name", e.getExamName());
                            infolist.add(info);
                        }
                    }
                }
                json.put("infolist", infolist);
            }
        }
        return json.toJSONString();
    }

    //根据考试取班级
    @ResponseBody
    @RequestMapping("/common/queryExamClassList")
    public String queryClassList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");//统计单位
        String examId = request.getParameter("examId");
        String classType = request.getParameter("classType");//空：全部 1，行政班 2，教学班
        List<String> statClsIds = emStatRangeService.getClassIdsBy(unitId, examId);
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        if (CollectionUtils.isEmpty(statClsIds)) {
            json.put("infolist", infolist);
            return json.toJSONString();
        }
        if (StringUtils.isBlank(classType) || StringUtils.equals(classType, "1")) {
            List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(statClsIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(clsList)) {
                for (Clazz clazz : clsList) {
                    JSONObject info = new JSONObject();
                    info.put("id", clazz.getId());
                    info.put("className", clazz.getClassNameDynamic());
                    infolist.add(info);
                }
            }
        }
        if (StringUtils.isBlank(classType) || StringUtils.equals(classType, "2")) {
            List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findListByIds(statClsIds.toArray(new String[0])), new TR<List<TeachClass>>() {
            });
            if (CollectionUtils.isNotEmpty(teaClsList)) {
                for (TeachClass teaCls : teaClsList) {
                    JSONObject info = new JSONObject();
                    info.put("id", teaCls.getId());
                    info.put("className", teaCls.getName());
                    infolist.add(info);
                }
            }
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //所有考试科目列表
    @ResponseBody
    @RequestMapping("/common/queryExamSubList")
    public String querySubList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId").split(",")[0];
        String subType = request.getParameter("subType");
        String fromNewGrade = request.getParameter("fromNewGrade");
        String fromReport = request.getParameter("fromReport");
        List<Course> list = reportService.getSubList(unitId, examId, "0");
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        if (StringUtils.equals(fromNewGrade, "true")) {//判断是否加上总分以及非7选3考试赋分
            EmExamInfo exam = emExamInfoService.findOne(examId);
            JSONObject info = new JSONObject();
            info.put("name", "true".equals(fromReport) ? "考试总分" : "总分");
            info.put("id", ExammanageConstants.ZERO32);
            infolist.add(info);
            if (exam != null && "1".equals(exam.getIsgkExamType())) {
                info = new JSONObject();
                info.put("name", "true".equals(fromReport) ? "赋分总分" : "非7选3考试分+选考赋分");
                info.put("id", ExammanageConstants.CON_SUM_ID);
                infolist.add(info);
            }
        }
        for (Course e : list) {
            if ("1".equals(subType) && !StringUtils.equals(e.getCourseTypeId(), "1")) {
                continue;
            }
            JSONObject info = new JSONObject();
            if (StringUtils.isBlank(e.getCourseTypeId())) {//不让null 传到页面
                e.setCourseTypeId("0");
            }
            info.put("id", e.getId() + "," + e.getCourseTypeId());
            String typeName = "";
            if (StringUtils.equals(e.getCourseTypeId(), "1")) {
                typeName = "true".equals(fromReport) ? "选考" : "(选考)";
            } else if (StringUtils.equals(e.getCourseTypeId(), "2")) {
                typeName = "true".equals(fromReport) ? "学考" : "(学考)";
            }
            info.put("name", e.getSubjectName() + typeName);
            infolist.add(info);
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //选考考试列表
    @ResponseBody
    @RequestMapping("/common/queryGkXKExamList")
    public String queryGkXKSubList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String examId = request.getParameter("examId");
        List<Course> list = reportService.getSubList(unitId, examId, "1");
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        for (Course e : list) {
            JSONObject info = new JSONObject();
            info.put("id", e.getId() + "," + e.getCourseTypeId());
            String typeName = "(选考)";
            info.put("name", e.getSubjectName() + typeName);
            infolist.add(info);
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //所有考试列表
    @ResponseBody
    @RequestMapping("/common/queryExamList")
    public String queryExamList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        searchDto.setSearchGradeCode(gradeCode);
        List<EmExamInfo> examlist = emExamInfoService.findExamList(null, unitId, searchDto, false);
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        for (EmExamInfo e : examlist) {
            JSONObject info = new JSONObject();
            info.put("id", e.getId());
            info.put("examCode", e.getExamCode());
            String name = e.getExamName();
            info.put("name", name);
            info.put("type", StringUtils.isBlank(e.getIsgkExamType()) ? "0" : e.getIsgkExamType());
            infolist.add(info);
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //高考模式考试列表
    @ResponseBody
    @RequestMapping("/common/queryGkExamList")
    public String queryGkExamList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        searchDto.setSearchGk("1");
        searchDto.setSearchGradeCode(gradeCode);
        List<EmExamInfo> examlist = emExamInfoService.findExamList(null, unitId, searchDto, false);
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<>();
        for (EmExamInfo e : examlist) {
            JSONObject info = new JSONObject();
            info.put("id", e.getId());
            info.put("examCode", e.getExamCode());
            String name = e.getExamName();
            info.put("name", name);
            infolist.add(info);
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    //http://127.0.0.1:8080/exammanage/common/queryExamClsList?unitId=40152FDEBFA4486494B9C5C7E2B2E867&acadyear=2018-2019&semester=1&gradeCode=31&subjectId=00000000000000000000000000000037
    //学期内参与改考试科目的统计班级
    @ResponseBody
    @RequestMapping("/common/queryExamClsList")
    public String queryExamClsList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        String subjectId = request.getParameter("subjectId");
        String classType = request.getParameter("classType");//1行政班，2教学班  其他全部
        EmExamInfoSearchDto dto = new EmExamInfoSearchDto();
        dto.setSearchAcadyear(acadyear);
        dto.setSearchGradeCode(gradeCode);
        dto.setSearchSemester(semester);
        JSONObject json = new JSONObject();
        List<EmExamInfo> examInfoList = emExamInfoService.findExamList(null, unitId, dto, false);
        if (CollectionUtils.isEmpty(examInfoList)) {
            return json.toJSONString();
        }
        Set<String> examIds = EntityUtils.getSet(examInfoList, EmExamInfo::getId);
        List<String> clsIds = emStatRangeService.findRangeIdBySubjectId(subjectId, "1", unitId, examIds);
        List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<TeachClass>>() {
        });
        List<Json> result = new ArrayList<>();
        if ("1".equals(classType)) {
            teaClsList = null;
        } else if ("2".equals(classType)) {
            clsList = null;
        }
        if (CollectionUtils.isNotEmpty(clsList)) {
            clsList.forEach(e -> {
                Json data = new Json();
                data.put("id", e.getId());
                data.put("name", e.getClassNameDynamic());
                result.add(data);
            });
        }
        if (CollectionUtils.isNotEmpty(teaClsList)) {
            teaClsList.forEach(e -> {
                Json data = new Json();
                data.put("id", e.getId());
                data.put("name", e.getName());
                result.add(data);
            });
        }
        json.put("infolist", result);
        return json.toJSONString();
    }

    //http://127.0.0.1:8080/exammanage/common/queryExamAllSubList?unitId=40152FDEBFA4486494B9C5C7E2B2E867&acadyear=2018-2019&semester=1&gradeCode=31
    //学期内参与过考试的所有科目
    @ResponseBody
    @RequestMapping("/common/queryExamAllSubList")
    public String queryExamAllSubList(HttpServletRequest request) {
        String unitId = request.getParameter("unitId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String gradeCode = request.getParameter("gradeCode");
        EmExamInfoSearchDto dto = new EmExamInfoSearchDto();
        dto.setSearchAcadyear(acadyear);
        dto.setSearchGradeCode(gradeCode);
        dto.setSearchSemester(semester);
        JSONObject json = new JSONObject();
        List<EmExamInfo> examInfoList = emExamInfoService.findExamList(null, unitId, dto, false);
        if (CollectionUtils.isEmpty(examInfoList)) {
            return json.toJSONString();
        }
        Set<String> examIds = EntityUtils.getSet(examInfoList, EmExamInfo::getId);
        List<String> subIds = emSubjectInfoService.findSubIdByExamIds(examIds);
        if (CollectionUtils.isEmpty(subIds)) {
            return json.toJSONString();
        }
        List<Course> subList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), new TR<List<Course>>() {
        });
        List<Json> result = new ArrayList<>();
        subList.forEach(e -> {
            Json data = new Json();
            data.put("id", e.getId());
            data.put("name", e.getSubjectName());
            result.add(data);
        });
        json.put("infolist", result);
        return json.toJSONString();
    }


}
