package net.zdsoft.studevelop.mobile.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.eis.diathesis.remote.service.DiathesisRemoteService;
import net.zdsoft.eis.remote.service.ExamRemoteService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StuSubjectAchiDto;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;
import net.zdsoft.studevelop.data.entity.StuDevelopMannerRecord;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.entity.StuHealthRecord;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopHonor;
import net.zdsoft.studevelop.data.entity.StudevelopMasterWords;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;
import net.zdsoft.studevelop.data.service.StuCheckAttendanceService;
import net.zdsoft.studevelop.data.service.StuDevelopMannerRecordService;
import net.zdsoft.studevelop.data.service.StuDevelopPunishmentService;
import net.zdsoft.studevelop.data.service.StuDevelopRewardsService;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import net.zdsoft.studevelop.data.service.StuHealthRecordService;
import net.zdsoft.studevelop.data.service.StudevelopActivityService;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.data.service.StudevelopHonorService;
import net.zdsoft.studevelop.data.service.StudevelopMasterWordsService;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import net.zdsoft.studevelop.data.service.StudevelopPerformItemService;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;
import net.zdsoft.studevelop.mobile.entity.StuHonor;
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;
import net.zdsoft.studevelop.mobile.entity.StuOutside;
import net.zdsoft.studevelop.mobile.service.StuFamilyWishesService;
import net.zdsoft.studevelop.mobile.service.StuHonorService;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;
import net.zdsoft.studevelop.mobile.service.StuOutsideService;

@Controller
@RequestMapping("/mobile/open/studevelop/handbook")
public class StuHandbookAction extends MobileAction{
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StudevelopActivityService studevelopActivityService;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StuFamilyWishesService stuFamilyWishesService;
	@Autowired
	private StuHealthRecordService stuHealthRecordService;
	@Autowired
	private StuCheckAttendanceService stuCheckAttendanceService;
	@Autowired
	private StuDevelopRewardsService stuDevelopRewardsService;
	@Autowired
	private StuDevelopPunishmentService stuDevelopPunishmentService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	@Autowired
	private StuDevelopMannerRecordService stuDevelopMannerRecordService;
	@Autowired
	private StudevelopMasterWordsService studevelopMasterWordsService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private StudevelopHonorService studevelopHonorService;
	@Autowired
	private StuHonorService stuHonorService;
	@Autowired
	private StudevelopPerformItemService studevelopPerformItemService;
	@Autowired
	private StudevelopMonthPerformanceService studevelopMonthPerformanceService;
	@Autowired
	private StuIntroductionService stuIntroductionService;
	@Autowired
	private StuOutsideService stuOutsideService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	private DiathesisRemoteService diathesisRemoteService;
	private ExamRemoteService examRemoteService;

