package net.zdsoft.desktop.constant;

public class XiGuConstant {
	
	private static String xgBeforeUrl = "http://open.edu.lzxgjy.com";
	
	public static final String APP_CODE = "code";
	
	public static final String XIGU_GET_USER_INFO_METHOD = "core.user.get";
	
	public static final String XIGU_GET_USER_ROLE_METHOD = "core.user.role.get";
	/**
	 * 默认的密码的值
	 */
	public static final String DEFAULT_PASS_WORD_VALUE = "xg12345678";
	/**
	 * APPKEY的值
	 */
	public static final String XIGU_APPKEY = "ad710c72769d44b4bc7affcd112ac7d2";
	/**
	 * appsecret的值
	 */
	public static final String XIGU_APPSECRET = "e3efba5242908bef5eed44a67d804f6b";
	
	/**
	 * 数据的解析
	 */
	public static final String RESOLVE_DATA_NAME = "data";
	/**
	 * token的值
	 */
	public static final String ACCESS_TOKEN_NAME = "access_token";
	
	/**
	 * token的有效时间值 单位 s 秒
	 */
	public static final String ACCESS_TOKEN_TIME_NAME = "expires_in";
	
	/**
	 * 默认isdeleted 的值
	 */
	public static final int DEFAULT_IS_DELETED_VALUE = 0;
	
	/**
	 * 默认evensource 的值   0 本地，1 订阅消息
	 */ 
	public static final int DEFAULT_EVENT_SOURCE_VALUE = 1;
	
	/**
	 * 西固请求token的接口
	 */
	public static final String XIGU_ACCESS_TOKEN_URL = xgBeforeUrl + "/authorization-token-server/oauth/token"; 
	
	/**
	 * 西固请求用户的接口
	 */
	public static final String XIGU_USER_INFO_URL = xgBeforeUrl + "/api";
	
	/**
	 * 西固请求用户的接口
	 */
	public static final String XIGU_GET_WPLOGIN_NAME = "http://edu.lzxgjy.com/sns/index.php?app=public&mod=WpLogin&act=returnWpLoginName";
	
	/**
	 * 1：教育局教师  2：学校教师 3：学校学生
	 */
	public static final Integer XIGU_EDU_TEACHER_TYPE = 1;
	public static final Integer XIGU_SCHOOL_TEACHER_TYPE = 2;
	public static final Integer XIGU_SCHOOL_STUDENT_TYPE = 3;
	
	
	/**
	 * 获取token的缓存key
	 */
	public static final String XG_TOKEN_REDIS_KEY = "xigu.token.redis.key";
	
	
	/**
	 * 获取appId的缓存key
	 */
	public static final String XG_APP_ID_KEY = "xigu.appId.redis.key";
	
	/**
	 * 获取server的缓存key
	 */
	public static final String XG_SERVER_KEY = "xigu.server.redis.key";
}
