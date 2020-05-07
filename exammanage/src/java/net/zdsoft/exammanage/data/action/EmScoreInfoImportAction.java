package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/exammanage/scoreInfo")
public class EmScoreInfoImportAction extends DataImportAction {
    @Autowired
    EmClassInfoService emClassInfoService;
    private Logger logger = Logger.getLogger(EmScoreInfoImportAction.class);
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private ClassRemoteService classRemoteService;

    @ControllerInfo("进入查询头")
    @RequestMapping("/head")
    public String index(String examId, ModelMap map) {
        map.put("examId", examId);
        List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamId(examId);
        map.put("emSubjectList", emSubjectList);

        return "/exammanage/importFtl/scoreIndexImport.ftl";
    }

    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String execute(String examId, String subjectId, String exportType, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        // 业务名称
        map.put("businessName", "成绩导入");
        // 导入URL
        map.put("businessUrl", "/exammanage/scoreInfo/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/scoreInfo/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        map.put("description", "");
        map.put("businessKey", UuidUtils.generateUuid());
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 1);
        //模板校验
        map.put("validateUrl", "/exammanage/scoreInfo/validate?exportType=" + exportType);
        // 导入说明
        StringBuffer description = new StringBuffer();
        description.append(getDescription());

        //导入参数
        JSONObject obj = new JSONObject();
        obj.put("examId", examId);
        obj.put("unitId", unitId);
        obj.put("subjectId", subjectId);
        obj.put("exportType", exportType);
        obj.put("ownerId", loginInfo.getOwnerId());
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("description", description);
        return "/exammanage/importFtl/scoreImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "scoreImport";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、不同的导入类型有不同的导入模板</p>"
                + "<p>2、改变科目和导入类型后请重新上传模板</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        return null;
    }

