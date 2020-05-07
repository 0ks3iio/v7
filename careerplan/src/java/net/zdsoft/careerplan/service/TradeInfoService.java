package net.zdsoft.careerplan.service;

import net.zdsoft.careerplan.entity.TradeInfo;
import net.zdsoft.basedata.service.BaseService;

public interface TradeInfoService extends BaseService<TradeInfo, String>{
	
	public TradeInfo findByTradeType(String tradeType);

}
