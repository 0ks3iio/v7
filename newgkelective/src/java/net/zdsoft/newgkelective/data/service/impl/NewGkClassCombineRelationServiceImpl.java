package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkClassCombineRelationDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;

@Service("newGkClassCombineRelationService")
public class NewGkClassCombineRelationServiceImpl extends BaseServiceImpl<NewGkClassCombineRelation, String>
		implements NewGkClassCombineRelationService {
	@Autowired
	private NewGkClassCombineRelationDao newGkClassCombineRelationDao;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkClassCombineRelation, String> getJpaDao() {
		return newGkClassCombineRelationDao;
	}

	@Override
	protected Class<NewGkClassCombineRelation> getEntityClass() {
		return NewGkClassCombineRelation.class;
	}

	@Override
	public List<NewGkClassCombineRelation> findByArrayItemId(String unitId, String arrayItemId) {
		if(StringUtils.isBlank(arrayItemId)) {
			return new ArrayList<>();
		}
		return newGkClassCombineRelationDao.findByUnitIdAndArrayItemId(unitId, arrayItemId);
	}

	@Override
	public List<Set<String>> getCombineRelation(String unitId, String arrayItemId) {
		List<NewGkClassCombineRelation> relaList = this.findByArrayItemId(unitId, arrayItemId);
		List<NewGkClassCombineRelation> combineRelaList = relaList.stream().filter(e -> NewGkElectiveConstant.COMBINE_TYPE_1.equals(e.getType())).collect(Collectors.toList());
		// 合班班级
		List<Set<String>> resultList = getGroupRelations(combineRelaList);
		return resultList;
	}

	@Override
	public List<Set<String>> getGroupRelations(List<NewGkClassCombineRelation> relaList) {
		Iterator<NewGkClassCombineRelation> iterator = relaList.iterator();
		List<Set<String>> resultList = new ArrayList<>();
		while(iterator.hasNext()){
			NewGkClassCombineRelation next = iterator.next();
			iterator.remove();
			String[] clsSubs = next.getClassSubjectIds().split(",");
			String clsSub = clsSubs[0];
			Set<String> group = new HashSet<>();
			group.add(clsSubs[0]);
			group.add(clsSubs[1]);
			while (iterator.hasNext()){
				String s2 = iterator.next().getClassSubjectIds();
				if(s2.contains(clsSub)){
					String[] split = s2.split(",");
					group.add(split[0]);
					group.add(split[1]);
					iterator.remove();
				}
			}
			iterator = relaList.iterator();
			while (iterator.hasNext()){
				String s2 = iterator.next().getClassSubjectIds();
				String[] split = s2.split(",");
				if(group.contains(split[0]) || group.contains(split[1])){
					iterator.remove();
				}
			}
			resultList.add(group);
			iterator = relaList.iterator();
		}
		return resultList;
	}

	@Override
	public void deleteByArrayItemId(String arrayItemId) {
		if(StringUtils.isNotEmpty(arrayItemId))
			newGkClassCombineRelationDao.deleteByArrayItemId(arrayItemId);
	}

    @Override
    public void deleteBySubjectIdOrClassIds(String... objectId) {
        newGkClassCombineRelationDao.deleteByClassSubjectIdsLike("%" + objectId + "%");
    }

}
