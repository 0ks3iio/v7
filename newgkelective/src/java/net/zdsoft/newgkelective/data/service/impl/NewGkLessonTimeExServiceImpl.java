package net.zdsoft.newgkelective.data.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkLessonTimeDao;
import net.zdsoft.newgkelective.data.dao.NewGkLessonTimeExDao;
import net.zdsoft.newgkelective.data.dto.LessonTimeDto;
import net.zdsoft.newgkelective.data.dto.LessonTimeDtoPack;
import net.zdsoft.newgkelective.data.dto.SubjectLessonTimeDto;
import net.zdsoft.newgkelective.data.dto.TimeInfDto;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;

@Service("newGkLessonTimeExService")
public class NewGkLessonTimeExServiceImpl extends BaseServiceImpl<NewGkLessonTimeEx, String> implements NewGkLessonTimeExService{

	@Autowired
	private NewGkLessonTimeExDao newGkLessonTimeExDao;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private NewGkLessonTimeDao NewGkLessonTimeDao;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkLessonTimeEx, String> getJpaDao() {
		return newGkLessonTimeExDao;
	}

	@Override
	protected Class<NewGkLessonTimeEx> getEntityClass() {
		return NewGkLessonTimeEx.class;
	}

	@Override
	public List<NewGkLessonTimeEx> findByObjectId(String[] scourceTypeIds,
			String[] scourceTypes) {
		if(ArrayUtils.isEmpty(scourceTypeIds)) {
			return new ArrayList<>();
		}
		Specification<NewGkLessonTimeEx> specification = new Specification<NewGkLessonTimeEx>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkLessonTimeEx> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				if (scourceTypeIds != null && scourceTypeIds.length > 0) {
//					ps.add(root.get("scourceTypeId").in(scourceTypeIds));
					queryIn("scourceTypeId",scourceTypeIds,root,ps,cb);
				}
				if (scourceTypes != null && scourceTypes.length > 0) {
					ps.add(root.get("scourceType").in((String[]) scourceTypes));
				}
				return cq.where(cb.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		};
		List<NewGkLessonTimeEx> exList = newGkLessonTimeExDao.findAll(specification);
		return exList;
	}

	@Override
    public List<NewGkLessonTimeEx> findByObjectIdWithMaster(String[] scourceTypeIds,
                                                  String[] scourceTypes) {
	    return findByObjectId(scourceTypeIds, scourceTypes);
    }

	@Override
	public void updateGradeLessonTimeEx(String scourceTypeId,String array_item_id,
			String scourceType, String timeType,
			List<NewGkLessonTimeEx> lessonTimeExs) {
		//1.在开始之前先检查总课表里的记录是否和科目安排课表里的记录冲突，如果是，删除科目课时安排的记录，以总课表为准。
		if(!(array_item_id==null)){
			deleteConTimeEx(array_item_id, scourceType, lessonTimeExs, null);
		}
		
		//删除newgkelective_lesson_time_ex中对应的记录
		List<NewGkLessonTimeEx> lessonTimeExsForDel = findByObjectId(new String[]{scourceTypeId}, new String[]{scourceType});
		Iterator<NewGkLessonTimeEx> iterator = lessonTimeExsForDel.iterator();
		//选出匹配timeType的集合
		if(!(timeType==null)){
			while(iterator.hasNext()){
				if(!timeType.equals(iterator.next().getTimeType())){
					iterator.remove();
				}
			}
		}
		
		deleteAll(lessonTimeExsForDel.toArray(new NewGkLessonTimeEx[] {}));
		
		//2.向newgkelective_lesson_time_ex表重新写数据
		saveAll(lessonTimeExs.toArray(new NewGkLessonTimeEx[0]));
	}
	
	@Override
	public void addSubjectLessonTime(
			SubjectLessonTimeDto subjectLessonTimeDto,String arrayItemId) {
		
		//1.根据array_item_id和subjectId向lessonTime添加一条信息
		Date date = new Date();
		String lessonTimeId  = UuidUtils.generateUuid();
		NewGkLessonTime newGkLessonTime = new NewGkLessonTime();
		newGkLessonTime.setId(lessonTimeId);
		newGkLessonTime.setArrayItemId(arrayItemId);
		newGkLessonTime.setIsJoin(1);
		newGkLessonTime.setObjectId(subjectLessonTimeDto.getSubjectId());
		newGkLessonTime.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_9);
		newGkLessonTime.setCreationTime(date);
		newGkLessonTime.setModifyTime(date);
		newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
		
