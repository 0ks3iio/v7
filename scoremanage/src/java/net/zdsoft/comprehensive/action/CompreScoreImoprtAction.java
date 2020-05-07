package net.zdsoft.comprehensive.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/comprehensive/subjects")
public class CompreScoreImoprtAction extends DataImportAction{
	private Logger logger = Logger.getLogger(CompreScoreImoprtAction.class);
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private CompreScoreService compreScoreService;
	@RequestMapping("/compreScore/import/main")
	public String execute(String gradeId, String subjectId, String examId, ModelMap map) {
		// 业务名称
		map.put("businessName", "综合素质");
		// 导入URL 
		map.put("businessUrl", "/comprehensive/subjects/compreScore/import");
		
		map.put("templateDownloadUrl", "/comprehensive/subjects/compreScore/template");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());			
		map.put("businessKey", UuidUtils.generateUuid());
		
		ExamInfo examInfo = examInfoService.findListByIds(new String[]{examId}).get(0);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		map.put("gradeName", grade.getGradeName());
		map.put("examName", examInfo.getExamName());
		map.put("gradeId", gradeId);
		map.put("subjectId", subjectId);
		map.put("examId", examId);
		return "/comprehensive/subjects/exam/comprehensiveImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "compreScoreImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
		        + "<p>2、若导入文件中不填写班级编号，那么班级名称作为教学班名称</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("学号");
		tis.add("班级名称");
		tis.add("班级编号");
		tis.add("科目名称");
		tis.add("科目编号");
		tis.add("卷面分");
		tis.add("实分");
		return tis;
	}

	@Override
	@RequestMapping("/compreScore/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
		String gradeId = jsStr.getString("gradeId");
		String examId = jsStr.getString("examId");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		String operatorId = getLoginInfo().getUserId();
		String jsonMsg = compreScoreService.zHSZImport(getLoginInfo().getUnitId(), examId, gradeId, operatorId, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/compreScore/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("综合素质导入", titleMap, sheetName2RecordListMap, response);	
	}

}
