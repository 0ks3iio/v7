package net.zdsoft.desktop.constant;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.utils.SecurityUtils;

/**
 * 
 * @author weixh
 * 2019年6月21日	
 */
public class ChangZhiConstant {
	public static final String CZ_GETUSER_URL = "http://60.220.220.241:48383/Sites/SSO/SSOService.aspx";
	
	public static final String CZ_LOGIN_USERNAME = "RYH";
	
	public static final String CZ_APP_KEY = "ZHBG";
	public static final String CZ_SECRET_KEY = "MeECv2LXDdYcPkYHiW3Bcg==";
	
	public static String signMd5(String token) {
		String result = StringUtils.EMPTY;// 声明结果变量
		result = "app_key=" + CZ_APP_KEY + "&token=" + token + CZ_SECRET_KEY;
//		result = Encoding.UTF8.GetString(Encoding.UTF8.GetBytes(result + secretKey)); // 字符编码
//		result = MD5.Encrypt(result);// 字符md5加密 特别注意：_Result在进行最后的MD5前，确保不包含encode编码（%252f等），如含有，先进行decode 等。
		result = SecurityUtils.encodeByMD5(result);
		return result;
	}
	
	public static void main(String[] args) {
		String ti = "04f9ac5efd7d8a3b480b71162ac9b2381561347125";
		System.out.println(signMd5(ti));
	}
}
