package net.zdsoft.desktop.constant;

public class BjjyConstant {
	private static final String BJ_START_URL = "http://bjzhjy.edu88.com:60003";
	public static final String BJ_GET_CODE_URL = BJ_START_URL+"/core/connect/authorize";
	public static final String BJ_GET_ACCESS_TOKEN_URL = BJ_START_URL + "/core/connect/token";
	public static final String BJ_GET_USER_INFO_URL = BJ_START_URL + "/core/connect/userinfo";
	
	public static final String BJ_CLIENT_ID_VAL = "ece0e362027e46e0821c03fc0033c6cblr4v";
	public static final String BJ_CLIENT_SECRET_VAL = "4015f8f4017746b7adaedc508b129e11lr4vYRVtXNklFgUm";
	
	public static final String DG_FIVE_URL_TYPE = "5";
	public static final String DG_SIX_URL_TYPE = "6";
	public static final String DG_SEVEN_URL_TYPE = "7";
	// 5---"eis_basedata"  6 --officedoc
	public static final String DG_FIVE_SERVER_CODE = "schres";
	public static final String DG_SIX_SERVER_CODE = "eis_basedata";
	public static final String DG_SERVER_SERVER_CODE = "gkelective";
	
	//获取token的Authorization的前缀
	public static final String BJ_GET_TOKEN_PREFIX = "Basic ";
	//获取用户Authorization的前缀
	public static final String BJ_GET_USER_TOKEN_PREFIX = "Bearer ";
	
	public static final String BJ_STATE_NAME = "state";
	public static final String BJ_CODE_NAME = "code";
	public static final String BJ_SCODE_VALUE = "openid profile";
	public static final String BJ_STATE_VALUE = "123456";
	
	public static final String BJ_SIX_SERVER_CODE = "officedoc";
	
	public static final String BJ_REDIRECT_URI = "/homepage/remote/openapi/binjiang/login";
	
}
