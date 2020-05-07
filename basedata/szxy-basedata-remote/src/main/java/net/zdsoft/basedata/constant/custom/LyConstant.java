package net.zdsoft.basedata.constant.custom;

public class LyConstant {

	private static final String LY_START_URL = "http://auth.lyeduyun.com";
	public static final String LY_GET_CODE_URL = LY_START_URL+"/core/connect/authorize";
	public static final String LY_GET_ACCESS_TOKEN_URL = LY_START_URL + "/core/connect/token";
	public static final String LY_GET_USER_INFO_URL = LY_START_URL + "/core/connect/userinfo";
	public static final String SYNC_DATA_LY_INTERFACE_URL = "http://api.lyeduyun.com:60004/Agent";
	
	public static final String LY_CLIENT_ID_VAL = "d2baadf7846140f49c9224203421fd55kWwc";
	public static final String LY_CLIENT_SECRET_VAL = "d6c3fae372e34716988126fa2bd8cd4ckWwcx5mXaPZ2UBVU";
	//获取token的Authorization的前缀
	public static final String LY_GET_TOKEN_PREFIX = "Basic ";
	//获取用户Authorization的前缀
	public static final String LY_GET_USER_TOKEN_PREFIX = "Bearer ";
	
	public static final String LY_STATE_NAME = "state";
	public static final String LY_CODE_NAME = "code";
	public static final String LY_SCODE_VALUE = "openid profile";
	public static final String LY_STATE_VALUE = "123456";
	
	public static final String LY_DEFAULT_REGION_CODE = "330825";
	
	public static final String LY_REDIRECT_URI = "/homepage/remote/openapi/longyou/login";
}
