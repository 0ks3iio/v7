package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyDormCheckResultDao;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;
import net.zdsoft.stuwork.data.entity.DyDormForm;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormCheckRemindService;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("dyDormCheckResultService")
public class DyDormCheckResultServiceImpl extends BaseServiceImpl<DyDormCheckResult, String> implements  DyDormCheckResultService{
	@Autowired
	private DyDormCheckResultDao dyDormCheckResultDao;
	@Autowired
	private DyDormCheckRemindService dyDormCheckRemindService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	
	
	public Map<String,String> getClassResult(String classId,String inputDate,String schoolId,String acadyear,String semesterStr){
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByCon(classId, schoolId, acadyear, semesterStr,StuworkConstants.STU_TYPE);
		Map<String,String> map=new HashMap<String, String>(); 
		if(CollectionUtils.isNotEmpty(bedList)){
			Set<String>  roomIds=new HashSet<String>();
			for(DyDormBed bed:bedList){
				roomIds.add(bed.getRoomId());
			}
			List<DyDormRoom> roomList=dyDormRoomService.findListByIds(roomIds.toArray(new String[0]));
			Map<String, String>  roomNameMap=EntityUtils.getMap(roomList, "id","roomName");
			
			List<DyDormCheckResult> resultList=dyDormCheckResultDao.findByCon(schoolId, DateUtils.string2Date(inputDate,"yyyy-MM-dd"),roomIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(resultList)){
				int score=0;
				String checkResult="";
				int i=0;
				for(DyDormCheckResult result:resultList){
					boolean flag=false;
					int markDec=0;
					int jlScore= result.getJlScore()==null?0:(int)(result.getJlScore()*10);
					int nwScore= result.getJlScore()==null?0:(int)(result.getNwScore()*10);
					int wsScore= result.getJlScore()==null?0:(int)(result.getWsScore()*10);
					String remark=result.getRemark();
					String roomId=result.getRoomId(); 
					if(remark==null){
						remark="";
					}
					if(jlScore!=100){
						markDec+=100-jlScore;
						flag=true;
					}
					if(nwScore!=100){
						markDec+=100-nwScore;
						flag=true;
					}
					if(wsScore!=100){
						markDec+=100-wsScore;
						flag=true;
					}
					if(!flag){
						continue;
					}
					score+=markDec;
					String str="";
					if(i!=0)  str="、";
					checkResult+=str+roomNameMap.get(roomId)+"("+"扣"+(float)markDec/10+"分 "+remark+")";
					i++;
				}
				map.put("score", (float)score/10+"");
				map.put("remark", checkResult);
			}
		}
		return map;
	}
	@Override
	public List<DyDormForm> getResRemForm(DormSearchDto dormDto){
		List<DyDormForm> formList=new ArrayList<DyDormForm>();
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByProCon
									(dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr(),StuworkConstants.STU_TYPE);
		int searchSection=dormDto.getSection();
		//boolean flag=StringUtils.isNotBlank(searchClassId);
		if(CollectionUtils.isNotEmpty(bedList)){
			List<DyDormCheckResult> resultList=findListBy(new String[]{"schoolId","acadyear","semester","week","day"}, 
					new Object[]{dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr(),dormDto.getWeek(),dormDto.getWeekDay()});
			//key-roomId  value考核情况 空则代表 无扣分
			Map<String,String> resultStrMap=getResultStrMap(resultList,false);
			//key-roomId  value考核情况 空则代表 无扣分
			Map<String,DyDormCheckRemind> remindMap=dyDormCheckRemindService.getRemindMap(dormDto);
			List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(dormDto.getUnitId(),StuworkConstants.STU_TYPE);
			Map<String, String>  roomNameMap=EntityUtils.getMap(roomList, "id","roomName");
			
			List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolId(dormDto.getUnitId()), new TR<List<Clazz>>(){});
			Map<String,Clazz> classMap=EntityUtils.getMap(classList,"id");
			
			//通过寝室床位得到 所有的classId 对应的roomIds
			Map<String,Set<String>> bedMap=new HashMap<String, Set<String>>();
			Set<String> classIds=new HashSet<String>();
			for(DyDormBed bed:bedList){
				String classId=bed.getClassId();
				Clazz clazz=classMap.get(classId);
				
				String roomId=bed.getRoomId();
				Set<String> roomIdSet=bedMap.get(classId);
				if(roomIdSet==null) 
					roomIdSet=new HashSet<String>();
				
				roomIdSet.add(roomId);
				if(clazz==null){
					continue;
				}else{
					if(clazz.getSection()==searchSection){
						bedMap.put(classId, roomIdSet);
						classIds.add(classId);
					}
				}
			}
			Set<Entry<String, Set<String>>> entries=bedMap.entrySet();
			DyDormCheckRemind remind=null;
			for(Entry<String, Set<String>> entry:entries){
				String classId=entry.getKey();
				DyDormForm dormForm=new DyDormForm();
				List<DyDormForm> inFormList=new ArrayList<DyDormForm>();
				Set<String> roomIdSet=entry.getValue();
				for(String roomId:roomIdSet){
					DyDormForm indormForm=new DyDormForm();
					indormForm.setRoomId(roomId);
					indormForm.setCheckResult(resultStrMap.get(roomId));
					remind=remindMap.get(roomId);
					if(remind!=null){
						indormForm.setCheckRemind(remind.getRemark());
						indormForm.setOtherInfo(remind.getOtherInfo());
					}
					indormForm.setExcellent(resultStrMap.get(roomId)=="");//空的代表 优秀
					//indormForm.setClassId(classId);
					indormForm.setRoomName(roomNameMap.get(roomId));
					//indormForm.setClassName("");
					inFormList.add(indormForm);
				}
				if(CollectionUtils.isNotEmpty(inFormList)){
					Collections.sort(inFormList,(o1,o2)->{
						return o1.getRoomName().compareTo(o2.getRoomName());
					});
				}
				Clazz clazz=classMap.get(classId);
				if(clazz!=null){
					dormForm.setClassAcadyear(clazz.getAcadyear());
					dormForm.setClassName(clazz.getClassNameDynamic());
					dormForm.setClassCode(clazz.getClassCode());
				}else{
					
				}
				dormForm.setClassId(classId);
				dormForm.setFormList(inFormList);
				formList.add(dormForm);
			}
		}
		Collections.sort(formList, new Comparator<DyDormForm>(){
			@Override
			public int compare(DyDormForm o1, DyDormForm o2) {
				if(StringUtils.isNotBlank(o2.getClassAcadyear())&& StringUtils.isNotBlank(o1.getClassAcadyear())){
					if(o2.getClassAcadyear().compareTo(o1.getClassAcadyear())!=0){
						return o2.getClassAcadyear().compareTo(o1.getClassAcadyear());
					}else if(StringUtils.isNotBlank(o2.getClassCode())&& StringUtils.isNotBlank(o1.getClassCode())){
						return o1.getClassCode().compareTo(o2.getClassCode());
					}
				}
				return 0;
			}
		});
		return formList;
	}
	@Override
	public String doCheckImport(String schoolId, String acadyear, String semester,String userId,String buildingId, String searchDate, List<String[]> datas){
			//  值周班导入数据处理
			Json importResultJson=new Json();
			List<String[]> errorDataList=new ArrayList<String[]>();
			int successCount  =0;
			String[] errorData=null;
			
			Date date=DateUtils.string2Date(searchDate,"yyyy-MM-dd");
			//得到星期几 周次 初始化
			DateInfo dateInfo=SUtils.dt(dateInfoRemoteService.findByDate(schoolId, acadyear,Integer.parseInt(semester)
								, date),new TypeReference<DateInfo>() {});
			int	week=0;//TODO
			int	day=0;
			if(dateInfo!=null){
				week=dateInfo.getWeek();
				day=dateInfo.getWeekday();
			}
			
			List<DyDormRoom> roomList=dyDormRoomService.findByName(schoolId, null, buildingId,StuworkConstants.STU_TYPE);
			//key - roomName
			Map<String,DyDormRoom> roomMap=EntityUtils.getMap(roomList, "roomName"); 
			//得到某天 某寝室楼下的考勤信息  为得到key为roomId的map
			Map<String,DyDormCheckResult> resultMap=getResultMap(schoolId, buildingId,searchDate);
			
			List<DyDormCheckResult> insertList=new ArrayList<DyDormCheckResult>();
			Set<DyDormCheckResult> deleteIds=new HashSet<DyDormCheckResult>();
			Map<String,String> testMap=new HashMap<String, String>();//判断room是否重复
			DyDormCheckResult result=null;
			for(int i =0;i< datas.size();i++){
				String[] dataArr = datas.get(i);
				int length=dataArr.length;
				String roomName=length>0?dataArr[0]:"";
				if(StringUtils.isNotBlank(roomName)){
					roomName=roomName.trim();
				}else{
					roomName="";
				}
				DyDormRoom room=roomMap.get(roomName);
				if(room==null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="寝室号";
					errorData[2]=roomName;
					errorData[3]="寝室号名称有误";
					errorDataList.add(errorData);
					continue;
				}
				String str=testMap.get(room.getId());
				if(StringUtils.isNotBlank(str)){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="寝室号";
					errorData[2]=dataArr[0];
					errorData[3]="寝室号重复";
					errorDataList.add(errorData);
					continue;
				}
				testMap.put(room.getId(),"one");
				
				if(length<2 || StringUtils.isBlank(dataArr[1])){
					errorData = new String[4];
					errorData[0]=errorDataList.size() +1 +"";
					errorData[1]="卫生";
					errorData[2]="";
					errorData[3]="卫生分数不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Pattern pattern = Pattern.compile("^[+]?([0-9]+(.[0-9]{1})?)$");
				Float ws=NumberUtils.toFloat(dataArr[1],1);
				boolean wsFlag=pattern.matcher(dataArr[1]).matches();
				if(ws<0 || ws>10 || !wsFlag){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="卫生";
					errorData[2]=dataArr[1];
					if(wsFlag){
						errorData[3]="卫生分数不在有效范围内，有效范围为0~"+10;
					}else{
						errorData[3]="卫生分数只能是数字且最多一位小数";
					}
					errorDataList.add(errorData);
					continue;
				}
				if(length<3 ||StringUtils.isBlank(dataArr[2])){
					errorData = new String[4];
					errorData[0]=errorDataList.size() +1 +"";
					errorData[1]="内务";
					errorData[2]="";
					errorData[3]="内务分数不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Float nw=NumberUtils.toFloat(dataArr[2],1);
				boolean nwFlag=pattern.matcher(dataArr[2]).matches();
				if(nw<0 || nw>10 || !nwFlag){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="内务";
					errorData[2]=dataArr[2];
					if(nwFlag){
						errorData[3]="内务分数不在有效范围内，有效范围为0~"+10;
					}else{
						errorData[3]="内务分数只能是数字且最多一位小数";
					}
					errorDataList.add(errorData);
					continue;
				}
				if(length<4 ||StringUtils.isBlank(dataArr[3])){
					errorData = new String[4];
					errorData[0]=errorDataList.size() +1 +"";
					errorData[1]="纪律";
					errorData[2]="";
					errorData[3]="纪律分数不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Float jl=NumberUtils.toFloat(dataArr[3],1);
				boolean jlFlag=pattern.matcher(dataArr[3]).matches();
				if(jl<0 || jl>10 || !jlFlag){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="纪律";
					errorData[2]=dataArr[3];
					if(jlFlag){
						errorData[3]="纪律分数不在有效范围内，有效范围为0~"+10;
					}else{
						errorData[3]="纪律分数只能是数字且最多一位小数";
					}
					errorDataList.add(errorData);
					continue;
				}
				
				String remark=length>4?remark=dataArr[4]:"";
				if(StringUtils.isNotBlank(remark) && remark.length()>100){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="扣分";
					errorData[2]=remark;
					errorData[3]="扣分不能超过100个字符";
					errorDataList.add(errorData);
					continue;
				}
				result=new DyDormCheckResult();
				result.setSchoolId(schoolId);
				result.setAcadyear(acadyear);
				result.setSemester(semester);
				result.setBuildingId(buildingId);
				result.setRoomId(room.getId());
				result.setWsScore(ws);
				result.setNwScore(nw);
				result.setJlScore(jl);
				result.setOperatorId(userId);
				result.setRemark(remark);
				result.setWeek(week);
				result.setDay(day);
				result.setInputDate(date);
				insertList.add(result);
				DyDormCheckResult deleteResult=resultMap.get(room.getId());
				if(deleteResult!=null){
					deleteIds.add(deleteResult);
				}
				successCount++;
			}
			if(CollectionUtils.isNotEmpty(deleteIds)){
				deleteAll(deleteIds.toArray(new DyDormCheckResult[0]));
			}
			if(CollectionUtils.isNotEmpty(insertList)){
				DyDormCheckResult[] inserts = insertList.toArray(new DyDormCheckResult[0]);
				checkSave(inserts);
				saveAll(inserts);
			}
			importResultJson.put("totalCount", datas.size());
			importResultJson.put("successCount", successCount);
			importResultJson.put("errorCount", errorDataList.size());
			importResultJson.put("errorData", errorDataList);
			return importResultJson.toJSONString();
			
	}
	@Override
	public void saveResultAndRemindList(DormSearchDto dormDto,String unitId,String userId) {
		List<DyDormCheckResult> resultList=dormDto.getResultList();
		List<DyDormCheckRemind> remindList=dormDto.getRemindList();
		
		String acadyear=dormDto.getAcadyear();
		String semesterStr=dormDto.getSemesterStr();
		int week=dormDto.getWeek();
		int day=dormDto.getDay();
		String buildingId=dormDto.getSearchBuildId();
		Date searchDate=dormDto.getSearchDate();
		//---保存考勤表---
		//一页的resultIds
		Set<String> resultIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(DyDormCheckResult result:resultList){
				if(StringUtils.isNotBlank(result.getId())){
					resultIds.add(result.getId());
				}
				result.setId(UuidUtils.generateUuid());
				result.setSchoolId(unitId);
				result.setAcadyear(acadyear);
				result.setSemester(semesterStr);
				result.setWeek(week);
				result.setDay(day);
				result.setBuildingId(buildingId);
				result.setOperatorId(userId);
				result.setInputDate(searchDate);
			}
		}
		if(CollectionUtils.isNotEmpty(resultIds)){
			dyDormCheckResultDao.deletedByIds(resultIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(resultList)){			
			saveAll(resultList.toArray(new DyDormCheckResult[0]));
		}
		
		//---保存提醒表---
		//一页的remindIds
		Set<String> remindIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(remindList)){
			for(DyDormCheckRemind remind:remindList){
				if(StringUtils.isNotBlank(remind.getId())){
					remindIds.add(remind.getId());
				}
				remind.setId(UuidUtils.generateUuid());
				remind.setSchoolId(unitId);
				remind.setAcadyear(acadyear);
				remind.setSemester(semesterStr);
				remind.setWeek(week);
				remind.setDay(day);
				remind.setBuildingId(buildingId);
				remind.setOperatorId(userId);
				remind.setCheckDate(searchDate);
			}
		}
		if(CollectionUtils.isNotEmpty(remindIds)){
			dyDormCheckRemindService.deleteByIds(remindIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(remindList)){			
			dyDormCheckRemindService.saveAll(remindList.toArray(new DyDormCheckRemind[0]));
		}
		
	}
	@Override
	public Map<String,DyDormCheckResult> getResultMap(String schoolId,String buildingId,String inputDate){
		//得到某天 某寝室楼下的考勤信息  为得到key为roomId的map
		//List<DyDormCheckResult> resultList=findListBy(new String[]{"schoolId","buildingId","week","day"}
											//,new Object[]{schoolId,buildingId,week,day});
		List<DyDormCheckResult> resultList=dyDormCheckResultDao.findByCon(schoolId, buildingId, DateUtils.string2Date(inputDate,"yyyy-MM-dd"));
		Map<String,DyDormCheckResult> resultMap=new HashMap<String,DyDormCheckResult>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(DyDormCheckResult result:resultList){
				resultMap.put(result.getRoomId(), result);
			}
		}
		return resultMap;
	}
	@Override
	public Map<String,String> getResultListForMap(String schoolId,String buildingId,String inputDate){
		List<DyDormCheckResult> resultList=dyDormCheckResultDao.findByCon(schoolId, buildingId, DateUtils.string2Date(inputDate,"yyyy-MM-dd"));
		return getResultStrMap(resultList,true);//true代表 返回stuworkremote需要的数据
	}
	public Map<String,String> getResultStrMap(List<DyDormCheckResult> resultList,boolean flag){
		//key-roomId  value考核情况 空则代表 无扣分
		Map<String,String> resultStrMap=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(DyDormCheckResult result:resultList){
				String checkResult="";
				int markDec=0;
				int jlScore= result.getJlScore()==null?0:(int)(result.getJlScore()*10);
				int nwScore= result.getJlScore()==null?0:(int)(result.getNwScore()*10);
				int wsScore= result.getJlScore()==null?0:(int)(result.getWsScore()*10);
				String remark=result.getRemark();
				remark=remark==null?"":":"+remark;
				if(jlScore!=100){
					markDec+=100-jlScore;
					checkResult+=" 纪律-"+(float)(100-jlScore)/10;
				}
				if(nwScore!=100){
					markDec+=100-nwScore;
					checkResult+=" 内务-"+(float)(100-nwScore)/10;
				}
				if(wsScore!=100){
					markDec+=100-wsScore;
					checkResult+=" 卫生-"+(float)(100-wsScore)/10;
				}
				if(StringUtils.isNotBlank(checkResult)){
					if(flag){
						checkResult=(float)markDec/10+","+checkResult+remark;
					}else{
						//checkResult="扣"+(float)markDec/10+"分("+checkResult+remark+")";
						checkResult=checkResult+remark;
					}
				}
				resultStrMap.put(result.getRoomId(), checkResult);
			}
		}
		return resultStrMap;
	}
	
	public Map<String,String> getResultStrMap2(List<DyDormCheckResult> resultList,boolean flag){
		//key-roomId  value考核情况 空则代表 无扣分
		Map<String,String> resultStrMap=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(DyDormCheckResult result:resultList){
				String checkResult="";
				int markDec=0;
				int jlScore= result.getJlScore()==null?0:(int)(result.getJlScore()*10);
				int nwScore= result.getJlScore()==null?0:(int)(result.getNwScore()*10);
				int wsScore= result.getJlScore()==null?0:(int)(result.getWsScore()*10);
				String remark=result.getRemark();
				remark=remark==null?"":":"+remark;
				if(jlScore!=100){
					markDec+=100-jlScore;
					checkResult+=" 纪律-"+(float)(100-jlScore)/10;
				}
				if(nwScore!=100){
					markDec+=100-nwScore;
					checkResult+=" 内务-"+(float)(100-nwScore)/10;
				}
				if(wsScore!=100){
					markDec+=100-wsScore;
					checkResult+=" 卫生-"+(float)(100-wsScore)/10;
				}
				if(StringUtils.isNotBlank(checkResult)){
					if(flag){
						checkResult=(float)markDec/10+","+checkResult+remark;
					}else{
						//checkResult="扣"+(float)markDec/10+"分("+checkResult+remark+")";
						checkResult=checkResult+remark;
					}
				}
				resultStrMap.put(result.getRoomId()+result.getDay(), checkResult);
			}
		}
		return resultStrMap;
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyDormCheckResult, String> getJpaDao() {
		return dyDormCheckResultDao;
	}
	
	@Override
	protected Class<DyDormCheckResult> getEntityClass() {
		return DyDormCheckResult.class;
	}
	@Override
	public List<DyDormForm> getResRemFormWeek(DormSearchDto dormDto) {
		List<DyDormForm> formList=new ArrayList<DyDormForm>();
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByCon
									(dormDto.getClassId(),dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr(),StuworkConstants.STU_TYPE);
		Set<String> roomIdSet2 = new HashSet<String>();
		for(DyDormBed bed : bedList){
			roomIdSet2.add(bed.getRoomId());
		}
		int searchSection=dormDto.getSection();
		if(CollectionUtils.isNotEmpty(bedList)){
			List<DyDormCheckResult> resultList=dyDormCheckResultDao.findByCon(dormDto.getUnitId(), dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getWeek(), roomIdSet2.toArray(new String[0]));
			//key-roomId  value考核情况 空则代表 无扣分
			Map<String,String> resultStrMap=getResultStrMap2(resultList,false);
			//key-roomId  value考核情况 空则代表 无扣分
			Map<String,DyDormCheckRemind> remindMap=dyDormCheckRemindService.getRemindWeekMap(dormDto,roomIdSet2.toArray(new String[0]));
			//List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(dormDto.getUnitId(),StuworkConstants.STU_TYPE);
			List<DyDormRoom> roomList=dyDormRoomService.findListByIds(roomIdSet2.toArray(new String[0]));
			Map<String, String>  roomNameMap=EntityUtils.getMap(roomList, "id","roomName");
			
			List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolId(dormDto.getUnitId()), new TR<List<Clazz>>(){});
			Map<String,Clazz> classMap=EntityUtils.getMap(classList,"id");
			
			//通过寝室床位得到 所有的classId 对应的roomIds
			Map<String,Set<String>> bedMap=new HashMap<String, Set<String>>();
			Set<String> classIds=new HashSet<String>();
			for(DyDormBed bed:bedList){
				String classId=bed.getClassId();
				Clazz clazz=classMap.get(classId);
				
				String roomId=bed.getRoomId();
				Set<String> roomIdSet=bedMap.get(classId);
				if(roomIdSet==null) 
					roomIdSet=new HashSet<String>();
				
				roomIdSet.add(roomId);
				if(clazz==null){
					continue;
				}else{
					if(clazz.getSection()==searchSection){
						bedMap.put(classId, roomIdSet);
						classIds.add(classId);
					}
				}
			}
			Set<Entry<String, Set<String>>> entries=bedMap.entrySet();
			DyDormCheckRemind remind=null;
			for(Entry<String, Set<String>> entry:entries){
				String classId=entry.getKey();
				DyDormForm dormForm=new DyDormForm();
				List<DyDormForm> inFormList=new ArrayList<DyDormForm>();
				Set<String> roomIdSet=entry.getValue();
				for(int i=1;i<=7;i++){
					
					for(String roomId:roomIdSet){
						DyDormForm indormForm=new DyDormForm();
						indormForm.setRoomId(roomId);
						indormForm.setCheckResult(resultStrMap.get(roomId+i));
						indormForm.setDay(i);
						remind=remindMap.get(roomId+i);
						if(remind!=null){
							indormForm.setCheckRemind(remind.getRemark());
							indormForm.setOtherInfo(remind.getOtherInfo());
						}
						indormForm.setExcellent(resultStrMap.get(roomId+i)=="");//空的代表 优秀
						//indormForm.setClassId(classId);
						indormForm.setRoomName(roomNameMap.get(roomId));
						//indormForm.setClassName("");
						inFormList.add(indormForm);
					}
				}
				if(CollectionUtils.isNotEmpty(inFormList)){
					Collections.sort(inFormList,(o1,o2)->{
						return o1.getRoomName().compareTo(o2.getRoomName());
					});
				}
				Clazz clazz=classMap.get(classId);
				if(clazz!=null){
					dormForm.setClassAcadyear(clazz.getAcadyear());
					dormForm.setClassName(clazz.getClassNameDynamic());
					dormForm.setClassCode(clazz.getClassCode());
				}else{
					
				}
				dormForm.setClassId(classId);
				dormForm.setFormList(inFormList);
				formList.add(dormForm);
			}
		}
		Collections.sort(formList, new Comparator<DyDormForm>(){
			@Override
			public int compare(DyDormForm o1, DyDormForm o2) {
				if(StringUtils.isNotBlank(o2.getClassAcadyear())&& StringUtils.isNotBlank(o1.getClassAcadyear())){
					if(o2.getClassAcadyear().compareTo(o1.getClassAcadyear())!=0){
						return o2.getClassAcadyear().compareTo(o1.getClassAcadyear());
					}else if(StringUtils.isNotBlank(o2.getClassCode())&& StringUtils.isNotBlank(o1.getClassCode())){
						return o1.getClassCode().compareTo(o2.getClassCode());
					}
				}
				return 0;
			}
		});
		return formList;
	}
	@Override
	public Map<String, DyDormCheckResult> getResultMapWeek(String schoolId,
			String acadyear, String semester, int week, String[] roomIds) {
		List<DyDormCheckResult> resultList=dyDormCheckResultDao.findByCon(schoolId, acadyear, semester, week, roomIds);
		Map<String,DyDormCheckResult> resultMap=new HashMap<String,DyDormCheckResult>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(DyDormCheckResult result:resultList){
				resultMap.put(result.getRoomId()+result.getDay(), result);
			}
		}
		return resultMap;
	}

}
