package net.zdsoft.careerplan.trade.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.zdsoft.careerplan.trade.service.AlipayTradeService;
import net.zdsoft.careerplan.trade.service.impl.AlipayTradeServiceImpl;
import net.zdsoft.framework.config.Evn;

/**
 * 支付宝调用公共参数
 */
public class AlipayConfigs {
    private static Log log = LogFactory.getLog(AlipayConfigs.class);
    private static Configuration configs;
    
    private static String charSet;//编码
    private static String openApiDomain;   // 支付宝openapi域名
    private static String mcloudApiDomain;  // 支付宝mcloudmonitor域名 沙箱 用于测试
    private static String pid;             // 商户partner id
    //支付宝分配给开发者的应用ID 
    private static String appid;           // 商户应用id

    private static String privateKey;      // RSA私钥，用于对商户请求报文加签
    private static String publicKey;       // RSA公钥，仅用于验证开发者网关
    private static String alipayPublicKey; // 支付宝RSA公钥，用于验签支付宝应答
    private static String signType;     // 签名类型  

    private static int maxQueryRetry;   // 最大查询次数
    private static long queryDuration;  // 查询间隔（毫秒）

    private static int maxCancelRetry;  // 最大撤销次数
    private static long cancelDuration; // 撤销间隔（毫秒）

    private static long heartbeatDelay ; // 交易保障线程第一次调度延迟（秒）
    private static long heartbeatDuration ; // 交易保障线程调度间隔（秒）
    
    private static String timeoutExpress;
    
    private static String qrCodeTimeoutExpress;
    
    private static String timeoutDate;
    private static String senderQq;
    private static String senderQqAuthorCode;
    private static String receiverQq;
    //private static String djStatic;
	public static String FILEPATH="conf/zfbinfo.properties";
    
    public static AlipayTradeService tradeService;
    
    public static String returnUrl;
    public static String notifyUrl;//异步通知暂时不使用
    
    private static String quitUrl;//用户付款中途退出返回商户网站的地址 
    

