package net.zdsoft.studevelop.data.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuHealthRecordDao;
import net.zdsoft.studevelop.data.entity.StuHealthRecord;
import net.zdsoft.studevelop.data.service.StuHealthRecordService;

import java.util.List;


@Service("stuHealthRecordService")
public class StuHealthRecordServiceImpl extends BaseServiceImpl<StuHealthRecord,String> implements StuHealthRecordService{

	@Autowired
	private StuHealthRecordDao stuHealthRecordDao;
	private Logger logger = Logger.getLogger(StuHealthRecordServiceImpl.class);
	@Override
	public StuHealthRecord getHealthRecordByStuIdSemes(String stuId,
			String acadyear, String semester) {
		List<StuHealthRecord> recordList = stuHealthRecordDao.getHealthRecordByStuIdSemes(stuId,acadyear,semester);
		if(CollectionUtils.isNotEmpty(recordList)){
			logger.info("start");
			return recordList.get(0);
		}else{
			return null;
		}

	}
	
	@Override
	protected BaseJpaRepositoryDao<StuHealthRecord, String> getJpaDao() {
		return stuHealthRecordDao;
	}

	@Override
	protected Class<StuHealthRecord> getEntityClass() {
		return StuHealthRecord.class;
	}

}
