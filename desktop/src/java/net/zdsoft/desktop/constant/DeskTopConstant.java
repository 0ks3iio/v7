package net.zdsoft.desktop.constant;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.basedata.entity.CourseSchedule;

/**
 * @author shenke
 * @since 2016/12/27 17:08
 */
public class DeskTopConstant {

	/** 功能区類型微代碼*/
	public static final String TYPE_MCODE_ID = "DM-ZMGNQ-LX";

    public static final String LOGIN_PAGE_MODE = "LOGIN.PAGE.MODE";
    public static final String LOGIN_MODE_CUSTOM = "1";
    public static final String CACHE_KEY = "unify_desktop_";
    public static final String UNIFY_DESKTOP_KEY = CACHE_KEY + "ticket_LOGININFO_";

    public static final String UNIFY_LOGIN_URL = "desktop/unify/unify-desktop.action";

    //开发模式使用
    //public static final String UNIFY_V7_URL = "v7";
    //public static final String UNIFY_V6_URL = "";
    //public static final String UNIFY_V5_URL = "";

    //验证码
    public static final String VERIFY_CODE = "0123456789abcdefghijklmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";
    public static final String VERIFY_NUMBER_aA_CODE = "0123456789abcdefghijklmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";
    public static final String VERIFY_NUMBER_a_CODE = "0123456789abcdefghijklmnprstuvwxyz";
    public static final String VERIFY_NUMBER_CODE = "0123456789";
    //桌面缓存KEY
    public static final String VERIFY_CODE_CACHE_KEY = "desktop_verify_code_key";
    public static final String DESKTOP_SESSION_MODEL = "desktop.session.model.key.";

    public static final String REGISTER_CODE_CACHE_KEY = "desktop_register_code_key";
    public static final String REGISTER_CODE_MSG_CACHE_KEY = "desktop_register_msg_code_key";
    
    //统一登录的地址 使用于 （数字校园 作为第三方应用 接入对方的平台， 实现统一的 登录页面）
  	public static final String SYSTEM_THIRD_APP_LOGIN_URL = "SYSTEM.THIRD.APP.LOGIN.URL"; 
    /**
     * 模块打开方式 ,参见Model
     */
    public static final Integer MODEL_OPEN_TYPE_DIV = 1;
    public static final Integer MODEL_OPEN_TYPE_IFRAME = 2;
    public static final Integer MODEL_OPEN_TYPE_NEW = 3;



    public static final String FUNCTION_AREA_USER_SET_OPEN = "DESKTOP.FUNCTION.AREA.USER.SET.OPEN";
    public static final String DESKTOP_LAYOUT = "DESKTOP.LAYOUT";

    //单位推荐应用
    public static final String UNIT_APPLICATIONS_TYPE = "1";
    //学生统计图
    public static final String STUDENT_STATISTICS_TYPE = "2";



    //我的消息
    public static final String MSG_TYPE = "1";
    //我的应用
    public static final String RECOMMENDED_APPLICATIONS_TYPE = "2";
    //常用操作
    public static final String COMMON_FUNCTION_TYPE = "3";
    //我的课表
    public static final String MY_STUDENT_SCHEDULE = "4";
    //办公公告
    public static final String OFFICE_NOTICE_TYPE = "5";
    //待办事项
    public static final String TODO_TYPE = "6";
    //行事历
    public static final String BEHAVE_CALENDAR = "7";
    //日历
    public static final String DAY_CALENDAR = "8";
    //我孩子的作业
    public static final String CHILDREN_HOME_WORK = "9";
    //周工作安排
    public static final String WEEKLY_PLAN = "10";
    //不在岗教职工
    public static final String NO_TEACHING_TEACHER = "11";
    //新高考选课流程
    public static final String XIN_GAO_KAO_FLOW = "12";
    //openapi统计图
    public static final String OPENOPI_COUNT_TYPE = "13";

    public static final String CHART_TYPE_RADAR = "radar";
    public static final String CHART_TYPE_HISTOGRAM = "histogram";
    public static final String CHART_TYPE_LINE = "line";
    public static final String CHART_TYPE_PIE = "pie";
    //通讯录分组类型  1-自定义组、2-部门、3-职务
    public static final String ADDBOOK_CUSTOM_GROUP = "1";

