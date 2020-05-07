package net.zdsoft.stuwork.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.service.DyStudentRewardPointService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/stuwork/studentReward/studentRewardPointImport")
public class DyStudentRewardPointImportAction extends DataImportAction{
private Logger logger = Logger.getLogger(DyStudentRewardPointImportAction.class);
	
	@Autowired
	private DyStudentRewardPointService dyStudentRewardPointService;
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map,String classesType) {
		if(StringUtils.isBlank(classesType)) {
			classesType = StuworkConstants.STUDENT_REWARD_GAME;
		}
		// 业务名称
		if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)){
			map.put("businessName", "学科竞赛");
		}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)){
			map.put("businessName", "校内奖励");
		}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)){
			map.put("businessName", "节日活动奖励");
		}else if(StuworkConstants.STUDENT_REWARD_OTHER.equals(classesType)){
			map.put("businessName", "其他奖励");
		}
		
		// 导入URL 
		map.put("businessUrl", "/stuwork/studentReward/studentRewardPointImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/studentReward/studentRewardPointImport/template?classesType="+classesType);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		map.put("businessKey", UuidUtils.generateUuid());
		
		map.put("classesType",classesType);
		if(!classesType.equals(StuworkConstants.STUDENT_REWARD_FESTIVAL)) {
			List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
			if(CollectionUtils.isEmpty(acadyearList)){
				return errorFtl(map,"学年学期不存在");
			}
			int semester=1;
			String acadyear = "";
			if(StringUtils.isBlank(acadyear)){
				Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()), Semester.class);
				if(se == null){
					se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
					semester = se.getSemester();
					acadyear = se.getAcadyear();
				}else{
					semester = se.getSemester();
					acadyear = se.getAcadyear();
				}
			}
			
			map.put("acadyearList", acadyearList);
			map.put("semester", semester);
			map.put("acadyear", acadyear);
			
		}
		

		return "/stuwork/studentReward/studentRewardPointImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "studentRewardPointImport";
	};

	@Override
	public String getDescription() {
		String str =  "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
				+ "<p>2、如果节假日导入，类型请填写：文化节 ，年份请填写：2018 </p>"
				+ "<p>3、导入的项目存在默认分数的情况下，导入文件中填写的分数无效</p>"
				+ "<p>3、导入类型若选择“覆盖导入”，则会先<span style='color:red;font-weigth:bold;'>清空</span>对应类型、学年学期/年份的数据再导入</p>";
		return str;
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
	
	public List<String> getRowTitleList2(String classesType) {
		List<String> tis = new ArrayList<String>();
		tis.add("学生姓名");
		tis.add("学号");
		if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)) {
			tis.add("类型");
			tis.add("项目名称");
			tis.add("级别");
			tis.add("奖级");
			tis.add("分值");
			tis.add("备注");
		}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)) {
			tis.add("类型");
			tis.add("项目名称");
			tis.add("分值");
			tis.add("备注");
		}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
			tis.add("类型");
			tis.add("项目名称");
			tis.add("奖级");
			tis.add("年份");
			tis.add("分值");
			tis.add("备注");
		}else if(StuworkConstants.STUDENT_REWARD_OTHER.equals(classesType)) {
			tis.add("项目名称");
		}
		
		
		return tis;
	}

	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String classesType=request.getParameter("classesType");
		List<String> tis = getRowTitleList2(classesType);
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)){
			ex.exportXLSFile("学科竞赛导入", titleMap, sheetName2RecordListMap, response);
		}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)){
			ex.exportXLSFile("校内奖励导入", titleMap, sheetName2RecordListMap, response);
		}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)){
			ex.exportXLSFile("节日活动奖励导入", titleMap, sheetName2RecordListMap, response);
		}else if(StuworkConstants.STUDENT_REWARD_OTHER.equals(classesType)){
			ex.exportXLSFile("其他奖励导入", titleMap, sheetName2RecordListMap, response);
		}
		
	}
	
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("学生奖励业务数据处理中......");
		JSONObject json = JSONObject.parseObject(params);
		String classesType=json.get("classesType").toString();	
		String semester= "";
		String acadyear="";
		if(!classesType.equals(StuworkConstants.STUDENT_REWARD_FESTIVAL)) {
			semester=json.get("semester").toString();
			acadyear=json.get("acadyear").toString();
		}
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList2(classesType).size());
		if(datas.size() == 0){
			Json importResultJson=new Json();
			importResultJson.put("totalCount", 0);
			importResultJson.put("successCount", 0);
			importResultJson.put("errorCount", 0);
			return importResultJson.toJSONString();
		}
		String ctv = json.getString("coverType");
		String jsonMsg = dyStudentRewardPointService.saveImport(datas,getLoginInfo().getUnitId(),classesType,acadyear,semester, ctv);
		logger.info("导入结束......");
		
//		System.out.println(datas.size());

//		// 业务处理模块　具体的业务在这里处理
//		//自己的处理接口　数据范围格式如下
//		//业务类中返回给我的一个json格式的数据
//		logger.info("导入结束......");
		
		return jsonMsg;
	}

}
