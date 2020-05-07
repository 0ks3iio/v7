package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.entity.EmFiltration;
import net.zdsoft.exammanage.data.service.EmClassInfoService;
import net.zdsoft.exammanage.data.service.EmFiltrationService;
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
@RequestMapping("/exammanage/filter")
public class FilterImportAction extends DataImportAction {

    private Logger logger = Logger.getLogger(FilterImportAction.class);

    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EmFiltrationService emFiltrationService;

    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String execute(String examId, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();

        // 业务名称
        map.put("businessName", "不排考学生");
        // 导入URL
        map.put("businessUrl", "/exammanage/filter/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/filter/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        StringBuffer description = new StringBuffer();
        description.append(getDescription());

        // 如果导入文件中前面有说明性文字的  这里需要传一个有效数据开始行（列名那一行）
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 0);
        //模板校验
        map.put("validateUrl", "/exammanage/filter/validate");


        List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
        //取到参加的班级id
        Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
        if (classIds.size() > 0) {
            List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classList)) {
                description.append("<p>3、参加该考试班级有:");
                String str = "";
                for (Clazz z : classList) {
                    str = str + "、" + z.getClassNameDynamic();
                }
                str = str.substring(1);
                description.append(str + "</p>");
            }
        }
        map.put("description", description);
        map.put("businessKey", UuidUtils.generateUuid());

        JSONObject obj = new JSONObject();
        obj.put("examId", examId);

        obj.put("unitId", unitId);
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("examId", examId);
        return "/exammanage/importFtl/filterImport.ftl";
    }


    @Override
    public String getObjectName() {
        return "studentFilter";
    }

    ;

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入时，会根据班级、学号、学生三者判断数据是否正确</p>"
                + "<p>2、导入班级名称为年级名称+班级名称</p>";
    }

    ;

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("班级");
        tis.add("学号");
        tis.add("学生姓名");
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
            errorData[1] = "班级";
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize, 0, 0, errorDataList);
        }

        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String examId = (String) performance.get("examId");
        String unitId = (String) performance.get("unitId");

        //参与的考试的班级
        List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
        if (CollectionUtils.isEmpty(classInfoList)) {
            for (String[] arr : datas) {
                String[] errorData = new String[4];
                sequence++;
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "班级";
                errorData[2] = "";
                errorData[3] = "该单位不参与该考试";
                errorDataList.add(errorData);
            }
            return result(totalSize, 0, totalSize, errorDataList);
        }

        //取到参加的班级id
        Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
        //一般不存在重名的班级（key:班级动态名称  value:id）
        Map<String, List<String>> classNameMap = new HashMap<String, List<String>>();
        //一般不存在班级学号姓名一样的学生    key:unitId_stucode_stuName  value:stuId）
        Map<String, List<String>> stuMap = new HashMap<String, List<String>>();
        if (classIds.size() > 0) {
            List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classList)) {
                for (Clazz z : classList) {
                    if (!classNameMap.containsKey(z.getClassNameDynamic())) {
                        classNameMap.put(z.getClassNameDynamic(), new ArrayList<String>());
                    }
                    classNameMap.get(z.getClassNameDynamic()).add(z.getId());
                }
            }

            if (CollectionUtils.isNotEmpty(studentList)) {
                for (Student s : studentList) {
                    String key = s.getClassId() + "_" + s.getStudentCode() + "_" + s.getStudentName();
                    if (!stuMap.containsKey(key)) {
                        stuMap.put(key, new ArrayList<String>());
                    }
                    stuMap.get(key).add(s.getId());
                }
            }
        }
        //已存在
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);


        List<EmFiltration> insertList = new ArrayList<EmFiltration>();
        EmFiltration em = null;
        int successCount = 0;
        for (String[] arr : datas) {
            String[] errorData = new String[4];
            sequence++;
            if (StringUtils.isBlank(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "班级";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学号";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(arr[2])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学生姓名";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }
            String className = arr[0].trim();
            String stuCode = arr[1].trim();
            String stuName = arr[2].trim();
            if (!classNameMap.containsKey(className)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "班级";
                errorData[2] = "班级：" + className;
                errorData[3] = className + "不参加该考试";
                errorDataList.add(errorData);
                continue;
            }
            List<String> cIds = classNameMap.get(className);
            if (cIds.size() > 1) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "班级";
                errorData[2] = "班级：" + className;
                errorData[3] = "班级名称不唯一";
                errorDataList.add(errorData);
                continue;
            }
            String classId = cIds.get(0);
            String key = classId + "_" + stuCode + "_" + stuName;
            if (!stuMap.containsKey(key)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学号";
                errorData[2] = "学号：" + stuCode;
                errorData[3] = "没有找到该学生";
                errorDataList.add(errorData);
                continue;
            }
            List<String> stuIds = stuMap.get(key);
            if (stuIds.size() > 1) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学生姓名";
                errorData[2] = "学生姓名：" + stuName;
                errorData[3] = "学生姓名不唯一";
                errorDataList.add(errorData);
                continue;
            }
            String studentId = stuIds.get(0);
            successCount++;
            if (filterMap.containsKey(studentId)) {
                continue;
            }

            em = new EmFiltration();
            em.setExamId(examId);
            em.setId(UuidUtils.generateUuid());
            em.setSchoolId(unitId);
            em.setStudentId(studentId);
            em.setType(ExammanageConstants.FILTER_TYPE1);
            insertList.add(em);
        }

        try {
            emFiltrationService.saveOrDel(insertList, examId, null, ExammanageConstants.FILTER_TYPE1);
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
        sheetName2RecordListMap.put(getObjectName(),
                new ArrayList<Map<String, String>>());
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile("不排考学生信息", titleMap, sheetName2RecordListMap, response);
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
