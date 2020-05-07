package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkBatchDao;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("gkBatchService")
public class GkBatchServiceImpl extends BaseServiceImpl<GkBatch, String> implements GkBatchService {

    @Autowired
    private GkBatchDao gkBatchDao;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private GkGroupClassService gkGroupClassService;
    @Autowired
    private GkRoundsService gkRoundsService;
    @Autowired
    private GkTeachClassStoreService gkTeachClassStoreService;
    @PersistenceContext
	private EntityManager entityManager;
    
    @Override
    protected BaseJpaRepositoryDao<GkBatch, String> getJpaDao() {
        return gkBatchDao;
    }

    @Override
    protected Class<GkBatch> getEntityClass() {
        return GkBatch.class;
    }


    @Override
    public List<GkBatch> findByClassIds(String roundsId, String[] teachClassIds) {
        if (ArrayUtils.isEmpty(teachClassIds)) {
            return null;
        }
        return gkBatchDao.findByClassIds(roundsId, teachClassIds);
    }

    @Override
    public List<GkBatch> findByRoundsIdAndPlaceIn(String roundsId, String[] placeIds) {
        if (ArrayUtils.isEmpty(placeIds)) {
            return null;
        }
        return gkBatchDao.findByPlaceIds(roundsId, placeIds);
    }

    @Override
    public List<GkBatch> findByBatchAndInClassIds(String roundId, int batch, String[] teachClassIds) {
    	if(teachClassIds==null || teachClassIds.length<=0){
    		return gkBatchDao.findByBatch(roundId,batch);
    	}
        return gkBatchDao.findByBatchAndInClassIds(roundId, batch, teachClassIds);
    }

    @Override
    public void deleteByRoundsId(String roundsId) {
    	List<GkBatch> list  = this.gkBatchDao.findByRoundsId(roundsId);
    	gkGroupClassService.deleteByRoundsId(roundsId, null);
    	if(CollectionUtils.isNotEmpty(list)){
    		Set<String> tclaids = EntityUtils.getSet(list, "teachClassId");
    		gkTeachClassStoreService.deleteByIds(tclaids.toArray(new String[0]));
    	}
    	gkBatchDao.deleteByRoundsId(roundsId);
    }
    @Override
    public void deleteByRoundsIdAndType(String roundsId, String type) {
        boolean flag = false;// 是否单科排
        if (GkElectveConstants.GKCONDITION_SINGLE_0.equals(type)) {
            flag = true;
        }
        // 清除之前的数据(组合排班 清空所有排班记录，单科排班 只清空单科排班)
        List<GkBatch> oldGkBatchList;
		if (flag) {
			oldGkBatchList = gkBatchDao.findByRoundsIdAndGroupClassId(BaseConstants.ZERO_GUID,roundsId);
		} else {
			oldGkBatchList = gkBatchDao.findByRoundsId(roundsId);
		}
        Set<String> oldteachIds = new HashSet<String>();
        Set<String> batchIds = new HashSet<String>();// 删除的batchId
        if (CollectionUtils.isNotEmpty(oldGkBatchList)) {
            for (GkBatch item : oldGkBatchList) {
                oldteachIds.add(item.getTeachClassId());
                batchIds.add(item.getId());
            }
        }
        if (oldteachIds.size() > 0) {// 删除教学班/学生/课程表 TODO
//        	GkRounds round = gkRoundsService.findRoundById(roundsId);
//        	GkSubjectArrange gkArrange = gkSubjectArrangeDao.findById(round.getSubjectArrangeId());
//			clearCourseSchedule(round, gkArrange);
        	gkTeachClassStoreService.deleteByIds(oldteachIds.toArray(new String[] {}));
        }
//        if (!flag) {
//            gkGroupClassService.deleteByRoundsId(roundsId, GkElectveConstants.USER_AUTO);
//        }
        if (batchIds.size() > 0) {
            deleteAll(batchIds.toArray(new String[0]));
        }
    }

    public List<GkBatch> saveAllEntitys(GkBatch... gkBatch) {
        return gkBatchDao.saveAll(checkSave(gkBatch));
    }

    @Override
    public void deleteAll(String[] ids) {
        gkBatchDao.deleteByIds(ids);
    }

