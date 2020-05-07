package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/exammanage/scoreImport")
public class EmScoreInputImportAction extends DataImportAction {
    @Autowired
    private EmLimitService emLimitService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private TeachClassRemoteService teachClassService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private TeachClassStuRemoteService teachClassStuRemoteService;

    private Logger logger = Logger.getLogger(EmScoreInputImportAction.class);

    @ControllerInfo("进入查询头")
    @RequestMapping("/head")
    public String index(String examId, String subTypeName, String noLimit, String classId, String classType, String subjectId, ModelMap map) {
        Set<String> subjectIds = new HashSet<>();
        LoginInfo loginInfo = getLoginInfo();
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null)
            return "/exammanage/scoreNewInput/examScoreIndexImport.ftl";
        List<EmSubjectInfo> infoList = null;
        infoList = emSubjectInfoService.findByExamId(examId);
        Map<String, String> typeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (EmSubjectInfo subjectInfo : infoList) {
                subjectIds.add(subjectInfo.getSubjectId());
                typeMap.put(subjectInfo.getSubjectId(), subjectInfo.getGkSubType());
            }
        }
        if ("1".equals(exam.getIsgkExamType())) {//新高考模式 分选考学考
            map.put("typeMap", typeMap);

            List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(loginInfo.getUnitId()), new TR<List<Course>>() {
            });
            Map<String, String> course73Map = courseList73.stream().collect(Collectors.toMap(Course::getId, Course::getSubjectCode));
            map.put("course73Map", course73Map);
        }
        if (StringUtils.isNotBlank(classId)) {
            subjectIds = new HashSet<>();//有班级 重新找对应的科目
            if ("1".equals(classType)) {
                if ("0".equals(noLimit)) {//普通
                    subjectIds = EntityUtils.getSet(emLimitService.findByExamIdST(examId, loginInfo.getOwnerId(), null, classId), EmLimit::getSubjectId);
                } else {
                    if (infoList == null)
                        infoList = emSubjectInfoService.findByExamId(examId);
                    Set<String> subIds = EntityUtils.getSet(infoList, EmSubjectInfo::getSubjectId);
                    List<ClassTeaching> list = SUtils.dt(classTeachingRemoteService.findBySearch(loginInfo.getUnitId(), exam.getAcadyear(),
                            exam.getSemester(), new String[]{classId}, 0, 0), new TR<List<ClassTeaching>>() {
                    });
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (ClassTeaching item : list) {
                            if (subIds.contains(item.getSubjectId())) {
                                subjectIds.add(item.getSubjectId());
                            }
                        }
                    }
                }
            } else {
                TeachClass teachClass = SUtils.dc(teachClassService.findOneById(classId), TeachClass.class);
                subjectIds.add(teachClass == null ? "" : teachClass.getCourseId());
            }
        }
        List<Course> courseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subjectIds)) {
            courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            Collections.sort(courseList, (o1, o2) -> {
                return o1.getOrderId() - o2.getOrderId();
            });
        }
        map.put("courseList", courseList);
        try {
            if (StringUtils.isNotBlank(subTypeName)) {
                subTypeName = URLDecoder.decode(subTypeName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("classId", classId);
        map.put("subjectId", subjectId);
        map.put("subTypeName", subTypeName);
        return "/exammanage/scoreNewInput/examScoreIndexImport.ftl";
    }

    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String execute(String examId, String subjectId, String subType, String stuCard, String classType, String classId, ModelMap map) {//
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null)
            return "/exammanage/scoreNewInput/examScoreImport.ftl";
        String examType = exam.getExamType();
        // 业务名称
        map.put("businessName", "成绩录入");
        // 导入URL
        map.put("businessUrl", "/exammanage/scoreImport/import/1");
        if (ExammanageConstants.EXAM_TYPE_FINAL.equals(examType)) {
            map.put("businessUrl", "/exammanage/scoreImport/import/2");
        }
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/scoreImport/template?examId=" + examId + "&subjectId=" + subjectId + "&classType=" + classType + "&classId=" + classId +
                "&subType=" + subType + "&stuCard=" + stuCard + "&gradeCodes=" + exam.getGradeCodes());
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        map.put("description", "");
        map.put("businessKey", UuidUtils.generateUuid());
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 0);
        //模板校验
        map.put("validateUrl", "/exammanage/scoreImport/validate/1?stuCard=" + stuCard);
        if (ExammanageConstants.EXAM_TYPE_FINAL.equals(examType)) {
            map.put("validateUrl", "/exammanage/scoreImport/validate/2?stuCard=" + stuCard);
        }
        // 导入说明
        StringBuffer description = new StringBuffer();
        description.append(getDescription());

        //导入参数
        JSONObject obj = new JSONObject();
        obj.put("examId", examId);
        obj.put("unitId", unitId);
        obj.put("classType", classType);
        obj.put("classId", classId);
        obj.put("stuCard", stuCard);
        obj.put("subjectId", subjectId);
        obj.put("subType", subType);
        obj.put("ownerId", loginInfo.getOwnerId());
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("description", description);
        return "/exammanage/scoreNewInput/examScoreImport.ftl";
    }

    @RequestMapping("/import/{flag}")
    @ResponseBody
    public String dataImport(String filePath, String params, @PathVariable("flag") String flag) {
        logger.info("业务数据处理中......");
        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String examId = (String) performance.get("examId");
        String unitId = (String) performance.get("unitId");
        String classType = (String) performance.get("classType");
        String classId = (String) performance.get("classId");
        String stuCard = (String) performance.get("stuCard");
        String ownerId = (String) performance.get("ownerId");
        String subjectId = (String) performance.get("subjectId");
        String subType = (String) performance.get("subType");

        EmExamInfo exam = emExamInfoService.findOne(examId);
        boolean isStuCode = "1".equals(stuCard);//是否是学号
        String str = "考号";
        if (isStuCode) {
            str = "学号";
        }
        //每一行数据   表头列名：0
        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 1, "2".equals(flag) ? getRowTitleList2(stuCard).size() : getRowTitleList(stuCard).size());
