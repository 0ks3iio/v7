package net.zdsoft.scoremanage.data.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.FiltrationService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/scoremanage/scoreImport")
public class ScoreInfoImportAction extends DataImportAction{

	private Logger logger = Logger.getLogger(ScoreInfoImportAction.class);
	
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private FiltrationService filtrationService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@ControllerInfo("进入查询头")
	@RequestMapping("/head")
	public String index(String examId, String classIdSearch,String subType,ModelMap map) {
		List<SubjectInfo> infoList=subjectInfoService.findByExamIdClassId(examId,classIdSearch);
		map.put("infoList", infoList);
		try {
			subType = URLDecoder.decode(subType,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("subType", subType);
		return "/scoremanage/scoreInfo/scoreIndexImport.ftl";
	}
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(String examId,String classType,String classIdSearch,String subjectId,String gradeCode,ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
	    String examType = examInfoService.findOne(examId).getExamType();
		// 业务名称
		map.put("businessName", "成绩录入");
		// 导入URL 
		map.put("businessUrl", "/scoremanage/scoreImport/import/1");
		if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examType)){
			map.put("businessUrl", "/scoremanage/scoreImport/import/2");
		}
		// 导入模板
		map.put("templateDownloadUrl", "/scoremanage/scoreImport/template?examId="+examId
				+"&classType="+classType+"&classIdSearch="+classIdSearch+"&subjectId="+subjectId);
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/scoremanage/scoreImport/validate/1?subjectId="+subjectId);
		if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examType)){
			map.put("validateUrl", "/scoremanage/scoreImport/validate/2?subjectId="+subjectId);
		}
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
		obj.put("examId", examId);
	    obj.put("unitId", unitId);
	    obj.put("classType", classType);
	    obj.put("classIdSearch", classIdSearch);
	    obj.put("subjectId", subjectId);
	    obj.put("gradeCode", gradeCode);
		obj.put("ownerId", loginInfo.getOwnerId());
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/scoremanage/scoreInfo/scoreImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "scoreImport";
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
		tis.add("*考试成绩");
		tis.add("*状态");
		return tis;
	}
	public List<String> getRowTitleList2() {
		List<String> tis = new ArrayList<String>();
		tis.add("*学号");
		tis.add("*姓名");
		tis.add("*考试成绩");
		tis.add("*状态");
		tis.add("*总评成绩");
		return tis;
	}