    @Override
	public void saveBatchs(GkRounds round,GkSubjectArrange gkArrange,List<GkGroupClassStu> groupClassStu,
			List<GkGroupClass> groupClass, List<GkBatch> bath,List<GkTeachClassStore> teachClass,
			List<GkTeachClassStuStore> teachClassStu,List<GkTeachClassEx> exList,boolean isCom){
    	//将之前轮次的教学班毕业---在新建轮次的时候加排除啦
        List<GkBatch> oldGkBatchList = null;
        Integer step = null;
        if(isCom){
			//清除组合批次 教学班  单科数据批次 教学班	2017年6月19日没有组合班批次概念
//        	step = GkElectveConstants.STEP_3;
//        	oldGkBatchList = gkBatchDao.findByRoundsId(round.getId());	
		}else{
			//清除单科数据批次 教学班
			oldGkBatchList = gkBatchDao.findByRoundsIdAndGroupClassId(BaseConstants.ZERO_GUID,round.getId());
			step = GkElectveConstants.STEP_5;
		}
        Set<String> oldteachIds = new HashSet<String>();
        Set<String> batchIds = new HashSet<String>();// 删除的batchId
        if (CollectionUtils.isNotEmpty(oldGkBatchList)) {
            for (GkBatch item : oldGkBatchList) {
                oldteachIds.add(item.getTeachClassId());
                batchIds.add(item.getId());
            }
        }
        if(batchIds.size()>0){
        	deleteAll(batchIds.toArray(new String[]{}));
        }
        if (oldteachIds.size() > 0) {// 删除教学班/学生
            teachClassRemoteService.deleteByIds(oldteachIds.toArray(new String[] {}));
        }
    	
		if(CollectionUtils.isNotEmpty(bath)){
			for(int i = 0; i < bath.size(); i++) {  
				entityManager.persist(bath.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();  
        	entityManager.clear();  
			//this.saveAll(bath.toArray(new GkBatch[0]));
		}
		if(CollectionUtils.isNotEmpty(groupClass)){
//			for(int i = 0; i < groupClass.size(); i++) {  
//				entityManager.persist(groupClass.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();
//        	entityManager.clear();
			gkGroupClassService.saveAll(groupClass.toArray(new GkGroupClass[0]));
		}
		if(CollectionUtils.isNotEmpty(groupClassStu)){
			for(int i = 0; i < groupClassStu.size(); i++) {  
				entityManager.persist(groupClassStu.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();
        	entityManager.clear();
			//gkGroupClassStuService.saveAll(groupClassStu.toArray(new GkGroupClassStu[0]));
		}
		if(CollectionUtils.isNotEmpty(teachClass)){
			for(int i = 0; i < teachClass.size(); i++) {  
				entityManager.persist(teachClass.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();
        	entityManager.clear();
			 //teachClassRemoteService.saveAll(SUtils.s(teachClass));
		}
		if(CollectionUtils.isNotEmpty(teachClassStu)){
			for(int i = 0; i < teachClassStu.size(); i++) {  
				entityManager.persist(teachClassStu.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();
        	entityManager.clear();
			//teachClassStuRemoteService.saveAll(SUtils.s(teachClassStu));
		}
		if(CollectionUtils.isNotEmpty(exList)){
			for(int i = 0; i < exList.size(); i++) {  
				entityManager.persist(exList.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();
        	entityManager.clear();
			//gkTeachClassExService.saveAll(exList.toArray(new GkTeachClassEx[]{}));
		}
		if(round.getStep()!=step){
    		gkRoundsService.updateStep(step, round.getId());
    	}
		//清除课程表 TODO 2017年6月14日 轮次里没有课程表
//		clearCourseSchedule(round,gkArrange);
	}
//    /**
//     * key:批次 value: 周几_上午_节次
//     */
//    private Map<Integer,Set<String>> getTime(GkRounds round){
//    	List<GkTimetableLimitArrang> limtIds = gkTimetableLimitArrangService.findGkTimetableLimitArrangList(round.getAcadyear(), round.getSemester(), round.getId(), GkElectveConstants.LIMIT_TYPE_6);
//    	Map<Integer,Set<String>> returnMap=new HashMap<Integer, Set<String>>();
//    	if(CollectionUtils.isNotEmpty(limtIds)){
//    		for(GkTimetableLimitArrang l:limtIds){
//    			int key = Integer.parseInt(l.getArrangId());
//    			String str=l.getWeekday()+"_"+l.getPeriodInterval()+"_"+l.getPeriod();
//    			if(!returnMap.containsKey(key)){
//    				returnMap.put(key, new HashSet<String>());
//    			}
//    			returnMap.get(key).add(str);
//    		}
//    	}
//    	return returnMap;
//    
//    }
    /**
     * 拿到value
     * @param map
     * @return
     */
//    private Set<String> getValues(Map<Integer,Set<String>> map){
//    	Set<String> returnSet=new HashSet<String>();
//    	if(map.size()<=0){
//    		return returnSet;
//    	}
//    	for(Integer key:map.keySet()){
//    		returnSet.addAll(map.get(key));
//    	}
//    	return returnSet;
//    }
//    @Override
//    public void clearCourseSchedule(GkRounds round,GkSubjectArrange gkArrange){
//    	//拿到当前时间段内所有课程表
//		List<GkRounds> rounds = gkRoundsService.findBySubjectArrangeIdHasDelete(gkArrange.getId());
//		List<String> roundsIds=new ArrayList<String>();
//		List<String> teachClassIds=new ArrayList<String>();
//		if(CollectionUtils.isNotEmpty(rounds)){
//			roundsIds = EntityUtils.getList(rounds, "id");
//			List<GkBatch> list = gkBatchDao.findByRoundsId(roundsIds.toArray(new String[]{}));
//			if(CollectionUtils.isNotEmpty(list)){
//				teachClassIds = EntityUtils.getList(list, "teachClassId");
//			}
//		}
//		//key:批次 value: 周几_上午_节次
//		Map<Integer, Set<String>> timeMap = getTime(round);
//		//拿到当前轮次的时间点 周几_上午_节次
//		Set<String> timeSet=getValues(timeMap);
////		if(timeSet.size()<=0){
//			//没有设置上课时间
////			throw new RuntimeException("没有设置上课时间！");
////			return;
////		}
//		
//		//需要删除的课程
//		Set<String> delIds=new HashSet<String>();
//		String str=null;
//		CourseScheduleDto dto=new CourseScheduleDto();
//		dto.setSchoolId(gkArrange.getUnitId());
//        dto.setAcadyear(round.getAcadyear());
//        dto.setSemester(Integer.parseInt(round.getSemester()));
//        dto.setDayOfWeek1(round.getDayOfWeek());
//        dto.setWeekOfWorktime1(round.getWeekOfWorktime());
//        dto.setDayOfWeek2(round.getDayOfWeek2());
//        dto.setWeekOfWorktime2(round.getWeekOfWorktime2());
//        //dto.setGradeId(gkArrange.getGradeId());
//        List<Clazz> clazzList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gkArrange.getGradeId()),new TR<List<Clazz>>(){});
//        Set<String> clazzIds=new HashSet<String>();
//        if(CollectionUtils.isNotEmpty(clazzList)){
//        	clazzIds=EntityUtils.getSet(clazzList,"id");
//        }
//        //当前时间内课程表（包括前与后）
//        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
//        if(CollectionUtils.isNotEmpty(crlist)){
//        	for(CourseSchedule c:crlist){
//        		//删除当前年级 各轮次的产生的教学班课程---遗留非7选3产生的教学班
//        		if(teachClassIds.contains(c.getClassId())){
//        			//开始
//        			if(c.getWeekOfWorktime()==round.getWeekOfWorktime()){
//    					if(c.getDayOfWeek()<round.getDayOfWeek()){
//    						continue;
//    					}
//    				}else if(c.getWeekOfWorktime()==round.getWeekOfWorktime2()){
//    					//结尾	
//    					if(c.getDayOfWeek()>round.getDayOfWeek2()){
//    						continue;
//    					}
//    				}
//        			delIds.add(c.getId());
//        			continue;
//        		}
//        		//删除需要的批次上课时间点 行政班数据
//        		if(timeSet.size() > 0 && clazzIds.contains(c.getClassId())){
//        			str = c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
//            		if(timeSet.contains(str)){
//            			delIds.add(c.getId());
//            		}
//        		}
//        	}
//        	if(delIds.size()>0){
//        		courseScheduleRemoteService.deleteCourseByIds(delIds.toArray(new String[0]));
//        	}
//        }
//    }
    
    
	@Override
	public List<GkBatch> findByRoundsId(String roundIds, String type) {
		if(StringUtils.isBlank(type)){
			return gkBatchDao.findByRoundsId(roundIds);
		}else if(GkElectveConstants.GKCONDITION_GROUP_1.equals(type)){
			return gkBatchDao.findByGroupRoundsId(BaseConstants.ZERO_GUID,roundIds);
		}else{
			return gkBatchDao.findByRoundsIdAndGroupClassId(BaseConstants.ZERO_GUID,roundIds);
		}
	}

	@Override
	public List<GkBatch> findBatchList(final String roundsId,final String gkBatchType) {
		Specification<GkBatch> s = new Specification<GkBatch>() {
            @Override
            public Predicate toPredicate(Root<GkBatch> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("roundsId").as(String.class), roundsId));
                if(StringUtils.isBlank(gkBatchType)){
                }else if(GkElectveConstants.GKCONDITION_GROUP_1.equals(gkBatchType)){
                	ps.add(cb.notEqual(root.get("groupClassId").as(String.class), Constant.GUID_ZERO));
                }else if(GkElectveConstants.GKCONDITION_SINGLE_0.equals(gkBatchType)){
                	ps.add(cb.equal(root.get("groupClassId").as(String.class), Constant.GUID_ZERO));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("batch").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
		return gkBatchDao.findAll(s);
	}

	@Override
	public List<GkBatch> findBatchListByGroupClassId(String roundsId, String... groupClassId) {
		if(ArrayUtils.isEmpty(groupClassId)){
			return new ArrayList<GkBatch>();
		}
		return gkBatchDao.findBatchListByGroupClassId(roundsId, groupClassId);
	}
	/**
	 * 
	 * @param teachClass
	 * @param time 周几_上午_节次
	 * @param dto 开始时间 与结束时间
	 * @return
	 */
//	private List<CourseSchedule> makeCourseSchedule(TeachClass teachClass,Set<String> time,CourseScheduleDto dto){
//		List<CourseSchedule> retuenList=new ArrayList<CourseSchedule>();
//		for(String t:time){
//			String[] arr = t.split("_");
//			int dayOfWeek=Integer.parseInt(arr[0]);
//			String periodInterval=arr[1];
//			int period=Integer.parseInt(arr[2]);
//			int start=dto.getWeekOfWorktime1();
//			int end=dto.getWeekOfWorktime2();
//			int dayStart=dto.getDayOfWeek1();
//			int dayEnd=dto.getDayOfWeek2();
//			for (int index=start;index<=end;index++){
//				CourseSchedule c = makeNewCourseSchedule(teachClass);
//				if(index==start){
//					if(dayOfWeek<dayStart){
//						continue;
//					}
//				}else if(index==end){
//					if(dayOfWeek>dayEnd){
//						continue;
//					}
//				}
//				c.setWeekOfWorktime(index);
//				c.setPeriod(period);
//				c.setPeriodInterval(periodInterval);
//				c.setDayOfWeek(dayOfWeek);
//				retuenList.add(c);
//			}
//		}
//		return retuenList;
//		
//	}
	
//	private CourseSchedule makeNewCourseSchedule(TeachClass t){
//		CourseSchedule c=new CourseSchedule();
//		c.setId(UuidUtils.generateUuid());
//		c.setAcadyear(t.getAcadyear());
//		c.setSemester(Integer.parseInt(t.getSemester()));
//		c.setClassId(t.getId());
//		c.setClassType(CourseSchedule.CLASS_TYPE_SEVEN);
//		c.setSubjectId(t.getCourseId());
//		c.setSchoolId(t.getUnitId());
//		c.setTeacherId(t.getTeacherId());
//		
//		//c.setSubjectType(subjectType)
//		return c;
//	}


//	@Override
//	public String saveAllotTeacher(GkRounds round,GkSubjectArrange gkArrange,
//			Map<String, Set<String>> subjectteacher) {
//		//key:批次 value: 周几_上午_节次
//		Map<Integer,Set<String>> timeMap=getTime(round);
//		//key:周几_上午_节次 value:批次
//		Map<String,Integer> bathTimeMap=getValuetoKey(timeMap);
//		if(bathTimeMap.size()<=0){
//			// 没有设置上课时间
//			return "没有设置上课时间！";
//		}
//		clearCourseSchedule(round, gkArrange);
//		CourseScheduleDto dto=new CourseScheduleDto();
//		dto.setSchoolId(gkArrange.getId());
//        dto.setAcadyear(round.getAcadyear());
//        dto.setSchoolId(gkArrange.getUnitId());
//        dto.setSemester(Integer.parseInt(round.getSemester()));
//        dto.setDayOfWeek1(round.getDayOfWeek());
//        dto.setWeekOfWorktime1(round.getWeekOfWorktime());
//        dto.setDayOfWeek2(round.getDayOfWeek2());
//        dto.setWeekOfWorktime2(round.getWeekOfWorktime2());
//        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
//        String str=null;
//        //key:批次 value: teacherIds //不能排的教师
//      	Map<Integer,Set<String>> noteacher=new HashMap<Integer, Set<String>>();
//      	Map<String,Integer> teacherClassNum=new HashMap<String, Integer>();//教师已排班级数
//        if(CollectionUtils.isNotEmpty(crlist)){
//        	for(CourseSchedule c:crlist){
//        		str = c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
//        		if(bathTimeMap.containsKey(str)){
//        			Integer bath = bathTimeMap.get(str);
//        			if(StringUtils.isNotBlank(c.getTeacherId())){
//        				if(!noteacher.containsKey(bath)){
//        					noteacher.put(bath, new HashSet<String>());
//        				}
//        				noteacher.get(bath).add(c.getTeacherId());
//        			}
//        		}
//        	
//        	}
//        }		
//        List<GkBatch> list = gkBatchDao.findByRoundsId(round.getId());
//        List<TeachClass> updateTeachList=new ArrayList<TeachClass>();
//        List<CourseSchedule> insertCourseList=new ArrayList<CourseSchedule>(); 
//        List<CourseSchedule> cList =null;
//        TeachClass teach=null;
//        if(CollectionUtils.isNotEmpty(list)){
//        	List<String> teachClassIds=EntityUtils.getList(list,"teachClassId");
//        	List<TeachClass> teachList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
//        	if(CollectionUtils.isNotEmpty(list)){
//        		Map<String, TeachClass> teachMap = EntityUtils.getMap(teachList, "id");
//        		for(GkBatch b:list){
//        			int index=b.getBatch();
//        			Set<String> time=timeMap.get(index);
//        			if(time.size()<=0){
//        				continue;
//        			}
//        			if(!noteacher.containsKey(index)){
//        				noteacher.put(index, new HashSet<String>());
//        			}
//        			Set<String> nottt = noteacher.get(index);
//        			
//        			if(teachMap.containsKey(b.getTeachClassId())){
//        				teach = teachMap.get(b.getTeachClassId());
//        				Set<String> teacherIdsSet = subjectteacher.get(teach.getCourseId());
//        				if(StringUtils.isNotBlank(teach.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(teach.getTeacherId()))){
//        					//已有老师
//        					if(!nottt.contains(teach.getTeacherId())){
//        						if(!teacherClassNum.containsKey(teach.getTeacherId())){
//        							teacherClassNum.put(teach.getTeacherId(), 1);
//        						}else{
//        							teacherClassNum.put(teach.getTeacherId(), teacherClassNum.get(teach.getTeacherId()+1));
//        						}
//        						//沿用原来老师
//        						//组装课程表
//        						cList = makeCourseSchedule(teach, time, dto);
//        						insertCourseList.addAll(cList);
//							}else{
////								String teacherId=null;
//								
////	        					if(teacherIdsSet!=null && teacherIdsSet.size()>0){
////	        						for(String s:teacherIdsSet){
////	        							if(!nottt.contains(s)){
////	        								teacherId=s;
////	        								break;
////	        							}
////	        						}
////	        					}
//								//取得最适合老师 已排班级数最少
//								String teacherId=findGoodTeacher(teacherClassNum,nottt,teacherIdsSet);
//	        					
//	        					if(StringUtils.isNotBlank(teacherId)){
//	        						teach.setTeacherId(teacherId);
//	        						noteacher.get(index).add(teacherId);
//	        						//组装课程表
//	        						cList = makeCourseSchedule(teach, time, dto);
//	        						insertCourseList.addAll(cList);
//	        						updateTeachList.add(teach);
//	        						if(!teacherClassNum.containsKey(teacherId)){
//	        							teacherClassNum.put(teacherId, 1);
//	        						}else{
//	        							teacherClassNum.put(teacherId, teacherClassNum.get(teacherId));
//	        						}
//	        					}else{
//	        						teach.setTeacherId(BaseConstants.ZERO_GUID);
//	        						//组装课程表
//	        						cList = makeCourseSchedule(teach, time, dto);
//	        						insertCourseList.addAll(cList);
//	        						updateTeachList.add(teach);
//	        					}
//							}
//        				}else{
////        					String teacherId=null;
////        					if(teacherIdsSet!=null && teacherIdsSet.size()>0){
////        						for(String s:teacherIdsSet){
////        							if(!nottt.contains(s)){
////        								teacherId=s;
////        								break;
////        							}
////        						}
////        					}
//        					String teacherId=findGoodTeacher(teacherClassNum,nottt,teacherIdsSet);
//        					
//        					if(StringUtils.isNotBlank(teacherId)){
//        						teach.setTeacherId(teacherId);
//        						noteacher.get(index).add(teacherId);
//        						//组装课程表
//        						cList = makeCourseSchedule(teach, time, dto);
//        						insertCourseList.addAll(cList);
//        						updateTeachList.add(teach);
//        						if(!teacherClassNum.containsKey(teacherId)){
//        							teacherClassNum.put(teacherId, 1);
//        						}else{
//        							teacherClassNum.put(teacherId, teacherClassNum.get(teacherId)+1);
//        						}
//        					}else{
//        						teach.setTeacherId(BaseConstants.ZERO_GUID);
//        						//组装课程表
//        						cList = makeCourseSchedule(teach, time, dto);
//        						insertCourseList.addAll(cList);
//        						updateTeachList.add(teach);
//        					}
//        					
//        				}
//        			}
//        		}
//        	}
//        }
//        entityManager.clear(); 
//        if(CollectionUtils.isNotEmpty(updateTeachList)){
//        	for(int i = 0; i < updateTeachList.size(); i++) {  
//				entityManager.merge(updateTeachList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//        	entityManager.clear();  
//        	//teachClassRemoteService.saveAll(SUtils.s(updateTeachList));     
//        }
//        if(CollectionUtils.isNotEmpty(insertCourseList)){
//        	for(int i = 0; i < insertCourseList.size(); i++) {  
//				entityManager.persist(insertCourseList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//        	entityManager.clear();  
//        	//courseScheduleRemoteService.saveAll(SUtils.s(insertCourseList));
//        }
//        //数据结束该项目解锁
//		if(GkElectveConstants.USE_TRUE==gkArrange.getIsLock()){
//			gkArrange.setIsLock(GkElectveConstants.USE_FALSE);
//			gkArrange.setModifyTime(new Date());
//			gkSubjectArrangeService.save(gkArrange);
//		}
//		gkRoundsService.updateStep(GkElectveConstants.STEP_5,round.getId() );
//		return null;
//		
//	}
	/**
	 * 查询班级数负担最轻 且有时间的老师
	 * @param teacherClassNum  老师已排班级数
	 * @param notTeacher  不能排的老师
	 * @param teacherIdsSet 所有该科目下可排的老师
	 * @return
	 */
//	private String findGoodTeacher(Map<String, Integer> teacherClassNum,
//			Set<String> notTeacher, Set<String> teacherIdsSet) {
//		String teacherId=null;
//		int claNum=0;
//		if(CollectionUtils.isNotEmpty(teacherIdsSet)){
//			for(String t:teacherIdsSet){
//				if(!notTeacher.contains(t)){
//					if(StringUtils.isNotBlank(teacherId)){
//						int temp=0;
//						if(teacherClassNum.containsKey(t)){
//							temp=teacherClassNum.get(t);
//						}
//						if(temp==0){
//							teacherId=t;
//							claNum=temp;
//							break;
//						}else{
//							if(temp<claNum){
//								teacherId=t;
//								claNum=temp;
//							}
//						}
//					}else{
//						teacherId=t;
//						if(teacherClassNum.containsKey(t)){
//							claNum=teacherClassNum.get(t);
//						}else{
//							claNum=0;
//							break;
//						}
//					}
//				}
//			}
//			
//		}
//		return teacherId;
//	}

//	private Map<String, Integer> getValuetoKey(Map<Integer, Set<String>> map) {
//		Map<String, Integer> returnMap=new HashMap<String, Integer>();
//		if(map.size()<=0){
//			return returnMap;
//		}
//		for(Integer key:map.keySet()){
//			Set<String> set = map.get(key);
//			if(set.size()>0){
//				for(String s:set){
//					returnMap.put(s, key);
//				}
//			}
//		}
//		return returnMap;
//	}

	@Override
	public List<GkBatch> findGkBatchList(final String roundsId,final Integer batch,final String classType) {
		Specification<GkBatch> s = new Specification<GkBatch>() {
            @Override
            public Predicate toPredicate(Root<GkBatch> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("roundsId").as(String.class), roundsId));
                if(batch!=null){
                	ps.add(cb.equal(root.get("batch").as(String.class), batch));
                }
                if(StringUtils.isNotBlank(classType)){
                	ps.add(cb.equal(root.get("classType").as(String.class), classType));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("batch").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
		return gkBatchDao.findAll(s);
	}

	@Override
	public void deleteByGroupClassId(String roundsId, String groupClassId, String schoolId) {
//		GkRounds gkRounds = gkRoundsService.findOne(roundsId);
//		List<GkBatch> baths = gkBatchDao.findBatchListByGroupClassId(roundsId, groupClassId);
//		if(CollectionUtils.isNotEmpty(baths)){
//			Set<String> bId=new HashSet<String>();
//			Set<String> tId=new HashSet<String>();
//			for(GkBatch b:baths){
//				bId.add(b.getId());
//				tId.add(b.getTeachClassId());
//			}
//			if(bId.size()>0){
//				gkBatchDao.deleteByIds(bId.toArray(new String[]{}));
//				teachClassRemoteService.deleteByIds(tId.toArray(new String[]{}));
//			}
//			// 删除课表信息
//			List<CourseSchedule> css = new ArrayList<CourseSchedule>();
//			for (String classId : tId) {
//				List<CourseSchedule> tps=SUtils.dt(courseScheduleRemoteService.findByClassId(schoolId, gkRounds.getAcadyear(), Integer.parseInt(gkRounds.getSemester()), classId,null),new TR<List<CourseSchedule>>(){});
////				List<CourseSchedule> tps = SUtils.dt(courseScheduleRemoteService.findByClassIdAll(schoolId, classId), new TR<List<CourseSchedule>>(){});
//				if(CollectionUtils.isNotEmpty(tps)){
//					css.addAll(tps);
//				}
//			}
//			if(css.size() > 0){
//				List<String> csIds = EntityUtils.getList(css, "id");
//				courseScheduleRemoteService.deleteCourseByIds(csIds.toArray(new String[0]));
//			}
//		}
		
		gkGroupClassService.deleteById(groupClassId);
		
	}
	
//	@Override
//	public void saveTeaAndCourseSch(List<TeachClass> clsList, String planId, String unitId) {
//		// TODO Auto-generated method stub
//		teachClassRemoteService.saveAll(SUtils.s(clsList));
//		//修改课表
//		Set<String> teaClsIds = EntityUtils.getSet(clsList, "id");
//		Map<String,String> teaClsTeaMap = EntityUtils.getMap(clsList, "id","teacherId");
//		GkRounds round = gkRoundsService.findOne(roundsId);
//		CourseScheduleDto dto=new CourseScheduleDto();
//        dto.setAcadyear(round.getAcadyear());
//        dto.setSchoolId(unitId);
//        dto.setSemester(Integer.parseInt(round.getSemester()));
//        dto.setDayOfWeek1(round.getDayOfWeek());
//        dto.setWeekOfWorktime1(round.getWeekOfWorktime());
//        dto.setDayOfWeek2(round.getDayOfWeek2());
//        dto.setWeekOfWorktime2(round.getWeekOfWorktime2());
//        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
//        List<CourseSchedule> updatelist = new ArrayList<CourseSchedule>();
//        for(CourseSchedule c : crlist){
//        	if(teaClsIds.contains(c.getClassId())){
//        		if(!StringUtils.equals(teaClsTeaMap.get(c.getClassId()), c.getTeacherId())){
//        			c.setTeacherId(teaClsTeaMap.get(c.getClassId()));
//        			updatelist.add(c);
//        		}
//        	}
//		}
//        if(CollectionUtils.isNotEmpty(updatelist)){
//        	courseScheduleRemoteService.saveAll(SUtils.s(updatelist));
//        }
//	}
	
//	@Override
//	public void saveAllAndCourseSchedule(List<GkBatch> gkBatchs, String roundsId, String unitId) {
//		saveAll(gkBatchs.toArray(new GkBatch[0]));
//		//修改课表
//		Set<String> teaClsIds = EntityUtils.getSet(gkBatchs, "teachClassId");
//		Map<String,String> teaClsPlaceMap = EntityUtils.getMap(gkBatchs, "teachClassId","placeId");
//		GkRounds round = gkRoundsService.findOne(roundsId);
//		CourseScheduleDto dto=new CourseScheduleDto();
//        dto.setAcadyear(round.getAcadyear());
//        dto.setSchoolId(unitId);
//        dto.setSemester(Integer.parseInt(round.getSemester()));
//        dto.setDayOfWeek1(round.getDayOfWeek());
//        dto.setWeekOfWorktime1(round.getWeekOfWorktime());
//        dto.setDayOfWeek2(round.getDayOfWeek2());
//        dto.setWeekOfWorktime2(round.getWeekOfWorktime2());
//        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
//        List<CourseSchedule> updatelist = new ArrayList<CourseSchedule>();
//        for(CourseSchedule c : crlist){
//        	if(teaClsIds.contains(c.getClassId())){
//        		if(!StringUtils.equals(teaClsPlaceMap.get(c.getClassId()), c.getPlaceId())){
//        			c.setPlaceId(teaClsPlaceMap.get(c.getClassId()));
//        			updatelist.add(c);
//        		}
//        	}
//		}
//        if(CollectionUtils.isNotEmpty(updatelist)){
//        	courseScheduleRemoteService.saveAll(SUtils.s(updatelist));
//        }
//	}

	@Override
	public void deleteByTeaClsId(String schoolId,GkRounds gkRounds, String teaClsId,boolean flag) {
		if(StringUtils.isNotBlank(teaClsId)){
			List<GkBatch> batchList = gkBatchDao.findByClassIds(gkRounds.getId(), new String[]{teaClsId});
			Set<String> bathId=null;
			if(CollectionUtils.isNotEmpty(batchList)){
				bathId = EntityUtils.getSet(batchList, "id");
			}
			if(bathId!=null && bathId.size()>0){
				gkBatchDao.deleteByIds(bathId.toArray(new String[]{}));
			}
			teachClassRemoteService.deleteByIds(new String[]{teaClsId});
//			if(flag){
//				List<CourseSchedule> list=SUtils.dt(courseScheduleRemoteService.findByClassId(schoolId, gkRounds.getAcadyear(), Integer.parseInt(gkRounds.getSemester()), teaClsId,null),new TR<List<CourseSchedule>>(){});
//				if(CollectionUtils.isNotEmpty(list)){
//					Set<String> cId = EntityUtils.getSet(list, "id");
//					courseScheduleRemoteService.deleteCourseByIds(cId.toArray(new String[]{}));
//				}
//			}
			
		}
	}

	@Override
	public List<GkBatch> findByRoundsId(String[] roundsIds) {
		if(roundsIds==null || roundsIds.length<=0){
			return new ArrayList<GkBatch>();
		}
		return gkBatchDao.findByRoundsId(roundsIds);
	}

	@Override
	public void deleteOnlyByRoundsId(String id) {
		gkBatchDao.deleteByRoundsId(id);
		
	}

	@Override
	public void deleteById(String batchId) {
		//GkBatch findOne = gkBatchDao.findOne(batchId);
		GkBatch findOne = gkBatchDao.getOne(batchId);
		if(findOne == null){
			throw new ControllerException("该"+BaseConstants.PC_KC+"已被删除！");
		}
		gkBatchDao.delete(findOne);
		gkTeachClassStoreService.deleteByIds(new String[]{findOne.getTeachClassId()});
	}

}
