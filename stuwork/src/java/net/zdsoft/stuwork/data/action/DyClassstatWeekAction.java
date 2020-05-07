package net.zdsoft.stuwork.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dto.DyClassStatListDto;
import net.zdsoft.stuwork.data.dto.DyWeekCheckResultDto;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultSubmitService;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleUserService;

@Component
@Controller
@RequestMapping("/stuwork")
public class DyClassstatWeekAction  extends BaseAction{
	@Autowired
	private DyWeekCheckRoleUserService dyWeekCheckRoleUserService;
	@Autowired
	private DyClassstatWeekService dyClassstatWeekService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
    private DyWeekCheckItemService dyWeekCheckItemService;
	@Autowired
    private DyCourseRecordService dyCourseRecordService;
	@Autowired
    private DyWeekCheckResultService dyWeekCheckResultService;
	@Autowired
    private DyWeekCheckResultSubmitService dyWeekCheckResultSubmitService;
	
	@Scheduled(cron="0 0 2 * * SUN") //每周日凌晨2点执行
    public void taskCycle(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(new Date()));
		//统计一周的数据
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);
		if(unit == null){
			System.out.println("找不到顶级单位：统计结束！");
			return;
		}
		//获得所有下属学校
		List<Unit> units = SUtils.dt(unitRemoteService.findByUnionId(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
		if(CollectionUtils.isEmpty(units)){
			System.out.println(unit.getUnitName()+"没有下属学校，统计结束！");
		}
		try {
			for(Unit u : units){
				System.out.println(u.getUnitName()+"开始统计...");
				Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1, u.getId()), Semester.class);
				if(se == null){
					System.out.println(u.getUnitName()+"学年学期设置与当前时间不匹配，无法统计...");
					continue;
				}
				//计算要统计的周次
				Date today = new Date();
				Date yesterday = new Date(today.getTime() - 86400000L);//86400000L，它的意思是说1天的时间=24小时 x 60分钟 x 60秒 x 1000毫秒 单位是L。
				today = sdf.parse(sdf.format(yesterday));
				DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(u.getId(), se.getAcadyear(), se.getSemester(), today), DateInfo.class);
				if(dateInfo == null){
					System.out.println(u.getUnitName()+"上周没有节假日设置,不统计...");
					continue;
				}
				dyClassstatWeekService.statByUnitId(u.getId(), se.getAcadyear(), se.getSemester()+"", dateInfo.getWeek());
				System.out.println(u.getUnitName()+"统计结束...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("统计出错...");
		}
        System.out.println("统计完成");  
    }  
	@ResponseBody
	@RequestMapping("/classStart/classStat/save")
	@ControllerInfo(value = "统计")
	public String classStat(String acadyear,String semester,Integer week,HttpServletRequest request,ModelMap map){
		//先判断所选周次是否已经结束，如果没结束则不能统计
		System.out.println("统计开始......");
		String unitId = this.getLoginInfo().getUnitId();
		List<DateInfo> dateInfos = SUtils.dt(dateInfoRemoteService.findByWeek(unitId, acadyear, NumberUtils.toInt(semester), week), new TR<List<DateInfo>>(){});
		if(CollectionUtils.isEmpty(dateInfos)){
			return error("找不到"+acadyear + "学年第"+semester+"学期第"+week+"周的节假日信息，无法统计！");
		}
//		if(net.zdsoft.framework.utils.DateUtils.compareForDay(dateInfos.get(dateInfos.size()-1).getInfoDate(), new Date())>=0){
//			return error("所选学年学期周次还没有结束或开始，无法统计！");
//		}
		try {
			dyClassstatWeekService.statByUnitId(unitId,acadyear,semester,week);
		} catch (Exception e) {
			return error("统计失败");
		}
		System.out.println("统计结束......");
		return success("操作成功");
	}
	
	@RequestMapping("/classStart/list/page")
	@ControllerInfo(value = "统计列表")
	public String classStartList(HttpServletRequest request,ModelMap map) {
		String classId = request.getParameter("classId");
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classId), new TR<List<Clazz>>(){});
		String className = clazzs.get(0).getClassNameDynamic();
		int week = NumberUtils.toInt(request.getParameter("week"));
		List<DyClassStatListDto> resultDto = dyClassstatWeekService.findClassTableDto(getLoginInfo().getUnitId(),acadyear,semester,classId,week);
		map.put("dtos", resultDto);
		map.put("className", className);
		DyClassstatWeek statWeek = dyClassstatWeekService.findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(getLoginInfo().getUnitId(),acadyear,semester,classId, week);
		if(statWeek == null){
			map.put("isHealthExcellen", false);
			map.put("isDisciplineExcellen", false);
			map.put("disciplineRank", 0);
			map.put("healthRank", 0);
			map.put("disciplineScore", 0f);
			map.put("healthScore", 0f);
		}else{
			map.put("isHealthExcellen", statWeek.getIsHealthExcellen());
			map.put("isDisciplineExcellen", statWeek.getIsDisciplineExcellen());
			map.put("disciplineScore", statWeek.getDisciplineScore());
			map.put("healthScore", statWeek.getHealthScore());
			map.put("disciplineRank", statWeek.getDisciplineRank());
			map.put("healthRank", statWeek.getHealthRank());
		}
		return "/stuwork/classStat/statIndexList.ftl";
	}
	
	@RequestMapping("/classStart/index/page")
	@ControllerInfo(value = "统计表admin")
	public String classStartIndex(HttpServletRequest request,ModelMap map) {
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		String userId = info.getUserId();
		String acadyear  =request.getParameter("acadyear");
		Integer semester  =NumberUtils.toInt(request.getParameter("semester"));
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        int week = 0;
        if(StringUtils.isBlank(acadyear)){
        	Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId), Semester.class);
        	if(se == null){
            	se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            }else{
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            	DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, semester, new Date()), DateInfo.class);
            	if(dateInfo != null){
            		week =dateInfo.getWeek();
            		map.put("week", week);
            	}
            }
        }
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("acadyear", acadyear);
        
        List<DateInfo> dateInfos = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, semester), new TR<List<DateInfo>>(){});
        if(CollectionUtils.isEmpty(dateInfos)){
        	return errorFtl(map,"请先维护该学年学期下的节假日信息");
        }
        int maxWeek = 0;
        for(DateInfo dateInfo : dateInfos){
        	if(dateInfo.getWeek() > maxWeek){
        		maxWeek = dateInfo.getWeek();
        	}
        }
        map.put("maxWeek", maxWeek);
        if(week == 0){
        	map.put("week", 1);
        }
		//判断是否是总管理员
		DyWeekCheckRoleUser role = dyWeekCheckRoleUserService.findByRoleTypeAndUser(unitId,DyWeekCheckRoleUser.CHECK_ADMIN, userId);
		if(role == null){
			map.put("hasAdmin", "0");
		}else{
			map.put("hasAdmin", "1");
		}
		return "/stuwork/classStat/statIndex.ftl";
	}
	
	@ControllerInfo("周汇总表首页")
	@RequestMapping("/weekStat/index/page")
	public String weekIndex(HttpServletRequest request, ModelMap map) {
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		String acadyear  =request.getParameter("acadyear");
		Integer semester  =NumberUtils.toInt(request.getParameter("semester"));
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        int week = 0;
        if(StringUtils.isBlank(acadyear)){
        	Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId), Semester.class);
        	if(se == null){
            	se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            }else{
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            	DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, semester, new Date()), DateInfo.class);
            	if(dateInfo != null){
            		week =dateInfo.getWeek();
            		map.put("week", week);
            	}
            }
        }
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("acadyear", acadyear);
        
        List<DateInfo> dateInfos = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, semester), new TR<List<DateInfo>>(){});
        if(CollectionUtils.isEmpty(dateInfos)){
        	return errorFtl(map,"请先维护该学年学期下的节假日信息");
        }
        int maxWeek = 0;
        for(DateInfo dateInfo : dateInfos){
        	if(dateInfo.getWeek() > maxWeek){
        		maxWeek = dateInfo.getWeek();
        	}
        }
        map.put("maxWeek", maxWeek);
        if(week == 0){
        	map.put("week", 1);
        }
		return "/stuwork/weekStat/statIndex.ftl";
	}
	
	@ControllerInfo("周汇总表班级周汇总列表")
	@RequestMapping("/weekStat/list/page")
	public String weekList(HttpServletRequest request, ModelMap map) {
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		String acadyear =request.getParameter("acadyear");
		int semester = NumberUtils.toInt(request.getParameter("semester"));
		int week = NumberUtils.toInt(request.getParameter("week"));
		List<Clazz> clsList = Clazz.dt(classRemoteService.findBySchoolIdCurAcadyear(unitId, acadyear));
//				.stream()
//				.sorted((a, b) -> a.getSection() == b.getSection()
//						? (StringUtils.trimToEmpty(a.getClassCode()).compareTo(b.getClassCode()))
//						: (a.getSection() - b.getSection()))
//				.collect(Collectors.toList());
		if(CollectionUtils.isEmpty(clsList)) {
			return "/stuwork/weekStat/weekList.ftl";
		}
		Set<String> gradeIds = EntityUtils.getSet(clsList, Clazz::getGradeId);
		Map<String, String> gradeNameMap = EntityUtils.getMap(
				SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>() {
				}), Grade::getId, Grade::getGradeName);
		Set<String> clsIds = EntityUtils.getSet(clsList, Clazz::getId);
		Map<String, DyClassstatWeek> clsStatWkMap = EntityUtils.getMap(dyClassstatWeekService.findBySchoolIdAndAcadyearAndSemesterAndWeek(unitId, acadyear, semester+"", week), DyClassstatWeek::getClassId);
		//获得考核项信息
		List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolId(unitId);
		//先取到班级日志数据
		Map<String,Float> classScoreMap = new HashMap<String, Float>();
		Map<String,String> stuRecordRemarkMap = new HashMap<String, String>();
