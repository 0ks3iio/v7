package net.zdsoft.stuwork.remote.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.dto.DyQualityComprehensiveDto;
import net.zdsoft.stuwork.data.dto.DyStuworkDataCountDto;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.entity.DyStudentLeave;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;
import net.zdsoft.stuwork.data.service.DyCollectService;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormCheckRemindService;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyQualityComprehensiveService;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;
import net.zdsoft.stuwork.data.service.DyStuHealthResultService;
import net.zdsoft.stuwork.data.service.DyStudentLeaveService;
import net.zdsoft.stuwork.data.service.DyStudentRewardPointService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service("stuworkRemoteService")
public class StuworkRemoteServiceImpl implements StuworkRemoteService{
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyCourseRecordService dyCourseRecordService;
	@Autowired
	private DyWeekCheckResultService dyWeekCheckResultService;
	@Autowired
	private DyWeekCheckItemService dyWeekCheckItemService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormCheckResultService dyDormCheckResultService;
	@Autowired
	private DyDormCheckRemindService dyDormCheckRemindService;
	@Autowired
	private DyStudentLeaveService dyStudentLeaveService;
	@Autowired
	private DyCollectService dyCollectService;
	
	@Autowired
	private DyStudentRewardPointService dyStudentRewardPointService;
	@Autowired
	private DyQualityComprehensiveService dyQualityComprehensiveService;
	@Autowired
	private DyPermissionService dyPermissionService;
	@Autowired
	private DyStuEvaluationService dyStuEvaluationService;
	@Autowired
	private DyStuHealthResultService dyStuHealthResultService;
//	@Autowired
//	private DyStudentRewardProjectService dyStudentRewardProjectService;
//	@Autowired
//	private DyStudentRewardSettingService dyStudentRewardSettingService;
	@Autowired
	private DyClassstatWeekService dyClassstatWeekService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	
	@Override
	public String findStuIdsByBuiId(String unitId,String buildingId,String acadyear,String semesterStr){
		List<DyDormRoom>  roomList=dyDormRoomService.findByName(unitId, null, buildingId,StuworkConstants.STU_TYPE);
		Set<String> roomIds=new HashSet<String>();
		for(DyDormRoom room:roomList){
			roomIds.add(room.getId());
		}
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByRoomIds(unitId, roomIds.toArray(new String[0]), acadyear, semesterStr);
		Set<String> studentIds=new HashSet<String>();
		for(DyDormBed bed:bedList){
			studentIds.add(bed.getOwnerId());//获取所有的学生ids
		}
		return SUtils.s(studentIds);
	}
	@Override
	public String findStuIdRnameMapByBuiId(String unitId,String buildingId,String acadyear,String semesterStr){
		List<DyDormRoom>  roomList=dyDormRoomService.findByName(unitId, null, buildingId,StuworkConstants.STU_TYPE);
		Set<String> roomIds=new HashSet<String>();
		for(DyDormRoom room:roomList){
			roomIds.add(room.getId());
		}
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByRoomIds(unitId, roomIds.toArray(new String[0]), acadyear, semesterStr);
		Map<String,String> roomNameMap=EntityUtils.getMap(roomList, "id","roomName");
		JSONArray array=new JSONArray();
		for(DyDormBed bed:bedList){
			JSONObject json=new JSONObject();
			json.put("studentId", bed.getOwnerId());//studentId 对应的roomName
			json.put("roomName", roomNameMap.get(bed.getRoomId()));//studentId 对应的roomName
			array.add(json);
		}
		return SUtils.s(array);
	}
	