//		Random r = new Random(1);
//		for (String[] strings : datas) {
//			strings[2] = r.nextInt(100)+"";
//			strings[4] = r.nextInt(100)+"";
//		}
        //错误数据序列号
        int sequence = 1;
        int totalSize = datas.size();

        List<String[]> errorDataList = new ArrayList<String[]>();
        if (CollectionUtils.isEmpty(datas)) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = str;
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return result(datas.size(), 0, 0, errorDataList);
        }
        if (exam == null) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = str;
            errorData[2] = "";
            errorData[3] = "该考试不存在";
            errorDataList.add(errorData);
            return result(datas.size() - 1, 0, 0, errorDataList);
        }

        String acadyear = exam.getAcadyear();
        String semester = exam.getSemester();

        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
        if (CollectionUtils.isEmpty(subjectInfoList)) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = str;
            errorData[2] = "";
            errorData[3] = "该考试科目已删除";
            errorDataList.add(errorData);
            return result(datas.size() - 1, 0, 0, errorDataList);
        }
        EmSubjectInfo subjectInfo = subjectInfoList.get(0);

        //班级下学生
        List<Student> stuList = new ArrayList<Student>();
        //不排考学生
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Map<String, Student> studentCodeMap = new HashMap<String, Student>();
        Map<String, Student> studentIdMap = new HashMap<String, Student>();
        //考号
        Map<String, String> examNumMap = emExamNumService.findNumStuIdMap(unitId, examId);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, exam.getGradeCodes()), new TR<List<Grade>>() {
        });
        String gradeId = gradeList.get(0).getId();

        List<TeachClass> teaClaList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId, acadyear, semester, subjectId, new String[]{gradeId}, true), TeachClass.class);
        Set<String> teachClassIds = teaClaList.stream().map(TeachClass::getId).collect(Collectors.toSet());
        Map<String, Set<String>> stuIdOfTclidsMap = SUtils.dt(teachClassStuRemoteService.findMapWithStuIdByClassIds(teachClassIds.toArray(new String[]{})), new TypeReference<Map<String, Set<String>>>() {
        });

        List<Student> stuList1 = null;
        if (StringUtils.isNotBlank(classId)) {
            if (ExammanageConstants.CLASS_TYPE1.equals(classType)) {
                stuList1 = SUtils.dt(studentService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
                });
            } else {
                stuList1 = SUtils.dt(studentService.findByTeachClassIds(new String[]{classId}), new TR<List<Student>>() {
                });
            }
        } else {
            stuList1 = SUtils.dt(studentService.findByGradeId(gradeId), new TR<List<Student>>() {
            });
        }