//		List<DyCourseStudentRecord> stuRecords = dyCourseStudentRecordService.findByWeekAndInClassId(unitId,acadyear,semester+"",week,clsIds.toArray(new String[0]));
//		Set<String> recordIds = EntityUtils.getSet(stuRecords, DyCourseStudentRecord::getRecordId);
//		List<DyCourseRecord> records = dyCourseRecordService.findListByIdIn(recordIds.toArray(new String[0]));
		List<DyCourseRecord> records = dyCourseRecordService.findListByRecordClassIds(unitId, acadyear, semester+"", week, clsIds.toArray(new String[0]));
//		List<Integer> skDays = new ArrayList<>();
		for(DyCourseRecord re : records){
//			if(StuworkConstants.COURSERECORD_SK_TYPE.equals(re.getType())) {
//				if(!skDays.contains(re.getDay())) {
//					skDays.add(re.getDay());
//				}
//			}
			if(!classScoreMap.containsKey(re.getClassId()+","+re.getType()+","+re.getDay())){
				classScoreMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 0f);
			}
			classScoreMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 
					classScoreMap.get(re.getClassId()+","+re.getType()+","+re.getDay()) +re.getScore());
			
			if (StringUtils.isNotEmpty(re.getRemark())) {
				if (!stuRecordRemarkMap.containsKey(re.getClassId() + "," + re.getType() + "," + re.getDay())) {
					stuRecordRemarkMap.put(re.getClassId() + "," + re.getType() + "," + re.getDay(), "");
				}
				stuRecordRemarkMap.put(re.getClassId() + "," + re.getType() + "," + re.getDay(),
						stuRecordRemarkMap.get(re.getClassId() + "," + re.getType() + "," + re.getDay()) + re.getRemark()
								+ ",");
			}
		}
