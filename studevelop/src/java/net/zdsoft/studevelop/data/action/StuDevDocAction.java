package net.zdsoft.studevelop.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.eis.diathesis.remote.service.DiathesisRemoteService;
import net.zdsoft.eis.remote.service.ExamRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.studevelop.data.constant.DocumentHandler;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StuSubjectAchiDto;
import net.zdsoft.studevelop.data.entity.*;
import net.zdsoft.studevelop.data.service.*;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;
import net.zdsoft.studevelop.mobile.entity.StuHonor;
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;
import net.zdsoft.studevelop.mobile.entity.StuOutside;
import net.zdsoft.studevelop.mobile.service.StuFamilyWishesService;
import net.zdsoft.studevelop.mobile.service.StuHonorService;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;
import net.zdsoft.studevelop.mobile.service.StuOutsideService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author weixh
 * @since 2017-8-17 下午2:45:22
 */
@Controller
@RequestMapping("/studevelop/devdoc")
public class StuDevDocAction extends CommonAuthAction {
	@Autowired
	private SemesterRemoteService semesterRemoteService;
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
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private StuOutsideService stuOutsideService;
	@Autowired
	private StuDevelopRewardsService stuDevelopRewardsService;
	@Autowired
	private StuDevelopSchoolNoticeService stuDevelopSchoolNoticeService;
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
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	private DiathesisRemoteService diathesisRemoteService;
	private ExamRemoteService examRemoteService;

//	private SchoolSemesterService  SchoolSemester;
	private File cssDir = null;
	private List<StudevelopAttachment> masterPicAtts = null;
	private List<StudevelopAttachment> schPicatts = null;
	private List<StudevelopAttachment> myHoratts = null;
	private List<StudevelopAttachment> clsHoratts = null;
	private Map<String,List<StudevelopAttachment>> activityMap;
	
