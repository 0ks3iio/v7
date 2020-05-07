package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkOpenSubjectDao;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;

@Service("newGkOpenSubjectService")
public class NewGkOpenSubjectServiceImpl extends BaseServiceImpl<NewGkOpenSubject,String> implements NewGkOpenSubjectService{

	@Autowired
	private NewGkOpenSubjectDao newGkOpenSubjectDao;
	
	
	
	@Override
	protected BaseJpaRepositoryDao<NewGkOpenSubject, String> getJpaDao() {
		return newGkOpenSubjectDao;
	}

	@Override
	protected Class<NewGkOpenSubject> getEntityClass() {
		return NewGkOpenSubject.class;
	}

	@Override
	public void deleteByDivideId(String divideId) {
		newGkOpenSubjectDao.deleteByDivideId(divideId);
	}

	@Override
	public void saveAllEntity(List<NewGkOpenSubject> openSubjectList) {
		if(CollectionUtils.isNotEmpty(openSubjectList)){
			newGkOpenSubjectDao.saveAll(checkSave(openSubjectList.toArray(new NewGkOpenSubject[]{})));
		}
	}

	@Override
	public List<NewGkOpenSubject> findByDivideId(String divideId) {
		return newGkOpenSubjectDao.findByDivideId(divideId);
	}
	
	public List<NewGkOpenSubject> findByDivideIdAndGroupType(String divideId, String groupType){
		if(StringUtils.isEmpty(groupType)) {
			return findByDivideId(divideId);
		}
		return newGkOpenSubjectDao.findByDivideIdAndGroupType(divideId, groupType);
	}

	@Override
	public List<NewGkOpenSubject> findByDivideIdAndSubjectTypeIn(String divideId, String[] subjectTypes) {
		return newGkOpenSubjectDao.findByDivideIdAndSubjectTypeIn(divideId,subjectTypes);
	}

	@Override
	public List<NewGkOpenSubject> findByDivideIdIn(String[] divideIds) {
		if(ArrayUtils.isEmpty(divideIds)){
			return new ArrayList<NewGkOpenSubject>();
		}
		return newGkOpenSubjectDao.findByDivideIdIn(divideIds);
	}

    @Override
    public void deleteBySubjectIds(String... subIds) {
        newGkOpenSubjectDao.deleteBySubjectIdIn(subIds);
    }

	@Override
	public List<NewGkOpenSubject> findByDivideIdAndSubjectTypeInWithMaster(String divideId, String[] subjectTypes) {
		return findByDivideIdAndSubjectTypeIn(divideId, subjectTypes);
	}


}
