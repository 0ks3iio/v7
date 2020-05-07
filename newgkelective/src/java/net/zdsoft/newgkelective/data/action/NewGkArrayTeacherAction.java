package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupExRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ArrayTeacherDto;
import net.zdsoft.newgkelective.data.dto.ArrayTeacherListDto;
import net.zdsoft.newgkelective.data.dto.SubjectInfo;
import net.zdsoft.newgkelective.data.dto.SubjectInfoListDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkCourseHeap;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTeacher;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkCourseHeapService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTeacherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;

@Controller
@RequestMapping("/newgkelective/{arrayId}")
public class NewGkArrayTeacherAction extends BaseAction{
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired 
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachGroupRemoteService teachGroupRemoteService;
	@Autowired
	private TeachGroupExRemoteService teachGroupExRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private NewGkCourseHeapService newGkCourseHeapService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkSubjectTeacherService newGkSubjectTeacherService;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private NewGkClassCombineRelationService combineRelationService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setAutoGrowNestedPaths(true);
		binder.setAutoGrowCollectionLimit(2048);
	}
	
	@ResponseBody
	@RequestMapping("/array/teacher/list/save")
	@ControllerInfo(value = "教师列表")
	public String doSaveTeacherList(                                   
			@PathVariable("arrayId") String arrayId, SubjectInfoListDto subjectInfoListDto) {
		//Map<String,Set<String>> teacherIdToSubjectIds = new HashMap<String,Set<String>>();
		Map<String,Set<String>> subIdToTeacherIds = new HashMap<String,Set<String>>();
		for(SubjectInfo subjectInfo : subjectInfoListDto.getSubjectInfoList()) {
			if(CollectionUtils.isNotEmpty(subjectInfo.getTeacherIds())){
				String subjectId = subjectInfo.getSubjectId();
				Set<String> teacherids = subIdToTeacherIds.get(subjectId);
				if(teacherids==null){
					teacherids =new HashSet<>();
					subIdToTeacherIds.put(subjectId, teacherids);
				}
				teacherids.addAll(subjectInfo.getTeacherIds());
			}
		}
		
		
		List<NewGkSubjectTeacher> newGkSubjectTeacherList = new ArrayList<NewGkSubjectTeacher>();
		NewGkSubjectTeacher newGkSubjectTeacher = null;
		for (String key :subIdToTeacherIds.keySet()) { 
			Set<String> teachids= subIdToTeacherIds.get(key);
			for(String tid: teachids){
				if(StringUtils.isEmpty(tid)){
					continue;
				}
				newGkSubjectTeacher = new NewGkSubjectTeacher();
				newGkSubjectTeacher.setId(UuidUtils.generateUuid());
				newGkSubjectTeacher.setArrayId(arrayId);
				newGkSubjectTeacher.setTeacherId(tid);
				newGkSubjectTeacher.setSubjectId(key);
				newGkSubjectTeacherList.add(newGkSubjectTeacher);
			}
			
		} 
		try{
			newGkSubjectTeacherService.deleteAndSave(arrayId,newGkSubjectTeacherList);
		}catch(Exception e) {
			e.printStackTrace();
			return returnError();
		}
		
		ResultDto resultDto = new ResultDto();
		resultDto.setSuccess(true);
		resultDto.setMsg("保存教师成功！");
		resultDto.setCode("00");
		if(subIdToTeacherIds.size() == 0) {
			resultDto.setCode("01");
		}
		return Json.toJSONString(resultDto);

	}
	
	
	@RequestMapping("/adjust/teacher/page")
	@ControllerInfo(value = "微调老师首页")
	public String showAdjustTeacherIndex(@PathVariable("arrayId")String arrayId,ModelMap map) {
		map.put("arrayId", arrayId);
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		String gradeId = newGkArray.getGradeId();
		map.put("gradeId",gradeId);
		List<NewGkCourseHeap> courseHeapList = newGkCourseHeapService.findByArrayId(arrayId);
		Set<String> subjectIds = EntityUtils.getSet(courseHeapList, NewGkCourseHeap::getSubjectId);
		Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		map.put("courseMap", courseMap);
		map.put("arrangeType", newGkArray.getArrangeType());
		return "/newgkelective/array/adjustTeacherIndex.ftl";
	}
	
	@RequestMapping("/{subjectId}/teacher/page")
	@ControllerInfo(value = "微调老师首页")
	public String showAdjustTeacherListIndex(@PathVariable("arrayId")String arrayId,@PathVariable("subjectId")String subjectId,ModelMap map) {
		map.put("arrayId", arrayId);
		map.put("subjectId", subjectId);
		List<NewGkSubjectTeacher> subjectTeacherList = newGkSubjectTeacherService.findByArrayIdAndSubjectId(arrayId,subjectId);
		List<NewGkCourseHeap> courseHeapList = newGkCourseHeapService.findByArrayIdAndSubjectId(arrayId,subjectId);
		Set<String> timetableSet = EntityUtils.getSet(courseHeapList, "timetableId");
		Map<String, NewGkTimetableTeacher> newGkTimetableTeacherMap = new HashMap<String, NewGkTimetableTeacher>();
		List<NewGkTimetableTeacher> newGkTimetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableSet.toArray(new String[timetableSet.size()]));
		for (NewGkTimetableTeacher newGkTimetableTeacher : newGkTimetableTeacherList) {
			newGkTimetableTeacherMap.put(newGkTimetableTeacher.getTimetableId(), newGkTimetableTeacher);
		}
		List<NewGkTimetable> timeTableList = newGkTimetableService.findListByIds(timetableSet.toArray(new String[timetableSet.size()]));
		Map<String, NewGkTimetableOther> timeTableOtherMap = new HashMap<String, NewGkTimetableOther>();
		List<NewGkTimetableOther> timeTableOtherList = newGkTimetableOtherService.findByArrayId(getLoginInfo().getUnitId(), arrayId);
		
		for (NewGkTimetableOther newGkTimetableOther : timeTableOtherList) {
			timeTableOtherMap.put(newGkTimetableOther.getId(), newGkTimetableOther);
		}
		Set<String> ttIds = EntityUtils.getSet(timeTableOtherList, NewGkTimetableOther::getTimetableId);
		timeTableList = timeTableList.stream().filter(e->ttIds.contains(e.getId())).collect(Collectors.toList());
		Map<String,NewGkTimetable> timetableMap=EntityUtils.getMap(timeTableList,"id");
		Set<String> placeIds = EntityUtils.getSet(timeTableOtherList, NewGkTimetableOther::getPlaceId);
		Set<String> classIds = EntityUtils.getSet(timeTableList, NewGkTimetable::getClassId);
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIds.toArray(new String[0])), new TR<List<TeachPlace>>(){});
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(classIds.toArray(new String[classIds.size()]));
		Map<String, String> placeIdToPlaceName = EntityUtils.getMap(placeList, "id", "placeName");
		Map<String, String> classIdToClassName = EntityUtils.getMap(divideClassList, "id", "className");
		Map<String, List<String>> timeTableIdToTimeTableOtherIds = new HashMap<String, List<String>>();
		timeTableIdToTimeTableOtherIds = EntityUtils.getListMap(timeTableOtherList, "timetableId", "id");
		Map<String, String> timeTableIdsToClassId = EntityUtils.getMap(timeTableList, "id", "classId");
		Map<String, List<String>> timeTableIdsToPlaceId = EntityUtils.getListMap(timeTableOtherList, "timetableId", "placeId");
		Map<String, List<String>> classIdToTimeTableIds = EntityUtils.getListMap(timeTableList, "classId", "id");
