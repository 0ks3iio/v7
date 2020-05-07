package net.zdsoft.tutor.data.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.dao.TutorRoundGradeDao;
import net.zdsoft.tutor.data.entity.TutorRoundGrade;
import net.zdsoft.tutor.data.service.TutorRoundGradeService;

/**
 * @author yangsj  2017年9月11日下午8:31:25
 */
@Service
public class TutorRoundGradeServiceImpl extends BaseServiceImpl<TutorRoundGrade, String> implements TutorRoundGradeService {
    
	@Autowired
	private TutorRoundGradeDao tutorRoundGradeDao;
	
	@Override
	protected BaseJpaRepositoryDao<TutorRoundGrade, String> getJpaDao() {
		// TODO Auto-generated method stub
		return tutorRoundGradeDao;
	}

	@Override
	protected Class<TutorRoundGrade> getEntityClass() {
		// TODO Auto-generated method stub
		return TutorRoundGrade.class;
	}

	@Override
	public List<TutorRoundGrade> findByUnitId(String unitId) {
		// TODO Auto-generated method stub
		return tutorRoundGradeDao.findByUnitId(unitId);
	}

	@Override
	public List<TutorRoundGrade> findByRoundId(String roundId) {
		// TODO Auto-generated method stub
		return tutorRoundGradeDao.findByRoundId(roundId);
	}

	@Override
	public void deleteByRoundId(String roundId) {
		// TODO Auto-generated method stub
		tutorRoundGradeDao.deleteByRoundId(roundId);
	}

	@Override
	public List<TutorRoundGrade> findbyGradeIdsAndUnitId(String[] gradeIds, String unitId) {
		// TODO Auto-generated method stub
		return tutorRoundGradeDao.findbyGradeIdsAndUnitId(gradeIds,unitId);
	}

	@Override
	public List<TutorRoundGrade> findbyGradeIds(String... gradeIds) {
		// TODO Auto-generated method stub
		return tutorRoundGradeDao.findbyGradeIds(gradeIds);
	}

	


}