	@Override
	public String findClassRemind(String classId,Date checkDate,String unitId,String acadyear,String semesterStr){
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
		return SUtils.s(reList);
	}
	@Override
	public String findClassResult(String classId,Date inputDate,String unitId,String acadyear,String semesterStr){
		Map<String,String> map=dyDormCheckResultService.getClassResult(classId,DateUtils.date2String(inputDate),unitId,acadyear,semesterStr);
		List<String[]> reList = new ArrayList<String[]>();
		String[] obj = new String[]{"5", map.get("score"),map.get("remark")};
		reList.add(obj);
		return SUtils.s(reList);
	}
	@Override
	public String findRemindByBuildingId(String buildingId,Date checkDate,String unitId){
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(unitId, buildingId, null,StuworkConstants.STU_TYPE, null);
		Map<String, DyDormCheckRemind> map =dyDormCheckRemindService.getRemindMap(unitId, buildingId, DateUtils.date2String(checkDate));
		JSONArray array=new JSONArray();
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				DyDormCheckRemind remind=map.get(room.getId());
				if(remind!=null && StringUtils.isNotBlank(remind.getRemark())){
					JSONObject json=new JSONObject();
					json.put("roomName", room.getRoomName());
					json.put("remark", remind.getRemark());
					array.add(json);
				}
			}
		}
		return SUtils.s(array);
	}
	@Override
	public String findResultByBuildingId(String buildingId,Date inputDate,String unitId){
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(unitId, buildingId, null,StuworkConstants.STU_TYPE, null);
		Map<String,String> map=dyDormCheckResultService.getResultListForMap(unitId, buildingId, DateUtils.date2String(inputDate));
		JSONArray array=new JSONArray();
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				String checkResult=map.get(room.getId());
				if(StringUtils.isNotBlank(checkResult)){
					String[] ss=checkResult.split(",");
					if(ss.length>1){
						JSONObject json=new JSONObject();
						json.put("roomName", room.getRoomName());
						json.put("score", ss[0]);
						json.put("remark", ss[1]);
						array.add(json);
					}
				}
			}
		}
		return SUtils.s(array);
	}
	@Override
	public String findIsInResidence(String studentId,String unitId,String acadyear,String semesterStr){
		DyDormBed bed=dyDormBedService.getbedByStudentId(studentId, unitId, acadyear, semesterStr);
		JSONObject json=new JSONObject();
		boolean flag=true;
		if(bed==null){
			flag=false;
		}
		json.put("isInResidence", flag);
		return SUtils.s(json);
	}
	@Override
	public String findbuildRoomByStudentId(String studentId,String unitId,String acadyear,String semesterStr){
		JSONObject json=new JSONObject();
		if(StringUtils.isBlank(acadyear)){
			Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId),Semester.class);
			acadyear=se.getAcadyear();
			semesterStr=se.getSemester()+"";
		}
		DyDormBed bed=dyDormBedService.getbedByStudentId(studentId, unitId, acadyear, semesterStr);
		if(bed!=null){
			String roomId=bed.getRoomId();
			DyDormRoom room=dyDormRoomService.findOne(roomId);
			if(room!=null){
				String buildingId=room.getBuildingId();
				DyDormBuilding building=dyDormBuildingService.findOne(buildingId);
				
				json.put("buildingId",buildingId);
				json.put("buildingName", building==null?"":building.getName());
				json.put("roomId", roomId);
				json.put("roonName", room.getRoomName());
			}
		}
		return SUtils.s(json);
	}
	@Override
	public String findStuRoomNameByClassIds(String unitId, String acadyear, String semester, String[] classIds) {
		List<String[]> reList = new ArrayList<String[]>();
		List<DyDormBed> bedlist = dyDormBedService.findByClassIds(unitId,acadyear,semester,classIds);
		Set<String> roomIds = EntityUtils.getSet(bedlist, "roomId");
		Map<String,DyDormRoom> roomMap = dyDormRoomService.findMapByIdIn(roomIds.toArray(new String[0]));
		String[] obj = null;
		for(DyDormBed bed : bedlist){
			obj = new String[3];
			obj[0] = bed.getOwnerId();
			obj[1] = bed.getClassId();
			obj[2] = roomMap.containsKey(bed.getRoomId())?roomMap.get(bed.getRoomId()).getRoomName():"";
			reList.add(obj);
		}
		return SUtils.s(reList);
	}
	@Override
	public String findStuRoomByClassIds(String unitId, String acadyear, String semester, String[] classIds) {
		List<String[]> reList = new ArrayList<String[]>();
		List<DyDormBed> bedlist = dyDormBedService.findByClassIds(unitId,acadyear,semester,classIds);
		Set<String> roomIds = EntityUtils.getSet(bedlist, "roomId");
		Map<String,DyDormRoom> roomMap = dyDormRoomService.findMapByIdIn(roomIds.toArray(new String[0]));
		String[] obj = null;
		for(DyDormBed bed : bedlist){
			obj = new String[3];
			obj[0] = bed.getOwnerId();
			obj[1] = bed.getClassId();
			obj[2] = roomMap.containsKey(bed.getRoomId())?roomMap.get(bed.getRoomId()).getBuildingId():"";
			reList.add(obj);
		}
		return SUtils.s(reList);
	}
	
	@Override
	public String findRoomNumAndStuNumByBuildingId(String unitId,
			String acadyear, String semester, String buildingId) {
		JSONObject json=new JSONObject();
		DyDormBuilding building = dyDormBuildingService.findOne(buildingId);
		if(building == null){
			json.put("buildingName","无寝室楼");
		}else{
			json.put("buildingName", building.getName());
		}
		List<DyDormRoom> roomlist = dyDormRoomService.findByName(unitId,null,buildingId,StuworkConstants.STU_TYPE);
		if(CollectionUtils.isEmpty(roomlist)){
			json.put("roomNums", 0);	
			json.put("studentNums", 0);
			return SUtils.s(json);
		}else{
			json.put("roomNums", roomlist.size());
		}
		Set<String> roomIds = EntityUtils.getSet(roomlist, "id");
		List<DyDormBed> bedlist = dyDormBedService.findStudentByRoomIds(unitId,acadyear,semester,roomIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(bedlist)){
			json.put("studentNums", 0);
		}else{
			json.put("studentNums", bedlist.size());	
		}
		
		return SUtils.s(json);
	}
	
	
	@Override
	public String findWeekCheckByClassId(String unitId, String acadyear,String semester,String classId,
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
			return SUtils.s(reList);
		}
		List<DyWeekCheckResult> result  = dyWeekCheckResultService.findByClassIdAndCheckDate(unitId, acadyear, semester, classId, dutyDate);
		List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolAndDay(unitId, dateInfo.getWeekday()); 
		Map<String,DyWeekCheckItem> itemMap = EntityUtils.getMap(items, "id");
		float score1 = 0f;
		String remark1 = "";
		float score2 = 0f;
		String remark2 = "";
		DecimalFormat decimalFormat=new DecimalFormat(".0");
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
//		infos2[1] = score2+"";
		infos1[2] = remark1;
		infos2[2] = remark2;
		reList.add(infos1);
		reList.add(infos2);
		return SUtils.s(reList);
	}
	
	
	@Override
	public String getBuildingSbyUnitId(String unitId) {
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
		JSONArray array=new JSONArray();
		if(CollectionUtils.isNotEmpty(buildingList)){
			for(DyDormBuilding building:buildingList){
				JSONObject json=new JSONObject();
				json.put("buildingId", building.getId());
				json.put("buildingName", building.getName());
				array.add(json);
			}
		}
		return SUtils.s(array);
	}
	
	
	
	@Override
	public String getCourseRecordData(String unitId, String acadyear,
			String semester, Date queryDate, String classId) {
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
			return SUtils.s(reList);
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
			nightScore = "";
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
	    return SUtils.s(reList);
	}
	@Override
	public String getStudentLeaveDate(String studentId) {
		List<DyStudentLeave> dyStudentLeaveList = dyStudentLeaveService.findDyStudentLeaveByStartTime(studentId, 3);
		DyStudentLeave dyStudentLeave = new DyStudentLeave();
		if(dyStudentLeaveList.size()>0){
			dyStudentLeave = dyStudentLeaveList.get(0);
		}
		JSONObject json=new JSONObject();
		json.put("dyStudentLeave", dyStudentLeave);
		JSONArray array=new JSONArray();
		array.add(json);
	    return SUtils.s(array);
	}

	@Override
	public String findDyDormBedByUnitId(String unitId,
			String acadyear, String semester, String[] studentIds) {
		return SUtils.s(dyDormBedService.findDyDormBedByUnitId(unitId, acadyear, semester, studentIds));
	}

	@Override
	public String findDyDormByIds(String[] dormIds) {		
		return SUtils.s(dyDormRoomService.findListByIdIn(dormIds));
	}
	
	
	@Override
	public String findStuworkCountByStudentId(Map<String, Integer> maxValueMap, String studentId, Integer maxValuePer, boolean isShow) {
		
		//Student student = studentR
		//老王的接口
		DyQualityComprehensiveDto dto = dyQualityComprehensiveService.getQualityScoreMap(maxValueMap, studentId, isShow);
		DyStuworkDataCountDto dyStuworkDataCountDto = dyStudentRewardPointService.findStuworkCountByStudentId(maxValueMap,studentId,maxValuePer, isShow);
		
		Float[] countFloats = dyStuworkDataCountDto.getCountNumbers();
		if(countFloats[0]!=null){
			if(dto.getCountNum()!=null){
				countFloats[0] = countFloats[0]+dto.getCountNum();
			}
		}else{
			if(dto.getCountNum()!=null){
				countFloats[0] = dto.getCountNum();
			}
		}
		
		dyStuworkDataCountDto.setCountNumbers(countFloats);

		Map<String,List<String[]>> infoMap =dyStuworkDataCountDto.getInfoMap();
		List<String[]> info1 = infoMap.get(DyStuworkDataCountDto.STUWORK_LIST);
		List<String[]> info2 = dto.getValueList();
		for (int j=0;j<info1.size();j++) {
			String[] strings = info1.get(j);
			String[] strings2 = info2.get(j);
			strings[0] = strings2[0];
			strings[10] = strings2[1];
			strings[8] = strings2[2];
			strings[12] = strings2[3];
			strings[14] = strings2[4];
			strings[1] = strings2[5];
			strings[11] = strings2[6];
			strings[9] = strings2[7];
			strings[13] = strings2[8];
			strings[15] = strings2[9];
		}
		infoMap.put(DyStuworkDataCountDto.STUWORK_LIST, info1);
		dyStuworkDataCountDto.setInfoMap(infoMap);
//		JSONObject json=new JSONObject();
//		json.put("dyStuworkDataCountDto", dyStuworkDataCountDto);
		
		//对所有的值保留2位小数
		Float[] a = dyStuworkDataCountDto.getCountNumbers();
		for (int i = 0; i < a.length; i++) {
			a[i] = Float.valueOf(saveTwoFloat(a[i]));
		}
		dyStuworkDataCountDto.setCountNumbers(a);
		Map<String,List<String[]>> infoMap1 = dyStuworkDataCountDto.getInfoMap();
		for (String key : infoMap1.keySet()) {
			List<String[]> list = infoMap1.get(key);
			for (String[] array : list) {
				for (int j=1;j< array.length;j=j+2) {
					array[j] = saveTwoFloat(Float.valueOf(array[j]));
				}
			}
		}
		
		return  SUtils.s(dyStuworkDataCountDto);
	}
	@Override
	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear){
		dyQualityComprehensiveService.setRecoverStuScore(studentIds,stuYearMap,acadyear);
		dyStudentRewardPointService.setRecoverStuScore(studentIds,stuYearMap,acadyear);
	}
	
	@Override
	public String findStuworkCountByUnitId(Map<String, Integer> maxValueMap, String unitId, Integer maxValuePer, Map<String, Boolean> showMap) {
		
		Map<String, Float> studentMap1 = dyStudentRewardPointService.findStuworkCountByUnitId(maxValueMap, unitId,maxValuePer,showMap);
		Map<String, Float> studentMap2 = dyQualityComprehensiveService.getAllStudentQualityScoreMap(maxValueMap, unitId,showMap);
		if(studentMap1.keySet().size()>0){
			for (String key : studentMap1.keySet()) {
				Float float1 = studentMap1.get(key);
				Float float2 = studentMap2.get(key);
				if(float1==null){
					float1=(float)0;
				}
				if(float2==null){
					float2=(float)0;
				}
				studentMap1.put(key, Float.valueOf(saveTwoFloat(float1+float2)));
			}
			
			return  SUtils.s(studentMap1);
		}else{
			return  SUtils.s(studentMap2);
		}
		
	
	}
	
	
	public String saveTwoFloat(float a){
		if(a!=0){
			
			DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
			String p=decimalFormat.format(a);//format 返回的是字符串
			
			return p;
		}else{
			return "0";
		}
	}
	@Override
	public Set<String> findClassSetByUserId(String userId) {
		return dyPermissionService.findClassSetByUserId(userId);
	}
	public String findCollectList2(String unitId,String acadyear,String semester,String[] studentIds){
		return SUtils.s(dyCollectService.findCollectList2(unitId, acadyear, semester, studentIds));
	}
	public String findCollectList4(String unitId,String acadyear,String semester,String[] studentIds){
		return SUtils.s(dyCollectService.findCollectList4(unitId, acadyear, semester, studentIds));
	}
	@Override
	public Set<String> findClassSetByUserIdClaType(String userId, String classsType, String permissionType) {

		return dyPermissionService.findClassSetByUserIdClaType(userId,classsType,permissionType);
	}

	@Override
	public String findStuEvaluationListByUidAndDto(String unitId, DyBusinessOptionDto dto) {
		List<DyStuEvaluation> dyStuEvaluationList = dyStuEvaluationService.findListByUidAndDto(unitId, dto);
		return SUtils.s(dyStuEvaluationList);
	}
	@Override
	public String findStuEvaluationOneByUidAndDto(String unitId, DyBusinessOptionDto dto) {
		DyStuEvaluation dyStuEvaluation = dyStuEvaluationService.findOneByUidAndDto(unitId, dto);
		return SUtils.s(dyStuEvaluation);
	}

	@Override
	public String findStuEvaluationListByUnitIdAndStudentIds(String unitId, String acadyear, String semester, String[] studentIds) {
		List<DyStuEvaluation> dyStuEvaluations = dyStuEvaluationService.findByStudentIdIn(studentIds, unitId, acadyear, semester);
		return SUtils.s(dyStuEvaluations);
	}

	@Override
	public String findStuHealthResultOneByStudnetId(String unitId, String acadyear,
			String semester, String studentId) {
		return SUtils.s(dyStuHealthResultService.findOneByStudnetId(unitId, acadyear, semester, studentId));
	}
	@Override
	public String findStuHealthResultListByStudentIds(String unitId, String acadyear,
			String semester, String[] studentIds) {
		return SUtils.s(dyStuHealthResultService.findListByStudentIds(unitId, acadyear, semester, studentIds));
	}
	@Override
	public String findStuworkCountByStudentIds(Map<String, Integer> maxValueMap, String classId, String[] studentIds, Integer maxValuePer, boolean isShow) {
		Map<String, DyQualityComprehensiveDto> dtoMap = dyQualityComprehensiveService.getQualityScoreMapByStudentIds(maxValueMap, classId, studentIds, isShow);
		Map<String, DyStuworkDataCountDto> dyStuworkDataCountDtoMap = dyStudentRewardPointService.findStuworkCountByStudentIds(maxValueMap,classId,studentIds,maxValuePer, isShow);
		for (String studentId : studentIds) {
			DyQualityComprehensiveDto dto = dtoMap.get(studentId);
			DyStuworkDataCountDto dyStuworkDataCountDto = dyStuworkDataCountDtoMap.get(studentId);
			Float[] countFloats = dyStuworkDataCountDto.getCountNumbers();
			if(countFloats[0]!=null){
				if(dto.getCountNum()!=null){
					countFloats[0] = countFloats[0]+dto.getCountNum();
				}
			}else{
				if(dto.getCountNum()!=null){
					countFloats[0] = dto.getCountNum();
				}
			}
			
			dyStuworkDataCountDto.setCountNumbers(countFloats);
			
			Map<String,List<String[]>> infoMap =dyStuworkDataCountDto.getInfoMap();
			List<String[]> info1 = infoMap.get(DyStuworkDataCountDto.STUWORK_LIST);
			List<String[]> info2 = dto.getValueList();
			for (int j=0;j<info1.size();j++) {
				String[] strings = info1.get(j);
				String[] strings2 = info2.get(j);
				strings[0] = strings2[0];
				strings[10] = strings2[1];
				strings[8] = strings2[2];
				strings[12] = strings2[3];
				strings[14] = strings2[4];
				strings[1] = strings2[5];
				strings[11] = strings2[6];
				strings[9] = strings2[7];
				strings[13] = strings2[8];
				strings[15] = strings2[9];
			}
			infoMap.put(DyStuworkDataCountDto.STUWORK_LIST, info1);
			dyStuworkDataCountDto.setInfoMap(infoMap);
//				JSONObject json=new JSONObject();
//				json.put("dyStuworkDataCountDto", dyStuworkDataCountDto);
			
			//对所有的值保留2位小数
			Float[] a = dyStuworkDataCountDto.getCountNumbers();
			for (int i = 0; i < a.length; i++) {
				a[i] = Float.valueOf(saveTwoFloat(a[i]));
			}
			dyStuworkDataCountDto.setCountNumbers(a);
			Map<String,List<String[]>> infoMap1 = dyStuworkDataCountDto.getInfoMap();
			for (String key : infoMap1.keySet()) {
				List<String[]> list = infoMap1.get(key);
				for (String[] array : list) {
					for (int j=1;j< array.length;j=j+2) {
						array[j] = saveTwoFloat(Float.valueOf(array[j]));
					}
				}
			}
		}
		
		return  SUtils.s(dyStuworkDataCountDtoMap);
	}
	@Override
	public String findXkjsByStudentIds(String unitId, String acadyear, String semester, String[] studentIds) {
		Map<String, String> map = dyStudentRewardPointService.findXkjsByStudentIds(unitId, acadyear, semester, studentIds);
		for (Entry<String, String> entry : map.entrySet()) {
				if(StringUtils.isNotBlank(entry.getValue())){
					map.put(entry.getKey(), saveTwoFloat(Float.valueOf(entry.getValue())));
				}
		}
		return  SUtils.s(map);
	}
}
