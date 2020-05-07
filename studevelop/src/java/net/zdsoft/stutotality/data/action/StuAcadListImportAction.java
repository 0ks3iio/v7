package net.zdsoft.stutotality.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.dto.StudentDto;
import net.zdsoft.stutotality.data.entity.StutotalityStuFinal;
import net.zdsoft.stutotality.data.service.*;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/stutotality")
public class StuAcadListImportAction extends DataImportAction{
    private Logger logger = Logger.getLogger(StuAttendanceRecordImportAction.class);
    @Autowired
    private StutotalityStuFinalService stutotalityStuFinalService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService ;
    @Autowired
    StutotalityStuResultService stutotalityStuResultService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    TeachClassRemoteService teachClassRemoteService;
    @Autowired
    ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    StutotalityItemService stutotalityItemService;
    @Autowired
    StutotalityItemOptionService stutotalityItemOptionService;
    @Autowired
    StutotalityOptionDescService stutotalityOptionDescService;
    @Autowired
    StutotalityHealthService stutotalityHealthService;
    @Autowired
    StutotalityRewardService stutotalityRewardService;
    @Autowired
    StutotalityStuRewardService stutotalityStuRewardService;

    @Autowired
    StutotalityHealthOptionService stutotalityHealthOptionService;
    @RequestMapping("/stuAcadList/import/main")
    public  String execute(ModelMap map, String classId){
        if(StringUtils.isNotBlank(classId)){
            String unitId = getLoginInfo().getUnitId();
            Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
            //当前学年 学期
            String acadyear = semesterObj.getAcadyear();
            String semester = semesterObj.getSemester() + "";
            // 业务名称
            map.put("businessName", "学期评价");
            // 导入URL
            map.put("businessUrl", "/stutotality/stuAcadList/import?classId="+classId);
            // 导入模板
            map.put("templateDownloadUrl", "/stutotality/stuAcadList/template?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
            // 导入对象
            map.put("objectName", getObjectName());
            // 导入说明
            map.put("description", getDescription());
            map.put("businessKey", UuidUtils.generateUuid());
            map.put("acadyear", acadyear);
            map.put("semester", semester);
            map.put("classId", classId);

        }
        return "/stutotality/result/stutotalityAcadImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "stuAcadListImport";
    }
    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确；</p>"
                + "<p>2、导入数据时，表格中的数据只会更新，不会累加；</p>";
    }
    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("学号");
        tis.add("姓名");
        tis.add("班主任寄语");
        return tis;
    }

    @Override
    @RequestMapping("/stuAcadList/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {

        logger.info("业务数据处理中......");
        List<String[]> datas = ExcelUtils.readExcel(filePath,
                getRowTitleList().size());
        if(datas.size() == 0){
            Json importResultJson=new Json();
            importResultJson.put("totalCount", 0);
            importResultJson.put("successCount", 0);
            importResultJson.put("errorCount", 0);
            return importResultJson.toJSONString();
        }
        JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
        String acadyear = jsStr.getString("acadyear");
        String semester = jsStr.getString("semester");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String classId = request.getParameter("classId");
        String jsonMsg = stutotalityStuFinalService.acadListImport(getLoginInfo().getUnitId(), datas, acadyear, semester,classId);
        logger.info("导入结束......");
        return jsonMsg;
    }

    @Override
    @RequestMapping("/stuAcadList/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<String> titleList = getRowTitleList();//表头
        Map<String,List<Map<String,String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
        sheetName2Map.put(getObjectName(),new ArrayList<Map<String, String>>());
        Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(),titleList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        int size = titleList.size();
        HSSFRow rowTitle = sheet.createRow(0);
        for(int j=0;j<size;j++){
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        String classId = request.getParameter("classId");
        Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
        List<StudentDto> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(studentList)) {
            Set<String> studentIds=EntityUtils.getSet(studentList,Student::getId);
            //获取原数据结果
            List<StutotalityStuFinal> stutotalityStuFinals = stutotalityStuFinalService.findByAcadyearAndSemesterAndUnitIdAndStudentIdsWithMaster
                    (semester.getAcadyear(),semester.getSemester().toString(),unitId,studentIds.toArray(new String[0]));
            Map<String,StutotalityStuFinal> stuFinalMap =EntityUtils.getMap(stutotalityStuFinals,StutotalityStuFinal::getStudentId);
            for (Student student : studentList) {
                StudentDto studentDto = new StudentDto();
                studentDto.setId(student.getId());
                studentDto.setStudentName(student.getStudentName());
                studentDto.setSex(student.getSex());
                studentDto.setStudentCode(student.getStudentCode());
                if(stuFinalMap.containsKey(student.getId())){
                    StutotalityStuFinal stutotalityStuFinal =stuFinalMap.get(student.getId());
                    studentDto.setFinalId(stutotalityStuFinal.getId());
                    if(StringUtils.isNotBlank(stutotalityStuFinal.getTeacherContent())){
                        String teacherContent = stutotalityStuFinal.getTeacherContent();
                        studentDto.setTeacherContent(teacherContent);
                    }
                }
                list.add(studentDto);
            }
        }

        int i = 1;
        for (StudentDto stu : list) {
            HSSFRow rowTitle2 = sheet.createRow(i);
            for(int t=0;t<3;t++){
                HSSFCell cell = rowTitle2.createCell(t);
                String str = "";
                if(t==0){
                    str = stu.getStudentCode();
                }else if(t==1){
                    str = stu.getStudentName();
                }else {
                    if(stu.getTeacherContent() == null ){
                        str = "";
                    }else {
                        str = stu.getTeacherContent()+"";
                    }
                }
                cell.setCellValue(new HSSFRichTextString(str));
            }
            i++;
        }
        String fileName = "期末评价导入";
        ExportUtils.outputData(workbook, fileName, response);
    }

}
