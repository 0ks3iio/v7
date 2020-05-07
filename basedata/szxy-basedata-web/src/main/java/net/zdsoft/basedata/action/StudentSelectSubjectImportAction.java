package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.StudentSelectSubjectService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/basedata/stuselect")
public class StudentSelectSubjectImportAction extends DataImportAction {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClassService classService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentSelectSubjectService studentSelectSubjectService;
    @Autowired
    private StudentService studentService;

    @RequestMapping("/import/main")
    public String execute(String acadyear, String semester, ModelMap map) {
        // 业务名称
        map.put("businessName", "选课结果");
        // 导入URL
        map.put("businessUrl", "/basedata/stuselect/import");

        map.put("templateDownloadUrl", "/basedata/stuselect/template?acadyear=" + acadyear + "&semester=" + semester);
        map.put("exportErrorExcelUrl", "/basedata/stuselect/exportErrorExcel");
        // 导入对象
        map.put("objectName", "studentSelectAction");
        // 导入说明
        map.put("description", "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确</p>");
        map.put("businessKey", UuidUtils.generateUuid());
        map.put("selectName", acadyear + "学年第" + semester + "学期");
        //模板校验
        map.put("validateUrl", "/basedata/stuselect/validate");
        return "basedata/student/studentSelectImport.ftl";
    }

    @RequestMapping("/template")
    @Override
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        //标题行固定
        sheet.createFreezePane(0, 2);

        List<String> titleList = getRowTitleList();

        CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
        sheet.addMergedRegion(car);

        //注意事项样式
        HSSFCellStyle headStyle = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        headStyle.setFont(headfont);
        headStyle.setAlignment(HorizontalAlignment.LEFT);//水平居左
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        headStyle.setWrapText(true);//自动换行

        //第一行
        HSSFRow remarkRow = sheet.createRow(0);
        //高度：5倍默认高度
        remarkRow.setHeightInPoints(5*sheet.getDefaultRowHeightInPoints());

        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(headStyle);
        //注意事项
        String remark = "填写注意：\n"
                + "1.科目组合优先级大于单科填写优先级;\n"
                + "2.科目组合填写时以逗号隔开，如“物理,化学,生物”;\n"
                + "3.如未填写科目组合，则请在要选择的科目下分别填写科目名称.";
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(headStyle);

        HSSFRow rowTitle = sheet.createRow(1);
        for(int i=0;i<titleList.size();i++){
        	if(i==titleList.size()-1){
        		sheet.setColumnWidth(i, 15 * 256);//列宽
        	}else{
        		sheet.setColumnWidth(i, 10 * 256);//列宽
        	}
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }

        String fileName = acadyear + "学年第" + semester + "学期选课结果导入";

