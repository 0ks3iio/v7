package net.zdsoft.stuwork.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleUserService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;
//路径地址跟着自己的业务走
@Controller
@RequestMapping("/stuwork/roleTeacher")
public class RoleTeacherImportAction extends DataImportAction {
	private Logger logger = Logger.getLogger(RoleTeacherImportAction.class);
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private DyWeekCheckRoleUserService dyWeekCheckRoleUserService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map) {
		// 业务名称
		map.put("businessName", "值周干部");
		// 导入URL 
		map.put("businessUrl", "/stuwork/roleTeacher/import");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/roleTeacher/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		map.put("semester", se.getSemester());
		map.put("acadyear", se.getAcadyear());
		
		map.put("businessKey", UuidUtils.generateUuid());
		return "/stuwork/weekCheck/roleUser/roleTeachImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "roleTeacher";
	};

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入时，只能导入当前学年学期的数据</p>"
				+ "<p>2、导入文件中请确认数据是否正确；维护为空的数据当天不安排教师，若不想更改当天原有数据则可以删除该行</p>"
				+ "<p>3、导入数据为教师职工姓名</p>";
	};

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("日期");
		tis.add("周次");
		tis.add("星期");
		School sch = SUtils.dc(schoolRemoteService.findOneById(getLoginInfo().getUnitId()), School.class);
		String[] sections = sch.getSections().split(",");
		Map<String, McodeDetail>  mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"), new TypeReference<Map<String, McodeDetail> >(){});
		for(String section : sections){
			tis.add(mcodeDetailMap.get(section).getMcodeContent());
		}
		return tis;
	}
	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		//加入模板数据
		getRecordList(recordList);
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("roleTeacherImport", titleMap, sheetName2RecordListMap, response);
	}
	
	private void getRecordList(List<Map<String, String>> recordList){
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		String acadyear = se.getAcadyear();
		String semester = se.getSemester()+"";
		String unitId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, NumberUtils.toInt(semester)), new TR<List<DateInfo>>(){});
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,String> valueMap = null;
		//组装数据
		for(DateInfo dateInfo : dateInfoList){
			valueMap = new HashMap<String, String>();
			valueMap.put("日期", sdf.format(dateInfo.getInfoDate()));
			valueMap.put("周次", dateInfo.getWeek()+"");
			valueMap.put("星期", dateInfo.getWeekday()+"");
			recordList.add(valueMap);
		}
	}
	
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		School school = SUtils.dc(schoolRemoteService.findOneById(this.getLoginInfo().getUnitId()), School.class);
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		String acadyear = se.getAcadyear();
		String semester = se.getSemester()+"";
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		String jsonMsg = dyWeekCheckRoleUserService.roleTeacherImport(this.getLoginInfo().getUnitId(),acadyear, semester, school.getSections(), datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

}
