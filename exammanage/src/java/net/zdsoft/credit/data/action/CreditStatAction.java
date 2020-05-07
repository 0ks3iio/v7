package net.zdsoft.credit.data.action;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.entity.*;
import net.zdsoft.credit.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.credit.data.constant.CreditConstants.*;

@RequestMapping("exammanage/credit")
@Controller
public class CreditStatAction extends BaseAction {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleRemoteService;
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
    UserRemoteService userRemoteService;
    @Autowired
    TeachClassRemoteService teachClassRemoteService;
    @Autowired
    ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    CreditPatchStudentService creditPatchStudentService;

    @RequestMapping("/summary")
    public String creditStatIndex(String year, String selectSemester, ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
//        Iterator<String> it = acadyearList.iterator();
//        while(it.hasNext()){
//            String year = it.next();
//            int yearNow = Integer.parseInt(semester.getAcadyear().substring(0,4));
//            if(Integer.parseInt(year.substring(0,4))-yearNow>2){
//                it.remove();
//            }else if(Integer.parseInt(year.substring(0,4))-yearNow<0){
//                it.remove();
//            }
//        }
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("selectYear", year);
        map.put("selectSemester", selectSemester);
        return "/exammanage/credit/creditStat/creditStatIndex.ftl";
    }

    @RequestMapping("/student/page")
    public String studentStatPage(String year, String selectSemester, ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
//        Iterator<String> it = acadyearList.iterator();
//        while(it.hasNext()){
//            String year = it.next();
//            int yearNow = Integer.parseInt(semester.getAcadyear().substring(0,4));
//            if(Integer.parseInt(year.substring(0,4))-yearNow>2){
//                it.remove();
//            }else if(Integer.parseInt(year.substring(0,4))-yearNow<0){
//                it.remove();
//            }
//        }
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("selectYear", year);
        map.put("selectSemester", selectSemester);
        return "/exammanage/credit/creditStat/creditStatStuIndex.ftl";
    }

    @RequestMapping("/creditStatStuList")
    public String creditStatStuList(String year, String semester, ModelMap modelMap) {
        String unitId = getLoginInfo().getUnitId();
        String studentId = getLoginInfo().getOwnerId();
//        //日常成绩
        List<CreditDailyInfo> creditDailyInfos = new ArrayList<>();
//        平时成绩
        List<CreditModuleInfo> moduleInfos1 = new ArrayList<>();
//        //模块成绩
        List<CreditModuleInfo> moduleInfos2 = new ArrayList<>();
        //        //补考成绩
        List<CreditModuleInfo> moduleInfos3 = new ArrayList<>();
//        //        //总分
//        List<CreditStat> moduleInfos3 = new ArrayList<>();
        float dailyPercent = 0;
        float usualPercent = 0;
        float modulePercent = 0;
        float passScore = 0;
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{year, unitId, semester});
        if (creditSet == null) {
            return "/exammanage/credit/creditStat/creditStatStuList.ftl";
        }
        //平时成绩
        List<CreditExamSet> examSets = creditExamSetService.findListBy(new String[]{"setId", "type", "classId"}, new String[]{creditSet.getId(), "1", student.getClassId()});
        List<CreditExamSet> examSets1 = creditExamSetService.findListBy(new String[]{"setId", "type", "classId"}, new String[]{creditSet.getId(), "2", student.getClassId()});
        List<CreditModuleInfo> moduleInfosAll = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester"}, new String[]{studentId, year, semester});
        moduleInfos1 = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester", "examType", "scoreType"}, new String[]{studentId, year, semester, CREDIT_EXAM_TYPE_1, SCORE_TYPE_1});
        moduleInfos2 = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester", "examType", "scoreType"}, new String[]{studentId, year, semester, CREDIT_EXAM_TYPE_2, SCORE_TYPE_1});
        moduleInfos3 = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester", "examType", "scoreType"}, new String[]{studentId, year, semester, CREDIT_EXAM_TYPE_3, SCORE_TYPE_1});
        List<CreditDailySet> parentList = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        if (creditSet != null) {
            dailyPercent = creditSet.getUsualScore() / creditSet.getSumScore();
            modulePercent = creditSet.getModuleScore() / creditSet.getSumScore();
            usualPercent = creditSet.getUsualScore() / creditSet.getSumScore();
            passScore = creditSet.getPassLine();
            List<CreditDailySet> list = creditDailySetService.findBySetId(creditSet.getId());
            if (CollectionUtils.isNotEmpty(list)) {
                Map<String, List<CreditDailySet>> stringListMap = new HashMap<>();
                for (CreditDailySet creditDailySet : list) {
                    if (StringUtils.isBlank(creditDailySet.getParentId())) {
                        parentList.add(creditDailySet);
                    }
                }
                for (CreditDailySet creditDailySet : parentList) {
                    List<CreditDailySet> sets = new ArrayList<>();
                    for (CreditDailySet creditDailySet1 : list) {
                        if (creditDailySet.getId().equals(creditDailySet1.getParentId())) {
                            sets.add(creditDailySet1);
                        }
                    }
                    creditDailySet.setSubSetList(sets);
                    stringListMap.put(creditDailySet.getId(), sets);
                }
                modelMap.put("stringListMap", stringListMap);
                creditDailyInfos = creditDailyInfoService.findListByNotNullDailyId(year, semester, studentId);
            }
        }
        if (CollectionUtils.isNotEmpty(moduleInfosAll)) {
            Set<String> subjectIds = moduleInfosAll.stream().map(CreditModuleInfo::getSubjectId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(creditDailyInfos)) {
                Set<String> subjectIds1 = creditDailyInfos.stream().map(CreditDailyInfo::getSubjectId).collect(Collectors.toSet());
                subjectIds.addAll(subjectIds1);
            }
            courses = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {
            });
        }
        //日常成绩
        Map<String, List<CreditDailyInfo>> listMap1 = new HashMap<>();
        if (CollectionUtils.isNotEmpty(creditDailyInfos)) {
            listMap1 = creditDailyInfos.stream().collect(Collectors.groupingBy(CreditDailyInfo::getSubjectId));
        }
        //平时成绩
        Map<String, List<CreditModuleInfo>> listMap2 = new HashMap<>();
        if (CollectionUtils.isNotEmpty(moduleInfos1)) {
            listMap2 = moduleInfos1.stream().collect(Collectors.groupingBy(CreditModuleInfo::getSubjectId));
        }
        //模块成绩
        Map<String, Float> listMap3 = new HashMap<>();
        Map<String, Float> listMap4 = new HashMap<>();
        Map<String, Course> courseMap = courses.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        if (CollectionUtils.isNotEmpty(moduleInfos2)) {
            Map<String, List<CreditModuleInfo>> map = moduleInfos2.stream().collect(Collectors.groupingBy(CreditModuleInfo::getSubjectId));
            for (Map.Entry entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Course course = courseMap.get(key);
                if (course.getFullMark() != null) {
                    int fullMark = course.getFullMark();
                    List<CreditModuleInfo> list1 = (List<CreditModuleInfo>) entry.getValue();
                    CreditModuleInfo creditModuleInfo = new CreditModuleInfo();
                    Float score = 0f;
                    for (CreditModuleInfo creditModuleInfo1 : list1) {
                        score = score + creditModuleInfo1.getScore();
                    }
                    score = score / list1.size();

                    if (creditSet != null) {
                        BigDecimal bg = new BigDecimal(score * creditSet.getModuleScore() / fullMark);
                        Float scorePercent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                        listMap4.put(key, scorePercent);
                        listMap3.put(key, score);
                    }
                }
            }
        }
        //补考成绩
        Map<String, Float> listMap5 = new HashMap<>();
        if (CollectionUtils.isNotEmpty(moduleInfos3)) {
            listMap5 = moduleInfos3.stream().collect(Collectors.toMap(CreditModuleInfo::getSubjectId, CreditModuleInfo::getScore));
        }
        courses.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getSubjectName().compareTo(o2.getSubjectName());
            }
        });
        //模块分成绩比例
        Map<String, Float> listMap6 = new HashMap<>();
        for (Course course : courses) {
            if (creditSet != null && course.getFullMark() != null) {
                listMap6.put(course.getId(), creditSet.getModuleScore() / course.getFullMark());
            } else {
                listMap6.put(course.getId(), 0f);
            }
        }
        //日常成绩比例
        Map<String, Float> listMap7 = new HashMap<>();
        for (Course course : courses) {
            if (creditSet != null && course.getFullMark() != null) {
                listMap7.put(course.getId(), creditSet.getUsualScore() / course.getFullMark());
            } else {
                listMap7.put(course.getId(), 0f);
            }
        }
        modelMap.put("isStudent", true);
        modelMap.put("listMap1", listMap1);
        modelMap.put("listMap2", listMap2);
        modelMap.put("listMap3", listMap3);
        modelMap.put("listMap4", listMap4);
        modelMap.put("listMap5", listMap5);
        modelMap.put("listMap6", listMap6);
        modelMap.put("listMap7", listMap7);
