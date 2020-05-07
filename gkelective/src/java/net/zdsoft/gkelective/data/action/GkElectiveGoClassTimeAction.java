package net.zdsoft.gkelective.data.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/gkelective/{arrangeId}")
public class GkElectiveGoClassTimeAction extends BaseAction{
	@Autowired
	private GkTimetableLimitArrangService gkTimetableLimitArrangService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkRoundsService gkRoundsService;
	@Autowired
	private GkSubjectService gkSubjectService;
	@Autowired
	private GkTeachPlacePlanService gkTeachPlacePlanService;
	
	@Resource
	private GradeRemoteService gradeRemoteService;
	@RequestMapping("/goClassTime/index/page")
    @ControllerInfo(value = "上课时间设置index")
    public String showIndex(@PathVariable String arrangeId,String planId,ModelMap map) {
		//课程表节次信息
		GkSubjectArrange  gaent = gkSubjectArrangeService.findArrangeById(arrangeId);
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(gaent.getGradeId()), new TR<Grade>(){});
		Map<String,Integer> timeIntervalMap = new LinkedHashMap<String,Integer>();
		timeIntervalMap.put("2", grade.getAmLessonCount()==null?4:grade.getAmLessonCount());
		timeIntervalMap.put("3", grade.getPmLessonCount()==null?4:grade.getPmLessonCount());
		timeIntervalMap.put("4", grade.getNightLessonCount()==null?3:grade.getNightLessonCount());
		//获取课程已经设置的信息
		
		GkTeachPlacePlan plan = gkTeachPlacePlanService.findOne(planId);
		
		List<GkTimetableLimitArrang> gkTimetableLimitArrangList = gkTimetableLimitArrangService.findGkTimetableLimitArrangList(plan.getAcadyear(), plan.getSemester(),plan.getId(), GkElectveConstants.LIMIT_ARRANG_TYPE);
		
    	String semStr = "";
    	if("1".equals(plan.getSemester())){
    		semStr = "上";
    	}else{
    		semStr = "下";
    	}
    	map.put("semester", semStr);
    	String gradeName = grade.getGradeName();
    	map.put("gradeName", gradeName);   	
    	
    	GkRounds rent =gkRoundsService.findRoundById(plan.getRoundsId());
    	
    	//同一次选课系统下 科目不可能重复
    	int bathNum=gkSubjectService.findSubNumByRoundsIdTeachModel(plan.getRoundsId(), GkSubject.TEACH_TYPE1);
    	if(GkRounds.OPENT_CLASS_0.equals(rent.getOpenClass())){
    		bathNum=3;
    	}
    	map.put("bathNum", bathNum);
    	
    	map.put("gkTimetableLimitArrangList", gkTimetableLimitArrangList);
    	map.put("planId", planId);
    	map.put("arrangeId", arrangeId);
    	map.put("timeIntervalMap", timeIntervalMap);
		map.put("gaent", gaent);
		map.put("plan", plan);
		return "/gkelective/goClassTime/goClassTimeIndex.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/goClassTime/save")
    @ControllerInfo(value = "保存课程设置")
    public String doSaveTimetableLimitArrang(String selectxyz,@PathVariable String arrangeId,String planId, String dataFilter, HttpSession httpSession){
    	try{
    		String error =  gkTimetableLimitArrangService.saveTimetableLimitArrang(selectxyz, planId, dataFilter); 		
    		if(StringUtils.isNotBlank(error)){
    			return error(error);
    		}else{
    			return returnSuccess();
    		}
    	}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
    }
	
	
	@ResponseBody
    @RequestMapping("/goClassTime/delete")
    @ControllerInfo(value = "删除课程设置")
    public String doDeleteGkTimetableLimitArrang(String selectxyz,@PathVariable String arrangeId,String planId){
    	try{
    		String[] array = selectxyz.split("_");
    		String weekday=array[0];
    		String period=array[1];
    		String periodInterval=array[2];
    		GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId,false);
    		gkTimetableLimitArrangService.deleteBySelectXyz(plan.getAcadyear(), plan.getSemester(), planId, period, periodInterval, weekday);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
    }
}
