package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/stuwork/evaluation/stu")
public class DyStuEvaluationImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(DormBedImportAction.class);

	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyStuEvaluationService dyStuEvaluationService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/doImport")
	public String execute(ModelMap map,String acadyear,String semester,String classId) {
		// 业务名称
		map.put("businessName", "学生评语");
		// 导入URL 
		map.put("businessUrl", "/stuwork/evaluation/stu/import");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/evaluation/stu/template?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("classId", classId);
		map.put("className", clazz!=null?clazz.getClassNameDynamic():"");
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/stuEvaluation/evaluationStuImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "evaImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
				+ "<p>2、导入学生信息不能重复</p>"
				+ "<p>3、导入班级名称为年级名称+班级名称</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("学号");
		tis.add("操行等第");
		tis.add("期末评语");
		tis.add("社团工作");
		return tis;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		String classId=request.getParameter("classId");
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		//加入模板数据
		getRecordList(recordList,acadyear,semester,classId);
		if(semester=="1"){
			semester="一";
		}else{
			semester="二";
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		String className="";
		if(clazz!=null){
			className=clazz.getClassNameDynamic();
		}
		ex.exportXLSFile(acadyear+"学年第"+semester+"学期"+className+"学生评语导入", titleMap, sheetName2RecordListMap, response);
	}
	
	private void getRecordList(List<Map<String, String>> recordList,String acadyear,String semester,String classId){
		String unitId = getLoginInfo().getUnitId();
		DyBusinessOptionDto dto=new DyBusinessOptionDto();
		dto.setAcadyear(acadyear);
		dto.setSemester(semester);
		dto.setClassId(classId);
		List<DyStuEvaluation>  evaList=dyStuEvaluationService.findListByUidAndDto(unitId, dto);
		Map<String,DyStuEvaluation> stuEvaMap=EntityUtils.getMap(evaList, "studentId");
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{dto.getClassId()}),new TR<List<Student>>(){});
		if(CollectionUtils.isNotEmpty(stuList)){
			Map<String,String> valueMap = null;
			for(Student stu:stuList){
				DyStuEvaluation eva=stuEvaMap.get(stu.getId());
				valueMap=new HashMap<String, String>();
				valueMap.put("姓名", stu.getStudentName());
				valueMap.put("学号", stu.getStudentCode());
				if(eva==null){
					valueMap.put("操行等第","");
					valueMap.put("期末评语","");
					valueMap.put("社团工作","");
				}else{
					valueMap.put("操行等第",eva.getGrade());
					valueMap.put("期末评语",eva.getRemark());
					valueMap.put("社团工作",eva.getAssociation());
				}
				recordList.add(valueMap);
			}
		}
	}
	
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		System.out.println(datas.size());
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		DyBusinessOptionDto dto=new DyBusinessOptionDto();
		JSONObject json = JSONObject.parseObject(params);
		dto.setAcadyear(json.getString("acadyear"));
		dto.setSemester(json.getString("semester"));
		dto.setClassId(json.getString("classId"));
		
		String jsonMsg =dyStuEvaluationService.doImport(getLoginInfo().getUnitId(),dto, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}


}
