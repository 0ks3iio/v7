package net.zdsoft.newgkelective.data.service.impl;

import java.util.*;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkChoCategoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newGkChoCategoryService")
public class NewGkChoCategoryServiceImpl extends BaseServiceImpl<NewGkChoCategory, String> implements NewGkChoCategoryService{
	@Autowired
	private NewGkChoCategoryDao newGkChoCategoryDao;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkChoCategory, String> getJpaDao() {
		return newGkChoCategoryDao;
	}

	@Override
	protected Class<NewGkChoCategory> getEntityClass() {
		return NewGkChoCategory.class;
	}


    @Override
    public void saveAndDeleteList(List<NewGkChoCategory> newGkChoCategoryList, String[] oldIds) {
        newGkChoCategoryDao.saveAll(newGkChoCategoryList);
        newGkChoCategoryDao.deleteByIdIn(oldIds);
    }

	@Override
	public List<NewGkChoCategory> findByChoiceId(String unitId, String choiceId) {
		List<NewGkChoCategory> categoryList = newGkChoCategoryDao.findByUnitIdAndChoiceId(unitId, choiceId);
		if(CollectionUtils.isEmpty(categoryList)){
			return new ArrayList<NewGkChoCategory>();
		}
		List<NewGkChoRelation> relationList = newGkChoRelationService.findByChoiceIdAndObjectTypeAndObjectTypeValIn(unitId, choiceId,
				NewGkElectiveConstant.CHOICE_TYPE_01, EntityUtils.getList(categoryList, NewGkChoCategory::getId).toArray(new String[0]));
		List<NewGkChoRelation> relList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId, new String[]{choiceId},NewGkElectiveConstant.CHOICE_TYPE_06);
		
		//父类类别id-科目组合ids
		Map<String, Set<String>> relMap =new HashMap<>();
		if(CollectionUtils.isNotEmpty(relList)){
			for(NewGkChoRelation ent: relList){
				Set<String> keys = relMap.get(ent.getObjectTypeVal());
				if(keys==null){
					keys=new HashSet<>();
					relMap.put(ent.getObjectTypeVal(), keys);
				}
				keys.add(ent.getObjectValue());
			}
		}
		Map<String, List<String>> relationMap = EntityUtils.getListMap(relationList, NewGkChoRelation::getObjectTypeVal, NewGkChoRelation::getObjectValue);
		for (NewGkChoCategory category : categoryList) {
			category.setCourseList(relationMap.get(category.getId()));
			Set<String> keys = relMap.get(category.getId());
			if(keys!=null){
				List<List<String>> ls =new ArrayList<>();
				for(String key:keys){
					List<String> zj= relationMap.get(key);
					ls.add(zj);
				}
				category.setCourseLists(ls);
			}
		}
		return categoryList;
	}

    @Override
    public Map<String, String> findMapByChoiceId(String unitId, String choiceId) {
        List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId, new String[]{choiceId}, NewGkElectiveConstant.CHOICE_TYPE_01);
        return EntityUtils.getMap(newGkChoRelationList, NewGkChoRelation::getObjectValue, NewGkChoRelation::getObjectTypeVal);
    }

}
