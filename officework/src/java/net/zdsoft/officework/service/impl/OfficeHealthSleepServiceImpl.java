package net.zdsoft.officework.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.officework.dao.OfficeHealthSleepDao;
import net.zdsoft.officework.entity.OfficeHealthSleep;
import net.zdsoft.officework.service.OfficeHealthSleepService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("officeHealthSleepService")
public class OfficeHealthSleepServiceImpl extends BaseServiceImpl<OfficeHealthSleep, String> implements OfficeHealthSleepService {
	
	@Autowired
	private OfficeHealthSleepDao officeHealthSleeptDao;
	
	@Override
	protected BaseJpaRepositoryDao<OfficeHealthSleep, String> getJpaDao() {
		return officeHealthSleeptDao;
	}

	@Override
	protected Class<OfficeHealthSleep> getEntityClass() {
		return OfficeHealthSleep.class;
	}

	@Override
	public List<OfficeHealthSleep> findByDateOwnerIds(String[] dateDays,
			String[] ownerIds) {
		if(dateDays==null||dateDays.length == 0 ||ownerIds==null||ownerIds.length == 0 ){
			return Lists.newArrayList();
		}
		return officeHealthSleeptDao.findByDateOwnerIds(dateDays,ownerIds);
	}


	
	
}