    public List<String> getRowTitleList2(String exportType) {
        List<String> tis = new ArrayList<String>();
        if ("1".equals(exportType)) {
            //班级
            tis.add("学号");
            tis.add("学生姓名");
            tis.add("班级");
            tis.add("分数");
            tis.add("考试状态");
            tis.add("分数统计状态");
        } else if ("2".equals(exportType)) {
            //考号
            tis.add("考号");
            tis.add("分数");
            tis.add("考试状态");
            tis.add("分数统计状态");
        }
        return tis;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        logger.info("业务数据处理中......");
        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String examId = (String) performance.get("examId");
        String unitId = (String) performance.get("unitId");
        String subjectId = (String) performance.get("subjectId");
        String exportType = (String) performance.get("exportType");
        String ownerId = (String) performance.get("ownerId");
        int sequence = 2;
        List<String[]> errorDataList = new ArrayList<String[]>();
        //每一行数据   表头列名：0
        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 2, getRowTitleList2(exportType).size());
        int totalSize = datas.size();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            if ("1".equals(exportType)) {
                errorData[1] = "学号";
            } else {
                errorData[1] = "考号";
            }
            errorData[2] = "";
            errorData[3] = "考试不存在";
            errorDataList.add(errorData);
            return result(totalSize, 0, 0, errorDataList);
        }

        String acadyear = examInfo.getAcadyear();
        String semester = examInfo.getSemester();
        if (CollectionUtils.isEmpty(datas)) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            if ("1".equals(exportType)) {
                errorData[1] = "学号";
            } else {
                errorData[1] = "考号";
            }
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize, 0, 0, errorDataList);
        }
        List<String> studentIdsList = new ArrayList<String>();
        //不排考学生
        Map<String, String> filteationMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);

        List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
        EmSubjectInfo info = emSubjectList.get(0);
        List<McodeDetail> gradeTypeList = new ArrayList<McodeDetail>();
        if (info.getInputType().equals("G") && StringUtils.isNotBlank(info.getGradeType())) {
            gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(info.getGradeType()), new TypeReference<List<McodeDetail>>() {
            });
        }
        List<McodeDetail> scoreStatusList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-CJLX"), new TypeReference<List<McodeDetail>>() {
        });
        List<EmScoreInfo> insertList = new ArrayList<EmScoreInfo>();
        Pattern p = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
        int successCount = 0;
        if ("1".equals(exportType)) {
            List<EmClassInfo> emClassList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
            List<Student> studentList = new ArrayList<Student>();
            List<Clazz> clazzList = new ArrayList<Clazz>();
            if (CollectionUtils.isNotEmpty(emClassList)) {
                Set<String> classIds = EntityUtils.getSet(emClassList, "classId");
                clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
            }
            Map<String, Clazz> ClazzsMap = new HashMap<String, Clazz>();
            if (CollectionUtils.isNotEmpty(clazzList)) {
                List<String> clazzIdList = EntityUtils.getList(clazzList, "id");
                ClazzsMap = EntityUtils.getMap(clazzList, "id");
                studentList = SUtils.dt(studentRemoteService.findByClassIds(clazzIdList.toArray(new String[0])), new TR<List<Student>>() {
                });
            }
            Map<String, Student> studentsMap = new HashMap<String, Student>();
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentsMap = EntityUtils.getMap(studentList, "studentCode");
            }
            for (String[] arr : datas) {
                String[] errorData = new String[4];
                sequence++;

                if (StringUtils.isBlank(arr[0])) {
                    //学号为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "学号";
                    errorData[2] = "";
                    errorData[3] = "学号不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[1])) {
                    //学生姓名为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "学生姓名";
                    errorData[2] = "";
                    errorData[3] = "学生姓名不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[2])) {
                    //班级为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "班级";
                    errorData[2] = "";
                    errorData[3] = "班级不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[3])) {
                    //分数为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "分数";
                    errorData[2] = "";
                    errorData[3] = "分数不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[4])) {
                    //考试状态为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考试状态";
                    errorData[2] = "";
                    errorData[3] = "考试状态不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[5])) {
                    //分数统计状态为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "分数统计状态";
                    errorData[2] = "";
                    errorData[3] = "分数统计状态不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                Student student = studentsMap.get(arr[0]);
                if (student == null) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "学号";
                    errorData[2] = "";
                    errorData[3] = "学号不存在或学号对应的学生非本次考试的考生";
                    errorDataList.add(errorData);
                    continue;
                }
                String studentId = student.getId();
                String classId = student.getClassId();
                String studentName = student.getStudentName();
                Clazz clazz = ClazzsMap.get(classId);
                String className = clazz.getClassNameDynamic();

                if (!arr[1].equals(studentName)) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "学生姓名";
                    errorData[2] = arr[1];
                    errorData[3] = "学生姓名错误";
                    errorDataList.add(errorData);
                    continue;
                }

                if (!arr[2].equals(className)) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "班级";
                    errorData[2] = arr[2];
                    errorData[3] = "班级错误";
                    errorDataList.add(errorData);
                    continue;
                }

                if (filteationMap.get(studentId) != null) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "学号";
                    errorData[2] = "";
                    errorData[3] = "该学生不排考";
                    errorDataList.add(errorData);
                    continue;
                }

                if (studentIdsList.contains(studentId)) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "学号";
                    errorData[2] = "";
                    errorData[3] = "请勿重复导入成绩";
                    errorDataList.add(errorData);
                    continue;
                }

                String score = "";
                if (info.getInputType().equals("G")) {
                    boolean isGradeType = false;
                    for (McodeDetail detail : gradeTypeList) {
                        if (arr[3].equals(detail.getMcodeContent())) {
                            score = detail.getThisId();
                            isGradeType = true;
                            break;
                        }
                    }

                    if (!isGradeType) {
                        //等第有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "分数";
                        errorData[2] = "";
                        errorData[3] = "请填写有效的等第";
                        errorDataList.add(errorData);
                        continue;
                    }
                } else {
                    Matcher m = p.matcher(arr[3]);
                    if (!m.matches()) {
                        //分数有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "分数";
                        errorData[2] = "";
                        errorData[3] = "格式不正确(最多3位整数，2位小数)!";
                        errorDataList.add(errorData);
                        continue;
                    }

                    if (Float.parseFloat(arr[3]) < 0 || Float.parseFloat(arr[3]) > info.getFullScore()) {
                        //分数有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "分数";
                        errorData[2] = "";
                        errorData[3] = "分数为0-" + info.getFullScore() + "之间(包括0和" + info.getFullScore() + ")";
                        errorDataList.add(errorData);
                        continue;
                    }
                    score = arr[3];
                }

                String scoreStatus = "";
                boolean isScoreStatus = false;
                for (McodeDetail detail : scoreStatusList) {
                    if (arr[4].equals(detail.getMcodeContent())) {
                        scoreStatus = detail.getThisId();
                        isScoreStatus = true;
                        break;
                    }
                }

                if (!isScoreStatus) {
                    //考试状态有效性
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考试状态";
                    errorData[2] = "";
                    errorData[3] = "请填写有效的考试状态";
                    errorDataList.add(errorData);
                    continue;
                }

                String facet = "";
                if (arr[5].equals("统计")) {
                    facet = "1";
                } else if (arr[5].equals("不统计")) {
                    facet = "0";
                } else {
                    //分数统计状态有效性
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "分数统计状态";
                    errorData[2] = "";
                    errorData[3] = "请填写有效的分数统计状态（统计或不统计）";
                    errorDataList.add(errorData);
                    continue;
                }

                studentIdsList.add(studentId);
                successCount++;
                EmScoreInfo emScoreInfo = new EmScoreInfo();
                emScoreInfo.setAcadyear(acadyear);
                emScoreInfo.setSemester(semester);
                emScoreInfo.setExamId(examId);
                emScoreInfo.setSubjectId(subjectId);
                emScoreInfo.setUnitId(unitId);
                emScoreInfo.setClassId(classId);
                emScoreInfo.setStudentId(studentId);
                emScoreInfo.setScoreStatus(scoreStatus);
                emScoreInfo.setScore(score);
                emScoreInfo.setOperatorId(ownerId);
                emScoreInfo.setInputType(info.getInputType());
                emScoreInfo.setCreationTime(new Date());
                emScoreInfo.setModifyTime(new Date());
                emScoreInfo.setFacet(facet);
                insertList.add(emScoreInfo);
            }
        } else {
            //按考场  考号
            //key:studentId
            Map<String, String> stuNum = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
            //暂不考虑 考号相同
            //key:examNum
            Map<String, String> numStu = new HashMap<String, String>();
            if (stuNum.size() > 0) {
                for (String key : stuNum.keySet()) {
                    numStu.put(stuNum.get(key), key);
                }
            }
            for (String[] arr : datas) {
                String[] errorData = new String[4];
                sequence++;
                String examNum = arr[0];
                String studentId = "";
                if (StringUtils.isBlank(examNum)) {
                    //考号为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考号";
                    errorData[2] = "";
                    errorData[3] = "考号不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[1])) {
                    //分数为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "分数";
                    errorData[2] = "";
                    errorData[3] = "分数不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[2])) {
                    //考试状态为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考试状态";
                    errorData[2] = "";
                    errorData[3] = "考试状态不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(arr[3])) {
                    //分数统计状态为空
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "分数统计状态";
                    errorData[2] = "";
                    errorData[3] = "分数统计状态不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                examNum = examNum.trim();
                if (!numStu.containsKey(examNum)) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考号";
                    errorData[2] = "考号：" + examNum;
                    errorData[3] = "考号不存在";
                    errorDataList.add(errorData);
                    continue;
                }
                studentId = numStu.get(examNum);

                if (filteationMap.get(studentId) != null) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考号";
                    errorData[2] = "";
                    errorData[3] = "该学生不排考";
                    errorDataList.add(errorData);
                    continue;
                }

                if (studentIdsList.contains(studentId)) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考号";
                    errorData[2] = "";
                    errorData[3] = "请勿重复导入成绩";
                    errorDataList.add(errorData);
                    continue;
                }

                String score = "";
                if (info.getInputType().equals("G")) {
                    boolean isGradeType = false;
                    for (McodeDetail detail : gradeTypeList) {
                        if (arr[1].equals(detail.getMcodeContent())) {
                            score = detail.getThisId();
                            isGradeType = true;
                            break;
                        }
                    }

                    if (!isGradeType) {
                        //等第有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "分数";
                        errorData[2] = "";
                        errorData[3] = "请填写有效的等第";
                        errorDataList.add(errorData);
                        continue;
                    }
                } else {
                    Matcher m = p.matcher(arr[1]);
                    if (!m.matches()) {
                        //分数有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "分数";
                        errorData[2] = "";
                        errorData[3] = "格式不正确(最多3位整数，2位小数)!";
                        errorDataList.add(errorData);
                        continue;
                    }

                    if (Float.parseFloat(arr[1]) < 0 || Float.parseFloat(arr[1]) > info.getFullScore()) {
                        //分数有效性
                        errorData[0] = String.valueOf(sequence);
                        errorData[1] = "分数";
                        errorData[2] = "";
                        errorData[3] = "分数为0-" + info.getFullScore() + "之间";
                        errorDataList.add(errorData);
                        continue;
                    }
                    score = arr[1];
                }

                String scoreStatus = "";
                boolean isScoreStatus = false;
                for (McodeDetail detail : scoreStatusList) {
                    if (arr[2].equals(detail.getMcodeContent())) {
                        scoreStatus = detail.getThisId();
                        isScoreStatus = true;
                        break;
                    }
                }

                if (!isScoreStatus) {
                    //考试状态有效性
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考试状态";
                    errorData[2] = "";
                    errorData[3] = "请填写有效的考试状态";
                    errorDataList.add(errorData);
                    continue;
                }

                String facet = "";
                if (arr[3].equals("统计")) {
                    facet = "1";
                } else if (arr[3].equals("不统计")) {
                    facet = "0";
                } else {
                    //分数统计状态有效性
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "分数统计状态";
                    errorData[2] = "";
                    errorData[3] = "请填写有效的分数统计状态（统计或不统计）";
                    errorDataList.add(errorData);
                    continue;
                }

                studentIdsList.add(studentId);
                successCount++;
                EmScoreInfo emScoreInfo = new EmScoreInfo();
                emScoreInfo.setAcadyear(acadyear);
                emScoreInfo.setSemester(semester);
                emScoreInfo.setExamId(examId);
                emScoreInfo.setSubjectId(subjectId);
                emScoreInfo.setUnitId(unitId);
                emScoreInfo.setStudentId(studentId);
                emScoreInfo.setScoreStatus(scoreStatus);
                emScoreInfo.setScore(score);
                emScoreInfo.setOperatorId(ownerId);
                emScoreInfo.setInputType(info.getInputType());
                emScoreInfo.setCreationTime(new Date());
                emScoreInfo.setModifyTime(new Date());
                emScoreInfo.setFacet(facet);
                insertList.add(emScoreInfo);
            }
            Map<String, String> studentIdAndClassIdMap = new HashMap<String, String>();
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdsList.toArray(new String[]{})), new TR<List<Student>>() {
            });
            for (Student student : studentList) {
                studentIdAndClassIdMap.put(student.getId(), student.getClassId());
            }
            for (EmScoreInfo emScoreInfo : insertList) {
                emScoreInfo.setClassId(studentIdAndClassIdMap.get(emScoreInfo.getStudentId()));
            }
        }

        if (CollectionUtils.isNotEmpty(insertList)) {
            emScoreInfoService.saveAll(insertList, new HashSet<String>(studentIdsList), subjectId, examId);
        }

        int errorCount = totalSize - successCount;
        String result = result(totalSize, successCount, errorCount, errorDataList);
        return result;
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        String templateParams = request.getParameter("templateParams");
        if (StringUtils.isNotBlank(templateParams)) {
            JSONObject performance = JSON.parseObject(templateParams, JSONObject.class);
            String examId = (String) performance.get("examId");
            String unitId = (String) performance.get("unitId");
            String subjectId = (String) performance.get("subjectId");
            String exportType = (String) performance.get("exportType");
            Course course = SUtils.dt(courseRemoteService.findOneById(subjectId), new TR<Course>() {
            });
            if (course == null) {
                logger.info("科目不存在");
                return;
            }
            //拿到该科目的设置
            List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
            if (CollectionUtils.isEmpty(emSubjectList) || emSubjectList.size() > 1) {
                //科目不存在 不唯一
                logger.info("科目设置不存在或不唯一");
                return;
            }
            EmSubjectInfo info = emSubjectList.get(0);
            //录入方式

            List<String> titleList = new ArrayList<String>();//表头
            //标题
            String tile = "成绩导入";
            if ("1".equals(exportType)) {
                //按班级
                titleList = getRowTitleList2("1");
                tile = tile + "--按班级";
            } else if ("2".equals(exportType)) {
                //按考号
                titleList = getRowTitleList2("2");
                tile = tile + "--按考号";
            }

            Map<String, List<Map<String, String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
            sheetName2Map.put(getObjectName(), new ArrayList<Map<String, String>>());
            Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
            titleMap.put(getObjectName(), titleList);


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            int size = titleList.size();

            //sheet 合并单元格
            CellRangeAddress car = new CellRangeAddress(0, 0, 0, size - 1);
            sheet.addMergedRegion(car);
            //列宽
            for (int i = 0; i < size; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }

            //第一行  注意事项--0
            HSSFRow titleRow = sheet.createRow(0);
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont headfont = workbook.createFont();
            headfont.setFontHeightInPoints((short) 12);// 字体大小
            headfont.setBold(true);//粗体显示
            style.setFont(headfont);
            style.setAlignment(HorizontalAlignment.LEFT);//水平
            style.setAlignment(HorizontalAlignment.CENTER); //居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            style.setWrapText(true);//自动换行
            //高度：5倍默认高度
            titleRow.setHeightInPoints(3 * sheet.getDefaultRowHeightInPoints());
            HSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(style);
            //注意事项
            titleCell.setCellValue(new HSSFRichTextString(tile));

            //第二行 表头--1
            HSSFRow rowTitle = sheet.createRow(1);
            for (int j = 0; j < size; j++) {
                HSSFCell cell = rowTitle.createCell(j);
                cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
            }


            ExportUtils exportUtils = ExportUtils.newInstance();
            exportUtils.outputData(workbook, "成绩导入", response);
        } else {
            logger.info("模板参数为空");
        }
    }

    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo, String exportType) {
        logger.info("模板校验中......");
        if (StringUtils.isBlank(validRowStartNo)) {
            validRowStartNo = "0";
        }
        try {
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
                    Integer.valueOf(validRowStartNo), getRowTitleList2(exportType).size());
            return templateValidate(datas, getRowTitleList2(exportType));

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

}