//        modelMap.put("listMap8",listMap8);
        modelMap.put("courses", courses);
        modelMap.put("examSets", examSets);
        modelMap.put("examSets1", examSets1);
        modelMap.put("parentList", parentList);
        modelMap.put("dailyPercent", dailyPercent);
        modelMap.put("usualPercent", usualPercent);
        modelMap.put("modulePercent", modulePercent);
        modelMap.put("passScore", passScore);
        modelMap.put("studentName", student.getStudentName());
        modelMap.put("studentId", studentId);
        return "/exammanage/credit/creditStat/creditStatStuList.ftl";
    }


    @RequestMapping("/creditStatClassList")
    public String creditStatClassList(ModelMap map, String year, String semester) {
        LoginInfo info = getLoginInfo();
        String unitId = info.getUnitId();
        String userId = info.getUserId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        map.put("unitClass", unit.getUnitClass());
        //年级Code列表
        List<Grade> gradeList = new ArrayList<Grade>();
        List<Grade> gradeList1 = new ArrayList<Grade>();
        boolean isAdmin = isAdmin(unitId, userId);
        gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(getLoginInfo().getUnitId(), year), new TR<List<Grade>>() {
        });
        Set<String> ids = EntityUtils.getSet(gradeList, Grade::getId);
        if (CollectionUtils.isNotEmpty(ids)) {
            gradeList1 = SUtils.dt(gradeRemoteService.findListByIds(ids.toArray(new String[0])), new TR<List<Grade>>() {
            });
        }
        for (Grade grade : gradeList) {
            for (Grade grade1 : gradeList1) {
                if (grade.getId().equals(grade1.getId())) {
                    if (!grade.getGradeName().equals(grade1.getGradeName())) {
                        grade.setGradeName(grade.getGradeName() + "(原" + grade1.getGradeName() + ")");
                    }
                }
            }
        }
        List<Grade> list = new ArrayList<>();
        Set<Grade> list1 = new HashSet<>();
        if (!isAdmin) {
            Map<String, Grade> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getId, Function.identity()));
            User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
            if (user.getOwnerId() != null) {
                List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByTeacherId(user.getOwnerId()), new TR<List<Clazz>>() {
                });
                List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findListByTeacherId(user.getOwnerId(), year, semester), new TR<List<TeachClass>>() {
                });
