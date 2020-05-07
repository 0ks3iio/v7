package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkChoRelationDao;
import net.zdsoft.newgkelective.data.dao.NewGkChoRelationJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;

@Service("newGkChoRelationService")
public class NewGkChoRelationServiceImpl extends BaseServiceImpl<NewGkChoRelation, String> implements NewGkChoRelationService{

	@Autowired
	private NewGkChoRelationDao newGkChoRelationDao;
	@Autowired
	private NewGkChoRelationJdbcDao newGkChoRelationJdbcDao;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkChoRelation, String> getJpaDao() {
		return newGkChoRelationDao;
	}

	@Override
	protected Class<NewGkChoRelation> getEntityClass() {
		return NewGkChoRelation.class;
	}

	@Override
	public List<String> findByChoiceIdAndObjectType(String unitId,
			String choiceId, String objectType) {
		return newGkChoRelationDao.findByChoiceIdAndObjectType(unitId,choiceId, objectType);			
	}
	
	@Override
	public List<NewGkChoRelation> findByChoiceIdsAndObjectType(String unitId,
			String[] choiceIds, String objectType) {
		if(choiceIds == null || choiceIds.length ==0) {
			return new ArrayList<NewGkChoRelation>();
		}
		return newGkChoRelationDao.findByChoiceIdInAndObjectType(unitId,choiceIds, objectType);
	}

    @Override
    public List<NewGkChoRelation> findByChoiceIdsAndObjectTypeWithMaster(String unitId,
                                                               String[] choiceIds, String objectType) {
        return findByChoiceIdsAndObjectType(unitId, choiceIds, objectType);
    }

	@Override
	public List<NewGkChoRelation> findByChoiceIdAndObjectTypeAndObjectValueIn(
			String unitId, String choiceId, String choiceType, String[] ObjectValues) {
		return newGkChoRelationDao.findByUnitIdAndChoiceIdAndObjectTypeAndObjectValueIn(unitId,choiceId, choiceType,ObjectValues);
	}

	public Map<String, String> findByChoiceIdsAndObjectTypeAndObjVal(String unitId, String[] choiceIds, String choiceType, String ObjectValues){
		Map<String, String> valMap = new HashMap<String, String>();
		if(ArrayUtils.isEmpty(choiceIds)) {
			return valMap;
		}
		Specification<NewGkChoRelation> specification = new Specification<NewGkChoRelation>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkChoRelation> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(root.get("choiceId").in(choiceIds));
				ps.add(cb.equal(root.get("objectType").as(String.class), choiceType));
				ps.add(cb.equal(root.get("objectValue").as(String.class), ObjectValues));
				return cq.where(cb.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		};
		List<NewGkChoRelation> res = newGkChoRelationDao.findAll(specification);
		if(CollectionUtils.isNotEmpty(res)) {
			valMap = EntityUtils.getMap(res, NewGkChoRelation::getChoiceId, NewGkChoRelation::getObjectValue);
		}
		return valMap;
	}

