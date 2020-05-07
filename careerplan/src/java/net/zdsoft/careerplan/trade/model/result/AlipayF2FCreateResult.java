package net.zdsoft.careerplan.trade.model.result;

import com.alipay.api.response.AlipayTradeCreateResponse;

import net.zdsoft.careerplan.trade.model.TradeStatus;

public class AlipayF2FCreateResult implements Result{
	private TradeStatus tradeStatus;
    private AlipayTradeCreateResponse response;

    public AlipayF2FCreateResult(AlipayTradeCreateResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeCreateResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradeCreateResponse getResponse() {
        return response;
    }

    @Override
    public boolean isTradeSuccess() {
        return response != null &&
                TradeStatus.SUCCESS.equals(tradeStatus);
    }
}
