package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;


@Controller
@RequestMapping("/basedata")
public class ClassHourAction extends BaseAction{
	@Autowired
	private ClassHourService classHourService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private ClassHourExService classHourExService;
	@Autowired
	private ClassService classService;
	
	@RequestMapping("/virtual/index/page")
	public String virtualIndex(ModelMap map) {
		
		return "/basedata/classhour/virtualIndex.ftl";
	}
	
	@RequestMapping("/classhour/index/page")
	public String index(ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		} 
        String unitId = getLoginInfo().getUnitId();
        map.put("acadyearList", acadyearList);
      
        Semester semester = semesterService.findCurrentSemester(2,unitId);
        map.put("semester", semester);
        List<Grade> gradeList = gradeService.findByUnitId(unitId);
		map.put("gradeList", gradeList);
		return "/basedata/classhour/hourIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/classhour/findCourseList")
	public String findCourseList(String acadyear,String semester,String gradeId) {
		List<ClassHour> hourList = classHourService.findListByUnitId(acadyear, semester, getLoginInfo().getUnitId(), gradeId,false);
		List<Course> courseList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(hourList)) {
 			Set<String> subIds = EntityUtils.getSet(hourList, e->e.getSubjectId());
			courseList = courseService.findListByIdIn(subIds.toArray(new String[] {}));
			//虚拟课程 排序号不为空
			Collections.sort(courseList, (x,y) -> (x.getOrderId()).compareTo(y.getOrderId()));
		}
		return Json.toJSONString(courseList);
	}
	
	
	@ResponseBody
	@RequestMapping("/classhour/findXnClassList")
	public String findXnClassList(String acadyear,String semester,String gradeId,String subjectId) {
		List<ClassHour> hourList = classHourService.findBySubjectIds(acadyear, semester, getLoginInfo().getUnitId(), gradeId, new String[] {subjectId});
		List<ClassHour> allHourList=classHourService.makeClassNames(hourList);
		return Json.toJSONString(allHourList);
	}
	
	/**
	 * 显示课表内容
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param hourId
	 * @param map
	 * @return
	 */
	@RequestMapping("/classhour/list/page")
	public String showTime(String acadyear,String semester,String gradeId,String hourId,ModelMap map) {
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		Grade grade = gradeService.findOne(gradeId);
		map.put("mm", grade.getMornPeriods()==null?0:grade.getMornPeriods());
		map.put("am", grade.getAmLessonCount()==null?0:grade.getAmLessonCount());
		map.put("pm", grade.getPmLessonCount()==null?0:grade.getPmLessonCount());
		map.put("nm", grade.getNightLessonCount()==null?0:grade.getNightLessonCount());
		map.put("edudays", grade.getWeekDays()==null?7:grade.getWeekDays());
		List<ClassHourEx> hourExList=new ArrayList<>();
		if(StringUtils.isNotBlank(hourId)) {
			map.put("hourId", hourId);
			ClassHour hourItem=classHourService.findOne(hourId);
			if(hourItem!=null) {
				hourExList = classHourExService.findByClassHourIdIn(new String[] {hourId});
			}
		}
		map.put("hourExList", hourExList);
		return "/basedata/classhour/hourTime.ftl";
	}
	
	
	
	
	/**
	 * 获取课程表查询字段 周次时间段 星期段   如果节假日有报错 返回key:error  成功 返回：searchDto(时间范围)
	 * 结合当前时间
	 * @return
	 */
	private Map<String,Object> makeDto(String acadyear,String semester,String unitId){
		Map<String,Object> objMap=new HashMap<String,Object>();
		CourseScheduleDto searchDto =null;
		
		Semester chooseSemest=semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),unitId);
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterEnd())>0){
			//学年学期已经结束  不用删除  无需进出课表
			return objMap;
		}else{
			DateInfo endDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSemest.getSemesterEnd());
			if(endDateInfo == null){
				objMap.put("error", "未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
				return objMap;
			}
			int weekOfWorktime1=0;
			int weekOfWorktime2=endDateInfo.getWeek();
			
			int dayOfWeek1=0;//开始星期
			int dayOfWeek2=endDateInfo.getWeekday()-1;//结束星期
			if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterBegin())<0){
				//学期未开始
				weekOfWorktime1=1;
				//星期是1开始的
				DateInfo startDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSemest.getSemesterBegin());
				if(startDateInfo == null){
					objMap.put("error", "未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				dayOfWeek1=startDateInfo.getWeekday()-1;
			}else{
				DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
				if(nowDateInfo == null){
					objMap.put("error", "未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				weekOfWorktime1=nowDateInfo.getWeek();
				dayOfWeek1=nowDateInfo.getWeekday();
			}
			//删除原来未上课的数据
			searchDto=new CourseScheduleDto();
			searchDto.setDayOfWeek1(dayOfWeek1);
			searchDto.setDayOfWeek2(dayOfWeek2);
			searchDto.setWeekOfWorktime1(weekOfWorktime1);
			searchDto.setWeekOfWorktime2(weekOfWorktime2);
		}
		
		objMap.put("searchDto", searchDto);
		return objMap;
	}
	/**
	 *  json
	 *  success:true/false
	 *  errorTitle:1、时间冲突 2、数据
	 *  errorHead:2、教师，学生，场地  2、保存失败
	 *  errorContent:具体内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/classhour/move")
	public String move(String acadyear,String semester,String gradeId,String hourId,String time1,String time2) {
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester) || StringUtils.isBlank(gradeId) || StringUtils.isBlank(time1) || StringUtils.isBlank(time2)
				|| StringUtils.isBlank(hourId)) {
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", "参数丢失");
			return json.toString();
		}
		
		//当前周
		String unitId=getLoginInfo().getUnitId();
		Map<String, Object> searchmap = makeDto(acadyear, semester, unitId);
		if(searchmap.containsKey("error")){
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", (String)searchmap.get("error"));
			return json.toString();
		}
		CourseScheduleDto searchDto=null;
		if(searchmap.containsKey("searchDto")){
			searchDto=(CourseScheduleDto)searchmap.get("searchDto");
		}else {
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", "当前学年学期已经结束 ,不能操作");
			return json.toString();
		}
		searchDto.setGradeId(gradeId);
		searchDto.setSchoolId(unitId);
		try {
			String jsonStr=classHourService.saveChange(searchDto,acadyear,semester,hourId,time1,time2);
			return jsonStr;
		}catch (Exception e) {
			e.printStackTrace();
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", "");
		}
		return json.toString();
	}
	
	
	@ResponseBody
	@RequestMapping("/classhour/delete")
	public String delete(String acadyear,String semester,String gradeId,String hourId,String time1) {
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester) || StringUtils.isBlank(gradeId) || StringUtils.isBlank(hourId) || StringUtils.isBlank(time1) ) {
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", "参数丢失");
			return json.toString();
		}

		//当前周
		String unitId=getLoginInfo().getUnitId();
		Map<String, Object> searchmap = makeDto(acadyear, semester, unitId);
		if(searchmap.containsKey("error")){
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", (String)searchmap.get("error"));
			return json.toString();
		}
		CourseScheduleDto searchDto=null;
		if(searchmap.containsKey("searchDto")){
			searchDto=(CourseScheduleDto)searchmap.get("searchDto");
		}else {
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", "当前学年学期已经结束 ,不能操作");
			return json.toString();
		}
		searchDto.setGradeId(gradeId);
		searchDto.setSchoolId(unitId);
		try {
			String jsonStr=classHourService.deleteBySubjectIdTime(searchDto,acadyear,semester,hourId,time1);
			return jsonStr;
		}catch (Exception e) {
			e.printStackTrace();
			json.put("success", false);
			json.put("errorTitle", "数据");
			json.put("errorHead", "保存失败");
			json.put("errorContent", "");
		}
		return json.toString();
	}
	
	

}