//		Map<String, List<String>> teacherIdToTimetableId = EntityUtils.getListMap(newGkTimetableTeacherList, "teacherId", "timetableId");
		Map<String, List<String>> classIdToHaveClassTime = new HashMap<String, List<String>>();
		
		Map<String, Set<String>> classIdToTimeTableId = new HashMap<String, Set<String>>();
		
		Set<String> teacherIdset = EntityUtils.getSet(subjectTeacherList, "teacherId");
		Map<String, String> teacherMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdset.toArray(new String[teacherIdset.size()])),new TypeReference<Map<String, String>>(){});
		//封装场地
		for (String string : classIds) {
			if(!classIdToTimeTableId.containsKey(string)){
				classIdToTimeTableId.put(string, new HashSet<String>());
			}
			List<String> list = classIdToTimeTableIds.get(string);
			for (String string2 : list) {
				classIdToTimeTableId.get(string).add(string2);
			}
		}
		//封装上课时间
		for (String string : classIds) {
			if(!classIdToHaveClassTime.containsKey(string)){
				classIdToHaveClassTime.put(string, new ArrayList<String>());
			}
			List<String> list = classIdToTimeTableIds.get(string);
			for (String string2 : list) {
				List<String> list2 = timeTableIdToTimeTableOtherIds.get(string2);
				for (String string3 : list2) {
					NewGkTimetableOther newGkTimetableOther = timeTableOtherMap.get(string3);
					if(newGkTimetableOther.getFirstsdWeek() == null) {
						return errorFtl(map, "存在时间点"+newGkTimetableOther.getDayOfWeek() 
						+ newGkTimetableOther.getPeriodInterval() 
						+ newGkTimetableOther.getPeriod()+"没有单双周信息");
					}
					classIdToHaveClassTime.get(string).add(newGkTimetableOther.getDayOfWeek() 
							+ newGkTimetableOther.getPeriodInterval() 
							+ newGkTimetableOther.getPeriod()
							+ newGkTimetableOther.getFirstsdWeek());
				}
			}
		}
		//封装上课老师Id
		Map<String,String> classIdsToTeacherId = new HashMap<String,String>();
		for (String string : classIds) {
			if(!classIdsToTeacherId.containsKey(string)){
				List<String> list = classIdToTimeTableIds.get(string);
				for (String string2 : list) {
					if(newGkTimetableTeacherMap.containsKey(string2)){
						classIdsToTeacherId.put(string, newGkTimetableTeacherMap.get(string2).getTeacherId());
					}
				}
			}
		}
		
		Map<String,String> classIdsToTimeTableId = new HashMap<String,String>();
		for (String string : classIds) {
			if(!classIdsToTimeTableId.containsKey(string)){
				List<String> list = classIdToTimeTableIds.get(string);
				for (String string2 : list) {
					if(newGkTimetableTeacherMap.containsKey(string2)){
						classIdsToTimeTableId.put(string, newGkTimetableTeacherMap.get(string2).getId());
					}
				}
			}
		}
		
		Map<Integer,Set<String>> heapToClassId = new HashMap<Integer, Set<String>>();
		
		//堆和ClassId已经封装，根据ClassId可以获取一个timeTableIds集合，可以方便封装班级名称 上课教室 上课时间等
		for (NewGkCourseHeap newGkCourseHeap : courseHeapList) {
			if(!heapToClassId.containsKey(newGkCourseHeap.getHeapNum())){
				heapToClassId.put(newGkCourseHeap.getHeapNum(), new HashSet<String>());
			}
			heapToClassId.get(newGkCourseHeap.getHeapNum()).add(timeTableIdsToClassId.get(newGkCourseHeap.getTimetableId()));
		}
		
		//封装数据
		Map<String,List<ArrayTeacherDto>> arrayTeacherDtoMap = new HashMap<String, List<ArrayTeacherDto>>();
		int index = 0;
		for(Map.Entry<Integer,Set<String>> entry: heapToClassId.entrySet()){
			arrayTeacherDtoMap.put(entry.getKey() + "", new ArrayList<ArrayTeacherDto>());
			List<ArrayTeacherDto> list = arrayTeacherDtoMap.get(entry.getKey()+"");
			//classId
			Set<String> value = entry.getValue();
			for (String classId : value) {
				ArrayTeacherDto arrayTeacherDto = new ArrayTeacherDto();
				arrayTeacherDto.setClassName(classIdToClassName.get(classId));
				//设置上课时间
				List<String> list2 = classIdToHaveClassTime.get(classId);
				// 某班级 分堆时的课 不存在了；可能是 手动调课时 去掉了
				if(CollectionUtils.isEmpty(list2))
					continue;
				String tempString = "";
				for(int i = 0; i < list2.size(); i++) {
					if(i == 0) {
						tempString = tempString + list2.get(i);
					}else {
						tempString = tempString + "-" + list2.get(i);
					}
				}
				String havaClassTime = convert(tempString);
				arrayTeacherDto.setHaveClassCode(tempString);
				arrayTeacherDto.setHaveClassTime(havaClassTime);
				
				Set<String> timetableIds = new HashSet<String>();
				
				Set<String> set = classIdToTimeTableId.get(classId);
				for (String string : set) {
					timetableIds.add(string);
				}
				String[] array = timetableIds.toArray(new String[timetableIds.size()]);
				//设置上课教室
				String placeName = "";
				for (int i = 0; i < array.length; i++) {
					if(!timetableMap.containsKey(array[i])){
						continue;
					}
					arrayTeacherDto.setSubjectType(timetableMap.get(array[i]).getSubjectType());
					arrayTeacherDto.setTimetibleId(array[i]);
					List<String> list3 = timeTableIdsToPlaceId.get(array[i]);
					Set<String> tempList = new HashSet<String>();
					for (String string : list3) {
						if(StringUtils.isNotBlank(string))
							tempList.add(string);
					}
					for (String string : tempList) {
						placeName = placeName + "，" + placeIdToPlaceName.get(string);
					}
				}
				String tempPlaceName = "";
				if(placeName != "") {
					tempPlaceName = placeName.substring(1);
				}
				
				arrayTeacherDto.setPlaceName(tempPlaceName);
				
				arrayTeacherDto.setTeacherId(classIdsToTeacherId.get(classId));
				arrayTeacherDto.setTimetableTeachId(classIdsToTimeTableId.get(classId));
				arrayTeacherDto.setIndex(index);
				index++;
				list.add(arrayTeacherDto);
			}
			//排序
			if(CollectionUtils.isNotEmpty(list)){
				Collections.sort(list, new Comparator<ArrayTeacherDto>() {

					@Override
					public int compare(ArrayTeacherDto o1, ArrayTeacherDto o2) {
						if(o1.getSubjectType()==null){
							return 0;
						}
						if(o2.getSubjectType()==null){
							return 0;
						}
						if(o2.getSubjectType().equals(o1.getSubjectType())){
							return o1.getClassName().compareTo(o2.getClassName());
						}
						return o1.getSubjectType().compareTo(o2.getSubjectType());
					}
						
				});
			}
			
			
		}
		
		map.put("teacherMap", teacherMap);
		map.put("arrayTeacherDtoMap", arrayTeacherDtoMap);
		
		return "/newgkelective/array/adjustTeacher.ftl";
	}
	