		NewGkLessonTimeDao.save(newGkLessonTime);
		//2.根据lesson_time_id向lessonTimeEx添加多条数据
		
		if(!(subjectLessonTimeDto.getTimeInf()==null)){
			List<NewGkLessonTimeEx> newGkLessonTimeExs = assembleLessonTimeExFromSubjectDto(subjectLessonTimeDto.getTimeInf(),lessonTimeId,arrayItemId);
			
			saveAll(newGkLessonTimeExs.toArray(new NewGkLessonTimeEx[0]));
		}
	}
	
	@Override
	public List<NewGkLessonTimeEx> assembleLessonTimeExFromSubjectDto(
			List<TimeInfDto> timeInf, String lessonTimeId,String arrayItemId) {
		List<NewGkLessonTimeEx> newGkLessonTimeExs = new ArrayList<>();
		for (TimeInfDto timeInfDto : timeInf) {
			NewGkLessonTimeEx newGkLessonTimeEx = new NewGkLessonTimeEx();
			newGkLessonTimeEx.setId(UuidUtils.generateUuid());
			newGkLessonTimeEx.setDayOfWeek(timeInfDto.getDay_of_week());
			newGkLessonTimeEx.setPeriod(timeInfDto.getPeriod());
			newGkLessonTimeEx.setPeriodInterval(timeInfDto.getPeriod_interval());
			newGkLessonTimeEx.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);
			newGkLessonTimeEx.setScourceTypeId(lessonTimeId);
			newGkLessonTimeEx.setArrayItemId(arrayItemId);
			newGkLessonTimeEx.setTimeType(timeInfDto.getTime_type());
			Date date = new Date();
			newGkLessonTimeEx.setCreationTime(date);
			newGkLessonTimeEx.setModifyTime(date);
			
			newGkLessonTimeExs.add(newGkLessonTimeEx);
		}
		return newGkLessonTimeExs;
	}

	@Override
	public String addLessonTimeTable(LessonTimeDtoPack lessonTimeDtoPack,
			String divide_id) {
		
		//1.根据divide_id获取grade_id
		NewGkDivide oneNewGkDivide = newGkDivideService.findById(divide_id);
		Date date = new Date();
		String arrayItemId = lessonTimeDtoPack.getLessonArrayItemId();
		if (StringUtils.isEmpty(arrayItemId)) {
			// 2.获取当前课时安排已经有的方案数量
			String divide_type = NewGkElectiveConstant.ARRANGE_TYPE_04;
			List<NewGkArrayItem> arrayItemList = newGkArrayItemService.findByDivideId(divide_id,
					new String[] { divide_type });
			Integer times = 0;
			if (arrayItemList.size() > 0) {
				times = arrayItemList.get(0).getTimes();
			}
			//2.2向array_item 插入一条数据
			NewGkArrayItem newGkArrayItem = new NewGkArrayItem();
			++times;
			String semesterJson = semesterRemoteService.getCurrentSemester(2, oneNewGkDivide.getUnitId());
			Semester semester = SUtils.dc(semesterJson, Semester.class);
			NewGkDivide divide = newGkDivideService.findById(divide_id);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
			String itemName = semester.getAcadyear() + "学年" + grade.getGradeName() + "第" + semester.getSemester()
					+ "学期排课特征" + times;
			
			arrayItemId = UuidUtils.generateUuid();
			newGkArrayItem.setId(arrayItemId);
			newGkArrayItem.setDivideId(divide_id);
			newGkArrayItem.setDivideType(divide_type);
			newGkArrayItem.setItemName(itemName);
			newGkArrayItem.setTimes(times);
			newGkArrayItem.setCreationTime(date);
			newGkArrayItem.setModifyTime(date);
			newGkArrayItem.setIsDeleted(0);
			newGkArrayItemService.save(newGkArrayItem);
		}
		List<String> objIds = new ArrayList<String>();
		List<NewGkLessonTime> toSaves = new ArrayList<NewGkLessonTime>();
		Map<String, String> objScMap = new HashMap<String, String>();
		//3.直接向newgkelective_lesson_time添加一条记录
		Integer is_join = 0;
		// 这里有没有常量可以用?  0:年级
		String object_type = lessonTimeDtoPack.getObjType();
		if(StringUtils.isEmpty(object_type)) {
			object_type = NewGkElectiveConstant.LIMIT_GRADE_0;
		}
		NewGkLessonTime gradeGkLessonTime = new NewGkLessonTime();
		gradeGkLessonTime.setId(UuidUtils.generateUuid());
		gradeGkLessonTime.setObjectId(oneNewGkDivide.getGradeId());
		gradeGkLessonTime.setObjectType(object_type);
		gradeGkLessonTime.setIsJoin(is_join);
		gradeGkLessonTime.setArrayItemId(arrayItemId);
		gradeGkLessonTime.setCreationTime(date);
		gradeGkLessonTime.setModifyTime(date);
		gradeGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
		String objStr = gradeGkLessonTime.getObjectId();
		objScMap.put(objStr, gradeGkLessonTime.getId());
		toSaves.add(gradeGkLessonTime);
		objIds.add(objStr);
		if (lessonTimeDtoPack.isNeedSource()) {
			List<LessonTimeDto> sources = lessonTimeDtoPack.getSourceTimeDto();
			if(CollectionUtils.isNotEmpty(sources)) {
				NewGkLessonTime lt;
				for(LessonTimeDto dto : sources) {
					if(dto == null 
							|| StringUtils.isEmpty(dto.getObjId()) 
							|| objIds.contains(dto.getObjId())) {
						continue;
					}
					objIds.add(dto.getObjId());
					lt = new NewGkLessonTime();
					lt.setId(UuidUtils.generateUuid());
					String[] obstrs = dto.getObjId().split("-");
					lt.setObjectId(obstrs[0]);
					lt.setObjectType(object_type);
					lt.setIsJoin(1);
					lt.setArrayItemId(arrayItemId);
					lt.setCreationTime(date);
					lt.setModifyTime(date);
					lt.setGroupType(dto.getGroupType());
					// TODO
					if(obstrs.length > 1) {
						lt.setLevelType(obstrs[1]);
					}
					toSaves.add(lt);
					objScMap.put(dto.getObjId(), lt.getId());
					lt = null;
				}
			}
		}
		newGkLessonTimeService.saveAll(toSaves.toArray(new NewGkLessonTime[0]));
		
		//4向newgkelective_lesson_time_ex插入N条记录，creation_time要一致
		lessonTimeDtoPack.setObjType(object_type);
		List<NewGkLessonTimeEx> LessonTimeExs = assembleLessonTimeExFromDtoForGrade(arrayItemId, lessonTimeDtoPack, objScMap);
		if (LessonTimeExs.size() > 0) {
			newGkLessonTimeExDao.insertBatch(LessonTimeExs);
		}
		return arrayItemId;
	}
	
	/**
	 * @param objScMap <key=objectId+levelType+groupType>
	 */
	public List<NewGkLessonTimeEx> assembleLessonTimeExFromDtoForGrade(String arrayItemId, LessonTimeDtoPack lessonTimeDtoPack, Map<String, String> objScMap) {
		ArrayList<NewGkLessonTimeEx> lessonTimeExList = new ArrayList<NewGkLessonTimeEx>();
		if(CollectionUtils.isEmpty(lessonTimeDtoPack.getLessonTimeDto())) {
			return lessonTimeExList;
		}
		Date date = new Date();
		for (LessonTimeDto lessonTimeDto : lessonTimeDtoPack.getLessonTimeDto()) {
			if(lessonTimeDto == null 
					|| StringUtils.isEmpty(lessonTimeDto.getObjId())) {
				continue;
			}
			String key = lessonTimeDto.getObjId();
			 // 不入库的数据过滤掉，原来的逻辑 isjoin=1时 这个为空
			if(!objScMap.containsKey(key)) {
				continue;
			}
			// 年级记录is_join==0，年级语数英记录是grouptype!=1  lessonTimeOfGrade.ftl这个页面现在不用了
//			if(lessonTimeDto.getIs_join()==0 
//					|| (StringUtils.isNotEmpty(lessonTimeDto.getGroupType()) 
//							&& !NewGkElectiveConstant.GROUP_TYPE_1.equals(lessonTimeDto.getGroupType()))){
				String scourceTypeId = objScMap.get(key);
				//创建lessontimeex对象
				NewGkLessonTimeEx newGkLessonTimeEx = new NewGkLessonTimeEx();
				newGkLessonTimeEx.setId(UuidUtils.generateUuid());
				newGkLessonTimeEx.setDayOfWeek(lessonTimeDto.getWeekday());
				newGkLessonTimeEx.setPeriodInterval(lessonTimeDto.getPeriod_interval());
				newGkLessonTimeEx.setPeriod(lessonTimeDto.getPeriod());
				newGkLessonTimeEx.setTimeType(lessonTimeDto.getTimeType() == null?NewGkElectiveConstant.ARRANGE_TIME_TYPE_01:lessonTimeDto.getTimeType());
				newGkLessonTimeEx.setCreationTime(date);
				newGkLessonTimeEx.setModifyTime(date);
				newGkLessonTimeEx.setScourceTypeId(scourceTypeId);
				newGkLessonTimeEx.setArrayItemId(arrayItemId);
				newGkLessonTimeEx.setScourceType(StringUtils.isEmpty(lessonTimeDtoPack.getSourceType())?NewGkElectiveConstant.SCOURCE_LESSON_01:lessonTimeDtoPack.getSourceType());
					
				//插入列表
				lessonTimeExList.add(newGkLessonTimeEx);
//			}
		}
		return lessonTimeExList;
	}
	
	/**
	 * 从LessonTimeDtoPack中取出数据保存多条LessonTime_Ex记录
	 * @param arrayItemId TODO
	 * @param lessonTimeDtoPack 前端传过来的数据对象，
	 * @param date	保存时间
	 * @param lesson_time_id less_time_id,对应表newgkelective_lesson_time中的id
	 */
	private List<NewGkLessonTimeEx> assembleLessonTimeExFromDto(String arrayItemId, LessonTimeDtoPack lessonTimeDtoPack, Map<String, String> objScMap) {
		ArrayList<NewGkLessonTimeEx> lessonTimeExList = new ArrayList<NewGkLessonTimeEx>();
		if(CollectionUtils.isEmpty(lessonTimeDtoPack.getLessonTimeDto())) {
			return lessonTimeExList;
		}
		Date date = new Date();
		for (LessonTimeDto lessonTimeDto : lessonTimeDtoPack.getLessonTimeDto()) {
			if(lessonTimeDto == null 
					|| StringUtils.isEmpty(lessonTimeDto.getObjId()) // 不入库的数据过滤掉，原来的逻辑 isjoin=1时 这个为空
					|| !objScMap.containsKey(lessonTimeDto.getObjId())) {
				continue;
			}
			// 参加排课
			if(lessonTimeDto.getIs_join()==1){
				String scourceTypeId = objScMap.get(lessonTimeDto.getObjId());
				//创建lessontimeex对象
				NewGkLessonTimeEx newGkLessonTimeEx = new NewGkLessonTimeEx();
				newGkLessonTimeEx.setId(UuidUtils.generateUuid());
				newGkLessonTimeEx.setDayOfWeek(lessonTimeDto.getWeekday());
				newGkLessonTimeEx.setPeriodInterval(lessonTimeDto.getPeriod_interval());
				newGkLessonTimeEx.setPeriod(lessonTimeDto.getPeriod());
				newGkLessonTimeEx.setTimeType(lessonTimeDto.getTimeType() == null?NewGkElectiveConstant.ARRANGE_TIME_TYPE_01:lessonTimeDto.getTimeType());
				newGkLessonTimeEx.setCreationTime(date);
				newGkLessonTimeEx.setModifyTime(date);
				newGkLessonTimeEx.setScourceTypeId(scourceTypeId);
				newGkLessonTimeEx.setArrayItemId(arrayItemId);
				newGkLessonTimeEx.setScourceType(StringUtils.isEmpty(lessonTimeDtoPack.getSourceType())?NewGkElectiveConstant.SCOURCE_LESSON_01:lessonTimeDtoPack.getSourceType());
					
				//插入列表
				lessonTimeExList.add(newGkLessonTimeEx);
			}
		}
		return lessonTimeExList;
	}
	
	public void saveBasicGradeTime(String arrayId, String itemId, String objectType, LessonTimeDtoPack lessonTimeDtoPack) {
		Grade gr = SUtils.dc(gradeRemoteService.findOneById(lessonTimeDtoPack.getSelGradeId()), Grade.class);
		if(gr == null) {
			throw new RuntimeException("年级不存在！");
		}
		Grade gDto = lessonTimeDtoPack.getGradeDto();
		if (gDto != null) {
			//获取学年学期--当前学年学期
			String semesterJson = semesterRemoteService.getCurrentSemester(2, gr.getSchoolId());
			Semester semester = SUtils.dc(semesterJson, Semester.class);
			if(semester==null) {
				throw new RuntimeException("未取得学年学期！");
			}
			//获取当前
			int oldMm=semester.getMornPeriods()==null?0:semester.getMornPeriods();
			int oldam=semester.getAmPeriods()==null?0:semester.getAmPeriods();
			int oldpm=semester.getPmPeriods()==null?0:semester.getPmPeriods();
			int oldNi=semester.getNightPeriods()==null?0:semester.getNightPeriods();
			
			int newMm=gDto.getMornPeriods()==null?0:gDto.getMornPeriods();
			int newam=gDto.getAmLessonCount()==null?0:gDto.getAmLessonCount();
			int newpm=gDto.getPmLessonCount()==null?0:gDto.getPmLessonCount();
			int newNi=gDto.getNightLessonCount()==null?0:gDto.getNightLessonCount();
			if(newMm>oldMm) {
				if(oldMm==0) {
					throw new RuntimeException("根据学年学期设置要求，早自习不能安排节次！");
				}
				throw new RuntimeException("根据学年学期设置要求，早自习节次不能超过"+oldMm+"节！");
			}
			if(newam>oldam) {
				if(oldam==0) {
					throw new RuntimeException("根据学年学期设置要求，上午不能安排节次！");
				}
				throw new RuntimeException("根据学年学期设置要求，上午节次不能超过"+oldam+"节！");
			}
			if(newpm>oldpm) {
				if(oldpm==0) {
					throw new RuntimeException("根据学年学期设置要求，下午不能安排节次！");
				}
				throw new RuntimeException("根据学年学期设置要求，下午节次不能超过"+oldpm+"节！");
			}
			if(newNi>oldNi) {
				if(oldNi==0) {
					throw new RuntimeException("根据学年学期设置要求，晚自习不能安排节次！");
				}
				throw new RuntimeException("根据学年学期设置要求，晚自习节次不能超过"+oldNi+"节！");
			}
			gr.setModifyTime(new Date());
			gr.setAmLessonCount(gDto.getAmLessonCount());
			gr.setPmLessonCount(gDto.getPmLessonCount());
			gr.setNightLessonCount(gDto.getNightLessonCount());
			gr.setRecess(gDto.getRecess());
			gr.setMornPeriods(gDto.getMornPeriods());
			gradeRemoteService.save(gr);
		}
		lessonTimeDtoPack.setBasicSave(true);
		this.updateLessonTime(arrayId, itemId, objectType, lessonTimeDtoPack);
	}
	@Override
	public void updateLessonTime(String arrayId, String arrayItemId, String objectType, LessonTimeDtoPack lessonTimeDtoPack) {
        NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
        boolean isXzbArray = false;
        if(arrayItem != null){
            NewGkDivide divide = newGkDivideService.findOne(arrayItem.getDivideId());
            isXzbArray = NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType());
        }

        List<LessonTimeDto> sources = lessonTimeDtoPack.getSourceTimeDto();
		Set<String> objIds = new HashSet<>();
		if (CollectionUtils.isNotEmpty(sources)) {
			Iterator<LessonTimeDto> sit = sources.iterator();
			while (sit.hasNext()) {
				LessonTimeDto dto = sit.next();
				if (dto == null || StringUtils.isEmpty(dto.getObjId())) {
					sit.remove();
					continue;
				}
				String[] obstrs = dto.getObjId().split("-");
				String objId = obstrs[0];
				objIds.add(objId);
			} 
		}
		String groupType = null;
		if(NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(objectType)) {
			groupType = lessonTimeDtoPack.getGroupType();
		} 
		if(!NewGkElectiveConstant.LIMIT_TEACHER_2.equals(objectType)) {
			objIds.clear();
		}
		List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(arrayItemId, 
				objIds.toArray(new String[0]), new String[] {objectType}, groupType);
		
		Map<String, String> objScMap = new HashMap<String, String>();
		Set<String> exIds = new HashSet<String>();
