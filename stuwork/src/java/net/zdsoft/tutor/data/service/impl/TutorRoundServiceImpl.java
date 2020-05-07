package net.zdsoft.tutor.data.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.dao.TutorRoundDao;
import net.zdsoft.tutor.data.entity.TutorRound;
import net.zdsoft.tutor.data.service.TutorRoundService;

/**
 * @author yangsj  2017年9月11日下午8:29:53
 */
@Service
public class TutorRoundServiceImpl extends BaseServiceImpl<TutorRound,String> implements TutorRoundService {
    
	@Autowired
	private TutorRoundDao tutorRoundDao;
	
	@Override
	protected BaseJpaRepositoryDao<TutorRound, String> getJpaDao() {
		// TODO Auto-generated method stub
		return tutorRoundDao;
	}

	@Override
	protected Class<TutorRound> getEntityClass() {
		// TODO Auto-generated method stub
		return TutorRound.class;
	}

	@Override
	public List<TutorRound> findByUnitId(String unitId) {
		// TODO Auto-generated method stub
		return tutorRoundDao.findByUnitId(unitId);
	}


}
