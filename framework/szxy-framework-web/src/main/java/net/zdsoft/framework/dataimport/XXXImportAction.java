package net.zdsoft.framework.dataimport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//路径地址跟着自己的业务走
@Controller
@RequestMapping("/common/xxx")
public class XXXImportAction extends DataImportAction {
	private Logger logger = Logger.getLogger(XXXImportAction.class);

	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map) {
		// 业务名称
		map.put("businessName", "学生信息");
		// 导入URL
		map.put("businessUrl", "/common/xxx/import");
		// 导入模板
		map.put("templateDownloadUrl", "/common/xxx/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());

		map.put("businessKey", UuidUtils.generateUuid());

		// 如果导入文件中前面有说明性文字的 这里需要传一个有效数据开始行（列名那一行）
		// 如果列名在第一行的就不需要传
		//map.put("validRowStartNo", 1);
		// 模板校验
		map.put("validateUrl", "/common/xxx/validate");

		return "/fw/commonftl/dataImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "student";
	};

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入时，会根据班级记学科确认数据是否存在，如果系统中已经存在，则会覆盖导入列的信息，如果不存在，则会新增记录</p>"
				+ "<p>2、导入文件中请确认数值；类型的小数位数及对应的年级班级，否则可能会导致数据不对</p>"
				+ "<p>3、导入班级名称为年级名称+班级名称</p>";
	};

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("班级");
		tis.add("学生");
		tis.add("学号");
		tis.add("性别");
		return tis;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath, 4);
		System.out.println(datas.size());
		// TODO 业务处理模块　具体的业务在这里处理
		// 自己的处理接口　数据范围格式如下
		// 业务类中返回给我的一个json格式的数据
		List<String[]> errorDataList = new ArrayList<String[]>();
		String[] errorData = new String[4];
		errorData[0] = "1";
		errorData[1] = "小王";
		errorData[2] = "33010000000000000";
		errorData[3] = "身份证号已经在系统中存在";
		errorDataList.add(errorData);

		errorData = new String[4];
		errorData[0] = "1";
		errorData[1] = "小王";
		errorData[2] = "33010000000000000";
		errorData[3] = "身份证号已经在系统中存在";
		errorDataList.add(errorData);

		errorData = new String[4];
		errorData[0] = "1";
		errorData[1] = "小王";
		errorData[2] = "33010000000000000";
		errorData[3] = "身份证号已经在系统中存在";
		errorDataList.add(errorData);

		errorData = new String[4];
		errorData[0] = "1";
		errorData[1] = "小王";
		errorData[2] = "33010000000000000";
		errorData[3] = "身份证号已经在系统中存在";
		errorDataList.add(errorData);
		Json importResultJson = new Json();
		importResultJson.put("totalCount", 100);
		importResultJson.put("successCount", 80);
		importResultJson.put("errorCount", 20);
		importResultJson.put("errorData", errorDataList);

		return importResultJson.toJSONString();
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),
				new ArrayList<Map<String, String>>());
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("XXXX", titleMap, sheetName2RecordListMap, response);
	}

    /**
     * 为基类的exportErrorExcel(request, response)方法提供RequestMapping
     * @param request
     * @param response
     */
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        super.exportErrorExcel(request, response);
    }

    @RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
				Integer.valueOf(validRowStartNo),4);
		String msg = templateValidate(datas, getRowTitleList());
		if (StringUtils.isNotBlank(msg)) {
			File file = new File(filePath);
			file.delete();
		}
		return msg;
	}

}
