package net.zdsoft.bigdata.data.utils;

import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by wangdongdong on 2018/8/3 09:35.
 */
public class CustomerHtmlResourceHandler implements HtmlResourceHandler {

    private String remotePath;

    public String getResourcePath(String id) {
        return remotePath;
    }

    public void handleResource(String id, byte[] data) {
        String encode = Base64.encodeBase64String(data);
        this.remotePath = "data:image/png;base64," + encode;
    }
}
