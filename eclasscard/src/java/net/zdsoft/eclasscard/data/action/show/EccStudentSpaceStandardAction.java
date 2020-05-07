package net.zdsoft.eclasscard.data.action.show;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.remote.utils.AttachmentUtils;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.EccStuQualityDto;
import net.zdsoft.eclasscard.data.dto.StuDormInfoDto;
import net.zdsoft.eclasscard.data.dto.StudentLeaveDto;
import net.zdsoft.eclasscard.data.entity.EccHonor;
import net.zdsoft.eclasscard.data.entity.EccHonorTo;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.service.*;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.*;
import net.zdsoft.officework.remote.service.OfficeAttanceRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stutotality.data.dto.StutotalityOptionDto;
import net.zdsoft.stutotality.data.dto.StutotalityResultDto;
import net.zdsoft.stutotality.data.dto.StutotalityTypeDto;
import net.zdsoft.stutotality.remote.service.StutotalityRemoteService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

import static net.zdsoft.eclasscard.data.constant.EccConstants.*;

@Controller
@RequestMapping("/eccShow/eclasscard/standard")
public class EccStudentSpaceStandardAction extends BaseAction{

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private EccHonorService eccHonorService;
	@Autowired
	private EccHonorToService eccHonorToService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private OfficeAttanceRemoteService officeAttanceRemoteService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	@Autowired
	private StutotalityRemoteService stutotalityRemoteService;
	
