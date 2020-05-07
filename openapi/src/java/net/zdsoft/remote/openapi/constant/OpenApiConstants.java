package net.zdsoft.remote.openapi.constant;

public class OpenApiConstants {
	
	public static final int REMOTE_JSON_RESULT_CODE_SUCCESS = 1;
	public static final int REMOTE_JSON_RESULT_CODE_ERROR = -1;

    /**
     * 接口类型 1基础数据接口 2其他业务接口 3推送接口
     */
    public static final int INTERFACE_DATA_TYPE_1 = 1;
    public static final int INTERFACE_DATA_TYPE_2 = 2;
    public static final int INTERFACE_DATA_TYPE_3 = 3;
    /**
     * 1对象属性 2接口
     */
    public static final int BUSINESS_TK_TYPE_1 = 1;
    public static final int BUSINESS_TK_TYPE_2 = 2;

    /**
     * 存储验证码的值
     */
    public static final String VERIFY_CODE_CACHE_KEY = "openapi_verify_code_key";
    public static final String VERIFY_CODE = "0123456789abcdefghijklmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";

    /**
     * session key
     */
    public static final String DEVELOPER_SESSION = "developer_session";

    public static final String FILE_PATH = "FILE.PATH";

    public static final String FILE_URL = "FILE.URL";

    public static final String OPEN_LOGO_INFO_SESSION = "opne.logo.info.session";
    public static final String OPEN_API_LOGO_RUL = "openapi.logo.url";
    public static final String OPEN_API_LOGO_NAME = "openapi.logo.name";
    public static final String OPEN_API_FOOT_VALUE = "openapi.foot.value";
    public static final String MOBILE_REGEX = "mobile.regex";
    public static final String UNICOM_REGEX = "unicom.regex";
    public static final String TELCOM_REGEX = "telcom.regex";
    public static final String DEFAULT_PASSWORD = "123456";
    
    //按日周月显示
    public static final String SHOW_INTERCE_DAY = "1";
    public static final String SHOW_INTERCE_WEEK = "2";
    public static final String SHOW_INTERCE_MONTH = "3";
    
    //导出基础表的remoteService的对照值
    public static final String SHOW_UNIT_REMOTE_SERVICE = "1";
    public static final String SHOW_SCHOOL_REMOTE_SERVICE = "2";
    public static final String SHOW_SUBSCHOOL_REMOTE_SERVICE = "3";
    public static final String SHOW_USER_REMOTE_SERVICE = "4";
    public static final String SHOW_TEACHER_REMOTE_SERVICE = "5";
    public static final String SHOW_STUDENT_REMOTE_SERVICE = "6";
    public static final String SHOW_FAMILY_REMOTE_SERVICE = "7";
    public static final String SHOW_DEPT_REMOTE_SERVICE = "8";
    
    //openapi的版本对应的值
    public static final String OPENAPI_V20_VERSION = "v2.0";
    public static final String OPENAPI_V21_VERSION = "v2.1";
    
    public static final String BOOLEAN_TRUE_VAL = "1";
    public static final String BOOLEAN_FALSE_VAL = "0";
    
    
    //openapi的参数对应的名称 
    public static final String IS_GET_TOP = "_isGetTop"; //是否获取顶级单位
    
    
    
    
    
    
    
    
    
    
}