//	@RequestMapping("/teacher/one/save")
//	@ResponseBody
//	public String doSaveOneTeacher(@PathVariable("arrayId") String arrayId, String teacherId, String haveClassCode, String timetibleId) {
//		String[] haveClassCodes = haveClassCode.split("-");
//		//判断组
//		NewGkCourseHeap newGkCourseHeap = newGkCourseHeapService.findByArrayIdAndTimetableId(arrayId,timetibleId); 
//		Integer tempHeap = newGkCourseHeap.getHeapNum();
//		List<NewGkTimetableTeacher> newGkTimetableTeacherList = newGkTimetableTeacherService.findByTeacherId(teacherId);
//		List<String> timetableIds = EntityUtils.getList(newGkTimetableTeacherList, "timetableId");
//		List<NewGkCourseHeap> newGkCourseHeapList = newGkCourseHeapService.findByArrayIdAndTimetableIdIn(arrayId,timetableIds.toArray(new String[timetableIds.size()]));
//		Set<Integer> heapSet = EntityUtils.getSet(newGkCourseHeapList, "heapNum");
//		for (Integer integer : heapSet) {
//			if(!integer.equals(tempHeap)){
//				return error("该老师已经在其他组任课，请重新选择老师");
//			}
//		}
//		
//		//理论上不是一个课程可以是多个老师 目前是单个老师 所以可以这样查询
//		NewGkTimetableTeacher newGkTimetableTeacher = newGkTimetableTeacherService.findByTimetableId(timetibleId);
//		if(newGkTimetableTeacher.getTeacherId().equals(teacherId)){
//			return success("原来就是该老师的课程，无需保存");
//		}
//		//判断该老师其他时间有没有课
//		List<NewGkTimetable> newGkTimetableList = newGkTimetableService.findByArrayIdAndIdIn(arrayId,timetableIds.toArray(new String[timetableIds.size()]));
//		List<String> timetableIdList = EntityUtils.getList(newGkTimetableList, "id");
//		List<String> havaClassTime = new ArrayList<String>();
//		List<NewGkTimetableOther> newGkTimetableOtherList = newGkTimetableOtherService.findByTimetableIds(timetableIdList.toArray(new String[timetableIdList.size()]));
//		for (NewGkTimetableOther newGkTimetableOther : newGkTimetableOtherList) {
//			havaClassTime.add(newGkTimetableOther.getDayOfWeek() + newGkTimetableOther.getPeriodInterval() + newGkTimetableOther.getPeriod());
//		}
//		for (String string : havaClassTime) {
//			for (String string2 : haveClassCodes) {
//				if(string.equals(string2)) {
//					return error("该老师在这个时间段有其他课程，请重新选择老师");
//				}
//			}
//		}
//		
//		newGkTimetableTeacher.setTeacherId(teacherId);
//		try {
//			newGkTimetableTeacherService.save(newGkTimetableTeacher);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return returnError();
//		}
//		return success("");
//	}
	
	@RequestMapping("/teacher/adjust/save")
	@ResponseBody
	public String doSaveAdjustTeacher(@PathVariable("arrayId") String arrayId, String subjectId, ArrayTeacherListDto arrayTeacherListDto) {
		NewGkArray newGkArray = newGkArrayService.findOne(arrayId);
		String unitId = getLoginInfo().getUnitId();
		// 合班数据
		List<Set<String>> combineClasss = combineRelationService.getCombineRelation(unitId,newGkArray.getLessonArrangeId());
		Map<String,Set<String>> combineMap = new HashMap<>();
		for (Set<String> ss : combineClasss) {
			if(ss.iterator().next().contains(subjectId)) {
				Set<String> collect = ss.stream().map(e->e.split("-")[0]).collect(Collectors.toSet());
				collect.forEach(e->combineMap.put(e, collect));
			}
		}
		
		
		//验证所有教师时间有没有冲突，不能只考虑组别
		List<ArrayTeacherDto> arrayTeacherList = arrayTeacherListDto.getArrayTeacherList();
		if(CollectionUtils.isEmpty(arrayTeacherList)){
			return error("没有需要保存的数据");
		}
		Set<String> teacherId=new HashSet<String>();
		Set<String> timetableId=new HashSet<String>();
		Map<String,Set<String>> timetableIdByTeacherIds=new HashMap<String,Set<String>>();
		for(ArrayTeacherDto dto:arrayTeacherList){
			timetableId.add(dto.getTimetibleId());
			if(StringUtils.isNotBlank(dto.getTeacherId())){
				teacherId.add(dto.getTeacherId());
				if(!timetableIdByTeacherIds.containsKey(dto.getTeacherId())){
					timetableIdByTeacherIds.put(dto.getTeacherId(), new HashSet<String>());
				}
				timetableIdByTeacherIds.get(dto.getTeacherId()).add(dto.getTimetibleId());
			}
		}
		Map<String, String> teacherMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherId.toArray(new String[teacherId.size()])),new TypeReference<Map<String, String>>(){});

		List<NewGkTimetable> allTablelist = newGkTimetableService.findByArrayId(unitId, arrayId);
		Map<String, String> ttCidMap = EntityUtils.getMap(allTablelist, NewGkTimetable::getId,NewGkTimetable::getClassId);
		
		List<NewGkDivideClass> xzbClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, newGkArray.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, 
				false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		Map<String, String> toOldCidMap = EntityUtils.getMap(
				xzbClassList.stream().filter(e -> e.getOldDivideClassId() != null).collect(Collectors.toList()),
				NewGkDivideClass::getId, NewGkDivideClass::getOldDivideClassId);
		
		newGkTimetableService.makeTeacher(allTablelist);
		newGkTimetableService.makeTime(unitId, arrayId, allTablelist);
		//全部组装时间
		Map<String,List<NewGkTimetableOther>> timeByTableIdMap=new HashMap<>();
		//数据库已存的老师组装 去除页面维护
		Map<String,Set<String>> othertimetableIdByTeacherIds=new HashMap<String,Set<String>>();
		if(CollectionUtils.isNotEmpty(allTablelist)){
			for(NewGkTimetable n:allTablelist){
				if(CollectionUtils.isNotEmpty(n.getTimeList())){
					for(NewGkTimetableOther o:n.getTimeList()){
						if(!timeByTableIdMap.containsKey(o.getTimetableId())){
							timeByTableIdMap.put(o.getTimetableId(), new ArrayList<>());
						}
//						String times=o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod();
						timeByTableIdMap.get(o.getTimetableId()).add(o);
					}
				}
				if(!timetableId.contains(n.getId())){
					if(CollectionUtils.isNotEmpty(n.getTeacherIdList())){
						for(String o:n.getTeacherIdList()){
							if(!othertimetableIdByTeacherIds.containsKey(o)){
								othertimetableIdByTeacherIds.put(o, new HashSet<String>());
							}
							othertimetableIdByTeacherIds.get(o).add(n.getId());
						}
					}
				}
			}
		}
		//验证老师是不是时间重复(考虑全局，不能局限于某个科目)
		for(Entry<String, Set<String>> tt:timetableIdByTeacherIds.entrySet()){
			String tid=tt.getKey();
			Map<String,List<NewGkTimetableOther>> timeMap=new HashMap<>();
			Set<String> timetableIdSet = tt.getValue();
			int k=0;
			//本科目内
			for(String t:timetableIdSet){
				List<NewGkTimetableOther> timeList = timeByTableIdMap.get(t);
				if(CollectionUtils.isNotEmpty(timeList)){
					for (NewGkTimetableOther o : timeList) {
						String timeStr =o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod();
						if(!timeMap.containsKey(timeStr)) {
							timeMap.put(timeStr, new ArrayList<>());
						}
						List<NewGkTimetableOther> list = timeMap.get(timeStr);
						if(o.getFirstsdWeek() == null) {
							return error("存在时间点 "+timeStr+" 没有单双周信息");
						}
						list.add(o);
						Integer adds = list.stream().map(e->e.getFirstsdWeek()).reduce((x,y)->x+y)
								.orElse(0);
						
						if(list.size() > 1 && adds != (NewGkElectiveConstant.FIRSTSD_WEEK_1+NewGkElectiveConstant.FIRSTSD_WEEK_2)){
							List<String> collect = list.stream()
									.map(e->ttCidMap.get(e.getTimetableId()))
									.filter(e->toOldCidMap.containsKey(e))
									.map(e->toOldCidMap.get(e))
									.collect(Collectors.toList());
							if(collect.size() == list.size() && combineMap.containsKey(collect.get(0)) && combineMap.get(collect.get(0)).containsAll(collect)) {
								continue;
							}
							return error(teacherMap.get(tid)+"教师安排任课班级存在时间冲突，请调整");
						}
						
					}
				}
			}
			if(othertimetableIdByTeacherIds.containsKey(tid)){
				Set<String> timetableIdSet2 = othertimetableIdByTeacherIds.get(tid);
				for(String t:timetableIdSet2){
					List<NewGkTimetableOther> timeList = timeByTableIdMap.get(t);
					if(CollectionUtils.isNotEmpty(timeList)){
						for (NewGkTimetableOther o : timeList) {
							String timeStr =o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod();
							if(!timeMap.containsKey(timeStr)) {
								timeMap.put(timeStr, new ArrayList<>());
							}
							List<NewGkTimetableOther> list = timeMap.get(timeStr);
							if(o.getFirstsdWeek() == null) {
								return error("存在时间点 "+timeStr+" 没有单双周信息");
							}
							list.add(o);
							Integer adds = list.stream().map(e->e.getFirstsdWeek()).reduce((x,y)->x+y)
									.orElse(0);
							
							if(list.size() > 1 && adds != (NewGkElectiveConstant.FIRSTSD_WEEK_1+NewGkElectiveConstant.FIRSTSD_WEEK_2)){
								List<String> collect = list.stream()
										.map(e->ttCidMap.get(e.getTimetableId()))
										.filter(e->toOldCidMap.containsKey(e))
										.map(e->toOldCidMap.get(e))
										.collect(Collectors.toList());
								if(collect.size() == list.size() && combineMap.containsKey(collect.get(0)) && combineMap.get(collect.get(0)).containsAll(collect)) {
									continue;
								}
								return error(teacherMap.get(tid)+"教师安排任课班级存在时间冲突，请调整");
							}
							
						}
//						k=k+timeList.size();
//						timeMap.addAll(timeList);
//						if(timeMap.size()!=k){
//							return error(teacherMap.get(tid)+"教师本科目安排任课的时间与该教师在其他科目任教时间出现冲突，请调整");
//						}
					}
				}
			}
			
		}

		
		// 教师有没有在其他组教课
		// 教师在同一个时间点有没有教授两门课
