package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;
import net.zdsoft.studevelop.data.service.StudevelopDutySituationService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2018/4/9.
 */
@Controller
@RequestMapping("/studevelop/dutySituation")
public class StuDevelopDutySituationImportAction extends DataImportAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private StudevelopDutySituationService studevelopDutySituationService;
    @RequestMapping("/importLink")
    public String importLink(StudevelopDutySituation dutySituation , ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        String acadyear = dutySituation.getAcadyear();
        String semester = dutySituation.getSemester();
        String classId = dutySituation.getClassId();

        map.put("dutySituation" , JSON.toJSONString(dutySituation));
        map.put("businessName","根据下面条件下载任职情况模板");
        map.put("businessUrl","/studevelop/dutySituation/import");
        map.put("templateDownloadUrl","/studevelop/dutySituation/template.action?classId="+classId);
        //模板校验
        map.put("validateUrl", "/studevelop/dutySituation/validate");

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

        return "/studevelop/record/dutySituationImport.ftl";
    }


    @Override
    public String getObjectName() {
        return "dutySituation";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("*姓名");
        titleList.add("班内编号");
        titleList.add("*学号");
        titleList.add("*所任职务");
        titleList.add("开始时间");
        titleList.add("结束时间");
        titleList.add("工作内容");
        titleList.add("工作表现情况");
        titleList.add("备注");
        return titleList;
    }
    @RequestMapping("/import")
    @ResponseBody
    @Override
    public String dataImport(String filePath, String params) {
        log.info("业务数据处理中。。。");
        List<String[]> errorDataList = new ArrayList<>();
        StudevelopDutySituation dutySituation = JSON.parseObject(params ,StudevelopDutySituation.class);

        String unitId = getLoginInfo().getUnitId();
        List<String[]> arrDatas = ExcelUtils.readExcelByRow(filePath,1,getRowTitleList().size());
        String msg = studevelopDutySituationService.saveImportDutySituationDatas(unitId , arrDatas ,dutySituation);

        log.info("业务数据导入结束。。。");

        return msg;
    }
    @RequestMapping("/template")
    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String classId = request.getParameter("classId");
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
        List<String> titleList = getRowTitleList();
        Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(),titleList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0,"任职情况");

        HSSFRow rowTitle = sheet.createRow(0);
        int size = titleList.size();
        for(int i=0;i<size ;i++){
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }
        int i=1;
        for(Student stu: studentList){

            HSSFRow row = sheet.createRow(i);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(new HSSFRichTextString(stu.getStudentName()));
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellValue(new HSSFRichTextString(stu.getClassInnerCode()));
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(new HSSFRichTextString(stu.getStudentCode()));
            i++;
        }
        ExportUtils.outputData(workbook,"任职情况",response);


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
            return templateValidate(datas, getRowTitleList());

        }catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }
}
