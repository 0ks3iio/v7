/*
 * @(#)ClassStuTimetableAction.java    Created on 2017年3月8日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id: TeacherTimetableAction.java 4653 2017-09-11 07:33:38Z zengzt $
 */
package net.zdsoft.basedata.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.ZipUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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

/**
 * @author zhaosheng
 * @version $Revision: 4653 $, $Date: 2017-09-11 15:33:38 +0800 (周一, 11 九月 2017) $
 */
@Controller
@RequestMapping("/timetable")
public class TeacherTimetableAction extends RoleCommonAction {

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
    private TeacherService teacherRemoteService;
    @Autowired
    private CourseScheduleService courseScheduleRemoteService;
    @Autowired
    private TeachClassService teachClassRemoteService;
    @Autowired
    private TeachPlaceService teachPlaceRemoteService;
    @Autowired
    private SchoolCalendarService schoolCalendarRemoteService;
    @Autowired
    private DeptService deptRemoteService;
    @Autowired
    private TeachGroupService teachGroupService;
    @Autowired
    private TeachGroupExService teachGroupExService;
    @RequestMapping("/teacher/index/page")
    @ControllerInfo("教师课程表首页")
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
        // 获取当前周次

        map.put("acadyearList", acadyearList);
        map.put("acadyearSearch", acadyearSearch);
        map.put("semesterSearch", semesterSearch);

