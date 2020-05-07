package net.zdsoft.careerplan.trade.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.*;

import net.zdsoft.careerplan.constant.BaseAlipayConstants;
import net.zdsoft.careerplan.trade.model.TradeStatus;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeCloseRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeCreateRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeQueryRequestBuilder;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FCloseResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FCreateResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FPrecreateResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FQueryResult;
import net.zdsoft.careerplan.trade.service.AlipayTradeService;

/**
 * 
 */
abstract class AbsAlipayTradeService extends AbsAlipayService implements AlipayTradeService {
	
    protected AlipayClient client;

    @Override
    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder) {
    	 validateBuilder(builder);
         AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
         request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
         request.setBizContent(builder.toJsonString());
        AlipayTradeQueryResponse response = (AlipayTradeQueryResponse) getResponse(client, request);
        AlipayF2FQueryResult result = new AlipayF2FQueryResult(response);
        if (querySuccess(response)) {
            // 查询返回该订单交易支付成功
            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError(response)) {
            // 查询发生异常，交易状态未知
            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {
            // 其他情况均表明该订单号交易失败
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }



    @Override
    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder) {
        validateBuilder(builder);

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(builder.getNotifyUrl());
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        log.info("trade.precreate bizContent:" + request.getBizContent());

        AlipayTradePrecreateResponse response = (AlipayTradePrecreateResponse) getResponse(client, request);

        AlipayF2FPrecreateResult result = new AlipayF2FPrecreateResult(response);
        //根据code 判断成功与失败
        if (response != null && BaseAlipayConstants.SUCCESS.equals(response.getCode())) {
            // 预下单交易成功
            result.setTradeStatus(TradeStatus.SUCCESS);

        } else if (tradeError(response)) {
            // 预下单发生异常，状态未知
            result.setTradeStatus(TradeStatus.UNKNOWN);

        } else {
            // 其他情况表明该预下单明确失败
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }

    @Override
    public AlipayF2FCloseResult tradeClose(AlipayTradeCloseRequestBuilder builder) {
    	 validateBuilder(builder);

         AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
         request.setBizContent(builder.toJsonString());
         log.info("trade.close bizContent:" + request.getBizContent());
         
         AlipayTradeCloseResponse response = (AlipayTradeCloseResponse) getResponse(client, request);

         AlipayF2FCloseResult result = new AlipayF2FCloseResult(response);
         if (response != null && BaseAlipayConstants.SUCCESS.equals(response.getCode())) {
             result.setTradeStatus(TradeStatus.SUCCESS);

         } else if (tradeError(response)) {
             result.setTradeStatus(TradeStatus.UNKNOWN);

         } else {
             // 其他情况表明该预下单明确失败
             result.setTradeStatus(TradeStatus.FAILED);
         }
         return result;
    }
    
    // 查询返回“支付成功”
    protected boolean querySuccess(AlipayTradeQueryResponse response) {
        return response != null &&
        		BaseAlipayConstants.SUCCESS.equals(response.getCode()) &&
                ("TRADE_SUCCESS".equals(response.getTradeStatus()) ||
                        "TRADE_FINISHED".equals(response.getTradeStatus())
                );
    }

    // 交易异常，或发生系统错误
    protected boolean tradeError(AlipayResponse response) {
        return response == null ||
        		BaseAlipayConstants.ERROR.equals(response.getCode());
    }
    
    public AlipayF2FCreateResult tradeCreate(AlipayTradeCreateRequestBuilder builder) {
    	validateBuilder(builder);
    	AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(builder.getNotifyUrl());
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        log.info("trade.create bizContent:" + request.getBizContent());
        AlipayTradeCreateResponse response = (AlipayTradeCreateResponse)getResponse(client, request);
        AlipayF2FCreateResult result = new AlipayF2FCreateResult(response);
        //根据code 判断成功与失败
        if (response != null && BaseAlipayConstants.SUCCESS.equals(response.getCode())) {
            //成功
            result.setTradeStatus(TradeStatus.SUCCESS);

        } else if (tradeError(response)) {
            // 预下单发生异常，状态未知
            result.setTradeStatus(TradeStatus.UNKNOWN);

        } else {
            // 其他情况表明该预下单明确失败
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }
}
