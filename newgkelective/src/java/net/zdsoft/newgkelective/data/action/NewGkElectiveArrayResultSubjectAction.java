package net.zdsoft.newgkelective.data.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ZipUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ArrayResultSubjectDto;
import net.zdsoft.newgkelective.data.dto.ArraySearchDto;
import net.zdsoft.newgkelective.data.dto.ClassTimetableDto;
import net.zdsoft.newgkelective.data.dto.CourseCategoryDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;
import net.zdsoft.system.entity.mcode.McodeDetail;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/newgkelective/{arrayId}")
public class NewGkElectiveArrayResultSubjectAction extends NewGkElectiveDivideCommonAction {

	
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private NewGkChoCategoryService newGkChoCategoryService;

	@RequestMapping("/arrayResult/subjectClassResult/tabHead/page")
	@ControllerInfo(value = "头部")
	public String showArrayHead(@PathVariable String arrayId, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassType(unitId, arrayId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4});
		if (CollectionUtils.isNotEmpty(timetableList)) {
			//科目列表
			Set<String> subjectIds = EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId);
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {
			});
//			Map<String, String> courseNameMap = EntityUtils.getMap(courseList, "id", "subjectName");
			//教室列表
			newGkTimetableService.makeTime(unitId, arrayId, timetableList);
			Set<String> placeIdSet = timetableList.stream().filter(e->e.getTimeList()!=null)
					.flatMap(e->e.getTimeList().stream()).filter(e->e.getPlaceId()!=null)
					.map(e->e.getPlaceId()).collect(Collectors.toSet());
			//防止顺序不一致 用list
			Map<String,String> placeNameMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMapByAttr(placeIdSet.toArray(new String[0]),"placeName"), new TR<Map<String,String>>() {});
			map.put("placeNameMap", placeNameMap);
			map.put("courseList", courseList);
//			Map<String, String> placeNameMap = getPlaceNameMap(placeIdSet);
//			map.put("courseNameMap", courseNameMap);
//			map.put("placeNameMap", placeNameMap);
		}
		//班级类型
//		Map<String, String> bestTypeMap = getBestTypeMap();
		//考试类型
		Map<String, String> subjectTypeMap = getSubjectTypeMap();

//		map.put("bestTypeMap", bestTypeMap);
		map.put("subjectTypeMap", subjectTypeMap);
		NewGkArray array = newGkArrayService.findOne(arrayId);
		map.put("isXzbArray", Objects.equals(array.getArrangeType(), NewGkElectiveConstant.ARRANGE_XZB));
		

		return "/newgkelective/arrayResult/subjectClassResultHead.ftl";
	}

	@RequestMapping("/arrayResult/subjectClassResult/list/page")
	@ControllerInfo("各科班级结果")
	public String showList(@PathVariable String arrayId, ArraySearchDto searchDto, ModelMap map, HttpServletRequest request) {

//		Map<String, String> bestTypeMap = getBestTypeMap();
		Map<String, String> subjectTypeMap = getSubjectTypeMap();

		List<ArrayResultSubjectDto> dtoList = newGkTimetableService.findByMoreConditions(this.getLoginInfo().getUnitId(), arrayId, searchDto);

		NewGkArray array = newGkArrayService.findOne(arrayId);
		map.put("isXzbArray", Objects.equals(array.getArrangeType(), NewGkElectiveConstant.ARRANGE_XZB));
		map.put("arrayId", arrayId);
		map.put("dtoList", dtoList);
		map.put("subjectTypeMap", subjectTypeMap);
//		map.put("bestTypeMap", bestTypeMap);

		return "/newgkelective/arrayResult/subjectClassResultList.ftl";
	}

	@RequestMapping("/arrayResult/subjectStudentResultLeft")
	@ControllerInfo("左边班级查询")
	public String showSubjectStudentResultLeft(@PathVariable String arrayId, String classId, String openType,ModelMap map) {
		map.put("classId", classId);
		map.put("arrayId", arrayId);
		//openType 1:学生 2课表
		map.put("openType", openType);
		return "/newgkelective/arrayResult/subjectStudentLeft.ftl";
	}
	
	
	@RequestMapping("/arrayResult/subjectStudentResult")
	@ControllerInfo("各科班级查看学生")
	public String showStudentList(@PathVariable String arrayId, String classId, ModelMap map) {

//		Map<String, String> bestTypeMap = getBestTypeMap();
		Map<String, String> subjectTypeMap = getSubjectTypeMap();
		Map<String, String> sexNameMap = getSexNameMap();
		NewGkArray newGkArray = newGkArrayService.findOne(arrayId);

		//学生
//		List<NewGkClassStudent> classStudentList = newGkClassStudentService.findListByIn("classId",new String[]{classId});
		
		List<NewGkClassStudent> classStudentList = newGkClassStudentService.findListBy(NewGkClassStudent.class,
				new String[] { "unitId","divideId" }, new String[] { newGkArray.getUnitId(), arrayId}, "classId",
				new String[] {classId}, new String[] {"studentId" });
		
		List<String> studentIdList = EntityUtils.getList(classStudentList, e->e.getStudentId());
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdList.toArray(new String[0])), new TR<List<Student>>() {});
		sortList(studentList);

		//新行政班
		String gradeId = newGkArray.getGradeId();
		String gradeName = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class).getGradeName();
		NewGkDivideClass divideClass = newGkDivideClassService.findOne(classId);
		List<NewGkDivideClass> newClassList = newGkDivideClassService.findByDivideIdAndClassType(newGkArray.getUnitId(), divideClass.getDivideId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_1 },false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		Map<String, String> newClassNameMap = EntityUtils.getMap(newClassList, "id", "className");
//		List<NewGkClassStudent> newClassStudentList = newGkClassStudentService.findListByIn("classId",
//				EntityUtils.getList(newClassList, e -> e.getId()).toArray(new String[0]));
		
		List<NewGkClassStudent> newClassStudentList = newGkClassStudentService.findListBy(NewGkClassStudent.class,
				new String[] { "unitId","divideId" }, new String[] { newGkArray.getUnitId(), arrayId}, "classId",
				EntityUtils.getList(newClassList, e -> e.getId()).toArray(new String[0]), new String[] {"classId","studentId" });
		
		Map<String, String> studentClassMap = new HashMap<String, String>();
		for (NewGkClassStudent newClassStudent : newClassStudentList) {
			studentClassMap.put(newClassStudent.getStudentId(), newClassNameMap.get(newClassStudent.getClassId()));
		}

		//教室
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassId(newGkArray.getUnitId(), arrayId, classId);
		List<String> timetableIdList = EntityUtils.getList(timetableList, e->e.getId());
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", timetableIdList.toArray(new String[0]));
		Set<String> placeIdSet = EntityUtils.getSet(timetableOtherList, e->e.getPlaceId());

		ArrayResultSubjectDto dto = new ArrayResultSubjectDto();
		dto.setClassId(divideClass.getId());
		dto.setClassName(divideClass.getClassName());
		dto.setSubjectType(subjectTypeMap.get(divideClass.getSubjectType()));
