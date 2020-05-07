package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.*;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkClassBatchDao;
import net.zdsoft.newgkelective.data.dao.NewGkDivideClassDao;
import net.zdsoft.newgkelective.data.dao.NewGkDivideClassJdbcDao;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkDivideStusubService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeachUpStuLogService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

@Service("newGkDivideClassService")
public class NewGkDivideClassServiceImpl extends BaseServiceImpl<NewGkDivideClass, String> implements NewGkDivideClassService{

	@Autowired
	private NewGkDivideClassDao newGkDivideClassDao;
	@Autowired
	private NewGkDivideClassJdbcDao newGkDivideClassJdbcDao;
	@Autowired
	private NewGkClassStudentService newGkClassStudentService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkClassBatchDao newGkClassBatchDao;
	@Autowired
    private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkOpenSubjectService openSubjectService;
	@Autowired
	private NewGkArrayService arrayService;
	@Autowired
	private NewGkSubjectTimeService subjectTimeService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private NewGkDivideStusubService newGkDivideStusubService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkTeachUpStuLogService newGkTeachUpStuLogService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkDivideClass, String> getJpaDao() {
		return newGkDivideClassDao;
	}

	@Override
	protected Class<NewGkDivideClass> getEntityClass() {
		return NewGkDivideClass.class;
	}