//                List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByTeacherId(unitId, user.getOwnerId()), new TR<List<ClassTeaching>>() {
//                });
                String str = classTeachingRemoteService.findClassTeachingList(unitId, year, semester, user.getOwnerId());
                List<ClassTeaching> classTeachingList = SUtils.dt(str, new TR<List<ClassTeaching>>() {
                });
                if (CollectionUtils.isNotEmpty(classTeachingList)) {
                    Set<String> classIds1 = classTeachingList.stream().map(ClassTeaching::getClassId).collect(Collectors.toSet());
                    List<Clazz> clazzes = SUtils.dt(classRemoteService.findListByIds(classIds1.toArray(new String[0])), new TR<List<Clazz>>() {
                    });
                    clazzsList.addAll(clazzes);
                }
                Set<String> gradeIds = clazzsList.stream().map(Clazz::getGradeId).collect(Collectors.toSet());
                //加入所在班级班主任年级
                for (String s : gradeIds) {
                    Grade grade = gradeMap.get(s);
                    if (grade != null) {
                        list1.add(grade);
                    }
                }
                //加入所在班级教学班的年级
                for (TeachClass teachClass : teachClasses) {
                    Grade grade = gradeMap.get(teachClass.getGradeId());
                    if (grade != null) {
                        list1.add(grade);
                    }
                }

                //加入所在班级行政班班的年级
                for (Clazz clazz : clazzsList) {
                    if (clazz != null) {
                        Grade grade = gradeMap.get(clazz.getGradeId());
                        if (grade != null) {
                            list1.add(grade);
                        }
                    }
                }
            }
        } else {
            list1.addAll(gradeList);
        }
        list.addAll(list1);
        list.sort(new Comparator<Grade>() {
            @Override
            public int compare(Grade o1, Grade o2) {
                return o1.getGradeCode().compareTo(o2.getGradeCode());
            }
        });
        map.put("isAdmin", isAdmin);
        map.put("gradeList", list);
        return "/exammanage/credit/creditStat/creditStatClassList.ftl";
    }

    @RequestMapping("/creditStatSumScore")
    public String creditStatSumScore(String gradeId, String year, String gradeName, String searchParam, String semester, ModelMap modelMap) {
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
        List<Student> studentList = new ArrayList<>();
        boolean isAdmin = isAdmin(unitId, userId);
        List<ClassDto> classDtoList = new ArrayList<>();
        Set<ClassDto> classDtos = new HashSet<>();
        classDtoList = RedisUtils.getObject(CLASSDTO_LIST_KEY + userId + gradeId, new TypeReference<List<ClassDto>>() {
        });
        studentList = RedisUtils.getObject(STUDENT_LIST_KEY + userId + gradeId, new TypeReference<List<Student>>() {
        });
        classDtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(classDtoList) || CollectionUtils.isEmpty(studentList)) {
            classDtoList = new ArrayList<>();
            studentList = new ArrayList<>();
            if (!isAdmin) {
                if (StringUtils.isNotBlank(user.getOwnerId())) {
                    String ownId = user.getOwnerId();
                    List<Clazz> clazzsList = SUtils.dt(classRemoteService.findListBy(new String[]{"gradeId", "teacherId"}, new String[]{gradeId, ownId}), new TR<List<Clazz>>() {
                    });
                    List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findListBy(new String[]{"gradeId", "teacherId"}, new String[]{gradeId, ownId}), new TR<List<TeachClass>>() {
                    });
                    String str = classTeachingRemoteService.findClassTeachingList(unitId, year, semester, user.getOwnerId());
                    List<ClassTeaching> classTeachingList = SUtils.dt(str, new TR<List<ClassTeaching>>() {
                    });
                    if (CollectionUtils.isNotEmpty(classTeachingList)) {
                        Set<String> classIds1 = classTeachingList.stream().map(ClassTeaching::getClassId).collect(Collectors.toSet());
                        List<Clazz> clazzes = SUtils.dt(classRemoteService.findListByIds(classIds1.toArray(new String[0])), new TR<List<Clazz>>() {
                        });
                        clazzsList.addAll(clazzes);
                    }
                    Set<String> classIds = clazzsList.stream().map(Clazz::getId).collect(Collectors.toSet());
//                    Map<String, Clazz> clazzMap = clazzsList.stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
                    for (Clazz clazz : clazzsList) {
                        if (clazz.getGradeId().equals(gradeId)) {
                            ClassDto classDto = new ClassDto();
                            classDto.setId(clazz.getId());
                            classDto.setClassName(clazz.getClassName());
                            classDto.setClassType("1");
                            classDtos.add(classDto);
                        }
                    }
                    for (TeachClass teachClass : teachClasses) {
                        ClassDto classDto = new ClassDto();
                        classDto.setId(teachClass.getId());
                        classDto.setClassName(teachClass.getName());
                        classDto.setClassType("2");
                        classDtos.add(classDto);
                    }
                    Set<String> teachClassIds = teachClasses.stream().map(TeachClass::getId).collect(Collectors.toSet());
                    if (CollectionUtils.isNotEmpty(classIds)) {
                        studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>() {
                        });
                    }
                    if (CollectionUtils.isNotEmpty(teachClassIds)) {
                        List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(teachClassIds.toArray(new String[0])), new TR<List<TeachClassStu>>() {
                        });
                        Map<String, List<TeachClassStu>> stringMap = teachClassStus.stream().collect(Collectors.groupingBy(TeachClassStu::getStudentId));
                        Set<String> studentIds = teachClassStus.stream().map(TeachClassStu::getStudentId).collect(Collectors.toSet());
                        List<Student> studentList1 = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
                        });
                        List<Student> students = new ArrayList<>();
                        for (int i = 0; i < studentList1.size(); i++) {
                            Student student = studentList1.get(i);
                            List<TeachClassStu> list = stringMap.get(student.getId());
                            if (CollectionUtils.isNotEmpty(list)) {
                                int j = 0;
                                for (TeachClassStu teachClassStu : list) {
                                    if (j == 0) {
                                        student.setClassId(teachClassStu.getClassId());
                                    } else {
                                        try {
                                            Student student1 = (Student) org.apache.commons.beanutils.BeanUtils.cloneBean(studentList1.get(0));
                                            student.setClassId(teachClassStu.getClassId());
                                            students.add(student1);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        } catch (NoSuchMethodException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    j++;
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(students)) {
                            studentList1.addAll(students);
                        }

                        if (CollectionUtils.isNotEmpty(studentList1)) {
                            studentList.addAll(studentList1);
                        }
                    }
                }
            } else {
                List<Clazz> clazzsList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), new TR<List<Clazz>>() {
                });
                if (CollectionUtils.isNotEmpty(clazzsList)) {
                    for (Clazz clazz : clazzsList) {
                        ClassDto classDto = new ClassDto();
                        classDto.setId(clazz.getId());
                        classDto.setClassName(clazz.getClassName());
                        classDto.setClassType("1");
                        classDtos.add(classDto);
                    }
                }
                List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findListBy(new String[]{"gradeId"}, new String[]{gradeId}), new TR<List<TeachClass>>() {
                });
                Set<String> classIds = clazzsList.stream().map(Clazz::getId).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(classIds)) {
                    studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>() {
                    });
                }
                Set<String> teachClassIds = teachClasses.stream().map(TeachClass::getId).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(teachClassIds)) {
                    List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(teachClassIds.toArray(new String[0])), new TR<List<TeachClassStu>>() {
                    });