//		dto.setBestType(bestTypeMap.get(divideClass.getBestType()));
		String placeName = "";
		if (CollectionUtils.isNotEmpty(placeIdSet)) {
			Map<String, String> placeNameMap = getPlaceNameMap(placeIdSet);
			for (String placeId : placeIdSet) {
				placeName += placeNameMap.get(placeId) + "、";
			}
		}
		if (!placeName.equals("")) {
			placeName = placeName.substring(0, placeName.length() - 1);
		}
		dto.setPlaceNames(placeName);
		dto.setStudentNum(classStudentList.size());

		Set<String> classIdSet = EntityUtils.getSet(studentList, e->e.getClassId());
		Map<String, String> classNameMap = getClassNameMap(classIdSet);

		//下拉班级列表
		//List<NewGkTimetable> timetableAll = newGkTimetableService.findByArrayIdAndClassType(arrayId, NewGkElectiveConstant.CLASS_TYPE_2);
		//Set<String> classIdAll = EntityUtils.getSet(timetableAll, e->e.getClassId());
		//List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(classIdAll.toArray(new String[0]));
		//sortClassList(divideClassList);

		map.put("arrayId", arrayId);
		map.put("classId", classId);
		map.put("dto", dto);
		//map.put("divideClassList", divideClassList);
		map.put("studentList", studentList);
		map.put("studentClassMap", studentClassMap);
		map.put("classNameMap", classNameMap);
		map.put("sexNameMap", sexNameMap);
		map.put("gradeName", gradeName);
		map.put("arrangeType", newGkArray.getArrangeType());
		return "/newgkelective/arrayResult/subjectStudentResult.ftl";
	}

	@RequestMapping("/arrayResult/subjectTimetableResult/index/page")
	@ControllerInfo("教学班查看课表UI")
	public String showTimetableListUI(@PathVariable String arrayId, String classId, String classType, String searchClsName, ModelMap map) {
		//为了生成课表，需要年级的对象
		NewGkArray array = newGkArrayService.findById(arrayId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()), Grade.class);
		String gradeName = "";
		if (classType.equals(NewGkElectiveConstant.CLASS_TYPE_1)) {
			map.put("gradeName", grade.getGradeName());
			gradeName = grade.getGradeName();
		}
		boolean isXzb=false;
		if(NewGkElectiveConstant.ARRANGE_XZB.equals(array.getArrangeType())) {
			isXzb=true;
		}
		//下拉班级列表
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassType(array.getUnitId(), arrayId,
				new String[] {classType});
		Set<String> classIdAll = EntityUtils.getSet(timetableList, e->e.getClassId());
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(classIdAll.toArray(new String[0]));
		boolean hasName = StringUtils.isNotEmpty(searchClsName);
		if(CollectionUtils.isNotEmpty(divideClassList)) {
			Iterator<NewGkDivideClass> cit = divideClassList.iterator();
			while(cit.hasNext()) {
				NewGkDivideClass dc = cit.next();
				if(!isXzb) {
					dc.setClassName(gradeName+dc.getClassName());
				}
				
				if(hasName && dc.getClassName().indexOf(searchClsName) == -1) {
					cit.remove();
				}
			}
		}
		sortClassList(divideClassList);
		map.put("divideClassList", divideClassList);
		map.put("arrayId", arrayId);
		map.put("searchClsName", searchClsName);
		map.put("classId", classId);
		map.put("classType", classType);
		return "/newgkelective/arrayResult/subjectTimetableResult.ftl";
	}
	

	private Map<String, Integer> getIntervalMap(Grade grade) {
		Integer mmCount = grade.getMornPeriods();
		Integer amCount = grade.getAmLessonCount();
		Integer pmCount = grade.getPmLessonCount();
		Integer nightCount = grade.getNightLessonCount();
	
		Map<String,Integer> piMap = new LinkedHashMap<>();
		piMap.put(BaseConstants.PERIOD_INTERVAL_1, mmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_2, amCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_3, pmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_4, nightCount);
		return piMap;
	}


	@RequestMapping("/saveClassAndSchedule/pageIndex")
	@ControllerInfo(value = "应用排课方案首页")
	public String saveClassAndScheduleIndex(@PathVariable String arrayId, ModelMap map) {
		NewGkArray array = newGkArrayService.findById(arrayId);
		if (array == null) {
			return errorFtl(map, "当前排课方案不存在");
		}
		map.put("arrangeType", array.getArrangeType());
		NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
		map.put("divideType", divide.getOpenType());
		map.put("gradeId", array.getGradeId());
		List<Semester> semesterList = SUtils.dt(semesterRemoteService.findListByDate(new Date()), new TR<List<Semester>>() {});
		if (CollectionUtils.isEmpty(semesterList)) {
			return errorFtl(map, "学年学期不存在");
		}
		List<String> acadyearList = new ArrayList<String>();
		for (Semester s : semesterList) {
			if (!acadyearList.contains(s.getAcadyear())) {
				acadyearList.add(s.getAcadyear());
			}
		}
		Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, array.getUnitId()),Semester.class);
		//Semester semesterObj = semesterList.get(semesterList.size()-1);
		String mapAcadyear = "";
		String mapSemester = "";
		String mapStartDate = "";
		String mapEndDate = "";
		String workBegin = "";
		if(semesterObj!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			Date aheadDate = new Date();
			aheadDate.setDate(aheadDate.getDate()-7);
			if(DateUtils.compareForDay(now,semesterObj.getSemesterEnd())>0){
				String inAcadyear = semesterObj.getAcadyear();
				int inSemester = semesterObj.getSemester();
				if(inSemester==2){
					if(acadyearList.contains(inAcadyear)){
						acadyearList.remove(inAcadyear);
					}
					inAcadyear=Integer.parseInt(inAcadyear.split("-")[0])+1+"-"+(Integer.parseInt(inAcadyear.split("-")[1])+1);
					inSemester=1;
				}else{
					inSemester=2;
				}
				semesterObj = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(inAcadyear, inSemester, array.getUnitId()),Semester.class);
				if(semesterObj!=null){
					mapAcadyear = inAcadyear;
					mapSemester = inSemester+"";
					mapStartDate = sdf.format(semesterObj.getSemesterBegin());
					mapEndDate = sdf.format(semesterObj.getSemesterEnd());
					workBegin = mapStartDate;
				}
			}else{
				if(DateUtils.compareForDay(semesterObj.getSemesterBegin(), now) > 0){
					mapStartDate = sdf.format(semesterObj.getSemesterBegin());
					workBegin = mapStartDate;
				}else{
					// 今天处于 某一学年 学期中间
					mapStartDate = sdf.format(aheadDate);
					workBegin = sdf.format(now);
				}
				mapAcadyear = semesterObj.getAcadyear();
				mapSemester = semesterObj.getSemester()+"";
				mapEndDate = sdf.format(semesterObj.getSemesterEnd());
				if(!acadyearList.contains(mapAcadyear)){
					acadyearList.add(mapAcadyear);
				}
			}
		}

		map.put("workBegin", workBegin);
		map.put("acadyearList", acadyearList);
		map.put("acadyear", mapAcadyear);
		map.put("semester", mapSemester);
		map.put("startDate",mapStartDate);
		map.put("endDate", mapEndDate);
		map.put("arrayId", arrayId);
		return "/newgkelective/arrayResult/classAndScheduleIndex.ftl";
	}

	@ResponseBody
	@RequestMapping("/saveClassAndSchedule/findSemester")
	@ControllerInfo(value = "查询某个学年学期")
	public String findSemester(String acadyear, String semester) {
		Semester semesterObj = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(acadyear, Integer.valueOf(semester),getLoginInfo().getUnitId()), Semester.class);
		JSONObject json = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date aheadTime = new Date();
		aheadTime.setDate(aheadTime.getDate()-7);
		if (semesterObj == null) {
			Date newDate = new Date();
			json.put("startDate", "");
			json.put("endDate", "");
			// 选中时间
			json.put("startDate1", sdf.format(newDate));
			json.put("endDate1", sdf.format(newDate));
		} else {
			String startDate = "";
			try {
				Date workBegin = sdf.parse(sdf.format(semesterObj.getSemesterBegin()));
				Date endDate = sdf.parse(sdf.format(semesterObj.getSemesterEnd()));
				Date startBegin = sdf.parse(sdf.format(new Date()));
				if (DateUtils.compareForDay(workBegin, startBegin) > 0) {
					//workBegin在startBegin后面 最正常情况
					startDate = sdf.format(workBegin);
					json.put("startDate", startDate);
					json.put("endDate", sdf.format(endDate));
					json.put("startDate1", startDate);
				} else {
					if (DateUtils.compareForDay(startBegin, endDate) > 0) {
						//startBegin在endDate后面 这个情况相当于在当前时间做过去的事
						startDate = sdf.format(startBegin);
						json.put("startDate", startDate);
						json.put("endDate", "");
						json.put("startDate1", startDate);
					} else {
						//startBegin在workBegin，endDate之间
						startDate = sdf.format(startBegin);
						String aheadTimeStr = sdf.format(aheadTime);

						json.put("startDate", aheadTimeStr);
						json.put("endDate", sdf.format(endDate));
						json.put("startDate1", startDate);
					}

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return json.toString();
	}

	@ResponseBody
	@RequestMapping("/saveClassAndSchedule/save")
	@ControllerInfo(value = "复制行政班，教学班和课程表")
	public String saveClassAndSchedule(@PathVariable String arrayId, String acadyear, Integer semester, Date startTime, ModelMap map) {
		try {
			NewGkArray array = newGkArrayService.findById(arrayId);
			if (array == null) {
				return error("对应选课项目已经不存在，保存失败！");
			}
			Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(2, this.getLoginInfo().getUnitId()), Semester.class);
			DateInfo startDateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), semesterObj.getAcadyear(), semesterObj.getSemester(), startTime), DateInfo.class);
			if (startDateInfo == null) {
				return error("保存失败,未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
			}
			Date endTime = semesterObj.getSemesterEnd();
			DateInfo endDateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), semesterObj.getAcadyear(), semesterObj.getSemester(), endTime), DateInfo.class);
			if (endDateInfo == null) {
				return error("保存失败,未维护节假日信息！");
			}
			newGkArrayService.saveToSchedule(array,startDateInfo,endDateInfo);
			String realName = this.getLoginInfo().getRealName();
			// 应用课表成功时，再去通知第三方平台
			new Thread(() -> {
				try {
					doPushXingYun(array, realName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage()+"");
		}

		return success("保存成功！");
	}

	private void doPushXingYun(NewGkArray array, String realName) {
		Grade grade = gradeRemoteService.findOneObjectById(array.getGradeId());
		if(grade != null){
			String pushHost = Evn.getString("xingyun_push_class_url");
			String remark = array.getRemark();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(StringUtils.isBlank(pushHost)){
				log.error("推送地址 未配置");
				remark = remark+" #操作人："+realName+" 具体应用时间："
						+sdf.format(new Date())+" 推送失败：推送地址 未配置";
			}else{
				ResultDto resultDto = newGkArrayService.pushThirdParty(grade,pushHost);
				if(resultDto.getMsg().length()>100){
					resultDto.setMsg(resultDto.getMsg().substring(0,100));
				}
				remark = array.getRemark()+" #操作人："+realName+" 具体应用时间："
						+sdf.format(new Date())+" "+resultDto.getMsg();
				if(!resultDto.isSuccess()){
					log.error("推送失败："+resultDto.getMsg());
				}
			}
			final int limit_len = 2000;
			if(remark.getBytes().length>limit_len){
				int subIndex = (remark.getBytes().length-limit_len)*3;
				if(subIndex > remark.length()) {
					subIndex = remark.length();
				}
				remark = remark.substring(subIndex);
			}
			array.setRemark(remark);
			array.setModifyTime(new Date());
			newGkArrayService.save(array);
		}
	}

	@RequestMapping("/arrayResult/changeClass/page")
	@ControllerInfo(value = "学生调班page")
	public String showChangeClass(@PathVariable String arrayId,String type,ModelMap map) {
		map.put("arrayId", arrayId);
		map.put("type", type);
		List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
		if("1".equals(type)){
			divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), arrayId, new String[] { NewGkElectiveConstant.CLASS_TYPE_2 },true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		}else{
			divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), arrayId, new String[] { NewGkElectiveConstant.CLASS_TYPE_2 },true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		}
		divideClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&StringUtils.isNotBlank(e.getBatch()))
				.collect(Collectors.toList());
		map.put("divideClassList", divideClassList);
		return "/newgkelective/arrayResult/resultChangeClassIndex.ftl";
	}
	
	@RequestMapping("/arrayResult/changeStuClass/page")
	@ControllerInfo(value = "学生调班2page")
	public String changeStuClass(@PathVariable String arrayId,String type,ModelMap map) {
		if(!"1".equals(type)){
			return showChangeClass(arrayId, type, map);
		}
		map.put("divideId", arrayId);
		NewGkDivide divide = newGkDivideService.findOne(arrayId);
		//年级下所有班级
		List<Clazz> classList = SUtils.dt(classRemoteService.findByInGradeIds(new String[] {divide.getGradeId()}), new TR<List<Clazz>>() {});
		map.put("classList", classList);
		//选课条件
		makeChooseFactor(divide.getChoiceId(), divide.getUnitId(), map);
		return "/newgkelective/divide/divideChange/divideChangeIndex.ftl";
	}
	
	public void makeChooseFactor(String choiceId,String unitId,ModelMap map) {
		Map<String, List<NewGkChoRelation>> newGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(unitId,choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,NewGkElectiveConstant.CHOICE_TYPE_06});
		List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> realList = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId),Course.class);
		courseList = courseList.stream().filter(e->realList.contains(e.getId())).collect(Collectors.toList());
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId, e -> e);
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		map.put("courseNameMap", courseNameMap);
		map.put("courseList", courseList);
		
		//科目选择
		List<NewGkChoCategory> newGkChoCategoryList = newGkChoCategoryService.findByChoiceId(unitId, choiceId);

		List<NewGkChoRelation> combinationRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_06);
		Set<String> subIds = new HashSet<>();
		if (combinationRelationList != null) {
			newGkChoRelationList.addAll(combinationRelationList);
			subIds = combinationRelationList.stream().map(e -> e.getObjectValue()).collect(Collectors.toSet());
		}
		Map<String, List<NewGkChoRelation>> categoryIdToRelationMap = EntityUtils.getListMap(newGkChoRelationList, NewGkChoRelation::getObjectTypeVal, e -> e);
		Map<String, NewGkChoCategory> newGkChoCategoryMap = EntityUtils.getMap(newGkChoCategoryList, NewGkChoCategory::getId);
		List<CourseCategoryDto> courseCategoryDtoList = new ArrayList<>();
		CourseCategoryDto courseCategoryDto;
		NewGkChoCategory newGkChoCategory;
		for (Map.Entry<String, List<NewGkChoRelation>> entry : categoryIdToRelationMap.entrySet()) {
			if (!subIds.contains(entry.getKey())) {
				newGkChoCategory = newGkChoCategoryMap.get(entry.getKey());
				courseCategoryDto = new CourseCategoryDto();
				courseCategoryDto.setId(newGkChoCategory.getId());
				courseCategoryDto.setCategoryName(newGkChoCategory.getCategoryName());
				courseCategoryDto.setOrderId(newGkChoCategory.getOrderId());
				courseCategoryDto.setMaxNum(newGkChoCategory.getMaxNum());
				courseCategoryDto.setMinNum(newGkChoCategory.getMinNum());
				List<Course> coursesTmp = new ArrayList<>();
				List<CourseCategoryDto> combinationList = new ArrayList<>();
				for (NewGkChoRelation tmp : entry.getValue()) {
					if (NewGkElectiveConstant.CHOICE_TYPE_01.equals(tmp.getObjectType())) {
						coursesTmp.add(courseMap.get(tmp.getObjectValue()));
					} else {
						CourseCategoryDto sub = new CourseCategoryDto();
						sub.setId(tmp.getObjectValue());
						combinationList.add(sub);
					}
				}
				courseCategoryDto.setCourseList(coursesTmp);
				courseCategoryDto.setCourseCombination(combinationList);
				courseCategoryDtoList.add(courseCategoryDto);
			}
		}
		// 封装科目组合
		for (CourseCategoryDto one : courseCategoryDtoList) {
			for (CourseCategoryDto sub : one.getCourseCombination()) {
				List<Course> courses = new ArrayList<>();
				StringBuilder name = new StringBuilder();
				for (NewGkChoRelation course : categoryIdToRelationMap.get(sub.getId())) {
					courses.add(courseMap.get(course.getObjectValue()));
					name.append(courseNameMap.get(course.getObjectValue()) + ",");
				}
				sub.setCategoryName(name.substring(0, name.length() - 1));
				sub.setCourseList(courses);
			}
		}
		courseCategoryDtoList.sort(new Comparator<CourseCategoryDto>() {
			@Override
			public int compare(CourseCategoryDto o1, CourseCategoryDto o2) {
				int num1=o1.getOrderId()==null?0:o1.getOrderId();
				int num2=o2.getOrderId()==null?0:o2.getOrderId();
				return num1 - num2;
			}
		});
		map.put("categoryList", courseCategoryDtoList);
	}

	
	@ResponseBody
	@RequestMapping("/arrayResult/changeClass/findClass")
	public String showFindClass(@PathVariable String arrayId, String divideClassId, ModelMap map) {
		List<NewGkDivideClass> rightClassList = newGkDivideClassService.findByBatch(divideClassId);
		JSONArray jsonArr=new JSONArray();
		JSONObject jsonObj=null;
		for (NewGkDivideClass newGkDivideClass : rightClassList) {
			if(divideClassId.equals(newGkDivideClass.getId()))continue;
			jsonObj = new JSONObject();
			jsonObj.put("classId", newGkDivideClass.getId());
			jsonObj.put("className", newGkDivideClass.getClassName());
			jsonArr.add(jsonObj);
		}
		return jsonArr.toJSONString();
	}
	
	@ResponseBody
    @RequestMapping("/arrayResult/changeClass/save")
	@ControllerInfo("学生调班save")
    public String doSaveChangeClass(@PathVariable String arrayId,String leftClassSelect,String rightClassSelect,String leftAddStu,String rightAddStu) {
		try{
			newGkClassStudentService.saveChangeClass(leftClassSelect, rightClassSelect, leftAddStu, rightAddStu);
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	
	
	@RequestMapping("/arrayResult/changeClass/list")
	public String showChangeClassList(@PathVariable String arrayId,String type, String divideClassId,String right, ModelMap map) {
		map.put("right", right);
		NewGkDivide divide = null;
		if("1".equals(type)){
			divide = newGkDivideService.findById(arrayId);
		}else{
			NewGkArray array = newGkArrayService.findById(arrayId);
			divide = newGkDivideService.findById(array.getDivideId());
		}
		NewGkDivideClass divideClass = newGkDivideClassService.findById(divide.getUnitId(), divideClassId, true);
		return makeStuResultDto(divideClass.getSubjectIds(), map, divide, divideClass.getStudentList().toArray(new String[] {}));
	}

	private String makeStuResultDto(String subjectId, ModelMap map, NewGkDivide divide, String[] studentIds) {
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds),Student.class);
		
		int manCount = (int)studentList.stream().filter(e->Objects.equals(e.getSex(), 1)).count();
		int woManCount = (int)studentList.stream().filter(e->Objects.equals(e.getSex(), 2)).count();
		
		// 成绩
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide, studentIds);
		
		
		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
		List<StudentResultDto> dtoList = new ArrayList<>();
		Map<String, String> clazzMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(studentList)) {
			Set<String> clazzIds = EntityUtils.getSet(studentList, e->e.getClassId());
			clazzMap = getClassNameMap(clazzIds);
		}
		
		//获取学生选课数据
		List<NewGkChoResult> choResultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), studentIds);
		Map<String, List<String>> stuSubjectMap = new HashMap<>();
		Set<String> subjectIds=new HashSet<>();
		for (NewGkChoResult choose : choResultList) {
			if(!stuSubjectMap.containsKey(choose.getStudentId())) {
				stuSubjectMap.put(choose.getStudentId(), new ArrayList<>());
			}
			stuSubjectMap.get(choose.getStudentId()).add(choose.getSubjectId());
			subjectIds.add(choose.getSubjectId());
		}
		//获得科目集合
		List<Course> courseList=new ArrayList<>(); ;
		if(CollectionUtils.isNotEmpty(subjectIds)) {
			courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>() {});
		}
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId, e->e);
		StudentResultDto dto;
		float courseAvg = (float)0.0;
		float ysyAvg = (float)0.0;
		float totalAvg = (float)0.0;
		
		Map<String,String> classMap=new LinkedHashMap<>(); 
		Map<String,Integer> classNumMap=new HashMap<>();
		Map<String,String> chooseSubjectMap=new LinkedHashMap<>();
		Map<String,Integer> subNumMap=new HashMap<>();
		int noArrangeStuNums=0;
		
		for (Student stu : studentList) {
			dto = new StudentResultDto();
			dto.setStudentId(stu.getId());
			dto.setStudentName(stu.getStudentName());
			if(stu.getSex() != null) {
				dto.setSex(codeMap.get(stu.getSex()+"").getMcodeContent());
			}else {
				dto.setSex("未维护");
			}
			if(clazzMap.containsKey(stu.getClassId())) {
				dto.setClassName(clazzMap.get(stu.getClassId()));
			}
			dto.setClassId(stu.getClassId());
			if(stuSubjectMap.containsKey(stu.getId())) {
				stuSubjectMap.get(stu.getId());
				dto.setChooseSubjects(keyListSort(stuSubjectMap.get(stu.getId())));
				dto.setChoResultStr(nameSet(courseMap,dto.getChooseSubjects()));
			}
			
			Map<String, Float> scoreMap = stuScoreMap.get(stu.getId());
			Float score1;
			float ysyScore = (float)0.0;
			float allScore = (float)0.0;
			if(scoreMap==null){
				score1 = (float)0.0;
			}else{
				score1 = scoreMap.get(subjectId);
				if(score1 == null) {
					score1 = (float)0.0;
				}
				for(Course cc:ysyCourses){
					if(scoreMap.containsKey(cc.getId())){
						Float ss = scoreMap.get(cc.getId());
						if(ss==null){
							ss = (float)0.0;
						}
						ysyScore=ysyScore+ss;
					}
				}
				
				for(Entry<String, Float> item:scoreMap.entrySet()){
					Float ss = item.getValue();
					if(ss==null){
						ss = (float)0.0;
					}
					allScore=allScore+ss;
				}
			}
			
			courseAvg += score1;
			ysyAvg += ysyScore;
			totalAvg += allScore;
			
			dto.getSubjectScore().put(subjectId, score1);
			dto.getSubjectScore().put("YSY", ysyScore);
			dto.getSubjectScore().put("TOTAL", allScore);
			if(clazzMap.containsKey(stu.getClassId())) {
				if(!classNumMap.containsKey(stu.getClassId())) {
					classMap.put(stu.getClassId(), clazzMap.get(stu.getClassId()));
					classNumMap.put(stu.getClassId(), 1);
				}else {
					classNumMap.put(stu.getClassId(), classNumMap.get(stu.getClassId())+1);
				}
				
			}
			if(StringUtils.isNotBlank(dto.getChooseSubjects())) {
				if(!chooseSubjectMap.containsKey(dto.getChooseSubjects())) {
					chooseSubjectMap.put(dto.getChooseSubjects(), dto.getChoResultStr());
					subNumMap.put(dto.getChooseSubjects(), 1);
				}else {
					subNumMap.put(dto.getChooseSubjects(), subNumMap.get(dto.getChooseSubjects())+1);
				}
			}
			noArrangeStuNums++;
			dtoList.add(dto);
		}
		
		int size = dtoList.size();
		if(size==0) {
			size = 1;
		}
		
		map.put("courseName", course.getSubjectName());
		map.put("subjectId", subjectId);
		map.put("dtoList", dtoList);
		map.put("manCount", manCount);
		map.put("woManCount", woManCount);
		map.put("courseAvg", courseAvg/size);
		map.put("ysyAvg", ysyAvg/size);
		map.put("totalAvg", totalAvg/size);
		
		List<String[]> classFilterList=new ArrayList<>();
		classFilterList.add(new String[] {"","全部",String.valueOf(noArrangeStuNums)});
		if(classMap.size()>0) {
			for(Entry<String, String> ii:classMap.entrySet()) {
				classFilterList.add(new String[] {ii.getKey(),ii.getValue(),String.valueOf(classNumMap.get(ii.getKey()))});
			}
		}
		List<String[]> subjectFilterList=new ArrayList<>();
		subjectFilterList.add(new String[] {"","全部",String.valueOf(noArrangeStuNums)});
		if(chooseSubjectMap.size()>0) {
			for(Entry<String, String> ii:chooseSubjectMap.entrySet()) {
				subjectFilterList.add(new String[] {ii.getKey(),ii.getValue(),String.valueOf(subNumMap.get(ii.getKey()))});
			}
		}
		//排序
		if(CollectionUtils.isNotEmpty(classFilterList)) {
			Collections.sort(classFilterList, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					if(o1[0].equals("")) {
						return 1;
					}else if(o2[0].equals("")) {
						return 1;
					}
					return Integer.parseInt(o2[2])-Integer.parseInt(o1[2]);
				}
			});
		}
		if(CollectionUtils.isNotEmpty(subjectFilterList)) {
			Collections.sort(subjectFilterList, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					if(o1[0].equals("")) {
						return 1;
					}else if(o2[0].equals("")) {
						return 1;
					}
					return Integer.parseInt(o2[2])-Integer.parseInt(o1[2]);
				}
			});
		}
		map.put("classFilterList", classFilterList);
		map.put("subjectFilterList", subjectFilterList);
		
		return "/newgkelective/arrayResult/resultChangeClassList.ftl";
	}
	
	private Map<String, Map<String, Float>> getScoreMap(NewGkDivide divide, String[] studentIds) {
		List<NewGkScoreResult> scoreList = newGkScoreResultService.findByReferScoreIdAndStudentIdIn(divide.getUnitId(), divide.getReferScoreId(), studentIds);
		Map<String,Map<String, Float>> stuScoreMap = new HashMap<>();
		for (NewGkScoreResult sr : scoreList) {
			String studentId = sr.getStudentId();
			if(!stuScoreMap.containsKey(sr.getStudentId())) {
				stuScoreMap.put(sr.getStudentId(), new HashMap<>());
			}
			stuScoreMap.get(studentId).put(sr.getSubjectId(), sr.getScore());
		}
		return stuScoreMap;
	}

	/**
	 * 学生按学号排序
	 * @param studentList
	 */
	private void sortList(List<Student> studentList) {
		Collections.sort(studentList, new Comparator<Student>() {
			@Override
			public int compare(Student o1, Student o2) {
				//if(o1.getClassName()==null || o2.getClassName()==null){
				//	return 0;
				//}
				//if(o1.getClassName().equals(o2.getClassName())){
				return o1.getStudentCode().compareTo(o2.getStudentCode());
				//}
				//return o1.getClassName().compareTo(o2.getClassName());
			}
		});
	}

	/**
	 * 班级按名字排序
	 * @param divideClassList
	 */
	private void sortClassList(List<NewGkDivideClass> divideClassList) {
		Collections.sort(divideClassList, new Comparator<NewGkDivideClass>() {
			@Override
			public int compare(NewGkDivideClass x, NewGkDivideClass y) {
				if(x.getOrderId()==null){
					return 1;
				}else if(y.getOrderId()==null){
					return -1;
				}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
					return x.getOrderId().compareTo(y.getOrderId());
				}
				return x.getClassName().compareTo(y.getClassName());
			}
		});
	}

	private Map<String, String> getSexNameMap() {
		List<McodeDetail> sexList = SUtils.dt(mcodeRemoteService.findAllByMcodeIds("DM-XB"), new TR<List<McodeDetail>>() {});
		Map<String, String> sexNameMap = EntityUtils.getMap(sexList, "thisId", "mcodeContent");
		return sexNameMap;
	}

	private Map<String, String> getClassNameMap(Set<String> classIdSet) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
		Map<String, String> classNameMap = EntityUtils.getMap(classList, "id", "classNameDynamic");
		return classNameMap;
	}

	private Map<String, String> getSubjectTypeMap() {
		Map<String, String> subjectTypeMap = new HashMap<String, String>();
		subjectTypeMap.put(NewGkElectiveConstant.SUBJECT_TYPE_A, "选考");
		subjectTypeMap.put(NewGkElectiveConstant.SUBJECT_TYPE_B, "学考");
		return subjectTypeMap;
	}

