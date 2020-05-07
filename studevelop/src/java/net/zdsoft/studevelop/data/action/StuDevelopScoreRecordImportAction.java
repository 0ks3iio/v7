package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopProjectService;
import net.zdsoft.studevelop.data.service.StuDevelopScoreRecordService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopScoreRecordImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(StuDevelopScoreRecordImportAction.class);
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService  classRemoteService;
	@Autowired
	private StuDevelopProjectService stuDevelopProjectService;
	@Autowired
	private StuDevelopScoreRecordService stuDevelopScoreRecordService;
	@Autowired
	private StuDevelopSubjectService stuDevelopSubjectService;
	@Autowired
	private StuDevelopCateGoryService stuDevelopCateGoryService;
	@RequestMapping("/scoreRecord/import/main")
	public String execute(ModelMap map,String acadyear,String semester, String studentId) {
		// 业务名称
		map.put("businessName", "成绩");
		// 导入URL 
		map.put("businessUrl", "/studevelop/scoreRecord/import");
		// 导入模板
		//String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
		map.put("templateDownloadUrl", "/studevelop/scoreRecord/template?studentId="+studentId+"&acadyear="+acadyear+"&semester="+semester);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
			
		map.put("businessKey", UuidUtils.generateUuid());
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("studentId", studentId);
		return "/studevelop/record/stuDevelopScoreRecordImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "stuDevelopScoreRecordImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确；</p>"
		        + "<p>2、当学科分为多个学科类别时，由于态度习惯仅对学科显示，故学科的态度习惯导入结果，<font color='red'>仅以表格中最后一行学科类别的态度习惯导入值为准</font>；</p>"
		        + "<p>3、成绩登记时，如果 平时、期末成绩只输入一个，则报告单成绩合并一格显示； 如果 平时、期末成绩都录入，则报告单成绩都显示；</p>";
		        
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		return tis;
	}

	@Override
	@RequestMapping("/scoreRecord/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
		String acadyear = jsStr.getString("acadyear");
		String semester = jsStr.getString("semester");
		String studentId = jsStr.getString("studentId");
		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
		Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		String gradeId = cls.getGradeId();
		List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectService.stuDevelopProjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				6+stuDevelopProjectList.size());
		if(datas.size() == 0){
			Json importResultJson=new Json();
			importResultJson.put("totalCount", 0);
			importResultJson.put("successCount", 0);
			importResultJson.put("errorCount", 0);
			return importResultJson.toJSONString();
		}
		String jsonMsg = stuDevelopScoreRecordService.doImport(getLoginInfo().getUnitId(), datas, acadyear, semester, gradeId);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/scoreRecord/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		String studentId = request.getParameter("studentId");
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
		Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		String className = cls.getClassNameDynamic();
		String gradeId = cls.getGradeId();
		List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectService.stuDevelopProjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
		List<StuDevelopSubject> stuDevelopSubjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
		Set<String> subjectIdSet = new HashSet<String>();
		for(StuDevelopSubject item : stuDevelopSubjectList){
			subjectIdSet.add(item.getId());
		}
		List<StuDevelopCateGory> stuDevelopCateGoryList;
		if(org.apache.commons.collections.CollectionUtils.isNotEmpty(subjectIdSet)){
			stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subjectIdSet.toArray(new String[0]));
		}else{
			stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		}			
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
		
		for(Student stu : studentList){
			for(StuDevelopSubject sub : stuDevelopSubjectList){
				int t = 0;
				for(StuDevelopCateGory gory : stuDevelopCateGoryList){
					if(sub.getId().equals(gory.getSubjectId())){
						Map<String,String> sMap = new HashMap<String,String>();
						sMap.put("姓名", stu.getStudentName());
						sMap.put("学号", stu.getStudentCode());
						sMap.put("班级", className);
						sMap.put("班内编号", stu.getClassInnerCode());
						sMap.put("学科", sub.getName());
						sMap.put("学科类别", gory.getCategoryName());
						recordList.add(sMap);
						t++;
					}
				}
				if(t==0){
					Map<String,String> sMap = new HashMap<String,String>();
					sMap.put("姓名", stu.getStudentName());
					sMap.put("学号", stu.getStudentCode());
					sMap.put("班级", className);
					sMap.put("班内编号", stu.getClassInnerCode());
					sMap.put("学科", sub.getName());
					recordList.add(sMap);
				}
			}
		}			
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("学号");
		tis.add("班级");
		tis.add("班内编号");
		tis.add("学科");
		tis.add("学科类别");
		for(StuDevelopProject pro : stuDevelopProjectList){
			tis.add(pro.getProjectName());
		}
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("成绩导入", titleMap, sheetName2RecordListMap, response);		
	}

}