//	@Override
	@RequestMapping("/import/{flag}")
	@ResponseBody
	public String dataImport(String filePath, String params,@PathVariable("flag")String flag) {
		logger.info("业务数据处理中......");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String examId = (String) performance.get("examId");
		String unitId = (String) performance.get("unitId");
		String classType = (String) performance.get("classType");
		String classIdSearch = (String) performance.get("classIdSearch");
		String subjectId = (String) performance.get("subjectId");
		String gradeCode = (String) performance.get("gradeCode");
		String ownerId = (String) performance.get("ownerId");
		
		ExamInfo exam = examInfoService.findOne(examId);
		
		//每一行数据   表头列名：0
		String subjectCode = courseRemoteService.findOneObjectById(subjectId).getSubjectCode();
		boolean needGeneral = false;
		if("2".equals(flag) && !BaseConstants.HW_CODE_KS.equals(subjectCode) && !BaseConstants.HW_CODE_XZ.equals(subjectCode)) {
			needGeneral = true;
		}
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,1,needGeneral?getRowTitleList2().size():getRowTitleList().size());
		
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
        
        if(exam==null){
			String[] errorData=new String[4];
	        sequence++;
	        errorData[0]=String.valueOf(sequence);
	        errorData[1]="学号";
	        errorData[2]="";
	        errorData[3]="该考试不存在";
	        errorDataList.add(errorData);
	        return result(datas.size()-1,0,0,errorDataList);
		}
        
        String acadyear = exam.getAcadyear();
        String semester = exam.getSemester();
        
        List<SubjectInfo> subjectInfoList = subjectInfoService.findByExamIdAndCourseId(examId,subjectId);
        subjectInfoList = subjectInfoList.stream().filter(e->StringUtils.isNotBlank(e.getRangeType())&&e.getRangeType().equals(gradeCode)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(subjectInfoList)) {
        	String[] errorData=new String[4];
	        sequence++;
	        errorData[0]=String.valueOf(sequence);
	        errorData[1]="学号";
	        errorData[2]="";
	        errorData[3]="该考试科目已删除";
	        errorDataList.add(errorData);
	        return result(datas.size()-1,0,0,errorDataList);
        }
        
        SubjectInfo subjectInfo = subjectInfoList.get(0);
        
        //班级下学生
        List<Student> stuList=new ArrayList<Student>();
        Map<String,Student> studentMap = new HashMap<String,Student>();
        //不排考学生
        Map<String, String> filterMap = filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE1);     
        if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
        	stuList = SUtils.dt(studentService.findByClassIds(new String[]{classIdSearch}), new TR<List<Student>>(){});
        }else{
        	stuList = SUtils.dt(studentService.findByTeachClassIds(new String[]{classIdSearch}), new TR<List<Student>>(){});
        }
        for (Student student : stuList) {
        	studentMap.put(student.getStudentCode(), student);
        }
        Set<String> stuId=new HashSet<String>();
        Set<String> subjectIds=new HashSet<String>();
        subjectIds.add(subjectId);
        //查询当前考试科目下 该班级已有学生信息
		Map<String, ScoreInfo> scoremap=scoreInfoService.findByExamIdAndUnitIdAndClassId(examId, unitId, classIdSearch, null,stuId,subjectIds);
        if(scoremap==null){
        	scoremap=new HashMap<String, ScoreInfo>();
        }
        
		List<McodeDetail> gradeTypeList = new ArrayList<McodeDetail>();
		if (subjectInfo.getInputType().equals("G") && StringUtils.isNotBlank(subjectInfo.getGradeType())) {
			gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(subjectInfo.getGradeType()),new TypeReference<List<McodeDetail>>(){});
		}
		List<McodeDetail> scoreStatusList =  SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-CJLX"),new TypeReference<List<McodeDetail>>(){});
		Pattern p = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$"); 
		List<String> studentIdsList = new ArrayList<String>();
		List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
		ScoreInfo newinfo=null;
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

            boolean join = true;
			if(StringUtils.isBlank(arr[3])){
				errorData[0]=String.valueOf(sequence);
				errorData[1]="状态";
				errorData[2]="";
				errorData[3]="状态不能为空";
				errorDataList.add(errorData);
				continue;
			} else {
				if (StringUtils.equals("本次未考", arr[3])) {
					join = false;
				}
			}
            
            if(join && StringUtils.isBlank(arr[2])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="考试成绩";
                errorData[2]="";
                errorData[3]="考试成绩不能为空";
                errorDataList.add(errorData);
                continue;
            }

            //总评成绩不能为空
            if(join && needGeneral && StringUtils.isBlank(arr[4])){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="总评成绩";
            	errorData[2]="";
            	errorData[3]="总评成绩不能为空";
            	errorDataList.add(errorData);
            	continue;
            }
            
            Student student = studentMap.get(arr[0]);
            if (student == null) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="该学号不存在或该学号对应的学生不是该班级的学生";
                errorDataList.add(errorData);
                continue;
            }
            
            if (!arr[1].equals(student.getStudentName())) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="该学号对应的学生姓名错误";
                errorDataList.add(errorData);
                continue;
            }
            
            if (filterMap.containsKey(student.getId())) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="该学生不参与排考";
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

			String score = null;
			String scoreStatus = null;
			if (join) {
				score = "";
				if (subjectInfo.getInputType().equals("G")) {
					boolean isGradeType = false;
					for (McodeDetail detail : gradeTypeList) {
						if (arr[2].equals(detail.getMcodeContent())) {
							score = detail.getThisId();
							isGradeType = true;
							break;
						}
					}

					if (!isGradeType) {
						//等第有效性
						errorData[0]=String.valueOf(sequence);
						errorData[1]="考试成绩";
						errorData[2]="";
						errorData[3]="请填写有效的等第";
						errorDataList.add(errorData);
						continue;
					}
				} else {
					Matcher m = p.matcher(arr[2]);
					if (!m.matches()) {
						//分数有效性
						errorData[0]=String.valueOf(sequence);
						errorData[1]="考试成绩";
						errorData[2]="";
						errorData[3]="格式不正确(最多3位整数，2位小数)!";
						errorDataList.add(errorData);
						continue;
					}

					if (Float.parseFloat(arr[2]) < 0 || Float.parseFloat(arr[2]) > subjectInfo.getFullScore()) {
						//分数有效性
						errorData[0]=String.valueOf(sequence);
						errorData[1]="考试成绩";
						errorData[2]="";
						errorData[3]="考试成绩为0-"+subjectInfo.getFullScore()+"之间";
						errorDataList.add(errorData);
						continue;
					}
					score = arr[2];
				}

				scoreStatus = "";
				boolean isScoreStatus = false;
				for (McodeDetail detail : scoreStatusList) {
					if (arr[3].equals(detail.getMcodeContent())) {
						scoreStatus = detail.getThisId();
						isScoreStatus = true;
						break;
					}
				}

				if (!isScoreStatus) {
					//考试状态有效性
					errorData[0]=String.valueOf(sequence);
					errorData[1]="考试状态";
					errorData[2]="";
					errorData[3]="请填写有效的考试状态";
					errorDataList.add(errorData);
					continue;
				}

				//总评成绩 格式 范围验证
				if(needGeneral && !p.matcher(arr[4]).matches()){
					//总评成绩 分数有效性
					errorData[0]=String.valueOf(sequence);
					errorData[1]="总评成绩";
					errorData[2]="";
					errorData[3]="格式不正确(最多3位整数，2位小数)!";
					errorDataList.add(errorData);
					continue;
				}
				if (needGeneral && (Float.parseFloat(arr[4]) < 0 || Float.parseFloat(arr[4]) > subjectInfo.getFullScore())){
					//分数范围
					errorData[0]=String.valueOf(sequence);
					errorData[1]="总评成绩";
					errorData[2]="";
					errorData[3]="成绩为0-"+subjectInfo.getFullScore()+"之间";
					errorDataList.add(errorData);
					continue;
				}
			} else {
				arr[2] = "";
				if (needGeneral) {
					arr[4] = "";
				}
				scoreStatus = "3";
			}

			studentIdsList.add(student.getId());
            successCount++;
            
            if (scoremap.containsKey(student.getId()+"_"+subjectId)) {
            	//修改
				newinfo=scoremap.get(student.getId()+"_"+subjectId);
				newinfo.setOperatorId(ownerId);
				newinfo.setModifyTime(new Date());
            } else {
            	newinfo = new ScoreInfo();
            	newinfo.setId(UuidUtils.generateUuid());
            	newinfo.setAcadyear(acadyear);
            	newinfo.setSemester(semester);
            	newinfo.setExamId(examId);
            	newinfo.setSubjectId(subjectId);
            	newinfo.setCreationTime(new Date());
            	newinfo.setModifyTime(new Date());
            	newinfo.setOperatorId(ownerId);
            	newinfo.setUnitId(unitId);
            }
            newinfo.setInputType(subjectInfo.getInputType());
            if (classType.equals("1")) {
            	newinfo.setClassId(classIdSearch);
            } else {
            	newinfo.setClassId(student.getClassId());
            	newinfo.setTeachClassId(classIdSearch);
            }
            newinfo.setStudentId(student.getId());
            newinfo.setScoreStatus(scoreStatus);
            newinfo.setScore(score);
            //wyy.设置总评成绩
            if(needGeneral){
            	newinfo.setToScore(arr[4]);
            }
            
            if (subjectInfo.getInputType().equals("G")) {
            	newinfo.setGradeType(subjectInfo.getGradeType());
            }
            scoreInfoList.add(newinfo);
        }
        
        if (CollectionUtils.isNotEmpty(scoreInfoList)) {
        	scoreInfoService.saveAllEntitys(scoreInfoList.toArray(new ScoreInfo[]{}));
		}
		
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList);
        return result;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		//是否需要显示总评成绩
		String examId = request.getParameter("examId");
		String examType = examInfoService.findOne(examId).getExamType();
		String classType = request.getParameter("classType");
		String classIdSearch = request.getParameter("classIdSearch");
		String subjectId = request.getParameter("subjectId");
		String unitId = getLoginInfo().getUnitId();
		
        Map<String, String> filterMap = filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE1);     
        List<Student> studentList = null;
        if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
        	studentList = SUtils.dt(studentService.findByClassIds(new String[]{classIdSearch}), new TR<List<Student>>(){});
        }else{
        	studentList = SUtils.dt(studentService.findByTeachClassIds(new String[]{classIdSearch}), new TR<List<Student>>(){});
        }

        Set<String> xzbClasIds = EntityUtils.getSet(studentList, Student::getClassId);
        List<Clazz> clazzList = classRemoteService.findListObjectByIds(xzbClasIds.toArray(new String[0]));
        Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId,Clazz::getClassName);
        
        Comparator<Student> c = (s1,s2)->{
        	String cn = classNameMap.get(s1.getClassId());
        	String cn2 = classNameMap.get(s2.getClassId());
        	if(!StringUtils.equals(cn, cn2)) {
				String cns1 = cn.replaceAll("[0-9]", "");
				String cns2 = cn2.replaceAll("[0-9]", "");
				if(!StringUtils.equals(cns1, cns2)) {
					return cn.compareTo(cn2);
				}
				int c1 = NumberUtils.toInt(cn.replaceAll("[^0-9]", ""));
				int c2 = NumberUtils.toInt(cn2.replaceAll("[^0-9]", ""));
				return c1-c2;
			}
        	if(!StringUtils.equals(s1.getClassId(), s2.getClassId())) {
				return s1.getClassId().compareTo(s2.getClassId());
			}
        	
			if(StringUtils.isNotBlank(s1.getClassInnerCode()) && StringUtils.isNotBlank(s2.getClassInnerCode())){
				return NumberUtils.toInt(s1.getClassInnerCode()) - NumberUtils.toInt(s2.getClassInnerCode());
			} else if(StringUtils.isNotBlank(s1.getClassInnerCode())) {
				return 1;
			} else if(StringUtils.isNotBlank(s2.getClassInnerCode())) {
				return 0;
			}
			
			if(StringUtils.isNotBlank(s1.getStudentCode()) && StringUtils.isNotBlank(s2.getStudentCode())) {
				return s1.getStudentCode().compareTo(s2.getStudentCode());
			}
			
			return 0;
        };
        
		Collections.sort(studentList, c );
        
        ArrayList<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
        Map<String,String> inMap = null;
        for (Student student : studentList) {
        	if(filterMap.containsKey(student.getId())){
        		continue;
        	}
        	inMap = new HashMap<String, String>();
        	inMap.put("*学号", student.getStudentCode());
        	inMap.put("*姓名", student.getStudentName());
        	inMap.put("*状态", "正常");
        	recordList.add(inMap);
        }
		
        Course course = courseRemoteService.findOneObjectById(subjectId);
        String subjectCode = course.getSubjectCode();
		List<String> titleList = getRowTitleList();//表头
		if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examType) && !BaseConstants.HW_CODE_KS.equals(subjectCode) && !BaseConstants.HW_CODE_XZ.equals(subjectCode)){
			titleList = getRowTitleList2();//表头
		}
		
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile("成绩录入", titleMap, sheetName2RecordListMap, response);
	}
	
	@RequestMapping("/validate/{flag}")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo,@PathVariable("flag") String flag) {
		logger.info("模板校验中......");
		String subjectId = getRequest().getParameter("subjectId");
		String subjectCode = courseRemoteService.findOneObjectById(subjectId).getSubjectCode();
		boolean needGeneral = false;
		if("2".equals(flag) && !BaseConstants.HW_CODE_KS.equals(subjectCode) && !BaseConstants.HW_CODE_XZ.equals(subjectCode)) {
			needGeneral = true;
		}
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),needGeneral?getRowTitleList2().size():getRowTitleList().size());
			return templateValidate(datas, needGeneral?getRowTitleList2():getRowTitleList());
			
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

	@Override
	public String dataImport(String filePath, String params) {
		// TODO Auto-generated method stub
		return null;
	}
}
