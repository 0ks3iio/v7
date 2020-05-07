package net.zdsoft.basedata.action;

import net.zdsoft.framework.utils.Objects;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachPlan;
import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingExService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/basedata/classStu/courseScheduleImport")
public class ClassScheduleImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(ClassScheduleImportAction.class);
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
	private ClassTeachingService classTeachingService;
	@Autowired
	private ClassTeachingExService classTeachingExService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private ClassHourService classHourService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private ClassHourExService classHourExService;
	
	
	@RequestMapping("/index/page")
	public String importIndex(ModelMap map,String gradeId,String srcAcadyear,String srcSemester,String srcWeek,
			String destAcadyear,String destSemester,String destWeek){
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "行政班课表");
		// 导入URL 
		map.put("businessUrl", "/basedata/classStu/courseScheduleImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/classStu/courseScheduleImport/template");
        map.put("exportErrorExcelUrl", "/basedata/classStu/courseScheduleImport/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",1);
		//模板校验
		map.put("validateUrl", "/basedata/classStu/courseScheduleImport/validate?acadyear="+destAcadyear+"&semester="+destSemester+"&gradeId="+gradeId);
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("gradeId", gradeId);
	    obj.put("srcAcadyear", srcAcadyear);
	    obj.put("srcSemester", srcSemester);
	    obj.put("srcWeek", srcWeek);
	    obj.put("destAcadyear", destAcadyear);
	    obj.put("destSemester", destSemester);
	    obj.put("destWeek", destWeek);
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/basedata/classTeaching/courseScheduleImport.ftl";
	}
	@Override
	public String getObjectName() {
		return "classScheduleImportAction";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、选择<font style='color:red;'>模板周次</font>后所下载的模板中会包含该周次的上课课程，更改周次后请重新下载模板</p>"
				+ "<p>3、<font style='color:red;'>起始周次</font>即在导入后只改变起始周次及其之后的课程表</p>"
				+ "<p>4、改变选项后请重新上传模板</p>";
		return desc;
		
	}

	@Override
	public List<String> getRowTitleList() {
		return null;
	}

	/**
	 * 导入数据
	 * 导入过程：
		先校验导入文件内容，若有错误数据则全部导入失败。
		1、校验数据格式内容；
		2、校验班级是否存在，是否重复，课程是否存在，是否是必修课，是否以教学班形式上课；
		3、校验单双周，单双周科目不能为同一门课，单双周科目只能存在一个节次；
		4、课程开设处理，班级中未开设某门课程时，添加班级课程开设(classTeaching)，并补充年级课程开设(gradeTeaching)。同时根据导入节次，补全班级课程开设中的课时(classTeaching.courseHour)，同时删除旧的添加新的6.0排课中间表(teachPlan)及扩展表(teachPlanEx)数据；
		5、课程表数据处理，删除旧的课程表数据，重新添加新的课程表数据(courseSchedule)；
		6、虚拟课程处理：
　　			a、如果班级之间的打包关系和旧的一致
				a1.新的时间点包含所有旧的时间点，则单独添加新的时间点，
				a2.新的时间点不完全包含旧的时间点，删除所有旧的时间点，添加所有新的时间点，
				都更新关联的教学班课表；
			b、如果班级之间的打包关系和旧的不一致，解除关联的教学班(teachClass.relaCourseId)，删除旧的时间点(classHour和classHourEx)，添加新的时间点；
		7、其他：没有校验教师、学生、教学班上课时间冲突。
	 */
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		//获取参数
		params = params.replace("&quot;", "\"");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		String acadyear = (String) performance.get("destAcadyear");
		String semester = (String) performance.get("destSemester");
		String gradeId = (String) performance.get("gradeId");
		String destWeek = (String) performance.get("destWeek");
		List<String> titleList = getRowTitleList2(acadyear, semester, gradeId).get(2);
		
		String operatorId = getLoginInfo().getUserId();
		
		//获取上传数据 第一行行标是0
		List<String[]> rowDatas = ExcelUtils.readExcelIgnoreDesc(filePath, titleList.size());
		rowDatas.remove(0);//星期
		rowDatas.remove(0);//节次
		
		//错误数据序列号
        int totalSize =rowDatas.size();
		List<String[]> errorDataList=new ArrayList<String[]>();
		
		//判断基本数据
        if(CollectionUtils.isEmpty(rowDatas)){
            String[] errorData=new String[4];
            errorData[0]="1";
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
        }
		if(StringUtils.isBlank(unitId) || StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
			String[] errorData=new String[4];
            errorData[0]="1";
            errorData[1]="";
            errorData[2]="";
            errorData[3]="参数丢失，无法保存";
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
		}
		
		//判断时间段，节假日是否维护
		Map<String, Object> map = makeDto(unitId, acadyear, semester, destWeek);
		if(map.containsKey("error")){
			String[] errorData=new String[4];
            errorData[0]="1";
            errorData[1]="";
            errorData[2]="";
            errorData[3]=(String) map.get("error");
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
		}
		CourseScheduleDto searchDto = (CourseScheduleDto) map.get("searchDto");
		
		//课程信息
		List<Course> courseList = courseService.findByTopUnitAndUnitId(unitId);
		if(CollectionUtils.isEmpty(courseList)){
			String[] errorData=new String[4];
            errorData[0]="1";
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位课程库中没有课程";
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
		}
		Map<String, List<Course>> subjectBySubjectNames=courseList.stream().collect(Collectors.groupingBy(Course::getSubjectName));
		Map<String, Course> courseMap =courseList.stream().collect(Collectors.toMap(e->e.getId(), e -> e));
		
		//班级信息
		List<Clazz> classList = classService.findByGradeId(unitId, gradeId, null);
		if(totalSize!=classList.size()){
			String[] errorData=new String[4];
            errorData[0]="1";
            errorData[1]="";
            errorData[2]="";
            errorData[3]="请确认表中包含该年级下的所有班级";
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
		}
		Set<String> classIds = EntityUtils.getSet(classList, Clazz::getId);
		Map<String, Clazz> classByClassName = EntityUtils.getMap(classList, Clazz::getClassNameDynamic);
		Map<String, Clazz> classMap = EntityUtils.getMap(classList, Clazz::getId);
		
		//重新导入时，如果班级打包关系和旧的一致则更新时间点和课程表，如果不一致且被教学班引用，则解除引用并删除课程表。
		//走班课程
		List<String> virtualIds = courseList.stream().filter(e->BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getCourseTypeId()))
				.map(e->e.getId()).collect(Collectors.toList());
		Map<String, List<TeachClass>> virtualTeachClassMap = new HashMap<String, List<TeachClass>>(); 
		List<String> delTeachClassIds = new ArrayList<String>();
		//原有打包关系key-courseId
		Map<String, List<List<String>>> oldBindMap = new HashMap<String, List<List<String>>>();
		//key-courseId+classId
		Map<String, ClassHour> oldClassHourMap = new HashMap<String, ClassHour>();
		//
		Map<String, List<ClassHourEx>> oldClassHourExMap = new HashMap<String, List<ClassHourEx>>();
		//key-classHourId,value-节次
		Map<String, List<String>> oldPeriodMap = new HashMap<String, List<String>>();
		if(CollectionUtils.isNotEmpty(virtualIds)){
			List<ClassHour> classHourList = classHourService.findBySubjectIds(acadyear, semester, unitId, gradeId, virtualIds.toArray(new String[virtualIds.size()]));
			List<ClassHourEx> classHourExList = classHourExService.findByClassHourIdIn(EntityUtils.getList(classHourList, ClassHour::getId).toArray(new String[0]));
			oldClassHourExMap = EntityUtils.getListMap(classHourExList, ClassHourEx::getClassHourId, Function.identity());
			for (ClassHour classHour : classHourList) {
				String[] cids; 
				if(StringUtils.isNotBlank(classHour.getClassIds())){
					cids = classHour.getClassIds().split(",");
				}else{
					cids = classIds.stream().collect(Collectors.toList()).toArray(new String[0]);
				}
				
				for (String cid : cids) {
					oldClassHourMap.put(classHour.getSubjectId()+cid, classHour);
				}
				
				if(!oldBindMap.containsKey(classHour.getSubjectId())){
					oldBindMap.put(classHour.getSubjectId(), new ArrayList<List<String>>());
				}
				oldBindMap.get(classHour.getSubjectId()).add(Arrays.asList(cids));
				if(oldClassHourExMap.containsKey(classHour.getId())){
					List<String> periodList = new ArrayList<String>();
					for (ClassHourEx ex : oldClassHourExMap.get(classHour.getId())) {
						periodList.add((ex.getDayOfWeek()+1)+"_"+(Integer.parseInt(ex.getPeriodInterval())-1)+"_"+ex.getPeriod());
					}
					oldPeriodMap.put(classHour.getId(), periodList);
				}
			}
			List<TeachClass> teachClassList = teachClassService.findByRelaCourseIds(unitId, acadyear, semester, gradeId, EntityUtils.getList(classHourList, ClassHour::getId).toArray(new String[0]));
			virtualTeachClassMap = EntityUtils.getListMap(teachClassList, TeachClass::getRelaCourseId, Function.identity());
		}
	    
	    //原有课程开设
		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(acadyear, semester, classIds.toArray(new String[0]));
		//key:classId+subjectId
		Map<String,ClassTeaching> classTeachingMap = new HashMap<String, ClassTeaching>();
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			Map<String, Long> collect = classTeachingList.stream()
					.collect(Collectors.groupingBy(e->e.getClassId()+e.getSubjectId(), Collectors.counting()));
			List<String> claSubNames = new ArrayList<String>();
			for (Entry<String, Long> entry : collect.entrySet()) {
				if(entry.getValue()>1){
					String errorClaId = entry.getKey().substring(0, 32);
					String errorSubId = entry.getKey().substring(32);
					claSubNames.add(classMap.get(errorClaId).getClassNameDynamic()+"-"+courseMap.get(errorSubId).getSubjectName());
				}
			}
			if(claSubNames.size()>0){
				String[] errorData=new String[4];
				errorData[0]="1";
				errorData[1]="";
				errorData[2]=StringUtils.join(claSubNames, ",");
				errorData[3]="该单位班级开设课程计划出现重复数据（一个班级开出多条相同科目数据）请检查清理！";
				errorDataList.add(errorData);
				return result(totalSize,0,totalSize,errorDataList,"");
			}
			classTeachingMap = classTeachingList.stream().collect(Collectors.toMap(e->e.getClassId()+e.getSubjectId(), e -> e));
		}
		
		//key1——classId，key2——节次，value——courseId
		Map<String, Map<String, List<String>>> allMap = new HashMap<String, Map<String,List<String>>>();
		//key——classId+节次+科目id，value——1单/2双周
		Map<String, Integer> weekTypeMap = new HashMap<String, Integer>();
		//key1-classId，key2-courseId,value-另个一个绑定的courseId可为null
		Map<String, Map<String, String>> bindCourseMap = new HashMap<String, Map<String,String>>();
		//key1-courseId，key2-classId,value—时间点集合
		Map<String, Map<String,List<String>>> classHourMap = new HashMap<String, Map<String,List<String>>>();
		
		int i=0;
		Clazz clazz = null;
		Course course = null;
		for(String[] arr:rowDatas){
			i++;
			
			//判断班级
			String className = arr[0]==null?"":arr[0].trim();
			if(StringUtils.isBlank(className)){
				String[] errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=className;
                errorData[3]="班级不能为空";
                errorDataList.add(errorData);
                continue;
			}
			if(!classByClassName.containsKey(className)){
				String[] errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=className;
                errorData[3]="未找到对应班级";
                errorDataList.add(errorData);
                continue;
			}
			
			clazz = classByClassName.get(className);
			if(allMap.containsKey(clazz.getId())){
				String[] errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=className;
                errorData[3]="班级重复";
                errorDataList.add(errorData);
                continue;
			}
			
			allMap.put(clazz.getId(), new HashMap<String, List<String>>());
			
			//判断课程
			boolean flag = false;
			for (int j = 1; j < arr.length; j++) {
				course=null;
				String subjectName = arr[j]==null?"":arr[j].trim();
				if(StringUtils.isBlank(subjectName)){
					continue;
				}
				subjectName=subjectName.replace("，", ",");
				List<String> subNames = new ArrayList<String>();
				boolean isWeekType=false;//是否单双周
				Map<String, Integer> tempMap = new HashMap<String, Integer>();//单双周，key=subjectName,value=1单/2双
				if(subjectName.contains(",")){//同时包含单双周
					isWeekType=true;
					String[] subjectNames = subjectName.split(",");
					if(subjectNames.length>2){
						String[] errorData=new String[4];
	                    errorData[0]=i+"";
		                errorData[1]="第"+i+"行";
		                errorData[2]=subjectName;
		                errorData[3]="科目格式错误";
		                errorDataList.add(errorData);
		                flag=true;
		                break;
					}
					if(subjectNames[0].endsWith("[单]")&&subjectNames[1].endsWith("[双]")){
						String subName0 = subjectNames[0].substring(0,subjectNames[0].length()-3);
						String subName1 = subjectNames[1].substring(0,subjectNames[1].length()-3);
						if(subName0.equals(subName1)){
							String[] errorData=new String[4];
							errorData[0]=i+"";
							errorData[1]="第"+i+"行";
							errorData[2]=subjectName;
							errorData[3]="单双周科目不能相同";
							errorDataList.add(errorData);
							flag=true;
							break;
						}
						subNames.add(subName0);
						subNames.add(subName1);
						tempMap.put(subName0, 1);
						tempMap.put(subName1, 2);
					}else if(subjectNames[0].endsWith("[双]")&&subjectNames[1].endsWith("[单]")){
						String subName0 = subjectNames[0].substring(0,subjectNames[0].length()-3);
						String subName1 = subjectNames[1].substring(0,subjectNames[1].length()-3);
						if(subName0.equals(subName1)){
							String[] errorData=new String[4];
							errorData[0]=i+"";
							errorData[1]="第"+i+"行";
							errorData[2]=subjectName;
							errorData[3]="单双周科目不能相同";
							errorDataList.add(errorData);
							flag=true;
							break;
						}
						subNames.add(subName0);
						subNames.add(subName1);
						tempMap.put(subName0, 2);
						tempMap.put(subName1, 1);
					}else{
						String[] errorData=new String[4];
	                    errorData[0]=i+"";
		                errorData[1]="第"+i+"行";
		                errorData[2]=subjectName;
		                errorData[3]="科目格式错误";
		                errorDataList.add(errorData);
		                flag=true;
		                break;
					}
					
				}else{
					if(subjectName.endsWith("[单]")){//只包含单周
						isWeekType=true;
						subjectName=subjectName.substring(0,subjectName.length()-3);
						subNames.add(subjectName);
						tempMap.put(subjectName, 1);
					}else if(subjectName.endsWith("[双]")){//只包含双周
						isWeekType=true;
						subjectName=subjectName.substring(0,subjectName.length()-3);
						subNames.add(subjectName);
						tempMap.put(subjectName, 2);
					}else{//正常上课
						subNames.add(subjectName);
					}
				}
				
				String titlePeriod = titleList.get(j);
				boolean flag2 = false;
				List<String> courseIds = new ArrayList<String>();
				for (String subName : subNames) {
					if(!subjectBySubjectNames.containsKey(subName)){
						String[] errorData=new String[4];
						errorData[0]=i+"";
						errorData[1]="第"+i+"行";
						errorData[2]=subName;
						errorData[3]="未找到对应科目";
						errorDataList.add(errorData);
						flag2=true;
						break;
					}else{
						List<Course> cList = subjectBySubjectNames.get(subName);
						for(Course cc:cList) {
							if(StringUtils.isNotBlank(cc.getSection()) && cc.getSection().indexOf(String.valueOf(clazz.getSection()))>=0){ 
								course=cc;
								break;
							}else{
								course=null;
							}
						}
						if(course==null) {
							String[] errorData=new String[4];
							errorData[0]=i+"";
							errorData[1]="第"+i+"行";
							errorData[2]=subName;
							errorData[3]=className+"所属学段中未开设课程"+subName;
							errorDataList.add(errorData);
							flag2=true;
							break;
						}
						if(BaseConstants.SUBJECT_TYPE_XX.equals(course.getType())){
							String[] errorData=new String[4];
							errorData[0]=i+"";
							errorData[1]="第"+i+"行";
							errorData[2]=subName;
							errorData[3]="课程"+subName+"为选修课";
							errorDataList.add(errorData);
							flag2=true;
							break;
						}
						
						if(virtualIds.contains(course.getId())){
							if(isWeekType){
								String[] errorData=new String[4];
								errorData[0]=i+"";
								errorData[1]="第"+i+"行";
								errorData[2]=subName;
								errorData[3]="走班课程不能以单双周形式上课";
								errorDataList.add(errorData);
								flag2=true;
								break;
							}
						}
						
						if(classTeachingMap.containsKey(clazz.getId()+course.getId())){
							if(Constant.IS_TRUE==classTeachingMap.get(clazz.getId()+course.getId()).getIsTeaCls()){
								String[] errorData=new String[4];
								errorData[0]=i+"";
								errorData[1]="第"+i+"行";
								errorData[2]=subName;
								errorData[3]="班级课程开设中"+subName+"为以教学班形式教学";
								errorDataList.add(errorData);
								flag2=true;
								break;
							}
						}
						if(virtualIds.contains(course.getId())){
							if(!classHourMap.containsKey(course.getId())){
								classHourMap.put(course.getId(), new HashMap<String, List<String>>());
							}
							if(!classHourMap.get(course.getId()).containsKey(clazz.getId())){
								classHourMap.get(course.getId()).put(clazz.getId(), new ArrayList<String>());
							}
							classHourMap.get(course.getId()).get(clazz.getId()).add(titlePeriod);
						}
					}
					
					if(!allMap.get(clazz.getId()).containsKey(titlePeriod)){
						allMap.get(clazz.getId()).put(titlePeriod, new ArrayList<String>());
					}
					allMap.get(clazz.getId()).get(titlePeriod).add(course.getId());
					if(isWeekType){
						weekTypeMap.put(clazz.getId()+titlePeriod+course.getId(), tempMap.get(subName));
						if(bindCourseMap.containsKey(clazz.getId())){
							if(bindCourseMap.get(clazz.getId()).containsKey(course.getId())){
								String[] errorData=new String[4];
								errorData[0]=i+"";
								errorData[1]="第"+i+"行";
								errorData[2]=subName;
								errorData[3]="单双周课程只能存在一个节次";
								errorDataList.add(errorData);
								flag2=true;
								break;
							}
						}else{
							bindCourseMap.put(clazz.getId(), new HashMap<String, String>());
						}
						courseIds.add(course.getId());
					}
				}
				
				if(isWeekType){
					if(courseIds.size()>1){
						bindCourseMap.get(clazz.getId()).put(courseIds.get(0), courseIds.get(1));
						bindCourseMap.get(clazz.getId()).put(courseIds.get(1), courseIds.get(0));
					}else if(courseIds.size()==1){
						bindCourseMap.get(clazz.getId()).put(courseIds.get(0), null);
					}
				}
				
				if(flag2){
					flag=true;
					break;
				}
			}//班级科目结尾
			
			if(flag){
				continue;
			}
			
		}//校验for结尾
		
		// 错误数据Excel导出
		String errorExcelPath = "";
		if(CollectionUtils.isNotEmpty(errorDataList)) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			
			List<List<String>> titleList2 = getRowTitleList2(acadyear, semester, gradeId);
			List<String> weekTitleList = titleList2.get(0);
			List<String> periodTitleList = titleList2.get(1);
			periodTitleList.add("错误数据");
			periodTitleList.add("错误原因");
			int size = periodTitleList.size();
	        //列宽
		    sheet.setColumnWidth(0, 10 * 256);// 首列宽度
	        for(int j=1;j<weekTitleList.size();j++){
	        	 sheet.setColumnWidth(j, 6 * 256);// 内容列宽度
	        }
	        for (int j = weekTitleList.size(); j < size; j++) {
	        	sheet.setColumnWidth(j, 15 * 256);// 错误提示列宽度
			}
	        //标题行固定
	        sheet.createFreezePane(0, 2);
		    // 单元格样式
		    HSSFFont font = workbook.createFont();
	        HSSFCellStyle style = workbook.createCellStyle();
	        style.setFont(font);
	        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
	        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
	        style.setWrapText(true);//自动换行
	        style.setBorderBottom(BorderStyle.THIN);  
	        style.setBorderLeft(BorderStyle.THIN);  
	        style.setBorderRight(BorderStyle.THIN);  
	        style.setBorderTop(BorderStyle.THIN);
			
	        HSSFFont errorFont = workbook.createFont();
	        errorFont.setColor(HSSFFont.COLOR_RED);
			HSSFCellStyle errorStyle = workbook.createCellStyle();
			errorStyle.setFont(errorFont);
			errorStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
			errorStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
			errorStyle.setWrapText(true);//自动换行
			errorStyle.setBorderBottom(BorderStyle.THIN);  
			errorStyle.setBorderLeft(BorderStyle.THIN);  
			errorStyle.setBorderRight(BorderStyle.THIN);  
			errorStyle.setBorderTop(BorderStyle.THIN);
			
			HSSFRow weekTitle = sheet.createRow(0);
			HSSFRow periodTitle = sheet.createRow(1);
			for(int j=0;j<size;j++){
				HSSFCell cell1 = weekTitle.createCell(j);
				cell1.setCellStyle(style);
				if(j<weekTitleList.size()){
					cell1.setCellValue(new HSSFRichTextString(weekTitleList.get(j)));
				}
				HSSFCell cell2 = periodTitle.createCell(j);
				cell2.setCellStyle(style);
				if(j==0||j>=weekTitleList.size()){
	            	cell2.setCellValue(periodTitleList.get(j));
	            }else{
	            	cell2.setCellValue(Integer.parseInt(periodTitleList.get(j)));
	            }
			}
			
			Map<String, String[]> errorDataMap = errorDataList.stream().collect(Collectors.toMap(e->e[0], Function.identity()));
			
			int j = 1;
			for (String[] rowData : rowDatas) {
				j++;
				HSSFRow row = sheet.createRow(j);
				for (int k=0;k<periodTitleList.size();k++) {
					HSSFCell cell = row.createCell(k);
					if (k<periodTitleList.size()-2) {
						cell.setCellStyle(style);
						cell.setCellValue(new HSSFRichTextString(rowData[k]));
					} else if (k==periodTitleList.size()-2) {
						cell.setCellStyle(errorStyle);
						if(errorDataMap.containsKey(String.valueOf(j-1))){
							cell.setCellValue(new HSSFRichTextString(errorDataMap.get(String.valueOf(j-1))[2]));
						}
					} else {
						cell.setCellStyle(errorStyle);
						if(errorDataMap.containsKey(String.valueOf(j-1))){
							cell.setCellValue(new HSSFRichTextString(errorDataMap.get(String.valueOf(j-1))[3]));
						}
					}
				}
			}
			
			errorExcelPath = saveErrorExcel(filePath, workbook);
			return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
		}

		//原有助理教师
		List<ClassTeachingEx> classTeachingExList = classTeachingExService.findByClassTeachingIdIn(EntityUtils.getSet(classTeachingList, ClassTeaching::getId).toArray(new String[0]));
		Map<String,List<String>> classTeachingIdToTeacherId = EntityUtils.getListMap(classTeachingExList, ClassTeachingEx::getClassTeachingId, ClassTeachingEx::getTeacherId);
		
		//新助理教师课程表
		List<TeachPlanEx> insertTeachPlanExList = new ArrayList<TeachPlanEx>();
		Map<String,Integer> ctCouMap = new HashMap<String, Integer>();
		
		//新课表
		List<CourseSchedule> insertCourseScheduleList = new ArrayList<CourseSchedule>();
		CourseSchedule c = null;
		int startWeek = searchDto.getWeekOfWorktime1();
		int startDay = searchDto.getDayOfWeek1();
		int endWeek = searchDto.getWeekOfWorktime2();
		int endDay = searchDto.getDayOfWeek2();
		
		//新中间表
		List<TeachPlan> insertTeachPlanList = new ArrayList<TeachPlan>();
		TeachPlan teachPlan = null;
		
		//新时间点
		List<ClassHour> insertClassHourList = new ArrayList<ClassHour>();
		List<ClassHourEx> insertClassHourExList = new ArrayList<ClassHourEx>();
		ClassHour classHour = null;
		ClassHourEx classHourEx = null;
		
		//解除绑定的教学班
		List<TeachClass> updateTeachClassList = new ArrayList<TeachClass>();
		
		Map<String, List<List<String>>> newBindMap = new HashMap<String, List<List<String>>>();
		for (Entry<String, Map<String, List<String>>> entry : classHourMap.entrySet()) {
			String courseId = entry.getKey();
			newBindMap.put(courseId, new ArrayList<List<String>>());
			Map<String, List<String>> tempMap = new HashMap<String, List<String>>();
			for (Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {
				List<String> periods = entry2.getValue();
				Collections.sort(periods);
				String k = StringUtils.join(periods, ",");
				if(!tempMap.containsKey(k)){
					tempMap.put(k, new ArrayList<String>());
				}
				tempMap.get(k).add(entry2.getKey());
			}
			newBindMap.get(courseId).addAll(tempMap.values());
		}
		
		List<ClassHour> delClassHourList = new ArrayList<ClassHour>();

		for (String virtualId : virtualIds) {
			if(newBindMap.containsKey(virtualId)&&oldBindMap.containsKey(virtualId)){
				List<List<String>> newList = newBindMap.get(virtualId);
				List<List<String>> oldList = oldBindMap.get(virtualId);
				for (List<String> ol : oldList) {
					boolean flag = true;
					for (List<String> nl : newList) {
						Collections.sort(ol);
						Collections.sort(nl);
						if(Objects.equals(nl, ol)){
							flag=false;
							break;
						}
					}
					if(flag){
						delClassHourList.add(oldClassHourMap.get(virtualId+ol.get(0)));
					}
				}
				for (List<String> nl : newList) {
					boolean flag = true;
					for (List<String> ol : oldList) {
						Collections.sort(nl);
						Collections.sort(ol);
						if(Objects.equals(nl, ol)){
							flag=false;
							break;
						}
					}
					if(flag){
						classHour = new ClassHour();
						classHour.setId(UuidUtils.generateUuid());
						classHour.setUnitId(unitId);
						classHour.setGradeId(gradeId);
						classHour.setAcadyear(acadyear);
						classHour.setSemester(semester);
						classHour.setSubjectId(virtualId);
						if(!nl.containsAll(classIds)){
							classHour.setClassIds(StringUtils.join(nl,","));
						}
						classHour.setCreationTime(new Date());
						classHour.setModifyTime(new Date());
						classHour.setIsDeleted(Constant.IS_DELETED_FALSE);
						insertClassHourList.add(classHour);
						
						List<String> periodList = classHourMap.get(virtualId).get(nl.get(0));
						for (String p : periodList) {
							classHourEx = makeClassHourEx(classHour.getId(), p);
							insertClassHourExList.add(classHourEx);
						}
					}else{
						ClassHour oldClassHour = oldClassHourMap.get(virtualId+nl.get(0));
						List<String> oldPeriodList = oldPeriodMap.get(oldClassHour.getId());
						List<String> newPeriodList = classHourMap.get(virtualId).get(nl.get(0));
						List<TeachClass> teachClassList = virtualTeachClassMap.get(oldClassHour.getId());
						if(CollectionUtils.isNotEmpty(oldPeriodList)){
							if(newPeriodList.containsAll(oldPeriodList)){
								newPeriodList.removeAll(oldPeriodList);
							}else{
								List<ClassHourEx> oldExList = oldClassHourExMap.get(oldClassHour.getId());
								if(CollectionUtils.isNotEmpty(oldExList)){
									for (ClassHourEx ex : oldExList) {
										ex.setIsDeleted(Constant.IS_DELETED_TRUE);
										ex.setModifyTime(new Date());
										insertClassHourExList.add(ex);
									}
								}
								if(CollectionUtils.isNotEmpty(teachClassList)) {
									delTeachClassIds.addAll(EntityUtils.getList(teachClassList, TeachClass::getId));//删除课表
								}
							}
						}
						for (String p : newPeriodList) {
							classHourEx = makeClassHourEx(oldClassHour.getId(), p);
							insertClassHourExList.add(classHourEx);
							if(CollectionUtils.isNotEmpty(teachClassList)){
								String[] timeArr = p.split("_");
								int dayOfWeek = Integer.parseInt(timeArr[0])-1;//星期从0开始
								String periodInterval = Integer.parseInt(timeArr[1])+1+"";//上下午
								int period = Integer.parseInt(timeArr[2]);//节次
								for (TeachClass teachClass : teachClassList) {
									course = courseMap.get(teachClass.getCourseId());
									c = new CourseSchedule();
									c.setAcadyear(acadyear);
									c.setSemester(Integer.parseInt(semester));
									c.setSchoolId(unitId);
									c.setClassId(teachClass.getId());
									c.setSubjectId(teachClass.getCourseId());
									c.setSubjectType(course.getType());
									c.setTeacherId(teachClass.getTeacherId());
									c.setCourseId(null);
									c.setClassType(2);
									c.setPunchCard(teachClass.getPunchCard()); 
									c.setPlaceId(teachClass.getPlaceId());
									c.setCreationTime(new Date());
									c.setModifyTime(new Date());
									c.setIsDeleted(Constant.IS_DELETED_FALSE);
									CourseSchedule c1 = null;
									for (int index = startWeek; index <= endWeek; index++) {
//										if (index == startWeek && dayOfWeek < startDay) {//开始日期
//											continue;
//										}
										if (index == endWeek && dayOfWeek > endDay) {//结束时间
											continue;
										}
										c1 = new CourseSchedule();
										EntityUtils.copyProperties(c, c1);
										c1.setId(UuidUtils.generateUuid());
										c1.setWeekOfWorktime(index);
										c1.setPeriod(period);
										c1.setPeriodInterval(periodInterval);
										c1.setDayOfWeek(dayOfWeek);
										insertCourseScheduleList.add(c1);
									}
								}
							}
						}
						
					}
				}
				
			}else if(newBindMap.containsKey(virtualId)){
				List<List<String>> newList = newBindMap.get(virtualId);
				for (List<String> nl : newList) {
					classHour = new ClassHour();
					classHour.setId(UuidUtils.generateUuid());
					classHour.setUnitId(unitId);
					classHour.setGradeId(gradeId);
					classHour.setAcadyear(acadyear);
					classHour.setSemester(semester);
					classHour.setSubjectId(virtualId);
					if(!nl.containsAll(classIds)){
						classHour.setClassIds(StringUtils.join(nl,","));
					}
					classHour.setCreationTime(new Date());
					classHour.setModifyTime(new Date());
					classHour.setIsDeleted(Constant.IS_DELETED_FALSE);
					insertClassHourList.add(classHour);
					
					List<String> periodList = classHourMap.get(virtualId).get(nl.get(0));
					for (String p : periodList) {
						classHourEx = makeClassHourEx(classHour.getId(), p);
						insertClassHourExList.add(classHourEx);
					}
				}
			}else if(oldBindMap.containsKey(virtualId)){
				List<List<String>> oldList = oldBindMap.get(virtualId);
				for (List<String> ol : oldList) {
					delClassHourList.add(oldClassHourMap.get(virtualId+ol.get(0)));
				}
			}
		}
		
		for (ClassHour ch : delClassHourList) {
			ch.setIsDeleted(Constant.IS_DELETED_TRUE);
			ch.setModifyTime(new Date());
			insertClassHourList.add(ch);
			List<ClassHourEx> oldExList = oldClassHourExMap.get(ch.getId());
			if(CollectionUtils.isNotEmpty(oldExList)){
				for (ClassHourEx ex : oldExList) {
					ex.setIsDeleted(Constant.IS_DELETED_TRUE);
					ex.setModifyTime(new Date());
					insertClassHourExList.add(ex);
				}
			}
			List<TeachClass> teachClassList = virtualTeachClassMap.get(ch.getId());
			if(CollectionUtils.isNotEmpty(teachClassList)){
				for (TeachClass teachClass : teachClassList) {
					teachClass.setRelaCourseId(null);
					teachClass.setModifyTime(new Date());
					delTeachClassIds.add(teachClass.getId());
				}
				updateTeachClassList.addAll(teachClassList);
			}
		}
		
		
		for(Entry<String, Map<String, List<String>>> entry:allMap.entrySet()){
			clazz=classMap.get(entry.getKey());
			Map<String, List<String>> timeMap = entry.getValue();
			for (Entry<String, List<String>> ent : timeMap.entrySet()) {
				String titlePeriod = ent.getKey();
				String[] timeArr = titlePeriod.split("_");
				int dayOfWeek = Integer.parseInt(timeArr[0])-1;//星期从0开始
				String periodInterval = Integer.parseInt(timeArr[1])+1+"";//上下午
				int period = Integer.parseInt(timeArr[2]);//节次
				for (String cid : ent.getValue()) {
					boolean isWeekType = false;//是否单双周
					int weekType=3;
					course = courseMap.get(cid);
					if(weekTypeMap.containsKey(clazz.getId()+titlePeriod+course.getId())){
						isWeekType=true;
						weekType=weekTypeMap.get(clazz.getId()+titlePeriod+course.getId());
					}
					if(classTeachingMap.containsKey(clazz.getId()+course.getId())){
						ClassTeaching ct = classTeachingMap.get(clazz.getId()+course.getId());
						Integer ch= ctCouMap.get(clazz.getId()+course.getId());
						if(ch==null){
							ct.setCourseHour(1f);
							ctCouMap.put(clazz.getId()+course.getId(),1);
						}else{
							ct.setCourseHour(1f+ct.getCourseHour());
						}
						ct.setModifyTime(new Date());
						ct.setWeekType(weekType);
						ct.setOperatorId(operatorId);
					}else{
						ClassTeaching classTeaching = new ClassTeaching();
						classTeaching.setId(UuidUtils.generateUuid());
						classTeaching.setUnitId(unitId);
						classTeaching.setClassId(clazz.getId());
						classTeaching.setSubjectId(course.getId());
						classTeaching.setAcadyear(acadyear);
						classTeaching.setSemester(semester);
						classTeaching.setCreationTime(new Date());
						classTeaching.setModifyTime(new Date());
						classTeaching.setEventSource(Constant.IS_FALSE);
						classTeaching.setIsDeleted(Constant.IS_FALSE);
						classTeaching.setIsTeaCls(Constant.IS_FALSE);
						classTeaching.setPunchCard(Constant.IS_TRUE);
						classTeaching.setSubjectType(course.getType());
						classTeaching.setCredit(course.getInitCredit());
						classTeaching.setFullMark(course.getFullMark());
						classTeaching.setPassMark(course.getInitPassMark());
						classTeaching.setCourseHour(1f);
						classTeaching.setWeekType(weekType);
						classTeaching.setOperatorId(operatorId);
						classTeachingMap.put(classTeaching.getClassId()+classTeaching.getSubjectId(), classTeaching);
					}
					
					c = new CourseSchedule();
					c.setAcadyear(acadyear);
					c.setSemester(Integer.parseInt(semester));
					c.setSchoolId(unitId);
					c.setClassId(clazz.getId());
					c.setSubjectId(course.getId());
					c.setSubjectType(course.getType());
					String courseId = null;
					String teacherId = null;
					ClassTeaching classTea= classTeachingMap.get(clazz.getId()+course.getId());
					if(classTea !=null){
						courseId = classTea.getId();
						teacherId= classTea.getTeacherId();
					}
					c.setTeacherId(teacherId);
					c.setCourseId(courseId);
					c.setClassType(1);
					c.setPunchCard(Constant.IS_TRUE); 
					if(!virtualIds.contains(course.getId())){
						c.setPlaceId(clazz.getTeachPlaceId()==null?"":clazz.getTeachPlaceId());
					}
					c.setCreationTime(new Date());
					c.setModifyTime(new Date());
					c.setIsDeleted(Constant.IS_DELETED_FALSE);
					c.setWeekType(weekType);
					CourseSchedule c1 = null;
					for (int index = startWeek; index <= endWeek; index++) {
						if (index == startWeek && dayOfWeek < startDay) {//开始日期
							continue;
						}
						if (index == endWeek && dayOfWeek > endDay) {//结束时间
							continue;
						}
						if(isWeekType){
							if(index%2==weekType-1){
								continue;
							}
						}
						c1 = new CourseSchedule();
						EntityUtils.copyProperties(c, c1);
						c1.setId(UuidUtils.generateUuid());
						c1.setWeekOfWorktime(index);
						c1.setPeriod(period);
						c1.setPeriodInterval(periodInterval);
						c1.setDayOfWeek(dayOfWeek);
						insertCourseScheduleList.add(c1);
						
						if(classTeachingIdToTeacherId.containsKey(courseId)){
							for (String tid : classTeachingIdToTeacherId.get(courseId)) {
								TeachPlanEx teachPlanEx = new TeachPlanEx();
								teachPlanEx.setId(UuidUtils.generateUuid());
								teachPlanEx.setAcadyear(acadyear);
								teachPlanEx.setSemester(Integer.parseInt(semester));
								teachPlanEx.setUnitId(unitId);
								teachPlanEx.setPrimaryTableId(c1.getId());
								teachPlanEx.setTeacherId(tid);
								teachPlanEx.setType("2");
								insertTeachPlanExList.add(teachPlanEx);
							}
						}
					}	
				}
			}
			
		}//for结尾

		
		try {
			if(MapUtils.isNotEmpty(classTeachingMap)){
				ClassTeaching ct = null;
				
				Set<String> subidslist = classTeachingMap.values().stream().map(t -> t.getSubjectId()).collect(Collectors.toSet());
				Map<String, String> subNamMap= courseService.findPartCouByIds(subidslist.toArray(new String[0]));
				for (Entry<String, ClassTeaching> entry : classTeachingMap.entrySet()) {
					ct = entry.getValue();
					if(virtualIds.contains(ct.getSubjectId())){
						continue;
					}
					teachPlan = new TeachPlan();
					teachPlan.setId(ct.getId());
					teachPlan.setAcadyear(acadyear);
					teachPlan.setSemester(Integer.parseInt(semester));
					teachPlan.setUnitId(unitId);
					teachPlan.setClassType(Constant.IS_TRUE);	
					teachPlan.setClassId(ct.getClassId());
					teachPlan.setSubjectId(ct.getSubjectId());
					teachPlan.setSubjectName(subNamMap.get(ct.getSubjectId()));
					teachPlan.setTeacherId(ct.getTeacherId());
					teachPlan.setPlaceRelation(Constant.IS_FALSE_Str);//自动匹配
					teachPlan.setPlaceId(classMap.get(ct.getClassId()).getTeachPlaceId());
					teachPlan.setWeekPeriod(ct.getCourseHour()==null?0:ct.getCourseHour().intValue());
					teachPlan.setClassName(classMap.get(ct.getClassId()).getClassNameDynamic());
					teachPlan.setPunchCard(Constant.IS_TRUE);//默认考勤
					teachPlan.setSubjNo(courseMap.get(ct.getSubjectId())==null?"":courseMap.get(ct.getSubjectId()).getSubjectCode());
					teachPlan.setWeekType(ct.getWeekType());
					if(ct.getWeekType()!=0&&CourseSchedule.WEEK_TYPE_NORMAL!=ct.getWeekType()){
						if(bindCourseMap.containsKey(ct.getClassId())){
							teachPlan.setBindingCourse(bindCourseMap.get(ct.getClassId()).get(ct.getSubjectId()));
						}
					}
					insertTeachPlanList.add(teachPlan);
				}
				
				if(CollectionUtils.isNotEmpty(delTeachClassIds)){
					classIds.addAll(delTeachClassIds);
				}
				searchDto.setClassIds(classIds.toArray(new String[0]));
				ArrayList<ClassTeaching> insertClassTeachingList = new ArrayList<>(classTeachingMap.values());
				//补充年级课程开设计划
				List<GradeTeaching> insertGradeTeachingList = new ArrayList<GradeTeaching>();
				if(CollectionUtils.isNotEmpty(insertClassTeachingList)){
					List<GradeTeaching> gradeTeachingList = gradeTeachingService.findBySearchList(unitId, acadyear, semester, gradeId, null);
					Set<String> ctSubjectIds = EntityUtils.getSet(insertClassTeachingList, ClassTeaching::getSubjectId);
					Set<String> gtSubjectIds = EntityUtils.getSet(gradeTeachingList, GradeTeaching::getSubjectId);
					ctSubjectIds.removeAll(gtSubjectIds);
					if(CollectionUtils.isNotEmpty(ctSubjectIds)){
						GradeTeaching gradeTeaching = null;
						for (String subjectId : ctSubjectIds) {
							gradeTeaching = new GradeTeaching();
							gradeTeaching.setId(UuidUtils.generateUuid());
							gradeTeaching.setAcadyear(acadyear);
							gradeTeaching.setGradeId(gradeId);
							gradeTeaching.setIsDeleted(Constant.IS_DELETED_FALSE);
							gradeTeaching.setSemester(semester);
							gradeTeaching.setCreationTime(new Date());
							gradeTeaching.setModifyTime(new Date());
							gradeTeaching.setSubjectId(subjectId);
							if(virtualIds.contains(subjectId)){
								gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
							}else{
								gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
							}
							gradeTeaching.setUnitId(unitId);
							gradeTeaching.setIsTeaCls(Constant.IS_FALSE);
							insertGradeTeachingList.add(gradeTeaching);
						}
					}
				}
				
				courseScheduleService.saveClassScheduleImport(searchDto,insertClassTeachingList,null,
						insertTeachPlanList,insertTeachPlanExList, insertCourseScheduleList,insertGradeTeachingList,insertClassHourList,
						insertClassHourExList,updateTeachClassList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return result(totalSize,0,totalSize,errorDataList,"");
		}
		return result(totalSize,totalSize,0,errorDataList,"");
	}
	
	private ClassHourEx makeClassHourEx(String classHourId, String p) {
		ClassHourEx classHourEx= new ClassHourEx();
		String[] timeArr = p.split("_");
		int dayOfWeek = Integer.parseInt(timeArr[0])-1;//星期从0开始
		String periodInterval = Integer.parseInt(timeArr[1])+1+"";//上下午
		int period = Integer.parseInt(timeArr[2]);//节次
		classHourEx.setId(UuidUtils.generateUuid());
		classHourEx.setClassHourId(classHourId);
		classHourEx.setPeriod(period);
		classHourEx.setPeriodInterval(periodInterval);
		classHourEx.setDayOfWeek(dayOfWeek);
		classHourEx.setCreationTime(new Date());
		classHourEx.setModifyTime(new Date());
		classHourEx.setIsDeleted(Constant.IS_DELETED_FALSE);
		return classHourEx;
	}
	
	/**
	 * 获取课程表查询字段 周次时间段 星期段   如果节假日有报错 返回key:error  成功 返回：searchDto
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * 结合当前时间
	 * @return
	 */
	private Map<String,Object> makeDto(String unitId,String acadyear,String semester,String destWeek){
		Map<String,Object> objMap=new HashMap<String,Object>();
		CourseScheduleDto searchDto =null;
		Semester chooseSem= semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),unitId);
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
			if(DateUtils.compareForDay(nowDate, chooseSem.getSemesterBegin())<0 || Constant.IS_TRUE_Str.equals(destWeek)){
				//学期未开始或者从第一周开始
				weekOfWorktime1=1;
				DateInfo startDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSem.getSemesterBegin());
				if(startDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				dayOfWeek1=startDateInfo.getWeekday()-1;
			}else{
//				DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
//				if(nowDateInfo == null){
//					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
//					return objMap;
//				}
//				weekOfWorktime1=nowDateInfo.getWeek();
//				dayOfWeek1=nowDateInfo.getWeekday();
				weekOfWorktime1=Integer.parseInt(destWeek);
				dayOfWeek1=0;
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
	
	
	/**
	 * 下载课程表导入模板
	 * @param response
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String templateParams = request.getParameter("templateParams");
		templateParams = templateParams.replace("&quot;", "\"");
		JSONObject performance = JSON.parseObject(templateParams,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		String gradeId = (String) performance.get("gradeId");
		String srcAcadyear = (String) performance.get("srcAcadyear");
		String srcSemester = (String) performance.get("srcSemester");
		String srcWeek = (String) performance.get("srcWeek");
		String destAcadyear = (String) performance.get("destAcadyear");
		String destSemester = (String) performance.get("destSemester");
			
		List<List<String>> titleList = getRowTitleList2(destAcadyear,destSemester, gradeId);//表头
		List<String> weekTitleList = titleList.get(0);
		List<String> periodTitleList = titleList.get(1);
		List<String> realTitleList = titleList.get(2);
	    int size = weekTitleList.size();
	    
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
        //列宽
	    sheet.setColumnWidth(0, 10 * 256);// 首列宽度
        for(int i=1;i<size;i++){
        	 sheet.setColumnWidth(i, 6 * 256);// 内容列宽度
        }
        //标题行固定
        sheet.createFreezePane(0, 3);
	    // 单元格样式
	    HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        style.setBorderBottom(BorderStyle.THIN);  
        style.setBorderLeft(BorderStyle.THIN);  
        style.setBorderRight(BorderStyle.THIN);  
        style.setBorderTop(BorderStyle.THIN);
        
        //注意事项样式
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setFont(headfont);
        headStyle.setAlignment(HorizontalAlignment.LEFT);//水平居左
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        headStyle.setWrapText(true);//自动换行
        
        //第一行注意事项
        HSSFRow remarkRow = sheet.createRow(0);
        //高度：6倍默认高度
        remarkRow.setHeightInPoints(6*sheet.getDefaultRowHeightInPoints());
  
        //注意事项内容
        HSSFCell titleCell = remarkRow.createCell(0);
        titleCell.setCellStyle(headStyle);
        String remark = getRemark();
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(headStyle);
        //合并单元格
        CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
        sheet.addMergedRegion(car);
	    
        HSSFRow weekTitle = sheet.createRow(1);
        HSSFRow periodTitle = sheet.createRow(2);
        for(int j=0;j<size;j++){
        	HSSFCell cell1 = weekTitle.createCell(j);
            cell1.setCellValue(new HSSFRichTextString(weekTitleList.get(j)));
            cell1.setCellStyle(style);
            HSSFCell cell2 = periodTitle.createCell(j);
            if(j==0){
            	cell2.setCellValue(periodTitleList.get(j));
            }else{
            	cell2.setCellValue(Integer.parseInt(periodTitleList.get(j)));
            }
            cell2.setCellStyle(style);
        }
        
        List<Clazz> classList = classService.findByGradeId(unitId, gradeId, null);
        Map<String, List<CourseSchedule>> csMap = new HashMap<String, List<CourseSchedule>>();
        Map<String, String> courseNameMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(srcWeek)){
        	List<CourseSchedule> csList = courseScheduleService.findCourseScheduleListByClassIdes(srcAcadyear, Integer.parseInt(srcSemester),
        			EntityUtils.getSet(classList, Clazz::getId).toArray(new String[0]), Integer.parseInt(srcWeek));
        	if(CollectionUtils.isNotEmpty(csList)){
        		csMap = EntityUtils.getListMap(csList, CourseSchedule::getClassId, Function.identity());
        		List<Course> courseList = courseService.findListByIds(EntityUtils.getSet(csList, CourseSchedule::getSubjectId).toArray(new String[0]));
        		courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
        	}
        }
        HSSFRow rowContent = null;
        HSSFCell cellContent = null;
        int i = 2;
        for (Clazz clazz : classList) {
        	i++;
			rowContent = sheet.createRow(i);
			cellContent = rowContent.createCell(0);
			cellContent.setCellValue(new HSSFRichTextString(clazz.getClassNameDynamic()));
			cellContent.setCellStyle(style);
			if(csMap.containsKey(clazz.getId())){
				List<CourseSchedule> csList = csMap.get(clazz.getId());
				Map<String, CourseSchedule> timeCourseMap = EntityUtils.getMap(csList, e->(e.getDayOfWeek()+1)+"_"+(Integer.parseInt(e.getPeriodInterval())-1)+"_"+e.getPeriod());
				HSSFCell rowCell = null;
				for(int j=1;j<size;j++){
					rowCell = rowContent.createCell(j);
					rowCell.setCellStyle(style);
		        	if(timeCourseMap.containsKey(realTitleList.get(j))){
		        		CourseSchedule cs = timeCourseMap.get(realTitleList.get(j));
			        	if(courseNameMap.containsKey(cs.getSubjectId())){
			        		String cellTxt = courseNameMap.get(cs.getSubjectId());
			        		if(cs.getWeekType()==CourseSchedule.WEEK_TYPE_ODD){
			        			cellTxt+="[单]";
			        		}else if(cs.getWeekType()==CourseSchedule.WEEK_TYPE_EVEN){
			        			cellTxt+="[双]";
			        		}
			        		rowCell.setCellValue(new HSSFRichTextString(cellTxt));
			        	}
		        	}
		        }
			}else{
				HSSFCell rowCell = null;
				for(int j=1;j<size;j++){
	        		rowCell = rowContent.createCell(j);
	        		rowCell.setCellStyle(style);
	        	}
			}
		}
		
        Grade grade = gradeService.findOne(gradeId);
		String fileName = grade.getGradeName()+"行政班课表导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}

	@RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }
	
	private List<List<String>> getRowTitleList2(String acadyear, String semester, String gradeId) {
		List<String> rowTitle1=new ArrayList<String>();
		rowTitle1.add("星期");
		List<String> rowTitle2 = new ArrayList<String>();
		rowTitle2.add("班级");
		List<String> rowTitle3 = new ArrayList<String>();
		rowTitle3.add("实际");
		Grade grade = gradeService.findById(gradeId);
		Integer morning = 0;
		Integer am = 5;
		Integer pm = 5;
		Integer night = 0;
		int weekDays = 5;
		if(grade!=null){
			morning = grade.getMornPeriods()==null?0:grade.getMornPeriods();
			am = grade.getAmLessonCount()==null?0:grade.getAmLessonCount();
			pm = grade.getPmLessonCount()==null?0:grade.getPmLessonCount();
			night = grade.getNightLessonCount()==null?0:grade.getNightLessonCount();
			if(grade.getWeekDays()!=null){
				weekDays = grade.getWeekDays();
			}
		}
		Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
		int start = 1;
		int end = 2;
		if(morning!=0){
			start = 0;
			timeMap.put(0, morning);
		}
		timeMap.put(1, am);
		timeMap.put(2, pm);
		if(night!=0){
			end = 3;
			timeMap.put(3,night);
		}
		for (int i = 1; i <=weekDays; i++) {//星期
			for (int j = 1; j <= morning+am+pm+night; j++) {//节次，不分上下午
				if(j==1){
					rowTitle1.add(BaseConstants.strOfNumberMap.get(i+""));
				}else{
					rowTitle1.add("");
				}
				rowTitle2.add(j+"");
			}
			for (int j = start; j <= end; j++) {//上下午
				for (int k = 1; k <= timeMap.get(j); k++) {//节次
					rowTitle3.add(i+"_"+j+"_"+k);
				}
			}
		}
		List<List<String>> rowTitleList = new ArrayList<List<String>>();
		rowTitleList.add(rowTitle1);
		rowTitleList.add(rowTitle2);
		rowTitleList.add(rowTitle3);
		return rowTitleList;
	}

	/**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo,String acadyear,String semester,String gradeId){
		logger.info("模板校验中......");
		validRowStartNo = "1";
		try{
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList2(acadyear,semester,gradeId).get(0).size());
			List<List<String>> rowTitleList = getRowTitleList2(acadyear,semester,gradeId);
			String errorDataMsg = templateValidate(datas, rowTitleList.get(0));
			if(StringUtils.isNotBlank(errorDataMsg)){
				return errorDataMsg;
			}
			datas.remove(0);
			return templateValidate(datas, rowTitleList.get(1));
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	private String result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	}
	
	private String getRemark(){
		String remark = "填写注意：\n"
				+ "1.单双周填写格式为：科目[单],科目[双]\n"
				+ "2.请勿随意删除模板中的班级名称，务必保证模板中的班级数据完整";
		return remark;
	}
	
}
