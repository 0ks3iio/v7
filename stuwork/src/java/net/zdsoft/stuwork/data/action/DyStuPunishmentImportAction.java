package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/stuwork")
public class DyStuPunishmentImportAction extends DataImportAction{

	private Logger logger = Logger.getLogger(DormCheckImportAction.class);
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyStuPunishmentService dyStuPunishmentService;
	
	@RequestMapping("/studentManage/import/main")
	public String execute(ModelMap map, String acadyear, String semester) {
		// 业务名称
		map.put("businessName", "学生违纪");
		// 导入URL 
		map.put("businessUrl", "/stuwork/studentManage/import");
		// 导入模板
		//String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
		map.put("templateDownloadUrl", "/stuwork/studentManage/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());

		// 回到进入导入页面之前的学年学期
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/studentManage/punishScoreInfoImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "stuPunishmentImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
				+ "<p>2、如果违纪类型不是自定义折分，则违纪扣分取违纪类型的折分值</p>"
				+ "<p>3、	违纪时间格式，例：20190101</p>"
				+ "<p>4、	导入类型若选择“覆盖导入”，则会先<span style='color:red;font-weigth:bold;'>清空</span>对应学年学期的数据再导入</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("学年");
		tis.add("学期");
		tis.add("姓名");
		tis.add("学号");
		tis.add("违纪类型");
		tis.add("违纪原因");
		tis.add("违纪扣分");
		tis.add("违纪时间");
		return tis;
	}

	@Override
	@RequestMapping("/studentManage/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject json = JSONObject.parseObject(params);
		String importType=json.get("importType").toString();	
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		if(datas.size() == 0){
			Json importResultJson=new Json();
			importResultJson.put("totalCount", 0);
			importResultJson.put("successCount", 0);
			importResultJson.put("errorCount", 0);
			return importResultJson.toJSONString();
		}
		String jsonMsg = dyStuPunishmentService.dealImport(getLoginInfo().getUnitId(), importType, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/studentManage/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> titleList = getRowTitleList();
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		int size = titleList.size();

		HSSFRow rowTitle = sheet.createRow(0);
		for (int i = 0; i < size; i++) {
			HSSFCell cell = rowTitle.createCell(i);
			cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
		}

		HSSFSheet hidden = workbook.createSheet("hidden");

		HSSFRow row;
		HSSFCell cell;
		for (int i = 0; i < acadyearList.size(); i++) {
			row = hidden.getRow(i);
			if (row == null){
				row = hidden.createRow(i);
			}
			cell = row.createCell(0);
			cell.setCellValue(acadyearList.get(i));
		}
		row = hidden.getRow(0);
		if (row == null){
			row = hidden.createRow(0);
		}
		cell = row.createCell(1);
		cell.setCellValue("第一学期");
		row = hidden.getRow(1);
		if (row == null){
			row = hidden.createRow(1);
		}
		cell = row.createCell(1);
		cell.setCellValue("第二学期");

		DVConstraint acadyearConstraint = DVConstraint.createFormulaListConstraint("hidden!$A$1:$A$" + acadyearList.size());
		CellRangeAddressList acadyearCellRangeAddressList = new CellRangeAddressList(1, 200, 0, 0);
		HSSFDataValidation acadyearDataValidation = new HSSFDataValidation(acadyearCellRangeAddressList, acadyearConstraint);
		sheet.addValidationData(acadyearDataValidation);

		DVConstraint semesterConstraint = DVConstraint.createFormulaListConstraint("hidden!$B$1:$B$2");
		CellRangeAddressList semesterCellRangeAddressList = new CellRangeAddressList(1, 200, 1, 1);
		HSSFDataValidation semesterDataValidation = new HSSFDataValidation(semesterCellRangeAddressList, semesterConstraint);
		sheet.addValidationData(semesterDataValidation);

		workbook.setSheetHidden(1, true);

		String fileName = "学生违纪导入";
		ExportUtils.outputData(workbook, fileName, response);
	}

}
