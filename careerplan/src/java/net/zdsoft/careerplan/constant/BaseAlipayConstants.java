package net.zdsoft.careerplan.constant;

public class BaseAlipayConstants {
	
	public static final String ZFB_LOGO="/static/images/pay/alipay.png";
	
	/**
	 * 生涯规划支付业务
	 */
	public static final String  PAY_TYPE_01 = "01";
	
	/**
	 * 订单状态标志
	 */
	public static final String TRADE_STATE_R_03="03";//订单支付成功，金额匹配
	public static final String TRADE_STATE_R_04="04";//订单支付成功,但是金额不匹配
	public static final String TRADE_STATE_R_05="05";//订单支付失败或者主动关闭
	
	
	// 成功
	public static final String PAYING  = "10003"; // 用户支付中
	
	
	
	//查询公用参数及如果 code
	public static final String SUCCESS = "10000"; //业务调用成功
	public static final String FAILED= "40004"; //业务调用失败
	public static final String ERROR = "20000"; // 系统异常，服务不可用
	
	//其他结果code--暂时不用
	public static final String ERROR_1 = "20001";//授权权限不足
	public static final String ERROR_2 = "40001";//缺少必选参数
	public static final String ERROR_3 = "40002";//非法参数
	public static final String ERROR_4 = "40006";//权限不足
	
	
	//查询支付宝交易结果 trade_status--能获取结果前提code=SUCCESS
	public static final String QUERY_01="WAIT_BUYER_PAY";//交易创建，等待买家付款
	public static final String QUERY_02="TRADE_CLOSED";//未付款交易超时关闭，或支付完成后全额退款
	public static final String QUERY_03="TRADE_SUCCESS";//交易支付成功
	public static final String QUERY_04="TRADE_FINISHED";//交易结束，不可退款

}
