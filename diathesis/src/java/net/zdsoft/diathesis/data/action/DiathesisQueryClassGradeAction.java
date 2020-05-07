//package net.zdsoft.diathesis.data.action;
//
//import net.zdsoft.basedata.entity.Clazz;
//import net.zdsoft.basedata.entity.Grade;
//import net.zdsoft.basedata.entity.Semester;
//import net.zdsoft.basedata.entity.Student;
//import net.zdsoft.basedata.remote.service.ClassRemoteService;
//import net.zdsoft.basedata.remote.service.GradeRemoteService;
//import net.zdsoft.basedata.remote.service.SemesterRemoteService;
//import net.zdsoft.basedata.remote.service.StudentRemoteService;
//import net.zdsoft.diathesis.data.constant.DiathesisConstant;
//import net.zdsoft.diathesis.data.entity.DiathesisProject;
//import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
//import net.zdsoft.diathesis.data.entity.DiathesisScoreType;
//import net.zdsoft.diathesis.data.service.DiathesisProjectService;
//import net.zdsoft.diathesis.data.service.DiathesisRecordService;
//import net.zdsoft.diathesis.data.service.DiathesisScoreInfoService;
//import net.zdsoft.diathesis.data.service.DiathesisScoreTypeService;
//import net.zdsoft.framework.action.BaseAction;
//import net.zdsoft.framework.entity.Json;
//import net.zdsoft.framework.entity.JsonArray;
//import net.zdsoft.framework.utils.EntityUtils;
//import net.zdsoft.framework.utils.SUtils;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @Author: panlf
// * @Date: 2019/9/9 10:17
// */
//@RestController
//@RequestMapping("/diathesis/query")
//public class DiathesisQueryClassGradeAction extends BaseAction {
//
//    @Autowired
//    DiathesisProjectService diathesisProjectService;
//    @Autowired
//    DiathesisScoreTypeService diathesisScoreTypeService;
//    @Autowired
//    DiathesisScoreInfoService diathesisScoreInfoService;
//    @Autowired
//    ClassRemoteService classRemoteService;
//    @Autowired
//    StudentRemoteService studentRemoteService;
//    @Autowired
//    GradeRemoteService gradeRemoteService;
//    @Autowired
//    SemesterRemoteService semesterRemoteService;
//    @Autowired
//    DiathesisRecordService diathesisRecordService;
//
//    @RequestMapping("/classQuery")
//    public String classQuery(String classId) {
//        String unitId = getLoginInfo().getUnitId();
//        Json result = new Json();
//        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
//        if (CollectionUtils.isEmpty(studentList)) return error("改班级暂无学生信息");
//        //总人数
//        result.put("studentNum", studentList.size());
//
//
//        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
//        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
//        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
//        List<DiathesisScoreType> typeList = diathesisScoreTypeService.findByUnitIdAndTypeAndGradeIdAndGradeCodeAndSemester(unitId, DiathesisConstant.INPUT_TYPE_3, clazz.getGradeId(), grade.getGradeCode(), semester.getSemester());
//        List<DiathesisScoreInfo> infoList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(typeList)) {
//            infoList = diathesisScoreInfoService.findByScoreTypeIdIn(EntityUtils.getArray(typeList, x -> x.getId(), String[]::new));
//        }
//        //基础信息
//        JsonArray baseInfo = new JsonArray();
//        Json classInfo = new Json();
//        classInfo.put("value", clazz.getClassNameDynamic());
//        classInfo.put("key", "班级");
//        baseInfo.add(classInfo);
//        Json acadyearInfo = new Json();
//        acadyearInfo.put("value", semester.getAcadyear());
//        acadyearInfo.put("key", "学年");
//        baseInfo.add(acadyearInfo);
//        Json semesterInfo = new Json();
//        semesterInfo.put("value", semester.getSemester() == 1 ? "第一学期" : "第二学期");
//        semesterInfo.put("key", "学期");
//        baseInfo.add(semesterInfo);
//        result.put("baseInfo", baseInfo);
//
//        //详细信息 学生完成率
//        Integer[] perNum = diathesisProjectService.findProjectEvaluationNumByUnitId(unitId);
//        Json detail = new Json();
//        List<String> titleList = new ArrayList<>(DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.values());
//        titleList.add(0, "学生姓名");
//        //详细信息的title
//        detail.put("title", titleList);
//
//        //详细信息的data
//        JsonArray stuArr = new JsonArray();
//        Map<String, Map<String, Long>> infoCountMap = new HashMap<>();
//        Map<String, Integer> completeMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(infoList)) {
//            infoCountMap = infoList.stream().collect(Collectors.groupingBy(x -> x.getStuId(), Collectors.groupingBy(x -> x.getType(), Collectors.counting())));
//            for (Student student : studentList) {
//                JsonArray stuData = new JsonArray();
//                stuData.add(student.getStudentName());
//                Map<String, Long> typeCountMap = infoCountMap.get(student.getId());
//                for (String type : DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.keySet()) {
//                    boolean status = typeCountMap != null && typeCountMap.get(type) != null && typeCountMap.get(type).intValue() == perNum[Integer.parseInt(type)];
//                    stuData.add(status);
//                    if (status) completeMap.merge(type, 1, (prev, one) -> prev + one);
//                }
//                stuArr.add(stuData);
//            }
//
//        }
//        result.put("detail", detail);
//
//        //总的评价统计
//        JsonArray evaluationArr = new JsonArray();
//        for (String s : DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.keySet()) {
//            Json per = new Json();
//            per.put("type", s);
//            per.put("typeName", DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.get(s));
//            Integer num = completeMap.get(s);
//            if (num == null) {
//                num=0;
//            }
//            per.put("completeRate", String.format("%.3f",num*1.0/studentList.size()));
//            evaluationArr.add(per);
//        }
//        result.put("evaluationArr", evaluationArr);
//
//        //写实记录统计
//        List<DiathesisProject> projectList = diathesisProjectService.findByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_RECORD});
//        //diathesisRecordService.findMapByProjectIdIn()
//
//        return "";
//    }
//
//    @RequestMapping("/gradeQuery")
//    public String gradeQuery(String gradeId) {
//        return "";
//    }
//}
