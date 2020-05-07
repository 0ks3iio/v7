package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccTimingLogDao;
import net.zdsoft.eclasscard.data.entity.EccTimingLog;
import net.zdsoft.eclasscard.data.service.EccTimingLogService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
@Service
public class EccTimingLogServiceImpl extends BaseServiceImpl<EccTimingLog, String> implements EccTimingLogService{
	@Autowired
	private EccTimingLogDao eccTimingLogDao;

	@Override
	protected BaseJpaRepositoryDao<EccTimingLog, String> getJpaDao() {
		
		return eccTimingLogDao;
	}

	@Override
	protected Class<EccTimingLog> getEntityClass() {
		return EccTimingLog.class;
	}

	@Override
	public List<EccTimingLog> findNewTimeList(String[] unitId) {
		List<EccTimingLog> list = eccTimingLogDao.findByUnitIdIn(unitId);
		//去掉多余
		Set<String> cardIdSet=new HashSet<>();
		List<EccTimingLog> rList=new ArrayList<EccTimingLog>();
		for(EccTimingLog e:list) {
			if(cardIdSet.contains(e.getCardId())) {
				continue;
			}
			rList.add(e);
			cardIdSet.add(e.getCardId());
		}
		return rList;
	}


	@Override
	public void saveTimeLog(EccTimingLog timeLog,EccTimingLog otherLog) {
		List<EccTimingLog> logList=new ArrayList<>();
		if(timeLog!=null) {
			eccTimingLogDao.updateAll(timeLog.getUnitId(),timeLog.getCardId(),timeLog.getCreationTime());
			logList.add(timeLog);
		}
		if(otherLog!=null) {
			logList.add(otherLog);
		}
		
		if(CollectionUtils.isNotEmpty(logList)) {
			eccTimingLogDao.saveAll(logList);
		}
		
	}

}
