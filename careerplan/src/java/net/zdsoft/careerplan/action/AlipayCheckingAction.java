package net.zdsoft.careerplan.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.base.Objects;

import net.zdsoft.careerplan.service.PaymentDetailsService;
import net.zdsoft.careerplan.trade.config.AlipayConfigs;
import net.zdsoft.careerplan.entity.PaymentDetails;
import net.zdsoft.framework.action.BaseAction;
/**
 * 订单和验证
 */
@Controller
@RequestMapping("/alipayChecking")
public class AlipayCheckingAction extends BaseAction{
	@Autowired
	private PaymentDetailsService paymentDetailsService;
	
	@RequestMapping("/notifyUrl")
	public String getRsaCheck1(HttpServletRequest request, HttpServletResponse response) {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  	}
		    //乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		
		//商户订单号
		PaymentDetails ent = paymentDetailsService.findOneWithMaster(params.get("out_trade_no"));
		//if()//验证数据状态是否改变  如果改变不在处理
		//return "success";
		
		//调用SDK验证签名
		boolean signVerified=false;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfigs.getAlipayPublicKey(), AlipayConfigs.getCharSet(), AlipayConfigs.getSignType());
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//——请在这里编写您的程序（以下代码仅作参考）——
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		
		if(signVerified) {//验证成功
			//商户订单号
			//支付宝交易号
			String trade_no = params.get("trade_no");
			//交易状态
			String trade_status = params.get("trade_status");
			
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_CLOSED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
				
				//PaymentDetails ent 修改状态信息
				return "success";
			}else{//(trade_status.equals("WAIT_BUYER_PAY"))//失败
				//判断是否超时  如果超时或者快到时间了重新生成二维码
				
				return "重新生成二维码";
			}
			
			
		}else {//验证失败
			//调试用，写文本函数记录程序运行情况是否正常
			String sWord = AlipaySignature.getSignCheckContentV1(params);
			return sWord;
		}
		
	}
	
	@RequestMapping("/payCheck")
	public String getPayCheck(PaymentDetails ent) {
		//如果没有传递return_url参数，在二维码页面，扫码成功以后页面是不会跳转，直接停留在二维码页面.
		//alipayRequest.setReturnUrl(AlipayConfig.return_url);
		//alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		//String out_trade_no = ent.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//付款金额，必填
		//String total_amount = new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8");
		//订单名称，必填
		//String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
		
		PaymentDetails ent1 = paymentDetailsService.findOne("out_trade_no");
		if(ent1!=null && ent1.getUserId().equals(this.getLoginInfo().getUserId())
				////防止为空
				&& Objects.equal(ent1.getCommodityAmount(), ent.getOrderAmount())){
			//如果信息正常就进行付款操作
		}
		//否则直接报错
		return "参数不对请重新刷新";
		
	}
	
	
}
