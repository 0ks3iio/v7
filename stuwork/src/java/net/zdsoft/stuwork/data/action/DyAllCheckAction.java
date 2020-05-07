package net.zdsoft.stuwork.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dto.AllCheckDto;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyAllCheck;
import net.zdsoft.stuwork.data.entity.DyClassOtherCheck;
import net.zdsoft.stuwork.data.service.DyAllCheckService;
import net.zdsoft.stuwork.data.service.DyClassOtherCheckService;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/stuwork/allCheck")
public class DyAllCheckAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyAllCheckService dyAllCheckService;
	@Autowired
	private DyClassstatWeekService dyClassstatWeekService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private DyClassOtherCheckService dyClassOtherCheckService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@Scheduled(cron="0 0 3 * * SUN") //每周日凌晨3点执行
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
				//dyClassstatWeekService.statByUnitId(u.getId(), se.getAcadyear(), se.getSemester()+"", dateInfo.getWeek());
				dyAllCheckService.saveStat(u.getId(), se.getAcadyear(), se.getSemester()+"", dateInfo.getWeek());
				System.out.println(u.getUnitName()+"统计结束...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("统计出错...");
		}
        System.out.println("统计完成");  
    }  
	@ResponseBody
	@RequestMapping("/statSave/page")
	@ControllerInfo(value = "统计")
	public String statSave(DormSearchDto dormDto){
		System.out.println("统计开始......");
		try {
			dyClassstatWeekService.statByUnitId(getLoginInfo().getUnitId(),  dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getWeek());
			dyAllCheckService.saveStat(getLoginInfo().getUnitId(), dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getWeek());
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		System.out.println("统计结束......");
		return returnSuccess();
	}
	
	@RequestMapping("/statIndex/page")
	public String statIndex(ModelMap map,DormSearchDto dormDto){
		String unitId=getLoginInfo().getUnitId();
		dormDto.setUnitId(unitId);
		//一个为空代表 初始刚进入
		if(StringUtils.isBlank(dormDto.getAcadyear())){
			Semester semester=SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId),Semester.class);
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
		return "/stuwork/allCheck/allCheckStatIndex.ftl";
	}
	@RequestMapping("/statList/page")
	public String statList(ModelMap map,DormSearchDto dormDto){
		
		dormDto.setUnitId(getLoginInfo().getUnitId());
		
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(dormDto.getClassId()),Clazz.class);
		
		DyAllCheck allCheck=dyAllCheckService.getAllCheckBy(getLoginInfo().getUnitId(), dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getWeek(), dormDto.getClassId());
		List<DyClassOtherCheck>  otherCheckList=dyClassOtherCheckService.findByClassIdAndWeek(dormDto.getClassId(), dormDto.getWeek(), dormDto.getAcadyear(), Integer.parseInt(dormDto.getSemesterStr()));
		
		if(allCheck==null) allCheck=new DyAllCheck();
		if(clazz!=null){
			map.put("className", clazz.getClassName());
		}
		map.put("dormDto", dormDto);
		map.put("otherCheckList", otherCheckList);
		map.put("allCheck", allCheck);
		
		return "/stuwork/allCheck/allCheckStatList.ftl";
	}
	
	@RequestMapping("/rankIndex/page")
	public String rankIndex(){
		return "/stuwork/allCheck/allCheckRankIndex.ftl";
	}
	
	@RequestMapping("/rankList/page")
	public String rankList(ModelMap map,DormSearchDto dormDto){
		String unitId=getLoginInfo().getUnitId();
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
		List<Clazz> classListAllSection=SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, dormDto.getAcadyear()), new TR<List<Clazz>>(){});
//		Map<String, Grade> gradeMap= EntityUtils.getMap(SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){}),"id");
		List<Clazz> classList=new ArrayList<Clazz>();
		if(CollectionUtils.isNotEmpty(classListAllSection)){
			for(Clazz clazz:classListAllSection){
				if(clazz.getSection()==section){
//					String gradeName=gradeMap.containsKey(clazz.getGradeId())?gradeMap.get(clazz.getGradeId()).getGradeName():"";
//					clazz.setClassNameDynamic(gradeName+clazz.getClassName());
					classList.add(clazz);
				}
			}
//			Collections.sort(classList,new Comparator<Clazz>(){
//				@Override
//				public int compare(Clazz o1, Clazz o2) {
//					return o1.getClassNameDynamic().compareTo(o2.getClassNameDynamic());
//				}
//				
//			});
		}
		
		List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester
				(unitId, dormDto.getAcadyear(), Integer.parseInt(dormDto.getSemesterStr())),new TR<List<DateInfo>>(){});
		int allWeek=0;
		if(CollectionUtils.isNotEmpty(dateInfoList)){
			allWeek=dateInfoList.get(dateInfoList.size()-1).getWeek();
		}else{
			return errorFtl(map, "当前学年学期 未维护周次");
		}
		List<AllCheckDto> dtoList=dyAllCheckService.getListBy(unitId, dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getSection(), allWeek);
		map.put("dtoList", dtoList);
		map.put("sectionList", sectionList);
		map.put("acadyearList", acadyearList);
		map.put("dormDto", dormDto);
		map.put("classList", classList);
		return "/stuwork/allCheck/allCheckRankList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/setWeekList")
	public String setWeekList(String acadyear,String semesterStr){
		try{
			List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester
					(getLoginInfo().getUnitId(), acadyear, Integer.parseInt(semesterStr)),new TR<List<DateInfo>>(){});
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
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
}
