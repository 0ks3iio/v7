package net.zdsoft.studevelop.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StuDevelopMannerRecordDao;
import net.zdsoft.studevelop.data.entity.StuDevelopMannerRecord;
import net.zdsoft.studevelop.data.service.StuDevelopMannerRecordService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("stuDevelopMannerRecordService")
public class StuDevelopMannerRecordServiceImpl extends BaseServiceImpl<StuDevelopMannerRecord, String> implements StuDevelopMannerRecordService{
    @Autowired
	private StuDevelopMannerRecordDao stuDevelopMannerRecordDao;
	
	@Override
	protected BaseJpaRepositoryDao<StuDevelopMannerRecord, String> getJpaDao() {
		return stuDevelopMannerRecordDao;
	}

	@Override
	protected Class<StuDevelopMannerRecord> getEntityClass() {
		return StuDevelopMannerRecord.class;
	}

	@Override
	public List<StuDevelopMannerRecord> findListByCls(String acadyear, String semester,
			String subjId, String[] stuIds) {
		return stuDevelopMannerRecordDao.findListByCls(acadyear, semester, subjId, stuIds);
	}

	@Override
	public void bacthSave(List<StuDevelopMannerRecord> mannerRecordList) {
		for(StuDevelopMannerRecord item : mannerRecordList){
			if(StringUtils.isBlank(item.getId())){
				item.setId(UuidUtils.generateUuid());
			}
		}
		stuDevelopMannerRecordDao.saveAll(mannerRecordList);
	}

	@Override
	public List<StuDevelopMannerRecord> findListByStu(String acadyear, String semester,
			String stuId, String subjectId) {		
		if(StringUtils.isNotBlank(subjectId)){
			return stuDevelopMannerRecordDao.findListByStuAndSub(acadyear, semester, stuId, subjectId);
		}else{
			return stuDevelopMannerRecordDao.findListByStu(acadyear, semester,stuId);
		}
	}

}
