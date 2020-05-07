package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkArrayItemDao;
import net.zdsoft.newgkelective.data.dto.ArrayFeaturesDto;
import net.zdsoft.newgkelective.data.dto.NewGkItemDto;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkRelateSubtimeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Service("newGkArrayItemService")
public class NewGkArrayItemServiceImpl extends BaseServiceImpl<NewGkArrayItem, String> implements NewGkArrayItemService{

	@Autowired
	private NewGkArrayItemDao newGkArrayItemDao;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkplaceArrangeService newGkplaceArrangeService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private NewGkTeacherPlanExService teacherPlanExService;
	@Autowired
	private NewGkChoRelationService relationService;
	@Autowired
	private NewGkPlaceItemService placeItemService;
	@Autowired
	private NewGkRelateSubtimeService relateSubtimeService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkArrayItem, String> getJpaDao() {
		return newGkArrayItemDao;
	}

	@Override
	protected Class<NewGkArrayItem> getEntityClass() {
		return NewGkArrayItem.class;
	}

	@Override
	public List<NewGkArrayItem> findByDivideId(String divideId,
			String[] divideType) {
		if(divideType==null || divideType.length<=0){
			return newGkArrayItemDao.findByDivideId(divideId);
		}else{
			return newGkArrayItemDao.findByDivideIdAndType(divideId,divideType);
		}
	}
	@Override
	public List<NewGkArrayItem> findByDivideIdWithMaster(String divideId, String[] divideType) {
		return findByDivideId(divideId, divideType);
	}
	
	public List<String> findIdsByDivideIdName(String divideId, String divideType, String arrayName){
		return newGkArrayItemDao.findIdsByDivideIdName(divideId, divideType, arrayName);
	}

	@Override
	public void deleteById(String unitId, String id) {
		// 年级特征 //课程特征 //教师特征 //课表设置 //场地 //班级特征
		newGkLessonTimeService.deleteByArrayItemId(unitId, id);
		
		relateSubtimeService.deletedByItemId(id);
		
		newGkTeacherPlanService.deleteByArrayItemId(id);
		
		newGkSubjectTimeService.deleteByArrayItemId(unitId, id);
		
		newGkplaceArrangeService.deleteByItemId(id);
		
		placeItemService.deleteByArrayItemId(id);
		
		newGkArrayItemDao.deleteId(new Date(),id);
		
		// 班级特征
		newGkClassSubjectTimeService.deleteByArrayItemId(id);
		
		newGkClassCombineRelationService.deleteByArrayItemId(id);
	} 

	@Override
	public void makeDtoData(String type,String gradeId, List<NewGkArrayItem> itemList) {
		if(CollectionUtils.isEmpty(itemList)){
			return;
		}

		if(NewGkElectiveConstant.ARRANGE_TYPE_01.equals(type)){
			//教室
			List<NewGkArrayItem> newGkArrayItemList = new ArrayList<NewGkArrayItem>();
			for(NewGkArrayItem item : itemList){
				if(NewGkElectiveConstant.ARRANGE_TYPE_01.equals(item.getDivideType())){
					newGkArrayItemList.add(item);
				}
			}
			if(CollectionUtils.isNotEmpty(newGkArrayItemList)){
				makePlaceItem(newGkArrayItemList);
			}
			
			
		}else if(NewGkElectiveConstant.ARRANGE_TYPE_02.equals(type)){
			//教师
			//makeTeacherItem(itemList);
			makeTeacherItem(itemList);
		}else if(NewGkElectiveConstant.ARRANGE_TYPE_03.equals(type)){
			//周课时
			makePeriodItem(itemList);
			
			
		}else if(NewGkElectiveConstant.ARRANGE_TYPE_04.equals(type)){
			//上课时间
//			makeLessonItem(gradeId,itemList);
			//排课特征方案
			makeLessonItem2(gradeId, itemList);
		}
		
	}
	
