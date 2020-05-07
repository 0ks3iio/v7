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

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.service.DyNightSchedulingService;

/**
 * @author yangsj  2017年11月30日下午4:36:11
 */
@Controller
@RequestMapping("/stuwork/night/import") 
public class DyNightSchedulingImportAction extends DataImportAction {
    
	private Logger logger = Logger.getLogger(DyNightSchedulingImportAction.class);
	@Autowired
	private DyNightSchedulingService dyNightSchedulingService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/scheduling/main")
	public String execute(ModelMap map,String acadyear,String semester) {
		// 业务名称
		map.put("businessName", "晚自习排班");
		// 导入URL 
		map.put("businessUrl", "/stuwork/night/import/scheduling/doimport");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/night/import/scheduling/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/courserecord/nightScheduling/nightSchedulingImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return "dyNightSchedulingImport";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
	   return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入数据为教师的用户名，不是用户真实名称</p>"
				+ "<p>2、注意一个班级对应一个老师</p>"
				+ "<p>3、导入文件中请确认数据是否正确</p>"
				+ "<p>4、日期的格式为(如：2017/12/4)</p>";
	}


	@Override
	@RequestMapping("/scheduling/doimport")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		System.out.println(datas.size());
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		String jsonMsg =dyNightSchedulingService.doImport(getLoginInfo().getUnitId(),datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	
	@Override
	@RequestMapping("/scheduling/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("晚自习排班信息导入", titleMap, sheetName2RecordListMap, response);
	}
    
	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		List<Clazz> classList = Clazz.dt(classRemoteService.findBySchoolId(getLoginInfo().getUnitId()));
		tis.add("日期");
		for (Clazz clazz : classList) {
			tis.add(clazz.getClassNameDynamic());
		}
		return tis;
	}

}
