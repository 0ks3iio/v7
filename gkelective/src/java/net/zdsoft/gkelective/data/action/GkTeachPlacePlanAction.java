package net.zdsoft.gkelective.data.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.GkSubjectDto;
import net.zdsoft.gkelective.data.dto.GkTeachPlacePlanDto;
import net.zdsoft.gkelective.data.dto.GoClassSearchDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachClassExService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 选择方案部分
 */
@Controller
@RequestMapping("/gkelective/{arrangeId}")
public class GkTeachPlacePlanAction extends BaseAction{
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkTeachPlacePlanService gkTeachPlacePlanService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private GkRoundsService gKRoundsService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private GkTimetableLimitArrangService gkTimetableLimitArrangService;
	@Autowired
	private GkSubjectService gkSubjectService;
	@Autowired
	private GkBatchService gkBatchService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private GkRelationshipService gkRelationshipService;
	@Autowired
	private GkTeachClassExService gkTeachClassExService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@RequestMapping("/arrangePlan/index/page")
    @ControllerInfo(value = "老师教师安排--方案列表")
	public String gkPlansList(@PathVariable String arrangeId,ModelMap map){
		GkSubjectArrange gkSubArr = gkSubjectArrangeService.findArrangeById(arrangeId);
		if(gkSubArr==null){
			return errorFtl(map,"对应选课项目已经不存在");
		}
		List<GkTeachPlacePlan> plansList=gkTeachPlacePlanService.findBySubjectArrangeId(arrangeId,true);
		makeTimerStr(plansList);
		map.put("plansList", plansList);
		map.put("arrangeId", arrangeId);
		map.put("gkSubArr", gkSubArr);
		Date nowDate=new Date();
		if(CollectionUtils.isNotEmpty(plansList)){
			for(GkTeachPlacePlan p:plansList){
				//如果当前时间在之前中间 不能新增 
				if(DateUtils.compareForDay(p.getStartTime(), nowDate)<0){
					//中间
					p.setCanDelete(false);
				}else{
					p.setCanDelete(true);
				}
			}
		}
		
		return "/gkelective/arrangePlan/planList.ftl";
	}
	
	private void makeTimerStr(List<GkTeachPlacePlan> plansList){
		if(CollectionUtils.isNotEmpty(plansList)){
			for(GkTeachPlacePlan plan :plansList){
				plan.setTimeStr(toMakeTime(plan.getWeekOfWorktime(),plan.getDayOfWeek()));
				plan.setTimeStr2(toMakeTime(plan.getWeekOfWorktime2(),plan.getDayOfWeek2()));
			}
		}
	}
	
	private String toMakeTime(int weekOfWorktime,int dayOfWeek){
		String returnStr="第"+weekOfWorktime+"周";
		returnStr=returnStr+","+makeWeekName(dayOfWeek);
		return returnStr;
	}

	@RequestMapping("/arrangePlan/edit/page")
    @ControllerInfo(value = "新增方案")
	public String gkPlanEdit(@PathVariable String arrangeId,ModelMap map){
		GkSubjectArrange gkSubArr = gkSubjectArrangeService.findArrangeById(arrangeId);
		if(gkSubArr==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/index/page","#showList");
			return errorFtl(map,"对应选课项目已经不存在");
		}
		Date nowDate=new Date();
		
		List<GkTeachPlacePlan> plansList=gkTeachPlacePlanService.findBySubjectArrangeId(arrangeId,true);
		//如果当前时间在之前中间 不能新增 
		if(CollectionUtils.isNotEmpty(plansList)){
			for(GkTeachPlacePlan g:plansList){
				if(DateUtils.compareForDay(g.getStartTime(), nowDate)<=0 && DateUtils.compareForDay(g.getEndTime(), nowDate)>=0){
					//中间
					addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
					return errorFtl(map,"存在还在执行的方案，不能新增");
				}else if(DateUtils.compareForDay(g.getStartTime(), nowDate)>0){
					//后面
					addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
					return errorFtl(map,"存在还在执行的方案，不能新增");
				}

			}
		}
		//List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){});
		
		List<Semester> semesterList= SUtils.dt(semesterService.findListByDate(new Date()),new TR<List<Semester>>(){});
        if(CollectionUtils.isEmpty(semesterList)){
			return errorFtl(map,"学年学期不存在");
		}
        List<String> acadyearList=new ArrayList<String>();
        for(Semester s:semesterList){
        	if(!acadyearList.contains(s.getAcadyear())){
        		acadyearList.add(s.getAcadyear());
        	}
        }
        map.put("acadyearList", acadyearList);
		Semester semesterObj = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(semesterObj!=null){
			String acadyear=semesterObj.getAcadyear();
			String semester=semesterObj.getSemester()+"";
			map.put("acadyear", acadyear);
		    map.put("semester", semester);
		    String startDate = "";
			try {
				Date workBegin = sdf.parse(sdf.format(semesterObj.getSemesterBegin()));
				Date startBegin = sdf.parse(sdf.format(new Date()));
				if(DateUtils.compareForDay(workBegin, startBegin)>0){
					startDate = sdf.format(workBegin);
				}else{
					startDate = sdf.format(startBegin);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    map.put("startDate", startDate);
		    String endDate = sdf.format(semesterObj.getSemesterEnd());
		    map.put("endDate", endDate);
		}else{
			map.put("acadyear", "");
		    map.put("semester", "");
		    map.put("startDate", "");
		    map.put("endDate", "");
		}
		//轮次
		List<GkRounds> allroundslist = gKRoundsService.findBySubjectArrangeId(arrangeId,null);
		//排除没走完的
		List<GkRounds> roundslist=new ArrayList<GkRounds>();
		if(CollectionUtils.isNotEmpty(allroundslist)){
			for(GkRounds g:allroundslist){
				if(g.getStep()>=GkElectveConstants.STEP_5){
					roundslist.add(g);
				}
			}
		}
		map.put("roundslist", roundslist);
		map.put("arrangeId", arrangeId);
		map.put("gkSubArr", gkSubArr);
		return "/gkelective/arrangePlan/planEdit.ftl";
	}
	@RequestMapping("/arrangePlan/editHead")
    @ControllerInfo(value = "查询")
	public String gkPlanEditHead(@PathVariable String arrangeId,String roundsId,ModelMap map){
		GkRounds rounds = gKRoundsService.findOne(roundsId);
		map.put("rounds", rounds);
		return "/gkelective/arrangePlan/planEditHead.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/arrangePlan/save")
    @ControllerInfo(value = "保存")
	public String savePlan(@PathVariable String arrangeId,GkTeachPlacePlan plan,ModelMap map){
		try {
			GkSubjectArrange arrange = gkSubjectArrangeService.findArrangeById(arrangeId);
			if(arrange==null){
				return error("对应选课项目已经不存在，保存失败！");
			}
			if(plan==null){
				return error("保存失败！");
			}
			Date nowDate=new Date();
			
			List<GkTeachPlacePlan> plansList=gkTeachPlacePlanService.findBySubjectArrangeId(arrangeId,true);
			//如果当前时间在之前中间 不能新增 
			if(CollectionUtils.isNotEmpty(plansList)){
				for(GkTeachPlacePlan g:plansList){
					if(DateUtils.compareForDay(g.getStartTime(), nowDate)<=0 && DateUtils.compareForDay(g.getEndTime(), nowDate)>=0){
						//中间
						return error("存在还在执行的方案，不能新增！");
					}else if(DateUtils.compareForDay(g.getStartTime(), nowDate)>0){
						//后面
						return error("存在还在执行的方案，不能新增！");
					}

				}
			}
			
			plan.setSubjectArrangeId(arrangeId);
			//开始时间小于结束时间
			if(DateUtils.compareForDay(plan.getStartTime(), plan.getEndTime())>0){
				return error("开始时间不能大于上课结束时间！");
			}
			if(DateUtils.compareForDay(nowDate, plan.getStartTime())>0){
				return error("当前时间不能大于上课开始时间！");
			}
			DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), plan.getAcadyear(), Integer.valueOf(plan.getSemester()), plan.getStartTime()), DateInfo.class);
			if(dateInfo == null){
				return error("保存失败,未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
			}
			Calendar cal = Calendar.getInstance();
	        cal.setTime(plan.getStartTime()); 
	        
	        int w=dayOfWeekByCalendar(cal);
	        //---cal.get(Calendar.DAY_OF_WEEK) 星期日 是1  这边需要星期一是0
//	        int w = cal.get(Calendar.DAY_OF_WEEK)-1-1;
//	        if (w < 0){
//	            w = 6;
//	        }
	        plan.setDayOfWeek(w);
	        plan.setWeekOfWorktime(dateInfo.getWeek());
			DateInfo dateInfo1 = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), plan.getAcadyear(), Integer.valueOf(plan.getSemester()), plan.getEndTime()), DateInfo.class);
			if(dateInfo1 == null){
				return error("保存失败,未维护节假日信息或者结束时间不在当前选择的学年学期内！");
			}
			
			
			plan.setWeekOfWorktime2(dateInfo1.getWeek());
			cal.setTime(plan.getEndTime());
			int w1=dayOfWeekByCalendar(cal);
