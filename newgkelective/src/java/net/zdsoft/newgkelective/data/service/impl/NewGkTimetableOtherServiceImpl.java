package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableOtherDao;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableOtherJdbcDao;
import net.zdsoft.newgkelective.data.dto.SimpleClassroomUseageDto;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newGkTimetableOtherService")
public class NewGkTimetableOtherServiceImpl extends BaseServiceImpl<NewGkTimetableOther, String>
	implements NewGkTimetableOtherService{
	
	@Autowired
	private NewGkTimetableOtherDao  newGkTimetableOtherDao;
	@Autowired
	private NewGkTimetableOtherJdbcDao newGkTimetableOtherJdbcDao;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private NewGkTimetableTeacherService timetableTeacherService;
	@Override
	protected BaseJpaRepositoryDao<NewGkTimetableOther, String> getJpaDao() {
		return newGkTimetableOtherDao;
	}

	@Override
	protected Class<NewGkTimetableOther> getEntityClass() {
		return NewGkTimetableOther.class;
	}
	@Override
	public void saveOne(String unitId,String otherStr,String placeId,String timeId,String otherId){
		delete(otherId);
		String[] others=otherStr.split(",");
		NewGkTimetableOther other=new NewGkTimetableOther();
		other.setUnitId(unitId);
		other.setTimetableId(timeId);
		other.setPlaceId(placeId);
		other.setDayOfWeek(Integer.parseInt(others[0]));
		other.setPeriodInterval(others[1]);
		other.setPeriod(Integer.parseInt(others[2]));
		other.setFirstsdWeek(Integer.parseInt(others[3]));
		newGkTimetableOtherJdbcDao.insertBatch(checkSave(other));
	}
	@Override
	public void deleteByTimetableIdIn(String[] timeTableIds) {
		if(timeTableIds==null || timeTableIds.length<=0){
			return;
		}
		newGkTimetableOtherJdbcDao.deleteByTimetableIdIn(timeTableIds);
	}
	@Override
	public void saveAllEntity(List<NewGkTimetableOther> insertOtherList) {
		if(CollectionUtils.isEmpty(insertOtherList)){
			return;
		}
		newGkTimetableOtherJdbcDao.insertBatch(checkSave(insertOtherList.toArray(new NewGkTimetableOther[]{})));
		
	}

	@Override
	public List<NewGkTimetableOther> findByArrayId(String unitId, String arrayId) {
//		return RedisUtils.getObject("NEW_GK_TIMETABLE_OTHER_"+arrayId, RedisUtils.TIME_SHORT_CACHE, new TypeReference<List<NewGkTimetableOther>>(){}, new RedisInterface<List<NewGkTimetableOther>>(){
//			@Override
//			public List<NewGkTimetableOther> queryData() {
				return newGkTimetableOtherDao.findListByUnitIdAndArrayId(unitId, arrayId);
//			}
//        });
		
	}

	@Override
	public List<SimpleClassroomUseageDto> seachClassroomUseage(String unitId, String arrayId) {
		//根据timetable的ids获取每个教室每天安排几节课 placeId dayofweek num
		List<Object[]> rowList = newGkTimetableOtherDao.seachClassroomUseage(unitId, arrayId);
		Map<String,Map<String,Set<String>>> tempMap = new HashMap<>();
		for (Object[] objects : rowList) {
			String placeId = (String)objects[0];
			String timestr = (String)objects[1];
			Long num = (Long)objects[2];
			
			String dayOfweek = timestr.substring(0, 1);
			Map<String,Set<String>> map = tempMap.get(placeId);
			if(map==null){
				map = new HashMap<>();
				tempMap.put(placeId, map);
			}
			if(map.get(dayOfweek) == null) {
				map.put(dayOfweek, new HashSet<>());
			}
			map.get(dayOfweek).add(timestr);
		}
		List<TeachPlace> teachPlaceList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(tempMap.keySet().toArray(new String[0])), new TR<List<TeachPlace>>(){});
		Map<String, TeachPlace> teachPlaceMap = EntityUtils.getMap(teachPlaceList, "id");
		
		//组装dto
		List<SimpleClassroomUseageDto> dtoList = new ArrayList<SimpleClassroomUseageDto>();
		for (Entry<String, Map<String,Set<String>>> entry : tempMap.entrySet()) {
			String placeId = entry.getKey();
			HashMap<String, Integer> useageMap = new HashMap<>();
			entry.getValue().entrySet().stream().forEach(e->useageMap.put(e.getKey(), e.getValue().size()));
			if(!teachPlaceMap.containsKey(placeId)){
				continue;
			}
			//获取place对象
			TeachPlace teachPlace = teachPlaceMap.get(placeId);
			
			//组装dto
			SimpleClassroomUseageDto simpleClassroomUseageDto = new SimpleClassroomUseageDto();
			simpleClassroomUseageDto.setPlaceId(placeId);
			simpleClassroomUseageDto.setPlaceName(teachPlace.getPlaceName());
			simpleClassroomUseageDto.setUseageMap(useageMap);
			
			dtoList.add(simpleClassroomUseageDto);
		}
		
		return dtoList;
	}

	@Override
	public List<NewGkTimetableOther> findByArrayIdAndPlaceId(String unitId,String arrayId,String placeId) {
//		return RedisUtils.getObject("NEW_GK_TIMETABLE_OTHER_"+arrayId+placeId, RedisUtils.TIME_SHORT_CACHE, new TypeReference<List<NewGkTimetableOther>>(){}, new RedisInterface<List<NewGkTimetableOther>>(){
//			@Override
//			public List<NewGkTimetableOther> queryData() {
				return newGkTimetableOtherDao.findByUnitIdAndArrayIdAndPlaceId(unitId,arrayId,placeId);
//			}
//        });
	}

	@Override
	public Map<String, List<NewGkTimetableOther>> findByTeacherIds(String unitId, Set<String> teacherIds, String arrayId) {
		if(CollectionUtils.isEmpty(teacherIds) || StringUtils.isBlank(arrayId)) {
			return new HashMap<>();
		}
		
		//*************
		List<NewGkTimetableTeacher> teachers = timetableTeacherService.findByTeacherIds(teacherIds, arrayId);
		Map<String, List<String>> teacherTimeTableIdMap = EntityUtils.getListMap(teachers, "teacherId", "timetableId");
		Set<String> timeTableIds = teacherTimeTableIdMap.values().stream().flatMap(e->e.stream()).collect(Collectors.toSet());
		
		List<NewGkTimetableOther> otherList = newGkTimetableOtherDao.findByUnitIdAndTimetableIdIn(unitId, timeTableIds.toArray(new String[0]));
		Map<String, List<NewGkTimetableOther>> tableOtherMap = EntityUtils.getListMap(otherList, "timetableId", null);
		
		Map<String,List<NewGkTimetableOther>> teacherTimeMap = new HashMap<>();
		for (String teacherId : teacherTimeTableIdMap.keySet()) {
			List<String> timeTableIdsT = teacherTimeTableIdMap.get(teacherId);
			List<NewGkTimetableOther> otherListT = timeTableIdsT.stream()
					.filter(e->tableOtherMap.containsKey(e))
					.flatMap(e->tableOtherMap.get(e).stream())
					.collect(Collectors.toList());
			teacherTimeMap.put(teacherId, otherListT);
		}
		return teacherTimeMap;
	}

	@Override
	public List<String> findPlaceIds(String unitId, String arrayId){
		return newGkTimetableOtherDao.findPlaceIds(unitId, arrayId);
	}
	
	@Override
	public void deleteByIdIn(String[] otherIds) {
		if(otherIds != null && otherIds.length>0)
			newGkTimetableOtherDao.deleteByIdIn(otherIds);
	}

}
