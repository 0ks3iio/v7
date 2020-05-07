package net.zdsoft.newgkelective.data.service.impl;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.ClassFeatureDto;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.CourseScheduleModifyDto;
import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableDao;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableJdbcDao;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableOtherDao;
import net.zdsoft.newgkelective.data.dto.ArrayResultSubjectDto;
import net.zdsoft.newgkelective.data.dto.ArraySearchDto;
import net.zdsoft.newgkelective.data.dto.NewGkArrayResultSaveDto;
import net.zdsoft.newgkelective.data.dto.NewGkClassFeatureDto;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("newGkTimetableService")
public class NewGkTimetableServiceImpl extends BaseServiceImpl<NewGkTimetable, String> implements NewGkTimetableService{

	@Autowired
	private NewGkTimetableDao newGkTimetableDao;
	@Autowired
	private NewGkTimetableJdbcDao newGkTimetableJdbcDao;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private NewGkClassStudentService newGkClassStudentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkTimetableOtherDao newGkTimetableOtherDao;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private NewGkTeacherPlanExService newGkTeacherPlanExService;
	@Autowired
	private NewGkPlaceItemService newGkPlaceItemService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkClassCombineRelationService combineRelationService;
	
	public static final Logger LOG = LoggerFactory.getLogger(NewGkTimetableServiceImpl.class);
	
	@Override
	protected BaseJpaRepositoryDao<NewGkTimetable, String> getJpaDao() {
		return newGkTimetableDao;
	}

	@Override
	protected Class<NewGkTimetable> getEntityClass() {
		return NewGkTimetable.class;
	}

	public NewGkTimetable findbySubIdAndClaId(String unitId, String subjectId,String classId){
		NewGkTimetable time=newGkTimetableDao.findbySubIdAndClaId(unitId, subjectId, classId);
		if(time==null) time=new NewGkTimetable();
		return time;
	}
	@Override
	public List<NewGkTimetable> findByArrayId(String unitId, String arrayId) {
		return newGkTimetableDao.findByUnitIdAndArrayId(unitId, arrayId);
	}
	@Override
	public List<NewGkTimetable> findByArrayIdWithMaster(String unitId, String arrayId) {
		return findByArrayId(unitId, arrayId);
	}

	@Override
	public void saveAll(String unitId, String divideId, NewGkArrayResultSaveDto dto) {
		String[] timeTableIds = dto.getTimeTableIds();
		if(timeTableIds!=null && timeTableIds.length>0){
			newGkTimetableTeacherService.deleteByTimetableIdIn(timeTableIds);
			newGkTimetableOtherService.deleteByTimetableIdIn(timeTableIds);
			
			newGkTimetableJdbcDao.deleteByArrayId(null,timeTableIds);
		}
		if(dto.getNewGkArray() != null) {
			newGkArrayService.save(dto.getNewGkArray());
		}else if(StringUtils.isNotBlank(dto.getStat())){
			newGkArrayService.updateStatById(dto.getStat(), dto.getArrayId());
		}
		if(CollectionUtils.isNotEmpty(dto.getInsertClassList())) {
			newGkDivideClassService.saveAllList(unitId, null, null, dto.getInsertClassList(), dto.getInsertStudentList(), false);
		}
		if(CollectionUtils.isNotEmpty(dto.getInsertTimeTableList())){
			newGkTimetableJdbcDao.insertBatch(checkSave(dto.getInsertTimeTableList().toArray(new NewGkTimetable[]{})));
		}
		if(CollectionUtils.isNotEmpty(dto.getInsertTeacherList())){
			newGkTimetableTeacherService.saveAllEntity(dto.getInsertTeacherList());
		}
		if(CollectionUtils.isNotEmpty(dto.getUpdateTeacherList())){
			newGkTimetableTeacherService.saveAll(dto.getUpdateTeacherList().toArray(new NewGkTimetableTeacher[0]));
		}
		if(CollectionUtils.isNotEmpty(dto.getInsertOtherList())){
			newGkTimetableOtherService.saveAllEntity(dto.getInsertOtherList());
		}
		//手动排课时使用
		if(dto.getDelTimeTableOtherIds()!= null && dto.getDelTimeTableOtherIds().length >0) {
			newGkTimetableOtherService.deleteByIdIn(dto.getDelTimeTableOtherIds());
		}
	}

	@Override
	public void makeTime(String unitId, String arrayId,List<NewGkTimetable> list) {
		if(CollectionUtils.isNotEmpty(list)){
//			Set<String> ids = EntityUtils.getSet(list, "id");
			List<NewGkTimetableOther> otherlist = newGkTimetableOtherService.findByArrayId(unitId ,arrayId);
			Map<String,List<NewGkTimetableOther>> otherMap=new HashMap<String,List<NewGkTimetableOther>>();
			if(CollectionUtils.isNotEmpty(otherlist)){
				for(NewGkTimetableOther o:otherlist){
					makeTimeStrName(o);
					if(!otherMap.containsKey(o.getTimetableId())){
						otherMap.put(o.getTimetableId(), new ArrayList<NewGkTimetableOther>());
					}
					otherMap.get(o.getTimetableId()).add(o);
				}
				
			}
			for(NewGkTimetable tt:list){
				if(otherMap.containsKey(tt.getId())){
					tt.setTimeList(otherMap.get(tt.getId()));
				}
			}
		}
		
	}

	private void makeTimeStrName(NewGkTimetableOther o) {
		String timeTr="";
		timeTr=timeTr+BaseConstants.dayOfWeekMap.get(o.getDayOfWeek()+"");
		timeTr=timeTr+BaseConstants.PERIOD_INTERVAL_Map.get(o.getPeriodInterval());
		timeTr=timeTr+"第"+o.getPeriod()+"节";
		o.setTimeTr(timeTr);
	}

	@Override
	public List<NewGkTimetable> findByArrayIdAndClassType(String unitId, String arrayId,
			String[] classTypes) {
		return newGkTimetableDao.findByArrayIdAndClassType(unitId,arrayId,classTypes);
	}

	@Override
	public List<NewGkTimetable> findByArrayIdAndClassId(String unitId, String arrayId, String classId) {
		return newGkTimetableDao.findByUnitIdAndArrayIdAndClassId(unitId, arrayId,classId);
	}

	@Override
	public List<ArrayResultSubjectDto> findByMoreConditions(String unitId,
			String arrayId, ArraySearchDto searchDto) {
		List<NewGkTimetable> timetableList = null;
		//根据科目查询
		if(StringUtils.isNotBlank(searchDto.getSubjectIds())){
			String[] subjectIds = searchDto.getSubjectIds().split(",");
			timetableList = newGkTimetableDao.findByUnitIdAndArrayIdAndClassTypeInAndSubjectIdIn(unitId,arrayId,
					new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4},subjectIds);
		}else{
			timetableList = newGkTimetableDao.findByArrayIdAndClassType(unitId, arrayId, 
					new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4});
		}
		if(CollectionUtils.isEmpty(timetableList)){
			return new ArrayList<ArrayResultSubjectDto>(); 
		}
		
		Map<String, String> bestTypeMap = getBestTypeMap();
		Map<String, String> subjectTypeMap = getSubjectTypeMap();
		
		String subjectType = searchDto.getSubjectType();
		String bestType = searchDto.getBestType();
		Map<String, String> timetableClassMap = EntityUtils.getMap(timetableList, "id","classId");
		//根据班级将timetable分组
		Map<String,List<NewGkTimetable>> classTimetableMap = new HashMap<String, List<NewGkTimetable>>();
		Set<String> clsIdSubIdSet = new HashSet<>();
		
		for (NewGkTimetable newGkTimetable : timetableList) {
			// TODO 在此处过滤 场地 和 选 学考 类型
			if(StringUtils.isNotBlank(subjectType) && !subjectType.equals(newGkTimetable.getSubjectType())) {
				continue;
			}
			
			List<NewGkTimetable> inList = classTimetableMap.get(newGkTimetable.getClassId());
			if(inList==null){
				inList = new ArrayList<NewGkTimetable>();
				classTimetableMap.put(newGkTimetable.getClassId(), inList);
			}
			inList.add(newGkTimetable);
			clsIdSubIdSet.add(newGkTimetable.getClassId()+newGkTimetable.getSubjectId());
		}
		
		//根据教室筛选
		List<String> placeIdList=null; 
		if(StringUtils.isNotBlank(searchDto.getPlaceIds())){
			String[] placeIds= searchDto.getPlaceIds().split(",");
			placeIdList = Arrays.asList(placeIds);
		}
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", EntityUtils.getList(timetableList, "id").toArray(new String[0]));
		Set<String> placeIdSet = EntityUtils.getSet(timetableOtherList,NewGkTimetableOther::getPlaceId);
		Map<String, String> placeNameMap = SUtils.dt(
				teachPlaceRemoteService.findTeachPlaceMapByAttr(placeIdSet.toArray(new String[0]), "placeName"),
				new TR<Map<String, String>>() {
				});
		Map<String,Set<String>> classPlaceMap = new HashMap<String, Set<String>>();
		for (NewGkTimetableOther timetableOther : timetableOtherList) {
			Set<String> inSet = classPlaceMap.get(timetableClassMap.get(timetableOther.getTimetableId()));
			if(inSet==null){
				inSet = new HashSet<String>();
				classPlaceMap.put(timetableClassMap.get(timetableOther.getTimetableId()), inSet);
			}
			if(StringUtils.isNotBlank(timetableOther.getPlaceId())) {
				inSet.add(timetableOther.getPlaceId());
			}
		}
		
		
		if(CollectionUtils.isNotEmpty(placeIdList)){
			for (Entry<String, Set<String>> entry : classPlaceMap.entrySet()) {
				if(CollectionUtils.isEmpty(entry.getValue())){
					classTimetableMap.remove(entry.getKey());
				}else{
					boolean flag = true;
					for (String placeId : placeIdList) {
						if(entry.getValue().contains(placeId)){
							flag=false;
							break;
						}
					}
					if(flag)classTimetableMap.remove(entry.getKey());
				}
			}
		}
		
		Set<String> timetableIdSet = new HashSet<String>();
		for (List<NewGkTimetable> list : classTimetableMap.values()) {
			timetableIdSet.addAll(EntityUtils.getSet(list, "id"));
		}
		
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findListByIn("timetableId", timetableIdSet.toArray(new String[0]));
		Map<String, String> teacherNameMap = SUtils.dt(teacherRemoteService.findPartByTeacher(EntityUtils.getSet(timetableTeacherList, "teacherId").toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		//根据班级将教师分组,key-classId,value-teacherIdList
		Map<String,Set<String>> classTeacherMap = new HashMap<String, Set<String>>();
		Map<String,List<String>> tabTeacherIdMap = new HashMap<>();
		for (NewGkTimetableTeacher timetableTeacher : timetableTeacherList) {
			String timetableId = timetableTeacher.getTimetableId();
			Set<String> teacherIdSet = classTeacherMap.get(timetableClassMap.get(timetableId));
			if(teacherIdSet==null){
				teacherIdSet = new HashSet<String>();
				classTeacherMap.put(timetableClassMap.get(timetableId), teacherIdSet);
			}
			teacherIdSet.add(timetableTeacher.getTeacherId());
			List<String> tids = tabTeacherIdMap.get(timetableId);
			if(tids == null) {
				tids = new ArrayList<>();
				tabTeacherIdMap.put(timetableId, tids);
			}
			tids.add(timetableTeacher.getTeacherId());
		}
		Map<String,String> tabTnameMap = new HashMap<>(); 
		for (String tabId : tabTeacherIdMap.keySet()) {
			String tnames = tabTeacherIdMap.get(tabId).stream().filter(e->teacherNameMap.containsKey(e))
					.map(e->teacherNameMap.get(e)).collect(Collectors.joining(","));
			tabTnameMap.put(tabId, tnames);
		}
		
		Map<String,String> classTeacherNameMap = new HashMap<String, String>();
		for (String classId : classTimetableMap.keySet()) {
			Set<String> teacherIdSet = classTeacherMap.get(classId);
			String name = "";
			if(CollectionUtils.isNotEmpty(teacherIdSet)){
				for (String teacherId : teacherIdSet) {
					if(teacherNameMap.get(teacherId) == null) {
						continue;
					}
					name += teacherNameMap.get(teacherId)+"、";
				}
			}
			if(!name.equals("")){
				name = name.substring(0,name.length()-1);
			}
			classTeacherNameMap.put(classId, name);
		}
		
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(classTimetableMap.keySet().toArray(new String[0]));
		if(CollectionUtils.isEmpty(divideClassList))
			return new ArrayList<>();
		//根据subjectType和bestType筛选
		getNewDivideClassList(subjectType, bestType, divideClassList);
		
		NewGkArray array = newGkArrayService.findOne(arrayId);
		List<NewGkClassStudent> classStudentList = newGkClassStudentService.findListBy(NewGkClassStudent.class,
				new String[] { "unitId","divideId" }, new String[] { unitId, array.getId()}, "classId",
				EntityUtils.getList(divideClassList, "id").toArray(new String[0]), new String[] {"classId", "studentId" });
		Set<String> studentIdSet = EntityUtils.getSet(classStudentList, "studentId");
		
//		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])),new TR<List<Student>>(){});
//		List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, null,null, studentIdSet.toArray(new String[0])),new TR<List<Student>>(){});
		Map<String, Integer> studentSexMap = SUtils.dt(
				studentRemoteService.findMapByAttr(studentIdSet.toArray(new String[0]), "sex"),
				new TR<Map<String, Integer>>() {
				});
		
//		Map<String, Integer> studentSexMap = EntityUtils.getMap(studentList, "id","sex");
		//根据班级将学生分组，key-classId,value-studentNum
		Map<String,Integer> classStudentMap = new HashMap<String, Integer>();
		Map<String,Integer> classBoyMap = new HashMap<String, Integer>();
		for (NewGkClassStudent classStudent : classStudentList) {
			Integer count = classStudentMap.get(classStudent.getClassId());
			if(count==null){
				count=0;
			}
			classStudentMap.put(classStudent.getClassId(), ++count);
			Integer boyCount = classBoyMap.get(classStudent.getClassId());
			if(boyCount==null){
				boyCount=0;
			}
			
			if(Integer.valueOf(BaseConstants.MALE).equals(studentSexMap.get(classStudent.getStudentId()))){
				boyCount++;
			}
			classBoyMap.put(classStudent.getClassId(), boyCount);
		}
		
		Set<String> subIds = classTimetableMap.values().stream().flatMap(e->e.stream()).map(e->e.getSubjectId()).collect(Collectors.toSet());
		Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subIds.toArray(new String[0])), new TypeReference<Map<String,String>>() {});
		
		List<ArrayResultSubjectDto> dtoList = new ArrayList<ArrayResultSubjectDto>();
		for (NewGkDivideClass divideClass : divideClassList) {
			List<NewGkTimetable> list = classTimetableMap.get(divideClass.getId());
			
			for (NewGkTimetable tab : list) {
				ArrayResultSubjectDto dto = new ArrayResultSubjectDto();
				dto.setClassId(divideClass.getId());
				dto.setClassName(divideClass.getClassName());
				dto.setSubjectName(subNameMap.get(tab.getSubjectId()));
				dto.setSubjectType(subjectTypeMap.get(tab.getSubjectType()));
				dto.setBestType(bestTypeMap.get(divideClass.getBestType()));
				String placeName = "";
				Set<String> placeIds = classPlaceMap.get(divideClass.getId());
				if(CollectionUtils.isNotEmpty(placeIds)){
					for (String placeId : placeIds) {
						placeName += placeNameMap.get(placeId)+"、";
					}
				}
				if(!placeName.equals("")){
					placeName = placeName.substring(0,placeName.length()-1);
				}
				dto.setPlaceNames(placeName);
				dto.setTeacherNames(tabTnameMap.get(tab.getId()));
				dto.setStudentNum(classStudentMap.get(divideClass.getId()));
				dto.setBoyNum(classBoyMap.get(divideClass.getId())==null?0:classBoyMap.get(divideClass.getId()));
				dto.setGirlNum(classStudentMap.get(divideClass.getId())==null?0:classStudentMap.get(divideClass.getId())-dto.getBoyNum());
				
				dtoList.add(dto);
				
			}
		}
		
		
		
		return dtoList;
	}
	
	private void getNewDivideClassList(String subjectType,String bestType,List<NewGkDivideClass> divideClassList){
		if(StringUtils.isNotBlank(subjectType)){
//			Iterator<NewGkDivideClass> iterator = divideClassList.iterator();
//			while(iterator.hasNext()){
//				NewGkDivideClass newGkDivideClass = iterator.next();
//				if(NewGkElectiveConstant.CLASS_TYPE_2.equals(newGkDivideClass.getClassType()) && !subjectType.equals(newGkDivideClass.getSubjectType())){
//					iterator.remove();
//				}
//			}
		}
		if(StringUtils.isNotBlank(bestType)){
			Iterator<NewGkDivideClass> iterator = divideClassList.iterator();
			while(iterator.hasNext()){
				NewGkDivideClass newGkDivideClass = iterator.next();
				if(bestType.equals(NewGkElectiveConstant.BEST_TYPE_2)){
					if(NewGkElectiveConstant.BEST_TYPE_1.equals(newGkDivideClass.getBestType())){
						iterator.remove();
					}
				}else{
					if(!bestType.equals(newGkDivideClass.getBestType())){
						iterator.remove();
					}
				}
			}
		}
	}
	
	private Map<String, String> getSubjectTypeMap() {
		Map<String,String> subjectTypeMap = new HashMap<String,String>();
		subjectTypeMap.put(NewGkElectiveConstant.SUBJECT_TYPE_A, "选考");
		subjectTypeMap.put(NewGkElectiveConstant.SUBJECT_TYPE_B, "学考");
		return subjectTypeMap;
	}
	
	private Map<String, String> getBestTypeMap() {
		Map<String,String> bestTypeMap = new HashMap<String, String>();
		bestTypeMap.put(NewGkElectiveConstant.BEST_TYPE_1, "尖子班");
		bestTypeMap.put(NewGkElectiveConstant.BEST_TYPE_2, "平行班");
		return bestTypeMap;
	}

	@Override
	public void makeTeacher(List<NewGkTimetable> timetableList) {
		if(CollectionUtils.isEmpty(timetableList)){
			return;
		}
		Set<String> ids=EntityUtils.getSet(timetableList, "id");
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(ids.toArray(new String[]{}));
		if(CollectionUtils.isEmpty(timetableTeacherList)){
			return;
		}
		Map<String,List<String>> timeTableTeacherMap = new HashMap<String, List<String>>();
		for (NewGkTimetableTeacher timetableTeacher : timetableTeacherList) {
			if(!timeTableTeacherMap.containsKey(timetableTeacher.getTimetableId())){
				timeTableTeacherMap.put(timetableTeacher.getTimetableId(), new ArrayList<String>());
			}
			timeTableTeacherMap.get(timetableTeacher.getTimetableId()).add(timetableTeacher.getTeacherId());
		}
		for(NewGkTimetable t:timetableList){
			if(timeTableTeacherMap.containsKey(t.getId())){
				t.setTeacherIdList(timeTableTeacherMap.get(t.getId()));
			}
		}
	}

	@Override
	public List<NewGkTimetable> findByArrayIdAndIdIn(String unitId, String arrayId,
			String[] array) {
		if(array != null && array.length > 0) {
			return newGkTimetableDao.findByUnitIdAndArrayIdAndIdIn(unitId,arrayId,array);
		}
		return new ArrayList<NewGkTimetable>();
	}

	@Override
	public List<NewGkTimetable> findByArrayIdAndClassIds(String unitId, String arrayId,
			String[] classIds) {
		if(ArrayUtils.isEmpty(classIds)){
			return newGkTimetableDao.findByUnitIdAndArrayId(unitId, arrayId);
		}
		return newGkTimetableDao.findByUnitIdAndArrayIdAndClassIdIn(unitId, arrayId, classIds);
	}

	@Override
	public List<NewGkTimetable> findByArrayIdAndSubjectId(String unitId, String arrayId,
			String[] subjectIds) {
		return newGkTimetableDao.findByUnitIdAndArrayIdAndSubjectIdIn(unitId,arrayId,subjectIds);
	}

	@Override
	public void deleteByArrayId(String arrayId) {
		
		List<NewGkTimetable> ttList = this.findListBy(NewGkTimetable.class, new String[] {"arrayId"},
				new String[] {arrayId}, new String[] {"id"});
		newGkTimetableJdbcDao.deleteByArrayId(arrayId, null);
		
		Set<String> ttIds = EntityUtils.getSet(ttList, NewGkTimetable::getId);
		newGkTimetableOtherService.deleteByTimetableIdIn(ttIds.toArray(new String[0]));
	}

	@Override
	public CourseScheduleModifyDto findClassScheduleInfo(String arrayId, String classId) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		String lessonArrangeId = array.getLessonArrangeId();
		// 获取 班级课程 信息 以及 班级应上课程信息
		List<CourseSchedule> scheduleList = findScheduleByClass(array.getUnitId(), arrayId, new String[] {classId});
		
		// 班级应上课程信息  课程特征以及 班级特征
		/* 已经有保存的结果 */
		NewGkDivideClass divideClass = newGkDivideClassService.findOne(classId);
		List<NewGkClassFeatureDto> savedCourseTimeList = newGkClassSubjectTimeService.makeExistsClassSubjectInfo(array.getUnitId(), lessonArrangeId, divideClass.getOldDivideClassId());
		
		/* 没有保存的结果  */
