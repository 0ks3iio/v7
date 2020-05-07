package net.zdsoft.framework.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.entity.Json;

public class AddressUtils {

    private static Logger log = Logger.getLogger(AddressUtils.class);

    /**
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Json getAddresses(String ip) {
//		try {
        return null;
        //return getAddressFromNet(ip);
//		} catch (IOException e) {
//			Json json = null;
//			for (int i = 0; i < 10; i++) {
//				try {
//					TimeUnit.SECONDS.sleep(1);
//					log.warn("try again! " + i);
//					json = getAddressFromNet(ip);
//				} catch (Exception e2) {
//					continue;
//				}
//				if (json != null) {
//					break;
//				}
//			}
//			return json;
//
//		}
    }

    private static Json getAddressFromNet(String ip) throws IOException {
        if (StringUtils.isBlank(ip) || StringUtils.startsWith(ip, "127.") || StringUtils.startsWith(ip, "192.")
                || StringUtils.startsWith(ip, "10.") || StringUtils.startsWith(ip, "0:0:0:0:0:0:0:1")) {
            return null;
        }
        Json locationInfo = null;
        String host = "https://dm-81.data.aliyun.com/rest/160601/ip/getIpInfo.json";
        String appcode = "cfddaf4a11ae4543871edfe451b0cbfd";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("ip", ip);

        HttpClientParam p = new HttpClientParam();
        p.setHeadMap(headers);
        p.setParamMap(querys);
        String returnStr;
        try {
            returnStr = HttpClientUtils.exeUrlSync(host, p);
            if (StringUtils.isNotBlank(returnStr)) {
                JSONObject result = JSONObject.parseObject(returnStr);
                String code = result.getString("code");
                if (!"0".equals(code)) {
                    return null;
                }
                JSONObject address = JSONObject.parseObject(result.getString("data"));
                String country = address.getString("country");// 国家
                String isp = address.getString("isp");// isp
                String cityRegionCode = address.getString("city_id");// 市区
                if (StringUtils.isBlank(cityRegionCode))
                    cityRegionCode = address.getString("region_id");
                if (StringUtils.isBlank(cityRegionCode) && StringUtils.isBlank(country))
                    return null;
                locationInfo = new Json();
                locationInfo.put("country", country);
                locationInfo.put("isp", isp);
                locationInfo.put("region_code", cityRegionCode);
                return locationInfo;
            }
        } catch (IOException e) {
            log.error("analyze " + ip + " error! " + e.getMessage());
            throw e;
        }
        return locationInfo;
    }

    // 测试
    public static void main(String[] args) throws Exception {
        String ip = "117.136.26.125";
        Json locationInfo = null;
        if (locationInfo == null) {
            Json address = null;
            address = AddressUtils.getAddresses(ip);
            System.out.println(address.toJSONString());
            // 输出结果为：广东省,广州市,越秀区
        }
    }
}
