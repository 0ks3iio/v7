/* 
 * @(#)Constant.java    Created on 2017-3-4
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.constant;

public class Constant {

    public final static String KEY_OPS_USER = "ops.user";

    public final static String REGION_CODE = "REGION.CODE";

    public final static String FILE_URL = "FILE.URL";
    public static final String FILE_SECOND_URL = "FILE.SECOND.URL";

    public final static String FILE_PATH = "FILE.PATH";

    public final static String PASSPORT_URL = "PASSPORT.URL";
    public final static String PASSPORT_SECOND_URL = "PASSPORT.SECOND.URL";
    /**
     * 个人中心参数，初始化passport需要
     */
    public final static String MEMBER_URL = "MEMBER.URL";
    /**
     * sys_option 是否接入passport
     */
    public final static String SYSTEM_PASSPORT_SWITCH = "SYSTEM.PASSPORT.SWITCH";

    // 密码规则（正则）
    public final static String SYSTEM_PASSWORD_EXPRESSION = "SYSTEM.PASSWORD.EXPRESSION";
    // 弱密码规则（正则）
    public final static String SYSTEM_PASSWORD_STRONG = "SYSTEM.PASSWORD.STRONG";
    // 密码规则说明
    public final static String SYSTEM_PASSWORD_ALERT = "SYSTEM.PASSWORD.ALERT";

    public final static String PASSPORT_CHECKTICKET = "PASSPORT.CHECKTICKET";

    public final static String PASSPORT_CLIENTLOGIN = "PASSPORT.CLIENTLOGIN";

    public final static String PASSPORT_DOC_LINK = "PASSPORT.DOC.LINK";

    public final static String PASSPORT_ACTIVE_LOGOUT = "PASSPORT.ACTIVE.LOGOUT";

    public final static String PASSPORT_DEMO_LINK = "PASSPORT.DEMO.LINK";

    public final static String PASSPORT_VERIFY_RUL = "PASSPORT.VERIFY";

    // smtp服务器地址，类似smtp.21cn.com
    public final static String EMAIL_SERVER = "EMAIL.SERVER";
    // 邮箱用户名
    public final static String EMAIL_USERNAME = "EMAIL.USERNAME";
    // 邮箱密码
    public final static String EMAIL_PASSWORD = "EMAIL.PASSWORD";
    // 管理员联系方式,如QQ:33XXXXXX,PHONE:187XXXXX
    public final static String ADMIN_CONTACT = "ADMIN.CONTACT";
    /*
     * 基本信息参数
     */
    public final static String PLAT_NAME = "plat.name";
    public final static String PLAT_ABBREVIATION = "plat.abbreviation";
    public final static String PLAT_LOGO_PATH = "plat.logo.path";
    public final static String LPAT_BOTTOM = "plat.bottom";
    // 反白logo
    public final static String PLAT_WHITE_LOGO_PATH = "plat.white.logo.path";

    // 应用来源(多个的情况用逗号隔开,1,内部应用,2,第三方应用)
    public final static String APP_SERVER_CLASS = "app.server.class";
    // 是否新增商品信息(兼容东莞)
    public final static String IS_ADD_BASE_WARE = "is.add.base.ware";

    /** base_sys_option首页地址参数*/
    public static final String INDEX_URL = "INDEX.URL";
    /** base_sys_option second首页地址参数，内外网访问区分*/
    public static final String INDEX_SECOND_URL = "INDEX.SECOND.URL";

    public static final String EIS_RUN_CODE = "desktop7";
    public static final String COOPERATOR_ID = "COOPERATOR.ID";


    /***-------登录页设置参数常量-----***/

    /**是否自定义登陆页*/
    public static final String SELF_LOGIN_PAGE = "SYSTEM.SELF.LOGIN.PAGE";

    /**登录页参数配置文件路径 取参数时需根据 FILE.PATH获取*/
    public static final String LOGIN_PAGE_DIR = "login_page.dir";
    /**登陆页配置文件名称 region + LOGIN_PAGE_CONF_NAME */
    public static final String LOGIN_PAGE_CONF_NAME = "_login_page_set.properties";

    /**其他页面title*/
    public static final String COMMON_PAGE_TITLE = "common_page_title";
    /**登录页title*/
    public static final String  LOGIN_PAGE_TITLE = "login_page_title";
    /**登录页logo*/
    public static final String  LOGIN_PAGE_LOGO_NAME = "login_page_logo_name";
    /**登录页LOGO背景图片*/
    public static final String  LOGIN_PAGE_LOGO_BG_PATH = "login_page_logo_bg_path";
    /**登录页背景图片*/
    public static final String  LOGIN_PAGE_BG_PATH ="login_page_bg_path";
    /**手机号作为账号登录*/
    public static final String PHONE_AS_USER_NAME = "phone_as_user_name";
    /** 登陆页是否启用动画 */
    public static final String LOGIN_PAGE_PLAYER = "login_player";
    /** 登录页警告说明 */
    public static final String LOGIN_PAGE_WARN = "login_page_warn";
    
    /** 登陆页是否启用登录页logo */
    public static final String ENABLE_LOGIN_PAGE_LOGO_NAME = "enable_login_page_logo_name";
    /** 登陆页是否启用登录页LOGO背景图片 */
    public static final String ENABLE_LOGIN_PAGE_LOGO_BG_PATH = "enable_login_page_logo_bg_path";

}
