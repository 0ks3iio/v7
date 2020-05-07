package net.zdsoft.careerplan.trade.model.builder;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 
 *  创建空交易记录
 */
public class AlipayTradeCreateRequestBuilder  extends RequestBuilder{
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
		if (StringUtils.isEmpty(bizContent.subject)) {
            throw new NullPointerException("subject should not be NULL!");
        }
		if (StringUtils.isEmpty(bizContent.totalAmount)) {
            throw new NullPointerException("total_amount should not be NULL!");
        }
        return true;
	}
	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AlipayTradeCreateRequestBuilder{");
        sb.append("bizContent=").append(bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
	public String getOutTradeNo() {
        return bizContent.outTradeNo;
    }

    public AlipayTradeCreateRequestBuilder setOutTradeNo(String outTradeNo) {
        bizContent.outTradeNo = outTradeNo;
        return this;
    }
    public String getSubject() {
        return bizContent.subject;
    }

    public AlipayTradeCreateRequestBuilder setSubject(String subject) {
        bizContent.subject = subject;
        return this;
    }
    
    public String getTotalAmount() {
        return bizContent.totalAmount;
    }

    public AlipayTradeCreateRequestBuilder setTotalAmount(String totalAmount) {
        bizContent.totalAmount = totalAmount;
        return this;
    }
    
    public String getTimeoutExpress() {
        return bizContent.timeoutExpress;
    }

    public AlipayTradeCreateRequestBuilder setTimeoutExpress(String timeoutExpress) {
        bizContent.timeoutExpress = timeoutExpress;
        return this;
    }
	    
	public static class BizContent {
		
        @SerializedName("out_trade_no")
        private String outTradeNo;
        
        @SerializedName("subject")
        private String subject;
        
        @SerializedName("total_amount")
        private String totalAmount;
        
        @SerializedName("timeout_express")
        private String timeoutExpress;
        
        @SerializedName("buyer_id")
        private String buyerId;
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("outTradeNo='").append(outTradeNo).append('\'');
            sb.append("subject='").append(subject).append('\'');
            sb.append("totalAmount='").append(totalAmount).append('\'');
            sb.append("timeoutExpress='").append(timeoutExpress).append('\'');
            sb.append("buyerId='").append(buyerId).append('\'');
            sb.append('}');
            return sb.toString();
        }

    }
}
