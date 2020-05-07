package net.zdsoft.credit.data.action;

import net.zdsoft.credit.data.service.CreditStatService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.McodeRemoteService;
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

@Controller
@RequestMapping("exammanage/credit")
public class ImportResitScoreAction extends DataImportAction {
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private CreditStatService creditStatService;

    @RequestMapping("/import/main")
    public String execute(ModelMap map, String gradeId, String year, String semester, String gradeName) {
        // 业务名称
        map.put("businessName", "补考信息");
        // 导入URL
        map.put("businessUrl", "/exammanage/credit/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/credit/template");
        // 导入对象
        map.put("objectName", getObjectName());
        // 导入说明
        map.put("description", getDescription());

        map.put("businessKey", UuidUtils.generateUuid());
        map.put("gradeId", gradeId);
        map.put("year", year);
        map.put("semester", semester);
        map.put("gradeName", gradeName);
        return "exammanage/credit/creditStat/importResitScore.ftl";
    }

    @Override
    public String getObjectName() {
        return "importResitScoreObject";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确；</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("学号");
        tis.add("学生姓名");
        tis.add("补考成绩");
        tis.add("学年");
        tis.add("学期");
        tis.add("科目");
        return tis;
    }

    @RequestMapping("/import")
    @ResponseBody
    @Override
    public String dataImport(String filePath, String params) {
        log.info("业务数据处理中。。。");
        List<String[]> errorDataList = new ArrayList<>();

        String unitId = getLoginInfo().getUnitId();
        List<String[]> arrDatas = ExcelUtils.readExcelByRow(filePath, 1, getRowTitleList().size());
        arrDatas.remove(0);
        String result = creditStatService.saveImportData(arrDatas, getLoginInfo());
        log.info("业务数据导入结束。。。");
        return result;
    }

    @Override
    @RequestMapping("/template")
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
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(headStyle);

        HSSFRow rowTitle = sheet.createRow(1);
        for (int j = 0; j < titleList.size(); j++) {
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        ExportUtils.outputData(workbook, "学生补考", response);
    }

    private String getRemark() {

        String remark = "填写注意：\n"
                + "1.学年填写格式为2017-2018,2018-2019,以此类推;\n"
                + "2.学期填写格式为第一学期,第二学期.";
        return remark;
    }
}