	@RequestMapping("/index/page")
	@ControllerInfo(value="成长手册首页")
	public String showIndex(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
//		String teacherId = getLoginInfo().getOwnerId();
		List<String> acadyears = SUtils.dt(semesterRemoteService.findAcadeyearList() ,new TR<List<String>>());

		if(CollectionUtils.isEmpty(acadyears)){
			return errorFtl(map , "还没有维护学年学期信息！");
		}

		Semester sc = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId) , Semester.class);
		String acadyear = "";
		int semester = 0;
		if(sc == null){
			acadyear = acadyears.get(0);
			semester = 1;
		}else{
			acadyear = sc.getAcadyear();
			semester = sc.getSemester();
		}

		Map<String, String> gradeMap = new HashMap<String,String>();
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId) ,Grade.class);
		for(Grade grade : gradeList){
			gradeMap.put(grade.getId(), grade.getGradeName());
		}
		List<Clazz> classList = null;
		if(isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
			map.put("isAdmin",true);
			classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
		}else {
			classList = headTeacherClass(acadyear);
		}
		if(CollectionUtils.isEmpty(classList)){
			return promptFlt(map , "不是（副）班主任和管理员不能维护和查询成长手册内容");
		}
		map.put("classList",classList);
		map.put("acadyear",acadyear);
		map.put("semester" ,semester);
		map.put("acadyearList" , acadyears);
		return "/studevelop/stuGrow/stuDevelopGrowIndex.ftl";
	}

	@RequestMapping("/studentList")
	@ControllerInfo(value="成长手册-学生列表")
	public String getStudentList(String classId , String acadyear, String semester , String studentName  , ModelMap map , HttpServletRequest request){
		String unitId = getLoginInfo().getUnitId();
		Student student = new Student();
		student.setIsLeaveSchool(0);
		try {
			studentName = URLDecoder.decode(studentName,"UTF-8");
			student.setStudentName(studentName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		map.put("acadyear", acadyear);
		map.put("semester", semester);
		List<String> classIds = new ArrayList<>();
		if(StringUtils.isNotEmpty(classId)){
			classIds.add(classId);
		}else{
			List<Clazz> clazzList = SUtils.dt( getClassList(acadyear) , Clazz.class);
			for(Clazz cla : clazzList){
				classIds.add(cla.getId());
			}
		}
		String stuList = studentRemoteService.findByIdsClaIdLikeStuCodeNames(unitId , null, classIds.toArray(new String[]{}) ,JSON.toJSONString(student) ,null);
		List<Student> studentList =  SUtils.dt(stuList,Student.class);
		List<String> studentIds = EntityUtils.getList(studentList, Student::getId);

		map.put("studentList" , studentList);

		if(CollectionUtils.isNotEmpty(studentIds)){
			List<StuIntroduction>  stuIntroductions = stuIntroductionService.findStudentsIntro(acadyear,semester, studentIds.toArray(new String[]{}));
			Map<String , StuIntroduction> stuIntroductionMap = new HashMap<>();
			for(StuIntroduction stuIntroduction : stuIntroductions){
				stuIntroductionMap.put(stuIntroduction.getStudentId() , stuIntroduction);
			}
			map.put("stuIntroductionMap" , stuIntroductionMap);
		}
		return "/studevelop/stuGrow/stuDevelopGrowList.ftl";
	}
	
	
	@RequestMapping("/classList")
	@ResponseBody
	@ControllerInfo(value="成长手册-获取班级列表")
	public String getClassList(String acadyear){
		String unitId = getLoginInfo().getUnitId();
		String teacherId = getLoginInfo().getOwnerId();
		Map<String, String> gradeMap = new HashMap<String,String>();
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId) ,Grade.class);
		for(Grade grade : gradeList){
			gradeMap.put(grade.getId(), grade.getGradeName());
		}
		List<Clazz> clazzes = SUtils.dt(classRemoteService.findBySchoolIdTeacherIdAll(unitId , teacherId) ,Clazz.class);
		Map<String , Clazz> clazzMap = new HashMap<String , Clazz>();
		List<Clazz> classList = new ArrayList<>();
		String year = acadyear.split("-")[1];
		int yearToInt = Integer.valueOf(year);
		for(Clazz clazz : clazzes){
			if(clazz.getIsDeleted() == 0 ){
				String gradeId = clazz.getGradeId();
				String gradeName = gradeMap.get(gradeId);
				clazz.setClassNameDynamic(gradeName + clazz.getClassName());
				String acadYear = clazz.getAcadyear().split("-")[1];
				int acadYerInt = Integer.valueOf(acadYear);
				if((yearToInt - acadYerInt) <= clazz.getSchoolingLength()){
					clazzMap.put(clazz.getAcadyear() ,clazz);
					classList.add(clazz);
				}

			}
		}
		return JSON.toJSONString(classList);
	}
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
	@RequestMapping("/studentDetail")
	@ControllerInfo(value="成长手册-详情")
	public String getStudentDetail( String acadyear , String semester,  String studentId , ModelMap map){

		map.put("acadyear",acadyear);
		map.put("semester" , semester);
		map.put("studentId", studentId);
		//
		//个人简介
		StuIntroduction stuIntroduction = stuIntroductionService.findObj(acadyear , semester,studentId);
		if(stuIntroduction == null)stuIntroduction = new StuIntroduction();
		map.put("stuIntroduction" ,stuIntroduction);

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
		List<StudevelopAttachment> wordsAtts = studevelopAttachmentService.getAttachmentByObjId(unitId, StuDevelopConstant.OBJTYPE_MASTER_PIC);
		map.put("wordsAtts",wordsAtts);
		if(words == null){
			words = new StudevelopMasterWords();
			words.setUnitId(unitId);
		}
		map.put("wordsAtts", wordsAtts);
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
//		ClassTeaching classTeaching = new ClassTeaching();
//		classTeaching.setSubjectName("小学品德与社会");
//		classTeaching.setTeacherName("东方红");
//		classTeachingList.add(classTeaching);

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

        StudevelopSchoolNotice schoolNotice = stuDevelopSchoolNoticeService.getSchoolNoticeByAcadyearSemesterUnitId(acadyear,semester,String.valueOf(grade.getSection()),unitId);
		//考勤
		StuCheckAttendance stuCheckAttendance;
		stuCheckAttendance = stuCheckAttendanceService.findBystudentId(acadyear, semester, studentId);
		if(null == stuCheckAttendance){
			stuCheckAttendance = new StuCheckAttendance();
		}
		if(null!=schoolNotice){
			if (schoolNotice.getStudyDate() != null) {
				stuCheckAttendance.setStudyDate(schoolNotice.getStudyDate());
			}
			stuCheckAttendance.setStudyBegin(schoolNotice.getStudyBegin());
	    	stuCheckAttendance.setRegisterBegin(schoolNotice.getRegisterBegin());
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
			arr[2]="1"+"优秀";
			arr[3]="100";
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
//			dto.setSubname("小学语文");
//			dto.setPsachi("100");
//			dto.setPsFullMark("100");
//			dto.setQmFullMark("100");
//			dto.setQmachi("50.6");
//			dto.setXxtd("2");
//
//			StuSubjectAchiDto dto2 = new StuSubjectAchiDto();
//			dto2.setSubname("小学数学");
//			dto2.setPsachi("80");
//			dto2.setPsFullMark("100");
//			dto2.setQmFullMark("100");
//			dto2.setQmachi("8.5");
//			dto2.setXxtd("");
//			StuSubjectAchiDto dto3 = new StuSubjectAchiDto();
//			dto3.setSubname("小学品德与社会");
//			dto3.setPsachi("0");
//			dto3.setPsFullMark("100");
//			dto3.setQmFullMark("100");
//			dto3.setQmachi("50");
//			dto3.setXxtd("");
//
//			achilist.add(dto);
//			achilist.add(dto2);
//			achilist.add(dto3);
//
//		}
		map.put("achilist", achilist);

		//校园活动
		List<StudevelopActivity> schoolActivitys = studevelopActivityService.findActBySemeRangeId(acadyear, Integer.valueOf(semester),
				StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_ACTIVITY, unitId, StuDevelopConstant.RANGETYPE_SCH);
		List<String> activityIdList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(schoolActivitys)){
			List<String> schActivityIds = EntityUtils.getList(schoolActivitys, StudevelopActivity::getId);
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
//		StudevelopHonor classHonor = studevelopHonorService.getStudevelopHonorByAcadyearAndSemester(acadyear,Integer.valueOf(semester),student.getClassId());

		List<StudevelopHonor> classHonorList = studevelopHonorService.getStudevelopHonorByAcadyearAndSemester(acadyear,Integer.valueOf(semester),student.getClassId());

		StudevelopHonor classHonor = null;
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
		if(CollectionUtils.isNotEmpty(schoolOutsideList)){
			List<String> schoolOutActivityIds = EntityUtils.getList(schoolOutsideList ,StuOutside::getId);
			activityIdList.addAll(schoolOutActivityIds);
		}
		//我的假期
		List<StuOutside> schoolHolodayList = stuOutsideService.findList(acadyear, semester, studentId, StuOutside.TYPE_2);
		map.put("schoolHolodayList" ,schoolHolodayList);
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

//		return "/studevelop/mobile/stuDevelopGrowDetail.ftl";
		return "/studevelop/stuGrow/stuDevelopGrowDetail.ftl";
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

	@RequestMapping("/release")
	@ResponseBody
	@ControllerInfo(value="成长手册-发布、取消发布、重新发布")
	public String release(String acadyear, String semester, String stuIds, int release, ModelMap map){
		if(StringUtils.isBlank(stuIds)){
			return error("没有选择要操作的学生！");
		}
		try {
			String[] sids = StringUtils.split(stuIds, ",");
			List<StuIntroduction> intros = stuIntroductionService.findStudentsIntro(acadyear, semester, sids);
			Map<String, StuIntroduction> inMap = EntityUtils.getMap(intros, StuIntroduction::getStudentId);
			List<StuIntroduction> ins = new ArrayList<StuIntroduction>();
			
			String dirPath = null;
			List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), StorageDir.class);
			if(CollectionUtils.isNotEmpty(dirs)){
				StorageDir dir = dirs.get(0);
				if(dir != null){
					dirPath = dir.getDir() + File.separator + StuDevelopConstant.DEVDOC_PATH_DIR
							+ File.separator + (acadyear+semester);
				}
			}
			
			for(String sid : sids){
				StuIntroduction in = inMap.get(sid);
				if(in == null){
					in = new StuIntroduction();
					in.setId(UuidUtils.generateUuid());
					in.setAcadyear(acadyear);
					in.setSemester(semester);
					in.setStudentId(sid);
					in.setCreationTime(new Date());
				}
				in.setModifyTime(new Date());
				in.setHasRelease(release);
				ins.add(in);
				try {
					// 手册内容删除
					if(release == 0 && StringUtils.isNotEmpty(dirPath)){
						File sdir = new File(dirPath + File.separator + sid);
						if(sdir.exists()){
							FileUtils.forceDelete(sdir);
						}
					}
				} catch (Exception e) {
					log.error("取消发布删除学生手册内容失败："+e.getMessage(), e);
				}
			}
			
			stuIntroductionService.saveAll(ins.toArray(new StuIntroduction[0]));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("操作失败！");
		}
		return success("操作成功！");
	}
	
	@RequestMapping("/export")
	@ControllerInfo(value="成长手册导出")
	public String export(String acadyear , String semester,  String stuIds, ModelMap map,
						 HttpServletRequest request, HttpServletResponse response){
		try {
			List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), StorageDir.class);
			if(CollectionUtils.isEmpty(dirs)){
				return null;
			}    
			StorageDir dir = dirs.get(0);
			if(dir == null){
				return null;
			}
