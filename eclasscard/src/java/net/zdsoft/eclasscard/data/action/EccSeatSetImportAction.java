package net.zdsoft.eclasscard.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.eclasscard.data.entity.EccSeatItem;
import net.zdsoft.eclasscard.data.entity.EccSeatSet;
import net.zdsoft.eclasscard.data.service.EccSeatItemService;
import net.zdsoft.eclasscard.data.service.EccSeatSetService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author niuchao
 * @date 2019/11/21 15:28
 */
@Controller
@RequestMapping("/eclasscard/seatSetImport")
public class EccSeatSetImportAction extends DataImportAction {
    private Logger logger = Logger.getLogger(EccSeatSetImportAction.class);

    @Autowired
    private EccSeatSetService eccSeatSetService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EccSeatItemService eccSeatItemService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;

    @RequestMapping("/head/page")
    public String importHead(ModelMap map,String gradeId, String classType,String subjectId, String classId) {
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);

        map.put("gradeList", gradeList);
        map.put("gradeId", gradeId);
        map.put("classType", classType);
        map.put("subjectId", subjectId);
        map.put("classId", classId);
        return "/eclasscard/classSeating/seatSetImportHead.ftl";
    }

    /**
     * 返回年级下的行政班数据
     * @param gradeId
     * @return
     */
    @RequestMapping("/getClassList")
    @ResponseBody
    public List<Clazz> getClassListByGradeId(String gradeId) {
        String unitId = getLoginInfo().getUnitId();
        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
        return classList;
    }

    /**
     * 返回年级id下的行政班
     * @param gradeId
     * @return
     */
    @RequestMapping("/getCourseList")
    @ResponseBody
    public List<Course> getCourseListByGradeId(ModelMap map,String gradeId) {
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_NEXT), Semester.class);
        //教学班
        List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId,semester.getAcadyear(),
                String.valueOf(semester.getSemester()),null,new String[]{gradeId},true),TeachClass.class);
        Set<String> subIdSet = EntityUtils.getSet(teachClassList, x -> x.getCourseId());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIdSet.toArray(new String[0])), Course.class);
        if(CollectionUtils.isNotEmpty(courseList)){
            courseList.stream().sorted((x, y) -> {
                if (x.getOrderId() == null) {
                    return -1;
                }
                if (y.getOrderId() == null) {
                    return 1;
                }
                return x.getOrderId() - y.getOrderId();
            }).collect(Collectors.toList());
        }
        return courseList;
    }

    @RequestMapping("/getTeachClassList")
    @ResponseBody
    public List<TeachClass> getTeacherClass(ModelMap map, String gradeId, String subjectId) {
        String unitId = getLoginInfo().getUnitId();
        Semester currentSemester = SUtils.dc(semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_NEXT), Semester.class);
        List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId, currentSemester.getAcadyear(),
                String.valueOf(currentSemester.getSemester()), subjectId, new String[]{gradeId}, true), TeachClass.class);
        return teachClassList;
    }

    @RequestMapping("/checkSeatSet")
    @ResponseBody
    public String checkSeatSet(String classId){
        String unitId = getLoginInfo().getUnitId();
        EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(unitId, classId);
        if(seatSet==null){
            return error("请先设置座位表");
        }else{
            return returnSuccess();
        }
    }



    @RequestMapping("/index/page")
    public String importIndex(ModelMap map, String classId, String isCover){
        LoginInfo loginInfo = getLoginInfo();
        String unitId=loginInfo.getUnitId();
        // 业务名称
        map.put("businessName", "座位表");
        // 导入URL
        map.put("businessUrl", "/eclasscard/seatSetImport/import");
        // 导入模板
        map.put("templateDownloadUrl", "/eclasscard/seatSetImport/template?unitId="+unitId+"&classId="+classId);
        //错误数据下载
        map.put("exportErrorExcelUrl", "/eclasscard/seatSetImport/exportErrorExcel");
        // 导入对象
        map.put("objectName", "");
        map.put("businessKey", UuidUtils.generateUuid());
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo",0);
        //模板校验
        map.put("validateUrl", "/eclasscard/seatSetImport/validate?unitId="+unitId+"&classId="+classId);
        // 导入说明
        StringBuffer description=new StringBuffer();
        description.append(getDescription());

        //导入参数
        JSONObject obj=new JSONObject();
        obj.put("unitId", unitId);
        obj.put("classId", classId);
        obj.put("isCover", isCover);
        map.put("monthPermance",JSON.toJSONString(obj));
        map.put("description", description);
        return "/eclasscard/classSeating/seatSetImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "eccClassSeatSetImportAction";
    }

    @Override
    public String getDescription() {
        String desc = "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
                + "<p>2、改变选项后请重新上传模板，同时不要随意修改模板中内容，否则容易导致上传文件与模板的不匹配</p>"
                + "<p>3、“是否覆盖”若选择是，则会先<font style='color:red;'>清空</font>整行数据再导入，否则只更新座位发生改变的学生</p>"
                + "<p>4、请仔细查看模板中的提示，请严格根据提示填写数据</p>";
        return desc;
    }

    @Override
    public List<String> getRowTitleList() {
        return new ArrayList<>();
    }

    private List<String> getRowTitleList2(Integer colNum) {
        List<String> titleList = new ArrayList<>();
        titleList.add("行\\列");
        for (int i=1; i<=colNum; i++){
            titleList.add(i+"");
        }
        return titleList;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        //获取参数
        JSONObject performance = JSON.parseObject(params,JSONObject.class);
        String unitId = (String) performance.get("unitId");
        String classId = (String) performance.get("classId");
        String isCover = (String) performance.get("isCover");
        EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(unitId, classId);

        List<Student> studentList = new ArrayList<>();
        Set<String> sameNames = new HashSet<>();
        if(Clazz.CLASS_TYPE_XZB.equals(seatSet.getClassType())){
            studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
        }else{
            studentList = SUtils.dt(studentRemoteService.findByTeachClassIds(new String[]{classId}), Student.class);
        }
        Map<String, Student> studentNameMap = new HashMap<>();
        Map<String, Student> studentNameMap2 = new HashMap<>();
        for (Student student : studentList) {
            if(studentNameMap.containsKey(student.getStudentName())){
                sameNames.add(student.getStudentName());
                continue;
            }else{
                studentNameMap.put(student.getStudentName(), student);
            }
            studentNameMap2.put(student.getStudentName()+"("+student.getStudentCode()+")", student);
        }

        List<String> titleList = getRowTitleList2(seatSet.getColNumber());
        //获取上传数据 第一行行标是0
        List<String[]> rowDatas = ExcelUtils.readExcelIgnoreDesc(filePath, titleList.size());
        rowDatas.remove(0);
        //错误数据序列号
        int sequence = 0;
        int totalSize =rowDatas.size();
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(rowDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize,0,0,errorDataList,"");
        }

        int successCount=0;
        int i=0;
        String[] errorData;
        Student student = null;
        EccSeatItem seatItem = null;
        List<EccSeatItem> seatItemList = new ArrayList<>();
        Set<Integer> sameRows = new HashSet<Integer>();
        Set<String> sameStuIds = new HashSet<>();
        for (String[] arr : rowDatas) {
            //判断班级
            i++;
            String rowNumStr = StringUtils.trim(arr[0]);
            if(StringUtils.isBlank(rowNumStr)){
                errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=rowNumStr;
                errorData[3]="行号不能为空";
                errorDataList.add(errorData);
                continue;
            }

            int rowNum = Integer.parseInt(rowNumStr);
            if(rowNum>seatSet.getRowNumber()){
                errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=rowNumStr;
                errorData[3]="行号不能大于班级设置的总行数"+seatSet.getRowNumber();
                errorDataList.add(errorData);
                continue;
            }

            boolean flag = false;
            if(sameRows.contains(rowNum)){
                errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=rowNumStr;
                errorData[3]="在之前的数据中已经存在第"+rowNumStr+"行";
                errorDataList.add(errorData);
                continue;
            }

            for (int j = 1; j < arr.length; j++) {
                String studentName = StringUtils.trim(arr[j]);
                if(StringUtils.isBlank(studentName)){
                    continue;
                }
                studentName = studentName.replaceAll("（","(").replaceAll("）",")");
                arr[j] = studentName;
                if(sameNames.contains(studentName)){
                    errorData=new String[4];
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=studentName;
                    errorData[3]="在该班级存在多个姓名为"+studentName+"的学生";
                    errorDataList.add(errorData);
                    flag=true;
                    break;
                }
                if(!studentNameMap.containsKey(studentName)){
                    if(studentName.contains("(")){
                        if(!studentNameMap2.containsKey(studentName)) {
                            errorData = new String[4];
                            errorData[0] = i + "";
                            errorData[1] = "第" + i + "行";
                            errorData[2] = studentName;
                            errorData[3] = "格式不正确或姓名与学号不匹配";
                            errorDataList.add(errorData);
                            flag = true;
                            break;
                        }
                    }else{
                        errorData=new String[4];
                        errorData[0]=i+"";
                        errorData[1]="第"+i+"行";
                        errorData[2]=studentName;
                        errorData[3]="在该班级中不存在学生"+studentName;
                        errorDataList.add(errorData);
                        flag=true;
                        break;
                    }
                }
            }
            if(flag){
                continue;
            }
            Set<String> tempSet = new HashSet<>();
            for (int j = 1; j < arr.length; j++) {
                String studentName = arr[j];
                if (StringUtils.isBlank(studentName)) {
                    continue;
                }
                student = studentNameMap.get(studentName);
                if(student==null){
                    student = studentNameMap2.get(studentName);
                }
                if (sameStuIds.contains(student.getId())||tempSet.contains(student.getId())) {
                    errorData = new String[4];
                    errorData[0] = i + "";
                    errorData[1] = "第" + i + "行";
                    errorData[2] = studentName;
                    errorData[3] = "当前导入文件中已存在学生"+studentName;
                    errorDataList.add(errorData);
                    flag = true;
                    break;
                }
                tempSet.add(student.getId());
            }
            if(flag){
                continue;
            }
            for (int j = 1; j < arr.length; j++) {
                String studentName = arr[j];
                if(StringUtils.isBlank(studentName)){
                    continue;
                }
                student = studentNameMap.get(studentName);
                if(student==null){
                    student = studentNameMap2.get(studentName);
                }
                seatItem = new EccSeatItem();
                seatItem.setId(UuidUtils.generateUuid());
                seatItem.setSeatId(seatSet.getId());
                seatItem.setUnitId(unitId);
                seatItem.setStudentId(student.getId());
                seatItem.setStudentName(student.getStudentName());
                seatItem.setClassId(student.getClassId());
                seatItem.setRowNum(rowNum);
                seatItem.setColNum(Integer.parseInt(titleList.get(j)));
                seatItem.setCreationTime(new Date());
                seatItemList.add(seatItem);
                sameStuIds.add(student.getId());
            }
            sameRows.add(rowNum);
            successCount++;
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题行固定
            sheet.createFreezePane(0, 1);

            List<String> errorTitleList = getRowTitleList2(seatSet.getColNumber());
            errorTitleList.add("错误数据");
            errorTitleList.add("错误原因");

            // 单元格样式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);//水平居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            style.setWrapText(true);//自动换行
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);

            //错误样式
            HSSFCellStyle errorStyle = workbook.createCellStyle();
            errorStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            errorStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            errorStyle.setBorderBottom(BorderStyle.THIN);
            errorStyle.setBorderLeft(BorderStyle.THIN);
            errorStyle.setBorderRight(BorderStyle.THIN);
            errorStyle.setBorderTop(BorderStyle.THIN);
            HSSFFont errorFont = workbook.createFont();
            errorFont.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(errorFont);

            int size = errorTitleList.size();
            HSSFRow titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
            //画线(由左上到右下的斜线)  在A1的第一个cell（行   列）加入一条对角线
            //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            //HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short)0, 0, (short)0, 0);
            //HSSFSimpleShape shape = patriarch.createSimpleShape(anchor);
            //shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            //shape.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID) ;
            for(int j=0;j<size;j++){
                if(j==size-1) {
                    sheet.setColumnWidth(j, 30 * 256);//列宽
                }else if(j==size-2){
                    sheet.setColumnWidth(j, 20 * 256);//列宽
                }else{
                    sheet.setColumnWidth(j, 12 * 256);//列宽
                }
                HSSFCell cell = titleRow.createCell(j);
                cell.setCellStyle(style);
                if(j==0 || j>=size-2){
                    cell.setCellValue(errorTitleList.get(j));
                }else{
                    cell.setCellValue(Integer.parseInt(errorTitleList.get(j)));
                }
            }

            for(int j=0;j<errorDataList.size();j++){
                HSSFRow row = sheet.createRow(j+1);
                row.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
                String[] datasDetail = rowDatas.get(Integer.parseInt(errorDataList.get(j)[0])-1);
                for(int k=0;k<size;k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellStyle(style);
                    if(k==0){
                        cell.setCellValue(Integer.parseInt(datasDetail[k]));
                    }else if (k<size-2) {
                        cell.setCellValue(datasDetail[k]);
                    } else if (k==size-2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[2]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }

        try {
            if(CollectionUtils.isNotEmpty(seatItemList)){
                if(BaseConstants.ONE_STR.equals(isCover)){
                    eccSeatItemService.saveAndDelete(unitId, seatSet.getId(), sameRows.toArray(new Integer[sameRows.size()]),
                            sameStuIds.toArray(new String[sameStuIds.size()]), seatItemList.toArray(new EccSeatItem[0]));
                }else{
                    List<EccSeatItem> itemList = eccSeatItemService.findListBySeatId(unitId, seatSet.getId());
                    List<String> saveCoo = EntityUtils.getList(seatItemList, e -> e.getColNum() +"-"+ e.getRowNum());
                    List<String> stuIds = itemList.stream().filter(e -> saveCoo.contains(e.getColNum() + "-" + e.getRowNum())).map(e -> e.getStudentId()).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(stuIds)){
                        sameStuIds.addAll(stuIds);
                    }
                    eccSeatItemService.saveAndDelete(unitId, seatSet.getId(), null,
                            sameStuIds.toArray(new String[sameStuIds.size()]), seatItemList.toArray(new EccSeatItem[0]));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
        }
        int errorCount = totalSize - successCount;
        return result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
    }

    private String result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        String classId = request.getParameter("classId");
        EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(unitId, classId);
        List<String> titleList =  getRowTitleList2(seatSet.getColNumber());
        int size = titleList.size();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        //标题行固定
        sheet.createFreezePane(0, 2);
        // 单元格样式
        HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        //注意事项样式
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setFont(headfont);
        headStyle.setAlignment(HorizontalAlignment.LEFT);//水平居左
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        headStyle.setWrapText(true);//自动换行

        //注意事项内容
        HSSFRow remarkRow = sheet.createRow(0);
        remarkRow.setHeightInPoints(6*sheet.getDefaultRowHeightInPoints());
        HSSFCell titleCell = remarkRow.createCell(0);
        titleCell.setCellStyle(headStyle);
        String remark = getRemark();
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(headStyle);
        CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
        sheet.addMergedRegion(car);

        HSSFRow titleRow = sheet.createRow(1);
        titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
        //画线(由左上到右下的斜线)  在A1的第一个cell（行   列）加入一条对角线
        //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        //HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short)0, 1, (short)0, 1);
        //HSSFSimpleShape shape = patriarch.createSimpleShape(anchor);
        //shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        //shape.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID) ;
        for(int i=0;i<size;i++){
            sheet.setColumnWidth(i, 12 * 256);//列宽
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellStyle(style);
            if(i==0){
                cell.setCellValue(titleList.get(i));
            }else{
                cell.setCellValue(Integer.parseInt(titleList.get(i)));
            }
        }
        HSSFRow contentRow = null;
        HSSFCell contentCell = null;
        for(int i=2;i<=seatSet.getRowNumber()+1;i++){
            contentRow = sheet.createRow(seatSet.getRowNumber()+3-i);
            contentRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
            for(int j=0;j<size;j++){
                contentCell = contentRow.createCell(j);
                contentCell.setCellStyle(style);
                if(j==0){
                  contentCell.setCellValue(i-1);
                }
            }
        }

        String className = "";
        if(Clazz.CLASS_TYPE_XZB.equals(seatSet.getClassType())){
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            className = clazz.getClassNameDynamic();
        }else{
            TeachClass teachClass = SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
            className = teachClass.getName();
        }
        String fileName = className+"座位表导入";

        ExportUtils.outputData(workbook, fileName, response);


    }

    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo,String unitId, String classId){
        EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(unitId, classId);
        logger.info("模板校验中......");
        try{
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList2(seatSet.getColNumber()).size());
            return templateValidate(datas, getRowTitleList2(seatSet.getColNumber()));
        }catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

    @RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark(){
        String remark = "填写注意：\n"
                + "1.默认教室靠近讲台的为第1行\n"
                + "2.学生填写格式有2种：1姓名，2姓名(学号)。如：张三或张三(20190303)均可\n"
                + "3.请勿随意修改模板中的列标题值\n";
        return remark;
    }
}