	@Override
	public void deleteByChoiceIdType(String unitId, String choiceId,
			String objectType, String... studentIds) {
		if(studentIds!=null && studentIds.length>0){
			if(studentIds.length<=1000){
				newGkChoRelationJdbcDao.deleteByChoiceIdTypeStu(unitId,choiceId,objectType, studentIds);
			}else{
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
					newGkChoRelationJdbcDao.deleteByChoiceIdTypeStu(unitId,choiceId,objectType, stuId);
				}
			}
			
		}else{
			newGkChoRelationJdbcDao.deleteByChoiceIdAndObjectTypeIn(unitId,choiceId,new String[]{objectType});
		}
	}
	
	public void deleteByTypeChoiceIds(String unitId, String objectType, String... choiceIds) {
		if(ArrayUtils.isEmpty(choiceIds)) {
			return;
		}
		newGkChoRelationJdbcDao.deleteByTypeChoiceIds( unitId, objectType, choiceIds);
	}

	@Override
	public void saveAndDeleteList(List<NewGkChoRelation> newGkChoRelations,
			String choiceId, String unitId) {
		if(choiceId!=null){
			newGkChoRelationJdbcDao.deleteByChoiceIdAndObjectTypeIn(unitId,choiceId, new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,NewGkElectiveConstant.CHOICE_TYPE_02,NewGkElectiveConstant.CHOICE_TYPE_03,NewGkElectiveConstant.CHOICE_TYPE_06});
		}
		if(CollectionUtils.isNotEmpty(newGkChoRelations)){
			newGkChoRelationJdbcDao.insertBatch(checkSave(newGkChoRelations.toArray(new NewGkChoRelation[]{} )));
		}
		RedisUtils.del("NEW_GK_CHORELATION_CHO_OBJTYPES_"+choiceId);
	}
	
	public List<Course> findChooseSubject(final String choiceId, String unitId){
		List<Course> list=new ArrayList<Course>();
		List<String> objectValues=findByChoiceIdAndObjectType(unitId,choiceId, NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isNotEmpty(objectValues))
			list = SUtils.dt(courseRemoteService.findBySubjectIdIn(objectValues.toArray(new String[0])),new TR<List<Course>>(){});
		
		return list;
	}
	
	@Override
	public Map<String, List<NewGkChoRelation>> findByGradeId(String gradeId) {
		return RedisUtils.getObject("NEW_GK_CHORELATION_ALL"+gradeId, RedisUtils.TIME_HALF_MINUTE, new TypeReference<Map<String, List<NewGkChoRelation>>>(){},new RedisInterface<Map<String, List<NewGkChoRelation>>>() {
			@Override
			public Map<String, List<NewGkChoRelation>> queryData() {
				List<NewGkChoRelation>  list = newGkChoRelationDao.findByGradeId(gradeId);
				Map<String, List<NewGkChoRelation>> map=new HashMap<String, List<NewGkChoRelation>>();
				if(CollectionUtils.isNotEmpty(list)){
					for(NewGkChoRelation ent :list){
						List<NewGkChoRelation> list2= map.get(ent.getObjectType());
						if(list2 == null){
							list2= new ArrayList<NewGkChoRelation>();
							map.put(ent.getObjectType(), list2);
						}
						list2.add(ent);
					}
				}
				return map;
			}
		});
	}

	@Override
	public void saveAndDeleteByList(List<NewGkChoRelation> saveList, String[] choiceIds,String unitId, String choiceType) {
		if(choiceIds!=null && choiceIds.length!=0){
			newGkChoRelationJdbcDao.deleteByTypeChoiceIds(unitId,
					choiceType, choiceIds);
		}
		if(CollectionUtils.isNotEmpty(saveList)){
			newGkChoRelationJdbcDao.insertBatch(checkSave(saveList.toArray(new NewGkChoRelation[]{} )));
		}
	}

    @Override
    public void deleteByObjectTypeVal(String divideId, String subjectId) {
        newGkChoRelationDao.deleteByChoiceIdAndObjectTypeVal(divideId, subjectId);
    }

    @Override
	public Map<String, List<NewGkChoRelation>> findByChoiceIdAndObjectTypeIn(String unitId,
			String choiceId, String[] objectTypes) {
		List<NewGkChoRelation>  list = newGkChoRelationDao.findByUnitIdAndChoiceIdAndObjectTypeIn(unitId,choiceId, objectTypes);
		Map<String, List<NewGkChoRelation>> map=new HashMap<String, List<NewGkChoRelation>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkChoRelation ent :list){
				List<NewGkChoRelation> list2= map.get(ent.getObjectType());
				if(CollectionUtils.isEmpty(list2)){
					list2= new ArrayList<NewGkChoRelation>();
					map.put(ent.getObjectType(), list2);
				}
				list2.add(ent);
			}
		}
		return map;
	}

	@Override
	public List<String> findByChoiceIdAndObjectTypeWithMaster(String unitId, String choiceId, String objectType) {
		return findByChoiceIdAndObjectType(unitId, choiceId, objectType);
	}

	@Override
	public List<NewGkChoRelation> findByChoiceIdAndObjectTypeAndObjectTypeValIn(String unitId, String choiceId, String choiceType, String[] objectTypeVals) {
		return newGkChoRelationDao.findByUnitIdAndChoiceIdAndObjectTypeAndObjectTypeValIn(unitId, choiceId, choiceType, objectTypeVals);
	}

}
