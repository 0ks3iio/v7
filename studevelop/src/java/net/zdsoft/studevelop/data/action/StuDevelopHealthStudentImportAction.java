package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudent;
import net.zdsoft.studevelop.data.service.StuHealthStudentService;
import net.zdsoft.studevelop.data.service.StuHealthyProjectService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * Created by Administrator on 2018/4/25.
 */

@Controller
@RequestMapping("/stuDevelop/healthyStudentImport")
public class StuDevelopHealthStudentImportAction extends DataImportAction{
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private StuHealthyProjectService stuHealthyProjectService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StuHealthStudentService stuHealthStudentService;
    @RequestMapping("/importLink")
    public String importLink(StudevelopHealthStudent healthStudent ,ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        String acadyear = healthStudent.getAcadyear();
        String semester = healthStudent.getSemester();
        String classId = healthStudent.getClassId();

        map.put("healthStudent" , JSON.toJSONString(healthStudent));
        map.put("businessName","身心健康");
        map.put("businessUrl","/stuDevelop/healthyStudentImport/import");
        map.put("templateDownloadUrl","/stuDevelop/healthyStudentImport/template.action?classId="+classId+"&acadyear="+acadyear+"&semester="+semester);
        //模板校验
//        map.put("validateUrl", "/studevelop/healthStudent/validate");

        map.put("objectName","");
        map.put("description" ,"");
        map.put("businessKey", UuidUtils.generateUuid());
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map , "学年学期不存在");
        }
        map.put("acadyearList",acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
        if(StringUtils.isEmpty(acadyear)&& semester == null &&semesterObj != null){
            acadyear = semesterObj.getAcadyear();
            semester = String.valueOf(semesterObj.getSemester());
        }
        map.put("acadyear" ,acadyear);
        map.put("semester" , semester);
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        map.put("classList",classList);
        map.put("classId",classId);
        return "/studevelop/record/stuHealthStudentImport.ftl";
    }


    @Override
    public String getObjectName() {


        return "healthStudent";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getRowTitleList() {
        return null;
    }
    @RequestMapping("/import")
    @ResponseBody
    @Override
    public String dataImport(String filePath, String params) {
        log.info("业务数据处理中。。。");
//        /stuDevelop/healthyStudent
        StudevelopHealthStudent healthStudent = JSONObject.parseObject(params,StudevelopHealthStudent.class);
        String msg ="";
        try{
            msg = stuHealthStudentService.saveHealthStudentImport(getLoginInfo().getUnitId(),healthStudent ,filePath);
        }catch (Exception e){
            e.printStackTrace();
        }



        log.info("业务数据导入结束。。。");
        return msg;
    }
    @RequestMapping("/template")
    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String classId = request.getParameter("classId");
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");

        Clazz cla = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
        int section =grade.getSection();
        StudevelopHealthProject project = new StudevelopHealthProject();
        project.setAcadyear(acadyear);
        project.setSemester(semester);
        project.setSchSection(String.valueOf(section));
        project.setSchoolId(cla.getSchoolId());
        List<StudevelopHealthProject> projectList = stuHealthyProjectService.getProjectByAcadyearSemesterSection(project);
        Map<String,List<StudevelopHealthProject>> healthProjectMap =  EntityUtils.getListMap(projectList ,"projectType",null);
        List<McodeDetail> detailList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                String classInnerCode1 = o1.getClassInnerCode();
                String classInnerCode2 = o2.getClassInnerCode();
                int code1 = NumberUtils.toInt(classInnerCode1);
                int code2 = NumberUtils.toInt(classInnerCode2);
                if(code1 > code2){
                    return 1;
                }else if( code1 == code2){
                    return 0;
                }else{
                    return -1;
                }
            }
        });

        List<String> titleList = getRowTitleList();
        Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(),titleList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0,"身心健康情况");

        HSSFRow rowTitle = sheet.createRow(0);
        HSSFCell cell1 = rowTitle.createCell(0);
        cell1.setCellValue("姓名");
        CellRangeAddress address1 = new CellRangeAddress(0,1,0,0);
        sheet.addMergedRegion(address1);
        HSSFCell cell2 = rowTitle.createCell(1);
        cell2.setCellValue("班内编号");
        CellRangeAddress address2 = new CellRangeAddress(0,1,1,1);
        sheet.addMergedRegion(address2);
        HSSFCell cell3 = rowTitle.createCell(2);
        cell3.setCellValue("学号");
        CellRangeAddress address3 = new CellRangeAddress(0,1,2,2);
        sheet.addMergedRegion(address3);
        int col0=3;
        int col1=3;
//        HSSFCell cellTemp = null;
//        CellRangeAddress addressTemp;
        HSSFRow row2 = sheet.createRow(1);
        for(McodeDetail detail : detailList){
            HSSFCell cellTemp = rowTitle.createCell(col0);
            cellTemp.setCellValue(detail.getMcodeContent());
            List<StudevelopHealthProject> list = healthProjectMap.get(detail.getThisId());
            if(list ==null) {
                list = new ArrayList<>();
            }
            int colDiff = list.size()>0? col0+list.size()-1:col0;
            CellRangeAddress addressTemp = new CellRangeAddress(0,0,col0,colDiff);
            sheet.addMergedRegion(addressTemp);
            for(StudevelopHealthProject healthProject :list){
                HSSFCell cellTempC = row2.createCell(col1);
                if("2".equals(healthProject.getProjectType())){
                    String value =healthProject.getProjectName();
                    if(StringUtils.isNotEmpty(healthProject.getProjectUnit())){
                        value = value +"("+healthProject.getProjectUnit()+")";
                    }
                    cellTempC.setCellValue(value);
                }else{
                    cellTempC.setCellValue(healthProject.getProjectName() );
                }

                col1++;


            }
            col0 +=list.size();

        }

        CellRangeAddress addressTemp2 = new CellRangeAddress(0,0,6,9);
        sheet.addMergedRegion(addressTemp2);
        CellRangeAddress addressTemp3 = new CellRangeAddress(0,0,10,11);
        sheet.addMergedRegion(addressTemp3);
        int row3=2;
        for(Student stu: studentList){
            HSSFRow row = sheet.createRow(row3);
            HSSFCell cel1 = row.createCell(0);
            cel1.setCellValue(new HSSFRichTextString(stu.getStudentName()));
            HSSFCell cel2 = row.createCell(1);
            cel2.setCellValue(new HSSFRichTextString(stu.getClassInnerCode()));
            HSSFCell cel3 = row.createCell(2);
            cel3.setCellValue(new HSSFRichTextString(stu.getStudentCode()));
            row3++;
        }
        ExportUtils.outputData(workbook,"学生身心健康情况",response);

    }
    /**
     * 模板校验
     * @return
     */
    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo){
        log.info("模板校验中......");
        validRowStartNo = "1";
        try{
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
                    Integer.valueOf(validRowStartNo),getRowTitleList().size());
            return templateHealthValidate(datas, getRowTitleList());

        }catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }
    public String templateHealthValidate(List<String[]> allDataList,
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
}
