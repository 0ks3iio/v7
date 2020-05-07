package net.zdsoft.careerplan.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.google.gson.annotations.SerializedName;
import com.google.zxing.WriterException;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import net.zdsoft.careerplan.constant.BaseAlipayConstants;
import net.zdsoft.careerplan.service.PaymentDetailsService;
import net.zdsoft.careerplan.service.TradeInfoService;
import net.zdsoft.careerplan.trade.config.AlipayConfigs;
import net.zdsoft.careerplan.trade.utils.GsonFactory;
import net.zdsoft.careerplan.trade.utils.ZxingUtils;
import net.zdsoft.careerplan.entity.PaymentDetails;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.careerplan.entity.TradeInfo;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ShengySign;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
/**
 * 生涯规划 开通vip
 */
@Controller
@RequestMapping("/careerPlanning")
public class CareerPlanAction extends BaseAction{

	protected static final Logger log = Logger.getLogger(CareerPlanAction.class);
	
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private PaymentDetailsService paymentDetailsService;
	@Autowired
	private TradeInfoService tradeInfoService;

	@RequestMapping(value={"/commodityDetails", "/common/commodityDetails"})
	@ControllerInfo("vip页面")
	public String careerPlanVip(HttpServletRequest request,ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		//进入VIP页面 先判断这个用户是否已经购买vip升级成功
		if (loginInfo != null) {
			boolean isVip=paymentDetailsService.checkByOrderTypeAndUserIdWithMaster(BaseAlipayConstants.PAY_TYPE_01, loginInfo.getUserId());
			if(isVip) {
				return backIndex(loginInfo,isVip);
			}
		}
		//获取价钱
		TradeInfo tradeInfo = tradeInfoService.findByTradeType(BaseAlipayConstants.PAY_TYPE_01);
		if(Evn.isDevModel() && tradeInfo==null) {
			//开发用的默认添加一条数据,正式数据应该是数据库直接插入或者是后台管理创建
			tradeInfo=new TradeInfo();
			tradeInfo.setAmountMoney(0.01);
			tradeInfo.setPrimeMoney(300.00);
			tradeInfo.setCreationTime(new Date());
			tradeInfo.setId(UuidUtils.generateUuid());
			tradeInfo.setIsDeleted(0);
			tradeInfo.setModifyTime(new Date());
			tradeInfo.setOperator(getLoginInfo().getRealName());
			tradeInfo.setTradeName("选科测评系统vip升级");
			tradeInfo.setTradeType(BaseAlipayConstants.PAY_TYPE_01);
			tradeInfoService.save(tradeInfo);
		}
		map.put("vipamount", tradeInfo.getAmountMoney());
		map.put("ptamount", tradeInfo.getPrimeMoney());
		if(isMobile(request)) {
			//进入H5页面
			if(isSaidian()) {
				return "/careerplan/saidian/h5/vipcareer.ftl";
			}
			return "/careerplan/h5/vipcareer.ftl";
		}
		if(isSaidian()) {
			return "/careerplan/saidian/vipcareer.ftl";
		}
		return "/careerplan/vipcareer.ftl";
	}
	@RequestMapping("/careerplanIndex/page")
	@ControllerInfo("回到生涯规划首页")
	public String backLoginIndex() {
		LoginInfo loginInfo = getLoginInfo();
		boolean isVip=paymentDetailsService.checkByOrderTypeAndUserIdWithMaster(BaseAlipayConstants.PAY_TYPE_01, loginInfo.getUserId());
		
		return backIndex(loginInfo,isVip);
	}
	
