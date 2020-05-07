package net.zdsoft.basedata.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachBuildingService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;

import org.apache.commons.collections.CollectionUtils;
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
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/timetable")
public class PlaceTimetableAction extends RoleCommonAction {

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
    private TeachBuildingService teachBuildingRemoteService;
    
    @RequestMapping("/place/index/page")
    @ControllerInfo("场地课程表首页")
    public String showIndex(ModelMap map, HttpSession httpSession) {
    	LoginInfo loginInfo = getLoginInfo(httpSession);
    	String unitId = loginInfo.getUnitId();
    	if(!isAdmin(unitId, loginInfo.getUserId())){
    		return errorFtl(map, NO_ROLE_MSG);
    	}
        List<String> acadyearList = semesterService.findAcadeyearList();
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = semesterService.findCurrentSemester(2,unitId);
        String acadyearSearch = semester.getAcadyear();
        String semesterSearch = semester.getSemester() + "";

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
        return "basedata/courseSchedule/place/timetableIndex.ftl";
    }

    @RequestMapping("/place/timeTable/page")
    @ControllerInfo("课程表详情")
    public String showTimeTable(String searchAcadyear, String searchSemester, String week, String type, String id,
            boolean showTeacher, boolean showClass, ModelMap map) {

        map.put("showTeacher", showTeacher);
        map.put("showClass", showClass);

        // 拼接标题
    	TeachPlace place = teachPlaceRemoteService.findOne(id);
        String title = searchAcadyear + "年" + "第" + searchSemester + "学期" + place.getPlaceName() + "的课程表";
        map.put("title", title);
        if (StringUtils.isBlank(week)) {
            return errorFtl(map, "周次不存在，请先维护基础信息节假日设置");
        }
        // 早上下晚时间段的 课程数
        Map<String, Integer> timeIntervalMap = new LinkedHashMap<String, Integer>();
        //查询场地
        List<CourseSchedule> timetableCourseScheduleList = courseScheduleRemoteService.findCourseScheduleListByPlaceId(place.getUnitId(), searchAcadyear, Integer.valueOf(searchSemester), id, Integer.valueOf(week), true);
        // 是否存在单双周问题

        List<CourseSchedule> timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByPlaceId(place.getUnitId(), searchAcadyear, Integer.valueOf(searchSemester), id, Integer.valueOf(week) + 1, true);
        if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
            timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByPlaceId(place.getUnitId(), searchAcadyear, Integer.valueOf(searchSemester), id, Integer.valueOf(week) - 1, true);
        }
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
            for (CourseSchedule one : timetableCourseScheduleTmp) {
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                    timetableCourseScheduleList.add(one);
                }
            }
        }

        Set<String> gradeIds = new HashSet<String>();
        Map<String, String> classId2Name = new HashMap<String, String>();
        Map<String, String> classId2GradeId = new HashMap<String, String>();
