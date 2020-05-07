package net.zdsoft.careerplan.trade.config;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.response.AlipayTradePrecreateResponse;

import net.zdsoft.careerplan.trade.model.builder.AlipayTradeCloseRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeQueryRequestBuilder;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FCloseResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FPrecreateResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FQueryResult;
import net.zdsoft.careerplan.trade.service.AlipayTradeService;
import net.zdsoft.careerplan.trade.utils.ZxingUtils;


public class Test {
	private static AlipayTradeService   tradeService;
	
	public static void main(String[] args) {
		//二维码
		String outTradeNo = "4028805f6b0da60001shadsafs0016" ;
		test1(outTradeNo);
		//test2(outTradeNo);
		//test3(outTradeNo);
		//System.out.println("1111");
		//test4(outTradeNo);
		//System.out.println("测试结果");
		
		//退款
		
		
	}
	
	 static {
	        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
	         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
	         */
		 	//AlipayConfigs.init(AlipayConfigs.FILEPATH);

	        /** 使用Configs提供的默认参数
	         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
	         */
	       // tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


	        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
//	        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
//	            .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
//	            .setFormat("json").build();
	    }
	
	public static void test1(String outTradeNo) {
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
       

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "品牌门店当面付扫码消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = "0.01";
        String discountableAmount="0.01";
        

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 支付超时，定义为120分钟
        String timeoutExpress = "3m";
//        alipayRequest.setBizContent("{" +
//        		" \"out_trade_no\":\"4028805f6b6e60ee016b6e6a6965000e\"," +
//        		" \"total_amount\":\"0.01\"," +
//        		" \"subject\":\"Iphone6 16G\"," +
//        		" \"product_code\":\"QUICK_WAP_PAY\"" +
//        		" }");//填充业务参数

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setTimeoutExpress(timeoutExpress).setDiscountableAmount(discountableAmount);
        tradeService=AlipayConfigs.tradeService;
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                System.out.println("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();

                // 需要修改为运行机器上的路径
                String filePath = String.format("G:/11/pic/qr-%s.png",
                    response.getOutTradeNo());
                System.out.println("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
            	 System.out.println("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
            	 System.out.println("系统异常，预下单状态未知!!!");
                break;

            default:
            	 System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
	}
	
	public static void test2(String outTradeNo) {
		
		AlipayTradeQueryRequestBuilder builder=new AlipayTradeQueryRequestBuilder()
				.setOutTradeNo(outTradeNo);
		tradeService=AlipayConfigs.tradeService;
		AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                System.out.println("支付宝下单成功: )");

                //AlipayTradePayResponse response = result.getResponse();
                
                break;

            case FAILED:
            	 System.out.println("支付宝下单失败!!!");
                break;

            case UNKNOWN:
            	 System.out.println("系统异常，下单状态未知!!!");
                break;

            default:
            	 System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
	}
	
	public static void test3(String outTradeNo) {
		AlipayTradeCloseRequestBuilder builder=new AlipayTradeCloseRequestBuilder()
				.setOutTradeNo(outTradeNo);
		tradeService=AlipayConfigs.tradeService;
		AlipayF2FCloseResult result = tradeService.tradeClose(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                System.out.println("关闭订单成功: )");

                //AlipayTradePayResponse response = result.getResponse();
                
                break;

            case FAILED:
            	 System.out.println("关闭订单失败!!!");
                break;

            case UNKNOWN:
            	 System.out.println("系统异常，关闭订单状态未知!!!");
                break;

            default:
            	 System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
	}
	
	public static void test4(String outTradeNo) {
		
//		AlipayTradeQueryRequestBuilder builder=new AlipayTradeQueryRequestBuilder()
//				.setOutTradeNo(outTradeNo);
//		tradeService=AlipayConfigs.tradeService;
//		AlipayF2FQueryResult result = tradeService.loopQueryTradeResult(builder);
//        switch (result.getTradeStatus()) {
//            case SUCCESS:
//                System.out.println("支付宝下单成功: )");
//
//                //AlipayTradePayResponse response = result.getResponse();
//                
//                break;
//
//            case FAILED:
//            	 System.out.println("支付宝下单失败!!!");
//                break;
//
//            case UNKNOWN:
//            	 System.out.println("系统异常，下单状态未知!!!");
//                break;
//
//            default:
//            	 System.out.println("不支持的交易状态，交易返回异常!!!");
//                break;
//        }
	}
	
	
	
	
}
