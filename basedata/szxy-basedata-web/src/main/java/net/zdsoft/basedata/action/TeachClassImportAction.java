package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

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
@RequestMapping("/basedata/teachclass/classImport")
public class TeachClassImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(TeachClassImportAction.class);
	@Autowired
	private GradeService gradeService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private ClassService classService;
	@Autowired
	private ClassHourService classHourService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@RequestMapping("/index/page")
	public String importIndex(ModelMap map,String acadyear,String semester){
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "教学班");
		// 导入URL 
		map.put("businessUrl", "/basedata/teachclass/classImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/teachclass/classImport/template");
        map.put("exportErrorExcelUrl", "/basedata/teachclass/classImport/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/teachclass/classImport/validate");
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
		return "";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、改变选项后请重新上传模板，同时不要随意修改模板中内容，否则容易导致上传文件与模板的不匹配</p>"
				+"<p>3、导入教学班信息，只做新增，对于教学班下还没有学生的教学班可以修改，否则请自行在页面处单个调整</p>"
				+ "<p style='color:red;'>4、教学班信息：如果选择关联走班课程，那么关联课程不能为空，同时维护场地，否则，如果需要维护时间场地时候，请维护在列名“上课时间及场地”中，请严格按照模板要求认真填写</p>"
				+"<p>5、请仔细查看模板中的提示，请严格根据提示填写数据</p>";
		return desc;
		
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> rowTitle=new ArrayList<String>();
		rowTitle.add("*教学班名称");
		rowTitle.add("*选修/必修");
		rowTitle.add("*课程名称");
		rowTitle.add("*面向对象");
		rowTitle.add("*任课老师编号");
		rowTitle.add("*任课老师姓名");
		rowTitle.add("学分");
		rowTitle.add("满分值");
		rowTitle.add("及格分");
		rowTitle.add("*是否考勤");
		rowTitle.add("上课时间及场地");
		rowTitle.add("关联课程");
		rowTitle.add("关联行政班级");
		rowTitle.add("场地");
		return rowTitle;
	}
	private String getRemark(){
		
		String remark = "填写注意：\n"
				+ "1.带 * 为必填\n"
				+ "2.教学班名称不能超过100个字符\n"
				+ "3.学分、满分值、及格分请填写不超过3位整数。\n"
				+ "4.是否考勤：请填写0或者1 (0不考勤  1考勤)\n"
				+ "5.选修/必修：请填写0或者1 (0选修  1必修) 如果选择必修，只能填写一个年级 ，选择选修，则可以填写多个年级\n"
				+ "6.面向对象请填写年级,若有多个年级，请用\",\"（英文逗号）分隔开\n"
				+ "7.上课时间及场地填写方法：a_b_c(d)(a:星期 b:上下午 c:节次 d:场地名称);例如1_2_3(302)(星期一下午第3节在302上课)\n 其中 a范围1-7 分别代表星期一到星期天;b范围1-3 分别代表上午、下午、晚上;c范围正整数输入;d场地名称。若多个时间请用\",\"（英文逗号）分隔开\n"
				+ "8.对于必修课，如果维护关联课程，那么第7点上课时间及场地无需填写，只要在列名“场地”中维护上课地点;如果有关联课程有班级要求，请填入其中关联一个行政班名称";
		return remark;
	}

	/**
	 * 导入数据
	 */
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		//获取上传数据 第一行行标是0
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
		
		TeachClass  teachClass=null;
		TeachClassEx ex=null;
		List<TeachClass> insertTeachClassList=new ArrayList<TeachClass>();
		List<TeachClassEx> insertExList=new ArrayList<TeachClassEx>();
		List<CourseSchedule> insertScheduleList=new ArrayList<CourseSchedule>();
		
		//取得各个年级的一天上课时间
		List<Grade> gradeList = gradeService.findByUnitId(unitId);
		if(CollectionUtils.isEmpty(gradeList)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(0);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位下不存在正常状态的年级";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		Set<String> samegradeNames=new HashSet<String>();//防止一个学校一个年级名称对应多个年级
		Map<String,String> gradeIdByGradeName=new HashMap<String,String>();//如果名称重复 这边取第一个访问的值
		Map<String,Grade> gradeMap=new HashMap<>();
		Map<String,Integer[]> dayTimeByGradeId=new HashMap<String,Integer[]>();
		for(Grade g:gradeList){
			gradeMap.put(g.getId(), g);
			if(gradeIdByGradeName.containsKey(g.getGradeName())){
				//重复年级
				samegradeNames.add(g.getGradeName());
				continue;
			}
			gradeIdByGradeName.put(g.getGradeName(), g.getId());
			Integer[] t=new Integer[3];
			t[0]=g.getAmLessonCount();
			t[1]=g.getPmLessonCount();
			t[2]=g.getNightLessonCount();
			dayTimeByGradeId.put(g.getId(), t);
		}
		
		
		//已存在的教学班
		TeachClassSearchDto searchdto=new TeachClassSearchDto();
		searchdto.setAcadyearSearch(acadyear);
		searchdto.setSemesterSearch(semester);
		searchdto.setUnitId(unitId);
		searchdto.setIsUsing("1");
		List<TeachClass> teachList = teachClassService.findListByDto(searchdto);

		Map<String,String> idByTeachClassName=new HashMap<String,String>();
		Set<String> sameTeachClassName=new HashSet<String>();
		Set<String> teachClassId=new HashSet<String>();
		
		//如果还未完成的教学班中 有关联的classHourId与老师都一致，那么就是冲突啦
		Map<String,Set<String>> sameRelaCourseTeacher=new HashMap<>();
		//如果还未完成的教学班中 有关联的classHourId与场地都一致，那么就是冲突啦
		Map<String,Set<String>> sameRelaCoursePlace=new HashMap<>();
		
		if(CollectionUtils.isNotEmpty(teachList)){
			for(TeachClass tea:teachList){
				teachClassId.add(tea.getId());
				if(idByTeachClassName.containsKey(tea.getName())){
					sameTeachClassName.add(tea.getName());
				}else{
					idByTeachClassName.put(tea.getName(), tea.getId());
				}
				if(StringUtils.isNotBlank(tea.getRelaCourseId())) {
					if(StringUtils.isNotBlank(tea.getTeacherId()) && !BaseConstants.ZERO_GUID.equals(tea.getTeacherId())) {
						String ss=tea.getGradeId()+"_"+tea.getRelaCourseId()+"_"+tea.getTeacherId();
						if(!sameRelaCourseTeacher.containsKey(ss)) {
							sameRelaCourseTeacher.put(ss, new HashSet<>());
						}
						sameRelaCourseTeacher.get(ss).add(tea.getId());
						
					}
					if(StringUtils.isNotBlank(tea.getPlaceId())) {
						String ss=tea.getGradeId()+"_"+tea.getRelaCourseId()+"_"+tea.getPlaceId();
						if(!sameRelaCoursePlace.containsKey(ss)) {
							sameRelaCoursePlace.put(ss, new HashSet<>());
						}
						sameRelaCoursePlace.get(ss).add(tea.getId());
					}
				}
			}
		}
		
		Set<String> existStuClassId=new HashSet<String>();
		Map<String,List<String>> classStuMap = new HashMap<String, List<String>>();
		if(teachClassId.size()>0){
			List<TeachClassStu> tStuList = teachClassStuService.findByClassIds(teachClassId.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(tStuList)){
				existStuClassId=EntityUtils.getSet(tStuList, TeachClassStu::getClassId);
			}
			for (TeachClassStu teachClassStu : tStuList) {
				if(!classStuMap.containsKey(teachClassStu.getClassId())){
					classStuMap.put(teachClassStu.getClassId(), new ArrayList<String>());
				}
				classStuMap.get(teachClassStu.getClassId()).add(teachClassStu.getStudentId());
			}
		}
		
		//根据教师code 取得单位下所有教师--暂时先用取得所有老师
		//key:teacherCode
		Map<String,List<Teacher>> teacherByCode=new HashMap<String,List<Teacher>>();
		List<Teacher> teacherList = teacherService.findByUnitId(unitId);
		if(CollectionUtils.isNotEmpty(teacherList)){
			for(Teacher t:teacherList){
				if(StringUtils.isNotBlank(t.getTeacherCode())){
					if(!teacherByCode.containsKey(t.getTeacherCode())){
						teacherByCode.put(t.getTeacherCode(), new ArrayList<Teacher>());
					}
					teacherByCode.get(t.getTeacherCode()).add(t);
				}
			}
		}else{
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(0);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位下没有维护教师信息";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		
		
		//该学年学期年级课程开设--暂时不考虑已经维护的年级课程 从选修变必修
		OpenTeachingSearchDto searchDto=new OpenTeachingSearchDto();
		searchDto.setAcadyear(acadyear);
		searchDto.setSemester(semester);
		searchDto.setUnitId(unitId);
		searchDto.setIsDeleted(0);
		List<GradeTeaching> gradeTeachList = gradeTeachingService.findBySearch(searchDto);
		//key:subjectName value:courseid
		Map<String,String> idByCourseName=new HashMap<String,String>();
		Set<String> sameCourseName=new HashSet<String>();
		Set<String> courseIds=new HashSet<String>();
		Map<String,Course> courseMap=new HashMap<String,Course>();
		
		Map<String,Map<String,Set<String>>> courseIdByGradeId=new HashMap<String,Map<String,Set<String>>>();
		if(CollectionUtils.isNotEmpty(gradeTeachList)){
			for(GradeTeaching g:gradeTeachList){
				if(g.getSubjectType()==null){
					continue;
				}
				if(!courseIdByGradeId.containsKey(g.getGradeId())){
					courseIdByGradeId.put(g.getGradeId(), new HashMap<String,Set<String>>());
				}
				String subjectType="";
				if(!BaseConstants.SUBJECT_TYPE_XX.equals(g.getSubjectType()))  {
					subjectType=BaseConstants.SUBJECT_TYPE_BX;
				}else {
					subjectType=BaseConstants.SUBJECT_TYPE_XX;
				}
				if(!courseIdByGradeId.get(g.getGradeId()).containsKey(subjectType)){
					courseIdByGradeId.get(g.getGradeId()).put(subjectType, new HashSet<String>());
				}
				courseIdByGradeId.get(g.getGradeId()).get(subjectType).add(g.getSubjectId());
				courseIds.add(g.getSubjectId());
			}
		}else{
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(0);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位下该学年学期下没有维护年级开设课程";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		
		Map<String,Course> relaCourseMap=new HashMap<String,Course>();
		//关联课程
		List<Course> relsList = courseService.getListByCondition(unitId, new String[]{BaseConstants.SUBJECT_TYPE_VIRTUAL}, BaseConstants.VIRTUAL_COURSE_TYPE, null, null, null,Constant.IS_TRUE);
		
		if(CollectionUtils.isNotEmpty(relsList)) {
			//关联课程 必须是行政班上课的课程 默认关联的科目肯定是走班课程
			courseIds.addAll(EntityUtils.getSet(relsList, e->e.getId()));
		}
		if(courseIds.size()>0){
			List<Course> courseList = courseService.findListByIds(courseIds.toArray(new String[]{}));
			if(CollectionUtils.isEmpty(courseList)){
				String[] errorData=new String[4];
				sequence++;
	            errorData[0]=String.valueOf(0);
	            errorData[1]="";
	            errorData[2]="";
	            errorData[3]="该单位下该学年学期下没有维护年级开设课程";
	            errorDataList.add(errorData);
	            return result(rowDatas.size(),0,0,errorDataList,"");
			}
			
			for(Course c:courseList){
				if(BaseConstants.VIRTUAL_COURSE_TYPE.equals(c.getCourseTypeId())) {
					//虚拟课程
					relaCourseMap.put(c.getId(), c);
				}
				courseMap.put(c.getId(), c);
				if(idByCourseName.containsKey(c.getSubjectName())){
					sameCourseName.add(c.getSubjectName());
				}else{
					idByCourseName.put(c.getSubjectName(), c.getId());
				}
			}
		}
		//场地
		List<TeachPlace> placeList = teachPlaceService.findTeachPlaceListByType(unitId, null);
		Map<String,String> idByPlaceName=new HashMap<String,String>();
		Set<String> samePlaceName=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(placeList)){
			for (TeachPlace string : placeList) {
				if(idByPlaceName.containsKey(string.getPlaceName())){
					samePlaceName.add(string.getPlaceName());
				}else{
					idByPlaceName.put(string.getPlaceName(), string.getId());
				}
			}
		}
		
		//考勤的前期
		Map<String, Object> punchCardTimes = makeDto(new Date(), acadyear, semester, unitId);
		String error="";
		CourseScheduleDto searchTimeDto=null;
		Map<String, Map<String, Set<String>>> scheduleMap=new HashMap<String, Map<String, Set<String>>>();
		//防止原来数据中同个时间地点重复 key:时间_教师id 时间_场地id
		Map<String,Set<String>> classIdByPlaceIdTimes=new HashMap<String,Set<String>>();
		Map<String,Set<String>> classIdByTeacherIdTimes=new HashMap<String,Set<String>>();
		//key:classId value时间_教师id 时间_场地id
		Map<String,Set<String>> oldPlaceIdTimeByClassId=new HashMap<String,Set<String>>();
		Map<String,Set<String>> oldTeacherIdTimeByClassId=new HashMap<String,Set<String>>();
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
					scheduleMap = makeTeacherPlace(list,searchTimeDto,classIdByPlaceIdTimes,classIdByTeacherIdTimes,
							oldPlaceIdTimeByClassId,oldTeacherIdTimeByClassId);
				}
				
			}
		}
		
		//根据学年学期
		List<ClassHour> classHourList = classHourService.findListByUnitId(acadyear, semester, unitId,null,true);
		Map<String,ClassHour> timeByGradeMap=new HashMap<>();
		//key:
		Map<String,ClassHour> timeByClazzSubIdMap=new HashMap<>();
		//暂不考虑同个班级同个subjectId有条数据 
		if(CollectionUtils.isNotEmpty(classHourList)) {
			for(ClassHour classHour:classHourList) {
				if(StringUtils.isNotBlank(classHour.getClassIds())) {
					for(String s:classHour.getClassIds().split(",")) {
						timeByClazzSubIdMap.put(s+classHour.getSubjectId(), classHour);
					}
				}else {
					if(!timeByGradeMap.containsKey(classHour.getGradeId()+classHour.getSubjectId())) {
						timeByGradeMap.put(classHour.getGradeId()+classHour.getSubjectId(), classHour);
					}
				}
			}
		}
		
		//该班级NameDOTO
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findByInGradeIds(EntityUtils.getSet(gradeList, e->e.getId()).toArray(new String[0])), Clazz.class);
		Map<String,Clazz> clazzMap=new HashMap<>();
		Set<String> sameClazzNames=new HashSet<>();
		for(Clazz c:clazzList) {
			if(!clazzMap.containsKey(c.getClassNameDynamic())) {
				clazzMap.put(c.getClassNameDynamic(), c);
			}else {
				sameClazzNames.add(c.getClassNameDynamic());
			}
			
		}
		
		
		Map<String, Set<String>> newUpateTeachTime=new HashMap<>();
		Map<String, String> newUpateTeachMap=new HashMap<>();
		
		Set<String> updateIds=new HashSet<String>();
		int successCount=0;
		boolean isAdd=true;
		//判断
		int i=0;
		//同一张表格不能重复导入教学班
		Set<String> sameName=new HashSet<String>();
        for (String[] array : rowDatas) {
        	i++;
        	teachClass=new TeachClass();
        	ex=null;
        	ExcelTeachDto exDto=new ExcelTeachDto(array);
        	// errorData[2], errorData[3]
        	String[] errorData1=checkColValues(exDto,teachClass,
        			sameCourseName,idByCourseName,relaCourseMap,
        			sameName,sameTeachClassName,idByTeachClassName,
        			teacherByCode);
        	if(errorData1!=null) {
                sequence++;
                errorData1[0]=i+"";
                errorData1[1]="第"+i+"行";
                errorDataList.add(errorData1);
                continue;
        	}
        	
        	//转换成年级ids
        	List<String> gradeIds=checkGradeIds(exDto.getGradeNames(), i, errorDataList, samegradeNames, gradeIdByGradeName);
        	
        	if(CollectionUtils.isEmpty(gradeIds)){
        		sequence++;
        		continue;
        	}
        	
        	Set<String> openCourseId=new HashSet<String>();//班级开设科目
        	openCourseId=makeCourseByGradeId(gradeIds,courseIdByGradeId,teachClass.getClassType());

        	//该对象是否开设对应学科
        	if(CollectionUtils.isEmpty(openCourseId) || (!openCourseId.contains(teachClass.getCourseId()))){
        		String[] errorData=new String[4];
                sequence++;
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=exDto.getSubjectName();
                errorData[3]="面向对象中的年级未开设该课程";
                errorDataList.add(errorData);
                continue;
        	}
        	//获取年级id
        	//必修选修面向对象验证
        	if(TeachClass.CLASS_TYPE_REQUIRED.contains(teachClass.getClassType())){
        		//必修对象只能有一个
        		if(gradeIds.size()>1){
        			String[] errorData=new String[4];
                    sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=exDto.getGradeNames();
                    errorData[3]="必修教学班只能选择一个对象";
                    errorDataList.add(errorData);
                    continue;
        		}
        		teachClass.setGradeId(gradeIds.get(0));
        	}else{
        		teachClass.setGradeId(ArrayUtil.print(gradeIds.toArray(new String[]{})));
        	}
        	
        	isAdd=StringUtils.isNotBlank(teachClass.getId())?false:true;
        	teachClass.setAcadyear(acadyear);
        	teachClass.setSemester(semester);
        	teachClass.setUnitId(unitId);
        	if(isAdd){
        		teachClass.setId(UuidUtils.generateUuid());
        	}
        	teachClass.setIsDeleted(0);
        	teachClass.setIsUsing("1");
        	teachClass.setCreationTime(new Date());
        	teachClass.setModifyTime(new Date());
        	
        	//原来班级下学生
        	List<Student> studentList=new ArrayList<>();
        	if(!isAdd) {
        		if(CollectionUtils.isNotEmpty(classStuMap.get(teachClass.getId()))) {
        			studentList = studentService.findListByIdIn(classStuMap.get(teachClass.getId()).toArray(new String[0]));
        		}
        	}
        	
        	//一、必修+存在关联课程
        	
        	if(TeachClass.CLASS_TYPE_REQUIRED.contains(teachClass.getClassType()) && StringUtils.isNotBlank(exDto.getRelaCourseName())) {
        		//1、验证关联课程id
        		String[] errorData=checkXnCourseId(exDto.getRelaCourseName(), teachClass, sameCourseName, idByCourseName, relaCourseMap);
        		if(errorData!=null) {
                    sequence++;
        			errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorDataList.add(errorData);
                    continue;
        		}
        		//结合班级名称获取具体关联id
            	ClassHour hh=null;
            	if(StringUtils.isNotBlank(exDto.getRelaClazzName())) {
            		if(sameClazzNames.contains(exDto.getRelaClazzName())) {
            			errorData=new String[4];
                        sequence++;
            			errorData[0]=i+"";
                        errorData[1]="第"+i+"行";
                        errorData[2]=exDto.getRelaClazzName();
                        errorData[3]="关联班级找不到唯一";
                        errorDataList.add(errorData);
                        continue;
            		}
            		if(!clazzMap.containsKey(exDto.getRelaClazzName())) {
            			errorData=new String[4];
                        sequence++;
            			errorData[0]=i+"";
                        errorData[1]="第"+i+"行";
                        errorData[2]=exDto.getRelaClazzName();
                        errorData[3]="关联班级找不到";
                        errorDataList.add(errorData);
                        continue;
            		}
            		String clazzId=clazzMap.get(exDto.getRelaClazzName()).getId();
            		hh = timeByClazzSubIdMap.get(clazzId+teachClass.getXnCourseId());
                	if(hh==null) {
                		hh=timeByGradeMap.get(teachClass.getGradeId()+teachClass.getXnCourseId());
                	}
            	}else {
                	hh=timeByGradeMap.get(teachClass.getGradeId()+teachClass.getXnCourseId());
            	}
            	if(hh==null) {
            		errorData=new String[4];
                    sequence++;
        			errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=exDto.getRelaCourseName();
                    errorData[3]="对应的关联课程，年级下行政班未安排上课";
                    errorDataList.add(errorData);
                    continue;
            	}
            	teachClass.setRelaCourseId(hh.getId());
            	//----验证学段
            	Grade gg=gradeMap.get(teachClass.getGradeId());
            	if(!relaCourseMap.get(hh.getSubjectId()).getSection().contains(gg.getSection().toString())) {
            		//虚拟课程 不维护必修课
            		errorData=new String[4];
                    sequence++;
                    errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]=exDto.getRelaCourseName();
                    errorData[3]="关联课程与面向对象学段不符合";
                    errorDataList.add(errorData);
                    continue;
            	}
            	errorData=checkPlaceName(teachClass, exDto.getPlaceNames(), samePlaceName, idByPlaceName);
            	if(errorData!=null) {
                    sequence++;
        			errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorDataList.add(errorData);
                    continue;
        		}
            	//验证存在的学生不在关联班级限制内
        		if(!isAdd && CollectionUtils.isNotEmpty(studentList)) {
        			if(StringUtils.isNotBlank(hh.getClassIds())) {
        				Set<String> clazzIds = EntityUtils.getSet(studentList, e->e.getClassId());
        				String classIdss=hh.getClassIds();
        				Set<String> leftClassIds=clazzIds.stream().filter(e->!classIdss.contains(e)).collect(Collectors.toSet());
        				if(CollectionUtils.isNotEmpty(leftClassIds)) {
        					errorData=new String[4];
                            sequence++;
                            errorData[0]=i+"";
                            errorData[1]="第"+i+"行";
                            errorData[2]=exDto.getRelaClazzName();
                            errorData[3]="已存在学生不属于虚拟课程关联的行政班内";
                            errorDataList.add(errorData);
                            continue;
        				}
        			}
        		}
        		//时间校验
        		Set<String> timeset = new HashSet<>();
        		/*******时间校验******************************/
        		if(CollectionUtils.isNotEmpty(hh.getTimeList())) {
        			timeset.addAll(hh.getTimeList());
        			boolean isBug=false;
        			//1：验证教师场地
        			//纯新的校验
    				for(String ttt:timeset){
    					//新增
    					//1-1:场地
    					if(StringUtils.isNotBlank(teachClass.getPlaceId())) {
    						if(classIdByPlaceIdTimes.containsKey(ttt+"_"+teachClass.getPlaceId())){
        						Set<String> clIds = classIdByPlaceIdTimes.get(ttt+"_"+teachClass.getPlaceId());
        						if(!isAdd) {
        							if(clIds.size()==1 && clIds.contains(teachClass.getId())){
            							//这里原来的时间
            							continue;
        							}
        						}
    							if(CollectionUtils.isNotEmpty(clIds)){
    								String[] tarr = ttt.split("_");
    								errorData=new String[4];
            	                    sequence++;
                                    errorData[0]=i+"";
            	                    errorData[1]="第"+i+"行";
            	                    errorData[2]=exDto.getTimePlaceStr();
            	                    errorData[3]="该场地在"+makeTimeMess2(tarr)+"已经被使用";
            	                    errorDataList.add(errorData);
            	                    isBug=true;
            	                    break;
    							}
        					}
    					}
    					//1-2：教师
    					if(classIdByTeacherIdTimes.containsKey(ttt+"_"+teachClass.getTeacherId())){
    						Set<String> clIds = classIdByTeacherIdTimes.get(ttt+"_"+teachClass.getTeacherId());
    						if(!isAdd) {
    							if(clIds.size()==1 && clIds.contains(teachClass.getId())){
        							//这里原来的时间
        							continue;
    							}
    						}
    						if(CollectionUtils.isNotEmpty(clIds)){
    							String[] tarr = ttt.split("_");
								errorData=new String[4];
        	                    sequence++;
                                // errorData[0]=String.valueOf(sequence);
                                errorData[0]=i+"";
        	                    errorData[1]="第"+i+"行";
        	                    errorData[2]=exDto.getTeachClassName();
        	                    errorData[3]="该教师在"+makeTimeMess2(tarr)+"已经在其他班级上课";
        	                    errorDataList.add(errorData);
        	                    isBug=true;
        	                    break;
							}
    					}
    				}
    				if(isBug) {
    					continue;
    				}
        			//验证学生
    				if(!isBug && existStuClassId.contains(teachClass.getId())) {
    					
						//存在学生
    					String msg=checkStudentTime(searchTimeDto, studentList, timeset, teachClass.getId(),newUpateTeachTime,newUpateTeachMap);
						if(StringUtils.isNotBlank(msg)){
							errorData=new String[4];
    	                    sequence++;
                            errorData[0]=i+"";
    	                    errorData[1]="第"+i+"行";
    	                    errorData[2]=exDto.getRelaCourseName();
    	                    errorData[3]=msg;
    	                    errorDataList.add(errorData);
    	                    isBug=true;
						}
    				}
        			if(isBug){
        				continue;
        			}
        			/************时间校验end****************/
        			//验证结束
        			if(!isAdd) {
        				newUpateTeachTime.put(teachClass.getId(), new HashSet<>());
        				newUpateTeachMap.put(teachClass.getId(), teachClass.getName());
        			}
        			
        			for(String ttt:timeset){
        				if(!isAdd){
        					//去除原来设置
        					Set<String> timePlace = oldPlaceIdTimeByClassId.get(teachClass.getId());
        					if(CollectionUtils.isNotEmpty(timePlace)){
        						for(String mm:timePlace){
        							if(classIdByPlaceIdTimes.containsKey(mm)){
        								classIdByPlaceIdTimes.get(mm).remove(teachClass.getId());
        							}
        						}
        					}
        					Set<String> timeTeacherId = oldTeacherIdTimeByClassId.get(teachClass.getId());
        					if(CollectionUtils.isNotEmpty(timeTeacherId)){
        						for(String mm:timeTeacherId){
        							if(classIdByTeacherIdTimes.containsKey(mm)){
        								classIdByTeacherIdTimes.get(mm).remove(teachClass.getId());
        							}
        						}
        					}
        					newUpateTeachTime.get(teachClass.getId()).add(ttt);
        				}
        				String[] tarr = ttt.split("_");
        				TeachClassEx st=new TeachClassEx();
        				st.setDayOfWeek(Integer.parseInt(tarr[0]));
        				st.setPeriod(Integer.parseInt(tarr[2]));
        				st.setPeriodInterval(tarr[1]);
        				st.setPlaceId(teachClass.getPlaceId());
        				//课程表
                		List<CourseSchedule> ll = makeCourseScheduleList(teachClass, searchTimeDto, st);
                		//增加教师+场地 新的校验
                		if(CollectionUtils.isNotEmpty(ll)){
                			insertScheduleList.addAll(ll);
                			String stt=st.getDayOfWeek()+"_"+st.getPeriodInterval()+"_"+st.getPeriod();
                			if(StringUtils.isNotBlank(teachClass.getPlaceId())) {
                				if(!classIdByPlaceIdTimes.containsKey(stt+"_"+teachClass.getPlaceId())){
                        			classIdByPlaceIdTimes.put(stt+"_"+teachClass.getPlaceId(), new HashSet<String>());
                        		}
                        		classIdByPlaceIdTimes.get(stt+"_"+teachClass.getPlaceId()).add(teachClass.getId());
                			}
                    		
                    		if(!classIdByTeacherIdTimes.containsKey(stt+"_"+teachClass.getTeacherId())){
                    			classIdByTeacherIdTimes.put(stt+"_"+teachClass.getTeacherId(), new HashSet<String>());
                    		}
                    		classIdByTeacherIdTimes.get(stt+"_"+teachClass.getTeacherId()).add(teachClass.getId());
                			
                		}
        			}
        			
        			
        			
        			if(StringUtils.isNotBlank(teachClass.getTeacherId())) {
        				String s=teachClass.getGradeId()+"_"+teachClass.getRelaCourseId()+"_"+teachClass.getTeacherId();
        				if(!sameRelaCourseTeacher.containsKey(s)) {
        					sameRelaCourseTeacher.put(s, new HashSet<>());
        				}
            			sameRelaCourseTeacher.get(s).add(teachClass.getId());
            		}
            		if(StringUtils.isNotBlank(teachClass.getPlaceId())) {
            			String s=teachClass.getGradeId()+"_"+teachClass.getRelaCourseId()+"_"+teachClass.getPlaceId();
        				if(!sameRelaCoursePlace.containsKey(s)) {
        					sameRelaCoursePlace.put(s, new HashSet<>());
        				}
        				sameRelaCoursePlace.get(s).add(teachClass.getId());
            		}
        			
        		}else {
        			
        			/*******对应虚拟课程下没有时间 那么就验证同一个老师不能在同样的关联下******************************/
        			if(StringUtils.isNotBlank(teachClass.getTeacherId())) {
        				String s=teachClass.getGradeId()+"_"+teachClass.getRelaCourseId()+"_"+teachClass.getTeacherId();
        				//一般就一个 2个就是冲突
        				Set<String> set = sameRelaCourseTeacher.get(s);
            			if(CollectionUtils.isNotEmpty(set) && !set.contains(teachClass.getId())) {
            				errorData=new String[4];
    	                    sequence++;
                            // errorData[0]=String.valueOf(sequence);
                            errorData[0]=i+"";
    	                    errorData[1]="第"+i+"行";
    	                    errorData[2]=exDto.getTeachClassName();
    	                    errorData[3]="该教师已经有再其他关联该同样时间的"+exDto.getRelaCourseName()+"上课";
    	                    errorDataList.add(errorData);
    	                    continue;
            			}else {
            				if(set==null) {
            					sameRelaCourseTeacher.put(s, new HashSet<>());
            				}
                			sameRelaCourseTeacher.get(s).add(teachClass.getId());
            			}
            		}
        			
            		if(StringUtils.isNotBlank(teachClass.getPlaceId())) {
            			String s=teachClass.getGradeId()+"_"+teachClass.getRelaCourseId()+"_"+teachClass.getPlaceId();
        				//一般就一个 2个就是冲突
        				Set<String> set = sameRelaCoursePlace.get(s);
            			if(CollectionUtils.isNotEmpty(set) && !set.contains(teachClass.getId())) {
            				errorData=new String[4];
    	                    sequence++;
                            // errorData[0]=String.valueOf(sequence);
                            errorData[0]=i+"";
    	                    errorData[1]="第"+i+"行";
    	                    errorData[2]=exDto.getPlaceNames();
    	                    errorData[3]="有其他关联该同样时间的"+exDto.getPlaceNames()+"使用该场地";
    	                    errorDataList.add(errorData);
    	                    continue;
            			}else {
            				if(set==null) {
            					sameRelaCoursePlace.put(s, new HashSet<>());
            				}
            				sameRelaCoursePlace.get(s).add(teachClass.getId());
            			}
            		}
            		
            		/*******对应虚拟课程下没有时间 那么就验证同一个老师不能在同样的关联下  end******************************/
        		}
        		
        	}else {
        		teachClass.setPlaceId(null);
        		teachClass.setRelaCourseId(null);
        		if(teachClass.getPunchCard()==1){
        			if(StringUtils.isBlank(exDto.getTimePlaceStr())) {
        				String[] errorData=new String[4];
                        sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
                        errorData[1]="第"+i+"行";
                        errorData[2]="";
                        errorData[3]="上课时间及场地不能为空";
                        errorDataList.add(errorData);
                        continue;
	        		}
        		}
        		if(StringUtils.isNotBlank(exDto.getTimePlaceStr())) {
        			
        			Set<String> timeset=new HashSet<String>();
        			//key:时间 value:placeId,placeName
        			Map<String,String[]> placeIdByTimes=new HashMap<String,String[]>();
        			//验证时间与场地
        			String[] res = compareTimePlace(exDto.getTimePlaceStr(), dayTimeByGradeId, gradeIds, samePlaceName, idByPlaceName, timeset, placeIdByTimes);
        			if(res!=null) {
        				String[] errorData=new String[4];
	                    sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
	                    errorData[1]="第"+i+"行";
	                    errorData[2]=res[0];
	                    errorData[3]=res[1];
	                    errorDataList.add(errorData);
	                    continue;
        			}
        			
            		if(searchTimeDto!=null){
            			List<TeachClassEx> texList=new ArrayList<TeachClassEx>();
            			//验证学生重复问题---修改原有数据
            			if(existStuClassId.contains(teachClass.getId())){
    						String msg=checkStudentTime(searchTimeDto, studentList, timeset, teachClass.getId(),newUpateTeachTime,newUpateTeachMap);
    						if(StringUtils.isNotBlank(msg)){
    							String[] errorData=new String[4];
        	                    sequence++;
                                errorData[0]=i+"";
        	                    errorData[1]="第"+i+"行";
        	                    errorData[2]=exDto.getTimePlaceStr();
        	                    errorData[3]=msg;
        	                    errorDataList.add(errorData);
        	                    continue;
    						}
            			}
            			boolean isBug=false;
            			//验证本身教学班的重复
            			String mess=null;
            			for(String ttt:timeset){
            				String[] tarr = ttt.split("_");
            				mess = makeTimeMess(tarr);
            				ex=new TeachClassEx();
                    		ex.setPlaceId(placeIdByTimes.get(ttt)[0]);
                    		ex.setDayOfWeek(Integer.parseInt(tarr[0])-1);
                    		ex.setPeriodInterval(String.valueOf((Integer.parseInt(tarr[1])+1)));
                    		ex.setPeriod(Integer.parseInt(tarr[2]));
                    		//判断重复--教师与场地
                			String stt=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
                			
                			//新增
        					if(classIdByPlaceIdTimes.containsKey(stt+"_"+ex.getPlaceId())){
        						Set<String> clIds = classIdByPlaceIdTimes.get(stt+"_"+ex.getPlaceId());
    							if(CollectionUtils.isNotEmpty(clIds)){
    								if(!isAdd) {
            							if(clIds.size()==1 && clIds.contains(teachClass.getId())){
                							//这里原来的时间
                							continue;
                						}
            						}
    								String[] errorData=new String[4];
            	                    sequence++;
                                    errorData[0]=i+"";
            	                    errorData[1]="第"+i+"行";
            	                    errorData[2]=placeIdByTimes.get(ttt)[1];
            	                    errorData[3]="该场地在"+mess+"已经被使用";
            	                    errorDataList.add(errorData);
            	                    isBug=true;
            	                    break;
    							}
        					}
        					if(classIdByTeacherIdTimes.containsKey(stt+"_"+teachClass.getTeacherId())){
        						Set<String> clIds = classIdByTeacherIdTimes.get(stt+"_"+teachClass.getTeacherId());
        						if(CollectionUtils.isNotEmpty(clIds)){
        							if(!isAdd) {
            							if(clIds.size()==1 && clIds.contains(teachClass.getId())){
                							continue;
                						}
            						}
        							String[] errorData=new String[4];
            	                    sequence++;
                                    // errorData[0]=String.valueOf(sequence);
                                    errorData[0]=i+"";
            	                    errorData[1]="第"+i+"行";
            	                    errorData[2]=exDto.getTeacherName();
            	                    errorData[3]="该教师在"+mess+"已经在其他班级上课";
            	                    errorDataList.add(errorData);
            	                    isBug=true;
            	                    break;
    							}
        					}
                			texList.add(ex);
            			}
            			if(isBug){
            				continue;
            			}
            			//验证结束
            			
                		if(CollectionUtils.isNotEmpty(texList)){
                			if(!isAdd) {
                				newUpateTeachTime.put(teachClass.getId(), new HashSet<>());
                				newUpateTeachMap.put(teachClass.getId(), teachClass.getName());
                			}
                			for(TeachClassEx st:texList){
                				st.setTeachClassId(teachClass.getId());
                				st.setId(UuidUtils.generateUuid());
                				insertExList.add(st);
                				if(!isAdd){
                					//去除原来设置
                					Set<String> timePlace = oldPlaceIdTimeByClassId.get(teachClass.getId());
                					if(CollectionUtils.isNotEmpty(timePlace)){
                						for(String mm:timePlace){
                							if(classIdByPlaceIdTimes.containsKey(mm)){
                								classIdByPlaceIdTimes.get(mm).remove(teachClass.getId());
                							}
                						}
                					}
                					Set<String> timeTeacherId = oldTeacherIdTimeByClassId.get(teachClass.getId());
                					if(CollectionUtils.isNotEmpty(timeTeacherId)){
                						for(String mm:timeTeacherId){
                							if(classIdByTeacherIdTimes.containsKey(mm)){
                								classIdByTeacherIdTimes.get(mm).remove(teachClass.getId());
                							}
                						}
                					}
                					newUpateTeachTime.get(teachClass.getId()).add(st.getDayOfWeek()+"_"+st.getPeriodInterval()+"_"+st.getPeriod());
                				}
                				//课程表
                        		List<CourseSchedule> ll = makeCourseScheduleList(teachClass, searchTimeDto, st);
                        		if(CollectionUtils.isNotEmpty(ll)){
                        			insertScheduleList.addAll(ll);
                        			String stt=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
                            		if(!classIdByPlaceIdTimes.containsKey(stt+"_"+ex.getPlaceId())){
                            			classIdByPlaceIdTimes.put(stt+"_"+ex.getPlaceId(), new HashSet<String>());
                            		}
                            		classIdByPlaceIdTimes.get(stt+"_"+ex.getPlaceId()).add(teachClass.getId());
                            		if(!classIdByTeacherIdTimes.containsKey(stt+"_"+teachClass.getTeacherId())){
                            			classIdByTeacherIdTimes.put(stt+"_"+teachClass.getTeacherId(), new HashSet<String>());
                            		}
                            		classIdByTeacherIdTimes.get(stt+"_"+teachClass.getTeacherId()).add(teachClass.getId());
                        			
                        		}
                			}
                		}
                		
                		
            		}else{
            			//只需要保存教学班
            		}
        			
        			
        			
        		}
        	}
        	
        	
        	if(!isAdd){
        		//修改
        		updateIds.add(teachClass.getId());
        	}
        	sameName.add(teachClass.getName());
        	insertTeachClassList.add(teachClass);
        	successCount++;
        	
        }//for结尾

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
        
		if(CollectionUtils.isNotEmpty(insertTeachClassList)){
			try{
				CourseScheduleDto deleteDto=null;
				if(updateIds.size()>0){
					if(searchTimeDto!=null){
						//删除课表 
						deleteDto=new CourseScheduleDto();
						deleteDto.setSchoolId(searchTimeDto.getSchoolId());
						deleteDto.setAcadyear(acadyear);
						deleteDto.setSemester(Integer.parseInt(semester));
						deleteDto.setClassIds(updateIds.toArray(new String[]{}));
						deleteDto.setWeekOfWorktime1(searchTimeDto.getWeekOfWorktime1());
						//deleteDto.setWeekOfWorktime2(searchTimeDto.getWeekOfWorktime2());
						deleteDto.setDayOfWeek1(searchTimeDto.getDayOfWeek1());
						//deleteDto.setDayOfWeek2(searchTimeDto.getDayOfWeek2());
						deleteDto.setWeekOfWorktime2(0);
					}else{
						deleteDto=new CourseScheduleDto();
						deleteDto.setClassIds(updateIds.toArray(new String[]{}));
					}
				}
				teachClassService.insertTeachClassAndCourseSchedule(insertTeachClassList, insertExList, insertScheduleList,deleteDto);
			}catch(Exception e){
				e.printStackTrace();
				return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
			}
		}
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
		return result;
	}
	//场地id
	private String[] checkPlaceName(TeachClass teachClass,String placeNames,
			Set<String> samePlaceName ,Map<String,String> idByPlaceName) {
		if(StringUtils.isNotBlank(placeNames)) {
			if(samePlaceName.contains(placeNames)){
				String[] errorData=new String[4];
	            errorData[0]="";
	            errorData[1]="";
                errorData[2]=placeNames;
                errorData[3]="场地名称未找到唯一地点";
                return errorData;
    		}
			if(!idByPlaceName.containsKey(placeNames)){
				String[] errorData=new String[4];
	            errorData[0]="";
	            errorData[1]="";
                errorData[2]=placeNames;
                errorData[3]="场地未找到";
                return errorData;
    		}
			teachClass.setPlaceId(idByPlaceName.get(placeNames));
		}else {
			teachClass.setPlaceId(null);
		}
		return null;
	}
	//获取xnCourseId
	private String[] checkXnCourseId(String relaCourseName,TeachClass teachClass,
			Set<String> sameCourseName,Map<String, String> idByCourseName,Map<String, Course> relaCourseMap) {
		if(sameCourseName.contains(relaCourseName)){
    		String[] errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=relaCourseName;
            errorData[3]="关联课程未找到唯一课程";
            return errorData;
    	}
    	if(!idByCourseName.containsKey(relaCourseName)){
    		String[] errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=relaCourseName;
            errorData[3]="关联课程未找到课程";
            return errorData;
    	}
    	
    	if(relaCourseMap.get(idByCourseName.get(relaCourseName))==null) {
    		//虚拟课程 不维护必修课
    		String[] errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=relaCourseName;
            errorData[3]="关联课程不是走班课程";
            return errorData;
    	}
    	teachClass.setXnCourseId(relaCourseMap.get(idByCourseName.get(relaCourseName)).getId());
		return null;
	}
	private List<String> checkGradeIds(String gradeNames,
			int i,List<String[]> errorDataList,
			Set<String> samegradeNames,Map<String,String> gradeIdByGradeName) {
		//获取年级ids
		//将中文，改为英文， 将空格替换成空字符
    	gradeNames.replace(" ", "");
    	gradeNames.replace("，", ",");
    	List<String> gradeNamesList=new ArrayList<String>();
    	if(gradeNames.indexOf(",")>=0){
    		//有
    		String[] gradeNamesArr = gradeNames.split(",");
    		//去除空
    		for(String s:gradeNamesArr){
    			if(StringUtils.isNotBlank(s)){
    				gradeNamesList.add(s);
    			}
    		}
    	}else{
    		gradeNamesList.add(gradeNames);
    	}
    	if(CollectionUtils.isEmpty(gradeNamesList)){
    		String[] errorData=new String[4];
            errorData[0]=i+"";
            errorData[1]="第"+i+"行";
            errorData[2]="";
            errorData[3]="面向对象未找到对应年级";
            errorDataList.add(errorData);
            return null;
    	}
    	//gradeNamesList 转换成gradeIds
    	List<String> gradeIds=new ArrayList<String>();
    	boolean bug=false;
    	for(String s:gradeNamesList){
    		if(samegradeNames.contains(s)){
    			String[] errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=s;
                errorData[3]="面向对象中"+s+"未找到唯一年级";
                errorDataList.add(errorData);
                bug=true;
                break;
    		}
    		if(!gradeIdByGradeName.containsKey(s)){
    			String[] errorData=new String[4];
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=s;
                errorData[3]="面向对象中"+s+"未找到对应年级";
                errorDataList.add(errorData);
                bug=true;
                break;
    		}
    		gradeIds.add(gradeIdByGradeName.get(s));
    	}
    	if(bug) {
    		return null;
    	}
    	if(CollectionUtils.isEmpty(gradeIds)){
    		String[] errorData=new String[4];
            errorData[0]=i+"";
            errorData[1]="第"+i+"行";
            errorData[2]=gradeNames;
            errorData[3]="面向对象未找到对应年级";
            errorDataList.add(errorData);
            return null;
    	}
		return gradeIds;
	}
	
	private String[] checkColValues(ExcelTeachDto exDto, TeachClass teachClass,
			Set<String> sameCourseName,Map<String, String> idByCourseName,Map<String, Course> relaCourseMap,
			Set<String> sameName,Set<String> sameTeachClassName,Map<String, String> idByTeachClassName,
			Map<String, List<Teacher>> teacherByCode) {
		String[] errorData=null;
		//1：判断不为空的参数
    	String[] cols=new String[] {exDto.getTeachClassName(),exDto.getSubjectType(),exDto.getSubjectName(),
    			exDto.getGradeNames(),exDto.getTeacherCode(),exDto.getTeacherName(),exDto.getPunchCardStr()};
    	String[] colNames=new String[] {"教学班名称","选修/必修","课程名称","面向对象","任课老师编号","任课老师姓名",
    			"是否考勤"};
    	
    	String errorBlank=checkBlank(cols,colNames);
		if(StringUtils.isNotBlank(errorBlank)) {
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]="";
            errorData[3]=errorBlank+"不能为空";
            return errorData;
    	}
		
		//1:字符长度
    	if(exDto.getTeachClassName().getBytes().length>100){
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getTeachClassName();
            errorData[3]="教学班名称不能超过100个字符";
            return errorData;
    	}
		
		//2:获取科目id
		if(sameCourseName.contains(exDto.getSubjectName())){
			errorData=new String[4];
			errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getSubjectName();
            errorData[3]="课程名称未找到唯一课程";
            return errorData;
    	}
    	if(!idByCourseName.containsKey(exDto.getSubjectName())){
    		errorData=new String[4];
    		errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getSubjectName();
            errorData[3]="课程名称未找到课程";
            return errorData;
    	}
    	
    	if(relaCourseMap.get(idByCourseName.get(exDto.getSubjectName()))!=null) {
    		//虚拟课程 不维护必修课
			 errorData=new String[4];
			 errorData[0]="";
	         errorData[1]="";
	         errorData[2]=exDto.getSubjectName();
	         errorData[3]="该课程是走班课程名称，只用于时间维护，不能用于开设教学班";
	         return errorData;
    	}
    	teachClass.setCourseId(idByCourseName.get(exDto.getSubjectName()));
    	//3:必修选修
    	//整数
    	if(!("0".equals(exDto.getSubjectType()) || "1".equals(exDto.getSubjectType()))){
    		errorData=new String[4];
			errorData[0]="";
	        errorData[1]="";
            errorData[2]=exDto.getSubjectType();
            errorData[3]="选修/必修只能输入0或者1；0代表选修、 1代表必修";
            return errorData;
    	}
    	if("0".equals(exDto.getSubjectType())){
    		teachClass.setClassType(TeachClass.CLASS_TYPE_ELECTIVE);
    	}else{
    		teachClass.setClassType(TeachClass.CLASS_TYPE_REQUIRED);
    	}
    	//4、是否考勤	
    	if(!("0".equals(exDto.getPunchCardStr()) || "1".equals(exDto.getPunchCardStr()))){
    		errorData=new String[4];
    		errorData[0]="";
	        errorData[1]="";
            errorData[2]=exDto.getPunchCardStr();
            errorData[3]="是否考勤只能输入0或者1；0代表不考勤、 1代表考勤";
            return errorData;
    	}
    	teachClass.setPunchCard(Integer.parseInt(exDto.getPunchCardStr()));
    	//5、学分 满分值 及格分 数值校验
    	String[] numCol=new String[] {exDto.getCreditStr(),exDto.getFullMarkStr(),exDto.getPassMarkStr()};
    	String[] numColName=new String[] {"学分","满分值","及格分"};
    	for(int i=0;i<numCol.length;i++) {
    		if(StringUtils.isNotBlank(numCol[i])){
        		try{
        			Integer num = Integer.parseInt(numCol[i]);
        			if(num<0 || num>999){
        				errorData=new String[4];
                        errorData[0]="";
                        errorData[1]="";
                        errorData[2]=numCol[i];
                        errorData[3]=numColName[i]+"必须为不超过三位的非负整数";
                        return errorData;
    				}
        			if(i==0) {
        				teachClass.setCredit(num);
        			}else if(i==1) {
        				teachClass.setFullMark(num);
        			}else if(i==2) {
        				teachClass.setPassMark(num);
        			}
        			
        		}catch (NumberFormatException e) {
        			errorData=new String[4];
                    errorData[0]="";
                    errorData[1]="";
                    errorData[2]=numCol[i];
                    errorData[3]=numColName[i]+"必须为不超过三位的非负整数";
                    return errorData;
        		}
        	}
    	}
    	
    	//6、教学班名称
    	if(sameName.contains(exDto.getTeachClassName())){
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getTeachClassName();
            errorData[3]="在之前的数据中已经存在"+exDto.getTeachClassName()+"班级";
            return errorData;
    	}
    	if(sameTeachClassName.contains(exDto.getTeachClassName())){
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getTeachClassName();
            errorData[3]="教学班名称未找到唯一";
            return errorData;
    	}
    	//是否已存在
    	if(idByTeachClassName.containsKey(exDto.getTeachClassName())){
    		/*
    		if(existStuClassId.contains(idByTeachClassName.get(teachClassName))){
    			String[] errorData=new String[4];
                sequence++;
                errorData[0]=String.valueOf(sequence);
                errorData[1]="第"+i+"行";
                errorData[2]=teachClassName;
                errorData[3]="该教学班下已经存在学生，不能修改";
                errorDataList.add(errorData);
                continue;
    		}
    		*/
    		teachClass.setId(idByTeachClassName.get(exDto.getTeachClassName()));
    	}
    	teachClass.setName(exDto.getTeachClassName());
    	// 6、验证老师

    	if(!teacherByCode.containsKey(exDto.getTeacherCode())){
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getTeacherCode();
            errorData[3]="任课老师编号不存在";
            return errorData;
    	}
    	List<Teacher> tList = teacherByCode.get(exDto.getTeacherCode());
    	String teacherId = makeTeacherIdByTeacherName(tList,exDto.getTeacherName());
    	if(StringUtils.isBlank(teacherId)){
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getTeacherName();
            errorData[3]="任课老师名称与编号不匹配";
            return errorData;
    	}
    	if("not only".equals(teacherId)){
    		errorData=new String[4];
            errorData[0]="";
            errorData[1]="";
            errorData[2]=exDto.getTeacherCode();
            errorData[3]="任课老师名称与编号不唯一";
            return errorData;
    	}
    	teachClass.setTeacherId(teacherId);
    	
    	
    	
		//如果有虚拟课程 获取虚拟课程id	
		
		return null;
	}
	private String checkBlank(String[] cols, String[] colNames) {
		for(int i=0;i<cols.length;i++) {
			if(StringUtils.isBlank(cols[i])) {
				return colNames[i];
			}
		}
		return null;
	}
	/**
	 * 
	 * @param timePlaceStr 输入的上课时间与场地
	 * @param dayTimeByGradeId
	 * @param gradeIds
	 * @param samePlaceName
	 * @param idByPlaceName
	 * @param timeset 返回的时间集合
	 * @param placeIdByTimes 返回对应时间的场地
	 * @return
	 */
	private String[] compareTimePlace(String timePlaceStr,Map<String,Integer[]> dayTimeByGradeId,
			List<String> gradeIds,Set<String> samePlaceName,Map<String,String> idByPlaceName,
			Set<String> timeset,Map<String,String[]> placeIdByTimes) {
	
		timePlaceStr=timePlaceStr.replaceAll("（", "(");
		timePlaceStr=timePlaceStr.replaceAll("）", ")");
		timePlaceStr=timePlaceStr.replaceAll("，", ",");
		
		List<String[]> timepalceList=new ArrayList<String[]>();
		//解析规则
		String mess = checkTimeAndPlace(timePlaceStr, timepalceList);
		if(StringUtils.isNotBlank(mess)){
			
			return new String[] {mess,"上课时间及场地未按照规则：请看提示"};
		}
		if(CollectionUtils.isEmpty(timepalceList)){
            return new String[] {"未找到正确时间地点","上课时间及场地未按照规则：请看提示"};
		}
		
		//时间是否符合
		Integer[] times=makeMaxTimes(dayTimeByGradeId,gradeIds);
		if(times[0]==0 && times[1]==0 && times[2]==0){
            return new String[] {"","面向对象中的年级未设置节次数"};
		}
		
		//判断场地是否存在
		for(String[] tp:timepalceList){
			String t=tp[0];
			String p=tp[1];
			if(samePlaceName.contains(p)){
                return new String[] {p,"场地名称未找到唯一地点"};
    		}
			if(!idByPlaceName.containsKey(p)){
                return new String[] {p,"场地未找到"};
    		}
			//时间是否符合年级设置
			String[] tarr = t.split("_");
			mess=makeTimeMess(tarr);
			if("1".equals(tarr[1])){
    			//上午
    			int am=times[0]==null?0:times[0];
    			if(Integer.parseInt(tarr[2])<=0 || Integer.parseInt(tarr[2])>am){
                    return new String[] {t,mess+"不存在，上午只有"+am+"节"};
    			}
    		}else if("2".equals(tarr[1])){
    			//下午
    			int pm=times[1]==null?0:times[1];
    			if(Integer.parseInt(tarr[2])<=0 || Integer.parseInt(tarr[2])>pm){
                    return new String[] {t,mess+"不存在，下午只有"+pm+"节"};
    			}
    		}else{
    			//晚上
    			int nm=times[2]==null?0:times[2];
    			if(Integer.parseInt(tarr[2])<=0 || Integer.parseInt(tarr[2])>nm){
                    return new String[] {t,mess+"不存在，晚上只有"+nm+"节"};
    			}
    		}
			
			if(timeset.contains(t)){
                return new String[] {t,"时间重复维护"};
			}
			timeset.add(t);
			placeIdByTimes.put(t, new String[]{idByPlaceName.get(p),p});
		}
		return null;
	}
	
	/**
	 * 取得一天上课时间节次
	 * @param dayTimeByGradeId
	 * @param gradeIds
	 * @return
	 */
	private Integer[] makeMaxTimes(Map<String, Integer[]> dayTimeByGradeId,
			List<String> gradeIds) {
		Integer[] retunArr=new Integer[3];
		int am=0;
		int pm=0;
		int nm=0;
		for(String s:gradeIds){
			if(!dayTimeByGradeId.containsKey(s)){
				continue;
			}
			Integer[] ss = dayTimeByGradeId.get(s);
			int am1=ss[0]==null?0:ss[0];
			int pm1=ss[1]==null?0:ss[1];
			int nm1=ss[2]==null?0:ss[2];
			if(am1>0){
				am=am1;
			}
			if(pm1>pm){
				pm=pm1;
			}
			if(nm1>nm){
				nm=nm1;
			}
		}
		retunArr[0]=am;
		retunArr[1]=pm;
		retunArr[2]=nm;
		return retunArr;
	}
	private List<CourseSchedule> makeCourseScheduleList(TeachClass teachClass,CourseScheduleDto searchTimeDto,TeachClassEx ex){
		List<CourseSchedule> insertList=new ArrayList<CourseSchedule>();
		CourseSchedule insertCourse=null;
		for(int i=searchTimeDto.getWeekOfWorktime1();i<=searchTimeDto.getWeekOfWorktime2();i++){
			//每一周
			if(i==searchTimeDto.getWeekOfWorktime1()){
				if(ex.getDayOfWeek()<searchTimeDto.getDayOfWeek1()){
					continue;
				}
			}else if(i==searchTimeDto.getWeekOfWorktime2()){
				if(ex.getDayOfWeek()>searchTimeDto.getDayOfWeek2()){
					continue;
				}
				
			}
			insertCourse=makeOne(teachClass);
			insertCourse.setWeekOfWorktime(i);
			insertCourse.setDayOfWeek(ex.getDayOfWeek());
			insertCourse.setPeriodInterval(ex.getPeriodInterval());
			insertCourse.setPeriod(ex.getPeriod());
			insertCourse.setPlaceId(ex.getPlaceId());
			insertList.add(insertCourse);
		}
		return insertList;
	}
	private CourseSchedule makeOne(TeachClass teachClass){
		CourseSchedule insertCourse=new CourseSchedule();
		insertCourse.setId(UuidUtils.generateUuid());
		insertCourse.setClassId(teachClass.getId());
		insertCourse.setClassType(CourseSchedule.CLASS_TYPE_TEACH);
		insertCourse.setClassName(teachClass.getName());
		insertCourse.setAcadyear(teachClass.getAcadyear());
		insertCourse.setPunchCard(teachClass.getPunchCard());
		insertCourse.setSchoolId(teachClass.getUnitId());
		insertCourse.setSemester(Integer.parseInt(teachClass.getSemester()));
		insertCourse.setSubjectId(teachClass.getCourseId());
		insertCourse.setSubjectType(teachClass.getClassType());
		insertCourse.setTeacherId(teachClass.getTeacherId());
		insertCourse.setWeekType(3);
		return insertCourse;
	}
	
	private Set<String> makeCourseByGradeId(List<String> gradeIds,
			Map<String, Map<String, Set<String>>> courseIdByGradeId, String subjectType) {
		Set<String> courseIds=new HashSet<String>();
		for(String s:gradeIds){
			if(!courseIdByGradeId.containsKey(s)){
				continue;
			}
			Map<String, Set<String>> map = courseIdByGradeId.get(s);
			if(!map.containsKey(subjectType)){
				continue;
			}
			if(CollectionUtils.isNotEmpty(map.get(subjectType))){
				courseIds.addAll(map.get(subjectType));
			}
		}
		return courseIds;
	}
	private String makeTeacherIdByTeacherName(List<Teacher> tList,
			String teacherName) {
		if(CollectionUtils.isEmpty(tList)){
			return null;
		}
		String teacherId="";
		boolean flag=false;
		for(Teacher t:tList){
			if(t.getTeacherName().equals(teacherName)){
				if(!flag){
					teacherId=t.getId();
				}else{
					//重复
					return "not only";
				}
			}
		}
		return teacherId;
	}
	/**
	 * 需要插入课表的周次时间段 星期段 如果节假日有报错 返回key:error  成功 返回：searchDto  返回null  学年学期已经结束  不用删除  无需进出课表 只需要保存教学班信息
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
			int dayOfWeek2=endDateInfo.getWeekday()-1;//结束星期
			if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterBegin())<0){
				//学期未开始
				weekOfWorktime1=0;
				DateInfo startDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSemest.getSemesterBegin());
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
				dayOfWeek1=nowDateInfo.getWeekday();
			}
			//删除原来不包括今天未上课的数据
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
	
	private List<CourseSchedule> findByTimes(CourseScheduleDto searchDto){
		CourseScheduleDto dto=new CourseScheduleDto();
		dto.setSchoolId(searchDto.getSchoolId());
		dto.setAcadyear(searchDto.getAcadyear());
		dto.setSemester(searchDto.getSemester());
		dto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
		dto.setWeekOfWorktime2(searchDto.getWeekOfWorktime2());
		List<CourseSchedule> list=courseScheduleService.getByCourseScheduleDto(dto);
		courseScheduleService.makeTeacherSet(list);
		return list;
	}
	/**
	 * key:timeStr key:1:教师2：场地 value 场地id或者教师id
	 * @param list
	 * @return
	 */
	private Map<String,Map<String,Set<String>>> makeTeacherPlace(List<CourseSchedule> list,CourseScheduleDto searchDto,
			Map<String,Set<String>> classIdByPlaceIdTimes,Map<String,Set<String>> classIdByTeacherIdTimes,
			Map<String,Set<String>> oldPlaceIdTimeByClassId,Map<String,Set<String>> oldTeacherIdTimeByClassId){
		Map<String,Map<String,Set<String>>> returnMap=new HashMap<String,Map<String,Set<String>>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(CourseSchedule c:list){
				if(c.getWeekOfWorktime()==searchDto.getWeekOfWorktime1() && c.getDayOfWeek()<searchDto.getDayOfWeek1()){
					continue;
				}
				if(c.getWeekOfWorktime()==searchDto.getWeekOfWorktime2() && c.getDayOfWeek()>searchDto.getDayOfWeek2()){
					continue;
				}
				String time=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
				if(!returnMap.containsKey(time)){
					returnMap.put(time, new HashMap<String,Set<String>>());
				}
				if(StringUtils.isNotBlank(c.getPlaceId())){
					if(!returnMap.get(time).containsKey("2")){
						returnMap.get(time).put("2", new HashSet<String>());
					}
					returnMap.get(time).get("2").add(c.getPlaceId());
					String key2=time+"_"+c.getPlaceId();
					if(!classIdByPlaceIdTimes.containsKey(key2)){
						classIdByPlaceIdTimes.put(key2, new HashSet<String>());
					}
					classIdByPlaceIdTimes.get(key2).add(c.getClassId());
					if(!oldPlaceIdTimeByClassId.containsKey(c.getClassId())){
						oldPlaceIdTimeByClassId.put(c.getClassId(), new HashSet<String>());
					}
					oldPlaceIdTimeByClassId.get(c.getClassId()).add(key2);
				}
				if(CollectionUtils.isNotEmpty(c.getTeacherIds())){
					if(!returnMap.get(time).containsKey("1")){
						returnMap.get(time).put("1", new HashSet<String>());
					}
					returnMap.get(time).get("1").addAll(c.getTeacherIds());
					for(String ts:c.getTeacherIds()){
						String key2=time+"_"+ts;
						if(!classIdByTeacherIdTimes.containsKey(key2)){
							classIdByTeacherIdTimes.put(key2, new HashSet<String>());
						}
						classIdByTeacherIdTimes.get(key2).add(c.getClassId());
						
						if(!oldTeacherIdTimeByClassId.containsKey(c.getClassId())){
							oldTeacherIdTimeByClassId.put(c.getClassId(), new HashSet<String>());
						}
						oldTeacherIdTimeByClassId.get(c.getClassId()).add(key2);
					}
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * 只验证a_b_c(d) 某条数据错误
	 * @param timePlaceStr 
	 * @param timrpalceList正确解析时间 场地名称
	 * @return
	 */
	private String checkTimeAndPlace(String timePlaceStr,List<String[]> timrpalceList){
		String[] timePlaceArr=timePlaceStr.split(",");
		String[] arr=null;
		for(String tt:timePlaceArr){
			if(StringUtils.isBlank(tt)){
				continue;
			}
			tt=tt.trim();
			//分段判断
    		int firstIndex=tt.indexOf("(");
    		if(!(firstIndex>0)){
    			return tt;
    		}
    		//时间
    		String timeStr=tt.substring(0,firstIndex);
    		//地点
			String rightStr=tt.substring(firstIndex,tt.length());
			
			//判断时间规则
			if(StringUtils.isBlank(timeStr) || !timeStr.matches("^[1-7]{1}_[1-3]{1}_[0-9]+$")){
    			return tt;
    		}
			//场地规则
			if(StringUtils.isBlank(rightStr) || rightStr.lastIndexOf(")")!=rightStr.length()-1){
				return tt;
			}
			String placeNameStr=rightStr.substring(1,rightStr.length()-1);
			if(StringUtils.isBlank(placeNameStr)){
				return tt;
			}
			arr=new String[]{timeStr,placeNameStr};
			timrpalceList.add(arr);
		}
		return null;
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
	    sheet.createFreezePane(0, 2);
	    
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
        //高度：12倍默认高度
        titleRow.setHeightInPoints(12*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        //注意事项
        String remark = getRemark();
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(style);
	    
        HSSFRow rowTitle = sheet.createRow(1);
        for(int j=0;j<size;j++){
        	sheet.setColumnWidth(j, 20 * 256);
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		
		String fileName = "教学班导入";
		
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
		validRowStartNo = "1";
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
	
	private String makeTimeMess(String[] timeStr){
		int day = Integer.parseInt(timeStr[0])-1;
		int pp=Integer.parseInt(timeStr[1])+1;
		int period=Integer.parseInt(timeStr[2]);
		Map<String, String> weekMap = BaseConstants.dayOfWeekMap;
		Map<String, String> periodIntervalMap = BaseConstants.PERIOD_INTERVAL_Map;
		String mess=weekMap.get(String.valueOf(day))+periodIntervalMap.get(String.valueOf(pp))
				+"第"+period+"节";
		return mess;
	}
	
	private String makeTimeMess2(String[] timeStr){
		int day = Integer.parseInt(timeStr[0]);
		int pp=Integer.parseInt(timeStr[1]);
		int period=Integer.parseInt(timeStr[2]);
		Map<String, String> weekMap = BaseConstants.dayOfWeekMap;
		Map<String, String> periodIntervalMap = BaseConstants.PERIOD_INTERVAL_Map;
		String mess=weekMap.get(String.valueOf(day))+periodIntervalMap.get(String.valueOf(pp))
				+"第"+period+"节";
		return mess;
	}
	
	/**
	 * 一旦有重复 就返回
	 * @param stuSearchDto
	 * @param studentId
	 * @param timestr
	 * @param teachClassId
	 * @param newUpateTeachTime 从excel上到下 前面比如教学班时间修改 那么校验用新时间
	 * @return
	 */
	private String checkStudentTime(CourseScheduleDto stuSearchDto,List<Student> studentList,Set<String> timestr,String teachClassId,
			Map<String,Set<String>> newUpateTeachTime,Map<String,String> teachNameMap){
		if(CollectionUtils.isEmpty(studentList)) {
			return null;
		}
		Set<String> newTimeStr = new HashSet<String>();
		for (String str : timestr) {
			String[] sp = str.split("_");
			newTimeStr.add((Integer.parseInt(sp[0])-1)+"_"+(Integer.parseInt(sp[1])+1)+"_"+sp[2]);
		}
		String[] studentId = EntityUtils.getSet(studentList,e->e.getId()).toArray(new String[0]);
		//根据学生id取得班级is_using=1的教学班 以及所在的行政班
		List<TeachClass> teachClassList=teachClassService.findByStudentIds(stuSearchDto.getAcadyear(),stuSearchDto.getSemester()+"",stuSearchDto.getSchoolId(),studentId);
		Set<String> classIds=new HashSet<String>();
		Map<String,TeachClass> jxbMap=new HashMap<String,TeachClass>();
		if(CollectionUtils.isNotEmpty(teachClassList)){
			classIds.addAll(EntityUtils.getSet(teachClassList, e->e.getId()));
			jxbMap=EntityUtils.getMap(teachClassList, e->e.getId());
		}
		
		Map<String,Clazz> xzbMap=new HashMap<String,Clazz>();
		if(CollectionUtils.isNotEmpty(studentList)){
			Set<String> xzbclassId=new HashSet<String>();
			xzbclassId.addAll(EntityUtils.getSet(studentList, e->e.getClassId()));
			List<Clazz> clazzList = classService.findListByIds(xzbclassId.toArray(new String[]{}));
			xzbMap=EntityUtils.getMap(clazzList, e->e.getId());
			classIds.addAll(EntityUtils.getSet(clazzList, e->e.getId()));
		}
		if(classIds.size()<=0){
			return null;
		}
		classIds.remove(teachClassId);
		if(classIds.size()<=0){
			return null;
		}
		
		if(newUpateTeachTime!=null && newUpateTeachTime.size()>0) {
			Set<String> sameIds = (Set<String>) CollectionUtils.intersection(newUpateTeachTime.keySet(), classIds);		
			if(CollectionUtils.isNotEmpty(sameIds)) {
				for(String s:sameIds) {
					Set<String> set = newUpateTeachTime.get(s);
					for(String sw:newTimeStr) {
						if(set.contains(sw)) {
							return "在"+makeTimeMess2(sw.split("_"))+"，存在学生与上文档中"+teachNameMap.get(s)+"上课冲突";
						}
					}
				}
			}
			classIds.removeAll(newUpateTeachTime.keySet());
		}
		if(classIds.size()<=0){
			return null;
		}
		
		
		int weekOfWorktime1=stuSearchDto.getWeekOfWorktime1();
		int weekOfWorktime2=stuSearchDto.getWeekOfWorktime2();
		int dayOfWeek1=stuSearchDto.getDayOfWeek1();
		int dayOfWeek2=stuSearchDto.getDayOfWeek2();
		stuSearchDto.setClassIds(classIds.toArray(new String[]{}));
		List<CourseSchedule> courseList = courseScheduleService.findByTimes(stuSearchDto, newTimeStr.toArray(new String[]{}));
		
		for(CourseSchedule c:courseList){
			if(c.getWeekOfWorktime()==weekOfWorktime1 && c.getDayOfWeek()<dayOfWeek1){
				continue;
			}
			if(c.getWeekOfWorktime()==weekOfWorktime2 && c.getDayOfWeek()>dayOfWeek2){
				continue;
			}
			//场地是不是冲突
			String mess=BaseConstants.dayOfWeekMap.get(c.getDayOfWeek()+"")+BaseConstants.PERIOD_INTERVAL_Map.get(c.getPeriodInterval())+"第"+c.getPeriod()+"节";
			if(jxbMap.containsKey(c.getClassId())){
				return "在"+mess+"，存在学生已经在班级"+jxbMap.get(c.getClassId()).getName()+"上课";
			}else if(xzbMap.containsKey(c.getClassId())){
				return "在"+mess+"，存在学生已经在班级"+xzbMap.get(c.getClassId()).getClassNameDynamic()+"上课";
			}
			return "在"+mess+"，存在学生上课冲突";
		}
		
		return null;
	}
	
	class ExcelTeachDto{
		private String teachClassName;
		private String subjectType;
		private String subjectName;
		private String gradeNames;
		private String teacherCode;
		private String teacherName;
		private String creditStr;
		private String fullMarkStr;
		private String passMarkStr;
		private String punchCardStr;
		private String timePlaceStr;
		private String relaCourseName;
    	//关联班级
		private String relaClazzName;
		private String placeNames;
		
		public ExcelTeachDto() {
			
		}
		public ExcelTeachDto(String[] colVlaues) {
			teachClassName=colVlaues[0]==null?null:colVlaues[0].trim();
			subjectType=colVlaues[1]==null?null:colVlaues[1].trim();
			subjectName=colVlaues[2]==null?null:colVlaues[2].trim();
			gradeNames=colVlaues[3]==null?null:colVlaues[3].trim();
			teacherCode=colVlaues[4]==null?null:colVlaues[4].trim();
			teacherName=colVlaues[5]==null?null:colVlaues[5].trim();
			creditStr=colVlaues[6]==null?null:colVlaues[6].trim();
			fullMarkStr=colVlaues[7]==null?null:colVlaues[7].trim();
			passMarkStr=colVlaues[8]==null?null:colVlaues[8].trim();
			punchCardStr=colVlaues[9]==null?null:colVlaues[9].trim();
			timePlaceStr=colVlaues[10]==null?null:colVlaues[10].trim();
			relaCourseName=colVlaues[11]==null?null:colVlaues[11].trim();
	    	//关联班级
			relaClazzName=colVlaues[12]==null?null:colVlaues[12].trim();
			placeNames=colVlaues[13]==null?null:colVlaues[13].trim();
		}
		public String getTeachClassName() {
			return teachClassName;
		}
		public void setTeachClassName(String teachClassName) {
			this.teachClassName = teachClassName;
		}
		public String getSubjectType() {
			return subjectType;
		}
		public void setSubjectType(String subjectType) {
			this.subjectType = subjectType;
		}
		public String getSubjectName() {
			return subjectName;
		}
		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}
		public String getGradeNames() {
			return gradeNames;
		}
		public void setGradeNames(String gradeNames) {
			this.gradeNames = gradeNames;
		}
		public String getTeacherCode() {
			return teacherCode;
		}
		public void setTeacherCode(String teacherCode) {
			this.teacherCode = teacherCode;
		}
		public String getTeacherName() {
			return teacherName;
		}
		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
		}
		public String getCreditStr() {
			return creditStr;
		}
		public void setCreditStr(String creditStr) {
			this.creditStr = creditStr;
		}
		public String getFullMarkStr() {
			return fullMarkStr;
		}
		public void setFullMarkStr(String fullMarkStr) {
			this.fullMarkStr = fullMarkStr;
		}
		public String getPassMarkStr() {
			return passMarkStr;
		}
		public void setPassMarkStr(String passMarkStr) {
			this.passMarkStr = passMarkStr;
		}
		public String getPunchCardStr() {
			return punchCardStr;
		}
		public void setPunchCardStr(String punchCardStr) {
			this.punchCardStr = punchCardStr;
		}
		public String getTimePlaceStr() {
			return timePlaceStr;
		}
		public void setTimePlaceStr(String timePlaceStr) {
			this.timePlaceStr = timePlaceStr;
		}
		public String getRelaCourseName() {
			return relaCourseName;
		}
		public void setRelaCourseName(String relaCourseName) {
			this.relaCourseName = relaCourseName;
		}
		public String getRelaClazzName() {
			return relaClazzName;
		}
		public void setRelaClazzName(String relaClazzName) {
			this.relaClazzName = relaClazzName;
		}
		public String getPlaceNames() {
			return placeNames;
		}
		public void setPlaceNames(String placeNames) {
			this.placeNames = placeNames;
		}
		
		
	}
	
	
	public static void main(String[] args) {
		String s1="1-2-3(小xc)";//1
		String s2="8-3-0(小xc)";//1
		String s3="1-2-3(()())";
		String s4="1-2-3(...)";

		System.out.println(s1.matches("^[1-7]{1}_[1-3]{1}_[0-9]+$"));
		System.out.println(s2.matches("^[1-7]{1}_[1-3]{1}_[0-9]+$"));
		System.out.println(s3.matches("^[1-7]{1}_[1-3]{1}_[0-9]+$"));
		System.out.println(s4.matches("^[1-7]{1}_[1-3]{1}_[0-9]+$"));
		
		System.out.println(s2.indexOf("小"));
		
	}
}
