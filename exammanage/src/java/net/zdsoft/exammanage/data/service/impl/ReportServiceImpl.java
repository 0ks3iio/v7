package net.zdsoft.exammanage.data.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.ExamSubjectDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.exammanage.data.constant.ExammanageConstants.*;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmSpaceItemService emSpaceItemService;
    @Autowired
    private EmStatSpaceService emStatSpaceService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private EmConversionService emConversionService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;

    @Override
    public String getAllScoreByStudentId(String studentId, String unitId, String examId) {
        // exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null)
            return "";
        List<EmStat> emStatList = emStatService.findByExamIdAndStudentId(emStatObject.getId(), examId, studentId);
        Map<String, EmStat> subjectIdStatMap = new HashMap<>();
        for (EmStat emStat : emStatList) {
            if (!emStat.getSubType().equals("1")) {
                subjectIdStatMap.put(emStat.getSubjectId(), emStat);
            }
        }
//		subjectIdStatMap=emStatList.stream().collect(Collectors.toMap(EmStat::getSubjectId, Function.identity()));
        Set<String> subjectIds = emStatList.stream().map(EmStat::getSubjectId).collect(Collectors.toSet());
        //获取所有科目
        List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})),
                new TR<List<Course>>() {
                });
        Collections.sort(courses, (o1, o2) -> {
            return o1.getOrderId() - o2.getOrderId();
        });
        List<EmExamInfo> examList = queryExamsByStudentId(studentId, null, null, null);
        String beforeExamId = null;
        int length = examList.size();//获取上一级考试id
        for (int i = 0; i < length; i++) {
            if (examList.get(i).getId().equals(examId) && (i + 1) < length) {
                beforeExamId = examList.get(i + 1).getId();
            }
        }
        EmStat beforeEmStat = null;
        if (StringUtils.isNotBlank(beforeExamId)) {
            EmStatObject beforeStatObject = emStatObjectService.findByUnitIdExamId(unitId, beforeExamId);
            beforeEmStat = emStatService.findByExamIdAndSubjectIdAndStudentId(beforeStatObject.getId(), beforeExamId, BaseConstants.ZERO_GUID, studentId);
        }
        JSONObject json = new JSONObject();
        EmStat nowEmStat = subjectIdStatMap.get(BaseConstants.ZERO_GUID);
        if (nowEmStat == null)
            return "";
        BigDecimal b = new BigDecimal(nowEmStat.getScore());
        json.put("score", b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());//总分
        json.put("classRank", nowEmStat.getClassRank());//班级排名
        json.put("classUpRank", beforeEmStat == null ? 0 : beforeEmStat.getClassRank() - nowEmStat.getClassRank());//班级提升名次
        json.put("gradeRank", nowEmStat.getGradeRank());//年级排名
        json.put("gradeUpRank", beforeEmStat == null ? 0 : beforeEmStat.getGradeRank() - nowEmStat.getGradeRank());//年级提升名次
        JSONArray array = new JSONArray();
        JSONObject info = null;
        for (Course course : courses) {
            info = new JSONObject();
            info.put("subjectName", course.getSubjectName());//科目名称
            EmStat emStat = subjectIdStatMap.get(course.getId());
            if (emStat != null) {
                info.put("subjectScore", emStat.getScore());//科目成绩
            }
            array.add(info);
        }
        json.put("infolist", array);
        return json.toJSONString();
    }

    @Override
    public String getStuAllExamScore(String unitId, String acadyear, String semester, String studentId) {
        // exammanage_stat_object 统计对象
        List<EmStatObject> emStatObjects = emStatObjectService.findByUnitId(unitId);

        if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester))
            return "";
        List<EmExamInfo> examList = queryExamsByStudentId(studentId, acadyear, semester, null);
        if (CollectionUtils.isEmpty(examList))
            return "";
        Collections.sort(examList, (o1, o2) -> {
            if (o1.getExamStartDate().compareTo(o2.getExamStartDate()) == 0) {
                return o1.getCreationTime().compareTo(o2.getCreationTime());
            }
            return o1.getExamStartDate().compareTo(o2.getExamStartDate());
        });
        Set<String> examIds = examList.stream().map(EmExamInfo::getId).collect(Collectors.toSet());
        List<EmStat> emStatList = emStatService.findByObjectIdsExamIdIn(emStatObjects.stream().map(EmStatObject::getId).collect(Collectors.toSet()).toArray(new String[]{})
                , examIds.toArray(new String[]{}), BaseConstants.ZERO_GUID, studentId);
        Map<String, EmStat> emStatMap = emStatList.stream().collect(Collectors.toMap(EmStat::getExamId, Function.identity()));
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject info = null;
        for (EmExamInfo exam : examList) {
            info = new JSONObject();
            info.put("examName", exam.getExamName());//考试名称
            EmStat emStat = emStatMap.get(exam.getId());
            if (emStat != null) {
                info.put("classRank", emStat.getClassRank());//班级排名
                info.put("gradeRank", emStat.getGradeRank());//年级排名
            }
            array.add(info);
        }
        json.put("infolist", array);
        return json.toJSONString();
    }

    @Override
    public String getStuAllExamRank(String unitId, String acadyear, String semester, String studentId) {
        // exammanage_stat_object 统计对象
        List<EmStatObject> emStatObjects = emStatObjectService.findByUnitId(unitId);
        if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester))
            return "";
        List<EmExamInfo> examList = queryExamsByStudentId(studentId, acadyear, semester, null);
        if (CollectionUtils.isEmpty(examList))
            return "";
        Collections.sort(examList, (o1, o2) -> {
            if (o1.getExamStartDate().compareTo(o2.getExamStartDate()) == 0) {
                return o1.getCreationTime().compareTo(o2.getCreationTime());
            }
            return o1.getExamStartDate().compareTo(o2.getExamStartDate());
        });
        Set<String> examIds = examList.stream().map(EmExamInfo::getId).collect(Collectors.toSet());
        List<EmStat> emStatList = emStatService.findByObjectIdsExamIdIn(emStatObjects.stream().map(EmStatObject::getId).collect(Collectors.toSet()).toArray(new String[]{})
                , examIds.toArray(new String[]{}), null, studentId);
        Map<String, EmStat> emStatMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(emStatList)) {
            for (EmStat emStat : emStatList) {
                if (!emStat.getSubType().equals("1")) {
                    emStatMap.put(emStat.getExamId() + emStat.getSubjectId(), emStat);
                }
            }
        }
        //获取所有科目
        List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(emStatList.stream().map(EmStat::getSubjectId).collect(Collectors.toSet()).toArray(new String[]{})),
                new TR<List<Course>>() {
                });
        Collections.sort(courses, (o1, o2) -> {
            return o1.getOrderId() - o2.getOrderId();
        });
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject info = null;
        for (EmExamInfo exam : examList) {
            info = new JSONObject();
            info.put("examName", exam.getExamName());//考试名称
            JSONArray arrayIn = new JSONArray();
            JSONObject infoIn = null;
            for (Course course : courses) {
                EmStat emStat = emStatMap.get(exam.getId() + course.getId());
                infoIn = new JSONObject();
                infoIn.put("subjectName", course.getSubjectName());//科目名称
                infoIn.put("subjecGradeRank", emStat == null ? "" : emStat.getGradeRank());//科目年级排名
                arrayIn.add(infoIn);
            }
            info.put("infolistIn", arrayIn);
            array.add(info);
        }
        json.put("infolist", array);
        return json.toJSONString();
    }

    @Override
    public List<EmExamInfo> queryExamsByStudentId(String studentId, String acadyear, String semester, String conType) {
        List<EmStat> emStatList = emStatService.findByStudentId(studentId);
        boolean flag = "2".equals(conType);//图表赋分 只查询高考模式
        Set<String> examIds = emStatList.stream().map(EmStat::getExamId).collect(Collectors.toSet());
        List<EmExamInfo> examList = emExamInfoService.findBySemesterAndIdIn(examIds.toArray(new String[]{}), null);
        List<EmExamInfo> lastList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examList)) {
            Collections.sort(examList, (o1, o2) -> {
                if (o2.getExamStartDate().compareTo(o1.getExamStartDate()) == 0) {
                    return o2.getCreationTime().compareTo(o1.getCreationTime());
                }
                return o2.getExamStartDate().compareTo(o1.getExamStartDate());
            });
            if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
                if (flag) {
                    for (EmExamInfo exam : examList) {
                        if ("1".equals(exam.getIsgkExamType()))
                            lastList.add(exam);//只获取高考模式
                    }
                } else
                    lastList.addAll(examList);
                Collections.sort(lastList, (o1, o2) -> {
                    if (o1.getExamStartDate().compareTo(o2.getExamStartDate()) == 0) {
                        return o1.getCreationTime().compareTo(o2.getCreationTime());
                    }
                    return o1.getExamStartDate().compareTo(o2.getExamStartDate());
                });
                return lastList;
            } else {
                for (EmExamInfo exam : examList) {//判断学年学期  当前台学期传的是3时：代表取整个学年的数据
                    if (exam.getAcadyear().equals(acadyear) && ("3".equals(semester) || exam.getSemester().equals(semester))) {
                        if (flag && "1".equals(exam.getIsgkExamType())) {
                            lastList.add(exam);//只获取高考模式
                        } else
                            lastList.add(exam);
                    }
                }
            }
			/*Collections.sort(examList, (o1,o2)->{
				if(o1.getExamStartDate().compareTo(o2.getExamStartDate())==0){
					return o1.getCreationTime().compareTo(o2.getCreationTime());
				}
				return o1.getExamStartDate().compareTo(o2.getExamStartDate());
			});*/
        }
        return lastList;
    }

    @Override
    public String getClassScoreRank(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String classId, String subType) {
        boolean is73Sub = false;
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            is73Sub = true;
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null)
            return null;
        //exammanage_stat 学生统计
        List<EmStat> statList = emStatService.findByClassIdAndSubId(emStatObject.getId(), examId, classId, subjectId, "1".equals(subType) ? "3" : subType);
        //Map<String,EmStat> statMap=statList.stream().collect(Collectors.toMap(EmStat::getStudentId, Function.identity()));
        String[] stuIds = statList.stream().map(EmStat::getStudentId).collect(Collectors.toSet()).toArray(new String[]{});
        Map<String, Clazz> clazzMap = SUtils.dt(classRemoteService.findBySchoolId(unitId), new TR<List<Clazz>>() {
        }).stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));

        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        //获取
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds), new TR<List<Student>>() {
        });
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        //subType为0时，默认是非7选3科目
        String[] names = null;
        names = is73Sub ? new String[]{"考号", "行政班级", "学生", "考试分", "赋分", "年级排名", "班级排名"}
                : new String[]{"考号", "行政班级", "学生", "考试分", "年级排名", "班级排名"};
        List<EmStatCrossEntity> crossList = new ArrayList<>();
        EmStatCrossEntity cross = null;
        Student stu = null;
        EmStat inStat = null;
        for (int i = 0; i < statList.size(); i++) {
            inStat = statList.get(i);
            stu = studentMap.get(inStat.getStudentId());
            if (stu == null)
                continue;
//			EmStat inStat=statMap.get(stu.getId());
            for (int j = 0; j < names.length; j++) {
                cross = new EmStatCrossEntity();
                cross.setColumnName(names[j]);//列名
                cross.setRowName(String.valueOf(i + 1));//行名
                cross.setColumnOrder(String.valueOf(j + 1));//列排序号
                cross.setRowOrder(String.valueOf(i + 1));//行排序号
                switch (names[j]) {
                    case "考号":
                        cross.setValue(examNumMap.get(stu.getId()));
                        break;
                    case "行政班级":
                        cross.setValue(clazzMap.get(stu.getClassId()).getClassNameDynamic());
                        break;
                    case "学生":
                        cross.setValue(stu.getStudentName());
                        break;
                    case "考试分":
                        cross.setValue(getString(inStat.getScore()));
                        break;
                    case "赋分":
                        cross.setValue(getString(inStat.getConScore()));
                        break;
                    case "年级排名":
                        cross.setValue(getString(inStat.getGradeRank()));
                        break;
                    case "班级排名":
                        cross.setValue(getString(inStat.getClassRank()));
                        break;
                    default:
                        break;
                }
                crossList.add(cross);
            }
        }
        EmStatRange statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeSubType(emStatObject.getId(),
                examId, subjectId, classId, EmStatRange.RANGE_CLASS, subType);
        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        String className = getClassNameById(classId);
        head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, null, subjectName, classId, className, "  考试排名表"));
        head.setHeader("科目:" + subjectName + "   教学班:" + className);
        if (statRange != null) {
            head.setSummary("班级平均分:" + statRange.getAvgScore() + "分   班级考试最高分:" + statRange.getMaxScore() + "分      班级考试最低分:" + statRange.getMinScore() + "分");
        }
        head.setColumnHeader("序号");
        json.put("infolist", crossList);
        json.put("params", head);
        return json.toJSONString();
    }

    @Override
    public String getClassScoreRankBySelf(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String classId, String subType) {
        boolean is73Sub = false;
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            is73Sub = true;
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null)
            return null;
        //exammanage_stat 学生统计
        List<EmStat> statList = new ArrayList<>();
        String[] stuIds = null;
        subType = "1".equals(subType) ? "3" : subType;
