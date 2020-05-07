package net.zdsoft.tutor.data.action.teacher;

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
import net.zdsoft.tutor.data.service.TutorResultService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/tutor/import")
public class TutorImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(TutorImportAction.class);
	@Autowired
	private TutorResultService TutorResultService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map) {
		// 业务名称
		map.put("businessName", "学生导师信息");
		// 导入URL 
		map.put("businessUrl", "/tutor/import/doimport");
		// 导入模板
		map.put("templateDownloadUrl", "/tutor/import/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		
		//map.put("roomType", roomType);
		//map.put("roomTypeName", roomType.equals("1")?"男生寝室":"女生寝室");
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/tutor/teacher/defend/tutorImport.ftl";
	}
	@Override
	public String getObjectName() {
		return "tutorImport";
	};

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
				+ "<p>2、一个学生对应一个导师，一个导师可导多个学生</p>"
				+ "<p>3、如果导入的学生已有导师，会替换掉原来的导师</p>";
	};

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("学号");
		tis.add("学生姓名");
		tis.add("教师编号");
		tis.add("教师姓名");
		return tis;
	}
	@Override
	@RequestMapping("/doimport")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		System.out.println(datas.size());
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		String jsonMsg =TutorResultService.doImport(getLoginInfo().getUnitId(), datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学生导师信息导入", titleMap, sheetName2RecordListMap, response);
		
	}


}
