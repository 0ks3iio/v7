package net.zdsoft.desktop.login;

import net.zdsoft.system.constant.Constant;

/**
 * 登录相关的常量参数
 * @author ke_shen@126.com
 * @since 2018/1/24 下午3:08
 */
final public class LoginConstant {

	////key

	/** sessionId key 利用passport ticket(uuid) */
	public static final String SESSION_ID_KEY 	= "session.id.by.ticket.id.";
	/** passport ticket key*/
	public static final String TICKET_KEY 		= "ticket";
	/** 等录错误信息 name key */
	public static final String LOGIN_ERROR_NAME = "loginError";

	/** passportSessionId attribute name*/
	public static final String SESSION_ATTRIBUTE_NAME = "passportSessionId";


	////code
	/** eis首页地址 option_code*/
	public static final String INDEX_URL_CODE 	= Constant.INDEX_URL;
	/** 是否使用passport登录页iniid （sys_option）*/
	public static final String USE_PASSPORT_LOGIN_CODE = "SYSTEM.USE.EISLOGIN.FOR.PASSPORT";

	public static final Integer USER_STATE_NORMAL = 1;


	////common static params
	/** passport 登录页action */
	public static final String PASSPORT_PAGE_ACTION = "/fpf/login/loginForPassport.action";
}