//		List<ArrayTeacherDto> arrayTeacherList = arrayTeacherListDto.getArrayTeacherList();
//		List<String> teacherIdList = EntityUtils.getList(arrayTeacherList, "teacherId");
//		
//		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByIds(teacherIdList.toArray(new String[teacherIdList.size()])),new TR<List<Teacher>>(){});
//		Map<String, String> teacherMap = EntityUtils.getMap(teacherList, "id", "teacherName");
//		
//		List<String> timetableTeachIdList = EntityUtils.getList(arrayTeacherList, "timetableTeachId");
//		
//		Map<String, List<String>> teacherId2HaveClassCode= new HashMap<String, List<String>>();
//		for(int i = 0; i < arrayTeacherList.size(); i++) {
//			String teacherId = arrayTeacherList.get(i).getTeacherId();
//			Integer heapNum = arrayTeacherList.get(i).getHeapNum();
//			String haveClassCode = arrayTeacherList.get(i).getHaveClassCode();
//			String[] haveClassCodes = haveClassCode.split("-");
//			if(!teacherId2HaveClassCode.containsKey(teacherId)){
//				teacherId2HaveClassCode.put(teacherId, new ArrayList<String>());
//			}
//			for (String string : haveClassCodes) {
//				if(teacherId2HaveClassCode.get(teacherId).contains(string)){
//					return error(teacherMap.get(teacherId)+"教师在" + convert(string) +"教了多节课，请调整");
//				}else {
//					teacherId2HaveClassCode.get(teacherId).add(string);
//				}
//			}
//			for(int j = i + 1; j < arrayTeacherList.size(); j++) {
//				String tempTeacherId = arrayTeacherList.get(j).getTeacherId();
//				Integer tempHeapNum = arrayTeacherList.get(j).getHeapNum();
//				if(tempTeacherId.equals(teacherId)){
//					if(heapNum != tempHeapNum){
//						return error(teacherMap.get(teacherId)+"教师在多个组任教，请调整");
//					}
//				}
//			}
//		}
		List<NewGkTimetableTeacher> newGkTimetableTeacherList = new ArrayList<NewGkTimetableTeacher>();
		NewGkTimetableTeacher newGkTimetableTeacher;
		for(int i = 0; i < arrayTeacherList.size(); i++) {
			if(StringUtils.isBlank(arrayTeacherList.get(i).getTeacherId())) {
				continue;
			}
			newGkTimetableTeacher = new NewGkTimetableTeacher();
			newGkTimetableTeacher.setId(UuidUtils.generateUuid());
			newGkTimetableTeacher.setTeacherId(arrayTeacherList.get(i).getTeacherId());
			newGkTimetableTeacher.setTimetableId(arrayTeacherList.get(i).getTimetibleId());
			newGkTimetableTeacherList.add(newGkTimetableTeacher);
		}
		
		try {
			newGkTimetableTeacherService.saveOrDel(arrayId, newGkTimetableTeacherList, timetableId.toArray(new String[]{}));
//			newGkTimetableTeacherService.deleteAndsave(timetableTeachIdList, newGkTimetableTeacherList);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return success("操作成功！");
	}


	@RequestMapping("/array/teacher/page")
	@ControllerInfo(value = "安排教师首页")
	public String showArrayTeacherIndex(
			@PathVariable("arrayId") String arrayId, HttpServletRequest request, ModelMap map) {
		map.put("arrayId", arrayId);
		
		String gradeName = "";
		String gradeId = request.getParameter("gradeId");
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		gradeId = newGkArray.getGradeId();
		gradeName = newGkArray.getArrayName();
		map.put("gradeName", gradeName);
		map.put("gradeId", gradeId);
		map.put("arrangeType", newGkArray.getArrangeType());
		List<NewGkCourseHeap> newGkCourseHeapList = newGkCourseHeapService.findByArrayId(arrayId);
		//key:subjectId,value:教研组ids
		Map<String,List<String>> subjectIdToTeachGroupId = new HashMap<String, List<String>>();
		//key:subjectId,value:教师ids
		Map<String,List<String>> subjectIdToTeacherId = new HashMap<String, List<String>>();
		Set<String> subjectIds = EntityUtils.getSet(newGkCourseHeapList, "subjectId");
		for (String string : subjectIds) {
			subjectIdToTeachGroupId.put(string, new ArrayList<String>());
			subjectIdToTeacherId.put(string, new ArrayList<String>());
		}
		//1、已安排的老师
		Set<String> timetableIds=EntityUtils.getSet(newGkCourseHeapList, "timetableId");
		//key:subjectId values:teacherIds 预排老师必须选中
		Map<String,Set<String>> arrayTeacherId=new HashMap<String,Set<String>>();
		if(CollectionUtils.isNotEmpty(timetableIds)){
			List<NewGkTimetable> list = newGkTimetableService.findListByIds(timetableIds.toArray(new String[]{}));
			Map<String, NewGkTimetable> gkTimetableMap = EntityUtils.getMap(list,"id");
			List<NewGkTimetableTeacher> tlist = newGkTimetableTeacherService.findByTimetableIds(timetableIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(tlist)){
				for (NewGkTimetableTeacher newGkTimetableTeacher : tlist) {
					if(newGkTimetableTeacher.getTeacherId() == null) {
//						continue;
					}
					String sub = gkTimetableMap.get(newGkTimetableTeacher.getTimetableId()).getSubjectId();
					if(!arrayTeacherId.containsKey(sub)){
						arrayTeacherId.put(sub, new HashSet<String>());
					}
					arrayTeacherId.get(sub).add(newGkTimetableTeacher.getTeacherId());
				}
			}
		}
		map.put("beforeArrangeSize", arrayTeacherId.size());
		
		Map<String, String> subjectIdToSubjectName = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		
		//2、已经保存的数据
		List<NewGkSubjectTeacher> selectSubjectTeacher = newGkSubjectTeacherService.findByArrayId(arrayId);
		//key:subjectId values:教师ids
		Map<String, List<String>> selectSubjectTeacherMap = EntityUtils.getListMap(selectSubjectTeacher, "subjectId", "teacherId");
		
		//3、查教研组数据，组装subjectIdToTeachGroupId，subjectIdToTeacherId
		Map<String, List<String>> teachGroupIdToTeacherId=new HashMap<String, List<String>>();
		String unitId = getLoginInfo().getUnitId();
		if(subjectIds!=null && !subjectIds.isEmpty()){
			List<TeachGroup> baseTeachGroupList = SUtils.dt(teachGroupRemoteService.findBySchoolIdAndSubjectIdIn(unitId,subjectIds.toArray(new String[0])),TeachGroup.class);
			for (TeachGroup baseTeachGroup : baseTeachGroupList) {
				String subjectId = baseTeachGroup.getSubjectId();
				if(subjectIdToTeachGroupId.containsKey(subjectId)){
	            	subjectIdToTeachGroupId.get(subjectId).add(baseTeachGroup.getId());
	            }
			}
			Set<String> baseTeachGroupIdSet = EntityUtils.getSet(baseTeachGroupList, "id");
			List<TeachGroupEx> baseTeachGroupExList = SUtils.dt(teachGroupExRemoteService.findByTeachGroupId(baseTeachGroupIdSet.toArray(new String[0])),TeachGroupEx.class);
			teachGroupIdToTeacherId = EntityUtils.getListMap(baseTeachGroupExList, "teachGroupId", "teacherId");
		}
		
			
		//总共老师数量teacherNumber
		for(Map.Entry<String,List<String>> entry : subjectIdToTeachGroupId.entrySet()) {
			String subjectId = entry.getKey();
			//teacherGroupId集合
			List<String> teacherGroupId = entry.getValue();
			for (String tgId : teacherGroupId) {
				List<String> teacherIds = teachGroupIdToTeacherId.get(tgId);
				if(teacherIds!=null && !teacherIds.isEmpty()){
					for (String teacherId : teacherIds) {
						if(!subjectIdToTeacherId.get(subjectId).contains(teacherId)){
							subjectIdToTeacherId.get(subjectId).add(teacherId);
						}
					}
				}
			}
			//往科目组添加未存在的预排教师id
			if(arrayTeacherId.containsKey(subjectId)){
				for(String s:arrayTeacherId.get(subjectId)){
					if(!subjectIdToTeacherId.get(subjectId).contains(s)){
						//去重复教师id
						subjectIdToTeacherId.get(subjectId).add(s);
					}
				}
			}
		}
		
		
		Map<String, Set<String>> classNumbers = new HashMap<String, Set<String>>();
		for (NewGkCourseHeap newGkCourseHeap : newGkCourseHeapList) {
			if(!classNumbers.containsKey(newGkCourseHeap.getSubjectId())){
				classNumbers.put(newGkCourseHeap.getSubjectId(), new HashSet<String>());
			}
			classNumbers.get(newGkCourseHeap.getSubjectId()).add(newGkCourseHeap.getTimetableId());
		}
		Map<String, Set<Integer>> teacherNumbers = new HashMap<String, Set<Integer>>();
		for (NewGkCourseHeap newGkCourseHeap : newGkCourseHeapList) {
			if(!teacherNumbers.containsKey(newGkCourseHeap.getSubjectId())){
				teacherNumbers.put(newGkCourseHeap.getSubjectId(), new HashSet<Integer>());
			}
			teacherNumbers.get(newGkCourseHeap.getSubjectId()).add(newGkCourseHeap.getHeapNum());
		}
		Set<String> teacherIdSet = new HashSet<String>();
		Map<String,SubjectInfo> subjectInfoMap = new HashMap<String,SubjectInfo>();
		for(Map.Entry<String,List<String>> entry : subjectIdToTeacherId.entrySet()) {
			String subjectId = entry.getKey();
			subjectInfoMap.put(subjectId, new SubjectInfo());
			//设置subjectId
			subjectInfoMap.get(subjectId).setSubjectId(subjectId);
			//设置subjectName
			subjectInfoMap.get(subjectId).setSubjectName(subjectIdToSubjectName.get(subjectId));
			//设置classNumber
			subjectInfoMap.get(subjectId).setClassNumber(classNumbers.get(subjectId).size());
			//设置teacherNumber
			subjectInfoMap.get(subjectId).setTeacherNumber(teacherNumbers.get(subjectId).size());
			List<String> teacherIds = entry.getValue();
			for (String teacherId : teacherIds) {
				teacherIdSet.add(teacherId);
				subjectInfoMap.get(subjectId).getTeacherIdAndState().put(teacherId, "0");
			}
			//改变已经选择老师的状态
			List<String> selectTeacherIds = selectSubjectTeacherMap.get(subjectId);
			Set<String> chooseTId=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(selectTeacherIds)) {
				for (String teacherId : selectTeacherIds) { 
					subjectInfoMap.get(subjectId).getTeacherIdAndState().put(teacherId, "1");
					chooseTId.add(teacherId);
				}
			}
			//预排老师状态 2
			Set<String> arrTeacherIds = arrayTeacherId.get(subjectId);
			if(CollectionUtils.isNotEmpty(arrTeacherIds)){
				for (String teacherId : arrTeacherIds) { 
					subjectInfoMap.get(subjectId).getTeacherIdAndState().put(teacherId, "2");
					chooseTId.add(teacherId);
				}
			}
			subjectInfoMap.get(subjectId).setSelectTeacherNumber(chooseTId.size());
		}

		Map<String, String> teachMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[teacherIdSet.size()])),new TypeReference<Map<String, String>>(){});
		map.put("teacherIdToTeacherName", teachMap);
		
		map.put("subjectInfoMap", subjectInfoMap);
		
		map.put("teacherNumber", teacherIdSet.size() + "");
		
		return "/newgkelective/array/teacherIndex.ftl";
	}

	private String convert(String tempString) {
		List<Integer> numbers = new ArrayList<Integer>();
		
		String[] split = tempString.split("-");
		for (String string : split) {
			numbers.add(Integer.parseInt(string));
		}
		Collections.sort(numbers);
		String result = "";
		for (Integer integer : numbers) {
			String temp = "";
			if( integer > 100 && integer < 1000) {
				temp += "星期一";
				//两位数,代表周一
				int shi = integer / 100;
				int ge = integer/10%10;
				int bi = integer%10;
				if(shi == 2) {
					temp += "上午";
				}else if(shi == 3){
					temp += "下午";
				}else if(shi == 4) {
					temp += "晚上";
				}
				if(ge == 1) {
					temp += "第一节";
				} else if(ge == 2) {
					temp += "第二节";
				} else if(ge == 3) {
					temp += "第三节";
				} else if(ge == 4) {
					temp += "第四节";
				} else if(ge == 5) {
					temp += "第五节";
				}
				switch(bi) {
					case 1: temp += "(单)"; break;
					case 2: temp += "(双)"; break;
				}
			}else {
				// 9999
				temp = "";
				int bai = integer / 1000;
				int shi = integer / 100 %10;
				int ge = integer/10 % 10;
				int bi = integer% 10;
				if(bai == 1) {
					temp += "星期二";
				}else if(bai == 2) {
					temp += "星期三";
				}else if(bai == 3) {
					temp += "星期四";
				}else if(bai == 4) {
					temp += "星期五";
				}else if(bai == 5) {
					temp += "星期六";
				}else if(bai == 6) {
					temp += "星期日";
				}
				if(shi == 2) {
					temp += "上午";
				}else if(shi == 3){
					temp += "下午";
				}else if(shi == 4) {
					temp += "晚上";
				}
				if(ge == 1) {
					temp += "第一节";
				} else if(ge == 2) {
					temp += "第二节";
				} else if(ge == 3) {
					temp += "第三节";
				} else if(ge == 4) {
					temp += "第四节";
				} else if(ge == 5) {
					temp += "第五节";
				}
				switch(bi) {
					case 1: temp += "(单)"; break;
					case 2: temp += "(双)"; break;
				}
			}
			result = result + "，" + temp;
		}
		String havaClassTime = result.substring(1);
		return havaClassTime;
	}
}
