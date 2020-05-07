package net.zdsoft.stutotality.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.entity.StutotalityHealthOption;
import net.zdsoft.stutotality.data.entity.StutotalityItem;
import net.zdsoft.stutotality.data.service.StutotalityHealthOptionService;
import net.zdsoft.stutotality.data.service.StutotalityItemOptionService;
import net.zdsoft.stutotality.data.service.StutotalityItemService;
import net.zdsoft.stutotality.data.service.StutotalityStuResultService;
import net.zdsoft.stutotality.data.util.StutotalityConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.zdsoft.stutotality.data.util.StutotalityResultImportUitls.result;

@Controller
@RequestMapping("/stutotality/result")
public class StutotalityResultImportAction extends DataImportAction {
    @Autowired
    StutotalityHealthOptionService stutotalityHealthOptionService;
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    StutotalityStuResultService stutotalityStuResultService;
    @Autowired
    StutotalityItemOptionService stutotalityItemOptionService;
    @Autowired
    StutotalityItemService stutotalityItemService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    StudentRemoteService studentRemoteService;
    private String importType1,classId1;
    @RequestMapping("/main")
    public String execute(ModelMap map,String importType,String classId) {
        importType1 = importType;
        classId1 = classId;
        if(importType.equals("0")) {
            // 业务名称
            map.put("businessName", "身体素质导入");
            // 导入URL
            map.put("businessUrl", "/stutotality/result/import?importType="+importType+"&classId="+classId);
            // 导入模板
            map.put("templateDownloadUrl", "/stutotality/result/template?importType="+importType+"&classId="+classId);
        }else {
            // 业务名称
            map.put("businessName", "学生期末成绩导入");
            // 导入URL
            map.put("businessUrl", "/stutotality/result/import?importType="+importType+"&classId="+classId);
            // 导入模板
            map.put("templateDownloadUrl", "/stutotality/result/template?importType="+importType+"&classId="+classId);
        }
        // 导入对象
        map.put("objectName", getObjectName());
        // 导入说明
        map.put("description", getDescription());


        map.put("businessKey", UuidUtils.generateUuid());

        String className="";
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        if(StringUtils.isNotBlank(classId)) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
            className = clazz.getClassNameDynamic();
        }
        map.put("className",className);
        map.put("year", semester.getAcadyear());
        map.put("semester", semester.getSemester());
        map.put("importType",importType);
        return "/stutotality/result/stutotalityScoHeaImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "stutotalityResultObject";
    }

    @Override
    public String getDescription() {
         return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确；</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        if(importType1.equals("0")) {
            List<StutotalityHealthOption> stutotalityHealthOptions = new ArrayList<>();
            if(StringUtils.isNotBlank(classId1)) {
                Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId1),Clazz.class);
                if(StringUtils.isNotBlank(clazz.getGradeId())) {
                    Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
                    stutotalityHealthOptions = stutotalityHealthOptionService.findByUnitIdAndGradeCode(getLoginInfo().getUnitId(), grade.getGradeCode());
                }
            }
            tis.add("学号");
            tis.add("学生姓名");
//        tis.add("成绩");
            if(CollectionUtils.isNotEmpty(stutotalityHealthOptions)){
                for(StutotalityHealthOption stutotalityHealthOption:stutotalityHealthOptions){
                    if(StringUtils.isNotBlank(stutotalityHealthOption.getHealthName())){
                        if(stutotalityHealthOption.getHealthName().contains("视力")){
                            tis.add("左视力");
                            tis.add("右视力");
                        }else {
                            tis.add(stutotalityHealthOption.getHealthName());
                        }
                    }
                }
            }
        }else {
            tis.add("学号");
            tis.add("学生姓名");
            if(StringUtils.isNotBlank(classId1)){
                Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId1),Clazz.class);
                Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
                List<StutotalityItem> itemList = new ArrayList<>();
                if(StringUtils.isNotBlank(clazz.getGradeId())) {
                    if (isAdmin(unitId, userId)) {
                        itemList = stutotalityItemService.getItemListByParams(unitId, semester.getAcadyear(), semester.getSemester().toString(), clazz.getGradeId(), 1);
                    } else {
                        List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByGradeId(unitId, clazz.getGradeId(), getLoginInfo().getOwnerId()), new TR<List<Clazz>>() {
                        });
                        if (CollectionUtils.isNotEmpty(clazzsList)) {
                            itemList = stutotalityItemService.getItemListByParams(unitId, semester.getAcadyear(), semester.getSemester().toString(), clazz.getGradeId(), 1);
                        } else {
                            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId, semester.getAcadyear(), semester.getSemester() + "", getLoginInfo().getOwnerId()), new TR<List<ClassTeaching>>() {
                            });
                            if (CollectionUtils.isNotEmpty(classTeachingList)) {
                                classTeachingList = classTeachingList.stream().filter(classTeaching -> classTeaching.getClassId().equals(classId1)).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(classTeachingList)) {
                                    Set<String> subjectIds = classTeachingList.stream().map(ClassTeaching::getSubjectId).collect(Collectors.toSet());
                                    if (CollectionUtils.isNotEmpty(subjectIds)) {
                                        itemList = stutotalityItemService.findByParams(unitId, semester.getAcadyear(), semester.getSemester() + "", clazz.getGradeId(), subjectIds.toArray(new String[0]));
                                        itemList = itemList.stream().filter(stutotalityItem -> stutotalityItem.getSubjectType().equals("2")).collect(Collectors.toList());
                                    }
                                }
                            }
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(itemList)){
                    for(StutotalityItem item:itemList){
                        if(StringUtils.isNotBlank(item.getItemName())){
                            tis.add(item.getItemName());
                        }
                    }
                }