//        List<Student> stuList1 = SUtils.dt(studentService.findByGradeId(gradeId), new TR<List<Student>>(){});
        if (StringUtils.isNotBlank(subType)) {
            Set<String> stuIds = null;
            if (CollectionUtils.isNotEmpty(gradeList)) {
                Grade grade = gradeList.get(0);
                if ("1".equals(subType)) {//选考
                    Map<String, Set<String>> xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, grade.getId(), true, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                    });
                    if (xuankaoStuIdMap == null)
                        xuankaoStuIdMap = new HashMap<>();
                    stuIds = xuankaoStuIdMap.get(subjectId);
                } else {//学考
                    Map<String, Set<String>> xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, grade.getId(), false, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                    });
                    if (xuekaoStuIdMap == null)
                        xuekaoStuIdMap = new HashMap<>();
                    stuIds = xuekaoStuIdMap.get(subjectId);
                }
                for (Student stu : stuList1) {
                    if (stuIds.contains(stu.getId())) {
                        stuList.add(stu);
                    }
                }
            }
        } else {
            stuList.addAll(stuList1);
        }
        Set<String> stuIds = new HashSet<String>();
        for (Student student : stuList) {
            stuIds.add(student.getId());
            studentCodeMap.put(student.getStudentCode(), student);
            studentIdMap.put(student.getId(), student);
        }
        //查询当前考试科目下 该班级已有学生信息
        //已有的成绩 一个科目 一个学生 一个成绩
        if (!"1".equals(subType) && !"2".equals(subType)) {
            subType = "0";
        }
        Map<String, EmScoreInfo> infoMap = emScoreInfoService.findByStudent(examId, subjectId, stuIds.toArray(new String[0]));
        if (infoMap == null) {
            infoMap = new HashMap<String, EmScoreInfo>();
        }
        List<McodeDetail> gradeTypeList = new ArrayList<McodeDetail>();
        if (subjectInfo.getInputType().equals("G") && StringUtils.isNotBlank(subjectInfo.getGradeType())) {
            gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(subjectInfo.getGradeType()), new TypeReference<List<McodeDetail>>() {
            });
        }
        List<McodeDetail> scoreStatusList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-CJLX"), new TypeReference<List<McodeDetail>>() {
        });
        Pattern p = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
        List<String> studentIdsList = new ArrayList<String>();
        List<EmScoreInfo> scoreInfoList = new ArrayList<>();
        EmScoreInfo newinfo = null;
        int successCount = 0;
        boolean needGeneral = datas.get(0).length == getRowTitleList2(stuCard).size();
        for (String[] arr : datas) {
            String[] errorData = new String[4];
            sequence++;

            if (StringUtils.isBlank(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = str;
                errorData[2] = "";
                errorData[3] = str + "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "姓名";
                errorData[2] = "";
                errorData[3] = "姓名不能为空";
                errorDataList.add(errorData);
                continue;
            }


            if (StringUtils.isBlank(arr[3])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "状态";
                errorData[2] = "";
                errorData[3] = "状态不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
           /* if(StringUtils.isBlank(arr[2])){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="考试成绩";
            	errorData[2]="";
            	errorData[3]="考试成绩不能为空";
            	errorDataList.add(errorData);
            	continue;
            }
            //总评成绩不能为空
            if(needGeneral &&StringUtils.isBlank(arr[4])){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="总评成绩";
            	errorData[2]="";
            	errorData[3]="总评成绩不能为空";
            	errorDataList.add(errorData);
            	continue;
            }*/
            Student student = null;
            if (isStuCode) {
                student = studentCodeMap.get(arr[0]);
            } else {
                student = studentIdMap.get(examNumMap.get(arr[0]));
            }
            if (student == null) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = str;
                errorData[2] = arr[0];
                errorData[3] = "该" + str + "不存在或该学号对应的学生不是该年级或选学考的学生";
                errorDataList.add(errorData);
                continue;
            }

            if (!arr[1].equals(student.getStudentName())) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = str;
                errorData[2] = arr[1];
                errorData[3] = "该" + str + "对应的学生姓名错误";
                errorDataList.add(errorData);
                continue;
            }

            if (filterMap.containsKey(student.getId())) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = str;
                errorData[2] = arr[1];
                errorData[3] = "该学生不参与排考";
                errorDataList.add(errorData);
                continue;
            }

            if (studentIdsList.contains(student.getId())) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = str;
                errorData[2] = arr[1];
                errorData[3] = "请勿重复导入成绩";
                errorDataList.add(errorData);
                continue;
            }


            String scoreStatus = "";
            boolean isScoreStatus = false;
            for (McodeDetail detail : scoreStatusList) {
                if (arr[3].equals(detail.getMcodeContent())) {
                    scoreStatus = detail.getThisId();
                    isScoreStatus = true;
                    break;
                }
            }

            if (!isScoreStatus) {
                //考试状态有效性
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试状态";
                errorData[2] = arr[3];
                errorData[3] = "请填写有效的考试状态";
                errorDataList.add(errorData);
                continue;
            }
            String score = "0";
            if (!"1".equals(scoreStatus)) {//不是缺考
                if (StringUtils.isBlank(arr[2])) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考试成绩";
                    errorData[2] = "";
                    errorData[3] = "考试成绩不能为空";
                    errorDataList.add(errorData);
                    continue;
                }
                //总评成绩不能为空
                if (needGeneral && StringUtils.isBlank(arr[4])) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "总评成绩";
                    errorData[2] = "";
                    errorData[3] = "总评成绩不能为空";
                    errorDataList.add(errorData);
                    continue;
                }
                if (subjectInfo.getInputType().equals("G")) {
                    boolean isGradeType = false;
                    for (McodeDetail detail : gradeTypeList) {
                        if (arr[2].equals(detail.getMcodeContent())) {
                            score = detail.getThisId();
                            isGradeType = true;
                            break;
                        }
                    }

                    if (!isGradeType) {
                        //等第有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "考试成绩";
                        errorData[2] = arr[2];
                        errorData[3] = "请填写有效的等第";
                        errorDataList.add(errorData);
                        continue;
                    }
                } else {
                    Matcher m = p.matcher(arr[2]);
                    if (!m.matches()) {
                        //分数有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "考试成绩";
                        errorData[2] = arr[2];
                        errorData[3] = "格式不正确(最多3位整数，2位小数)!";
                        errorDataList.add(errorData);
                        continue;
                    }

                    if (Float.parseFloat(arr[2]) < 0 || Float.parseFloat(arr[2]) > subjectInfo.getFullScore()) {
                        //分数有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "考试成绩";
                        errorData[2] = arr[2];
                        errorData[3] = "考试成绩为0-" + subjectInfo.getFullScore() + "之间";
                        errorDataList.add(errorData);
                        continue;
                    }
                    score = arr[2];
                }
                //总评成绩 格式 范围验证
                if (needGeneral && !p.matcher(arr[4]).matches()) {
                    //总评成绩 分数有效性
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "总评成绩";
                    errorData[2] = arr[4];
                    errorData[3] = "格式不正确(最多3位整数，2位小数)!";
                    errorDataList.add(errorData);
                    continue;
                }
                if (needGeneral && (Float.parseFloat(arr[4]) < 0 || Float.parseFloat(arr[4]) > subjectInfo.getFullScore())) {
                    //分数范围
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "总评成绩";
                    errorData[2] = arr[4];
                    errorData[3] = "成绩为0-" + subjectInfo.getFullScore() + "之间";
                    errorDataList.add(errorData);
                    continue;
                }
            }

            studentIdsList.add(student.getId());
            successCount++;

            if (infoMap.containsKey(student.getId())) {
                //修改
                newinfo = infoMap.get(student.getId());
                newinfo.setOperatorId(ownerId);
                newinfo.setModifyTime(new Date());
            } else {
                newinfo = new EmScoreInfo();
                newinfo.setId(UuidUtils.generateUuid());
                newinfo.setAcadyear(acadyear);
                newinfo.setSemester(semester);
                newinfo.setExamId(examId);
                newinfo.setSubjectId(subjectId);
                newinfo.setGkSubType(subType);
                newinfo.setCreationTime(new Date());
                newinfo.setModifyTime(new Date());
                newinfo.setOperatorId(ownerId);
                newinfo.setUnitId(unitId);
            }
            newinfo.setInputType(subjectInfo.getInputType());
            newinfo.setClassId(student.getClassId());
            Set<String> tclids = stuIdOfTclidsMap.get(student.getId());
            if (CollectionUtils.isNotEmpty(tclids)) {
                for (String tclsid : tclids) {
                    newinfo.setTeachClassId(tclsid);
                    break;
                }
            }
            /*if (classType.equals("1")) {
            	newinfo.setClassId(classId);
            } else {
            	newinfo.setClassId(student.getClassId());
            	newinfo.setTeachClassId(classId);
            }*/
            newinfo.setStudentId(student.getId());
            newinfo.setScoreStatus(scoreStatus);
            newinfo.setScore(score);
            //wyy.设置总评成绩
            if (needGeneral) {
                newinfo.setToScore(StringUtils.isNotBlank(arr[4]) ? arr[4] : "0");
            }
            scoreInfoList.add(newinfo);
        }
        if (CollectionUtils.isNotEmpty(scoreInfoList)) {
            emScoreInfoService.saveAll(scoreInfoList.toArray(new EmScoreInfo[]{}));
        }

        int errorCount = totalSize - successCount;
        String result = result(totalSize, successCount, errorCount, errorDataList);
        return result;
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        //是否需要显示总评成绩
        String examId = request.getParameter("examId");
        String examType = emExamInfoService.findOne(examId).getExamType();
        String classType = request.getParameter("classType");
        String classId = request.getParameter("classId");
        String stuCard = request.getParameter("stuCard");
        String subjectId = request.getParameter("subjectId");
        String subType = request.getParameter("subType");
        String gradeCodes = request.getParameter("gradeCodes");
        String unitId = getLoginInfo().getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String acadyear = examInfo.getAcadyear();
        String semester = examInfo.getSemester();
        //班级下学生
        List<Student> stuList = new ArrayList<>();
        //不排考学生
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
//        Map<String,Student> studentMap = new HashMap<String,Student>();

        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCodes), new TR<List<Grade>>() {
        });
        String gradeId = gradeList.get(0).getId();
        List<Student> stuList1 = null;
        if (StringUtils.isNotBlank(classId)) {
            if (ExammanageConstants.CLASS_TYPE1.equals(classType)) {
                stuList1 = SUtils.dt(studentService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
                });
            } else {
                stuList1 = SUtils.dt(studentService.findByTeachClassIds(new String[]{classId}), new TR<List<Student>>() {
                });
            }
        } else {
            stuList1 = SUtils.dt(studentService.findByGradeId(gradeId), new TR<List<Student>>() {
            });
        }
        if (StringUtils.isNotBlank(subType)) {
            Set<String> stuIds = null;
            if (CollectionUtils.isNotEmpty(gradeList)) {
                if ("1".equals(subType)) {//选考
                    Map<String, Set<String>> xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, gradeId, true, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                    });
                    if (xuankaoStuIdMap == null)
                        xuankaoStuIdMap = new HashMap<>();
                    stuIds = xuankaoStuIdMap.get(subjectId);
                } else {//学考
                    Map<String, Set<String>> xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, gradeId, false, new String[]{subjectId}), new TR<Map<String, Set<String>>>() {
                    });
                    if (xuekaoStuIdMap == null)
                        xuekaoStuIdMap = new HashMap<>();
                    stuIds = xuekaoStuIdMap.get(subjectId);
                }
                for (Student stu : stuList1) {
                    if (stuIds.contains(stu.getId())) {
                        stuList.add(stu);
                    }
                }
            }
        } else {
            stuList.addAll(stuList1);
        }
        ArrayList<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
        //考号
        Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        boolean flag = "1".equals(stuCard);//是否是学号
        Map<String, String> inMap = null;
        for (Student student : stuList) {
            if (filterMap.containsKey(student.getId())) {
                continue;
            }
            inMap = new HashMap<String, String>();
            if (flag) {
                inMap.put("*学号", student.getStudentCode());
            } else {
                inMap.put("*考号", examNumMap.get(student.getId()));
            }
            inMap.put("*姓名", student.getStudentName());
            inMap.put("*状态", "正常");
            recordList.add(inMap);
        }

        List<String> titleList = getRowTitleList(stuCard);//表头
        if (ExammanageConstants.EXAM_TYPE_FINAL.equals(examType)) {
            titleList = getRowTitleList2(stuCard);//表头
        }

        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        sheetName2RecordListMap.put(getObjectName(), recordList);
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile("成绩录入", titleMap, sheetName2RecordListMap, response);
    }

    @RequestMapping("/validate/{flag}")
    @ResponseBody
    public String validate(String filePath, String stuCard, String validRowStartNo, @PathVariable("flag") String flag) {
        logger.info("模板校验中......");
        if (StringUtils.isBlank(validRowStartNo)) {
            validRowStartNo = "0";
        }
        try {
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
                    Integer.valueOf(validRowStartNo), "2".equals(flag) ? getRowTitleList2(stuCard).size() : getRowTitleList(stuCard).size());
            return templateValidate(datas, "2".equals(flag) ? getRowTitleList2(stuCard) : getRowTitleList(stuCard));

        } catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

    private String result(int totalCount, int successCount, int errorCount, List<String[]> errorDataList) {
        Json importResultJson = new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }

    @Override
    public String getObjectName() {
        return "scoreImport";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、改变选项后请重新上传模板</p>";
    }

    public List<String> getRowTitleList(String stuCard) {
        List<String> tis = new ArrayList<String>();
        tis.add("1".equals(stuCard) ? "*学号" : "*考号");
        tis.add("*姓名");
        tis.add("*考试成绩");
        tis.add("*状态");
        return tis;
    }

    public List<String> getRowTitleList2(String stuCard) {
        List<String> tis = new ArrayList<String>();
        tis.add("1".equals(stuCard) ? "*学号" : "*考号");
        tis.add("*姓名");
        tis.add("*考试成绩");
        tis.add("*状态");
        tis.add("*总评成绩");
        return tis;
    }

    @Override
    public String dataImport(String filePath, String params) {
        return null;
    }

    @Override
    public List<String> getRowTitleList() {
        return null;
    }

}
