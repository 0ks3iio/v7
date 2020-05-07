package net.zdsoft.syncdata.custom.uc.constant;

public class UcConstant {

	private static String ucBeforeUrl = "http://uc.szxy.wanpeng.com";
	/**
	 * 数据的解析
	 */
	public static final String RESOLVE_DATA_NAME = "data";
	
	/**
	 * 同步用户的缓存key
	 */
	public static final String UC_BEFORE_USER_REDIS_KEY = "syncdata.uc.user";
	
	/**
	 * 获取token的缓存key
	 */
	public static final String UC_BEFORE_TOKEN_REDIS_KEY = "uc.before.token.redis.key.";
	
	/**
	 * xk.mysk的apCode
	 */
	public static final String UC_MSYK_AP_CODE = "RFP3EYIQhK3nGnU56000002096516290";
	/**
	 * xk.mysk的verifyKey
	 */
	public static final String UC_MSYK_VERIFY_KEY = "B90474892A49E5FEC243B45DE65A8407";
	/**
	 * xk.mysk的nonceStr
	 */
	public static final String UC_MSYK_NONCE_STR = "xkmsyk";
	/**
	 * xk.mysk的nonceStr
	 */
	public static final String UC_GET_TOKEN_URL = ucBeforeUrl + "/uc/access_token/get_token";
	/**
	 * xk.mysk的addUser
	 */
	public static final String UC_ADD_USER_URL = ucBeforeUrl + "/uc/user/list/addUser";
	/**
	 * xk.mysk的updateUcUser
	 */
	public static final String UC_UPDATE_USER_URL = ucBeforeUrl + "/uc/user/list/updateUcUser";
	/**
	 * xk.mysk的deleteUcUser
	 */
	public static final String UC_DELETE_USER_URL = ucBeforeUrl + "/uc/user/list/deleteUcUser";
	
	
	/**
	 * 正确的code
	 */
	public static final String UC_TRUE_CODE_VALUE = "200";
	/**
	 * 错误的code
	 */
	public static final String UC_FALSE_CODE_VALUE = "400";
}



