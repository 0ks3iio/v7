package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import net.zdsoft.studevelop.data.service.StudevelopPerformItemService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/studevelop/monthPerformance")
public class StuDevelopMouthPerformanceImportAction  extends DataImportAction {
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StudevelopPerformItemService studevelopPerformItemService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private StudevelopMonthPerformanceService studevelopMonthPerformanceService;
    private Logger logger = Logger.getLogger(StuDevelopMouthPerformanceImportAction.class);
    @RequestMapping("/importLink")
    public String importLink(StudevelopMonthPerformance studevelopMonthPerformance , ModelMap map ){

        String unitId = getUnitId();
        String acadyear = studevelopMonthPerformance.getAcadyear();
        Integer semester = studevelopMonthPerformance.getSemester();
        String classId = studevelopMonthPerformance.getClassId();
        Integer performMonth = studevelopMonthPerformance.getPerformMonth();

        map.put("monthPermance",JSON.toJSONString(studevelopMonthPerformance));
        map.put("businessName","根据下面条件下载每月在校表现");

        map.put("businessUrl","/studevelop/monthPerformance/import");



        map.put("templateDownloadUrl","/studevelop/monthPerformance/template?classId="+classId);
        map.put("objectName","");
        map.put("description" ,"");
        map.put("businessKey", UuidUtils.generateUuid());
//        map.put("resourceUrl","version7");
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map , "学年学期不存在");
        }
        map.put("acadyearList",acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
        if(StringUtils.isEmpty(acadyear)&& semester == null &&semesterObj != null){
            acadyear = semesterObj.getAcadyear();
            semester = semesterObj.getSemester();
        }
        map.put("acadyear" ,acadyear);
        map.put("semester" , semester);
//        List<Grade> gradeList = gradeService.findByUnitId(unitId);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId) , Grade.class);

        List<String> gradeStr = new ArrayList<String>();
        for(Grade grade: gradeList){
            gradeStr.add(grade.getId());
        }
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        map.put("classList",classList);
        map.put("classId",classId);
//        Calendar calendar = Calendar.getInstance();
//        int month = calendar.get(Calendar.MONTH) + 1;
//        if(performonth == null){
//            map.put("currentMonth",month);
//        }else{
//            map.put("currentMonth",performonth);
//        }
        map.put("performMonth",performMonth);


        return "/studevelop/stuGrow/stuDevelopMonthPerformanceImport.ftl";

    }
    @Override
    public String getObjectName() {
        return "studevelopMonthPerformance";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> titleList = new ArrayList<String>();
        titleList.add("姓名");

        return titleList;
    }
    @RequestMapping("/import")
    @ResponseBody
    @Override
    public String dataImport(String filePath, String params) {

        logger.info("业务数据处理中......");
        List<String[]> errorDataList=new ArrayList<String[]>();
        StudevelopMonthPerformance performance = JSON.parseObject(params,StudevelopMonthPerformance.class);
        String classId = performance.getClassId();
        Clazz cla = Clazz.dc(classRemoteService.findOneById(classId));
        int ny = NumberUtils.toInt(performance.getAcadyear().substring(5));
        int oy = NumberUtils.toInt(cla.getAcadyear().substring(0, 4));
        String gradeCode = cla.getSection().intValue()+""+(ny-oy);
        List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(getUnitId(), gradeCode);
        //
        Map<String,StuDevelopPerformItem> itemMap = new HashMap<String, StuDevelopPerformItem>();
            //解析Excel数据
        List<String[]> data = ExcelUtils.readExcel(filePath,itemList.size()+2);

        //错误数据序列号
        int sequence =1;
        if(CollectionUtils.isEmpty(itemList)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="项目名称";
            errorData[2]="";
            errorData[3]="该班级下没有考评项目";
            errorDataList.add(errorData);
            return result(data.size()-1,0,0,errorDataList);
        }
        List<String> itemIdList = new ArrayList<String>();
        for(StuDevelopPerformItem item :itemList){
            itemMap.put(item.getItemName(),item);
            itemIdList.add(item.getId());
        }
        //封装一个key 以unitid ，学年 学期 月份 项目id 学生id 做的key
        List<StudevelopMonthPerformance> monthPerformances = studevelopMonthPerformanceService.getStudevelopMonthPerformanceByItemIds(getUnitId(),performance.getAcadyear(),performance.getSemester(),performance.getPerformMonth(),itemIdList.toArray(new String[0]));
        Map<String,StudevelopMonthPerformance> monthPerformanceMap = new HashMap<String, StudevelopMonthPerformance>();
        if(CollectionUtils.isNotEmpty(monthPerformances)){
            for(StudevelopMonthPerformance monthPerformance : monthPerformances){
                String key = monthPerformance.getUnitId()+"_"+monthPerformance.getAcadyear()+"_"+monthPerformance.getSemester()+""+monthPerformance.getPerformMonth()+""+ monthPerformance.getItemId()+"_" + monthPerformance.getStudentId();
                monthPerformanceMap.put(key,monthPerformance);
            }
        }

        //建立等级 和 permancecode的Id对应值
        Map<Integer,Map<String,StuDevelopPerformItemCode>> indexCodeMap = new HashMap<Integer, Map<String,StuDevelopPerformItemCode>>();
        String[] labels = {"A","B","C","D","E"};
        if(CollectionUtils.isNotEmpty(data)){
            String[] title = data.get(0);
            for(int i=2;i<title.length;i++){
                StuDevelopPerformItem performItem = itemMap.get(title[i]);
                if(performItem == null){
                    String[] errorData=new String[4];
                    sequence++;
                    errorData[0]=String.valueOf(sequence);
                    errorData[1]="项目名称";
                    errorData[2]=title[i];
                    errorData[3]="该考评项目在该班级下不存在";
                    errorDataList.add(errorData);
                }else{
                    List<StuDevelopPerformItemCode> codeList = performItem.getCodeList();
                    if(CollectionUtils.isNotEmpty(codeList)){
//                        indexCodeMap.put(i,codeList);
                        Map<String,StuDevelopPerformItemCode> itemCodeMap1 = new HashMap<String, StuDevelopPerformItemCode>();
                        int m=0;
                        for(StuDevelopPerformItemCode l:codeList){
                            itemCodeMap1.put(labels[m++]+"："  +l.getCodeContent(),l);
                        }
                        indexCodeMap.put(i,itemCodeMap1);
                    }
                }
            }
        }
        //取班级下的学生数据
        List<Student> studentList  = SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
        Map<String,Student> idCardStuMap = new HashMap<String, Student>();
        Map<String,Student> stuNameMap = new HashMap<String, Student>();
        for(Student stu:studentList){
            idCardStuMap.put(stu.getIdentityCard(),stu);
            stuNameMap.put(stu.getStudentName(),stu);
        }
        //校验数据有效性 ，该班级下是否存在该学生， 学生姓名和身份证是否相符
        String superKey = getUnitId()+"_"+performance.getAcadyear()+"_"+performance.getSemester()+"_"+performance.getPerformMonth();
        List<StudevelopMonthPerformance> updateMonthPerformance = new ArrayList<StudevelopMonthPerformance>();
        int totalSize =data.size()-1;
        int updateCount=0;
        for(int i=1;i<data.size();i++){


                String[] arr = data.get(i);
                int j=0;
                if(StringUtils.isEmpty(arr[j])){
                    String[] errorData=new String[4];
                    sequence++;
                    errorData[0]=String.valueOf(sequence);
                    errorData[1]="学生姓名 ";
                    errorData[2]="";
                    errorData[3]="学生姓名为空，不能导入";
                    errorDataList.add(errorData);
                    continue;
                }else {
                    Student student1 = stuNameMap.get(arr[j]);
                    if(student1 == null){
                        String[] errorData=new String[4];
                        sequence++;
                        errorData[0]=String.valueOf(sequence);
                        errorData[1]="学生姓名 ";
                        errorData[2]="";
                        errorData[3]="该学生在该班级下不存在，不能导入";
                        errorDataList.add(errorData);
                        continue;
                    }
                    j =j+1;
                    if(StringUtils.isNotEmpty(arr[j])){
                        Student student2= idCardStuMap.get(arr[j]);
                        String student2Name = student2.getStudentName();
                        if(!student1.getStudentName().equals(student2Name)){
                            String[] errorData=new String[4];
                            sequence++;
                            errorData[0]=String.valueOf(sequence);
                            errorData[1]="姓名 ";
                            errorData[2]=student1.getStudentName();
                            errorData[3]="该学生姓名和身份证号不符，不能导入";
                            errorDataList.add(errorData);
                            continue;
                        }else{
                            student1 = student2;
                        }
                    }
                    String stuId =student1.getId();
                    j++;
                    for(;j<arr.length;j++){
//                        List<StuDevelopPerformItemCode> codeList = indexCodeMap.get(j);
                          Map<String,StuDevelopPerformItemCode> codeMap = indexCodeMap.get(j);
                          if(MapUtils.isEmpty(codeMap)){
                              continue;
                          }
                        StuDevelopPerformItemCode code = codeMap.get(arr[j]);
                        if(code == null){
                            String[] errorData=new String[4];
                            sequence++;
                            errorData[0]=String.valueOf(sequence);
                            errorData[1]="等级 ";
                            errorData[2]=student1.getStudentName() + "_"+arr[j];
                            errorData[3]="该考评项目下不存在该等级";
                            errorDataList.add(errorData);
                            continue;
                        }else{
                            superKey = superKey +"_"+code.getItemId()+"_"+stuId;
                            StudevelopMonthPerformance monthPerformance = monthPerformanceMap.get(superKey);
                            if(monthPerformance == null){
                                monthPerformance= new StudevelopMonthPerformance();

                                monthPerformance.setId(UuidUtils.generateUuid());
                                monthPerformance.setUnitId(getUnitId());
                                monthPerformance.setSemester(performance.getSemester());
                                monthPerformance.setAcadyear(performance.getAcadyear());
//                            monthPerformance.setClassId(classId);
                                monthPerformance.setPerformMonth(performance.getPerformMonth());
                                monthPerformance.setCreationTime(new Date());
                                monthPerformance.setStudentId(stuId);
                                monthPerformance.setItemId(code.getItemId());
                                monthPerformance.setResultId(code.getId());
                            }else{
                                monthPerformance.setResultId(code.getId());
                                monthPerformance.setModifyTime(new Date());
                            }

                            updateMonthPerformance.add(monthPerformance);

                        }
                    }
               }

            updateCount++;
        }

        try{
            studevelopMonthPerformanceService.saveAll(updateMonthPerformance.toArray(new StudevelopMonthPerformance[0]));
        }catch (Exception e){
            e.printStackTrace();

        }

//        int successCount = updateMonthPerformance.size();
        int errorCount = totalSize - updateCount;
        String result = result(totalSize,updateCount,errorCount,errorDataList);
        return result;
    }

    private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }
    @RequestMapping("/template")
    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

        String classId = request.getParameter("classId");
