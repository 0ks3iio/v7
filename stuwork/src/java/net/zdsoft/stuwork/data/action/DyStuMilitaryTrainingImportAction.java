package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.service.DyStuMilitaryTrainingService;

import org.apache.log4j.Logger;
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
@Controller
@RequestMapping("/stuwork")
public class DyStuMilitaryTrainingImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(DormCheckImportAction.class);
	@Autowired
	private DyStuMilitaryTrainingService dyStuMilitaryTrainingService;
	@RequestMapping("/militaryTraining/import/main")
	public String execute(ModelMap map,Date searchDate,String searchBuildId) {
		// 业务名称
		map.put("businessName", "军训管理");
		// 导入URL 
		map.put("businessUrl", "/stuwork/militaryTraining/import");
		// 导入模板
		//String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
		map.put("templateDownloadUrl", "/stuwork/militaryTraining/template");
		//模板校验
		map.put("validateUrl", "/stuwork/militaryTraining/validate");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
			
		map.put("businessKey", UuidUtils.generateUuid());
		return "/stuwork/studentManage/stuMilitaryTrainingImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "militaryTrainingImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("学号");
		tis.add("等第");
		tis.add("学年");
		tis.add("学期");
		tis.add("备注");
		return tis;
	}

	@Override
	@RequestMapping("/militaryTraining/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,
				getRowTitleList().size());
		datas.remove(0);
		String jsonMsg = dyStuMilitaryTrainingService.saveImport(getLoginInfo().getUnitId(), datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/militaryTraining/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> titleList = getRowTitleList();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		
		CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
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
        remarkRow.setHeightInPoints(4*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(headStyle);
        //注意事项
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(headStyle);
        
        HSSFRow rowTitle = sheet.createRow(1);
        for(int i=0;i<titleList.size();i++){
        	HSSFCell cell = rowTitle.createCell(i);
        	cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }
		
		String fileName = "军训管理导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}
	
	/**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/militaryTraining/validate")
	@ResponseBody
	public String validate(String filePath){
		logger.info("模板校验中......");
		try{
			List<String> rowTitleList = getRowTitleList();
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,rowTitleList.size());
			return templateValidate(datas, rowTitleList);
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	private String getRemark() {

		String remark = "填写注意：\n" 
						+ "1.学年填写格式为2017-2018,2018-2019,以此类推;\n"
						+ "2.学期填写格式为第一学期,第二学期.";
		return remark;
	}
}
