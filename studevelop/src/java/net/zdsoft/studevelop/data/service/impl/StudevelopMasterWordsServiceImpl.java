package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StudevelopMasterWordsDao;
import net.zdsoft.studevelop.data.entity.StudevelopMasterWords;
import net.zdsoft.studevelop.data.service.StudevelopMasterWordsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * studevelop_master_words
 * @author 
 * 
 */
@Service("studevelopMasterWordsService")
public class StudevelopMasterWordsServiceImpl extends BaseServiceImpl<StudevelopMasterWords, String> implements StudevelopMasterWordsService{
	@Autowired
	private StudevelopMasterWordsDao studevelopMasterWordsDao;

	@Override
	public Integer delete(String[] ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(StudevelopMasterWords studevelopMasterWords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudevelopMasterWords> getStudevelopMasterWordsByUnitId(
			String unitId) {
		return studevelopMasterWordsDao.findByUnitId(unitId);
	}

	@Override
	protected BaseJpaRepositoryDao<StudevelopMasterWords, String> getJpaDao() {
		return studevelopMasterWordsDao;
	}

	@Override
	protected Class<StudevelopMasterWords> getEntityClass() {
		return StudevelopMasterWords.class;
	}

}