	private int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	private int getAcadYearLast(String acadyear){
		String[] acadyears = acadyear.split("-");
		String acad = acadyears[1];
		return Integer.valueOf(acad);
	}
	@RequestMapping("/index")
    @ControllerInfo(value = "成长手册Index")
	public String showMyStu(String studentId,String acadyear, String semester, ModelMap map){
		List<StuIntroduction> stuIntrList=stuIntroductionService.findListByStudentId(studentId, 1);
		if(CollectionUtils.isNotEmpty(stuIntrList)){
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
			//获取学段名称
			String sectionName=getSectionName(clazz.getSection());
			int openAcadyear=getAcadYearLast(clazz.getAcadyear());
			Map<String,List<StuIntroduction>> stuIntroMap=new HashMap<String,List<StuIntroduction>>();
			List<String> numberList=new ArrayList<String>();
			for(StuIntroduction stuIntro:stuIntrList){
				String number=getCH(getAcadYearLast(stuIntro.getAcadyear())-openAcadyear+1);
				if(StringUtils.isBlank(number)) continue;
				if(!stuIntroMap.containsKey(number)){
					numberList.add(number);
					stuIntroMap.put(number, new ArrayList<StuIntroduction>());
				}
				stuIntroMap.get(number).add(stuIntro);
			}
			map.put("sectionName", sectionName);
			map.put("numberList", numberList);
			map.put("stuIntroMap", stuIntroMap);
		}else{
			return errorFtl(map, "成长手册还未发布，暂时无法查看！");
		}
		map.put("studentId", studentId);
		return "/studevelop/mobile/stuDevelopGrowIndex.ftl"; 
	}
	@RequestMapping("/show")
    @ControllerInfo(value = "成长手册")
	public String showMyFamily(String studentId, String acadyear, String semester, ModelMap map){
		map.put("isShow","true");
		map.put("acadyear",acadyear);
		map.put("semester" , semester);
		map.put("studentId", studentId);
		String fileUrl=stuIntroductionService.getFileURL();
		map.put("fileUrl", fileUrl);
		//
		//个人简介
		StuIntroduction stuIntroduction = stuIntroductionService.findObj(acadyear , semester,studentId);
		if(stuIntroduction == null)stuIntroduction = new StuIntroduction();
		map.put("stuIntroduction" ,stuIntroduction);
		if(stuIntroduction.getHasRelease() == null || stuIntroduction.getHasRelease() == 0){
			return errorFtl(map, "成长手册还未发布，暂时无法查看！");
		}

		Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		Clazz stuClass = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(stuClass.getGradeId()),Grade.class);
		map.put("grade" , grade);
		student.setClassName(stuClass.getClassNameDynamic());
		//幸福一家
		StuFamilyWishes family = stuFamilyWishesService.findObj(acadyear, semester, studentId);
		if(family==null){
			family = new StuFamilyWishes();

		}
		map.put("family", family);

		map.put("student",student);
		String unitId = student.getSchoolId();
		//校园简介
		String schAcadyear = StuDevelopConstant.DEFAULT_ACADYEAR;
		int schSemester = NumberUtils.toInt(StuDevelopConstant.DEFAULT_SEMESTER);
		List<StudevelopActivity> acts = studevelopActivityService.findActBySemeRangeId(schAcadyear, schSemester, StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_INFO, unitId, StuDevelopConstant.RANGETYPE_SCH);
		StudevelopActivity act = new StudevelopActivity();

		if(CollectionUtils.isNotEmpty(acts)){
			act = acts.get(0);
			act.setNewEn(false);
			List<StudevelopAttachment> atts = studevelopAttachmentService.findListByObjIds(act.getId());
			map.put("picList", atts);
		}
		map.put("schoolInfo" ,act);
		//校长寄语
		School school = SUtils.dc(schoolRemoteService.findOneById(student.getSchoolId()), School.class);
		map.put("myschool",school);

//		StudevelopMasterWords words = studevelopMasterWordsService.getStudevelopMasterWordsByUnitId(unitId);
		List<StudevelopMasterWords> wordsList = studevelopMasterWordsService.getStudevelopMasterWordsByUnitId(unitId);
		StudevelopMasterWords  words=null;
		if(CollectionUtils.isNotEmpty(wordsList)){
			words = wordsList.get(0);
		}
		if(words == null){
			words = new StudevelopMasterWords();
			words.setUnitId(unitId);
		}
		map.put("words", words);

