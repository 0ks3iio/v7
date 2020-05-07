package net.zdsoft.scoremanage.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.HwConstants;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: panlf
 * @Date: 2019/11/19 10:08
 */
@RequestMapping("/scoremanage/excelExport")
@RestController
public class HwScoreExcelExportAction extends BaseAction {

    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private HwStatisService hwStatisService;


    /**
     * type 02 保送生成绩汇总    03 个人成绩汇总
     * HwConstants.PLAN_TYPE_2
     * excel 导出一个学生的数据-
     */
    @RequestMapping("/{type}/stuExport/{studentId}")
    public void stuExport(@PathVariable("type") String type, @PathVariable("studentId") String studentId, HttpServletResponse response) {
        List<List<String>> reportList;
        if (HwConstants.PLAN_TYPE_2.equals(type)) {
            reportList = hwStatisService.getReportByStudentId(getLoginInfo().getUnitId(), studentId);
        } else {
            reportList = hwStatisService.getPersonalReportByStudentId(getLoginInfo().getUnitId(), studentId);
        }
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        if (CollectionUtils.isEmpty(reportList)) {
            return;
        }
        createOneSheet(sheet, reportList, getStyleList(workbook));
        String fileName = String.format("%s成绩汇总", student.getStudentName());
        ExportUtils.outputData(workbook, fileName, response);
    }


    /**
     * type 02 保送生成绩汇总    03 个人成绩汇总
     * excel 导出一个班级的数据
     */
    @RequestMapping("/{type}/classExport/{classId}")
    public void classExport(@PathVariable("type") String type, @PathVariable("classId") String classId, HttpServletResponse response) {
        Map<String, List<List<String>>> infoListMap;
        if (HwConstants.PLAN_TYPE_2.equals(type)) {
            infoListMap = hwStatisService.getReportByClassId(getLoginInfo().getUnitId(), classId);
        } else {
            infoListMap = hwStatisService.getPersonalReportByClassId(getLoginInfo().getUnitId(), classId);
        }
        if (CollectionUtils.isNotEmpty(infoListMap.keySet())) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            List<HSSFCellStyle> styleList = getStyleList(workbook);
            for (String stuName : infoListMap.keySet()) {
                List<List<String>> reportList = infoListMap.get(stuName);
                HSSFSheet sheet = workbook.createSheet(stuName);
                createOneSheet(sheet, reportList, styleList);
            }
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            ExportUtils.outputData(workbook, clazz.getClassName() + "成绩汇总统计", response);
        }
    }


    @RequestMapping(value = "/{type}/checkDataCount",method = RequestMethod.GET)
    @ResponseBody
    public String checkDataCount(@PathVariable("type") String type,String classId,String studentId){
        Integer count=hwStatisService.countDataByPlanType(getLoginInfo().getUnitId(),type,classId,studentId);
        return String.valueOf(count==null?0:count);
    }

    private void createOneSheet(HSSFSheet sheet, List<List<String>> reportList, List<HSSFCellStyle> styleList) {
        if (CollectionUtils.isEmpty(reportList)) {
            return;
        }
        int len = reportList.get(2).size();
        //第一行 大标题  : 学生个人成绩汇总表
        CellRangeAddress car = new CellRangeAddress(0, 0, 0, len - 1);
        sheet.addMergedRegion(car);
        HSSFRow remarkRow = sheet.createRow(0);
        remarkRow.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());

        CellRangeAddress carInfo = new CellRangeAddress(1, 1, 0, len - 1);
        sheet.addMergedRegion(carInfo);
        for (int i = 0; i < reportList.size(); i++) {
            List<String> list = reportList.get(i);
            HSSFCellStyle style;
            if (i == 0) {
                //大标题
                HSSFCell cell = remarkRow.createCell(0);
                cell.setCellValue(new HSSFRichTextString(list.get(0)));
                cell.setCellStyle(styleList.get(0));
                continue;
            }
            HSSFRow row = sheet.createRow(i);
            if (i == 1) {
                //学生个人信息栏
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(new HSSFRichTextString(list.get(0)));
                cell.setCellStyle(styleList.get(1));
                continue;
            }
            if (i > reportList.size() - 3) {
                //盖章,学校名
                style = styleList.get(3);
                for (int k = 0; k < len - 1; k++) {
                    list.add(0, "");
                }
            } else {
                //成绩信息
                style = styleList.get(2);
            }
            for (int j = 0; j < list.size(); j++) {
                String val = list.get(j);
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(new HSSFRichTextString(val));
                cell.setCellStyle(style);
            }
        }
        //String maxString = reportList.stream().skip(2).max((x, y) -> x.get(0).length() - y.get(0).length()).get().get(0);
        //设置自动列宽
        for (int i = 0; i < len; i++) {
            if (i < 1) {
                //第一列有合并单元格
                sheet.autoSizeColumn(0, true);
                sheet.setColumnWidth(i, 3200);
            } else {
                try {
                    sheet.setColumnWidth(i, reportList.get(2).get(i).getBytes().length * 512);
                } catch (Exception e) {
                    sheet.autoSizeColumn(i);
                }
            }
        }
    }

    private List<HSSFCellStyle> getStyleList(HSSFWorkbook workbook) {
        ArrayList<HSSFCellStyle> styleList = new ArrayList<>();
        styleList.add(getTitleStyle(workbook));
        styleList.add(getStuInfoStyle(workbook));
        styleList.add(getStyle(workbook));
        styleList.add(getSchoolStyle(workbook));
        return styleList;
    }

    private HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor((short) 8);
        style.setLeftBorderColor((short) 8);
        style.setRightBorderColor((short) 8);
        style.setBottomBorderColor((short) 8);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * '盖章'  信息格式
     * @param workbook
     * @return
     */
    private HSSFCellStyle getSchoolStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    /**
     * 学生信息格式
     * @param workbook
     * @return
     */
    private HSSFCellStyle getStuInfoStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    /**
     * 大标题格式
     * @param workbook
     * @return
     */
    private HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
