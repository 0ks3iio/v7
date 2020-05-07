package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;
import net.zdsoft.stuwork.data.service.DyStuHealthProjectItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthResultService;

/**
 * @author yangsj  2017年9月27日下午3:48:52
 */
@Controller
@RequestMapping("/stuwork")
public class DyStuHealthImportAction extends DataImportAction{
    
	private Logger logger = Logger.getLogger(DyStuHealthImportAction.class);
	@Autowired
	private DyStuHealthResultService dyStuHealthResultService;
	@Autowired
	private DyStuHealthProjectItemService dyStuHealthProjectItemService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/health/import/main")
	public String execute(ModelMap map,String acadyear,String semester) {
		// 业务名称
		map.put("businessName", "体检信息");
		// 导入URL 
		map.put("businessUrl", "/stuwork/health/import/doimport");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/health/import/template?acadyear="+acadyear+"&semester="+semester);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/health/input/healthImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return "dyStuHealthImport";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
	   return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
				+ "<p>2、注意模板的体检项的顺序是否正确</p>";
	}


	@Override
	@RequestMapping("/health/import/doimport")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("业务数据处理中......");
		JSONObject json = JSONObject.parseObject(params);
		String acadyear=json.get("acadyear").toString();
		String semester=json.get("semester").toString();
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList(acadyear,semester).size());
		System.out.println(datas.size());
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		String jsonMsg =dyStuHealthResultService.doImport(getLoginInfo().getUnitId(),acadyear,semester,datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	
	@Override
	@RequestMapping("/health/import/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList(acadyear,semester);
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学生体检信息导入", titleMap, sheetName2RecordListMap, response);
	}
    
	public List<String> getRowTitleList(String acadyear,String semester) {
		List<String> tis = new ArrayList<String>();
		List<DyStuHealthProjectItem>  listDSHPI = dyStuHealthProjectItemService.findBySemester(getLoginInfo().getUnitId(), acadyear, semester);
		tis.add("学号");
		tis.add("学生姓名");
		for (DyStuHealthProjectItem dyStuHealthProjectItem : listDSHPI) {
			tis.add(dyStuHealthProjectItem.getItemName());
		}
		return tis;
	}

	@Override
	public List<String> getRowTitleList() {
		// TODO Auto-generated method stub
		return null;
	}
}
