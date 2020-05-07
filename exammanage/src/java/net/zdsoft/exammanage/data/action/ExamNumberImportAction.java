package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmExamNum;
import net.zdsoft.exammanage.data.service.EmClassInfoService;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmExamNumService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
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
@RequestMapping("/exammanage/examNumber")
public class ExamNumberImportAction extends DataImportAction {
    private Logger logger = Logger.getLogger(ExamPlaceImportAction.class);
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private GradeRemoteService gradeRemoteService;


    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String execute(String examId, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();

        // 业务名称
        map.put("businessName", "考生考号设置");
        // 导入URL
        map.put("businessUrl", "/exammanage/examNumber/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/examNumber/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        map.put("description", getDescription());
        map.put("businessKey", UuidUtils.generateUuid());

        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 1);
        //模板校验
        map.put("validateUrl", "/exammanage/examNumber/validate");

        JSONObject obj = new JSONObject();
        obj.put("examId", examId);
        obj.put("unitId", unitId);
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("examId", examId);
        return "/exammanage/importFtl/examNumberImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "examPlace";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入时，会根据班级、学号、学生三者判断数据是否正确</p>"
                + "<p>2、导入班级名称为年级名称+班级名称</p>"
                + "<p>3、考号要求：不超过50个数字组成</p>"
                + "<p>4、若某学生已有正式考号，且导入文件中该学生考号已被别的学生正式使用，那么导入后该学生的原有考号将被清除</p>";

    }

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("班级");
        tis.add("学号");
        tis.add("学生姓名");
        tis.add("考号");
        return tis;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        logger.info("业务数据处理中......");
        //每一行数据   表头列名：0
        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 2, getRowTitleList().size());
        //先验证数据是否正确---再考试班级中学生 不考虑排不排考
        //所有正确后，判断考号是否重复
        //参数
        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String examId = (String) performance.get("examId");
        String unitId = (String) performance.get("unitId");
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        int totalSize = datas.size();
        int sequence = 2;//初始行
        List<String[]> errorDataList = new ArrayList<String[]>();
        if (CollectionUtils.isEmpty(datas)) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = "班级";
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize, 0, errorDataList.size(), errorDataList);
        }
        //取到参加的班级id
        Set<String> classIds = new HashSet<>();
        if (StringUtils.equals("1", examInfo.getIsgkExamType())) {
            //参与的考试的班级
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
            if (CollectionUtils.isEmpty(classInfoList)) {
                for (String[] s : datas) {
                    String[] errorData = new String[4];
                    sequence++;
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "班级";
                    errorData[2] = "";
                    errorData[3] = "该班级不参与该考试";
                    errorDataList.add(errorData);
                }
                return result(totalSize, 0, errorDataList.size(), errorDataList);
            }
            classIds = EntityUtils.getSet(classInfoList, EmClassInfo::getClassId);
        } else {
            //TODO
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId, examInfo.getAcadyear()), new TR<List<Grade>>() {
            });
            String gId = null;
            for (Grade e : gradeList) {
                if (StringUtils.equals(e.getGradeCode(), examInfo.getGradeCodes())) {
                    gId = e.getId();
                    break;
                }
            }
            if (StringUtils.isNotBlank(gId)) {
                List<Clazz> clsList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gId), new TR<List<Clazz>>() {
                });
                classIds = EntityUtils.getSet(clsList, Clazz::getId);
            }
        }

        //一般不存在重名的班级（key:班级动态名称  value:id）
        Map<String, List<String>> classNameMap = new HashMap<String, List<String>>();
        //一般不存在班级学号姓名一样的学生    key:unitId_stucode_stuName  value:stuId）
        Map<String, List<String>> stuMap = new HashMap<String, List<String>>();
        if (CollectionUtils.isNotEmpty(classIds)) {
            List<Student> studentList = new ArrayList<>();
            if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
                List<EmEnrollStudent> emStus = emEnrollStudentService.findByExamIdAndSchoolIdInClassIds(examId, unitId, classIds.toArray(new String[0]));
                Set<String> stuIds = EntityUtils.getSet(emStus, "studentId");
                studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
                });
            } else {
                studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
                });
            }

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

        Map<String, String> stuNumberMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);


        //文件中不能考号不能重复 key:考号 value:行号
        Map<String, Integer> sameMap = new HashMap<String, Integer>();

        Map<String, String> insertStuIdsMap = new LinkedHashMap<String, String>();//<文件中数据正确的学生，考号>
        Map<String, Integer> insertStuIdsHangMap = new LinkedHashMap<String, Integer>();//<文件中数据正确的学生,行号>
        List<EmExamNum> fList = new ArrayList<EmExamNum>();

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
                errorData[2] = "";
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
                errorData[3] = "参考学生中未找到改学生";
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
            //正确学生
            String studentId = stuIds.get(0);

            if (StringUtils.isBlank(arr[3])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考号";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            String stuNumber = arr[3].trim();

            if (!isNumeric(stuNumber, null)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考号";
                errorData[2] = "考号：" + stuNumber;
                errorData[3] = "只能由数字组成";
                errorDataList.add(errorData);
                continue;
            }
            if (stuNumber.length() > 50) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考号";
                errorData[2] = "考号：" + stuNumber;
                errorData[3] = "不能超过50位";
                errorDataList.add(errorData);
                continue;
            }
            if (sameMap.containsKey(stuNumber)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考号";
                errorData[2] = "考号：" + stuNumber;
                errorData[3] = "与行" + sameMap.get(stuNumber) + "重复";
                errorDataList.add(errorData);
                continue;
            } else if (insertStuIdsMap.containsKey(studentId)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "学生姓名";
                errorData[2] = "学生姓名：" + stuName;
                errorData[3] = "该学生重复输入考号";
                errorDataList.add(errorData);
                continue;

            } else {
                insertStuIdsMap.put(studentId, stuNumber);
                insertStuIdsHangMap.put(studentId, sequence);
                sameMap.put(stuNumber, sequence);
            }
        }
        int successCount = 0;
        Set<String> delStu = new HashSet<String>();
        if (insertStuIdsMap.size() > 0) {
            Set<String> oldSet = new HashSet<String>();
            for (String s : stuNumberMap.keySet()) {
                if (!insertStuIdsMap.containsKey(s)) {
                    oldSet.add(stuNumberMap.get(s));
                }
            }

            EmExamNum f = new EmExamNum();
            //验证与页面除外的学生是否重复---如果重复 不保存改考号，还会将这个学生的考号清除
            for (String s : insertStuIdsMap.keySet()) {
                delStu.add(s);
                if (oldSet.contains(insertStuIdsMap.get(s))) {
                    String[] errorData = new String[4];
                    errorData[0] = String.valueOf(insertStuIdsHangMap.get(s));
                    errorData[1] = "考号";
                    errorData[2] = "考号：" + insertStuIdsMap.get(s);
                    errorData[3] = "该考号已被使用";
                    errorDataList.add(errorData);
                    continue;
                } else {
                    f = new EmExamNum();
                    f.setExamId(examId);
                    f.setSchoolId(unitId);
                    f.setStudentId(s);
                    f.setId(UuidUtils.generateUuid());
                    f.setExamNumber(insertStuIdsMap.get(s));
                    fList.add(f);
                    successCount++;
                }
            }


        } else {
            return result(totalSize, 0, totalSize, errorDataList);
        }

        try {
            emExamNumService.addOrDel(fList, examId, delStu);

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

    private boolean isNumeric(String str, Integer num) {
        Pattern pattern = null;
        if (num == null) {
            pattern = Pattern.compile("[0-9]*");
        } else {
            pattern = Pattern.compile("[0-9]{" + num + "}");
        }

        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 模板填写说明
     *
     * @return
     */
    private String getExplanation() {
        StringBuffer message = new StringBuffer();
        message.append("填写注意：\n");
        message.append("1：考号要求：必填，不超过50个数字组成。");
        return message.toString();
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        List<String> titleList = getRowTitleList();//表头
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

        // HSSFCell----单元格样式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行

        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：3倍默认高度
        titleRow.setHeightInPoints(3 * sheet.getDefaultRowHeightInPoints());

        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        //注意事项
        titleCell.setCellValue(new HSSFRichTextString(getExplanation()));
        titleCell.setCellStyle(style);

        HSSFRow rowTitle = sheet.createRow(1);
        for (int j = 0; j < size; j++) {
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.outputData(workbook, "学生考号设置", response);

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

