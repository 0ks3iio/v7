package net.zdsoft.credit.data.service.impl;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.dao.CreditStatDao;
import net.zdsoft.credit.data.entity.*;
import net.zdsoft.credit.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("creditStatService")
public class CreditStatServiceImpl extends BaseServiceImpl<CreditStat, String> implements CreditStatService {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleService;
    @Autowired
    SchoolRemoteService schoolRemoteService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    McodeRemoteService mcodeRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    StudentRemoteService studentRemoteService;
    @Autowired
    CreditStatService creditStatService;
    @Autowired
    CreditSetService creditSetService;
    @Autowired
    CreditModuleInfoService creditModuleInfoService;
    @Autowired
    CreditExamSetService creditExamSetService;
    @Autowired
    CreditDailySetService creditDailySetService;
    @Autowired
    CourseRemoteService courseRemoteService;
    @Autowired
    CreditDailyInfoService creditDailyInfoService;
    @Autowired
    ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    CreditStatDao creditStatDao;
    @Autowired
    CreditStatLogService creditStatLogService;
    @Autowired
    CreditPatchStudentService creditPatchStudentService;

    @Override
    protected BaseJpaRepositoryDao<CreditStat, String> getJpaDao() {
        return creditStatDao;
    }

    @Override
    protected Class<CreditStat> getEntityClass() {
        return CreditStat.class;
    }