//                Set<String> itemNames = EntityUtils.getSet(itemList, StutotalityItem::getItemName);
//                for (String s : itemNames) {
//                    tis.add(s);
//                }

            }

        }
        return tis;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        String importType = getRequest().getParameter("importType");
        String classId = getRequest().getParameter("classId");
        log.info("业务数据处理中。。。");
        List<String[]> errorDataList = new ArrayList<>();

        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        List<String[]> arrDatas = ExcelUtils.readExcelByRow(filePath, 1, getRowTitleList().size());
        String[] arrDatas1 = arrDatas.get(0);
        arrDatas.remove(0);
        String result;

        if(importType.equals("0")) {
            if(StringUtils.isNotBlank(classId)) {
                Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
                List<StutotalityHealthOption> stutotalityHealthOptions = new ArrayList<>();
                if(StringUtils.isNotBlank(clazz.getGradeId())) {
                    Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
                    stutotalityHealthOptions = stutotalityHealthOptionService.findByUnitIdAndGradeCode(unitId, grade.getGradeCode());
                }
                List<String> healthNames = new ArrayList<>();
                healthNames.add("学号");
                healthNames.add("学生姓名");
                if(CollectionUtils.isNotEmpty(stutotalityHealthOptions)){
                    for(StutotalityHealthOption stutotalityHealthOption:stutotalityHealthOptions){
                        if(StringUtils.isNotBlank(stutotalityHealthOption.getHealthName())){
                            if(stutotalityHealthOption.getHealthName().contains("视力")){
                                healthNames.add("左视力");
                                healthNames.add("右视力");
                            }else {
                                healthNames.add(stutotalityHealthOption.getHealthName());
                            }
                        }
                    }
                }
                String s0 = ArrayUtils.toString(healthNames.toArray(new String[0]), ",");
                String s1 = ArrayUtils.toString(arrDatas1, ",");
                if(!s0.equals(s1)){
                    String [] errorData = new String[4];
                    errorData[0] = "1";
                    errorData[1] = "";
                    errorData[2] = "";
                    errorData[3] = "导入模板已更新，请重新下载模板";
                    errorDataList.add(errorData);
                    return result(arrDatas.size(), 0, errorDataList.size(), errorDataList);
                }
            }
            result = stutotalityStuResultService.saveImportData(classId,arrDatas, getLoginInfo());
        }else {
            if(StringUtils.isNotBlank(classId)) {
                Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
                Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
                List<StutotalityItem> itemList = new ArrayList<>();
                if(StringUtils.isNotBlank(clazz.getGradeId())) {
                    if (isAdmin(unitId, userId)) {
                        itemList = stutotalityItemService.getItemListByParams(unitId, semester.getAcadyear(), semester.getSemester().toString(), clazz.getGradeId(), 1);
                    } else {
                        List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByGradeId(unitId, clazz.getGradeId(), getLoginInfo().getOwnerId()), new TR<List<Clazz>>() {
                        });
                        if (CollectionUtils.isNotEmpty(clazzsList)) {
                            itemList = stutotalityItemService.getItemListByParams(unitId, semester.getAcadyear(), semester.getSemester().toString(), clazz.getGradeId(), 1);
                        } else {
                            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId, semester.getAcadyear(), semester.getSemester() + "", getLoginInfo().getOwnerId()), new TR<List<ClassTeaching>>() {
                            });
                            if (CollectionUtils.isNotEmpty(classTeachingList)) {
                                classTeachingList = classTeachingList.stream().filter(classTeaching -> classTeaching.getClassId().equals(classId)).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(classTeachingList)) {
                                    Set<String> subjectIds = classTeachingList.stream().map(ClassTeaching::getSubjectId).collect(Collectors.toSet());
                                    if (CollectionUtils.isNotEmpty(subjectIds)) {
                                        itemList = stutotalityItemService.findByParams(unitId, semester.getAcadyear(), semester.getSemester() + "", clazz.getGradeId(), subjectIds.toArray(new String[0]));
                                        itemList = itemList.stream().filter(stutotalityItem -> stutotalityItem.getSubjectType().equals("2")).collect(Collectors.toList());
                                    }
                                }
                            }
                        }
                    }
                }else {
                    String [] errorData = new String[4];
                    errorData[0] = "1";
                    errorData[1] = "";
                    errorData[2] = "";
                    errorData[3] = "该班级无年级";
                    errorDataList.add(errorData);
                    return result(arrDatas.size(), 0, errorDataList.size(), errorDataList);
                }
