package net.zdsoft.careerplan.trade.service.impl;

import com.alipay.api.DefaultAlipayClient;

import net.zdsoft.careerplan.trade.config.AlipayConfigs;

import org.apache.commons.lang.StringUtils;

/**
 *
 *初始化公用参数 client 
 *  
 */
public class AlipayTradeServiceImpl extends AbsAlipayTradeService {

    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;
        private String format;
        private String charset;
        private String alipayPublicKey;
        private String signType;

        public AlipayTradeServiceImpl build() {
            if (StringUtils.isEmpty(gatewayUrl)) {
                gatewayUrl = AlipayConfigs.getOpenApiDomain(); // 与mcloudmonitor网关地址不同
            }
            if (StringUtils.isEmpty(appid)) {
                appid = AlipayConfigs.getAppid();
            }
            if (StringUtils.isEmpty(privateKey)) {
                privateKey = AlipayConfigs.getPrivateKey();
            }
            if (StringUtils.isEmpty(format)) {
                format = "json";
            }
            if (StringUtils.isEmpty(charset)) {
                charset = AlipayConfigs.getCharSet();
            }
            if (StringUtils.isEmpty(alipayPublicKey)) {
                alipayPublicKey = AlipayConfigs.getAlipayPublicKey();
            }
            if (StringUtils.isEmpty(signType)) {
                signType = AlipayConfigs.getSignType();
            }

            return new AlipayTradeServiceImpl(this);
        }

        public ClientBuilder setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
            return this;
        }

        public ClientBuilder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public ClientBuilder setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        public ClientBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public ClientBuilder setGatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
            return this;
        }

        public ClientBuilder setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }
        
        public ClientBuilder setSignType(String signType) {
            this.signType = signType;
            return this;
        }
        
        public String getAlipayPublicKey() {
            return alipayPublicKey;
        }
        
        public String getSignType() {
            return signType;
        }

        public String getAppid() {
            return appid;
        }

        public String getCharset() {
            return charset;
        }

        public String getFormat() {
            return format;
        }

        public String getGatewayUrl() {
            return gatewayUrl;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }
    
    public AlipayTradeServiceImpl(ClientBuilder builder) {
        if (StringUtils.isEmpty(builder.getGatewayUrl())) {
            throw new NullPointerException("gatewayUrl should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAppid())) {
            throw new NullPointerException("appid should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getPrivateKey())) {
            throw new NullPointerException("privateKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getFormat())) {
            throw new NullPointerException("format should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getCharset())) {
            throw new NullPointerException("charset should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAlipayPublicKey())) {
            throw new NullPointerException("alipayPublicKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getSignType())) {
            throw new NullPointerException("signType should not be NULL!");
        }
        
        client = new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(),
                builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey(), builder.getSignType());
    }

	

}
