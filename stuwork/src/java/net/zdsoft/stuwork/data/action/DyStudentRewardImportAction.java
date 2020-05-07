package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.service.DyStudentRewardSettingService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/stuwork/studentReward/studentRewardImport")
public class DyStudentRewardImportAction extends DataImportAction{
private Logger logger = Logger.getLogger(DyStudentRewardImportAction.class);
	
	
	@Autowired
	private DyStudentRewardSettingService dyStudentRewardSettingService;


	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map) {
		// 业务名称
		map.put("businessName", "学科竞赛");
		// 导入URL 
		map.put("businessUrl", "/stuwork/studentReward/studentRewardImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/studentReward/studentRewardImport/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/studentReward/studentRewardImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "studentRewardImport";
	};

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"+ "<p>2、不填写分数默认为录入时输入分数</p>";
	};

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("类型");
		tis.add("项目名称");
		tis.add("级别");
		tis.add("奖级");
		tis.add("分值");
		tis.add("备注");
		return tis;
	}
	

	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学科竞赛导入", titleMap, sheetName2RecordListMap, response);
	}
	
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("学科竞赛业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
//		System.out.println(datas.size());
		JSONObject json = JSONObject.parseObject(params);
		
//		// 业务处理模块　具体的业务在这里处理
//		//自己的处理接口　数据范围格式如下
//		//业务类中返回给我的一个json格式的数据
		String jsonMsg = dyStudentRewardSettingService.doImport(datas,getLoginInfo().getUnitId());
		logger.info("导入结束......");
		return jsonMsg;
	}

}
