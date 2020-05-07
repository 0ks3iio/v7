package net.zdsoft.stuwork.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormForm;
import net.zdsoft.stuwork.data.entity.DyDormStatResult;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormStatResultService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Component
@Controller
@RequestMapping("/stuwork")
public class DyDormStatResultAction extends BaseAction{
	@Autowired
	private DyDormStatResultService dyDormStatResultService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DyDormCheckResultService dyDormCheckResultService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	
	@RequestMapping("/dorm/stat/star/page")
	@ControllerInfo(value = "starIndex")
	public String starIndex(ModelMap map){
		return "/stuwork/dorm/stat/dyDormStarIndex.ftl";
	}
	@RequestMapping("/dorm/stat/starList/page")
	@ControllerInfo(value = "starList")
	public String starList(ModelMap map,DormSearchDto dormDto){
		String acadyear=dormDto.getAcadyear();
		String unitId=getLoginInfo().getUnitId();
		dormDto.setUnitId(unitId);
		
		School school=SUtils.dt(schoolRemoteService.findOneById(unitId),new TypeReference<School>(){});
		if(school==null){
			return errorFtl(map, "找不到学校");
		}
		String[] sections=school.getSections().split(",");
		List<Integer> sectionList = new ArrayList<Integer>();
		for(String section:sections){
			sectionList.add(Integer.parseInt(section.trim()));
		}
		//判断 0时  是初次进入 还是选择了幼儿园
		int section=dormDto.getSection();
		if(section==0){
			boolean flag=false;
			for(String sectionStr:sections){
				if(sectionStr.equals("0")){
					flag=true;
					break;
				}
			}
			if(!flag){
				section=Integer.parseInt(sections[0].trim());
				dormDto.setSection(section);
			}
		}
		//只要有一个为空 则代表是刚进入的状态  都需要获取默认值
		if(StringUtils.isBlank(acadyear)){
			Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,unitId),new TypeReference<Semester>(){});//TODO
			if(semester!=null){
				dormDto.setAcadyear(semester.getAcadyear());
				dormDto.setSemesterStr(String.valueOf(semester.getSemester()));
			}else{
				return errorFtl(map,"当前时间不在学年学期范围内");
			}
			dormDto.setSection(Integer.parseInt(sections[0].trim()));
			
		}
		List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester
				(unitId, dormDto.getAcadyear(), Integer.parseInt(dormDto.getSemesterStr())),new TR<List<DateInfo>>(){});
		int allWeek=0;
		if(CollectionUtils.isNotEmpty(dateInfoList)){
			allWeek=dateInfoList.get(dateInfoList.size()-1).getWeek();
		}else{
			return errorFtl(map, "当前学年学期 未维护周次");
		}
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		
		List<DyDormForm> formList=dyDormStatResultService.getStar(dormDto,allWeek);
		if(CollectionUtils.isNotEmpty(formList)){
			map.put("statList", formList.get(0).getStatList());
		}
		map.put("formList", formList);
		map.put("sectionList", sectionList);
		map.put("acadyearList", acadyearList);
		map.put("dormDto", dormDto);
		return "/stuwork/dorm/stat/dyDormStarList.ftl";
	}
	
	@RequestMapping("/dorm/stat/day/page")
	@ControllerInfo(value = "dayIndex")
	public String dayIndex(ModelMap map){
		return "/stuwork/dorm/stat/dyDormDayIndex.ftl";
	}
	@RequestMapping("/dorm/stat/dayList/page")
	@ControllerInfo(value = "dayList")
	public String dayList(DormSearchDto dormDto,ModelMap map){
		String unitId=getLoginInfo().getUnitId();
		dormDto.setUnitId(unitId);
		//一个为空代表 初始刚进入
		if(StringUtils.isBlank(dormDto.getAcadyear())){
			Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,unitId),new TypeReference<Semester>(){});//TODO
			if(semester!=null){
				dormDto.setAcadyear(semester.getAcadyear());
				dormDto.setSemesterStr(String.valueOf(semester.getSemester()));
			}else{
				return errorFtl(map,"当前时间不在学年学期范围内");
			}
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate
					(unitId, dormDto.getAcadyear(), semester.getSemester(), DateUtils.string2Date(DateUtils.date2String(new Date()),"yyyy-MM-dd")), DateInfo.class);
			dormDto.setWeek(dateInfo!=null?dateInfo.getWeek():1);
			dormDto.setWeekDay(dateInfo!=null?dateInfo.getWeekday():1);
		}
		School school=SUtils.dt(schoolRemoteService.findOneById(unitId),new TypeReference<School>(){});
		if(school==null){
			return errorFtl(map, "找不到学校");
		}
		String[] sections=school.getSections().split(",");
		List<Integer> sectionList = new ArrayList<Integer>();
		for(String section:sections){
			sectionList.add(Integer.parseInt(section.trim()));
		}
		//判断 0时  是初次进入 还是选择了幼儿园
		int section=dormDto.getSection();
		if(section==0){
			boolean flag=false;
			for(String sectionStr:sections){
				if(sectionStr.equals("0")){
					flag=true;
					break;
				}
			}
			if(!flag){
				section=Integer.parseInt(sections[0].trim());
				dormDto.setSection(section);
			}
		}
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		
		List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester
				(unitId, dormDto.getAcadyear(), Integer.parseInt(dormDto.getSemesterStr())),new TR<List<DateInfo>>(){});
		if(CollectionUtils.isNotEmpty(dateInfoList)){
			int allWeek=dateInfoList.get(dateInfoList.size()-1).getWeek();
			List<Integer> weekList=new ArrayList<Integer>();
			for(int i=1;i<=allWeek;i++){
				weekList.add(i);
			}
			map.put("weekList", weekList);
		}else{
			return errorFtl(map, "当前学年 未维护周次");
		}
		
		List<DyDormForm> formList=dyDormCheckResultService.getResRemForm(dormDto);
		
		map.put("formList", formList);
		map.put("sectionList", sectionList);
		map.put("acadyearList", acadyearList);
		map.put("dormDto", dormDto);
		return "/stuwork/dorm/stat/dyDormDayList.ftl";
	}
	
	//********************分割线 ************以后下考核汇总
	@ResponseBody
	@RequestMapping("/dorm/stat/setWeekList")
	@ControllerInfo(value = "setWeekList")
	public String setWeekList(ModelMap map,DormSearchDto dormDto){
		try{
			List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester
					(getLoginInfo().getUnitId(), dormDto.getAcadyear(), Integer.parseInt(dormDto.getSemesterStr())),new TR<List<DateInfo>>(){});
			if(CollectionUtils.isNotEmpty(dateInfoList)){
				int allWeek=dateInfoList.get(dateInfoList.size()-1).getWeek();
				JSONArray array=new JSONArray();
				for(int i=1;i<=allWeek;i++){
					JSONObject json=new JSONObject();
					json.put("week", i);
					array.add(json);
				}
				JSONObject json=new JSONObject();
				json.put("weekList", array);
				return json.toJSONString();
			}
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
		
	}
	@RequestMapping("/dorm/stat/index/page")
	@ControllerInfo(value = "index")
	public String statIndex(ModelMap map,DormSearchDto dormDto){
		String unitId=getLoginInfo().getUnitId();
		dormDto.setUnitId(unitId);
		//一个为空代表 初始刚进入
		if(StringUtils.isBlank(dormDto.getAcadyear())){
			Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,unitId),new TypeReference<Semester>(){});//TODO
			if(semester!=null){
				dormDto.setAcadyear(semester.getAcadyear());
				dormDto.setSemesterStr(String.valueOf(semester.getSemester()));
			}else{
				return errorFtl(map,"当前时间不在学年学期范围内");
			}
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, 
					dormDto.getAcadyear(), semester.getSemester(),DateUtils.string2Date(DateUtils.date2String(new Date()),"yyyy-MM-dd")), DateInfo.class);
			dormDto.setWeek(dateInfo!=null?dateInfo.getWeek():1);
		}
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		
		List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester
				(unitId, dormDto.getAcadyear(), Integer.parseInt(dormDto.getSemesterStr())),new TR<List<DateInfo>>(){});
		if(CollectionUtils.isNotEmpty(dateInfoList)){
			int allWeek=dateInfoList.get(dateInfoList.size()-1).getWeek();
			List<Integer> weekList=new ArrayList<Integer>();
			for(int i=1;i<=allWeek;i++){
				weekList.add(i);
			}
			map.put("weekList", weekList);
		}
		
		map.put("acadyearList", acadyearList);
		map.put("dormDto", dormDto);
		return "/stuwork/dorm/stat/dyDormStatIndex.ftl";
	}
	@RequestMapping("/dorm/stat/list")
	@ControllerInfo(value = "statList")
	public String statList(ModelMap map,HttpServletRequest request,DormSearchDto dormDto){
		
		dormDto.setUnitId(getLoginInfo().getUnitId());
		List<DyDormStatResult> statList=dyDormStatResultService.getStat(dormDto);
		Float scoreAverage=new Float(0);
		Float rewardAverage=new Float(0);
		if(CollectionUtils.isNotEmpty(statList)){
			int lengtgh=statList.size();
			for(DyDormStatResult stat:statList){
				scoreAverage+=stat.getScoreALL();
				rewardAverage+=stat.getRewardAll();
			}
			scoreAverage=scoreAverage/lengtgh;
			rewardAverage=rewardAverage/lengtgh;
		}
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(dormDto.getClassId()),Clazz.class);
		if(clazz!=null){
			Grade grade=SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
			String className=grade!=null?grade.getGradeName():"";
			className+=clazz.getClassName();
			map.put("className", className);
		}
		map.put("scoreAverage", scoreAverage);
		map.put("rewardAverage", rewardAverage);
		map.put("statList", statList);
		map.put("dormDto", dormDto);
		return "/stuwork/dorm/stat/dyDormStatList.ftl";
	}
	@RequestMapping("/dorm/stat/statDetail")
	@ControllerInfo(value = "statDetail")
	public String statDetail(ModelMap map,String id){
		DyDormStatResult stat=dyDormStatResultService.findOne(id);
		if(stat==null){
			stat=new DyDormStatResult();
		}
		map.put("stat", stat);
		return "/stuwork/dorm/stat/dyDormStatDetail.ftl";
	}
	@Scheduled(cron="0 0 1 * * SUN") //每周日凌晨1点执行
	public void setStat(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(new Date()));
		//统计一周的数据
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);
		if(unit == null){
			System.out.println("找不到顶级单位：统计结束！");
			return;
		}
		//获得所有下属学校
		List<Unit> units = SUtils.dt(unitRemoteService.findByUnionId
				(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
		if(CollectionUtils.isEmpty(units)){
			System.out.println(unit.getUnitName()+"没有下属学校，统计结束！");
		}
		try {
			for(Unit u : units){
				System.out.println(u.getUnitName()+"开始统计...");
				Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0, u.getId()), Semester.class);
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
				dyDormStatResultService.save(u.getId(),
						se.getAcadyear(), se.getSemester()+"", dateInfo.getWeek());
				System.out.println(u.getUnitName()+"统计结束...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("统计出错...");
		}
        System.out.println("统计完成");  
	}
	@ResponseBody
	@RequestMapping("/dorm/stat/save")
	@ControllerInfo(value = "statSave")
	public String statSave(ModelMap map,HttpServletRequest request,DormSearchDto dormDto){
		try{
			dyDormStatResultService.save(getLoginInfo().getUnitId(),
					dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getWeek());
			
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
		
	}
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
}
