/* 
 * @(#)ClassStuTimetableAction.java    Created on 2017年3月8日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ClassStuTimetableAction.java 7451 2018-01-18 12:59:01Z niuchao $
 */
package net.zdsoft.basedata.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ZipUtils;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhaosheng
 * @version $Revision: 7451 $, $Date: 2018-01-18 20:59:01 +0800 (周四, 18 一月 2018) $
 */
@Controller
@RequestMapping("/timetable")
public class ClassStuTimetableAction extends RoleCommonAction {

    public static final String ODD_WEEK = "（单）";
    public static final String EVEN_WEEK = "（双）";

    @Autowired
    private SemesterService semesterService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CourseService courseRemoteService;
    @Autowired
    private GradeService gradeRemoteService;
    @Autowired
    private ClassService classRemoteService;
    @Autowired
    private StudentService studentRemoteService;
    @Autowired
    private TeacherService teacherRemoteService;
    @Autowired
    private CourseScheduleService courseScheduleRemoteService;
    @Autowired
    private TeachClassStuService teachClassStuRemoteService;
    @Autowired
    private TeachPlaceService teachPlaceRemoteService;
    @Autowired
    private SchoolCalendarService schoolCalendarRemoteService;
    @Autowired(required=false)
    private OpenApiNewElectiveService openApiNewElectiveService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;

    @RequestMapping("/classStu/index/page")
    @ControllerInfo("课程表首页")
    public String showIndex(String searchType, ModelMap map, HttpSession httpSession) {
        List<String> acadyearList = semesterService.findAcadeyearList();
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        Semester semester = semesterService.findCurrentSemester(2,unitId);
        String acadyearSearch = semester.getAcadyear();
        String semesterSearch = semester.getSemester() + "";

        map.put("acadyearList", acadyearList);
        map.put("acadyearSearch", acadyearSearch);
        map.put("semesterSearch", semesterSearch);

        Unit u = unitService.findOne(unitId);
        Map<String, Integer> cur2Max =schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(acadyearSearch, semesterSearch, u.getId());
        Integer weekSearch = cur2Max.get("current");
        if(weekSearch==null){
        	weekSearch=1;
        }
        map.put("weekSearch", weekSearch);
        map.put("max", cur2Max.get("max"));
        map.put("unitName", u.getUnitName());
        return "basedata/courseSchedule/classStu/timetableIndex.ftl";
    }

    @RequestMapping("/getWeek/json")
	@ResponseBody
    public String getWeek(String searchAcadyear, String searchSemester, HttpSession httpSession) {
    	LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
    	Map<String, Integer> cur2Max =schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(searchAcadyear, searchSemester, unitId);
        Integer weekSearch = cur2Max.get("current");
        if(weekSearch==null){
        	weekSearch=1;
        }
        JSONObject jo = new JSONObject();
        jo.put("weekSearch", weekSearch);
        jo.put("max", cur2Max.get("max"));
    	return jo.toJSONString();
    }
    