//        Clazz cla = classService.findOne(classId);
        Clazz cla = Clazz.dc(classRemoteService.findOneById(classId));
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()) ,Grade.class);
        List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(getUnitId(),grade.getGradeCode());
        List<String> titleList = new ArrayList<String>();
        titleList.add("名字");
        titleList.add("身份证号");
        Map<String,List<String>>  performCodeMap = new HashMap<String, List<String>>();
        String[] labels = {"A","B","C","D","E"};
        for(StuDevelopPerformItem item : itemList){
            String itemName = item.getItemName();
            List<StuDevelopPerformItemCode> list = item.getCodeList();
            titleList.add(itemName);
            int size = list.size();
            List<String> stringList = new ArrayList<String>();

            for(int i=0;i<size;i++){
                stringList.add(labels[i]+"：" +list.get(i).getCodeContent());
            }
            performCodeMap.put(itemName,stringList);
        }


        Map<String,List<Map<String,String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
        sheetName2Map.put(getObjectName(),new ArrayList<Map<String, String>>());
        Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(),titleList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0,"每月在校表现");

        HSSFCellStyle style = workbook.createCellStyle();

        // 设置字体HorizontalAlignment.CENTER)
        HSSFFont headfont = workbook.createFont();
        headfont.setFontName("黑体");
        headfont.setFontHeightInPoints((short) 22);// 字体大小
        headfont.setBold(true);// 加粗
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.CENTER);
        CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size() );
        sheet.addMergedRegion(car);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(new HSSFRichTextString("每月在校表现"));
        titleCell.setCellStyle(style);

        HSSFRow rowTitle = sheet.createRow(1);
        int size = titleList.size();
        for(int j=0;j<size;j++){
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        List<Student> studentList  = SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});

        int  i=2;
        for(Student stu:studentList){
            HSSFRow row = sheet.createRow(i);
            int j=0;
            HSSFCell studentName = row.createCell(j++);
            studentName.setCellValue(new HSSFRichTextString(stu.getStudentName()));
            HSSFCell idCard = row.createCell(j++);
            idCard.setCellValue(new HSSFRichTextString(stu.getIdentityCard()));
            i++;
        }

        int studentListSize = studentList.size();
        for(int j=2 ;j<size;j++){

            String itemName = titleList.get(j);
            List<String> list = performCodeMap.get(itemName);

            DVConstraint constraint = DVConstraint.createExplicitListConstraint(list.toArray(new String[0]));
            CellRangeAddressList regions =  new CellRangeAddressList(2,studentListSize+1,j,j);
            HSSFDataValidation validation = new HSSFDataValidation(regions,constraint);

            sheet.addValidationData(validation);
        }

        ExportUtils.outputData(workbook,"每月在校表现",response);
    }

    public String getUnitId(){
        LoginInfo loginInfo = getLoginInfo();
        return loginInfo.getUnitId();
    }
}