    private AlipayConfigs() {
    	
    }
    static {
    	init(FILEPATH);
    	tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }
    // 根据文件名读取配置文件，文件后缀名必须为.properties
    public synchronized static void init(String filePath) {
        if (configs != null) {
            return;
        }
        try {
            configs = new PropertiesConfiguration(filePath);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        if (configs == null) {
            throw new IllegalStateException("can`t find file by path:" + filePath);
        }
      
        charSet = configs.getString("charset");
        openApiDomain = configs.getString("open_api_domain");
        mcloudApiDomain = configs.getString("mcloud_api_domain");

        appid = configs.getString("appid");

        // RSA
        privateKey = configs.getString("private_key");
        publicKey = configs.getString("public_key");
        alipayPublicKey = configs.getString("alipay_public_key");
        signType = configs.getString("sign_type");
        
        timeoutExpress = configs.getString("timeout_express");
        
        
        qrCodeTimeoutExpress=configs.getString("qr_code_timeout_express");
        
        timeoutDate = configs.getString("timeout_date");
        senderQq = configs.getString("sender_qq");
        senderQqAuthorCode = configs.getString("sender_qq_author_code");
        receiverQq = configs.getString("receiver_qq");
        //Evn.getCreeplanDjStatic()--放到freework下
       // djStatic =configs.getString("dj_static");
//        dockingCharge =configs.getString("docking_charge");
        
        
        returnUrl=configs.getString("return_url");
        notifyUrl=configs.getString("notify_url");
        quitUrl=configs.getString("quit_url");
        
        log.info("配置文件名: " + filePath);
        log.info(description());
    }
    

    public static String description() {
        StringBuilder sb = new StringBuilder("Configs{");
        sb.append("支付宝openapi网关: ").append(openApiDomain).append("\n");
        if (StringUtils.isNotEmpty(mcloudApiDomain)) {
            sb.append(", 支付宝mcloudapi网关域名: ").append(mcloudApiDomain).append("\n");
        }

        if (StringUtils.isNotEmpty(pid)) {
            sb.append(", pid: ").append(pid).append("\n");
        }
        sb.append(", appid: ").append(appid).append("\n");

        sb.append(", 商户RSA私钥: ").append(getKeyDescription(privateKey)).append("\n");
        sb.append(", 商户RSA公钥: ").append(getKeyDescription(publicKey)).append("\n");
        sb.append(", 支付宝RSA公钥: ").append(getKeyDescription(alipayPublicKey)).append("\n");
        sb.append(", 签名类型: ").append(signType).append("\n");

        sb.append(", 查询重试次数: ").append(maxQueryRetry).append("\n");
        sb.append(", 查询间隔(毫秒): ").append(queryDuration).append("\n");
        sb.append(", 撤销尝试次数: ").append(maxCancelRetry).append("\n");
        sb.append(", 撤销重试间隔(毫秒): ").append(cancelDuration).append("\n");

        sb.append(", 交易保障调度延迟(秒): ").append(heartbeatDelay).append("\n");
        sb.append(", 交易保障调度间隔(秒): ").append(heartbeatDuration).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private static String getKeyDescription(String key) {
        int showLength = 6;
        if (StringUtils.isNotEmpty(key) &&
                key.length() > showLength) {
            return new StringBuilder(key.substring(0, showLength))
                    .append("******")
                    .append(key.substring(key.length() - showLength))
                    .toString();
        }
        return null;
    }

    public static Configuration getConfigs() {
        return configs;
    }

    public static String getCharSet() {
		return charSet;
	}


	public static String getOpenApiDomain() {
        return openApiDomain;
    }

    public static String getMcloudApiDomain() {
        return mcloudApiDomain;
    }

    public static void setMcloudApiDomain(String mcloudApiDomain) {
    	AlipayConfigs.mcloudApiDomain = mcloudApiDomain;
    }

    public static String getPid() {
        return pid;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public static String getSignType() {
        return signType;
    }

    public static int getMaxQueryRetry() {
        return maxQueryRetry;
    }

    public static long getQueryDuration() {
        return queryDuration;
    }

    public static int getMaxCancelRetry() {
        return maxCancelRetry;
    }

    public static long getCancelDuration() {
        return cancelDuration;
    }

    public static void setConfigs(Configuration configs) {
    	AlipayConfigs.configs = configs;
    }

    public static void setOpenApiDomain(String openApiDomain) {
    	AlipayConfigs.openApiDomain = openApiDomain;
    }

    public static void setPid(String pid) {
    	AlipayConfigs.pid = pid;
    }

    public static void setAppid(String appid) {
    	AlipayConfigs.appid = appid;
    }

    public static void setPrivateKey(String privateKey) {
    	AlipayConfigs.privateKey = privateKey;
    }

    public static void setPublicKey(String publicKey) {
    	AlipayConfigs.publicKey = publicKey;
    }

    public static void setAlipayPublicKey(String alipayPublicKey) {
    	AlipayConfigs.alipayPublicKey = alipayPublicKey;
    }

    public static void setSignType(String signType) {
    	AlipayConfigs.signType = signType;
    }
    
    public static void setMaxQueryRetry(int maxQueryRetry) {
    	AlipayConfigs.maxQueryRetry = maxQueryRetry;
    }

    public static void setQueryDuration(long queryDuration) {
    	AlipayConfigs.queryDuration = queryDuration;
    }

    public static void setMaxCancelRetry(int maxCancelRetry) {
    	AlipayConfigs.maxCancelRetry = maxCancelRetry;
    }

    public static void setCancelDuration(long cancelDuration) {
    	AlipayConfigs.cancelDuration = cancelDuration;
    }

    public static long getHeartbeatDelay() {
        return heartbeatDelay;
    }

    public static void setHeartbeatDelay(long heartbeatDelay) {
    	AlipayConfigs.heartbeatDelay = heartbeatDelay;
    }

    public static long getHeartbeatDuration() {
        return heartbeatDuration;
    }

    public static void setHeartbeatDuration(long heartbeatDuration) {
    	AlipayConfigs.heartbeatDuration = heartbeatDuration;
    }


	public static String getTimeoutExpress() {
		return timeoutExpress;
	}


	public static void setTimeoutExpress(String timeoutExpress) {
		AlipayConfigs.timeoutExpress = timeoutExpress;
	}


	public static String getTimeoutDate() {
		return timeoutDate;
	}


	public static void setTimeoutDate(String timeoutDate) {
		AlipayConfigs.timeoutDate = timeoutDate;
	}


	public static String getSenderQq() {
		return senderQq;
	}


	public static void setSenderQq(String senderQq) {
		AlipayConfigs.senderQq = senderQq;
	}


	public static String getSenderQqAuthorCode() {
		return senderQqAuthorCode;
	}


	public static void setSenderQqAuthorCode(String senderQqAuthorCode) {
		AlipayConfigs.senderQqAuthorCode = senderQqAuthorCode;
	}


	public static String getReceiverQq() {
		return receiverQq;
	}


	public static void setReceiverQq(String receiverQq) {
		AlipayConfigs.receiverQq = receiverQq;
	}


	public static AlipayTradeService getTradeService() {
		return tradeService;
	}


	public static void setTradeService(AlipayTradeService tradeService) {
		AlipayConfigs.tradeService = tradeService;
	}


	public static void setCharSet(String charSet) {
		AlipayConfigs.charSet = charSet;
	}


	public static String getReturnUrl() {
		return returnUrl;
	}


	public static void setReturnUrl(String returnUrl) {
		AlipayConfigs.returnUrl = returnUrl;
	}


	public static String getNotifyUrl() {
		return notifyUrl;
	}


	public static String getQuitUrl() {
		return quitUrl;
	}


	public static void setQuitUrl(String quitUrl) {
		AlipayConfigs.quitUrl = quitUrl;
	}


	public static void setNotifyUrl(String notifyUrl) {
		AlipayConfigs.notifyUrl = notifyUrl;
	}
//	public static String getDjStatic() {
//		return djStatic;
//	}
//	public static void setDjStatic(String djStatic) {
//		AlipayConfigs.djStatic = djStatic;
//	}


	public static String getQrCodeTimeoutExpress() {
		return qrCodeTimeoutExpress;
	}


	public static void setQrCodeTimeoutExpress(String qrCodeTimeoutExpress) {
		AlipayConfigs.qrCodeTimeoutExpress = qrCodeTimeoutExpress;
	}
	
}

