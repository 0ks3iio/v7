package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.service.EmEnrollStuCountService;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmJoinexamschInfoService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/exammanage/edu/examStu/enrollstuimport")
public class EnrollStudentsImportAction extends DataImportAction {

    private Logger logger = Logger.getLogger(EnrollStudentsImportAction.class);

    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmEnrollStuCountService emEnrollStuCountService;

    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String execute(String examId, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        // 业务名称
        map.put("businessName", "报名学生导入");
        // 导入URL
        map.put("businessUrl", "/exammanage/edu/examStu/enrollstuimport/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/edu/examStu/enrollstuimport/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        map.put("description", getDescription());
        map.put("businessKey", UuidUtils.generateUuid());

        //如果列名在第一行的就不需要传
        //map.put("validRowStartNo",1);
        //模板校验
        map.put("validateUrl", "/exammanage/edu/examStu/enrollstuimport/validate");

        JSONObject obj = new JSONObject();
        obj.put("unitId", unitId);
        obj.put("examId", examId);
        map.put("monthPermance", JSON.toJSONString(obj));
        return "/exammanage/importFtl/enrollStudentsImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "enrollStudentsImport";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、请填写正确的学生信息</p>";

    }

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("学校");
        tis.add("学籍号");
        tis.add("姓名");
        return tis;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        logger.info("业务数据处理中......");
        //每一行数据   表头列名：0
        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 1, getRowTitleList().size());
        //错误数据序列号
        int sequence = 1;
        int totalSize = datas.size();