//        Map<String, Grade> gradeId2Grade = new HashMap<String, Grade>();
        
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
        
        Grade grade = gradeRemoteService.findTimetableMaxRangeBySchoolId(null,gradeIds.toArray(new String[0]));
        // 早上下晚时间段的 课程数
        grade.setMornPeriods(grade.getMornPeriods()==null?0:grade.getMornPeriods());
        if (grade.getMornPeriods() != 0) {
            timeIntervalMap.put(BaseConstants.PERIOD_INTERVAL_1, grade.getMornPeriods());
        }
        grade.setAmLessonCount(grade.getAmLessonCount()==null?0:grade.getAmLessonCount());
        if (grade.getAmLessonCount() != 0) {
            timeIntervalMap.put(BaseConstants.PERIOD_INTERVAL_2, grade.getAmLessonCount());
        }
        grade.setPmLessonCount(grade.getPmLessonCount()==null?0:grade.getPmLessonCount());
        if (grade.getPmLessonCount() != 0) {
            timeIntervalMap.put(BaseConstants.PERIOD_INTERVAL_3, grade.getPmLessonCount());
        }
        grade.setNightLessonCount(grade.getNightLessonCount()==null?0:grade.getNightLessonCount());
        if (grade.getNightLessonCount() != 0) {
            timeIntervalMap.put(BaseConstants.PERIOD_INTERVAL_4, grade.getNightLessonCount());
        }
        map.put("edudays", grade.getWeekDays());
        
        
        // timetableCourseScheduleList中className,subjectName,placeName内容填充
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
            Set<String> subjectIds = new HashSet<String>();
            Map<String, Course> id2CourseName = new HashMap<String, Course>();
            Map<String, String> teacherNameMap = new HashMap<String, String>();
            Set<String> teacherIds=new HashSet<String>();
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (StringUtils.isBlank(courseSchedule.getSubjectName()) && StringUtils.isNotBlank(courseSchedule.getSubjectId())) {
                    subjectIds.add(courseSchedule.getSubjectId());
                }
                if(CollectionUtils.isNotEmpty(courseSchedule.getTeacherIds())){
                	teacherIds.addAll(courseSchedule.getTeacherIds());
                }
            }
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = courseRemoteService.findListByIds(subjectIds.toArray(new String[0]));
                if(CollectionUtils.isNotEmpty(courseList))
                   id2CourseName = EntityUtils.getMap(courseList, Course::getId);
                
            }
            if(CollectionUtils.isNotEmpty(teacherIds)){
            	List<Teacher> teacherList = teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{}));
            	if(CollectionUtils.isNotEmpty(teacherList))
            		teacherNameMap = EntityUtils.getMap(teacherList,Teacher::getId,Teacher::getTeacherName);
            }
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
            	//课程名称
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
                }
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(courseSchedule.getWeekType())) {
                    courseSchedule.setSubjectName(courseSchedule.getSubjectName() + ODD_WEEK);
                }
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(courseSchedule.getWeekType())) {
                    courseSchedule.setSubjectName(courseSchedule.getSubjectName() + EVEN_WEEK);
                }
               
                // 在行政班上的课的班级名称填充
                if (StringUtils.isBlank(courseSchedule.getClassName())) {
                	String claname = classId2Name.get(courseSchedule.getClassId());
                	if(StringUtils.isNotBlank(claname)){
                		courseSchedule.setClassName(claname);
                	}
                }
                //填充教师
                if(CollectionUtils.isNotEmpty(courseSchedule.getTeacherIds())){
                	String tname=makeTeacherName(courseSchedule.getTeacherIds(),teacherNameMap);
                	courseSchedule.setTeacherName(tname);
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
  		map.put("courseScheduleListMap",courseScheduleListMap);
  		//老版本
  		//map.put("timetableCourseScheduleList", timetableCourseScheduleList);
        map.put("timeIntervalMap", timeIntervalMap);
        
        
        return "basedata/courseSchedule/place/timetableDetail.ftl";
    }

    @RequestMapping("/place/exportTimetableZip")
    @ResponseBody
    @ControllerInfo("建筑所有场地课程表Excel打包导出zip")
    public String exportResult(String searchAcadyear, String searchSemester, String week, String id, boolean showTeacher, boolean showClass, HttpServletResponse resp){
        TeachBuilding teachBuilding = teachBuildingRemoteService.findOne(id);
        List<TeachPlace> teachPlaceList = teachPlaceRemoteService.findListBy(new String[]{"teachBuildingId"},new Object[]{id});
        Map<String, String> teachPlaceIdToNameMap = EntityUtils.getMap(teachPlaceList, TeachPlace::getId, TeachPlace::getPlaceName);
        //获取场地Id -> 课表的映射
        Map<String, List<CourseSchedule>>  placeCourseMap = new HashMap<>();
        //获取出现的年级，以便后续获取上下午课时数
        Set<String> gradeIds = new HashSet<>();
        for (TeachPlace teachPlace : teachPlaceList) {
            //查询场地
            List<CourseSchedule> timetableCourseScheduleList = courseScheduleRemoteService.findCourseScheduleListByPlaceId(teachPlace.getUnitId(),searchAcadyear, Integer.valueOf(searchSemester), teachPlace.getId(), Integer.valueOf(week),true);
            // 是否存在单双周问题

            List<CourseSchedule> timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByPlaceId(teachPlace.getUnitId(), searchAcadyear, Integer.valueOf(searchSemester), teachPlace.getId(), Integer.valueOf(week) + 1, true);
            if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
                timetableCourseScheduleTmp = courseScheduleRemoteService.findCourseScheduleListByPlaceId(teachPlace.getUnitId(), searchAcadyear, Integer.valueOf(searchSemester), teachPlace.getId(), Integer.valueOf(week) - 1, true);
            }
            if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
                for (CourseSchedule one : timetableCourseScheduleTmp) {
                    if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                        timetableCourseScheduleList.add(one);
                    }
                }
            }


            Map<String, String> classId2Name = new HashMap<String, String>();
            Map<String, String> classId2GradeId = new HashMap<String, String>();