    @RequestMapping("/classStu/timeTable/page")
    @ControllerInfo("课程表详情")
    public String showTimeTable(String searchAcadyear, String searchSemester, String week, String type, String id,
            boolean showPlace, boolean showTeacher, ModelMap map) {
        String gradeId;
        String classDynamicName;
        String studentName = "";
        List<TeachClass> teachClasses = new ArrayList<TeachClass>();
        List<CourseSchedule> timetableCourseScheduleList=new ArrayList<CourseSchedule>();
        List<CourseSchedule> timetableCourseScheduleTmp=null;
        Clazz clazz = null;
        map.put("showPlace", showPlace);
        map.put("showTeacher", showTeacher);
        if (("1").equals(type)) {
            // 查询id为班级id
            clazz = classRemoteService.findOne(id);
            if(clazz==null){
            	return errorFtl(map, "请选择正确的查询班级或学生");
            }
            gradeId = clazz.getGradeId();
            classDynamicName = clazz.getClassName();
        }
        else if (("2").equals(type)) {
            // id为学生id
            Student student = studentRemoteService.findOne(id);
            if(student==null){
            	return errorFtl(map, "请选择正确的查询班级或学生");
            }
            clazz = classRemoteService.findOne(student.getClassId());
            
            // 获取学年学期下学生的所有的教学班List
            teachClasses = teachClassStuRemoteService.findByStudentId2(id, searchAcadyear, searchSemester);
            
            studentName = student.getStudentName();
            gradeId = clazz.getGradeId();
            classDynamicName = clazz.getClassName();
        }
        else {
            return errorFtl(map, "请选择正确的查询班级或学生");
        }
        
        if (StringUtils.isBlank(week)) {
            return errorFtl(map, "周次不存在，请先维护基础信息节假日设置");
        }
        
        Grade grade = gradeRemoteService.findOne(gradeId);
        classDynamicName = grade.getGradeName() + classDynamicName;
        if (("1").equals(type)) {
            String title = searchAcadyear + "年" + "第" + searchSemester + "学期" + classDynamicName + "的课程表";
            map.put("title", title);
        }
        else {
            String title = searchAcadyear + "年" + "第" + searchSemester + "学期" + studentName + "的课程表";
            map.put("title", title);
        }
        
        // 早上下晚时间段的 课程数
        Map<String, Integer> timeIntervalMap = new LinkedHashMap<String, Integer>();
        grade.setMornPeriods(grade.getMornPeriods()==null?0:grade.getMornPeriods());
        if (grade.getMornPeriods() != 0) {
            timeIntervalMap.put("1", grade.getMornPeriods());
        }
        grade.setAmLessonCount(grade.getAmLessonCount()==null?0:grade.getAmLessonCount());
        if (grade.getAmLessonCount() != 0) {
            timeIntervalMap.put("2", grade.getAmLessonCount());
        }
        grade.setPmLessonCount(grade.getPmLessonCount()==null?0:grade.getPmLessonCount());
        if (grade.getPmLessonCount() != 0) {
            timeIntervalMap.put("3", grade.getPmLessonCount());
        }
        grade.setNightLessonCount(grade.getNightLessonCount()==null?0:grade.getNightLessonCount());
        if (grade.getNightLessonCount() != 0) {
            timeIntervalMap.put("4", grade.getNightLessonCount());
        }
        map.put("edudays", grade.getWeekDays());
        //课程表
        if (("1").equals(type)) {
            //班级
        	Map<String, String> classId2Name = new HashMap<String, String>();
        	Set<String> classdIds = new HashSet<String>();
        	if(clazz!=null){
        		classId2Name.put(clazz.getId(), clazz.getClassNameDynamic());
        		classdIds.add(clazz.getId());
        	}        	
        	//查出学生课程
        	if(classdIds.size()>0){
                timetableCourseScheduleList = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), classdIds.toArray(new String[0]), Integer.valueOf(week), "1");

                timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), classdIds.toArray(new String[0]), Integer.valueOf(week) + 1, "1");
                if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
                    timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), classdIds.toArray(new String[0]), Integer.valueOf(week) - 1, "1");
                }
                if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
                    for (CourseSchedule one : timetableCourseScheduleTmp) {
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                            timetableCourseScheduleList.add(one);
                        }
                    }
                }

        	}
        	makeCourse(timetableCourseScheduleList,classId2Name,type);
        }
        else {
            //学生
        	Map<String, String> classId2Name = new HashMap<String, String>();
        	Set<String> classdIds = new HashSet<String>();
        	if(clazz!=null){
        		classId2Name.put(clazz.getId(), clazz.getClassNameDynamic());
        		classdIds.add(clazz.getId());
        	}
        	if(CollectionUtils.isNotEmpty(teachClasses)){
        		for(TeachClass t:teachClasses){
        			classId2Name.put(t.getId(), t.getName());
        			classdIds.add(t.getId());
        		}
        	}
        	//6.0选课学生进入课程表数据
        	List<String> jxclaids=null;
        	if(CollectionUtils.isNotEmpty(jxclaids)){
        		classdIds.addAll(jxclaids);
        	}
        	//查出学生课程
        	if(classdIds.size()>0){
                timetableCourseScheduleList = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), classdIds.toArray(new String[0]), Integer.valueOf(week), "2");

                timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), classdIds.toArray(new String[0]), Integer.valueOf(week) + 1, "2");
                if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
                    timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), classdIds.toArray(new String[0]), Integer.valueOf(week) - 1, "2");
                }
                if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
                    for (CourseSchedule one : timetableCourseScheduleTmp) {
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                            timetableCourseScheduleList.add(one);
                        }
                    }
                }

        	}
        	makeCourse(timetableCourseScheduleList,classId2Name,type);
        }
  
        map.put("type", type);
        map.put("timetableCourseScheduleList", timetableCourseScheduleList);
        map.put("timeIntervalMap", timeIntervalMap);
        return "basedata/courseSchedule/classStu/timetableDetail.ftl";
    }
    
    /**
     * timetableCourseScheduleList中className,subjectName,placeName内容填充
     * @param timetableCourseScheduleList
     * @param classId2Name
     */
    private void makeCourse( List<CourseSchedule> timetableCourseScheduleList,Map<String,String> classId2Name,String type){
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
            Set<String> subjectIds = new HashSet<String>();
            Map<String, Course> id2CourseName = new HashMap<String, Course>();
            Set<String> placeIds = new HashSet<String>();
            Map<String, String> id2PlaceName = new HashMap<String, String>();
            Set<String> teacherIds = new HashSet<String>();
            Map<String, String> id2TeacherName = new HashMap<String, String>();
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (courseSchedule.getSubjectId() != null && StringUtils.isBlank(courseSchedule.getSubjectName())) {
                    subjectIds.add(courseSchedule.getSubjectId());
                }
                if (courseSchedule.getPlaceId() != null) {
                    placeIds.add(courseSchedule.getPlaceId());
                }
                if (courseSchedule.getTeacherId() != null) {
                    teacherIds.add(courseSchedule.getTeacherId());
                }
            }
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = courseRemoteService.findListByIdIn(subjectIds.toArray(new String[0]));
                for (Course c : courseList) {
                    id2CourseName.put(c.getId(), c);
                }
            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                id2PlaceName = teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0]));
            }
            if (CollectionUtils.isNotEmpty(teacherIds)) {
                List<Teacher> teacherList = teacherRemoteService.findListByIds(teacherIds.toArray(new String[0]));
                for (Teacher t : teacherList) {
                    id2TeacherName.put(t.getId(), t.getTeacherName());
                }
            }
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
                	Course cs = id2CourseName.get(courseSchedule.getSubjectId());
	           		 if(cs != null){
	           			 courseSchedule.setSubjectName(cs.getSubjectName());
	           			if(StringUtils.isNotEmpty(cs.getBgColor())) {
	           				 String[] bcs = cs.getBgColor().split(",");
	           				if (StringUtils.isNotEmpty(bcs[0])) {
								courseSchedule.setBgColor(bcs[0]);
							}
							if (bcs.length > 1 && StringUtils.isNotEmpty(bcs[1])) {
								courseSchedule.setBorderColor(bcs[1]);
							}
	           			 }
	           		 }else {
	           			 courseSchedule.setSubjectName(courseSchedule.getSubjectName());
	   				}
                }else{
                	courseSchedule.setSubjectName("");
                }
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(courseSchedule.getWeekType())) {
                    courseSchedule.setSubjectName(courseSchedule.getSubjectName() + ODD_WEEK);
                }
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(courseSchedule.getWeekType())) {
                    courseSchedule.setSubjectName(courseSchedule.getSubjectName() + EVEN_WEEK);
                }
                if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
                    courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
                }else{
                	courseSchedule.setPlaceName("");
                }
                if (id2TeacherName != null && id2TeacherName.get(courseSchedule.getTeacherId()) != null) {
                    courseSchedule.setTeacherName(id2TeacherName.get(courseSchedule.getTeacherId()));
                }else{
                	courseSchedule.setTeacherName("");
                }
                if (("2").equals(type)) {
                    courseSchedule.setSubAndTeacherName(courseSchedule.getSubjectName());
                    if (StringUtils.isNotBlank(courseSchedule.getTeacherName())) {
                        courseSchedule.setSubAndTeacherName(courseSchedule.getSubAndTeacherName() + "("+ courseSchedule.getTeacherName() + ")");
                    }
                    
                }
                // 在行政班上的课的班级名称填充
                if (courseSchedule.getClassName() == null) {
                	String claname =classId2Name.get(courseSchedule.getClassId());
                	if(StringUtils.isNotEmpty(claname)){
                		courseSchedule.setClassName(classId2Name.get(courseSchedule.getClassId()));
                	}else{
                		courseSchedule.setClassName("");
                	}
//                	else {//教学班
//                		courseSchedule.setClassName(courseSchedule.getSubjectName());
//					}
                }
            }
        }
    }


    @RequestMapping("/courseSchedule/index/page")
    @ControllerInfo("学生端课程表首页")
    public String showstuIndex(ModelMap map, HttpSession httpSession) {
    	LoginInfo loginInfo = getLoginInfo(httpSession);
    	String unitId = loginInfo.getUnitId();
        Semester semester = semesterService.findCurrentSemester(2,unitId);
        if (semester == null) {
            return errorFtl(map, "学年学期不存在");
        }
        String acadyearSearch = semester.getAcadyear();
        String semesterSearch = semester.getSemester() + "";
        Unit u = unitService.findOne(unitId);
        Map<String, Integer> cur2Max = schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(acadyearSearch, semesterSearch, u.getId());
        Integer weekSearch = cur2Max.get("current");
        if(weekSearch==null){
        	weekSearch=1;
        }
        if (loginInfo.getOwnerType() != User.OWNER_TYPE_STUDENT) {
            return errorFtl(map, "你登录的不是学生账号！");
        }
        String studentId = getLoginInfo().getOwnerId();
        Student student = studentRemoteService.findOne(studentId);
        if (student == null) {
            return errorFtl(map, "帐号已被删除！");
        }
        
        String title=acadyearSearch+"学年第"+semesterSearch+"学期"+student.getStudentName()+"的课程表";
        Clazz clazz = classRemoteService.findOne(student.getClassId());
        // 获取学年学期下学生的所有的教学班List
        List<TeachClass> teachClasses = teachClassStuRemoteService.findByStudentId2(studentId, acadyearSearch, semesterSearch);
        String gradeId = clazz.getGradeId();
       // String classId = clazz.getId();
        String classDynamicName = clazz.getClassName();
        Grade grade = gradeRemoteService.findOne(gradeId);
        classDynamicName = grade.getGradeName() + classDynamicName;
        Map<String, Integer> timeIntervalMap = new LinkedHashMap<String, Integer>();
        timeIntervalMap.put("2", grade.getAmLessonCount());
        timeIntervalMap.put("3", grade.getPmLessonCount());
        timeIntervalMap.put("4", grade.getNightLessonCount());
    	Map<String, String> classId2Name = new HashMap<String, String>();
    	Set<String> classdIds = new HashSet<String>();
    	if(clazz!=null){
    		classId2Name.put(clazz.getId(), classDynamicName);
    		classdIds.add(clazz.getId());
    	}
    	if(CollectionUtils.isNotEmpty(teachClasses)){
    		for(TeachClass t:teachClasses){
    			classId2Name.put(t.getId(), t.getName());
    			classdIds.add(t.getId());
    		}
    	}
    	
    	List<CourseSchedule> timetableCourseScheduleList=new ArrayList<CourseSchedule>();
    	
    	//6.0选课学生进入课程表数据
    	List<String> jxclaids=null;
    	if(openApiNewElectiveService!=null){
        	jxclaids = openApiNewElectiveService.getClassByUidSemesterStuId(unitId, acadyearSearch, semesterSearch, studentId);
        }
    	if(CollectionUtils.isNotEmpty(jxclaids)){
    		classdIds.addAll(jxclaids);
    	}
    	//查出学生课程
    	if(classdIds.size()>0){
    		timetableCourseScheduleList =  courseScheduleRemoteService.findCourseScheduleListByClassIdes(acadyearSearch, Integer.valueOf(semesterSearch), classdIds.toArray(new String[0]), weekSearch);
    	}
    	makeCourse(timetableCourseScheduleList,classId2Name,"2");
        map.put("timetableCourseScheduleList", timetableCourseScheduleList);
        map.put("timeIntervalMap", timeIntervalMap);
        map.put("type", "2");
        map.put("title", title);
        return "basedata/courseSchedule/classStu/timetableDetail.ftl";

    }
    
    @RequestMapping("/classStu/exportTimetableStuZip")
	@ResponseBody
	@ControllerInfo("年级所有学生课程表Excel打包导出zip")
    public String exportStuResult(String searchAcadyear, String searchSemester, String week, String id, boolean showPlace, boolean showTeacher, HttpServletResponse resp){
    	Grade grade = gradeRemoteService.findOne(id);
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		String teacherId = loginInfo.getOwnerId();
		//行政班
		List<Clazz> clazzList = new ArrayList<Clazz>();
		if(!isAdmin(unitId, loginInfo.getUserId())&&!teacherId.equals(grade.getTeacherId())){
			clazzList = classRemoteService.findBySchoolIdAndGradeIds(unitId, new String[]{id});
			clazzList = clazzList.stream().filter(e->teacherId.equals(e.getTeacherId())||teacherId.equals(e.getViceTeacherId())).collect(Collectors.toList());
    	}else{
    		clazzList = classRemoteService.findByGradeId(id);
    	}
		//获取学生和所在的行政班和教学班映射，key-studentId,value-classIdList
		Map<String,List<String>> stuClassMap = new HashMap<String, List<String>>();
    	if(grade != null){
    		for(Clazz clazz : clazzList){
    			clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
    		}
    	}
		String[] classIds = EntityUtils.getList(clazzList, Clazz::getId).toArray(new String[0]);
		Map<String, List<Student>> studentMap = studentRemoteService.findMapByClassIdIn(classIds);
		List<Student> studentList = new ArrayList<Student>();
		for (Entry<String, List<Student>> item : studentMap.entrySet()) {
			if(CollectionUtils.isNotEmpty(item.getValue())){
				studentList.addAll(item.getValue());
			}
		}
		for (Student student : studentList) {
			if(!stuClassMap.containsKey(student.getId())){
				stuClassMap.put(student.getId(), new ArrayList<String>());
			}
			stuClassMap.get(student.getId()).add(student.getClassId());
		}
		Map<String, String> classId2Name = clazzList.stream().collect(Collectors.toMap(Clazz::getId, Clazz::getClassNameDynamic));
		//教学班
		List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findByGradeId(id),TeachClass.class);
