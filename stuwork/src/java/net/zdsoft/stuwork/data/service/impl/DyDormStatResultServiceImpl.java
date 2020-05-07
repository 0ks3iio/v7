package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyDormStatResultDao;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;
import net.zdsoft.stuwork.data.entity.DyDormForm;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.entity.DyDormStatResult;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;
import net.zdsoft.stuwork.data.service.DyDormStatResultService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyDormStatResultService")
public class DyDormStatResultServiceImpl extends BaseServiceImpl<DyDormStatResult, String> implements DyDormStatResultService{
	@Autowired
	private DyDormStatResultDao dyDormStatResultDao;
	@Autowired
	private DyDormCheckResultService dyDormCheckResultService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	//返回一个key-roomId   value-不同周的统计数据（剔除相同寝室的不同班级的重复数据） 
	public Map<String,List<DyDormStatResult>> getStatMap(DormSearchDto dormDto){
		List<DyDormStatResult> statList=findListBy(new String[]{"schoolId","acadyear","semester"}, 
				new String[]{dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr()});
		Map<String,List<DyDormStatResult>> statMap=new HashMap<String, List<DyDormStatResult>>();
		if(CollectionUtils.isNotEmpty(statList)){
			for(DyDormStatResult stat:statList){
				List<DyDormStatResult> instatList=statMap.get(stat.getRoomId());
				if(CollectionUtils.isEmpty(instatList)){
					instatList=new ArrayList<DyDormStatResult>();
				}
				boolean flag=false;
				for(DyDormStatResult instat:instatList){
					if(instat.getWeek()==stat.getWeek()){
						flag=true;
					}
				}
				if(!flag){
					instatList.add(stat);
					statMap.put(stat.getRoomId(), instatList);
				}
			}
		}
		return statMap;
	}
	@Override
	public List<DyDormForm> getStar(DormSearchDto dormDto,int allWeek){
		List<DyDormForm> formList=new ArrayList<DyDormForm>();
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByProCon
											(dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr(),StuworkConstants.STU_TYPE);
		int searchSection=dormDto.getSection();
		if(CollectionUtils.isNotEmpty(bedList)){
			List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(dormDto.getUnitId(),StuworkConstants.STU_TYPE);
			Map<String, DyDormRoom>  roomMap=EntityUtils.getMap(roomList, "id");
			List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolId(dormDto.getUnitId()), new TR<List<Clazz>>(){});
			Map<String,Clazz> classMap=EntityUtils.getMap(classList,"id");
			//返回一个key-roomId   value-不同周的统计数据（剔除相同寝室的不同班级的重复数据） 
			Map<String,List<DyDormStatResult>> statMap=getStatMap(dormDto);
			
			//通过寝室床位得到 所有的classId 对应的roomIds
			Map<String,Set<String>> bedMap=new HashMap<String, Set<String>>();
			Set<String> classIds=new HashSet<String>();
			for(DyDormBed bed:bedList){
				String classId=bed.getClassId();
				String roomId=bed.getRoomId();
				Set<String> roomIdSet=bedMap.get(classId);
				if(roomIdSet==null) 
					roomIdSet=new HashSet<String>();
				roomIdSet.add(roomId);
				
				Clazz clazz=classMap.get(classId);
				if(clazz!=null){
					if(clazz.getSection()==searchSection){
						bedMap.put(classId, roomIdSet);
						classIds.add(classId);
					}
				}
			}
			//包涵每个班级classId对应每个roomId 每个roomId对应所有周次的是否文明寝室
			Set<Entry<String, Set<String>>> entries=bedMap.entrySet();
			for(Entry<String, Set<String>> entry:entries){
				List<DyDormForm> inList=new ArrayList<>();
				String classId=entry.getKey();
				//DyDormForm dormForm=new DyDormForm();
				//List<DyDormForm> informList=new ArrayList<DyDormForm>();
				Set<String> roomIdSet=entry.getValue();
				for(String roomId:roomIdSet){
					DyDormForm indormForm=new DyDormForm();
					indormForm.setRoomName(roomMap.get(roomId).getRoomName());
//					indormForm.setStatList(statMap.get(roomId));
					List<DyDormStatResult> everList=statMap.get(roomId);
					if(CollectionUtils.isEmpty(everList)){
						everList=new ArrayList<DyDormStatResult>();
					}
					Map<Integer,DyDormStatResult> everMap=EntityUtils.getMap(everList, "week");
					List<DyDormStatResult> statList=new ArrayList<DyDormStatResult>();
					for(int i=1;i<=allWeek;i++){
						DyDormStatResult stat=everMap.get(i);
						if(stat!=null){
							statList.add(stat);
						}else{
							statList.add(new DyDormStatResult());
						}
					}
					indormForm.setStatList(statList);
					Clazz clazz=classMap.get(classId);
					if(clazz!=null){
						indormForm.setClassName(clazz.getClassNameDynamic());
						indormForm.setClassCode(clazz.getClassCode());
						indormForm.setClassAcadyear(clazz.getAcadyear());
					}
					inList.add(indormForm);
				}
				if(CollectionUtils.isNotEmpty(inList)){
					Collections.sort(inList,(o1,o2)->{
						return o1.getRoomName().compareTo(o2.getRoomName());
					});
				}
				formList.addAll(inList);
			}
		}
		if(CollectionUtils.isNotEmpty(formList)) {
			Collections.sort(formList, new Comparator<DyDormForm>() {
				@Override
				public int compare(DyDormForm o1, DyDormForm o2) {
//					return o1.getClassCode().compareTo(o2.getClassCode());
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
		}
		return formList;
	}
	@Override
	public List<DyDormStatResult> getStatNotClassId(String schoolId,String acadyear,String semester,int week){
		List<DyDormStatResult> statList=dyDormStatResultDao.findListByCon(schoolId,acadyear,semester,week);
		return setProperty(statList);
	}
	@Override
	public List<DyDormStatResult> getStat(DormSearchDto dormDto){
		List<DyDormStatResult> statList=dyDormStatResultDao.findListByCon(dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr(),dormDto.getWeek(),dormDto.getClassId());
		return setProperty(statList);
	}
	public List<DyDormStatResult> setProperty(List<DyDormStatResult> statList){
		List<DyDormStatResult> lastList=new ArrayList<DyDormStatResult>();
		if(CollectionUtils.isNotEmpty(statList)){
			Set<String> roomIds=new HashSet<String>();
			for(DyDormStatResult stat:statList){
				roomIds.add(stat.getRoomId());
			}
			Map<String,DyDormRoom> roomMap=dyDormRoomService.findMapByIdIn(roomIds.toArray(new String[0]));
			for(DyDormStatResult stat:statList){
				DyDormRoom room=roomMap.get(stat.getRoomId());
				if(room==null){
					continue;
				}
				stat.setRoomName(room.getRoomName());
				Float allScore=new Float(0);
				Float rewardScore=new Float(0);
				allScore+=stat.getAJlScore()==null?0:stat.getAJlScore();
				allScore+=stat.getANwScore()==null?0:stat.getANwScore();
				allScore+=stat.getAWsScore()==null?0:stat.getAWsScore();
				allScore+=stat.getBJlScore()==null?0:stat.getBJlScore();
				allScore+=stat.getBNwScore()==null?0:stat.getBNwScore();
				allScore+=stat.getBWsScore()==null?0:stat.getBWsScore();
				allScore+=stat.getCJlScore()==null?0:stat.getCJlScore();
				allScore+=stat.getCNwScore()==null?0:stat.getCNwScore();
				allScore+=stat.getCWsScore()==null?0:stat.getCWsScore();
				allScore+=stat.getDJlScore()==null?0:stat.getDJlScore();
				allScore+=stat.getDNwScore()==null?0:stat.getDNwScore();
				allScore+=stat.getDWsScore()==null?0:stat.getDWsScore();
				allScore+=stat.getEJlScore()==null?0:stat.getEJlScore();
				allScore+=stat.getENwScore()==null?0:stat.getENwScore();
				allScore+=stat.getEWsScore()==null?0:stat.getEWsScore();
				allScore+=stat.getFJlScore()==null?0:stat.getFJlScore();
				allScore+=stat.getFNwScore()==null?0:stat.getFNwScore();
				allScore+=stat.getFWsScore()==null?0:stat.getFWsScore();
				allScore+=stat.getGJlScore()==null?0:stat.getGJlScore();
				allScore+=stat.getGNwScore()==null?0:stat.getGNwScore();
				allScore+=stat.getGWsScore()==null?0:stat.getGWsScore();
				stat.setScoreALL(allScore);
				
				rewardScore+=stat.getExcellentScore()!=null?stat.getExcellentScore():0;
				stat.setRewardAll(rewardScore);
				lastList.add(stat);
			}
		}
		return lastList;
	}
	@Override
	public void save(String schoolId,String acadyear,String semesterStr,int week){
		
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByProCon(schoolId,acadyear,semesterStr,StuworkConstants.STU_TYPE);
		//通过寝室床位得到 所有的classId 对应的roomIds
		if(CollectionUtils.isNotEmpty(bedList)){
			//Set<String> roomIds=new HashSet<String>();//所有的roomIds
			Map<String,Set<String>> bedMap=new HashMap<String, Set<String>>();
			Set<String> classIds=new HashSet<String>();
			for(DyDormBed bed:bedList){
				String classId=bed.getClassId();
				String roomId=bed.getRoomId();
				Set<String> roomIdSet=bedMap.get(classId);
				if(roomIdSet==null) 
					roomIdSet=new HashSet<String>();
				roomIdSet.add(roomId);
				bedMap.put(classId, roomIdSet);
				
				//roomIds.add(roomId);
				classIds.add(classId);
			}
			//统计前先删除重新统计的数据
			dyDormStatResultDao.deleteBySth(schoolId, acadyear, semesterStr, week, classIds.toArray(new String[0]));
			
			//获取roomId对应的buildingId map
			Map<String,String> roomBuildingIdMap=new HashMap<String, String>();
			List<DyDormRoom> roomList=dyDormRoomService.findListBy("unitId",schoolId);
			if(CollectionUtils.isNotEmpty(roomList)){
				for(DyDormRoom room:roomList){
					roomBuildingIdMap.put(room.getId(), room.getBuildingId());
				}
			}
			//获取考核卫生 内务 纪律 信息
			List<DyDormCheckResult> resultList=dyDormCheckResultService.findListBy(new String[]{"schoolId","acadyear",
					"semester","week"}, new Object[]{schoolId,acadyear,semesterStr,week});
			Map<String,List<DyDormCheckResult>> resultListMap=new HashMap<String, List<DyDormCheckResult>>();
			//得到key-roomId   value-List<DyDormCheckResult>
			for(DyDormCheckResult result:resultList){
				String roomId=result.getRoomId();
				List<DyDormCheckResult> rList=resultListMap.get(roomId);
				if(CollectionUtils.isEmpty(rList)){
					rList=new ArrayList<DyDormCheckResult>();
				}
				rList.add(result);
				resultListMap.put(roomId, rList);
			}
			//统计
			Set<Entry<String, Set<String>>> entries=bedMap.entrySet();
			List<DyDormStatResult> statList=new ArrayList<DyDormStatResult>();
			for(Entry<String, Set<String>> entry:entries){
				String classId=entry.getKey();
				Set<String> roomIdSet=entry.getValue();
				for(String roomId:roomIdSet){
					DyDormStatResult stat=new DyDormStatResult();
					stat.setId(UuidUtils.generateUuid());
					stat.setAcadyear(acadyear);
					stat.setSchoolId(schoolId);
					stat.setSemester(semesterStr);
					stat.setRoomId(roomId);
					stat.setClassId(classId);
					stat.setWeek(week);
					stat.setBuildingId(roomBuildingIdMap.get(roomId));
					
					List<DyDormCheckResult> rList=resultListMap.get(roomId);
					if(CollectionUtils.isNotEmpty(rList)){
						boolean isExcellent=true;
						Float excellentScore=new Float(2);
						for(DyDormCheckResult result:rList){
//							boolean isExcellent=true;
//							Float excellentScore=new Float(2);
							Float jlSocre=result.getJlScore();
							Float wsScore=result.getWsScore();
							Float nwScore=result.getNwScore();
							if(jlSocre==null||wsScore==null||nwScore==null
									||jlSocre<10||wsScore<10||nwScore<10){
								isExcellent=false;
								excellentScore=new Float(0);
							}
//							stat.setIsExcellent(isExcellent);
//							stat.setExcellentScore(excellentScore);
							if(result.getDay()==1){
								stat.setAJlScore(jlSocre);
								stat.setAWsScore(wsScore);
								stat.setANwScore(nwScore);
							}else if(result.getDay()==2){
								stat.setBJlScore(jlSocre);
								stat.setBWsScore(wsScore);
								stat.setBNwScore(nwScore);
							}else if(result.getDay()==3){
								stat.setCJlScore(jlSocre);
								stat.setCWsScore(wsScore);
								stat.setCNwScore(nwScore);
							}else if(result.getDay()==4){
								stat.setDJlScore(jlSocre);
								stat.setDWsScore(wsScore);
								stat.setDNwScore(nwScore);
							}else if(result.getDay()==5){
								stat.setEJlScore(jlSocre);
								stat.setEWsScore(wsScore);
								stat.setENwScore(nwScore);
							}else if(result.getDay()==6){
								stat.setFJlScore(jlSocre);
								stat.setFWsScore(wsScore);
								stat.setFNwScore(nwScore);
							}else{
								stat.setGJlScore(jlSocre);
								stat.setGWsScore(wsScore);
								stat.setGNwScore(nwScore);
							}
						}
						stat.setIsExcellent(isExcellent);
						stat.setExcellentScore(excellentScore);
					}
					statList.add(stat);
				}
			}
			
			saveAll(statList.toArray(new DyDormStatResult[0]));
		}
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyDormStatResult, String> getJpaDao() {
		return dyDormStatResultDao;
	}

	@Override
	protected Class<DyDormStatResult> getEntityClass() {
		return DyDormStatResult.class;
	}

}
