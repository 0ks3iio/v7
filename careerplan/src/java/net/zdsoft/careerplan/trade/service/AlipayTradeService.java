package net.zdsoft.careerplan.trade.service;


import net.zdsoft.careerplan.trade.model.builder.AlipayTradeCloseRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeCreateRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeQueryRequestBuilder;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FCloseResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FCreateResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FPrecreateResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FQueryResult;

/**
 * 调用支付宝
 */
public interface AlipayTradeService {
	
    // 查询--获取结果
    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder);
    
    // 预下单(生成二维码)---但是其实没有生成订单 所以根据二维码失效时间考虑
    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder);
    
    //主动关闭订单--用于未付款 但是已经扫码后的交易进行关闭
    public AlipayF2FCloseResult tradeClose(AlipayTradeCloseRequestBuilder builder);
    
    //创建下单
    public AlipayF2FCreateResult tradeCreate(AlipayTradeCreateRequestBuilder builder);
    
}
