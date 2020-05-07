package net.zdsoft.careerplan.trade.model.builder;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.StringUtils;

/**
 * 预下单
 */
public class AlipayTradePrecreateRequestBuilder extends RequestBuilder {

    private BizContent bizContent = new BizContent();

    @Override
    public BizContent getBizContent() {
        return bizContent;
    }

    /**
     * 验证请求参数完整性
     */
    @Override
    public boolean validate() {
        if (StringUtils.isEmpty(bizContent.outTradeNo)) {
            throw new NullPointerException("out_trade_no should not be NULL!");
        }
        if (StringUtils.isEmpty(bizContent.totalAmount)) {
            throw new NullPointerException("total_amount should not be NULL!");
        }
        if (StringUtils.isEmpty(bizContent.subject)) {
            throw new NullPointerException("subject should not be NULL!");
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AlipayTradePrecreateRequestBuilder{");
        sb.append("bizContent=").append(bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public AlipayTradePrecreateRequestBuilder setAppAuthToken(String appAuthToken) {
        return (AlipayTradePrecreateRequestBuilder) super.setAppAuthToken(appAuthToken);
    }

    @Override
    public AlipayTradePrecreateRequestBuilder setNotifyUrl(String notifyUrl) {
        return (AlipayTradePrecreateRequestBuilder) super.setNotifyUrl(notifyUrl);
    }

    public String getOutTradeNo() {
        return bizContent.outTradeNo;
    }

    public AlipayTradePrecreateRequestBuilder setOutTradeNo(String outTradeNo) {
        bizContent.outTradeNo = outTradeNo;
        return this;
    }

    public String getSellerId() {
        return bizContent.sellerId;
    }

    public AlipayTradePrecreateRequestBuilder setSellerId(String sellerId) {
        bizContent.sellerId = sellerId;
        return this;
    }

    public String getTotalAmount() {
        return bizContent.totalAmount;
    }

    public AlipayTradePrecreateRequestBuilder setTotalAmount(String totalAmount) {
        bizContent.totalAmount = totalAmount;
        return this;
    }

    public String getSubject() {
        return bizContent.subject;
    }

    public AlipayTradePrecreateRequestBuilder setSubject(String subject) {
        bizContent.subject = subject;
        return this;
    }

    public String getTimeoutExpress() {
        return bizContent.timeoutExpress;
    }

    public AlipayTradePrecreateRequestBuilder setTimeoutExpress(String timeoutExpress) {
        bizContent.timeoutExpress = timeoutExpress;
        return this;
    }
    
    public String getQrCodeTimeoutExpress() {
        return bizContent.qrCodeTimeoutExpress;
    }

    public AlipayTradePrecreateRequestBuilder setQrCodeTimeoutExpress(String qrCodeTimeoutExpress) {
        bizContent.qrCodeTimeoutExpress = qrCodeTimeoutExpress;
        return this;
    } 
    
    public String getDiscountableAmount() {
        return bizContent.discountableAmount;
    }

    public AlipayTradePrecreateRequestBuilder setDiscountableAmount(String discountableAmount) {
        bizContent.discountableAmount = discountableAmount;
        return this;
    }
    //请求参数
    public static class BizContent {
    	// 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复 ---必填
        @SerializedName("out_trade_no")
        private String outTradeNo;//换算成本系统就是生成的订单号id

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID---可选
        @SerializedName("seller_id")
        private String sellerId;

        // 订单总金额，此处单位为元，精确到小数点后2位，不能超过1亿元
        @SerializedName("total_amount")
        private String totalAmount;
        
        // 订单标题，粗略描述用户的支付目的。如“****消费”
        @SerializedName("subject")
        private String subject;
        
        @SerializedName("discountable_amount")
        private String discountableAmount;


        // 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss --暂时不用
//        @SerializedName("time_expire ")
//        private String timeExpire ;
        
        //(相对时间) 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m 
        @SerializedName("timeout_express")
        private String timeoutExpress;
        //二维码的失效时间
        @SerializedName("qr_code_timeout_express ")
        private String qrCodeTimeoutExpress;
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("outTradeNo='").append(outTradeNo).append('\'');
            sb.append(", sellerId='").append(sellerId).append('\'');
            sb.append(", totalAmount='").append(totalAmount).append('\'');
            sb.append(", subject='").append(subject).append('\'');
            sb.append(", timeoutExpress='").append(timeoutExpress).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