        ExportUtils.outputData(workbook, fileName, response);
    }

    @RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    @RequestMapping("/import")
    @ResponseBody
    @Override
    public String dataImport(String filePath, String params) {
        JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
        String acadyear = jsStr.getString("acadyear");
        String semester = jsStr.getString("semester");
        String gradeId = jsStr.getString("gradeId");
        String unitId = getLoginInfo().getUnitId();
        List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList().size());
        String[] titleArr = datas.get(0);
        datas.remove(0);
        List<String[]> errorDataList=new ArrayList<String[]>();
        Json importResultJson=new Json();
        if(CollectionUtils.isEmpty(datas)){
            String[] errorData=new String[4];
            errorData[0]=String.valueOf(1);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            importResultJson.put("totalCount", datas.size());
            importResultJson.put("successCount", 0);
            importResultJson.put("errorCount", datas.size());
            importResultJson.put("errorData", errorDataList);
            importResultJson.put("errorExcelPath", "");
            return importResultJson.toJSONString();

        }
        String gradeName = gradeService.findOne(gradeId).getGradeName();

        int successCount  =0;
        String[] errorData=null;
        Set<String> stuCodeSet = new HashSet<String>();
        List<StudentSelectSubject> insertList=new ArrayList<>();
        for(String[] arr : datas){
            if(StringUtils.isNotBlank(arr[1])){
                stuCodeSet.add(arr[1]);
            }
        }
        //学号重复---暂时没有考虑
        List<Student> stuList = new ArrayList<Student>();
        Set<String> clsIdSet = new HashSet<String>();
        if(CollectionUtils.isNotEmpty(stuCodeSet)){
            stuList = studentService.findBySchIdStudentCodes(getLoginInfo().getUnitId(), stuCodeSet.toArray(new String[0]));
            clsIdSet = EntityUtils.getSet(stuList, Student::getClassId);
        }
        List<Clazz> clsList = new ArrayList<Clazz>();
        Map<String, String> clsMap = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(clsIdSet)){
            clsList = classService.findListByIds(clsIdSet.toArray(new String[0]));
            clsMap = EntityUtils.getMap(clsList, Clazz::getId, Clazz::getGradeId);
        }
        Map<String, String> stuClsMap = new HashMap<String, String>();
        for(Student stu : stuList){
            stuClsMap.put(stu.getId(), clsMap.get(stu.getClassId()));
        }
        Map<String, String> stuCodeNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeIdMap = new HashMap<String, String>();
        for(Student stu : stuList){
            stuCodeNameMap.put(stu.getStudentCode(), stu.getStudentName());
            stuCodeIdMap.put(stu.getStudentCode(), stu.getId());
        }

        List<String> courseIdList = EntityUtils.getList(SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>() {}), Course::getId);
        Map<String, String> courNameMap = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(courseIdList)){
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIdList.toArray(new String[0])), new TR<List<Course>>(){});
            for(Course course : courseList){
                courNameMap.put(course.getSubjectName(), course.getId());
            }
        }
        //获取表里已有数据
        //成功数据的学生
        Set<String> chooseStudentId=new HashSet<String>();
        int t=0;
        String errorMess;
        String chooseGroup;
        for(String[] arr : datas){
            t++;
            //姓名
            if(StringUtils.isBlank(arr[0])){
                errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
                errorData[1]="第"+String.valueOf(t)+"行";
                errorData[2]="姓名";
                errorData[3]="学生姓名不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if(StringUtils.isBlank(arr[1])){
                errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
                errorData[1]="第"+String.valueOf(t)+"行";
                errorData[2]="学号";
                errorData[3]="学号不能为空";
                errorDataList.add(errorData);
                continue;
            }else{
                if(StringUtils.isBlank(stuCodeNameMap.get(arr[1]))){
                    errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
                    errorData[1]="第"+String.valueOf(t)+"行";
                    errorData[2]="学号";
                    errorData[3]="不存在该学号所属的学生";
                    errorDataList.add(errorData);
                    continue;
                }else{
                    if(StringUtils.isNotBlank(arr[0]) && !arr[0].equals(stuCodeNameMap.get(arr[1]))){
                        errorData = new String[4];
                        // errorData[0]=errorDataList.size()+1+"";
                        errorData[0]=t+"";
                        errorData[1]="第"+String.valueOf(t)+"行";
                        errorData[2]="姓名："+arr[0]+"学号："+arr[1];
                        errorData[3]="学生姓名与该学号不匹配";
                        errorDataList.add(errorData);
                        continue;
                    }
                    if(!gradeId.equals(stuClsMap.get(stuCodeIdMap.get(arr[1])))){
                        errorData = new String[4];
                        // errorData[0]=errorDataList.size()+1+"";
                        errorData[0]=t+"";
                        errorData[1]="第"+String.valueOf(t)+"行";
                        errorData[2]="年级";
                        errorData[3]="该学生不属于"+gradeName+"年级";
                        errorDataList.add(errorData);
                        continue;
                    }
                }
            }
            String studentId=stuCodeIdMap.get(arr[1]);
            if(chooseStudentId.contains(studentId)){
                errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
                errorData[1]="第"+String.valueOf(t)+"行";
                errorData[2]=arr[0];
                errorData[3]="学生重复！";
                errorDataList.add(errorData);
                continue;
            }

            chooseGroup = arr[arr.length-1];
            Set<String> chooseGroupSet = new HashSet<String>();
            Set<String> chooseSubjectIds=new HashSet<String>();
            errorMess="";
            if(StringUtils.isNotBlank(chooseGroup)){
                String[] chooseGroupArr = chooseGroup.replaceAll("，", ",").split(",");
                if(chooseGroupArr.length!=3){
                    errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
                    errorData[1]="第"+String.valueOf(t)+"行";
                    errorData[2]="科目组合";
                    errorData[3]="填写格式不正确";
                    errorDataList.add(errorData);
                    continue;
                }
                chooseGroupSet.addAll(Arrays.asList(chooseGroupArr));
                if(chooseGroupSet.size()!=3){
                    errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
                    errorData[1]="第"+String.valueOf(t)+"行";
                    errorData[2]=arr[0];
                    errorData[3]="科目组合中有重复课程，请核对";
                    errorDataList.add(errorData);
                    continue;
                }
                for (String item : chooseGroupSet) {
                    if(null == courNameMap.get(item)){
                        errorMess=errorMess+";"+"本次选课中不存在课程"+item;
                        continue;
                    }
                    chooseSubjectIds.add(courNameMap.get(item));
                }
                if(StringUtils.isNotBlank(errorMess)) {
                    errorMess=errorMess.substring(1);
                    errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
                    errorData[1]="第"+String.valueOf(t)+"行";
                    errorData[2]="科目组合";
                    errorData[3]=errorMess;
                    errorDataList.add(errorData);
                    continue;
                }
            }else{
                for(int i=2;i<arr.length-1;i++){
                    if(StringUtils.isNotBlank(arr[i])){
                        if(!arr[i].equals(titleArr[i])){
                            errorMess=errorMess+";填写科目"+arr[i]+"与表头不一致";
                        }else{
                            chooseGroupSet.add(arr[i]);
                        }
                    }
                }
                if(StringUtils.isNotBlank(errorMess)) {
                    errorMess=errorMess.substring(1);
                    errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
                    errorData[1]="第"+String.valueOf(t)+"行";
                    errorData[2]="科目";
                    errorData[3]=errorMess;
                    errorDataList.add(errorData);
                    continue;
                }
                if(chooseGroupSet.size()!=3){
                    errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
                    errorData[1]="第"+String.valueOf(t)+"行";
                    errorData[2]="科目";
                    errorData[3]="选课门数应等于3";
                    errorDataList.add(errorData);
                    continue;
                }
                for (String item : chooseGroupSet) {
                    chooseSubjectIds.add(courNameMap.get(item));
                }
            }
            chooseStudentId.add(stuCodeIdMap.get(arr[1]));

            for(String s:chooseSubjectIds){
                StudentSelectSubject selResult = new StudentSelectSubject();
                selResult.setSchoolId(getLoginInfo().getUnitId());
                selResult.setGradeId(gradeId);
                selResult.setStudentId(studentId);
                selResult.setCreationTime(new Date());
                selResult.setModifyTime(new Date());
                selResult.setAcadyear(acadyear);
                selResult.setSemester(Integer.valueOf(semester));
                selResult.setId(UuidUtils.generateUuid());
                selResult.setSubjectId(s);
                selResult.setIsDeleted(0);
                insertList.add(selResult);
            }
            successCount++;

        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题行固定
            sheet.createFreezePane(0, 1);
            List<String> titleList = getRowTitleList();
            titleList.add("错误数据");
            titleList.add("错误原因");

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int i=0;i<titleList.size();i++){
            	if(i>=titleList.size()-3){
            		sheet.setColumnWidth(i, 15 * 256);//列宽
            	}else{
            		sheet.setColumnWidth(i, 10 * 256);//列宽
            	}
                HSSFCell cell = rowTitle.createCell(i);
                cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
            }

            for(int i=0;i<errorDataList.size();i++){
                HSSFRow row = sheet.createRow(i+1);
                String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(i)[0]) - 1);
                for (int j=0;j<titleList.size();j++) {
                    HSSFCell cell = row.createCell(j);
                    if (j<titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[j]));
                    } else if (j==titleList.size()-2) {
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

        if(CollectionUtils.isNotEmpty(insertList)){
            try{
                studentSelectSubjectService.updateStudentSelects(unitId, acadyear, Integer.valueOf(semester), chooseStudentId.toArray(new String[0]), insertList);
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

    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo) {
        if (StringUtils.isBlank(validRowStartNo)) {
            validRowStartNo = "1";
        }
        try{
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList().size());
            return templateValidate(datas, getRowTitleList());

        }catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

    @Override
    public String templateValidate(List<String[]> allDataList,
                                    List<String> titleList) {
        String errorDataMsg = "";
        if (CollectionUtils.isNotEmpty(allDataList)) {
            String[] realTitles = allDataList.get(0);
            if (realTitles != null) {
                if (realTitles.length != titleList.size()) {
                    return errorDataMsg = "导入数据列数(" + realTitles.length + ")与模板列数不符("
                            + titleList.size() + ")";
                }
                for (int i = 0; i < realTitles.length; i++) {
                    if (!titleList.contains(realTitles[i])) {
                        errorDataMsg = "模板中不存在列名：" + realTitles[i];
                        break;
                    }
                }
            }
        } else {
            errorDataMsg = "模板中不存在数据";
        }
        return errorDataMsg;
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("姓名");
        tis.add("学号");
        String unitId = getLoginInfo().getUnitId();
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
        tis.addAll(EntityUtils.getList(courseList, Course::getSubjectName));
        tis.add("科目组合");
        return tis;
    }

    @Override
    public String getObjectName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