    public static final String ADDBOOK_DEPT_GROUP = "2";

    public static final String ADDBOOK_DUTY_GROUP = "3";


    /**
     * eis6 he eis5 url
     */
    public static final String EIS6_DOMAIN = "EIS6.DOMAIN";
    public static final String EIS5_DOMAIN = "EIS5.DOMAIN";


    //6.0中的办公公告的类型  bulletinType
    public static final String OFFICE_BULLETIN_TYPE = "3";
    /** 通知 */
    public static final String OFFICE_BULLETIN_TYPE_NOTICE = "1";

    //6.0中的行事历的类型
    public static final String OFFICE_BEHAVECALENDAR_TYPE = "2";



    public static final String PERIOD_MORN  = CourseSchedule.PERIOD_INTERVAL_1;
    public static final String PERIOD_AM    = CourseSchedule.PERIOD_INTERVAL_2;
    public static final String PERIOD_PM    = CourseSchedule.PERIOD_INTERVAL_3;
    public static final String PERIOD_NIGHT = CourseSchedule.PERIOD_INTERVAL_4;



    //---------------汶川桌面对接参数---------------
    /** 学易云单点教师登录URL */
    public static final String WENCHUAN_PASSPORT_URL_TEACHER = "http://sso.api.xueyiyun.com/login/teacherlogin";
    /** 学易云单点学生登录URL */
    public static final String WENCHUAN_PASSPORT_URL_STUDENT = "http://sso.api.xueyiyun.com/login/studentlogin";
    /** 获取token的密钥*/
    public static final String WENCHUAN_APP_SECRET = "040938837f8ee844c5ea77a265627ebc";
    /** 取Token的地址*/
    public static final String WENCHUAN_GET_TOKEN_URL = "http://sso.api.xueyiyun.com/auth/getaccesstoken";
    /** 获取Token和登录都需要带上指定的服务ID */
    public static final String WENCHUAN_APP_ID = "163460";
    /** 汶川易学云上的AP key=appId value = app_secret*/
    public static final Map<String,String> WENCHUAN_YI_XUE_YUN_AP = new HashMap<String, String>(2){{
        put(WENCHUAN_APP_ID, WENCHUAN_APP_SECRET);
        put("163546", "75d2a771b799a6ca9bf0e72b9faa7489");
    }};

    public static final Map<String,Integer> WENCHUAN_SUBJECT_MAP = new HashMap<String, Integer>(){{
        put("化学",10);
        put("物理",11);
        put("语文",13);
        put("数学",12);
        put("英语",14);
        put("历史",15);
        put("地理",16);
        put("政治",17);
        put("生物",18);
    }};
    //---------------汶川桌面对接参数---------------
    
    //OpenApiV20调用接口的三中类型
    public static final String OPENAPI_DESCRIPTION ="开发者调用接口：";
    
    public static final String OPENAPI_V20_ONE_PARAMETER = "开发者调用接口：/v2.0/type";
    public static final String OPENAPI_V20_TWO_PARAMETER = "开发者调用接口：/v2.0/type/id";
    public static final String OPENAPI_V20_THREE_PARAMETER = "开发者调用接口：/v2.0/type/id/subType";
    
    //OpenApiV21调用接口的三中类型
    public static final String OPENAPI_V21_ONE_PARAMETER = "开发者调用接口：/v2.1/basedata/queryData/type";
    public static final String OPENAPI_V21_TWO_PARAMETER = "开发者调用接口：/v2.1/basedata/queryData/type/id";
    public static final String OPENAPI_V21_THREE_PARAMETER = "开发者调用接口：/v2.1/basedata/queryData/type/id/subType";
    
    
    //调用接口的平均分
    public static final Integer OPENAPI_AVERAGE = 12;

    //模块是否已经添加
    public static final String FUNCTIONAREA_IS_ADD = "1";
    public static final String FUNCTIONAREA_IS_NOT_ADD = "0";
    
    
}
