package net.zdsoft.qulity.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.comprehensive.remote.service.ComStatisticsRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.qulity.data.constant.QualityConstants;
import net.zdsoft.qulity.data.dto.StuPdfDto;
import net.zdsoft.qulity.data.dto.StuworkDataCountDto;
import net.zdsoft.qulity.data.entity.QualityParam;
import net.zdsoft.qulity.data.service.QualityParamService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author niuchao
 * @date 2019/10/14 11:23
 */

@Controller
@RequestMapping("/quality/common")
public class QualityCommonAction extends BaseAction {

    /**
     * "XNXQ";//学年学期 "XKCJ";//学科成绩 "YYBS";//英语笔试 "YYKS";//英语口试 "TYCJ";//体育成绩
     */
    public static final String key_1 = "XNXQ";
    public static final String key_2 = "XKCJ";
    public static final String key_3 = "YYBS";
    public static final String key_4 = "YYKS";
    public static final String key_5 = "TYCJ";

    String[] paramCodes = { QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER, QualityConstants.QULITY_CXDD_MAX_NUMBER,
            QualityConstants.QULITY_JX_MAX_NUMBER, QualityConstants.QULITY_XN_MAX_NUMBER,
            QualityConstants.QULITY_PYXJ_MAX_NUMBER, QualityConstants.QULITY_QXXHD_MAX_NUMBER,
            QualityConstants.QULITY_STGG_MAX_NUMBER, QualityConstants.QULITY_TCGX_MAX_NUMBER,
            QualityConstants.QULITY_TYCJ_MAX_NUMBER, QualityConstants.QULITY_XKCJ_MAX_NUMBER,
            QualityConstants.QULITY_XKJS_MAX_NUMBER, QualityConstants.QULITY_XSGB_MAX_NUMBER,
            QualityConstants.QULITY_YYBS_MAX_NUMBER, QualityConstants.QULITY_YYKS_MAX_NUMBER,
            QualityConstants.QULITY_ZZBX_MAX_NUMBER };

    @Autowired
    StuworkRemoteService stuworkRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    StudentRemoteService studentRemoteService;
    @Autowired
    QualityParamService qualityParamService;
    @Autowired
    ComStatisticsRemoteService comStatisticsRemoteService;
    @Autowired
    CourseRemoteService courseRemoteService;

