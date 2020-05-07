package net.zdsoft.stuwork.data.action;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.dto.DyClassStatListDto;
import net.zdsoft.stuwork.data.dto.DyDormFormWeekDto;
import net.zdsoft.stuwork.data.dto.DyWeekCheckResultDto;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;
import net.zdsoft.stuwork.data.entity.DyDormForm;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.entity.DyDormStatResult;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormCheckRemindService;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;
import net.zdsoft.stuwork.data.service.DyDormStatResultService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/eccShow") 
public class DyEccLoadAction extends BaseAction{
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DyWeekCheckItemService dyWeekCheckItemService;
	@Autowired
	private DyWeekCheckResultService dyWeekCheckResultService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyCourseRecordService dyCourseRecordService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormCheckRemindService dyDormCheckRemindService;
	@Autowired
	private DyDormCheckResultService dyDormCheckResultService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private DyClassstatWeekService dyClassstatWeekService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyDormStatResultService dyDormStatResultService;
	@RequestMapping("/classdy/classpage")
	public String showClassPageAdmin(String unitId, String classId,String view, ModelMap map){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date queryDate=cal.getTime();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		boolean showWs = false;
		boolean showJl = false;
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			String semester = String.valueOf(sem.getSemester());
			DyClassstatWeek dyClassstatWeek = dyClassstatWeekService.findBySchoolIdAndAcadyearAndSemesterAndClassIdAndDate(unitId, acadyear, semester, classId, new Date());
			if(dyClassstatWeek!=null){
				showWs = dyClassstatWeek.getIsHealthExcellen();
				showJl = dyClassstatWeek.getIsDisciplineExcellen();
			}
			List<String[]> weekCheak = findWeekCheckByClassId(unitId, acadyear, semester, classId, queryDate);//1 值周卫生 2值周纪律]
			List<String[]> courseRecord = getCourseRecordData(unitId, acadyear, semester, queryDate, classId);//上课日志
			List<String[]> classResult = findClassResult(classId, queryDate, unitId, acadyear, semester);//班级扣分
			List<String[]> classRemind = findClassRemind(classId, queryDate, unitId, acadyear, semester);//班级提醒
			map.put("weekCheak", weekCheak);
			map.put("courseRecord", courseRecord);
			map.put("classResult", classResult);
			map.put("classRemind", classRemind);
		}
		map.put("showWs", showWs);
		map.put("showJl", showJl);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/moraleducation/eccClassDyLoad.ftl";
		}else{
			return "/eclasscard/show/moraleducation/eccClassDyLoad.ftl";
		}
		
	}
	
	//昨日扣分寝室
	@RequestMapping("/classdy/dormscorepage")
	public String showDormScorePageAdmin(String unitId, String view,String buildingId, ModelMap map){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date queryDate=cal.getTime();
		List<String[]> dormscoreList = findResultByBuildingId(buildingId, queryDate, unitId);
		map.put("dormscoreList", dormscoreList);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/moraleducation/eccDormScoreDyLoad.ftl";
		}else{
			return "/eclasscard/show/moraleducation/eccDormScoreDyLoad.ftl";
		}
	}
	
	//昨日提醒寝室
	@RequestMapping("/classdy/dormremindpage")
	public String showDormRemindPageAdmin(String unitId,String view, String buildingId, ModelMap map){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date queryDate=cal.getTime();
		List<String[]> dormremindList = findRemindByBuildingId(buildingId, queryDate, unitId);
		map.put("dormremindList", dormremindList);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/moraleducation/eccDormRemindDyLoad.ftl";
		}else{
			return "/eclasscard/show/moraleducation/eccDormRemindDyLoad.ftl";
		}
	}
	
	//寝室扣分
	public List<String[]> findResultByBuildingId(String buildingId,Date inputDate,String unitId){
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(unitId, buildingId, null,StuworkConstants.STU_TYPE, null);
		Map<String,String> map=dyDormCheckResultService.getResultListForMap(unitId, buildingId, DateUtils.date2String(inputDate));
		List<String[]> reList = new ArrayList<String[]>();
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				String checkResult=map.get(room.getId());
				if(StringUtils.isNotBlank(checkResult)){
					String[] ss=checkResult.split(",");
					if(ss.length>1){
						String[] array = new String[3];
						array[0] = room.getRoomName();
						array[1] = ss[0];
						array[2] = ss[1];
						reList.add(array);
					}
				}
			}
		}
		return reList;
	}
	
	//寝室提醒
	public List<String[]> findRemindByBuildingId(String buildingId,Date checkDate,String unitId){
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(unitId, buildingId, null,StuworkConstants.STU_TYPE, null);
		Map<String, DyDormCheckRemind> map =dyDormCheckRemindService.getRemindMap(unitId, buildingId, DateUtils.date2String(checkDate));
		List<String[]> reList = new ArrayList<String[]>();
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				DyDormCheckRemind remind=map.get(room.getId());
				if(remind!=null && StringUtils.isNotBlank(remind.getRemark())){					
					String[] array = new String[2];
					array[0] = room.getRoomName();
					array[1] = remind.getRemark();
					reList.add(array);
				}
			}
		}
		return reList;
	}
	
	//班级提醒
	public List<String[]> findClassRemind(String classId,Date checkDate,String unitId,String acadyear,String semesterStr){
		List<String[]> reList = new ArrayList<String[]>();
		String allRemark="";
		
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByCon(classId, unitId, acadyear, semesterStr,StuworkConstants.STU_TYPE);
		if(CollectionUtils.isNotEmpty(bedList)){
			Set<String>  roomIds=new HashSet<String>();
			for(DyDormBed bed:bedList){
				roomIds.add(bed.getRoomId());
			}
			List<DyDormCheckRemind>  remindList=dyDormCheckRemindService.getRemindByCon(unitId, DateUtils.date2String(checkDate), roomIds.toArray(new String[0]));
			List<DyDormRoom> roomList=dyDormRoomService.findListByIds(roomIds.toArray(new String[0]));
			Map<String,String> roomNameMap=EntityUtils.getMap(roomList,"id","roomName");
			if(CollectionUtils.isNotEmpty(remindList)){
				int i=0;
				for(DyDormCheckRemind remind:remindList){
					String roomName=roomNameMap.get(remind.getRoomId());
					String remark=remind.getRemark();
					if(StringUtils.isBlank(roomName)|| StringUtils.isBlank(remark)){
						continue;
					}
					String str="";
					if(i!=0) str="、";
					allRemark+=str+roomName+":"+remark;
					i++;
				}
			}
		}
		String[] obj = new String[]{"6","",allRemark};
		reList.add(obj);
		return reList;
	}
		
	//班级扣分
	public List<String[]> findClassResult(String classId,Date inputDate,String unitId,String acadyear,String semesterStr){
		Map<String,String> map=dyDormCheckResultService.getClassResult(classId,DateUtils.date2String(inputDate),unitId,acadyear,semesterStr);
		List<String[]> reList = new ArrayList<String[]>();
		String[] obj = new String[]{"5", map.get("score"),map.get("remark")};
		reList.add(obj);
		return reList;
	}
		
	
	//上课日志数据
	public List<String[]> getCourseRecordData(String unitId, String acadyear, String semester, Date queryDate, String classId) {
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
		int periodNum = sem.getMornPeriods() + sem.getAmPeriods() + sem.getPmPeriods() + sem.getNightPeriods();
		DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, Integer.parseInt(semester), queryDate), DateInfo.class);
		List<String[]> reList = new ArrayList<String[]>();
		//3上课日志 2晚自习日志
		String[] infos1 = new String[]{"3","",""};
		String[] infos2 = new String[]{"4","",""};
		if(dateInfo == null){
			reList.add(infos1);
			reList.add(infos2);
			return reList;
		}
		int week = dateInfo.getWeek();
		int day = dateInfo.getWeekday();
		List<DyCourseRecord> dyCourseRecordList = dyCourseRecordService.findListByRecordClassId(unitId, acadyear, semester, week, day, "1", classId);
		List<CourseSchedule> courseScheduleListResult = new ArrayList<CourseSchedule>();
		for(int i=1;i<=periodNum;i++){
			int count = 0;
			float num = 0;
			float score;
			CourseSchedule item = new CourseSchedule();
			if(dyCourseRecordList.size()>0){
				for(DyCourseRecord item2 : dyCourseRecordList){
    				if(item2.getPeriod() == i){
    					count = count + 1;
    					num = num + item2.getScore();
    				}
    			}
				score = num/count;
				if(String.valueOf(score).equals("NaN")){
					item.setScore(String.valueOf(""));
				}else{
					item.setScore(String.valueOf(score).split("\\.")[0]);
				}
			}else{
				item.setScore(String.valueOf(""));
			}
			courseScheduleListResult.add(item);
		}
		int scoreNum = 0;
		for(CourseSchedule item :  courseScheduleListResult){
			if(StringUtils.isNotBlank(item.getScore())){
				scoreNum = scoreNum + Integer.parseInt(item.getScore());
			}
		}
		//晚自习
		List<DyCourseRecord> dyCourseRecordList2 = dyCourseRecordService.findListByRecordClassId(unitId, acadyear, semester, week, day, "2", classId);    		
		String nightScore = "";
		String remark = "";
		if(dyCourseRecordList2.size()>0){
			nightScore = String.valueOf(dyCourseRecordList2.get(0).getScore());
			remark = dyCourseRecordList2.get(0).getRemark();
		}else{
			nightScore = "0";
			remark = "";
		}
		JSONObject json=new JSONObject();
		json.put("scoreNum", scoreNum);
		json.put("nightScore", nightScore);
		infos1[1]  =scoreNum + "";
		infos2[1] = nightScore;
		infos2[2] = remark;//晚自习日志备注
		reList.add(infos1);
		reList.add(infos2);
	    return reList;
	}
	
	/**
	 * 找到行政班一天内各个考核项的扣分情况,只会返回被扣分的数据
	 * 1 值周卫生 2值周纪律
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classId
	 * @param dutyDate
	 * @return
	 */
	public List<String[]> findWeekCheckByClassId(String unitId, String acadyear,String semester,String classId,
			Date dutyDate) {
		List<String[]> reList = new ArrayList<String[]>();
		//1 值周卫生 2值周纪律
		String[] infos1 = new String[]{"1","",""};
		String[] infos2 = new String[]{"2","",""};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dutyDate =  sdf.parse(sdf.format(dutyDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, NumberUtils.toInt(semester),dutyDate), DateInfo.class);
		if(dateInfo == null){
			reList.add(infos1);
			reList.add(infos2);
			return reList;
		}
		List<DyWeekCheckResult> result  = dyWeekCheckResultService.findByClassIdAndCheckDate(unitId, acadyear, semester, classId, dutyDate);
		List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolAndDay(unitId, dateInfo.getWeekday()); 
		Map<String,DyWeekCheckItem> itemMap = EntityUtils.getMap(items, "id");
		float score1 = 0f;
		String remark1 = "";
		float score2 = 0f;
		String remark2 = "";
		DecimalFormat decimalFormat=new DecimalFormat("0.0");
		for(DyWeekCheckResult re : result){
			if(itemMap.get(re.getItemId()).getType() == 1){
				score1 = score1 - re.getScore();
				if(StringUtils.isBlank(re.getRemark())){
					continue;
				}
				if(StringUtils.isBlank(remark1)){
					remark1 = re.getRemark();
				}else{
					remark1 = remark1 + "、"+re.getRemark();
				}
				if(re.getScore() > 0f){
					remark1 = remark1+ "-"+decimalFormat.format(re.getScore());
				}else{
					remark1 = remark1 +"；";
				}
			}else{
				score2 = score2 - re.getScore();
				if(StringUtils.isBlank(re.getRemark())){
					continue;
				}
				if(StringUtils.isBlank(remark2)){
					remark2 = re.getRemark();
				}else{
					remark2 = remark2 + "、"+re.getRemark();
				}
				if(re.getScore() > 0f){
					remark2 = remark2+ "-"+decimalFormat.format(re.getScore());
				}else{
					remark2 = remark2 +"；";
				}
			}
		}
		if(score1 < 0f){
			infos1[1]  =decimalFormat.format(score1);
		}else{
			infos1[1]  ="0";
		}
		if(score2 < 0f){
			infos2[1]  =decimalFormat.format(score2);
		}else{
			infos2[1]  ="0";
		}
		infos1[2] = remark1;
		infos2[2] = remark2;
		reList.add(infos1);
		reList.add(infos2);
		return reList;
	}
	
	@RequestMapping("/classSpace/dyCheckHead")
	public String findWeekCheckDataHead(String unitId, String classId, String week, ModelMap map){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, semester, new Date()), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, semester),new TR<List<DateInfo>>(){});
			if(CollectionUtils.isNotEmpty(dateInfoList)){
				int allWeek=dateInfoList.get(dateInfoList.size()-1).getWeek();
				List<Integer> weekList=new ArrayList<Integer>();
				for(int i=1;i<=allWeek;i++){
					weekList.add(i);
				}
				map.put("weekList", weekList);
			}
			if(StringUtils.isBlank(week)){
				week = String.valueOf(dateInfo.getWeek());
			}
			map.put("week", Integer.parseInt(week));
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			map.put("unitId", unitId);
			map.put("classId", classId);
		}else{
			return errorFtl(map, "所选日期不在当前学期范围内！");
		}
		return "/eclasscard/show/classspace/dycheckHead.ftl";
	}
	
	//德育考核
	@RequestMapping("/classSpace/dyCheckList")
	public String findWeekCheckDataList(String unitId, String acadyear, String semester, String classId, String week, ModelMap map){
		Map<String,String> scoreMap = new HashMap<String, String>();
		Map<String,String> remarkMap = new HashMap<String, String>();
		Map<String,String> scoreMap2 = new HashMap<String, String>();
		
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classId), new TR<List<Clazz>>(){});
		String className = clazzs.get(0).getClassNameDynamic();
		List<DyClassStatListDto> resultDto = dyClassstatWeekService.findClassTableDto(unitId,acadyear,String.valueOf(semester),classId,Integer.parseInt(week));
		List<DyClassStatListDto> resultDto1 = new ArrayList<DyClassStatListDto>();
		List<DyClassStatListDto> resultDto2 = new ArrayList<DyClassStatListDto>();
		List<DyClassStatListDto> resultDto3 = new ArrayList<DyClassStatListDto>();
		for(DyClassStatListDto dto : resultDto){
			if("1".equals(dto.getItemType())){//卫生
				resultDto1.add(dto);
			}else if("2".equals(dto.getItemType())){//纪律
				resultDto2.add(dto);
			}else{
				resultDto3.add(dto);
			}
			List<DyWeekCheckResultDto> dtoList = dto.getDtos();
			for(DyWeekCheckResultDto dto2 : dtoList){
				if("上课日志".equals(dto.getItemName()) && "3".equals(dto.getItemType())){
					if(StringUtils.isNotBlank(dto2.getRemark())){
						remarkMap.put("skrz"+dto2.getDay(), "（备注："+dto2.getRemark().substring(0, dto2.getRemark().length()-1)+"）");
					}
					if(dto2.isAllUnSubmint()){
						scoreMap.put("skrz"+dto2.getDay(), "");
					}else if(dto2.isUnCheck()){
						scoreMap.put("skrz"+dto2.getDay(), "/");	
					}else{
						scoreMap.put("skrz"+dto2.getDay(), dto2.getScore()+"");	
					}
				}else if("晚自习日志".equals(dto.getItemName()) && "3".equals(dto.getItemType())){
					if(StringUtils.isNotBlank(dto2.getRemark())){
						remarkMap.put("wzxrz"+dto2.getDay(), "（备注："+dto2.getRemark().substring(0, dto2.getRemark().length()-1)+"）");
					}
					if(dto2.isAllUnSubmint()){
						scoreMap.put("wzxrz"+dto2.getDay(), "");
					}else if(dto2.isUnCheck()){
						scoreMap.put("wzxrz"+dto2.getDay(), "/");	
					}else{
						scoreMap.put("wzxrz"+dto2.getDay(), dto2.getScore()+"");	
					}
				}else{						
					remarkMap.put(dto.getItemId()+dto2.getDay(), dto2.getRemark());
					if(dto2.isAllUnSubmint()){
						scoreMap.put(dto.getItemId()+dto2.getDay(), "");
					}else if(dto2.isUnCheck()){
						scoreMap.put(dto.getItemId()+dto2.getDay(), "/");
					}else{
						scoreMap.put(dto.getItemId()+dto2.getDay(), dto2.getScore()+"");
					}
				}
			}
		}
		map.put("resultDto1", resultDto1);//卫生
		map.put("resultDto2", resultDto2);//纪律
		map.put("resultDto3", resultDto3);//纪律
		map.put("className", className);
		DyClassstatWeek statWeek = dyClassstatWeekService.findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(unitId,acadyear,String.valueOf(semester),classId, Integer.parseInt(week));		
		if(statWeek == null){
			map.put("isHealthExcellen", false);//是否有卫生小红旗
			map.put("isDisciplineExcellen", false);//是否有纪律小红旗
			map.put("disciplineRank", 0);//纪律排名
			map.put("healthRank", 0);//卫生排名
			map.put("disciplineScore", 0f);//纪律总分
			map.put("healthScore", 0f);//卫生总分
		}else{
			map.put("isHealthExcellen", statWeek.getIsHealthExcellen());
			map.put("isDisciplineExcellen", statWeek.getIsDisciplineExcellen());
			map.put("disciplineScore", statWeek.getDisciplineScore());
			map.put("healthScore", statWeek.getHealthScore());
			map.put("disciplineRank", statWeek.getDisciplineRank());
			map.put("healthRank", statWeek .getHealthRank());
		}
		List<DyDormFormWeekDto> dtoList = new ArrayList<DyDormFormWeekDto>();
		DormSearchDto dormDto = new DormSearchDto();
		dormDto.setAcadyear(acadyear);
		dormDto.setSemesterStr(String.valueOf(semester));
		dormDto.setWeek(Integer.parseInt(week));
		dormDto.setSection(clazzs.get(0).getSection());
		dormDto.setUnitId(unitId);
		dormDto.setClassId(classId);
		Map<String, DyDormForm> formMap = new HashMap<String, DyDormForm>();
		List<DyDormForm> formList = dyDormCheckResultService.getResRemFormWeek(dormDto);
		for(DyDormForm form : formList){
			formMap.put(form.getClassId(), form);
		}
		DyDormForm form = formMap.get(classId);
		for(int i=1;i<=7;i++){
			if(null!=form){					
				List<DyDormForm> formList2 = form.getFormList();
				String str1 = "";//表扬
				String str21 = "";//批评
				String str22 = "";//提醒
				String str3 = "";//其他
				for(DyDormForm form2 : formList2){
					if(form2.isExcellent() && StringUtils.isBlank(form2.getCheckRemind()) && i==form2.getDay()){
						str1 = str1 + form2.getRoomName() + "  ";
					}
					if(!form2.isExcellent() && StringUtils.isNotBlank(form2.getCheckResult()) && i==form2.getDay()){
						str21 = str21 + form2.getRoomName() + "（"+form2.getCheckResult()+"）";
					}
					if(StringUtils.isNotBlank(form2.getCheckRemind()) && i==form2.getDay()){
						str22 = str22 + form2.getRoomName() + "（"+form2.getCheckRemind()+"）";
					}
					if(StringUtils.isNotBlank(form2.getOtherInfo()) && i==form2.getDay()){
						str3 = str3 + form2.getRoomName() + "（"+form2.getOtherInfo()+"）"+"  ";
					}
				}
				//if(StringUtils.isNotBlank(str1)){
					DyDormFormWeekDto dto1 = new DyDormFormWeekDto();
					dto1.setDay(i);
					dto1.setItemType("1");
					dto1.setData(str1);
					dtoList.add(dto1);
				//}
				//if(StringUtils.isNotBlank(str21) || StringUtils.isNotBlank(str22)){
					DyDormFormWeekDto dto2 = new DyDormFormWeekDto();
					dto2.setDay(i);
					dto2.setItemType("2");
					if(StringUtils.isNotBlank(str21)){
						str21 = "扣分："+str21;
					}
					if(StringUtils.isNotBlank(str22)){
						str22 = "提醒："+str22;
					}
					dto2.setData(str21+str22);
					dtoList.add(dto2);
				//}
				//if(StringUtils.isNotBlank(str3)){
					DyDormFormWeekDto dto3 = new DyDormFormWeekDto();
					dto3.setDay(i);
					dto3.setItemType("3");
					dto3.setData(str3);
					dtoList.add(dto3);
				//}
			}
			map.put("dtoList", dtoList);
			for(DyDormFormWeekDto dto : dtoList){
				scoreMap2.put(dto.getItemType()+i, dto.getData());
			}
		}
		
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByCon(classId, unitId, acadyear, semester,StuworkConstants.STU_TYPE);
		Set<String> roomIdSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(bedList)){
			for(DyDormBed bed:bedList){
				roomIdSet.add(bed.getRoomId());
			}
		}
		List<DyDormRoom> roomList = new ArrayList<DyDormRoom>();
		if(CollectionUtils.isNotEmpty(roomIdSet)){			
			roomList=dyDormRoomService.findListByIds(roomIdSet.toArray(new String[0]));
		}
		Map<String,DyDormStatResult> statResultMap = new HashMap<String, DyDormStatResult>();
		List<DyDormStatResult> statList=dyDormStatResultService.getStat(dormDto);
		float allScoreCount=0;
		float excellentScoreCount=0;
		for(DyDormStatResult res : statList){
			statResultMap.put(res.getRoomId(),res);
			if(null!=res.getScoreALL()){
				allScoreCount = allScoreCount + res.getScoreALL();				
			}	
			if(null!=res.getExcellentScore()){				
				excellentScoreCount = excellentScoreCount + res.getExcellentScore();
			}
		}
		float allScoreAvg=0;
		float excellentScoreAvg=0;
		if(CollectionUtils.isNotEmpty(statList)){
			allScoreAvg = allScoreCount/(statList.size());
			excellentScoreAvg = excellentScoreCount/(statList.size());
		}

		Map<String,DyDormCheckResult> checkResultMap = new HashMap<String, DyDormCheckResult>();
		if(CollectionUtils.isNotEmpty(roomIdSet)){			
			checkResultMap = dyDormCheckResultService.getResultMapWeek(unitId, acadyear, semester, Integer.parseInt(week), roomIdSet.toArray(new String[0]));
		}
		map.put("allScoreAvg", allScoreAvg);
		map.put("excellentScoreAvg", excellentScoreAvg);
		map.put("roomList", roomList);
		map.put("checkResultMap", checkResultMap);
		map.put("statResultMap", statResultMap);
			
		map.put("remarkMap", remarkMap);
		map.put("scoreMap", scoreMap);
		map.put("scoreMap2", scoreMap2);
		map.put("classId", classId);
		map.put("unitId", unitId);
		return "/eclasscard/show/classspace/dycheck.ftl";
	}

}