//		Map<String,String> classNameMap=new HashMap<>();
        if (StringUtils.isBlank(classId)) {//此为考务自己报表
            gradeCode = StringUtils.isNotBlank(gradeCode) ? gradeCode : emExamInfoService.findOne(examId).getGradeCodes();
			/*List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,gradeCode),new TR<List<Grade>>(){});
			if(CollectionUtils.isNotEmpty(gradeList)){
	        	List<Clazz> clazzList=SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeList.get(0).getId()),new TR<List<Clazz>>(){});
	        	classNameMap=clazzList.stream().collect(Collectors.toMap(Clazz::getId, Clazz::getClassNameDynamic));
	        	Set<String> classIds=clazzList.stream().map(Clazz::getId).collect(Collectors.toSet());
	        	stuIds=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})),new TR<List<Student>>(){}).stream().map(Student::getId).collect(Collectors.toSet()).toArray(new String[]{});
	        }*/
            statList = emStatService.findByAllExamIdAndObjIdAndSubjectId(examId, subjectId, emStatObject.getId());
        } else {
            statList = emStatService.findByClassIdAndSubId(emStatObject.getId(), examId, classId, subjectId, "1".equals(subType) ? "3" : subType);
        }
        stuIds = statList.stream().map(EmStat::getStudentId).collect(Collectors.toSet()).toArray(new String[]{});
        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        //获取
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds), new TR<List<Student>>() {
        });
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        Student stu = null;
        JSONObject json = new JSONObject();
        List<EmStat> lastList = new ArrayList<>();
        for (EmStat inStat : statList) {
            if (StringUtils.isBlank(classId) && !StringUtils.equals(subType, inStat.getSubType())) {
                continue;
            }
            stu = studentMap.get(inStat.getStudentId());
            if (stu != null) {
                inStat.setStudentName(stu.getStudentName());
                inStat.setStudentCode(stu.getStudentCode());
                inStat.setExamNum(examNumMap.get(stu.getId()));
                inStat.setStudentCode(stu.getStudentCode());
//				String className=classNameMap.get(stu.getClassId());
//				if(StringUtils.isNotBlank(className))
//					inStat.setClassName(className);
            }
            if (inStat.getScoreT() != null) {
                BigDecimal b1 = new BigDecimal(inStat.getScoreT());
                inStat.setScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }
            lastList.add(inStat);
        }
        Collections.sort(lastList, (o1, o2) -> {
            return o1.getGradeRank() - o2.getGradeRank();
        });
        if (StringUtils.isBlank(classId)) {
            EmStatRange statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeSubType(emStatObject.getId(),
                    examId, subjectId, unitId, EmStatRange.RANGE_SCHOOL, subType);
            if (statRange != null) {
                json.put("summary", "年级考试平均分:" + statRange.getAvgScore() + "分              年级考试最高分:" + statRange.getMaxScore() + "分                年级考试最低分:" + statRange.getMinScore() + "分");
            }
        } else {
            EmStatRange statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeSubType(emStatObject.getId(),
                    examId, subjectId, classId, EmStatRange.RANGE_CLASS, subType);
            if (statRange != null) {
                json.put("summary", "班级平均分:" + statRange.getAvgScore() + "分       班级考试最高分:" + statRange.getMaxScore() + "分          班级考试最低分:" + statRange.getMinScore() + "分");
            }
        }
        json.put("title", getTitle(unitId, acadyear, semester, examId, gradeCode, null, subjectName, null, null, "  考试排名表"));
        json.put("statList", lastList);
        json.put("is73Sub", is73Sub);
        return json.toJSONString();
    }

    @Override
    public String getClassSubRankBySelf(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        boolean is73Sub = false;
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            is73Sub = true;
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //exammanage_stat_range 统计结果
        if (emStatObject == null) {
            return "";
        }
        List<EmStatRange> statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, subType);
        Set<String> rangeIds = statArrangeList.stream().map(EmStatRange::getRangeId).collect(Collectors.toSet());
        //基础数据
        Map<String, Clazz> clazzMap = SUtils.dt(classRemoteService.findListByIds(rangeIds.toArray(new String[]{})), new TR<List<Clazz>>() {
        }).stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
        Map<String, TeachClass> teachClassMap = SUtils.dt(teachClassRemoteService.findListByIds(rangeIds.toArray(new String[]{})), new TR<List<TeachClass>>() {
        }).stream().collect(Collectors.toMap(TeachClass::getId, Function.identity()));
        Map<String, Teacher> teacherMap = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        }).stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
        Map<String, Set<String>> teacherIdsMap = emSubjectInfoService.findTeacher(examId, subjectId, unitId, rangeIds, rangeIds);
        //subType为0时，默认是非7选3科目

        EmStatRange statRange = null;
        for (int i = 0; i < statArrangeList.size(); i++) {//TODO
            statRange = statArrangeList.get(i);
            String rangeName = getRangeName(statRange, clazzMap, teachClassMap);

            Set<String> teacherIdss = teacherIdsMap.get(statRange.getRangeId());
            StringBuilder teacherNames = new StringBuilder();
            if (CollectionUtils.isNotEmpty(teacherIdss)) {
                String[] teacherIds = teacherIdss.toArray(new String[]{});
                for (int l = 0; l < teacherIds.length; l++) {
                    if (teacherMap.containsKey(teacherIds[l])) {
                        teacherNames.append(l == 0 ? teacherMap.get(teacherIds[l]).getTeacherName() : "," + teacherMap.get(teacherIds[l]).getTeacherName());
                    }
                }
            }
            if (statRange.getAvgScoreT() != null) {
                BigDecimal b1 = new BigDecimal(statRange.getAvgScoreT());
                statRange.setAvgScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }
            statRange.setClassName(rangeName);
            statRange.setRangeName(rangeName);
            statRange.setSubjectName(subjectName);
            statRange.setTeacherName(teacherNames.toString());
        }
        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, "", subjectName, "", "", "各教学班  平均分排名表"));
        json.put("params", head);
        json.put("statArrangeList", statArrangeList);
        json.put("is73Sub", is73Sub);
        return json.toJSONString();
    }

    @Override
    public String getClassSubRank(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        boolean is73Sub = false;
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            is73Sub = true;
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //exammanage_stat_range 统计结果
        List<EmStatRange> statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, subType);
        String[] rangeIds = statArrangeList.stream().map(EmStatRange::getRangeId).collect(Collectors.toSet()).toArray(new String[]{});
        //基础数据
        Map<String, Clazz> clazzMap = SUtils.dt(classRemoteService.findListByIds(rangeIds), new TR<List<Clazz>>() {
        }).stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
        Map<String, TeachClass> teachClassMap = SUtils.dt(teachClassRemoteService.findListByIds(rangeIds), new TR<List<TeachClass>>() {
        }).stream().collect(Collectors.toMap(TeachClass::getId, Function.identity()));
        Map<String, Teacher> teacherMap = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        }).stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
        //subType为0时，默认是非7选3科目
        String[] names = null;
        names = is73Sub ? new String[]{"教学班级", "任课老师", "考试最高分", "考试最低分", "标准差", "考试平均分", "考试平均分排名", "赋分最高分", "赋分最低分", "赋分标准差", "赋分平均分", "赋分平均分排名"}
                : new String[]{"教学班级", "任课老师", "考试最高分", "考试最低分", "标准差", "考试平均分", "考试平均分排名"};

        EmStatRange statRange = null;
        List<EmStatCrossEntity> crossList = new ArrayList<>();
        EmStatCrossEntity cross = null;
        for (int i = 0; i < statArrangeList.size(); i++) {//TODO
            statRange = statArrangeList.get(i);
            String rangeName = getRangeName(statRange, clazzMap, teachClassMap);

            StringBuilder teacherNames = new StringBuilder();
            if (StringUtils.isNotBlank(statRange.getTeacherIds())) {
                String[] teacherIds = statRange.getTeacherIds().split(",");
                for (int l = 0; l < teacherIds.length; l++) {
                    if (teacherMap.containsKey(teacherIds[l])) {
                        teacherNames.append(l == 0 ? teacherMap.get(teacherIds[l]).getTeacherName() : "," + teacherMap.get(teacherIds[l]).getTeacherName());
                    }
                }
            }
            for (int j = 0; j < names.length; j++) {
                cross = new EmStatCrossEntity();
                cross.setColumnName(names[j]);//列名
                cross.setRowName(String.valueOf(i + 1));//行名
                cross.setColumnOrder(String.valueOf(j + 1));//列排序号
                cross.setRowOrder(String.valueOf(i + 1));//行排序号
                switch (names[j]) {
                    case "教学班级":
                        cross.setValue(rangeName);
                        break;
                    case "任课老师":
                        cross.setValue(teacherNames.toString());
                        break;
                    case "考试最高分":
                        cross.setValue(getString(statRange.getMaxScore()));
                        break;
                    case "考试最低分":
                        cross.setValue(getString(statRange.getMinScore()));
                        break;
                    case "标准差":
                        cross.setValue(getString(statRange.getNormDeviation()));
                        break;
                    case "考试平均分":
                        cross.setValue(getString(statRange.getAvgScore()));
                        break;
                    case "考试平均分排名":
                        cross.setValue(getString(statRange.getRank()));
                        break;
                    case "赋分最高分":
                        cross.setValue(getString(statRange.getConScoreUp()));
                        break;
                    case "赋分最低分":
                        cross.setValue(getString(statRange.getConScoreLow()));
                        break;
                    case "赋分标准差":
                        cross.setValue(getString(statRange.getConNormDeviation()));
                        break;
                    case "赋分平均分":
                        cross.setValue(getString(statRange.getConAvgScore()));
                        break;
                    case "赋分平均分排名":
                        cross.setValue(getString(statRange.getConAvgRank()));
                        break;
                    default:
                        break;
                }
                crossList.add(cross);
            }
        }
        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, "", subjectName, "", "", "各教学班  平均分排名表"));
        head.setHeader("科目:" + subjectName);
        head.setColumnHeader("序号");
        json.put("infolist", crossList);
        json.put("params", head);
        return json.toJSONString();
    }

    public String getRankAndScoreStat(String unitId, String acadyear, String semester, String examId, String gradeCode,
                                      String subjectId, String subType, String parmType) {
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //exammanage_stat_range 统计结果
        if (emStatObject == null) {
            return "";
        }
        List<EmStatRange> statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, subType);
        List<EmStatRange> statArSchoolList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_SCHOOL, subType);
        if (CollectionUtils.isNotEmpty(statArSchoolList))
            statArrangeList.add(statArSchoolList.get(0));
        List<EmStatCrossEntity> crossList = new ArrayList<>();
        List<String> scoreItemList = new ArrayList<>();
        List<String> rangeNameList = new ArrayList<>();
        Map<String, String> valueMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(statArrangeList)) {
            String[] rangeIds = statArrangeList.stream().map(EmStatRange::getRangeId).collect(Collectors.toSet()).toArray(new String[]{});
            String[] stataRangeIds = statArrangeList.stream().map(EmStatRange::getId).collect(Collectors.toSet()).toArray(new String[]{});
            //统计多值结果
            List<EmStatSpace> statSpaceList = emStatSpaceService.findByObjectIdAndIsConAndRangeIdIn(emStatObject.getId(), "0", stataRangeIds);//不是赋分等级统计
            if (CollectionUtils.isNotEmpty(statSpaceList)) {

                String[] spaceItemIds = statSpaceList.stream().map(EmStatSpace::getSpaceItemId).collect(Collectors.toSet()).toArray(new String[]{});
                //间距
                List<EmSpaceItem> spaceItemList = emSpaceItemService.findByParmTypeAndIdIn(parmType, spaceItemIds);
                Map<String, EmSpaceItem> spaceItemMap = spaceItemList.stream().collect(Collectors.toMap(EmSpaceItem::getId, Function.identity()));
                //获取statRangeId对应的EmStatSpace集合（且将间距冗余 并按分数段排序）   map
                Map<String, List<EmStatSpace>> listMap = getStatSpaceListMap(statSpaceList, spaceItemMap);
                //基础数据
                Map<String, Clazz> clazzMap = SUtils.dt(classRemoteService.findListByIds(rangeIds), new TR<List<Clazz>>() {
                }).stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
                Map<String, TeachClass> teachClassMap = SUtils.dt(teachClassRemoteService.findListByIds(rangeIds), new TR<List<TeachClass>>() {
                }).stream().collect(Collectors.toMap(TeachClass::getId, Function.identity()));

                EmStatCrossEntity cross = null;
                EmStatRange statRange = null;
                for (int i = 0; i < statArrangeList.size(); i++) {
                    statRange = statArrangeList.get(i);
                    String rangeName = getRangeName(statRange, clazzMap, teachClassMap);
                    if (!rangeNameList.contains(rangeName)) {
                        rangeNameList.add(rangeName);
                    }
                    if (listMap.containsKey(statRange.getId())) {
                        List<EmStatSpace> inStatSpaceList = listMap.get(statRange.getId());
                        EmStatSpace statSpace = null;
                        for (int j = 0; j < inStatSpaceList.size(); j++) {
                            statSpace = inStatSpaceList.get(j);
                            if (!scoreItemList.contains(statSpace.getSpaceItemName())) {
                                scoreItemList.add(statSpace.getSpaceItemName());
                            }
                            cross = new EmStatCrossEntity();
                            cross.setColumnName(rangeName);//列名
                            cross.setRowName(statSpace.getSpaceItemName());//行名
                            cross.setColumnOrder(String.valueOf(i + 1));//列排序号
                            cross.setRowOrder(String.valueOf(j + 1));//行排序号
                            cross.setValue(getString(statSpace.getScoreNum()) + (statSpace.getBlance() == null ? "" : "(" + statSpace.getBlance() + "%)"));
                            crossList.add(cross);
                            valueMap.put(statSpace.getSpaceItemName() + rangeName, getString(statSpace.getScoreNum()) + (statSpace.getBlance() == null ? "" : "(" + statSpace.getBlance() + "%)"));
                            valueMap.put(statSpace.getSpaceItemName() + rangeName + "num", getString(statSpace.getScoreNum()));
                        }
                    }
                }
            }
        }
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        if ("1".equals(parmType)) {
            head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, "", subjectName, "", "", "各教学班   考试分数段人数占比对比表"));
        } else {
            head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, "", subjectName, "", "", "各教学班   考试名次段人数占比对比表"));
        }
        head.setHeader("科目:" + subjectName);
        head.setColumnHeader("分数段");
        json.put("infolist", crossList);
        json.put("params", head);
        json.put("scoreItemList", scoreItemList);
        json.put("rangeNameList", rangeNameList);
        json.put("valueMap", valueMap);
        return json.toJSONString();
    }

    @Override
    public String getClassRank(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        return getRankAndScoreStat(unitId, acadyear, semester, examId, gradeCode, subjectId, subType, EmSpaceItem.PARM_CLASS_RANK);
    }

    @Override
    public String getClassScore(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        return getRankAndScoreStat(unitId, acadyear, semester, examId, gradeCode, subjectId, subType, EmSpaceItem.PARM_SPACE);
    }

    @Override
    public String getIsConScore(String unitId, String acadyear, String semester, String examId, String gradeCode, String subjectId, String subType) {
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //exammanage_stat_range 统计结果
        if (emStatObject == null) {
            return "";
        }
        List<EmStatRange> statArrangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, subType);
        List<EmStatRange> statArSchoolList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, subjectId, EmStatRange.RANGE_SCHOOL, subType);
        if (CollectionUtils.isNotEmpty(statArSchoolList))
            statArrangeList.add(statArSchoolList.get(0));
        List<EmStatCrossEntity> crossList = new ArrayList<>();
        List<String> scoreItemList = new ArrayList<>();
        List<String> rangeNameList = new ArrayList<>();
        Map<String, String> valueMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(statArrangeList)) {
            String[] rangeIds = statArrangeList.stream().map(EmStatRange::getRangeId).collect(Collectors.toSet()).toArray(new String[]{});
            String[] stataRangeIds = statArrangeList.stream().map(EmStatRange::getId).collect(Collectors.toSet()).toArray(new String[]{});
            //统计多值结果
            List<EmStatSpace> statSpaceList = emStatSpaceService.findByObjectIdAndIsConAndRangeIdIn(emStatObject.getId(), "1", stataRangeIds);//不是赋分等级统计
            if (CollectionUtils.isNotEmpty(statSpaceList)) {

                String[] spaceItemIds = statSpaceList.stream().map(EmStatSpace::getSpaceItemId).collect(Collectors.toSet()).toArray(new String[]{});
                //间距
                List<EmSpaceItem> spaceItemList = emSpaceItemService.findByParmTypeAndIdIn(EmSpaceItem.PARM_SPACE, spaceItemIds);
                Map<String, EmSpaceItem> spaceItemMap = spaceItemList.stream().collect(Collectors.toMap(EmSpaceItem::getId, Function.identity()));
                //获取statRangeId对应的EmStatSpace集合（且将间距冗余 并按分数段排序）   map
                Map<String, List<EmStatSpace>> listMap = getStatSpaceListMap(statSpaceList, null);
                //基础数据
                Map<String, Clazz> clazzMap = SUtils.dt(classRemoteService.findListByIds(rangeIds), new TR<List<Clazz>>() {
                }).stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
                Map<String, TeachClass> teachClassMap = SUtils.dt(teachClassRemoteService.findListByIds(rangeIds), new TR<List<TeachClass>>() {
                }).stream().collect(Collectors.toMap(TeachClass::getId, Function.identity()));
                List<EmConversion> conList = emConversionService.findByUnitId(unitId);
                EmStatCrossEntity cross = null;
                EmStatRange statRange = null;
                for (int i = 0; i < statArrangeList.size(); i++) {
                    statRange = statArrangeList.get(i);
                    String rangeName = getRangeName(statRange, clazzMap, teachClassMap);
                    if (!rangeNameList.contains(rangeName)) {
                        rangeNameList.add(rangeName);
                    }
                    if (listMap.containsKey(statRange.getId())) {
                        Map<Integer, EmStatSpace> inMap = listMap.get(statRange.getId()).stream().collect(Collectors.toMap(EmStatSpace::getScoreRank, Function.identity()));
                        EmStatSpace statSpace = null;
                        for (int j = 0; j < conList.size(); j++) {
                            statSpace = inMap.get(conList.get(j).getScoreRank());
                            if (statSpace == null) {
                                statSpace = new EmStatSpace();
                                statSpace.setScoreRank(conList.get(j).getScoreRank());
                                statSpace.setBlance(new Float(0.0));
                            }
                            if (!scoreItemList.contains(statSpace.getScoreRank() + "")) {
                                scoreItemList.add(statSpace.getScoreRank() + "");
                            }
                            cross = new EmStatCrossEntity();
                            cross.setColumnName(rangeName);//列名
                            cross.setRowName(statSpace.getScoreRank() + "");//行名
                            cross.setColumnOrder(String.valueOf(i + 1));//列排序号
                            cross.setRowOrder(String.valueOf(j + 1));//行排序号
                            cross.setValue(getString(statSpace.getScoreNum()) + (statSpace.getBlance() == null ? "" : "(" + statSpace.getBlance() + "%)"));
                            crossList.add(cross);
                            valueMap.put(statSpace.getScoreRank() + rangeName, getString(statSpace.getScoreNum()) + (statSpace.getBlance() == null ? "" : "(" + statSpace.getBlance() + "%)"));
                            valueMap.put(statSpace.getScoreRank() + rangeName + "num", getString(statSpace.getScoreNum()));
                        }
                    }
                }
            }
        }
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName() + "(选考)";

        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, null, subjectName, null, null, "各教学班    7选3选考赋分等级人数占比对比表"));
        head.setHeader("科目:" + subjectName);
        head.setColumnHeader("等级");
        json.put("infolist", crossList);
        json.put("params", head);
        json.put("scoreItemList", scoreItemList);
        json.put("rangeNameList", rangeNameList);
        json.put("valueMap", valueMap);
        return json.toJSONString();
    }

    @Override
    public String getAllExamSubject(String unitId, String acadyear, String semester, String gradeCode, String subjectId, String subType, String classId) {
        //exammanage_stat_object 统计对象
        List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadAndGradeId(unitId, acadyear, semester, gradeCode);

        List<EmStatObject> emStatObjectList = emStatObjectService.findByUnitId(unitId);//TODO 是否需要统计类型
        String[] statObjectIds = emStatObjectList.stream().map(EmStatObject::getId).collect(Collectors.toSet()).toArray(new String[0]);
        //exammanage_stat_range 统计结果
        List<EmStatRange> statArrangeList = emStatRangeService.findBySubIdAndTypeAndObjectIds(subjectId, classId, EmStatRange.RANGE_CLASS, subType, statObjectIds);
        boolean is73Sub = false;
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            is73Sub = true;
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        List<EmStatCrossEntity> crossList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(statArrangeList)) {
            Set<String> examIds = statArrangeList.stream().map(EmStatRange::getExamId).collect(Collectors.toSet());
            Set<String> lastExamIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(examList)) {
                examList.forEach(item -> {
                    if (examIds.contains(item.getId())) {
                        lastExamIds.add(item.getId());
                    }
                });
            }
            Map<String, EmExamInfo> examMap = emExamInfoService.findListByIdIn(lastExamIds.toArray(new String[]{})).stream().collect(Collectors.toMap(EmExamInfo::getId, Function.identity()));
            //subType为0时，默认是非7选3科目
            String[] names = null;
            names = is73Sub ? new String[]{"统计人数", "考试最高分", "考试最低分", "考试分标准差", "班级所有学生考试分之和", "班级考试平均分", "考试平均分排名", "赋分最高分", "赋分最低分", "赋分标准差", "班级所有学生赋分之和", "班级赋分平均分", "赋分平均分排名"}
                    : new String[]{"统计人数", "考试最高分", "考试最低分", "考试分标准差", "班级所有学生考试分之和", "班级考试平均分", "考试平均分排名"};

            EmStatRange statRange = null;
            EmStatCrossEntity cross = null;
            for (int i = 0; i < statArrangeList.size(); i++) {//TODO
                statRange = statArrangeList.get(i);
                EmExamInfo exam = examMap.get(statRange.getExamId());
                if (exam == null)
                    continue;
                for (int j = 0; j < names.length; j++) {
                    cross = new EmStatCrossEntity();
                    cross.setColumnName(names[j]);//列名
                    cross.setRowName(exam.getExamName());//行名
                    cross.setColumnOrder(String.valueOf(j + 1));//列排序号
                    cross.setRowOrder(String.valueOf(i + 1));//行排序号
                    switch (names[j]) {
                        case "统计人数":
                            cross.setValue(getString(statRange.getStatNum()));
                            break;
                        case "考试最高分":
                            cross.setValue(getString(statRange.getMaxScore()));
                            break;
                        case "考试最低分":
                            cross.setValue(getString(statRange.getMinScore()));
                            break;
                        case "考试分标准差":
                            cross.setValue(getString(statRange.getNormDeviation()));
                            break;
                        case "班级所有学生考试分之和":
                            cross.setValue(getString(statRange.getSumScore()));
                            break;
                        case "班级考试平均分":
                            cross.setValue(getString(statRange.getAvgScore()));
                            break;
                        case "考试平均分排名":
                            cross.setValue(getString(statRange.getRank()));
                            break;
                        case "赋分最高分":
                            cross.setValue(getString(statRange.getConScoreUp()));
                            break;
                        case "赋分最低分":
                            cross.setValue(getString(statRange.getConScoreLow()));
                            break;
                        case "赋分标准差":
                            cross.setValue(getString(statRange.getConNormDeviation()));
                            break;
                        case "班级所有学生赋分之和":
                            cross.setValue(getString(statRange.getConSumScore()));
                            break;
                        case "班级赋分平均分":
                            cross.setValue(getString(statRange.getConAvgScore()));
                            break;
                        case "赋分平均分排名":
                            cross.setValue(getString(statRange.getConAvgRank()));
                            break;
                        default:
                            break;
                    }
                    crossList.add(cross);
                }
            }
        }

        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        String className = getClassNameById(classId);
        head.setTitle(getTitle(unitId, acadyear, semester, null, gradeCode, null, subjectName, classId, className, "历次考试对比表"));
        head.setHeader("教学班:" + className);
        head.setColumnHeader("考试");
        json.put("infolist", crossList);
        json.put("params", head);
        return json.toJSONString();
    }

    @Override
    public String getAllExamSubjectBySelf(String unitId, String acadyear, String semester, String gradeCode, String subjectId, String subType, String classId) {
        //exammanage_stat_object 统计对象
        List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadAndGradeId(unitId, acadyear, semester, gradeCode);

        List<EmStatObject> emStatObjectList = emStatObjectService.findByUnitId(unitId);//TODO 是否需要统计类型
        if (CollectionUtils.isEmpty(emStatObjectList))
            return null;
        String[] statObjectIds = emStatObjectList.stream().map(EmStatObject::getId).collect(Collectors.toSet()).toArray(new String[0]);
        //exammanage_stat_range 统计结果
        List<EmStatRange> statArrangeList = emStatRangeService.findBySubIdAndTypeAndObjectIds(subjectId, classId, EmStatRange.RANGE_CLASS, subType, statObjectIds);
        boolean is73Sub = false;
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course.getSubjectName();
        if ("1".equals(subType)) {
            is73Sub = true;
            subjectName += "(选考)";
        } else if ("2".equals(subType)) {
            subjectName += "(学考)";
        }
        List<EmStatRange> lastList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(statArrangeList)) {
            Set<String> examIds = statArrangeList.stream().map(EmStatRange::getExamId).collect(Collectors.toSet());
            Set<String> lastExamIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(examList)) {
                examList.forEach(item -> {
                    if (examIds.contains(item.getId())) {
                        lastExamIds.add(item.getId());
                    }
                });
            }
            Map<String, EmExamInfo> examMap = emExamInfoService.findListByIdIn(lastExamIds.toArray(new String[]{})).stream().collect(Collectors.toMap(EmExamInfo::getId, Function.identity()));
            //subType为0时，默认是非7选3科目
            for (EmStatRange statRange : statArrangeList) {//TODO
                EmExamInfo exam = examMap.get(statRange.getExamId());
                if (exam == null)
                    continue;
                statRange.setExamName(exam.getExamName());
                statRange.setExamTime(exam.getCreationTime() == null ? new Date() : exam.getCreationTime());
                lastList.add(statRange);
            }
        }
        Collections.sort(lastList, (o1, o2) -> {
            return o2.getExamTime().compareTo(o1.getExamTime());
        });
        JSONObject json = new JSONObject();
        EmStatHeadEntity head = new EmStatHeadEntity();
        String className = getClassNameById(classId);
        head.setTitle(getTitle(unitId, acadyear, semester, null, gradeCode, null, subjectName, classId, className, "历次考试对比表"));
        head.setHeader("教学班:" + className);
        json.put("statArrangeList", lastList);
        json.put("params", head);
        json.put("is73Sub", is73Sub);
        return json.toJSONString();
    }

    @Override
    public String getExamAllSubject(String unitId, String acadyear, String semester, String gradeCode, String examId, String classId) {
        JSONObject json = new JSONObject();
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        //exammanage_stat 学生统计
        //List<EmStat> statList=emStatService.findByClassId(emStatObject.getId(), examId, classId);
        Set<String> classIds = new HashSet<>();
        classIds.add(classId);
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
        });
        Set<String> stuIds = studentList.stream().map(Student::getId).collect(Collectors.toSet());
        List<EmStat> statList = emStatService.findByStudentIds(emStatObject.getId(), examId, null, stuIds.toArray(new String[]{}));

        Map<String, List<EmStat>> statListMap = new HashMap<>();
        List<EmStat> totalList = new ArrayList<>();
        Set<String> subIds = new HashSet<>();
        statList.forEach(item -> {
            if (!statListMap.containsKey(item.getStudentId())) {
                statListMap.put(item.getStudentId(), new ArrayList<>());
            }
            statListMap.get(item.getStudentId()).add(item);
            subIds.add(item.getSubjectId());
            if (EmStat.STAT_TOTAL.equals(item.getSubjectId())) {
                totalList.add(item);
                stuIds.add(item.getStudentId());
            }
        });
        Collections.sort(totalList, (o1, o2) -> {
            return o1.getClassRank() - o2.getClassRank();
        });
        //获取