//		List<NewGkSubjectTime> courseTimeList = newGkClassSubjectTimeService.findClassSubjectList(lessonArrangeId, divideClass.getOldDivideClassId(), array.getUnitId());
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemId(lessonArrangeId);
		Map<String,NewGkSubjectTime> courseTimeMap =  EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+e.getSubjectType());
		Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(array.getUnitId(), array.getId(), lessonArrangeId, NewGkElectiveConstant.CLASS_SOURCE_TYPE2,
				Arrays.asList(classId));
		List<NewGkSubjectTime> courseTimeList = xzbSubjects.get(classId).stream().filter(e->courseTimeMap.containsKey(e[0]+e[1]))
				.map(e->courseTimeMap.get(e[0]+e[1])).collect(Collectors.toList());
		List<NewGkClassFeatureDto> classTimeList = courseTimeList.stream()
				.map(e->new NewGkClassFeatureDto(e.getSubjectId(),e.getSubjectType(),e.getPeriod(),NewGkElectiveConstant.WEEK_TYPE_NORMAL))
				.collect(Collectors.toList());
		
		// 没有保存结果 根据 科目获取不排课时间
		Set<String> subjectIds = classTimeList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		Map<String, NewGkClassFeatureDto> codToMap = EntityUtils.getMap(classTimeList, NewGkClassFeatureDto::getSubjectCode);
		String[] objectIds = subjectIds.toArray(new String[0]);
		String[] objectType = new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_9};
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(lessonArrangeId, objectIds, objectType, true);
//		Map<String,List<String>> subjectNoTimeMap = new HashMap<>();
		for (NewGkLessonTime lt : lessonTimeList) {
			String courseCode = lt.getObjectId()+"-"+lt.getLevelType();
			
			if(CollectionUtils.isNotEmpty(lt.getTimesList()) && codToMap.containsKey(courseCode)) {
				String noArrangeTime = lt.getTimesList().stream()
						.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType()))
						.map(e -> e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod())
						.collect(Collectors.joining(","));
				if(StringUtils.isBlank(noArrangeTime)) {
					continue;
				}
				codToMap.get(courseCode).setNoArrangeTime(noArrangeTime);
			}
		}
		
		
		// 如果某个科目在课程特征中有在 班级特征中没有 就将它添加到 班级特征中去
		Set<String> codes = EntityUtils.getSet(savedCourseTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		for (NewGkClassFeatureDto dto : classTimeList) {
			if(!codes.contains(dto.getSubjectId()+"-"+dto.getSubjectType())) {
				savedCourseTimeList.add(dto);
			}
		}
		
		// 获取科目 名称
		makeCourseName(savedCourseTimeList);

		// 组装教师
		Map<String,NewGkClassFeatureDto> subIds = EntityUtils.getMap(savedCourseTimeList, e -> e.getSubjectId(),e->e);

		List<NewGkTeacherPlan> plans = newGkTeacherPlanService.findByArrayItemIdAndSubjectIdIn(array.getLessonArrangeId(),
				subIds.keySet().toArray(new String[0]), true);
		for (NewGkTeacherPlan plan : plans) {
			List<NewGkTeacherPlanEx> exList = plan.getTeacherPlanExList();
			if(CollectionUtils.isNotEmpty(exList)){
				for (NewGkTeacherPlanEx ex : exList) {
					if(ex.getClassIds()!=null
							&& ex.getClassIds().contains(divideClass.getOldDivideClassId())
							&& subIds.containsKey(plan.getSubjectId())){
						subIds.get(plan.getSubjectId()).setTeacherId(ex.getTeacherId());
					}
				}
			}

		}

		/* 年级禁排时间  */
		/*List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeService.findByItemIdObjectId(lessonArrangeId, null,
				new String[] { NewGkElectiveConstant.LIMIT_GRADE_0 }, true);
		List<String> gradeNoClick = newGkLessonTimeList.stream()
				.filter(e->CollectionUtils.isNotEmpty(e.getTimesList()))
				.flatMap(e->e.getTimesList().stream())
				.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType()))
				.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
				.collect(Collectors.toList());*/
		Map<NewGkClassFeatureDto, NewGkClassFeatureDto> dtoMap = new HashMap<>();
		Map<String, NewGkClassFeatureDto> codeDtoMap = EntityUtils.getMap(savedCourseTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		List<List<NewGkClassFeatureDto>> list = new ArrayList<>();
		Set<NewGkClassFeatureDto> used = new HashSet<>();
		for (NewGkSubjectTime st : courseTimeList) {
			NewGkClassFeatureDto dto1 = codeDtoMap.get(st.getSubjectId()+"-"+st.getSubjectType());
			if(NewGkElectiveConstant.FIRSTSD_WEEK_3 != st.getFirstsdWeek() && st.getFirstsdWeekSubject() != null) {
				if(!used.contains(dto1)) {
					NewGkClassFeatureDto dto2 = codeDtoMap.get(st.getFirstsdWeekSubject());
					if(dto1.getCourseWorkDay()<1 || dto2.getCourseWorkDay()<1){
						used.add(dto2);
						continue;
					}

					if(Objects.equals(NewGkElectiveConstant.WEEK_TYPE_ODD,dto2.getWeekType())){
						list.add(Arrays.asList(dto2,dto1));
					}else{
						list.add(Arrays.asList(dto1,dto2));
					}
					used.add(dto2);
					if(dto1.getCourseWorkDay() > 1) {
						dto1.setCourseWorkDay(dto1.getCourseWorkDay()-1);
						list.add(Arrays.asList(dto1));
					}
					if(dto2.getCourseWorkDay() > 1) {
						dto2.setCourseWorkDay(dto2.getCourseWorkDay()-1);
						list.add(Arrays.asList(dto2));
					}
				}
			}else {
				if(dto1.getCourseWorkDay()>0)
					list.add(Arrays.asList(dto1));
			}
		}
		// 转换NewGkClassFeatureDto -> ClassFeatureDto
		List<List<ClassFeatureDto>> classSubjectList = transferClassFeatureDto(list);
		classSubjectList.stream().flatMap(e->e.stream()).forEach(e->e.setClassId(classId));
		
		CourseScheduleModifyDto dto = new CourseScheduleModifyDto();
		dto.setScheduleList(scheduleList);
		dto.setClassSubjectList(classSubjectList);
//		dto.setGradeNoClick(gradeNoClick);
		
		return dto;
	}

	@Override
	public CourseScheduleModifyDto findClassScheduleInfoWithMaster(String arrayId, String classId) {
		return this.findClassScheduleInfo(arrayId, classId);
	}
	
	private List<List<ClassFeatureDto>> transferClassFeatureDto(List<List<NewGkClassFeatureDto>> list) {
		List<List<ClassFeatureDto>> classSubjectList = new ArrayList<>();
		ClassFeatureDto cfd = null;
		for (List<NewGkClassFeatureDto> l1 : list) {
			List<ClassFeatureDto> dl = new ArrayList<>();
			for (NewGkClassFeatureDto dto : l1) {
				cfd = new ClassFeatureDto();
				cfd.setSubjectId(dto.getSubjectId());
				cfd.setSubjectName(dto.getSubjectName());
				cfd.setSubjectType(dto.getSubjectType());
				cfd.setTeacherId(dto.getTeacherId());
				cfd.setTeacherName(dto.getTeacherName());
				cfd.setCourseWorkDay(dto.getCourseWorkDay());
				cfd.setNoArrangeTime(dto.getNoArrangeTime());
				cfd.setCombineClass(dto.getCombineClass());
				cfd.setMeanwhiles(dto.getMeanwhiles());
				cfd.setClassId(dto.getClassId());
				cfd.setWeekType(dto.getWeekType());
				dl.add(cfd);
			}
			classSubjectList.add(dl);
		}
		return classSubjectList;
	}

	private void makeCourseName(List<NewGkClassFeatureDto> courseTimeList) {
		Set<String> subjectIds = courseTimeList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[] {}));
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		courseTimeList.forEach(c->{
			if(courseMap.get(c.getSubjectId()) != null) {
				c.setSubjectName(courseMap.get(c.getSubjectId()).getSubjectName());
//				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(c.getSubjectType())) {
//					c.setSubjectName(c.getSubjectName()+"选");
//				}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(c.getSubjectType())) {
//					c.setSubjectName(c.getSubjectName()+"学");
//				}
			}
		});
		// 排序
		Collections.sort(courseTimeList, (x,y)->{
			if(courseMap.get(x.getSubjectId()) != null) {
				return Optional.ofNullable(courseMap.get(x.getSubjectId()).getOrderId()).orElse(Integer.MAX_VALUE)
						.compareTo(Optional.ofNullable(
								Optional.ofNullable(courseMap.get(y.getSubjectId())).orElse(new Course()).getOrderId())
								.orElse(Integer.MAX_VALUE));
			}
			return 1;
		});
	}

	@Override
	public List<CourseSchedule> findScheduleByClass(String unitId, String arrayId, String[] classIds) {
		List<NewGkTimetable> timetableList = this.findByArrayIdAndClassIds(unitId, arrayId, classIds);

		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", EntityUtils.getList(timetableList, "id").toArray(new String[0]));
		//获得老师集合
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findListByIn("timetableId", EntityUtils.getList(timetableList, "id").toArray(new String[0]));
		
		List<CourseSchedule> scheduleList = makeCourseSchedule(timetableList, timetableOtherList, timetableTeacherList);
		return scheduleList;
	}

	private List<CourseSchedule> makeCourseSchedule(List<NewGkTimetable> timetableList,
			List<NewGkTimetableOther> timetableOtherList, List<NewGkTimetableTeacher> timetableTeacherList) {
		Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(EntityUtils.getSet(timetableTeacherList, "teacherId").toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		//获得教室集合
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(EntityUtils.getSet(timetableOtherList, "placeId").toArray(new String[0])), new TR<List<TeachPlace>>() {});
		Map<String, String> placeNameMap = EntityUtils.getMap(placeList, "id", "placeName");

		//获得科目集合
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getSet(timetableList, "subjectId").toArray(new String[0])), new TR<List<Course>>() {});
		Map<String, String> courseMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);

		Set<String> classIds = EntityUtils.getSet(timetableList, NewGkTimetable::getClassId);
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListBy(NewGkDivideClass.class, null, null,
				"id", classIds.toArray(new String[0]), new String[] { "id", "className","classType","oldDivideClassId" });
		
		Map<String, NewGkDivideClass> classIdMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		//课表-科目映射 和 课表-老师映射
		Map<String, NewGkTimetable> timetableMap = EntityUtils.getMap(timetableList, "id");
		Map<String, String> timetableTeacherMap = EntityUtils.getMap(timetableTeacherList, "timetableId", "teacherId");
		
		List<CourseSchedule> scheduleList = new ArrayList<>();
		CourseSchedule schedule = null;
		for (NewGkTimetableOther tto : timetableOtherList) {
			schedule = new CourseSchedule();
			
			NewGkTimetable tt = timetableMap.get(tto.getTimetableId());
			NewGkDivideClass clazz = classIdMap.get(tt.getClassId());
			schedule.setId(tto.getId());
			schedule.setRecordId(tto.getTimetableId());
			schedule.setClassId(tt.getClassId());
			schedule.setOldDivideClassId(clazz.getOldDivideClassId());
			schedule.setSubjectId(tt.getSubjectId());
			schedule.setSubjectType(tt.getSubjectType());
			schedule.setTeacherId(timetableTeacherMap.get(tto.getTimetableId()));
			schedule.setPlaceId(tto.getPlaceId());
			
			schedule.setSubjectName(courseMap.get(schedule.getSubjectId()));
			schedule.setTeacherName(teacherNameMap.get(schedule.getTeacherId()));
			schedule.setPlaceName(placeNameMap.get(schedule.getPlaceId()));
			schedule.setClassName(clazz.getClassName());
			schedule.setClassType(Integer.parseInt(clazz.getClassType()));
			
			schedule.setDayOfWeek(tto.getDayOfWeek());
			schedule.setPeriodInterval(tto.getPeriodInterval());
			schedule.setPeriod(tto.getPeriod());
			schedule.setWeekType(tto.getFirstsdWeek());
			
			scheduleList.add(schedule);
		}
		return scheduleList;
	}
	
	@Override
	public List<CourseSchedule> makeScheduleByTeacher(String arrayId,Set<String> techerIds){
		List<CourseSchedule> scheduleList = new ArrayList<>();
		
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTeacherIds(techerIds, arrayId);
		Set<String> timetableIds = EntityUtils.getSet(timetableTeacherList, NewGkTimetableTeacher::getTimetableId);
		
		List<NewGkTimetable> timetableList = this.findListByIds(timetableIds.toArray(new String[0]));
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", EntityUtils.getList(timetableList, "id").toArray(new String[0]));
		
		scheduleList = makeCourseSchedule(timetableList, timetableOtherList, timetableTeacherList);
		
		return scheduleList;
	}
	
	@Override
	public Map<String,List<String>> findClassMovePeriodMap(String arrayId, Map<String, Set<String>> teacherMoveTimeMap, Map<String, Set<String>> placeMoveTimeMap){
		NewGkArray array = newGkArrayService.findOne(arrayId);
		
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(),
				array.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_1, NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3 },
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		List<NewGkDivideClass> noMoveZhbs = newGkDivideClassService.findNoMoveZhbs(array.getUnitId(), arrayId, NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		Set<String> noMoveIds = EntityUtils.getSet(noMoveZhbs, e -> e.getId());
		Map<String, NewGkDivideClass> classIdMap = EntityUtils.getMap(classList, NewGkDivideClass::getId);
		List<NewGkTimetable> jxbTimetableList = this.findByArrayIdAndClassType(array.getUnitId(), arrayId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3});
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(EntityUtils.getSet(jxbTimetableList, e->e.getId()).toArray(new String[0]));
		Map<String, String> ttTeacherIdMap = EntityUtils.getMap(timetableTeacherList, e->e.getTimetableId(),e->e.getTeacherId());
		this.makeTime(array.getUnitId(), arrayId, jxbTimetableList);
		Map<String,Set<String>> timeStuIdMap = new HashMap<>();
		for (NewGkTimetable tt : jxbTimetableList) {
			List<NewGkTimetableOther> timeList = tt.getTimeList();
			if(CollectionUtils.isEmpty(timeList))
				continue;
			List<String> studentIds = classIdMap.get(tt.getClassId()).getStudentList();
			Set<String> timestrs = new HashSet<>();
			for (NewGkTimetableOther tto : timeList) {
				String timestr = tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod();
				Set<String> stus = timeStuIdMap.get(timestr);
				if(stus == null) {
					stus = new HashSet<>();
					timeStuIdMap.put(timestr, stus);
				}
				stus.addAll(studentIds);
				timestrs.add(timestr);
				// place
				String placeId = tto.getPlaceId();
				if(StringUtils.isBlank(placeId)) {
					continue;
				}
				if(!NewGkElectiveConstant.CLASS_TYPE_3.equals(tt.getClassType()) || noMoveIds.contains(tt.getClassId())) {
					// TODO 对应的 行政班 只有 物理选 或者 历史 选的 class_type=3 的 班级时 才允许调课
					Set<String> set = placeMoveTimeMap.get(placeId);
					if(set == null) {
						set = new HashSet<>();
						placeMoveTimeMap.put(placeId, set);
					}
					set.add(timestr);
				}
			}
			// teacher
			// teacher
			String tid = ttTeacherIdMap.get(tt.getId());
			if(StringUtils.isBlank(tid)) {
				continue;
			}
			Set<String> set = teacherMoveTimeMap.get(tid);
			if(set == null) {
				set = new HashSet<>();
				teacherMoveTimeMap.put(tid, set);
			}
			set.addAll(timestrs);
		}
		// 行政班 排课 加入批次点时间 禁排
		Map<String,Set<String>> xzbMovePeriodMap = new HashMap<>();
		if(NewGkElectiveConstant.ARRANGE_XZB.equals(array.getArrangeType())){
			makeXzbMoveTimes(array, classList, xzbMovePeriodMap);
			if(xzbMovePeriodMap.size()>0){
				freshPlaceMap(placeMoveTimeMap, array, xzbMovePeriodMap);
				freshTeaMap(teacherMoveTimeMap, array, xzbMovePeriodMap);
			}
		}

		// 
		List<NewGkDivideClass> xzbList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());
		Map<String,List<String>> classMoveTimeMap = new HashMap<>();
		for (NewGkDivideClass dc : xzbList) {
			List<String> studentList = dc.getStudentList();
			List<String> timeList = new ArrayList<>();
			for (String timestr : timeStuIdMap.keySet()) {
				Set<String> moveClassStuIds = timeStuIdMap.get(timestr);
				
				boolean anyMatch = studentList.stream().anyMatch(e->moveClassStuIds.contains(e));
				if(anyMatch)
					timeList.add(timestr);
			}
			if(xzbMovePeriodMap.containsKey(dc.getOldDivideClassId())){
				Set<String> times = xzbMovePeriodMap.get(dc.getOldDivideClassId());
				timeList.addAll(times);
				timeList = timeList.stream().distinct().collect(Collectors.toList());
			}
			classMoveTimeMap.put(dc.getId(), timeList);
		}
		
		return classMoveTimeMap;
	}

	private void freshTeaMap(Map<String, Set<String>> teacherMoveTimeMap, NewGkArray array, Map<String, Set<String>> xzbMovePeriodMap) {
		List<NewGkTeacherPlan> tpList = newGkTeacherPlanService.findByArrayItemIds(new String[]{array.getLessonArrangeId()}, true);
		for (NewGkTeacherPlan tp : tpList) {
			if(CollectionUtils.isEmpty(tp.getTeacherPlanExList())){
				continue;
			}
			for (NewGkTeacherPlanEx tpe : tp.getTeacherPlanExList()) {
				if(StringUtils.isBlank(tpe.getClassIds())){
					continue;
				}
				String[] cidArr = tpe.getClassIds().split(",");
				for (String cid : cidArr) {
					Set<String> times = teacherMoveTimeMap.computeIfAbsent(tpe.getTeacherId(), e -> new HashSet<>());
					if(xzbMovePeriodMap.containsKey(cid)){
						Set<String> xzbTimes = xzbMovePeriodMap.get(cid);
						times.addAll(xzbTimes);
					}
				}
			}
		}
	}

	private void freshPlaceMap(Map<String, Set<String>> placeMoveTimeMap, NewGkArray array, Map<String, Set<String>> xzbMovePeriodMap) {
		List<NewGkPlaceItem> xzbPlaceList = newGkPlaceItemService.findByArrayItemIdAndTypeIn(array.getPlaceArrangeId(),
				new String[]{NewGkElectiveConstant.SCOURCE_PLACE_TYEP_1});
		Map<String, String> plaClsMap = EntityUtils.getMap(xzbPlaceList, NewGkPlaceItem::getPlaceId, e -> e.getObjectId());
		for (String pid : plaClsMap.keySet()) {
			String cid = plaClsMap.get(pid);
			Set<String> times = placeMoveTimeMap.computeIfAbsent(cid, e -> new HashSet<>());
			if(xzbMovePeriodMap.containsKey(cid)){
				Set<String> xzbTimes = xzbMovePeriodMap.get(cid);
				times.addAll(xzbTimes);
			}
		}
	}

	/**
	 * 行政班排课时，根据虚拟课程 构造 行政班对应的本班的 走班批次时间，哪怕没有走班学生
	 * @param array
	 * @param classList
	 * @param xzbMovePeriodMap
	 */
	private void makeXzbMoveTimes(NewGkArray array, List<NewGkDivideClass> classList, Map<String, Set<String>> xzbMovePeriodMap) {
		Map<String, Set<String>> xzbBatchMap = new HashMap<>();
		classList.stream().filter(e-> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
			.forEach(cls->{
				String clsIds = cls.getBatch().split("-")[1];
				String[] clsIdarr = clsIds.split(",");
				if(clsIdarr.length <=0){
					return;
				}
				String code = cls.getSubjectType() + cls.getBatch();
				for (String s : clsIdarr) {
					xzbBatchMap.computeIfAbsent(s, e->new HashSet<>()).add(code);
				}
			});
		if (xzbBatchMap.size() <= 0) {
			return;
		}
		List<NewGkLessonTime> batchTimeList = newGkLessonTimeService.findByObjectTypeAndItem(NewGkElectiveConstant.LIMIT_SUBJECT_7,
				new String[]{array.getLessonArrangeId()}, true);
		Map<String,Set<String>> batchPeriodMap = new HashMap<>();
		for (NewGkLessonTime lt : batchTimeList) {
			String code = "";
			if(NewGkElectiveConstant.DIVIDE_GROUP_5.equals(lt.getGroupType())) {
				code = NewGkElectiveConstant.SUBJECT_TYPE_A + lt.getLevelType();
			}else if(NewGkElectiveConstant.DIVIDE_GROUP_6.equals(lt.getGroupType())) {
				code = NewGkElectiveConstant.SUBJECT_TYPE_B + lt.getLevelType();
			}
			Set<String> times = lt.getTimesList().stream().map(e -> e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod()).collect(Collectors.toSet());
			batchPeriodMap.put(code,times);
		}
		for (String xzbId : xzbBatchMap.keySet()) {
			Set<String> collect = xzbBatchMap.get(xzbId).stream().filter(e -> batchPeriodMap.containsKey(e))
					.flatMap(e -> batchPeriodMap.get(e).stream()).collect(Collectors.toSet());
			xzbMovePeriodMap.put(xzbId,collect);
		}
	}

	@Override
	public void updateTimetablePlaces(String arrayId, List<NewGkPlaceItem> placeItemList, String[] classTypes) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat())) {
			// 只有未排课时才能改变场地
			return;
		}
		
		
		List<NewGkTimetable> timetableList = this.findByArrayId(array.getUnitId(), arrayId);
		List<String> types = Arrays.asList(classTypes);
		timetableList = timetableList.stream().filter(t->types.contains(t.getClassType())).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(timetableList))
			return;
		
		if(placeItemList == null) placeItemList = new ArrayList<>();
		Map<String, String> objPlaceMap = EntityUtils.getMap(placeItemList, NewGkPlaceItem::getObjectId, NewGkPlaceItem::getPlaceId);
		Map<String, NewGkTimetable> ttMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId);
		List<NewGkTimetableOther> ttoList = newGkTimetableOtherDao.findByUnitIdAndTimetableIdIn(array.getUnitId(), ttMap.keySet().toArray(new String[0]));
		List<NewGkDivideClass> divdieClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(),
				arrayId, classTypes, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		Map<String, String> toOldCidMap = EntityUtils.getMap(divdieClassList, NewGkDivideClass::getId, NewGkDivideClass::getOldDivideClassId);
		for (NewGkTimetableOther tto : ttoList) {
			String timetableId = tto.getTimetableId();
			String cid = ttMap.get(timetableId).getClassId();
			String oldCid = toOldCidMap.get(cid);
			
			String placeId = objPlaceMap.get(oldCid);
			tto.setPlaceId(placeId);
		}
		
		newGkTimetableOtherService.saveAll(ttoList.toArray(new NewGkTimetableOther[0]));
	}
	
	@Override
	public void updateTimetableTeachers(String arrayId,String subjectId, List<NewGkTeacherPlanEx> teacherPlanExList) {
		if(StringUtils.isBlank(arrayId) || StringUtils.isBlank(subjectId)) {
			LOG.error("arrayId 和 subjectId不能为空");
			return;
		}
		
		NewGkArray array = newGkArrayService.findOne(arrayId);
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat())) {
			// 只有未排课时才能改变场地
			return;
		}
		
		
		List<NewGkTimetable> timetableList = findByArrayIdAndSubjectId(array.getUnitId(), arrayId, new String[] {subjectId});
		Map<String, String> ttClassMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId, NewGkTimetable::getClassId);
		Map<String, String> classIdMap = EntityUtils.getMap(timetableList, NewGkTimetable::getClassId, NewGkTimetable::getId);
		List<NewGkTimetableTeacher> tttList = newGkTimetableTeacherService.findByTimetableIds(ttClassMap.keySet().toArray(new String[0]));
		List<NewGkDivideClass> divdieClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(),
				arrayId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		Map<String, String> toOldCidMap = EntityUtils.getMap(divdieClassList, NewGkDivideClass::getId, NewGkDivideClass::getOldDivideClassId);
		Map<String, String> toNewCidMap = EntityUtils.getMap(divdieClassList, NewGkDivideClass::getOldDivideClassId, NewGkDivideClass::getId);
		Map<String,String> newClassTeacherMap = new HashMap<>();
		for (NewGkTeacherPlanEx tpe : teacherPlanExList) {
			String teacherId = tpe.getTeacherId();
			String classIds = tpe.getClassIds();
			if(StringUtils.isNotBlank(classIds)) {
				String[] classIdArr = classIds.split(",");
				for (String cid : classIdArr) {
					newClassTeacherMap.put(cid, teacherId);
				}
			}
		}
		Map<String,NewGkTimetableTeacher> existedClassTeacherMap = new HashMap<>();
		for (NewGkTimetableTeacher ttt : tttList) {
			String classId = ttClassMap.get(ttt.getTimetableId());
			
			existedClassTeacherMap.put(toOldCidMap.get(classId), ttt);
		}
		List<NewGkTimetableTeacher> insertTeacherList = new ArrayList<>();
		Set<String> delttIds = new HashSet<>(ttClassMap.keySet());
		for (String classId : newClassTeacherMap.keySet()) {
			String newTeacherId = newClassTeacherMap.get(classId);
			
			NewGkTimetableTeacher ttt = existedClassTeacherMap.get(classId);
			if(ttt != null && StringUtils.isNotBlank(newTeacherId)) {
				ttt.setTeacherId(newTeacherId);
			}else {
				String newCid = toNewCidMap.get(classId);
				String timetableId = classIdMap.get(newCid);
				if(StringUtils.isBlank(timetableId)) {
					continue;
				}
				if(StringUtils.isNotBlank(newTeacherId)){
					ttt = new NewGkTimetableTeacher();
					ttt.setId(UuidUtils.generateUuid());
					ttt.setTeacherId(newTeacherId);
					ttt.setTimetableId(timetableId);
				}
			}
			insertTeacherList.add(ttt);
			delttIds.remove(ttt.getTimetableId());
		}
		
		newGkTimetableTeacherService.deleteByTimetableIdIn(delttIds.toArray(new String[0]));
		newGkTimetableTeacherService.saveAll(insertTeacherList.toArray(new NewGkTimetableTeacher[0]));
	}
	
	@Override
	public void updatePreTimetable(String arrayId, List<NewGkDivideClass> allClassList) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		NewGkDivide divide = newGkDivideService.findOne(array.getDivideId());
		if(divide == null) {
			throw new RuntimeException("找不到对应的 分班方案");
		}
		String openType = divide.getOpenType();
		
		// 未排课时才能更新课表
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat()) || newGkArrayService.checkIsArrayIng(arrayId)) {
			// 只有未排课时才能更新
			return;
		}
		
		if(allClassList == null) {
			allClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), 
					array.getId(), null, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		}
		
		List<NewGkLessonTime> allTimeList = newGkLessonTimeService.findByItemIdObjectId(
				array.getLessonArrangeId(), null, new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_7,NewGkElectiveConstant.LIMIT_GRADE_0 }, true);
		Map<String, NewGkSubjectTime> subjectPeriodMap = new HashMap<>();
		Map<String,List<NewGkLessonTimeEx>> timeMap = new HashMap<>();
		if(!NewGkElectiveConstant.DIVIDE_TYPE_10.equals(openType)
				&& !NewGkElectiveConstant.DIVIDE_TYPE_12.equals(openType)) {
			// 同时将走班课程 存入课程表
			List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemIdAndSubjectTypeIn(
					array.getLessonArrangeId(),
					new String[] { NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.SUBJECT_TYPE_B });
			subjectPeriodMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType(),Function.identity());
			List<NewGkLessonTime> lessonTimeList = allTimeList.stream().filter(e->NewGkElectiveConstant.LIMIT_SUBJECT_7.equals(e.getObjectType())).collect(Collectors.toList());
			// 批次时间点
			for (NewGkLessonTime lt : lessonTimeList) {
				String groupType = lt.getGroupType();
				String level = lt.getLevelType();
				String levelType = null;
				if("5".equals(groupType)) {
					levelType = NewGkElectiveConstant.SUBJECT_TYPE_A + level;
				}else if("6".equals(groupType)) {
					levelType = NewGkElectiveConstant.SUBJECT_TYPE_B + level;
				}
				List<NewGkLessonTimeEx> timesList = lt.getTimesList();
				if(CollectionUtils.isNotEmpty(timesList)) {
					timeMap.put(levelType, timesList);
				}
			}
		}
		// 固定排课课表
		String gradeId = array.getGradeId();
		List<NewGkLessonTime> xzbLessonTimeList = allTimeList.stream().filter(
				e -> !e.getObjectId().equals(gradeId) && NewGkElectiveConstant.LIMIT_GRADE_0.equals(e.getObjectType()))
				.collect(Collectors.toList());
		
		List<NewGkTimetableTeacher> tttList = new ArrayList<>();
		List<NewGkTimetable> ttList = new ArrayList<>();
		List<NewGkTimetableOther> ttoList = new ArrayList<>();
		makeResult(array, allClassList, subjectPeriodMap, timeMap, xzbLessonTimeList, tttList, ttList,
				ttoList);
		
		
		List<NewGkTimetable> oldlist=this.findByArrayId(array.getUnitId(), array.getId());
		NewGkArrayResultSaveDto dto=new NewGkArrayResultSaveDto();
		dto.setInsertOtherList(ttoList);
		dto.setInsertTimeTableList(ttList);
		dto.setInsertTeacherList(tttList);
		if(CollectionUtils.isNotEmpty(oldlist)){
			Set<String> set = EntityUtils.getSet(oldlist, NewGkTimetable::getId);
			dto.setTimeTableIds(set.toArray(new String[]{}));
		}
		dto.setArrayId(array.getId());
		this.saveAll(array.getUnitId(), array.getDivideId(), dto);
	}
	
	@Override
	public void updatePreTimetableByBatch(String arrayId) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		
		// 未排课时才能更新课表
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat()) || newGkArrayService.checkIsArrayIng(arrayId)) {
			// 只有未排课时才能更新
			return;
		}
		
		List<NewGkTimetable> tableList = this.findByArrayId(array.getUnitId(), arrayId);
		this.makeTime(array.getUnitId(), arrayId, tableList);
		
		List<NewGkTimetable> delTtList = tableList.stream().filter(t->NewGkElectiveConstant.CLASS_TYPE_2.equals(t.getClassType())).collect(Collectors.toList());
		
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemIdAndSubjectTypeIn(
				array.getLessonArrangeId(),
				new String[] { NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.SUBJECT_TYPE_B });
		Map<String, NewGkSubjectTime> subjectPeriodMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType(),Function.identity());
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(
				array.getLessonArrangeId(), null, new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_7}, true);
		// 批次时间点
		Map<String,List<NewGkLessonTimeEx>> timeMap = new HashMap<>();
		List<String> allTimeList = new ArrayList<>();
		for (NewGkLessonTime lt : lessonTimeList) {
			String groupType = lt.getGroupType();
			String level = lt.getLevelType();
			String levelType = null;
			if("5".equals(groupType)) {
				levelType = NewGkElectiveConstant.SUBJECT_TYPE_A + level;
			}else if("6".equals(groupType)) {
				levelType = NewGkElectiveConstant.SUBJECT_TYPE_B + level;
			}
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			if(CollectionUtils.isNotEmpty(timesList)) {
				timeMap.put(levelType, timesList);
				List<String> times = timesList.stream().map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()).collect(Collectors.toList());
				allTimeList.addAll(times);
			}
		}

		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), 
				array.getId(), null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		List<NewGkDivideClass> jxbList = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
		// 保存好的
		String gradeId = array.getGradeId();
		List<NewGkTimetableTeacher> tttList = new ArrayList<>();
		List<NewGkTimetable> ttList = new ArrayList<>();
		List<NewGkTimetableOther> ttoList = new ArrayList<>();
		makeResult(array, jxbList, subjectPeriodMap, timeMap, null, tttList, ttList,
				ttoList);
		
		// 判断冲突 删掉冲突的行政班课程
		tableList.removeAll(delTtList);
		Map<String, NewGkDivideClass> classIdMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getId);
		Map<String, NewGkTimetableOther> ttoMap = EntityUtils.getMap(ttoList, NewGkTimetableOther::getTimetableId);
		Map<String, NewGkTimetable> ttMap = EntityUtils.getMap(ttList, NewGkTimetable::getId);
		Map<String,List<NewGkDivideClass>> jxbTimeClassMap = new HashMap<>();
		makeTimeClassMap(ttoList, classIdMap, ttMap, jxbTimeClassMap,new HashMap<>());
		
		List<NewGkTimetableOther> xzbTtoList = tableList.stream().flatMap(e->e.getTimeList().stream()).collect(Collectors.toList());
		Map<String, NewGkTimetable> ttMap2 = EntityUtils.getMap(tableList, NewGkTimetable::getId);
		Map<String,List<NewGkTimetableOther>> timeOtherMap = new HashMap<>();
		makeTimeClassMap(xzbTtoList, classIdMap, ttMap2, new HashMap<>(),timeOtherMap);
		
		List<NewGkTimetableOther> delTtoList = new ArrayList<>();
		for (String timestr : jxbTimeClassMap.keySet()) {
			List<NewGkDivideClass> jxbListT = jxbTimeClassMap.get(timestr);
			Set<String> allStuIds = jxbListT.stream().flatMap(e->e.getStudentList().stream()).collect(Collectors.toSet());
			
			List<NewGkTimetableOther> list = timeOtherMap.get(timestr);
			if(CollectionUtils.isEmpty(list))
				continue;
			for (NewGkTimetableOther tto : list) {
				String classId = ttMap2.get(tto.getTimetableId()).getClassId();
				NewGkDivideClass dc = classIdMap.get(classId);
				List<String> studentList = dc.getStudentList();
				boolean anyMatch = studentList.stream().anyMatch(e->allStuIds.contains(e));
				if(anyMatch)
					delTtoList.add(tto);
			}
		}
		
		// 保存结果
		NewGkArrayResultSaveDto dto=new NewGkArrayResultSaveDto();
		dto.setInsertOtherList(ttoList);
		dto.setInsertTimeTableList(ttList);
		dto.setInsertTeacherList(tttList);
		if(CollectionUtils.isNotEmpty(delTtList)){
			Set<String> set = delTtList.stream().map(NewGkTimetable::getId).collect(Collectors.toSet());
			dto.setTimeTableIds(set.toArray(new String[]{}));
		}
		dto.setArrayId(array.getId());
		Set<String> set = EntityUtils.getSet(delTtoList, NewGkTimetableOther::getId);
		newGkTimetableOtherService.deleteByIdIn(set.toArray(new String[0]));
		this.saveAll(array.getUnitId(), array.getDivideId(), dto);
	}

	@Override
	public void deleteScheduleBySubIds(String arrayId, String[] subjctIdTypes){
		NewGkArray array = newGkArrayService.findOne(arrayId);
		if(subjctIdTypes == null || subjctIdTypes.length==0){
			return;
		}
		// 未排课时才能更新课表
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat()) || newGkArrayService.checkIsArrayIng(arrayId)) {
			// 只有未排课时才能更新
			return;
		}
		String unitId = array.getUnitId();
		String[] subjctIds = (String[]) Stream.of(subjctIdTypes).map(e -> e.split("-")[0]).toArray(e->new String[e]);
		List<NewGkTimetable> allTimetables = this.findByArrayIdAndSubjectId(unitId, arrayId, subjctIds);
		Set<String> codes = Stream.of(subjctIdTypes).collect(Collectors.toSet());

		List<NewGkTimetable> delTimetables = allTimetables.stream()
				.filter(e ->codes.contains(e.getSubjectId() + "-" + e.getSubjectType()))
				.collect(Collectors.toList());
		List<String> delTtid = EntityUtils.getList(delTimetables, e -> e.getId());

		deleteAll(delTimetables.toArray(new NewGkTimetable[0]));
		newGkTimetableOtherService.deleteByTimetableIdIn(delTtid.toArray(new String[0]));
		newGkTimetableTeacherService.deleteByTimetableIdIn(delTtid.toArray(new String[0]));
	}

	@Override
	public void updateByCouseFeature(String arrayId, List<NewGkSubjectTime> subjectTimeList) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		// 未排课时才能更新课表
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat()) || newGkArrayService.checkIsArrayIng(arrayId)) {
			// 只有未排课时才能更新
			return;
		}

		NewGkDivide divide = newGkDivideService.findOne(array.getDivideId());
		if(!NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				&& !NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())){
			// 非组合固定模式 无需更新课表
			return;
		}
		String unitId = array.getUnitId();
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, arrayId,
				new String[]{NewGkElectiveConstant.CLASS_TYPE_4, NewGkElectiveConstant.CLASS_TYPE_2},
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		List<NewGkDivideClass> oldDivideList = classList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
				&& StringUtils.isBlank(e.getOldDivideClassId())).collect(Collectors.toList());
		List<NewGkDivideClass> zhbList = classList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
				.collect(Collectors.toList());
		Set<String> oldSubIds = EntityUtils.getSet(oldDivideList, e -> e.getSubjectIds());

		//TODO
		List<String> newSubIds = subjectTimeList.stream().filter(e -> NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType())
				&& Objects.equals(NewGkElectiveConstant.IF_INT_1, e.getFollowZhb())).map(e -> e.getSubjectId()).collect(Collectors.toList());
		Set<String> delSubs = new HashSet<>();
		for (String oldSubId : oldSubIds) {
			if(!newSubIds.contains(oldSubId)){
				// 此科目 被从组合班 删除:需要删除 班级 课表
				delSubs.add(oldSubId);
			}else{
				newSubIds.remove(oldSubId);
			}
		}
		// 需要删除的 班级
		List<NewGkDivideClass> delClas = oldDivideList.stream().filter(e -> delSubs.contains(e.getSubjectIds())).collect(Collectors.toList());
		// 需要添加的 班级
		Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(newSubIds.toArray(new String[0])), new TypeReference<Map<String, String>>() {});
		List<NewGkDivideClass> insertClassList = new ArrayList<>();
		List<NewGkClassStudent> insertStuList = new ArrayList<>();
		for (String newSubId : newSubIds) {
			String subName = Optional.ofNullable(subNameMap.get(newSubId)).orElse("未知");
			List<NewGkDivideClass> copyList = EntityUtils.copyProperties(zhbList, NewGkDivideClass.class, NewGkDivideClass.class);
			copyList.forEach(e->{
				e.setRelateId(e.getId());
				e.setId(UuidUtils.generateUuid());
				e.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
				e.setSubjectIdsB(null);
				e.setSubjectIds(newSubId);
				e.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_O);
				e.setParentId(null);
				e.setBatch(null);
				e.setOldDivideClassId(null);
				e.setClassName(e.getClassName()+"-"+subName);
				if(CollectionUtils.isNotEmpty(e.getStudentList())){
					e.getStudentList().forEach(s->{
						NewGkClassStudent stu=new NewGkClassStudent();
						stu.setId(UuidUtils.generateUuid());
						stu.setUnitId(unitId);
						stu.setDivideId(arrayId);
						stu.setClassId(e.getId());
						stu.setStudentId(s);
						stu.setModifyTime(new Date());
						stu.setCreationTime(new Date());
						insertStuList.add(stu);
					});
					insertClassList.add(e);
				}
			});
		}
		// 需要删除的课表
		delSubs.addAll(newSubIds);
		if(CollectionUtils.isNotEmpty(delSubs)){
			List<NewGkTimetable> delTimetables = findByArrayIdAndSubjectId(unitId, arrayId, delSubs.toArray(new String[0]));
			Set<String> delTtid = EntityUtils.getSet(delTimetables, e -> e.getId());
			deleteAll(delTimetables.toArray(new NewGkTimetable[0]));
			newGkTimetableOtherService.deleteByTimetableIdIn(delTtid.toArray(new String[0]));
			newGkTimetableTeacherService.deleteByTimetableIdIn(delTtid.toArray(new String[0]));
		}

		if(CollectionUtils.isNotEmpty(delClas)){
			newGkDivideClassService.deleteAll(delClas.toArray(new NewGkDivideClass[0]));
		}
		if(CollectionUtils.isNotEmpty(insertClassList)){
			newGkDivideClassService.saveAll(insertClassList.toArray(new NewGkDivideClass[0]));
		}
		if(CollectionUtils.isNotEmpty(insertStuList)){
			newGkClassStudentService.saveAll(insertStuList.toArray(new NewGkClassStudent[0]));
		}

	}

	private void makeTimeClassMap(List<NewGkTimetableOther> ttoList, Map<String, NewGkDivideClass> classIdMap,
			Map<String, NewGkTimetable> ttMap, Map<String, List<NewGkDivideClass>> timeClassMap, Map<String,List<NewGkTimetableOther>> timeOtherMap) {
		for (NewGkTimetableOther tto : ttoList) {
			String classId = ttMap.get(tto.getTimetableId()).getClassId();
			NewGkDivideClass newGkDivideClass = classIdMap.get(classId);
			if(newGkDivideClass == null) {
				LOG.error("classId="+classId+" 找不到班级");
				continue;
			}
			String tc = tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod();
			if(!timeClassMap.containsKey(tc)) {
				timeClassMap.put(tc, new ArrayList<>());
				timeOtherMap.put(tc, new ArrayList<>());
			}
			timeClassMap.get(tc).add(newGkDivideClass);
			timeOtherMap.get(tc).add(tto);
		}
	}
	
	private void makeResult(NewGkArray array, List<NewGkDivideClass> allClassList,
			Map<String, NewGkSubjectTime> subjectPeriodMap,
			Map<String, List<NewGkLessonTimeEx>> timeMap, List<NewGkLessonTime> xzbLessonTimeList,
			List<NewGkTimetableTeacher> tttList, List<NewGkTimetable> ttList, List<NewGkTimetableOther> ttoList) {
		allClassList = allClassList.stream().filter(e->!(NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&StringUtils.isBlank(e.getBatch()))).collect(Collectors.toList());
		// 场地
		List<NewGkPlaceItem> placeItemList = newGkPlaceItemService
				.findByArrayItemId(array.getPlaceArrangeId());
		Map<String, String> classPlaceMap = EntityUtils.getMap(placeItemList, NewGkPlaceItem::getObjectId,NewGkPlaceItem::getPlaceId);
		
		// 时间进行排序
		if(timeMap == null){ timeMap = new HashMap<>();}
		timeMap.values().forEach(e->e.sort((x,y)->x.getDayOfWeek()-y.getDayOfWeek()));

		// 教师
		Map<String,String> classTeacherMap = new HashMap<>();
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(new String[] {array.getLessonArrangeId()}, true);
		for (NewGkTeacherPlan tp : teacherPlanList) {
			String subjectId = tp.getSubjectId();
			List<NewGkTeacherPlanEx> teacherPlanExList = tp.getTeacherPlanExList();
			if(CollectionUtils.isNotEmpty(teacherPlanExList)) {
				for (NewGkTeacherPlanEx tpe : teacherPlanExList) {
					String teacherId = tpe.getTeacherId();
					String classIds = tpe.getClassIds();
					if(StringUtils.isBlank(classIds))
						continue;
					String[] split = classIds.split(",");
					for (String cid : split) {
						String code = cid+"-"+subjectId;
						classTeacherMap.put(code, teacherId);
					}
				}
			}
			
		}
		
		Date now = new Date();
		// 构建课表
		Map<String,Integer> periodProgressMap = new HashMap<>();  // 记录某个被拆分的班级 子班级占用了那些时间点
		Map<String,Set<NewGkLessonTimeEx>> splitTimeMap = new HashMap<>();  //
		Set<NewGkDivideClass> collect = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
					&&StringUtils.isNotBlank(e.getParentId()))
				.collect(Collectors.toSet());
		long count2 = collect.stream().filter(e->subjectPeriodMap.containsKey(e.getSubjectIds()+"-"+e.getSubjectType())).count();
		Set<String> parentIds = new HashSet<>();
		if(count2 > 0)
			parentIds = collect.stream().map(e->e.getParentId()).collect(Collectors.toSet());
		Map<String, String> toOldCid = EntityUtils.getMap(allClassList, NewGkDivideClass::getId, NewGkDivideClass::getOldDivideClassId);
		for (NewGkDivideClass divideClass : allClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_0.equals(divideClass.getClassType())
					|| parentIds.contains(divideClass.getId()))  // 组合班 和被拆分的 班级 不构建课表
				continue;
			
			String oldDivideClassId = divideClass.getOldDivideClassId();
			String subjectId = divideClass.getSubjectIds();
			String subjectType = divideClass.getSubjectType();
			String teacherId = classTeacherMap.get(oldDivideClassId+"-"+subjectId);
			String placeId = classPlaceMap.get(oldDivideClassId);
			if(StringUtils.isNotBlank(divideClass.getParentId())) {
				placeId = classPlaceMap.get(toOldCid.get(divideClass.getParentId()));
			}
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClass.getClassType())) {
				for (NewGkLessonTime lt : xzbLessonTimeList) {
					List<NewGkLessonTimeEx> timesList = lt.getTimesList();
					NewGkSubjectTime st = subjectPeriodMap.get(lt.getObjectId()+"-"+NewGkElectiveConstant.SUBJECT_TYPE_O);
					Integer period = timesList.size();
					if(st != null && st.getPeriod() > timesList.size()) {
						throw new RuntimeException("固定排课时间点不够");
					}else if(st != null) {
						period = st.getPeriod();
						if(Integer.valueOf(0).equals(st.getIsNeed())) {
							placeId = null;
						}
					}
					
					makeEntitys(array, now, tttList, ttList, ttoList, lt.getObjectId(), NewGkElectiveConstant.SUBJECT_TYPE_O,
							period, teacherId, placeId, timesList, divideClass);
                }
            }else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(divideClass.getClassType())) {
                NewGkSubjectTime st = subjectPeriodMap.get(subjectId+"-"+subjectType);
                if(st == null || st.getPeriod() < 1)
					continue;
				Integer period = st.getPeriod();
				if(Integer.valueOf(0).equals(st.getIsNeed())) {
					placeId = null;
				}
				List<NewGkLessonTimeEx> list = timeMap.get(subjectType+divideClass.getBatch());
				
				Integer count = 0;
				List<NewGkLessonTimeEx> theTimes = list;
				if(StringUtils.isNotBlank(divideClass.getParentId())) {
					count = periodProgressMap.get(divideClass.getParentId());
					if(count == null) {
						count = 0;
						periodProgressMap.put(divideClass.getParentId(), count);
					}
					periodProgressMap.put(divideClass.getParentId(), count+period);
					// 使拆分班级 课时分布 均匀些
					Set<NewGkLessonTimeEx> last = splitTimeMap.get(divideClass.getParentId());
					if(last != null){
						theTimes = list.stream().filter(e->!last.contains(e)).collect(Collectors.toList());
					}else{
						if(Math.abs(list.size()-2.0*period)<=1){
							theTimes = new ArrayList<>();
							int f = (period == (int)Math.ceil(list.size()/2.0)?0:1);
							for (int i=0;i<list.size();i++) {
								if(i%2 == f){
									theTimes.add(list.get(i));
								}
							}
						}else{
							List<NewGkLessonTimeEx> exList = new ArrayList<>(list);
							Collections.shuffle(exList);
							theTimes = exList.subList(count,count+period);
						}
						splitTimeMap.put(divideClass.getParentId(),new HashSet<>(theTimes));
					}
				}
				if(list == null || list.size() < (count+period)) {
//					period = list.size();
					throw new RuntimeException("批次时间点不足");
				}
				
				makeEntitys(array, now, tttList, ttList, ttoList, subjectId, subjectType,
						period, teacherId, placeId, theTimes, divideClass);
			}
		}
	}

	
	@Override
	public void updateTimetableByGradeFea(String arrayId, List<NewGkLessonTime> newGradeTimeList) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		String gradeId = array.getGradeId();
		Grade grade = gradeRemoteService.findOneObjectById(gradeId);
		
		Integer mmCount = grade.getMornPeriods();
		Integer amCount = grade.getAmLessonCount();
		Integer pmCount = grade.getPmLessonCount();
		Integer nightCount = grade.getNightLessonCount();
		
		Map<String,Integer> piMap = new HashMap<>();
		piMap.put(BaseConstants.PERIOD_INTERVAL_1, mmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_2, amCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_3, pmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_4, nightCount);
		
		new HashSet<>(piMap.keySet()).forEach(e->{
			if(piMap.get(e) == null || piMap.get(e)<1)
				piMap.remove(e);
		});
		// 未排课时才能更新课表
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat()) || newGkArrayService.checkIsArrayIng(arrayId)) {
			// 只有未排课时才能更新
			return;
		}
		
		
		// 
		List<NewGkLessonTime> oldFixedTimeList = newGkLessonTimeService.findByItemIdObjectId(array.getLessonArrangeId(),
				null, new String[] { NewGkElectiveConstant.LIMIT_GRADE_0 }, true);
		oldFixedTimeList = oldFixedTimeList.stream().filter(
				e -> !e.getObjectId().equals(gradeId) && NewGkElectiveConstant.LIMIT_GRADE_0.equals(e.getObjectType()))
				.collect(Collectors.toList());
		List<NewGkTimetable> tableList = this.findByArrayId(array.getUnitId(), arrayId);
		this.makeTime(array.getUnitId(), arrayId, tableList);
		
		Set<String> oldFixedSubjectIds = EntityUtils.getSet(oldFixedTimeList, NewGkLessonTime::getObjectId);
		Set<NewGkTimetable> delTimetableList = tableList.stream().filter(e->oldFixedSubjectIds.contains(e.getSubjectId())).collect(Collectors.toSet());
		
		List<NewGkTimetableOther> delTtoList = new ArrayList<>();
		Set<String> newTimeSet = newGradeTimeList.stream().flatMap(e->e.getTimesList().stream()).map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()).collect(Collectors.toSet());
		for (NewGkTimetable tt : tableList) {
			List<NewGkTimetableOther> timeList = tt.getTimeList();
			if(delTimetableList.contains(tt)) {
				continue;
			}
			if(CollectionUtils.isEmpty(timeList)) {
				delTimetableList.add(tt);
				continue;
			}
				
			List<NewGkTimetableOther> list = timeList.stream().filter(
					e -> newTimeSet.contains(e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod())
							|| !piMap.containsKey(e.getPeriodInterval()))
					.collect(Collectors.toList());
			if(list.size() == timeList.size()) {
				delTimetableList.add(tt);
			}else {
				delTtoList.addAll(list);
			}
		}
		
		// 保存现有固定排课科目
		List<NewGkDivideClass> allXzbClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), 
				array.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		List<NewGkLessonTime> fixedLessonTimeList = newGradeTimeList.stream().filter(
				e -> !e.getObjectId().equals(gradeId) && NewGkElectiveConstant.LIMIT_GRADE_0.equals(e.getObjectType())).collect(Collectors.toList());
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemIdAndSubjectTypeIn(
				array.getLessonArrangeId(),
				new String[] { NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.SUBJECT_TYPE_B });
		Map<String, NewGkSubjectTime> subjectPeriodMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType(),Function.identity());
		
		List<NewGkTimetableTeacher> tttList = new ArrayList<>();
		List<NewGkTimetable> ttList = new ArrayList<>();
		List<NewGkTimetableOther> ttoList = new ArrayList<>();
		makeResult(array, allXzbClassList, subjectPeriodMap, null, fixedLessonTimeList, tttList, ttList,
				ttoList);
		
		// 保存
		NewGkArrayResultSaveDto dto=new NewGkArrayResultSaveDto();
		dto.setInsertOtherList(ttoList);
		dto.setInsertTimeTableList(ttList);
		dto.setInsertTeacherList(tttList);
		if(CollectionUtils.isNotEmpty(delTimetableList)){
			Set<String> set = delTimetableList.stream().map(NewGkTimetable::getId).collect(Collectors.toSet());
			dto.setTimeTableIds(set.toArray(new String[]{}));
		}
		dto.setArrayId(array.getId());
		Set<String> set = EntityUtils.getSet(delTtoList, NewGkTimetableOther::getId);
		newGkTimetableOtherService.deleteByIdIn(set.toArray(new String[0]));
		this.saveAll(array.getUnitId(), array.getDivideId(), dto);
		
	}
	
	private NewGkTimetable makeEntitys(NewGkArray array, Date now, List<NewGkTimetableTeacher> tttList,
			List<NewGkTimetable> ttList, List<NewGkTimetableOther> ttoList,
			String subjectId, String subjectType, Integer period, String teacherId, String placeId,
			List<NewGkLessonTimeEx> list, NewGkDivideClass newClass) {
		// timetable
		NewGkTimetable tt = new NewGkTimetable();
		tt.setId(UuidUtils.generateUuid());
		tt.setCreationTime(now);
		tt.setModifyTime(now);
		tt.setClassId(newClass.getId());
		tt.setClassType(newClass.getClassType());
		tt.setSubjectId(subjectId);
		tt.setSubjectType(subjectType);
		tt.setArrayId(array.getId());
		tt.setUnitId(array.getUnitId());
		ttList.add(tt);
		// timetableteacher 
		if(StringUtils.isNotBlank(teacherId)) {
			NewGkTimetableTeacher ttt = new NewGkTimetableTeacher();
			ttt.setId(UuidUtils.generateUuid());
			ttt.setTeacherId(teacherId);
			ttt.setTimetableId(tt.getId());
			tttList.add(ttt);
		}
		
		for(int i=0;i<period;i++) {
			NewGkLessonTimeEx lte = list.get(i);
			
			NewGkTimetableOther tto = new NewGkTimetableOther();
			tto.setId(UuidUtils.generateUuid());
			tto.setTimetableId(tt.getId());
			tto.setDayOfWeek(lte.getDayOfWeek());
			tto.setPeriodInterval(lte.getPeriodInterval());
			tto.setPeriod(lte.getPeriod());
			tto.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
			tto.setPlaceId(placeId);
			tto.setUnitId(array.getUnitId());
			ttoList.add(tto);
		}
		return tt;
	}
	
	@Override
	public void updateTimetableAllTeachers(String arrayId, List<String> subjectIdList) {

		if(StringUtils.isBlank(arrayId)) {
			LOG.error("arrayId 和不能为空");
			return;
		}
		
		NewGkArray array = newGkArrayService.findOne(arrayId);
		if(!NewGkElectiveConstant.IF_0.equals(array.getStat())) {
			// 只有未排课时才能改变课表中的教师安排
			return;
		}
		
		List<NewGkTeacherPlan> planList = null;
		List<NewGkTimetable> timetableList = null;
		if(CollectionUtils.isNotEmpty(subjectIdList)) {
			planList = newGkTeacherPlanService.findByArrayItemIdAndSubjectIdIn(
					array.getLessonArrangeId(), subjectIdList.toArray(new String[0]), true);
			timetableList = findByArrayIdAndSubjectId(array.getUnitId(), arrayId, subjectIdList.toArray(new String[0]));
		}else {
			planList = newGkTeacherPlanService.findByArrayItemIds(new String[]{array.getLessonArrangeId()} , true);
			timetableList = findByArrayId(array.getUnitId(), arrayId);
		}
		
		Set<String> ttIds = EntityUtils.getSet(timetableList, NewGkTimetable::getId);
		List<NewGkTimetableTeacher> tttList = newGkTimetableTeacherService.findByTimetableIds(ttIds.toArray(new String[0]));
		List<NewGkDivideClass> divdieClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(),
				arrayId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, false,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		divdieClassList = divdieClassList.stream()
				.filter(e->!(NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&& StringUtils.isBlank(e.getOldDivideClassId()))).collect(Collectors.toList());
		Map<String, String> toOldCidMap = EntityUtils.getMap(divdieClassList, NewGkDivideClass::getId,NewGkDivideClass::getOldDivideClassId);
		Map<String, String> toNewCidMap = EntityUtils.getMap(divdieClassList, NewGkDivideClass::getOldDivideClassId,NewGkDivideClass::getId);
		
		Map<String, NewGkTimetable> ttIdMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId, Function.identity());
		Map<String, NewGkTimetableTeacher> existedTeacherMap = new HashMap<>();
		for (NewGkTimetableTeacher ttt : tttList) {
			NewGkTimetable timetable = ttIdMap.get(ttt.getTimetableId());
			String subjectId = timetable.getSubjectId();
			String classId = timetable.getClassId();
			
			existedTeacherMap.put(subjectId+"-"+classId, ttt);
		}
		//
		Map<String, String> ttCodeMap = EntityUtils.getMap(timetableList, e->e.getSubjectId()+"-"+e.getClassId(),e->e.getId());
		List<NewGkTimetableTeacher> saveTttList = new ArrayList<>();
		List<String> delttIds = new ArrayList<>(ttIds);
		for (NewGkTeacherPlan tp : planList) {
			String subjectId = tp.getSubjectId();
			List<NewGkTeacherPlanEx> tpeList = tp.getTeacherPlanExList();
			if(CollectionUtils.isEmpty(tpeList))
				continue;
			
			for (NewGkTeacherPlanEx tpe : tpeList) {
				String teacherId = tpe.getTeacherId();
				String classIds = tpe.getClassIds();
				if(StringUtils.isNotBlank(classIds)) {
					String[] classArr = classIds.split(",");
					for (String cid : classArr) {
						String newCid = toNewCidMap.get(cid);
						String code = subjectId+"-"+newCid;
						NewGkTimetableTeacher ttt = existedTeacherMap.get(code);
						if(ttt != null) {
							ttt.setTeacherId(teacherId);
						}else {
							ttt = new NewGkTimetableTeacher();
							ttt.setId(UuidUtils.generateUuid());
							String ttId = ttCodeMap.get(code);
							if(ttId == null) {
								continue;
							}
							ttt.setTimetableId(ttId);
							ttt.setTeacherId(teacherId);
						}
						delttIds.remove(ttt.getTimetableId());
						saveTttList.add(ttt);
					}
				}
				
			}
		}
		
		newGkTimetableTeacherService.deleteByTimetableIdIn(delttIds.toArray(new String[0]));
		newGkTimetableTeacherService.saveAll(saveTttList.toArray(new NewGkTimetableTeacher[0]));
		
		String msg = newGkArrayService.checkAllTeacherConflict(arrayId);
		if(StringUtils.isNotBlank(msg))
			throw new RuntimeException(msg);
	}

	@Override
	public void saveCopyArray(NewGkArrayResultSaveDto dto) {
		NewGkArray array = dto.getNewGkArray();
		String stat = array.getStat();
		this.saveAll(array.getUnitId(), array.getDivideId(), dto);
		if(!NewGkElectiveConstant.IF_0.equals(stat) && !NewGkElectiveConstant.IF_OTHER_2.equals(stat)){
			// 1.排课完成 3.解决完整冲突 4.教师分堆完成 时对新的排课方案进行分堆操作
			newGkArrayService.autoArraySameClass(array.getUnitId(), array.getId());
		}
		newGkArrayService.updateStatById(stat, array.getId());
	}

	@Override
	public CourseScheduleModifyDto findTeacherScheduleInfo(String arrayId, String objId) {
		NewGkArray array = newGkArrayService.findOne(arrayId);
		NewGkDivide divide = newGkDivideService.findOne(array.getDivideId());
		String lessonArrangeId = array.getLessonArrangeId();
		String unitId = array.getUnitId();
		String openType = divide.getOpenType();
		
		// 获取 班级课程 信息 以及 班级应上课程信息
		List<CourseSchedule> scheduleList = this.makeScheduleByTeacher(arrayId, Arrays.stream(new String[] {objId}).collect(Collectors.toSet()));
		
		// 底部课程信息
		List<NewGkTeacherPlanEx> teacherPlanExList = newGkTeacherPlanExService.findByTeacherId(lessonArrangeId, new String[] {objId});
		Map<String,List<NewGkTeacherPlanEx>> tpIdMap = EntityUtils.getListMap(teacherPlanExList, NewGkTeacherPlanEx::getTeacherPlanId,Function.identity());
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findListByIds(tpIdMap.keySet().toArray(new String[0]));
		Set<String> subjectIds = EntityUtils.getSet(teacherPlanList, NewGkTeacherPlan::getSubjectId);
		Set<String> classIds = EntityUtils.getSet(teacherPlanExList, NewGkTeacherPlanEx::getClassIds).stream()
				.filter(e -> StringUtils.isNotBlank(e)).flatMap(e -> Stream.of(e.split(",")))
				.collect(Collectors.toSet());
		
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemId(lessonArrangeId);
		List<NewGkClassSubjectTime> classSubjTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId, 
				lessonArrangeId,classIds.toArray(new String[0]),subjectIds.toArray(new String[0]), null);
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, arrayId,
				null, false,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		Set<String> objIds = new HashSet<>(subjectIds);
		objIds.addAll(EntityUtils.getList(classSubjTimeList, NewGkClassSubjectTime::getId));
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(lessonArrangeId,
				objIds.toArray(new String[0]),
				new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_9, NewGkElectiveConstant.LIMIT_SUBJECT_5 }, true);
		Function<NewGkLessonTime,String> fun = (NewGkLessonTime e) -> {
			if (CollectionUtils.isNotEmpty(e.getTimesList())) {
				String times = e.getTimesList().stream()
						.filter(t->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(t.getTimeType()))
						.map(t -> t.getDayOfWeek() + "_" + t.getPeriodInterval() + "_" + t.getPeriod())
						.collect(Collectors.joining(","));
				return times;
			} else {
				return "";
			}
		};
		Map<String, String> subjectNoTime = lessonTimeList.stream().filter(e -> NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(e.getObjectType()))
				.collect(Collectors.toMap(e -> e.getObjectId() + "-" + e.getLevelType(), fun));
		Map<String, String> clasSubjectNoTime = lessonTimeList.stream().filter(e -> NewGkElectiveConstant.LIMIT_SUBJECT_5.equals(e.getObjectType()))
				.collect(Collectors.toMap(e -> e.getObjectId(), fun));
		Set<String> xzbSubIds = subjectTimeList.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType())).map(e->e.getSubjectId()).collect(Collectors.toSet());
		
		
		Map<String, NewGkDivideClass> cidMap = EntityUtils.getMap(classList, NewGkDivideClass::getId);
		Map<String, NewGkDivideClass> oldCidMap = EntityUtils.getMap(classList, NewGkDivideClass::getOldDivideClassId);
		Map<String, NewGkClassSubjectTime> cstCodeMap = EntityUtils.getMap(classSubjTimeList, e->e.getClassId()+"-"+e.getSubjectId());
		Map<String, NewGkSubjectTime> stCodeMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		
		List<NewGkClassFeatureDto> classFeatureDtoList = new ArrayList<>();
		NewGkClassFeatureDto dto = null;
		NewGkSubjectTime subjectTime = null;
		NewGkClassSubjectTime csTime = null;
		String subjectType = null;
		for (NewGkTeacherPlan tp : teacherPlanList) {
			String subjectId = tp.getSubjectId();
			List<NewGkTeacherPlanEx> list = tpIdMap.get(tp.getId());
			for (NewGkTeacherPlanEx tpe : list) {
				if(StringUtils.isBlank(tpe.getClassIds()))
					continue;
				String[] classIdArr = tpe.getClassIds().split(",");
				for (String cid : classIdArr) {
					NewGkDivideClass clas = oldCidMap.get(cid);
					
					String cstCode = cid+"-"+subjectId;
					csTime = cstCodeMap.get(cstCode);
					if(csTime != null) {
						// 存在班级特征
						dto = new NewGkClassFeatureDto(subjectId, csTime.getSubjectType(), csTime.getPeriod(),csTime.getWeekType());
						dto.setNoArrangeTime(clasSubjectNoTime.get(csTime.getId()));
					}else {
						// 不存在班级特征 直接使用 课程特征数据
						if(NewGkElectiveConstant.CLASS_TYPE_2.equals(clas.getClassType())) {
							subjectType = clas.getSubjectType();
							continue;
						}else if(xzbSubIds.contains(subjectId)){
							subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
						} else if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(openType)){
							subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
						}else if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(openType)){
							subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
							if(StringUtils.isNotBlank(clas.getSubjectIds()) && clas.getSubjectIds().contains(subjectId))
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
						}else if(NewGkElectiveConstant.CLASS_TYPE_3.equals(clas.getClassType())){
							if(subjectId.equals(clas.getSubjectIds())) {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
							}else {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_B;
							}
							continue;
						}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(clas.getClassType())) { 
							if(clas.getSubjectIds().contains(subjectId)) {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
							}else {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_B;
							}
						}else{
							NewGkDivideClass relatedClass = cidMap.get(clas.getRelateId());
							String[] subIds = relatedClass.getSubjectIds().split(",");
							if(xzbSubIds.contains(subjectId)) {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
							}else if(Arrays.asList(subIds).contains(subjectId)) {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
							}else {
								subjectType = NewGkElectiveConstant.SUBJECT_TYPE_B;
							}
						}
						subjectTime = stCodeMap.get(subjectId+"-"+subjectType);
						dto = new NewGkClassFeatureDto(subjectId, subjectType, subjectTime.getPeriod(),NewGkElectiveConstant.WEEK_TYPE_NORMAL);
						dto.setNoArrangeTime(subjectNoTime.get(subjectId+"-"+subjectType));
					}
					dto.setClassId(clas.getId());
					classFeatureDtoList.add(dto);
					
					String code = dto.getSubjectId() + "-" + dto.getSubjectType();
					subjectTime = stCodeMap.get(code);
					if(dto.getCourseWorkDay()>0 && !Objects.equals(NewGkElectiveConstant.WEEK_TYPE_NORMAL, subjectTime.getFirstsdWeek())) {
						dto.setCourseWorkDay(dto.getCourseWorkDay()-1);
					}
				}
			}
		}
		
		
		makeCourseName(classFeatureDtoList);
		
		List<List<NewGkClassFeatureDto>> collect = classFeatureDtoList.stream().map(e->Arrays.asList(e)).collect(Collectors.toList());
		
		// 转换NewGkClassFeatureDto -> ClassFeatureDto
		List<List<ClassFeatureDto>> classSubjectList = transferClassFeatureDto(collect);
		
		// 结果
		CourseScheduleModifyDto modifyDto = new CourseScheduleModifyDto();
		modifyDto.setScheduleList(scheduleList);
		modifyDto.setClassSubjectList(classSubjectList);
		
		return modifyDto;
	}

	@Override
	public List<CourseSchedule> makeScheduleByPlace(String unitId, String arrayId,Set<String> placeIds){

		List<CourseSchedule> scheduleList = new ArrayList<>();
		List<String> timetableIds = this.findIdByPlaceIds(unitId,arrayId,placeIds.toArray(new String[0]));
		List<NewGkTimetable> timetableList = this.findListByIds(timetableIds.toArray(new String[0]));
		
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableIds.toArray(new String[0]));
		
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId",
				timetableIds.toArray(new String[0]));
		
		scheduleList = makeCourseSchedule(timetableList, timetableOtherList, timetableTeacherList);
		
		return scheduleList;
	}
	
	@Override
	public CourseScheduleModifyDto findPlaceScheduleInfo(String arrayId, String placeId) {
		
		NewGkArray array = newGkArrayService.findOne(arrayId);
		String unitId = array.getUnitId();
		List<CourseSchedule> scheduleList = makeScheduleByPlace(unitId, arrayId, Stream.of(placeId).collect(Collectors.toSet()));
		
		// 底部显示 班级栏
		String placeArrangeId = array.getPlaceArrangeId();
		String lessonArrangeId = array.getLessonArrangeId();
		List<NewGkPlaceItem> placeItemList = newGkPlaceItemService.findByArrayItemId(placeArrangeId);
		Set<String> classIds = placeItemList.stream().filter(e->placeId.equals(e.getPlaceId())).map(NewGkPlaceItem::getObjectId).collect(Collectors.toSet());
		
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, arrayId,
				null, false,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		
		//TODO  行政班 课程
		List<NewGkDivideClass> classList = allClassList.stream().filter(e->classIds.contains(e.getOldDivideClassId())).collect(Collectors.toList());
		List<String> xzbCids = classList.stream().filter(e->!NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.map(e->e.getOldDivideClassId()).collect(Collectors.toList());
		
		
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemId(lessonArrangeId);
		List<NewGkClassSubjectTime> classSubjTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId, 
				lessonArrangeId,xzbCids.toArray(new String[0]),null, null);
		Set<String> objIds = EntityUtils.getSet(subjectTimeList, NewGkSubjectTime::getSubjectId);
		objIds.addAll(EntityUtils.getList(classSubjTimeList, NewGkClassSubjectTime::getId));
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(lessonArrangeId,
				objIds.toArray(new String[0]),
				new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_9, NewGkElectiveConstant.LIMIT_SUBJECT_5 }, true);
		Function<NewGkLessonTime,String> fun = (NewGkLessonTime e) -> {
			if (CollectionUtils.isNotEmpty(e.getTimesList())) {
				String times = e.getTimesList().stream()
						.filter(t->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(t.getTimeType()))
						.map(t -> t.getDayOfWeek() + "_" + t.getPeriodInterval() + "_" + t.getPeriod())
						.collect(Collectors.joining(","));
				return times;
			} else {
				return "";
			}
		};
		Map<String, String> subjectNoTime = lessonTimeList.stream().filter(e -> NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(e.getObjectType()))
				.collect(Collectors.toMap(e -> e.getObjectId() + "-" + e.getLevelType(), fun));
		Map<String, String> clasSubjectNoTime = lessonTimeList.stream().filter(e -> NewGkElectiveConstant.LIMIT_SUBJECT_5.equals(e.getObjectType()))
				.collect(Collectors.toMap(e -> e.getObjectId(), fun));
		Set<String> xzbSubIds = subjectTimeList.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType())).map(e->e.getSubjectId()).collect(Collectors.toSet());
		
		
		Map<String, NewGkDivideClass> cidMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getId);
		Map<String, NewGkDivideClass> oldCidMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getOldDivideClassId);
		Map<String, NewGkClassSubjectTime> cstCodeMap = EntityUtils.getMap(classSubjTimeList, e->e.getClassId()+"-"+e.getSubjectId());
		Map<String, NewGkSubjectTime> stCodeMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		
		List<String> realXzbCids = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.map(e->e.getOldDivideClassId()).collect(Collectors.toList());
		Map<String, List<String[]>> xzbSubjectMap = newGkDivideClassService.findXzbSubjects(unitId, array.getDivideId(), lessonArrangeId,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, realXzbCids);
		List<String> fakeXzbCids = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
				.map(e->e.getOldDivideClassId()).collect(Collectors.toList());
		Map<String, List<String[]>> fakeXzbSubjectMap = newGkDivideClassService.findFakeXzbSubjects(unitId, array.getDivideId(), lessonArrangeId,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, fakeXzbCids);

		// 获取应该屏蔽的 classType=3 班级
		List<NewGkDivideClass> noMoveZhbs = newGkDivideClassService.findNoMoveZhbs(unitId, arrayId, NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		Set<String> noMoveIds = EntityUtils.getSet(noMoveZhbs, e -> e.getId());

		List<NewGkClassFeatureDto> classFeatureDtoList = new ArrayList<>();
		NewGkClassFeatureDto dto = null;
		NewGkSubjectTime subjectTime = null;
		NewGkClassSubjectTime csTime = null;
		String subjectType = null;
		String code = null;
		String noTime = null;
		
		for (NewGkDivideClass cls : classList) {
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(cls.getClassType())) {
				// 教学班
				dto = new NewGkClassFeatureDto();
				dto.setClassId(cls.getId());
				dto.setSubjectId(cls.getSubjectIds());
				dto.setSubjectType(cls.getSubjectType());
				code = dto.getSubjectId()+"-"+dto.getSubjectType();
				noTime = subjectNoTime.get(code);
				dto.setNoArrangeTime(noTime);
				subjectTime = stCodeMap.get(code);
				if(subjectTime == null) {
					LOG.error("课程特征找不到科目："+code);
					continue;
				}
				dto.setCourseWorkDay(subjectTime.getPeriod());
				
				//TODO 暂时屏蔽 教学班显示
//				classFeatureDtoList.add(dto);
			}else if(NewGkElectiveConstant.CLASS_TYPE_1.equals(cls.getClassType())) {
				// 行政班
				// TODO 考虑拆分班级的情况
				// TODO 获取在 行政班上课的 科目
				makeXzbDto(subjectNoTime, clasSubjectNoTime, cstCodeMap, stCodeMap, xzbSubjectMap, classFeatureDtoList,
						cls);
			}else if(NewGkElectiveConstant.CLASS_TYPE_3.equals(cls.getClassType()) && !noMoveIds.contains(cls.getId())) {
				// 合班 教学班 应该不予显示;TODO 现在显示出来 但是 对应的 行政班 只有 物理选 或者 历史选的 时候 才会显示 ，否则 不显示
				Map<String,List<String[]>> clsSubMap = new HashMap<>();
				clsSubMap.put(cls.getOldDivideClassId(), Arrays.asList(new String[] {cls.getSubjectIds(),NewGkElectiveConstant.SUBJECT_TYPE_A},
						new String[] {cls.getSubjectIdsB(),NewGkElectiveConstant.SUBJECT_TYPE_B}));
				makeXzbDto(subjectNoTime, clasSubjectNoTime, cstCodeMap, stCodeMap, clsSubMap, classFeatureDtoList,
						cls);
			}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(cls.getClassType())) {
				// 伪行政班
				makeXzbDto(subjectNoTime, clasSubjectNoTime, cstCodeMap, stCodeMap, fakeXzbSubjectMap, classFeatureDtoList,
						cls);
			}
		}
		
		makeCourseName(classFeatureDtoList);
		
		Map<String, NewGkClassFeatureDto> codeDtoMap = EntityUtils.getMap(classFeatureDtoList, e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType());
		List<List<NewGkClassFeatureDto>> list = new ArrayList<>();
		Set<NewGkClassFeatureDto> used = new HashSet<>();
		for(NewGkClassFeatureDto cfDto:classFeatureDtoList) {
			NewGkSubjectTime st = stCodeMap.get(cfDto.getSubjectId()+"-"+cfDto.getSubjectType());
			if(NewGkElectiveConstant.FIRSTSD_WEEK_3 != st.getFirstsdWeek() && st.getFirstsdWeekSubject() != null) {
				if(!used.contains(cfDto)) {
					NewGkClassFeatureDto dtoRela = codeDtoMap.get(cfDto.getClassId()+"-"+st.getFirstsdWeekSubject());
					used.add(dtoRela);
					if(dtoRela.getCourseWorkDay()<1 || cfDto.getCourseWorkDay() <1){
						continue;
					}
					if(Objects.equals(NewGkElectiveConstant.WEEK_TYPE_ODD,dtoRela.getWeekType())){
						list.add(Arrays.asList(dtoRela,cfDto));
					}else{
						list.add(Arrays.asList(cfDto,dtoRela));
					}
					if(cfDto.getCourseWorkDay() > 1) {
						cfDto.setCourseWorkDay(cfDto.getCourseWorkDay()-1);
						list.add(Arrays.asList(cfDto));
					}
					if(dtoRela.getCourseWorkDay() > 1) {
						dtoRela.setCourseWorkDay(dtoRela.getCourseWorkDay()-1);
						list.add(Arrays.asList(dtoRela));
					}
				}
			}else {
				if(cfDto.getCourseWorkDay()>0){
					list.add(Arrays.asList(cfDto));
				}
			}
		}
		
		
		// 转换NewGkClassFeatureDto -> ClassFeatureDto
		List<List<ClassFeatureDto>> classSubjectList = transferClassFeatureDto(list);
		
		// 结果
		CourseScheduleModifyDto modifyDto = new CourseScheduleModifyDto();
		modifyDto.setScheduleList(scheduleList);
		modifyDto.setClassSubjectList(classSubjectList);
		
		return modifyDto;
	}

	private void makeXzbDto(Map<String, String> subjectNoTime, Map<String, String> clasSubjectNoTime,
			Map<String, NewGkClassSubjectTime> cstCodeMap, Map<String, NewGkSubjectTime> stCodeMap,
			Map<String, List<String[]>> xzbSubjectMap, List<NewGkClassFeatureDto> classFeatureDtoList,
			NewGkDivideClass cls) {
		NewGkClassFeatureDto dto;
		NewGkSubjectTime subjectTime;
		String code;
		String noTime;
		List<String[]> subjectIdTypes = xzbSubjectMap.get(cls.getOldDivideClassId());
		for (String[] string : subjectIdTypes) {
			String subId = string[0];
			String subType = string[1];
			dto = new NewGkClassFeatureDto();
			dto.setClassId(cls.getId());
			dto.setSubjectId(subId);
			dto.setSubjectType(subType);
			dto.setWeekType(NewGkElectiveConstant.WEEK_TYPE_NORMAL);

			NewGkClassSubjectTime csT = cstCodeMap.get(cls.getOldDivideClassId()+"-"+subId);
			code = dto.getSubjectId()+"-"+dto.getSubjectType();
			subjectTime = stCodeMap.get(code);
			if(subjectTime == null) {
				LOG.error("课程特征找不到科目："+code);
				continue;
			}
			if(csT !=null) {
				noTime = clasSubjectNoTime.get(csT.getId());
				dto.setCourseWorkDay(csT.getPeriod());
				dto.setNoArrangeTime(noTime);
				dto.setWeekType(csT.getWeekType());
			}else {
				noTime = subjectNoTime.get(code);
				dto.setNoArrangeTime(noTime);
				dto.setCourseWorkDay(subjectTime.getPeriod());
			}
			
			
			classFeatureDtoList.add(dto);
		}
	}
	
	@Override
	public CourseScheduleModifyDto findPlaceScheduleInfoWithMaster(String arrayId, String placeId) {
		return findPlaceScheduleInfo(arrayId, placeId);
	}
	
	@Override
	public CourseScheduleModifyDto findTeacherScheduleInfoWithMaster(String arrayId, String objId) {
		return this.findTeacherScheduleInfo(arrayId, objId);
	}
	
    @Override
    public void deleteBySubjectIds(String... subjectIds) {
        newGkTimetableDao.deleteBySubjectIdIn(subjectIds);
    }

    @Override
    public void deleteByClassIds(String... classIds) {
        newGkTimetableDao.deleteByClassIdIn(classIds);
    }
    

   
    @Override
    public void getIndex(String arrayId,Map<String,String> classNameMap,Map<String,String> courseNameMap,Map<String,NewGkTimetable> timetableMap
    		,Map<String,List<NewGkTimetableOther>> lastPlaceMap,Map<String,List<NewGkTimetableOther>> lastStudentMap){//,Map<String,List<NewGkTimetableOther>> lastTeacheridtMap){
    	NewGkArray array = newGkArrayService.findOne(arrayId);
    	List<NewGkTimetable> timeList = this.findByArrayId(array.getUnitId(), arrayId);
    	if(CollectionUtils.isNotEmpty(timeList)){
    		Set<String> subjectIds=new HashSet<String>();
    		Set<String> classIds=new HashSet<String>();
    		Set<String> timeIds=new HashSet<String>();
    		// key-timeId   value-classId 
    		Map<String,String> timeMap=new HashMap<String,String>();
    		
    		for(NewGkTimetable time:timeList){
    			subjectIds.add(time.getSubjectId());
    			classIds.add(time.getClassId());
    			timeIds.add(time.getId());
				timeMap.put(time.getId(),time.getClassId());	
				if(timetableMap!=null) 
					timetableMap.put(time.getId(),time);	
    		}
    		if(classNameMap!=null && courseNameMap!=null){
    			classNameMap.putAll(newGkDivideClassService.findByArrayIdMap(arrayId));
    			//putAll(EntityUtils.getMap(newGkDivideClassService.findByIds(classIds.toArray(new String[0])),"id","className"));
    			courseNameMap.putAll(SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){}));
    		}
    		List<NewGkTimetableOther>  timeOtherList=newGkTimetableOtherService.findByArrayId(array.getUnitId(), arrayId);
    		//Map<String, NewGkTimetableTeacher>teachMap=newGkTimetableTeacherService.findByIdInMap(timeIds.toArray(new String [0]));
			getLastMap(lastPlaceMap, lastStudentMap, timeMap, classIds,timeOtherList,arrayId);//,lastTeacheridtMap,teachMap);
    	}
    }
    /**
     * 得到学生以及场地的包含冲突的map
     * @param lastPlaceMap
     * @param lastStudentMap
     * @param timeMap
     * @param classIds
     * @param timeOtherList
     */
    public void getLastMap(Map<String,List<NewGkTimetableOther>> lastPlaceMap,Map<String,List<NewGkTimetableOther>> lastStudentMap,
    		Map<String,String> timeMap,Set<String> classIds,List<NewGkTimetableOther>  timeOtherList,String arrayId){
    		//Map<String,List<NewGkTimetableOther>> lastTeacheridtMap,Map<String, NewGkTimetableTeacher>teachMap){
    	// key:classId value studentIds
    	String unitId = newGkArrayService.findOne(arrayId).getUnitId();
    	Map<String, List<String>> classIdOfStuIdsMap=newGkClassStudentService.findMapByClassIds(unitId,arrayId, classIds.toArray(new String[0]));
    	if(MapUtils.isEmpty(classIdOfStuIdsMap)){
    		// 当行政班排课时 可能没有学生 使用假学生ID 来表达班级内部的冲突
			Map<String, List<String>> finalClassIdOfStuIdsMap = new HashMap<>();
			AtomicInteger atoint = new AtomicInteger(0);
			classIds.forEach(cid-> finalClassIdOfStuIdsMap.put(cid,Arrays.asList("FAKE_STU_ID_"+atoint.getAndIncrement())));
			classIdOfStuIdsMap = finalClassIdOfStuIdsMap;
		}
    	//key:classId   value-otherList
		Map<String,List<NewGkTimetableOther>> classIdOfOtherMap=new HashMap<String,List<NewGkTimetableOther>>();
    	if(CollectionUtils.isNotEmpty(timeOtherList)){
			for(NewGkTimetableOther timeOther:timeOtherList){
				String classId=timeMap.get(timeOther.getTimetableId());
				if(!classIdOfOtherMap.containsKey(classId)){
					classIdOfOtherMap.put(classId, new ArrayList<NewGkTimetableOther>());
				}
				classIdOfOtherMap.get(classId).add(timeOther);
				if(StringUtils.isBlank(timeOther.getPlaceId())){
					continue;
				}
				String key=timeOther.getPlaceId()+","+timeOther.getDayOfWeek()+","+timeOther.getPeriodInterval()+","+timeOther.getPeriod();
				if(!lastPlaceMap.containsKey(key)){
					lastPlaceMap.put(key,new ArrayList<NewGkTimetableOther>());
				}
				lastPlaceMap.get(key).add(timeOther);
				//添加教师的冲突
//				NewGkTimetableTeacher tenet= teachMap.get(timeOther.getTimetableId());
//				if(tenet!=null){
//					String teakey=tenet.getTeacherId()+","+timeOther.getDayOfWeek()+","+timeOther.getPeriodInterval()+","+timeOther.getPeriod();
//					if(!lastTeacheridtMap.containsKey(teakey)){
//						lastTeacheridtMap.put(key,new ArrayList<NewGkTimetableOther>());
//					}
//					lastTeacheridtMap.get(key).add(timeOther);
//				}

			}
			//key- studentId+坐标点      value-timeids暂定
    		for(Entry<String,List<String>> entry:classIdOfStuIdsMap.entrySet()){
    			String classId=entry.getKey();
    			List<String> studentIds=entry.getValue();
    			List<NewGkTimetableOther> otherList = classIdOfOtherMap.get(classId);
    			if(CollectionUtils.isNotEmpty(otherList)){
    				for(String studentId:studentIds){
    					for(NewGkTimetableOther other:otherList){
    						String key=studentId+","+other.getDayOfWeek()+","+other.getPeriodInterval()+","+other.getPeriod();
    						if(!lastStudentMap.containsKey(key)){
    							lastStudentMap.put(key,new ArrayList<NewGkTimetableOther>());
    						}
    						lastStudentMap.get(key).add(other);
    					}
    				}
    			}
    		}
		}
    }
    
    public int getTree(List<TreeNodeDto> treeNodeDtoList,Map<String,List<NewGkTimetableOther>> lastStudentMap,Map<String,List<NewGkTimetableOther>> lastPlaceMap,
    		Map<String,NewGkTimetable> timetableMap,Map<String,String> courseNameMap,Map<String,String> classNameMap){//,Map<String,List<NewGkTimetableOther>> lastTeacheridtMap){
    	int conNumber=0;
    	TreeNodeDto treeNodeDto=null;
    	//key-subjectId    value-classids
    	Map<String,Set<String>>  treeSet=new HashMap<String,Set<String>>();
    	setTreeMap(lastStudentMap, timetableMap, treeSet);
    	setTreeMap(lastPlaceMap, timetableMap, treeSet);
    	//setTreeMap(lastTeacheridtMap, timetableMap, treeSet);
    	for(Entry<String,Set<String>> entry:treeSet.entrySet()){
    		String subjectId=entry.getKey();
    		Set<String> classIds=entry.getValue();
    		List<String> classIdList = new ArrayList<String>();
    		classIdList.addAll(classIds);
    		String regex="[^0-9]";
    		classIdList.sort(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					String n1 = classNameMap.get(o1).replaceAll(regex, "");
					String n2 = classNameMap.get(o2).replaceAll(regex, "");
					if(n1.length()!=n2.length()){
						if(n1.length()<n2.length()){
							n1="0"+n1;
						}else{
							n2="0"+n2;
						}
					}
					return classNameMap.get(o1).replaceAll("[0-9]", n1).compareTo(classNameMap.get(o2).replaceAll("[0-9]", n2));
				}
			});
    		treeNodeDto = new TreeNodeDto();
    		treeNodeDto.setpId("");
            treeNodeDto.setId(subjectId);
            treeNodeDto.setName(courseNameMap.get(subjectId));
            treeNodeDto.setTitle(courseNameMap.get(subjectId));
            treeNodeDto.setOpen(false);
            treeNodeDto.setType("course");
            treeNodeDto.setIsParent(true);
