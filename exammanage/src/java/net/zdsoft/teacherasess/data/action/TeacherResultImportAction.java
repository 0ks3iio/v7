package net.zdsoft.teacherasess.data.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teacherasess.data.dto.TeaConvertDto;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertResult;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertResultService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/teacherasess/convert/resultImport")
public class TeacherResultImportAction extends DataImportAction{
	@Autowired
	private TeacherasessConvertResultService teacherasessConvertResultService;
	@Autowired
	private TeacherasessConvertService teacherasessConvertService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private ClassRemoteService classService;
	
	private Logger logger = Logger.getLogger(TeacherResultImportAction.class);
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(TeaConvertDto dto,String subjectId,ModelMap map){
		String convertId=dto.getConvertId();
		String gradeId=dto.getGradeId();
		String unitId=getLoginInfo().getUnitId();
		// 业务名称
		map.put("businessName", "成绩录入");
		// 导入URL 
		map.put("businessUrl", "/teacherasess/convert/resultImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/teacherasess/convert/resultImport/template?convertId="+dto.getConvertId()+"&subjectId="+subjectId);
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/teacherasess/convert/resultImport/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		//导入参数
    	JSONObject obj=new JSONObject();
		obj.put("convertId", convertId);
	    obj.put("subjectId", subjectId);
	    obj.put("unitId", unitId);
	    obj.put("gradeId", gradeId);
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("subjectId", subjectId);
	    map.put("convertId", convertId);
	    //map.put("description", description);
		return "/teacherasess/convert/resultImport.ftl";
	}
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,HttpServletResponse response) {
		//是否需要显示总评成绩
		String convertId = request.getParameter("convertId");
		String subjectId = request.getParameter("subjectId");
		List<TeacherasessConvertResult> resultList = teacherasessConvertResultService.findListByConvertId(convertId,subjectId,null);
		
        ArrayList<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
        Map<String,String> inMap = null;
        if(CollectionUtils.isNotEmpty(resultList)){
        	for(TeacherasessConvertResult result:resultList){
        		inMap=new HashMap<>();
        		inMap.put("*学号", result.getStudentCode());
        		inMap.put("*姓名",result.getStudentName());
        		inMap.put("*行政班",result.getClassName());
        		BigDecimal  b=new  BigDecimal(result.getScore());  
        		inMap.put("*折算分",b.setScale(1,  BigDecimal.ROUND_HALF_UP).toString());
        		inMap.put("*年级排名",result.getRank()+"");
        		recordList.add(inMap);
        	}
        }
        
		List<String> titleList = getRowTitleList();//表头
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile("折算分方案结果录入", titleMap, sheetName2RecordListMap, response);
	}
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath,String validRowStartNo) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	@Override
	public String getObjectName() {
		return "teacherresultImport";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("*学号");
		tis.add("*姓名");
		tis.add("*行政班");
		tis.add("*折算分");
		tis.add("*年级排名");
		return tis;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String convertId = (String) performance.get("convertId");
		String subjectId = (String) performance.get("subjectId");
		String gradeId = (String) performance.get("gradeId");
		String unitId = (String) performance.get("unitId");
		
		//每一行数据   表头列名：0
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,1,getRowTitleList().size());
		
		 //错误数据序列号
        int sequence =1;
        int totalSize =datas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(datas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="学号";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(datas.size(),0,0,errorDataList);
        }
        if(!subjectId.equals(BaseConstants.ZERO_GUID)){
        	String[] errorData=new String[4];
	        sequence++;
	        errorData[0]=String.valueOf(sequence);
	        errorData[1]="学号";
	        errorData[2]="";
	        errorData[3]="只能导入总分数据";
	        errorDataList.add(errorData);
	        return result(datas.size()-1,0,0,errorDataList);
        }
        TeacherasessConvert convert = teacherasessConvertService.findOne(convertId);
        if(convert==null){
        	String[] errorData=new String[4];
	        sequence++;
	        errorData[0]=String.valueOf(sequence);
	        errorData[1]="学号";
	        errorData[2]="";
	        errorData[3]="该方案已删除";
	        errorDataList.add(errorData);
	        return result(datas.size()-1,0,0,errorDataList);
        }
        List<Student> stuList=SUtils.dt(studentService.findByGradeId(gradeId), new TR<List<Student>>(){});
        Map<String,Student> studentCodeMap=EntityUtils.getMap(stuList, Student::getStudentCode);
        List<Clazz> classList=SUtils.dt(classService.findListByIds(EntityUtils.getSet(stuList, Student::getClassId).toArray(new String[]{})),new TR<List<Clazz>>(){});
        Map<String,String> classNameMap=EntityUtils.getMap(classList, Clazz::getId,Clazz::getClassNameDynamic);
        
        Pattern p = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$"); 
        Pattern p1 = Pattern.compile("^[0-9]*[1-9][0-9]*$"); 
		List<String> studentIdsList = new ArrayList<String>();
		List<TeacherasessConvertResult> resultList = new ArrayList<>();
		TeacherasessConvertResult result=null;
        int successCount=0;
        for(String[] arr:datas){
        	String[] errorData=new String[4];
            sequence++;
            
            if(StringUtils.isBlank(arr[0])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="学号不能为空";
                errorDataList.add(errorData);
                continue;
            }
            if(StringUtils.isBlank(arr[1])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="姓名";
                errorData[2]="";
                errorData[3]="姓名不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[2])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="行政班";
                errorData[2]="";
                errorData[3]="行政班不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[3])){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="折算分";
            	errorData[2]="";
            	errorData[3]="折算分不能为空";
            	errorDataList.add(errorData);
            	continue;
            }
            if(StringUtils.isBlank(arr[4])){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="年级排名";
            	errorData[2]="";
            	errorData[3]="年级排名不能为空";
            	errorDataList.add(errorData);
            	continue;
            }
            Student student = studentCodeMap.get(arr[0]);
            if (student == null) {
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="学号";
            	errorData[2]="";
            	errorData[3]="该学号不存在或该学号对应的学生不是该年级的学生";
            	errorDataList.add(errorData);
            	continue;
            }
            String studentName=student.getStudentName();
            if (!StringUtils.trim(arr[1]).equals(studentName)) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="该学号对应的学生姓名错误,应为:"+studentName;
                errorDataList.add(errorData);
                continue;
            }
            
            if (studentIdsList.contains(student.getId())) {
            	errorData[0]=String.valueOf(sequence);
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="请勿重复导入成绩";
				errorDataList.add(errorData);
				continue;
            }
            String className=classNameMap.get(student.getClassId());
            if (!StringUtils.trim(arr[2]).equals(className)) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="该学号对应的班级姓名错误,应为:"+className;
                errorDataList.add(errorData);
                continue;
            }
			Matcher m = p.matcher(arr[3]);
			if (!m.matches()) {
				//分数有效性
				errorData[0]=String.valueOf(sequence);
				errorData[1]="折算分";
				errorData[2]="";
				errorData[3]="格式不正确(最多3位整数，2位小数)!";
				errorDataList.add(errorData);
				continue;
			}
			
			if (Float.parseFloat(arr[3]) < 0) {
				//分数有效性
				errorData[0]=String.valueOf(sequence);
				errorData[1]="折算分";
				errorData[2]="";
				errorData[3]="折算分成绩为不能小于0";
				errorDataList.add(errorData);
				continue;
			}
			Matcher m1 = p1.matcher(arr[4]);
			if (!m1.matches()) {
				//分数有效性
				errorData[0]=String.valueOf(sequence);
				errorData[1]="年级排名";
				errorData[2]="";
				errorData[3]="格式不正确(只能正整数)!";
				errorDataList.add(errorData);
				continue;
			}
			
            studentIdsList.add(student.getId());
            successCount++;
            
            result=new TeacherasessConvertResult();
            result.setId(UuidUtils.generateUuid());
            result.setUnitId(unitId);
            result.setConvertId(convertId);
            result.setSubjectId(subjectId);
            result.setStudentName(studentName);
            result.setStudentId(student.getId());
            result.setStudentCode(student.getStudentCode());
            result.setClassId(student.getClassId());
            result.setClassName(className);
            result.setScore(NumberUtils.toFloat(arr[3]));
            result.setRank(NumberUtils.toInt(arr[4]));
            resultList.add(result);
        }
        //先删除后增加新的导入
        teacherasessConvertResultService.deleteByConvertId(convertId, subjectId);
        teacherasessConvertResultService.saveAll(resultList.toArray(new TeacherasessConvertResult[0]));
        
        int errorCount = totalSize - successCount;
        String resultStr = result(totalSize,successCount,errorCount,errorDataList);
        return resultStr;
	}
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	 }
}
