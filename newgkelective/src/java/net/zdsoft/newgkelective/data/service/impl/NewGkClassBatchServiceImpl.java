package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkClassBatchDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkClassBatchService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;

/**
 * 
 * @author weixh
 * @since 2018年6月12日 上午10:01:52
 */
@Service("newGkClassBatchService")
public class NewGkClassBatchServiceImpl extends BaseServiceImpl<NewGkClassBatch, String>
		implements NewGkClassBatchService {
	@Autowired
	private NewGkClassBatchDao newGkClassBatchDao;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	NewGkChoResultService newGkChoResultService;
	@Autowired
	NewGkClassStudentService newGkClassStudentService;
	@Autowired
	NewGkDivideService newGkDivideService;
	@Autowired
	CourseRemoteService courseRemoteService;
	@Autowired
	NewGkChoiceService newGkChoiceService;

	@Override
	protected BaseJpaRepositoryDao<NewGkClassBatch, String> getJpaDao() {
		return newGkClassBatchDao;
	}

	@Override
	protected Class<NewGkClassBatch> getEntityClass() {
		return NewGkClassBatch.class;
	}

	public List<NewGkClassBatch> findByDivideClsIds(final String... clsIds){
		if(ArrayUtils.isEmpty(clsIds)) {
			return new ArrayList<NewGkClassBatch>();
		}
		Specification<NewGkClassBatch> specification = new Specification<NewGkClassBatch>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkClassBatch> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(root.get("divideClassId").in((String[]) clsIds));
				return cq.where(cb.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		};
		return newGkClassBatchDao.findAll(specification);
	}
	public List<NewGkClassBatch> findByDivideClsIdsWithMaster(final String... clsIds){
		return findByDivideClsIds(clsIds);
	}
	@Override
	public List<NewGkClassBatch> findbyBatchAndSubjectId(String divideId, String batch, String subjectId) {
		if(StringUtils.isBlank(subjectId)) {
			return newGkClassBatchDao.findByDivideIdAndBatch(divideId,batch);
		}
		
		return newGkClassBatchDao.findByBatchAndSubjectId(divideId,batch,subjectId);
	}

	public void saveBatchs(List<NewGkClassBatch> batchs, String divideId, boolean deleteByCls,List<NewGkDivideClass> updateStuClass,String unitId) {
		if(CollectionUtils.isEmpty(batchs)) {
			return;
		}
		if(CollectionUtils.isNotEmpty(updateStuClass)) {
			Set<String> delStuClassId=new HashSet<>();
			List<NewGkClassStudent> insertClassStudent=new ArrayList<>();
			NewGkClassStudent newGkClassStudent;
			for(NewGkDivideClass cc:updateStuClass) {
				delStuClassId.add(cc.getId());
				if(CollectionUtils.isNotEmpty(cc.getStudentList())) {
					for(String ss:cc.getStudentList()) {
						newGkClassStudent =new NewGkClassStudent();
						newGkClassStudent.setClassId(cc.getId());
						newGkClassStudent.setCreationTime(new Date());
						newGkClassStudent.setDivideId(cc.getDivideId());
						newGkClassStudent.setStudentId(ss);
						newGkClassStudent.setId(UuidUtils.generateUuid());
						newGkClassStudent.setUnitId(unitId);
						newGkClassStudent.setModifyTime(new Date());
						insertClassStudent.add(newGkClassStudent);
					}
				}
			}
			newGkClassStudentService.deleteByClassIdIn(unitId, divideId, delStuClassId.toArray(new String[] {}));
			newGkClassStudentService.saveAllList(insertClassStudent);
		}
		
		
		// 删除原来的时间点记录数据
		if(deleteByCls) {
			Set<String> cids = EntityUtils.getSet(batchs, NewGkClassBatch::getDivideClassId);
			newGkClassBatchDao.deleteByDivideIdAndDivideClassIdIn(divideId, cids.toArray(new String[0]));
		}
		this.saveAll(batchs.toArray(new NewGkClassBatch[0]));
	}
	@Override
	public void saveBatchs2(String unitId, String divideId, List<NewGkDivideClass> updateStuClass, List<NewGkClassBatch> clsBatchs) {
		if(CollectionUtils.isNotEmpty(updateStuClass)) {
//			Set<String> delStuClassId=new HashSet<>();
			List<NewGkClassStudent> insertClassStudent=new ArrayList<>();
			NewGkClassStudent newGkClassStudent;
			for(NewGkDivideClass cc:updateStuClass) {
//				delStuClassId.add(cc.getId());
				if(NewGkElectiveConstant.CLASS_TYPE_2.equals(cc.getClassType()) && CollectionUtils.isNotEmpty(cc.getStudentList())) {
					for(String ss:cc.getStudentList()) {
						newGkClassStudent =new NewGkClassStudent();
						newGkClassStudent.setClassId(cc.getId());
						newGkClassStudent.setCreationTime(new Date());
						newGkClassStudent.setDivideId(cc.getDivideId());
						newGkClassStudent.setStudentId(ss);
						newGkClassStudent.setId(UuidUtils.generateUuid());
						newGkClassStudent.setUnitId(unitId);
						newGkClassStudent.setModifyTime(new Date());
						insertClassStudent.add(newGkClassStudent);
					}
				}
			}
			newGkDivideClassService.saveAll(updateStuClass.toArray(new NewGkDivideClass[0]));
			newGkClassStudentService.saveAllList(insertClassStudent);
		}
		
		if(CollectionUtils.isNotEmpty(clsBatchs)) {
			this.saveAll(clsBatchs.toArray(new NewGkClassBatch[0]));
		}
	}
	
	@Override
	public void updateMoveCourse(String unitId, String divideId, String clsId, String planType, List<String> delClsIds,
			List<NewGkDivideClass> updateStuClass, List<NewGkClassBatch> clsBatchs) {
		// 
		if(CollectionUtils.isNotEmpty(delClsIds)) {
			newGkDivideClassService.deleteByClassIdIn(unitId, divideId, delClsIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(updateStuClass)) {
			List<NewGkClassStudent> insertClassStudent=new ArrayList<>();
			NewGkClassStudent newGkClassStudent;
			for(NewGkDivideClass cc:updateStuClass) {
				if(NewGkElectiveConstant.CLASS_TYPE_2.equals(cc.getClassType()) && CollectionUtils.isNotEmpty(cc.getStudentList())) {
					for(String ss:cc.getStudentList()) {
						newGkClassStudent =new NewGkClassStudent();
						newGkClassStudent.setClassId(cc.getId());
						newGkClassStudent.setCreationTime(new Date());
						newGkClassStudent.setDivideId(cc.getDivideId());
						newGkClassStudent.setStudentId(ss);
						newGkClassStudent.setId(UuidUtils.generateUuid());
						newGkClassStudent.setUnitId(unitId);
						newGkClassStudent.setModifyTime(new Date());
						insertClassStudent.add(newGkClassStudent);
					}
				}
			}
			newGkDivideClassService.saveAll(updateStuClass.toArray(new NewGkDivideClass[0]));
			newGkClassStudentService.saveAllList(insertClassStudent);
		}
		
		if(StringUtils.isNotBlank(clsId)) {
			this.deleteByDivideClassIds(divideId, new String[] {clsId}, planType);
		}
		if(CollectionUtils.isNotEmpty(clsBatchs)) {
			this.saveAll(clsBatchs.toArray(new NewGkClassBatch[0]));
		}
	}
	
	@Override
	public void dealResetFloatingPlan(String unitId, String divideId, String planType) {
		if(StringUtils.isBlank(planType)) {
			return;
		}
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, false, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<String> zhbIds = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())).map(e->e.getId()).collect(Collectors.toList());
		List<String> jxbIds = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
					&&Objects.equals(planType, e.getSubjectType()))
				.map(e->e.getId()).collect(Collectors.toList());
		
		if(CollectionUtils.isNotEmpty(zhbIds)) {
			this.deleteByDivideClassIds(divideId, zhbIds.toArray(new String[0]), planType);
		}
		if(CollectionUtils.isNotEmpty(jxbIds)) {
			newGkDivideClassService.deleteByClassIdIn(unitId, divideId, jxbIds.toArray(new String[0]));
		}
	}
	
	@Override
	public void deleteByDivideClassIds(String divideId, String[] divideClsIds,String subjectType) {
		if(StringUtils.isBlank(subjectType)) {
			newGkClassBatchDao.deleteByDivideIdAndDivideClassIdIn(divideId, divideClsIds);
		}else {
			newGkClassBatchDao.deleteByClasIdAndSubjectType(divideId, divideClsIds,subjectType);
		}
	}
	
	public void saveClsBatch(NewGkDivide divide, String divideClsId, NewGkDivideClass divideCls) {
		// TODO 
		String[] names = divideCls.getClassName().replaceAll(" ", "").split(",");
		String[] subIds = divideCls.getSubjectIds().replaceAll(" ", "").split(",");
		String[] bas = divideCls.getBatch().replaceAll(" ", "").split(",");
		String[] ids = divideCls.getId().replaceAll(" ", "").split(",");
		List<NewGkDivideClass> dcs = new ArrayList<NewGkDivideClass>();
		Map<String, String> subIdMap = new HashMap<String, String>();
		Map<String, String> exMap = new HashMap<String, String>();
		Map<String, Integer> subSumMap = new HashMap<String, Integer>();
		Map<String, String> clsNameMap = new HashMap<String, String>();
		Map<String, String> exClsNameMap = new HashMap<String, String>();
		// 新建班级
		if(subIds!=null && subIds.length>0) {
			for(String subid :subIds) {
				List<NewGkDivideClass> classList = newGkDivideClassService.findClassBySubjectIds(divide.getUnitId(), divide.getId(),
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subid, false);
				int classIndex=1;
				if(CollectionUtils.isNotEmpty(classList)) {
					classIndex = classList.size()+1;
					for(NewGkDivideClass dc : classList) {
						exClsNameMap.put(dc.getId()+dc.getSubjectIds()+dc.getBatch(), dc.getClassName());
						clsNameMap.put(dc.getClassName(), dc.getId());
					}
					clsNameMap.putAll(EntityUtils.getMap(classList, NewGkDivideClass::getClassName, NewGkDivideClass::getId));
				}
				subSumMap.put(subid, classIndex);
			}
		}
		
					
		for(int i=0;i<bas.length;i++) {
			NewGkDivideClass dc = new NewGkDivideClass();
			dc.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
			dc.setDivideId(divide.getId());
			dc.setRelateId(divideClsId);
			dc.setSubjectIds(subIds[i]);
			dc.setBatch(bas[i]);
			dc.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
			dc.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			dc.setModifyTime(new Date());
			
			if (ids.length > i && StringUtils.isNotEmpty(ids[i])) {
				dc.setId(ids[i]);
				exMap.put(dc.getSubjectIds()+dc.getBatch(), dc.getId());
			}
			// 数据不变，维持班级名称不要变
			if(StringUtils.isNotEmpty(dc.getId()) && exClsNameMap.containsKey(dc.getId()+dc.getSubjectIds()+dc.getBatch())) {
				dc.setClassName(exClsNameMap.get(dc.getId()+dc.getSubjectIds()+dc.getBatch()));
			}
			if (StringUtils.isEmpty(dc.getClassName())) {
				int ssindex = subSumMap.get(subIds[i]);
				String cn = names[i] + ssindex + "班";
				while (clsNameMap.containsKey(cn)) {
					if (StringUtils.isNotEmpty(dc.getId()) && dc.getId().equals(clsNameMap.get(cn))) {
						break;
					}
					ssindex++;
					cn = names[i] + ssindex + "班";
				}
				dc.setClassName(cn);
			}
			if(StringUtils.isEmpty(dc.getId())) {
				dc.setId(UuidUtils.generateUuid());
				dc.setCreationTime(new Date());
			}
			subIdMap.put(dc.getSubjectIds(), dc.getId());
			dcs.add(dc);
		}
		
		String[] dcids = new String[] {divideClsId};
		List<String> delCids = new ArrayList<String>();
		if (exMap.size() > 0) {
			List<NewGkDivideClass> reDcs = newGkDivideClassService.findByRelateIdsWithMaster(dcids);
			List<NewGkDivideClass> toDels = new ArrayList<NewGkDivideClass>();
			boolean del = false;
			for (NewGkDivideClass dc : reDcs) {
				del = false;
				if (exMap.containsKey(dc.getSubjectIds() + dc.getBatch())) {
					if (dc.getId().equals(exMap.get(dc.getSubjectIds() + dc.getBatch()))) {
						subIdMap.remove(dc.getSubjectIds());
					} else {
						del = true;
					}
				} else {
					del = true;
				}
				if(del) {
					toDels.add(dc);
					delCids.add(dc.getId());
				}
			} 
			// 删除无用的班级
			if(toDels.size() > 0) {
				newGkDivideClassService.deleteAll(toDels.toArray(new NewGkDivideClass[0]));
				newGkClassBatchDao.deleteByDivideIdAndDivideClassIdIn(divide.getId(), dcids);
			}
			if(delCids.size() > 0) {
				newGkClassStudentService.deleteByClassIdIn(divide.getUnitId(), divide.getId(), delCids.toArray(new String[0]));
			}
		}
		
//		if (subIdMap.size() > 0) {
//			List<NewGkClassStudent> csList = newGkClassStudentService.findListByIn("classId",dcids);
//			List<String> stuIds = EntityUtils.getList(csList, "studentId");
//			// 学生选课数据整理
//			List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,
//					divide.getChoiceId(), stuIds.toArray(new String[0]));
//			List<NewGkClassStudent> toSaves = new ArrayList<NewGkClassStudent>();
//			for(NewGkChoResult re : resultList) {
//				if(subIdMap.containsKey(re.getSubjectId())) {
//					NewGkClassStudent cs = new NewGkClassStudent();
//					cs.setClassId(subIdMap.get(re.getSubjectId()));
//					cs.setStudentId(re.getStudentId());
//					cs.setId(UuidUtils.generateUuid());
//					cs.setCreationTime(new Date());
//					cs.setUnitId(divide.getUnitId());
//					cs.setDivideId(divide.getId());
//					cs.setModifyTime(cs.getCreationTime());
//					toSaves.add(cs);
//				}
//			}
//			if(toSaves.size() > 0) {
//				newGkClassStudentService.saveAll(toSaves.toArray(new NewGkClassStudent[0]));
//			}
//		}
		newGkDivideClassService.saveAll(dcs.toArray(new NewGkDivideClass[0]));
	}

	@Override
	public void deleteByDivideId(String unitId,String divideId, String[] classIds) {
		if(ArrayUtils.isNotEmpty(classIds)) {
			newGkDivideClassService.deleteByClassIdIn(unitId, divideId, classIds);
		}
		newGkClassBatchDao.deleteByDivideId(divideId);
	}

    @Override
    public void deleteBySubjectIds(String... subjectIds) {
        newGkClassBatchDao.deleteBySubjectIdIn(subjectIds);
    }

	@Override
	public long countByDivideId(String divideId) {
		Long num =(Long)newGkClassBatchDao.countByDivideId(divideId);
		
		return num==null?0:num;
	}

	@Override
	public void saveBatchs3(String unitId, String divideId, List<NewGkDivideClass> updateStuClass,
			List<NewGkClassBatch> clsBatchs, String planType) {
		dealResetFloatingPlan(unitId, divideId, planType);
		if(CollectionUtils.isNotEmpty(updateStuClass)) {
			newGkDivideClassService.saveAll(updateStuClass.toArray(new NewGkDivideClass[0]));
		}
		if(CollectionUtils.isNotEmpty(clsBatchs)) {
			this.saveAll(clsBatchs.toArray(new NewGkClassBatch[0]));
		}
	}


	@Override
	public List<NewGkClassBatch> findBySubjectTypeWithMaster(String divideId, String subjectType) {
		return newGkClassBatchDao.findByDivideIdAndSubjectType(divideId, subjectType);
	}

}
