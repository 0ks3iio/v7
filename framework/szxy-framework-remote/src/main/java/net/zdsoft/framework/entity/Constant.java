package net.zdsoft.framework.entity;

import java.io.Serializable;

public class Constant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String SESSION_COUNT_PREFIX_SET = "redis.session.count";
    public static final String SESSION_COUNT_PREFIX = "redis.session.count.";
	
    public static final int IS_DELETED_TRUE = 1;
    public static final int IS_DELETED_FALSE = 0;
    public static final int IS_TRUE = 1;
    public static final int IS_FALSE = 0;

    public static final String IS_TRUE_Str = "1";
    public static final String IS_FALSE_Str = "0";

    public static final int CLASS_EDU = 1;// 教育局
    public static final int CLASS_SCH = 2;// 学校

    public static final String GUID_ZERO = "00000000000000000000000000000000";
    public static final String GUID_ONE = "11111111111111111111111111111111";
    public static final int NUMBER_MINUS_ONE = -1;

    public static final String EIS_SCHEDULER_START = "eis.scheduler.start";
    /**
     * 是否开发模式，外网发布的时候，需要调整为false
     */
    public static final String FW_DEVMODEL = "fw.devModel";
    /**
     * 是否使用activeMQ
     */
    public static final String ACTIVEMQ_ENABLE = "activemq.enable";
    /**
     * redis是否开启
     */
    public static final String FW_REDIS_ENABLE = "fw.redis.enable";
    /**
     * memcached是否开启
     */
    public static final String FW_MEMCACHED_ENABLE = "fw.memcached.enable";
    /**
     * 服务地址
     */
    public static final String EIS_WEB_URL = "eis_web_url";

    /**
     * 统一登出地址
     */
    public static final String UNIFY_LOGOUT_URL = "unify_logout_url";
    /**
     * 资源文件路径
     */
    public static final String EIS_RESOURCE_URL = "eis_resource_url";
    /**
     * 登陆页
     */
    public static final String EIS_LOGIN_URL = "eis_login_url";
    /**
     * 开发者平台登录页
     */
    public static final String OPENAPI_LOGIN_URL = "openapi_login_url";
    /**
     * 是否连接passport
     */
    public static final String CONNECTION_PASSPORT = "connection_passport";
    /**
     * passport key
     */
    public static final String PASSPORT_VERIFYKEY = "passport_verifyKey";
    /**
     * passport地址
     */
    public static final String PASSPORT_URL = "passport_url";
    /**
     * passport server
     */
    public static final String PASSPORT_SERVER_ID = "passport_server_id";
    /**
     * 微课 appid
     */
    public static final String WEIKE_APP_ID = "weikeAppId";
    /**
     * 微课公众号id
     */
    public static final String WEIKE_PUBLIC_ID = "weikePublicId";
    /**
     * 微课url
     */
    public static final String WEIKE_URL = "weikeUrl";

    /**
     * 微课VerifyCode
     */
    public static final String WEIKE_VERIFY_CODE = "weikeVerifyCode";
    /**
     * 是否启动double
     */
    public static final String DUBBO_ENABLE = "dubbo_enable";
    /**
     * redis-session失效时间
     */
    public static final String REDIS_SESION_TIMEOUT = "redis_sesion_timeout";
    /**
     * store_path
     */
    public static final String STORE_PATH = "store_path";
    /**
     * 产品标识，用于字段控制等
     */
    public static final String PRODUCT_MARK = "product_mark";

    public final static String FILE_URL = "FILE.URL";

    public final static String FILE_PATH = "FILE.PATH";
    
    public final static String HOME_WORK_URL = "home_work_url";
    
    public final static String DQ_WEB_URL = "dq_web_url";

    /** redis保存passport KEY*/
    public final static String PASSPORT_TICKET_KEY = "_passport_ticket_key_";

}