		//任课老师
		List<ClassTeaching>  classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear,semester,new String[]{student.getClassId()}),ClassTeaching.class);
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId) , Teacher.class);

		Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList , Teacher::getId);
		List<String> subjecdList = EntityUtils.getList(classTeachingList, ClassTeaching::getSubjectId);

		Map<String , Course> courseMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(subjecdList)){
			List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjecdList.toArray(new String[0])) , Course.class);
			courseMap = EntityUtils.getMap(courseList , Course::getId);
		}


		for(ClassTeaching classTeaching : classTeachingList){
			Teacher teacher = teacherMap.get(classTeaching.getTeacherId());
			if(teacher != null) classTeaching.setTeacherName(teacher.getTeacherName());
			Course course = courseMap.get(classTeaching.getSubjectId());
			if(course != null)classTeaching.setSubjectName(course.getSubjectName());
		}
		map.put("classTeachingList",classTeachingList);
		//同班同学

		List<Student> classmates = SUtils.dt(studentRemoteService.findByClassIds(new String[]{student.getClassId()}) ,Student.class);
		List<String> studentIds = new ArrayList<>();
		Iterator<Student>  studentIterator = classmates.iterator();
		while(studentIterator.hasNext()){
			Student stu = studentIterator.next();
			studentIds.add(stu.getId());
			if(studentId.equals(stu.getId())){
				studentIterator.remove();
			}
		}
		List<StuIntroduction>  stuIntroductions = stuIntroductionService.findStudentsIntro(acadyear,semester, studentIds.toArray(new String[]{}));
		Map<String , StuIntroduction> stuIntroductionMap = new HashMap<>();
		for(StuIntroduction stuIntro : stuIntroductions){
			stuIntroductionMap.put(stuIntro.getStudentId() , stuIntro);
		}
		map.put("stuIntroductionMap" , stuIntroductionMap);

		map.put("classmates" , classmates);

		//身体健康
		StuHealthRecord stuHealthRecord;
		stuHealthRecord = stuHealthRecordService.getHealthRecordByStuIdSemes(studentId, acadyear, semester);
		if(null == stuHealthRecord){
			stuHealthRecord = new StuHealthRecord();
		}else{
			stuHealthRecord.setAttention(getOtherNum(stuHealthRecord.getAttention()));
			stuHealthRecord.setObservation(getOtherNum(stuHealthRecord.getObservation()));
			stuHealthRecord.setMemory(getOtherNum(stuHealthRecord.getMemory()));
			stuHealthRecord.setThinking(getOtherNum(stuHealthRecord.getThinking()));
			stuHealthRecord.setMood(getOtherNum(stuHealthRecord.getMood()));
			stuHealthRecord.setWill(getOtherNum(stuHealthRecord.getWill()));
		}
		map.put("stuHealthRecord" ,stuHealthRecord);

		//
		//考勤
		StuCheckAttendance stuCheckAttendance;
		stuCheckAttendance = stuCheckAttendanceService.findBystudentId(acadyear, semester, studentId);
		if(null == stuCheckAttendance){
			stuCheckAttendance = new StuCheckAttendance();
		}
		if(null == stuCheckAttendance.getLate()){
			stuCheckAttendance.setLate(0);
		}
		if(null == stuCheckAttendance.getLeaveEarly()){
			stuCheckAttendance.setLeaveEarly(0);
		}
		if(null == stuCheckAttendance.getWasteVacation()){
			stuCheckAttendance.setWasteVacation(0);
		}
		map.put("stuCheckAttendance" ,stuCheckAttendance);

		diathesisRemoteService = getDiathesisRemoteService();
		//综合素质
		List<String[]> qualityOfMind = null;
		if (diathesisRemoteService != null) {
			try {
				qualityOfMind = diathesisRemoteService.getStuReportData4OneLevel(acadyear, semester, unitId, studentId,"思想品德素质").getQualityOfMind();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		if(null == qualityOfMind){
			qualityOfMind = new ArrayList<String[]>();
//			String[] str1 = {"fefef","A"};
//			String[] str2 = {"fefef","D"};
//			String[] str3 = {"fefef","E"};
//			qualityOfMind.add(str1);
//			qualityOfMind.add(str2);
//			qualityOfMind.add(str3);
		}
		map.put("qualityOfMind" ,qualityOfMind);

		Semester semesterOjb = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acadyear,Integer.valueOf(semester)),Semester.class);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = semesterOjb.getSemesterBegin();
		String startStr = format.format(start);
		String[] arrs = startStr.split("-");
		String year = arrs[0];
		String monthStr = arrs[1];
		int monthInteger = Integer.valueOf(arrs[1]);
		List<String> yearMonths = new ArrayList<>();
		while(start.before(semesterOjb.getSemesterEnd())){
			monthStr = (monthInteger > 9 ? String.valueOf(monthInteger):"0"+monthInteger);
			yearMonths.add(year+"." +monthStr);
			monthInteger++;
			start = DateUtils.getDate(Integer.valueOf(year),monthInteger ,1);
			monthInteger = getMonth(start);
			year = DateUtils.date2String(start,"yyyy");
		}
		Collections.reverse(yearMonths);
		map.put("yearMonths" ,yearMonths);
		//每月表现
		Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
		if(semesterObj != null){
			String curAcadyear = semesterObj.getAcadyear();

			int length = getAcadYearLast(curAcadyear) - getAcadYearLast(acadyear);
			String gradeCode = grade.getGradeCode();
			String gradeCodeLast = gradeCode.substring(1);

			int size = Integer.valueOf(gradeCodeLast) - length;
			String newGradeCode = gradeCode.substring(0,1)+size;

			List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(unitId,newGradeCode);
			map.put("performanceItems",itemList);
			List<StudevelopMonthPerformance> performanceList = studevelopMonthPerformanceService.getMonthPermanceListByStuId(unitId,acadyear,Integer.valueOf(semester),studentId);
			Map<String,Map<String ,StudevelopMonthPerformance>>  monthItemIdMap= new HashMap<>();
			for(StudevelopMonthPerformance performance : performanceList){
				String monStr = performance.getPerformMonth() > 9 ? String.valueOf(performance.getPerformMonth()): "0"+performance.getPerformMonth();

				Map<String ,StudevelopMonthPerformance>  itemIdMap = monthItemIdMap.get(monStr);
				if(itemIdMap == null){
					itemIdMap = new HashMap<>();
				}
				itemIdMap.put(performance.getItemId() , performance);
				monthItemIdMap.put(monStr , itemIdMap);
			}

			Set<String> monthItemKey = monthItemIdMap.keySet();
			Iterator<String> monthIterator = yearMonths.iterator();
			while(monthIterator.hasNext()){
				String mon = monthIterator.next().substring(5);
				if(!monthItemKey.contains(mon)){
					monthIterator.remove();
				}

			}

			map.put("performanceMapList" , monthItemIdMap);
		}
		//惩罚与奖励
		List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsService.findByAcaAndSemAndStuId(acadyear, semester, studentId);
		List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentService.findByAcaAndSemAndStuId(acadyear, semester, studentId);
		//教师寄语
		StuEvaluateRecord stuEvaluateRecord;
		stuEvaluateRecord = stuEvaluateRecordService.findById(studentId, acadyear, semester);
		if(null == stuEvaluateRecord){
			stuEvaluateRecord = new StuEvaluateRecord();
		}
		map.put("stuDevelopRewardsList" ,stuDevelopRewardsList);
		map.put("stuDevelopPunishmentList" , stuDevelopPunishmentList);
		map.put("stuEvaluateRecord" ,stuEvaluateRecord);

		examRemoteService = getExamRemoteService();
		//成绩
		//考试成绩
		List<String[]> examlist = null;
		if (examRemoteService != null) {
			try {
				examlist = examRemoteService.findStuAchiList(acadyear,semester, unitId, studentId).getResults();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		if(null == examlist){
			examlist = new ArrayList<String[]>();
			/*String[] arr=new String[9];
			arr[0]="17791836172300352783776306040588";
			arr[1]="体育";
			arr[2]="1"+"_"+"优秀";
			arr[3]="100";
			arr[7]="3";
			examlist.add(arr);*/
		}

		List<StuDevelopMannerRecord> mannerList = stuDevelopMannerRecordService.findListByStu(acadyear, semester, studentId, null);
		StuSubjectAchiDto achi;
		String subId;
		List<StuSubjectAchiDto> achilist = new ArrayList<StuSubjectAchiDto>();

		for(String[] el:examlist){
			subId = el[0];
			achi = new StuSubjectAchiDto();
			achi.setSubid(subId);
			achi.setSubname(el[1]);
			if(el[2]!=null){
				String[] psarr=el[2].split("_");
				float psachi=NumberUtils.toFloat(psarr[0]);
				float psFullMark=NumberUtils.toFloat(el[5],100);
				//等第的情况
				if(NumberUtils.toInt(el[7])>0){
					achi.setPsachi(psarr[1]);
					achi.setPsPercent(getPercent(NumberUtils.toInt(el[7]),psachi));
				}else{
					achi.setPsachi(psachi + "");
					achi.setPsPercent(psachi/psFullMark);
				}
				achi.setPsFullMark(psFullMark + "");
			}
			if(el[3]!=null){
				String[] qmarr=el[3].split("_");
				float qmachi=NumberUtils.toFloat(qmarr[0]);
				float qmFullMark=NumberUtils.toFloat(el[6],100);
				if(NumberUtils.toInt(el[8])>0){
					achi.setQmachi(qmarr[1]);
					achi.setQmPercent(getPercent(NumberUtils.toInt(el[8]),qmachi));
				}else{
					achi.setQmachi(qmachi + "");
					achi.setQmPercent(qmachi/qmFullMark);
				}
				achi.setSchPlace(el[4]);
				achi.setQmFullMark(qmFullMark+ "");
			}
			
			for(StuDevelopMannerRecord re : mannerList){
				if(subId.equals(re.getSubjectId())){
					achi.setXxtd(re.getManner());
					achi.setCoomunication(re.getCommunication());
					achi.setDiscovery(re.getDiscovery());
				}
			}
			achilist.add(achi);
		}

//		if(CollectionUtils.isEmpty(achilist)){
//			StuSubjectAchiDto dto = new StuSubjectAchiDto();
//			dto.setSubname("语文");
//			dto.setPsachi("0");
//			dto.setPsFullMark("100");
//			dto.setQmFullMark("100");
//			dto.setQmachi("0");
//			dto.setXxtd("");
//
//			StuSubjectAchiDto dto2 = new StuSubjectAchiDto();
//			dto2.setSubname("数学");
//			dto2.setPsachi("0");
//			dto2.setPsFullMark("100");
//			dto2.setQmFullMark("100");
//			dto2.setQmachi("0");
//			dto2.setXxtd("");
//			achilist.add(dto);
//			achilist.add(dto2);
//
//		}
		map.put("achilist", achilist);



		//校园活动
		List<StudevelopActivity> schoolActivitys = studevelopActivityService.findActBySemeRangeId(acadyear, Integer.valueOf(semester),
				StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_ACTIVITY, unitId, StuDevelopConstant.RANGETYPE_SCH);
		List<String> activityIdList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(schoolActivitys)){
			List<String> schActivityIds = EntityUtils.getList(schoolActivitys ,StudevelopActivity::getId);
			activityIdList.addAll(schActivityIds);
		}
		map.put("schoolActivity" ,schoolActivitys);

		//主题活动
		List<StudevelopActivity> themeActivity = studevelopActivityService.findActBySemeRangeId(acadyear, Integer.valueOf(semester),
				StuDevelopConstant.ACTIVITY_TYPE_THEME_ACTIVITY, student.getClassId(), StuDevelopConstant.RANGETYPE_CLASS);
		if(CollectionUtils.isNotEmpty(themeActivity)){
			List<String> themeActivityIds = EntityUtils.getList(themeActivity ,StudevelopActivity::getId);
			activityIdList.addAll(themeActivityIds);
		}
		map.put("themeActivity" ,themeActivity);

		//班级活动
		List<StudevelopActivity> classActivity = studevelopActivityService.findActBySemeRangeId(acadyear, Integer.valueOf(semester),
				StuDevelopConstant.ACTIVITY_TYPE_CLASS_ACTIVITY, student.getClassId(), StuDevelopConstant.RANGETYPE_CLASS);
		if(CollectionUtils.isNotEmpty(classActivity)){
			List<String> themeActivityIds = EntityUtils.getList(classActivity ,StudevelopActivity::getId);
			activityIdList.addAll(themeActivityIds);
		}
		map.put("classActivity" ,classActivity);
		
		//班级荣誉
		List<StudevelopHonor> classHonorList = studevelopHonorService.getStudevelopHonorByAcadyearAndSemester(acadyear,Integer.valueOf(semester),student.getClassId());
		StudevelopHonor classHonor=null;
		if(CollectionUtils.isNotEmpty(classHonorList)){
			classHonor = classHonorList.get(0);
		}
		if(classHonor != null){
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(classHonor.getId(), "class_honor");
			map.put("classHonorDetails", atts);
		}

		//校外表现

		List<StuOutside> schoolOutsideList = stuOutsideService.findList(acadyear, semester, studentId, StuOutside.TYPE_1);
		map.put("schoolOutsideList" ,schoolOutsideList);
		map.put("schoolOutsideActivity" ,schoolOutsideList);
		if(CollectionUtils.isNotEmpty(schoolOutsideList)){
			List<String> schoolOutActivityIds = EntityUtils.getList(schoolOutsideList ,StuOutside::getId);
			activityIdList.addAll(schoolOutActivityIds);
		}
		//我的假期
		List<StuOutside> schoolHolodayList = stuOutsideService.findList(acadyear, semester, studentId, StuOutside.TYPE_2);
		map.put("schoolHolodayList" ,schoolHolodayList);
		map.put("myHolidayActivity" ,schoolHolodayList);
		if(CollectionUtils.isNotEmpty(schoolHolodayList)){
			List<String> schoolHolodayListIds = EntityUtils.getList(schoolHolodayList ,StuOutside::getId);
			activityIdList.addAll(schoolHolodayListIds);
		}
		//活动的关联Map 一次性都取
		Map<String,List<StudevelopAttachment>>  activityMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(activityIdList)){
//			List<String> schoolActivityIds = EntityUtils.getList(schoolActivitys,"id");
			List<StudevelopAttachment> atts = studevelopAttachmentService.findListByObjIds(activityIdList.toArray(new String[0]));
			for( StudevelopAttachment att : atts){

				List<StudevelopAttachment> list = activityMap.get(att.getObjId());
				if(list == null){
					list =new ArrayList<>();
					activityMap.put(att.getObjId() , list);
				}
				list.add(att);
			}

		}
		map.put("activityMap", activityMap);
		//我的荣誉
		StuHonor myHonor = stuHonorService.findObj(acadyear, semester, studentId);
		if( myHonor != null){
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(myHonor.getId(), "studev_honor");
			map.put("myHonorDetails", atts);
		}