	private void makeLessonItem2(String gradeId,List<NewGkArrayItem> items){
		if(CollectionUtils.isNotEmpty(items)){
			
			//每天
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);	
			if(grade==null){
				return;
			}
			int oneDays=0;//总天数
			if(grade.getMornPeriods()!=null){
				oneDays=oneDays+grade.getMornPeriods();
			}
			if(grade.getAmLessonCount()!=null){
				oneDays=oneDays+grade.getAmLessonCount();
			}
			if(grade.getPmLessonCount()!=null){
				oneDays=oneDays+grade.getPmLessonCount();
			}
			if(grade.getNightLessonCount()!=null){
				oneDays=oneDays+grade.getNightLessonCount();
			}
			int weekDays=oneDays*(grade.getWeekDays()!=null?grade.getWeekDays():7);//一周总天数

			NewGkItemDto newGkItemDto;
			
			Map<String,Set<String>> noArrangeByEx=new HashMap<String,Set<String>>();//不排课对应的NewGkLessonTime的id
			Map<String,Set<String>> arrangeByEx=new HashMap<String,Set<String>>();//固定排课对应的NewGkLessonTime的id
			
			
			Map<String,Integer> numByObjectId=new HashMap<String,Integer>();//key:NewGkLessonTime的id
			
			Set<String> itemIds=EntityUtils.getSet(items, "id");
			List<NewGkLessonTime> limitByGradeList = newGkLessonTimeService.findByObjectTypeAndItem(NewGkElectiveConstant.LIMIT_GRADE_0, itemIds.toArray(new String[]{}), false);
			
			if(CollectionUtils.isNotEmpty(limitByGradeList)){
				Set<String> ids = new HashSet<String>();
				Set<String> noArrayAllIds = new HashSet<String>();
				Set<String> arrayAllIds = new HashSet<String>();
				
				for(NewGkLessonTime time:limitByGradeList){
					ids.add(time.getId());
					if(gradeId.equals(time.getObjectId())){
						//年级不排课数量
						if(!noArrangeByEx.containsKey(time.getArrayItemId())){
							noArrangeByEx.put(time.getArrayItemId(), new HashSet<String>());
						}
						noArrangeByEx.get(time.getArrayItemId()).add(time.getId());
						noArrayAllIds.add(time.getId());
					}else{
						if(!arrangeByEx.containsKey(time.getArrayItemId())){
							arrangeByEx.put(time.getArrayItemId(), new HashSet<String>());
						}
						arrangeByEx.get(time.getArrayItemId()).add(time.getId());
						arrayAllIds.add(time.getId());
					}
					
				}
				List<NewGkLessonTimeEx> exList = newGkLessonTimeExService.findByObjectId(ids.toArray(new String[]{}), null);
				
				if(CollectionUtils.isNotEmpty(exList)){
					for(NewGkLessonTimeEx ex:exList){
						if(noArrayAllIds.contains(ex.getScourceTypeId())){
							//取不排课
							if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(ex.getScourceType())){
								
								if(numByObjectId.containsKey(ex.getScourceTypeId())){
									numByObjectId.put(ex.getScourceTypeId(), numByObjectId.get(ex.getScourceTypeId())+1);
								}else{
									numByObjectId.put(ex.getScourceTypeId(), 1);
								}
							}
						}else if(arrayAllIds.contains(ex.getScourceTypeId())){
							//取排课
							if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(ex.getScourceType())){
								if(numByObjectId.containsKey(ex.getScourceTypeId())){
									numByObjectId.put(ex.getScourceTypeId(), numByObjectId.get(ex.getScourceTypeId())+1);
								}else{
									numByObjectId.put(ex.getScourceTypeId(), 1);
								}
							}
						}
					}
				}
			}
			Set<String> subjectIds=new HashSet<String>();
			Map<String,Map<String,Integer>> subjectTeacherNum=new HashMap<String,Map<String,Integer>>();//教师数量
			//教师数量
			List<NewGkTeacherPlan> planList = newGkTeacherPlanService.findByArrayItemIds(itemIds.toArray(new String[]{}), true);
            if(CollectionUtils.isNotEmpty(planList)){
            	for(NewGkTeacherPlan plan : planList){
                	if(!subjectTeacherNum.containsKey(plan.getArrayItemId())){
                		subjectTeacherNum.put(plan.getArrayItemId(), new HashMap<String,Integer>());
                	}
                	subjectTeacherNum.get(plan.getArrayItemId()).put(plan.getSubjectId(), CollectionUtils.isNotEmpty(plan.getExTeacherIdList())?plan.getExTeacherIdList().size():0);
                	subjectIds.add(plan.getSubjectId());
            	}
            }
            List<Course> subjectIdList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {});
			//为了科目顺序保持一致 循环subjectIdList
            for(NewGkArrayItem dto:items){
            	List<String> type=new ArrayList<String>();
                List<String> num=new ArrayList<String>();
//            	if(arrange.containsKey(dto.getId())){
//            		type.add("固定排课时间点");
//            		num.add(arrange.get(dto.getId())+"");
//            	}else{
//            		type.add("固定排课时间点");
//            		num.add("0");
//            	}
//            	if(noArrange.containsKey(dto.getId())){
//            		type.add("固定不排课时间点");
//            		num.add(noArrange.get(dto.getId())+"");
//            	}else{
//            		type.add("固定排课时间点");
//            		num.add("0");
//            	}
                if(noArrangeByEx.containsKey(dto.getId())){
                	for(String s:noArrangeByEx.get(dto.getId())){
                		int factNum=weekDays;
                		if(numByObjectId.containsKey(s)){
                			factNum=factNum-numByObjectId.get(s);
                		}
                		if(factNum>0){
                    		type.add("课时");
                    		num.add(factNum+"");
                    	}else{
                    		type.add("课时");
                    		num.add("0");
                    	}
                	}
                }else{
                	type.add("课时");
            		num.add(weekDays+"");
                }
                
            	Map<String, Integer> oneMap = subjectTeacherNum.get(dto.getId());
            	if(oneMap==null || oneMap.size()==0){
            		
            	}else{
            		if(CollectionUtils.isNotEmpty(subjectIdList)){
                		for(Course cc:subjectIdList){
                    		if(oneMap.containsKey(cc.getId())){
                    			type.add(cc.getSubjectName());
                        		num.add(oneMap.get(cc.getId())+"");
                    		}
                    	}
                	}
            	}
            	
            	newGkItemDto = new NewGkItemDto();
            	if(CollectionUtils.isNotEmpty(type)){
            		newGkItemDto.setTypeName(type.toArray(new String[]{}));
            		newGkItemDto.setNum(num.toArray(new String[]{}));
            	}
            	dto.setNewGkItemDto(newGkItemDto);
            	
            }
		}
		
	}
	
	

	private void makeLessonItem(String gradeId,List<NewGkArrayItem> itemList) {
		Map<String,String> weekMap = NewGkElectiveConstant.dayOfWeekMap;
		Set<String> ids=EntityUtils.getSet(itemList, "id");
		// TODO
		List<NewGkLessonTime> timeList=newGkLessonTimeService.findByObjectTypeAndItem(NewGkElectiveConstant.LIMIT_GRADE_0,ids.toArray(new String[]{}),true);
		if(CollectionUtils.isEmpty(timeList)){
			return;
		}
		Map<String,List<NewGkLessonTimeEx>> itemTimeExMap=new HashMap<String,List<NewGkLessonTimeEx>>();

		for(NewGkLessonTime tt:timeList){
			if(CollectionUtils.isEmpty(tt.getTimesList())){
				continue;
			}
			if(!itemTimeExMap.containsKey(tt.getArrayItemId())){
				itemTimeExMap.put(tt.getArrayItemId(), new ArrayList<NewGkLessonTimeEx>());
			}
			//timeList 与itemList 是一一对应 所以不需要排除相同数据
			itemTimeExMap.get(tt.getArrayItemId()).addAll(tt.getTimesList());
		}
		
		//每天
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);	
		if(grade==null){
			return;
		}
		int oneDays=0;
		if(grade.getAmLessonCount()!=null){
			oneDays=oneDays+grade.getAmLessonCount();
		}
		if(grade.getPmLessonCount()!=null){
			oneDays=oneDays+grade.getPmLessonCount();
		}
		if(grade.getNightLessonCount()!=null){
			oneDays=oneDays+grade.getNightLessonCount();
		}
		if(oneDays==0){
			return;
		}
		//每一天的不排课时间数量
		Map<String,Integer> map=null;
		List<NewGkLessonTimeEx> exList=null;
		for(NewGkArrayItem time:itemList){
			exList=new ArrayList<NewGkLessonTimeEx>();
			if(itemTimeExMap.containsKey(time.getId())){
				exList = itemTimeExMap.get(time.getId());
			}
			map=new HashMap<String,Integer>();
			if(CollectionUtils.isNotEmpty(exList)){
				for(NewGkLessonTimeEx e:exList){
					if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(e.getTimeType())){
						continue;
					}
					if(!map.containsKey(e.getDayOfWeek()+"")){
						map.put(e.getDayOfWeek()+"", 0);
					}
					map.put(e.getDayOfWeek()+"", map.get(e.getDayOfWeek()+"")+1);
				}
			}
			NewGkItemDto newGkItemDto = new NewGkItemDto();
			//默认展现7天
			String[] num = new String[NewGkElectiveConstant.dayOfWeeks];
			String [] typeName = new String[NewGkElectiveConstant.dayOfWeeks];
			newGkItemDto = new NewGkItemDto();
			for (int i=0;i<NewGkElectiveConstant.dayOfWeeks;i++) {
				if(map.containsKey(i+"")){
					int tt = oneDays-map.get(i+"");
					if(tt<=0){
						num[i]=0+"";
					}else{
						num[i]=tt+"";
					}
				}else{
					num[i]=oneDays+"";
				}
				typeName[i]=weekMap.get(i+"");
			}
			newGkItemDto.setNum(num);
			newGkItemDto.setTypeName(typeName);
			time.setNewGkItemDto(newGkItemDto);
		}
	}
	
	@Override
	public Map<String,Integer> findFreeLessonByArrayItem(NewGkArrayItem newGkArrayItem){
		NewGkDivide divide = newGkDivideService.findById(newGkArrayItem.getDivideId());
		//1.根据array_item_id 与object_id object_type is_join获取一条lesson_time记录
		String arrayItemId = newGkArrayItem.getId();
		String objectType = NewGkElectiveConstant.LIMIT_GRADE_0;
		String gradeId = divide.getGradeId();
		String isJoin = "0";
		
		String[] params = new String[]{arrayItemId,gradeId,objectType,isJoin};
		String[] names = new String[]{"arrayItemId","objectId","objectType","isJoin"};
		NewGkLessonTime lesson_Time = newGkLessonTimeService.findOneBy(names, params);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
		
		//2.查出每天有几节不上课
		String scourceTypeId = lesson_Time.getId();
		String scourceType = NewGkElectiveConstant.SCOURCE_LESSON_01;
//		String timeType = NewGkElectiveConstant.ARRANGE_TIME_TYPE_01;
		names = new String[]{"scourceTypeId","scourceType","timeType"};
		List<NewGkLessonTimeEx> lessonTimeExList = newGkLessonTimeExService.findByObjectId(new String[]{scourceTypeId}, new String[]{scourceType});
		//算出每天有几节不可以排课
		Integer lessonCounts = grade.getAmLessonCount()+grade.getPmLessonCount()+grade.getNightLessonCount();
		Map<String,Integer> freeLessonMap = new TreeMap<String,Integer>();
		for(int i=0;i<NewGkElectiveConstant.dayOfWeeks;i++){
			freeLessonMap.put(i+"",lessonCounts);
		}
		for (NewGkLessonTimeEx newGkLessonTimeEx : lessonTimeExList) {
			Integer count = freeLessonMap.get(newGkLessonTimeEx.getDayOfWeek()+"");
			if(count==null){
				continue;
			}
			if(count > 0){
				freeLessonMap.put(newGkLessonTimeEx.getDayOfWeek()+"", count-1);
			}
		}
		
		//删除掉为value为0的键值对
		/*Iterator<Entry<String, Integer>> iterator = freeLessonMap.entrySet().iterator();
		while(iterator.hasNext()){
			if(iterator.next().getValue()==0){
				iterator.remove();
			}
		}*/
		return freeLessonMap;
	}

	private void makePeriodItem(List<NewGkArrayItem> itemList) {
		List<String> itemIdList = EntityUtils.getList(itemList, "id");
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findListByIn("arrayItemId", itemIdList.toArray(new String[0]));
    	if(CollectionUtils.isNotEmpty(subjectTimeList)){
    		//根据arrayItemId分组
    		Map<String,List<NewGkSubjectTime>> subjectTimeMap = new HashMap<String, List<NewGkSubjectTime>>();
    		for (NewGkSubjectTime subjectTime : subjectTimeList) {
    			List<NewGkSubjectTime> inList = subjectTimeMap.get(subjectTime.getArrayItemId());
    			if(inList==null){
    				inList = new ArrayList<NewGkSubjectTime>();
    				subjectTimeMap.put(subjectTime.getArrayItemId(), inList);
    			}
    			inList.add(subjectTime);
			}
    		Set<String> subjectIds = EntityUtils.getSet(subjectTimeList, "subjectId");
    		Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
    		for (NewGkArrayItem arrayItem : itemList) {
    			NewGkItemDto itemDto = new NewGkItemDto();
    			List<NewGkSubjectTime> inList = subjectTimeMap.get(arrayItem.getId());
    			String[] nums = new String[inList.size()];
    			String[] typeNames = new String[inList.size()];
    			int i = 0;
    			//文理需要标明
     			for (NewGkSubjectTime subjectTime : inList) {
					nums[i] = subjectTime.getPeriod()+"";
					String subjectName=courseNameMap.get(subjectTime.getSubjectId());
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals( subjectTime.getSubjectType())
							|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals( subjectTime.getSubjectType())){
						subjectName=subjectName+subjectTime.getSubjectType();
					}
					typeNames[i] = subjectName;
					i++;
				}
    			itemDto.setNum(nums);
    			itemDto.setTypeName(typeNames);
    			arrayItem.setNewGkItemDto(itemDto);
    		}

    	}
    	
		
		
	}
	private void makeTeacherItem(List<NewGkArrayItem> itemList){
		List<String> itemIdList = EntityUtils.getList(itemList, "id");

		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(itemIdList.toArray(new String[0]) , true);

		if(CollectionUtils.isNotEmpty(teacherPlanList)){
			Map<String , List<NewGkTeacherPlan>> teacherPlanMap = new HashMap<>();
			for(NewGkTeacherPlan plan : teacherPlanList){
				List<NewGkTeacherPlan> planList = teacherPlanMap.get(plan.getArrayItemId());
				if(planList == null){
					planList = new ArrayList<>();
					teacherPlanMap.put(plan.getArrayItemId(),planList);
				}
				planList.add(plan);
			}
			Set<String> subjectIds = EntityUtils.getSet(teacherPlanList, "subjectId");
			Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			for (NewGkArrayItem arrayItem : itemList) {
				NewGkItemDto itemDto = new NewGkItemDto();
				List<NewGkTeacherPlan> inList = teacherPlanMap.get(arrayItem.getId());
				if(inList == null){
					inList = new ArrayList<>();
				}
				String[] nums = new String[inList.size()];
				String[] typeNames = new String[inList.size()];
				int i = 0;
				for (NewGkTeacherPlan plan : inList) {
					List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
					if(planExList == null){
						planExList = new ArrayList<>();
					}
					nums[i] = String.valueOf(planExList.size());
//					String subjectType = subjectTime.getSubjectType();
//					if(subjectType.equals(NewGkElectiveConstant.SUBJECT_TYPE_O)){
//						subjectType="";
//					}
					typeNames[i] = courseNameMap.get(plan.getSubjectId());
					i++;
				}
				itemDto.setNum(nums);
				itemDto.setTypeName(typeNames);
				arrayItem.setNewGkItemDto(itemDto);
			}
		}

	}
	private void makePlaceItem(List<NewGkArrayItem> newGkArrayItemList) {		
//		NewGkDivide newGkDivide = newGkDivideService.findById(newGkArrayItemList.get(0).getDivideId());
		Set<String> arrayItemIdSet = new HashSet<String>();
		for(NewGkArrayItem item : newGkArrayItemList){
			arrayItemIdSet.add(item.getId());
		}
		if(CollectionUtils.isNotEmpty(arrayItemIdSet)){
			List<NewGkplaceArrange> newGkplaceArrangeList = newGkplaceArrangeService.findByArrayItemIds(arrayItemIdSet.toArray(new String[0]));
		    for(NewGkArrayItem item1 : newGkArrayItemList){
		    	int i = 0;
		    	for(NewGkplaceArrange item2 : newGkplaceArrangeList){
		    		if(item1.getId().equals(item2.getArrayItemId())){
		    			item1.setCountPlace(i);
		    			i++;
		    		}
		    	}
		    	item1.setCountPlace(i);
//		    	item1.setGalleryful(String.valueOf(newGkDivide.getGalleryful())+"+"+String.valueOf(newGkDivide.getMaxGalleryful()));
		    }
		    Map<String, NewGkItemDto> newGkItemDtoMap = getNewGkItemDtoMap(arrayItemIdSet, newGkplaceArrangeList);
		    Map<String, Integer> groupInfo = placeItemService.findGroupInfo(arrayItemIdSet.toArray(new String[0]));
		    for(NewGkArrayItem item : newGkArrayItemList){
		    	item.setNewGkItemDto(newGkItemDtoMap.get(item.getId()));
		    	
		    	if(!groupInfo.containsKey(item.getId()) || groupInfo.get(item.getId()) < 1) {
		    		item.setGalleryful("当前方案还未给任何班级安排教室");
		    	}
		    }
		    
		    
		}		
//		Collections.sort(newGkArrayItemList,new Comparator<NewGkArrayItem>(){
//			public int compare(NewGkArrayItem arg0, NewGkArrayItem arg1) {
//				return arg0.getItemName().compareTo(arg1.getItemName());
//			}
//	    });
		
	}
	
	
	
	public Map<String, NewGkItemDto> getNewGkItemDtoMap(Set<String> arrayItemIdSet, List<NewGkplaceArrange> newGkplaceArrangeList){
		Map<String, NewGkItemDto> newGkItemDtoMap = new HashMap<String, NewGkItemDto>();
		Set<String> placeIdSet = new HashSet<String>();
		for(NewGkplaceArrange item : newGkplaceArrangeList){
			placeIdSet.add(item.getPlaceId());
		}
		Map<String, String> teachPlaceTypeMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(placeIdSet)){
			Map<String, String> placeTypeKap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMapByAttr(placeIdSet.toArray(new String[0]),
					"placeType"), new TR<Map<String, String>>(){});
			if(placeTypeKap.size() <=0){
				return newGkItemDtoMap;
			}
			for(String itemId : placeTypeKap.keySet()){
				String placeType = placeTypeKap.get(itemId);
		    	String[] typeArr = placeType.split(",");
		    	String type = "";
		    	if(typeArr.length>0){
		    		type = typeArr[0];
		    	}else{
		    		type = placeType;
		    	}
		    	teachPlaceTypeMap.put(itemId, type);
		    }
			
		}
		List<McodeDetail> mcodeDetailList = SUtils.dt(mcodeRemoteService.findByMcodeIds(new String[]{"DM-CDLX"}), new TR<List<McodeDetail>>(){});
		Map<String, String> placeTypeNameMap = new HashMap<String, String>();
		for(McodeDetail item : mcodeDetailList){
			placeTypeNameMap.put(item.getThisId(), item.getMcodeContent());
		}
		Map<String, Set<String>> typeMap = new HashMap<String, Set<String>>();
		Map<String, List<String>> placeIdMap = new HashMap<String, List<String>>();
		for(String arrayItemId : arrayItemIdSet){
			Set<String> typeSet = new HashSet<String>();
			List<String> placeIdList = new ArrayList<String>();
			for(NewGkplaceArrange item : newGkplaceArrangeList){
				if(arrayItemId.equals(item.getArrayItemId())){
					typeSet.add(teachPlaceTypeMap.get(item.getPlaceId()));
					placeIdList.add(item.getPlaceId());
				}
			}
			typeMap.put(arrayItemId, typeSet);
			placeIdMap.put(arrayItemId, placeIdList);
		}
		for(String arrayItemId : arrayItemIdSet){
			NewGkItemDto newGkItemDto = new NewGkItemDto();
			Set<String> typeSet = typeMap.get(arrayItemId);
			List<String> placeIdList = placeIdMap.get(arrayItemId);
			String[] typeArr = new String[typeSet.size()];
			String[] numArr = new String[typeSet.size()];
			int p = 0;
			for(String type : typeSet){
				if(type == null) {
					continue;
				}
				int i = 0;
				typeArr[p] = placeTypeNameMap.get(type);
				for(String placeId : placeIdList){
					if(teachPlaceTypeMap.get(placeId) == null) {
						continue;
					}
					if(type.equals(teachPlaceTypeMap.get(placeId))){
						i++;
					}
				}
				numArr[p] = String.valueOf(i);
				p++;
			}
			if(typeSet.size() == 0) {
				typeArr = new String[] {"教室"};
				numArr = new String[] {"0"};
			}
			newGkItemDto.setTypeName(typeArr);
			newGkItemDto.setNum(numArr);
			newGkItemDtoMap.put(arrayItemId, newGkItemDto);
		}
		return newGkItemDtoMap;
	}

	@Override
	public void saveCopyArrayFeature(ArrayFeaturesDto featureDto) {
		
		if(featureDto.getArrayItem() == null) {
			throw new RuntimeException("arrayItem 为空，新的排课特征未能成功复制。");
		}
		
		save(featureDto.getArrayItem());
		if(CollectionUtils.isNotEmpty(featureDto.getLessonTimeList())) {
			newGkLessonTimeService.saveAll(featureDto.getLessonTimeList().toArray(new NewGkLessonTime[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getLessonTimeExList())) {
			newGkLessonTimeExService.saveBatch(featureDto.getLessonTimeExList());
		}
		if(CollectionUtils.isNotEmpty(featureDto.getSubjectTimes())) {
			newGkSubjectTimeService.saveAll(featureDto.getSubjectTimes().toArray(new NewGkSubjectTime[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getRelationList())) {
			relationService.saveAll(featureDto.getRelationList().toArray(new NewGkChoRelation[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getTeacherPlanList())) {
			newGkTeacherPlanService.saveAll(featureDto.getTeacherPlanList().toArray(new NewGkTeacherPlan[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getTeacherPlanExList())) {
			teacherPlanExService.saveAll(featureDto.getTeacherPlanExList().toArray(new NewGkTeacherPlanEx[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getRelateSubTimeList())) {
			relateSubtimeService.saveAll(featureDto.getRelateSubTimeList().toArray(new NewGkRelateSubtime[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getNewClasSubjTimes())) {
			newGkClassSubjectTimeService.saveAll(featureDto.getNewClasSubjTimes().toArray(new NewGkClassSubjectTime[] {}));
		}
		if(CollectionUtils.isNotEmpty(featureDto.getNewCombineList())) {
			newGkClassCombineRelationService.saveAll(featureDto.getNewCombineList().toArray(new NewGkClassCombineRelation[] {}));
		}
	}

}
