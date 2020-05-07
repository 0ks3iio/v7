package net.zdsoft.careerplan.trade.model.result;

import com.alipay.api.response.AlipayTradeCloseResponse;

import net.zdsoft.careerplan.trade.model.TradeStatus;

/**
 * 主动关闭订单
 *
 */
public class AlipayF2FCloseResult implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradeCloseResponse response;

    public AlipayF2FCloseResult(AlipayTradeCloseResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeCloseResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradeCloseResponse getResponse() {
        return response;
    }

    @Override
    public boolean isTradeSuccess() {
        return response != null &&
                TradeStatus.SUCCESS.equals(tradeStatus);
    }

}