//    		array.add(JSON.toJSON(treeNodeDto));
            treeNodeDtoList.add(treeNodeDto);
    		TreeNodeDto inDto=null;
    		for(String classId:classIdList){
    			inDto = new TreeNodeDto();
    			inDto.setpId(subjectId);
    			inDto.setId(classId);
    			inDto.setName(classNameMap.get(classId));
    			inDto.setTitle(classNameMap.get(classId));
    			inDto.setOpen(false);
    			inDto.setType("class");
//    			array.add(JSON.toJSON(inDto));
    			treeNodeDtoList.add(inDto);
    			conNumber++;
    		}
    	}
    	return conNumber;
    }
    
    /**
     * 获取树结果
     * @param lastMap
     * @param timetableMap
     * @param treeSet
     */
	public void setTreeMap(Map<String,List<NewGkTimetableOther>> lastMap,Map<String,NewGkTimetable> timetableMap,Map<String,Set<String>>  treeSet){
		NewGkTimetable timetable=null;
		for(Entry<String,List<NewGkTimetableOther>> entry:lastMap.entrySet()){
    		List<NewGkTimetableOther> inList=entry.getValue();
    		if(inList.size()>1){
    			if(inList.size()==2 && inList.get(0).getFirstsdWeek()!=null && inList.get(1).getFirstsdWeek()!=null 
    					&& (inList.get(0).getFirstsdWeek()+inList.get(1).getFirstsdWeek()==NewGkElectiveConstant.FIRSTSD_WEEK_1+NewGkElectiveConstant.FIRSTSD_WEEK_2)){
    				continue;	
    			}
    			for(NewGkTimetableOther other:inList){
    				timetable=timetableMap.get(other.getTimetableId());
    				if(timetable!=null){
    					if(!treeSet.containsKey(timetable.getSubjectId())){
    						treeSet.put(timetable.getSubjectId(), new HashSet<String>());
    					}
    					treeSet.get(timetable.getSubjectId()).add(timetable.getClassId());
    				}
    			}
    		}
    	}
	}
    
	@Override
	public int getConflictNum(String arrayId, List<TreeNodeDto> dtoList) {
		Map<String,String> classNameMap=new HashMap<String,String>();
    	Map<String,String> courseNameMap=new HashMap<String, String>();
    	Map<String,NewGkTimetable> timetableMap=new HashMap<String,NewGkTimetable>();
    	//key- placeId+坐标点      value-timeids暂定
		Map<String,List<NewGkTimetableOther>> lastPlaceMap=new HashMap<String,List<NewGkTimetableOther>>();
		//key- studentId+坐标点      value-timeids暂定
		Map<String,List<NewGkTimetableOther>> lastStudentMap=new HashMap<String,List<NewGkTimetableOther>>();
		//key- teacherid+坐标点      value-timeids暂定
		//Map<String,List<NewGkTimetableOther>> lastTeacheridtMap=new HashMap<String,List<NewGkTimetableOther>>();
		
		getIndex(arrayId, classNameMap, courseNameMap, timetableMap, lastPlaceMap, lastStudentMap);//,lastTeacheridtMap);
		
		int conNumber = getTree(dtoList,lastStudentMap, lastPlaceMap,timetableMap,courseNameMap,classNameMap);//,lastTeacheridtMap);
		
		return conNumber;
	}

	@Override
	public int getConflictNumWithMaster(String arrayId, List<TreeNodeDto> dtoList) {
		return getConflictNum(arrayId, dtoList);
	}

	@Override
	public List<NewGkTimetableOther> findArrayResultBy(String unitId, String arrayId, Set<String> allClazzIds,Set<String> timetableIds,
			List<Course> courseList){
		List<NewGkTimetableOther> newGkTimetableOtherList = new ArrayList<NewGkTimetableOther>();
		
		Set<String> timetableIdSet = new HashSet<String>();
		List<NewGkTimetable> newGkTimetableList = null;
		if(CollectionUtils.isNotEmpty(timetableIds)) {
			newGkTimetableList = this.findByIds(timetableIds.toArray(new String[0]));
			allClazzIds = EntityUtils.getSet(newGkTimetableList, NewGkTimetable::getClassId);
		}else if(CollectionUtils.isNotEmpty(allClazzIds)) {
			newGkTimetableList = this.findByArrayIdAndClassIds(unitId, arrayId, allClazzIds.toArray(new String[0]));
		}else {
			return newGkTimetableOtherList;
		}
		
		Set<String> subjectIdSet = new HashSet<String>();
		for(NewGkTimetable timetable : newGkTimetableList){
			subjectIdSet.add(timetable.getSubjectId());
			timetableIdSet.add(timetable.getId());
		}
		List<Course> courseList2 = courseRemoteService.findListObjectByIds(subjectIdSet.toArray(new String[0]));
		if(courseList != null) {
			courseList.addAll(courseList2);
		}
		Map<String, String> subjectNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		//班级名称
		
        List<NewGkDivideClass> newGkDivideClassList2 = newGkDivideClassService.findListByIds(allClazzIds.toArray(new String[0]));
        Map<String, String> clsNameMap = EntityUtils.getMap(newGkDivideClassList2, NewGkDivideClass::getId, NewGkDivideClass::getClassName);
        
        Map<String, String> timetableClassNameMap = new HashMap<String, String>();
        Map<String, String> timetableSubjectNameMap = new HashMap<String, String>();
        for(NewGkTimetable timetable : newGkTimetableList){
        	timetableClassNameMap.put(timetable.getId(), clsNameMap.get(timetable.getClassId()));
        	timetableSubjectNameMap.put(timetable.getId(), subjectNameMap.get(timetable.getSubjectId()));
		}
		
		Map<String, String> placeNameMap = new HashMap<String, String>();
		Map<String, String> teacherNameMap = new HashMap<String, String>();
		Map<String, String> timetableTeacherIdMap = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(timetableIdSet)){
        	List<NewGkTimetableTeacher> newGkTimetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(timetableIdSet.toArray(new String[0]));
        	Set<String> teacherIdSet = new HashSet<String>();
        	for(NewGkTimetableTeacher item : newGkTimetableTeacherList){
        		timetableTeacherIdMap.put(item.getTimetableId(), item.getTeacherId());
        		teacherIdSet.add(item.getTeacherId());
        	}
        	if(CollectionUtils.isNotEmpty(teacherIdSet)){
        		teacherNameMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[teacherIdSet.size()])),new TypeReference<Map<String, String>>(){});
        	}
        	
        	NewGkArray array = newGkArrayService.findOne(arrayId);
        	newGkTimetableOtherList = newGkTimetableOtherService.findByArrayId(array.getUnitId(), arrayId);;
        	
        	//wyy.
        	List<NewGkTimetableOther> newGkTimetableOtherList2 = new ArrayList<NewGkTimetableOther>();
        	for (NewGkTimetableOther other : newGkTimetableOtherList) {
				if(timetableIdSet.contains(other.getTimetableId())){
					newGkTimetableOtherList2.add(other);
				}
			}
        	newGkTimetableOtherList = newGkTimetableOtherList2;
        	
            Set<String> placeIdSet = new HashSet<String>();
        	for(NewGkTimetableOther other : newGkTimetableOtherList){
        		placeIdSet.add(other.getPlaceId());
            }
        	if(CollectionUtils.isNotEmpty(placeIdSet)){
        		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIdSet.toArray(new String[0])), new TR<List<TeachPlace>>(){});
        	    for(TeachPlace place : placeList){
        	    	placeNameMap.put(place.getId(), place.getPlaceName());
        	    }
        	}
        	for(NewGkTimetableOther other : newGkTimetableOtherList){
        		other.setPlaceName(placeNameMap.get(other.getPlaceId()));
        		other.setSubjectName(timetableSubjectNameMap.get(other.getTimetableId()));
        		other.setClassName(timetableClassNameMap.get(other.getTimetableId()));
        		other.setTeacherName(teacherNameMap.get(timetableTeacherIdMap.get(other.getTimetableId())));
            }
        }
		return newGkTimetableOtherList;
	}
	@Override
	public List<String> findIdByPlaceIds(String unitId, String arrayId, String[] placeIds){
		if(placeIds == null || placeIds.length<=0) {
			return new ArrayList<>();
		}

		List<String> timetableIds = newGkTimetableDao.findIdByPlaceId(unitId,arrayId,placeIds);
		return timetableIds;
	}

	@Override
	public Map<String,List<CourseScheduleDto>> findConflictTeaIds(String unitId, String arrayId){
		List<CourseScheduleDto> dtoList =  newGkTimetableJdbcDao.findConflictTeaIds(unitId,arrayId);
		Map<String,List<CourseScheduleDto>> resultMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(dtoList)){

			String lessonArrangeId = "";
			// 合班关系
			NewGkArray array = newGkArrayService.findOne(arrayId);
			List<Set<String>> combineRelations = combineRelationService.getCombineRelation(unitId, array.getLessonArrangeId());
			// 判断 两个班级课程 是否为 合班班级的情况
			BiFunction<String,String,Boolean> predict = (x, y)-> combineRelations.stream().anyMatch(csId->csId.contains(x)&&csId.contains(y));
			Map<String,String> toOldCidMap =  newGkDivideClassService.findOldDivideClassIdMap(arrayId);
			Map<String, List<CourseScheduleDto>> dtoTimeMap = EntityUtils.getListMap(dtoList, dto -> dto.getTeacherId() +"-"+ dto.getDayOfWeek() + dto.getPeriodInterval() + dto.getPeriod(), Function.identity());
			// key:tid+timestr
			for (String key : dtoTimeMap.keySet()) {
				String tid = key.split("-")[0];
				List<CourseScheduleDto> dtos = dtoTimeMap.get(key);
				//TODO 未考虑合班的 情况，同时排课不用考虑
				if(dtos.size()<2){
					continue;
				}else {
					boolean hasConflict = false;
					for (int i = 0;!hasConflict&& i < dtos.size(); i++) {
						CourseScheduleDto dto1 = dtos.get(i);
						String csId = toOldCidMap.get(dto1.getClassId()) + "-" + dto1.getSubjectId() + "-" + dto1.getSubjectType();
						for (int j = i+1; !hasConflict &&j < dtos.size(); j++) {
							CourseScheduleDto dto2 = dtos.get(j);
							if(dto1.getClassId()!=null && dto1.getClassId().equals(dto2.getClassId())){
								hasConflict = true;
								break;
							}
							String csId2 = toOldCidMap.get(dto2.getClassId()) + "-" + dto2.getSubjectId() + "-" + dto2.getSubjectType();
							if(dto1.getWeekType()+dto2.getWeekType()!=NewGkElectiveConstant.WEEK_TYPE_NORMAL
								&& !predict.apply(csId,csId2)){
								hasConflict = true;
								break;
							}
						}
					}
					if(hasConflict){
						resultMap.computeIfAbsent(tid,k->new ArrayList<>()).addAll(dtos);
					}
				}
			}
		}
		return resultMap;
	}
}