//			int w1 = cal.get(Calendar.DAY_OF_WEEK)-1-1;
//			//-1 星期天 0星期1
//	        if (w1 < 0){
//	            w1 = 6;
//	        }
	        plan.setDayOfWeek2(w1);
	        plan.setId(UuidUtils.generateUuid());
	        plan.setCreationTime(new Date());
	        plan.setModifyTime(new Date());
	        plan.setStep(GkElectveConstants.STEP_0);
	        gkTeachPlacePlanService.savePlan(plan);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}

		return success("新增成功！");
	}
	@ResponseBody
	@RequestMapping("/arrangePlan/findSemester")
    @ControllerInfo(value = "查询某个学年学期")
	public String findSemester(String acadyear,String semester){
		Semester semesterObj = SUtils.dc(semesterService.findByAcadYearAndSemester(acadyear, Integer.valueOf(semester)), Semester.class);
		JSONObject json=new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(semesterObj==null){
			Date newDate = new Date();
			json.put("startDate", "");
			json.put("endDate", "");
			json.put("startDate1", sdf.format(newDate));
			json.put("endDate1", sdf.format(newDate));
		}else{
		    String startDate = "";
			try {
				Date workBegin = sdf.parse(sdf.format(semesterObj.getSemesterBegin()));
				Date endDate = sdf.parse(sdf.format(semesterObj.getSemesterEnd()));
				Date startBegin = sdf.parse(sdf.format(new Date()));
				if(DateUtils.compareForDay(workBegin, startBegin)>0){
					//workBegin在startBegin后面 最正常情况
					startDate = sdf.format(workBegin);
					json.put("startDate", startDate);
					json.put("endDate", sdf.format(endDate));
					json.put("startDate1", startDate);
					json.put("endDate1", sdf.format(endDate));
				}else{
					if(DateUtils.compareForDay(startBegin,endDate)>0){
						//startBegin在endDate后面 这个情况相当于在当前时间做过去的事
						startDate = sdf.format(startBegin);
						json.put("startDate", startDate);
						json.put("endDate", "");
						json.put("startDate1", startDate);
						json.put("endDate1", startDate);
					}else{
						//startBegin在workBegin，endDate之间
						startDate = sdf.format(startBegin);
						
						json.put("startDate", startDate);
						json.put("endDate", sdf.format(endDate));
						json.put("startDate1", startDate);
						json.put("endDate1", sdf.format(endDate));
					}
					
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping("/arrangePlan/doDelete")
    @ControllerInfo(value = "删除方案")
    public String doDeletePlan(@PathVariable String arrangeId,String planId) {
		try {
			GkTeachPlacePlan plan =gkTeachPlacePlanService.findPlanById(planId,false);
            if (plan == null) {
                return error("你要删除的信息不存在,请刷新后重试");
            }else {
            	Date nowDate=new Date();
        		
            	//如果当前时间在之前中间 不能新增 
        		if(DateUtils.compareForDay(plan.getStartTime(), nowDate)<0){
					//中间
					return error("方案已经执行，不能删除");
				}
        		gkTeachPlacePlanService.deleteByPlanId(planId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError("删除失败！", e.getMessage());
        }
        return success("删除成功！");
	}
	
	@RequestMapping("/arrangePlan/openTeachPlace/page")
    @ControllerInfo(value = "老师教师安排")
	public String openTeachPlace(@PathVariable String arrangeId,String planId,ModelMap map){
		//去开班前
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要的信息不存在,请刷新后重试！");
        }
		GkSubjectArrange ar = gkSubjectArrangeService.findArrangeById(plan.getSubjectArrangeId());
		if(ar == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统！");
		}
		map.put("gkSubArr", ar);
		
		List<GkTimetableLimitArrang> limtIds = gkTimetableLimitArrangService.findGkTimetableLimitArrangList(plan.getAcadyear(), plan.getSemester(), plan.getId(), GkElectveConstants.LIMIT_TYPE_6);
    	if(CollectionUtils.isEmpty(limtIds)){
    		addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "上课时间还没有维护，还不能进行下一步操作！");
    	}
		
		int num=gkSubjectService.findSubNumByRoundsIdTeachModel(plan.getId(),GkElectveConstants.USE_TRUE);
		if(num<1){
			addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "没有设置开班"+BaseConstants.PC_KC+"！");
		}
		Map<String,Integer> limitMap=new HashMap<String, Integer>();
		for(GkTimetableLimitArrang g:limtIds){
    		if(!limitMap.containsKey(g.getArrangId())){
    			limitMap.put(g.getArrangId(), 1);
    		}else{
    			limitMap.put(g.getArrangId(), limitMap.get(g.getArrangId())+1);
    		}
    	}		
    	//Set<String> arrangIds=EntityUtils.getSet(limtIds, "arrangId");
    	if(!GkElectveConstants.TRUE_STR.equals(plan.getGkRounds().getOpenClass())){
    		num=ar.getSubjectNum();
    	}
    	//判断
    	if(num!=limitMap.keySet().size()){
    		addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
    		return errorFtl(map, "上课时间的维护与按实际需要的"+BaseConstants.PC_KC+"数不符！");	
    	}
    	//维护的批次时间不等
    	int gg=-1;
    	for(String key:limitMap.keySet()){
    		if(gg<0){
    			gg=limitMap.get(key);
    		}else{
    			if(!(gg==limitMap.get(key))){
    				addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
    				return errorFtl(map, "各"+BaseConstants.PC_KC+"维护的上课节次数不同，请先修改！");	
    			}
    		}
    	}
    	
    	map.put("arrangeId", arrangeId);
    	map.put("planId", planId);
    	map.put("plan", plan);
		return "/gkelective/arrangePlan/openTeachPlaceIndex.ftl";
	}
	
	@RequestMapping("/arrangePlan/makePlan/page")
    @ControllerInfo(value = "自动分配教师老师安排基础数据维护")
	public String toMakePlan(@PathVariable String arrangeId,String planId,ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要的信息不存在,请刷新后重试！");
        }
		//老师安排
		//需要走班的科目
		List<GkSubject> gkSubjectList = gkSubjectService.findByRoundsId(plan.getRoundsId(), GkElectveConstants.USE_TRUE);
		if(CollectionUtils.isEmpty(gkSubjectList)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "没有设置开班"+BaseConstants.PC_KC+"！");
		}
		//key:批次 value:数量
		Map<Integer,Integer> placeNumMap=new HashMap<Integer, Integer>();
		//key:subjectId,key:批次 value:数量
		Map<String,Map<Integer,Integer>> subjectMap=new HashMap<String, Map<Integer,Integer>>();
		List<GkBatch> bathList = gkBatchService.findByRoundsId(new String[]{planId});
		if(CollectionUtils.isEmpty(bathList)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "没有"+BaseConstants.PC_KC+"结果！");
		}
		Set<String> teachClassIds = EntityUtils.getSet(bathList, "teachClassId");
		List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
		Map<String, TeachClass> teachClassMap=new HashMap<String, TeachClass>();
		if(CollectionUtils.isNotEmpty(teachClassList)){
			teachClassMap = EntityUtils.getMap(teachClassList, "id");
		}
		TeachClass teach=null;
		for(GkBatch g:bathList){
			if(!placeNumMap.containsKey(g.getBatch())){
				placeNumMap.put(g.getBatch(), 1);
			}else{
				placeNumMap.put(g.getBatch(), (placeNumMap.get(g.getBatch())+1));
			}
			if(teachClassMap.containsKey(g.getTeachClassId())){
				teach = teachClassMap.get(g.getTeachClassId());
				if(!subjectMap.containsKey(teach.getCourseId())){
					subjectMap.put(teach.getCourseId(), new HashMap<Integer, Integer>());
					
				}
				if(!subjectMap.get(teach.getCourseId()).containsKey(g.getBatch())){
					subjectMap.get(teach.getCourseId()).put(g.getBatch(), 1);
				}else{
					Integer num = subjectMap.get(teach.getCourseId()).get(g.getBatch());
					subjectMap.get(teach.getCourseId()).put(g.getBatch(), (num+1));
				}
			}	
		}
		//取得每个值的最大值
		int placeMax=findMax(placeNumMap);
		Map<String,Integer> maxSubjectMap=new HashMap<String, Integer>();
		for(String key:subjectMap.keySet()){
			maxSubjectMap.put(key, findMax(subjectMap.get(key)));
		}
		//先默认全部
		List<Course> courseList = SUtils.dt(courseRemoteService.findByBaseCourseCodes(BaseConstants.SUBJECT_73), new TR<List<Course>>() {});
		Map<String,Course> courseMap=EntityUtils.getMap(courseList,"id");
		
		List<GkSubjectDto> subjectDtoList=new ArrayList<GkSubjectDto>();
		GkSubjectDto dto=null;
		
		boolean isAdd=true;//是否是第一次
		//为了获取教师信息
		Set<String> ids = EntityUtils.getSet(gkSubjectList, "id");
		//获取科目对应的任课老师
		List<GkRelationship> gkRtpList = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_01,ids.toArray(new String[0]));
        Map<String, List<String>> gkRtpMap = EntityUtils.getListMap(gkRtpList, "primaryId", "relationshipTargetId");
        if(CollectionUtils.isNotEmpty(gkRtpList)){
        	isAdd=false;
        }
		for(GkSubject g:gkSubjectList){
			dto=new GkSubjectDto();
			dto.setGkSubjectId(g.getId());
			dto.setSubjectId(g.getSubjectId());
			dto.setPunchCard(g.getPunchCard());
			if(courseMap.containsKey(g.getSubjectId())){
				dto.setSubjectName(courseMap.get(g.getSubjectId()).getSubjectName());
			}
			if(maxSubjectMap.containsKey(g.getSubjectId())){
				dto.setTeacherNum(maxSubjectMap.get(g.getSubjectId()));
			}else{
				dto.setTeacherNum(0);
			}
			List<String> teas =gkRtpMap.get(g.getId());
			if(CollectionUtils.isNotEmpty(teas)){
				dto.setTeacherIds(teas.toArray(new String[0]));
			}
			subjectDtoList.add(dto);
		}
		map.put("subjectDtoList", subjectDtoList);
		map.put("arrangeId", arrangeId);
		map.put("planId", planId);
		map.put("placeMax", placeMax);
		//获取方案对应的场地
		List<GkRelationship> placeReList = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_04,new String[]{planId});
		Set<String> placeIdSet = EntityUtils.getSet(placeReList, "relationshipTargetId");
		map.put("placeIdSet", placeIdSet);
		
		//教师选择
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), new TR<List<Teacher>>(){});
		map.put("teacherList", teacherList);
		//场地选择
		Map<String, String> placeMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(getLoginInfo().getUnitId(), 
				GkElectveConstants.TEACH_PLACE_CLASSROOM), new TR<Map<String, String>>(){});
		if(isAdd && placeIdSet.size()>0){
			isAdd=false;
		}
		map.put("placeMap", placeMap);
		map.put("isAdd", isAdd);
		map.put("plan", plan);
		return "/gkelective/arrangePlan/openTeachPlaceEdit.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/arrangePlan/saveMakePlan")
    @ControllerInfo(value = "保存基础数据")
    public String saveMakePlan(@PathVariable String arrangeId,GkTeachPlacePlanDto dto,String type) {
		try {
            if(dto==null){
            	return error("保存失败！");
            }
            gkTeachPlacePlanService.saveItem(dto,type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功！");
	}
	private int findMax(Map<Integer,Integer> map){
		int max=0;
		for(Integer i:map.keySet()){
			if(max<map.get(i)){
				max=map.get(i);
			}
		}
		return max;
	}
	
	/***************************查看  微调*******************************************/
	@RequestMapping("/arrangePlan/toTeacherPlace/page")
    @ControllerInfo(value = "微调老师,场地")
	public String toTeacherPlace(@PathVariable String arrangeId,String planId,String viewtype,ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(planId,GkSubject.TEACH_TYPE1);
        Set<String> courseIds=new HashSet<String>();
        for (GkSubject gkSubject : findGkSubjectList) {
			courseIds.add(gkSubject.getSubjectId());
		}
        int batchSize = 0;
		if(GkElectveConstants.TRUE_STR.equals(plan.getGkRounds().getOpenClass())){
			batchSize = findGkSubjectList.size();
		}else{
			batchSize = 3 ;
		}
		List<Course> coursesList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		map.put("coursesList", coursesList);
		map.put("batchSize", batchSize);
        map.put("viewtype", viewtype);//场地
        return "/gkelective/arrangePlan/openTeacherIndex.ftl";
	}
	
	@RequestMapping("/arrangePlan/showList/page")
    @ControllerInfo(value = "微调老师,场地列表")
	public String showList(@PathVariable String arrangeId,String planId,GoClassSearchDto goClassSearchDto,String viewtype,ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
    	map.put("planId", planId);
		map.put("plan", plan);
		
		Map<String,List<GkBatch>> gkBatchMapByCourse = new LinkedHashMap<String, List<GkBatch>>();
		Map<String,List<GkBatch>> gkBatchMapByBatch = new LinkedHashMap<String, List<GkBatch>>();
		
		//根据批次 与选考 学考
		List<GkBatch> findGkBatchList = gkBatchService.findGkBatchList(planId, (StringUtils.isNotBlank(goClassSearchDto.getSearchBatch())?Integer.valueOf(goClassSearchDto.getSearchBatch()):null), goClassSearchDto.getSearchGkType());
		//教学班ids
		Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
		
		Map<String, TeachClass> teaClsMap = new HashMap<String, TeachClass>();//找教学班
		Map<String, GkTeachClassEx> teaClsExMap = new HashMap<String, GkTeachClassEx>();//找教学班辅助信息
		Map<String, Student> stuMap = new HashMap<String, Student>();//找学生
		Map<String, Course> couMap = new HashMap<String, Course>();//找科目
		Map<String, Set<String>> teaClsStuMap = new HashMap<String, Set<String>>();//教学班对应学生
		Set<String> linStr = null;
		if(CollectionUtils.isNotEmpty(teachClassIds)){
			List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
			List<GkTeachClassEx> findGkTeachClassExList = gkTeachClassExService.findGkTeachClassExList(planId, teachClassIds.toArray(new String[0]));
			teaClsExMap = EntityUtils.getMap(findGkTeachClassExList, "teachClassId");
			teaClsMap = EntityUtils.getMap(teaClsList, "id");
			Set<String> courseIds = EntityUtils.getSet(teaClsList, "courseId");
			
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
			couMap = EntityUtils.getMap(courseList, "id");
			Map<String, List<String>> studentTeachClassMap = SUtils.dt(teachClassStuRemoteService.findMapWithStuIdByClassIds(teachClassIds.toArray(new String[0])),new TR<Map<String,List<String>>>(){});
			List<String> value = null;
			for(Map.Entry<String, List<String>> entry : studentTeachClassMap.entrySet()){
				value = entry.getValue();
				for (String clsId : value) {
					linStr = teaClsStuMap.get(clsId);
					if(linStr == null){
						linStr = new HashSet<String>();
						teaClsStuMap.put(clsId, linStr);
					}
					linStr.add(entry.getKey());
				}
			}
			List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(studentTeachClassMap.keySet().toArray(new String[0])),new TR<List<Student>>(){});
			stuMap = EntityUtils.getMap(stuList, "id");
		}
		Set<String> courseIds = new HashSet<String>();
		if(StringUtils.isNotBlank(goClassSearchDto.getSearchCourseId())){
			courseIds.add(goClassSearchDto.getSearchCourseId());
		}
		
		List<GkBatch> linGkBatchList = null;
		int manNum;
		int wManNum;
		Student stu = null;
		TeachClass teachClass = null;
		Course course = null;
		GkTeachClassEx gkTeachClassEx = null;
		boolean isNotArrange=false;//是否筛选未安排
		if("0".equals(goClassSearchDto.getSearchArrange())){
			isNotArrange=true;
		}
		if("1".equals(viewtype)){
			//按科目查看
			for(GkBatch item : findGkBatchList){
				teachClass = teaClsMap.get(item.getTeachClassId());
				if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getCourseId()))){
					continue;
				}
				if(isNotArrange && StringUtils.isNotBlank(teachClass.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(teachClass.getTeacherId()))){
					continue;
				}
				item.setClassName(teachClass.getName());
				//教师
				item.setTeacherId(teachClass.getTeacherId());
				gkTeachClassEx = teaClsExMap.get(item.getTeachClassId());
				item.setAverageScore(gkTeachClassEx.getAverageScore());
				course = couMap.get(teachClass.getCourseId());
				linGkBatchList = gkBatchMapByCourse.get(course.getSubjectName());
				if(linGkBatchList == null){
					linGkBatchList = new ArrayList<GkBatch>();
					gkBatchMapByCourse.put(course.getSubjectName(), linGkBatchList);
				}
				linGkBatchList.add(item);
				linStr = teaClsStuMap.get(item.getTeachClassId());
				if(CollectionUtils.isNotEmpty(linStr)){
					item.setNumber(linStr.size());
					manNum = 0;
					wManNum = 0;
					for (String stuId : linStr) {
						stu = stuMap.get(stuId);
						if(stu != null && stu.getSex() != null){
							if(stu.getSex() == GkElectveConstants.MALE){
								manNum++;
							}else if(stu.getSex() == GkElectveConstants.FEMALE){
								wManNum++;
							}
						}
					}
					item.setManNumber(manNum);
					item.setWomanNumber(wManNum);
				}
			}
			map.put("gkBatchMap", gkBatchMapByCourse);
		}else{
			//按批次查看
			for(GkBatch item : findGkBatchList){
				teachClass = teaClsMap.get(item.getTeachClassId());
				if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getCourseId()))){
					continue;
				}
				if(isNotArrange && (StringUtils.isNotBlank(item.getPlaceId()))){
					continue;
				}
				item.setClassName(teachClass.getName());
				//场地
				item.setPlaceId(item.getPlaceId());
				gkTeachClassEx = teaClsExMap.get(item.getTeachClassId());
				item.setAverageScore(gkTeachClassEx.getAverageScore());
				course = couMap.get(teachClass.getCourseId());
				item.setSubjectName(course.getSubjectName());
				linGkBatchList = gkBatchMapByBatch.get(BaseConstants.PC_KC+item.getBatch());
				if(linGkBatchList == null){
					linGkBatchList = new ArrayList<GkBatch>();
					gkBatchMapByBatch.put(BaseConstants.PC_KC+item.getBatch(), linGkBatchList);
				}
				linGkBatchList.add(item);
				linStr = teaClsStuMap.get(item.getTeachClassId());
				if(CollectionUtils.isNotEmpty(linStr)){
					item.setNumber(linStr.size());
					manNum = 0;
					wManNum = 0;
					for (String stuId : linStr) {
						stu = stuMap.get(stuId);
						if(stu != null && stu.getSex() != null){
							if(stu.getSex() == GkElectveConstants.MALE){
								manNum++;
							}else if(stu.getSex() == GkElectveConstants.FEMALE){
								wManNum++;
							}
						}
					}
					item.setManNumber(manNum);
					item.setWomanNumber(wManNum);
				}
			}
			map.put("gkBatchMap", gkBatchMapByBatch);
		}
	
		
		//教师选择
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), new TR<List<Teacher>>(){});
		map.put("teacherList", teacherList);
		//场地选择
		Map<String, String> placeMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(getLoginInfo().getUnitId(), 
				GkElectveConstants.TEACH_PLACE_CLASSROOM), new TR<Map<String, String>>(){});
		map.put("placeMap", placeMap);
		
		map.put("arrangeId", arrangeId);
		map.put("viewtype", viewtype);
        return "/gkelective/arrangePlan/openTeacherList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/arrangePlan/teacher/checkSave")
	@ControllerInfo(value = "验证老师并保存")
	public String checkSaveTeacher(@PathVariable String arrangeId, String planId,String teaClsId, int batch, String teacherId){
		TeachClass teachClass = SUtils.dt(teachClassRemoteService.findOneById(teaClsId), new TR<TeachClass>(){});
        if(teachClass==null){
        	return error("对应班级班级不存在，请刷新后操作");
        }
        if(StringUtils.isBlank(teacherId) && (StringUtils.isBlank(teachClass.getTeacherId()) || BaseConstants.ZERO_GUID.equals(teachClass.getTeacherId()))){
			return success("");
		}
        if(StringUtils.isNotBlank(teacherId)){
        	GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, false);
    		if(plan==null){
    			return error("对应方案已经不存在，请刷新后操作");
    		}
        	//key:批次 value: 周几_上午_节次
    		Map<Integer,Set<String>> timeMap=getTime(plan);
    		Set<String> timeList = timeMap.get(batch);
    		CourseScheduleDto dto=new CourseScheduleDto();
            dto.setAcadyear(plan.getAcadyear());
            dto.setSchoolId(getLoginInfo().getUnitId());
            dto.setSemester(Integer.parseInt(plan.getSemester()));
            dto.setDayOfWeek1(plan.getDayOfWeek());
            dto.setWeekOfWorktime1(plan.getWeekOfWorktime());
            dto.setDayOfWeek2(plan.getDayOfWeek2());
            dto.setWeekOfWorktime2(plan.getWeekOfWorktime2());
            List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
            String sb = "";
            for(CourseSchedule c : crlist){
            	//开始
    			if(c.getWeekOfWorktime()==plan.getWeekOfWorktime()){
					if(c.getDayOfWeek()<plan.getDayOfWeek()){
						continue;
					}
				}else if(c.getWeekOfWorktime()==plan.getWeekOfWorktime2()){
					//结尾	
					if(c.getDayOfWeek()>plan.getDayOfWeek2()){
						continue;
					}
				}
            	
            	String str = c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
            	if(timeList.contains(str) && !StringUtils.equals(c.getClassId(), teaClsId)){
            		//取到该时间节点下面，并且不是该班级的数据
            		if(StringUtils.equals(c.getTeacherId(), teacherId)){
            			//判断这个时间点下面，改老师是否已经被其它班级的课程占
            			if(1 != c.getClassType()){
            				TeachClass teaCls = SUtils.dt(teachClassRemoteService.findOneById(c.getClassId()), new TR<TeachClass>(){});
            				Grade gr = SUtils.dt(gradeRemoteService.findOneById(teaCls.getGradeId()), new TR<Grade>(){});
            				if(gr==null){
            					sb=","+teaCls.getName();
            				}
            				else{
            					sb=","+gr.getGradeName()+teaCls.getName();
            				}
            				//sb = "与"+teaCls.getName() + "安排的任课时间有冲突";
            			}else{
            				Clazz cls = SUtils.dt(classRemoteService.findOneById(c.getClassId()), new TR<Clazz>(){});
            				Grade gr = SUtils.dt(gradeRemoteService.findOneById(cls.getGradeId()), new TR<Grade>(){});
            				//sb =  "与"+gr.getGradeName() + cls.getClassName() + "安排的任课时间有冲突";
            				sb=","+gr.getGradeName() + cls.getClassName();
            			}
            			break;
            		}
            	}
            	str = "";
            }
            if(sb.length() > 0){
            	sb=sb.substring(1);
            	return error("该老师已在"+sb+"任课");
    		}else{
    			//有可能这个上课设置时间恰好没有包括在课表内 判断同一批次有无重复  至少在页面上不给人以错觉
    			List<GkBatch> list = gkBatchService.findByBatchAndInClassIds(planId, batch, null);
                if(CollectionUtils.isNotEmpty(list)){
                	Set<String> teachClassIds = EntityUtils.getSet(list, "teachClassId");
                	List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
        			Map<String, TeachClass> teaClsMap = EntityUtils.getMap(teaClsList, "id");
        			for(GkBatch g:list){
        				if(StringUtils.equals(teaClsId,g.getTeachClassId())){
        					continue;
        				}
        				if(teaClsMap.containsKey(g.getTeachClassId())){
        					TeachClass teaCls = teaClsMap.get(g.getTeachClassId());
        					if(StringUtils.equals(teaCls.getTeacherId(), teacherId)){
        						
        						Grade gr = SUtils.dt(gradeRemoteService.findOneById(teaCls.getGradeId()), new TR<Grade>(){});
                				if(gr==null){
                					sb=","+teaCls.getName();
                				}
                				else{
                					sb=","+gr.getGradeName()+teaCls.getName();
                				}
                				break;
        					}
        				}
        				
        			}
        			 if(sb.length() > 0){
    	            	sb=sb.substring(1);
    	            	return error("该老师已在"+sb+"任课");
        			 }
        			
                }
    		}
             
            
        }
        if(StringUtils.isBlank(teacherId)){
        	teacherId=BaseConstants.ZERO_GUID;
        }
       
        teachClass.setTeacherId(teacherId);
        //成功
        List<TeachClass> clsList=new ArrayList<TeachClass>();
        clsList.add(teachClass);
        try{
        	gkTeachPlacePlanService.saveTeaAndCourseSch(clsList, planId, this.getLoginInfo().getUnitId());
        }catch (Exception e) {
        	e.printStackTrace();
			return error("操作失败");
		}
		return success("");
	}
	
	@ResponseBody
	@RequestMapping("/arrangePlan/place/checkSave")
	@ControllerInfo(value="场地校验")
	public String checkSavePlace(@PathVariable String arrangeId,String planId, String batchIds,String placeId){
		if(StringUtils.isBlank(batchIds)){
			return success("");
		}
		String bId=batchIds.split("-")[0];//批次id
		int batch = NumberUtils.toInt(batchIds.split("-")[1]);//批次
		GkBatch bath = gkBatchService.findOne(bId);
		if(bath==null){
			return error("数据已经不存在，请刷新后再操作");
		}
		if(StringUtils.isBlank(placeId) && StringUtils.isBlank(bath.getPlaceId())){
			return success("");
		}
		//需要修改
		if(StringUtils.isNotBlank(placeId) ){
			GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, false);
    		if(plan==null){
    			return error("对应方案已经不存在，请刷新后操作");
    		}
    		//key:批次 value: 周几_上午_节次
			Map<Integer,Set<String>> timeMap=getTime(plan);
			Set<String> time = timeMap.get(batch);
			if(time.size()<=0){
				return error("数据已经不存在，请刷新后再操作");
			}
			
			//key:周几_上午_节次 value:批次
			//Map<String,Integer> bathTimeMap=getValuetoKey(timeMap);
			CourseScheduleDto dto=new CourseScheduleDto();
	        dto.setAcadyear(plan.getAcadyear());
	        dto.setSchoolId(getLoginInfo().getUnitId());
	        dto.setSemester(Integer.parseInt(plan.getSemester()));
	        dto.setDayOfWeek1(plan.getDayOfWeek());
	        dto.setWeekOfWorktime1(plan.getWeekOfWorktime());
	        dto.setDayOfWeek2(plan.getDayOfWeek2());
	        dto.setWeekOfWorktime2(plan.getWeekOfWorktime2());
	        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
	        String sb = "";
			for(CourseSchedule c : crlist){
				if(c.getWeekOfWorktime()==plan.getWeekOfWorktime()){
					if(c.getDayOfWeek()<plan.getDayOfWeek()){
						continue;
					}
				}else if(c.getWeekOfWorktime()==plan.getWeekOfWorktime2()){
					//结尾	
					if(c.getDayOfWeek()>plan.getDayOfWeek2()){
						continue;
					}
				}
				String str = c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
	        	if(time.contains(str) && (!c.getClassId().equals(bath.getTeachClassId()))){
	        		//取到该时间节点下面，并且不是该班级的数据
	        		if(StringUtils.equals(c.getPlaceId(), placeId)){
	        			//判断这个时间点下面，该场地是否已经被其它班级的课程占
	        			if(1 != c.getClassType()){
	        				TeachClass teaCls = SUtils.dt(teachClassRemoteService.findOneById(c.getClassId()), new TR<TeachClass>(){});
	        				Grade gr = SUtils.dt(gradeRemoteService.findOneById(teaCls.getGradeId()), new TR<Grade>(){});
            				if(gr==null){
            					sb = "与"+teaCls.getName() + "安排的场地有冲突";
            				}
            				else{
            					sb = "与"+gr.getGradeName()+teaCls.getName() + "安排的场地有冲突";
            				}
	        				
	        			}else{
	        				Clazz cls = SUtils.dt(classRemoteService.findOneById(c.getClassId()), new TR<Clazz>(){});
	        				Grade gr = SUtils.dt(gradeRemoteService.findOneById(cls.getGradeId()), new TR<Grade>(){});
	        				sb =  "与"+gr.getGradeName() + cls.getClassName() + "安排的场地有冲突";
	        			}
	        			break;
	        		}
	        	}
	        	str = "";
			}
			if(StringUtils.isNotBlank(sb)){
				return error(sb);
			}else{
    			//有可能这个上课设置时间恰好没有包括在课表内 判断同一批次有无重复  至少在页面上不给人以错觉
    			List<GkBatch> list = gkBatchService.findByBatchAndInClassIds(planId, batch, null);
                if(CollectionUtils.isNotEmpty(list)){
                	for(GkBatch g:list){
        				if(StringUtils.equals(g.getId(),bId)){
        					continue;
        				}
        				if(StringUtils.equals(g.getPlaceId(), placeId)){
        					
        					TeachClass teaCls = SUtils.dt(teachClassRemoteService.findOneById(g.getTeachClassId()), new TR<TeachClass>(){});
	        				Grade gr = SUtils.dt(gradeRemoteService.findOneById(teaCls.getGradeId()), new TR<Grade>(){});
            				if(gr==null){
            					sb = "与"+teaCls.getName() + "安排的场地有冲突";
            				}
            				else{
            					sb = "与"+gr.getGradeName()+teaCls.getName() + "安排的场地有冲突";
            				}
            				break;
        				}
        				
        			}
        			 if(StringUtils.isNotBlank(sb)){
    	            	return error(sb);
        			 }
        			
                }
    		}
		}
		bath.setPlaceId(placeId);
		List<GkBatch> checkBas=new ArrayList<GkBatch>();
		checkBas.add(bath);
		
		try{
        	gkTeachPlacePlanService.saveAllAndCourseSchedule(checkBas, planId, this.getLoginInfo().getUnitId());
        }catch (Exception e) {
        	e.printStackTrace();
			return error("操作失败");
		}
		return success("");
	}	
	
	
	@RequestMapping("/arrangePlan/findAllResultIndex/page")
    @ControllerInfo(value = "安排结果")
	public String findAllResultIndex(@PathVariable String arrangeId,String planId,ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        return "/gkelective/arrangePlan/openAllResultIndex.ftl";
	}
	
	
	@RequestMapping("/arrangePlan/resultListHead/page")
    @ControllerInfo(value = "安排结果查询")
	public String resultListHead(@PathVariable String arrangeId,String planId,ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        map.put("plan", plan);
        
        Set<String> courseIds = new HashSet<String>();
		List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(planId,GkSubject.TEACH_TYPE1);
		for (GkSubject gkSubject : findGkSubjectList) {
			courseIds.add(gkSubject.getSubjectId());
		}
		List<Course> coursesList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		int batchSize = 0;
		if(GkElectveConstants.TRUE_STR.equals(plan.getGkRounds().getOpenClass())){
			batchSize = findGkSubjectList.size();
		}else{
			batchSize = 3 ;
		}
		map.put("coursesList", coursesList);
		map.put("batchSize", batchSize);  
        return "/gkelective/arrangePlan/resultListHead.ftl";
	}
	@RequestMapping("/arrangePlan/resultList/page")
    @ControllerInfo(value = "安排结果列表")
	public String resultList(@PathVariable String arrangeId,String planId,GoClassSearchDto goClassSearchDto, ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        map.put("plan", plan);
        
		map.put("searchViewTypeRedio", goClassSearchDto.getSearchViewTypeRedio());
		Map<String,List<GkBatch>> gkBatchMapByBatch = new LinkedHashMap<String, List<GkBatch>>();
		Map<String,List<GkBatch>> gkBatchMapByCourse = new LinkedHashMap<String, List<GkBatch>>();
		List<GkBatch> findGkBatchList = gkBatchService.findGkBatchList(planId, (StringUtils.isNotBlank(goClassSearchDto.getSearchBatch())?Integer.valueOf(goClassSearchDto.getSearchBatch()):null), goClassSearchDto.getSearchGkType());
		Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
		
		Set<String> placeIds=EntityUtils.getSet(findGkBatchList, "placeId");
		//场地选择
		Map<String, String> placeMap =new HashMap<String, String>();
		if(placeIds.size()>0){
			placeMap=SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0])), new TR<Map<String, String>>(){});
		}
		//教师选择
		Map<String,Teacher> teacherMap=new HashMap<String, Teacher>();
		
		Map<String, TeachClass> teaClsMap = new HashMap<String, TeachClass>();//找教学班
		Map<String, GkTeachClassEx> teaClsExMap = new HashMap<String, GkTeachClassEx>();//找教学班辅助信息
		Map<String, Student> stuMap = new HashMap<String, Student>();//找学生
		Map<String, Course> couMap = new HashMap<String, Course>();//找科目
		
		Map<String, Set<String>> teaClsStuMap = new HashMap<String, Set<String>>();//教学班对应学生
		Set<String> linStr = null;
		
		boolean isNotArrange=false;//是否筛选未安排
		if("0".equals(goClassSearchDto.getSearchArrange())){
			isNotArrange=true;
		}
		if(CollectionUtils.isNotEmpty(teachClassIds)){
			
			List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
			
			Set<String> teacherIds = EntityUtils.getSet(teaClsList, "teacherId");
			if(teacherIds.size()>0){
				List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])),new TR<List<Teacher>>(){});
				if(CollectionUtils.isNotEmpty(teacherList)){
					teacherMap=EntityUtils.getMap(teacherList, "id");
				}
			}
			
			List<GkTeachClassEx> findGkTeachClassExList = gkTeachClassExService.findGkTeachClassExList(planId, teachClassIds.toArray(new String[0]));
			teaClsExMap = EntityUtils.getMap(findGkTeachClassExList, "teachClassId");
			teaClsMap = EntityUtils.getMap(teaClsList, "id");
			Set<String> courseIds = EntityUtils.getSet(teaClsList, "courseId");
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
			couMap = EntityUtils.getMap(courseList, "id");
			
			Map<String, List<String>> studentTeachClassMap = SUtils.dt(teachClassStuRemoteService.findMapWithStuIdByClassIds(teachClassIds.toArray(new String[0])),new TR<Map<String,List<String>>>(){});
			List<String> value = null;
			for(Map.Entry<String, List<String>> entry : studentTeachClassMap.entrySet()){
				value = entry.getValue();
				for (String clsId : value) {
					linStr = teaClsStuMap.get(clsId);
					if(linStr == null){
						linStr = new HashSet<String>();
						teaClsStuMap.put(clsId, linStr);
					}
					linStr.add(entry.getKey());
				}
			}
			List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(studentTeachClassMap.keySet().toArray(new String[0])),new TR<List<Student>>(){});
			stuMap = EntityUtils.getMap(stuList, "id");
		}
		Set<String> courseIds = new HashSet<String>();
		if(StringUtils.isNotBlank(goClassSearchDto.getSearchCourseId())){
			courseIds.add(goClassSearchDto.getSearchCourseId());
		}
		List<GkBatch> linGkBatchList = null;
		int manNum;
		int wManNum;
		Student stu = null;
		TeachClass teachClass = null;
		Course course = null;
		GkTeachClassEx gkTeachClassEx = null;
		if("1".equals(goClassSearchDto.getSearchViewTypeRedio())){
			//按批次查看
			for(GkBatch item : findGkBatchList){
				teachClass = teaClsMap.get(item.getTeachClassId());
				if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getCourseId()))){
					continue;
				}
				
				if(isNotArrange && StringUtils.isNotBlank(item.getPlaceId()) 
						&& StringUtils.isNotBlank(teachClass.getTeacherId())
						&& (!BaseConstants.ZERO_GUID.equals(teachClass.getTeacherId()))){
					continue;
				}
				
				item.setClassName(teachClass.getName());
				if(StringUtils.isNotBlank(item.getPlaceId()) && placeMap.containsKey(item.getPlaceId())){
					item.setPlaceName(placeMap.get(item.getPlaceId()));
				}
				if(teacherMap.containsKey(teachClass.getTeacherId())){
					item.setTeacherName(teacherMap.get(teachClass.getTeacherId()).getTeacherName());
				}
				gkTeachClassEx = teaClsExMap.get(item.getTeachClassId());
				item.setAverageScore(gkTeachClassEx.getAverageScore());
				course = couMap.get(teachClass.getCourseId());
				item.setSubjectName(course.getSubjectName());
				linGkBatchList = gkBatchMapByBatch.get(BaseConstants.PC_KC+item.getBatch());
				if(linGkBatchList == null){
					linGkBatchList = new ArrayList<GkBatch>();
					gkBatchMapByBatch.put(BaseConstants.PC_KC+item.getBatch(), linGkBatchList);
				}
				linGkBatchList.add(item);
				linStr = teaClsStuMap.get(item.getTeachClassId());
				if(CollectionUtils.isNotEmpty(linStr)){
					item.setNumber(linStr.size());
					manNum = 0;
					wManNum = 0;
					for (String stuId : linStr) {
						stu = stuMap.get(stuId);
						if(stu != null && stu.getSex() != null){
							if(stu.getSex() == GkElectveConstants.MALE){
								manNum++;
							}else if(stu.getSex() == GkElectveConstants.FEMALE){
								wManNum++;
							}
						}
					}
					item.setManNumber(manNum);
					item.setWomanNumber(wManNum);
				}
			}
			map.put("gkBatchMap", gkBatchMapByBatch);
		}else if("2".equals(goClassSearchDto.getSearchViewTypeRedio())){
			//按科目查看
			for(GkBatch item : findGkBatchList){
				teachClass = teaClsMap.get(item.getTeachClassId());
				if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getCourseId()))){
					continue;
				}
				if(isNotArrange && StringUtils.isNotBlank(item.getPlaceId()) 
						&& StringUtils.isNotBlank(teachClass.getTeacherId())
						&& (!BaseConstants.ZERO_GUID.equals(teachClass.getTeacherId()))){
					continue;
				}
				item.setClassName(teachClass.getName());
				if(StringUtils.isNotBlank(item.getPlaceId()) && placeMap.containsKey(item.getPlaceId())){
					item.setPlaceName(placeMap.get(item.getPlaceId()));
				}
				if(teacherMap.containsKey(teachClass.getTeacherId())){
					item.setTeacherName(teacherMap.get(teachClass.getTeacherId()).getTeacherName());
				}
				
				gkTeachClassEx = teaClsExMap.get(item.getTeachClassId());
				item.setAverageScore(gkTeachClassEx.getAverageScore());
				course = couMap.get(teachClass.getCourseId());
				linGkBatchList = gkBatchMapByCourse.get(course.getSubjectName());
				if(linGkBatchList == null){
					linGkBatchList = new ArrayList<GkBatch>();
					gkBatchMapByCourse.put(course.getSubjectName(), linGkBatchList);
				}
				linGkBatchList.add(item);
				linStr = teaClsStuMap.get(item.getTeachClassId());
				if(CollectionUtils.isNotEmpty(linStr)){
					item.setNumber(linStr.size());
					manNum = 0;
					wManNum = 0;
					for (String stuId : linStr) {
						stu = stuMap.get(stuId);
						if(stu != null && stu.getSex() != null){
							if(stu.getSex() == GkElectveConstants.MALE){
								manNum++;
							}else if(stu.getSex() == GkElectveConstants.FEMALE){
								wManNum++;
							}
						}
					}
					item.setManNumber(manNum);
					item.setWomanNumber(wManNum);
				}
			}
			map.put("gkBatchMap", gkBatchMapByCourse);
		}
		return "/gkelective/arrangePlan/resultList.ftl";
	}
	
	
	
	@RequestMapping("/arrangePlan/resultPie/page")
    @ControllerInfo(value = "结果统计")
	public String resultPie(@PathVariable String arrangeId,String planId,ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        map.put("plan", plan);
        
        Set<String> courseIds = new HashSet<String>();
		List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(planId,GkSubject.TEACH_TYPE1);
		for (GkSubject gkSubject : findGkSubjectList) {
			courseIds.add(gkSubject.getSubjectId());
		}
		List<Course> coursesList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		int batchSize = 0;
		if(GkElectveConstants.TRUE_STR.equals(plan.getGkRounds().getOpenClass())){
			batchSize = findGkSubjectList.size();
		}else{
			batchSize = 3 ;
		}
		map.put("coursesList", coursesList);
		Map<String, Course> courseMap = EntityUtils.getMap(coursesList, "id");
    	
		map.put("batchSize", batchSize);  
        
        List<GkBatch> findGkBatchList = gkBatchService.findByRoundsId(planId, null);
        
        Map<String,Set<String>> subjectTeacherIds=new HashMap<String, Set<String>>();

        Map<Integer,Set<String>> batchPlaceIds=new HashMap<Integer, Set<String>>();
        
        List<Teacher> searchTeacherList=new ArrayList<Teacher>();
        Set<String> tId=new HashSet<String>();
        if(CollectionUtils.isNotEmpty(findGkBatchList)){
        	Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
        	Set<String> placeIds=EntityUtils.getSet(findGkBatchList, "placeId");
        	Map<String, String> placeMap =new HashMap<String, String>();
    		if(placeIds.size()>0){
    			placeMap=SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0])), new TR<Map<String, String>>(){});
    		}
        	List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
        	Map<String, TeachClass> teachClassMap = EntityUtils.getMap(teaClsList, "id");
			Set<String> teacherIds = EntityUtils.getSet(teaClsList, "teacherId");
			Map<String, Teacher> teacherMap=new HashMap<String, Teacher>();
			if(teacherIds.size()>0){
				List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])),new TR<List<Teacher>>(){});
				if(CollectionUtils.isNotEmpty(teacherList)){
					teacherMap = EntityUtils.getMap(teacherList, "id");
				}
			}
			
			for(GkBatch g:findGkBatchList){
				TeachClass teach = teachClassMap.get(g.getTeachClassId());
				if(teach==null){
					continue;
				}
				if(StringUtils.isNotBlank(g.getPlaceId())){
					if(placeMap.containsKey(g.getPlaceId())){
						//有效场地
						if(!batchPlaceIds.containsKey(g.getBatch())){
							batchPlaceIds.put(g.getBatch(), new HashSet<String>());
						}
						batchPlaceIds.get(g.getBatch()).add(g.getPlaceId());
					}
				}
				if(teacherMap.containsKey(teach.getTeacherId())){
					//有效教师
					if(!subjectTeacherIds.containsKey(teach.getCourseId())){
						subjectTeacherIds.put(teach.getCourseId(), new HashSet<String>());
					}
					subjectTeacherIds.get(teach.getCourseId()).add(teach.getTeacherId());
					if(!tId.contains(teach.getTeacherId())){
						searchTeacherList.add(teacherMap.get(teach.getTeacherId()));
						tId.add(teach.getTeacherId());
					}
					
				}
			}
        }
        
        Map<String,Integer> teacherCount=new HashMap<String, Integer>();
        for(String key:courseIds){
        	if(courseMap.containsKey(key)){
        		if(subjectTeacherIds.containsKey(key)){
        			teacherCount.put(courseMap.get(key).getSubjectName(), subjectTeacherIds.get(key).size());
        		}else{
        			teacherCount.put(courseMap.get(key).getSubjectName(), 0);
        		}
        	}
        }
        
        
        //key:batch
        Map<String,Integer> placeCount=new HashMap<String, Integer>();
        for(int i=1;i<=batchSize;i++){
        	if(batchPlaceIds.containsKey(i)){
        		placeCount.put(BaseConstants.PC_KC+i, batchPlaceIds.get(i).size());
    		}else{
    			placeCount.put(BaseConstants.PC_KC+i, 0);
    		}
        }
        
        //各科老师统计图
		JSONObject json=new JSONObject();
		
		json.put("legendData", teacherCount.keySet().toArray(new String[0]));
		JSONArray jsonArr=new JSONArray();
		JSONObject json2=null;
		for(Map.Entry<String,Integer> entry : teacherCount.entrySet()){
			json2=new JSONObject();
			json2.put("value", entry.getValue());
			json2.put("name", entry.getKey());
			jsonArr.add(json2);
		}
		json.put("loadingData", jsonArr);
		String jsonStringData=json.toString();
		map.put("jsonStringData1", jsonStringData);
		//各批次教师统计图
		json=new JSONObject();
		json.put("legendData", placeCount.keySet().toArray(new String[0]));
		jsonArr=new JSONArray();
		json2=null;
		for(Map.Entry<String,Integer> entry : placeCount.entrySet()){
			json2=new JSONObject();
			json2.put("value", entry.getValue());
			json2.put("name", entry.getKey());
			jsonArr.add(json2);
		}
		json.put("loadingData", jsonArr);
		jsonStringData=json.toString();
		map.put("jsonStringData2", jsonStringData);
		
		//教师筛选
		
		map.put("teacherList", searchTeacherList);
        return "/gkelective/arrangePlan/resultPieIndex.ftl";
	}
	
	@RequestMapping("/arrangePlan/resultPieList/page")
    @ControllerInfo(value = "安排教师结果列表")
	public String resultPieList(@PathVariable String arrangeId,String planId,GoClassSearchDto goClassSearchDto, ModelMap map){
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
        map.put("arrangeId", arrangeId);
        map.put("planId", planId);
        map.put("plan", plan);
        
		Map<String,List<GkBatch>> gkBatchMapByTeacher = new LinkedHashMap<String, List<GkBatch>>();
		//批次列表
		List<GkBatch> findGkBatchList = gkBatchService.findGkBatchList(planId, (StringUtils.isNotBlank(goClassSearchDto.getSearchBatch())?Integer.valueOf(goClassSearchDto.getSearchBatch()):null), goClassSearchDto.getSearchGkType());
		Set<String> courseIds = new HashSet<String>();//页面查询条件
		if(StringUtils.isNotBlank(goClassSearchDto.getSearchCourseId())){
			courseIds.add(goClassSearchDto.getSearchCourseId());
		}
		
		//组装数据用到的参数
		Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
		Set<String> placeIds=EntityUtils.getSet(findGkBatchList, "placeId");
		//场地选择
		Map<String, String> placeMap =new HashMap<String, String>();
		if(placeIds.size()>0){
			placeMap=SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0])), new TR<Map<String, String>>(){});
		}
		//教师选择
		Map<String,Teacher> teacherMap=new HashMap<String, Teacher>();
		Map<String, TeachClass> teaClsMap = new HashMap<String, TeachClass>();//找教学班
		Map<String, Course> courseMap = new HashMap<String, Course>();//找科目
		
		if(CollectionUtils.isNotEmpty(teachClassIds)){
			List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
			teaClsMap = EntityUtils.getMap(teaClsList, "id");
			Set<String> teacherIds = EntityUtils.getSet(teaClsList, "teacherId");
			if(teacherIds.size()>0){
				List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])),new TR<List<Teacher>>(){});
				if(CollectionUtils.isNotEmpty(teacherList)){
					teacherMap=EntityUtils.getMap(teacherList, "id");
				}
			}
			Set<String> subjectIds = EntityUtils.getSet(teaClsList, "courseId");
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>(){});
			courseMap = EntityUtils.getMap(courseList, "id");
		}
		List<GkBatch> linGkBatchList = null;
		TeachClass teachClass = null;
		String teacherName =null;
		//按批次查看
		for(GkBatch item : findGkBatchList){
			teachClass = teaClsMap.get(item.getTeachClassId());
			if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getCourseId()))){
				continue;
			}
			if(!teacherMap.containsKey(teachClass.getTeacherId())){
				continue;
			}
			if(StringUtils.isNotBlank(goClassSearchDto.getSearchTeacherId()) && (!goClassSearchDto.getSearchTeacherId().equals(teachClass.getTeacherId()))){
				continue;
			}
			
			if(courseMap.containsKey(teachClass.getCourseId())){
				item.setSubjectName(courseMap.get(teachClass.getCourseId()).getSubjectName());
			}
			item.setClassName(teachClass.getName());
			
			if(StringUtils.isNotBlank(item.getPlaceId()) && placeMap.containsKey(item.getPlaceId())){
				item.setPlaceName(placeMap.get(item.getPlaceId()));
			}
			teacherName = teacherMap.get(teachClass.getTeacherId()).getTeacherName();
			linGkBatchList = gkBatchMapByTeacher.get(teacherName);
			if(linGkBatchList == null){
				linGkBatchList = new ArrayList<GkBatch>();
				gkBatchMapByTeacher.put(teacherName, linGkBatchList);
			}
			linGkBatchList.add(item);

		}
		map.put("gkBatchMap", gkBatchMapByTeacher);
		
		//key:批次 value: 星期一（上午第1节、下午第1节）、星期三（第1、3节）、星期五（第2、3节）
		Map<String,String> timeMap=getTimeStr(plan);
		map.put("timeMap", timeMap);
		
		return "/gkelective/arrangePlan/resultPieList.ftl";
	}
	
	
	@RequestMapping("/arrangePlan/single/detail/page")
	@ControllerInfo(value = "全部开班结果-查看班级学生")
	public String showSingleDetail(@PathVariable String arrangeId,String planId,String batchId, String teachClassId,String viewtype,ModelMap map){
		map.put("arrangeId", arrangeId);
		map.put("planId", planId);
		map.put("batchId", batchId);
		map.put("teachClassId", teachClassId);
		map.put("viewtype", viewtype);
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId, true);
        if (plan == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangePlan/index/page","#showList");
	        return errorFtl(map, "你要删除的信息不存在,请刷新后重试！");
        }
		map.put("plan", plan);
		GkBatch gkBatch = gkBatchService.findOne(batchId);
		TeachClass teachClass = SUtils.dt(teachClassRemoteService.findOneById(teachClassId),new TR<TeachClass>(){});
		if(gkBatch == null || teachClass == null){
			return errorFtl(map, "班级已不存在！");
		}
		map.put("gkBatch", gkBatch);
		map.put("className", teachClass.getName());
		List<Student> stuList = new ArrayList<Student>();
		List<TeachClassStu> findByClassIds=SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[]{teachClassId}),new TR<List<TeachClassStu>>(){});
		if(CollectionUtils.isNotEmpty(findByClassIds)){
			stuList = SUtils.dt(studentRemoteService.findListByIds(EntityUtils.getSet(findByClassIds, "studentId").toArray(new String[0])),new TR<List<Student>>(){});
		}
		map.put("stuList", stuList);
		return "/gkelective/arrangePlan/showDetailList.ftl";
	}
	
	 /**
     * key:批次 value: 周几_上午_节次
     */
    private Map<Integer,Set<String>> getTime(GkTeachPlacePlan plan){
    	List<GkTimetableLimitArrang> limtIds = gkTimetableLimitArrangService.findGkTimetableLimitArrangList(plan.getAcadyear(), plan.getSemester(), plan.getId(), GkElectveConstants.LIMIT_TYPE_6);
    	Map<Integer,Set<String>> returnMap=new HashMap<Integer, Set<String>>();
    	if(CollectionUtils.isNotEmpty(limtIds)){
    		for(GkTimetableLimitArrang l:limtIds){
    			int key = Integer.parseInt(l.getArrangId());
    			String str=l.getWeekday()+"_"+l.getPeriodInterval()+"_"+l.getPeriod();
    			if(!returnMap.containsKey(key)){
    				returnMap.put(key, new HashSet<String>());
    			}
    			returnMap.get(key).add(str);
    		}
    	}
    	return returnMap;
    }
    /**
     * 星期一（第1节）、星期三（第1、3节）、星期五（第2、3节）
     */
    private Map<String,String> getTimeStr(GkTeachPlacePlan plan){
    	List<GkTimetableLimitArrang> limtIds = gkTimetableLimitArrangService.findGkTimetableLimitArrangList(plan.getAcadyear(), plan.getSemester(), plan.getId(), GkElectveConstants.LIMIT_TYPE_6);
    	//一天节次数--取设置上课时间的那边
    	GkSubjectArrange  gaent = gkSubjectArrangeService.findArrangeById(plan.getSubjectArrangeId());
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(gaent.getGradeId()), new TR<Grade>(){});
		Map<String,Integer> timeIntervalMap = new LinkedHashMap<String,Integer>();
		timeIntervalMap.put("2", grade.getAmLessonCount()==null?4:grade.getAmLessonCount());
		timeIntervalMap.put("3", grade.getPmLessonCount()==null?4:grade.getPmLessonCount());
		timeIntervalMap.put("4", grade.getNightLessonCount()==null?3:grade.getNightLessonCount());
    	//周一到周日
    	int minWeekDay=0;
    	int maxWeekDay=6;
    	//批次<星期,节次>
    	Map<Integer,Map<String,Set<Integer>>> map=new HashMap<Integer, Map<String,Set<Integer>>>();
    	if(CollectionUtils.isNotEmpty(limtIds)){
    		for(GkTimetableLimitArrang l:limtIds){
    			int key = Integer.parseInt(l.getArrangId());
    			if(!map.containsKey(key)){
    				map.put(key, new HashMap<String, Set<Integer>>());
    			}
    			int period = Integer.parseInt(l.getPeriod());
    			
    			Map<String, Set<Integer>> itemMap = map.get(key);
    			if(!itemMap.containsKey(l.getWeekday())){
    				itemMap.put(l.getWeekday(), new HashSet<Integer>());
    			}
    			if("2".equals(l.getPeriodInterval())){
    				itemMap.get(l.getWeekday()).add(period);
    			}else if("3".equals(l.getPeriodInterval())){
    				itemMap.get(l.getWeekday()).add(period+timeIntervalMap.get("2"));
    			}else if("4".equals(l.getPeriodInterval())){
    				itemMap.get(l.getWeekday()).add(period+timeIntervalMap.get("2")+timeIntervalMap.get("3"));
    			}else{
    				itemMap.get(l.getWeekday()).add(period);
    			}
    		}
    	}
    	
    	Map<String,String> returnMap=new HashMap<String, String>();
    	Map<String, Set<Integer>> itemMap;
    	String mess="";
    	String str="";
    	Set<Integer> set;
    	for(Integer batch:map.keySet()){
    		itemMap = map.get(batch);
    		if(itemMap==null || itemMap.size()<=0){
    			continue;
    		}
    		for(int i=minWeekDay;i<=maxWeekDay;i++){
        		if(itemMap.containsKey(i+"")){
        			set = itemMap.get(i+"");
        			if(set==null || set.size()<=0){
        				continue;
        			}
        			str=makeWeekName(i);
        			str=str+"（第"+setToString(set.toArray(new Integer[0]))+"节）";
        			mess=mess+"、"+str;
        			str="";
        		}
        	}
    		if(StringUtils.isNotBlank(mess)){
    			mess=mess.substring(1);
    			returnMap.put(batch+"", mess);
    		}else{
    			returnMap.put(batch+"", "");
    		}
    		mess="";
    	}
    	
    	return returnMap;
    }
    
    private String setToString(Integer[] arr){
    	//升序
    	Arrays.sort(arr);
    	String s="";
    	for(Integer i:arr){
    		s=s+"、"+i;
    	}
    	s=s.substring(1);
    	return s;
    }
    
    private String makeWeekName(int day){
		switch (day) {
		case 0:
			return "星期一";
		case 1:
			return "星期二";
		case 2:
			return "星期三";
		case 3:
			return "星期四";
		case 4:
			return "星期五";
		case 5:
			return "星期六";
		case 6:
			return "星期日";
		default:
			return "星期"+(day+1);
		}
	}
    
    /**
	 * 当前日期星期index
	 * 
	 * @param ca
	 * @return
	 */
	public static int dayOfWeekByCalendar(Calendar ca) {
		int dayOfWeek = 0;
		switch (ca.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			dayOfWeek = 0;
			break;
		case Calendar.TUESDAY:
			dayOfWeek = 1;
			break;
		case Calendar.WEDNESDAY:
			dayOfWeek = 2;
			break;
		case Calendar.THURSDAY:
			dayOfWeek = 3;
			break;
		case Calendar.FRIDAY:
			dayOfWeek = 4;
			break;
		case Calendar.SATURDAY:
			dayOfWeek = 5;
			break;
		case Calendar.SUNDAY:
			dayOfWeek = 6;
			break;
		}
		return dayOfWeek;
	}
    
}