//                listMap1=creditStats.stream().collect(Collectors.groupingBy(CreditStat::getSubjectId));
                    Map<String, List<TeachClassStu>> stringMap = teachClassStus.stream().collect(Collectors.groupingBy(TeachClassStu::getStudentId));
                    Set<String> studentIds = teachClassStus.stream().map(TeachClassStu::getStudentId).collect(Collectors.toSet());
                    List<Student> studentList1 = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
                    });
                    List<Student> students = new ArrayList<>();
                    for (int i = 0; i < studentList1.size(); i++) {
                        Student student = studentList1.get(0);
                        List<TeachClassStu> list = stringMap.get(student.getId());
                        if (CollectionUtils.isNotEmpty(list)) {
                            int j = 0;
                            for (TeachClassStu teachClassStu : list) {
                                if (j == 0) {
                                    student.setClassId(teachClassStu.getClassId());
                                } else {
                                    try {
                                        Student student1 = (Student) org.apache.commons.beanutils.BeanUtils.cloneBean(studentList1.get(0));
                                        student.setClassId(teachClassStu.getClassId());
                                        students.add(student1);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    }
                                }
                                j++;
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(students)) {
                        studentList.addAll(students);
                    }
                }
            }
            classDtoList.addAll(classDtos);
            classDtoList.sort(new Comparator<ClassDto>() {
                @Override
                public int compare(ClassDto o1, ClassDto o2) {
                    return o1.getClassName().compareTo(o2.getClassName());
                }
            });
            studentList.sort(new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o1.getStudentName().compareTo(o2.getStudentName());
                }
            });
        }
        RedisUtils.setObject(CLASSDTO_LIST_KEY + userId + gradeId, classDtoList, 3600);
        RedisUtils.setObject(STUDENT_LIST_KEY + userId + gradeId, studentList, 3600);
        if (StringUtils.isNotBlank(searchParam)) {
            List<ClassDto> classDtoList1 = classDtoList.stream().filter(ClassDto -> ClassDto.getClassName().contains(searchParam)).collect(Collectors.toList());
            Set<String> classIds = classDtoList1.stream().map(ClassDto::getId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(classDtoList1)) {
                studentList = studentList.stream().filter(Student -> classIds.contains(Student.getClassId())).collect(Collectors.toList());
                classDtoList.clear();
                classDtoList.addAll(classDtoList1);
            } else {
                studentList = studentList.stream().filter(Student -> Student.getStudentName().contains(searchParam)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(studentList)) {
                    Set<String> classIds1 = studentList.stream().map(Student::getClassId).collect(Collectors.toSet());
                    classDtoList = classDtoList.stream().filter(ClassDto -> classIds1.contains(ClassDto.getId())).collect(Collectors.toList());
                }
            }
        }
        modelMap.put("searchParam", searchParam);
        modelMap.put("gradeName", gradeName);
        modelMap.put("gradeId", gradeId);
        modelMap.put("year", year);
        modelMap.put("semester", semester);
        modelMap.put("classDtos", classDtoList);
        modelMap.put("studentList", studentList);
        modelMap.put("isAdmin", isAdmin);
        return "/exammanage/credit/creditStat/creditStatSum.ftl";
    }

    @RequestMapping("/creditStatStuSum")
    @ResponseBody
    public String creditStatStuSum(String gradeId, String year, String semester, ModelMap modelMap) {
        String unitId = getLoginInfo().getUnitId();
        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{year, unitId, semester});
        if (creditSet == null) {
            return returnError("-1", "请设置学分规则");
        } else {
            try {
                List<CreditDailySet> creditDailySetList = creditDailySetService.findBySetId(creditSet.getId());
                if (CollectionUtils.isNotEmpty(creditDailySetList)) {
                    creditSet.setDailySetList(creditDailySetList);
                } else {
                    return returnError("-1", "请设置学分规则");
                }
                creditStatService.deleteByGradeId(gradeId, year, semester);
                creditStatService.sumCreditStat(gradeId, year, semester, unitId);
                return returnSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return returnError();
            }
        }
    }

    @RequestMapping("/creditStatStuSumList")
    public String creditStatStuSumList(String studentId, String classId, String year, String semester, ModelMap modelMap) {
        String unitId = getLoginInfo().getUnitId();
//        //日常成绩
        List<CreditDailyInfo> creditDailyInfos = new ArrayList<>();
//        平时成绩
        List<CreditModuleInfo> moduleInfos1 = new ArrayList<>();
//        //模块成绩
        List<CreditModuleInfo> moduleInfos2 = new ArrayList<>();
        //        //补考成绩
        List<CreditModuleInfo> moduleInfos3 = new ArrayList<>();
//        //        //总分
//        List<CreditStat> moduleInfos3 = new ArrayList<>();
        float dailyPercent = 0;
        float usualPercent = 0;
        float modulePercent = 0;
        float passScore = 0;
        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{year, unitId, semester});
        List<CreditExamSet> examSets = new ArrayList<>();
        List<CreditExamSet> examSets1 = new ArrayList<>();
        //平时成绩
        List<CreditModuleInfo> moduleInfosAll = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester"}, new String[]{studentId, year, semester});
        moduleInfos1 = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester", "examType", "scoreType"}, new String[]{studentId, year, semester, CREDIT_EXAM_TYPE_1, SCORE_TYPE_1});
        moduleInfos2 = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester", "examType", "scoreType"}, new String[]{studentId, year, semester, CREDIT_EXAM_TYPE_2, SCORE_TYPE_1});
        moduleInfos3 = creditModuleInfoService.findListBy(new String[]{"studentId", "acadyear", "semester", "examType", "scoreType"}, new String[]{studentId, year, semester, CREDIT_EXAM_TYPE_3, SCORE_TYPE_1});
        List<CreditDailySet> parentList = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        if (creditSet != null) {
            examSets = creditExamSetService.findListBy(new String[]{"setId", "type", "classId"}, new String[]{creditSet.getId(), "1", classId});
            examSets1 = creditExamSetService.findListBy(new String[]{"setId", "type", "classId"}, new String[]{creditSet.getId(), "2", classId});
            dailyPercent = creditSet.getUsualScore() / creditSet.getSumScore();
            modulePercent = creditSet.getModuleScore() / creditSet.getSumScore();
            usualPercent = creditSet.getUsualScore() / creditSet.getSumScore();
            passScore = creditSet.getPassLine();
            List<CreditDailySet> list = creditDailySetService.findBySetId(creditSet.getId());
            if (CollectionUtils.isNotEmpty(list)) {
                Map<String, List<CreditDailySet>> stringListMap = new HashMap<>();
                for (CreditDailySet creditDailySet : list) {
                    if (StringUtils.isBlank(creditDailySet.getParentId())) {
                        parentList.add(creditDailySet);
                    }
                }
                for (CreditDailySet creditDailySet : parentList) {
                    List<CreditDailySet> sets = new ArrayList<>();
                    for (CreditDailySet creditDailySet1 : list) {
                        if (creditDailySet.getId().equals(creditDailySet1.getParentId())) {
                            sets.add(creditDailySet1);
                        }
                    }
                    creditDailySet.setSubSetList(sets);
                    stringListMap.put(creditDailySet.getId(), sets);
                }
                modelMap.put("stringListMap", stringListMap);
                creditDailyInfos = creditDailyInfoService.findListByNotNullDailyId(year, semester, studentId);
            }
        }
        if (CollectionUtils.isNotEmpty(moduleInfosAll)) {
            Set<String> subjectIds = moduleInfosAll.stream().map(CreditModuleInfo::getSubjectId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(creditDailyInfos)) {
                Set<String> subjectIds1 = creditDailyInfos.stream().map(CreditDailyInfo::getSubjectId).collect(Collectors.toSet());
                subjectIds.addAll(subjectIds1);
            }
            courses = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {
            });
        }

        Map<String, String> stringMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(examSets)) {
            stringMap = examSets.stream().collect(Collectors.toMap(CreditExamSet::getId, CreditExamSet::getName));
        }
        Set<String> setNames = new HashSet<>();
        if (CollectionUtils.isNotEmpty(examSets)) {
            setNames = examSets.stream().map(CreditExamSet::getName).collect(Collectors.toSet());
        }
        //日常成绩
        Map<String, List<CreditDailyInfo>> listMap1 = new HashMap<>();
        if (CollectionUtils.isNotEmpty(creditDailyInfos)) {
            listMap1 = creditDailyInfos.stream().collect(Collectors.groupingBy(CreditDailyInfo::getSubjectId));
        }
        //平时成绩
        Map<String, List<CreditModuleInfo>> listMap2 = new HashMap<>();
        if (CollectionUtils.isNotEmpty(moduleInfos1)) {
            for (CreditModuleInfo creditModuleInfo : moduleInfos1) {
                if (StringUtils.isNotBlank(stringMap.get(creditModuleInfo.getExamSetId()))) {
                    creditModuleInfo.setSetName(stringMap.get(creditModuleInfo.getExamSetId()));
                }
            }
            listMap2 = moduleInfos1.stream().collect(Collectors.groupingBy(CreditModuleInfo::getSubjectId));
        }
        //模块成绩
        Map<String, Float> listMap3 = new HashMap<>();
        Map<String, Float> listMap4 = new HashMap<>();
        Map<String, Course> courseMap = courses.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        if (CollectionUtils.isNotEmpty(moduleInfos2)) {
            Map<String, List<CreditModuleInfo>> map = moduleInfos2.stream().collect(Collectors.groupingBy(CreditModuleInfo::getSubjectId));
            for (Map.Entry entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Course course = courseMap.get(key);
                if(course.getFullMark() == null) {
            		course.setFullMark(100);
            	}
                if (course.getFullMark() != null) {
                    int fullMark = course.getFullMark();
                    List<CreditModuleInfo> list1 = (List<CreditModuleInfo>) entry.getValue();
                    CreditModuleInfo creditModuleInfo = new CreditModuleInfo();
                    Float score = 0f;
                    for (CreditModuleInfo creditModuleInfo1 : list1) {
                        score = score + creditModuleInfo1.getScore();
                    }
                    score = score / list1.size();

                    if (creditSet != null) {
                        BigDecimal bg = new BigDecimal(score * creditSet.getModuleScore() / fullMark);
                        Float scorePercent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                        listMap4.put(key, scorePercent);
                        listMap3.put(key, score);
                    }
                }
            }
        }
        //补考成绩
        Map<String, Float> listMap5 = new HashMap<>();
        if (CollectionUtils.isNotEmpty(moduleInfos3)) {
            listMap5 = moduleInfos3.stream().collect(Collectors.toMap(CreditModuleInfo::getSubjectId, CreditModuleInfo::getScore));
        }
        courses.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getSubjectName().compareTo(o2.getSubjectName());
            }
        });
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        //模块分成绩比例
        Map<String, Float> listMap6 = new HashMap<>();
        for (Course course : courses) {
        	if(course.getFullMark() == null) {
        		course.setFullMark(100);
        	}
            if (creditSet != null && course.getFullMark() != null) {
                listMap6.put(course.getId(), creditSet.getModuleScore() / course.getFullMark());
            } else {
                listMap6.put(course.getId(), 0f);
            }
        }
        //日常成绩比例
        Map<String, Float> listMap7 = new HashMap<>();
        for (Course course : courses) {
        	if(course.getFullMark() == null) {
        		course.setFullMark(100);
        	}
            if (creditSet != null && course.getFullMark() != null) {
                listMap7.put(course.getId(), creditSet.getUsualScore() / course.getFullMark());
            } else {
                listMap7.put(course.getId(), 0f);
            }
        }
        modelMap.put("isStudent", false);
        modelMap.put("listMap1", listMap1);
        modelMap.put("listMap2", listMap2);
        modelMap.put("listMap3", listMap3);
        modelMap.put("listMap4", listMap4);
        modelMap.put("listMap5", listMap5);
        modelMap.put("listMap6", listMap6);
        modelMap.put("listMap7", listMap7);
        modelMap.put("setNames", setNames);
