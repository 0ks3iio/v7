package net.zdsoft.careerplan.trade.model.builder;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭。
 */
public class AlipayTradeCloseRequestBuilder extends RequestBuilder {

	private BizContent bizContent = new BizContent();

    @Override
    public BizContent getBizContent() {
        return bizContent;
    }
	@Override
	public boolean validate() {
		if (StringUtils.isEmpty(bizContent.outTradeNo)) {
            throw new NullPointerException("out_trade_no should not be NULL!");
        }
        return true;
	}
	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AlipayTradeCloseRequestBuilder{");
        sb.append("bizContent=").append(bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
	 public String getOutTradeNo() {
        return bizContent.outTradeNo;
    }

    public AlipayTradeCloseRequestBuilder setOutTradeNo(String outTradeNo) {
        bizContent.outTradeNo = outTradeNo;
        return this;
    }
	    
	public static class BizContent {
		
        @SerializedName("out_trade_no")
        private String outTradeNo;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("outTradeNo='").append(outTradeNo).append('\'');
            sb.append('}');
            return sb.toString();
        }

    }
	

}