//                if (StringUtils.isNotBlank(clazz.getGradeId())) {
//                    itemList = stutotalityItemService.getItemListByParams(getLoginInfo().getUnitId(), semester.getAcadyear(), semester.getSemester().toString(), null, 1);
//                } else {
//                    itemList = stutotalityItemService.getItemListByParams(getLoginInfo().getUnitId(), semester.getAcadyear(), semester.getSemester().toString(), clazz.getGradeId(), 1);
//                }

                List<String> itemNames = new ArrayList<>();
                itemNames.add("学号");
                itemNames.add("学生姓名");
                if(CollectionUtils.isNotEmpty(itemList)){
                    for(StutotalityItem item:itemList){
                        if(StringUtils.isNotBlank(item.getItemName())){
                            itemNames.add(item.getItemName());
                        }
                    }
                }
                String s0 = ArrayUtils.toString(itemNames.toArray(new String[0]), ",");
                String s1 = ArrayUtils.toString(arrDatas1, ",");
                if(!s0.equals(s1)){
                    String [] errorData = new String[4];
                    errorData[0] = "1";
                    errorData[1] = "";
                    errorData[2] = "";
                    errorData[3] = "导入模板已更新，请重新下载模板";
                    errorDataList.add(errorData);
                    return result(arrDatas.size(), 0, errorDataList.size(), errorDataList);
                }
            }
            result = stutotalityStuResultService.saveImportData1(classId,arrDatas, getLoginInfo());
        }
        log.info("业务数据导入结束。。。");
        return result;
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String importType = request.getParameter("importType");
        String classId = request.getParameter("classId");
        List<String> titleList = getRowTitleList();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        CellRangeAddress car = new CellRangeAddress(0, 0, 0, titleList.size() - 1);
        sheet.addMergedRegion(car);

        // 注意事项样式
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
        remarkRow.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());

        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(headStyle);
        //注意事项
        if(importType.equals("0")) {
            String remark = getRemark1();
            remarkCell.setCellValue(new HSSFRichTextString(remark));
            remarkCell.setCellStyle(headStyle);
            remarkCell.setCellValue(new HSSFRichTextString(remark));
        }else {
            String remark = getRemark();
            remarkCell.setCellValue(new HSSFRichTextString(remark));
            remarkCell.setCellStyle(headStyle);
            remarkCell.setCellValue(new HSSFRichTextString(remark));
        }
        remarkCell.setCellStyle(headStyle);
        HSSFRow rowTitle = sheet.createRow(1);
        List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
        int i = 2;
        for (Student stu : students) {
            HSSFRow rowTitle2 = sheet.createRow(i);
            for(int t=0;t<2;t++){
                HSSFCell cell = rowTitle2.createCell(t);
                String str = "";
                if(t==0){
                    str = stu.getStudentCode();
                }else if(t==1){
                    str = stu.getStudentName();
                }
                cell.setCellValue(new HSSFRichTextString(str));
            }
            i++;
        }
        for (int j = 0; j < titleList.size(); j++) {
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        if(importType.equals("0")) {
            ExportUtils.outputData(workbook, "身体素质", response);
        }else {
            ExportUtils.outputData(workbook, "期末成绩", response);

        }
    }

    private String getRemark() {

        String remark = "填写注意：\n"
                + "1.只能录取当前学年学期的期末数据;\n"
                + "2.任课老师只能录入自己所任课的科目成绩;\n"
                +"3.分数只能在0-5且必须为0.5的整数倍;\n";
        return remark;
    }

    private String getRemark1() {

        String remark = "填写注意：\n"
                + "1.只能录取当前学年学期的数据;\n";
        return remark;
    }

    /**
     * 判断是否为教务管理员
     */
    private boolean isAdmin(String unitId, String userId) {
        boolean res = customRoleRemoteService.checkUserRole(unitId, StutotalityConstant.STUTOTALITY_SUBSYSTEM, StutotalityConstant.STUTOTALITY_MANAGE_CODE, userId);
        return res;
//        return true;
    }

}
