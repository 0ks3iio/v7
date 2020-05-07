package net.zdsoft.tutor.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.dao.TutorParamDao;
import net.zdsoft.tutor.data.entity.TutorParam;
import net.zdsoft.tutor.data.service.TutorParamService;

/**
 * @author yangsj  2017年9月11日下午8:21:11
 */
@Service
public class TutorParamServiceImpl extends BaseServiceImpl<TutorParam, String> implements TutorParamService {
    
	@Autowired
	private TutorParamDao tutorParamDao;
	
	@Override
	protected BaseJpaRepositoryDao<TutorParam, String> getJpaDao() {
		// TODO Auto-generated method stub
		return tutorParamDao;
	}

	@Override
	protected Class<TutorParam> getEntityClass() {
		// TODO Auto-generated method stub
		return TutorParam.class;
	}

	@Override
	public TutorParam findByUnitIdAndPtype(String unitId, String paramType) {
		// TODO Auto-generated method stub
		return tutorParamDao.findByUnitIdAndPtype(unitId,paramType);
	}

}