        Unit u = unitService.findOne(unitId);
        Map<String, Integer> cur2Max = schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(acadyearSearch, semesterSearch, u.getId());
        Integer weekSearch = cur2Max.get("current");
        if(weekSearch==null){
        	weekSearch=1;
        }
        map.put("weekSearch", weekSearch);
        map.put("max", cur2Max.get("max"));
        map.put("unitName", u.getUnitName());
        map.put("isAdmin", isAdmin(unitId, loginInfo.getUserId()));
        return "basedata/courseSchedule/teacher/timetableIndex.ftl";
    }

    @RequestMapping("/teacher/timeTable/page")
    @ControllerInfo("课程表详情")
    public String showTimeTable(String searchAcadyear, String searchSemester, String id, String week, String type, boolean showPlace, boolean showClass, ModelMap map) {
        map.put("showPlace", showPlace);
        map.put("showClass", showClass);

    	// 拼接标题
    	String realName;
    	if("1".equals(type)){
    		TeachGroup teachGroup = teachGroupService.findOne(id);
    		if(teachGroup==null){
    			realName = "未分配教研组";
    		}else{
    			realName = teachGroup.getTeachGroupName();
    		}
    	}else{
    		Teacher teacher = teacherRemoteService.findOne(id);
    		realName = teacher.getTeacherName();
    	}
        String title = searchAcadyear + "年" + "第" + searchSemester + "学期" + realName + "的课程表";
        map.put("title", title);
        if (StringUtils.isBlank(week)) {
            return errorFtl(map, "周次不存在，请先维护基础信息节假日设置");
        }
        // 早上下晚时间段的 课程数
        Map<String, Integer> timeIntervalMap = new LinkedHashMap<String, Integer>();
        // 查出教师所有行政班任课信息
        List<CourseSchedule> timetableCourseScheduleList = new ArrayList<CourseSchedule>();
		List<CourseSchedule> timetableCourseScheduleTmp = null;
        Set<String> teacherIds = new HashSet<String>();

        if("1".equals(type)){
        	List<TeachGroupEx> groupExList = teachGroupExService.findByTeachGroupId(new String[]{id});
        	if(CollectionUtils.isNotEmpty(groupExList)){
        		teacherIds = EntityUtils.getSet(groupExList, TeachGroupEx::getTeacherId);
        		timetableCourseScheduleList = courseScheduleRemoteService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.valueOf(searchSemester), Integer.valueOf(week), teacherIds.toArray(new String[0]));
                // 是否存在单双周问题

                timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.valueOf(searchSemester), Integer.valueOf(week) + 1, teacherIds.toArray(new String[0]));
                if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
                    timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.valueOf(searchSemester), Integer.valueOf(week) - 1, teacherIds.toArray(new String[0]));
                }
                if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
                    for (CourseSchedule one : timetableCourseScheduleTmp) {
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                            timetableCourseScheduleList.add(one);
                        }
                    }
                }
            }

        }else{
            timetableCourseScheduleList = courseScheduleRemoteService.findCourseScheduleListByTeacherId(searchAcadyear, Integer.valueOf(searchSemester), id, Integer.valueOf(week));

            timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByTeacherId(searchAcadyear, Integer.valueOf(searchSemester), id, Integer.valueOf(week) + 1);
            if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
                timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByTeacherId(searchAcadyear, Integer.valueOf(searchSemester), id, Integer.valueOf(week) - 1);
            }
            if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
                for (CourseSchedule one : timetableCourseScheduleTmp) {
                    if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                        timetableCourseScheduleList.add(one);
                    }
                }
            }
        }

		Set<String> gradeIds = new HashSet<String>();
        Map<String, String> classId2Name = new HashMap<String, String>();
        Map<String, String> classId2GradeId = new HashMap<String, String>();

        Set<String> classIds = EntityUtils.getSet(timetableCourseScheduleList, CourseSchedule::getClassId);
        List<Clazz> classes = classRemoteService.findListByIds(classIds.toArray(new String[0]));
        if(CollectionUtils.isNotEmpty(classes)){
	        for (Clazz c : classes) {
	            gradeIds.add(c.getGradeId());
	            classId2GradeId.put(c.getId(), c.getGradeId());
	            classId2Name.put(c.getId(), c.getClassNameDynamic());
	        }
        }
     // 获取学年学期下学生的所有的教学班List
        List<TeachClass> teachClasses = teachClassRemoteService.findListByIds(classIds.toArray(new String[0]));
        if(CollectionUtils.isNotEmpty(teachClasses)){
	        for (TeachClass c : teachClasses) {
	            gradeIds.add(c.getGradeId());
	            classId2GradeId.put(c.getId(), c.getGradeId());
	            classId2Name.put(c.getId(), c.getName());
	        }
        }

        Grade grades = gradeRemoteService.findTimetableMaxRangeBySchoolId(null, gradeIds.toArray(new String[0]));
        if (Integer.valueOf(0).compareTo(grades.getMornPeriods())<0) {
            timeIntervalMap.put("1", grades.getMornPeriods());
        }
        timeIntervalMap.put("2", grades.getAmLessonCount());
        timeIntervalMap.put("3", grades.getPmLessonCount());
        if (Integer.valueOf(0).compareTo(grades.getNightLessonCount())<0) {
            timeIntervalMap.put("4", grades.getNightLessonCount());
        }
        map.put("edudays", grades.getWeekDays());
        // timetableCourseScheduleList中className,subjectName,placeName内容填充
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
            Set<String> subjectIds = new HashSet<String>();
            Map<String, Course> id2CourseName = new HashMap<String, Course>();
            Set<String> placeIds = new HashSet<String>();
            Map<String, String> id2PlaceName = new HashMap<String, String>();
            Map<String, String> id2TeacherName = new HashMap<String, String>();
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (StringUtils.isBlank(courseSchedule.getSubjectName()) && StringUtils.isNotBlank(courseSchedule.getSubjectId())) {
                    subjectIds.add(courseSchedule.getSubjectId());
                }
                if (courseSchedule.getPlaceId() != null) {
                    placeIds.add(courseSchedule.getPlaceId());
                }
            }
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = courseRemoteService.findListByIds(subjectIds.toArray(new String[0]));
                if(CollectionUtils.isNotEmpty(courseList))
                   id2CourseName = EntityUtils.getMap(courseList, Course::getId);

            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                id2PlaceName = teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0]));
            }
            if(CollectionUtils.isNotEmpty(teacherIds)){
            	id2TeacherName = teacherRemoteService.findPartByTeacher(teacherIds.toArray(new String[0]));
            }
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
            	//课程名称
				if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
					Course cs = id2CourseName.get(courseSchedule.getSubjectId());
					if (cs != null) {
						courseSchedule.setSubjectName(cs.getSubjectName());
						if (StringUtils.isNotEmpty(cs.getBgColor())) {
							String[] bcs = cs.getBgColor().split(",");
							if (StringUtils.isNotEmpty(bcs[0])) {
								courseSchedule.setBgColor(bcs[0]);
							}
							if (bcs.length > 1 && StringUtils.isNotEmpty(bcs[1])) {
								courseSchedule.setBorderColor(bcs[1]);
							}
						}
					} else {
						courseSchedule.setSubjectName(courseSchedule.getSubjectName());
					}
					if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(courseSchedule.getWeekType())) {
						courseSchedule.setSubjectName(courseSchedule.getSubjectName() + ODD_WEEK);
					}
					if (Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(courseSchedule.getWeekType())) {
						courseSchedule.setSubjectName(courseSchedule.getSubjectName() + EVEN_WEEK);
					}
				}
                //场地
                if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
                    courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
                }
                // 在行政班上的课的班级名称填充
                if (StringUtils.isBlank(courseSchedule.getClassName())) {
                	String claname = classId2Name.get(courseSchedule.getClassId());
                	if(StringUtils.isNotBlank(claname)){
                		courseSchedule.setClassName(claname);
                	}
                }
                if("1".equals(type)){
                	String tname = id2TeacherName.get(courseSchedule.getTeacherId());
                	if(id2TeacherName != null && id2TeacherName.get(courseSchedule.getTeacherId()) !=null){
                		courseSchedule.setTeacherName(tname);
                		String pingyingFirse=PinyinUtils.toHanyuPinyin(tname, true);
						if(pingyingFirse==null) {
							pingyingFirse="";
						}
						//获取首字母用来排序
						courseSchedule.setRemark(pingyingFirse);
                	}
                }
            }
        }
        //把原来的 timetableCourseScheduleList 改成2个map,一个为单节课的,一个为多节课的

		HashMap<String, List<CourseSchedule>> courseScheduleListMap = new HashMap<>();
		for(CourseSchedule c:timetableCourseScheduleList){
			Integer dayOfWeek = c.getDayOfWeek();
			int period = c.getPeriod();
			String periodInterval = c.getPeriodInterval();
			if(dayOfWeek==null ||period==0 || StringUtils.isBlank(periodInterval)){
				continue;
			}
			String key=""+dayOfWeek+"_"+period+"_"+periodInterval;
			if(courseScheduleListMap.get(key)!=null){
				courseScheduleListMap.get(key).add(c);
			}else{
				ArrayList<CourseSchedule> list = new ArrayList<>();
				list.add(c);
				courseScheduleListMap.put(key,list);
			}

		}
		
		if("1".equals(type)){
			for (Entry<String, List<CourseSchedule>> entry : courseScheduleListMap.entrySet()) {
				if(entry.getValue().size()>1){
					//根据名称首字母排序
					Collections.sort(entry.getValue(), new Comparator<CourseSchedule>() {
						@Override
						public int compare(CourseSchedule o1, CourseSchedule o2) {
							return o1.getRemark().compareTo(o2.getRemark());
						}
						
					});
				}
			}
		}
		map.put("courseScheduleListMap",courseScheduleListMap);
		map.put("type", type);
		//老版本
		//map.put("timetableCourseScheduleList", timetableCourseScheduleList);
        map.put("timeIntervalMap", timeIntervalMap);
        return "basedata/courseSchedule/teacher/timetableDetail.ftl";
    }

    @RequestMapping("/teacher/exportTimetableZip")
	@ResponseBody
	@ControllerInfo("学校所有老师课程表Excel打包导出zip")
    public String exportResult(String searchAcadyear, String searchSemester, String week, String id, HttpServletResponse resp){
    	List<Teacher> teacherList = teacherRemoteService.findByUnitId(id);
    	teacherList = teacherList.stream().filter(e->StringUtils.isNotBlank(e.getDeptId())).collect(Collectors.toList());
    	String[] teacherIds = EntityUtils.getList(teacherList, Teacher::getId).toArray(new String[0]);
    	//获取部门和所有老师的映射
    	Map<String, List<Teacher>> deptTeacherMap = EntityUtils.getListMap(teacherList, Teacher::getDeptId, Function.identity());
    	List<Dept> deptList = deptRemoteService.findListByIds(deptTeacherMap.keySet().toArray(new String[0]));

    	Map<String, List<CourseSchedule>> teacherScheduleMap = courseScheduleRemoteService.findCourseScheduleMapByTeacherId(searchAcadyear, searchSemester, teacherIds, Integer.parseInt(week));
    	//List<CourseSchedule> csList = courseScheduleRemoteService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.parseInt(searchSemester), Integer.parseInt(week), teacherIds),CourseSchedule.class);
    	List<CourseSchedule> csList = new ArrayList<CourseSchedule>();
    	if(MapUtils.isNotEmpty(teacherScheduleMap)){
    		for (Entry<String, List<CourseSchedule>> entry : teacherScheduleMap.entrySet()) {
				csList.addAll(entry.getValue());
			}
    	}

    	// 单双周
        Map<String, List<CourseSchedule>> teacherScheduleMapTmp = courseScheduleRemoteService.findCourseScheduleMapByTeacherId(searchAcadyear, searchSemester, teacherIds, Integer.parseInt(week) + 1);
        if (MapUtils.isEmpty(teacherScheduleMapTmp)) {
            teacherScheduleMapTmp = courseScheduleRemoteService.findCourseScheduleMapByTeacherId(searchAcadyear, searchSemester, teacherIds, Integer.parseInt(week) - 1);
        }
        if (MapUtils.isNotEmpty(teacherScheduleMapTmp)) {
            for (Entry<String, List<CourseSchedule>> entry : teacherScheduleMapTmp.entrySet()) {
                Iterator<CourseSchedule> iterator = entry.getValue().iterator();
                while (iterator.hasNext()) {
                    CourseSchedule one = iterator.next();
                    if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                        csList.add(one);
                    } else {
                        iterator.remove();
                    }
                }
            }
        }

		String[] classIds = EntityUtils.getSet(csList, CourseSchedule::getClassId).toArray(new String[0]);

    	Set<String> gradeIdSet = new HashSet<String>();
    	Map<String,String> classId2Name = new HashMap<String, String>();

    	List<Clazz> classList = classRemoteService.findListByIds(classIds);
    	for (Clazz clazz : classList) {
			gradeIdSet.add(clazz.getGradeId());
			classId2Name.put(clazz.getId(), clazz.getClassNameDynamic());
		}
    	List<TeachClass> teachClassList = teachClassRemoteService.findListByIds(classIds);
    	for (TeachClass teachClass : teachClassList) {
			if(StringUtils.isNotBlank(teachClass.getGradeId())){
				String[] inIds = teachClass.getGradeId().split(",");
				gradeIdSet.addAll(Arrays.asList(inIds));
			}
			classId2Name.put(teachClass.getId(), teachClass.getName());
		}

    	//csList中className,subjectName,placeName内容填充
        if (CollectionUtils.isNotEmpty(csList)) {
            Set<String> subjectIds = new HashSet<String>();
            Map<String, String> id2CourseName = new HashMap<String, String>();
            Set<String> placeIds = new HashSet<String>();
            Map<String, String> id2PlaceName = new HashMap<String, String>();
            for (CourseSchedule courseSchedule : csList) {
                if (StringUtils.isBlank(courseSchedule.getSubjectName()) && StringUtils.isNotBlank(courseSchedule.getSubjectId())) {
                    subjectIds.add(courseSchedule.getSubjectId());
                }
                if (courseSchedule.getPlaceId() != null) {
                    placeIds.add(courseSchedule.getPlaceId());
                }
            }
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = courseRemoteService.findListByIds(subjectIds.toArray(new String[0]));
                if(CollectionUtils.isNotEmpty(courseList))
                   id2CourseName = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);

            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                id2PlaceName = teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0]));
            }

            for (Entry<String, List<CourseSchedule>> entry : teacherScheduleMap.entrySet()) {
            	for (CourseSchedule courseSchedule : entry.getValue()) {
            		//课程名称
            		if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
            			String subname = id2CourseName.get(courseSchedule.getSubjectId());
            			if(StringUtils.isNotBlank(subname)){
            				courseSchedule.setSubjectName(subname);
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
            		//场地
            		if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
            			courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
            		}else{
            			courseSchedule.setPlaceName("");
            		}
            		//班级名称填充
            		if (StringUtils.isBlank(courseSchedule.getClassName())) {
            			String claname = classId2Name.get(courseSchedule.getClassId());
            			if(StringUtils.isNotBlank(claname)){
            				courseSchedule.setClassName(claname);
            			}else{
            				courseSchedule.setClassName("");
            			}
            		}
            	}
            }
			for (Entry<String, List<CourseSchedule>> entry : teacherScheduleMapTmp.entrySet()) {
				for (CourseSchedule courseSchedule : entry.getValue()) {
					//课程名称
					if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
						String subname = id2CourseName.get(courseSchedule.getSubjectId());
						if(StringUtils.isNotBlank(subname)){
							courseSchedule.setSubjectName(subname);
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
					//场地
					if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
						courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
					}else{
						courseSchedule.setPlaceName("");
					}
					//班级名称填充
					if (StringUtils.isBlank(courseSchedule.getClassName())) {
						String claname = classId2Name.get(courseSchedule.getClassId());
						if(StringUtils.isNotBlank(claname)){
							courseSchedule.setClassName(claname);
						}else{
							courseSchedule.setClassName("");
						}
					}
				}
			}
        }

    	Grade grade = gradeRemoteService.findTimetableMaxRangeBySchoolId(null, gradeIdSet.toArray(new String[0]));
    	int morn = grade.getMornPeriods();
    	int am = grade.getAmLessonCount();
    	int pm = grade.getPmLessonCount();
        int night = grade.getNightLessonCount();

        //创建文件夹
        String dirName = searchAcadyear + "年第" + searchSemester+"学期" + "第"+week+"周教师课程表";
        File dirFile = new File(dirName);
        if(dirFile.exists()){
        	dirFile.delete();
        }
        dirFile.mkdirs();

        //excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = null;
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
		List<CourseSchedule> teacherScheduleList = null;
		//每部门为一个excel
		for (Dept dept : deptList) {

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

			//每个老师为一个sheet
			List<Teacher> tList = deptTeacherMap.get(dept.getId());
			if(CollectionUtils.isNotEmpty(tList)){
				Collections.sort(tList, new Comparator<Teacher>() {

					@Override
					public int compare(Teacher o1, Teacher o2) {
						return o1.getTeacherCode().compareTo(o2.getTeacherCode());
					}
				});
				//sheet样式
				sheet = workbook.createSheet(dept.getDeptName());
				sheet.setDefaultColumnWidth(15);//默认列宽
				sheet.setColumnWidth(0, 256*10);//上下午列宽
				sheet.setColumnWidth(1, 256*5);//节次列宽
				int index = -1;
				for(Teacher teacher : tList){

					teacherScheduleList = teacherScheduleMap.get(teacher.getId());
					if(CollectionUtils.isEmpty(teacherScheduleList)){
						continue;//如果课程为空，不导出
					}
					if(teacherScheduleMapTmp != null && CollectionUtils.isNotEmpty(teacherScheduleMapTmp.get(teacher.getId()))){
						teacherScheduleList.addAll(teacherScheduleMapTmp.get(teacher.getId()));
					}

					index++;
					int actRow = index*(lessonCount+3);

					//标题
					titleName = searchAcadyear + "年第" + searchSemester+"学期第" +week+"周"+ teacher.getTeacherName() +"的课程表";
					titleRow = sheet.createRow(actRow);
					titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
					titleCell = titleRow.createCell(0);
					titleCell.setCellValue(new HSSFRichTextString(titleName));
					titleCell.setCellStyle(style);
					craTitle = new CellRangeAddress(actRow,actRow,0,titleList.size()-1);
					sheet.addMergedRegion(craTitle);
					RegionUtil.setBorderTop(BorderStyle.MEDIUM, craTitle, sheet); // 上边框
					RegionUtil.setBorderBottom(BorderStyle.MEDIUM, craTitle, sheet);  // 下边框
					RegionUtil.setBorderLeft(BorderStyle.MEDIUM, craTitle, sheet); // 左边框
					RegionUtil.setBorderRight(BorderStyle.MEDIUM, craTitle, sheet);  // 右边框

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
								if((craNight!=null && actRow+i==craNight.getFirstRow())||night==1&&i==morn+am+pm+2){
									inCell.setCellValue(new HSSFRichTextString("晚上"));
								}
							}
							if(j==1){
								inCell.setCellValue(i-1);
							}
						}
					}

					//课程
					for (CourseSchedule cs : teacherScheduleList) {
						int acPeriod = cs.getPeriod();
						if(BaseConstants.PERIOD_INTERVAL_2.equals(cs.getPeriodInterval())){
							acPeriod+=morn;
						}
						if(BaseConstants.PERIOD_INTERVAL_3.equals(cs.getPeriodInterval())){
							acPeriod+=morn+am;
						}
						if(BaseConstants.PERIOD_INTERVAL_4.equals(cs.getPeriodInterval())){
							acPeriod+=morn+am+pm;
						}
						HSSFRow inRow = sheet.getRow(actRow+acPeriod+1);
						if(inRow==null){
							continue;
						}
						HSSFCell inCell = inRow.getCell(cs.getDayOfWeek()+2);

						if(inCell==null) {
							continue;
						}
						String cellValue = inCell.getStringCellValue();
						if(StringUtils.isNotBlank(cellValue)){
							if(StringUtils.isNotBlank(cs.getSubjectName())) {
								cellValue=cellValue+"\n"+cs.getSubjectName();
							}
							if(StringUtils.isNotBlank(cs.getTeacherName())) {
								cellValue=cellValue+"\n"+cs.getTeacherName();
							}
							if(StringUtils.isNotBlank(cs.getPlaceName())) {
								cellValue=cellValue+"\n"+cs.getPlaceName();
							}
//							cellValue += "\n"+cs.getClassName()+"\n"+cs.getSubjectName()+"\n"+cs.getPlaceName();
						}else{
							if(StringUtils.isNotBlank(cs.getClassName())) {
								cellValue=cs.getClassName();
							}
							if(StringUtils.isNotBlank(cs.getSubjectName())) {
								if(StringUtils.isNotBlank(cellValue)) {
									cellValue=cellValue+"\n"+cs.getSubjectName();
								}else {
									cellValue=cs.getSubjectName();
								}
							}
							if(StringUtils.isNotBlank(cs.getPlaceName())) {
								if(StringUtils.isNotBlank(cellValue)) {
									cellValue=cellValue+"\n"+cs.getPlaceName();
								}else {
									cellValue=cs.getPlaceName();
								}
							}
//							cellValue = cs.getClassName()+"\n"+cs.getSubjectName()+"\n"+cs.getPlaceName();
						}
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
			}
			//写入文件
			try {
				File file = new File(dirFile,dept.getDeptName()+".xls");
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
