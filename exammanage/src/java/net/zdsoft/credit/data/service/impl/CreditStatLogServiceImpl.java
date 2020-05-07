package net.zdsoft.credit.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.dao.CreditStatLogDao;
import net.zdsoft.credit.data.entity.CreditStatLog;
import net.zdsoft.credit.data.service.CreditStatLogService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("creditStatLogService")
public class CreditStatLogServiceImpl extends BaseServiceImpl<CreditStatLog, String> implements CreditStatLogService {
    @Autowired
    private CreditStatLogDao creditStatLogDao;
    
    @Override
    public CreditStatLog findBySetIdAndGradeId(String setId, String gradeId) {
    	return creditStatLogDao.findBySetIdAndGradeId(setId,gradeId);
    }
    
	@Override
	protected BaseJpaRepositoryDao<CreditStatLog, String> getJpaDao() {
		return creditStatLogDao;
	}

	@Override
	protected Class<CreditStatLog> getEntityClass() {
		return CreditStatLog.class;
	}

	@Override
	public void deleteByParams(String year, String semster, String gradeId) {
		creditStatLogDao.deleteByParams(year, semster, gradeId);
	}
}