//			String stPath = Evn.<SysOptionRemoteService> getBean(
//					"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
//			if(stPath == null){
//				return errorFtl(map, "dir==null");
//			}
			String[] sids = StringUtils.split(stuIds, ",");
			Map<String, Student> stuMap = EntityUtils.getMap(Student.dt(studentRemoteService.findListByIds(sids)), Student::getId);
			if(MapUtils.isEmpty(stuMap)){
				return null;
			}
			// 学生导出文件存放地址 store/devdoc/2016-20171/stuid/stuid.zip
			// 学年学期导出目录
			String dirPath = dir.getDir() + File.separator + StuDevelopConstant.DEVDOC_PATH_DIR
					+ File.separator + (acadyear+semester);
			//System.out.println("dirPath="+dirPath);
			File dirFile = new File(dirPath);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}

			// 导出压缩包 临时存放路径
			Date now = new Date();
			File exportDir = null;
			File exportZip = null;
			String exportName = acadyear+"第"+semester+"学期学生成长手册"+ DateUtils.date2String(now, "yyyyMMddHHmm");
			if (sids.length > 1) {
				exportDir = new File(dir.getDir() + File.separator + "swfupload"
						+ File.separator
						+ DateUtils.date2String(now, "yyyyMMdd")
						+ File.separator + (exportName));
				if(!exportDir.exists()){
					exportDir.mkdirs();
				}
			} else {
				exportName = acadyear+"第"+semester+"学期"+stuMap.get(sids[0]).getStudentName()+"的学生成长手册";
			}
			//System.out.println(exportDir);
			for(String studentId : sids){
				Student stu = stuMap.get(studentId);
				if(stu == null){
					continue;
				}
				File stuZip = new File(dirPath + File.separator + studentId
						+ File.separator + (studentId+".zip"));
				//if(!stuZip.exists()){
				if(cssDir == null){
					String webPath = request.getRealPath("/studevelop/devdoc");
					cssDir = new File(webPath);
					if(!cssDir.exists()){
					}
				}
				stuZip = dealWithStuDoc(dirPath, acadyear, semester, studentId, map);
				
				//}
				if(stuZip != null && stuZip.exists()){
					if (sids.length == 1) {
						exportZip = stuZip;
					} else {
						FileUtils.copyFile(stuZip, new File(exportDir.getAbsolutePath()
								+ File.separator + (stu.getStudentName()+StringUtils.trimToEmpty(stu.getStudentCode())+".zip")));
					}
				}
			}
			if(sids.length > 1){
				String zipName = ZipUtils.makeZip(exportDir.getAbsolutePath());
				exportZip = new File(zipName);
			}
			if(exportZip != null){
				InputStream in = new FileInputStream(exportZip);
				ServletUtils.download(in, request, response, exportName+".zip");
			} else {
				log.error("学生手册导出失败：没有可导出的手册文件！");
				return errorFtl(map, "学生手册导出失败：没有可导出的手册文件！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return errorFtl(map, "导出失败！");
		}
		return null;
	}

	/**
	 *
	 * @param dirPath 学年学期导出目录
	 * @param acadyear 学年
	 * @param semester 学期
	 * @param studentId 学生id
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private File dealWithStuDoc(String dirPath, String acadyear, String semester,
								String studentId, ModelMap map){
		File stuZip = null;
		try {
			Map<String, Object> datam = new HashMap<String, Object>();
			getStudentDetail(acadyear, semester, studentId, map);
			String[] propertys = {"acadyear", "semester", "studentId", "stuIntroduction", "grade", "family", "student",
					"picList","schoolInfo", "myschool", "words", "classTeachingList",
					"stuIntroductionMap", "classmates", "stuHealthRecord", "stuCheckAttendance",
					"qualityOfMind", "performanceItems", "performanceMapList", "stuDevelopRewardsList",
					"stuDevelopPunishmentList", "stuEvaluateRecord", "achilist", "schoolActivity",
					"themeActivity", "classActivity", "classHonorDetails", "schoolOutsideList",
					"schoolHolodayList", "activityMap", "myHonorDetails","yearMonths"};
			for(String pro : propertys){
				dealMapValue(pro, datam, map);
			}
			Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeIds(new String[]{"DM-XB","DM-XXTD"}), 
					new TypeReference<Map<String, Map<String, McodeDetail>>>() {
            });
			if(mcodeMap.containsKey("DM-XB")){
				datam.put("xbMcodeMap", mcodeMap.get("DM-XB"));
			}
			if(mcodeMap.containsKey("DM-XXTD")){
				datam.put("xxtdMcodeMap", mcodeMap.get("DM-XXTD"));
			}
			datam.put("mcodeMap", mcodeMap);
			File dataDir = new File(dirPath + File.separator + studentId
					+ File.separator + studentId);
			if(!dataDir.exists()){
				dataDir.mkdirs();
			}
//			String[] pstrs = {"css","images","js"};
//			for (String pstr : pstrs) {
//				File df = new File(cssDir.getAbsolutePath() + File.separator
//						+ pstr);
				FileUtils.copyDirectory(cssDir, dataDir);
//			}
			// TODO 附件图片处理开始===========================================
			// masterPic 校长头像，班级学生头像，幸福一家图片
			masterPicAtts = null;
			masterPicAtts = studevelopAttachmentService.getAttachmentByObjId(getLoginInfo().getUnitId(), StuDevelopConstant.OBJTYPE_MASTER_PIC);
			List<StudevelopAttachment> toAtts = new ArrayList<StudevelopAttachment>();
			if(CollectionUtils.isNotEmpty(masterPicAtts)){
				toAtts.add(masterPicAtts.get(0));
			}
			StuFamilyWishes fam = (StuFamilyWishes) datam.get("family");
			if(fam != null && fam.isExistsImgPath()){
				toAtts.add(fam.getImgAtt());
				fam.setImgPath(fam.getImgAtt().getId()+"."+fam.getImgAtt().getExtName());
				datam.put("family", fam);
			}
			Map<String , StuIntroduction> inMap = (Map<String, StuIntroduction>) datam.get("stuIntroductionMap");
			if(MapUtils.isNotEmpty(inMap)){
				Iterator<Entry<String, StuIntroduction>> init = inMap.entrySet().iterator();
				while(init.hasNext()){
					Entry<String, StuIntroduction> en = init.next();
					StuIntroduction in = en.getValue();
					if(in.getImgAtt() != null){
						toAtts.add(in.getImgAtt());
						in.setImgPath(in.getImgAtt().getId()+"."+in.getImgAtt().getExtName());
					}
					if(studentId.equals(in.getStudentId())){
						datam.put("stuIntroduction", in);
					}
				}
			}
			
			schPicatts = null;
			if (datam.containsKey("picList")) {
				schPicatts = (List<StudevelopAttachment>) datam
						.get("picList");
				if(CollectionUtils.isNotEmpty(schPicatts)){
					toAtts.addAll(schPicatts);
				}
			}
			if(schPicatts == null){
				schPicatts = new ArrayList<StudevelopAttachment>();
			}
			myHoratts = null;
			if(datam.containsKey("myHonorDetails")){
				myHoratts = (List<StudevelopAttachment>) datam
						.get("myHonorDetails");
				if(CollectionUtils.isNotEmpty(myHoratts)){
					toAtts.addAll(myHoratts);
				}
			}
			if(myHoratts == null){
				myHoratts = new ArrayList<StudevelopAttachment>();
			}
			clsHoratts = null;
			if (datam.containsKey("classHonorDetails")) {
				clsHoratts = (List<StudevelopAttachment>) datam
						.get("classHonorDetails");
				if(CollectionUtils.isNotEmpty(clsHoratts)){
					toAtts.addAll(clsHoratts);
				}
			}
			if(clsHoratts == null){
				clsHoratts = new ArrayList<StudevelopAttachment>();
			}
			activityMap = (Map<String, List<StudevelopAttachment>>) datam.get("activityMap");
			if(MapUtils.isNotEmpty(activityMap)){
				Iterator<Entry<String, List<StudevelopAttachment>>> it = activityMap.entrySet().iterator();
				while(it.hasNext()){
					Entry<String, List<StudevelopAttachment>> en = it.next();
					toAtts.addAll(en.getValue());
				}
			}
			if(activityMap == null){
				activityMap = new HashMap<String, List<StudevelopAttachment>>();
			}
			Set<String> dirIds = EntityUtils.getSet(toAtts, StudevelopAttachment::getDirId);
			String ss = storageDirRemoteService.findListByIds(dirIds.toArray(new String[0]));
			List<StorageDir> dirs = SUtils.dt(ss, StorageDir.class);
			if(CollectionUtils.isEmpty(dirs)){
				datam.remove("picList");
				datam.remove("classHonorDetails");
				datam.remove("activityMap");
				datam.remove("myHonorDetails");
			} else {
				Map<String, String> dpm = EntityUtils.getMap(dirs, StorageDir::getId, StorageDir::getDir);
				File imgDir = new File(dataDir.getAbsolutePath()+ File.separator + "images");
				if(!imgDir.exists()){
					imgDir.mkdirs();
				}
				for(StudevelopAttachment at : toAtts){
					File srcFile = null;
					if(at.getOriginFile() != null){
						srcFile = at.getOriginFile();
					} else if(at.getSmallFile() != null){
						srcFile = at.getSmallFile();
					}
					
					if (srcFile == null) {
						if (StringUtils.isEmpty(at.getFilePath())
								|| StringUtils.isEmpty(at.getDirId())
								|| !dpm.containsKey(at.getDirId())) {
							removeInvalidAtts(at);
							continue;
						}
						String dp = dpm.get(at.getDirId());
						File dpf = new File(dp);
						if (!dpf.exists()) {
							removeInvalidAtts(at);
							continue;
						}
						File af = new File(dp + File.separator
								+ at.getFilePath());
						if (!af.exists()) {
							removeInvalidAtts(at);
							continue;
						}
						File of = new File(af.getParent() + File.separator
								+ StuDevelopConstant.PIC_ORIGIN_NAME + "."
								+ at.getExtName());
						if (of.exists()) {
							srcFile = of;
						} else {
							srcFile = af;
						}
					}
					if (StuDevelopConstant.OBJTYPE_MASTER_PIC.equals(at
							.getObjecttype())) {
						datam.put("masterPic",
								at.getId() + "." + at.getExtName());
					}
					FileUtils.copyFile(srcFile,
							new File(imgDir.getAbsolutePath()
									+ File.separator + at.getId() + "."
									+ at.getExtName()));
				}
				if (schPicatts.size() > 0) {
					datam.put("picList", schPicatts);
				} else {
					datam.remove("picList");
				}
				if (clsHoratts.size() > 0) {
					datam.put("classHonorDetails", clsHoratts);
				} else {
					datam.remove("classHonorDetails");
				}
				if (activityMap.size() > 0) {
					datam.put("activityMap", activityMap);
				} else {
					datam.remove("activityMap");
				}
				if(myHoratts.size() > 0){
					datam.put("myHonorDetails", myHoratts);
				} else {
					datam.remove("myHonorDetails");
				}
			}
			// TODO 附件图片处理结束===========================================
			String value = DocumentHandler.createDocString(datam, "devDoc.ftl");
			InputStream in = new StringInputStream(value, "utf-8");
			File docFile = new File(dataDir+File.separator+"学生成长手册.html");
			FileUtils.copyInputStreamToFile(in, docFile);
			String zipName = ZipUtils.makeZipWithFile(dataDir.getAbsolutePath());
			stuZip = new File(zipName);
			// ==============打包之后 删除 测试阶段暂留=============
			FileUtils.deleteDirectory(dataDir);
		} catch (Exception e) {
			log.error("学生"+studentId+"成长手册导出失败："+e.getMessage(), e);
		}
		return stuZip;
	}
	
	/**
	 * 附件无效（目录不存在，文件取不到等）时去除附件
	 * @param at
	 */
	private void removeInvalidAtts(StudevelopAttachment at){
		if(StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_INFO.equals(at.getObjecttype())){
			schPicatts.remove(at);
		} else if(StuDevelopConstant.OBJTYPE_CLASS_HONOR.equals(at.getObjecttype())){
			clsHoratts.remove(at);
		} else if(StuDevelopConstant.OBJTYPE_STUDEV_HONOR.equals(at.getObjecttype())){
			myHoratts.remove(at);
		} else if(activityMap.containsKey(at.getObjId())){
			activityMap.get(at.getObjId()).remove(at);
		} else {
			//System.out.println(at.getId());
		}
	}

	/**
	 * 学生数据拷贝
	 * @param key
	 * @param datam
	 * @param map
	 */
	private void dealMapValue(String key, Map<String, Object> datam, ModelMap map){
		if (map.containsKey(key)) {
			datam.put(key, map.get(key));
			map.remove(key);
		}
	}
	
	public static void main(String[] args) {
		File d2 = new File("d:\\2");
		File d1 = new File("d:\\1");
		try {
			FileUtils.copyDirectory(d1, d2);
//			ZipUtils.makeZipWithFile(d2.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public float getPercent(int l,float psachiInt){
		for(int i=1;i<=l;i++){
			if((int)psachiInt==i){
				return 1-(psachiInt-1)/l;
			}
		}
		return 0f;
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