//		map.put("myHonor" ,myHonor);
		return "/studevelop/mobile/stuDevelopGrowShow.ftl";
		//return "/studevelop/mobile/myHandbook.ftl";
	}
	
	public DiathesisRemoteService getDiathesisRemoteService() {
		try {
			if(diathesisRemoteService == null){
				diathesisRemoteService = Evn.getBean("diathesisRemoteService");
				log.error("综合素质duddo服务没有开启，没有提供者");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diathesisRemoteService;
	}

	public ExamRemoteService getExamRemoteService() {
		if(examRemoteService == null){
			examRemoteService = Evn.getBean("examRemoteService");
			log.error("教务成绩duddo服务没有开启，没有提供者");
		}
		return examRemoteService;
	}
	public float getPercent(int l,float psachiInt){
		for(int i=1;i<=l;i++){
			if((int)psachiInt==i){
				return 1-(psachiInt-1)/l;
			}
		}
		return 0f;
	}
	private String getSectionName(int section){
		String sectionName="";
		if(section==1){
			sectionName="小学";
		}else if(section==2){
			sectionName="初中";
		}else if(section==3){
			sectionName="高中";
		}else if(section==9){
			sectionName="剑桥高中";
		}
		return sectionName;
	}
	private  String getCH(int input) {  
        String sd = "";  
        switch (input) {  
        case 1:  
            sd = "一";  
            break;  
        case 2:  
            sd = "二";  
            break;  
        case 3:  
            sd = "三";  
            break;  
        case 4:  
            sd = "四";  
            break;  
        case 5:  
            sd = "五";  
            break;  
        case 6:  
            sd = "六";  
            break;  
        default:  
            break;  
        }  
        return sd;  
    }  
	public String getOtherNum(String numStr){
		int num=0;
		if(StringUtils.isNotBlank(numStr)){
			num=Integer.parseInt(numStr.trim());
		}
		if(num==1) num=3;
		else if(num==3) num=1;
		return String.valueOf(num);
	}
}