    @ResponseBody
    @RequestMapping("/getClassList")
    public String getClassList(ModelMap map, HttpServletRequest request) {
        String gradeId=request.getParameter("gradeId");
        String unitId=request.getParameter("unitId");
        String userId=request.getParameter("userId");
        JSONObject JsonData=new JSONObject();
        List<Clazz> clazzList= SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId),new TR<List<Clazz>>(){});
        //查询该用户的班级权限(班主任有班级权限，年级组长有年级下所有班级权限)
        Set<String> classIds=stuworkRemoteService.findClassSetByUserId(userId);
        JSONArray array=new JSONArray();
        if(CollectionUtils.isNotEmpty(clazzList)){
            JSONObject json=null;
            for (Clazz clazz : clazzList) {
                if(classIds.contains(clazz.getId())){
                    json=new JSONObject();
                    json.put("id", clazz.getId());
                    json.put("className", clazz.getClassNameDynamic());
                    array.add(json);
                }
            }
        }
        JsonData.put("array", array);
        return JsonData.toJSONString();
    }
    /**
     * 根据用户获取对应的年级
     * @param userId
     * @return
     */
    public List<Grade> getGradeList(String userId){
        //查询该用户的班级权限(班主任有班级权限，年级组长有年级下所有班级权限)
        Set<String> classIds=stuworkRemoteService.findClassSetByUserId(userId);
        List<Clazz> clazzs = SUtils.dt(classRemoteService
                        .findClassListByIds(classIds.toArray(new String[0])),
                new TR<List<Clazz>>() {
                });
        Set<String> gradeIds = EntityUtils.getSet(clazzs, Clazz::getGradeId);
        List<Grade> returngrade = Lists.newArrayList();
        if (gradeIds.size() > 0) {
            List<Grade> grades = SUtils.dt(gradeRemoteService
                            .findListByIds(gradeIds.toArray(new String[0])),
                    new TR<List<Grade>>() {
                    });
            for (Grade grade : grades) {
                if (grade.getSection() > 2) {
                    returngrade.add(grade);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(returngrade)) {
            Collections.sort(returngrade, new Comparator<Grade>() {
                @Override
                public int compare(Grade o1, Grade o2) {
                    return o1.getGradeCode().compareTo(o2.getGradeCode());
                }
            });
        }
        return returngrade;
    }

    public Set<String> getClassIds(String classId,String gradeId){
        LoginInfo login=getLoginInfo();
        Set<String> classIds = Sets.newHashSet();
        if (StringUtils.isNotBlank(classId)) {
            classIds.add(classId);
        } else if (StringUtils.isNotBlank(gradeId)) {
            List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdGradeId(login.getUnitId(), gradeId), new TR<List<Clazz>>(){});
            for (Clazz clazz : clazzs) {
                classIds.add(clazz.getId());
            }
        }
        Set<String> classIds2 = stuworkRemoteService.findClassSetByUserId(login.getUserId());
        classIds.retainAll(classIds2);
        return classIds;
    }

    public void sortStuList(List<Student> studentList, Map<String,Clazz> clazzMap){
        for (Student student : studentList) {
            if(clazzMap.containsKey(student.getClassId())){
                student.setClassInnerCode(clazzMap.get(student.getClassId()).getClassCode());
                student.setClassName(clazzMap.get(student.getClassId()).getClassNameDynamic());
            }
        }
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if(StringUtils.isNotBlank(o1.getClassInnerCode()) && StringUtils.isNotBlank(o2.getClassInnerCode())){
                    if(o1.getClassInnerCode().compareTo(o2.getClassInnerCode())==0){
                        return o1.getIdentityCard().compareTo(o2.getIdentityCard());
                    }
                    return o1.getClassInnerCode().compareTo(o2.getClassInnerCode());
                }
                return 0;
            }
        });
    }

    @RequestMapping("/class/pdfHtml")
    public String scoreClassPdfHtml(String classId, ModelMap map) {
        int graduateYear = 0;
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
                Clazz.class);
        boolean isShow =false;
        if (clazz != null) {
            isShow = qualityParamService.findIsShowByGradeId(clazz.getSchoolId(), clazz.getGradeId());
            if (StringUtils.isNotBlank(clazz.getAcadyear())) {
                int beginYear = Integer.parseInt(clazz.getAcadyear()
                        .substring(2, 4));
                graduateYear = beginYear + clazz.getSchoolingLength();
            }
        }
        List<QualityParam> params = qualityParamService
                .findByUnitId(clazz.getSchoolId(), false);
        if (CollectionUtils.isEmpty(params)) {
            params = Lists.newArrayList();
            for (String paramCode : paramCodes) {
                QualityParam qulityParam = new QualityParam();
                qulityParam.setParamType(paramCode);
                params.add(qulityParam);
            }
        }
        Integer maxValuePer = 0;
        for (QualityParam param : params) {
            if (QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER.equals(param
                    .getParamType())) {
                maxValuePer = param.getParamPer();
            }
        }
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
        studentList.forEach(e->e.setClassName(clazz.getClassNameDynamic()));
        String[] studentIds = EntityUtils.getList(studentList, e->e.getId()).toArray(new String[0]);
        Map<String, Integer> maxValueMap = EntityUtils.getMap(params,
                "paramType", "param");
        Map<String, StuworkDataCountDto> countDtoMap = SUtils.dt(stuworkRemoteService
                .findStuworkCountByStudentIds(maxValueMap, classId, studentIds,
                        maxValuePer, isShow), new TR<Map<String, StuworkDataCountDto>>(){});

        Map<String, Map<String, String[]>> stuCulturalMap = comStatisticsRemoteService
                .findStatisticsByStudentIds(classId, studentIds, maxValueMap);
        /***
         * 页面显示学考成绩 key:subjectId
         */
        Map<String, Map<String, String>> stuXkMap = comStatisticsRemoteService
                .findXKStatisticsByStudentIds(clazz.getSchoolId(), studentIds);
        Set<String> courseIdSet = new HashSet<String>();
        stuXkMap.entrySet().forEach(e->{
            courseIdSet.addAll(e.getValue().keySet());
        });
        List<Course> courseList = SUtils.dt(courseRemoteService
                .orderCourse(courseRemoteService.findListByIds(courseIdSet
                        .toArray(new String[0]))), new TR<List<Course>>() {
        });

        List<StuPdfDto> dtoList = new ArrayList<>();
        StuPdfDto dto = null;
        for (Student student : studentList) {
            String studentId= student.getId();
            Map<String, String> xkMap = stuXkMap.get(studentId);
            StringBuffer xkResult = new StringBuffer();
            for (Course one : courseList) {
                if (xkMap.get(one.getId()) != null) {
                    xkResult.append(";" + one.getSubjectName() + ":"
                            + xkMap.get(one.getId()));
                }
            }
            Float totalScore = new Float(0);
            Float dyScore = new Float(0);
            Float myScore = new Float(0);
            Float xkjsScore = new Float(0);
            List<String[]> dyList = Lists.newArrayList();
            List<String[]> myList = Lists.newArrayList();
            List<String[]> xkjsList = Lists.newArrayList();
            String[] acadyears = new String[4];
            StuworkDataCountDto countDto = countDtoMap.get(studentId);
            if (countDto != null) {
                Float[] countNumbers = countDto.getCountNumbers();
                int index = 0;
                for (Float num : countNumbers) {
                    if (num == null) {
                        index++;
                        continue;
                    }
                    if (index == 0) {
                        dyScore = num;
                    }
                    if (index == 1) {
                        myScore = num;
                    }
                    if (index == 2) {
                        xkjsScore = num;
                    }
                    totalScore = totalScore + num;
                    index++;
                }

                acadyears = countDto.getAcadyears();
                Map<String, List<String[]>> infoMap = countDto.getInfoMap();
                if (infoMap.containsKey(StuworkDataCountDto.STUWORK_LIST)) {
                    dyList = infoMap.get(StuworkDataCountDto.STUWORK_LIST);
                }
                if (infoMap.containsKey(StuworkDataCountDto.FESTIVAL_LIST)) {
                    myList = infoMap.get(StuworkDataCountDto.FESTIVAL_LIST);
                }
                if (infoMap.containsKey(StuworkDataCountDto.GAME_LIST)) {
                    xkjsList = infoMap.get(StuworkDataCountDto.GAME_LIST);
                }
            }
            Map<String, String[]> culturalMap = stuCulturalMap.get(studentId);
            if (culturalMap == null) {
                culturalMap = Maps.newHashMap();
                culturalMap.put(key_1, new String[] { "", "", "", "", "", "" });
                culturalMap.put(key_2, new String[] { "", "", "", "", "", "", "",
                        "", "", "", "", "", "", "", "", "", "", "", "" });
                culturalMap.put(key_3, new String[] { "", "", "", "", "", "", "",
                        "", "", "", "", "", "", "", "", "", "", "", "" });
                culturalMap.put(key_4, new String[] { "", "", "", "", "", "", "",
                        "", "", "", "", "", "", "", "", "", "", "", "" });
                culturalMap.put(key_5, new String[] { "", "", "", "", "", "", "",
                        "", "", "", "", "", "", "", "" });
            }
            String[] xnxq = culturalMap.get(key_1);
            String[] xkcj = culturalMap.get(key_2);
            String[] yybs = culturalMap.get(key_3);
            String[] yyks = culturalMap.get(key_4);
            String[] tycj = culturalMap.get(key_5);
            if (StringUtils.isNotBlank(xkcj[0])) {
                if(!isShow && StringUtils.isNotBlank(xkcj[3])){
                    xkcj[0] = (Float.valueOf(xkcj[0]) - Float.valueOf(xkcj[3]))+"";
                }
                totalScore = totalScore + Float.valueOf(xkcj[0]);
            }
            if (StringUtils.isNotBlank(yybs[0])) {
                if(!isShow && StringUtils.isNotBlank(yybs[3])){
                    yybs[0] = (Float.valueOf(yybs[0]) - Float.valueOf(yybs[3]))+"";
                }
                totalScore = totalScore + Float.valueOf(yybs[0]);
            }
            if (StringUtils.isNotBlank(yyks[0])) {
                if(!isShow && StringUtils.isNotBlank(yyks[3])){
                    yyks[0] = (Float.valueOf(yyks[0]) - Float.valueOf(yyks[3]))+"";
                }
                totalScore = totalScore + Float.valueOf(yyks[0]);
            }
            if (StringUtils.isNotBlank(tycj[0])) {
                if(!isShow && StringUtils.isNotBlank(tycj[3])){
                    tycj[0] = (Float.valueOf(tycj[0]) - Float.valueOf(tycj[3]))+"";
                }
                totalScore = totalScore + Float.valueOf(tycj[0]);
            }
            if (StringUtils.isNotBlank(xkMap.get(BaseConstants.ZERO_GUID))) {
                totalScore = totalScore
                        + Float.valueOf(xkMap.get(BaseConstants.ZERO_GUID));
            }

            dto = new StuPdfDto();
            dto.setStudentId(student.getId());
            dto.setStudentCode(student.getStudentCode());
            dto.setStudentName(student.getStudentName());
            dto.setSex(student.getSex());
            dto.setClassName(student.getClassName());
            dto.setTotalScore(totalScore);

            dto.setXnxq(xnxq);
            dto.setXkcj(xkcj);
            dto.setYybs(yybs);
            dto.setYyks(yyks);

            dto.setAcadyears(acadyears);
            dto.setXkjsScore(xkjsScore);
            dto.setXkjsList(xkjsList);
            dto.setXkScore(Float.parseFloat(xkMap.get(BaseConstants.ZERO_GUID)));
            if(xkResult.length() != 0){
                dto.setXkResult(xkResult.substring(1));
            }

            dto.setDyScore(dyScore);
            dto.setDyList(dyList);

            dto.setTycj(tycj);
            dto.setMyScore(myScore);
            dto.setMyList(myList);

            dtoList.add(dto);
        }
        map.put("isShow", isShow);
        map.put("dtoList", dtoList);
        map.put("graduateYear", graduateYear);
        return "/quality/score/htmlQualityScoreClazz.ftl";
    }

    @RequestMapping("/student/pdfHtml")
    public String scoreStudentPdfHtml(String studentId, ModelMap map){
        Student student = SUtils.dc(
                studentRemoteService.findOneById(studentId), Student.class);
        String classId = student.getClassId();
        int graduateYear = 0;
        boolean isShow = false;
        if (StringUtils.isNotBlank(classId)) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
                    Clazz.class);
            isShow = qualityParamService.findIsShowByGradeId(student.getSchoolId(),clazz.getGradeId());
            if (clazz != null) {
                student.setClassName(clazz.getClassNameDynamic());
                if (StringUtils.isNotBlank(clazz.getAcadyear())) {
                    int beginYear = Integer.parseInt(clazz.getAcadyear()
                            .substring(2, 4));
                    graduateYear = beginYear + clazz.getSchoolingLength();
                }
            }
        }

        List<QualityParam> params = qualityParamService
                .findByUnitId(student.getSchoolId(), false);
        if (CollectionUtils.isEmpty(params)) {
            params = Lists.newArrayList();
            for (String paramCode : paramCodes) {
                QualityParam qulityParam = new QualityParam();
                qulityParam.setParamType(paramCode);
                params.add(qulityParam);
            }
        }
        Integer maxValuePer = 0;
        for (QualityParam param : params) {
            if (QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER.equals(param
                    .getParamType())) {
                maxValuePer = param.getParamPer();
            }
        }
        Map<String, Integer> maxValueMap = EntityUtils.getMap(params,
                "paramType", "param");
        StuworkDataCountDto countDto = SUtils.dc(stuworkRemoteService
                .findStuworkCountByStudentId(maxValueMap, studentId,
                        maxValuePer, isShow), StuworkDataCountDto.class);
        Map<String, String[]> culturalMap = comStatisticsRemoteService
                .findStatisticsByStudentId(studentId, maxValueMap);
        /***
         * 页面显示学考成绩 key:subjectId
         */
        Map<String, String> xkMap = comStatisticsRemoteService
                .findXKStatisticsByStudentId(studentId);
        List<Course> courseList = SUtils.dt(courseRemoteService
                .orderCourse(courseRemoteService.findListByIds(xkMap.keySet()
                        .toArray(new String[0]))), new TR<List<Course>>() {
        });
        StringBuffer xkResult = new StringBuffer();
        for (Course one : courseList) {
            if (xkMap.get(one.getId()) != null) {
                xkResult.append(";" + one.getSubjectName() + ":"
                        + xkMap.get(one.getId()));
            }
        }

        Float totalScore = new Float(0);
        Float dyScore = new Float(0);
        Float myScore = new Float(0);
        Float xkjsScore = new Float(0);
        List<String[]> dyList = Lists.newArrayList();
        List<String[]> myList = Lists.newArrayList();
        List<String[]> xkjsList = Lists.newArrayList();

        String[] acadyears = new String[4];
        if (countDto != null) {
            Float[] countNumbers = countDto.getCountNumbers();
            int index = 0;
            for (Float num : countNumbers) {
                if (num == null) {
                    index++;
                    continue;
                }
                if (index == 0) {
                    dyScore = num;
                }
                if (index == 1) {
                    myScore = num;
                }
                if (index == 2) {
                    xkjsScore = num;
                }
                totalScore = totalScore + num;
                index++;
            }

            acadyears = countDto.getAcadyears();
            Map<String, List<String[]>> infoMap = countDto.getInfoMap();
            if (infoMap.containsKey(StuworkDataCountDto.STUWORK_LIST)) {
                dyList = infoMap.get(StuworkDataCountDto.STUWORK_LIST);
            }
            if (infoMap.containsKey(StuworkDataCountDto.FESTIVAL_LIST)) {
                myList = infoMap.get(StuworkDataCountDto.FESTIVAL_LIST);
            }
            if (infoMap.containsKey(StuworkDataCountDto.GAME_LIST)) {
                xkjsList = infoMap.get(StuworkDataCountDto.GAME_LIST);
            }
        }
        if (culturalMap == null) {
            culturalMap = Maps.newHashMap();
            culturalMap.put(key_1, new String[] { "", "", "", "", "", "" });
            culturalMap.put(key_2, new String[] { "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "" });
            culturalMap.put(key_3, new String[] { "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "" });
            culturalMap.put(key_4, new String[] { "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "" });
            culturalMap.put(key_5, new String[] { "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "" });
        }
        String[] xnxq = culturalMap.get(key_1);
        String[] xkcj = culturalMap.get(key_2);
        String[] yybs = culturalMap.get(key_3);
        String[] yyks = culturalMap.get(key_4);
        String[] tycj = culturalMap.get(key_5);
        if (StringUtils.isNotBlank(xkcj[0])) {
            if(!isShow && StringUtils.isNotBlank(xkcj[3])){
                xkcj[0] = (Float.valueOf(xkcj[0]) - Float.valueOf(xkcj[3]))+"";
            }
            totalScore = totalScore + Float.valueOf(xkcj[0]);
        }
        if (StringUtils.isNotBlank(yybs[0])) {
            if(!isShow && StringUtils.isNotBlank(yybs[3])){
                yybs[0] = (Float.valueOf(yybs[0]) - Float.valueOf(yybs[3]))+"";
            }
            totalScore = totalScore + Float.valueOf(yybs[0]);
        }
        if (StringUtils.isNotBlank(yyks[0])) {
            if(!isShow && StringUtils.isNotBlank(yyks[3])){
                yyks[0] = (Float.valueOf(yyks[0]) - Float.valueOf(yyks[3]))+"";
            }
            totalScore = totalScore + Float.valueOf(yyks[0]);
        }
        if (StringUtils.isNotBlank(tycj[0])) {
            if(!isShow && StringUtils.isNotBlank(tycj[3])){
                tycj[0] = (Float.valueOf(tycj[0]) - Float.valueOf(tycj[3]))+"";
            }
            totalScore = totalScore + Float.valueOf(tycj[0]);
        }
        if (StringUtils.isNotBlank(xkMap.get(BaseConstants.ZERO_GUID))) {
            totalScore = totalScore
                    + Float.valueOf(xkMap.get(BaseConstants.ZERO_GUID));
        }
        map.put("isShow", isShow);
        map.put("graduateYear", graduateYear);
        map.put("student", student);
        map.put("totalScore", totalScore);
        // 学考
        map.put("courseList", courseList);
        map.put("xkMap", xkMap);
        map.put("xkScore", xkMap.get(BaseConstants.ZERO_GUID));
        if (xkResult.length() != 0) {
            map.put("xkResult", xkResult.substring(1));
        }
        // 德育、美育
        map.put("dyScore", dyScore);
        map.put("myScore", myScore);
        map.put("xkjsScore", xkjsScore);
        map.put("acadyears", acadyears);
        map.put("dyList", dyList);
        map.put("myList", myList);
        map.put("xkjsList", xkjsList);
        map.put("xnxq", xnxq);
        map.put("xkcj", xkcj);
        map.put("yybs", yybs);
        map.put("yyks", yyks);
        map.put("tycj", tycj);
        return "/quality/score/htmlQualityScoreStudent.ftl";
    }
}