//        modelMap.put("listMap8",listMap8);
        modelMap.put("courses", courses);
        modelMap.put("examSets", examSets);
        modelMap.put("examSets1", examSets1);
        modelMap.put("parentList", parentList);
        modelMap.put("dailyPercent", dailyPercent);
        modelMap.put("usualPercent", usualPercent);
        modelMap.put("modulePercent", modulePercent);
        modelMap.put("passScore", passScore);
        modelMap.put("studentName", student.getStudentName());
        modelMap.put("studentId", studentId);

        return "/exammanage/credit/creditStat/creditStatStuSumList.ftl";
    }

    @RequestMapping("/creditStatClassSumIndex")
    public String creditStatClassSumIndex(String classId, String classType, String subjectId, String year, String semester, ModelMap modelMap) {
        List<CreditStat> creditStats = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        creditStats = creditStatService.findListBy(new String[]{"classId", "acadyear", "semester"}, new String[]{classId, year, semester});
        if (CollectionUtils.isNotEmpty(creditStats)) {
            Set<String> subjectIds = creditStats.stream().map(CreditStat::getSubjectId).collect(Collectors.toSet());
            courses = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {
            });
        }
        modelMap.put("isTeacher", false);
        if (classType.equals("1")) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            List<ClassTeaching> classTeachings = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(year, semester, new String[]{classId}), new TR<List<ClassTeaching>>() {
            });
            if (CollectionUtils.isNotEmpty(classTeachings)) {
                Set<String> teacherIds = classTeachings.stream().map(ClassTeaching::getTeacherId).collect(Collectors.toSet());
                if (teacherIds.contains(getLoginInfo().getOwnerId())) {
                    modelMap.put("isTeacher", true);
                }
            }
            modelMap.put("className", clazz.getClassNameDynamic());
        } else {
            TeachClass teachClass = SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
            if (teachClass.getTeacherId().equals(getLoginInfo().getOwnerId())) {
                modelMap.put("isTeacher", true);
            }
            modelMap.put("className", teachClass.getName());
        }
        boolean isAdmin = isAdmin(getLoginInfo().getUnitId(), getLoginInfo().getUserId());
        modelMap.put("isAdmin", isAdmin);
        modelMap.put("courses", courses);
        modelMap.put("classId", classId);
        return "/exammanage/credit/creditStat/creditStatClassSumIndex.ftl";
    }

    @RequestMapping("/creditStatClassSumList")
    public String creditStatClassSumList(String classId, String subjectId, String year, String semester, ModelMap modelMap) {
        List<Student> studentList = new ArrayList<>();
        if (StringUtils.isNotEmpty(classId) && StringUtils.isNotBlank(subjectId)) {
            studentList = SUtils.dt(studentRemoteService.findOneById(classId), new TR<List<Student>>() {
            });
            String unitId = getLoginInfo().getUnitId();
//        //日常成绩
            List<CreditDailyInfo> creditDailyInfos = new ArrayList<>();
//        平时成绩
            List<CreditModuleInfo> moduleInfos1 = new ArrayList<>();
//        //模块成绩
            List<CreditModuleInfo> moduleInfos2 = new ArrayList<>();
            //        //补考成绩
            List<CreditModuleInfo> moduleInfos3 = new ArrayList<>();
            float dailyPercent = 0;
            float modulePercent = 0;
            float usualPercent = 0;
            float passScore = 0;
            CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{year, unitId, semester});
            List<CreditDailySet> parentList = new ArrayList<>();
            List<CreditExamSet> examSets = new ArrayList<>();
            List<CreditExamSet> examSets1 = new ArrayList<>();
            if (creditSet != null) {
                examSets = creditExamSetService.findListBy(new String[]{"setId", "type", "classId", "subjectId"}, new String[]{creditSet.getId(), "1", classId, subjectId});
                examSets1 = creditExamSetService.findListBy(new String[]{"setId", "type", "classId", "subjectId"}, new String[]{creditSet.getId(), "2", classId, subjectId});
                List<CreditStat> moduleInfosSum = creditStatService.findListBy(new String[]{"subjectId", "acadyear", "semester", "classId"}, new String[]{subjectId, year, semester, classId});

                moduleInfos1 = creditModuleInfoService.findListBy(new String[]{"classId", "acadyear", "semester", "examType", "scoreType", "subjectId"}, new String[]{classId, year, semester, CREDIT_EXAM_TYPE_1, SCORE_TYPE_1, subjectId});
                moduleInfos2 = creditModuleInfoService.findListBy(new String[]{"classId", "acadyear", "semester", "examType", "scoreType", "subjectId"}, new String[]{classId, year, semester, CREDIT_EXAM_TYPE_2, SCORE_TYPE_1, subjectId});
                moduleInfos3 = creditModuleInfoService.findListBy(new String[]{"classId", "acadyear", "semester", "examType", "scoreType", "subjectId"}, new String[]{classId, year, semester, CREDIT_EXAM_TYPE_3, SCORE_TYPE_1, subjectId});

                if (CollectionUtils.isNotEmpty(moduleInfosSum)) {
                    Set<String> studentIds = moduleInfosSum.stream().map(CreditStat::getStudentId).collect(Collectors.toSet());
                    studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
                    });
                }
                passScore = creditSet.getPassLine();
                List<CreditDailySet> list = creditDailySetService.findBySetId(creditSet.getId());
                if (CollectionUtils.isNotEmpty(list)) {
                    Map<String, List<CreditDailySet>> stringListMap = new HashMap<>();
                    for (CreditDailySet creditDailySet : list) {
                        if (StringUtils.isBlank(creditDailySet.getParentId())) {
                            parentList.add(creditDailySet);
                        }
                    }
                    for (CreditDailySet creditDailySet : parentList) {
                        List<CreditDailySet> sets = new ArrayList<>();
                        for (CreditDailySet creditDailySet1 : list) {
                            if (creditDailySet.getId().equals(creditDailySet1.getParentId())) {
                                sets.add(creditDailySet1);
                            }
                        }
                        creditDailySet.setSubSetList(sets);
                        stringListMap.put(creditDailySet.getId(), sets);
                    }
                    modelMap.put("stringListMap", stringListMap);
                    creditDailyInfos = creditDailyInfoService.findListByClassIdAndNotNullDailyId(year, semester, classId, subjectId);
                }
            }

            Set<String> studentIds = new HashSet<>();

            List<CreditModuleInfo> moduleInfosAll = creditModuleInfoService.findListBy(new String[]{"classId", "acadyear", "semester", "subjectId"}, new String[]{classId, year, semester, subjectId});

            if (CollectionUtils.isNotEmpty(moduleInfosAll)) {
                Set<String> stuIds = moduleInfosAll.stream().map(CreditModuleInfo::getStudentId).collect(Collectors.toSet());
                studentIds.addAll(stuIds);
                if (CollectionUtils.isNotEmpty(creditDailyInfos)) {
                    Set<String> studentIds1 = creditDailyInfos.stream().map(CreditDailyInfo::getStudentId).collect(Collectors.toSet());
                    studentIds.addAll(studentIds1);
                }
                studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
                });
            }

            //日常成绩
            Map<String, List<CreditDailyInfo>> listMap1 = new HashMap<>();
            if (CollectionUtils.isNotEmpty(creditDailyInfos)) {
                listMap1 = creditDailyInfos.stream().collect(Collectors.groupingBy(CreditDailyInfo::getStudentId));
            }
            //平时成绩
            Map<String, List<CreditModuleInfo>> listMap2 = new HashMap<>();
            if (CollectionUtils.isNotEmpty(moduleInfos1)) {
                listMap2 = moduleInfos1.stream().collect(Collectors.groupingBy(CreditModuleInfo::getStudentId));
            }
            Course course = new Course();
            course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
            //模块分成绩比例
            if (course.getFullMark() != null) {
                modulePercent = creditSet.getModuleScore() / course.getFullMark();
            }
            //日常成绩比例
            if (course.getFullMark() != null) {
                usualPercent = creditSet.getUsualScore() / course.getFullMark();
            }
            //模块成绩
            Map<String, Float> listMap3 = new HashMap<>();
            Map<String, Float> listMap4 = new HashMap<>();
            if (CollectionUtils.isNotEmpty(moduleInfos2)) {
//                Set<String> subjectIds = moduleInfos1.stream().map(CreditStat::getSubjectId).collect(Collectors.toSet());
//                List<Course> list = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>(){});
//                courseSet.addAll(list);
                Map<String, List<CreditModuleInfo>> map = moduleInfos2.stream().collect(Collectors.groupingBy(CreditModuleInfo::getStudentId));
                for (Map.Entry entry : map.entrySet()) {
                    String key = entry.getKey().toString();
                    List<CreditModuleInfo> list1 = (List<CreditModuleInfo>) entry.getValue();
                    CreditStat creditStat1 = new CreditStat();
                    Float score = 0f;
                    for (CreditModuleInfo creditModuleInfo : list1) {
                        score = score + creditModuleInfo.getScore();
                    }
                    score = score / list1.size();
                    BigDecimal bg = new BigDecimal(score * modulePercent);
                    BigDecimal bg1 = new BigDecimal(score);

                    Float scorePercent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    Float score1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    listMap4.put(key, scorePercent);
                    listMap3.put(key, score1);
                }
            }
            //补考成绩
            Map<String, Float> listMap5 = new HashMap<>();
            if (CollectionUtils.isNotEmpty(moduleInfos3)) {
                listMap5 = moduleInfos3.stream().collect(Collectors.toMap(CreditModuleInfo::getStudentId, CreditModuleInfo::getScore));
            }
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentList.sort(new Comparator<Student>() {
                    @Override
                    public int compare(Student o1, Student o2) {
                        return o1.getStudentCode().compareTo(o2.getStudentCode());
                    }
                });
            }
            modelMap.put("listMap1", listMap1);
            modelMap.put("listMap2", listMap2);
            modelMap.put("listMap3", listMap3);
            modelMap.put("listMap4", listMap4);
            modelMap.put("listMap5", listMap5);
            modelMap.put("course", course);
            modelMap.put("studentList", studentList);
            modelMap.put("examSets", examSets);
            modelMap.put("examSets1", examSets1);
            modelMap.put("parentList", parentList);
            modelMap.put("usualPercent", usualPercent);
            modelMap.put("modulePercent", modulePercent);
            modelMap.put("passScore", passScore);
        }
        return "/exammanage/credit/creditStat/creditStatClassSumList.ftl";
    }

    @RequestMapping("/exportPatchStudent")
    public void exportPatchStudent(String studentId, String gradeId, String classId, String subjectId, String year, String semester, HttpServletRequest req, ModelMap map) {
    	
    	String unitId = this.getLoginInfo().getUnitId();
    	String userId = this.getLoginInfo().getUserId();
    	User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
    	String teacherId = user.getOwnerId();
    	Set<String> clsIds = new HashSet<>();
    	Set<String> subClsIds = new HashSet<>();
    	boolean hasAdmin = isAdmin(unitId, userId);
    	if(StringUtils.isNotBlank(classId)) {
    		clsIds.add(classId);
    	}else {
    		//TODO 获取有权限的班级列表
    		if(!hasAdmin) {
    			//班主任权限
    			List<Clazz> clazzsList = SUtils.dt(classRemoteService.findListBy(new String[]{"gradeId", "teacherId"}, new String[]{gradeId, teacherId}), new TR<List<Clazz>>() {});
    			if(CollectionUtils.isNotEmpty(clazzsList)) {
    				for (Clazz clazz : clazzsList) {
						clsIds.add(clazz.getId());
					}
    			}
                List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findListBy(new String[]{"gradeId", "teacherId"}, new String[]{gradeId, teacherId}), new TR<List<TeachClass>>() {});
                if(CollectionUtils.isNotEmpty(teachClasses)) {
                	for (TeachClass e : teachClasses) {
						clsIds.add(e.getId());
					}
                }
                //任课老师
                String str = classTeachingRemoteService.findClassTeachingList(unitId, year, semester, teacherId);
                List<ClassTeaching> classTeachingList = SUtils.dt(str, new TR<List<ClassTeaching>>() {});
                if(CollectionUtils.isNotEmpty(classTeachingList)) {
	                for (ClassTeaching e : classTeachingList) {
						subClsIds.add(e.getClassId()+"_"+e.getSubjectId());
					}
                }
        	}
    	}
        List<CreditPatchStudent> list1 = creditPatchStudentService.findListByParam(year, semester, studentId, gradeId, classId, subjectId);
        if(hasAdmin) {
        	SortUtils.ASC(list1, "classId");
//        	SortUtils.ASC(list1, "studentCode");
            doExportList1(list1, getResponse(), "");
            return;
        }
        List<CreditPatchStudent> list = new ArrayList<>();
        for (CreditPatchStudent e : list1) {
			if(clsIds.contains(e.getClassId())
					|| subClsIds.contains(e.getClassId()+"_"+e.getSubjectId())) {
				list.add(e);
			}
		}
        SortUtils.ASC(list, "classId");
