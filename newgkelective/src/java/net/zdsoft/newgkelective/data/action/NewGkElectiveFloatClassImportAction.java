package net.zdsoft.newgkelective.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.*;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.basedata.entity.Student;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date: 2019/05/31
 * 新全手动模式教学班导入
 */
@Controller
@RequestMapping("/newgkelective/floatingplan/teachClass/import")
public class NewGkElectiveFloatClassImportAction extends NewGkElectiveDivideCommonAction {

    @Autowired
    private StudentRemoteService studentRemoteService;

    @RequestMapping("/execute")
    public String execute(String divideId, String subjectType, ModelMap map) {
        // 业务名称
        map.put("businessName", "教学班信息导入");
        // 导入URL
        map.put("businessUrl", "/newgkelective/floatingplan/teachClass/import/data");
        // 模板校验
        map.put("validateUrl", "/newgkelective/floatingplan/teachClass/import/validate?divideId=" + divideId + "&subjectType=" + subjectType);
        // 导入模板下载
        map.put("templateDownloadUrl", "/newgkelective/floatingplan/teachClass/import/template?divideId=" + divideId + "&subjectType=" + subjectType);
        // 错误模板导出
        map.put("exportErrorExcelUrl", "/newgkelective/floatingplan/teachClass/import/exportErrorExcel");
        // 导入对象
        map.put("objectName", getObjectName());
        // 导入说明
        map.put("description", getDescription());
        map.put("businessKey", UuidUtils.generateUuid());
        NewGkDivide divide = newGkDivideService.findOne(divideId);
        if (divide == null) {
            return errorFtl(map, "对应排课数据不存在");
        }
        String divideMame = divide.getDivideName();
        map.put("divide", divide);
        String tmp;
        if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
            tmp = "选考";
        } else {
            tmp = "学考";
        }
        map.put("divideName", divideMame + tmp + "教学班信息导入");
        map.put("divideId", divideId);
        map.put("subjectType", subjectType);
        return "/newgkelective/floatingPlan/floatTeachClassImport.ftl";
    }

    @Override
    public String getObjectName() {
        return null;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> rowTitleList = new ArrayList<>();
        rowTitleList.add("学号");
        rowTitleList.add("姓名");
        rowTitleList.add("科目");
        rowTitleList.add("教学班名称");
        rowTitleList.add("时间点");
        return rowTitleList;
    }

    @Override
    @RequestMapping("/data")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        params = params.replace("&quot;", "\"");
        JSONObject jsStr = JSONObject.parseObject(params);
        String divideId = jsStr.getString("divideId");
        String subjectType = jsStr.getString("subjectType");
        NewGkDivide divide = newGkDivideService.findById(divideId);
        List<String[]> errorDataList = new ArrayList<>();
        Json importResultJson = new Json();
        if (divide == null) {
            String[] errorData=new String[4];
            errorData[0] = String.valueOf(1);
            errorData[1] = "";
            errorData[2] = "";
            errorData[3] = "该分班数据不存在";
            errorDataList.add(errorData);
            importResultJson.put("totalCount", 0);
            importResultJson.put("successCount", 0);
            importResultJson.put("errorCount", 0);
            importResultJson.put("errorData", errorDataList);
            importResultJson.put("errorExcelPath", "");
            return importResultJson.toJSONString();
        }

        List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList().size());
        String[] titleArr = datas.get(0);
        datas.remove(0);

        if (CollectionUtils.isEmpty(datas)) {
            String[] errorData = new String[4];
            errorData[0] = String.valueOf(1);
            errorData[1] = "";
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            importResultJson.put("totalCount", datas.size());
            importResultJson.put("successCount", 0);
            importResultJson.put("errorCount", datas.size());
            importResultJson.put("errorData", errorDataList);
            importResultJson.put("errorExcelPath", "");
            return importResultJson.toJSONString();
        }

        String unitId = divide.getUnitId();
        Integer batchCount;
        if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
            batchCount = divide.getBatchCountTypea();
        } else {
            batchCount = divide.getBatchCountTypeb();
        }
        if (batchCount == null) {
            String[] errorData = new String[4];
            errorData[0] = String.valueOf(1);
            errorData[1] = "";
            errorData[2] = "";
            errorData[3] = "分班方案批次点信息未确定";
            errorDataList.add(errorData);
            importResultJson.put("totalCount", datas.size());
            importResultJson.put("successCount", 0);
            importResultJson.put("errorCount", datas.size());
            importResultJson.put("errorData", errorDataList);
            importResultJson.put("errorExcelPath", "");
            return importResultJson.toJSONString();
        }

        List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {subjectType});
        Set<String> openSubjectIdSet = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(openSubjectIdSet.toArray(new String[0])), Course.class);
        Map<String, String> courseNameToIdMap = EntityUtils.getMap(courseList, Course::getSubjectName, Course::getId);

        List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
                NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        Map<String, NewGkDivideClass> combinationToBaseClassMap = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
                .collect(Collectors.toMap(e -> e.getRelateId(), e -> e));
        Map<String, NewGkDivideClass> allClassMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getId);
        List<NewGkDivideClass> mixClassList = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
                .collect(Collectors.toList());
        List<NewGkDivideClass> combinationClassList = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && !BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
                .collect(Collectors.toList());
        List<NewGkDivideClass> teachClassList = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()) && Objects.equals(e.getSubjectType(), subjectType))
                .collect(Collectors.toList());
        Map<String, NewGkDivideClass> classNameToTeachClassMap = EntityUtils.getMap(teachClassList, NewGkDivideClass::getClassName);

        // 已安排学生班级信息
        Map<String, NewGkDivideClass> studentIdAndSubjectIdToClass = new HashMap<>();
        if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
            for (NewGkDivideClass one : mixClassList) {
                if (CollectionUtils.isNotEmpty(one.getStudentList()) && StringUtils.isNotBlank(one.getSubjectIdsB())) {
                    for (String subjectId : one.getSubjectIdsB().split(",")) {
                        for (String studentId : one.getStudentList()) {
                            studentIdAndSubjectIdToClass.put(studentId + "_" + subjectId, one);
                        }
                    }
                }
            }
        }
        for (NewGkDivideClass one : combinationClassList) {
            if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType) && StringUtils.isNotBlank(one.getSubjectIds()) && CollectionUtils.isNotEmpty(one.getStudentList())) {
                for (String subjectId : one.getSubjectIds().split(",")) {
                    for (String studentId : one.getStudentList()) {
                        studentIdAndSubjectIdToClass.put(studentId + "_" + subjectId, one);
                    }
                }
            }
            if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType) && StringUtils.isNotBlank(one.getSubjectIdsB()) && CollectionUtils.isNotEmpty(one.getStudentList())) {
                for (String subjectId : one.getSubjectIdsB().split(",")) {
                    for (String studentId : one.getStudentList()) {
                        studentIdAndSubjectIdToClass.put(studentId + "_" + subjectId, one);
                    }
                }
            }
        }
        for (NewGkDivideClass one : teachClassList) {
            if (CollectionUtils.isNotEmpty(one.getStudentList())) {
                for (String studentId : one.getStudentList()) {
                    studentIdAndSubjectIdToClass.put(studentId + "_" + one.getSubjectIds(), one);
                }
            }
        }

        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenBMap = null;
        if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
            studentChosenBMap = new HashMap<>();
        }
        makeStudentChooseResult(divide, openSubjectIdSet, subjectChosenMap, studentChosenMap, studentChosenBMap, subjectType);

        Map<String, NewGkDivideClass> insertClassMap = new HashMap<>();
        List<NewGkClassStudent> insertClassStudentList = new ArrayList<>();

        List<Student> studentList = SUtils.dt(studentRemoteService.findByGradeId(divide.getGradeId()), Student.class);
        // key -> studentCode
        // 暂不考虑学号重复的情况
        Map<String, Student> studentCodeToStudentMap = EntityUtils.getMap(studentList, Student::getStudentCode);

        int index = 0;
        int successCount = 0;
        Pattern pattern = Pattern.compile("^\\d{1}$");
        String[] errorData;
        // 先遍历一遍 标记存在两个时间点的教学班
        Set<String> errorTeachClassNameSet = new HashSet<>();
        Map<String, String> teachClassNameToBatch = new HashMap<>();
        for (String[] classStudentInfoArr : datas) {
            // 教学班名称或者时间点为空 下一轮遍历进行处理
            if (StringUtils.isBlank(classStudentInfoArr[3]) || StringUtils.isBlank(classStudentInfoArr[4])) {
                continue;
            }
            if (teachClassNameToBatch.get(classStudentInfoArr[3]) != null) {
                if (!teachClassNameToBatch.get(classStudentInfoArr[3]).equals(classStudentInfoArr[4])) {
                    errorTeachClassNameSet.add(classStudentInfoArr[3]);
                }
            } else {
                teachClassNameToBatch.put(classStudentInfoArr[3], classStudentInfoArr[4]);
            }
        }
        for (String[] classStudentInfoArr : datas) {
            index++;

            if (StringUtils.isBlank(classStudentInfoArr[0])) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "学号";
                errorData[3] = "学生学号不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (studentCodeToStudentMap.get(classStudentInfoArr[0]) == null) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "学号";
                errorData[3] = "该学生不属于该年级";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(classStudentInfoArr[1])) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "姓名";
                errorData[3] = "学生姓名不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (!classStudentInfoArr[1].equals(studentCodeToStudentMap.get(classStudentInfoArr[0]).getStudentName())) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "姓名";
                errorData[3] = "学生姓名与学号不一致";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(classStudentInfoArr[2])) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "科目";
                errorData[3] = "科目名称不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (courseNameToIdMap.get(classStudentInfoArr[2]) == null) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "科目";
                errorData[3] = "该次分班未开放该科目或科目名称填写错误";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(classStudentInfoArr[3])) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "教学班名称";
                errorData[3] = "教学班名称不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isBlank(classStudentInfoArr[4])) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "时间点";
                errorData[3] = "时间点不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if (!pattern.matcher(classStudentInfoArr[4]).matches()) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "时间点";
                errorData[3] = "时间点信息应为1，2，3...";
                errorDataList.add(errorData);
                continue;
            }
            if (Integer.valueOf(classStudentInfoArr[4]) < 1 || Integer.valueOf(classStudentInfoArr[4]) > 3) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "时间点";
                errorData[3] = "时间点信息超过范围";
                errorDataList.add(errorData);
                continue;
            }
            if (errorTeachClassNameSet.contains(classStudentInfoArr[3])) {
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "时间点";
                errorData[3] = "同一个教学班不能对应两个时间点";
                errorDataList.add(errorData);
                continue;
            }
            if (studentIdAndSubjectIdToClass.get(studentCodeToStudentMap.get(classStudentInfoArr[0]).getId() + "_" + courseNameToIdMap.get(classStudentInfoArr[2])) != null) {
                NewGkDivideClass classTmp = studentIdAndSubjectIdToClass.get(studentCodeToStudentMap.get(classStudentInfoArr[0]).getId() + "_" + courseNameToIdMap.get(classStudentInfoArr[2]));
                errorData = new String[4];
                errorData[0] = String.valueOf(index);
                errorData[1] = "第" + index + "行";
                errorData[2] = "学生";
                errorData[3] = "该学生该科目已安排在" + classTmp.getClassName() + "上课";
                errorDataList.add(errorData);
                continue;
            }
            // 处理教学班批次点信息，与已存在的数据不相同，或同一个教学班存在不同的批次点则都不会存入数据库
            NewGkDivideClass classTmp;
            if (classNameToTeachClassMap.get(classStudentInfoArr[3]) != null) {
                if (classNameToTeachClassMap.get(classStudentInfoArr[3]).getBatch().equals(classStudentInfoArr[4]) && insertClassMap.get(classStudentInfoArr[3]) == null) {
                    classTmp = classNameToTeachClassMap.get(classStudentInfoArr[3]);
                    classTmp.setModifyTime(new Date());
                    insertClassMap.put(classStudentInfoArr[3], classTmp);
                } else {
                    errorData = new String[4];
                    errorData[0] = String.valueOf(index);
                    errorData[1] = "第" + index + "行";
                    errorData[2] = "时间点";
                    errorData[3] = "已存在不同时间点的同名教学班";
                    errorDataList.add(errorData);
                    continue;
                }
            } else {
                if (insertClassMap.get(classStudentInfoArr[3]) == null) {
                    classTmp = initNewGkDivideClass(divideId, courseNameToIdMap.get(classStudentInfoArr[2]), NewGkElectiveConstant.CLASS_TYPE_2);
                    classTmp.setBatch(classStudentInfoArr[4]);
                    classTmp.setClassName(classStudentInfoArr[3]);
                    classTmp.setSubjectType(subjectType);
                    insertClassMap.put(classStudentInfoArr[3], classTmp);
                } else {
                    classTmp = insertClassMap.get(classStudentInfoArr[3]);
                }
            }

            // 避免同一个学生同一门科目重复安排班级
            studentIdAndSubjectIdToClass.put(studentCodeToStudentMap.get(classStudentInfoArr[0]).getId() + "_" + courseNameToIdMap.get(classStudentInfoArr[2]), classTmp);

            // 保存学生
            NewGkClassStudent classStudent = initClassStudent(unitId, divideId, classTmp.getId(), studentCodeToStudentMap.get(classStudentInfoArr[0]).getId());
            insertClassStudentList.add(classStudent);

            successCount++;
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            // 标题行固定
            sheet.createFreezePane(0, 1);

            List<String> titleList = getRowTitleList();
            titleList.add("错误数据");
            titleList.add("错误原因");

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int i = 0; i < titleList.size(); i++) {
                if (i >= titleList.size() - 3) {
                    sheet.setColumnWidth(i, 15 * 256);
                } else {
                    sheet.setColumnWidth(i, 10 * 256);
                }
                HSSFCell cell = rowTitle.createCell(i);
                cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
            }

            for (int i = 0; i < errorDataList.size(); i++) {
                HSSFRow row = sheet.createRow(i+1);
                String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(i)[0]) - 1);
                for (int j=0; j < titleList.size(); j++) {
                    HSSFCell cell = row.createCell(j);
                    if (j < titleList.size() - 2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[j]));
                    } else if (j == titleList.size() - 2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[2]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }
        importResultJson.put("errorExcelPath", errorExcelPath);

        if(CollectionUtils.isNotEmpty(insertClassMap.values()) || CollectionUtils.isNotEmpty(insertClassStudentList)){
            try{
                newGkDivideClassService.saveAllList(unitId, divideId,
                        null, new ArrayList<>(insertClassMap.values()), insertClassStudentList, false);
            }catch(Exception e){
                e.printStackTrace();
                importResultJson.put("totalCount", datas.size());
                importResultJson.put("successCount", 0);
                importResultJson.put("errorCount", datas.size());
                importResultJson.put("errorData", errorDataList);
                return importResultJson.toJSONString();
            }

        }
        importResultJson.put("totalCount", datas.size());
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorDataList.size());
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String divideId = request.getParameter("divideId");
        String subjectType = request.getParameter("subjectType");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        // 标题行固定
        sheet.createFreezePane(0, 2);
        List<String> titleList = getRowTitleList();

        CellRangeAddress car = new CellRangeAddress(0, 0, 0, titleList.size() - 1);
        sheet.addMergedRegion(car);

        // 注意事项样式
        HSSFCellStyle headStyle = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);
        headfont.setColor(HSSFFont.COLOR_RED);
        headStyle.setFont(headfont);
        headStyle.setAlignment(HorizontalAlignment.LEFT);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setWrapText(true);

        // 第一行
        HSSFRow remarkRow = sheet.createRow(0);
        // 高度 5倍默认高度
        remarkRow.setHeightInPoints(5 * sheet.getDefaultRowHeightInPoints());

        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(headStyle);
        // 注意事项
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(headStyle);

        HSSFRow rowTitle = sheet.createRow(1);
        for (int i = 0;i < titleList.size(); i++) {
            if (i == titleList.size() - 1) {
                sheet.setColumnWidth(i, 15 * 256);
            }else{
                sheet.setColumnWidth(i, 10 * 256);
            }
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }

        String fileName = "教学班信息导入";

        ExportUtils.outputData(workbook, fileName, response);
    }

    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String divideId, String subjectType) {
        try{
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
            return templateValidate(datas, getRowTitleList());
        }catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

    @RequestMapping("exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {
        String remark = "填写注意：\n"
                + "1.请确保各教学班时间点不会冲突;\n"
                + "2.若教学班已存在，该教学班之前的学生信息将被覆盖;\n";
        return remark;
    }

    /**
     * @param divide
     * @param openSubjectIdSet 开课科目Id
     * @param subjectChosenMap K:科目 V：学生
     * @param studentChosenMap K:学生 V：选考科目
     * @param studentChosenBMap K:学生 V：学考科目
     * @param subjectType A -> 选考 B -> 学考
     */
    private void makeStudentChooseResult(NewGkDivide divide, Set<String> openSubjectIdSet, Map<String,Set<String>> subjectChosenMap, Map<String,Set<String>> studentChosenMap, Map<String,Set<String>> studentChosenBMap, String subjectType) {
        List<NewGkChoResult> chosenList = newGkChoResultService
                .findByChoiceIdAndKindType(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, divide.getChoiceId());

        for (NewGkChoResult result : chosenList) {
            if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
                if(!subjectChosenMap.containsKey(result.getSubjectId())) {
                    subjectChosenMap.put(result.getSubjectId(), new TreeSet<>());
                }
                subjectChosenMap.get(result.getSubjectId()).add(result.getStudentId());
            }
            if(!studentChosenMap.containsKey(result.getStudentId())) {
                studentChosenMap.put(result.getStudentId(), new TreeSet<>());
            }
            studentChosenMap.get(result.getStudentId()).add(result.getSubjectId());
        }
        if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
            // 取补集
            for (String student : studentChosenMap.keySet()) {
                studentChosenBMap.put(student, new TreeSet<>(CollectionUtils.subtract(openSubjectIdSet, studentChosenMap.get(student))));
            }
            for (Map.Entry<String, Set<String>> entry : studentChosenBMap.entrySet()) {
                for (String subject : entry.getValue()) {
                    if (subjectChosenMap.get(subject) == null) {
                        subjectChosenMap.put(subject, new TreeSet<>());
                    }
                    subjectChosenMap.get(subject).add(entry.getKey());
                }
            }
        }
    }
}