	@Override
	public List<NewGkDivideClass> findByDivideIdAndClassType(String unitId,
			String divideId,String[] classTypes,boolean isMakeStudent, String sourceType, boolean containChildren) {
		List<NewGkDivideClass> list = newGkDivideClassJdbcDao.findByDivideIdAndClassType(divideId, sourceType, classTypes, containChildren);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<NewGkDivideClass>();
		}
		if(isMakeStudent){
			toMakeStudentList(unitId,divideId, list);
		}
		return list;
	}
	
	public List<NewGkDivideClass> findByRelateIdsWithMaster(String[] relateIds){
		if(ArrayUtils.isEmpty(relateIds)) {
			return new ArrayList<NewGkDivideClass>();
		}
		Specification<NewGkDivideClass> specification = new Specification<NewGkDivideClass>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkDivideClass> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(root.get("relateId").in((String[]) relateIds));
				return cq.where(cb.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		};
		return newGkDivideClassDao.findAll(specification);
	}

    @Override
    public List<NewGkDivideClass> findByDivideIdAndClassTypeAndSubjectIds(String unitId, String divideId, String classType, String subjectIds, boolean isMakeStudent) {
        List<NewGkDivideClass> list = newGkDivideClassDao.findByDivideIdAndClassTypeAndSubjectIds(divideId, classType, subjectIds);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        if(isMakeStudent){
            toMakeStudentList(unitId, divideId, list);
        }
        return list;
    }

    public List<NewGkDivideClass> findByDivideIdAndClassTypeSubjectType(String unitId,
			String divideId, String[] classTypes, boolean isMakeStudent, String sourceType, String subjectType){
		if(StringUtils.isEmpty(subjectType)) {
			return findByDivideIdAndClassType(unitId, divideId, classTypes, isMakeStudent, sourceType, false);
		}
		Specification<NewGkDivideClass> specification = new Specification<NewGkDivideClass>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkDivideClass> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb
						.equal(root.get("divideId").as(String.class), divideId));
				if(ArrayUtils.isNotEmpty(classTypes)){
					ps.add(root.get("classType").in((String[]) classTypes));
				}
				ps.add(cb
						.equal(root.get("sourceType").as(String.class), sourceType));
				ps.add(cb
						.equal(root.get("subjectType").as(String.class), subjectType));
				cq.where(cb.and(ps.toArray(new Predicate[0])));
				//order by classType,subjectIds,bestType,orderId,className"
				cq.orderBy(cb.asc(root.get("classType").as(String.class)));  
				cq.orderBy(cb.asc(root.get("subjectIds").as(String.class))); 
				cq.orderBy(cb.asc(root.get("bestType").as(String.class)));  
				cq.orderBy(cb.asc(root.get("orderId").as(String.class)));  
				cq.orderBy(cb.asc(root.get("className").as(String.class)));  
				 
				return cq.getRestriction();
			}
		};
		List<NewGkDivideClass> list = newGkDivideClassDao.findAll(specification);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<NewGkDivideClass>();
		}
		if(isMakeStudent){
			toMakeStudentList(unitId,divideId, list);
		}
		return list;
	}
	
	@Override
	public List<NewGkDivideClass> findByGradeId(String gradeId,String sourceType){
		return RedisUtils.getObject("NEW_GK_DIVIDE_CLASS_BY_"+gradeId, RedisUtils.TIME_SHORT_CACHE, new TypeReference<List<NewGkDivideClass>>(){}, new RedisInterface<List<NewGkDivideClass>>(){
			@Override
			public List<NewGkDivideClass> queryData() {
				return newGkDivideClassDao.findByGradeId(gradeId,sourceType);
			}
        });
	}
	
	@Override
	public List<NewGkDivideClass> findByDivideIdAndSourceType(String divideId,String sourceType,boolean isRedis){
		if(isRedis){
			return RedisUtils.getObject("NEW_GK_DIVIDE_CLASS_BY_"+divideId, RedisUtils.TIME_SHORT_CACHE, new TypeReference<List<NewGkDivideClass>>(){}, new RedisInterface<List<NewGkDivideClass>>(){
				@Override
				public List<NewGkDivideClass> queryData() {
					return newGkDivideClassDao.findByDivideIdAndSourceTypeOrderByClassName(divideId,sourceType);
				}
	        });
		}else{
			return newGkDivideClassDao.findByDivideIdAndSourceTypeOrderByClassName(divideId,sourceType);
		}
		
	}
	
	public void toMakeStudentList(String unitId,String divideId, List<NewGkDivideClass> list) {
		Set<String> ids = EntityUtils.getSet(list, NewGkDivideClass::getId);
		Map<String,List<String>> map=newGkClassStudentService.findMapByClassIds(unitId,divideId, ids.toArray(new String[]{}));
		for(NewGkDivideClass divideClass:list){
			if(map.containsKey(divideClass.getId())){
				divideClass.setStudentList(map.get(divideClass.getId()));
				divideClass.setStudentCount(divideClass.getStudentList().size());
			}else {
				divideClass.setStudentCount(0);
			}
		}
	}

	@Override
	public void saveAllList(String unitId,String divideId,
			String[] delClassId, List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList, boolean isStat) {
		if(delClassId!=null && delClassId.length>0){
			deleteByClassIdIn(unitId,divideId, delClassId);
		}
		if(CollectionUtils.isNotEmpty(insertClassList)){
			newGkDivideClassDao.saveAll(checkSave(insertClassList.toArray(new NewGkDivideClass[]{})));
			//newGkDivideClassJdbcDao.insertBatch(checkSave(insertClassList.toArray(new NewGkDivideClass[]{})));
		}
		if(CollectionUtils.isNotEmpty(insertStudentList)){
			newGkClassStudentService.saveAllList(insertStudentList);
		}
		if(StringUtils.isNotBlank(divideId) && isStat){
			newGkDivideService.updateStat(divideId,NewGkElectiveConstant.IF_1);
		}
		
	}

	@Override
    public void saveAllSplit(String unitId, String divideId, String spllitSubjectId, List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList, List<NewGkChoRelation> newGkChoRelationList) {
        Set<String> parentIds;
        Set<String> oldSplitClassIds = new HashSet<>();
	    if (CollectionUtils.isNotEmpty(insertClassList)) {
            parentIds = EntityUtils.getSet(insertClassList, NewGkDivideClass::getParentId);
            oldSplitClassIds = EntityUtils.getSet(newGkDivideClassDao.findByParentIdIn(parentIds.toArray(new String[0])), NewGkDivideClass::getId);
            newGkDivideClassDao.deleteByParentIdIn(parentIds.toArray(new String[0]));
            newGkDivideClassDao.saveAll(checkSave(insertClassList.toArray(new NewGkDivideClass[]{})));
        }
        if (CollectionUtils.isNotEmpty(insertStudentList)) {
            newGkClassStudentService.deleteByClassIdIn(unitId, divideId, oldSplitClassIds.toArray(new String[0]));
            newGkClassStudentService.saveAllList(insertStudentList);
        }
        if (CollectionUtils.isNotEmpty(newGkChoRelationList)) {
            newGkChoRelationService.deleteByObjectTypeVal(divideId, spllitSubjectId);
            newGkChoRelationService.saveAll(newGkChoRelationList.toArray(new NewGkChoRelation[0]));
        }
    }

	@Override
	public void deleteByClassIdIn(String unitId, String divideId, String[] delClassId) {
		newGkClassStudentService.deleteByClassIdIn(unitId, divideId, delClassId);
		if(delClassId!=null && delClassId.length>0){
			if(delClassId.length > 1000){
				String[] delUnitIds = new String[1000];
				int j = 0;
				for(int i=0;i<delClassId.length;i++){
					delUnitIds[j++] = delClassId[i];
					if(j >= 1000 || i >= (delClassId.length-1)){
						newGkDivideClassJdbcDao.deleteByIdInOrDivideId(delUnitIds,null);
						delUnitIds = new String[1000];
						if(delClassId.length - 1 - i < 1000){
							delUnitIds = new String[delClassId.length - 1 - i];
						}
						j = 0;
					}
				}
				if(j != 0){
					System.out.println("--------删除出错----------");
				}
				
			}else{
				newGkDivideClassJdbcDao.deleteByIdInOrDivideId(delClassId,null);
			}
			
			//newgkelective_class_bacth
			newGkClassBatchDao.deleteByDivideIdAndDivideClassIdIn(divideId, delClassId);
		}else {
			newGkClassBatchDao.deleteByDivideId(divideId);
		}
		// 拆分后班级删除
        newGkDivideClassDao.deleteByParentIdIn(delClassId);
	}
	@Override
	public String saveByHand(String unitId, NewGkDivideClass newGkDivideClass){
		String classType=newGkDivideClass.getClassType();
		String divideId=newGkDivideClass.getDivideId();
		NewGkDivideClass divideClass=null;
		List<NewGkDivideClass> insertList=new ArrayList<NewGkDivideClass>();
		//查已有的对应的尖子班
		List<NewGkDivideClass> haveClassList=findByDivideIdAndClassType(unitId, divideId, new String[]{classType},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){//行政班
			int  size=0;
			if(CollectionUtils.isNotEmpty(haveClassList)){
				String className=haveClassList.get(haveClassList.size()-1).getClassName();
				size=className==null?0:Integer.parseInt(className.substring(1, 2));
			}
			int classNum=newGkDivideClass.getClassNum();
			for(int i=1;i<=classNum;i++){
				divideClass=new NewGkDivideClass();
				divideClass.setId(UuidUtils.generateUuid());
				divideClass.setDivideId(divideId);
				divideClass.setClassName((size+i)+"班");
				divideClass.setClassType(classType);
				divideClass.setRelateId("");
				divideClass.setSubjectIds("");
				divideClass.setSubjectType("");
				divideClass.setBestType(NewGkElectiveConstant.BEST_TYPE_1);
				divideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
				divideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
				insertList.add(divideClass);
			}
		}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(classType)){//3科组合
			Map<String,List<NewGkDivideClass>> sizeMap=new HashMap<String,List<NewGkDivideClass>>();
			for(NewGkDivideClass haveClass:haveClassList){
				List<NewGkDivideClass> inList=sizeMap.get(haveClass.getSubjectIds());
				if(CollectionUtils.isEmpty(inList)){
					inList=new ArrayList<NewGkDivideClass>();
				}
				inList.add(haveClass);
				sizeMap.put(haveClass.getSubjectIds(), inList);
			}
			List<NewGkConditionDto> newDtoList=newGkDivideClass.getNewDtoList();
			if(CollectionUtils.isNotEmpty(newDtoList)){
				for(NewGkConditionDto newDto:newDtoList){
					Integer classNum=newDto.getClassNum();
					if(classNum==null || classNum==0){
						continue;
					}
					List<NewGkDivideClass> inList=sizeMap.get(newDto.getSubjectIdstr());
					int  size=0;
					if(CollectionUtils.isNotEmpty(inList)){
						inList.get(inList.size()-1);
					    size=Integer.parseInt((inList.get(inList.size()-1)).getClassName().substring(3,4));
					}
					for(int i=1;i<=classNum;i++){
						divideClass=new NewGkDivideClass();
						divideClass.setId(UuidUtils.generateUuid());
						divideClass.setDivideId(divideId);
						divideClass.setClassName(newDto.getSubShortNames()+(size+i)+"班");
						divideClass.setClassType(classType);
						divideClass.setRelateId("");
						divideClass.setSubjectIds(newDto.getSubjectIdstr());
						divideClass.setSubjectType("");
						divideClass.setBestType(NewGkElectiveConstant.BEST_TYPE_1);
						divideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
						divideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
						insertList.add(divideClass);
					}
				}
			}
		}
		newGkDivideClassDao.saveAll(checkSave(insertList.toArray(new NewGkDivideClass[]{})));
		//newGkDivideClassJdbcDao.insertBatch(checkSave(insertList.toArray(new NewGkDivideClass[0])));
		if(insertList.size()>0){
			return insertList.get(0).getId();
		}
		return "";
	}

	@Override
	public void deleteById(String unitId, String divideId, String divideClassId) {
		if(StringUtils.isNotBlank(divideClassId)){
			 newGkClassStudentService.deleteByClassIdIn(unitId, divideId, new String[]{divideClassId});
			 newGkDivideClassJdbcDao.deleteByIdInOrDivideId(new String[]{divideClassId}, null);
		}
	}

	@Override
	public void makeStuNum(String unitId,String divideId, List<NewGkDivideClass> divideClassList) {
		if(CollectionUtils.isNotEmpty(divideClassList)){
			Set<String> ids = EntityUtils.getSet(divideClassList, NewGkDivideClass::getId);
			Map<String, List<String>> map = newGkClassStudentService.findMapByClassIds(unitId,divideId, ids.toArray(new String[]{}));
			for(NewGkDivideClass item:divideClassList){
				if(map.containsKey(item.getId())){
					item.setStudentCount(map.get(item.getId()).size());
				}
			}
		}
	}

	@Override
	public Map<String, String> findByArrayIdMap(String arrayId) {
		return RedisUtils.getObject("NEW_GK_DIVIDE_CLASS_MAP_BY_"+arrayId, RedisUtils.TIME_SHORT_CACHE, new TypeReference<Map<String, String>>(){}, new RedisInterface<Map<String, String>>(){
			@Override
			public Map<String, String> queryData() {
				List<Object[]>list = newGkDivideClassDao.findByArrayIdMap(arrayId);
				Map<String, String> map =new HashMap<String, String>();
				if(CollectionUtils.isNotEmpty(list)){
					for(Object[] sts:list){
						map.put((String)sts[0], (String)sts[1]);
					}
				}
				return map;
			}
        });
		
		
	}

	@Override
	public List<NewGkDivideClass> findClassBySubjectIds(String unitId,
			String divideId, String sourceType, String classType,String subjectIds, boolean isMakeStu) {
		List<NewGkDivideClass> list=new ArrayList<NewGkDivideClass>();
		if(StringUtils.isNotBlank(subjectIds)){
			list = newGkDivideClassDao.findClassBySubjectIds(divideId,sourceType,classType,subjectIds);
		}else{
			list = findByDivideIdAndClassType(unitId, divideId, new String[]{classType}, false, sourceType, false);
		}
		
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<NewGkDivideClass>();
		}else{
			if(isMakeStu){
				toMakeStudentList(unitId,divideId, list);
			}
			return list;
		}
	}

	@Override
	public NewGkDivideClass findById(String unitId, String divideClassId, boolean isMakeStu) {
		NewGkDivideClass newGkDivideClass=findOne(divideClassId);
		if(newGkDivideClass!=null){
			if(isMakeStu){
				List<NewGkDivideClass> list = new ArrayList<NewGkDivideClass>();
				list.add(newGkDivideClass);
				toMakeStudentList(unitId,newGkDivideClass.getDivideId(), list);
			}
		}
		return newGkDivideClass;
	}

	@Override
	public void updateStu(String unitId,
			String divideId,String[] divideClassIds, List<NewGkDivideClass> updateList) {
		Set<String> delClassId=new HashSet<String>();
		Set<String> delClassStuId=new HashSet<String>();
		if(ArrayUtils.isNotEmpty(divideClassIds)){
			List<String> list = Arrays.asList(divideClassIds);
			delClassId.addAll(list);
			delClassStuId.addAll(list);
		}
		List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
		NewGkClassStudent ss;
		if(CollectionUtils.isNotEmpty(updateList)){
			for(NewGkDivideClass cc:updateList){
				for(String studentId:cc.getStudentList()){
					ss=new NewGkClassStudent();
					ss.setId(UuidUtils.generateUuid());
					ss.setCreationTime(new Date());
					ss.setModifyTime(new Date());
					ss.setStudentId(studentId);
					ss.setClassId(cc.getId());
					ss.setDivideId(cc.getDivideId());
					ss.setUnitId(unitId);
					insertStuList.add(ss);
				}
				delClassStuId.add(cc.getId());
				
			}
		}
		if(CollectionUtils.isNotEmpty(delClassId)){
			deleteOnlyClass(delClassId.toArray(new String[]{}));
		}
		if(CollectionUtils.isNotEmpty(delClassStuId)){
			newGkClassStudentService.deleteByClassIdIn(unitId, divideId, delClassStuId.toArray(new String[]{}));
		}
		if(CollectionUtils.isNotEmpty(insertStuList)){
			newGkClassStudentService.saveAllList(insertStuList);
		}
	}
	
	private void deleteOnlyClass(String[] classIds){
		if(ArrayUtils.isEmpty(classIds)){
			return;
		}
		if(classIds.length<=1000){
			newGkDivideClassJdbcDao.deleteByIdInOrDivideId(classIds,null);
		}else{
			int cyc = classIds.length / 1000 + (classIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > classIds.length)
					max = classIds.length;
				String[] classIdsArr = ArrayUtils.subarray(classIds, i * 1000, max);
				newGkDivideClassJdbcDao.deleteByIdInOrDivideId(classIdsArr,null);
			}
		}
	}

	@Override
	public List<NewGkDivideClass> findByDivideIdIn(String[] divideIds) {
		if(ArrayUtils.isEmpty(divideIds)){
			return new ArrayList<NewGkDivideClass>();
		}
		return newGkDivideClassDao.findByDivideIdIn(divideIds);
	}

	@Override
	public void saveChangeByStuId(String arrayId, String stuId,
			String[] chooseClass,String unitId) {
		List<NewGkDivideClass> chooselist = findListByIdIn(chooseClass);
		boolean isNeedDelXZB=false;
		for(NewGkDivideClass cc:chooselist){
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(cc.getClassType())){
				//组合数据
				isNeedDelXZB=true;
				break;
			}
		}
		String[] classType=null;
		if(isNeedDelXZB){
			classType=new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2};
		}else{
			classType=new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2};
		}
		List<NewGkClassStudent> oldList = newGkClassStudentService.findListByDivideStudentId(arrayId,classType , new String[]{stuId}, NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		
		List<NewGkClassStudent> newList=new ArrayList<NewGkClassStudent>();
		NewGkClassStudent newGkClassStudent;
		for(String clazzId:chooseClass){
			newGkClassStudent=new NewGkClassStudent();
			newGkClassStudent.setId(UuidUtils.generateUuid());
			newGkClassStudent.setStudentId(stuId);
			newGkClassStudent.setClassId(clazzId);
			newGkClassStudent.setCreationTime(new Date());
			newGkClassStudent.setDivideId(arrayId);
			newGkClassStudent.setUnitId(unitId);
			newList.add(newGkClassStudent);
		}
		String[] idarr=null;
		if(CollectionUtils.isNotEmpty(oldList)){
			Set<String> ids = EntityUtils.getSet(oldList, NewGkClassStudent::getId);
			idarr = ids.toArray(new String[]{});
		}
		NewGkClassStudent[] addArr=null;
		if(CollectionUtils.isNotEmpty(newList)){
			addArr=newList.toArray(new NewGkClassStudent[]{});
		}
		newGkClassStudentService.saveAndDel(idarr, addArr);
	}

	@Override
	public List<NewGkDivideClass> findByBatch(String divideClassId) {
		NewGkDivideClass d = findOne(divideClassId);
		return newGkDivideClassDao.findBySameBatchClassList(
				d.getDivideId(),d.getSourceType(),d.getClassType(),d.getSubjectIds(),d.getSubjectType(),d.getBatch());
	}

	@Override
	public Map<String, Integer> findCountByDivideIdAndClassType(
			String[] divideIds, String[] classTypes) {
		Map<String, Integer> map=new HashMap<String, Integer>();
		List<Object[]> list =new ArrayList<Object[]>();
		if(divideIds!=null && divideIds.length>0){
			if(divideIds.length<=1000){
				list = newGkDivideClassDao.findCountByDivideIdAndClassType(divideIds, classTypes);
			}else{
				int cyc = divideIds.length / 1000 + (divideIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > divideIds.length)
						max = divideIds.length;
					String[] arrId = ArrayUtils.subarray(divideIds, i * 1000, max);
					List<Object[]> list1 = newGkDivideClassDao.findCountByDivideIdAndClassType(arrId, classTypes);
					if(CollectionUtils.isNotEmpty(list1)){
						list.addAll(list1);
					}
				}
			}
			
		}
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] objArr:list){
				Integer sum = Integer.valueOf("" + objArr[0]);
				String divideId = objArr[1].toString();
				String classType = objArr[2].toString();
				String subjectType="";
				if(objArr[3]!=null){
					subjectType = objArr[3].toString();
				}
				
				String key=divideId+"_"+classType+"_"+subjectType;
				map.put(key, sum);
			}
		}

		return map;
	}

	@Override
	public Map<String, Integer> findCountByDivideIdAndSubjectType(
			String[] divideIds) {
		Map<String, Integer> map=new HashMap<String, Integer>();
		List<Object[]> list =new ArrayList<Object[]>();
		if(divideIds!=null && divideIds.length>0){
			if(divideIds.length<=1000){
				list = newGkDivideClassDao.findCountByDivideIdAndSubjectType(divideIds);
			}else{
				int cyc = divideIds.length / 1000 + (divideIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > divideIds.length)
						max = divideIds.length;
					String[] arrId = ArrayUtils.subarray(divideIds, i * 1000, max);
					List<Object[]> list1 = newGkDivideClassDao.findCountByDivideIdAndSubjectType(arrId);
					if(CollectionUtils.isNotEmpty(list1)){
						list.addAll(list1);
					}
				}
			}
			
		}
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] objArr:list){
				Integer sum = Integer.valueOf("" + objArr[0]);
				String divideId = objArr[1].toString();
				String subjectId = objArr[2].toString();
				String subjectType="";
				if(objArr[3]!=null){
					subjectType = objArr[3].toString();
				}else {
					continue;
				}
				String bestType = "";
				if(objArr[4]!=null){
					bestType = objArr[4].toString();
				}
				String key=divideId+"_"+subjectId+"_"+subjectType+"_"+bestType;
				map.put(key, sum);
			}
		}
		return map;
	}

	@Override
	public void updateMoveStudents(String oldClassId, String[] stuIds, List<NewGkDivideClass> list,
			List<NewGkClassStudent> makeStudents) {
		if(StringUtils.isNotBlank(oldClassId)) {
			newGkClassStudentService.deleteByClassIdAndStuIdIn(oldClassId, stuIds);
		}
		saveAllList(null, null, null, list, makeStudents, false);
	}

	@Override
	public List<NewGkDivideClass> findListByDivideId(String divideId,String sourceType, String classType, String subjectId,
			String subjectType ) {
		return newGkDivideClassDao.findClassBySubjectIdsAndType(divideId, sourceType, classType, subjectId,subjectType);
	}

	@Override
	public void deleteByDivideId(String unitId, String divideId) {
		newGkClassStudentService.deleteByDivideId(unitId, divideId);
		//newgkelective_class_batch(班级批次详情表)
		newGkClassBatchDao.deleteByDivideId(divideId);
		newGkDivideClassJdbcDao.deleteByIdInOrDivideId(null,divideId);
	}

	@Override
	public List<NewGkDivideClass> findListByRelateId(String divideId, String sourceType,String classType, String[] relateIds) {
		return newGkDivideClassDao.findListByRelateId(divideId,sourceType,classType,relateIds);
	}

	@Override
	public List<String> findByClassTypeAndTime(String arrayId, NewGkTimetableOther timeInf,
			String[] classTypes) {
		if(StringUtils.isBlank(arrayId)) {
			return new ArrayList<>();
		}
		if(classTypes == null || classTypes.length ==0) {
			return new ArrayList<>();
		}
		if(timeInf == null || timeInf.getDayOfWeek() == null 
				|| StringUtils.isBlank(timeInf.getPeriodInterval())
				|| timeInf.getPeriod() == null) {
			return new ArrayList<>();
		}
		
		List<String> cl = newGkDivideClassDao.findByClassTypeAndTime(arrayId,timeInf.getDayOfWeek(), 
				timeInf.getPeriodInterval(),timeInf.getPeriod(),classTypes);
		return cl;
	}

	@Override
	public void saveAllList(List<NewGkDivide> insertDivideList, List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStudentList) {
		if(CollectionUtils.isNotEmpty(insertDivideList)){
			newGkDivideService.saveAll(insertDivideList.toArray(new NewGkDivide[insertDivideList.size()]));
		}
		if(CollectionUtils.isNotEmpty(insertClassList)){
			this.saveAll(insertClassList.toArray(new NewGkDivideClass[insertClassList.size()]));
		}
		if(CollectionUtils.isNotEmpty(insertStudentList)){
			newGkClassStudentService.saveAll(insertStudentList.toArray(new NewGkClassStudent[insertStudentList.size()]));
		}
	}

    // Basedata Sync Method
    @Override
    public void deleteBySubjectIds(String... subjectIds) {
    	for(String subid:subjectIds){
    		newGkDivideClassDao.deleteBySubjectIdsLike("%" + subid + "%");
    	}
    }

    // Basedata Sync Method
    @Override
    public void deleteByClassIds(String... classIds) {
        newGkDivideClassDao.deleteByOldClassIdIn(classIds);
        newGkDivideClassDao.deleteByIdIn(classIds);
    }

    @Override
    public List<NewGkDivideClass> findByParentIds(String[] parentIds) {
        return newGkDivideClassDao.findByParentIdIn(parentIds);
    }

    @Override
	public List<NewGkDivideClass> findByDivideIdAndClassTypeWithMaster(String unitId, String divideId,
			String[] classTypes, boolean isMakeStudent, String sourceType, boolean containChildren) {
		return findByDivideIdAndClassType(unitId, divideId, classTypes, isMakeStudent, sourceType, containChildren);
	}

	@Override
	public List<NewGkDivideClass> findClassBySubjectIdsWithMaster(String unitId, String divideId, String sourceType,
			String classType, String subjectIds, boolean isMakeStu) {
		return findClassBySubjectIds(unitId, divideId, sourceType, classType, subjectIds, isMakeStu);
	}

	@Override
	public NewGkDivideClass findByIdWithMaster(String unitId, String divideClassId, boolean isMakeStu) {
		return findById(unitId, divideClassId, isMakeStu);
	}

	@Override
	public List<NewGkDivideClass> findByDivideIdAndClassTypeSubjectTypeWithMaster(String unitId, String divideId,
			String[] classTypes, boolean isMakeStudent, String sourceType, String subjectType) {
		return findByDivideIdAndClassTypeSubjectType(unitId, divideId, classTypes, isMakeStudent, sourceType, subjectType);
	}

	@Override
	public List<NewGkDivideClass> findListByDivideIdWithMaster(String divideId, String sourceType, String classType,
			String subjectId, String subjectType) {
		
		return findListByDivideId(divideId, sourceType, classType, subjectId, subjectType);
	}

	@Override
	public long countByDivideId(String divideId, String classType, String classSourceType) {
		Long num = (Long)newGkDivideClassDao.countByDivideId(divideId,classType,classSourceType);
		
		return num==null?0:num;
	}

	@Override
	public void saveStuList(String groupClassId, List<NewGkClassStudent> insertStudentList) {
		newGkClassStudentService.deleteByClassIds(new String[]{groupClassId});
		if(CollectionUtils.isNotEmpty(insertStudentList)) {
			newGkClassStudentService.saveAll(insertStudentList.toArray(new NewGkClassStudent[0]));
		}
	}

	@Override
	public List<NewGkDivideClass> findByDivideIdAndClassTypeAndSubjectIdsWithMaster(String unitId, String divideId,
			String classType, String subjectIds, boolean isMakeStudent) {
		return findByDivideIdAndClassTypeAndSubjectIds(unitId, divideId, classType, subjectIds, isMakeStudent);
	}

	@Override
	public void saveClassOrDel(String unitId,String divideId,List<NewGkDivideClass> updateList, String[] ids,String[] delClasstuIds) {
		if(ids!=null) {
			newGkClassStudentService.deleteByClassIdIn(unitId, divideId, ids);
			newGkDivideClassDao.deleteByIdIn(ids);
		}
		if(delClasstuIds!=null) {
			newGkClassStudentService.deleteByIds(delClasstuIds);
		}
		if(CollectionUtils.isNotEmpty(updateList)) {
			saveAll(updateList.toArray(new NewGkDivideClass[0]));
		}
	}

	@Override
	public void deleteByClassIdIn2(String unitId, String divideId, String[] classIds) {
		newGkClassStudentService.deleteByClassIdIn(unitId, divideId, classIds);
		if(classIds!=null && classIds.length>0){
			if(classIds.length > 1000){
				String[] delUnitIds = new String[1000];
				int j = 0;
				for(int i=0;i<classIds.length;i++){
					delUnitIds[j++] = classIds[i];
					if(j >= 1000 || i >= (classIds.length-1)){
						newGkDivideClassJdbcDao.deleteByIdInOrDivideId(delUnitIds,null);
						delUnitIds = new String[1000];
						if(classIds.length - 1 - i < 1000){
							delUnitIds = new String[classIds.length - 1 - i];
						}
						j = 0;
					}
				}
				if(j != 0){
					System.out.println("--------删除出错----------");
				}
				
			}else{
				newGkDivideClassJdbcDao.deleteByIdInOrDivideId(classIds,null);
			}
			
		}

		
	}

	@Override
	public void savejxbBySubjectType(String unitId,String divideId, String subjectType, List<NewGkDivideClass> insertClassList,
			List<NewGkClassStudent> insertStudentList) {
		List<NewGkDivideClass> allClassList =findByDivideIdAndClassTypeSubjectType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, subjectType);
		List<String> jxbIds = EntityUtils.getList(allClassList, e->e.getId());
		
		if(CollectionUtils.isNotEmpty(jxbIds)) {
			deleteByClassIdIn(unitId, divideId, jxbIds.toArray(new String[0]));
		}
		
		if(CollectionUtils.isNotEmpty(insertClassList)){
			this.saveAll(insertClassList.toArray(new NewGkDivideClass[insertClassList.size()]));
		}
		if(CollectionUtils.isNotEmpty(insertStudentList)){
			newGkClassStudentService.saveAll(insertStudentList.toArray(new NewGkClassStudent[insertStudentList.size()]));
		}
	}
	
	@Override
	public Map<String,List<String[]>> findXzbSubjects(String unitId, String divideId, String lessonArrangeId, String sourceType, List<String> xzbIds){
		Map<String,List<String[]>> xzbSubMap = new HashMap<>();
		if(CollectionUtils.isEmpty(xzbIds)) {
			return xzbSubMap;
		}
		List<NewGkDivideClass> xzbList = this.findByIdIn(xzbIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(xzbList)) {
			return xzbSubMap;
		}
		
		NewGkDivide realDivide = newGkDivideService.findOne(divideId);
		if(NewGkElectiveConstant.CLASS_SOURCE_TYPE1.equals(sourceType)) {
			realDivide = newGkDivideService.findOne(divideId);
		}else if(NewGkElectiveConstant.CLASS_SOURCE_TYPE2.equals(sourceType)) {
			NewGkArray array = arrayService.findOne(divideId);
			realDivide = newGkDivideService.findOne(array.getDivideId());
		}
		List<NewGkSubjectTime> subjectTimeList = subjectTimeService.findByArrayItemId(lessonArrangeId);
		Set<String> allSubIdTypes = new HashSet<>();
		Set<String> aSubjectIds = new HashSet<>();
		Set<String> bSubjectIds = new HashSet<>();
		List<String[]> xzbSubs = new ArrayList<>();
		for (NewGkSubjectTime st : subjectTimeList) {
			allSubIdTypes.add(st.getSubjectId()+"-"+st.getSubjectType());
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(st.getSubjectType())) {
				aSubjectIds.add(st.getSubjectId());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(st.getSubjectType())) {
				bSubjectIds.add(st.getSubjectId());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(st.getSubjectType())) {
				if(Objects.equals(st.getFollowZhb(),NewGkElectiveConstant.IF_INT_1) && (NewGkElectiveConstant.DIVIDE_TYPE_10.equals(realDivide.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(realDivide.getOpenType()))){
					continue;
				}
				xzbSubs.add(new String[] {st.getSubjectId(),st.getSubjectType()});
			}
		}
		
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(realDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(realDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(realDivide.getOpenType())) {
			xzbList.forEach(e->xzbSubMap.put(e.getId(), xzbSubs));
			return xzbSubMap;
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(realDivide.getOpenType())) {
			// 单科分层重组 固定组合 重组
			// 物理 历史
			List<Course> course2List = SUtils.dt(courseRemoteService.findWuliLiShi(unitId), Course.class);
			List<String> courseId2List = EntityUtils.getList(course2List, Course::getId);
			
			Map<String,String> zhbIdMap = EntityUtils.getMap(xzbList, NewGkDivideClass::getRelateId,NewGkDivideClass::getId);
			List<NewGkDivideClass> zhbList = this.findByIdIn(zhbIdMap.keySet().toArray(new String[0]));
			for (NewGkDivideClass zhb : zhbList) {
				String subjectIds = zhb.getSubjectIds();
				String xzbId = zhbIdMap.get(zhb.getId());
				List<String[]> list = new ArrayList<>(xzbSubs);
				String[] split = subjectIds.split(",");
				if(split.length==1) {
					if(aSubjectIds.contains(subjectIds)) {
						list.add(new String[] {subjectIds,NewGkElectiveConstant.SUBJECT_TYPE_A});
					}
					String subId2 = subjectIds.equals(courseId2List.get(0))?courseId2List.get(1):courseId2List.get(0);
					if(bSubjectIds.contains(subId2)) {
						list.add(new String[] {subId2,NewGkElectiveConstant.SUBJECT_TYPE_B});
					}
				}else if(split.length==3) {
					List<String> subIds = Arrays.asList(split);
					aSubjectIds.stream().filter(e->subIds.contains(e)).forEach(e->list.add(new String[] {e,NewGkElectiveConstant.SUBJECT_TYPE_A}));
					bSubjectIds.stream().filter(e->!subIds.contains(e)).forEach(e->list.add(new String[] {e,NewGkElectiveConstant.SUBJECT_TYPE_B}));
				}
				xzbSubMap.put(xzbId, list);
			}
			return xzbSubMap;
		}
		
		Set<String> zhbIds = EntityUtils.getSet(xzbList, NewGkDivideClass::getRelateId);
		List<NewGkDivideClass> zhbList = this.findByIdIn(zhbIds.toArray(new String[0]));
		List<NewGkDivideClass> jxbList = this.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false,
				sourceType, true);
		// 如果divideId 是 arrayId? 班级为空时
//		String realDivideId = divideId;
//		if(NewGkElectiveConstant.CLASS_SOURCE_TYPE2.equals(sourceType)) {
//			NewGkArray array = arrayService.findOne(divideId);
//			realDivideId = array.getDivideId();
//		}
		//TODO 
		
		
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbList, NewGkDivideClass::getId);
		Map<String, List<NewGkDivideClass>> zhbToJxbMap = jxbList.stream()
				.filter(e->StringUtils.isNotBlank(e.getRelateId()))
				.collect(Collectors.groupingBy(NewGkDivideClass::getRelateId));
		
		Map<String, NewGkDivideClass> jxbMap = EntityUtils.getMap(jxbList, NewGkDivideClass::getId);
		Map<String, Set<String>> relaSubjmap = jxbList.stream()
				.filter(e->StringUtils.isNotBlank(e.getParentId()) && jxbMap.containsKey(e.getParentId()))
				.collect(Collectors.groupingBy(e->jxbMap.get(e.getParentId()).getSubjectIds()+"-"+jxbMap.get(e.getParentId()).getSubjectType(),
						Collectors.mapping(c->c.getSubjectIds()+"-"+c.getSubjectType(), Collectors.toSet())));
		Set<String> parentIds = jxbList.stream()
				.filter(e->StringUtils.isNotBlank(e.getParentId()))
				.map(e->e.getParentId())
				.collect(Collectors.toSet());
		
		
		boolean pure3 = false;
		for (NewGkDivideClass xzb : xzbList) {
			Set<String> xzbSubIds = new HashSet<>();
			if(StringUtils.isNotBlank(xzb.getRelateId())) {
				NewGkDivideClass zhbClass = zhbMap.get(xzb.getRelateId());
				String[] subjectIdsT = zhbClass.getSubjectIds().split(",");
				pure3 = false;
				if(!BaseConstants.ZERO_GUID.equals(subjectIdsT[0])) {  // 32个0 代表混合班
					// 不是混合班
					xzbSubIds = Arrays.stream(subjectIdsT).filter(e->aSubjectIds.contains(e))
							.map(e->e+"-A").collect(Collectors.toSet());
					if(subjectIdsT.length > 2) {
						// 3科组合班
						pure3 = true;
						
						xzbSubIds.addAll(bSubjectIds.stream().filter(e -> !Arrays.asList(subjectIdsT).contains(e))
								.map(e -> e + "-B").collect(Collectors.toSet()));
					}
				}
				if(!pure3 && StringUtils.isNotBlank(zhbClass.getSubjectIdsB())) { // 不是3科组合班，都要检查是否存在 学考科目在行政班上课
					String[] subIdsB = zhbClass.getSubjectIdsB().split(",");
					xzbSubIds.addAll(bSubjectIds.stream().filter(e -> Arrays.asList(subIdsB).contains(e))
							.map(e -> e + "-B").collect(Collectors.toSet()));
				}
				if(CollectionUtils.isNotEmpty(zhbToJxbMap.get(zhbClass.getId()))) {
					// 合成班1
					List<NewGkDivideClass> list = zhbToJxbMap.get(zhbClass.getId());
					// 纯组合班 或者 2+X 组合班 的部分科目已经合班 不再作为行政班 上课
					if(zhbClass.getSubjectIdsB() == null) zhbClass.setSubjectIdsB("");
					for (NewGkDivideClass clz : list) {
						if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhbClass.getSubjectType())) {
							xzbSubIds.remove(clz.getSubjectIds()+"-"+clz.getSubjectType());
						}else if(zhbClass.getSubjectIds().contains(clz.getSubjectIds()) 
								|| zhbClass.getSubjectIdsB().contains(clz.getSubjectIds())) {
							xzbSubIds.remove(clz.getSubjectIds()+"-"+clz.getSubjectType());
						}
					}
				}
				// 如果有拆分的科目 ，替换为被拆分的科目
				if(parentIds.size()>0) {
					List<String> collect2 = xzbSubIds.stream().filter(x->relaSubjmap.containsKey(x))
						.collect(Collectors.toList());
					if(CollectionUtils.isNotEmpty(collect2)) {
						xzbSubIds.removeAll(collect2);
						xzbSubIds.addAll(collect2.stream().flatMap(e -> relaSubjmap.get(e).stream())
								.filter(e -> allSubIdTypes.contains(e)).collect(Collectors.toList()));
					}
				}
			}else if(StringUtils.isNotBlank(xzb.getSubjectIds())) {
				//TODO 行政班排课 虚拟课程 在行政班上课的虚拟课程科目
				String[] subIds = xzb.getSubjectIds().split(",");
				xzbSubIds = Arrays.stream(subIds).filter(e->aSubjectIds.contains(e))
						.map(e->e+"-A").collect(Collectors.toSet());
			}
			
			xzbSubMap.put(xzb.getId(), xzbSubIds.stream().map(e->e.split("-")).collect(Collectors.toList()));
			xzbSubMap.get(xzb.getId()).addAll(xzbSubs);
		}
		
		return xzbSubMap;
	}
	
	@Override
	public Map<String,List<String[]>> findFakeXzbSubjects(String unitId, String divideId, String lessonArrangeId, String sourceType, List<String> fakeXzbIds){
		Map<String,List<String[]>> xzbSubMap = new HashMap<>();
		
		NewGkDivide realDivide = newGkDivideService.findOne(divideId);
		if(NewGkElectiveConstant.CLASS_SOURCE_TYPE1.equals(sourceType)) {
			realDivide = newGkDivideService.findOne(divideId);
		}else if(NewGkElectiveConstant.CLASS_SOURCE_TYPE2.equals(sourceType)) {
			NewGkArray array = arrayService.findOne(divideId);
			realDivide = newGkDivideService.findOne(array.getDivideId());
		}
		String openType = realDivide.getOpenType();
		if(!NewGkElectiveConstant.DIVIDE_TYPE_10.equals(openType)
				&& !NewGkElectiveConstant.DIVIDE_TYPE_12.equals(openType)) {
			return xzbSubMap;
		}
		
		List<NewGkDivideClass> fakeClassList = this.findByDivideIdAndClassType(unitId, divideId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_4},
				false, sourceType, false);
		List<Course> course2List = SUtils.dt(courseRemoteService.findWuliLiShi(unitId), Course.class);
		List<String> courseId2List = EntityUtils.getList(course2List, Course::getId);
		
		List<NewGkSubjectTime> subjectTimeList = subjectTimeService.findByArrayItemId(lessonArrangeId);
		Set<String> allSubIds = new HashSet<>();
		Set<String> aSubjectIds = new HashSet<>();
		Set<String> bSubjectIds = new HashSet<>();
		Set<String> course4List = new HashSet<>();
		List<String[]> specialCoruses = new ArrayList<>();
		for (NewGkSubjectTime st : subjectTimeList) {
			allSubIds.add(st.getSubjectId()+"-"+st.getSubjectType());
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(st.getSubjectType())) {
				aSubjectIds.add(st.getSubjectId());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(st.getSubjectType())) {
				bSubjectIds.add(st.getSubjectId());
			}
			if(!NewGkElectiveConstant.SUBJECT_TYPE_O.equals(st.getSubjectType()) 
					&&!courseId2List.contains(st.getSubjectId())) {
				course4List.add(st.getSubjectId());
			}
			if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(st.getSubjectType())
					&& Objects.equals(NewGkElectiveConstant.IF_INT_1,st.getFollowZhb())){
				specialCoruses.add(new String[]{st.getSubjectId(),st.getSubjectType()});
			}
		}
		for (NewGkDivideClass dc : fakeClassList) {
			String subjectIds = dc.getSubjectIds();
			List<String> subId2 = Arrays.asList(subjectIds.split(","));
			
			List<String[]> subIdTypes = subId2.stream().filter(e->aSubjectIds.contains(e)).map(e->new String[] {e,"A"}).collect(Collectors.toList());
			course4List.stream().filter(e->!subId2.contains(e)&&bSubjectIds.contains(e)).forEach(e->subIdTypes.add(new String[] {e,"B"}));
			subIdTypes.addAll(specialCoruses);
			xzbSubMap.put(dc.getId(), subIdTypes);
		}
		
		return xzbSubMap;
	}

	@Override
	public List<NewGkDivideClass> findNoMoveZhbs(String unitId, String divideId,String sourceType){
		if(sourceType == null){
			sourceType = NewGkElectiveConstant.CLASS_SOURCE_TYPE1;
		}
		List<NewGkDivideClass> classList = findByDivideIdAndClassType(unitId, divideId, new String[]{NewGkElectiveConstant.CLASS_TYPE_3}, false,
				sourceType, false);
		Map<String, List<NewGkDivideClass>> listMap = EntityUtils.getListMap(classList, NewGkDivideClass::getParentId, e -> e);
		List<NewGkDivideClass> results = new ArrayList<>();
		for (String k:listMap.keySet()){
			List<NewGkDivideClass> list = listMap.get(k);
			if(list.size()==2){
				results.addAll(list);
			}
		}
		return results;
	}
	
	public String checkChoose(NewGkDivide divide,String subjectIds) {
		String chooseError=newGkChoiceService.checkSubjectIds(divide.getChoiceId(), subjectIds);
		if(StringUtils.isNotBlank(chooseError)) {
			return chooseError;
		}
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[0])), Course.class);
    	Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId(),e->e);
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_09)
				|| divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_11)) {
			//选课结果必须是物理历史2选1
			String key=null;
			for(String s:subjectIds.split(",")) {
				if(!courseMap.containsKey(s)) {
					return "选课科目不对，存在科目找不到";
				}
				if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(courseMap.get(s).getSubjectCode())) {
					if(key==null) {
						key=s;
					}else {
						return "请选择3+1+2模式的选课";
					}
				}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(courseMap.get(s).getSubjectCode())) {
					if(key==null) {
						key=s;
					}else {
						return "请选择3+1+2模式的选课";
					}
				}
				
			}
			if(StringUtils.isBlank(key)) {
				return "请选择3+1+2模式的选课";
			}
		}
		return null;
	}

	@Override
	public Map<String, String> findOldDivideClassIdMap(String arrayId) {
		List<Object[]> retList = newGkDivideClassDao.findOldDivideClassIdMap(arrayId);
		Map<String,String> result = new HashMap<>();
		for (Object[] objects : retList) {
			result.put((String) objects[0], (String) objects[1]);
		}
		return result;
	}

	@Override
	public String saveChangeByStudentId(String divideId, String studentId, String subjectIds, String classIds,String oppoName) {
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide==null) {
			return "分班方案已不存在";
		}
		String[] classIdArr = classIds.split(",");
		List<NewGkDivideClass> list = findListByIdIn(classIdArr);
		if(classIdArr.length!=list.size()) {
			return "班级数据有调整，请刷新后重新操作";
		}
		//选课结果验证
		String chooseError=newGkChoiceService.checkSubjectIds(divide.getChoiceId(), subjectIds);
		if(StringUtils.isNotBlank(chooseError)) {
			return chooseError;
		}
		String[] chooseSubjectArr=subjectIds.split(",");
		//所有选课科目
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[0])), Course.class);
    	Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId(),e->e);
    	
		String subkey=null;
    	
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_09) || divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10)
				|| divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)
				|| divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_11)) {
			//选课结果必须是物理历史2选1
			for(String s:chooseSubjectArr) {
				if(!courseMap.containsKey(s)) {
					return "选课科目不对，存在科目找不到";
				}
				if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(courseMap.get(s).getSubjectCode())) {
					if(subkey==null) {
						subkey=s;
					}else {
						return "请选择3+1+2模式的选课";
					}
				}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(courseMap.get(s).getSubjectCode())) {
					if(subkey==null) {
						subkey=s;
					}else {
						return "请选择3+1+2模式的选课";
					}
				}
			}
			if(StringUtils.isBlank(subkey)) {
				return "请选择3+1+2模式的选课";
			}
		}
		//需要科目
		List<NewGkOpenSubject> openList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A,NewGkElectiveConstant.SUBJECT_TYPE_B});
		Set<String> subIdsAllA=new HashSet<>();
		Set<String> subIdsAllB=new HashSet<>();
		//获得该学生需要开设科目
		Set<String> subIdsA=new HashSet<>();
		Set<String> subIdsB=new HashSet<>();
		for(NewGkOpenSubject oo:openList) {
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(oo.getSubjectType())) {
				if(subjectIds.contains(oo.getSubjectId())) {
					subIdsA.add(oo.getSubjectId());
				}
				subIdsAllA.add(oo.getSubjectId());
			}else {
				if(!subjectIds.contains(oo.getSubjectId())) {
					subIdsB.add(oo.getSubjectId());
				}
				subIdsAllB.add(oo.getSubjectId());
			}
		}
		/*********************分类型保存方式**************************/
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10) || divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)) {
			//组合模式  页面传递的数据 xzb与伪xzb
			NewGkDivideClass xzbClass=null;
			NewGkDivideClass otherZhbClass=null;//伪xzb
			for(NewGkDivideClass c:list) {
				if(NewGkElectiveConstant.CLASS_TYPE_1.equals(c.getClassType())) {
					if(xzbClass!=null) {
						return "调整出错，选中多个行政班";
					}
					xzbClass=c;
				}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(c.getClassType())) {
					if(otherZhbClass!=null) {
						return "调整出错，选中组合班";
					}
					otherZhbClass=c;
				}else {
					return "调整出错，选中班级类型不对";
				}
			}
			if(xzbClass==null) {
				return "调整出错，选中班级不匹配";
			}
			NewGkDivideClass zhbClass=null;//10--重组用的
			NewGkDivideClass zhbClass3=null;//合并用的
			if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10)) {
				//重组
				if(StringUtils.isNotBlank(xzbClass.getRelateId())) {
					zhbClass=findOne(xzbClass.getRelateId());
				}
				if(zhbClass==null) {
					return "调整出错，选中班级不匹配";
				}
				//判断是否匹配
				if(zhbClass.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_3)) {
					if(list.size()>1) {
						return "调整出错，选中班级不匹配";
					}
					String[] subIds = zhbClass.getSubjectIds().split(",");
					for(String m:subIds) {
						if(!subjectIds.contains(m)) {
							return "调整出错，选中行政班对应的组合形式不适合该选课结果";
						}
					}
				}else if(zhbClass.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_1)){
					if(otherZhbClass==null) {
						return "调整出错，选中班级不匹配";
					}
					if(list.size()>2) {
						return "调整出错，选中班级不匹配";
					}
					if(!subjectIds.contains(zhbClass.getSubjectIds())) {
						return "调整出错，选中行政班对应的组合形式不适合该选课结果";
					}
				}else {
					return "调整出错，选中班级不匹配";
				}
			}else {
				if(otherZhbClass==null) {
					return "调整出错，选中班级不匹配";
				}
				//获取该班级物理历史关联
				List<NewGkDivideClass> class3List = newGkDivideClassDao.findListLikeRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_3, xzbClass.getId());
				if(CollectionUtils.isEmpty(class3List)) {
					return "调整出错，未找到该行政班下物理或者历史班级";
				}
				for(NewGkDivideClass s:class3List) {
					if(s.getSubjectIds().equals(subkey)) {
						if(zhbClass3==null) {
							zhbClass3=s;
						}else {
							return "调整出错，找到该行政班下多个物理或者历史班级";
						}
					}
				}
				if(zhbClass3==null) {
					return "调整出错，未找到该行政班下物理或者历史班级";
				}
			}
			
			
			//进行保存
			List<String> allChangeClassList=new ArrayList<>();;
			allChangeClassList.addAll(EntityUtils.getList(list, e->e.getId()));
			if(zhbClass!=null) {
				allChangeClassList.add(zhbClass.getId());
			}
			if(otherZhbClass!=null) {
				//找到关联的教学班
				List<NewGkDivideClass> jxbList = findListByRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, new String[] {otherZhbClass.getId()});
				if(CollectionUtils.isNotEmpty(jxbList)) {
					allChangeClassList.addAll(EntityUtils.getList(jxbList, e->e.getId()));
				}
			}
			if(zhbClass3!=null) {
				List<NewGkDivideClass> relaclass3List = newGkDivideClassDao.findListLikeRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, zhbClass3.getId());
				if(CollectionUtils.isEmpty(relaclass3List)) {
					return "调整出错，未找到该行政班下物理或者历史班级";
				}
				allChangeClassList.addAll(EntityUtils.getList(relaclass3List, e->e.getId()));
			}
			//老数据
			List<NewGkClassStudent> leftList = newGkClassStudentService.findListByStudentId(divide.getUnitId(), divideId, studentId);

			saveChangeList(divide, allChangeClassList, studentId, chooseSubjectArr, leftList, oppoName);
			
			return null;
		}
		/*********************除10，12外**************************/	
		
		//1个行政班+多个教学班
		NewGkDivideClass xzbClass=null;
		Map<String,NewGkDivideClass> jxbClassMap=new HashMap<>();
		Set<String> containJshu=new HashSet<>();//技术教学班
		for(NewGkDivideClass c:list) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(c.getClassType())) {
				if(xzbClass!=null) {
					return "调整出错，选中多个行政班";
				}
				xzbClass=c;
			}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(c.getClassType())) {
				if(jxbClassMap.containsKey(c.getSubjectIds())) {
					return "调整出错，同个科目选中多个教学班";
				}
				jxbClassMap.put(c.getSubjectIds(), c);
				//技术
				if("3037".equals(courseMap.get((c.getSubjectIds())).getSubjectCode())){
					containJshu.add(c.getId());
				}
			}else {
				return "调整出错，选中班级不是行政班或者教学班";
			}
		}
		if(xzbClass==null) {
			return "调整出错，未找到行政班";
		}

		//行政班排课科目
		Set<String> xzbA=new HashSet<>();
		Set<String> xzbB=new HashSet<>();
		NewGkDivideClass zhbClass=null;
		//拆分暂时不考虑---3+1+2也暂时不考虑
		
		List<String> leftClassIds=new ArrayList<>();
		
		if(StringUtils.isNotBlank(xzbClass.getRelateId())) {
			//获取按行政班排课的数据
			zhbClass = findOne(xzbClass.getRelateId());
			if(zhbClass!=null) {
				if(!NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(zhbClass.getSubjectType())) {
					for(String s:zhbClass.getSubjectIds().split(",")) {
						if(!subjectIds.contains(s)) {
							return "调整出错，选中行政班对应的组合形式不适合该选课结果";
						}
					}
				}
				List<NewGkDivideClass> relaJxbList = findListByRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
						NewGkElectiveConstant.CLASS_TYPE_2, new String[] {xzbClass.getRelateId()});
				Set<String> leftSubIds=new HashSet<>();
				if(CollectionUtils.isNotEmpty(relaJxbList)) {
					 leftSubIds = EntityUtils.getSet(relaJxbList, e->e.getSubjectIds());
				}
				
				if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(zhbClass.getSubjectType())) {
					for(String s:subIdsAllA) {
						if(zhbClass.getSubjectIds().contains(s) && !leftSubIds.contains(s)) {
							xzbA.add(s);
						}
					}
					for(String s:subIdsAllB) {
						if(!zhbClass.getSubjectIds().contains(s) && !leftSubIds.contains(s)) {
							xzbB.add(s);
						}
					}
				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(zhbClass.getSubjectIds())) {
					if(StringUtils.isNotBlank(zhbClass.getSubjectIdsB())) {
						for(String s:subIdsAllB) {
							if(zhbClass.getSubjectIdsB().contains(s) && !leftSubIds.contains(s)) {
								xzbB.add(s);
							}
						}
					}
				}else {
					for(String s:subIdsAllA) {
						if(zhbClass.getSubjectIds().contains(s) && !leftSubIds.contains(s)) {
							xzbA.add(s);
						}
					}
					if(StringUtils.isNotBlank(zhbClass.getSubjectIdsB())) {
						for(String s:subIdsAllB) {
							if(zhbClass.getSubjectIdsB().contains(s) && !leftSubIds.contains(s)) {
								xzbB.add(s);
							}
						}
					}
				}
				
				//验证xzbA xzbB 范围小于subIdsA subIdsB
				if(CollectionUtils.isNotEmpty(xzbA)) {
					int s=CollectionUtils.intersection(xzbA, subIdsA).size();
					if(s<xzbA.size()) {
						return "调整出错，选中行政班对应的组合形式,存在以行政班上课，不适合该选课结果";
					}
				}
				
				if(CollectionUtils.isNotEmpty(xzbB)) {
					int s=CollectionUtils.intersection(xzbB, subIdsB).size();
					if(s<xzbB.size()) {
						return "调整出错，选中行政班对应的组合形式,存在以行政班上课，不适合该选课结果";
					}
				}
			}
		}else {
			if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_11)) {
				/**
				  A-1,B-0:物理历史选考根据预先的分配组合结果物理历史班分层，物理历史学考跟选考结果无关，独立走班---都是走批次点 
				            （预先的分配组合结果只是一个过渡，学生暂时应该只能位于存在有该历史或者物理科目的行政班，优先同时选考去向该分配组合的学生所在选考班）---所有批次点 只要保证批次点不冲突
				  A-1,B-1：物理历史选考根据预先的分配组合结果物理历史班分层，物理历史学考根据预先的分配组合结果物理历史班分层---都是走批次点 
				                跟A-1,B-0：主要行政班限制，一致方式选考+学考优先同时选考去向该分配组合的学生所在选考班 在行政班这边显示优先选学考班级信息
				  A-1,B-2：物理历史选考根据预先的分配组合结果物理历史班分层--物理或者历史学考科目不安排
				*/
				
				/**
				 * A-2,B-0:预先的分配组合结果物理历史班，选考按行政班上课  剩余4门走批次;相当于物理历史学考跟选考结果无关，独立走班
				   A-2,B-2:预先的分配组合结果物理历史班，选考按行政班上课  剩余4门走批次;预先的分配组合结果物理历史班，学考按行政班上课  剩余4门走批次
				*/
				//特殊处理 物理历史是按照教学班来的
				List<NewGkDivideClass> class3List = newGkDivideClassDao.findListLikeRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_3, xzbClass.getId());
				if(CollectionUtils.isEmpty(class3List)) {
					return "调整出错，未找到该行政班下物理或者历史班级";
				}
				NewGkDivideClass class3Sub=null;
				for(NewGkDivideClass s:class3List) {
					if(s.getSubjectIds().equals(subkey)) {
						if(class3Sub==null) {
							class3Sub=s;
						}else {
							return "调整出错，找到该行政班下多个物理或者历史班级";
						}
					}
				}
				if(class3Sub==null) {
					return "调整出错，未找到该行政班下物理或者历史班级";
				}
				
				boolean isNeedXzbA=false;
				boolean isNeedXzbB=false;
				if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_11)) {
					if(divide.getFollowType().indexOf("A-2")>-1 && divide.getFollowType().indexOf("B-2")>-1) {
						isNeedXzbA=true;
						isNeedXzbB=true;
					}else if(divide.getFollowType().indexOf("A-2")>-1 && divide.getFollowType().indexOf("B-0")>-1) {
						isNeedXzbA=true;
					}else if(divide.getFollowType().indexOf("A-1")>-1 && divide.getFollowType().indexOf("B-2")>-1){
						isNeedXzbB=true;
					}else if(divide.getFollowType().indexOf("A-1")>-1 && divide.getFollowType().indexOf("B-1")>-1){
						
					}else if(divide.getFollowType().indexOf("A-1")>-1 && divide.getFollowType().indexOf("B-0")>-1){
						
					}else {
						return "数据有误，分班模式有误，请联系管理员";
					}
				}
				//这些模式 物理与历史都是必须安排行政班上课
				if(isNeedXzbA || isNeedXzbB) {
					List<NewGkDivideClass> relaclass3List = newGkDivideClassDao.findListLikeRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, class3Sub.getId());
					if(CollectionUtils.isEmpty(relaclass3List)) {
						return "调整出错，未找到该行政班下物理或者历史班级";
					}
					leftClassIds.add(class3Sub.getId());
					//当作行政班科目
					for(NewGkDivideClass s:relaclass3List) {
						if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(s.getSubjectType())) {
							xzbA.add(class3Sub.getSubjectIds());
						}else {
							xzbB.add(class3Sub.getSubjectIdsB());
						}
						leftClassIds.add(s.getId());
					}
				}
			} 
		}
		//获取该行政班下学生批次集合
		
		List<NewGkClassStudent> studentStuList = newGkClassStudentService.findListByClassIds(divide.getUnitId(), divideId, new String[] {xzbClass.getId()});
		List<NewGkClassStudent> leftList=null;
		boolean isCheckTime=false;
		Set<String> userTime=new HashSet<>();
		if(CollectionUtils.isNotEmpty(studentStuList)) {
			Set<String> stuIdsSet=EntityUtils.getSet(studentStuList,e->e.getStudentId());
			stuIdsSet.add(studentId);
			//获取这批学生旧数据
			List<NewGkClassStudent> list1 = newGkClassStudentService.findListByDivideStudentId(divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4}, stuIdsSet.toArray(new String[0]), NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
			if(CollectionUtils.isNotEmpty(list1)) {
				
				leftList=list1.stream().filter(e->e.getStudentId().equals(studentId)).collect(Collectors.toList());
				
				List<NewGkClassStudent> otherList = list1.stream().filter(e->!e.getStudentId().equals(studentId)).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(otherList)) {
					Set<String> otherJxbIds = EntityUtils.getSet(otherList, e->e.getClassId());
					userTime=findByIdIn(otherJxbIds.toArray(new String[0])).stream().filter(e->{
							return e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2) && StringUtils.isNotBlank(e.getBatch());
						}).map(e->(e.getSubjectType()+e.getBatch())).collect(Collectors.toSet());
					if(CollectionUtils.isNotEmpty(userTime)) {
						isCheckTime=true;
					}
				}
			}
		}else {
			leftList=newGkClassStudentService.findListByDivideStudentId(divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4}, new String[] {studentId},NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		}
		
		
		Map<String,NewGkDivideClass> timeJxbMap=new HashMap<>();
		for(String s:subIdsA) {
			if(xzbA.contains(s)) {
				continue;
			}
			if(!jxbClassMap.containsKey(s) || !jxbClassMap.get(s).getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
				return "调整出错，存在选考科目未有去向";
			}
			if(timeJxbMap.containsKey(jxbClassMap.get(s).getSubjectType()+jxbClassMap.get(s).getBatch())) {
				return "调整出错，选考存在多个科目在同一时间内";
			}
			timeJxbMap.put(jxbClassMap.get(s).getSubjectType()+jxbClassMap.get(s).getBatch(), jxbClassMap.get(s));
		}
		for(String s:subIdsB) {
			if(xzbB.contains(s)) {
				continue;
			}
			if(!jxbClassMap.containsKey(s) || !jxbClassMap.get(s).getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_B)) {
				return "调整出错，存在学考科目未有去向";
			}
			if(timeJxbMap.containsKey(jxbClassMap.get(s).getSubjectType()+jxbClassMap.get(s).getBatch())) {
				return "调整出错，学考存在多个科目在同一时间内";
			}
			timeJxbMap.put(jxbClassMap.get(s).getSubjectType()+jxbClassMap.get(s).getBatch(), jxbClassMap.get(s));
		}
		if(timeJxbMap.size()!=jxbClassMap.size()) {
			return "调整出错，选中教学班数据不对";
		}
		//剩下科目是不是有各自的时间---暂时确定验证该班级下的所有学生去向时间范围一致
		if(isCheckTime && timeJxbMap.size()>0) {
			//可以空余时间
			List<String> allBathList=new ArrayList<>();
			for(int i=1;i<=divide.getBatchCountTypea();i++) {
				allBathList.add(NewGkElectiveConstant.SUBJECT_TYPE_A+i);
			}
			for(int i=1;i<=divide.getBatchCountTypeb();i++) {
				allBathList.add(NewGkElectiveConstant.SUBJECT_TYPE_B+i);
			}
			//加上这个学生开辟出来的新批次点
			Set<String> leftSet=new HashSet<>();
			for(String s:timeJxbMap.keySet()) {
				if(userTime.contains(s)) {
					continue;
				}
				leftSet.add(s);
			}
			allBathList.removeAll(userTime);
			if(CollectionUtils.isNotEmpty(leftSet)) {
				if(CollectionUtils.isEmpty(allBathList)) {
					return "调整出错，需要的批次时间不符合";
				}
				//可能出现  组合班  定一走二   1,2批次六个走班  通过调整 可能会导致新增学生 1,3批次---暂时页面上的控制
				if(CollectionUtils.intersection(allBathList, leftSet).size()!=leftSet.size()) {
					return "调整出错，需要的批次时间不符合";
				}
			}
			
		}
		
		//进行保存
		List<String> allChangeClassList=new ArrayList<>();;
		allChangeClassList.addAll(EntityUtils.getList(list, e->e.getId()));
		if(zhbClass!=null) {
			allChangeClassList.add(zhbClass.getId());
		}
		if(CollectionUtils.isNotEmpty(leftClassIds)) {
			allChangeClassList.addAll(leftClassIds);
		}
		//技术拆分
		if(CollectionUtils.isNotEmpty(containJshu)) {
			List<NewGkDivideClass> containList = findByParentIds(containJshu.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(containList)) {
				allChangeClassList.addAll(EntityUtils.getList(containList, e->e.getId()));
			}
		}

		saveChangeList(divide, allChangeClassList, studentId, chooseSubjectArr, leftList, oppoName);
		return null;
	}
	
	private void saveChangeList(NewGkDivide divide,List<String> allChangeClassIdsList,
			String studentId,String[] chooseSubjectArr,List<NewGkClassStudent> oldItemList,String oppoName) {
		//进行保存
		List<NewGkClassStudent> insertClassStuList=new ArrayList<>();
		NewGkClassStudent dto;
		for(String ss:allChangeClassIdsList) {
			dto=new NewGkClassStudent();
			dto.setClassId(ss);
			dto.setCreationTime(new Date());
			dto.setDivideId(divide.getId());
			dto.setId(UuidUtils.generateUuid());
			dto.setModifyTime(new Date());
			dto.setStudentId(studentId);
			dto.setUnitId(divide.getUnitId());
			insertClassStuList.add(dto);
		}
		if(CollectionUtils.isNotEmpty(oldItemList)) {
			newGkClassStudentService.deleteAll(oldItemList.toArray(new NewGkClassStudent[0]));
		}
		newGkClassStudentService.saveAll(insertClassStuList.toArray(new NewGkClassStudent[0]));
		//保存选课
		boolean isUpdateFuzhu=true;
		String[] noUpdateType=new String[] {NewGkElectiveConstant.DIVIDE_TYPE_01,NewGkElectiveConstant.DIVIDE_TYPE_02,
				NewGkElectiveConstant.DIVIDE_TYPE_05,NewGkElectiveConstant.DIVIDE_TYPE_06};
		if(Arrays.asList(noUpdateType).contains(divide.getOpenType())) {
			isUpdateFuzhu=false;
		}
		//修改选课结果
		updateChoose(divide, studentId, chooseSubjectArr, oppoName,isUpdateFuzhu);
	}
	
	public void updateChoose(NewGkDivide divide,String studentId,String[] subjectIds,String oppoName,boolean isUpdateFuzhu) {
		Arrays.sort(subjectIds);
		// 操作记录
		boolean different = false;
		List<NewGkChoResult> oldChoiceResult = newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, divide.getChoiceId(), studentId);
		//选课科目
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		
		Student student = studentRemoteService.findOneObjectById(studentId);
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[0])), Course.class);
    	Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
    	NewGkTeachUpStuLog newGkTeachUpStuLog = new NewGkTeachUpStuLog();
    	newGkTeachUpStuLog.setId(UuidUtils.generateUuid());
    	newGkTeachUpStuLog.setChoiceId(divide.getChoiceId());
    	newGkTeachUpStuLog.setOperatorName(oppoName);
    	newGkTeachUpStuLog.setUnitId(divide.getUnitId());
    	newGkTeachUpStuLog.setGradeId(divide.getGradeId());
    	newGkTeachUpStuLog.setClassId(student.getClassId());
    	newGkTeachUpStuLog.setStudentId(studentId);
    	newGkTeachUpStuLog.setStudentCode(student.getStudentCode());
    	newGkTeachUpStuLog.setStudentName(student.getStudentName());
    	newGkTeachUpStuLog.setModifyTime(new Date());
    	StringBuilder stringBuilder = new StringBuilder("分班调整：由");
    	if (CollectionUtils.isNotEmpty(oldChoiceResult)) {
			for (NewGkChoResult one : oldChoiceResult) {
				stringBuilder.append(courseNameMap.get(one.getSubjectId()));
			}
		} else {
    		stringBuilder.append("未选");
		}
    	stringBuilder.append("修改至");
    	List<NewGkChoResult> resultList=new ArrayList<>();
    	NewGkChoResult result=null;
        for (int i = 0; i < subjectIds.length; i++) {
        	result = new NewGkChoResult();
        	result.setId(UuidUtils.generateUuid());
        	result.setUnitId(divide.getUnitId());
    		result.setCreationTime(new Date());
    		result.setModifyTime(new Date());
    		result.setChoiceId(divide.getChoiceId());
    		result.setStudentId(studentId);
    		result.setSubjectId(subjectIds[i]);
    		if (stringBuilder.indexOf(courseNameMap.get(subjectIds[i])) < 0) {
    			different = true;
			}
    		stringBuilder.append(courseNameMap.get(subjectIds[i]));
    		result.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
    		result.setKindType(NewGkElectiveConstant.KIND_TYPE_01);
    		resultList.add(result);
        }
        newGkTeachUpStuLog.setRemark(stringBuilder.toString());
       
        newGkChoResultService.saveAllResult(divide.getUnitId(),divide.getChoiceId(),resultList,new String[]{studentId},null,new String[]{studentId});
		if (different) {
			newGkTeachUpStuLogService.save(newGkTeachUpStuLog);
		}
		if(isUpdateFuzhu) {
			newGkDivideStusubService.deleteByChoiceIdAndDivideIdAndStudentId(divide.getChoiceId(), divide.getId(),studentId);
			List<NewGkDivideStusub> subList=new ArrayList<>();
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
			NewGkDivideStusub sub = new NewGkDivideStusub();
			sub.setChoiceId(divide.getChoiceId());
			sub.setDivideId(divide.getId());
			sub.setStudentCode(student.getStudentCode());
			sub.setStudentName(student.getStudentName());
			sub.setClassId(student.getClassId());
			sub.setClassName(clazz.getClassName());
			sub.setUnitId(divide.getUnitId());
			sub.setStudentId(student.getId());
			if(Objects.equals(student.getSex(), 2)) {
				sub.setStudentSex("女");
			}else {
				sub.setStudentSex("男");
			}
			sub.setCreationTime(new Date());
			sub.setModifyTime(new Date());
			
			NewGkDivideStusub sub1 = new NewGkDivideStusub();
			sub1=EntityUtils.copyProperties(sub, sub1);
			sub.setId(UuidUtils.generateUuid());
			sub1.setId(UuidUtils.generateUuid());
			sub.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
		
			sub.setSubjectIds(ArrayUtil.print(subjectIds));
			subList.add(sub);
			sub1.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
			Set<String> noChoose=new HashSet<>();
			noChoose.addAll(subjectIdsList);
			noChoose.removeAll(Arrays.asList(subjectIds));
			String[] chooseArr = noChoose.toArray(new String[0]);
			Arrays.sort(chooseArr);
			sub1.setSubjectIds(ArrayUtil.print(chooseArr));
			subList.add(sub1);
			newGkDivideStusubService.saveAll(subList.toArray(new NewGkDivideStusub[0] ));
		}
        
	}

	@Override
	public void deleteByStudentId(NewGkDivide divide, String studentId) {
		List<NewGkClassStudent> stuList = newGkClassStudentService.findListByStudentId(divide.getUnitId(), divide.getId(), studentId);
		if(CollectionUtils.isNotEmpty(stuList)) {
			newGkClassStudentService.deleteAll(stuList.toArray(new NewGkClassStudent[0] ));
		}
		newGkDivideStusubService.deleteByChoiceIdAndDivideIdAndStudentId(divide.getChoiceId(), divide.getId(),studentId);
	}
	
	public static void main(String[] args) {
		List<String> strings = Arrays.asList("abc", "11", "bc", "efg", "abcd","11", "jkl");
		List<String> filtered = strings.stream().filter(string -> string.isEmpty()).
				collect(Collectors.toList());
		System.out.println(filtered.size());
	}
	

	
}