//            Map<String, Grade> gradeId2Grade = new HashMap<String, Grade>();

            Set<String> classIds = EntityUtils.getSet(timetableCourseScheduleList,e->e.getClassId());
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

            if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
                Set<String> subjectIds = new HashSet<String>();
                Map<String, String> id2CourseName = new HashMap<String, String>();
                Map<String, String> teacherNameMap = new HashMap<String, String>();
                Set<String> teacherIds=new HashSet<String>();
                for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                    if (StringUtils.isBlank(courseSchedule.getSubjectName()) && StringUtils.isNotBlank(courseSchedule.getSubjectId())) {
                        subjectIds.add(courseSchedule.getSubjectId());
                    }
                    if(CollectionUtils.isNotEmpty(courseSchedule.getTeacherIds())){
                        teacherIds.addAll(courseSchedule.getTeacherIds());
                    }
                }
                if (CollectionUtils.isNotEmpty(subjectIds)) {
                    List<Course> courseList = courseRemoteService.findListByIdIn(subjectIds.toArray(new String[0]));
                    if(CollectionUtils.isNotEmpty(courseList))
                        id2CourseName = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);

                }
                if(CollectionUtils.isNotEmpty(teacherIds)){
                    List<Teacher> teacherList = teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{}));
                    if(CollectionUtils.isNotEmpty(teacherList))
                        teacherNameMap = EntityUtils.getMap(teacherList,Teacher::getId,Teacher::getTeacherName);
                }
                for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                    //课程名称
                    if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
                        String subname = id2CourseName.get(courseSchedule.getSubjectId());
                        if(StringUtils.isNotBlank(subname)){
                            courseSchedule.setSubjectName(subname);
                        }else {
                            courseSchedule.setSubjectName(courseSchedule.getSubjectName());
                        }
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(courseSchedule.getWeekType())) {
                            courseSchedule.setSubjectName(courseSchedule.getSubjectName() + ODD_WEEK);
                        }
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(courseSchedule.getWeekType())) {
                            courseSchedule.setSubjectName(courseSchedule.getSubjectName() + EVEN_WEEK);
                        }
                    }

                    // 在行政班上的课的班级名称填充
                    if (StringUtils.isBlank(courseSchedule.getClassName())) {
                        String claname = classId2Name.get(courseSchedule.getClassId());
                        if(StringUtils.isNotBlank(claname)){
                            courseSchedule.setClassName(claname);
                        }
                    }
                    //填充教师
                    if(CollectionUtils.isNotEmpty(courseSchedule.getTeacherIds())){
                        String tname=makeTeacherName(courseSchedule.getTeacherIds(),teacherNameMap);
                        courseSchedule.setTeacherName(tname);
                    }
                }
            }
            placeCourseMap.put(teachPlace.getId(), timetableCourseScheduleList);
        }

        //获取上下午课时数
        Grade grades = gradeRemoteService.findTimetableMaxRangeBySchoolId(null, gradeIds.toArray(new String[0]));
        Integer morn = grades.getMornPeriods();
        Integer am = grades.getAmLessonCount();
        Integer pm = grades.getPmLessonCount();
        Integer night = grades.getNightLessonCount();

        //excel初始化
        HSSFWorkbook workbook = null;
        HSSFCellStyle style = null;
        String titleName = null;
        HSSFSheet sheet = null;
        HSSFRow titleRow = null;
        HSSFCell titleCell = null;
        List<CourseSchedule> placeCourseScheduleList = null;
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(teachBuilding.getBuildingName());
        sheet.setDefaultColumnWidth(15);
        style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        //设置打印方向为横向
        PrintSetup ps = sheet.getPrintSetup();
        ps.setLandscape(true);
        ps.setFitWidth((short)1);
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        ps.setScale((short)88);

        //excel表相关单元格格式及内容
        int index = 0;
        List<String> titleList = getTitleList(grades.getWeekDays());
        CellRangeAddress craTitle = null;
        CellRangeAddress craMorn = null;
        CellRangeAddress craAm = null;
        CellRangeAddress craPm = null;
        CellRangeAddress craNight = null;

        //开始按场地ID进行遍历，所有场地课表均在同一个sheet
        for (TeachPlace teachPlace : teachPlaceList) {
            placeCourseScheduleList = placeCourseMap.get(teachPlace.getId());
            craTitle = new CellRangeAddress(index+0,index+0,0,titleList.size()-1);
            if(morn != 0){
            	craMorn = new CellRangeAddress(index+2,index+2+morn-1,0,0);
            }
            if(am != 0){
                craAm = new CellRangeAddress(index+2+morn,index+2+morn+am-1,0,0);
            }
            if(pm != 0){
                craPm = new CellRangeAddress(index+morn+am+2,index+2+morn+am+pm-1,0,0);
            }
            if(night != 0){
                craNight = new CellRangeAddress(index+morn+am+pm+2,index+2+morn+am+pm+night-1,0,0);
            }
            //标题
            titleName = searchAcadyear + "年第" + searchSemester+"学期第"+week+"周" + teachPlaceIdToNameMap.get(teachPlace.getId()) +"的课程表";
            sheet.addMergedRegion(craTitle);
            titleRow = sheet.createRow(index+0);
            titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
            titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(style);
            for (int i=0; i<titleList.size(); i++) {
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellStyle(style);
            }
            titleCell.setCellValue(new HSSFRichTextString(titleName));
            //周一到周日
            HSSFRow titleListRow = sheet.createRow(index+1);
            for (int i=0; i<titleList.size(); i++) {
                HSSFCell cell = titleListRow.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
            }
            //上下午晚上
            if(craMorn!=null){
                sheet.addMergedRegion(craMorn);
            }
            if(craAm!=null){
                sheet.addMergedRegion(craAm);
            }
            if(craPm!=null){
                sheet.addMergedRegion(craPm);
            }
            if(craNight!=null){
                sheet.addMergedRegion(craNight);
            }
            for (int i = index+2; i < index+morn+am+pm+night+2; i++) {
                HSSFRow inRow = sheet.createRow(i);
                inRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
                for (int j = 0; j < titleList.size(); j++) {
                    HSSFCell inCell = inRow.createCell(j);
                    inCell.setCellStyle(style);
                    if(j==0){
                        if(craMorn!=null && i==craMorn.getFirstRow()){
                            inCell.setCellValue(new HSSFRichTextString("早自习"));
                        }
                        if(craAm!=null && i==craAm.getFirstRow()){
                        	inCell.setCellValue(new HSSFRichTextString("上午"));
                        }
                        if(craPm!=null && i==craPm.getFirstRow()){
                            inCell.setCellValue(new HSSFRichTextString("下午"));
                        }
                        if(craNight!=null && i==craNight.getFirstRow()){
                            inCell.setCellValue(new HSSFRichTextString("晚上"));
                        }
                    }
                    if(j==1){
                        inCell.setCellValue((i-1)%(morn+am+pm+night+2+1));
                    }
                }
            }

            //课程填充
            for (CourseSchedule cs : placeCourseScheduleList) {
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
                HSSFRow inRow = sheet.getRow(index+acPeriod+1);
                HSSFCell inCell = inRow.getCell(cs.getDayOfWeek()+2);
                //可能遗留周末数据 一周5天 报空
                if(inCell==null) {
                	continue;
                }
//                StringBuffer content = new StringBuffer();
//                if (StringUtils.isNotBlank(inCell.getStringCellValue())) {
//                    content.append(inCell.getStringCellValue()+"\n");
//                }
//                
//                content.append(cs.getClassName());
//                if (StringUtils.isNotBlank(cs.getSubjectName())) {
//                    content.append("\n" + cs.getSubjectName());
//                }
//                if (StringUtils.isNotBlank(cs.getTeacherName())) {
//                    content.append("\n" + cs.getTeacherName());
//                }
                String cellValue = inCell.getStringCellValue();
				if(StringUtils.isNotBlank(cellValue)){
					if(StringUtils.isNotBlank(cs.getClassName())) {
						cellValue=cellValue+"\n"+cs.getClassName();
					}
					if(StringUtils.isNotBlank(cs.getSubjectName())) {
						cellValue=cellValue+"\n"+cs.getSubjectName();
					}
					if(StringUtils.isNotBlank(cs.getTeacherName())) {
						cellValue=cellValue+"\n"+(showTeacher ? cs.getTeacherName() : "");
					}
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
					if(StringUtils.isNotBlank(cs.getTeacherName())) {
						if(StringUtils.isNotBlank(cellValue)) {
							cellValue=cellValue+"\n"+cs.getTeacherName();
						}else {
							cellValue=(showTeacher ? cs.getTeacherName() : "");
						}
					}
//					cellValue = cs.getClassName()+"\n"+cs.getSubjectName()+"\n"+cs.getPlaceName();
				}
                inCell.setCellValue(new HSSFRichTextString(cellValue));
            }

            //插入分页
            index += (morn+ am + pm + night + 2 + 1);
            sheet.setRowBreak(index - 1);
        }
        File file = new File(teachBuilding.getBuildingName() + ".xls");

        //开始写入并导出
        try {
            OutputStream stream = new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
            ExportUtils.outputFile(file.getPath(), resp);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnSuccess();
    }

	private String makeTeacherName(Set<String> teacherIds,
			Map<String, String> teacherNameMap) {
		String tname="";
		for(String s:teacherIds){
			if(teacherNameMap.containsKey(s)){
				tname=tname+"、"+teacherNameMap.get(s);
			}
		}
		if(StringUtils.isNotBlank(tname)){
			tname=tname.substring(1);
		}
		return tname;
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
