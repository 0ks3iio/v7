package net.zdsoft.careerplan.trade.model.result;

import com.alipay.api.response.AlipayTradeQueryResponse;

import net.zdsoft.careerplan.trade.model.TradeStatus;

/**
 * 
 */
public class AlipayF2FQueryResult implements Result {
	private TradeStatus tradeStatus;
	private AlipayTradeQueryResponse response;

    public AlipayF2FQueryResult(AlipayTradeQueryResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeQueryResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradeQueryResponse getResponse() {
        return response;
    }

    @Override
    public boolean isTradeSuccess() {
        return response != null &&
                TradeStatus.SUCCESS.equals(tradeStatus);
    }
}