//		List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})),new TR<List<Student>>(){});
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        Map<String, Course> courseMap = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[]{})), new TR<List<Course>>() {
        })
                .stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        List<EmStatCrossEntity> crossList = new ArrayList<>();
        EmStatCrossEntity cross = null;
        Student stu = null;
        for (int i = 0; i < totalList.size(); i++) {
            stu = studentMap.get(totalList.get(i).getStudentId());
            if (!statListMap.containsKey(stu.getId()))
                continue;
            List<EmStat> inStatList = statListMap.get(stu.getId());
            for (EmStat emStat : inStatList) {
                Course cousre = courseMap.get(emStat.getSubjectId());
                if (cousre == null)
                    continue;
                emStat.setOrderId(cousre.getOrderId());
            }
            Collections.sort(inStatList, (o1, o2) -> {
                if (o1.getOrderId() != null && o2.getOrderId() != null) {
                    return o1.getOrderId() - o2.getOrderId();
                }
                return 0;
            });
            EmStat allScoreStat = new EmStat();
            for (int j = 0; j < inStatList.size(); j++) {
                EmStat inStat = inStatList.get(j);
                if (j == 0) {
                    cross = new EmStatCrossEntity();
                    cross.setColumnName("考号");
                    cross.setRowName(String.valueOf(i + 1));//行名
                    cross.setColumnOrder("1");//列排序号
                    cross.setRowOrder(String.valueOf(i + 1));//行排序号
                    cross.setValue(examNumMap.get(stu.getId()));
                    crossList.add(cross);
                    cross = new EmStatCrossEntity();
                    cross.setColumnName("学生");
                    cross.setRowName(String.valueOf(i + 1));//行名
                    cross.setColumnOrder("2");//列排序号
                    cross.setRowOrder(String.valueOf(i + 1));//行排序号
                    cross.setValue(stu.getStudentName());
                    crossList.add(cross);
                }
                if ("1".equals(inStat.getSubType())) {
                    continue;
                }
                Course course = courseMap.get(inStat.getSubjectId());
                if (course == null) {
                    if (EmStat.STAT_TOTAL.equals(inStat.getSubjectId())) {//设置总分的数据
                        allScoreStat.setScore(inStat.getScore());
                        allScoreStat.setGradeRank(inStat.getGradeRank());
                        allScoreStat.setClassRank(inStat.getClassRank());
                    }
                    continue;
                }
                cross = new EmStatCrossEntity();
                if ("3".equals(inStat.getSubType())) {
                    cross.setColumnName(course.getSubjectName() + "选考");//列名
                    cross.setColumnOrder((course.getOrderId() * 10 + 1) + "");//列排序号
                } else if ("2".equals(inStat.getSubType())) {
                    cross.setColumnName(course.getSubjectName() + "学考");//列名
                    cross.setColumnOrder((course.getOrderId() * 10 + 2) + "");//列排序号
                } else {
                    cross.setColumnName(course.getSubjectName());//列名
                    cross.setColumnOrder((course.getOrderId() * 10) + "");//列排序号
                }
                cross.setRowName(String.valueOf(i + 1));//行名
                cross.setRowOrder(String.valueOf(i + 1));//行排序号
                cross.setValue(getString(inStat.getScore()));
                crossList.add(cross);
            }
            cross = new EmStatCrossEntity();
            cross.setColumnName("总分");//列名
            cross.setRowName(String.valueOf(i + 1));//行名
            cross.setColumnOrder(String.valueOf(9999));//列排序号
            cross.setRowOrder(String.valueOf(i + 1));//行排序号
            cross.setValue(getString(allScoreStat.getScore()));
            crossList.add(cross);
            cross = new EmStatCrossEntity();
            cross.setColumnName("年级排名");//列名
            cross.setRowName(String.valueOf(i + 1));//行名
            cross.setColumnOrder(String.valueOf(10000));//列排序号
            cross.setRowOrder(String.valueOf(i + 1));//行排序号
            cross.setValue(getString(allScoreStat.getGradeRank()));
            crossList.add(cross);
            cross = new EmStatCrossEntity();
            cross.setColumnName("班级排名");//列名
            cross.setRowName(String.valueOf(i + 1));//行名
            cross.setColumnOrder(String.valueOf(10001));//列排序号
            cross.setRowOrder(String.valueOf(i + 1));//行排序号
            cross.setValue(getString(allScoreStat.getClassRank()));
            crossList.add(cross);
        }
        EmStatHeadEntity head = new EmStatHeadEntity();
        EmStatRange statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, EmStat.STAT_TOTAL, classId, EmStatRange.RANGE_CLASS);
        if (statRange != null) {
            head.setSummary("班级考试总平均分:" + statRange.getAvgScore() + "分   班级考试总分最高分:" + statRange.getMaxScore() + "分      班级考试总分最低分:" + statRange.getMinScore() + "分");
        }
        String className = getClassNameById(classId);
        head.setTitle(getTitle(unitId, acadyear, semester, examId, gradeCode, null, null, classId, className, "考试总分排名表"));
        head.setHeader("行政班:" + className);
        head.setColumnHeader("序号");
        json.put("infolist", crossList);
        json.put("params", head);
        return json.toJSONString();
    }

    @Override
    public String getExamClassAllSubjectBySelf(String unitId, String acadyear, String semester, String classId,
                                               String examId, String tableType) {
        JSONObject json = new JSONObject();
        boolean flag = "1".equals(tableType);//true代表考试分  false代表赋分
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return json.toJSONString();
        }
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
        });
        Set<String> stuIds = studentList.stream().map(Student::getId).collect(Collectors.toSet());
        List<EmStat> statList = emStatService.findByStudentIds(emStatObject.getId(), examId, null, stuIds.toArray(new String[]{}));
        if (CollectionUtils.isEmpty(statList)) {
            return json.toJSONString();
        }
        Map<String, List<EmStat>> statListMap = new HashMap<>();
        List<EmStat> totalList = new ArrayList<>();
        Set<String> subIds = new HashSet<>();
        statList.forEach(item -> {
            if (!statListMap.containsKey(item.getStudentId())) {
                statListMap.put(item.getStudentId(), new ArrayList<>());
            }
            statListMap.get(item.getStudentId()).add(item);
            subIds.add(item.getSubjectId());
            if (flag) {
                if (ExammanageConstants.ZERO32.equals(item.getSubjectId())) {
                    totalList.add(item);
                    stuIds.add(item.getStudentId());
                }
            } else {
                if (ExammanageConstants.CON_SUM_ID.equals(item.getSubjectId())) {
                    totalList.add(item);
                    stuIds.add(item.getStudentId());
                }
            }
        });
        Collections.sort(totalList, (o1, o2) -> {
            return o1.getGradeRank() - o2.getGradeRank();
        });
        //获取
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));

        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        Map<String, EmStat> emStatMap = new HashMap<>();//学生id+科目id+,科目类型   得到一个统计数据
        List<ExamSubjectDto> subjectDtoList = new ArrayList<>();//考试科目
        List<Course> courses = getSubList(unitId, examId, "0");
        for (Course e : courses) {
            if (!flag && StringUtils.equals(e.getCourseTypeId(), "2")) {
                continue;
            }
            ExamSubjectDto subjectDto = new ExamSubjectDto();
            subjectDto.setSubjectId(e.getId() + "," + e.getCourseTypeId());
            subjectDto.setSubType(e.getCourseTypeId());
            String typeName = "";
            if (StringUtils.equals(e.getCourseTypeId(), "1")) {
                typeName = "(选考)";
            } else if (StringUtils.equals(e.getCourseTypeId(), "2")) {
                typeName = "(学考)";
            }
            subjectDto.setSubjectName(e.getSubjectName() + typeName);
            subjectDtoList.add(subjectDto);
        }
        for (EmStat emStat : totalList) {
            Student stu = studentMap.get(emStat.getStudentId());
            if (stu != null) {
                emStat.setExamNum(examNumMap.get(stu.getId()));
                emStat.setStudentName(stu.getStudentName());
                List<EmStat> inStatList = statListMap.get(stu.getId());
                if (CollectionUtils.isNotEmpty(inStatList)) {
                    for (EmStat inStat : inStatList) {
                        String subType = inStat.getSubType();
                        if (flag) {
                            if ("3".equals(subType)) {
                                subType = "1";
                            }
                            if ("1".equals(inStat.getSubType())) {//1的类型不需要
                                continue;
                            }
                        } else {
                            if ("2".equals(inStat.getSubType()) || "3".equals(inStat.getSubType())) {//只保留0 和1的类型
                                continue;
                            }
                        }
                        emStatMap.put(stu.getId() + inStat.getSubjectId() + "," + subType, inStat);
                    }
                }
            }
        }
        EmStatRange statRange = null;
        String title = null;
        if (flag) {
            statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ExammanageConstants.ZERO32, classId, EmStatRange.RANGE_CLASS);
            title = getTitle(unitId, acadyear, semester, examId, null, null, null, classId, null, "  总分 排名表");
        } else {
            statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ExammanageConstants.CON_SUM_ID, classId, EmStatRange.RANGE_CLASS);
            title = getTitle(unitId, acadyear, semester, examId, null, null, null, classId, null, "  非7选3+选考赋分 排名表");
        }
        if (statRange != null) {
            json.put("summary", "<p>该班平均分：<b>" + statRange.getAvgScore() + "</b> 分，最高分：<b>" + statRange.getMaxScore() + "</b> 分，最低分：<b>" + statRange.getMinScore() + "</b> 分</p>");
        }
        json.put("title", title);
        json.put("subjectDtoList", subjectDtoList);
        json.put("totalList", totalList);
        json.put("emStatMap", emStatMap);
        return json.toJSONString();
    }

    @Override
    public String getExamAllSubjectBySelf(String unitId, String acadyear, String semester, String gradeCode, String examId, String tableType) {
        JSONObject json = new JSONObject();
        boolean flag = "1".equals(tableType) || "3".equals(tableType);//true代表考试分  false代表赋分
        //exammanage_stat_object 统计对象
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null)
            return "";
        //exammanage_stat 学生统计