//		Map<String, String> objTypeScMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(times)){
			for(NewGkLessonTime time : times) {
				String key;
				if (StringUtils.isNotEmpty(time.getLevelType())) {
					key = time.getObjectId() +"-"+ time.getLevelType() +"-"+ time.getGroupType();
				} else {
					key = time.getObjectId();
				}
				objScMap.put(key, time.getId());
				exIds.add(time.getId());
//				objTypeScMap.put(key, time.getId());
			}
		}
		List<NewGkLessonTime> toSaves = new ArrayList<NewGkLessonTime>();
		if(CollectionUtils.isNotEmpty(sources)) {
			NewGkLessonTime lt;
			for(LessonTimeDto dto : sources) {
				if(dto == null 
						|| StringUtils.isEmpty(dto.getObjId())) {
					continue;
				}
				if (StringUtils.isEmpty(dto.getGroupType())) {
					dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
				}
				String[] obstrs = dto.getObjId().split("-");
				String objId = obstrs[0];
				String ltype = "";
				if(obstrs.length > 1) {
                    ltype = obstrs[1];
                    if(isXzbArray){
                        ltype = obstrs[1]+"-"+obstrs[2];
                    }
				}
				if(objScMap.containsKey(dto.getObjId())) {
					continue;
				}
				lt = new NewGkLessonTime();
				lt.setId(UuidUtils.generateUuid());
				lt.setObjectId(objId);
				lt.setObjectType(objectType);
				lt.setIsJoin(dto.getIs_join());
				lt.setArrayItemId(arrayItemId);
				lt.setCreationTime(new Date());
				lt.setModifyTime(new Date());
				lt.setGroupType(dto.getGroupType());
				lt.setLevelType(ltype);
				toSaves.add(lt);
				objScMap.put(dto.getObjId(), lt.getId());
				lt = null;
			}
		}
		if(objScMap.size() == 0) {
			return;
		}
		

		lessonTimeDtoPack.setObjType(objectType);
		List<NewGkLessonTimeEx> exs;
		if (NewGkElectiveConstant.LIMIT_GRADE_0.equals(objectType)) {
			exs = assembleLessonTimeExFromDtoForGrade(arrayItemId, lessonTimeDtoPack, objScMap);
		} else {
			exs = assembleLessonTimeExFromDto(arrayItemId, lessonTimeDtoPack, objScMap);
		}
		
		// 非基础条件的年级设置
		if (!lessonTimeDtoPack.isBasicSave()) {
			NewGkArrayItem item = newGkArrayItemService.findOne(arrayItemId);
			NewGkDivide divide = newGkDivideService.findOne(item.getDivideId());
			// 文理语数外独立分班时的总课表 需要清除冲突记录
			boolean subDelCon = NewGkElectiveConstant.LIMIT_SUBJECT_7.equals(objectType)
					&& NewGkElectiveConstant.DIVIDE_TYPE_03.equals(divide.getOpenType());
			if (exs.size() > 0 && (NewGkElectiveConstant.LIMIT_GRADE_0.equals(objectType) || subDelCon)) {
				deleteConTimeEx(arrayItemId, NewGkElectiveConstant.SCOURCE_LESSON_01, exs, objectType);
			} 
		}
		
		//保存年级特征时 更新对应的预排课表,一定要在保存数据之前
		if(NewGkElectiveConstant.LIMIT_GRADE_0.equals(objectType) && StringUtils.isNotBlank(arrayId)) {
			List<NewGkLessonTime> gradeTimeList = new ArrayList<>();
			gradeTimeList.addAll(times);
			gradeTimeList.addAll(toSaves);
			gradeTimeList = gradeTimeList.stream().map(e->{
				NewGkLessonTime newGkLessonTime = new NewGkLessonTime();
				try {
					BeanUtils.copyProperties(newGkLessonTime, e);
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}
				return newGkLessonTime;
			}).collect(Collectors.toList());
			Map<String, NewGkLessonTime> map = EntityUtils.getMap(gradeTimeList, NewGkLessonTime::getId);
			for (NewGkLessonTimeEx lte : exs) {
				if(!map.containsKey(lte.getScourceTypeId()))
					continue;
				map.get(lte.getScourceTypeId()).getTimesList().add(lte);
			}
			
			newGkTimetableService.updateTimetableByGradeFea(arrayId, gradeTimeList);
		}
		
		if (toSaves.size() > 0) {
			newGkLessonTimeService.saveAll(toSaves.toArray(new NewGkLessonTime[0]));
		}
		
		// 删除原来的
		if (CollectionUtils.isNotEmpty(exIds)) {
			String stype = lessonTimeDtoPack.getSourceType();
			if(StringUtils.isEmpty(stype)) {
				stype = NewGkElectiveConstant.SCOURCE_LESSON_01;
			}
			List<NewGkLessonTimeEx> exs2 = findByObjectId(exIds.toArray(new String[0]), new String[] {stype});
			if(NewGkElectiveConstant.LIMIT_TEACHER_2.equals(objectType)) {
				exs2 = exs2.stream().filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType())).collect(Collectors.toList());
			}
			if (CollectionUtils.isNotEmpty(exs2)) {
				this.deleteAll(exs2.toArray(new NewGkLessonTimeEx[0]));
			}
		}
		
		if (exs.size() > 0) {
			saveBatch(exs);
		}
		//保存课表设置时 更新对应的预排课表,一定要在保存批次时间点之后
		if(NewGkElectiveConstant.LIMIT_SUBJECT_7.equals(objectType) && StringUtils.isNotBlank(arrayId)) {
			newGkTimetableService.updatePreTimetableByBatch(arrayId);
		}
	}
	
	/**
	 * 年级课表保存，清除课程表中冲突的记录
	 * @param itemId
	 * @param sourceType
	 * @param exs
	 * @param objectType 
	 */
	private void deleteConTimeEx(String itemId, String sourceType, List<NewGkLessonTimeEx> exs, String objectType) {
		String[] obtyps = new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_9 };
		if(NewGkElectiveConstant.LIMIT_GRADE_0.equals(objectType)) {
			obtyps = new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_7, NewGkElectiveConstant.LIMIT_SUBJECT_9 };
		}
		List<NewGkLessonTime> subjLessonTimeList = newGkLessonTimeService.findByItemIdObjectId(itemId, null,
				obtyps, false);
		if(CollectionUtils.isEmpty(subjLessonTimeList)) {
			return;
		}
		//根据lesstimeId查询科目课时安排的LessonTImeEx记录
		String[] subjLessonTimeIds = EntityUtils.getSet(subjLessonTimeList, NewGkLessonTime::getId).toArray(new String[0]);
		if(ArrayUtils.isEmpty(subjLessonTimeIds)) {
			return;
		}
		
		List<NewGkLessonTimeEx> subjLessonTimeExs = findByObjectId(subjLessonTimeIds, new String[] { sourceType });
		//判断是否冲突，是则删除
		for (NewGkLessonTimeEx subjLessonTimeEx : subjLessonTimeExs) {
			for (NewGkLessonTimeEx gradeLessonTimeEx : exs) {
				if (gradeLessonTimeEx.getDayOfWeek().equals(subjLessonTimeEx.getDayOfWeek())
						&& gradeLessonTimeEx.getPeriodInterval().equals(subjLessonTimeEx.getPeriodInterval())
						&& gradeLessonTimeEx.getPeriod().equals(subjLessonTimeEx.getPeriod())) {
					delete(subjLessonTimeEx);
					break;
				}
			}
		} 
	}

	@Override
	public void updateSubjectLessonTimes(String arrayItemId,
			SubjectLessonTimeDto subjectLessonTimeDto) {
		String objectType = NewGkElectiveConstant.LIMIT_SUBJECT_9;
		String[] strs = subjectLessonTimeDto.getSubjectId().split("-");
		String subId = strs[0];
		String lt = "";
		if (strs.length > 1) {
			lt = strs[1];
		}
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, 
					new String[]{subId}, new String[]{objectType}, false);
		
		String lessonTimeId = "";
		//如果用户没有插入任何科目课时安排数据，就手动插入一条
		if(lessonTimeList.size()>0){
			for(NewGkLessonTime time : lessonTimeList) {
				if(StringUtils.trimToEmpty(time.getLevelType()).equals(lt)) {
					lessonTimeId = time.getId();
				}
			}
		}
		if(StringUtils.isEmpty(lessonTimeId)){
			NewGkLessonTime newGkLessonTime = new NewGkLessonTime();
			lessonTimeId = UuidUtils.generateUuid();
			Date date = new Date();
			newGkLessonTime.setId(lessonTimeId);
			newGkLessonTime.setObjectId(subId);
			newGkLessonTime.setObjectType(objectType);
			newGkLessonTime.setIsJoin(1);
			newGkLessonTime.setArrayItemId(arrayItemId);
			newGkLessonTime.setCreationTime(date);
			newGkLessonTime.setModifyTime(date);
			// divideopentype=01.05 时，区分选考、学考保存 TODO
			if (strs.length > 1) {
				newGkLessonTime.setLevelType(strs[1]);
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(newGkLessonTime.getLevelType())) {
					newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_5);
				} else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(newGkLessonTime.getLevelType())) {
					newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_6);
				}
			}
			if(StringUtils.isEmpty(newGkLessonTime.getGroupType())) {
				newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
			}
			newGkLessonTimeService.save(newGkLessonTime);
		}
		
		List<NewGkLessonTimeEx> lessonTimeExs = new ArrayList<>();
		if(!(subjectLessonTimeDto.getTimeInf()==null)){
			lessonTimeExs = assembleLessonTimeExFromSubjectDto(subjectLessonTimeDto.getTimeInf(), lessonTimeId,arrayItemId);
		}
		
		updateGradeLessonTimeEx(lessonTimeId, null, NewGkElectiveConstant.SCOURCE_LESSON_01, null, lessonTimeExs);
		
	}

	@Override
	public void deleteByScourceTypeIdIn(String[] scourceTypeIds) {
		if(scourceTypeIds!=null && scourceTypeIds.length>0){
			if(scourceTypeIds.length<=1000){
				newGkLessonTimeExDao.deleteByScourceTypeIdIn(scourceTypeIds);
			}else{
				int cyc = scourceTypeIds.length / 1000 + (scourceTypeIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > scourceTypeIds.length)
						max = scourceTypeIds.length;
					String[] iids = ArrayUtils.subarray(scourceTypeIds, i * 1000, max);
					newGkLessonTimeExDao.deleteByScourceTypeIdIn(iids);
				}
			
			}
		}
		
	}

	@Override
	public void deleteByArrayItemId(String arrayItemId) {
		
		newGkLessonTimeExDao.deleteByArrayItemId(arrayItemId);
	}

	@Override
	public void saveBatch(List<NewGkLessonTimeEx> list) {
		if(CollectionUtils.isNotEmpty(list)) {
			newGkLessonTimeExDao.insertBatch(list);
		}
	}

	@Override
	public void deleteByItemIdAndType(String arrayItemId, String objectType, String timeType) {
		newGkLessonTimeExDao.deleteByItemIdAndType(arrayItemId, objectType, timeType);
	}
}