	public String backIndex(LoginInfo loginInfo,boolean isVip) {
		//直接组装数据
    	//根据user获取regin
    	String regionCode=loginInfo.getRegion();
    	Region region = SUtils.dc(regionRemoteService.findByFullCode(regionCode), Region.class);
    	if(region==null) {
    		//行政区域码不对
    		return "/desktop/error.ftl";
    	}
    	String reginName=region.getFullName();
    	Map<String, String> provinceMap = ShengySign.provinceMap;
    	String provinceNum="";
    	for(Entry<String, String> kk:provinceMap.entrySet()) {
    		if(reginName.contains(kk.getKey())) {
    			provinceNum=kk.getValue();
    			break;
    		}
    	}
    	if(StringUtils.isBlank(provinceNum)) {
    		//省匹配不上
    		return "/desktop/error.ftl";
    	}
    	//是不是已经购买vip 
    	String operTypeValue="&operType=6&operValue=0";
    	if(isVip) {
    		//VIP用户
    		operTypeValue="&operType="+Evn.getCreeplanDjStatic()+"&operValue=1";
    	}
    	
    	//暂时不通过base_server获取域名 直接默认http://sy.yunschool.com
    	//openNickName 真实姓名
    	String parmUrl="&openUsername="+loginInfo.getUserName()+"&openUserProvince="+provinceNum+
    			"&openUserCity=&openUserArea=&openUserSchool=&openNickName="+UrlUtils.encode(loginInfo.getRealName(),"utf-8")+operTypeValue+"&operData=1&isH5=0&remark=";
    	String url="http://sy.yunschool.com/openAuth/login?secretKey=2a532915f2d60fc9b875ea7e335f2fea" + 
    			"&sign="+ShengySign.getShengySign()+"&openUserId=" + loginInfo.getUserId()+parmUrl;
    	return "redirect:" + url;
	}
	
	@RequestMapping("/vippayindex/page")
	@ControllerInfo("vip支付页面")
	public String payindex(HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		//不存在订单自动生成订单或者获取未完成订单（未完成订单只有一条）
		PaymentDetails paymentDetails = paymentDetailsService.saveOrFindOne(BaseAlipayConstants.PAY_TYPE_01, loginInfo.getUserId(),true);
		if(paymentDetails==null) {
			return errorFtl(map, "未生成订单");
		}
		map.put("isBuy", true);
		//根据用户id+类型判断订单是否已经购买成功
		if(BaseAlipayConstants.TRADE_STATE_R_03.equals(paymentDetails.getTradeStatus())) {
			//是，进入已经购买成功无需再次购买 3秒内进入首页
			paymentDetails.setUserName(getLoginInfo().getUserName());
			map.put("paymentDetails", paymentDetails);
			if(isMobile(request)) {
				if(isSaidian()) {
					return "/careerplan/saidian/h5/vippayResult2.ftl";
				}
				return "/careerplan/h5/vippayResult2.ftl";
			}
			if(isSaidian()) {
				return "/careerplan/saidian/vippayResult2.ftl";
			}
			return "/careerplan/vippayResult2.ftl";
		}
		
//		if(isMobile(request)) {
			//点击购买进入手机支付宝页面
			try {
				doPost(paymentDetails,request, response);
			} catch (ServletException e) {
				e.printStackTrace();
				return errorFtl(map, "调用错误");
			} catch (IOException e) {
				e.printStackTrace();
				return errorFtl(map, "调用错误");
			}
			return null;
//		}
//		
//		
//		paymentDetails.setUserName(loginInfo.getRealName());
//		//返回订单详情
//		map.put("paymentDetails", paymentDetails);
//		//二维码失效时间
//		//map.put("timeleter", paymentDetails.getInvalidTime());
//		map.put("timeleter", DateUtils.addMinute(new Date(),30));
//		return "/basedata/careerplan/vippayIndex.ftl";
	}
	@RequestMapping("/vippayResult2/page")
	@ControllerInfo("结果页")
	public String vippayResult2(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		LoginInfo loginInfo = getLoginInfo();
		//不存在订单自动生成订单或者获取未完成订单（未完成订单只有一条）
		PaymentDetails paymentDetails = paymentDetailsService.saveOrFindOne(BaseAlipayConstants.PAY_TYPE_01, loginInfo.getUserId(),true);
		if(paymentDetails==null) {
			paymentDetails=new PaymentDetails();
		}
		map.put("isBuy", false);
		map.put("paymentDetails", paymentDetails);
		if(isMobile(request)) {
			if(isSaidian()) {
				return "/careerplan/saidian/h5/vippayResult2.ftl";
			}
			return "/careerplan/h5/vippayResult2.ftl";
		}
		if(isSaidian()) {
			return "/careerplan/saidian/vippayResult2.ftl";
		}
		return "/careerplan/vippayResult2.ftl";
		
	}
	
	
	
