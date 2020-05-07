package net.zdsoft.stutotality.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stutotality.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/stutotality")
public class StuRewardListImportAction extends DataImportAction{
    private Logger logger = Logger.getLogger(StuAttendanceRecordImportAction.class);
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
    @RequestMapping("/stuReward/import/main")
    public  String execute(ModelMap map, String classId){
        if(StringUtils.isNotBlank(classId)){
            String unitId = getLoginInfo().getUnitId();
            Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
            //当前学年 学期
            String acadyear = semesterObj.getAcadyear();
            String semester = semesterObj.getSemester() + "";
            // 业务名称
            map.put("businessName", "获奖情况");
            // 导入URL
            map.put("businessUrl", "/stutotality/stuRewardList/import?classId="+classId);
            // 导入模板
            map.put("templateDownloadUrl", "/stutotality/stuRewardList/template?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
            // 导入对象
            map.put("objectName", getObjectName());
            // 导入说明
            map.put("description", getDescription());
            map.put("businessKey", UuidUtils.generateUuid());
            map.put("acadyear", acadyear);
            map.put("semester", semester);
            map.put("classId", classId);
        }
        return "/stutotality/result/stutotalityRewardImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "stuRewardListImport";
    }
    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确；</p>"
                + "<p>2、导入数据时，表格中的数据只会累计增加，不会更新；</p>";
    }
    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("学号");
        tis.add("姓名");
        tis.add("获奖级别");
        tis.add("备注");
        return tis;
    }

    @Override
    @RequestMapping("/stuRewardList/import")
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
        List<String[]> newdatas = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(datas)){
            for (int i = 0; i< datas.size();i++) {
                if(i !=0){
                    newdatas.add(datas.get(i));
                }
            }
        }

        JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
        String acadyear = jsStr.getString("acadyear");
        String semester = jsStr.getString("semester");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String classId = request.getParameter("classId");

        String jsonMsg = stutotalityStuRewardService.acadListImport(getLoginInfo().getUnitId(), newdatas, acadyear, semester,classId);
        logger.info("导入结束......");
        return jsonMsg;
    }

    @Override
    @RequestMapping("/stuRewardList/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
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
        String rewardNames = "填写注意：\n"
                + "1.获奖级别只能填当前学年学期维护的奖励级别";

        remarkCell.setCellValue(new HSSFRichTextString(rewardNames));
        remarkCell.setCellStyle(headStyle);

        HSSFRow rowTitle = sheet.createRow(1);
        for (int j = 0; j < titleList.size(); j++) {
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        String fileName = "获奖情况导入";
        ExportUtils.outputData(workbook, fileName, response);
    }



}
