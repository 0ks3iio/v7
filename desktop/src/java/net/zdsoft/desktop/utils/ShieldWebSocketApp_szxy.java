package net.zdsoft.desktop.utils;

import java.net.URISyntaxException;

import com.iflytek.fsp.shield.java.sdk.websocket.BaseApp;

  public class ShieldWebSocketApp_szxy extends BaseApp {

      public ShieldWebSocketApp_szxy()  throws URISyntaxException{
          this.appId = "d33dcde2d83f4bd28be484a281ed6c41";
          this.appSecret = "a7621c88e2466d104517a834bb4467f4";
          this.host = "openapi.ahjygl.gov.cn";
          this.stage = "RELEASE";
          this.publicKey = "305C300D06092A864886F70D0101010500034B00304802410087BCCE08487F2AD237FD7C2B2A5E6310598EB8CB4FCEBF591A38714D7A91D97C98CE1BA002A9ECB75B4A27850918C823B8C9F800272A4A95A45134CA2E79E9EF0203010001";
          this.equipmentNo = "XXX";
          this.signStrategyUrl = "/getSignStrategy";
          this.tokenUrl = "/getTokenUrl";
          this.webSocketPort=4999;
          this.icloudlockEnabled = false;//关闭云锁验证
      }
      private static ShieldWebSocketApp_szxy singleton;

      public static ShieldWebSocketApp_szxy getInstance() throws URISyntaxException {
            if (singleton == null) {
                synchronized (ShieldWebSocketApp_szxy.class) {
                    if (singleton == null) {
                        singleton = new ShieldWebSocketApp_szxy();
                    }
                }
            }
            return singleton;
      }


}