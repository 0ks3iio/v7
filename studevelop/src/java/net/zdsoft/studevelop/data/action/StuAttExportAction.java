package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;
import net.zdsoft.studevelop.data.service.StuCheckAttendanceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/studevelop/attImport") 
public class StuAttExportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(StuAttExportAction.class);
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuCheckAttendanceService stuCheckAttendanceService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@RequestMapping("/main")
    @ControllerInfo(value = "进入导入头")
    public String showIndex(ModelMap map,String acadyear,String semester,String classId) {
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "考勤登记");
		// 导入URL 
		map.put("businessUrl", "/studevelop/attImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/studevelop/attImport/template?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/studevelop/attImport/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("acadyear", acadyear);
	    obj.put("semester", semester);
	    obj.put("classId", classId);
		obj.put("ownerId", loginInfo.getOwnerId());
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/studevelop/record/export/stuCheckAttExprot.ftl";
	}
	@Override
	public String getObjectName() {
		return "stuAttExport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、改变选项后请重新上传模板</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("*学号");
		tis.add("*姓名");
		tis.add("事假天数");
		tis.add("病假天数");
		tis.add("旷课节数");
		tis.add("迟到节数");
		tis.add("早退节数");
		tis.add("备注");
		return tis;
	}
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		String classId = (String) performance.get("classId");
		
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
            errorData[1]="姓名";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(datas.size(),0,0,errorDataList);
        }
        //班级下学生
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
		Set<String> stuIdSet = new HashSet<>();//成功导入的学生id
		
		Map<String,Student> stuCodeMap=new HashMap<>(); 
		if(CollectionUtils.isNotEmpty(stuList)){
			stuCodeMap=stuList.stream().collect(Collectors.toMap(Student::getStudentCode, Function.identity(),(k1,k2)->k1));
		}
        
		Pattern p = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1})?$"); 
		Pattern p1 = Pattern.compile("^(0|[1-9]\\d{0,2})?$"); 
		List<String> studentIdsList = new ArrayList<String>();
		List<StuCheckAttendance> checkAttList = new ArrayList<>();
		StuCheckAttendance checkAtt=null;
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
            Student student=stuCodeMap.get(arr[0]);
            if (student == null) {
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="学号";
            	errorData[2]=arr[0];
            	errorData[3]="该学号不存在，或该学号对应的学生不是该班级的学生";
            	errorDataList.add(errorData);
            	continue;
            }
            
            if (!arr[1].equals(student.getStudentName())) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]=arr[1];
                errorData[3]="该学号对应的学生姓名错误";
                errorDataList.add(errorData);
                continue;
            }
            if (studentIdsList.contains(student.getId())) {
            	errorData[0]=String.valueOf(sequence);
				errorData[1]="学号";
				errorData[2]=arr[1];
				errorData[3]="请勿重复导入成绩";
				errorDataList.add(errorData);
				continue;
            }
			Matcher m = p.matcher(arr[2]);
			if (!m.matches()) {
				errorData[0]=String.valueOf(sequence);
				errorData[1]="事假天数";
				errorData[2]=arr[2];
				errorData[3]="格式不正确(最多3位整数，1位小数)!";
				errorDataList.add(errorData);
				continue;
			}
			m = p.matcher(arr[3]);
			if (!m.matches()) {
				errorData[0]=String.valueOf(sequence);
				errorData[1]="病假天数";
				errorData[2]=arr[3];
				errorData[3]="格式不正确(最多3位整数，1位小数)!";
				errorDataList.add(errorData);
				continue;
			}
			m = p1.matcher(arr[4]);
			if (!m.matches()) {
				errorData[0]=String.valueOf(sequence);
				errorData[1]="旷课节数";
				errorData[2]=arr[4];
				errorData[3]="格式不正确(最多3位正整数)!";
				errorDataList.add(errorData);
				continue;
			}
			m = p1.matcher(arr[5]);
			if (!m.matches()) {
				errorData[0]=String.valueOf(sequence);
				errorData[1]="迟到节数";
				errorData[2]=arr[5];
				errorData[3]="格式不正确(最多3位正整数)!";
				errorDataList.add(errorData);
				continue;
			}
			m = p1.matcher(arr[6]);
			if (!m.matches()) {
				errorData[0]=String.valueOf(sequence);
				errorData[1]="早退节数";
				errorData[2]=arr[6];
				errorData[3]="格式不正确(最多3位正整数)!";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(arr[7]) && arr[7].length()>200){
				errorData[0]=String.valueOf(sequence);
				errorData[1]="备注";
				errorData[2]=arr[7];
				errorData[3]="不能超过200个字符!";
				errorDataList.add(errorData);
				continue;
			}
            studentIdsList.add(student.getId());
            successCount++;
            //保存数据
            checkAtt=new StuCheckAttendance();
            checkAtt.setId(UuidUtils.generateUuid());
            checkAtt.setStudentId(student.getId());
            checkAtt.setAcadyear(acadyear);
            checkAtt.setSemester(semester);
	        checkAtt.setBusinessVacation(Double.valueOf(arr[2]));
	        checkAtt.setIllnessVacation(Double.valueOf(arr[3]));
	        checkAtt.setWasteVacation(Integer.valueOf(arr[4]));
	        checkAtt.setLate(Integer.valueOf(arr[5]));
	        checkAtt.setLeaveEarly(Integer.valueOf(arr[6]));
	        checkAtt.setRemark(arr[7]);
	        checkAttList.add(checkAtt);
	        stuIdSet.add(student.getId());
        }
        stuCheckAttendanceService.deleteByStuIds(acadyear, semester, stuIdSet.toArray(new String[]{}));
        stuCheckAttendanceService.saveAll(checkAttList.toArray(new StuCheckAttendance[]{}));
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList);
        return result;
	}
	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String classId = request.getParameter("classId");
		ArrayList<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
		StringBuilder sb=new StringBuilder(acadyear+"年");
		sb.append("1".equals(semester)?"第一学期":"第二学期");
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		sb.append(clazz.getClassNameDynamic());
		sb.append("考勤登记导入");
	    //获取班级下的所有学生 
  		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
  		if(CollectionUtils.isNotEmpty(stuList)){
  			Set<String> stuIdSet = stuList.stream().map(Student::getId).collect(Collectors.toSet());
  			Map<String,StuCheckAttendance> checkAttMap=new HashMap<>();
  			//获取学生考勤数据
  			List<StuCheckAttendance> checkAttendanceslist = stuCheckAttendanceService.findListByCls(acadyear,semester,stuIdSet.toArray(new String[0]));
  			if(CollectionUtils.isNotEmpty(checkAttendanceslist)){
  				checkAttMap=checkAttendanceslist.stream().collect(Collectors.toMap(StuCheckAttendance::getStudentId, Function.identity(),(k2,k1)->k1));
  			}
  			Map<String,String> inMap = null;
  			StuCheckAttendance checkAtt=null;
  			for (Student student : stuList) {
  				inMap = new HashMap<String, String>();
  				inMap.put("*学号", student.getStudentCode());
				inMap.put("*姓名", student.getStudentName());
				checkAtt=checkAttMap.get(student.getId());
				if(checkAtt==null){
					checkAtt=new StuCheckAttendance();
				}
				inMap.put("事假天数", checkAtt.getBusinessVacation()+"");
				inMap.put("病假天数", checkAtt.getIllnessVacation()+"");
				inMap.put("旷课节数", checkAtt.getWasteVacation()+"");
				inMap.put("迟到节数", checkAtt.getLate()+"");
				inMap.put("早退节数", checkAtt.getLeaveEarly()+"");
				inMap.put("备注", checkAtt.getRemark()==null?"":checkAtt.getRemark());
  				recordList.add(inMap);
  			}
  		}
		List<String> titleList = getRowTitleList();//表头
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile(sb.toString(), titleMap, sheetName2RecordListMap, response);
	}
	
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo) {
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
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	 }

}
