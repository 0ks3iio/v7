package net.zdsoft.basedata.constant.custom;

/**
 * 东莞对接用到的参数
 * @author 
 *
 */

public class DgjyConstant {

	/*=====================项目各自定义特性 Start =======================*/
	public static final String DG_RESPONSE_TYPE_VALUE = "id_token token"; // Id Token + Access Token
	public static final String DG_SCOPE_VALUE = "openid"; // Only SSO
	
	public static final String DG_NONCE_VALUE = "123456";
	public static final String DG_RESPONSE_MODE_VALUE = "form_post";
//	static final String STATE = UUID.randomUUID().toString(); //为了安全，可以动态生成，然后在login success时判断值相同
//	public static final String STATE = "%7B%22url%22%3A%22desktop%2Findex%2Fpage%22%2C%22username%22%3A%22zhangxiaoyi%22%7D";
	public static final String DG_LOGIN_AUTHORIZE_URL = "https://hui-m.dgjy.net/connect/authorize";
	public static final String DG_LOGIN_SERVER_URL = "https://hui-m.dgjy.net";
	
	public static final String DG_LOGIN_DOMIN_NAME_URL = "http://openapi.dgjy.net";
	
    public static final String DG_CLIENT_ID_VALUE = "wangpeng";
	public static final String DG_CLIENT_SECRET_VALUE = "secret";
	public static final String DG_GET_SCOPE_VALUE = "UserInfoWebAPI";
	public static final String DG_GRANT_TYPE_VALUE = "client_credentials";
	
	public static final String DG_STATE_NAME = "state";
	public static final String DG_ID_TOKEN_NAME = "id_token";
	public static final String DG_USER_NAME = "username";
	public static final String DG_URL_NAME = "url";
	public static final String DG_URL_TYPE_NAME = "type";
	
	
//	redirect-uri: http://192.168.3.112:8080/auth/login/success
	public static final String DG_REDIRECT_URI = "/homepage/remote/openapi/dgjy/login";
	
	
	public static final String DG_GET_ACCESSTOKEN_URI = "https://hui-m.dgjy.net/connect/token";
	
	public static final String DG_FIVE_URL_TYPE = "5";
	public static final String DG_SIX_URL_TYPE = "6";
	public static final String DG_SEVEN_URL_TYPE = "7";
	// 5---"eis_basedata"  6 --officedoc
	public static final String DG_FIVE_SERVER_CODE = "schres";
	public static final String DG_SIX_SERVER_CODE = "eis_basedata";
	public static final String DG_SERVER_SERVER_CODE = "gkelective";
	
}