	private static OpenApiOfficeService openApiOfficeService;
    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
        }
        return openApiOfficeService;
    }
    
    @RequestMapping("/studentSpace/login")
    @ControllerInfo("学生空间登录界面")
	public String studentSpaceLogin(String cardId,String view,String studentId,ModelMap map){
    	map.put("hasStu", false);
    	if(StringUtils.isNotBlank(studentId)){
    		Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
    		if(student!=null){
    			map.put("hasStu", true);
    			map.put("studentId", studentId);
    			map.put("stuName", student.getStudentName());
    		}
    	}
    	EccInfo eccInfo = eccInfoService.findOne(cardId);
    	EccOtherSet eccOtherSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_4);
    	if (eccOtherSet!=null) {
    		map.put("loginType", eccOtherSet.getNowvalue());
    	} else {
    		map.put("loginType", 1);
    	}
		if (EccConstants.ECC_VIEW_2.equals(view)) {
			return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceLogin.ftl";
		} else {
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceLogin.ftl";
		}
	}
    
    @ResponseBody
	@RequestMapping("/stuLoginUser/page")
	@ControllerInfo("学生空间登录验证")
	public String stuLoginUser(String type, String cardId, String cardNumber, String studentId, String password, HttpSession httpSession, ModelMap map){
    	// 根据卡号号查找
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		Student student = null;
		if(StringUtils.isNotBlank(studentId)){
			student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			if (student != null && StringUtils.isNotBlank(cardNumber) && Objects.equals(student.getCardNumber(), cardNumber)) {
				return error("学生名与卡号不一致！");
			}
		}
		if (student == null && StringUtils.isNotBlank(cardNumber)) {
			student = SUtils.dt(studentRemoteService.findByCardNumber(eccInfo.getUnitId(), cardNumber), new TR<Student>() {});
		}
		if (student == null) {
			return error("找不到此学生！");
		}
		User user = SUtils.dc(userRemoteService.findByOwnerId(student.getId()),User.class);
		if (user == null) {
			return error("找不到此用户！");
		}
		if (StringUtils.isNotBlank(type)) {
			// 开发模式不校验验证码
			initLoginInfo(httpSession, user);
			return success("登录成功");
		}
		String pwd = user.getPassword();
		if (StringUtils.length(pwd) == 64) {
			pwd = PWD.decode(pwd);
		}
		if (StringUtils.equalsIgnoreCase(pwd, password)) {
			// 开发模式不校验验证码
			initLoginInfo(httpSession, user);
			return success("登录成功");
		} else {
			return error("密码错误！");
		}
	}
    
    private void initLoginInfo(HttpSession httpSession, User user) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setOwnerType(user.getOwnerType());
        loginInfo.setUnitId(user.getUnitId());
        loginInfo.setUserId(user.getId());
        loginInfo.setOwnerId(user.getOwnerId());
        loginInfo.setUserName(user.getUsername());
        loginInfo.setRealName(user.getRealName());
        loginInfo.setUnitClass(2);
        ServletUtils.setLoginInfo(httpSession, loginInfo);
    }
    
    @RequestMapping("/studentSpace/index")
    @ControllerInfo("学生空间主页")
	public String studentSpaceIndex(String cardId,String toleaveMsg,String view,ModelMap map){
		String userId=getLoginInfo().getUserId();
		int tabType = 1;
		if(StringUtils.isNotBlank(toleaveMsg) && "1".equals(toleaveMsg)){
			tabType = 6;
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		if(user!=null){	
			String studentId=user.getOwnerId();
			Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			if(student!=null){
				map.put("studentName", student.getStudentName());
				String time = DateUtils.date2StringByMinute(student.getModifyTime());
				if(time==null)time = DateUtils.date2StringByMinute(new Date());
				map.put("showPicUrl", EccUtils.showPictureUrl(student.getFilePath(), student.getSex(),time));
			}
		}
		map.put("userId", userId);
		map.put("tabType", tabType);
		Set<String> unitSet = officeAttanceRemoteService.getAttUnitIds();
		boolean healthData = false;
		if (CollectionUtils.isNotEmpty(unitSet)) {
			healthData = unitSet.stream().anyMatch(unit -> unit.equals(getLoginInfo().getUnitId()));
		}
		map.put("healthData", healthData);
		boolean showZHPJ=false;
		if("1".equals(EccNeedServiceUtils.getEClassCardShowZHPJ(getLoginInfo().getUnitId()))) {
			showZHPJ=true;
		}
		map.put("showZHPJ", showZHPJ);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceIndex.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceIndex.ftl";
		}
	}
    
	@RequestMapping("/stuSchedule/page")
	@ControllerInfo("学生空间-我的课表")
	public String stuCourseSchedule(String userId,String view,ModelMap map){
		User user = User.dc(userRemoteService.findOneById(userId, true));
        int type = 2;
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        if(semester == null){
        	map.put("nothing", "true");
			if(EccConstants.ECC_VIEW_2.equals(view)){
		    	return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceSchedule.ftl";
			}else{
				return "/eclasscard/standard/show/studentspace/eccStudentSpaceSchedule.ftl";
			}
        }
        Calendar calendar = Calendar.getInstance();
        DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
        if(dateInfo == null){
        	map.put("nothing", "true");
			if(EccConstants.ECC_VIEW_2.equals(view)){
		    	return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceSchedule.ftl";
			}else{
				return "/eclasscard/standard/show/studentspace/eccStudentSpaceSchedule.ftl";
			}
        }
        map.put("today", dateInfo.getWeekday());
        //已经填充 班级名称
        List<CourseSchedule> tcs = SUtils.dt(courseScheduleRemoteService.findCourseScheduleListByPerId
        		(semester.getAcadyear(), semester.getSemester(), dateInfo.getWeek(), user.getOwnerId(), type + ""), CourseSchedule.class);
        CourseSchedule dule=new CourseSchedule();

        //获取 早中晚 上课节数
        Student student = SUtils.dt(studentRemoteService.findOneById(user.getOwnerId()), new TR<Student>() {});
        Clazz stuClazz = SUtils.dt(classRemoteService.findOneById(student.getClassId()),new TR<Clazz>() {});
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(stuClazz.getGradeId()),new TR<Grade>() {});  
        Map<String, Integer> intervalMap = new LinkedHashMap<String, Integer>();
        int mornPeriods=grade.getMornPeriods() == null ? 0 : grade.getMornPeriods();;
        int amPeriods=grade.getAmLessonCount() == null ? 0 : grade.getAmLessonCount();
        int pmPeriods=grade.getPmLessonCount() == null ? 0 : grade.getPmLessonCount();
        int nightPeriods=grade.getNightLessonCount() == null ? 0 : grade.getNightLessonCount();
        int allCourseNum=mornPeriods+amPeriods+pmPeriods+nightPeriods;
        intervalMap.put(PERIOD_MORN,mornPeriods);
        intervalMap.put(PERIOD_AM,amPeriods);
        intervalMap.put(PERIOD_PM,pmPeriods);
        intervalMap.put(PERIOD_NIGHT,nightPeriods);

        Map<String, List<CourseSchedule>> csMap = new HashMap<String, List<CourseSchedule>>();
        Set<String> subjectIds = EntityUtils.getSet(tcs, cs -> cs.getSubjectId());
        List<Course> coureList=SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>(){});
        Map<String,String> courseNameMap=new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(coureList)){
        	for(Course course:coureList){
        		courseNameMap.put(course.getId(), course.getShortName());
        	}
        }
        
        for (CourseSchedule tc : tcs) {
            int period = tc.getPeriod();
            String periodInverval = tc.getPeriodInterval();
            tc.setSubjectName(courseNameMap.get(tc.getSubjectId()));
            if ( periodInverval.equals(PERIOD_MORN) ) {
                if ( intervalMap.get(PERIOD_MORN) == 0 ) {
                    continue;
                }
            }
            if ( periodInverval.equals(PERIOD_AM) ) {
                if ( intervalMap.get(PERIOD_AM) == 0 ) {
                    continue;
                } else {
                    period = period + intervalMap.get(PERIOD_MORN);
                }
            }
            if ( periodInverval.equals(PERIOD_PM) ) {
                if ( intervalMap.get(PERIOD_PM) == 0 ) {
                    continue;
                } else {
                    period = period + intervalMap.get(PERIOD_MORN) + intervalMap.get(PERIOD_AM);
                }
            }
            if ( periodInverval.equals(PERIOD_NIGHT) ) {
                if ( intervalMap.get(PERIOD_NIGHT) == 0 ) {
                    continue;
                } else {
                    period = period + intervalMap.get(PERIOD_MORN) + intervalMap.get(PERIOD_AM) + intervalMap.get(PERIOD_PM);
                }
            }
            List<CourseSchedule> ts = csMap.get(period+"");
            if (ts == null) {
                ts = new ArrayList<CourseSchedule>();
            }
            ts.add(tc);
            csMap.put(period+"", ts);
        }
        JSONObject values = new JSONObject();
        Set<Integer> dayOfWeekSets = Sets.newHashSet();
        for (int i = 1; i < allCourseNum+1; i++) {
        	JSONObject course = new JSONObject();
        	List<CourseSchedule> ts = csMap.get(i + "");
        	if (ts == null)
                continue;
        	for (CourseSchedule t : ts) {
                String weekday = String.valueOf(t.getDayOfWeek() + 1);
                dayOfWeekSets.add(t.getDayOfWeek());
                course.put("courseName" + weekday, t.getSubjectName());
                course.put("className" + weekday, t.getClassName());
                String clazz=t.getClassName();
                course.put("className" + weekday, clazz == null ? "" : clazz);
                course.put("placeName" + weekday, t.getPlaceName());
            }
            values.put("p" + i, course);
        }
        map.put("weekEnd",dayOfWeekSets.containsAll(new HashSet<Integer>(){{add(new Integer(5));add(new Integer(6));}}));
        map.put("values", values);
        map.put("intervalMap",intervalMap);
        map.put("allCourseNum", allCourseNum);
        if(EccConstants.ECC_VIEW_2.equals(view)){
        	return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceSchedule.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceSchedule.ftl";
		}
	}
    
	protected int getIntValue(Integer integer) {
		return getIntValue(integer, 0);
	}
	
	protected int getIntValue(Integer integer, int defaultValue) {
		if (integer != null) {
			return integer.intValue();
		}
		return defaultValue;
	}
	
	@RequestMapping("/studentSpace/stuhonor")
	@ControllerInfo("学生空间-我的荣誉")
	public String stuScheduleStuhonor(String userId,String view,ModelMap map){
		User user = User.dc(userRemoteService.findOneById(userId, true));
		Student student = SUtils.dt(studentRemoteService.findOneById(user.getOwnerId()), new TR<Student>() {});
		List<EccHonorTo> eccHonorTos = eccHonorToService.findByObjectIdIn(new String[]{student.getId()});
		List<EccHonor> eccHonors = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(eccHonorTos)) {
			List<String> honorIds = EntityUtils.getList(eccHonorTos, ht -> ht.getHonorId());
			eccHonors = eccHonorService.findByIdsDesc(honorIds.toArray(new String[0]));
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(honorIds.toArray(new String[0])),new TR<List<Attachment>>(){});
			Map<String,Attachment> pictureMap = EntityUtils.getMap(attachments, att -> att.getObjId());
			Attachment attachment = null;
			String smallFilePath = "";
			for (EccHonor eccHonor : eccHonors) {
				eccHonor.setStudentName(student.getStudentName());
				attachment = pictureMap.get(eccHonor.getId());
				eccHonor.setAttachmentId(attachment.getId());
				smallFilePath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), EccConstants.ECC_SMALL_IMG);
				attachment.setFilePath(smallFilePath);
				eccHonor.setPictureUrl(attachment.getShowPicUrl());
			}
		}
		map.put("eccHonors", eccHonors);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/studentspace/eccStudentHonor.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentHonor.ftl";
		}
	}
	
	@RequestMapping("/studentSpace/stuleave")
	@ControllerInfo("学生空间-我的请假")
	public String studentSpaceLeave(String userId,String view,ModelMap map){
		map.put("userId", userId);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceLeaveIndex.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceLeaveIndex.ftl";
		}
	}
	
	@RequestMapping("/studentSpace/showleave")
	@ControllerInfo("学生空间-请假查询")
	public String studentSpaceShowLeave(String userId,String view,String leaveType,ModelMap map){
		if(StringUtils.isBlank(userId)){
			userId=getLoginInfo().getUserId();
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		List<StudentLeaveDto> dtolist = new ArrayList<StudentLeaveDto>();
		if(user!=null){	
			String studentId=user.getOwnerId();
			Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			map.put("studentName", student.getStudentName());
			String classId = student.getClassId();
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			map.put("className", cls.getClassNameDynamic());
			String jsonDormStr = stuworkRemoteService.findbuildRoomByStudentId(studentId, student.getSchoolId(), null, null);
			StuDormInfoDto dormInfoDto = SUtils.dc(jsonDormStr,StuDormInfoDto.class);
			if(dormInfoDto != null && StringUtils.isNotBlank(dormInfoDto.getBuildingName())&&StringUtils.isNotBlank(dormInfoDto.getRoonName())){
				map.put("room", dormInfoDto.getBuildingName()+"#"+dormInfoDto.getRoonName());
			}else{
				map.put("room", "");
			}
			String jsonStr = getOpenApiOfficeService().getStuLeaveInfo(user.getUnitId(), studentId);
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			if(jsonArray != null&&jsonArray.size()>0){
	    		StudentLeaveDto dto = null;
	    		for(int i = 0;i<jsonArray.size();i++){
	    			dto =new StudentLeaveDto();
	    			JSONObject jsonParam = jsonArray.getJSONObject(i);
	    			StudentLeaveDto.toDto(jsonParam,dto);
	    			dtolist.add(dto);
	    		}
	    	}
	    	map.put("leaveDtos", dtolist);
		}
		map.put("userId", userId);
		School school = SUtils.dc(schoolRemoteService.findOneById(user.getUnitId()), School.class);
		map.put("schoolName", school.getSchoolName());
		map.put("leaveType",leaveType);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceShowLeave.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceShowLeave.ftl";
		}
	}
	
	@RequestMapping("/studentSpace/leaveapply")
	@ControllerInfo("学生空间-请假申请")
	public String studentSpaceLeaveApply(String userId,String view,ModelMap map){
		if(StringUtils.isBlank(userId)){
			userId=getLoginInfo().getUserId();
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		String studentId=user.getOwnerId();
		map.put("studentId", studentId);
		Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		map.put("studentName", student.getStudentName());
		String classId = student.getClassId();
		map.put("classId", classId);
		Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		map.put("className", cls.getClassNameDynamic());
		map.put("schoolId", cls.getSchoolId());
		map.put("gradeId", cls.getGradeId());
		String jsonDormStr = stuworkRemoteService.findbuildRoomByStudentId(studentId, student.getSchoolId(), null, null);
		StuDormInfoDto dormInfoDto = SUtils.dc(jsonDormStr,StuDormInfoDto.class);
		if(dormInfoDto != null && StringUtils.isNotBlank(dormInfoDto.getBuildingName())&&StringUtils.isNotBlank(dormInfoDto.getRoonName())){
			map.put("room", dormInfoDto.getBuildingName()+"#"+dormInfoDto.getRoonName());
		}else{
			map.put("room", "");
		}
		map.put("userId", userId);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceLeaveApply.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceLeaveApply.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/studentSpace/leaveflow")
	@ControllerInfo("学生空间-请假流程")
	public String studentSpaceLeaveFlow(String leaveType,String schoolId,String gradeId,ModelMap map){
		Map<String,String> flowMap = new HashMap<>();
		String jsonStr = getOpenApiOfficeService().getHwStuLeaveFlows(schoolId, gradeId, leaveType);
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);
		if(jsonArray != null&&jsonArray.size()>0){
			for(int i = 0;i<jsonArray.size();i++){
				JSONObject jsonParam = jsonArray.getJSONObject(i);
				if(jsonParam.containsKey("flowId")&&
						jsonParam.containsKey("flowName")){
					flowMap.put(jsonParam.getString("flowId").toString(), jsonParam.getString("flowName").toString());
				}
			}
		}
		String jsonflow = JSONObject.toJSONString(flowMap);
		return jsonflow;
	}

	
	@RequestMapping("/stuQuality/page")
	@ControllerInfo("学生空间-综合素质")
	public String stuQuality(String userId,String view,ModelMap map){
		User user = User.dc(userRemoteService.findOneById(userId, true));
		String studentId=user.getOwnerId();
		List<StutotalityTypeDto> qualityTypeList = SUtils.dt(stutotalityRemoteService.getStutotalitytypeList(studentId), new TR<List<StutotalityTypeDto>>() {});
        map.put("userId", userId);
        map.put("qualityTypeList", qualityTypeList);
        
        if(EccConstants.ECC_VIEW_2.equals(view)){
        	return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceQualityIndex.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceQualityIndex.ftl";
		}
	}
	
	@RequestMapping("/stuQuality/list/page")
	@ControllerInfo("学生空间-综合素质具体成绩")
	public String stuQualityList(String userId,String view,String qualityType,String showTypeKey,ModelMap map){
		User user = User.dc(userRemoteService.findOneById(userId, true));
		String studentId=user.getOwnerId();
		List<StutotalityResultDto> dto=SUtils.dt(stutotalityRemoteService.getStutotalityItemResult(studentId, qualityType),new TR<List<StutotalityResultDto>>() {});
		List<EccStuQualityDto> dtoList=new ArrayList<EccStuQualityDto>();
		EccStuQualityDto eccStuQualityDto=null;
		EccStuQualityDto partDto=null;
		String showTitle="";
		String showOne="0";//不需要底色灰色星星
		if("4".equals(qualityType)) {
			//获奖情况 不显示空
			showOne="1";
		}
		if(CollectionUtils.isNotEmpty(dto)) {
			for(StutotalityResultDto s:dto) {
				eccStuQualityDto=new EccStuQualityDto();
				eccStuQualityDto.setItemkey(s.getItemId());
				eccStuQualityDto.setMainTitle(s.getItemName());
				eccStuQualityDto.setPartList1(new ArrayList<EccStuQualityDto>());
				eccStuQualityDto.setPartList2(new ArrayList<EccStuQualityDto>());
				if(CollectionUtils.isNotEmpty(s.getOptionDtoList1())) {
					for(StutotalityOptionDto ss:s.getOptionDtoList1()) {
						partDto=new EccStuQualityDto();
						partDto.setMainTitle(ss.getOptionName());
						if(ss.getScore()==null) {
							partDto.setScore("");
							partDto.setStarScore("");
						}else {
							partDto.setScore(ss.getScore().toString());
							if(ss.getScore()>=5.5) {
								partDto.setFiveBetter("1");
							}else {
								partDto.setFiveBetter("0");
							}
							partDto.setStarScore(makeFloat(ss.getScore()).toString());
						}
						partDto.setTimeStr(ss.getTimeStr());
						partDto.setDescription(ss.getDescription());
						eccStuQualityDto.getPartList1().add(partDto);
					}
				}
				if(CollectionUtils.isNotEmpty(s.getOptionDtoList2())) {
					for(StutotalityOptionDto ss:s.getOptionDtoList2()) {
						partDto=new EccStuQualityDto();
						partDto.setMainTitle(ss.getOptionName());
						if(ss.getScore()==null) {
							partDto.setScore("");
							partDto.setStarScore("");
						}else {
							partDto.setScore(ss.getScore().toString());
							if(ss.getScore()>=5.5) {
								partDto.setFiveBetter("1");
							}else {
								partDto.setFiveBetter("0");
							}
							
							partDto.setStarScore(makeFloat(ss.getScore()));
						}
						eccStuQualityDto.getPartList2().add(partDto);
					}
				}
				dtoList.add(eccStuQualityDto);
				
				if(StringUtils.isNotBlank(showTypeKey) && eccStuQualityDto.getItemkey().equals(showTypeKey)) {
					showTitle=showTypeKey;
				}
			}
		}
		map.put("dtoList", dtoList);
		map.put("showTitleKey", showTitle);
		map.put("showOne", showOne);
        if(EccConstants.ECC_VIEW_2.equals(view)){
        	return "/eclasscard/standard/verticalshow/studentspace/eccStudentSpaceQualityList.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/eccStudentSpaceQualityList.ftl";
		}
	}
	//取星星显示
	public String makeFloat(float value) {
		//取整
		int num=(int)value;
		//默认最大10颗星
		if(num>=10) {
			return "10";
		}
		if((0.5+num)<=value) {
			return num+".5";
		}else {
			return ""+num;
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping("/stuQuality/saveStudentScore")
	public String saveStuQuality(String userId,String qualityCodeId) {
		//根据qualityCodeId 判断code有没有 或者被扫了
		//返回二维码信息 提示保存成功
		User user = User.dc(userRemoteService.findOneById(userId, true));
		String studentId=user.getOwnerId();
		StutotalityTypeDto dto=SUtils.dc(stutotalityRemoteService.saveCode(studentId, qualityCodeId),StutotalityTypeDto.class);
		JSONObject obj=new JSONObject();
		obj.put("status", dto.getStatus());
		obj.put("msg", dto.getMsg()==null?"":dto.getMsg());
		obj.put("itemType", dto.getType()==null?"":dto.getType());
		obj.put("itemKey", dto.getItemId()==null?"":dto.getItemId());
		obj.put("codeName", dto.getItemName()==null?"":dto.getItemName());
		obj.put("score", dto.getScore()==null?"":makeFloat(dto.getScore()));
		return obj.toJSONString();
	}
	
	
}