//		List<TeachClass> teachClassList = SUtils.dt(teachClassStuRemoteService.findByStuIds(EntityUtils.getSet(studentList, Student::getId).toArray(new String[0])),TeachClass.class);
		//过滤掉getParentId为null
		Map<String, String> childParentMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(teachClassList)) {
			List<TeachClass> teachClassList2=teachClassList.stream().filter(e->StringUtils.isNotBlank(e.getParentId())).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(teachClassList2)) {
				childParentMap = EntityUtils.getMap(teachClassList2,TeachClass::getId,TeachClass::getParentId);
			}
		}
//		Map<String, String> childParentMap = EntityUtils.getMap(teachClassList,TeachClass::getId,TeachClass::getParentId);
		
		classId2Name.putAll(teachClassList.stream().collect(Collectors.toMap(TeachClass::getId, TeachClass::getName)));
		List<TeachClassStu> teachClassStuList = teachClassStuRemoteService.findStudentByClassIds(EntityUtils.getSet(teachClassList, TeachClass::getId).toArray(new String[0]));
		for (TeachClassStu teachClassStu : teachClassStuList) {
			if(!stuClassMap.containsKey(teachClassStu.getStudentId())){
				stuClassMap.put(teachClassStu.getStudentId(), new ArrayList<String>());
			}
			stuClassMap.get(teachClassStu.getStudentId()).add(teachClassStu.getClassId());
			if(StringUtils.isNotBlank(childParentMap.get(teachClassStu.getClassId()))){
				stuClassMap.get(teachClassStu.getStudentId()).add(childParentMap.get(teachClassStu.getClassId()));
			}
		}
		
		//获取当前条件下所有的课程表数据并组装，按班级分类
        List<CourseSchedule> csList = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.parseInt(searchSemester), classId2Name.keySet().toArray(new String[0]), Integer.parseInt(week), "2");

        List<CourseSchedule> timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.parseInt(searchSemester), classId2Name.keySet().toArray(new String[0]), Integer.parseInt(week) + 1, "2");
        if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
            timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.parseInt(searchSemester), classId2Name.keySet().toArray(new String[0]), Integer.parseInt(week) - 1, "2");
        }
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
            for (CourseSchedule one : timetableCourseScheduleTmp) {
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                    csList.add(one);
                }
            }
        }


        makeCourse(csList, classId2Name , "2");
        Map<String,List<CourseSchedule>> classScheduleMap = new HashMap<String, List<CourseSchedule>>();
        for (CourseSchedule cs : csList) {
			if(!classScheduleMap.containsKey(cs.getClassId())){
				classScheduleMap.put(cs.getClassId(), new ArrayList<CourseSchedule>());
			}
			classScheduleMap.get(cs.getClassId()).add(cs);
		}
        
        //创建文件夹
        String dirName = searchAcadyear + "年第" + searchSemester+"学期" + grade.getGradeName()+ "第"+week+"周学生课程表";
        File dirFile = new File(dirName);
        if(dirFile.exists()){
        	dirFile.delete();
        }
        dirFile.mkdirs();
        
        //excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = new CellRangeAddress(0,0,0,titleList.size()-1);
        int morn = grade.getMornPeriods()==null?0:grade.getMornPeriods();
        int am = grade.getAmLessonCount();
		int pm = grade.getPmLessonCount();;
		int night = grade.getNightLessonCount();
		int lessonCount = morn+am+pm+night;
		CellRangeAddress craMorn = null;
		CellRangeAddress craAm = null;
		CellRangeAddress craPm = null;
		CellRangeAddress craNight = null;
        
        HSSFWorkbook workbook = null;
        HSSFCellStyle style = null;
		String titleName = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		List<CourseSchedule> stuScheduleList = null;
		//每班为一个excel
		for (Clazz clazz : clazzList) {
			workbook = new HSSFWorkbook();
			//表格样式
			style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);//水平
			style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
			style.setWrapText(true);//自动换行
			style.setBorderTop(BorderStyle.MEDIUM);//上边框
			style.setBorderBottom(BorderStyle.MEDIUM);//下边框
			style.setBorderLeft(BorderStyle.MEDIUM);//左边框
			style.setBorderRight(BorderStyle.MEDIUM);//右边框
			//每个学生为一个sheet
			List<Student> sList = studentMap.get(clazz.getId());
			if(CollectionUtils.isNotEmpty(sList)){
				sList.sort(new Comparator<Student>() {

					@Override
					public int compare(Student o1, Student o2) {
						if(StringUtils.isBlank(o1.getStudentCode())) {
							return -1;
						}
						if(StringUtils.isBlank(o2.getStudentCode())) {
							return 0;
						}
						return o1.getStudentCode().compareTo(o2.getStudentCode());
					}
				});
				//工作表创建及样式
				sheet = workbook.createSheet(clazz.getClassNameDynamic());
				sheet.setDefaultColumnWidth(15);//默认列宽
				sheet.setColumnWidth(0, 256*10);//上下午列宽
				sheet.setColumnWidth(1, 256*5);//节次列宽
				int index = -1;
				for(Student student : sList){
					
					List<String> stuClaIds = stuClassMap.get(student.getId());
					stuScheduleList = new ArrayList<CourseSchedule>();
					for (String claId : stuClaIds) {
						if(classScheduleMap.containsKey(claId)){
							stuScheduleList.addAll(classScheduleMap.get(claId));
						}
					}
					if(CollectionUtils.isEmpty(stuScheduleList)){
						continue;//如果课程为空，不导出
					}
					
					index++;
					int actRow = index*(lessonCount+3);
					
					//标题
					titleName = searchAcadyear + "年第" + searchSemester+"学期第"+week+"周" + student.getStudentName() +"的课程表";
					titleRow = sheet.createRow(actRow);
					titleRow.setHeightInPoints(4*sheet.getDefaultRowHeightInPoints());
					titleCell = titleRow.createCell(0);
					titleCell.setCellValue(new HSSFRichTextString(titleName));
					titleCell.setCellStyle(style);
					craTitle = new CellRangeAddress(actRow,actRow,0,titleList.size()-1);
					sheet.addMergedRegion(craTitle);
					RegionUtil.setBorderTop(BorderStyle.MEDIUM, craTitle, sheet); // 上边框
					RegionUtil.setBorderBottom(BorderStyle.MEDIUM, craTitle, sheet); // 下边框
					RegionUtil.setBorderLeft(BorderStyle.MEDIUM, craTitle, sheet); // 左边框
					RegionUtil.setBorderRight(BorderStyle.MEDIUM, craTitle, sheet); // 右边框
					
					//周一到周日
					HSSFRow titleListRow = sheet.createRow(actRow+1);
					for (int i=0; i<titleList.size(); i++) {
						 HSSFCell cell = titleListRow.createCell(i);
						 cell.setCellStyle(style);
						 cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
					}
					
					//早上下午晚上
					if(morn>1){
						craMorn = new CellRangeAddress(actRow+2,actRow+morn+2-1,0,0);
						sheet.addMergedRegion(craMorn);
					}
					if(am>1){
						craAm = new CellRangeAddress(actRow+morn+2,actRow+morn+am+2-1,0,0);
						sheet.addMergedRegion(craAm);
					}
					if(pm>1){
						craPm = new CellRangeAddress(actRow+morn+am+2,actRow+morn+am+pm+2-1,0,0);
						sheet.addMergedRegion(craPm);
					}
					if(night>1){
			        	craNight = new CellRangeAddress(actRow+morn+am+pm+2,actRow+lessonCount+2-1,0,0);
			        	sheet.addMergedRegion(craNight);
			        }
					for (int i = 2; i < lessonCount+2; i++) {
						HSSFRow inRow = sheet.createRow(actRow+i);
						inRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
						for (int j = 0; j < titleList.size(); j++) {
							HSSFCell inCell = inRow.createCell(j);
							inCell.setCellStyle(style);
							if(j==0){
								if((craMorn!=null && actRow+i==craMorn.getFirstRow())||(morn==1&&i==2)){
									inCell.setCellValue(new HSSFRichTextString("早自习"));
								}
								if((craAm!=null && actRow+i==craAm.getFirstRow())||(am==1&&i==morn+2)){
									inCell.setCellValue(new HSSFRichTextString("上午"));
								}
								if((craPm!=null && actRow+i==craPm.getFirstRow())||(pm==1&&i==morn+am+2)){
									inCell.setCellValue(new HSSFRichTextString("下午"));
								}
								if((craNight!=null && actRow+i==craNight.getFirstRow())||(night==1&&i==morn+am+pm+2)){
									inCell.setCellValue(new HSSFRichTextString("晚上"));
								}
							}
							if(j==1){
								inCell.setCellValue(i-1);
							}
						}
					}
					
					//课程
					for (CourseSchedule cs : stuScheduleList) {
						int acPeriod = cs.getPeriod();
						if(BaseConstants.PERIOD_INTERVAL_2.equals(cs.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()==null?0:grade.getMornPeriods();
						}
						if(BaseConstants.PERIOD_INTERVAL_3.equals(cs.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()==null?0:grade.getMornPeriods()+grade.getAmLessonCount();
						}
						if(BaseConstants.PERIOD_INTERVAL_4.equals(cs.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()==null?0:grade.getMornPeriods()+grade.getAmLessonCount()+grade.getPmLessonCount();
						}
						HSSFRow inRow = sheet.getRow(actRow+acPeriod+1);
						if(inRow==null){
							continue;
						}
						HSSFCell inCell = inRow.getCell(cs.getDayOfWeek()+2);
						//防止报空
						if(inCell==null) {
							continue;
						}
                        String cellValue;
                        if (StringUtils.isNotBlank(inCell.getRichStringCellValue().getString())) {
                            cellValue = inCell.getRichStringCellValue().getString() + "\n";
                        } else {
                            cellValue = "";
                        }
						if(StringUtils.isNotBlank(cs.getClassName())) {
							cellValue+=cs.getClassName();
						}
						if(StringUtils.isNotBlank(cs.getSubAndTeacherName())) {
							if(StringUtils.isNotBlank(cellValue)) {
								cellValue=cellValue+"\n"+(showTeacher ? cs.getSubAndTeacherName() : cs.getSubjectName());
							}else {
								cellValue=(showTeacher ? cs.getSubAndTeacherName() : cs.getSubjectName());
							}
						}
						if(StringUtils.isNotBlank(cs.getPlaceName())) {
							if(StringUtils.isNotBlank(cellValue)) {
								cellValue=cellValue+"\n"+(showPlace ? cs.getPlaceName() : "");
							}else {
								cellValue=(showPlace ? cs.getPlaceName() : "");
							}
						}
//						String cellValue = cs.getClassName()+"\n"+cs.getSubAndTeacherName()+"\n"+cs.getPlaceName();
						inCell.setCellValue(new HSSFRichTextString(cellValue));
					}
					
					//打印设置
					sheet.setAutobreaks(false);//不自动分页
					for (int i = 0; i < sheet.getLastRowNum(); i++) {
						if ((i+1) % (lessonCount+3) == 0){
							sheet.setRowBreak(i);//设置每lessonCount+3行分页打印 
						}
					}
					HSSFPrintSetup printSetup = sheet.getPrintSetup();//打印设置
					printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //A4纸
					printSetup.setLandscape(true);//横向打印
					
				}
			}
			
			//写入文件
			try {
				File file = new File(dirFile, clazz.getClassNameDynamic() + ".xls");
				OutputStream stream = new FileOutputStream(file);
				workbook.write(stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//打包
		try {
			String zip = ZipUtils.makeZip(dirName);
			ExportUtils.outputFile(zip,resp);
			new File(zip).delete();
			FileUtils.deleteDirectory(dirFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnSuccess();
    }
    
    @RequestMapping("/classStu/exportTimetableClaXls")
   	@ResponseBody
   	@ControllerInfo("年级所有班级课程表Excel导出")
	public String exportClaResult(String searchAcadyear, String searchSemester, String week, String id, boolean showPlace, boolean showTeacher, HttpServletResponse resp){
       	Grade grade = gradeRemoteService.findOne(id);
   		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		String teacherId = loginInfo.getOwnerId();
		//行政班
		List<Clazz> clazzList = new ArrayList<Clazz>();
		if(!isAdmin(unitId, loginInfo.getUserId())&&!teacherId.equals(grade.getTeacherId())){
			clazzList = classRemoteService.findBySchoolIdAndGradeIds(unitId, new String[]{id});
			clazzList = clazzList.stream().filter(e->teacherId.equals(e.getTeacherId())||teacherId.equals(e.getViceTeacherId())).collect(Collectors.toList());
    	}else{
    		clazzList = classRemoteService.findByGradeId(id);
    	}
		for(Clazz clazz : clazzList){
			clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
		}
   		Map<String, String> classId2Name = clazzList.stream().collect(Collectors.toMap(Clazz::getId, Clazz::getClassNameDynamic));
   		
   		//获取当前条件下所有的课程表数据并组装，按班级分类
        List<CourseSchedule> csList = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.parseInt(searchSemester), classId2Name.keySet().toArray(new String[0]), Integer.parseInt(week));

        List<CourseSchedule> timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.parseInt(searchSemester), classId2Name.keySet().toArray(new String[0]), Integer.parseInt(week) + 1);
        if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
            timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByClassIdes(searchAcadyear, Integer.parseInt(searchSemester), classId2Name.keySet().toArray(new String[0]), Integer.parseInt(week) - 1);
        }
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
            for (CourseSchedule one : timetableCourseScheduleTmp) {
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                    csList.add(one);
                }
            }
        }


   		makeCourse(csList, classId2Name , "1");
   		Map<String,List<CourseSchedule>> classScheduleMap = new HashMap<String, List<CourseSchedule>>();
   		for (CourseSchedule cs : csList) {
   			if(!classScheduleMap.containsKey(cs.getClassId())){
   				classScheduleMap.put(cs.getClassId(), new ArrayList<CourseSchedule>());
   			}
   			classScheduleMap.get(cs.getClassId()).add(cs);
   		}
   		//表名
   		String bookName = searchAcadyear + "年第" + searchSemester+"学期" + grade.getGradeName()+ "第"+week+"周班级课程表";
        
   		int morn = grade.getMornPeriods() == null ? 0 : grade.getMornPeriods();
		int am = grade.getAmLessonCount() == null ? 0 : grade.getAmLessonCount();
		int pm = grade.getPmLessonCount() == null ? 0 : grade.getPmLessonCount();
		int night = grade.getNightLessonCount() == null ? 0 : grade.getNightLessonCount();
		//excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = null;
        int lessonCount = morn+am+pm+night;
        CellRangeAddress craMorn = null;
        CellRangeAddress craAm = null;
    	CellRangeAddress craPm = null;
    	CellRangeAddress craNight = null;
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        //年级为一个excel
        HSSFSheet sheet = workbook.createSheet(bookName);
        sheet.setDefaultColumnWidth(15);//默认列宽
        sheet.setColumnWidth(0, 256*10);//上下午列宽
        sheet.setColumnWidth(1, 256*5);//节次列宽
        //表格样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//水平
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行
        //CellStyle.BORDER_THICK
        style.setBorderTop(BorderStyle.MEDIUM);//上边框
        style.setBorderBottom(BorderStyle.MEDIUM);//下边框
        style.setBorderLeft(BorderStyle.MEDIUM);//左边框
        style.setBorderRight(BorderStyle.MEDIUM);//右边框
        
        
		String titleName = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
   		List<CourseSchedule> claScheduleList = null;
   		int index = -1;
   		for (Clazz clazz : clazzList) {
   			
   			claScheduleList = classScheduleMap.get(clazz.getId());
			if(CollectionUtils.isEmpty(claScheduleList)){
				continue;//如果课程为空，不导出
			}
			
			index++;
			int actRow = index*(lessonCount+3);
			
			//标题
			titleName = searchAcadyear + "年第" + searchSemester+"学期第"+week+"周" + clazz.getClassNameDynamic() +"的课程表";
			titleRow = sheet.createRow(actRow);
			titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
			titleCell = titleRow.createCell(0);
			titleCell.setCellValue(new HSSFRichTextString(titleName));
			titleCell.setCellStyle(style);
			craTitle = new CellRangeAddress(actRow,actRow,0,titleList.size()-1);
			sheet.addMergedRegion(craTitle);
			RegionUtil.setBorderTop(style.getBorderTopEnum(), craTitle, sheet); // 上边框
			RegionUtil.setBorderBottom(style.getBorderBottomEnum(), craTitle, sheet); // 下边框
			RegionUtil.setBorderLeft(style.getBorderLeftEnum(), craTitle, sheet); // 左边框
			RegionUtil.setBorderRight(style.getBorderRightEnum(), craTitle, sheet); // 右边框
			
			//周一到周日
			HSSFRow titleListRow = sheet.createRow(actRow+1);
			for (int i=0; i<titleList.size(); i++) {
				 HSSFCell cell = titleListRow.createCell(i);
				 cell.setCellStyle(style);
				 cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
			}
			//早上下午晚上
			if(morn>1){
				craMorn = new CellRangeAddress(actRow+2,actRow+morn+2-1,0,0);
				sheet.addMergedRegion(craMorn);
			}
			if(am>1){
				craAm = new CellRangeAddress(actRow+morn+2,actRow+morn+am+2-1,0,0);
				sheet.addMergedRegion(craAm);
			}
			if(pm>1){
				craPm = new CellRangeAddress(actRow+morn+am+2,actRow+morn+am+pm+2-1,0,0);
				sheet.addMergedRegion(craPm);
			}
			if(night>1){
	        	craNight = new CellRangeAddress(actRow+morn+am+pm+2,actRow+lessonCount+2-1,0,0);
	        	sheet.addMergedRegion(craNight);
	        }
			for (int i = 2; i < lessonCount+2; i++) {
				HSSFRow inRow = sheet.createRow(actRow+i);
				inRow.setHeightInPoints(5*sheet.getDefaultRowHeightInPoints());
				for (int j = 0; j < titleList.size(); j++) {
					HSSFCell inCell = inRow.createCell(j);
					inCell.setCellStyle(style);
					if(j==0){
						if((craMorn!=null && actRow+i==craMorn.getFirstRow())||(morn==1&&i==2)){
							inCell.setCellValue(new HSSFRichTextString("早自习"));
						}
						if((craAm!=null && actRow+i==craAm.getFirstRow())||(am==1&&i==morn+2)){
							inCell.setCellValue(new HSSFRichTextString("上午"));
						}
						if((craPm!=null && actRow+i==craPm.getFirstRow())||(pm==1&&i==morn+am+2)){
							inCell.setCellValue(new HSSFRichTextString("下午"));
						}
						if((craNight!=null && actRow+i==craNight.getFirstRow())||(night==1&&i==morn+am+pm+2)){
							inCell.setCellValue(new HSSFRichTextString("晚上"));
						}
					}
					if(j==1){
						inCell.setCellValue(i-1);
					}
				}
			}
			//课程
			for (CourseSchedule cs : claScheduleList) {
				int acPeriod = cs.getPeriod();
				if(BaseConstants.PERIOD_INTERVAL_2.equals(cs.getPeriodInterval())){
					acPeriod+=grade.getMornPeriods()==null?0:grade.getMornPeriods();
				}
				if(BaseConstants.PERIOD_INTERVAL_3.equals(cs.getPeriodInterval())){
					acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount();
				}
				if(BaseConstants.PERIOD_INTERVAL_4.equals(cs.getPeriodInterval())){
					acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount()+grade.getPmLessonCount();
				}
				HSSFRow inRow = sheet.getRow(actRow+acPeriod+1);
				if(inRow == null) {
				    continue;
                }
				HSSFCell inCell = inRow.getCell(cs.getDayOfWeek()+2);
				//防止报空
				if(inCell==null) {
					continue;
				}
                String cellValue;
                if (StringUtils.isNotBlank(inCell.getRichStringCellValue().getString())) {
				    cellValue = inCell.getRichStringCellValue().getString() + "\n";
                } else {
                    cellValue = "";
                }
				if(StringUtils.isNotBlank(cs.getSubjectName())) {
					cellValue+=cs.getSubjectName();
				}
				if(StringUtils.isNotBlank(cs.getTeacherName())) {
					if(StringUtils.isNotBlank(cellValue)) {
						cellValue=cellValue+"\n"+(showTeacher ? cs.getTeacherName() : "");
					}else {
						cellValue=(showTeacher ? cs.getTeacherName() : "");
					}
				}
				if(StringUtils.isNotBlank(cs.getPlaceName())) {
					if(StringUtils.isNotBlank(cellValue)) {
						cellValue=cellValue+"\n"+(showPlace ? cs.getPlaceName() : "");
					}else {
						cellValue=(showPlace ? cs.getPlaceName() : "");
					}
				}
//				String cellValue = cs.getSubjectName()+"\n"+cs.getTeacherName()+"\n"+cs.getPlaceName();
				inCell.setCellValue(new HSSFRichTextString(cellValue));
			}
   		}
   		
   		//打印设置
   		sheet.setAutobreaks(false);//不自动分页
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			if ((i+1) % (lessonCount+3) == 0){
				sheet.setRowBreak(i);//设置每lessonCount+3行分页打印 
			}
		}
		HSSFPrintSetup printSetup = sheet.getPrintSetup();//打印设置
		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //A4纸
		printSetup.setLandscape(true);//横向打印
		
   		//导出文件
   		ExportUtils.outputData(workbook, bookName, resp);
   		return returnSuccess();
       }
    
    private List<String> getTitleList(int weekDays){
        List<String> titleList = new ArrayList<String>();
        titleList.add("");
        titleList.add("");
        titleList.add("周一");
        titleList.add("周二");
        titleList.add("周三");
        titleList.add("周四");
        titleList.add("周五");
        if(weekDays>5){
        	titleList.add("周六");
            titleList.add("周日");
        }
        return titleList;
    }

}
