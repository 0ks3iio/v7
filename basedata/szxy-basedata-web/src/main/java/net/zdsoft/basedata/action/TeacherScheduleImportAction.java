package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.*;

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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/basedata/teacher/courseScheduleImport")
public class TeacherScheduleImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(TeacherScheduleImportAction.class);
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ClassService classService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TeachPlanService teachPlanService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private TeachPlanExService teachPlanExService;
	@Autowired
	private ClassTeachingExService classTeachingExService;
	
	private static int SUBJECT_SIZE=60;//课程数暂取60
	
	@RequestMapping("/index/page")
	public String importIndex(ModelMap map,String acadyear,String semester, String gradeId, String isCover){
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "任课老师");
		// 导入URL 
		map.put("businessUrl", "/basedata/teacher/courseScheduleImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/teacher/courseScheduleImport/template");
        map.put("exportErrorExcelUrl", "/basedata/teacher/courseScheduleImport/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/teacher/courseScheduleImport/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("acadyear", acadyear);
	    obj.put("semester", semester);
	    obj.put("gradeId", gradeId);
	    obj.put("isCover", isCover);
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/basedata/classTeaching/courseScheduleImport.ftl";
	}
	@Override
	public String getObjectName() {
		return "teacherScheduleImportAction";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、<font style='color:red;'>是否覆盖</font>默认为否，即只导入本学期内当前时间之后的课程表；选择是后，会同时改变本学期当前时间之前已上完的课程表</p>"
				+ "<p>3、改变选项后请重新上传模板</p>";
		return desc;
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> rowTitle=new ArrayList<String>();
		rowTitle.add("班级");
		rowTitle.add("此处填写科目名称，如语文");
		rowTitle.add("此处填写科目名称，如数学");
		return rowTitle;
	}

	/**
	 * 导入数据
	 */
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		//获取参数
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		String gradeId = (String) performance.get("gradeId");
		String isCover = (String) performance.get("isCover");
		
		List<String[]> rowDatas = ExcelUtils.readExcelIgnoreDesc(filePath,SUBJECT_SIZE);
		//错误数据序列号
		int sequence = 0;
		List<String[]> errorDataList=new ArrayList<String[]>();
		
		//判断基本数据
        if(CollectionUtils.isEmpty(rowDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
        }
		if(StringUtils.isBlank(unitId) || StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="参数丢失，无法保存";
            errorDataList.add(errorData);
            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
		//判断时间段，节假日是否维护
		Map<String, Object> map = makeDto(unitId, acadyear, semester, isCover);
		if(map.containsKey("error")){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]=(String) map.get("error");
            errorDataList.add(errorData);
            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
		CourseScheduleDto searchDto = (CourseScheduleDto) map.get("searchDto");
		// 判断课程库是否有课程
		/*List<String> unitIds = new ArrayList<String>();
		Unit unit = unitService.findTopUnit(getLoginInfo().getUnitId());
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);*/
		List<Course> courseList = courseService.findByTopUnitAndUnitId(unitId);
		if(CollectionUtils.isEmpty(courseList)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位课程库中没有课程";
            errorDataList.add(errorData);
            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
		//判断教师信息
		List<Teacher> teacherList = teacherService.findByUnitId(unitId);
		if(CollectionUtils.isEmpty(teacherList)){
			String[] errorData=new String[4];
			sequence++;
			errorData[0]=String.valueOf(sequence);
			errorData[1]="";
			errorData[2]="";
			errorData[3]="该单位下没有维护教师信息";
			errorDataList.add(errorData);
			return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
		
		//基础数据：教师
		List<String> sameTeacherNames = new ArrayList<String>();
		Map<String, String> teacherIdByName = new HashMap<String, String>();
		for(Teacher t:teacherList){
			if(teacherIdByName.containsKey(t.getTeacherName())){
				sameTeacherNames.add(t.getTeacherName());
			}else{
				teacherIdByName.put(t.getTeacherName(), t.getId());
			}
		}
		
		//基础数据：科目
//		Map<String, Course> subjectBySubjectName = EntityUtils.getMap(courseList,Course::getSubjectName);
		Map<String, List<Course>> subjectsBySubjectName =EntityUtils.getListMap(courseList,Course::getSubjectName, e->e);
		
		Map<String,Course> courseMap=EntityUtils.getMap(courseList, Course::getId);
				
		
		//基础数据：班级
		List<Clazz> classList = classService.findByGradeId(unitId, gradeId, null);
	    Set<String> classIds = EntityUtils.getSet(classList, Clazz::getId);
	    Map<String, Clazz> classByClassName = EntityUtils.getMap(classList, Clazz::getClassNameDynamic); 

		//判断首行课程
		String[] titleArr = rowDatas.get(0);
		int titleSize = 0;
		for (int i = titleArr.length-1; i >0 ; i--) {
			String subjectName = titleArr[i] == null ? "" : titleArr[i].trim();
			if ("错误数据".equals(subjectName) || "错误原因".equals(subjectName)) {
			    subjectName = "";
            }
			if(StringUtils.isNotBlank(subjectName)){
				titleSize = i+1;
				break;
			}
		}
		
		if(titleSize - 1 == 0){
			String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="课程名称为空";
            errorDataList.add(errorData);
            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
		List<String> titleList = new ArrayList<String>();
		titleList.add(titleArr[0]);
		//验证这些科目是否数据库存在
		for (int i = 1; i < titleSize; i++) {
			String subjectName = titleArr[i]==null?"":titleArr[i].trim();
			if(StringUtils.isBlank(subjectName)){
				String[] errorData=new String[4];
	            sequence++;
	            errorData[0]=String.valueOf(sequence);
	            errorData[1]="";
	            errorData[2]="";
	            errorData[3]="存在课程名称为空";
	            errorDataList.add(errorData);
	            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
			}else{
				List<Course> cList = subjectsBySubjectName.get(subjectName);
				if(CollectionUtils.isEmpty(cList)) {
					String[] errorData=new String[4];
	                sequence++;
	                errorData[0]=String.valueOf(sequence);
	                errorData[1]="";
	                errorData[2]=subjectName;
	                errorData[3]="课程名称中"+subjectName+"未找到对应科目";
	                errorDataList.add(errorData);
	                return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
				}else {
					//是不是都是选修课
					boolean f=false;
					for(Course c:cList) {
						if(!BaseConstants.SUBJECT_TYPE_XX.equals(c.getType())){
							f=true;
							break;
						}
					}
					if(!f) {
						String[] errorData=new String[4];
		                sequence++;
		                errorData[0]=String.valueOf(sequence);
		                errorData[1]="";
		                errorData[2]=subjectName;
		                errorData[3]="课程"+subjectName+"为选修课";
		                errorDataList.add(errorData);
		                return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
					}
					f=false;
					for(Course c:cList) {
						if(!BaseConstants.VIRTUAL_COURSE_TYPE.equals(c.getCourseTypeId())){
							f=true;
							break;
						}
					}
					if(!f) {
						String[] errorData=new String[4];
		                sequence++;
		                errorData[0]=String.valueOf(sequence);
		                errorData[1]="";
		                errorData[2]=subjectName;
		                errorData[3]="课程"+subjectName+"为走班课程";
		                errorDataList.add(errorData);
		                return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
					}
				}
				
//				if(!subjectBySubjectName.containsKey(subjectName)){
//					String[] errorData=new String[4];
//	                sequence++;
//	                errorData[0]=String.valueOf(sequence);
//	                errorData[1]="第1行";
//	                errorData[2]=subjectName;
//	                errorData[3]="课程名称中"+subjectName+"未找到对应科目";
//	                errorDataList.add(errorData);
//	                return result(rowDatas.size(),0,rowDatas.size(),errorDataList);
//				}else{
//					Course course = subjectBySubjectName.get(subjectName);
//					if(!Constant.IS_TRUE_Str.equals(course.getType())){
//						String[] errorData=new String[4];
//		                sequence++;
//		                errorData[0]=String.valueOf(sequence);
//		                errorData[1]="第1行";
//		                errorData[2]=subjectName;
//		                errorData[3]="课程"+subjectName+"为选修课";
//		                errorDataList.add(errorData);
//		                return result(rowDatas.size(),0,rowDatas.size(),errorDataList);
//					}
//				}
			}
			titleList.add(subjectName);
		}
		Set<String> titleSet = new HashSet<String>();
		titleSet.addAll(titleList);
		if(titleSet.size()!=titleList.size()){
			String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="课程中存在重复课程";
            errorDataList.add(errorData);
            return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
//		List<String> titleSubjects = new ArrayList<String>();
//		for (int j=1;j<titleList.size();j++) {
//			titleSubjects.add(subjectBySubjectName.get(titleList.get(j)).getId());
//		}
		
		//更新数据：所有课程开设数据
		//List<ClassTeaching> classTeachingList = classTeachingService.findClassTeachingList(acadyear, semester, classIds.toArray(new String[0]),unitId,Constant.IS_DELETED_FALSE,titleSubjects.toArray(new String[0]),true);
		List<ClassTeaching> classTeachingList = classTeachingService.findClassTeachingList(acadyear, semester, classIds.toArray(new String[0]),unitId,Constant.IS_DELETED_FALSE,null,true);
		Map<String,List<ClassTeaching>> classTeachingMap = new HashMap<String, List<ClassTeaching>>();//key:classId
		Map<String,List<ClassTeaching>> classIdSubjectNameMap=new HashMap<>();//key:classId+subjectName
		
		if(CollectionUtils.isEmpty(classTeachingList)){
			String[] errorData=new String[4];
			sequence++;
			errorData[0]=String.valueOf(sequence);
			errorData[1]="";
			errorData[2]="";
			errorData[3]="请先维护班级课程开设";
			errorDataList.add(errorData);
			return result(rowDatas.size()-1,0,rowDatas.size()-1,errorDataList,"");
		}
		
		//开课计划中存在的科目
		Set<String> allSubjects=new HashSet<>();
		for (ClassTeaching classTeaching : classTeachingList) {
			//去除以教学班形式的开设计划
			if(classTeaching.getIsTeaCls()==Constant.IS_TRUE) {
				continue;
			}
			if(!classTeachingMap.containsKey(classTeaching.getClassId())){
				classTeachingMap.put(classTeaching.getClassId(), new ArrayList<ClassTeaching>());
			}
			classTeachingMap.get(classTeaching.getClassId()).add(classTeaching);
			if(!courseMap.containsKey(classTeaching.getSubjectId())) {
				//原来的开设计划科目在课程库中差不到 一般不会出现
				continue;
			}
			String key=classTeaching.getClassId()+courseMap.get(classTeaching.getSubjectId()).getSubjectName();
			if(!classIdSubjectNameMap.containsKey(key)) {
				classIdSubjectNameMap.put(key, new ArrayList<ClassTeaching>());
			}
			classIdSubjectNameMap.get(key).add(classTeaching);
			allSubjects.add(classTeaching.getSubjectId());
		}
		
		//更新数据：中间表
		List<TeachPlan> teachPlanList = teachPlanService.findTeachPlanListByClassIdsAndSubjectIds(acadyear,Integer.parseInt(semester),classIds.toArray(new String[0]),allSubjects.toArray(new String[0]));
		Map<String,List<TeachPlan>> teachPlanMap = new HashMap<String, List<TeachPlan>>();
		for (TeachPlan teachPlan : teachPlanList) {
			if(!teachPlanMap.containsKey(teachPlan.getClassId())){
				teachPlanMap.put(teachPlan.getClassId(), new ArrayList<TeachPlan>());
			}
			teachPlanMap.get(teachPlan.getClassId()).add(teachPlan);
		}
		
		//更新数据：课程表
		int startWeek = searchDto.getWeekOfWorktime1();
	  	int startDay = searchDto.getDayOfWeek1();
	  	List<CourseSchedule> courseScheduleList = courseScheduleService.findCourseScheduleListByClassIdsAndSubjectIds(unitId, acadyear, Integer.parseInt(semester), classIds.toArray(new String[0]), allSubjects.toArray(new String[0]));
	  	List<String> courseScheduleIdList = new ArrayList<String>();
		Map<String,List<CourseSchedule>> courseScheduleMap = new HashMap<String, List<CourseSchedule>>();
		for (CourseSchedule cs : courseScheduleList) {
			//排除之前的课程表
			if(cs.getWeekOfWorktime()<startWeek || (cs.getWeekOfWorktime()==startWeek && cs.getDayOfWeek()<=startDay)){
				continue;
			}
			if(!courseScheduleMap.containsKey(cs.getClassId())){
				courseScheduleMap.put(cs.getClassId(), new ArrayList<CourseSchedule>());
			}
			courseScheduleMap.get(cs.getClassId()).add(cs);
			courseScheduleIdList.add(cs.getId());
		}
		
		//删除数据：助教老师
		List<TeachPlanEx> teachPlanExList = teachPlanExService.findByPrimaryTableIdIn(courseScheduleIdList.toArray(new String[0]));
		Map<String, List<TeachPlanEx>> teachPlanExMap = EntityUtils.getListMap(teachPlanExList, TeachPlanEx::getPrimaryTableId, Function.identity());
		List<ClassTeachingEx> deleteClassTeachingExList = new ArrayList<ClassTeachingEx>();
		
		//新增数据：助教老师
		List<ClassTeachingEx> newClassTeachingExList = new ArrayList<ClassTeachingEx>();
		ClassTeachingEx classTeachingEx = null;
		
		//删除数据：助教老师课表
		List<TeachPlanEx> deleteTeachPlanExList = new ArrayList<TeachPlanEx>();
		
		//新增数据：助教老师课表
		List<TeachPlanEx> newTeachPlanExList = new ArrayList<TeachPlanEx>();
		TeachPlanEx teachPlanEx = null;
			
		//获取表中数据
		Map<String,List<String>> teacherIdByClassId = new HashMap<String, List<String>>();//key:classId+subjectId
		List<CourseSchedule> updateCourseScheduleList = new ArrayList<CourseSchedule>();
		List<ClassTeaching> updateClassTeachingList = new ArrayList<ClassTeaching>();
		List<TeachPlan> updateTeachPlanList = new ArrayList<TeachPlan>();
		Clazz clazz = null;
		
		rowDatas.remove(0);
		int totalSize =rowDatas.size();
		int successCount=0;
		int i=0;
		//这是判断
		for(String[] arr:rowDatas){
			i++;
			//判断班级
			String className = arr[0]==null?"":arr[0].trim();
			if(StringUtils.isBlank(className)){
				String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=className;
                errorData[3]="班级不能为空";
                errorDataList.add(errorData);
                continue;
			}
			if(!classByClassName.containsKey(className)){
				String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=className;
                errorData[3]="班级中"+className+"未找到对应班级";
                errorDataList.add(errorData);
                continue;
			}
			//确定班级
			clazz = classByClassName.get(className);
			
			List<ClassTeaching> ctList = classTeachingMap.get(clazz.getId());
			if(CollectionUtils.isEmpty(ctList)){
				String[] errorData=new String[4];
				sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
				errorData[1]="第"+i+"行";
				errorData[2]="";
				errorData[3]="请先维护该行政班课程开设";
				errorDataList.add(errorData);
				continue;
			}
//			Set<String> subjectSet = EntityUtils.getSet(ctList, ClassTeaching::getSubjectId);
			
			boolean flag1 = false;
			for (int j = 1; j < titleSize; j++) {
				String teacherName = StringUtils.isBlank(arr[j])?"":arr[j].replaceAll("\\u00A0", "").replaceAll("\\u3000", "").replaceAll("，", ",");
				if(StringUtils.isBlank(teacherName)) {
					continue;
				}
				String subName=titleList.get(j);
				List<ClassTeaching> ll = classIdSubjectNameMap.get(clazz.getId()+subName);
				if(CollectionUtils.isEmpty(ll)) {
					String[] errorData=new String[4];
					sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
					errorData[1]="第"+i+"行";
					errorData[2]=titleList.get(j);
					errorData[3]="该班级没有开设此以行政班上课方式的课程，不应填写任课老师";
					errorDataList.add(errorData);
					flag1=true;
					break;
				}
				if(ll.size()>1) {
					String[] errorData=new String[4];
					sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
					errorData[1]="第"+i+"行";
					errorData[2]=titleList.get(j);
					errorData[3]="该班级开设此以行政班上课方式的课程"+subName+"，不唯一";
					errorDataList.add(errorData);
					flag1=true;
					break;
				}
				ClassTeaching ct=ll.get(0);
				String[] teacherNames = teacherName.split(",");
				Set<String> teacherNameSet = new HashSet<String>();
				teacherNameSet.addAll(Arrays.asList(teacherNames));
				if(teacherNameSet.size()!=teacherNames.length){
					String[] errorData=new String[4];
					sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
					errorData[1]="第"+i+"行";
					errorData[2]=teacherName;
					errorData[3]="任课老师重复";
					errorDataList.add(errorData);
					flag1=true;
					break;
				}
				boolean flag2 = false;
				for (String tname : teacherNames) {
					if(!teacherIdByName.containsKey(tname)){
						String[] errorData=new String[4];
						sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
						errorData[1]="第"+i+"行";
						errorData[2]=tname;
						errorData[3]="任课老师不存在";
						errorDataList.add(errorData);
						flag2=true;
						break;
					}else if(sameTeacherNames.contains(tname)){
						String[] errorData=new String[4];
						sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
						errorData[1]="第"+i+"行";
						errorData[2]=tname;
						errorData[3]="该老师姓名存在多个";
						errorDataList.add(errorData);
						flag2 = true;
						break;
					}
				}
				if(flag2){
					flag1=true;
					break;
				}
				
				teacherIdByClassId.put(clazz.getId()+ct.getSubjectId(),new ArrayList<String>());
	        	for (String tname : teacherNames) {
	        		String teacherId = teacherIdByName.get(tname);
	        		teacherIdByClassId.get(clazz.getId()+ct.getSubjectId()).add(teacherId);
	        	}
			}
			
			if(flag1){
				continue;
			}
			
			for (ClassTeaching ct : ctList) {
				ct.setModifyTime(new Date());
				List<ClassTeachingEx> exList = ct.getExList();
				Map<String, ClassTeachingEx> exMap = EntityUtils.getMap(exList, ClassTeachingEx::getTeacherId);
				if(teacherIdByClassId.containsKey(ct.getClassId()+ct.getSubjectId())){
					List<String> tlist = teacherIdByClassId.get(ct.getClassId()+ct.getSubjectId());
					ct.setTeacherId(tlist.get(0));
					if(tlist.size()>1){
						for (int j=1 ;j<tlist.size(); j++) {
							if(!exMap.containsKey(tlist.get(j))){
								classTeachingEx = new ClassTeachingEx();
								classTeachingEx.setId(UuidUtils.generateUuid());
								classTeachingEx.setClassTeachingId(ct.getId());
								classTeachingEx.setTeacherId(tlist.get(j));
								newClassTeachingExList.add(classTeachingEx);
							}
						}
					}
					for (Entry<String, ClassTeachingEx> entry : exMap.entrySet()) {
						if(!tlist.contains(entry.getKey()) || tlist.indexOf(entry.getKey())==0){
							deleteClassTeachingExList.add(entry.getValue());
						}
					}
				}else{
					//教师没有维护不做处理
//					ct.setTeacherId(null);
//					if(CollectionUtils.isNotEmpty(exList)){
//						deleteClassTeachingExList.addAll(exList);
//					}
				}
				updateClassTeachingList.add(ct);
			}
			
			List<CourseSchedule> csList = courseScheduleMap.get(clazz.getId());
			if(CollectionUtils.isNotEmpty(csList)){
				for (CourseSchedule cs : csList) {
					List<TeachPlanEx> exList = teachPlanExMap.get(cs.getId());
					Map<String, TeachPlanEx> exMap = EntityUtils.getMap(exList, TeachPlanEx::getTeacherId);
					if(teacherIdByClassId.containsKey(cs.getClassId()+cs.getSubjectId())){
						List<String> tlist = teacherIdByClassId.get(cs.getClassId()+cs.getSubjectId());
						cs.setTeacherId(tlist.get(0));
						cs.setModifyTime(new Date());
						if(tlist.size()>1){
							for (int j=1 ;j<tlist.size(); j++) {
								if(!exMap.containsKey(tlist.get(j))){
									teachPlanEx = new TeachPlanEx();
									teachPlanEx.setId(UuidUtils.generateUuid());
									teachPlanEx.setPrimaryTableId(cs.getId());
									teachPlanEx.setTeacherId(tlist.get(j));
									teachPlanEx.setUnitId(unitId);
									teachPlanEx.setAcadyear(acadyear);
									teachPlanEx.setSemester(Integer.parseInt(semester));
									teachPlanEx.setType("2");
									newTeachPlanExList.add(teachPlanEx);
								}
							}
						}
						for (Entry<String, TeachPlanEx> entry : exMap.entrySet()) {
							if(!tlist.contains(entry.getKey()) || tlist.indexOf(entry.getKey())==0){
								deleteTeachPlanExList.add(entry.getValue());
							}
						}
					}else{
//						cs.setTeacherId(null);
//						
//						if(CollectionUtils.isNotEmpty(exList)){
//							deleteTeachPlanExList.addAll(exList);
//						}
					}
				}
				updateCourseScheduleList.addAll(csList);
			}
			
			List<TeachPlan> tpList = teachPlanMap.get(clazz.getId());
			if(CollectionUtils.isNotEmpty(tpList)){
				for (TeachPlan tp : tpList) {
					if(teacherIdByClassId.containsKey(tp.getClassId()+tp.getSubjectId())){
						tp.setTeacherId(teacherIdByClassId.get(tp.getClassId()+tp.getSubjectId()).get(0));
					}else{
						tp.setTeacherId(null);
					}
					updateTeachPlanList.add(tp);
				}
			}
    		successCount++;
		}//for结尾

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题行固定
    	    sheet.createFreezePane(1, 1);

            titleList.add("错误数据");
            titleList.add("错误原因");

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int j=0;j<titleList.size();j++){
            	sheet.setColumnWidth(j, 10 * 256);//列宽
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

		try {
			courseScheduleService.saveTeacherScheduleImport(updateClassTeachingList,updateCourseScheduleList,updateTeachPlanList
					,newTeachPlanExList,newClassTeachingExList,deleteTeachPlanExList,deleteClassTeachingExList);
		} catch (Exception e) {
			e.printStackTrace();
			return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
		}
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
		return result;
	}
	
	/**
	 * 下载任课表导入模板
	 * @param response
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String templateParams = request.getParameter("templateParams");
		templateParams = templateParams.replace("&quot;", "\"");
		JSONObject performance = JSON.parseObject(templateParams,JSONObject.class);
		String gradeId = (String) performance.get("gradeId");
		String unitId = (String) performance.get("unitId");
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		
		List<String> titleList = getRowTitleList();

		List<Clazz> classList = classService.findByGradeId(unitId, gradeId, null);
		Set<String> classIds = EntityUtils.getSet(classList, Clazz::getId);

		// 年级课程开设详情
		List<String> gradeIdList = new ArrayList<String>();
		gradeIdList.add(gradeId);
		OpenTeachingSearchDto gradeTeachingSearchDto = new OpenTeachingSearchDto();
		gradeTeachingSearchDto.setAcadyear(acadyear);
		gradeTeachingSearchDto.setSemester(semester);
		gradeTeachingSearchDto.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
		gradeTeachingSearchDto.setGradeIds(gradeIdList.toArray(new String[gradeIdList.size()]));
		gradeTeachingSearchDto.setUnitId(unitId);
		gradeTeachingSearchDto.setIsDeleted(Constant.IS_DELETED_FALSE);
		List<GradeTeaching> gradeTeachingList = gradeTeachingService.findBySearch(gradeTeachingSearchDto);
		Set<String> subjectIdSet = EntityUtils.getSet(gradeTeachingList, GradeTeaching::getSubjectId);
		Map<String, String> courseMap = courseService.findPartCouByIds(subjectIdSet.toArray(new String[0]));
		if (CollectionUtils.isNotEmpty(gradeTeachingList)) {
			titleList.remove(1);
			titleList.remove(1);
			gradeTeachingList.stream().forEach(e -> titleList.add(courseMap.get(e.getSubjectId())));
		}

		// 已存在的班级任课信息
		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(acadyear, semester, classIds.toArray(new String[0]));
		Map<String, ClassTeaching> classTeachingMap = classTeachingList.stream().collect(Collectors.toMap((e -> e.getClassId() + "_" + e.getSubjectId()), e -> e));
		List<ClassTeachingEx> classTeachingExList = classTeachingExService.findByClassTeachingIdIn(EntityUtils.getSet(classTeachingList, ClassTeaching::getId).toArray(new String[0]));
		Map<String, List<ClassTeachingEx>> classTeachingExMap = classTeachingExList.stream().collect(Collectors.groupingBy(e -> e.getClassTeachingId()));
		Set<String> teacherIds = new HashSet<>();
		for (ClassTeaching one : classTeachingList) {
			teacherIds.add(one.getTeacherId());
		}
		for (ClassTeachingEx one : classTeachingExList) {
			teacherIds.add(one.getTeacherId());
		}
		Map<String, String> teacherNameMap = teacherService.findPartByTeacher(teacherIds.toArray(new String[0]));

		HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    //标题行固定
	    sheet.createFreezePane(1, 2);
	    int size = SUBJECT_SIZE;
	    
	    //sheet 合并单元格
	    CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
        sheet.addMergedRegion(car);
	    
	    // 注意事项样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：7倍默认高度
        titleRow.setHeightInPoints(7*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        //注意事项
        String remark = getRemark();
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(style);
	    
        HSSFRow rowTitle = sheet.createRow(1);
        for(int i=0;i<titleList.size();i++){
        	sheet.setColumnWidth(i, 10 * 256);//列宽
        	HSSFCell cell = rowTitle.createCell(i);
        	cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }

        HSSFRow rowContent = null;
        HSSFCell cellContent = null;
        int i = 2;
        for (Clazz clazz : classList) {
			rowContent = sheet.createRow(i++);
			cellContent = rowContent.createCell(0);
			cellContent.setCellValue(new HSSFRichTextString(clazz.getClassNameDynamic()));
			int j = 1;
			for (GradeTeaching one : gradeTeachingList) {
				ClassTeaching tmp = classTeachingMap.get(clazz.getId() + "_" + one.getSubjectId());
				if (tmp != null && tmp.getTeacherId() != null) {
					cellContent = rowContent.createCell(j);
					StringBuilder stringBuilder = new StringBuilder(teacherNameMap.containsKey(tmp.getTeacherId())?teacherNameMap.get(tmp.getTeacherId()):"");
					if (classTeachingExMap.get(tmp.getId()) != null) {
						for (ClassTeachingEx sub : classTeachingExMap.get(tmp.getId())) {
							if(teacherNameMap.containsKey(sub.getTeacherId())){
								stringBuilder.append("," + teacherNameMap.get(sub.getTeacherId()));
							}
						}
					}
					cellContent.setCellValue(new HSSFRichTextString(stringBuilder.toString()));
				}
				j++;
			}
		}
		
		String fileName = "任课老师导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}

    @RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark(){
		
		String remark = "填写注意：\n"
				+ "1.各班级各科目老师可以填写多个，多个老师之间以逗号隔开，第一个会被写入主任课老师，其他会被写入助教老师\n"
				+ "2.首行班级后填写相应科目，如下所示，以此类推\n"
				+ "3.各班级后填写相应科目任课老师姓名，填写格式为：老师姓名1，老师姓名2";
		return remark;
	}
	
	/**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo){
		logger.info("模板校验中......");
		validRowStartNo = "1";
		try{
			List<String> rowTitleList = getRowTitleList();
			rowTitleList.remove(1);
			rowTitleList.remove(1);
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,rowTitleList.size());
			return templateValidate(datas, rowTitleList);
			
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
	
	/**
	 * 获取课程表查询字段 周次时间段 星期段   如果节假日有报错 返回key:error  成功 返回：searchDto
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param isCover
	 * 结合当前时间
	 * @return
	 */
	private Map<String,Object> makeDto(String unitId,String acadyear,String semester,String isCover){
		Map<String,Object> objMap=new HashMap<String,Object>();
		CourseScheduleDto searchDto =null;
		Semester chooseSem=semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),unitId);
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, chooseSem.getSemesterEnd())>0){
			//学年学期已经结束  不用删除  无需进出课表
			objMap.put("error", "学年学期已经结束！");
			return objMap;
		}else{
			DateInfo endDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSem.getSemesterEnd());
			if(endDateInfo == null){
				objMap.put("error", "未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
				return objMap;
			}
			int weekOfWorktime1=1;
			int weekOfWorktime2=endDateInfo.getWeek();
			
			int dayOfWeek1=0;//开始星期
			int dayOfWeek2=endDateInfo.getWeekday()-1;//结束星期
			if(DateUtils.compareForDay(nowDate, chooseSem.getSemesterBegin())<0 || Constant.IS_TRUE_Str.equals(isCover)){
				//学期未开始或覆盖
				weekOfWorktime1=1;
				DateInfo startDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSem.getSemesterBegin());
				if(startDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				dayOfWeek1=startDateInfo.getWeekday()-1;
			}else{
				DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
				if(nowDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				weekOfWorktime1=nowDateInfo.getWeek();
				dayOfWeek1=nowDateInfo.getWeekday()-1;
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
		}
		objMap.put("searchDto", searchDto);
		return objMap;
	}
}