        List<String[]> errorDataList = new ArrayList<String[]>();
        if (CollectionUtils.isEmpty(datas)) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = "学校";
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize, 0, 0, errorDataList);
        }

        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String unitId = (String) performance.get("unitId");
        String examId = (String) performance.get("examId");

        //获取考试信息
        List<EmExamInfo> examInfos = emExamInfoService.findListByIds(new String[]{examId});
        EmExamInfo emExamInfo = examInfos.get(0);
        //String gradeCodes = emExamInfo.getGradeCodes();

        //获取考试相关学校信息
        List<EmJoinexamschInfo> emJoinexamschInfos = emJoinexamschInfoService.findByExamId(examId);
        Set<String> schoolIds = EntityUtils.getSet(emJoinexamschInfos, "schoolId");
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        Map<String, String> schoolNameMap = EntityUtils.getMap(schools, "schoolName", "id");

        //获取每个学校的考试年级对象
        Map<String, List<Grade>> gradeMap = SUtils.dt(gradeRemoteService.findBySchoolIdMap(schoolIds.toArray(new String[0])), new TypeReference<Map<String, List<Grade>>>() {
        });
        List<String> gradeList = Lists.newArrayList();
        for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
            List<Grade> grades = entry.getValue();
            for (Grade grade : grades) {
                //if (grade.getGradeCode().equals(gradeCodes)) {
                gradeList.add(grade.getId());
                //break;
                //}
            }
        }

        List<Clazz> clazzs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(gradeList)) {
            clazzs = SUtils.dt(classRemoteService.findByInGradeIds(gradeList.toArray(new String[0])), new TR<List<Clazz>>() {
            });
        }
        Set<String> clazzIds = EntityUtils.getSet(clazzs, "id");

        //获取所有考试学生信息
        List<Student> students = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clazzIds)) {
            students = SUtils.dt(studentRemoteService.findByClassIds(clazzIds.toArray(new String[0])), new TR<List<Student>>() {
            });
        }
        Map<String, Student> studentMap = EntityUtils.getMap(students, "unitiveCode");

        //已报名的学生信息
        List<EmEnrollStudent> emEnrollStudents = emEnrollStudentService.findByExamId(examId);
        List<String> enrollStudentsList = Lists.newArrayList();
        for (EmEnrollStudent emEnrollStudent : emEnrollStudents) {
            enrollStudentsList.add(emEnrollStudent.getSchoolId() + "_" + emEnrollStudent.getExamId() + "_" + emEnrollStudent.getStudentId());
        }


        List<String> headList = getRowTitleList();
        Map<String, Integer> numMap = Maps.newHashMap();
        List<String> stuOneList = Lists.newArrayList();

        List<EmEnrollStudent> insertList = new ArrayList<EmEnrollStudent>();
        EmEnrollStudent em = null;
        int successCount = 0;
        for (String[] arr : datas) {
            String[] errorData = new String[4];
            sequence++;

            Boolean haveerror = false;
            for (int i = 0; i < headList.size(); i++) {
                if (StringUtils.isBlank(arr[i])) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = headList.get(i);
                    errorData[2] = "";
                    errorData[3] = "不能为空";
                    errorDataList.add(errorData);
                    haveerror = true;
                    break;
                }
            }

            if (haveerror) {
                continue;
            }

            if (!schoolNameMap.containsKey(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学校";
                errorData[2] = "";
                errorData[3] = "该场考试此学校不参与";
                errorDataList.add(errorData);
                continue;
            }

            String schoolId = schoolNameMap.get(arr[0]);

            if (!studentMap.containsKey(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学籍号";
                errorData[2] = "";
                errorData[3] = "该学籍号不存在或此学生不在考试对象范围内";
                errorDataList.add(errorData);
                continue;
            }

            Student student = studentMap.get(arr[1]);

            if (!schoolId.equals(student.getSchoolId())) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学校";
                errorData[2] = "";
                errorData[3] = arr[0] + "没有该学生";
                errorDataList.add(errorData);
                continue;
            }

            if (!student.getStudentName().equals(arr[2])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "姓名";
                errorData[2] = "";
                errorData[3] = "姓名错误";
                errorDataList.add(errorData);
                continue;
            }

            if (stuOneList.contains(student.getId())) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "姓名";
                errorData[2] = "";
                errorData[3] = "该学生重复导入";
                errorDataList.add(errorData);
                continue;
            } else {
                stuOneList.add(student.getId());
            }

            successCount++;
            if (enrollStudentsList.contains(schoolId + "_" + examId + "_" + student.getId())) {
                continue;
            }

            em = new EmEnrollStudent();
            em.setId(UuidUtils.generateUuid());
            em.setUnitId(unitId);
            em.setSchoolId(schoolId);
            em.setExamId(examId);
            em.setStudentId(student.getId());
            em.setClassId(student.getClassId());
            em.setHasPass(ExammanageConstants.ENROLL_STU_PASS_0);
            insertList.add(em);

            if (numMap.containsKey(examId + "_" + schoolId)) {
                numMap.put(examId + "_" + schoolId, numMap.get(examId + "_" + schoolId) + 1);
            } else {
                numMap.put(examId + "_" + schoolId, 1);
            }
        }

        try {
            emEnrollStudentService.saveAll(insertList.toArray(new EmEnrollStudent[insertList.size()]));
            List<EmEnrollStuCount> emEnrollStuCounts = emEnrollStuCountService.findByExamId(examId, null);
            Map<String, EmEnrollStuCount> stuCountMap = Maps.newHashMap();
            for (EmEnrollStuCount count : emEnrollStuCounts) {
                stuCountMap.put(count.getExamId() + "_" + count.getSchoolId(), count);
            }
            List<EmEnrollStuCount> emEnrollStuCountList = Lists.newArrayList();
            EmEnrollStuCount stuCount = null;
            for (Map.Entry<String, Integer> entry : numMap.entrySet()) {
                if (stuCountMap.containsKey(entry.getKey())) {
                    stuCount = stuCountMap.get(entry.getKey());
                    stuCount.setSumCount(entry.getValue() + stuCount.getSumCount());
                } else {
                    stuCount = new EmEnrollStuCount();
                    stuCount.setId(UuidUtils.generateUuid());
                    stuCount.setExamId(examId);
                    stuCount.setSchoolId(entry.getKey().split("_")[1]);
                    stuCount.setPassNum(0);
                    stuCount.setNotPassNum(0);
                    stuCount.setSumCount(entry.getValue());
                }
                emEnrollStuCountList.add(stuCount);
            }
            emEnrollStuCountService.saveAll(emEnrollStuCountList.toArray(new EmEnrollStuCount[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return result(totalSize, 0, totalSize, errorDataList);
        }

        int errorCount = totalSize - successCount;
        String result = result(totalSize, successCount, errorCount, errorDataList);
        return result;

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
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        List<String> titleList = getRowTitleList();//表头
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        sheetName2RecordListMap.put(getObjectName(), new ArrayList<Map<String, String>>());
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile("报名学生导入", titleMap, sheetName2RecordListMap, response);
    }

    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo) {
        logger.info("模板校验中......");
        if (StringUtils.isBlank(validRowStartNo)) {
            validRowStartNo = "0";
        }
        try {
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
                    Integer.valueOf(validRowStartNo), getRowTitleList().size());
            return templateValidate(datas, getRowTitleList());

        } catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

}