//		Set<String> classIds=new HashSet<>();
//		String gradeId="";
        gradeCode = StringUtils.isNotBlank(gradeCode) ? gradeCode : emExamInfoService.findOne(examId).getGradeCodes();
		/*List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,gradeCode),new TR<List<Grade>>(){});
		if(CollectionUtils.isNotEmpty(gradeList)){
			gradeId=gradeList.get(0).getId();
			List<Clazz> clazzList=SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId),new TR<List<Clazz>>(){});
			classIds.addAll(clazzList.stream().map(Clazz::getId).collect(Collectors.toSet()));
		}*/

        List<EmStat> statList = emStatService.findByExamIdAndObjectSubType(examId, emStatObject.getId());
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(EntityUtils.getSet(statList, EmStat::getStudentId).toArray(new String[0])), new TR<List<Student>>() {
        });
//		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})),new TR<List<Student>>(){});

        Map<String, List<EmStat>> statListMap = new HashMap<>();
        List<EmStat> totalList = new ArrayList<>();
        Set<String> subIds = new HashSet<>();
        statList.forEach(item -> {
            if (!statListMap.containsKey(item.getStudentId())) {
                statListMap.put(item.getStudentId(), new ArrayList<>());
            }
            statListMap.get(item.getStudentId()).add(item);
            subIds.add(item.getSubjectId());
            if (flag) {
                if (ExammanageConstants.ZERO32.equals(item.getSubjectId())) {
                    totalList.add(item);
//					stuIds.add(item.getStudentId());
                }
            } else {
                if (ExammanageConstants.CON_SUM_ID.equals(item.getSubjectId())) {
                    totalList.add(item);
//					stuIds.add(item.getStudentId());
                }
            }
        });
        if ("3".equals(tableType)) {
            Collections.sort(totalList, (o1, o2) -> {
                return o1.getAbilityRank() - o2.getAbilityRank();
            });
        } else {
            Collections.sort(totalList, (o1, o2) -> {
                return o1.getGradeRank() - o2.getGradeRank();
            });
        }
        //获取