	@RequestMapping("/vippayresult/page")
	@ControllerInfo("vip支付成功页面")
	public String payRseult(String orderId,HttpServletRequest request,ModelMap map) {
		//如果订单号被篡改 就提示失败
		LoginInfo loginInfo = getLoginInfo();
		if(StringUtils.isBlank(orderId)) {
			//orderId 如果丢失 返回首页
			return backLoginIndex();
		}
		PaymentDetails paymentDetails = paymentDetailsService.findByOrderTypeAndOrderIdAndUserIdWithMaster(BaseAlipayConstants.PAY_TYPE_01, orderId,loginInfo.getUserId());
		//返回订单结果
		if(paymentDetails!=null) {
			paymentDetails.setUserName(loginInfo.getRealName());
		}else {
			//被篡改 就提示失败
			paymentDetails=new PaymentDetails();
			paymentDetails.setUserName(loginInfo.getRealName());
		}
		map.put("paymentDetails", paymentDetails);
		if(isMobile(request)) {
			if(isSaidian()) {
				return "/careerplan/saidian/h5/vippayResult.ftl";
			}
			return "/careerplan/h5/vippayResult.ftl";
		}
		if(isSaidian()) {
			return "/careerplan/saidian/vippayResult.ftl";
		}
		return "/careerplan/vippayResult.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/paycheckResult")
	@ControllerInfo("循环查询")
	public String paycheckResult(String orderId,ModelMap map) {
		PaymentDetails paymentDetails = paymentDetailsService.findByOrderTypeAndOrderIdAndUserIdWithMaster(BaseAlipayConstants.PAY_TYPE_01, orderId,this.getLoginInfo().getUserId());
		if(paymentDetails==null) {
			return success("1");
		}else if(StringUtils.isBlank(paymentDetails.getTradeStatus())) {
			//继续循环
			return success("0");
		}else if(BaseAlipayConstants.TRADE_STATE_R_05.equals(paymentDetails.getTradeStatus())) {
			//支付宝二维码已经被扫 而且失效同时其实已经关闭状态 通过失效 提示失效
			return success("2");
		}
		return success("1");
	}
	
	
	@ResponseBody
	@RequestMapping("/paycheck")
	@ControllerInfo("生成二维码前校验是否失效")
	public String payCheck(String orderId,ModelMap map) {
		//如果订单号被篡改 返回0 正常 1失效 2查询错误
		PaymentDetails paymentDetails = paymentDetailsService.findByOrderTypeAndOrderIdAndUserIdWithMaster(BaseAlipayConstants.PAY_TYPE_01, orderId,this.getLoginInfo().getUserId());
		//返回订单结果
		if(paymentDetails!=null) {
			if(BaseAlipayConstants.TRADE_STATE_R_03.equals(paymentDetails.getTradeStatus()) || BaseAlipayConstants.TRADE_STATE_R_04.equals(paymentDetails.getTradeStatus())) {
				//是，进入已经购买成功,04特别提示联系管理员
				return success("3");
			}else if(BaseAlipayConstants.TRADE_STATE_R_05.equals(paymentDetails.getTradeStatus())) {
				//支付宝二维码已经被扫 而且失效同时其实已经关闭状态 通过失效
				return success("2");
			}
		}else {
			return success("1");//查询失败 也提示失效
		}
		
		return success("0");
	}
	