//		Collections.sort(skDays);
		
		List<DyWeekCheckResultSubmit> sublist = dyWeekCheckResultSubmitService.findByWeek(unitId,acadyear,semester+"",week);
		//key:roleType+","+day
		Map<String,DyWeekCheckResultSubmit> subMap = new HashMap<String, DyWeekCheckResultSubmit>();
		for(DyWeekCheckResultSubmit sub : sublist){
			subMap.put(sub.getRoleType()+","+sub.getDay(), sub);
		}
		//再取到值周考核数据
		Map<String, List<DyWeekCheckResult>> clsResultMap = EntityUtils.getListMap(dyWeekCheckResultService.findByWeekAndInClassId(unitId,acadyear,semester+"",week,clsIds.toArray(new String[0])), DyWeekCheckResult::getClassId, Function.identity());
    	
		Map<String,String> scoreMap = new HashMap<String, String>();
		Map<String,String> remarkMap = new HashMap<String, String>();
		for(Clazz cls : clsList){
			cls.setClassNameDynamic(StringUtils.trimToEmpty(gradeNameMap.get(cls.getGradeId())) + cls.getClassName());
			String classId = cls.getId();
			
			List<DyClassStatListDto> dtolist = new ArrayList<DyClassStatListDto>();
			DyWeekCheckResultDto dto = null;
			DyClassStatListDto listDto  =null;
			for(int i = 1;i<3;i++){
				listDto = new DyClassStatListDto();
				if(i==1){
					listDto.setItemName("上课日志");
				}else{
					listDto.setItemName("晚自习日志");
				}
				listDto.setItemType("3");
				for(int day = 1;day < 8;day++){
					dto = new DyWeekCheckResultDto();
					dto.setDay(day);
					if(classScoreMap.containsKey(classId+","+i+","+day)){
						dto.setScore(classScoreMap.get(classId+","+i+","+day));
					}else{
						dto.setUnCheck(true);
					}
					dto.setRemark(stuRecordRemarkMap.get(classId+","+i+","+day));
					listDto.getDtos().add(dto);
				}
				dtolist.add(listDto);
			}
			
			// 小红旗、排名、总分
	    	DyClassstatWeek statWeek = clsStatWkMap.get(cls.getId());
	    	if(statWeek != null) {
	    		scoreMap.put("wshq"+classId+"8", statWeek.getIsHealthExcellen()?"★":"");
	    		scoreMap.put("wszf"+classId+"8", statWeek.getHealthScore()+"");
	    		scoreMap.put("wsjl"+classId+"8", statWeek.getIsHealthExcellen()?"2":"0");
	    		scoreMap.put("jlhq"+classId+"8", statWeek.getIsDisciplineExcellen()?"★":"");
	    		scoreMap.put("jlzf"+classId+"8", statWeek.getDisciplineScore()+"");
	    		scoreMap.put("jljl"+classId+"8", statWeek.getIsDisciplineExcellen()?"2":"0");
	    	} else {
	    		scoreMap.put("wshq"+classId+"8", "");
	    		scoreMap.put("wszf"+classId+"8", "0");
	    		scoreMap.put("wsjl"+classId+"8", "0");
	    		scoreMap.put("jlhq"+classId+"8", "");
	    		scoreMap.put("jlzf"+classId+"8", "0");
	    		scoreMap.put("jljl"+classId+"8", "0");
	    	}
	    	
	    	List<DyWeekCheckResult> resultlist= clsResultMap.get(cls.getId());
	    	if(resultlist == null) {
	    		resultlist = new ArrayList<>();
	    	}
	    	float dayScore = 0f;
	    	boolean roleAllNoSubmint = true;//一个项目是否所有角色都没有提交
			boolean roleSub = false;//一个角色是否提交
	    	for(DyWeekCheckItem item : items){
				listDto = new DyClassStatListDto();
				listDto.setItemId(item.getId());
				listDto.setItemName(item.getItemName());
				listDto.setItemType(item.getType()+"");
				for(int day = 1;day < 8;day++){
					dto = new DyWeekCheckResultDto();
					dto.setItemId(item.getId());
					dto.setItemName(item.getItemName());
					dto.setItemOrder(item.getOrderId());
					dto.setWeek(week);
					dto.setDay(day);
					dto.setItemType(item.getType());
					if(item.getHasTotalScore() == 1){
						dayScore = item.getTotalScore();
					} else {
						dayScore = 0f;
					}
					//TODO
					if(!item.getDays().contains(day+"")){
						dto.setUnCheck(true);
					} else {
						dto.setUnCheck(false);
						roleAllNoSubmint = true;
						for(DyWeekCheckItemRole itemRole : item.getItemRoles()){
							String remark="";
							roleSub = false;
							//先去结果表中找角色的扣分情况，并计算总分
							for(DyWeekCheckResult result : resultlist){
								if(StringUtils.equals(item.getId(), result.getItemId()) 
										&& StringUtils.equals(itemRole.getRoleType(), result.getRoleType())
										&& result.getDay() == day){
									roleSub = true;
									dayScore = dayScore - result.getScore();
									result.setRoleName(itemRole.getRoleName());
									dto.getResult().add(result);
									if(null!=result.getRemark()){									
										remark=remark+result.getRemark()+",";
									}
								}
							}
							if(StringUtils.isNotBlank(remark)){
								dto.setRemark("（备注："+remark.substring(0, remark.length()-1)+"）");
							}
							//如果结果表中找不到就去提交表中查，如果提交表中有当天改角色的提交记录，
							//则表明该角色对这个班级的这个item不扣分
							if(!roleSub){
								if(!subMap.containsKey(itemRole.getRoleType()+","+day)){
									dto.getUnSubRole().add(itemRole.getRoleName());//记录下没有提交的角色
								} else {
									roleAllNoSubmint = false;
								}
							}else{
								roleAllNoSubmint = false;
							}
						}
						dto.setScore(dayScore);
						if(roleAllNoSubmint){
							dto.setAllUnSubmint(true);
						}else{
							dto.setAllUnSubmint(false);
						}
					}
					listDto.getDtos().add(dto);
				}
				dtolist.add(listDto);
			}
	    	
	    	List<DyClassStatListDto> resultDto1 = new ArrayList<DyClassStatListDto>();
			List<DyClassStatListDto> resultDto2 = new ArrayList<DyClassStatListDto>();
			List<DyClassStatListDto> resultDto3 = new ArrayList<DyClassStatListDto>();
			for(DyClassStatListDto sdto : dtolist){
				if("1".equals(sdto.getItemType())){//卫生
					resultDto1.add(sdto);
				}else if("2".equals(sdto.getItemType())){//纪律
					resultDto2.add(sdto);
				}else{
					resultDto3.add(sdto);
				}
				List<DyWeekCheckResultDto> chkdtoList = sdto.getDtos();
				for(DyWeekCheckResultDto dto2 : chkdtoList){
					if("上课日志".equals(sdto.getItemName()) && "3".equals(sdto.getItemType())){
						if(StringUtils.isNotBlank(dto2.getRemark())){
							remarkMap.put("skrz"+classId+dto2.getDay(), "（备注："+dto2.getRemark().substring(0, dto2.getRemark().length()-1)+"）");
						}
						if(dto2.isAllUnSubmint()){
							scoreMap.put("skrz"+classId+dto2.getDay(), "");
						}else if(dto2.isUnCheck()){
							scoreMap.put("skrz"+classId+dto2.getDay(), "/");	
						}else{
							scoreMap.put("skrz"+classId+dto2.getDay(), dto2.getScore()+"");	
						}
					}else if("晚自习日志".equals(sdto.getItemName()) && "3".equals(sdto.getItemType())){
						if(StringUtils.isNotBlank(dto2.getRemark())){
							remarkMap.put("wzxrz"+classId+dto2.getDay(), "（备注："+dto2.getRemark().substring(0, dto2.getRemark().length()-1)+"）");
						}
						if(dto2.isAllUnSubmint()){
							scoreMap.put("wzxrz"+classId+dto2.getDay(), "");
						}else if(dto2.isUnCheck()){
							scoreMap.put("wzxrz"+classId+dto2.getDay(), "/");	
						}else{
							scoreMap.put("wzxrz"+classId+dto2.getDay(), dto2.getScore()+"");	
						}
					}else{						
						if (StringUtils.isNotEmpty(dto2.getRemark())) {
							remarkMap.put(sdto.getItemId() + classId + dto2.getDay(), dto2.getRemark());
						}
						if(dto2.isAllUnSubmint()){
							scoreMap.put(sdto.getItemId()+classId+dto2.getDay(), "");
						}else if(dto2.isUnCheck()){
							scoreMap.put(sdto.getItemId()+classId+dto2.getDay(), "/");
						}else{
							scoreMap.put(sdto.getItemId()+classId+dto2.getDay(), dto2.getScore()+"");
						}
					}
				}
			}
		}
    	
		getDefaultItems(items);
		Map<String, List<DyWeekCheckResultDto>> typeMap = new HashMap<>();
		int wsCount = 0;
		int jlCount = 0;
		for (int i = 1; i < 3; i++) {
			for(int day = 1;day < 9;day++){
				List<DyWeekCheckResultDto> dtos = new ArrayList<>();
				for (DyWeekCheckItem item : items) {
					if(item.getType() == i && item.getDays().contains(day+"")) {
						DyWeekCheckResultDto itemDto = new DyWeekCheckResultDto();
						itemDto.setDay(day);
						itemDto.setItemName(item.getItemName());
						itemDto.setItemId(item.getId());
						dtos.add(itemDto);
						if(i == 1) {
							wsCount++;
						} else {
							jlCount++;
						}
					}
				}
				if(dtos.size() == 0) {
					if(i == 1) {
						wsCount++;
					} else {
						jlCount++;
					}
				}
				typeMap.put(i+"-"+day, dtos);
			}
		}
		map.put("wsCount", wsCount);
		map.put("jlCount", jlCount);
		map.put("typeMap", typeMap);
		map.put("remarkMap", remarkMap);
		map.put("scoreMap", scoreMap);
    	map.put("clsList", clsList);
		//TODO
		return "/stuwork/weekStat/weekList.ftl";
	}
	
	private void getDefaultItems(List<DyWeekCheckItem> items) {
		String[][] vals = {{"skrz","上课日志",2+"","1,2,3,4,5"},{"wzxrz","晚自习日志",2+"","1,2,3,4,5,6,7"},
				{"wj","违纪",2+"","1,2,3,4,5,6,7"},
				{"wszf","卫生总得分",1+"","8"},{"wshq","红旗",1+"","8"},{"wsjl","奖励",1+"","8"},
				{"jlzf","纪律总得分",2+"","8"},{"jlhq","红旗",2+"","8"},{"jljl","奖励",2+"","8"}};
		for(String[] val : vals) {
			DyWeekCheckItem sk = new DyWeekCheckItem();
			sk.setId(val[0]);
			sk.setItemName(val[1]);
			sk.setType(NumberUtils.toInt(val[2]));
			if(val.length > 3) {
				Set<String> days = new HashSet<>();
				CollectionUtils.addAll(days, val[3].split(","));
				sk.setDays(days);
			}
			items.add(sk);
		}
	}
	
	@ControllerInfo("周汇总表导出")
	@RequestMapping("/weekStat/export")
	public void weekExport(HttpServletRequest request, HttpServletResponse resp, ModelMap map) {
		String acadyear =request.getParameter("acadyear");
		int semester = NumberUtils.toInt(request.getParameter("semester"));
		int week = NumberUtils.toInt(request.getParameter("week"));
		// TODO
		weekList(request, map);
		ExportUtils ex = ExportUtils.newInstance();
		String fileName = acadyear+"学年第"+(semester==1?"一":"二")+"学期第"+week+"周各班教室常规考核一览";
		Map<String, List<String>> fieldTitleMap = new HashMap<>();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
		String sheet=acadyear+"学年第"+(semester==1?"一":"二")+"学期第"+week+"周";
		List<String> titles = new ArrayList<>();
		titles.add("类型");
		titles.add("周次");
		titles.add("考核项目");
		List<Clazz> clsList = (List<Clazz>) map.get("clsList");
		if(CollectionUtils.isEmpty(clsList)) {
			sheet = "没有班级数据";
			fieldTitleMap.put(sheet, titles);
			ex.exportXLSFile(fileName, fieldTitleMap, sheetName2RecordListMap, resp);
			return;
		}
		for(Clazz cls : clsList) {
			titles.add(cls.getClassNameDynamic());
		}
		fieldTitleMap.put(sheet, titles);
		Map<String, List<DyWeekCheckResultDto>> typeMap = (Map<String, List<DyWeekCheckResultDto>>) map.get("typeMap");
		if(typeMap == null) {
			typeMap = new HashMap<>();
		}
		Map<String,String> scoreMap = (Map<String, String>) map.get("scoreMap");
		if(scoreMap == null) {
			scoreMap = new HashMap<>();
		}
		Map<String,String> remarkMap = (Map<String, String>) map.get("remarkMap");
		if(remarkMap == null) {
			remarkMap = new HashMap<>();
		}
		String[][] days = {{"7","周日"},{"1","周一"},{"2","周二"},{"3","周三"},{"4","周四"},{"5","周五"},{"6","周六"},{"8",""}};
		List<Map<String, String>> rows = new ArrayList<>();
		for(int i=1;i<3;i++) {
			for(String[] day : days){
				List<DyWeekCheckResultDto> wdtos = typeMap.get(i+"-"+day[0]);
				if(CollectionUtils.isEmpty(wdtos)) {
					Map<String, String> valMap = new HashMap<>();
					for(int j=0;j<titles.size();j++) {
						String val = "";
						if(j==0) {
							if(i==1) {
								val = "卫生情况";
							} else {
								val = "纪律情况";
							}
						} else if(j == 1) {
							val = day[1];
						}
						valMap.put(titles.get(j), val);
					}
					rows.add(valMap);
				} else {
					for(DyWeekCheckResultDto wd : wdtos) {
						Map<String, String> valMap = new HashMap<>();
						if (i == 1) {
							valMap.put("类型", "卫生情况");
						} else if(i==2) {
							valMap.put("类型", "纪律情况");
						}
						valMap.put("周次", day[1]);
						valMap.put("考核项目", wd.getItemName());
						for(Clazz cls : clsList) {
							valMap.put(cls.getClassNameDynamic(), StringUtils.trimToEmpty(scoreMap.get(wd.getItemId()+cls.getId()+day[0]))
									+StringUtils.trimToEmpty(remarkMap.get(wd.getItemId()+cls.getId()+day[0])));
						}
						rows.add(valMap);
					}
				}
			}
		}
		sheetName2RecordListMap.put(sheet, rows);
		ex.exportXLSFile(fileName, fieldTitleMap, sheetName2RecordListMap, resp);
	}
}