//		List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})),new TR<List<Student>>(){});
        Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));

        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        Map<String, EmStat> emStatMap = new HashMap<>();//学生id+科目id+,科目类型   得到一个统计数据
        List<ExamSubjectDto> subjectDtoList = new ArrayList<>();//考试科目
        List<Course> courses = getSubList(unitId, examId, "0");
        for (Course e : courses) {
            if (!flag && StringUtils.equals(e.getCourseTypeId(), "2")) {
                continue;
            }
            ExamSubjectDto subjectDto = new ExamSubjectDto();
            subjectDto.setSubjectId(e.getId() + "," + e.getCourseTypeId());
            subjectDto.setSubType(e.getCourseTypeId());
            String typeName = "";
            if (StringUtils.equals(e.getCourseTypeId(), "1")) {
                typeName = "(选考)";
            } else if (StringUtils.equals(e.getCourseTypeId(), "2")) {
                typeName = "(学考)";
            }
            subjectDto.setSubjectName(e.getSubjectName() + typeName);
            subjectDtoList.add(subjectDto);
        }
        for (EmStat emStat : totalList) {
            Student stu = studentMap.get(emStat.getStudentId());
            if (stu != null) {
                emStat.setExamNum(examNumMap.get(stu.getId()));
                emStat.setStudentName(stu.getStudentName());
                emStat.setStudentCode(stu.getStudentCode());
                List<EmStat> inStatList = statListMap.get(stu.getId());
                if (CollectionUtils.isNotEmpty(inStatList)) {
                    for (EmStat inStat : inStatList) {
                        String subType = inStat.getSubType();
                        if (flag) {
                            if ("3".equals(subType)) {
                                subType = "1";
                            }
                            if ("1".equals(inStat.getSubType())) {//1的类型不需要
                                continue;
                            }
                        } else {
                            if ("2".equals(inStat.getSubType()) || "3".equals(inStat.getSubType())) {//只保留0 和1的类型
                                continue;
                            }
                        }
                        emStatMap.put(stu.getId() + inStat.getSubjectId() + "," + subType, inStat);
                    }
                }
            }
        }
        EmStatRange statRange = null;
        String title = null;
        if (flag) {
            statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ExammanageConstants.ZERO32, unitId, EmStatRange.RANGE_SCHOOL);
            title = getTitle(unitId, acadyear, semester, examId, gradeCode, null, null, null, null, "  考试分总分 排名表");
        } else {
            statRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ExammanageConstants.CON_SUM_ID, unitId, EmStatRange.RANGE_SCHOOL);
            title = getTitle(unitId, acadyear, semester, examId, gradeCode, null, null, null, null, "  非7选3+选考赋分 排名表");
        }
        if (statRange != null) {
            json.put("summary", "年级考试总平均分:" + statRange.getAvgScore() + "分              年级考试总分最高分:" + statRange.getMaxScore() + "分               年级考试总分最低分:" + statRange.getMinScore() + "分");
        }
        json.put("title", title);
        json.put("subjectDtoList", subjectDtoList);
        json.put("totalList", totalList);
        json.put("emStatMap", emStatMap);
        return json.toJSONString();
    }

    @Override
    public String getScoreRankingByClassSelf(String unitId, String acadyear, String semester, String gradeCode,
                                             String examId, String subjectParam) {
        JSONObject json = new JSONObject();
        List<EmStatRange> emStatRangeList = new ArrayList<>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return json.toJSONString();
        }
        if (subjectParam.equals("0")) {
            emStatRangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, ZERO32, "1", "0");
        } else if (subjectParam.equals("1")) {
            emStatRangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, CON_SUM_ID, "1", "0");
        }
        if (CollectionUtils.isEmpty(emStatRangeList)) {
            return json.toJSONString();
        }
        Set<String> classIds = EntityUtils.getSet(emStatRangeList, EmStatRange::getRangeId);
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        List<Teacher> teacherList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(clazzList)) {
            Set<String> teacherIds = new HashSet<>();
            for (Clazz clazz : clazzList) {
                teacherIds.add(clazz.getTeacherId());
            }
            if (CollectionUtils.isNotEmpty(teacherIds)) {
                teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{})), new TR<List<Teacher>>() {
                });
            }
            Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList, Teacher::getId);
            if (CollectionUtils.isNotEmpty(emStatRangeList)) {
                for (EmStatRange emStatRange : emStatRangeList) {
                    for (Clazz clazz : clazzList) {
                        String classId = clazz.getId();
                        if (emStatRange.getRangeId().equals(classId) && emStatRange.getRangeType().equals("1")) {
                            emStatRange.setClassNameOrder(clazz.getClassCode() + "");
                            emStatRange.setClassName(clazz.getClassNameDynamic());
                            if (teacherMap.containsKey(clazz.getTeacherId())) {
                                emStatRange.setTeacherName(teacherMap.get(clazz.getTeacherId()).getTeacherName());
                            } else {
                                emStatRange.setTeacherName("");
                            }
                        }
                    }
                }

            }
        }
        emStatRangeList.sort(new Comparator<EmStatRange>() {
            @Override
            public int compare(EmStatRange o1, EmStatRange o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });
        json.put("statArrangeList", emStatRangeList);
        String name = "";
        if (subjectParam != null) {
            if (subjectParam.equals("0")) {
                name = "各行政班 总分平均分排名表";
            } else if (subjectParam.equals("1")) {
                name = "各行政班 非7选3+选考赋分对比表";
            }
        }
        String title = getTitle(unitId, acadyear, semester, examId, gradeCode, null, null, null, null, name);
        json.put("title", title);
        return json.toJSONString();
    }

    @Override
    public String getScoreRankingByClass(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam) {
        JSONObject json = new JSONObject();
        List<EmStatRange> emStatRangeList = new ArrayList<EmStatRange>();
        String searchGradeId = "";
        Grade grade = null;
        if (StringUtils.isNotBlank(gradeCode)) {
            String grades = gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode);
            List<Grade> gradeList = SUtils.dt(grades, new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                grade = gradeList.get(0);
                searchGradeId = grade.getId();
            }
        }
        if (StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(searchGradeId)) {
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, searchGradeId), new TR<List<Clazz>>() {
            });
            List<Teacher> teacherList = new ArrayList<Teacher>();
            EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
            Set<String> classIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(clazzList)) {
                for (Clazz clazz : clazzList) {
                    classIds.add(clazz.getId());
                }
                Set<String> teacherIds = new HashSet<>();
                for (Clazz clazz : clazzList) {
                    teacherIds.add(clazz.getTeacherId());
                }
                if (CollectionUtils.isNotEmpty(classIds) && emStatObject != null) {
                    if (subjectParam.equals("0")) {
                        emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, ZERO32, "1", classIds.toArray(new String[]{}));
                    } else if (subjectParam.equals("1")) {
                        emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, CON_SUM_ID, "1", classIds.toArray(new String[]{}));
                    }
                }
                if (CollectionUtils.isNotEmpty(teacherIds)) {
                    teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{})), new TR<List<Teacher>>() {
                    });
                }
                Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList, Teacher::getId);

                if (CollectionUtils.isNotEmpty(emStatRangeList)) {
                    for (EmStatRange emStatRange : emStatRangeList) {
                        for (Clazz clazz : clazzList) {
                            String classId = clazz.getId();
                            if (emStatRange.getRangeId().equals(classId) && emStatRange.getRangeType().equals("1")) {
                                emStatRange.setClassNameOrder(clazz.getDisplayOrder() + "");
                                emStatRange.setClassName(clazz.getClassNameDynamic());
                                if (teacherMap.containsKey(clazz.getTeacherId())) {
                                    emStatRange.setTeacherName(teacherMap.get(clazz.getTeacherId()).getTeacherName());
                                } else {
                                    emStatRange.setTeacherName("");
                                }
                            }
                        }
                    }

                }
            }
        }
        emStatRangeList.sort(new Comparator<EmStatRange>() {
            @Override
            public int compare(EmStatRange o1, EmStatRange o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });
        json.put("infolist", emStatRangeList);
        EmStatHeadEntity emStatHeadEntity = new EmStatHeadEntity();
        String name = "";
        if (subjectParam != null) {
            if (subjectParam.equals("0")) {
                name = "各行政班 考试总分平均分排名表";
            } else if (subjectParam.equals("1")) {
                name = "各行政班 非7选3+选考赋分对比表";
            }
        }
        if (grade != null) {
            String title = getTitle(unitId, acadyear, semester, examId, gradeCode, grade.getGradeName(), null, null, null, name);
            emStatHeadEntity.setTitle(title);
        }
        json.put("params", emStatHeadEntity);
        return json.toJSONString();
    }

    @Override
    public String getRankDistributeByClassSelf(String unitId, String acadyear, String semester, String examId
            , String gradeCode, String subjectParam) {
        return getRankAndScoreDistribute(unitId, acadyear, semester, examId, gradeCode, subjectParam, EmSpaceItem.PARM_CLASS_RANK);
    }

    @Override
    public String getScoreDistributeByClassSelf(String unitId, String acadyear, String semester, String examId,
                                                String gradeCode, String subjectParam) {
        return getRankAndScoreDistribute(unitId, acadyear, semester, examId, gradeCode, subjectParam, EmSpaceItem.PARM_SPACE);
    }

    public String getRankAndScoreDistribute(String unitId, String acadyear, String semester, String examId,
                                            String gradeCode, String subjectParam, String parmType) {
        JSONObject json = new JSONObject();
        List<EmStatRange> emStatRangeList = new ArrayList<>();
        List<EmStatRange> emStatRangeTotalList = new ArrayList<>();
        List<EmStatSpace> emStatSpaceList = new ArrayList<>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return json.toString();
        }
        if (subjectParam.equals("0")) {
            emStatRangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, ZERO32, "1", "0");
            emStatRangeTotalList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, ZERO32, "2", new String[]{emStatObject.getUnitId()});
        } else if (subjectParam.equals("1")) {
            emStatRangeList = emStatRangeService.findByExamIdAndSubIdAndType(emStatObject.getId(), examId, CON_SUM_ID, "1", "0");
            emStatRangeTotalList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, CON_SUM_ID, "2", new String[]{emStatObject.getUnitId()});
        }
        if (CollectionUtils.isEmpty(emStatRangeList)) {
            return json.toString();
        }
        Map<String, EmStatRange> rangeMap = EntityUtils.getMap(emStatRangeList, EmStatRange::getRangeId);
        Set<String> statRangIds = EntityUtils.getSet(emStatRangeList, EmStatRange::getId);
        if (CollectionUtils.isNotEmpty(emStatRangeTotalList)) {
            statRangIds.add(emStatRangeTotalList.get(0).getId());
        }
        emStatSpaceList = emStatSpaceService.findByStatRangeIdIn(statRangIds.toArray(new String[]{}));
        Set<String> spaceItemIds = EntityUtils.getSet(emStatSpaceList, EmStatSpace::getSpaceItemId);
        List<EmSpaceItem> spaceList = emSpaceItemService.findByParmTypeAndIdIn(parmType, spaceItemIds.toArray(new String[]{}));
        Map<String, EmStatSpace> emStatSpaceMap = EntityUtils.getMap(emStatSpaceList, EmStatSpace::getItemIdAndStatRangeId);
        Set<String> clsIds = EntityUtils.getSet(emStatRangeList, EmStatRange::getRangeId);
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        spaceList.sort(new Comparator<EmSpaceItem>() {
            @Override
            public int compare(EmSpaceItem o1, EmSpaceItem o2) {
                return (int) (o2.getLowScore() - o1.getLowScore());
            }
        });
        Map<String, String> emStatMap = new HashMap<>();
        Map<String, String> emStatNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(spaceList) && CollectionUtils.isNotEmpty(emStatRangeList)) {

            for (int i = 0; i < spaceList.size(); i++) {
                EmSpaceItem emSpaceItem = spaceList.get(i);
                for (Clazz clazz : clazzList) {
                    String key = clazz.getId() + emSpaceItem.getId();
                    String queryId = rangeMap.get(clazz.getId()).getId() + emSpaceItem.getId();
                    if (emStatSpaceMap.containsKey(queryId)) {
                        emStatMap.put(key, emStatSpaceMap.get(queryId).getScoreNum() + "(" + emStatSpaceMap.get(queryId).getBlance() + "%)");
                        emStatNumMap.put(key, emStatSpaceMap.get(queryId).getScoreNum());
                    } else {
                        emStatMap.put(key, "0");
                        emStatNumMap.put(key, "0");
                    }
                }
                String key = emSpaceItem.getId();
                String queryId = emStatRangeTotalList.get(0).getId() + emSpaceItem.getId();
                if (emStatSpaceMap.containsKey(queryId)) {
                    emStatMap.put(key, emStatSpaceMap.get(queryId).getScoreNum() + "(" + emStatSpaceMap.get(queryId).getBlance() + "%)");
                    emStatNumMap.put(key, emStatSpaceMap.get(queryId).getScoreNum());
                } else {
                    emStatMap.put(key, "0");
                    emStatNumMap.put(key, "0");
                }
            }
        }
        String name = "";
        if (subjectParam != null) {
            if (subjectParam.equals("0")) {
                name = "各行政班  ";
            } else if (subjectParam.equals("1")) {
                name = "各行政班  非7选3+选考赋分";
            }
            name += parmType.equals(EmSpaceItem.PARM_SPACE) ? "总分分数段人数占比对比表" : "总分名次段人数占比对比表";
        }
        String title = getTitle(unitId, acadyear, semester, examId, gradeCode, null, null, null, null, name);
        json.put("title", title);
        json.put("clazzList", clazzList);
        json.put("spaceList", spaceList);
        json.put("emStatMap", emStatMap);
        json.put("emStatNumMap", emStatNumMap);
        return json.toJSONString();

    }

    @Override
    public String getScoreDistributeByClass(String unitId, String acadyear, String semester, String gradeCode, String examId, String subjectParam) {
        JSONObject json = new JSONObject();
        List<EmStatRange> emStatRangeList = new ArrayList<EmStatRange>();
        List<EmStatRange> emStatRangeTotalList = new ArrayList<EmStatRange>();
        List<EmStatSpace> emStatSpaceList = new ArrayList<EmStatSpace>();
        List<EmStatCrossEntity> emStatCrossEntities = new ArrayList<EmStatCrossEntity>();
        List<Clazz> clazzList = new ArrayList<Clazz>();
        String searchGradeId = "";
        Grade grade = null;
        if (StringUtils.isNotBlank(gradeCode)) {
            String grades = gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode);
            List<Grade> gradeList = SUtils.dt(grades, new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                grade = gradeList.get(0);
                searchGradeId = grade.getId();
            }
        }
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (StringUtils.isNotBlank(searchGradeId)) {
            clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, searchGradeId), new TR<List<Clazz>>() {
            });
        }
        Set<String> classIds = EntityUtils.getSet(clazzList, "id");
        ;

        if (CollectionUtils.isNotEmpty(clazzList)) {
            if (CollectionUtils.isNotEmpty(classIds) && emStatObject != null) {
                if (subjectParam.equals("0")) {
                    emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, ZERO32, "1", classIds.toArray(new String[]{}));
                    emStatRangeTotalList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, ZERO32, "2", new String[]{emStatObject.getUnitId()});
                } else if (subjectParam.equals("1")) {
                    emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, CON_SUM_ID, "1", classIds.toArray(new String[]{}));
                    emStatRangeTotalList = emStatRangeService.findByExamIdAndRangeIdIn(emStatObject.getId(), examId, CON_SUM_ID, "2", new String[]{emStatObject.getUnitId()});
                }
            }
            Set<String> statRangIds = EntityUtils.getSet(emStatRangeList, "id");
            if (CollectionUtils.isNotEmpty(emStatRangeTotalList)) {
                statRangIds.add(emStatRangeTotalList.get(0).getId());
            }
            //List<EmSpaceItem> spaceList = emSpaceItemService.findByIds(new String[]{spaceItemId});
            emStatSpaceList = emStatSpaceService.findByStatRangeIdIn(statRangIds.toArray(new String[]{}));
            Set<String> spaceItemIds = EntityUtils.getSet(emStatSpaceList, "spaceItemId");
            List<EmSpaceItem> spaceList = emSpaceItemService.findByParmTypeAndIdIn("1", spaceItemIds.toArray(new String[]{}));
            Map<String, EmStatSpace> emStatSpaceMap = EntityUtils.getMap(emStatSpaceList, EmStatSpace::getItemIdAndStatRangeId);
            //Map<String, EmStatSpace> statSpaceMap = EntityUtils.getMap(emStatSpaceList, EmStatSpace::getStatRangeId);
            //Map<String , EmStatRange> emStatRangeMap= EntityUtils.getMap(emStatRangeList, EmStatRange::getId);
            Map<String, Clazz> clazzMap = EntityUtils.getMap(clazzList, Clazz::getId);
            spaceList.sort(new Comparator<EmSpaceItem>() {
                @Override
                public int compare(EmSpaceItem o1, EmSpaceItem o2) {
                    return (int) (o2.getLowScore() - o1.getLowScore());
                }
            });
            if (CollectionUtils.isNotEmpty(spaceList) && CollectionUtils.isNotEmpty(emStatRangeList)) {
                for (int i = 0; i < spaceList.size(); i++) {
                    EmSpaceItem emSpaceItem = spaceList.get(i);
                    for (int j = 0; j < emStatRangeList.size() + 1; j++) {
                        String queryId = "";
                        EmStatCrossEntity emStatCrossEntity = new EmStatCrossEntity();

                        if (j == emStatRangeList.size()) {
                            emStatCrossEntity.setColumnName("年级全体");
                            queryId = emStatRangeTotalList.get(0).getId() + emSpaceItem.getId();
                            //statRangId = emStatRangeTotalList.get(0).getId();
                        } else {
                            queryId = emStatRangeList.get(j).getId() + emSpaceItem.getId();
                            //statRangId = emStatRangeList.get(j).getId();
                            EmStatRange emStatRange = emStatRangeList.get(j);
                            emStatCrossEntity.setColumnName(clazzMap.get(emStatRange.getRangeId()).getClassNameDynamic());
                        }

                        emStatCrossEntity.setColumnOrder(j + 1 + "");
                        emStatCrossEntity.setRowOrder(i + 1 + "");
                        emStatCrossEntity.setRowName(emSpaceItem.getName());
                        if (emStatSpaceMap.containsKey(queryId)) {
                            emStatCrossEntity.setValue(emStatSpaceMap.get(queryId).getScoreNum() + "(" + emStatSpaceMap.get(queryId).getBlance() + "%)");
                        } else {
                            emStatCrossEntity.setValue("");
                        }
                        emStatCrossEntities.add(emStatCrossEntity);
                    }
                }
            }
        }

        json.put("infolist", emStatCrossEntities);
        EmStatHeadEntity emStatHeadEntity = new EmStatHeadEntity();
		/*if(CollectionUtils.isNotEmpty(clazzList)){
		    title=title+clazzList.get(0).getClassName()+"行政班";
		}*/
        String name = "";
        if (subjectParam != null) {
            if (subjectParam.equals("0")) {
                name = "各行政班  考试总分分数段人数占比对比表";
            } else if (subjectParam.equals("1")) {
                name = "各行政班  非7选3+选考赋分总分分数段人数占比对比表";
            }
        }
        if (grade != null) {
            String title = getTitle(unitId, acadyear, semester, examId, gradeCode, grade.getGradeName(), null, null, null, name);
            emStatHeadEntity.setTitle(title);
            emStatHeadEntity.setColumnHeader("分数段");

        }
        json.put("params", emStatHeadEntity);
        return json.toJSONString();
    }

    @Override
    public String getScoreCountByClassIdSelf(String unitId, String acadyear, String semester, String classId, String subjectParam) {
        JSONObject json = new JSONObject();
        List<EmExamInfo> emExamInfoList = new ArrayList<>();
        List<EmStatRange> emStatRangeList = new ArrayList<>();
        List<Clazz> clazzList = new ArrayList<>();
        if (StringUtils.isNotEmpty(classId)) {
            List<String> examList = emClassInfoService.findExamIdByClassIds(new String[]{classId});
            List<EmExamInfo> list1 = emExamInfoService.findBySemesterAndIdIn(examList.toArray(new String[]{}), semester);
            for (EmExamInfo e : list1) {
                if (StringUtils.equals(e.getAcadyear(), acadyear)) {
                    emExamInfoList.add(e);
                }
            }
            clazzList = SUtils.dt(classRemoteService.findClassListByIds(new String[]{classId}), new TR<List<Clazz>>() {
            });
        } else {
            return json.toJSONString();
        }

        if (CollectionUtils.isNotEmpty(emExamInfoList)) {
            for (EmExamInfo emExamInfo : emExamInfoList) {
                String examId = emExamInfo.getId();
                EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
                if (StringUtils.isNotBlank(classId) && emStatObject != null) {
                    EmStatRange emStatRange = new EmStatRange();
                    if (subjectParam.equals("0")) {
                        emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ZERO32, classId, "1");
                    } else if (subjectParam.equals("1")) {
                        emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, CON_SUM_ID, classId, "1");
                    }
                    if (emStatRange != null) {
                        if (emStatRange.getAvgScoreT() != null) {
                            BigDecimal b1 = new BigDecimal(emStatRange.getAvgScoreT());
                            emStatRange.setAvgScoreT(Float.parseFloat(b1.setScale(1, BigDecimal.ROUND_HALF_UP).toString()));
                        }
                        emStatRange.setExamName(emExamInfo.getExamName());
                        emStatRangeList.add(emStatRange);
                    }
                }
            }
        }
        json.put("infolist", emStatRangeList);
        String name = "";
        if (subjectParam != null) {
            if (subjectParam.equals("0")) {
                name = "历次考试 总分对比表";
            } else if (subjectParam.equals("1")) {
                name = "历次考试  非7选3+选考赋分对比表";
            }
        }
        String title = getTitle(unitId, acadyear, semester, null, null, null, null, null, CollectionUtils.isNotEmpty(clazzList) ? clazzList.get(0).getClassNameDynamic() : "", name);
        json.put("title", title);
        return json.toJSONString();
    }

    @Override
    public String getScoreCountByClassId(String unitId, String acadyear, String semester, String classId, String subjectParam) {
        JSONObject json = new JSONObject();
        List<EmExamInfo> emExamInfoList = new ArrayList<EmExamInfo>();
        List<EmStatRange> emStatRangeList = new ArrayList<EmStatRange>();
        List<Clazz> clazzList = new ArrayList<>();
        if (StringUtils.isNotEmpty(classId)) {
            List<String> examList = emClassInfoService.findExamIdByClassIds(new String[]{classId});
            emExamInfoList = emExamInfoService.findBySemesterAndIdIn(examList.toArray(new String[]{}), semester);
            clazzList = SUtils.dt(classRemoteService.findClassListByIds(new String[]{classId}), new TR<List<Clazz>>() {
            });
        }
        if (CollectionUtils.isNotEmpty(emExamInfoList)) {
            for (EmExamInfo emExamInfo : emExamInfoList) {
                String examId = emExamInfo.getId();
                EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
                if (StringUtils.isNotBlank(classId) && emStatObject != null) {
                    EmStatRange emStatRange = new EmStatRange();
                    if (subjectParam.equals("0")) {
                        emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ZERO32, classId, "1");
                    } else if (subjectParam.equals("1")) {
                        emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, CON_SUM_ID, classId, "1");
                    }
                    if (emStatRange != null) {
                        emStatRange.setExamName(emExamInfo.getExamName());
                    } else {
                        continue;
//						emStatRange.setExamName("");
                    }
                    emStatRangeList.add(emStatRange);
                }
            }
        }
        json.put("infolist", emStatRangeList);
        EmStatHeadEntity emStatHeadEntity = new EmStatHeadEntity();
        String name = "";
        if (subjectParam != null) {
            if (subjectParam.equals("0")) {
                name = "历次考试 考试总分对比表";
            } else if (subjectParam.equals("1")) {
                name = "历次考试  非7选3+选考赋分对比表";
            }
        }
        if (CollectionUtils.isNotEmpty(clazzList)) {
            emStatHeadEntity.setHeader("行政班:" + clazzList.get(0).getClassNameDynamic());
            String title = getTitle(unitId, acadyear, semester, null, null, null, null, null, clazzList.get(0).getClassNameDynamic(), name);
            emStatHeadEntity.setTitle(title);
        }
        json.put("params", emStatHeadEntity);
        return json.toJSONString();
    }

    @Override
    public String getScoreDetailByStudentId(String unitId, String acadyear, String semester, String classId, String examId, String studentId) {
        //System.out.println("unitId"+unitId+"examId"+examId);
        JSONObject json = new JSONObject();
        List<EmStat> emStatList = new ArrayList<EmStat>();
        List<EmStat> emStats = new ArrayList<>();
        if (StringUtils.isNotEmpty(examId)) {
            EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
            if (emStatObject != null) {
                emStatList = emStatService.findByExamIdAndStudentId(emStatObject.getId(), examId, studentId);
                if (CollectionUtils.isNotEmpty(emStatList)) {
                    Set<String> subIds = EntityUtils.getSet(emStatList, EmStat::getSubjectId);
                    Map<String, EmStat> emStatMap = EntityUtils.getMap(emStatList, EmStat::getSubjectId);
                    if (emStatMap.containsKey(ZERO32)) {
                        emStatMap.get(ZERO32).setSubjectName("总分");
                        emStatMap.get(ZERO32).setSubjectScore(emStatMap.get(ZERO32).getScore() + "");
                        emStats.add(emStatMap.get(ZERO32));
                    } else if (emStatMap.containsKey(CON_SUM_ID)) {
                        emStatMap.get(CON_SUM_ID).setSubjectName("非7选3科目总分");
                        emStatMap.get(CON_SUM_ID).setSubjectScore(emStatMap.get(CON_SUM_ID).getScore() + "");
                        emStats.add(emStatMap.get(CON_SUM_ID));
                    } else if (emStatMap.containsKey(CON_YSY_ID)) {
                        emStatMap.get(CON_YSY_ID).setSubjectName("非7选3+选考赋分");
                        emStatMap.get(CON_YSY_ID).setSubjectScore(emStatMap.get(CON_YSY_ID).getScore() + "");
                        emStats.add(emStatMap.get(CON_YSY_ID));
                    }
                    List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[]{})), new TR<List<Course>>() {
                    });
                    courses.sort(new Comparator<Course>() {
                        @Override
                        public int compare(Course o1, Course o2) {
                            return o1.getOrderId() - o2.getOrderId();
                        }
                    });
                    Map<String, EmStat> stringEmStatMap = EntityUtils.getMap(emStatList, EmStat::getSubjectId);
                    for (Course course : courses) {
                        for (EmStat emStat : stringEmStatMap.values()) {
                            if (emStat.getSubjectId().equals(course.getId())) {
                                if (emStat.getSubType().equals("1")) {
                                    emStat.setSubjectName(course.getSubjectName() + "(选考)");
                                    emStat.setSubjectScore(emStat.getScore() + "(考试分)，" + emStat.getConScore() + "(赋分)");
                                } else if (emStat.getSubType().equals("2")) {
                                    emStat.setSubjectScore(emStat.getScore() + "");
                                    emStat.setSubjectName(course.getSubjectName() + "(学考)");
                                } else {
                                    emStat.setSubjectName(course.getSubjectName());
                                    emStat.setSubjectScore(emStat.getScore() + "");
                                }
                                emStats.add(emStat);
                            }
                        }
                    }
                }
            }
        }
        json.put("infolist", emStats);
        EmStatHeadEntity emStatHeadEntity = new EmStatHeadEntity();
        List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, null, null, new String[]{studentId}), new TR<List<Student>>() {
        });
        if (CollectionUtils.isNotEmpty(studentList)) {
            String title = "";
            if (semester.equals("1")) {
                title = acadyear + "学年第一学期 ";
            } else if (semester.equals("2")) {
                title = acadyear + "学年第二学期 ";
            }
            title = title + getTitle(null, acadyear, semester, examId, null, null, null, null, null, "考试详情");
            emStatHeadEntity.setTitle(title);
            String studentName = studentList.get(0).getStudentName();
            String studentCode = studentList.get(0).getStudentCode();
            emStatHeadEntity.setHeader("学号:" + studentCode + "  " + "姓名:" + studentName);
        }
        json.put("params", emStatHeadEntity);
        return json.toJSONString();
    }

    @Override
    public String getAllExamStuDetailBySelf(String unitId, String acadyear, String semester,
                                            String gradeCode, String classId, String studentId, String conType, String showAll) {
        // exammanage_stat_object 统计对象
        List<EmStatObject> emStatObjects = emStatObjectService.findByUnitId(unitId);
        if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester) || CollectionUtils.isEmpty(emStatObjects))
            return "";
        List<EmExamInfo> examList = null;
        if ("1".equals(showAll)) {//全部
            examList = queryExamsByStudentId(studentId, null, null, conType);
        } else
            examList = queryExamsByStudentId(studentId, acadyear, semester, conType);
        if (CollectionUtils.isEmpty(examList)) {
            return "";
        }
        Set<String> examIds = examList.stream().map(EmExamInfo::getId).collect(Collectors.toSet());
        List<EmStat> emStatListAll = emStatService.findByObjectIdsExamIdIn(emStatObjects.stream().map(EmStatObject::getId).collect(Collectors.toSet()).toArray(new String[]{})
                , examIds.toArray(new String[]{}), null, studentId);
        //考试id 对应的学生统计数据
        Map<String, List<EmStat>> emStatListMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(emStatListAll)) {
            for (EmStat emStat : emStatListAll) {
                if (!emStatListMap.containsKey(emStat.getExamId())) {
                    emStatListMap.put(emStat.getExamId(), new ArrayList<>());
                }
                emStatListMap.get(emStat.getExamId()).add(emStat);
            }
        }
        //获取所有科目
        List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(emStatListAll.stream().map(EmStat::getSubjectId).collect(Collectors.toSet()).toArray(new String[]{})),
                new TR<List<Course>>() {
                });
		/*Collections.sort(courses, (o1,o2)->{
			return o1.getOrderId() - o2.getOrderId();
		});*/
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject info = null;
        Map<String, String> testMap = new HashMap<>();
        List<ExamSubjectDto> subjectDtoList = new ArrayList<>();
        for (EmExamInfo exam : examList) {
            boolean isgk = "1".equals(exam.getIsgkExamType());
            if ("2".equals(conType) && !isgk) {//赋分过滤普通考试
                continue;
            }
            info = new JSONObject();
            List<EmStat> inStatList = new ArrayList<>();
            List<EmStat> emStatList = emStatListMap.get(exam.getId());
            if (CollectionUtils.isNotEmpty(emStatList)) {
                Map<String, EmStat> emStatMap = EntityUtils.getMap(emStatList, EmStat::getSubjectId);
                EmStat zeroStat = emStatMap.get(ZERO32);
//				EmStat conYsyStat=emStatMap.get(CON_YSY_ID);
                EmStat conSumStat = emStatMap.get(CON_SUM_ID);
                if (StringUtils.isBlank(conType)) {
                    if (zeroStat != null) {
                        zeroStat.setSubType("0");
                        zeroStat.setSubjectName("总分");
                        zeroStat.setSubjectScore(zeroStat.getScore() + "(考试分)，" + getFloatString(zeroStat.getScoreT(), 1) + "(标准分T)");
                        inStatList.add(zeroStat);
                        getSubjectDtoList(subjectDtoList, testMap, ZERO32 + zeroStat.getSubType(), "总分", 0);
                    }
                    if (conSumStat != null) {
                        conSumStat.setSubType("0");
                        conSumStat.setSubjectName("非7选3考试分+选考赋分");
                        conSumStat.setSubjectScore(getString(conSumStat.getScore()));
                        inStatList.add(conSumStat);
                        getSubjectDtoList(subjectDtoList, testMap, CON_SUM_ID + conSumStat.getSubType(), "非7选3考试分+选考赋分", 1);
                    }
                }
                for (Course course : courses) {
                    EmStat emStat = emStatMap.get(course.getId());
                    if (emStat == null)
                        continue;
                    if (StringUtils.isBlank(emStat.getSubType()))
                        emStat.setSubType("0");
                    if (StringUtils.isBlank(conType) || "1".equals(conType) || "3".equals(conType)) {
                        if (emStat.getSubType().equals("1")) {
                            getSubjectDtoList(subjectDtoList, testMap, course.getId() + emStat.getSubType(), course.getSubjectName() + "(选考)", (course.getOrderId() + 1) * 10 + 1);
                            emStat.setSubjectName(course.getSubjectName() + "(选考)");
                            emStat.setSubjectScore(emStat.getScore() + "(考试分)，" + emStat.getConScore() + "(赋分)，" + getFloatString(emStat.getScoreT(), 1) + "(标准分T)");
                        } else if (emStat.getSubType().equals("2")) {
                            getSubjectDtoList(subjectDtoList, testMap, course.getId() + emStat.getSubType(), course.getSubjectName() + "(学考)", (course.getOrderId() + 1) * 10 + 2);
                            emStat.setSubjectName(course.getSubjectName() + "(学考)");
                            emStat.setSubjectScore(getString(emStat.getScore()) + "(考试分)，" + getFloatString(emStat.getScoreT(), 1) + "(标准分T)");
                        } else if (emStat.getSubType().equals("0")) {
                            getSubjectDtoList(subjectDtoList, testMap, course.getId() + emStat.getSubType(), course.getSubjectName(), (course.getOrderId() + 1) * 10);
                            emStat.setSubjectName(course.getSubjectName());
                            emStat.setSubjectScore(getString(emStat.getScore()) + "(考试分)，" + getFloatString(emStat.getScoreT(), 1) + "(标准分T)");
                        }
                    } else {
                        if (emStat.getSubType().equals("1")) {
                            getSubjectDtoList(subjectDtoList, testMap, course.getId() + emStat.getSubType(), course.getSubjectName() + "(选考)", (course.getOrderId() + 1) * 10);
                            emStat.setSubjectName(course.getSubjectName() + "(选考)");
                            emStat.setSubjectScore(getString(emStat.getConScore()));
                        }
                    }
                    inStatList.add(emStat);
                }
                if ("1".equals(conType) || "3".equals(conType)) {//考试分
                    if (zeroStat != null) {
                        zeroStat.setSubType("0");
                        zeroStat.setSubjectName("总分");
                        zeroStat.setSubjectScore(getString(zeroStat.getScore()));
                        inStatList.add(zeroStat);
                        getSubjectDtoList(subjectDtoList, testMap, ZERO32 + zeroStat.getSubType(), "总分", 10000);
                    }
                } else {//赋分
                    if (conSumStat != null) {
                        conSumStat.setSubType("0");
                        conSumStat.setSubjectName("非7选3考试分+选考赋分");
                        conSumStat.setSubjectScore(getString(conSumStat.getScore()));
                        inStatList.add(conSumStat);
                        getSubjectDtoList(subjectDtoList, testMap, CON_SUM_ID + conSumStat.getSubType(), "非7选3考试分+选考赋分", 9999);
                    }
                }
            }
            info.put("examName", exam.getExamName());//考试名称
            info.put("examId", exam.getId());
            info.put("inStatList", inStatList);
            array.add(info);
        }
        json.put("infolist", array);
        if (CollectionUtils.isNotEmpty(subjectDtoList)) {
            Collections.sort(subjectDtoList, (o1, o2) -> {
                return o1.getSubjectCode() - o2.getSubjectCode();
            });
        }
        json.put("subjectDtoList", subjectDtoList);
        return json.toJSONString();
    }

    public void getSubjectDtoList(List<ExamSubjectDto> subjectDtoList, Map<String, String> testMap,
                                  String subjectId, String subjectName, Integer subjectCode) {
        if (!testMap.containsKey(subjectId)) {
            ExamSubjectDto subjectDto = new ExamSubjectDto();
            testMap.put(subjectId, "one");
            subjectDto.setSubjectId(subjectId);
            subjectDto.setSubjectName(subjectName);
            subjectDto.setSubjectCode(subjectCode);
            subjectDtoList.add(subjectDto);
        }
    }

    @Override
    public String getJsonByUnitIdAndExamGradeRank(String unitId, String examId,
                                                  String acadyear, String semter, String gradeCode) {
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<JSONObject>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return json.toJSONString();
        }
        if (emStatObject == null) {
            return json.toJSONString();
        }
        String section = gradeCode.trim().substring(0, 1);
        String g = gradeCode.trim().substring(1, 2);
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-2DF", "DM-RKXD-" + section}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        String xueqi = "";
        if (StringUtils.equals(semter, "1")) {
            xueqi = "第一";
        } else if (StringUtils.equals(semter, "2")) {
            xueqi = "第二";
        }
        String gName = findMapMapByMcodeIds.get("DM-RKXD-" + section).get(g).getMcodeContent();
        String title = "";
        if (exam.getExamName().contains(acadyear + "学年")) {
            title = exam.getExamName() + "考试" + gName + "年级    考试总分排名表";
        } else {
            title = acadyear + "学年" + xueqi + "学期" + exam.getExamName() + "考试" + gName + "年级    考试总分排名表";
        }
        EmStatRange emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeSubType(emStatObject.getId(), examId, ExammanageConstants.ZERO32, unitId, "2", "0");
        Map params = new HashMap();
        String summary = "年级总分平均分：" + "  分" + "         " + "年级总分最高分：" + " 分                " + "年级总分最低分：" + "  分";
        if (emStatRange != null) {
            summary = "年级总分平均分：" + emStatRange.getAvgScore() + "分" + "         " + "年级总分最高分：" + emStatRange.getMaxScore() + "分                " + "年级总分最低分：" + emStatRange.getMinScore() + "分";
        }
        params.put("title", title);
        params.put("summary", summary);
        json.put("params", params);
        List<EmStat> emStats = emStatService.findByExamIdAndObjectSubType(examId, emStatObject.getId());
        Set<String> studentIdSet = new HashSet<String>();
        Set<String> classIdSet = new HashSet<String>();
        Set<String> subjectIdSet = new HashSet<>();
        Map<String, List<EmStat>> emStaMap = new HashMap<String, List<EmStat>>();
        for (EmStat emStat : emStats) {
            if (StringUtils.equals(emStat.getSubjectId(), ExammanageConstants.SUBJECT_8) || StringUtils.equals(emStat.getSubjectId(), ExammanageConstants.SUBJECT_9)) {
                continue;
            }
            subjectIdSet.add(emStat.getSubjectId());
            studentIdSet.add(emStat.getStudentId());
            if (emStaMap.containsKey(emStat.getStudentId())) {
                List<EmStat> emStats2 = emStaMap.get(emStat.getStudentId());
                emStats2.add(emStat);
                emStaMap.put(emStat.getStudentId(), emStats2);
            } else {
                List<EmStat> emStats2 = new ArrayList<EmStat>();
                emStats2.add(emStat);
                emStaMap.put(emStat.getStudentId(), emStats2);
            }
        }
        if (CollectionUtils.isNotEmpty(studentIdSet)) {
            List<Course> courselist = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])), new TR<List<Course>>() {
            });
            Map<String, Course> courseMap = EntityUtils.getMap(courselist, Course::getId);
            List<EmStat> desStat = emStatService.findByExamIdAndObjectsAndSubjectId(examId, ExammanageConstants.ZERO32, emStatObject.getId());
            Map<String, String> stuNumMap = emExamNumService.findByExamIdAndStudentIdIn(examId, studentIdSet.toArray(new String[0]));
            List<Student> slist = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> stuMap = EntityUtils.getMap(slist, Student::getId);
            for (Entry<String, Student> entry : stuMap.entrySet()) {
                classIdSet.add(entry.getValue().getClassId());
            }
            List<Clazz> claList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> claMap = EntityUtils.getMap(claList, Clazz::getId);
            int j = 1;
            for (EmStat emStat : desStat) {
                List<EmStat> emStats2 = emStaMap.get(emStat.getStudentId());
                if (stuMap.containsKey(emStat.getStudentId())) {
                    Student student = stuMap.get(emStat.getStudentId());
                    for (int i = 0; i < 3; i++) {
                        JSONObject info = new JSONObject();
                        if (i == 0) {
                            info.put("columnOrder", 1);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            String examNo = stuNumMap.get(emStat.getStudentId());
                            info.put("value", StringUtils.isNotBlank(examNo) ? examNo : "");
                            info.put("columnName", "考号");
                        } else if (i == 1) {
                            info.put("columnOrder", 2);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            if (claMap.containsKey(student.getClassId())) {
                                info.put("value", claMap.get(student.getClassId()).getClassNameDynamic());
                            } else {
                                info.put("value", "");
                            }
                            info.put("columnName", "行政班级");
                        } else if (i == 2) {
                            info.put("columnOrder", 3);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            info.put("value", student.getStudentName());
                            info.put("columnName", "学生");
                        }
                        infolist.add(info);
                    }

                } else {
                    continue;
                }
                out:
                for (EmStat emStat2 : emStats2) {
                    if (StringUtils.equals(emStat2.getSubType(), "3")) {
                        continue;
                    }
                    if (StringUtils.equals(emStat.getSubjectId(), ExammanageConstants.ZERO32)) {
                        JSONObject info = new JSONObject();
                        info.put("rowName", j);
                        Integer subjectOrder = 0;
                        if (courseMap.containsKey(emStat2.getSubjectId())) {
                            Course course = courseMap.get(emStat2.getSubjectId());
                            if (StringUtils.equals(emStat2.getSubType(), "0")) {
                                info.put("columnOrder", course.getOrderId() + 3);
                                subjectOrder = course.getOrderId();
                                String title1 = "";
                                if (StringUtils.equals(emStat2.getSubType(), "1")) {
                                    title1 = "（选考）";
                                } else if (StringUtils.equals(emStat2.getSubType(), "2")) {
                                    title1 = "（学考）";
                                }
                                info.put("columnName", course.getSubjectName() + title1);
                            } else {
                                subjectOrder = course.getOrderId() + 6;
                                info.put("columnOrder", subjectOrder);
                                String title1 = "";
                                if (StringUtils.equals(emStat2.getSubType(), "1")) {
                                    title1 = "（选考）";
                                } else if (StringUtils.equals(emStat2.getSubType(), "2")) {
                                    title1 = "（学考）";
                                }
                                info.put("columnName", course.getSubjectName() + title1);
                            }
                        } else if (StringUtils.equals(emStat.getSubjectId(), ExammanageConstants.ZERO32)) {
                            info.put("columnOrder", 110);
                            info.put("columnName", "总分");
                        }
                        info.put("value", emStat2.getScore());
                        info.put("rowOrder", j);
                        infolist.add(info);
                    } else {

                    }
                }
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        JSONObject info = new JSONObject();
                        info.put("columnOrder", 111);
                        info.put("rowName", j);
                        info.put("value", emStat.getClassRank());
                        info.put("rowOrder", j);
                        info.put("columnName", "班级排名");
                        infolist.add(info);
                    } else {
                        JSONObject info = new JSONObject();
                        info.put("columnOrder", 112);
                        info.put("rowName", j);
                        info.put("value", emStat.getGradeRank());
                        info.put("rowOrder", j);
                        info.put("columnName", "年级排名");
                        infolist.add(info);
                    }
                }
                j++;
            }
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    @Override
    public String getJsonByUnitIdAndExamSubjectRank(String unitId,
                                                    String examId, String acadyear, String semter, String gradeCode,
                                                    String subjectId, String subType) {
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<JSONObject>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return json.toJSONString();
        }
        if (emStatObject == null) {
            return json.toJSONString();
        }
        String section = gradeCode.trim().substring(0, 1);
        String g = gradeCode.trim().substring(1, 2);
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-2DF", "DM-RKXD-" + section}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        String xueqi = "";
        if (StringUtils.equals(semter, "1")) {
            xueqi = "第一";
        } else if (StringUtils.equals(semter, "2")) {
            xueqi = "第二";
        }
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String gName = findMapMapByMcodeIds.get("DM-RKXD-" + section).get(g).getMcodeContent();
        String title = "";
        if (exam.getExamName().contains(acadyear + "学年")) {
            title = exam.getExamName() + "考试" + gName + "年级   " + course.getSubjectName() + "科目考试总分排名表";
        } else {
            title = acadyear + "学年" + xueqi + "学期" + exam.getExamName() + "考试" + gName + "年级   " + course.getSubjectName() + "科目考试总分排名表";
        }
        EmStatRange emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndTypeSubType(emStatObject.getId(), examId, subjectId, unitId, "2", subType);
        Map params = new HashMap();
        String summary = "年级总分平均分：" + "  分" + "         " + "年级总分最高分：" + " 分                " + "年级总分最低分：" + "  分";
        if (emStatRange != null) {
            summary = "年级总分平均分：" + emStatRange.getAvgScore() + "分" + "         " + "年级总分最高分：" + emStatRange.getMaxScore() + "分                " + "年级总分最低分：" + emStatRange.getMinScore() + "分";
        }
        params.put("title", title);
        String title1 = "";
        if (StringUtils.equals(subType, "1")) {
            title1 = "（选考）";
        } else if (StringUtils.equals(subType, "2")) {
            title1 = "（学考）";
        }
        params.put("header", "科目：" + course.getSubjectName() + title1);
        params.put("summary", summary);
        json.put("params", params);
        List<EmStat> emStats = emStatService.findByExamIdAndObjectsAndSubjectIdTotal(examId, subjectId, emStatObject.getId(), subType);
        List<EmStat> emStatss = new ArrayList<EmStat>();
        if (StringUtils.equals(subType, "1")) {
            emStatss = emStatService.findByExamIdAndObjectsAndSubjectIdTotal(examId, subjectId, emStatObject.getId(), StringUtils.equals(subType, "1") ? "3" : subType);
        } else if (StringUtils.equals(subType, "2") || StringUtils.equals(subType, "0")) {
            emStatss = emStatService.findByExamIdAndObjectsAndSubjectIdTotal(examId, subjectId, emStatObject.getId(), subType);
        }
        Map<String, EmStat> emStatMap = EntityUtils.getMap(emStatss, EmStat::getStudentId);
        Set<String> studentIdSet = new HashSet<String>();
        Set<String> classIdSet = new HashSet<String>();
        for (EmStat emStat : emStats) {
            studentIdSet.add(emStat.getStudentId());
        }
        if (CollectionUtils.isNotEmpty(studentIdSet)) {
            Map<String, String> stuNumMap = emExamNumService.findByExamIdAndStudentIdIn(examId, studentIdSet.toArray(new String[0]));
            List<Student> slist = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> stuMap = EntityUtils.getMap(slist, Student::getId);
            for (Entry<String, Student> entry : stuMap.entrySet()) {
                classIdSet.add(entry.getValue().getClassId());
            }
            List<Clazz> claList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> claMap = EntityUtils.getMap(claList, Clazz::getId);
            int j = 1;
            for (EmStat enEmStat : emStats) {
                if (stuMap.containsKey(enEmStat.getStudentId())) {
                    Student student = stuMap.get(enEmStat.getStudentId());
                    for (int i = 0; i < 4; i++) {
                        JSONObject info = new JSONObject();
                        if (i == 0) {
                            info.put("columnOrder", 1);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            String examNo = stuNumMap.get(enEmStat.getStudentId());
                            info.put("value", StringUtils.isNotBlank(examNo) ? examNo : "");
                            info.put("columnName", "考号");
                        } else if (i == 1) {
                            info.put("columnOrder", 2);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            if (claMap.containsKey(student.getClassId())) {
                                info.put("value", claMap.get(student.getClassId()).getClassNameDynamic());
                            } else {
                                info.put("value", "");
                            }
                            info.put("columnName", "行政班级");
                        } else if (i == 2) {
                            info.put("columnOrder", 3);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            info.put("value", student.getStudentName());
                            info.put("columnName", "学生");
                        } else if (i == 3) {
                            info.put("columnOrder", 4);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            info.put("value", enEmStat.getScore());
                            info.put("columnName", "考试分");
                        }
                        infolist.add(info);
                    }
                    int k = 4;
                    if (StringUtils.equals(enEmStat.getSubType(), "1")) {
                        JSONObject info = new JSONObject();
                        info.put("columnOrder", 5);
                        info.put("rowName", j);
                        info.put("rowOrder", j);
                        k = 5;
                        info.put("value", enEmStat.getConScore());
                        info.put("columnName", "赋分");
                        infolist.add(info);
                    }
                    for (int i = 1; i < 3; i++) {
                        JSONObject info = new JSONObject();
                        if (i == 1) {
                            info.put("columnOrder", k + i);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            info.put("value", emStatMap.get(enEmStat.getStudentId()).getClassRank());
                            info.put("columnName", "班级排名");
                        } else if (i == 2) {
                            info.put("columnOrder", k + i);
                            info.put("rowName", j);
                            info.put("rowOrder", j);
                            info.put("value", emStatMap.get(enEmStat.getStudentId()).getGradeRank());
                            info.put("columnName", "年级排名");
                        }
                        infolist.add(info);
                    }

                } else {
                    continue;
                }
                j++;
            }
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    @Override
    public String getJsonByUnitIdAndExamId(String unitId, String examId, String acadyear, String semter, String gradeCode) {
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<JSONObject>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return json.toString();
        }
        String section = gradeCode.trim().substring(0, 1);
        String g = gradeCode.trim().substring(1, 2);
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-2DF", "DM-RKXD-" + section}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        String xueqi = "";
        if (StringUtils.equals(semter, "1")) {
            xueqi = "第一";
        } else if (StringUtils.equals(semter, "2")) {
            xueqi = "第二";
        }
        String gName = findMapMapByMcodeIds.get("DM-RKXD-" + section).get(g).getMcodeContent();
        String title = "";
        if (exam.getExamName().contains(acadyear + "学年")) {
            title = acadyear + "学年" + xueqi + "学期" + exam.getExamName() + gName + "年级    非7选3+选考赋分 排名表";
        } else {
            title = exam.getExamName() + gName + "年级    非7选3+选考赋分 排名表";
        }
        EmStatRange emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ExammanageConstants.CON_SUM_ID, unitId, "2");
        Map params = new HashMap();
        String summary = "年级总分平均分：" + "  分" + "         " + "年级总分最高分：" + " 分                " + "年级总分最低分：" + "  分";
        if (emStatRange != null) {
            summary = "年级总分平均分：" + emStatRange.getAvgScore() + "分" + "         " + "年级总分最高分：" + emStatRange.getMaxScore() + "分                " + "年级总分最低分：" + emStatRange.getMinScore() + "分";
        }
        params.put("title", title);
        params.put("summary", summary);
        json.put("params", params);
        List<EmStat> emStats = emStatService.findByExamIdAndObjects(examId, emStatObject.getId());
        Set<String> studentIdSet = new HashSet<String>();
        Set<String> classIdSet = new HashSet<String>();
        Set<String> subjectIdSet = new HashSet<>();
        Map<String, List<EmStat>> emStaMap = new HashMap<String, List<EmStat>>();
        for (EmStat emStat : emStats) {
            subjectIdSet.add(emStat.getSubjectId());
            studentIdSet.add(emStat.getStudentId());
            if (emStaMap.containsKey(emStat.getStudentId())) {
                List<EmStat> emStats2 = emStaMap.get(emStat.getStudentId());
                emStats2.add(emStat);
                emStaMap.put(emStat.getStudentId(), emStats2);
            } else {
                List<EmStat> emStats2 = new ArrayList<EmStat>();
                emStats2.add(emStat);
                emStaMap.put(emStat.getStudentId(), emStats2);
            }
        }
        if (CollectionUtils.isNotEmpty(studentIdSet)) {
            List<Course> courselist = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])), new TR<List<Course>>() {
            });
            Map<String, Course> courseMap = EntityUtils.getMap(courselist, Course::getId);
            List<EmStat> desStat = emStatService.findByExamIdAndObjectsAndSubjectId(examId, ExammanageConstants.SUBJECT_9, emStatObject.getId());
            Map<String, String> stuNumMap = emExamNumService.findByExamIdAndStudentIdIn(examId, studentIdSet.toArray(new String[0]));
            List<Student> slist = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> stuMap = EntityUtils.getMap(slist, Student::getId);
            for (Entry<String, Student> entry : stuMap.entrySet()) {
                classIdSet.add(entry.getValue().getClassId());
            }
            List<Clazz> claList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> claMap = EntityUtils.getMap(claList, Clazz::getId);
            int j = 1;
            for (EmStat emStat : desStat) {
                List<EmStat> emStats2 = emStaMap.get(emStat.getStudentId());
                int k = 2;
                for (EmStat emStat2 : emStats2) {
                    if (StringUtils.equals(emStat2.getSubType(), "3") || StringUtils.equals(emStat2.getSubType(), "2") || StringUtils.equals(emStat2.getSubjectId(), ExammanageConstants.ZERO32)) {
                        continue;
                    }
                    for (int i = 0; i < (StringUtils.equals(emStat2.getSubType(), "1") ? 4 : 3); i++) {
                        JSONObject info = new JSONObject();

                        Integer subjectOrder = 0;
                        if (courseMap.containsKey(emStat2.getSubjectId())) {
                            Course course = courseMap.get(emStat2.getSubjectId());
                            if (StringUtils.equals(emStat2.getSubType(), "0")) {
                                subjectOrder = course.getOrderId();
                                String title1 = "";
                                if (StringUtils.equals(emStat2.getSubType(), "1")) {
                                    title1 = "（选考）";
                                } else if (StringUtils.equals(emStat2.getSubType(), "2")) {
                                    title1 = "（学考）";
                                    subjectOrder = subjectOrder + k;
                                } else {
                                    k++;
                                }
                                info.put("subjectName", course.getSubjectName() + title1);
                            } else {
                                subjectOrder = course.getOrderId();
                                String title1 = "";
                                if (StringUtils.equals(emStat2.getSubType(), "1")) {
                                    title1 = "（选考）";
                                } else if (StringUtils.equals(emStat2.getSubType(), "2")) {
                                    title1 = "（学考）";
                                    subjectOrder = subjectOrder + k;
                                } else {
                                    k++;
                                }
                                info.put("subjectName", course.getSubjectName() + title1);
                            }
                        } else if (StringUtils.equals(emStat2.getSubjectId(), ExammanageConstants.SUBJECT_9)) {
                            subjectOrder = 10000;
                            info.put("subjectName", "总分（非七选三科目+选考赋分）");
                        } else if (StringUtils.equals(emStat2.getSubjectId(), ExammanageConstants.SUBJECT_8)) {
                            if (subjectOrder < 4) {
                                subjectOrder = 4;
                            } else {
                                subjectOrder = subjectOrder + 1;
                            }
                            info.put("subjectName", "非七选三科目总分");
                        }
                        if (i == 0) {
                            info.put("subName", "考试分");
                            info.put("value", emStat2.getScore());
                        } else if (i == 1) {
                            info.put("subName", "班级排名");
                            info.put("value", emStat2.getClassRank());
                        } else if (i == 2) {
                            info.put("subName", "年级排名");
                            info.put("value", emStat2.getGradeRank());
                        } else if (StringUtils.equals(emStat2.getSubType(), "1") && i == 3) {
                            info.put("subName", "赋分");
                            info.put("value", emStat2.getConScore());
                        }
                        if (stuMap.containsKey(emStat.getStudentId())) {
                            Student student = stuMap.get(emStat.getStudentId());
                            info.put("studentName", student.getStudentName());
                            if (claMap.containsKey(student.getClassId())) {
                                info.put("className", claMap.get(student.getClassId()).getClassNameDynamic());
                            } else {
                                info.put("className", "");
                            }
                        } else {
                            info.put("studentName", "学生已删除");
                            info.put("className", "");
                        }
                        info.put("subjectOrder", subjectOrder);
                        info.put("columnOrder", j);
                        String examNo = stuNumMap.get(emStat.getStudentId());
                        info.put("examNo", StringUtils.isNotBlank(examNo) ? examNo : "");
                        infolist.add(info);
                    }
                }
                j++;
            }
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    @Override
    public String getJsonByUnitIdAndExamIdAndClassId(String unitId,
                                                     String examId, String acadyear, String semter, String classId) {
        JSONObject json = new JSONObject();
        List<JSONObject> infolist = new ArrayList<JSONObject>();
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return json.toString();
        }
        if (emStatObject == null) {
            return json.toString();
        }
        String xueqi = "";
        if (StringUtils.equals(semter, "1")) {
            xueqi = "第一";
        } else if (StringUtils.equals(semter, "2")) {
            xueqi = "第二";
        }
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        String title = "";
        if (exam.getExamName().contains(acadyear + "学年")) {
            title = exam.getExamName() + "考试" + clazz.getClassNameDynamic() + "行政班    非7选3+选考赋分 排名表";
        } else {
            title = acadyear + "学年" + xueqi + "学期" + exam.getExamName() + "考试" + clazz.getClassNameDynamic() + "行政班    非7选3+选考赋分 排名表";
        }
        EmStatRange emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(emStatObject.getId(), examId, ExammanageConstants.SUBJECT_8, unitId, "2");
        Map params = new HashMap();
        String summary = "班级总分平均分：" + "  分" + "         " + "班级总分最高分：" + " 分                " + "班级总分最低分：" + "  分";
        if (emStatRange != null) {
            summary = "班级总分平均分：" + emStatRange.getAvgScore() + "分" + "         " + "班级总分最高分：" + emStatRange.getMaxScore() + "分                " + "班级总分最低分：" + emStatRange.getMinScore() + "分";
        }
        params.put("header", "行政班：" + clazz.getClassNameDynamic());
        params.put("title", title);
        params.put("summary", summary);
        json.put("params", params);
        List<Student> studentlist = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
        });
        Set<String> studentIdSet = new HashSet<String>();
        for (Student student : studentlist) {
            studentIdSet.add(student.getId());
        }
        List<EmStat> emStats = emStatService.findByStudentIds(emStatObject.getId(), examId, null, studentIdSet.toArray(new String[0]));
        Set<String> subjectIdSet = new HashSet<>();
        Map<String, List<EmStat>> emStaMap = new HashMap<String, List<EmStat>>();
        for (EmStat emStat : emStats) {
            subjectIdSet.add(emStat.getSubjectId());
            if (emStaMap.containsKey(emStat.getStudentId())) {
                List<EmStat> emStats2 = emStaMap.get(emStat.getStudentId());
                emStats2.add(emStat);
                emStaMap.put(emStat.getStudentId(), emStats2);
            } else {
                List<EmStat> emStats2 = new ArrayList<EmStat>();
                emStats2.add(emStat);
                emStaMap.put(emStat.getStudentId(), emStats2);
            }
        }
        if (CollectionUtils.isNotEmpty(studentIdSet)) {
            List<Course> courselist = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])), new TR<List<Course>>() {
            });
            Map<String, Course> courseMap = EntityUtils.getMap(courselist, Course::getId);
            List<EmStat> desStat = emStatService.findByExamIdAndObjectsAndSubjectIdAndClassId(examId, ExammanageConstants.SUBJECT_9, emStatObject.getId(), classId);
            Map<String, String> stuNumMap = emExamNumService.findByExamIdAndStudentIdIn(examId, studentIdSet.toArray(new String[0]));
            List<Student> slist = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> stuMap = EntityUtils.getMap(slist, Student::getId);
            int j = 1;
            for (EmStat emStat : desStat) {
                List<EmStat> emStats2 = emStaMap.get(emStat.getStudentId());
                for (EmStat emStat2 : emStats2) {
                    if (StringUtils.equals(emStat2.getSubType(), "3") || StringUtils.equals(emStat2.getSubType(), "2") || StringUtils.equals(emStat2.getSubjectId(), ExammanageConstants.ZERO32)) {
                        continue;
                    }
                    for (int i = 0; i < (StringUtils.equals(emStat2.getSubType(), "1") ? 4 : 3); i++) {
                        JSONObject info = new JSONObject();
                        info.put("columnOrder", j);
                        String examNo = stuNumMap.get(emStat.getStudentId());
                        info.put("examNo", StringUtils.isNotBlank(examNo) ? examNo : "");
                        Integer subjectOrder = 0;
                        if (courseMap.containsKey(emStat2.getSubjectId())) {
                            Course course = courseMap.get(emStat2.getSubjectId());
                            subjectOrder = course.getOrderId();
                            String title1 = "";
                            if (StringUtils.equals(emStat2.getSubType(), "1")) {
                                title1 = "（选考）";
                            } else if (StringUtils.equals(emStat2.getSubType(), "2")) {
                                title1 = "（学考）";
                            }
                            info.put("subjectName", course.getSubjectName() + title1);
                        } else if (StringUtils.equals(emStat2.getSubjectId(), ExammanageConstants.SUBJECT_9)) {
                            subjectOrder = 10000;
                            info.put("subjectName", "总分（非七选三科目+选考赋分）");
                        } else if (StringUtils.equals(emStat2.getSubjectId(), ExammanageConstants.SUBJECT_8)) {
                            if (subjectOrder < 4) {
                                subjectOrder = 4;
                            } else {
                                subjectOrder = subjectOrder + 1;
                            }
                            info.put("subjectName", "非七选三科目总分");
                        }
                        if (i == 0) {
                            info.put("subName", "考试分");
                            info.put("value", emStat2.getScore());
                        } else if (i == 1) {
                            info.put("subName", "班级排名");
                            info.put("value", emStat2.getClassRank());
                        } else if (i == 2) {
                            info.put("subName", "年级排名");
                            info.put("value", emStat2.getGradeRank());
                        } else if (StringUtils.equals(emStat2.getSubType(), "1") && i == 3) {
                            info.put("subName", "赋分");
                            info.put("value", emStat2.getConScore());
                        }
                        if (stuMap.containsKey(emStat.getStudentId())) {
                            Student student = stuMap.get(emStat.getStudentId());
                            info.put("studentName", student.getStudentName());
                        } else {
                            info.put("studentName", "学生已删除");
                        }
                        info.put("subjectOrder", subjectOrder);
                        infolist.add(info);
                    }
                }
                j++;
            }
        }
        json.put("infolist", infolist);
        return json.toJSONString();
    }

    /**
     * 获取rangeName
     *
     * @param statRange
     * @param clazzMap
     * @param teachClassMap
     * @return
     */
    public String getRangeName(EmStatRange statRange, Map<String, Clazz> clazzMap, Map<String, TeachClass> teachClassMap) {
        String rangeName = "";
        if (EmStatRange.RANGE_SCHOOL.equals(statRange.getRangeType())) {
            rangeName = "年级全体";
        } else if (clazzMap.containsKey(statRange.getRangeId())) {
            rangeName = clazzMap.get(statRange.getRangeId()).getClassNameDynamic();
            statRange.setClassNameOrder(clazzMap.get(statRange.getRangeId()).getClassCode());
        } else if (teachClassMap.containsKey(statRange.getRangeId())) {
            rangeName = teachClassMap.get(statRange.getRangeId()).getName();
        }
        return rangeName;
    }

    /**
     * 获取statRangeId对应的EmStatSpace集合   map（赋分和非赋分有区别）
     *
     * @param statSpaceList
     * @return
     */
    public Map<String, List<EmStatSpace>> getStatSpaceListMap(List<EmStatSpace> statSpaceList, Map<String, EmSpaceItem> spaceItemMap) {
        Map<String, List<EmStatSpace>> listMap = new HashMap<>();
        boolean flag = spaceItemMap == null;
        for (EmStatSpace item : statSpaceList) {
            if (!flag) {//null 赋分的情况进来 不需要间距
                EmSpaceItem spaceItem = spaceItemMap.get(item.getSpaceItemId());
                if (spaceItem == null)
                    continue;
                item.setSpaceItemUpScore(spaceItem.getUpScore());
                item.setSpaceItemName(spaceItem.getName());
            }
            if (!listMap.containsKey(item.getStatRangeId())) {
                listMap.put(item.getStatRangeId(), new ArrayList<>());
            }
            listMap.get(item.getStatRangeId()).add(item);
        }
        for (Entry<String, List<EmStatSpace>> entry : listMap.entrySet()) {
            Collections.sort(entry.getValue(), (o1, o2) -> {
                if (!flag) {
                    if (o2.getSpaceItemUpScore() != null && o1.getSpaceItemUpScore() != null) {
                        return o2.getSpaceItemUpScore().compareTo(o1.getSpaceItemUpScore());
                    }
                } else {
                    return o1.getScoreRank() - o2.getScoreRank();
                }
                return 0;
            });
        }
        return listMap;
    }

    public String getString(Object obj) {
        if (obj == null)
            return "0";
        return obj.toString();
    }

    public String getFloatString(Float f, int i) {
        BigDecimal b = new BigDecimal(f);
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    public String getTitle(String unitId, String acadyear, String semester, String examId, String gradeCode,
                           String gradeName, String subjectName, String classId, String className, String name) {
        StringBuilder title = new StringBuilder();
        EmExamInfo exam = emExamInfoService.findOne(examId);
        String examName = exam == null ? "" : exam.getExamName();
        if (StringUtils.isNotBlank(acadyear)) {
            if (StringUtils.isBlank(examName) || !examName.contains(acadyear)) {
                title.append(acadyear + "学年");
            }
        }
        String strSem = "";
        if ("1".equals(semester)) {
            strSem = "第一学期";
        } else {
            strSem = "第二学期";
        }
        if (StringUtils.isBlank(examName) || !examName.contains(strSem)) {
            title.append(strSem);
        }
        title.append(examName);
        if (StringUtils.isNotBlank(gradeName)) {
            title.append(gradeName + "年级  ");
        } else if (StringUtils.isNotBlank(gradeCode)) {
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), new TR<List<Grade>>() {
            });
            if (CollectionUtils.isNotEmpty(gradeList)) {
                title.append(gradeList.get(0).getGradeName() + "年级  ");
            }
        }
        if (StringUtils.isNotBlank(subjectName)) {
            title.append(subjectName);
        }
        if (StringUtils.isNotBlank(className)) {
            title.append(className);
        } else if (StringUtils.isNotBlank(classId)) {
            title.append(getClassNameById(classId));
        }
        title.append(StringUtils.isNotBlank(name) ? name : "");
        return title.toString();
    }

    /**
     * 获取行政班或教学班名称
     *
     * @param classId
     * @return
     */
    public String getClassNameById(String classId) {
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        if (clazz != null) {
            return clazz.getClassNameDynamic();
        } else {
            TeachClass teachClass = SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
            return teachClass == null ? "" : teachClass.getName();
        }
    }

    @Override
    public List<Course> getSubList(String unitId, String examId, String gkType) {
        List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(examId);
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        List<EmSubjectInfo> elist = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
        });
        Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
        if (StringUtils.equals(gkType, "0")) {
            //取所有科目
            for (EmSubjectInfo sub : infoList) {
                if (StringUtils.equals(sub.getInputType(), "S")) {
                    elist.add(sub);
                }
            }
            if (CollectionUtils.isNotEmpty(elist)) {
                Set<String> subjectIds = EntityUtils.getSet(elist, EmSubjectInfo::getSubjectId);
                Map<String, Course> courseMap = EntityUtils.getMap(SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
                }), Course::getId);
                for (EmSubjectInfo e : elist) {
                    Course c = courseMap.get(e.getSubjectId());
                    if (id73Set.contains(e.getSubjectId())) {
                        if (StringUtils.equals(e.getGkSubType(), "0")) {
                            if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
                                Course c1 = new Course();
                                c1.setCourseTypeId("1");
                                c1.setId(c.getId());
                                c1.setSubjectName(c.getSubjectName());
                                courseList.add(c1);
                                Course c2 = new Course();
                                c2.setCourseTypeId("2");
                                c2.setId(c.getId());
                                c2.setSubjectName(c.getSubjectName());
                                courseList.add(c2);
                            } else {
                                c.setCourseTypeId(e.getGkSubType() == null ? "0" : e.getGkSubType());
                                courseList.add(c);
                            }
                        } else {
                            c.setCourseTypeId(e.getGkSubType() == null ? "0" : e.getGkSubType());
                            courseList.add(c);
                        }
                    } else {
                        c.setCourseTypeId("0");
                        courseList.add(c);
                    }
                }

            }
        } else if (StringUtils.equals(gkType, "1")) {
            //取选考科目
            for (EmSubjectInfo sub : infoList) {
                if (StringUtils.isNotBlank(sub.getGkSubType()) && !StringUtils.equals(sub.getGkSubType(), "2")
                        && id73Set.contains(sub.getSubjectId())
                        && StringUtils.equals(sub.getInputType(), "S")) {
                    elist.add(sub);
                }
            }
            if (CollectionUtils.isNotEmpty(elist)) {
                Set<String> subjectIds = EntityUtils.getSet(elist, EmSubjectInfo::getSubjectId);
                courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
                });
                for (Course e : courseList) {
                    e.setCourseTypeId("1");
                }
            }
        }
        return courseList;
    }

}
