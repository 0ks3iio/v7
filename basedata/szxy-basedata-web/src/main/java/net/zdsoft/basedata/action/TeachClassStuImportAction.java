package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/basedata/teachclass/studentImport")
public class TeachClassStuImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(TeachClassStuImportAction.class);
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private ClassService classService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ClassHourService classHourService;

	@RequestMapping("/index/page")
	public String importIndex(ModelMap map,String acadyear,String semester){
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "教学班学生");
		// 导入URL 
		map.put("businessUrl", "/basedata/teachclass/studentImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/teachclass/studentImport/template");
        map.put("exportErrorExcelUrl", "/basedata/teachclass/studentImport/exportErrorExcel");
		// 导入对象
		map.put("objectName", getObjectName());
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/teachclass/studentImport/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("acadyear", acadyear);
	    obj.put("semester", semester);
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/basedata/teachClass/teachclassImport.ftl";
	}
	@Override
	public String getObjectName() {
		return "teachClassStudent";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、改变选项后请重新上传模板，同时不要随意修改模板中内容，否则容易导致上传文件与模板的不匹配</p>"
				+"<p>3、导入教学班内信息，只做新增，不进行修改</p>";
		return desc;
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> rowTitle=new ArrayList<String>();
		rowTitle.add("*学号");
		rowTitle.add("*学生姓名");
		rowTitle.add("*教学班名称");
		return rowTitle;
	}

	/**
	 * 导入
	 * @return
	 */
	@RequestMapping("/import")
	@ResponseBody
	@Override
	public String dataImport(String filePath, String params) {
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		//获取上传数据
		List<String[]> rowDatas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
		rowDatas.remove(0);
		//错误数据序列号
        int sequence = 0;
        int totalSize =rowDatas.size();
		List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(rowDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
        }
		if(StringUtils.isBlank(unitId) || StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(0);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="参数丢失，无法保存";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		List<TeachClassStu> insertList=new ArrayList<TeachClassStu>();
		TeachClassStu teachClassStu=null;
		int successCount=0;
		
		//所有未删除教学班
		List<TeachClass> teachClassList = teachClassService.findBySearch(unitId, acadyear, semester, null, null, null);
		if(CollectionUtils.isEmpty(teachClassList)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(0);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位下该学年学期下没有维护教学班";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		
		//取得学生
		Set<String> stuCode=new HashSet<String>();
		for (String[] array : rowDatas) {
			String studentCode=array[0]==null?null:array[0].trim();
			if(StringUtils.isNotBlank(studentCode)){
				stuCode.add(studentCode);
			}
		}
		//学生整体
		Map<String,Set<Student>> studentByCode=new HashMap<String,Set<Student>>();
		Set<String> xzbClassIds=new HashSet<String>();
		//学生已存在的班级 包括行政班
		Map<String,Set<String>> classIdByStudentId=new HashMap<String,Set<String>>();
				
		if(stuCode.size()>0){
			List<Student> studentList = studentService.findBySchIdStudentCodes(unitId, stuCode.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(studentList)){
				for(Student s:studentList){
					
					classIdByStudentId.put(s.getId(), new HashSet<String>());
					classIdByStudentId.get(s.getId()).add(s.getClassId());
					
					xzbClassIds.add(s.getClassId());
					if(StringUtils.isEmpty(s.getStudentCode())){
						continue;
					}
					if(!studentByCode.containsKey(s.getStudentCode())){
						studentByCode.put(s.getStudentCode(), new HashSet<Student>());
					}
					studentByCode.get(s.getStudentCode()).add(s);
				}
			}
		}
		
		//行政班的走教学班的课程
		Map<String,Set<String>> xzbOpenCourseId=new HashMap<String,Set<String>>();
		
		Map<String,Set<String>> classIdByGradeId=new HashMap<String,Set<String>>();
		if(xzbClassIds.size()>0){
			OpenTeachingSearchDto classTeachDto=new OpenTeachingSearchDto();
			classTeachDto.setAcadyear(acadyear);
			classTeachDto.setSemester(semester);
			classTeachDto.setUnitId(unitId);
			classTeachDto.setIsTeaCls(1);
			classTeachDto.setIsDeleted(0);
			classTeachDto.setClassIds(xzbClassIds.toArray(new String[]{}));
			List<ClassTeaching> classTeachingList =classTeachingService.findBySearch(classTeachDto, null);
			if(CollectionUtils.isNotEmpty(classTeachingList)){
				for(ClassTeaching c:classTeachingList){
					if(!xzbOpenCourseId.containsKey(c.getClassId())){
						xzbOpenCourseId.put(c.getClassId(), new HashSet<String>());
					}
					xzbOpenCourseId.get(c.getClassId()).add(c.getSubjectId());
				}
			}
			List<Clazz> clazzList = classService.findListByIdIn(xzbClassIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(clazzList)){
				for(Clazz clazz:clazzList){
					if(!classIdByGradeId.containsKey(clazz.getGradeId())){
						classIdByGradeId.put(clazz.getGradeId(), new HashSet<String>());
					}
					classIdByGradeId.get(clazz.getGradeId()).add(clazz.getId());
				}
			}
			
		}
		
		
		//key:classId,value 周次_星期_上下午_节次
		Map<String,Set<String>> timeByClass=new HashMap<String,Set<String>>();
		//只判断当前时间以及当前时间后
		Map<String, Object> punchCardTimes = makeDto(new Date(), acadyear, semester, unitId);
		String error=null;
		CourseScheduleDto searchTimeDto=null;
		if(punchCardTimes==null || punchCardTimes.size()<=0){
			//无需保存到课程表
		}else{
			if(punchCardTimes.containsKey("error")){
				error=(String)punchCardTimes.get("error");
			}
			if(punchCardTimes.containsKey("searchDto")){
				searchTimeDto=(CourseScheduleDto) punchCardTimes.get("searchDto");
				List<CourseSchedule> list = findByTimes(searchTimeDto);
				if(CollectionUtils.isNotEmpty(list)){
					makeClassTimeMap(list,searchTimeDto,timeByClass);
				}
				
			}
		}
		
		
		Set<String> sameTeachClassName=new HashSet<String>();
		Map<String,String> idByTeachName=new HashMap<String,String>();
		Map<String,TeachClass> teachClassMap=new HashMap<String,TeachClass>();
		Set<String> jxbClasId=new HashSet<String>();
		//关联ids
		Set<String> hourIds=new HashSet<>();
		for(TeachClass t:teachClassList){
			teachClassMap.put(t.getId(), t);
			if(idByTeachName.containsKey(t.getName())){
				sameTeachClassName.add(t.getName());
			}else{
				idByTeachName.put(t.getName(), t.getId());
			}
			jxbClasId.add(t.getId());
			if(StringUtils.isNotBlank(t.getRelaCourseId())) {
				hourIds.add(t.getRelaCourseId());
			}
		}
		Map<String,ClassHour> classHourMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(hourIds)) {
			List<ClassHour> classHourList = classHourService.findListByIdIn(hourIds.toArray(new String[0]));
			classHourMap=EntityUtils.getMap(classHourList,e->e.getId());
		}
		
		//学生已存在的教学班  同科目 key:studentId 
		Map<String,Map<String,Set<String>>> teachClassIdBySubjectStudent=new HashMap<String,Map<String,Set<String>>>();
		List<TeachClassStu> teachStuList = teachClassStuService.findByClassIds(jxbClasId.toArray(new String[]{}));
		if(CollectionUtils.isNotEmpty(teachStuList)){
			for(TeachClassStu st:teachStuList){
				if(!classIdByStudentId.containsKey(st.getStudentId())){
					continue;
				}
				classIdByStudentId.get(st.getStudentId()).add(st.getClassId());
				if(!teachClassIdBySubjectStudent.containsKey(st.getStudentId())){
					teachClassIdBySubjectStudent.put(st.getStudentId(), new HashMap<String,Set<String>>());
				}
				TeachClass tt = teachClassMap.get(st.getClassId());
				Map<String, Set<String>> classBysubject = teachClassIdBySubjectStudent.get(st.getStudentId());
				if(!classBysubject.containsKey(tt.getCourseId())){
					classBysubject.put(tt.getCourseId(), new HashSet<String>());
				}
				classBysubject.get(tt.getCourseId()).add(st.getClassId());
			}
		}
		
		//学生id_teachClassId  防止学生重复导入数据
		Set<String> stuIdClassId=new HashSet<String>();
		//判断
		int i=0;
        for (String[] array : rowDatas) {
        	i++;
        	teachClassStu=new TeachClassStu();
        	String studentCode=array[0]==null?null:array[0].trim();
        	String studentName=array[1]==null?null:array[1].trim();
        	String teachClassName=array[2]==null?null:array[2].trim();
        	//判断不为空的参数
        	if(StringUtils.isBlank(studentCode)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="学号不能为空";
                errorDataList.add(errorData);
                continue;
        	}
        	if(StringUtils.isBlank(studentName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="学生姓名不能为空";
                errorDataList.add(errorData);
                continue;
        	}
        	if(StringUtils.isBlank(teachClassName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="教学班不能为空";
                errorDataList.add(errorData);
                continue;
        	}
        	if(!studentByCode.containsKey(studentCode)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=studentCode;
                errorData[3]="未找到对应学生";
                errorDataList.add(errorData);
                continue;
        	}
        	Set<Student> studentSet = studentByCode.get(studentCode);
        	List<Student> choosestudentList=findByStudentName(studentSet,studentName);
        	if(CollectionUtils.isEmpty(choosestudentList)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=studentName;
                errorData[3]="学生姓名与学号不匹配";
                errorDataList.add(errorData);
                continue;
        	}
        	if(choosestudentList.size()>1){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=studentName;
                errorData[3]="学生姓名与学号不唯一";
                errorDataList.add(errorData);
                continue;
        	}
        	Student student = choosestudentList.get(0);
        	teachClassStu.setStudentId(student.getId());
        	
        	if(sameTeachClassName.contains(teachClassName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=teachClassName;
                errorData[3]="教学班不唯一";
                errorDataList.add(errorData);
                continue;
        	}
        	if(!idByTeachName.containsKey(teachClassName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=teachClassName;
                errorData[3]="未找到对应教学班";
                errorDataList.add(errorData);
                continue;
        	}
    		if(Constant.IS_TRUE_Str.equals(teachClassMap.get(idByTeachName.get(teachClassName)).getIsMerge())){
    			String[] errorData=new String[4];
    			sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
    			errorData[1]="第"+i+"行";
    			errorData[2]=teachClassName;
    			errorData[3]="该教学班为合并班级，不能添加学生";
    			errorDataList.add(errorData);
    			continue;
    		}
        	teachClassStu.setClassId(idByTeachName.get(teachClassName));
        	//验证
        	TeachClass teachClass = teachClassMap.get(teachClassStu.getClassId());
        	if(teachClass.getIsUsing().equals("0")){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=teachClassName;
                errorData[3]="该教学班已经完成教学，不能维护学生数据";
                errorDataList.add(errorData);
                continue;
        	}
        	//学生在教学班面向对象中
        	//必修课需要能够跨年级
        	if(!getLimit()){
        		if(!checkStudentInGradeIds(classIdByGradeId,teachClass.getGradeId(),student.getClassId())){
        			String[] errorData=new String[4];
        			sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
        			errorData[1]="第"+i+"行";
        			errorData[2]=teachClassName;
        			errorData[3]="该学生不在教学班的面向对象中";
        			errorDataList.add(errorData);
        			continue;
        		}
        	}
        	//该学生同类型正在使用教学班
        	if(teachClassIdBySubjectStudent.containsKey(teachClassStu.getStudentId())){
        		Set<String> set = teachClassIdBySubjectStudent.get(teachClassStu.getStudentId()).get(teachClass.getCourseId());
        		if(CollectionUtils.isNotEmpty(set)){
        			if(set.contains(teachClassStu.getClassId())){
        				String[] errorData=new String[4];
                        sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
                        errorData[1]="第"+i+"行";
                        errorData[2]=studentName;
                        errorData[3]="该学生已经存在该教学班";
                        errorDataList.add(errorData);
                        continue;
                      //学生允许排进同个课程的多个教学班
        			}else{
                        if(!getLimit() || Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())){
	        				String[] errorData=new String[4];
	                        sequence++;
                            // errorData[0]=String.valueOf(sequence);
                            errorData[0]=i+"";
	                        errorData[1]="第"+i+"行";
	                        errorData[2]=studentName;
	                        if(!getLimit()){
	                        	errorData[3]="该学生已经存在其他同课程的教学班";
	                        }else{
	                        	errorData[3]="该学生已经存在于其他同课程的用于合并的教学班";
	                        }
	                        errorDataList.add(errorData);
	                        continue;
                        }
        			}
        			
        		}
        		
        	}
        	//学生存在关联同一个走班课程时间的两个班级
			if(StringUtils.isNotBlank(teachClass.getRelaCourseId())) {
				
				//增加行政班限制
				ClassHour classHour = classHourMap.get(teachClass.getRelaCourseId());
				if(classHour==null) {
					//这个正常情况不会出现
				}else {
					if(StringUtils.isNotBlank(classHour.getClassIds())) {
						if(!classHour.getClassIds().contains(student.getClassId())) {
							String[] errorData=new String[4];
	                        sequence++;
	                        // errorData[0]=String.valueOf(sequence);
	                        errorData[0]=i+"";
	                        errorData[1]="第"+i+"行";
	                        errorData[2]=studentName;
	                        errorData[3]="由于关联课程班级限制，不能进入该教学班";
	                        errorDataList.add(errorData);
	                        continue;
						}
					}
				}
				
				boolean isError=false;
				Set<String> set2=new HashSet<>();
				Map<String, Set<String>> map1 = teachClassIdBySubjectStudent.get(teachClassStu.getStudentId());
				if(map1!=null && map1.size()>0) {
					for(Entry<String, Set<String>> item11:map1.entrySet()) {
						set2.addAll(item11.getValue());
					}
					for(String s:set2) {
						if(teachClass.getRelaCourseId().equals(teachClassMap.get(s).getRelaCourseId())) {
	                        isError=true;
	                        break;
						}
					}
				}
				
				if(isError) {
					String[] errorData=new String[4];
                    sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=studentName;
                    
                    errorData[3]="该学生已经存在与其他也是关联同样走班课程的教学班内";
                   
                    errorDataList.add(errorData);
                    continue;
				}
			}
        	//学生有没有课程走班
        	if(xzbOpenCourseId.containsKey(student.getClassId())){
        		if(!xzbOpenCourseId.get(student.getClassId()).contains(teachClass.getCourseId())){
        			String[] errorData=new String[4];
                    sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=studentName;
                    errorData[3]="学生所在行政班未开设该科目或者开设该科目不以教学班方式";
                    errorDataList.add(errorData);
                    continue;
        		}
        	}else{
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=studentName;
                errorData[3]="学生所在行政班未开设该科目或者开设该科目不以教学班方式";
                errorDataList.add(errorData);
                continue;
        	}
        	
//        	if(StringUtils.isNotBlank(teachClass.getRelaCourseId())) {
//        		if(!relaOpenCourseId.get(student.getClassId()).contains(teachClass.getRelaCourseId())) {
//        			String[] errorData=new String[4];
//                    sequence++;
//                    // errorData[0]=String.valueOf(sequence);
//                    errorData[0]=i+"";
//                    errorData[1]="第"+i+"行";
//                    errorData[2]=studentName;
//                    errorData[3]="学生所在行政班未开设该教学班关联课程的走班科目";
//                    errorDataList.add(errorData);
//                    continue;
//        		}
//        		
//        	}
        	//取得时间
    		if(timeByClass.containsKey(teachClass.getId())) {
    			if(StringUtils.isNotBlank(error)){
        			String[] errorData=new String[4];
                    sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=teachClassName;
                    errorData[3]="该教学班需要考勤，无法取得上课时间数据原因："+error;
                    errorDataList.add(errorData);
                    continue;
        		}
        		//走教学班
        		Set<String> classIds = classIdByStudentId.get(student.getId());
        		boolean checkTime=checkStudentTime(classIds,timeByClass,teachClass.getId());
        		if(checkTime){
        			String[] errorData=new String[4];
                    sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=studentName;
                    errorData[3]="上课时间重复";
                    errorDataList.add(errorData);
                    continue;
        		}
    		}else {
    			//没有时间 不用验证冲突
    			if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())) {
    				//用于合并
    				if(timeByClass.containsKey(teachClass.getParentId())) {
    					if(StringUtils.isNotBlank(error)){
    	        			String[] errorData=new String[4];
    	                    sequence++;
    	                    // errorData[0]=String.valueOf(sequence);
    	                    errorData[0]=i+"";
    	                    errorData[1]="第"+i+"行";
    	                    errorData[2]=teachClassName;
    	                    errorData[3]="无法取得上课时间数据原因："+error;
    	                    errorDataList.add(errorData);
    	                    continue;
    	        		}
    	        		//走教学班
    	        		Set<String> classIds = classIdByStudentId.get(student.getId());
    	        		boolean checkTime=checkStudentTime(classIds,timeByClass,teachClass.getParentId());
    	        		if(checkTime){
    	        			String[] errorData=new String[4];
    	                    sequence++;
    	                    // errorData[0]=String.valueOf(sequence);
    	                    errorData[0]=i+"";
    	                    errorData[1]="第"+i+"行";
    	                    errorData[2]=studentName;
    	                    errorData[3]="上课时间重复";
    	                    errorDataList.add(errorData);
    	                    continue;
    	        		}
    				}
    			}
    			
    		}
        	String sameKey=teachClassStu.getStudentId()+"_"+teachClassStu.getClassId();
        	if(stuIdClassId.contains(sameKey)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=studentName+"("+teachClassName+")";
                errorData[3]="这行数据跟之前数据重复";
                errorDataList.add(errorData);
                continue;
        	}
        	teachClassStu.setCreationTime(new Date());
        	teachClassStu.setModifyTime(new Date());
        	teachClassStu.setIsDeleted(0);
        	teachClassStu.setId(UuidUtils.generateUuid());
        	insertList.add(teachClassStu);
        	stuIdClassId.add(sameKey);
        	if(!teachClassIdBySubjectStudent.containsKey(teachClassStu.getStudentId())) {
        		teachClassIdBySubjectStudent.put(teachClassStu.getStudentId(), new HashMap<>());
        	}
        	if(!teachClassIdBySubjectStudent.get(teachClassStu.getStudentId()).containsKey(teachClass.getCourseId())) {
        		teachClassIdBySubjectStudent.get(teachClassStu.getStudentId()).put(teachClass.getCourseId(), new HashSet<>());
        	}
        	teachClassIdBySubjectStudent.get(teachClassStu.getStudentId()).get(teachClass.getCourseId()).add(teachClass.getId());
        	
        	successCount++;
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题行固定
            sheet.createFreezePane(0, 1);
            
            List<String> titleList = getRowTitleList();
            titleList.add("错误数据");
            titleList.add("错误原因");

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int j=0;j<titleList.size();j++){
            	sheet.setColumnWidth(j, 20 * 256);
                HSSFCell cell = rowTitle.createCell(j);
                cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
            }

            for(int j=0;j<errorDataList.size();j++){
                HSSFRow row = sheet.createRow(j+1);
                String[] datasDetail = rowDatas.get(Integer.parseInt(errorDataList.get(j)[0]) - 1);
                for (int k=0;k<titleList.size();k++) {
                    HSSFCell cell = row.createCell(k);
                    if (k<titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[k]));
                    } else if (k==titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[2]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }
        
        if(CollectionUtils.isNotEmpty(insertList)){
			try{
				teachClassStuService.saveAllEntitys(insertList.toArray(new TeachClassStu[]{}));
			}catch(Exception e){
				e.printStackTrace();
				return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
			}
		}
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
		return result;
		
	}
	/**
	 * classId是否在教学班面向对象gradeIds中
	 * @param classIdByGradeId
	 * @param gradeId
	 * @param classId
	 * @return
	 */
	private boolean checkStudentInGradeIds(
			Map<String, Set<String>> classIdByGradeId, String gradeIds,
			String classId) {
		if(classIdByGradeId.size()<=0){
			return false;
		}
		String[] arr =null;
		if(gradeIds.indexOf(",")>=0){
			arr = gradeIds.split(",");
		}else{
			arr=new String[]{gradeIds};
		}
		for(String g:arr){
			if(!classIdByGradeId.containsKey(g)){
				continue;
			}
			if(classIdByGradeId.get(g).contains(classId)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 组装timeByClass
	 * @param list
	 * @param searchTimeDto
	 * @param timeByClass
	 */
	private void makeClassTimeMap(List<CourseSchedule> list, 
			CourseScheduleDto searchTimeDto,Map<String, Set<String>> timeByClass) {
		if(CollectionUtils.isNotEmpty(list)){
			for(CourseSchedule c:list){
				if(c.getWeekOfWorktime()==searchTimeDto.getWeekOfWorktime1() && c.getDayOfWeek()<searchTimeDto.getDayOfWeek1()){
					continue;
				}
				if(c.getWeekOfWorktime()==searchTimeDto.getWeekOfWorktime2() && c.getDayOfWeek()>searchTimeDto.getDayOfWeek2()){
					continue;
				}
				String time=c.getWeekOfWorktime()+"_"+c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
				if(!timeByClass.containsKey(c.getClassId())){
					timeByClass.put(c.getClassId(), new HashSet<String>());
				}
				timeByClass.get(c.getClassId()).add(time);
				
			}
		}
	}
	/**
	 * 需要查询的周次时间段 星期段 如果节假日有报错 返回key:error  成功 返回：searchDto  返回null  学年学期已经结束  不用删除  无需进出课表 只需要保存教学班信息
	 * @param nowDate
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @return
	 */
	private Map<String,Object> makeDto(Date nowDate,String acadyear,String semester,String unitId){
		Map<String,Object> objMap=new HashMap<String,Object>();
		CourseScheduleDto searchDto =null;
		Semester chooseSemest=semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),unitId);
		if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterEnd())>0){
			//学年学期已经结束  不用删除  无需进出课表
			return objMap;
		}else{
			DateInfo endDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSemest.getSemesterEnd());
			if(endDateInfo == null){
				objMap.put("error", "保存失败,未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
				return objMap;
			}
			int weekOfWorktime1=0;
			int weekOfWorktime2=endDateInfo.getWeek();
			
			int dayOfWeek1=0;//开始星期
			int dayOfWeek2=endDateInfo.getWeekday();//结束星期
			if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterBegin())<0){
				//学期未开始
				weekOfWorktime1=0;
				DateInfo startDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSemest.getSemesterBegin());
				if(startDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				dayOfWeek1=startDateInfo.getWeekday();
			}else{
				DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
				if(nowDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				weekOfWorktime1=nowDateInfo.getWeek();
				dayOfWeek1=nowDateInfo.getWeekday();
			}
			//删除原来未上课的数据
			searchDto=new CourseScheduleDto();
			searchDto.setAcadyear(acadyear);
			searchDto.setSemester(Integer.parseInt(semester));
			searchDto.setDayOfWeek1(dayOfWeek1);
			searchDto.setDayOfWeek2(dayOfWeek2);
			searchDto.setWeekOfWorktime1(weekOfWorktime1);
			searchDto.setWeekOfWorktime2(weekOfWorktime2);
			searchDto.setSchoolId(unitId);
			objMap.put("searchDto", searchDto);
		}
		return objMap;
	}
	/**
	 * 查询课程表
	 * @param searchDto
	 * @return
	 */
	private List<CourseSchedule> findByTimes(CourseScheduleDto searchDto){
		CourseScheduleDto dto=new CourseScheduleDto();
		dto.setSchoolId(searchDto.getSchoolId());
		dto.setAcadyear(searchDto.getAcadyear());
		dto.setSemester(searchDto.getSemester());
		dto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
		dto.setWeekOfWorktime2(searchDto.getWeekOfWorktime2());
		List<CourseSchedule> list=courseScheduleService.getByCourseScheduleDto(dto);
		return list;
	}
	/**
	 * 直接判断课程表时间是否重复  暂时不根据教学班那边定义的时间节点
	 * @param classIds
	 * @param timeByClass
	 * @param teachClassId
	 * @return
	 */
	private boolean checkStudentTime(Set<String> classIds,
			Map<String, Set<String>> timeByClass, String teachClassId) {
		if(!timeByClass.containsKey(teachClassId)){
			return false;
		}
		if(CollectionUtils.isEmpty(classIds)){
			return false;
		}
		if(CollectionUtils.isEmpty(timeByClass.get(teachClassId))){
			return false;
		}
		Set<String> checkTime = timeByClass.get(teachClassId);
		for(String s:classIds){
			if(!timeByClass.containsKey(s)){
				continue;
			}
			Set<String> set = timeByClass.get(s);
			if(CollectionUtils.isEmpty(set)){
				continue;
			}
			//set与checkTime有交集
			Collection interSet = CollectionUtils.intersection(checkTime, set);
			if(interSet!=null && interSet.size()>0){
				return true;
			}
		}
		return false;
	}
	/**
	 * 找到匹配的学生
	 * @param studentSet
	 * @param studentName
	 * @return
	 */
	private List<Student> findByStudentName(Set<Student> studentSet, String studentName) {
		if(CollectionUtils.isEmpty(studentSet)){
			return new ArrayList<Student>();
		}
		List<Student> returnList = new ArrayList<Student>();
		boolean flag=false;
		for(Student s:studentSet){
			if(s.getStudentName().equals(studentName)){
				if(!flag){
					returnList.add(s);
				}else{
					//重复
					returnList.add(s);
					return returnList;
				}
			}
		}
		return returnList;
	}
	/**
	 * 下载课程导入模板
	 * @param type
	 * @param response
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> titleList = getRowTitleList();//表头
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
	    //标题行固定
	    sheet.createFreezePane(0, 1);
	    
        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        for(int j=0;j<size;j++){
        	sheet.setColumnWidth(j, 20 * 256);
        	HSSFCell cell = titleRow.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		
		String fileName = "教学班学生导入";
		
		ExportUtils.outputData(workbook, fileName, response);
		
	}

    @RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    /**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo){
		logger.info("模板校验中......");
		validRowStartNo = "0";
		try{
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	 }
	private boolean getLimit(){
		String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
		if(Constant.IS_FALSE_Str.equals(limit)){
			return true;
		}
		return false;
	}
}