	@RequestMapping("/payImage")
    @ResponseBody
    public String showImage(HttpServletRequest request, HttpServletResponse response,String orderId){
		PaymentDetails paymentDetails = paymentDetailsService.findByIdWithMaster(orderId);
		//返回订单结果
		if(paymentDetails!=null && paymentDetails.getUserId().equals(getLoginInfo().getUserId()) && BaseAlipayConstants.PAY_TYPE_01.equals(paymentDetails.getOrderType())) {
			
		}else {
			return null;
		}
		try {
			OutputStream out = response.getOutputStream();
			/**读取Logo图片*/	 
	        File logoFile=new File(request.getRealPath(BaseAlipayConstants.ZFB_LOGO));
			BufferedImage fileImage = ZxingUtils.getQRCode(paymentDetails.getQrCode(), 256,logoFile);
			
			ImageIO.write(fileImage,"png",out);
	        out.flush();
		} catch (WriterException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payresult/page")
	@ControllerInfo("return_url")
	public String payReturn(HttpServletRequest request,HttpServletResponse response) {
		//获取支付宝GET过来反馈信息  ---订单号
		String order_no = request.getParameter("out_trade_no");    //获取订单号  
		//根据订单号判断
		if(StringUtils.isBlank(order_no)) {
			//重定向到首页
			return backLoginIndex();
		}
		//进入结果页
		return "redirect:" + UrlUtils.getPrefix(request)+"/careerPlanning/vippayresult/page?orderId="+order_no;
	}
	
	
	//手机端
	//"跳转支付宝"
	public void doPost(PaymentDetails paymentDetails,HttpServletRequest httpRequest,
		HttpServletResponse httpResponse) throws ServletException, IOException {
		String urlPre=UrlUtils.getPrefix(httpRequest);
		if(isMobile(httpRequest)){
			//手机端： productCode：QUICK_WAP_PAY
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigs.getOpenApiDomain(),  
					AlipayConfigs.getAppid(), AlipayConfigs.getPrivateKey(),
					"json", AlipayConfigs.getCharSet(), AlipayConfigs.getAlipayPublicKey(), AlipayConfigs.getSignType()); //获得初始化的AlipayClient
			
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
			alipayRequest.setReturnUrl(urlPre+AlipayConfigs.getReturnUrl());//回跳地址
			alipayRequest.setNotifyUrl(urlPre+AlipayConfigs.getNotifyUrl());
			//找到改用户的
			BizContent bizContent=new BizContent(paymentDetails.getId(),
					String.valueOf(paymentDetails.getCommodityAmount()),paymentDetails.getOrderName(),"QUICK_WAP_PAY",urlPre+AlipayConfigs.getQuitUrl(),AlipayConfigs.getTimeoutExpress());
			
			alipayRequest.setBizContent(GsonFactory.getGson().toJson(bizContent));
			String form="";
			try {
				form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
			httpResponse.setContentType("text/html;charset=" + AlipayConfigs.getCharSet());
			httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();
		}else{
			//pc端使用支付宝公用付款页面  productCode：FAST_INSTANT_TRADE_PAY
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigs.getOpenApiDomain(), AlipayConfigs.getAppid(), AlipayConfigs.getPrivateKey(), "json", AlipayConfigs.getCharSet(), AlipayConfigs.getPublicKey(), AlipayConfigs.getSignType()); //获得初始化的AlipayClient
		    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
			alipayRequest.setReturnUrl(urlPre+AlipayConfigs.getReturnUrl());
			alipayRequest.setNotifyUrl(urlPre+AlipayConfigs.getNotifyUrl());//在公共参数中设置回跳和通知地址
			BizContent bizContent=new BizContent(paymentDetails.getId(),
			String.valueOf(paymentDetails.getCommodityAmount()),paymentDetails.getOrderName(),"FAST_INSTANT_TRADE_PAY",urlPre+AlipayConfigs.getQuitUrl(),AlipayConfigs.getTimeoutExpress());
			alipayRequest.setBizContent(GsonFactory.getGson().toJson(bizContent));
		    String form="";
		    try {
		        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		    }
		    httpResponse.setContentType("text/html;charset=" + AlipayConfigs.getCharSet());
		    httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
		    httpResponse.getWriter().flush();
		    httpResponse.getWriter().close();
		}
		
		
	}
	
	
	private boolean isMobile(HttpServletRequest request) {
        UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = ua.getOperatingSystem();
        if(DeviceType.MOBILE.equals(os.getDeviceType())) {
            return true;
        }
        return false;
    }
	 
	//请求参数
    public class BizContent {
    	// 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复 ---必填
        @SerializedName("out_trade_no")
        private String outTradeNo;//换算成本系统就是生成的订单号id

        // 订单总金额，此处单位为元，精确到小数点后2位，不能超过1亿元
        @SerializedName("total_amount")
        private String totalAmount;
        
        // 订单标题，粗略描述用户的支付目的。如“****消费”
        @SerializedName("subject")
        private String subject;
        
        @SerializedName("product_code")
        private String productCode;
        
        @SerializedName("quit_url")
        private String quitUrl; //用户付款中途退出返回商户网站的地址 
        
        @SerializedName("timeout_express")
        private String timeoutExpress;
        
        public BizContent() {
        	
        }
        public BizContent(String outTradeNo,String totalAmount,String subject,String productCode,String quitUrl,String timeoutExpress) {
        	this.outTradeNo=outTradeNo;
        	this.productCode=productCode;
        	this.subject=subject;
        	this.totalAmount=totalAmount;
        	this.quitUrl=quitUrl;
        	this.timeoutExpress= timeoutExpress;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("outTradeNo='").append(outTradeNo).append('\'');
            sb.append(", totalAmount='").append(totalAmount).append('\'');
            sb.append(", subject='").append(subject).append('\'');
            sb.append(", product_code='").append(productCode).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
    
    
     
    /**
     * 支付宝支付成功后.异步请求该接口	
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/asnyayresult",method=RequestMethod.POST)	
    @ResponseBody	
    public String notify(HttpServletRequest request,HttpServletResponse response) throws IOException {  		
    	log.info("==================支付宝异步返回支付结果开始");		
    	//1.从支付宝回调的request域中取值			
    	//获取支付宝返回的参数集合        
    	Map<String, String[]> aliParams = request.getParameterMap();          
    	//用以存放转化后的参数集合        
    	Map<String, String> conversionParams = new HashMap<String, String>();  	    
    	for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {  	        
    		String key = iter.next();  	        
    		String[] values = aliParams.get(key);  	       
    		String valueStr = "";  	        
    		for (int i = 0; i < values.length; i++) {  	            
    			valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";  	       
    		}  	        
    		// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化  	        
    		// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "uft-8");  	        
    		conversionParams.put(key, valueStr);  	    
    	}  		    
		log.info("==================返回参数集合："+conversionParams);	  
		String status=notify(conversionParams);	   
		return status;	
    }
    
    /**
     *  支付宝异步请求逻辑处理
     * @param conversionParams
     * @return
     */
    public String notify(Map<String, String> conversionParams){				
    	  log.info("==================支付宝异步请求逻辑处理");
		  //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
		  boolean signVerified = false;  
		  try { 
		    //调用SDK验证签名
		    signVerified = AlipaySignature.rsaCheckV1(conversionParams, AlipayConfigs.getAlipayPublicKey(), AlipayConfigs.getCharSet(), AlipayConfigs.getSignType()); 
		  } catch (AlipayApiException e) { 
			  log.info("==================验签失败 ！"); 
			  e.printStackTrace();     
		  }  
	    	   
		  //对验签进行处理
		  if (signVerified) {
	    	  //验签通过    
	    	  //获取需要保存的数据
	    	  String outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
	    	  try { 
	    		  PaymentDetails paymentDetails = paymentDetailsService.findByOrderTypeAndOrderIdAndUserIdWithMaster(BaseAlipayConstants.PAY_TYPE_01, outTradeNo,this.getLoginInfo().getUserId());
	    		  if(BaseAlipayConstants.TRADE_STATE_R_05.equals(paymentDetails.getTradeStatus()) ||BaseAlipayConstants.TRADE_STATE_R_04.equals(paymentDetails.getTradeStatus())
	    				  ||BaseAlipayConstants.TRADE_STATE_R_03.equals(paymentDetails.getTradeStatus())) {
	    			  return "success";
	    		  }
	    		  return "fail";
	    	  }catch (Exception e) {
				e.printStackTrace();
				 return "fail";
			}
	
	    }else {
	    	 return "fail";
	    }
    }
    
    private boolean isSaidian() {
		return Evn.isSaidian();//是否赛点环境
	}
  
	
}
