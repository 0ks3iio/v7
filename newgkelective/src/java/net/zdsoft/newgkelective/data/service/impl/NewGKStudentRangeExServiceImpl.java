package net.zdsoft.newgkelective.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.dao.NewGKStudentRangeExDao;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;

@Service("newGKStudentRangeExService")
public class NewGKStudentRangeExServiceImpl extends BaseServiceImpl<NewGKStudentRangeEx, String> implements NewGKStudentRangeExService {
	@Autowired
	private NewGKStudentRangeExDao newGKStudentRangeExDao;

	@Override
	protected BaseJpaRepositoryDao<NewGKStudentRangeEx, String> getJpaDao() {
		return newGKStudentRangeExDao;
	}

	@Override
	protected Class<NewGKStudentRangeEx> getEntityClass() {
		return NewGKStudentRangeEx.class;
	}
	@Override
	public List<NewGKStudentRangeEx> findByDivideId(String divideId) {
		return newGKStudentRangeExDao.findByDivideId(divideId);
	}

	@Override
	public List<NewGKStudentRangeEx> findByDivideIdAndSubjectType(String divideId, String subjectType) {
		return newGKStudentRangeExDao.findByDivideIdAndSubjectType(divideId,subjectType);
	}

	@Override
	public void saveAndDelete(List<NewGKStudentRangeEx> exList, String divideId) {
		newGKStudentRangeExDao.deleteByDivideId(divideId);
		if(!CollectionUtils.isEmpty(exList)){
			saveAll(exList.toArray(new NewGKStudentRangeEx[0]));
		}
	}

	@Override
	public void saveAndDelete(List<NewGKStudentRangeEx> exList, String subjectType, String divideId) {
		newGKStudentRangeExDao.deleteByDivideIdAndSubjectType(divideId, subjectType);
		if(!CollectionUtils.isEmpty(exList)){
			saveAll(exList.toArray(new NewGKStudentRangeEx[0]));
		}
	}

    @Override
    public void deleteBySubjectIds(String... subIds) {
        newGKStudentRangeExDao.deleteBySubjectIdIn(subIds);
    }

	@Override
	public void deleteByDivideIdAndSubjectType(String divideId, String subjectType) {
		if(StringUtils.isBlank(subjectType)) {
			newGKStudentRangeExDao.deleteByDivideId(divideId);
		}else {
			newGKStudentRangeExDao.deleteByDivideIdAndSubjectType(divideId,subjectType);
		}
	}
}
