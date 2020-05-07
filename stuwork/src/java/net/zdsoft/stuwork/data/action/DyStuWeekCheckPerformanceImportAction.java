package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.service.DyStuWeekCheckPerformanceService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/stuwork")
public class DyStuWeekCheckPerformanceImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(DormCheckImportAction.class);
	
	@Autowired
	private DyStuWeekCheckPerformanceService dyStuWeekCheckPerformanceService;
	@RequestMapping("/weekCheckPerformance/import/main")
	public String execute(ModelMap map,String acadyear,String semester, String week) {
		// 业务名称
		map.put("businessName", "值周表现");
		// 导入URL 
		map.put("businessUrl", "/stuwork/weekCheckPerformance/import");
		// 导入模板
		//String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
		map.put("templateDownloadUrl", "/stuwork/weekCheckPerformance/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
			
		map.put("businessKey", UuidUtils.generateUuid());
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("week", week);
		return "/stuwork/studentManage/weekCheckPerformanceImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "weekCheckPerformanceImport";
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
		tis.add("备注");
		tis.add("周次");
		return tis;
	}

	@Override
	@RequestMapping("/weekCheckPerformance/import")
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
		String week = jsStr.getString("week");
		String jsonMsg = dyStuWeekCheckPerformanceService.doImport(getLoginInfo().getUnitId(), datas, acadyear, semester, week);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/weekCheckPerformance/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("值周表现导入", titleMap, sheetName2RecordListMap, response);		
	}

}
