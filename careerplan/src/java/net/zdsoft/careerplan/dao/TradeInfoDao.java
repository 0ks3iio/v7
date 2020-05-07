package net.zdsoft.careerplan.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.careerplan.entity.TradeInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TradeInfoDao extends BaseJpaRepositoryDao<TradeInfo, String>{

	@Query(" From TradeInfo where tradeType=?1 and isDeleted=0")
	public TradeInfo findByTradeType(String tradeType);
}
