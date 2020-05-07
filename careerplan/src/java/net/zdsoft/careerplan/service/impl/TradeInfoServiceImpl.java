package net.zdsoft.careerplan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.careerplan.dao.TradeInfoDao;
import net.zdsoft.careerplan.service.TradeInfoService;
import net.zdsoft.careerplan.entity.TradeInfo;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
@Service("tradeInfoService")
public class TradeInfoServiceImpl extends BaseServiceImpl<TradeInfo, String>  implements TradeInfoService{

	@Autowired
	private TradeInfoDao tradeInfoDao;
	@Override
	protected BaseJpaRepositoryDao<TradeInfo, String> getJpaDao() {
		return tradeInfoDao;
	}

	@Override
	protected Class<TradeInfo> getEntityClass() {
		return TradeInfo.class;
	}

	@Override
	public TradeInfo findByTradeType(String tradeType) {
		return tradeInfoDao.findByTradeType(tradeType);
	}
	
	

}