    @Override
    public void sumCreditStat(String gradeId, String year, String semester, String unitId) {
        Set<String> classIds = new HashSet<>();
        Set<String> studentIds = new HashSet<>();
        Set<String> subjectIds = new HashSet<>();
        Map<String,String> classTypeMap = new HashMap<>();
        Map<String,List<CreditDailyInfo>> dailyInfoMap = new HashMap<>();
        Map<String,List<CreditModuleInfo>> moduleInfoMap = new HashMap<>();
        List<CreditStat> creditStatsTotal = new ArrayList<>();
        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{year, unitId, semester});
        List<CreditDailyInfo> creditDailyInfos = creditDailyInfoService.findListByNullDailyId(year,unitId, semester, gradeId);
        //
        List<CreditModuleInfo> creditModuleInfos = creditModuleInfoService.findListBy(new String[]{"acadyear", "unitId", "semester","gradeId","scoreType"},new String[]{year, unitId, semester,gradeId,CreditConstants.CREDIT_SCORE_TYPE_3});
        if(CollectionUtils.isNotEmpty(creditDailyInfos)) {
            Set<String> ids = creditDailyInfos.stream().map(CreditDailyInfo::getClassId).collect(Collectors.toSet());
            Set<String> stuIds = creditDailyInfos.stream().map(CreditDailyInfo::getStudentId).collect(Collectors.toSet());
            Set<String> subIds = creditDailyInfos.stream().map(CreditDailyInfo::getSubjectId).collect(Collectors.toSet());
            dailyInfoMap = creditDailyInfos.stream().collect(Collectors.groupingBy(CreditDailyInfo::getStudentId));
            classIds.addAll(ids);
            studentIds.addAll(stuIds);
            subjectIds.addAll(subIds);
        }
        if(CollectionUtils.isNotEmpty(creditModuleInfos)){
            Set<String> ids = creditModuleInfos.stream().map(CreditModuleInfo::getClassId).collect(Collectors.toSet());
            Set<String> stuIds = creditModuleInfos.stream().map(CreditModuleInfo::getStudentId).collect(Collectors.toSet());
            Set<String> subIds = creditModuleInfos.stream().map(CreditModuleInfo::getSubjectId).collect(Collectors.toSet());
            moduleInfoMap= creditModuleInfos.stream().collect(Collectors.groupingBy(CreditModuleInfo::getStudentId));
            classIds.addAll(ids);
            studentIds.addAll(stuIds);
            subjectIds.addAll(subIds);
        }
        if(CollectionUtils.isNotEmpty(classIds)){
            for(String classId:classIds){
                for(CreditDailyInfo creditDailyInfo:creditDailyInfos){
                    if(creditDailyInfo.getClassId().equals(classId)){
                        classTypeMap.put(classId,creditDailyInfo.getClassType());
                        continue;
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(classIds)){
            for(String classId:classIds){
                for(CreditModuleInfo creditModuleInfo:creditModuleInfos){
                    if(creditModuleInfo.getClassId().equals(classId)){
                        classTypeMap.put(classId,creditModuleInfo.getClassType());
                        continue;
                    }
                }
            }
        }
        List<CreditPatchStudent> creditPatchStudents = new ArrayList<>();
        //统计总分
        List<CreditStat> creditStatSum = new ArrayList<>();
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
        });
        Map<String,Student> stringStudentMap = studentList.stream().collect(Collectors.toMap(Student::getId,Function.identity()));
        if(CollectionUtils.isNotEmpty(studentIds)){

            for(String studentId:studentIds) {

                if(MapUtils.isNotEmpty(dailyInfoMap)){
                    List<CreditDailyInfo> creditDailyInfo = dailyInfoMap.get(studentId);
                    List<CreditModuleInfo> list = moduleInfoMap.get(studentId);
                    Map<String, List<CreditModuleInfo>> map = new HashMap<>();
                    Map<String, List<CreditDailyInfo>> stringListMap = new HashMap<>();
                    if(CollectionUtils.isNotEmpty(list)) {
                        map = list.stream().collect(Collectors.groupingBy(CreditModuleInfo::getSubjectId));
                    }
                    if(CollectionUtils.isNotEmpty(creditDailyInfo)) {
                        stringListMap = creditDailyInfo.stream().collect(Collectors.groupingBy(CreditDailyInfo::getSubjectId));
                    }
//                    if(studentId.equals("2DDD57A989BA442F8657D653D1C0A362")){
//                        System.out.println(1111);
//                    }
                    if (CollectionUtils.isNotEmpty(subjectIds)) {
                        for (String subjectId : subjectIds) {
//                            if(subjectId.equals("00000000000000000000000000000007")){
//                                System.out.println(1111);
//                            }
                            List<CreditModuleInfo> creditModuleInfos1 = new ArrayList<>();
                            if(MapUtils.isNotEmpty(map)) {
                                creditModuleInfos1 = map.get(subjectId);
                            }
                            List<CreditDailyInfo> creditDailyInfos1 = new ArrayList<>();
                            if(MapUtils.isNotEmpty(stringListMap)) {
                                creditDailyInfos1 = stringListMap.get(subjectId);
                            }
                            Map<String, CreditDailyInfo> creditDailyInfoMap = new HashMap<>();
                            if (CollectionUtils.isNotEmpty(creditDailyInfos1)) {
                                creditDailyInfoMap = creditDailyInfos1.stream().collect(Collectors.toMap(CreditDailyInfo::getClassId, Function.identity()));
                            }
                            Map<String, List<CreditModuleInfo>> listMap = new HashMap<>();
                            if (CollectionUtils.isNotEmpty(creditModuleInfos1)) {
                                listMap = creditModuleInfos1.stream().collect(Collectors.groupingBy(CreditModuleInfo::getClassId));
                            }
                            if (CollectionUtils.isNotEmpty(classIds)) {
                                for (String classId : classIds) {
                                    CreditStat creditStat = new CreditStat();
                                    creditStat.setId(UuidUtils.generateUuid());
                                    creditStat.setStudentId(studentId);
                                    creditStat.setAcadyear(year);
                                    creditStat.setSemester(semester);
                                    creditStat.setClassId(classId);
                                    //                creditStat.setDailySetId(creditSet.getId());
                                    creditStat.setGradeId(gradeId);
                                    creditStat.setUnitId(unitId);
                                    if (stringStudentMap.get(studentId) != null) {
                                        creditStat.setStudentName(stringStudentMap.get(studentId).getStudentName());
                                        creditStat.setStudentCode(stringStudentMap.get(studentId).getStudentCode());
                                    }
                                    creditStat.setSetId(creditSet.getId());
                                    if (StringUtils.isNotBlank(classTypeMap.get(classId))) {
                                        creditStat.setClassType(classTypeMap.get(classId));
                                    }
                                    creditStat.setSubjectId(subjectId);
                                    creditStat.setSetType("4");
                                    creditStat.setScore(0f);
                                    if (creditDailyInfoMap.get(classId) != null) {
                                        creditStat.setScore(creditDailyInfoMap.get(classId).getScore());
                                        if (MapUtils.isNotEmpty(listMap)) {
                                            if (CollectionUtils.isNotEmpty(listMap.get(classId))) {
                                                List<CreditModuleInfo> list1 = listMap.get(classId);
                                                Set<String> strings = list1.stream().map(CreditModuleInfo::getExamType).collect(Collectors.toSet());
                                                for (CreditModuleInfo creditModuleInfo : list1) {
                                                    if (strings.contains(CreditConstants.CREDIT_EXAM_TYPE_3)) {
                                                        if (creditModuleInfo.getScore() != null) {
                                                            if (!creditModuleInfo.getExamType().equals(CreditConstants.CREDIT_EXAM_TYPE_2)&&creditModuleInfo.getScoreType().equals(CreditConstants.CREDIT_SCORE_TYPE_3)) {
                                                                creditStat.setScore(creditStat.getScore() + creditModuleInfo.getScore());
                                                            }
                                                        }
                                                    } else {
                                                        if (creditModuleInfo.getScore() != null) {
                                                            if (!creditModuleInfo.getExamType().equals(CreditConstants.CREDIT_EXAM_TYPE_3)&&creditModuleInfo.getScoreType().equals(CreditConstants.CREDIT_SCORE_TYPE_3)) {
                                                                creditStat.setScore(creditStat.getScore() + creditModuleInfo.getScore());
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                        creditStatSum.add(creditStat);
                                    } else if (CollectionUtils.isNotEmpty(listMap.get(classId))) {
                                        List<CreditModuleInfo> list1 = listMap.get(classId);
                                        Set<String> strings = list1.stream().map(CreditModuleInfo::getScoreType).collect(Collectors.toSet());
                                        for (CreditModuleInfo creditModuleInfo : list1) {
                                            if (strings.contains(CreditConstants.CREDIT_EXAM_TYPE_3)) {
                                                if (creditModuleInfo.getScore() != null) {
                                                    if (!creditModuleInfo.getExamType().equals(CreditConstants.CREDIT_EXAM_TYPE_2)&&creditModuleInfo.getScoreType().equals(CreditConstants.CREDIT_SCORE_TYPE_3)) {
                                                        creditStat.setScore(creditStat.getScore() + creditModuleInfo.getScore());
                                                    }
                                                }
                                            } else {
                                                if (creditModuleInfo.getScore() != null) {
                                                    if (!creditModuleInfo.getExamType().equals(CreditConstants.CREDIT_EXAM_TYPE_3)&&creditModuleInfo.getScoreType().equals(CreditConstants.CREDIT_SCORE_TYPE_3)) {
                                                        creditStat.setScore(creditStat.getScore() + creditModuleInfo.getScore());
                                                    }
                                                }
                                            }

                                        }
                                        creditStatSum.add(creditStat);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for(CreditStat creditStat:creditStatSum) {
//            if(creditStat.getStudentId().equals("2DDD57A989BA442F8657D653D1C0A362")){
//                System.out.println(1111);
//            }
            if (creditStat.getScore() < creditSet.getPassLine()) {
            	Set<String> ids = new HashSet<>();
                String subjectId =creditStat.getSubjectId();
                String studentId = creditStat.getStudentId();
                String classId = creditStat.getClassId();
                //不及格学生数据
                if (!ids.contains(subjectId + studentId)
                		&& moduleInfoMap.containsKey(studentId)) {
                    CreditPatchStudent creditPatchStudent = new CreditPatchStudent();
                    creditPatchStudent.setAcadyear(year);
                    creditPatchStudent.setClassId(classId);
                    if (StringUtils.isNotBlank(classTypeMap.get(classId))) {
                        creditStat.setClassType(classTypeMap.get(classId));
                    }
                    creditPatchStudent.setGradeId(gradeId);
                    creditPatchStudent.setSemester(semester);
                    creditPatchStudent.setStudentCode(creditStat.getStudentCode());
                    creditPatchStudent.setStudentName(creditStat.getStudentName());
                    creditPatchStudent.setStudentId(studentId);
                    creditPatchStudent.setSubjectId(subjectId);
                    creditPatchStudent.setUnitId(unitId);
                    creditPatchStudent.setId(UuidUtils.generateUuid());
                    creditPatchStudents.add(creditPatchStudent);
                    ids.add(creditPatchStudent.getSubjectId() + creditPatchStudent.getStudentId());
                }
            }
        }

        List<CreditStat> creditStatProcess = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(studentIds)){
            for(String studentId:studentIds){
                if(MapUtils.isNotEmpty(dailyInfoMap)) {
                    List<CreditDailyInfo> creditDailyInfo = dailyInfoMap.get(studentId);
                    List<CreditModuleInfo> list = moduleInfoMap.get(studentId);
                    Map<String, List<CreditModuleInfo>> map = new HashMap<>();
                    Map<String, List<CreditDailyInfo>> stringListMap = new HashMap<>();
                    if(CollectionUtils.isNotEmpty(list)) {
                        map = list.stream().collect(Collectors.groupingBy(CreditModuleInfo::getSubjectId));
                    }
                    if(CollectionUtils.isNotEmpty(creditDailyInfo)) {
                        stringListMap = creditDailyInfo.stream().collect(Collectors.groupingBy(CreditDailyInfo::getSubjectId));
                    }
                    if (CollectionUtils.isNotEmpty(subjectIds)) {
                        for (String subjectId : subjectIds) {
                            List<CreditModuleInfo> creditModuleInfos1 = new ArrayList<>();
                            if(MapUtils.isNotEmpty(map)) {
                                creditModuleInfos1 = map.get(subjectId);
                            }
                            List<CreditDailyInfo> creditDailyInfos1 = new ArrayList<>();
                            if(MapUtils.isNotEmpty(stringListMap)) {
                                creditDailyInfos1 = stringListMap.get(subjectId);
                            }
                            Map<String, CreditDailyInfo> creditDailyInfoMap = new HashMap<>();
                            if (CollectionUtils.isNotEmpty(creditDailyInfos1)) {
                                creditDailyInfoMap = creditDailyInfos1.stream().collect(Collectors.toMap(CreditDailyInfo::getClassId, Function.identity()));
                            }
                            Map<String, List<CreditModuleInfo>> listMap = new HashMap<>();
                            if (CollectionUtils.isNotEmpty(creditModuleInfos1)) {
                                listMap = creditModuleInfos1.stream().collect(Collectors.groupingBy(CreditModuleInfo::getClassId));
                            }
                            if (CollectionUtils.isNotEmpty(classIds)) {
                                for (String classId : classIds) {
                                    CreditStat creditStat = new CreditStat();
                                    creditStat.setId(UuidUtils.generateUuid());
                                    creditStat.setStudentId(studentId);
                                    creditStat.setAcadyear(year);
                                    creditStat.setSemester(semester);
                                    creditStat.setClassId(classId);
                                    //                creditStat.setDailySetId(creditSet.getId());
                                    creditStat.setGradeId(gradeId);
                                    creditStat.setUnitId(unitId);
                                    if (stringStudentMap.get(studentId) != null) {
                                        creditStat.setStudentName(stringStudentMap.get(studentId).getStudentName());
                                        creditStat.setStudentCode(stringStudentMap.get(studentId).getStudentCode());
                                    }
                                    creditStat.setSetId(creditSet.getId());
                                    if (StringUtils.isNotBlank(classTypeMap.get(classId))) {
                                        creditStat.setClassType(classTypeMap.get(classId));
                                    }
                                    creditStat.setSubjectId(subjectId);
                                    creditStat.setSetType("3");
                                    creditStat.setScore(0f);
                                    if (creditDailyInfoMap.get(classId) != null) {
                                        creditStat.setScore(creditDailyInfoMap.get(classId).getScore());
                                        if(MapUtils.isNotEmpty(listMap)) {
                                            if (CollectionUtils.isNotEmpty(listMap.get(classId))) {
                                                List<CreditModuleInfo> list1 = listMap.get(classId);
                                                for (CreditModuleInfo creditModuleInfo : list1) {
                                                    if (creditModuleInfo.getScore() != null && creditModuleInfo.getExamType().equals(CreditConstants.CREDIT_EXAM_TYPE_1)) {
                                                        creditStat.setScore(creditStat.getScore() + creditModuleInfo.getScore());
                                                    }
                                                }
                                            }
                                        }
                                        creditStatProcess.add(creditStat);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(creditPatchStudents)){
            List<CreditPatchStudent> list = creditPatchStudentService.findListBy(new String[]{"gradeId","acadyear","semester","unitId"},new String[]{gradeId,year,semester,unitId});
            if(CollectionUtils.isNotEmpty(list)) {
                creditPatchStudentService.deleteAll(list.toArray(new CreditPatchStudent[0]));
            }
            creditPatchStudentService.saveAll(creditPatchStudents.toArray(new CreditPatchStudent[0]));
        }
        creditStatsTotal.addAll(creditStatProcess);
		creditStatsTotal.addAll(creditStatSum);
        creditStatService.saveAll(creditStatsTotal.toArray(new CreditStat[0]));
        creditStatLogService.deleteByParams(year,semester,gradeId);
        CreditStatLog creditStatLog = new CreditStatLog();
        creditStatLog.setAcadyear(year);
        creditStatLog.setSemester(semester);
        creditStatLog.setCreationTime(new Date());
        creditStatLog.setGradeId(gradeId);
        creditStatLog.setUnitId(unitId);
        creditStatLog.setHasStat("1");
        creditStatLog.setSetId(creditSet.getId());
        creditStatLog.setId(UuidUtils.generateUuid());
        creditStatLogService.save(creditStatLog);
    }

    @Override
    public List<CreditStat> findBySubDailySetIds(String[] subDailySetIds) {
        return creditStatDao.findBySubDailySetIds(subDailySetIds);
    }

    @Override
    public List<CreditStat> findBydailySetIds(String[] dailySetIds) {
        return creditStatDao.findBydailySetIds(dailySetIds);
    }

    @Override
    public void deleteByGradeId(String gradeId, String year, String semester) {
        creditStatDao.delByGradeId(gradeId, year, semester);
    }

    @Override
    public String saveImportData(List<String[]> arrDatas, LoginInfo loginInfo) {
        int successCount = 0;
        String[] errorData = null;
        List<String[]> errorDataList = new ArrayList<String[]>();

        if (CollectionUtils.isEmpty(arrDatas)) {
            errorData = new String[4];
            errorData[0] = "1";
            errorData[1] = "";
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return "";
        }
        int totalCount = arrDatas.size();
        Set<String> stuCodeSet = new HashSet<String>();
        List<CreditModuleInfo> insertList = new ArrayList<CreditModuleInfo>();
        for (String[] arr : arrDatas) {
            stuCodeSet.add(arr[0]);
        }
        List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(loginInfo.getUnitId(), stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {
        });
        Map<String, Student> stuMap = new HashMap<String, Student>();
        for (Student stu : stuList) {
            stuMap.put(stu.getStudentCode(), stu);
        }

        Student stu = null;
        CreditModuleInfo creditModuleInfo = null;
        Set<String> stuIdSet = new HashSet<>();
        for (String[] arr : arrDatas) {
            String stuCode = arr[0] == null ? "" : arr[0].trim();
            String stuName = arr[1] == null ? "" : arr[1].trim();
            String score = arr[2] == null ? "" : arr[2].trim();
            String acadyear = arr[3] == null ? "" : arr[3].trim();
            String semester = arr[4] == null ? "" : arr[4].trim();
            String subjectName = arr[5] == null ? "" : arr[5].trim();
            if (StringUtils.isBlank(stuCode)) {
                errorData = new String[4];
                errorData[0] = errorDataList.size() + 1 + "";
                errorData[1] = "学号";
                errorData[2] = "";
                errorData[3] = "学号不能为空";
                errorDataList.add(errorData);
                continue;
            } else {
                stu = stuMap.get(stuCode);
                if (stu == null) {
                    errorData = new String[4];
                    errorData[0] = errorDataList.size() + 1 + "";
                    errorData[1] = "学号";
                    errorData[2] = stuCode;
                    errorData[3] = "不存在该学号所属的学生";
                    errorDataList.add(errorData);
                    continue;
                } else {
                    if (!stuName.equals(stu.getStudentName())) {
                        errorData = new String[4];
                        errorData[0] = errorDataList.size() + 1 + "";
                        errorData[1] = "学号";
                        errorData[2] = "姓名：" + stuName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学号：" + stuCode;
                        errorData[3] = "学生姓名与该学号不匹配";
                        errorDataList.add(errorData);
                        continue;
                    }
                }
                if (stuIdSet.contains(stu.getId()+"_"+subjectName)) {
                    errorData = new String[4];
                    errorData[0] = errorDataList.size() + 1 + "";
                    errorData[1] = "学号";
                    errorData[2] = stuCode;
                    errorData[3] = "该学生科目成绩存在重复";
                    errorDataList.add(errorData);
                    continue;
                }
            }
            if (StringUtils.isBlank(stuName)) {
                errorData = new String[4];
                errorData[0] = errorDataList.size() + 1 + "";
                errorData[1] = "学生姓名";
                errorData[2] = "";
                errorData[3] = "学生姓名不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(score)) {
                errorData = new String[4];
                errorData[0] = errorDataList.size() + 1 + "";
                errorData[1] = "学号";
                errorData[2] = "";
                errorData[3] = "学号不能为空";
                errorDataList.add(errorData);
                continue;
            } else if (!(Float.parseFloat(score) >= 0 && Float.parseFloat(score) <= 999999)) {
                errorData = new String[4];
                errorData[0] = errorDataList.size() + 1 + "";
                errorData[1] = "分数";
                errorData[2] = arr[4];
                errorData[3] = "请输入正确的分数，0-999999并且可以保留一位小数的数字！";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isNotBlank(acadyear)) {
                List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
                if (!acadyearList.contains(acadyear)) {
                    errorData = new String[4];
                    errorData[0] = errorDataList.size() + 1 + "";
                    errorData[1] = "学年";
                    errorData[2] = acadyear;
                    errorData[3] = "请填写正确的学年";
                    errorDataList.add(errorData);
                    continue;
                }
            }
            if (StringUtils.isNotBlank(semester)) {
                if ("第一学期".equals(semester)) {
                    semester = "1";
                } else if ("第二学期".equals(semester)) {
                    semester = "2";
                } else {
                    errorData = new String[4];
                    errorData[0] = errorDataList.size() + 1 + "";
                    errorData[1] = "学期";
                    errorData[2] = semester;
                    errorData[3] = "请填写正确的学期";
                    errorDataList.add(errorData);
                    continue;
                }
            }
            List<Course> courseList = new ArrayList<>();
            if (StringUtils.isNotBlank(subjectName)) {
                courseList = SUtils.dt(courseRemoteService.findBySubjectNameIn(loginInfo.getUnitId(),new String[]{subjectName}),new TR<List<Course>>(){});
                if(CollectionUtils.isEmpty(courseList)){
                    errorData = new String[4];
                    errorData[0] = errorDataList.size() + 1 + "";
                    errorData[1] = "科目";
                    errorData[2] = subjectName;
                    errorData[3] = "请填写正确的科目名称";
                    errorDataList.add(errorData);
                    continue;
                }
            } else {
                errorData = new String[4];
                errorData[0] = errorDataList.size() + 1 + "";
                errorData[1] = "科目";
                errorData[2] = subjectName;
                errorData[3] = "科目名称不能为空";
                errorDataList.add(errorData);
                continue;
            }


//            Student student = SUtils.dc(studentRemoteService.findOneBy(new String[]{"studentCode", "unitId", "isDeleted"},new String[]{stuCode,loginInfo.getUnitId(), "0"}), Student.class);
            List<Student> students = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(loginInfo.getUnitId(),new String[]{stuCode}),new TR<List<Student>>(){});
            Student student = null;
            if(CollectionUtils.isNotEmpty(students)){
                student = students.get(0);
            }
            if (student != null) {
                List<CreditModuleInfo> list = creditModuleInfoService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId","examType","scoreType"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId(),"2","1"});
                List<CreditModuleInfo> list1 = creditModuleInfoService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId","examType","scoreType"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId(),"3","1"});
                //补考折分
                List<CreditModuleInfo> moduleInfos = creditModuleInfoService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId","examType","scoreType"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId(),"3","3"});
//                List<CreditModuleInfo> list = creditModuleInfoService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId","examType","scoreType"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId(),"3","3"});
                if (CollectionUtils.isEmpty(list)) {
                    errorData = new String[4];
                    errorData[0] = errorDataList.size() + 1 + "";
                    errorData[1] = "考试";
                    errorData[2] = score;
                    errorData[3] = "不存在该学生的考试信息";
                    errorDataList.add(errorData);
                    continue;
                }else {
                    try {
                        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{acadyear, loginInfo.getUnitId(), semester});
                        if(creditSet!=null){
                            float modulePercent = creditSet.getModuleScore() / creditSet.getSumScore();
                            float usualPercent = creditSet.getUsualScore() / creditSet.getSumScore();
                            List<CreditDailyInfo> creditDailyInfo1 = creditDailyInfoService.findListByNullDailyIdByStuId(acadyear,loginInfo.getUnitId(),semester,student.getId());
                            List<CreditModuleInfo> creditModuleInfos = creditModuleInfoService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId","examType","scoreType"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId(),"1","1"});
                            List<CreditModuleInfo> creditModuleInfos1 = creditModuleInfoService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId","examType","scoreType"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId(),"2","1"});
                            float sumScore = 0f;
                            if(CollectionUtils.isNotEmpty(creditDailyInfo1)){
                                sumScore = sumScore+creditDailyInfo1.get(0).getScore();
                            }
                            if(CollectionUtils.isNotEmpty(creditModuleInfos)){
                                sumScore = sumScore+creditModuleInfos.get(0).getScore()*usualPercent;
                            }
                            if(CollectionUtils.isNotEmpty(creditModuleInfos1)){
                                sumScore = sumScore+Float.parseFloat(score)*modulePercent;
                            }
                            if(sumScore>creditSet.getPassLine()){
                                List<CreditPatchStudent> creditPatchStudents = creditPatchStudentService.findListBy(new String[]{"acadyear", "semester", "studentId","subjectId"}, new String[]{acadyear, semester, student.getId(),courseList.get(0).getId()});
                                creditPatchStudentService.deleteAll(creditPatchStudents.toArray(new CreditPatchStudent[0]));
                            }
                            //插入补考折分
                            CreditModuleInfo creditModuleInfo1=new CreditModuleInfo();
                            if(CollectionUtils.isNotEmpty(moduleInfos)){
                                creditModuleInfo1 = moduleInfos.get(0);
                                BigDecimal bg = new BigDecimal(Float.parseFloat(score)*modulePercent);
                                Float sc = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                creditModuleInfo1.setScore(sc);
                            }else {
                                //否则根据模块成绩生成
                                creditModuleInfo1 = (CreditModuleInfo) BeanUtils.cloneBean(list.get(0));
                                creditModuleInfo1.setId(UuidUtils.generateUuid());
                                creditModuleInfo1.setExamType("3");
                                creditModuleInfo1.setScoreType("3");
                                BigDecimal bg = new BigDecimal(Float.parseFloat(score)*modulePercent);
                                Float sc = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                creditModuleInfo1.setScore(sc);
                            }
                            insertList.add(creditModuleInfo1);
                        }
                        //如果已有补考成绩
                        if (CollectionUtils.isNotEmpty(list1)) {
                            creditModuleInfo = list1.get(0);
                        } else {
                            //否则根据模块成绩生成
                            creditModuleInfo = (CreditModuleInfo) BeanUtils.cloneBean(list.get(0));
                            creditModuleInfo.setId(UuidUtils.generateUuid());
                            creditModuleInfo.setExamType("3");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    creditModuleInfo.setScore(Float.parseFloat(score));
                    stuIdSet.add(stu.getId()+"_"+subjectName);
                    insertList.add(creditModuleInfo);
                    successCount++;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            creditModuleInfoService.saveAll(insertList.toArray(new CreditModuleInfo[0]));
        }
        return result(totalCount, successCount, errorDataList.size(), errorDataList);
    }

    public String  result(int totalCount , int successCount , int errorCount , List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }
}