//        SortUtils.ASC(list, "studentCode");
        doExportList1(list, getResponse(), "");
    }

    private void doExportList1(List<CreditPatchStudent> list, HttpServletResponse response, String titleName) {
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
        Map<String, List<String>> titleMap = new HashMap<>();
        List<String> tis = getRowTitleList1();
        if (CollectionUtils.isNotEmpty(list)) {
        	Set<String> clsIds = list.stream().map(CreditPatchStudent::getClassId).collect(Collectors.toSet());
        	Map<String, Clazz> clsMap = new HashMap<>();
        	Map<String, TeachClass> tClsMap = new HashMap<>();
        	if(CollectionUtils.isNotEmpty(clsIds)) {
        		List<Clazz> clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {});
        		if(CollectionUtils.isNotEmpty(clslist)) {
        			clsMap = EntityUtils.getMap(clslist, Clazz::getId); 
        		}
        		List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<TeachClass>>() {});
        		if(CollectionUtils.isNotEmpty(teachClasses)) {
        			tClsMap = EntityUtils.getMap(teachClasses, TeachClass::getId);
        		}
        	}
            Set<String> courseIds = list.stream().map(CreditPatchStudent::getSubjectId).collect(Collectors.toSet());
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>() {
            });
            Map<String, Course> stringCourseMap = courseList.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
            for (String courseId : stringCourseMap.keySet()) {
            	List<Map<String, String>> recordList = new ArrayList<>();
                int index = 1;
                String subStr = stringCourseMap.get(courseId).getSubjectName();
                for (CreditPatchStudent creditPatchStudent : list) {
                    if (!StringUtils.equals(creditPatchStudent.getSubjectId(),courseId)){
                        continue;
                    }
                    Map<String, String> sMap = new HashMap<>();
                    sMap.put("序号", index + "");
                    sMap.put("学年", creditPatchStudent.getAcadyear());
                    sMap.put("学期", creditPatchStudent.getSemester());
                    sMap.put("学号", creditPatchStudent.getStudentCode());
                    sMap.put("学生姓名", creditPatchStudent.getStudentName());
                    if (stringCourseMap.get(creditPatchStudent.getSubjectId()) != null) {
                        sMap.put("科目名称", stringCourseMap.get(creditPatchStudent.getSubjectId()).getSubjectName());
                    } else {
                        sMap.put("科目名称", "");
                    }
                    sMap.put("班级","");
                    if(clsMap.containsKey(creditPatchStudent.getClassId())) {
                    	sMap.put("班级", clsMap.get(creditPatchStudent.getClassId()).getClassNameDynamic());
                    }
                    if(tClsMap.containsKey(creditPatchStudent.getClassId())) {
                    	sMap.put("班级", tClsMap.get(creditPatchStudent.getClassId()).getName());
                    }
                    index = index + 1;
//                    if(creditPatchStudent.getStudentId().equals("725D87389A3B425FB2BF1AE1737F96DB")){
//                        System.out.println(1111);
//                    }
                    recordList.add(sMap);
                }
                sheetName2RecordListMap.put(subStr + "补考名单", recordList);
                titleMap.put(subStr + "补考名单", tis);
            }
        }
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile(titleName + "补考人员名单导出", titleMap, sheetName2RecordListMap, response);
    }

    private List<String> getRowTitleList1() {
        List<String> tis = new ArrayList<String>();
        tis.add("序号");
        tis.add("学年");
        tis.add("学期");
        tis.add("学号");
        tis.add("学生姓名");
        tis.add("科目名称");
        tis.add("班级");
        return tis;
    }

    /**
     * 判断是否为教务管理员
     */
    private boolean isAdmin(String unitId, String userId) {
//        return true;
        return customRoleRemoteService.checkUserRole(unitId, CreditConstants.SUBSYSTEM_86, CreditConstants.EDUCATION_CODE, userId);
    }

}
