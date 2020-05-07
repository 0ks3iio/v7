package net.zdsoft.studevelop.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuLeagueRecordDao;
import net.zdsoft.studevelop.data.entity.StuLeagueRecord;
import net.zdsoft.studevelop.data.service.StuLeagueRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("StuLeagueRecordService")
public class StuLeagueRecordServiceImpl extends BaseServiceImpl<StuLeagueRecord,String> implements StuLeagueRecordService{

	@Autowired
	private StuLeagueRecordDao stuLeagueRecordDao;
	
	@Override
	public List<StuLeagueRecord> findListByCls(String acadyear,
			String semester, String[] array) {
		return stuLeagueRecordDao.findListByCls(acadyear,semester,array);
	}
	
	@Override
	public List<StuLeagueRecord> getStuLeagueRecordList(String stuId,
			String acadyear, String semester) {
		return stuLeagueRecordDao.getStuLeagueRecordList(stuId,acadyear,semester);
	}

	@Override
	public StuLeagueRecord findById(String id) {
		return stuLeagueRecordDao.findById(id).orElse(null);
	}

	@Override
	protected BaseJpaRepositoryDao<StuLeagueRecord, String> getJpaDao() {
		return stuLeagueRecordDao;
	}

	@Override
	protected Class<StuLeagueRecord> getEntityClass() {
		return StuLeagueRecord.class;
	}


}