/*	private Map<String, String> getBestTypeMap() {
		Map<String, String> bestTypeMap = new HashMap<String, String>();
		bestTypeMap.put(NewGkElectiveConstant.BEST_TYPE_1, "尖子班");
		bestTypeMap.put(NewGkElectiveConstant.BEST_TYPE_2, "平行班");
		return bestTypeMap;
	}*/

	private Map<String, String> getPlaceNameMap(Set<String> placeIdSet) {
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIdSet.toArray(new String[0])), new TR<List<TeachPlace>>() {});
		Map<String, String> placeNameMap = EntityUtils.getMap(placeList, "id", "placeName");
		return placeNameMap;
	}
	
	@RequestMapping("/arrayResult/class/exportTimetableAll")
	@ResponseBody
	@ControllerInfo("新行政班或教学班课程表Excel")
    public String exportNewClass(@PathVariable String arrayId,String classType,HttpServletResponse resp){
		NewGkArray array = newGkArrayService.findById(arrayId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()), Grade.class);
		String[] searchClassTypes = new String[] {classType};
		if(NewGkElectiveConstant.CLASS_TYPE_2.equals(classType)) {
			searchClassTypes = new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4};
		}
		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), arrayId, 
				searchClassTypes,false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		newGkDivideClassList = newGkDivideClassList.stream().filter(e->!(NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
				&& StringUtils.isBlank(e.getBatch()))).collect(Collectors.toList());
		boolean isXzb=NewGkElectiveConstant.ARRANGE_XZB.equals(array.getArrangeType());
		Set<String> classIds = EntityUtils.getSet(newGkDivideClassList, NewGkDivideClass::getId);
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassIds(array.getUnitId(), arrayId, classIds.toArray(new String[0]));
		Map<String, String> timetableClassMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId,NewGkTimetable::getClassId);
		String[] timetableIds = EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]);

		//获得教室集合
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", timetableIds);
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(EntityUtils.getSet(timetableOtherList, NewGkTimetableOther::getPlaceId).toArray(new String[0])), new TR<List<TeachPlace>>() {});
		Map<String, String> placeNameMap = EntityUtils.getMap(placeList, TeachPlace::getId, TeachPlace::getPlaceName);
		//获得老师集合
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableIds);
		Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(EntityUtils.getSet(timetableTeacherList, NewGkTimetableTeacher::getTeacherId).toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		//获得科目集合
		Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId).toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		//课表-科目映射 和 课表-老师映射
		Map<String, String> timetableSubjectMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId, NewGkTimetable::getSubjectId);
		Map<String, String> timetableTeacherMap = EntityUtils.getMap(timetableTeacherList, NewGkTimetableTeacher::getTimetableId, NewGkTimetableTeacher::getTeacherId);
		//封装数据
		Map<String, List<ClassTimetableDto>> classTimetableMap = new HashMap<String, List<ClassTimetableDto>>();
		for (NewGkTimetableOther timetableOther : timetableOtherList) {
			ClassTimetableDto dto = new ClassTimetableDto();
			dto.setDayOfWeek(timetableOther.getDayOfWeek());
			dto.setPeriodInterval(timetableOther.getPeriodInterval());
			dto.setPeriod(timetableOther.getPeriod());
			dto.setFirstsdWeek(timetableOther.getFirstsdWeek());
			if (placeNameMap.containsKey(timetableOther.getPlaceId())) {
				dto.setPlaceName(placeNameMap.get(timetableOther.getPlaceId()));
			} else {
				dto.setPlaceName("");
			}
			dto.setSubjectName(courseNameMap.get(timetableSubjectMap.get(timetableOther.getTimetableId())));
			String tid = timetableTeacherMap.get(timetableOther.getTimetableId());
			if (teacherNameMap.containsKey(tid)) {
				dto.setTeacherName(teacherNameMap.get(tid));
			} else {
				dto.setTeacherName("");
			}
			dto.setTimeTableOtherId(timetableOther.getId());
			String key = timetableClassMap.get(timetableOther.getTimetableId());
			if(!classTimetableMap.containsKey(key)){
				classTimetableMap.put(key, new ArrayList<ClassTimetableDto>());
			}
			classTimetableMap.get(key).add(dto);
		}		
		
        //excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = new CellRangeAddress(0,0,0,titleList.size()-1);
        int lessonCount = 0;
        CellRangeAddress craMs = null;
        CellRangeAddress craAm = null;
        CellRangeAddress craPm = null;
        CellRangeAddress craNight = null;
		if(grade.getMornPeriods()!=0){
			craMs = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getMornPeriods()-1,0,0);
			lessonCount+=grade.getMornPeriods();
		}
        if(grade.getAmLessonCount()!=0){
        	craAm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getAmLessonCount()-1,0,0);
        	lessonCount+=grade.getAmLessonCount();
        }
        if(grade.getPmLessonCount()!=0){
        	craPm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getPmLessonCount()-1,0,0);
        	lessonCount+=grade.getPmLessonCount();
        }
        if(grade.getNightLessonCount()!=0){
        	craNight = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getNightLessonCount()-1,0,0);
        	lessonCount+=grade.getNightLessonCount();
        }
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
		style.setWrapText(true);//自动换行
		style.setAlignment(HorizontalAlignment.CENTER);//水平
		style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle = workbook.createCellStyle();
        titleStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleStyle.setBorderTop(BorderStyle.MEDIUM);
        titleStyle.setBorderRight(BorderStyle.MEDIUM);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        titleStyle.setWrapText(true);//自动换行
        HSSFFont font = workbook.createFont();
        font.setBold(true);// 加粗
        titleStyle.setFont(font);
		String titleName = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		//每班为一个sheet
		Set<String> pids = EntityUtils.getSet(newGkDivideClassList, NewGkDivideClass::getParentId);
		for(NewGkDivideClass divideClass : newGkDivideClassList){
			List<ClassTimetableDto> dtoList = classTimetableMap.get(divideClass.getId());
			if(pids.contains(divideClass.getId()))
				continue;
			
			//标题
			String realClassName = "";
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType) && !isXzb){
				realClassName = grade.getGradeName()+divideClass.getClassName();
			}else{
				realClassName = divideClass.getClassName();
			}
			titleName = realClassName+"课程表";
			sheet = workbook.createSheet(realClassName);
			sheet.setDefaultColumnWidth(15);
			sheet.addMergedRegion(craTitle);
            //设置打印方向为横向
            PrintSetup ps = sheet.getPrintSetup();
            ps.setLandscape(true);
            ps.setFitWidth((short)1);
            ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            ps.setScale((short)88);
			titleRow = sheet.createRow(0);
			titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
			titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(titleStyle);
            for (int i=0; i<titleList.size(); i++) {
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellStyle(titleStyle);
            }
            titleCell.setCellValue(new HSSFRichTextString(titleName));
			//周一到周日
			HSSFRow titleListRow = sheet.createRow(1);
			for (int i=0; i<titleList.size(); i++) {
				 HSSFCell cell = titleListRow.createCell(i);
				 cell.setCellStyle(style);
				 cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
			}
			//上下午晚上
			if(craMs!=null && grade.getMornPeriods()>1){
				sheet.addMergedRegion(craMs);
			}
			if(craAm!=null && grade.getAmLessonCount()>1){
				sheet.addMergedRegion(craAm);
			}
			if(craPm!=null && grade.getPmLessonCount()>1){
				sheet.addMergedRegion(craPm);
			}
			if(craNight!=null && grade.getNightLessonCount()>1){
				sheet.addMergedRegion(craNight);
			}
			for (int i = 2; i < lessonCount+2; i++) {
				HSSFRow inRow = sheet.createRow(i);
				inRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
				for (int j = 0; j < titleList.size(); j++) {
					HSSFCell inCell = inRow.createCell(j);
					inCell.setCellStyle(style);
					if(j==0){
						if(craMs!=null && i==craMs.getFirstRow()){
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
                        if(craMs!=null && i>=craMs.getFirstRow() && i<=craMs.getLastRow()){
                            inCell.setCellValue(i - 1);
                        }
                        if(craAm!=null && i>=craAm.getFirstRow() && i<=craAm.getLastRow()){
                            inCell.setCellValue(i - 1 - grade.getMornPeriods());
                        }
                        if(craPm!=null && i>=craPm.getFirstRow() && i<=craPm.getLastRow()){
                            inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount());
                        }
                        if(craNight!=null && i>=craNight.getFirstRow() && i<=craNight.getLastRow()){
                            inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount() - grade.getPmLessonCount());
                        }
					}
				}
			}
			//课程
			if(CollectionUtils.isNotEmpty(dtoList)){
				for (ClassTimetableDto dto : dtoList) {
					int acPeriod = dto.getPeriod();
					if(BaseConstants.PERIOD_INTERVAL_2.equals(dto.getPeriodInterval())){
						acPeriod+=grade.getMornPeriods();
					}
					if(BaseConstants.PERIOD_INTERVAL_3.equals(dto.getPeriodInterval())){
						acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount();
					}
					if(BaseConstants.PERIOD_INTERVAL_4.equals(dto.getPeriodInterval())){
						acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount()+grade.getPmLessonCount();
					}
					HSSFRow inRow = sheet.getRow(acPeriod+1);
					if(inRow==null){
						continue;
					}
					HSSFCell inCell = inRow.getCell(dto.getDayOfWeek()+2);
					//防止报空异常
					if(inCell==null) {
						continue;
					}
					String cellValue = inCell.getRichStringCellValue().getString();
					String fn;
					if(NewGkElectiveConstant.FIRSTSD_WEEK_1==dto.getFirstsdWeek()){
						fn="(单)";
					}else if(NewGkElectiveConstant.FIRSTSD_WEEK_2==dto.getFirstsdWeek()){
						fn="(双)";
					}else{
						fn="";
					}
					if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){
						if(StringUtils.isNotBlank(cellValue)){
							cellValue += "\n"+dto.getSubjectName()+"\n"+dto.getTeacherName()+"\n"+dto.getPlaceName()+fn;
						}else{
							cellValue = dto.getSubjectName()+"\n"+dto.getTeacherName()+"\n"+dto.getPlaceName()+fn;
						}
					}else{
						if(StringUtils.isNotBlank(cellValue)){
							cellValue += "\n"+dto.getTeacherName()+"\n"+dto.getPlaceName()+fn;
						}else{
							cellValue = dto.getTeacherName()+"\n"+dto.getPlaceName()+fn;
						}
						
					}
					inCell.setCellValue(new HSSFRichTextString(cellValue));
				}
			}
		}
		String beforeStr=toTrim(array.getArrayName());
		if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){
			if(isXzb) {
				ExportUtils.outputData(workbook, beforeStr+"行政班课表", resp);
			}else {
				ExportUtils.outputData(workbook, beforeStr+"新行政班课表", resp);
			}
		}else{
			ExportUtils.outputData(workbook, beforeStr+"各科班级课表", resp);
		}
		return returnSuccess();
    }
	
	@RequestMapping("/arrayResult/classStu/exportTimetableAll")
	@ResponseBody
	@ControllerInfo("年级所有学生课程表Excel打包Zip")
    public String exportClassStu(@PathVariable String arrayId,HttpServletResponse resp){
		NewGkArray array = newGkArrayService.findById(arrayId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()), Grade.class);
		
		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), 
				arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,
						NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4},true,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		List<NewGkDivideClass> xzbList = newGkDivideClassList.parallelStream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
		Set<String> studentIds = new HashSet<>();
		for (NewGkDivideClass d : xzbList) {
			if(CollectionUtils.isNotEmpty(d.getStudentList())){
				studentIds.addAll(d.getStudentList());
			}
		}
		
		List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(array.getUnitId(), null, null, studentIds.toArray(new String[0])), Student.class);
		Map<String, Student> studentMap = EntityUtils.getMap(studentList, Student::getId,Function.identity());
		Map<String,List<Student>> clazzStudentMap = new HashMap<>();
		for (NewGkDivideClass d : xzbList) {
			if(CollectionUtils.isNotEmpty(d.getStudentList())){
				clazzStudentMap.put(d.getId(), new ArrayList<>());
				for (String sid : d.getStudentList()) {
					if(studentMap.containsKey(sid)){
						clazzStudentMap.get(d.getId()).add(studentMap.get(sid));
					}
				}
			}
		}
		
		List<NewGkClassStudent> divideClassStudentList = newGkClassStudentService.findListByStudentIds(array.getUnitId(), array.getId(), studentIds.toArray(new String[0]));
		Map<String, List<String>> studentClassMap = EntityUtils.getListMap(divideClassStudentList, NewGkClassStudent::getStudentId, NewGkClassStudent::getClassId);
		
		Map<String, String> classNameMap = EntityUtils.getMap(newGkDivideClassList, NewGkDivideClass::getId,NewGkDivideClass::getClassName);
		
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(array.getUnitId(), arrayId);
		String[] timetableIds = EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]);

		//获得教室集合
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", timetableIds);
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(EntityUtils.getSet(timetableOtherList, NewGkTimetableOther::getPlaceId).toArray(new String[0])), new TR<List<TeachPlace>>() {});
		Map<String, String> placeNameMap = EntityUtils.getMap(placeList, TeachPlace::getId, TeachPlace::getPlaceName);
		//获得老师集合
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableIds);
		Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(EntityUtils.getSet(timetableTeacherList, NewGkTimetableTeacher::getTeacherId).toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		//获得科目集合
		Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId).toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		//课表-科目映射 和 课表-老师映射
		Map<String, NewGkTimetable> timetableMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId,Function.identity());
		Map<String, String> timetableTeacherMap = EntityUtils.getMap(timetableTeacherList, NewGkTimetableTeacher::getTimetableId, NewGkTimetableTeacher::getTeacherId);
		//封装数据
		Map<String, List<NewGkTimetableOther>> classTimetableMap = new HashMap<String, List<NewGkTimetableOther>>();
		for (NewGkTimetableOther timetableOther : timetableOtherList) {
			if (placeNameMap.containsKey(timetableOther.getPlaceId())) {
				timetableOther.setPlaceName(placeNameMap.get(timetableOther.getPlaceId()));
			} else {
				timetableOther.setPlaceName("");
			}
			NewGkTimetable timetable = timetableMap.get(timetableOther.getTimetableId());
			timetableOther.setSubjectName(courseNameMap.get(timetable.getSubjectId()));
			String tid = timetableTeacherMap.get(timetableOther.getTimetableId());
			if (teacherNameMap.containsKey(tid)) {
				timetableOther.setTeacherName(teacherNameMap.get(tid));
			} else {
				timetableOther.setTeacherName("");
			}
			timetableOther.setClassName(classNameMap.get(timetable.getClassId()));
			if(!classTimetableMap.containsKey(timetable.getClassId())){
				classTimetableMap.put(timetable.getClassId(), new ArrayList<NewGkTimetableOther>());
			}
			classTimetableMap.get(timetable.getClassId()).add(timetableOther);
		}		
		
        //创建文件夹
        String dirName = toTrim(array.getArrayName())+"学生课程表";
        File dirFile = new File(dirName);
        if(dirFile.exists()){
        	dirFile.delete();
        }
        dirFile.mkdirs();
        
        //excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = new CellRangeAddress(0,0,0,titleList.size()-1);
        int lessonCount = 0;
        CellRangeAddress craMs = null;
        CellRangeAddress craAm = null;
        CellRangeAddress craPm = null;
        CellRangeAddress craNight = null;
		if(grade.getMornPeriods()!=0){
			craMs = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getMornPeriods()-1,0,0);
			lessonCount+=grade.getMornPeriods();
		}
        if(grade.getAmLessonCount()!=0){
        	craAm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getAmLessonCount()-1,0,0);
        	lessonCount+=grade.getAmLessonCount();
        }
        if(grade.getPmLessonCount()!=0){
        	craPm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getPmLessonCount()-1,0,0);
        	lessonCount+=grade.getPmLessonCount();
        }
        if(grade.getNightLessonCount()!=0){
        	craNight = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getNightLessonCount()-1,0,0);
        	lessonCount+=grade.getNightLessonCount();
        }
        
        HSSFWorkbook workbook = null;
        HSSFCellStyle style = null;
        HSSFCellStyle titleStyle= null;
        HSSFFont font = null;
		String titleName = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		List<NewGkTimetableOther> stuTimetableList = null;
		//每班为一个excel
		for (NewGkDivideClass clazz : xzbList) {
			workbook = new HSSFWorkbook();
			style = workbook.createCellStyle();
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setBorderLeft(BorderStyle.MEDIUM);
			style.setBorderTop(BorderStyle.MEDIUM);
			style.setBorderRight(BorderStyle.MEDIUM);
			style.setAlignment(HorizontalAlignment.CENTER);//水平
			style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
			style.setWrapText(true);//自动换行

            titleStyle = workbook.createCellStyle();
            titleStyle.setBorderBottom(BorderStyle.MEDIUM);
            titleStyle.setBorderLeft(BorderStyle.MEDIUM);
            titleStyle.setBorderTop(BorderStyle.MEDIUM);
            titleStyle.setBorderRight(BorderStyle.MEDIUM);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            titleStyle.setWrapText(true);//自动换行
            font = workbook.createFont();
            font.setBold(true);// 加粗
            titleStyle.setFont(font);
			//每个学生为一个sheet
			List<Student> sList = clazzStudentMap.get(clazz.getId());
			if(CollectionUtils.isNotEmpty(sList)){
				sList.sort(new Comparator<Student>() {

					@Override
					public int compare(Student o1, Student o2) {
						if(o1.getStudentCode() == null){
							return 1;
						}else if(o2.getStudentCode() == null){
							return -1;
						}
						return o1.getStudentCode().compareTo(o2.getStudentCode());
					}
				});
				for(Student student : sList){
					//标题
					titleName = student.getStudentName() + "的课程表";
					sheet = workbook.createSheet(student.getStudentName()+"("+student.getStudentCode()+")");
					sheet.setDefaultColumnWidth(15);
					sheet.addMergedRegion(craTitle);
                    //设置打印方向为横向
                    PrintSetup ps = sheet.getPrintSetup();
                    ps.setLandscape(true);
                    ps.setFitWidth((short)1);
                    ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
                    ps.setScale((short)88);
					titleRow = sheet.createRow(0);
					titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
					titleCell = titleRow.createCell(0);
					titleCell.setCellStyle(titleStyle);
					for (int i=0; i<titleList.size(); i++) {
						HSSFCell cell = titleRow.createCell(i);
						cell.setCellStyle(titleStyle);
					}
					titleCell.setCellValue(new HSSFRichTextString(titleName));
					//周一到周日
					HSSFRow titleListRow = sheet.createRow(1);
					for (int i=0; i<titleList.size(); i++) {
						 HSSFCell cell = titleListRow.createCell(i);
						 cell.setCellStyle(style);
						 cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
					}
					//上下午晚上
					if(craMs!=null && grade.getMornPeriods()>1){
						sheet.addMergedRegion(craMs);
					}
					if(craAm!=null && grade.getAmLessonCount()>1){
						sheet.addMergedRegion(craAm);
					}
					if(craPm!=null && grade.getPmLessonCount()>1){
						sheet.addMergedRegion(craPm);
					}
					if(craNight!=null && grade.getNightLessonCount()>1){
						sheet.addMergedRegion(craNight);
					}
					for (int i = 2; i < lessonCount+2; i++) {
						HSSFRow inRow = sheet.createRow(i);
						inRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
						for (int j = 0; j < titleList.size(); j++) {
							HSSFCell inCell = inRow.createCell(j);
							inCell.setCellStyle(style);
							if(j==0){
								if(craMs!=null && i==craMs.getFirstRow()){
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
                                if(craMs!=null && i>=craMs.getFirstRow() && i<=craMs.getLastRow()){
                                    inCell.setCellValue(i - 1);
                                }
                                if(craAm!=null && i>=craAm.getFirstRow() && i<=craAm.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods());
                                }
                                if(craPm!=null && i>=craPm.getFirstRow() && i<=craPm.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount());
                                }
                                if(craNight!=null && i>=craNight.getFirstRow() && i<=craNight.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount() - grade.getPmLessonCount());
                                }
							}
						}
					}
					//课程
					List<String> stuClaIds = studentClassMap.get(student.getId());
					stuTimetableList = new ArrayList<NewGkTimetableOther>();
					for (String claId : stuClaIds) {
						if(classTimetableMap.containsKey(claId)){
							stuTimetableList.addAll(classTimetableMap.get(claId));
						}
					}
					for (NewGkTimetableOther o : stuTimetableList) {
						int acPeriod = o.getPeriod();
						if(BaseConstants.PERIOD_INTERVAL_2.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods();
						}
						if(BaseConstants.PERIOD_INTERVAL_3.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount();
						}
						if(BaseConstants.PERIOD_INTERVAL_4.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount()+grade.getPmLessonCount();
						}
						HSSFRow inRow = sheet.getRow(acPeriod+1);
						if(inRow==null){
							continue;
						}
						HSSFCell inCell = inRow.getCell(o.getDayOfWeek()+2);
						if(inCell==null) {
							continue;
						}
						String cellValue = inCell.getRichStringCellValue().getString();
						String fn;
						if(NewGkElectiveConstant.FIRSTSD_WEEK_1==o.getFirstsdWeek()){
							fn="(单)";
						}else if(NewGkElectiveConstant.FIRSTSD_WEEK_2==o.getFirstsdWeek()){
							fn="(双)";
						}else{
							fn="";
						}
						if(StringUtils.isNotBlank(cellValue)){
							cellValue += "\n"+o.getSubjectName()+"\n"+o.getTeacherName()+"\n"+o.getClassName()+"\n"+o.getPlaceName()+fn;
						}else{
							cellValue = o.getSubjectName()+"\n"+o.getTeacherName()+"\n"+o.getClassName()+"\n"+o.getPlaceName()+fn;
						}
						inCell.setCellValue(new HSSFRichTextString(cellValue));
					}
					
				}
			}
			//写入文件
			File file = new File(dirFile,clazz.getClassName()+".xls");
			try {
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
	
	@RequestMapping("/arrayResult/teacher/exportTimetableAll")
	@ResponseBody
	@ControllerInfo("所有老师课程表Excel打包Zip")
    public String exportTeacher(@PathVariable String arrayId,HttpServletResponse resp){
		NewGkArray array = newGkArrayService.findById(arrayId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()), Grade.class);
		
		
		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), arrayId, 
				new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4},false, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		Map<String, String> classNameMap = EntityUtils.getMap(newGkDivideClassList, NewGkDivideClass::getId,NewGkDivideClass::getClassName);
		
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(array.getUnitId(), arrayId);
		String[] timetableIds = EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]);

		//获得教室集合
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", timetableIds);
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(EntityUtils.getSet(timetableOtherList, NewGkTimetableOther::getPlaceId).toArray(new String[0])), new TR<List<TeachPlace>>() {});
		Map<String, String> placeNameMap = EntityUtils.getMap(placeList, TeachPlace::getId, TeachPlace::getPlaceName);
		//获得老师集合
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableIds);
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(EntityUtils.getSet(timetableTeacherList, NewGkTimetableTeacher::getTeacherId).toArray(new String[0])), new TR<List<Teacher>>() {});
		if(CollectionUtils.isEmpty(teacherList)){
			//创建文件夹
	        String dirName = toTrim(array.getArrayName())+"教师课程表";
	        File dirFile = new File(dirName);
	        if(dirFile.exists()){
	        	dirFile.delete();
	        }
	        dirFile.mkdirs();
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
		List<Dept> deptList = SUtils.dt(deptRemoteService.findListByIds(EntityUtils.getSet(teacherList, Teacher::getDeptId).toArray(new String[0])),Dept.class);
		Map<String, List<Teacher>> deptTeacherMap = EntityUtils.getListMap(teacherList, Teacher::getDeptId, Function.identity());
		
		//获得科目集合
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId).toArray(new String[0])), new TR<List<Course>>() {});
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);

		//课表-科目映射 和 课表-老师映射
		Map<String, NewGkTimetable> timetableMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId,Function.identity());
		Map<String, String> timetableTeacherMap = EntityUtils.getMap(timetableTeacherList, NewGkTimetableTeacher::getTimetableId, NewGkTimetableTeacher::getTeacherId);
		//封装数据
		Map<String, List<NewGkTimetableOther>> teacherTimetableMap = new HashMap<String, List<NewGkTimetableOther>>();
		for (NewGkTimetableOther timetableOther : timetableOtherList) {
			if (placeNameMap.containsKey(timetableOther.getPlaceId())) {
				timetableOther.setPlaceName(placeNameMap.get(timetableOther.getPlaceId()));
			} else {
				timetableOther.setPlaceName("");
			}
			NewGkTimetable timetable = timetableMap.get(timetableOther.getTimetableId());
			timetableOther.setSubjectName(courseNameMap.get(timetable.getSubjectId()));
			String tid = timetableTeacherMap.get(timetableOther.getTimetableId());
			if(StringUtils.isBlank(tid)){
				continue;
			}
			timetableOther.setClassName(classNameMap.get(timetable.getClassId()));
			if(!teacherTimetableMap.containsKey(tid)){
				teacherTimetableMap.put(tid, new ArrayList<NewGkTimetableOther>());
			}
			teacherTimetableMap.get(tid).add(timetableOther);
		}		
		
        //创建文件夹
        String dirName = toTrim(array.getArrayName())+"教师课程表";
        File dirFile = new File(dirName);
        if(dirFile.exists()){
        	dirFile.delete();
        }
        dirFile.mkdirs();
        
        //excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = new CellRangeAddress(0,0,0,titleList.size()-1);
        int lessonCount = 0;
        CellRangeAddress craMs = null;
        CellRangeAddress craAm = null;
        CellRangeAddress craPm = null;
        CellRangeAddress craNight = null;
        if(grade.getMornPeriods()!=0){
            craMs = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getMornPeriods()-1,0,0);
            lessonCount+=grade.getMornPeriods();
        }
        if(grade.getAmLessonCount()!=0){
        	craAm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getAmLessonCount()-1,0,0);
        	lessonCount+=grade.getAmLessonCount();
        }
        if(grade.getPmLessonCount()!=0){
        	craPm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getPmLessonCount()-1,0,0);
        	lessonCount+=grade.getPmLessonCount();
        }
        if(grade.getNightLessonCount()!=0){
        	craNight = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getNightLessonCount()-1,0,0);
        	lessonCount+=grade.getNightLessonCount();
        }
        
        HSSFWorkbook workbook = null;
        HSSFCellStyle style = null;
        HSSFCellStyle titleStyle = null;
        HSSFFont font = null;
		String titleName = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		List<NewGkTimetableOther> teacherTimetableList = null;
		//每个部门为一个excel
		for (Dept dept : deptList) {
			workbook = new HSSFWorkbook();
			style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
			style.setAlignment(HorizontalAlignment.CENTER);//水平
			style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
			style.setWrapText(true);//自动换行

            titleStyle = workbook.createCellStyle();
            titleStyle.setBorderBottom(BorderStyle.MEDIUM);
            titleStyle.setBorderLeft(BorderStyle.MEDIUM);
            titleStyle.setBorderTop(BorderStyle.MEDIUM);
            titleStyle.setBorderRight(BorderStyle.MEDIUM);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            titleStyle.setWrapText(true);//自动换行
            font = workbook.createFont();
            font.setBold(true);// 加粗
            titleStyle.setFont(font);
			//每个教师为一个sheet
			List<Teacher> tList = deptTeacherMap.get(dept.getId());
			if(CollectionUtils.isNotEmpty(tList)){
				tList.sort(new Comparator<Teacher>() {

					@Override
					public int compare(Teacher o1, Teacher o2) {
						return o1.getTeacherCode().compareTo(o2.getTeacherCode());
					}
				});
				for(Teacher t : tList){
					//标题
					titleName = t.getTeacherName() + "的课程表";
					sheet = workbook.createSheet(t.getTeacherName()+"("+t.getTeacherCode()+")");
					sheet.setDefaultColumnWidth(15);
					sheet.addMergedRegion(craTitle);
					titleRow = sheet.createRow(0);
					titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
					titleCell = titleRow.createCell(0);
                    titleCell.setCellStyle(titleStyle);
                    //设置打印方向为横向
                    PrintSetup ps = sheet.getPrintSetup();
                    ps.setLandscape(true);
                    ps.setFitWidth((short)1);
                    ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
                    ps.setScale((short)88);
                    for (int i=0; i<titleList.size(); i++) {
                        HSSFCell cell = titleRow.createCell(i);
                        cell.setCellStyle(titleStyle);
                    }
                    titleCell.setCellValue(new HSSFRichTextString(titleName));
					//周一到周日
					HSSFRow titleListRow = sheet.createRow(1);
					for (int i=0; i<titleList.size(); i++) {
						 HSSFCell cell = titleListRow.createCell(i);
                         cell.setCellStyle(style);
						 cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
					}
					//上下午晚上
					if(craMs!=null && grade.getMornPeriods()>1){
						sheet.addMergedRegion(craMs);
					}
					if(craAm!=null && grade.getAmLessonCount()>1){
						sheet.addMergedRegion(craAm);
					}
					if(craPm!=null && grade.getPmLessonCount()>1){
						sheet.addMergedRegion(craPm);
					}
					if(craNight!=null && grade.getNightLessonCount()>1){
						sheet.addMergedRegion(craNight);
					}
					for (int i = 2; i < lessonCount+2; i++) {
						HSSFRow inRow = sheet.createRow(i);
						inRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
						for (int j = 0; j < titleList.size(); j++) {
							HSSFCell inCell = inRow.createCell(j);
							inCell.setCellStyle(style);
							if(j==0){
                                if(craMs!=null && i==craMs.getFirstRow()){
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
                                if(craMs!=null && i>=craMs.getFirstRow() && i<=craMs.getLastRow()){
                                    inCell.setCellValue(i - 1);
                                }
                                if(craAm!=null && i>=craAm.getFirstRow() && i<=craAm.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods());
                                }
                                if(craPm!=null && i>=craPm.getFirstRow() && i<=craPm.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount());
                                }
                                if(craNight!=null && i>=craNight.getFirstRow() && i<=craNight.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount() - grade.getPmLessonCount());
                                }
							}
						}
					}
					//课程
					teacherTimetableList = teacherTimetableMap.get(t.getId());
					if(teacherTimetableList ==null){
						continue;
					}
					for (NewGkTimetableOther o : teacherTimetableList) {
						int acPeriod = o.getPeriod();
                        if(BaseConstants.PERIOD_INTERVAL_2.equals(o.getPeriodInterval())){
                            acPeriod+=grade.getMornPeriods();
                        }
						if(BaseConstants.PERIOD_INTERVAL_3.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount();
						}
						if(BaseConstants.PERIOD_INTERVAL_4.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount()+grade.getPmLessonCount();
						}
						HSSFRow inRow = sheet.getRow(acPeriod+1);
						if(inRow==null){
							continue;
						}
						HSSFCell inCell = inRow.getCell(o.getDayOfWeek()+2);
						if(inCell==null) {
							continue;
						}
						String cellValue = inCell.getRichStringCellValue().getString();
						String fn;
						if(NewGkElectiveConstant.FIRSTSD_WEEK_1==o.getFirstsdWeek()){
							fn="(单)";
						}else if(NewGkElectiveConstant.FIRSTSD_WEEK_2==o.getFirstsdWeek()){
							fn="(双)";
						}else{
							fn="";
						}
						if(StringUtils.isNotBlank(cellValue)){
							cellValue += "\n"+o.getSubjectName()+"\n"+o.getClassName()+"\n"+o.getPlaceName()+fn;
						}else{
							cellValue = o.getSubjectName()+"\n"+o.getClassName()+"\n"+o.getPlaceName()+fn;
						}
						inCell.setCellValue(new HSSFRichTextString(cellValue));
					}
					
				}
			}
			//写入文件
			File file = new File(dirFile,dept.getDeptName()+".xls");
			try {
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
	
	@RequestMapping("/arrayResult/place/exportTimetableAll")
	@ResponseBody
	@ControllerInfo("所有场地课程表Excel打包Zip")
    public String exportPlace(@PathVariable String arrayId,HttpServletResponse resp){
		NewGkArray array = newGkArrayService.findById(arrayId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()), Grade.class);
		
		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), 
				arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,
						NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4},false, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		Map<String, String> classNameMap = EntityUtils.getMap(newGkDivideClassList, NewGkDivideClass::getId,NewGkDivideClass::getClassName);
		
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(array.getUnitId(), arrayId);
		String[] timetableIds = EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]);

		//获得教室集合
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", timetableIds);
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(EntityUtils.getSet(timetableOtherList, NewGkTimetableOther::getPlaceId).toArray(new String[0])), new TR<List<TeachPlace>>() {});
		List<TeachBuilding> buildingList = SUtils.dt(teachBuildingRemoteService.findListByIds(EntityUtils.getSet(placeList, TeachPlace::getTeachBuildingId).toArray(new String[0])),TeachBuilding.class);
		Map<String,List<TeachPlace>> buildingPlaceMap = new HashMap<String, List<TeachPlace>>();
		for (TeachPlace teachPlace : placeList) {
			if(StringUtils.isNotBlank(teachPlace.getTeachBuildingId())){
				if(!buildingPlaceMap.containsKey(teachPlace.getTeachBuildingId())){
					buildingPlaceMap.put(teachPlace.getTeachBuildingId(), new ArrayList<TeachPlace>());
				}
				buildingPlaceMap.get(teachPlace.getTeachBuildingId()).add(teachPlace);
			}else{
				if(!buildingPlaceMap.containsKey("其他楼")){
					TeachBuilding tb = new TeachBuilding();
					tb.setId("其他楼");
					tb.setBuildingName("其他楼");
					buildingList.add(tb);
					buildingPlaceMap.put("其他楼", new ArrayList<TeachPlace>());
				}
				buildingPlaceMap.get("其他楼").add(teachPlace);
			}
		}
		
		//获得老师集合
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableIds);
		Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(EntityUtils.getSet(timetableTeacherList, NewGkTimetableTeacher::getTeacherId).toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		
		//获得科目集合
		Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId).toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		//课表-科目映射 和 课表-老师映射
		Map<String, NewGkTimetable> timetableMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId,Function.identity());
		Map<String, String> timetableTeacherMap = EntityUtils.getMap(timetableTeacherList, NewGkTimetableTeacher::getTimetableId, NewGkTimetableTeacher::getTeacherId);
		//封装数据
		for (NewGkTimetableOther timetableOther : timetableOtherList) {
			NewGkTimetable timetable = timetableMap.get(timetableOther.getTimetableId());
			timetableOther.setSubjectName(courseNameMap.get(timetable.getSubjectId()));
			String tid = timetableTeacherMap.get(timetableOther.getTimetableId());
			if (teacherNameMap.containsKey(tid)) {
				timetableOther.setTeacherName(teacherNameMap.get(tid));
			} else {
				timetableOther.setTeacherName("");
			}
			timetableOther.setClassName(classNameMap.get(timetable.getClassId()));
		}		
