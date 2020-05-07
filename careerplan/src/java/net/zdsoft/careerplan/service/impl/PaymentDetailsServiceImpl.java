package net.zdsoft.careerplan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.response.AlipayTradePrecreateResponse;

import net.zdsoft.careerplan.constant.BaseAlipayConstants;
import net.zdsoft.careerplan.dao.PaymentDetailsDao;
import net.zdsoft.careerplan.service.PaymentDetailsService;
import net.zdsoft.careerplan.service.TradeInfoService;
import net.zdsoft.careerplan.trade.config.AlipayConfigs;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeCloseRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import net.zdsoft.careerplan.trade.model.builder.AlipayTradeQueryRequestBuilder;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FCloseResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FPrecreateResult;
import net.zdsoft.careerplan.trade.model.result.AlipayF2FQueryResult;
import net.zdsoft.careerplan.trade.service.AlipayTradeService;
import net.zdsoft.careerplan.entity.PaymentDetails;
import net.zdsoft.careerplan.entity.TradeInfo;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
@Service("paymentDetailsService")
public class PaymentDetailsServiceImpl extends BaseServiceImpl<PaymentDetails, String>
	implements PaymentDetailsService{
	protected static final Logger log = Logger.getLogger(PaymentDetailsService.class);
	@Autowired
	private PaymentDetailsDao paymentDetailsDao;
	@Autowired
	private TradeInfoService tradeInfoService;

	@Override
	public boolean checkByOrderTypeAndUserIdWithMaster(String orderType, String userId) {
		
        PaymentDetails ent =this.saveOrFindOne(orderType, userId,false);
        if(ent!=null && BaseAlipayConstants.TRADE_STATE_R_03.equals(ent.getTradeStatus())){
        	return true;
        }
		return false;
	}

	@Override
	protected BaseJpaRepositoryDao<PaymentDetails, String> getJpaDao() {
		return paymentDetailsDao;
	}

	@Override
	protected Class<PaymentDetails> getEntityClass() {
		return PaymentDetails.class;
	}

	/**
	 * 生成预订单-根据用户加锁
	 * 存在已付款完成的订单 返回完成的订单
	 */
	@Override
	public PaymentDetails saveOrFindOne(String orderType, String userId,boolean creatEnt) {
		//防止重复 加锁
        //加分布式锁以防止可能发生的重复初始化现象
		PaymentDetails paymentDetails=null;
		RedisUtils.hasLocked("payDetailInitLock_" + userId+"_"+orderType );
		try {
			TradeInfo tradeInfo = tradeInfoService.findByTradeType(BaseAlipayConstants.PAY_TYPE_01);
			AlipayTradeService tradeService = AlipayConfigs.tradeService;
			//1、查询用户合理订单，默认去除失败，交易过时自动关闭
			//2、如果订单未完成状态 但对应二维码失效时间已过，去
			
			PaymentDetails pdSuccess=null;
			//所有订单
			List<PaymentDetails> paymentDetailsList = paymentDetailsDao.findByOrderTypeAndUserId(orderType,userId);
			//第一次创建
			if(CollectionUtils.isEmpty(paymentDetailsList) && creatEnt) {
				paymentDetails = this.toPaymentDetails(tradeInfo, userId, tradeService);
			}else {
				//检查这个人所有订单情况
				Map<String,List<PaymentDetails>>map =toCheckList(paymentDetailsList, tradeService);
				
				List<PaymentDetails> list1 =map.get("1");
				if(CollectionUtils.isNotEmpty(list1)){
					pdSuccess = list1.get(0);
				}
				
				//关闭多个进行中的只保留最新的一个
				List<PaymentDetails> list0 =map.get("0");
				if(CollectionUtils.isNotEmpty(list0)){
					int i=0;
					for(PaymentDetails ent: list0){
						if(i==0 && pdSuccess==null){
							paymentDetails=ent;
							continue;
						}
						
						AlipayTradeCloseRequestBuilder  cbuilder =new AlipayTradeCloseRequestBuilder().setOutTradeNo(ent.getId());
						AlipayF2FCloseResult cresult =tradeService.tradeClose(cbuilder);
						if(cresult.isTradeSuccess()){
							this.updateToEnt(ent, BaseAlipayConstants.TRADE_STATE_R_05,"支付宝中信息TradeStatus:"+cresult.getResponse().getCode()+"msg:"+cresult.getResponse().getMsg()+",submsg:"+cresult.getResponse().getSubMsg(), 
									null);
						}
						i+=1;
					}
				}
				
				if(pdSuccess!=null){
					RedisUtils.unLock("payDetailInitLock_" + userId+"_"+orderType);
					return pdSuccess;
				}
				
				if(paymentDetails==null && creatEnt){
					paymentDetails = this.toPaymentDetails(tradeInfo, userId, tradeService);
				}
			}
		} finally {
			RedisUtils.unLock("payDetailInitLock_" + userId+"_"+orderType);
		}
		return paymentDetails;
	}
	
	private Map<String,List<PaymentDetails>> toCheckList(List<PaymentDetails> paymentDetailsList,AlipayTradeService tradeService){
		Map<String,List<PaymentDetails>>map =new HashMap<String,List<PaymentDetails>>();
		
		for(PaymentDetails ent:paymentDetailsList ){
			//过滤已经标记过的支付记录（订单支付失败、成功、订单支付成功 但是金额不匹配）
			if(BaseAlipayConstants.TRADE_STATE_R_05.equals(ent.getTradeStatus()) ||BaseAlipayConstants.TRADE_STATE_R_04.equals(ent.getTradeStatus())){
				continue;
			}
			if(BaseAlipayConstants.TRADE_STATE_R_03.equals(ent.getTradeStatus())){
				List<PaymentDetails> list = map.get("1");
				if(CollectionUtils.isEmpty(list)){
					list=new ArrayList<>();
				}
				list.add(ent);
				map.put("1", list);
				continue;
			}
			//根据订单查询支付宝接口
			PaymentDetails cent =this.getAlipayCheck(ent);
			if(BaseAlipayConstants.TRADE_STATE_R_03.equals(cent.getTradeStatus())){
				List<PaymentDetails> list = map.get("1");
				if(CollectionUtils.isEmpty(list)){
					list=new ArrayList<>();
				}
				list.add(ent);
				map.put("1", list);
			//其实为空的就是进行中
			}else if(!(BaseAlipayConstants.TRADE_STATE_R_03.equals(BaseAlipayConstants.TRADE_STATE_R_04)
					&&BaseAlipayConstants.TRADE_STATE_R_03.equals(BaseAlipayConstants.TRADE_STATE_R_05))){
				List<PaymentDetails> list = map.get("0");
				if(CollectionUtils.isEmpty(list)){
					list=new ArrayList<>();
				}
				list.add(ent);
				map.put("0", list);
			}
		}
		
		return map;
	}
	
	private PaymentDetails updateToEnt(PaymentDetails ent,String status,String msg,String amount){
		ent.setTradeStatus(status);
		ent.setModifyTime(new Date());
		if(StringUtils.isNotBlank(amount))
			ent.setOrderAmount(Double.valueOf(amount));
		ent.setMsg(msg);
		this.save(ent);
		return ent;
	}
	
	private PaymentDetails toPaymentDetails(TradeInfo tradeInfo,String userId,AlipayTradeService tradeService){
		PaymentDetails paymentDetails=null;
		//自动生成二维码
        String totalAmount = String.valueOf(tradeInfo.getAmountMoney());
        // 创建扫码支付请求builder，设置请求参数
        String outTradeNo = UuidUtils.generateUuid();
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(tradeInfo.getTradeName()).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setTimeoutExpress("2m")
        	.setQrCodeTimeoutExpress("2m");
        	//如果没有传递return_url参数，在二维码页面，扫码成功以后页面是不会跳转，直接停留在二维码页面.
      		//alipayRequest.setReturnUrl(AlipayConfig.return_url);
      		//alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
//				AlipayTradeCreateRequestBuilder builder1=new AlipayTradeCreateRequestBuilder()
//				.setOutTradeNo(outTradeNo).setSubject(tradeInfo.getTradeName())
//				.setTotalAmount(totalAmount).setTimeoutExpress(AlipayConfigs.getTimeoutExpress());;
//				//预下单之后同时创建交易订单
//				AlipayF2FCreateResult result1 = tradeService.tradeCreate(builder1);
//				switch (result1.getTradeStatus()) {
//					case SUCCESS:
//						log.info(outTradeNo+":创建交易订单成功!!!");
//						break;
//					case FAILED:
//						log.info(outTradeNo+":创建交易订单状态失败!!!");
//		                break;
//					case UNKNOWN:
//		            	log.info(outTradeNo+":系统异常，创建交易订单状态未知!!!");
//		                break;
//		            default:
//		            	log.info(outTradeNo+":不支持的交易状态，交易返回异常!!!");
//	                break;
//            	}
            	//不管创建交易订单是否成功 还是保存一份数据
                AlipayTradePrecreateResponse response = result.getResponse();
                paymentDetails=new PaymentDetails();
    			paymentDetails.setId(outTradeNo);
    			paymentDetails.setCommodityAmount(tradeInfo.getAmountMoney());
    			//paymentDetails.setOrderAmount(tradeInfo.getAmountMoney());
    			paymentDetails.setOrderName(tradeInfo.getTradeName());
    			paymentDetails.setOrderType(tradeInfo.getTradeType());
    			paymentDetails.setUserId(userId);
    			Date date=new Date();
    			paymentDetails.setModifyTime(date);
    			paymentDetails.setCreationTime(date);
    			//默认状态为空还在等待支付中
//    			paymentDetails.setTradeStatus(BaseAlipayConstants.TRADE_STATE_P_01);
    			//支付宝60分钟 本地90分钟
    			paymentDetails.setInvalidTime(DateUtils.addMinute(date,Integer.valueOf(AlipayConfigs.getTimeoutDate())));
                paymentDetails.setQrCode(response.getQrCode());
                save(paymentDetails);
                break;

            case FAILED:
            	log.info(outTradeNo+":支付宝预下单失败!!!");
                break;

            case UNKNOWN:
            	log.info(outTradeNo+":系统异常，预下单状态未知!!!");
            	 //System.out.println("系统异常，预下单状态未知!!!");
                break;

            default:
            	log.info(outTradeNo+":不支持的交易状态，交易返回异常!!!");
            	 //System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return paymentDetails;
	}
	
	private PaymentDetails getAlipayCheck(PaymentDetails ent){
		AlipayTradeService tradeService = AlipayConfigs.tradeService;
		//进行中或者未处理的支付记录
		AlipayTradeQueryRequestBuilder builder=new AlipayTradeQueryRequestBuilder().setOutTradeNo(ent.getId());
		AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
		//result.getResponse()有可能访问频繁，连接超时报错
		//1成功状态
		if(result.getResponse()!=null && BaseAlipayConstants.SUCCESS.equals(result.getResponse().getCode()) 
				&& BaseAlipayConstants.QUERY_03.equals(result.getResponse().getTradeStatus())){
			//成功有两个情况 一种是成功，一个金额不一直成功---buyerPayAmount这个选填 默认支付总金额TotalAmount
			String buyerPayAmount = result.getResponse().getTotalAmount();
			if(ent.getCommodityAmount() ==Double.valueOf(buyerPayAmount)){
				ent= this.updateToEnt(ent, BaseAlipayConstants.TRADE_STATE_R_03, 
						"支付宝中信息TradeStatus:"+result.getResponse().getTradeStatus()+"msg:"+result.getResponse().getMsg()+",submsg:"+result.getResponse().getSubMsg(),
						buyerPayAmount);
			}else{
				ent= this.updateToEnt(ent, BaseAlipayConstants.TRADE_STATE_R_04, 
						"支付宝中信息TradeStatus:"+result.getResponse().getTradeStatus()+"msg:"+result.getResponse().getMsg()+",submsg:"+result.getResponse().getSubMsg(), 
						buyerPayAmount);
				try {
					this.ToQQMail(ent.getId(), ent.getCommodityAmount(), ent.getOrderAmount());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//2支付进行中
		}else if(result.getResponse()!=null  && BaseAlipayConstants.SUCCESS.equals(result.getResponse().getCode()) 
				&& BaseAlipayConstants.QUERY_01.equals(result.getResponse().getTradeStatus())){
				//未知的继续等待  如接近截止时间
				if(DateUtils.compareIgnoreSecond(ent.getInvalidTime(),new Date())<=0){
					AlipayTradeCloseRequestBuilder  cbuilder =new AlipayTradeCloseRequestBuilder().setOutTradeNo(ent.getId());
					AlipayF2FCloseResult cresult =tradeService.tradeClose(cbuilder);
					if(cresult.getResponse().isSuccess()){
						ent= this.updateToEnt(ent, BaseAlipayConstants.TRADE_STATE_R_05, 
								"支付宝中信息TradeStatus:"+cresult.getResponse().getCode()+"msg:"+cresult.getResponse().getMsg()+",submsg:"+cresult.getResponse().getSubMsg(), null);
					}else{
						//关闭失败继续下面
					}
				}
		//未付款超时等关闭
		}else if(result.getResponse()!=null  && BaseAlipayConstants.SUCCESS.equals(result.getResponse().getCode()) 
				&& BaseAlipayConstants.QUERY_02.equals(result.getResponse().getTradeStatus())){
			
			ent= this.updateToEnt(ent, BaseAlipayConstants.TRADE_STATE_R_05,
					"支付宝中信息TradeStatus:"+result.getResponse().getTradeStatus()+"msg:"+result.getResponse().getMsg()+",submsg:"+result.getResponse().getSubMsg()
					, result.getResponse().getTotalAmount());
		//查下状态未扫码	
		}else if(result.getResponse()==null || BaseAlipayConstants.FAILED.equals(result.getResponse().getCode())){
			//未知的继续等待  如接近截止时间  关闭失效会导致保存二维码 可以支付成功
//			if(DateUtils.compareIgnoreSecond(ent.getInvalidTime(),new Date())<=0){
//				ent=  this.updateToEnt(ent, BaseAlipayConstants.TRADE_STATE_R_05, "未扫码订单记录关闭", null);
//			}		
		}
		return ent;
	}

	@Override
	public PaymentDetails findByOrderTypeAndOrderIdAndUserIdWithMaster(String orderType, String orderId, String userId) {
		 RedisUtils.hasLocked("payDetailInitLock_" + userId+"_"+orderId );
		 PaymentDetails ent;
		 try {
			 ent = findByIdWithMaster(orderId);
			//过滤已经标记过的支付记录（订单支付失败、成功、订单支付成功 但是金额不匹配）
				if(BaseAlipayConstants.TRADE_STATE_R_05.equals(ent.getTradeStatus()) ||BaseAlipayConstants.TRADE_STATE_R_04.equals(ent.getTradeStatus())){
					RedisUtils.unLock("payDetailInitLock_" + userId+"_"+orderId);
					return ent;
				}
				if(BaseAlipayConstants.TRADE_STATE_R_03.equals(ent.getTradeStatus())){
					RedisUtils.unLock("payDetailInitLock_" + userId+"_"+orderId);
					return ent;
				}
				ent = this.getAlipayCheck(ent);
		} finally {
			RedisUtils.unLock("payDetailInitLock_" + userId+"_"+orderId);
		}
		return ent;
	}

	@Override
	public PaymentDetails findByIdWithMaster(String id) {
		return paymentDetailsDao.findOneById(id);
	}
	
	private void ToQQMail(String outId,Double commodityAmount,Double orderAmount) throws Exception {
		Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp"); // 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com"); // 主机名
        properties.put("mail.smtp.port", 465);  // 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");  // 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true"); // 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress(AlipayConfigs.getSenderQq()));
        // 设置收件人地址 
        message.setRecipients( RecipientType.TO, new InternetAddress[] { new InternetAddress(AlipayConfigs.getSenderQq()) });
        // 设置邮件标题
        message.setSubject("生涯规划支付金额异常请处理");
        // 设置邮件内容
        message.setText("生涯规划支付金额异常请处理：base_payment_details 支付宝订单："+outId+"，付款金额："+orderAmount+"商品金额:"+commodityAmount);
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect(AlipayConfigs.getReceiverQq(), AlipayConfigs.getSenderQqAuthorCode());// 密码为刚才得到的授权码
        // 发送邮件 
        transport.sendMessage(message, message.getAllRecipients());
	}
}