//		Map<String, List<NewGkTimetableOther>> placeTimetableMap = EntityUtils.getListMap(timetableOtherList, NewGkTimetableOther::getPlaceId, Function.identity());
		Map<String, List<NewGkTimetableOther>> placeTimetableMap = timetableOtherList.stream().filter(e->StringUtils.isNotBlank(e.getPlaceId())).collect(Collectors.groupingBy(NewGkTimetableOther::getPlaceId));
		
        //创建文件夹
        String dirName = toTrim(array.getArrayName())+"教室课程表";
        File dirFile = new File(dirName);
        if(dirFile.exists()){
        	dirFile.delete();
        }
        dirFile.mkdirs();
        
        //excel表相关通过格式及内容
        List<String> titleList = getTitleList(grade.getWeekDays());
        CellRangeAddress craTitle = new CellRangeAddress(0,0,0,titleList.size()-1);
        int lessonCount = 0;
        CellRangeAddress craMs = null;
        CellRangeAddress craAm = null;
        CellRangeAddress craPm = null;
        CellRangeAddress craNight = null;
		if(grade.getMornPeriods()!=0){
			craMs = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getMornPeriods()-1,0,0);
			lessonCount+=grade.getMornPeriods();
		}
        if(grade.getAmLessonCount()!=0){
        	craAm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getAmLessonCount()-1,0,0);
        	lessonCount+=grade.getAmLessonCount();
        }
        if(grade.getPmLessonCount()!=0){
        	craPm = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getPmLessonCount()-1,0,0);
        	lessonCount+=grade.getPmLessonCount();
        }
        if(grade.getNightLessonCount()!=0){
        	craNight = new CellRangeAddress(lessonCount+2,lessonCount+2+grade.getNightLessonCount()-1,0,0);
        	lessonCount+=grade.getNightLessonCount();
        }
        
        HSSFWorkbook workbook = null;
        HSSFCellStyle style = null;
        HSSFCellStyle titleStyle = null;
        HSSFFont font = null;
		String titleName = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		List<NewGkTimetableOther> placeTimetableList = null;
		//每栋楼为一个excel
		for (TeachBuilding building : buildingList) {
			workbook = new HSSFWorkbook();
			style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
			style.setAlignment(HorizontalAlignment.CENTER);//水平
			style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
			style.setWrapText(true);//自动换行

            titleStyle = workbook.createCellStyle();
            titleStyle.setBorderBottom(BorderStyle.MEDIUM);
            titleStyle.setBorderLeft(BorderStyle.MEDIUM);
            titleStyle.setBorderTop(BorderStyle.MEDIUM);
            titleStyle.setBorderRight(BorderStyle.MEDIUM);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            titleStyle.setWrapText(true);//自动换行
            font = workbook.createFont();
            font.setBold(true);// 加粗
            titleStyle.setFont(font);
			//每个教师为一个sheet
			List<TeachPlace> tpList = buildingPlaceMap.get(building.getId());
			if(CollectionUtils.isNotEmpty(tpList)){
				tpList.sort(new Comparator<TeachPlace>() {

					@Override
					public int compare(TeachPlace o1, TeachPlace o2) {
						return o1.getPlaceCode().compareTo(o2.getPlaceCode());
					}
				});
				for(TeachPlace tp : tpList){
					//标题
					titleName = tp.getPlaceName() + "的课程表";
					sheet = workbook.createSheet(tp.getPlaceName());
					sheet.setDefaultColumnWidth(15);
					sheet.addMergedRegion(craTitle);
                    //设置打印方向为横向
                    PrintSetup ps = sheet.getPrintSetup();
                    ps.setLandscape(true);
                    ps.setFitWidth((short)1);
                    ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
                    ps.setScale((short)88);
					titleRow = sheet.createRow(0);
					titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
					titleCell = titleRow.createCell(0);
                    titleCell.setCellStyle(titleStyle);
                    for (int i=0; i<titleList.size(); i++) {
                        HSSFCell cell = titleRow.createCell(i);
                        cell.setCellStyle(titleStyle);
                    }
					titleCell.setCellValue(new HSSFRichTextString(titleName));
					//周一到周日
					HSSFRow titleListRow = sheet.createRow(1);
					for (int i=0; i<titleList.size(); i++) {
						 HSSFCell cell = titleListRow.createCell(i);
                        cell.setCellStyle(style);
						 cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
					}
					//上下午晚上
					if(craMs!=null && grade.getMornPeriods()>1){
						sheet.addMergedRegion(craMs);
					}
					if(craAm!=null && grade.getAmLessonCount()>1){
						sheet.addMergedRegion(craAm);
					}
					if(craPm!=null && grade.getPmLessonCount()>1){
						sheet.addMergedRegion(craPm);
					}
					if(craNight!=null && grade.getNightLessonCount()>1){
						sheet.addMergedRegion(craNight);
					}
					for (int i = 2; i < lessonCount+2; i++) {
						HSSFRow inRow = sheet.createRow(i);
						inRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
						for (int j = 0; j < titleList.size(); j++) {
							HSSFCell inCell = inRow.createCell(j);
							inCell.setCellStyle(style);
							if(j==0){
								if(craMs!=null && i==craMs.getFirstRow()){
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
                                if(craMs!=null && i>=craMs.getFirstRow() && i<=craMs.getLastRow()){
                                    inCell.setCellValue(i - 1);
                                }
                                if(craAm!=null && i>=craAm.getFirstRow() && i<=craAm.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods());
                                }
                                if(craPm!=null && i>=craPm.getFirstRow() && i<=craPm.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount());
                                }
                                if(craNight!=null && i>=craNight.getFirstRow() && i<=craNight.getLastRow()){
                                    inCell.setCellValue(i - 1 - grade.getMornPeriods() - grade.getAmLessonCount() - grade.getPmLessonCount());
                                }
							}
						}
					}
					//课程
					placeTimetableList = placeTimetableMap.get(tp.getId());
					for (NewGkTimetableOther o : placeTimetableList) {
						int acPeriod = o.getPeriod();
						if(BaseConstants.PERIOD_INTERVAL_2.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods();
						}
						if(BaseConstants.PERIOD_INTERVAL_3.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount();
						}
						if(BaseConstants.PERIOD_INTERVAL_4.equals(o.getPeriodInterval())){
							acPeriod+=grade.getMornPeriods()+grade.getAmLessonCount()+grade.getPmLessonCount();
						}
						HSSFRow inRow = sheet.getRow(acPeriod+1);
						if(inRow==null){
							continue;
						}
						HSSFCell inCell = inRow.getCell(o.getDayOfWeek()+2);
						if(inCell==null) {
							continue;
						}
						String cellValue = inCell.getRichStringCellValue().getString();
						String fn;
						if(NewGkElectiveConstant.FIRSTSD_WEEK_1==o.getFirstsdWeek()){
							fn="(单)";
						}else if(NewGkElectiveConstant.FIRSTSD_WEEK_2==o.getFirstsdWeek()){
							fn="(双)";
						}else{
							fn="";
						}
						if(StringUtils.isNotBlank(cellValue)){
							cellValue += "\n"+o.getSubjectName()+"\n"+o.getTeacherName()+"\n"+o.getClassName()+fn;
						}else{
							cellValue = o.getSubjectName()+"\n"+o.getTeacherName()+"\n"+o.getClassName()+fn;
						}
						inCell.setCellValue(new HSSFRichTextString(cellValue));
					}
					
				}
			}
			//写入文件
			File file = new File(dirFile,building.getBuildingName()+".xls");
			try {
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
	
	
    
    private List<String> getTitleList(Integer weekDay){
		if(weekDay == null){
			weekDay=7;
		}
    	List<String> titleList = new ArrayList<String>();
		titleList.add("");
		titleList.add("");
		
		for(int i=0;i<weekDay;i++) {
			titleList.add(BaseConstants.dayOfWeekMap2.get(i+""));
		}
		
		return titleList;
    }
    
    
    @ResponseBody
    @RequestMapping("/tree/clazzStudentForDivide/page")
    @ControllerInfo("ztree-行政班-学生")
    public String clazzStudentForDivideTree(@PathVariable String arrayId) {
    	
    	return RedisUtils.getObject("GK_TREE_CLASS_STUDENT_BY_"+arrayId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<String>(){}, new RedisInterface<String>(){
			@Override
			public String queryData() {
				String unitId=getLoginInfo().getUnitId();
				List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(unitId, arrayId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
				list.sort((x,y)->{
					if(x.getOrderId()==null){
						return 1;
					}else if(y.getOrderId()==null){
						return -1;
					}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
						return x.getOrderId().compareTo(y.getOrderId());
					}
					return x.getClassName().compareTo(y.getClassName());
				});
				JSONArray jsonArr = new JSONArray();
//				NewGkArray array = newGkArrayService.findOne(arrayId);
				if(CollectionUtils.isNotEmpty(list)) {
					Set<String> stuIds = list.stream().flatMap(e->e.getStudentList().stream()).collect(Collectors.toSet());
					List<Student> studentListTemp = SUtils.dt(studentRemoteService.findPartStudentById(stuIds.toArray(new String[0])), new TR<List<Student>>(){});
				    Map<String,Student> studentMap=EntityUtils.getMap(studentListTemp,e->e.getId());
				    Map<String,List<Student>> studentByClassId=new HashMap<>();
				    TreeNodeDto treeNodeDto=null;
					for(NewGkDivideClass clazz:list) {
						treeNodeDto = new TreeNodeDto();
			            treeNodeDto.setpId("");
			            treeNodeDto.setId(clazz.getId());
			            treeNodeDto.setName(clazz.getClassName());
			            treeNodeDto.setTitle(clazz.getClassName());
			            treeNodeDto.setOpen(false);
			            treeNodeDto.setType("clazz");
			            studentByClassId.put(clazz.getId(), new ArrayList<>());
			            if(CollectionUtils.isNotEmpty(clazz.getStudentList())) {
			            	for(String s:clazz.getStudentList()) {
			            		if(studentMap.containsKey(s)) {
			            			studentByClassId.get(clazz.getId()).add(studentMap.get(s));
			            		}
			            	}
			            }
			            jsonArr.add(JSON.toJSON(treeNodeDto));
					}
					for(Map.Entry<String,List<Student>>item:studentByClassId.entrySet()) {
						if(CollectionUtils.isNotEmpty(item.getValue())) {
			            	findStudentZTreeJson(item.getKey(),jsonArr, item.getValue(),false);
			            }
					}
					
				}
				
		     	return Json.toJSONString(jsonArr);
			}
        });
    	
    	
		
    }
	


    public void findStudentZTreeJson(String pId, JSONArray jsonArr, List<Student> list, boolean isParent) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        Collections.sort(list, new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
				return o2.getStudentName().compareTo(o1.getStudentName());
			}
        	
		});
        for (Student student : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(student.getId());
            treeNodeDto.setName(student.getStudentName());
            treeNodeDto.setTitle(student.getStudentName());
            treeNodeDto.setOpen(false);
            treeNodeDto.setType("student");
            if (isParent) {
                treeNodeDto.setIsParent(isParent);
            }
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }
    @ResponseBody
    @RequestMapping("/tree/subjectClazzForDivide/page")
    @ControllerInfo("ztree-科目-教学班")
    public String subjectClazzForDivide(@PathVariable String arrayId) {
    	//下拉班级列表
    	return RedisUtils.getObject("GK_TREE_SUBJECT_CLAZZ_BY_"+arrayId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<String>(){}, new RedisInterface<String>(){
			@Override
			public String queryData() {
				JSONArray jsonArr = new JSONArray();
				String unitId = getLoginInfo().getUnitId();
		    	List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassType(unitId, arrayId, 
		    			new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4});
		    	Set<String> classIdAll = EntityUtils.getSet(timetableList, e->e.getClassId());
		    	List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(classIdAll.toArray(new String[0]));
		    	Set<String> subjectIds = EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId);
		    	Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		    	TreeNodeDto treeNodeDto=null;
		    	if(courseNameMap !=null) {
		    		for(String key:courseNameMap.keySet()) {
						treeNodeDto = new TreeNodeDto();
			            treeNodeDto.setpId("");
			            treeNodeDto.setId(key);
			            treeNodeDto.setName(courseNameMap.get(key));
			            treeNodeDto.setTitle(courseNameMap.get(key));
			            treeNodeDto.setOpen(false);
			            treeNodeDto.setType("subject");
			            jsonArr.add(JSON.toJSON(treeNodeDto));
		        	}
		    	}
		    	Map<String,List<NewGkDivideClass>> clazzBysubject=new HashMap<>();
		    	if(CollectionUtils.isNotEmpty(divideClassList)) {
		    		sortClassList(divideClassList);
		    		
		    		Map<String, NewGkDivideClass> classMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		    		for (NewGkTimetable tab : timetableList) {
		    			List<NewGkDivideClass> list = clazzBysubject.get(tab.getSubjectId());
		    			if(list == null) {
		    				list = new ArrayList<>();
		    				clazzBysubject.put(tab.getSubjectId(), list);
		    			}
		    			if(classMap.containsKey(tab.getClassId())) {
		    				list.add(classMap.get(tab.getClassId()));
		    			}
					}
		    		
//		    		for(NewGkDivideClass item:divideClassList) {
//		    			if(!clazzBysubject.containsKey(item.getSubjectIds())) {
//		    				clazzBysubject.put(item.getSubjectIds(), new ArrayList<>());
//		    			}
//		    			clazzBysubject.get(item.getSubjectIds()).add(item);
//		    		}
		    		for(Map.Entry<String,List<NewGkDivideClass>> ll:clazzBysubject.entrySet()) {
		    			if(CollectionUtils.isNotEmpty(ll.getValue())) {
		    				for(NewGkDivideClass vv:ll.getValue()) {
		    					treeNodeDto = new TreeNodeDto();
		    		            treeNodeDto.setpId(ll.getKey());
		    		            treeNodeDto.setId(vv.getId());
		    		            treeNodeDto.setName(vv.getClassName());
		    		            treeNodeDto.setTitle(vv.getClassName());
		    		            treeNodeDto.setOpen(false);
		    		            treeNodeDto.setType("clazz");
		    		            jsonArr.add(JSON.toJSON(treeNodeDto));
		    				}
		    			}
		    		}
		    	}
		    	
		    	return Json.toJSONString(jsonArr);
			}
		});
    }
    
    @ResponseBody
    @RequestMapping("/tree/subjectTeacherForDivide/page")
    @ControllerInfo("ztree-科目-教师")
    public String subjectTeacherForDivide(@PathVariable String arrayId) {
    	//防止一个老师教多门 教师id=subjectId_teacherId
    	//页面默认选中取该教师教的第一门课
    	return RedisUtils.getObject("GK_TREE_SUBJECT_TEACHER_BY_"+arrayId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<String>(){}, new RedisInterface<String>(){
			@Override
			public String queryData() {
				String unitId = getLoginInfo().getUnitId();
				JSONArray jsonArr = new JSONArray();
				
				Set<String> courseIds=new HashSet<>();	
				List<Teacher> teacherList=new ArrayList<Teacher>();
				Map<String,Teacher> tMap=new HashMap<>();
				List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(unitId, arrayId);
				Map<String,List<String>> teacherIdsBySubIds =new HashMap<String,List<String>>();
				if(CollectionUtils.isNotEmpty(timetableList)){
					Map<String, NewGkTimetable> timeTableMap = EntityUtils.getMap(timetableList, e->e.getId());
					Set<String> ids = EntityUtils.getSet(timetableList,NewGkTimetable::getId);
					List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(ids.toArray(new String[]{}));
					if(CollectionUtils.isNotEmpty(timetableTeacherList)){
						Set<String> tids = new HashSet<>();
						for(NewGkTimetableTeacher t:timetableTeacherList) {
							tids.add(t.getTeacherId());
							String subId=timeTableMap.get(t.getTimetableId()).getSubjectId();
							if(!teacherIdsBySubIds.containsKey(subId)) {
								teacherIdsBySubIds.put(subId, new ArrayList<>());
								courseIds.add(subId);
							}
							if(teacherIdsBySubIds.get(subId).contains(t.getTeacherId())) {
								continue;
							}else {
								teacherIdsBySubIds.get(subId).add(t.getTeacherId());
							}
						}
						teacherList = SUtils.dt(teacherRemoteService.findListByIds(tids.toArray(new String[0])),new TR<List<Teacher>>() {});
						tMap=EntityUtils.getMap(teacherList, e->e.getId());
					}
				}
				
				Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(courseIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		    	TreeNodeDto treeNodeDto=null;
		    	if(courseNameMap !=null) {
		    		for(String key:courseNameMap.keySet()) {
						treeNodeDto = new TreeNodeDto();
			            treeNodeDto.setpId("");
			            treeNodeDto.setId(key);
			            treeNodeDto.setName(courseNameMap.get(key));
			            treeNodeDto.setTitle(courseNameMap.get(key));
			            treeNodeDto.setOpen(false);
			            treeNodeDto.setType("subject");
			            jsonArr.add(JSON.toJSON(treeNodeDto));
		        	}
		    	}
		    	for(Map.Entry<String,List<String>> kk:teacherIdsBySubIds.entrySet()) {
		    		List<String> tIdList = kk.getValue();
		    		List<Teacher> tList=new ArrayList<>();
		    		for(String s:tIdList) {
		    			if(tMap.containsKey(s)) {
		    				tList.add(tMap.get(s));
		    			}
		    		}
		    		if(CollectionUtils.isNotEmpty(tList)) {
		    			Collections.sort(tList, new Comparator<Teacher>() {

		    				@Override
		    				public int compare(Teacher o1, Teacher o2) {
		    					return o2.getTeacherName().compareTo(o1.getTeacherName());
		    				}
		    	        	
		    			});
		    			for(Teacher tt:tList) {
	    					treeNodeDto = new TreeNodeDto();
	    		            treeNodeDto.setpId(kk.getKey());
	    		            treeNodeDto.setId(kk.getKey()+"_"+tt.getId());
	    		            treeNodeDto.setName(tt.getTeacherName());
	    		            treeNodeDto.setTitle(tt.getTeacherName());
	    		            treeNodeDto.setOpen(false);
	    		            treeNodeDto.setType("teacher");
	    		            jsonArr.add(JSON.toJSON(treeNodeDto));
	    				}
		    		}
		    	}
		    	
		    	return Json.toJSONString(jsonArr);
			}
		});
    }
   //去空 去英文括号
    public String toTrim(String s) {
    	s=s.replaceAll(" ", "").replaceAll("\\(", "（").replaceAll("\\)", "）");
    	return s;
    }
    
	@RequestMapping("/exportJxbClaStu")
	@ResponseBody
	@ControllerInfo("所有新教学班学生名单Excel")
    public String exportNewClass(@PathVariable String arrayId,HttpServletResponse resp){
		
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), 
				arrayId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		List<NewGkDivideClass> jxbClazzList = divideClassList.parallelStream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2)).collect(Collectors.toList());
		Set<String> parentIds = EntityUtils.getSet(jxbClazzList, NewGkDivideClass::getParentId);
		jxbClazzList = jxbClazzList.parallelStream().filter(e->!parentIds.contains(e.getId())).collect(Collectors.toList());
		NewGkArray array = newGkArrayService.findOne(arrayId);
		Set<String> studentIds = new HashSet<String>();
		for (NewGkDivideClass d : jxbClazzList) {
			if(CollectionUtils.isNotEmpty(d.getStudentList())){
				studentIds.addAll(d.getStudentList());
			}
		}
		//班级相关信息
		List<ArrayResultSubjectDto> dtoList = newGkTimetableService.findByMoreConditions(this.getLoginInfo().getUnitId(), arrayId, new ArraySearchDto());
		Map<String, ArrayResultSubjectDto> dtoMap = EntityUtils.getMap(dtoList, ArrayResultSubjectDto::getClassId);
		// 获取学生选课 情况
		NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
		List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), null, null, studentIds.toArray(new String[0])), Student.class);
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		
		Set<String> classIds = EntityUtils.getSet(studentList,Student::getClassId);
		List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[] {})), Clazz.class);
		Map<String, Clazz> classMap = EntityUtils.getMap(classList, Clazz::getId);
		
		
		List<NewGkChoResult> choResultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), studentIds.toArray(new String[] {}));
		Map<String, List<String>> stuSubjectMap = new HashMap<>();
		for (NewGkChoResult choose : choResultList) {
			if(!stuSubjectMap.containsKey(choose.getStudentId())) {
				stuSubjectMap.put(choose.getStudentId(), new ArrayList<>());
			}
			stuSubjectMap.get(choose.getStudentId()).add(choose.getSubjectId());
		}
		
		// 选课组合
		List<String> subjectIds = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		
		Map<String, List<String>> stuAJxbMap = new HashMap<>();
		Map<String, List<String>> stuBJxbMap = new HashMap<>();
		
		Map<String, String> stuXzbMap = new HashMap<>();
		for (NewGkDivideClass divideClass2 : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClass2.getClassType())) {
				// 行政班
				for (String stuId : divideClass2.getStudentList()) {
					stuXzbMap.put(stuId, divideClass2.getClassName());
				}
			}else {
				for (String stuId : divideClass2.getStudentList()) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(divideClass2.getSubjectType())) {
						if(!stuAJxbMap.containsKey(stuId)) {
							stuAJxbMap.put(stuId, new ArrayList<>());
						}
						stuAJxbMap.get(stuId).add(divideClass2.getClassName());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(divideClass2.getSubjectType())) {
						if(!stuBJxbMap.containsKey(stuId)) {
							stuBJxbMap.put(stuId, new ArrayList<>());
						}
						stuBJxbMap.get(stuId).add(divideClass2.getClassName());
					}
				}
			}
			
		}
		
		// 结果key1:studentId,key2:标题
		Map<String,Map<String,String>> studentMap = new HashMap<String, Map<String,String>>();
		Map<String,String> resultMap;
		String aJxbName;
		String bJxbName;
		for (Student student : studentList) {
			//单科分层可能存在学生没有选课
			String chooseSubjects="";
			if(stuSubjectMap.containsKey(student.getId())) {
				for(String s:stuSubjectMap.get(student.getId())) {
					if(courseMap.containsKey(s)) {
						chooseSubjects=chooseSubjects+"、"+courseMap.get(s);
					}
				}
			}
			if(StringUtils.isNotBlank(chooseSubjects)) {
				chooseSubjects=chooseSubjects.substring(1);
			}
		
			resultMap = new HashMap<String, String>();
			
			resultMap.put("姓名", student.getStudentName());
			resultMap.put("学号", student.getStudentCode());
			resultMap.put("性别", codeMap.get(student.getSex()+"").getMcodeContent());
			resultMap.put("原行政班", classMap.get(student.getClassId()).getClassName());
			resultMap.put("新行政班", stuXzbMap.get(student.getId()));
			resultMap.put("已选学科", chooseSubjects);
			aJxbName = "";
			if(CollectionUtils.isNotEmpty(stuAJxbMap.get(student.getId()))){
				for (String className : stuAJxbMap.get(student.getId())) {
					aJxbName+="、"+className;
				}
				aJxbName = aJxbName.substring(1);
			}
			resultMap.put("选考班", aJxbName);
			bJxbName = "";
			if(CollectionUtils.isNotEmpty(stuBJxbMap.get(student.getId()))){
				for (String className : stuBJxbMap.get(student.getId())) {
					bJxbName+="、"+className;
				}
				bJxbName = bJxbName.substring(1);
			}
			resultMap.put("学考班", bJxbName);
			studentMap.put(student.getId(), resultMap);
		}
		
		String[] titleArr;
		if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())) {
			titleArr = new String[]{"姓名","学号","性别","原行政班","已选学科","选考班","学考班","其他信息"};
		}else{
			titleArr = new String[]{"姓名","学号","性别","原行政班","新行政班","已选学科","选考班","学考班","其他信息"};
		}		
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//水平
		style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		style.setWrapText(true);//自动换行
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		//每班为一个sheet
		int index;
		for(NewGkDivideClass divideClass : jxbClazzList){
			index=0;
			sheet = workbook.createSheet(divideClass.getClassName());
			//sheet.setDefaultColumnWidth(15);
			//首行
			titleRow = sheet.createRow(index++);
			for (int i=0; i<titleArr.length; i++) {
				 titleCell = titleRow.createCell(i);
				 titleCell.setCellValue(new HSSFRichTextString(titleArr[i]));
			}
			List<String> sList = divideClass.getStudentList();
			if(CollectionUtils.isNotEmpty(sList)){
				ArrayResultSubjectDto dto = dtoMap.get(divideClass.getId());
				int tempIndex = 1;
				for (String s : sList) {
					Map<String, String> inMap = studentMap.get(s);
					Map<String, String> tempMap = new HashMap<String, String>();
					if(tempIndex==1){
						tempMap.put("其他信息", dto.getSubjectType());
					}else if(tempIndex==2){
						tempMap.put("其他信息", dto.getPlaceNames());
					}else if(tempIndex==3){
						tempMap.put("其他信息", dto.getTeacherNames());
					}
					HSSFRow contentRow = sheet.createRow(index++);
					for (int i=0; i<titleArr.length; i++) {
						HSSFCell cell = contentRow.createCell(i);
						if(titleArr[i].equals("其他信息")){
							cell.setCellValue(new HSSFRichTextString(tempMap.get(titleArr[i])));
						}else{
							cell.setCellValue(new HSSFRichTextString(inMap.get(titleArr[i])));
						}
					}
					tempIndex++;
				}
			}
		}
		
		ExportUtils.outputData(workbook, array.getArrayName().replaceAll(" ", "")+"各班学生名单", resp);
		return returnSuccess();
    }

}
