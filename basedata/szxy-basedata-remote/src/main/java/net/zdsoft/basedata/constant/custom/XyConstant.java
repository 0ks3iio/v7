package net.zdsoft.basedata.constant.custom;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

/**
 * TODO 这里实在没办法了，见谅
 * 使用Evn都被注释了，注释的部分是最开始的部分
 */
@Configuration
@Lazy(false)
public class XyConstant implements InitializingBean, EnvironmentAware {

	private Logger logger = LoggerFactory.getLogger(XyConstant.class);

	private Environment environment;

	@Override
	public void afterPropertiesSet() throws Exception {
		XyConstant.XY_START_URL = environment.getProperty("xingyun_sync_data_url");
		XyConstant.XY_APP_ID_VAL = environment.getProperty("xingyun_app_id");
		XyConstant.XY_APP_TOKEN_VAL = environment.getProperty("xingyun_app_token");
		XyConstant.XY_PUBLIC_KEY = environment.getProperty("xingyun_rsa_public_key");
		XyConstant.XY_IS_ENCRYPTION = environment.getProperty("xingyun_is_encryption");

		XyConstant.XINGYUN_JIECHENG_CLIENT_ID = environment.getProperty("xingyun_jiecheng_client_id");
		XyConstant.XINGYUN_JIECHENG_CLIENT_SECRET = environment.getProperty("xingyun_jiecheng_client_secret");
		XyConstant.XINGYUN_JIECHENG_PREFIX = environment.getProperty("xingyun_jiecheng_prefix");

		logger.warn("XyConstant load config from environment XY_START_URL [{}]", XY_START_URL);
		logger.warn("XyConstant load config from environment XY_APP_ID_VAL [{}]", XY_APP_ID_VAL);
		logger.warn("XyConstant load config from environment XY_APP_TOKEN_VAL [{}]", XY_APP_TOKEN_VAL);
		logger.warn("XyConstant load config from environment XY_PUBLIC_KEY [{}]", XY_PUBLIC_KEY);
		logger.warn("XyConstant load config from environment XY_IS_ENCRYPTION [{}]", XY_IS_ENCRYPTION);
		logger.warn("XyConstant load config from environment XINGYUN_JIECHENG_CLIENT_ID [{}]", XINGYUN_JIECHENG_CLIENT_ID);
		logger.warn("XyConstant load config from environment XINGYUN_JIECHENG_CLIENT_SECRET [{}]", XINGYUN_JIECHENG_CLIENT_SECRET);
		logger.warn("XyConstant load config from environment XINGYUN_JIECHENG_PREFIX [{}]", XINGYUN_JIECHENG_PREFIX);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	private static String XY_START_URL ;//= Evn.getString("xingyun_sync_data_url");
//			"http://183.250.187.102:7080";
	public static final String XY_GET_TICKET_URL = XY_START_URL+"/authentication/authentication/ticket";
	public static final String XY_GET_REFRESH_TICKET_URL = XY_START_URL + "/authentication/authentication/refresh";
	public static final String XY_GET_USER_INFO_URL = XY_START_URL + "/user-info-service/user";
	public static final String XY_LOGIN_URL = XY_START_URL + "/port/#/login";
	public static final String XY_LOGIN_OUT_URL = XY_START_URL + "/port/#/iframe-logout";
	
	
	public static final String XY_LISTEN_IP_VAL = "10.100.7.1:9092";
	public static String XY_APP_ID_VAL;// = Evn.getString("xingyun_app_id");
	public static String XY_APP_TOKEN_VAL;// = Evn.getString("xingyun_app_token");
	
	public static String XY_PUBLIC_KEY;// = Evn.getString("xingyun_rsa_public_key");
	public static String XY_IS_ENCRYPTION;// = Evn.getString("xingyun_is_encryption"); //kafka数据是否加密
	
	
	public static final String XY_APP_TOKEN_NAME = "appToken";
	public static final String XY_APP_ID_NAME = "appId";
	public static final String XY_TEMP_TOKEN_NAME = "temp-token";
	public static final String XY_HEAD_TICKET_NAME = "authentication";
	
	public static final String XY_TOKEN_NAME = "ticket";
	
	public static final String XY_PAGEURL_NAME = "pageURL";
	
	public static final String XY_SERVER_CODE_NAME = "serverCode";
	
	public static final String NEW_USER_NAME = "newUserName";
	
	public static final String XY_LOGIN_NAME = "loginId";
	
	public static final String XY_LOGIN_IMAGE = "static/images/desktop/headlog/xingyun.png";
	
	/**
	 * 星云账号的前缀
	 */
	public static final String XY_BEFORE_USERNAME_VALUE = "xy_";
	
	/**
	 * 星云管理员加 （星云管理员）
	 */
	public static final String XY_AFTER_TEACHER_NAME = "(星云管理员)";
	
	/**
	 * 星云是否允许同步更新
	 */
	public static final String XY_USING_UPDATE_TRIGGER = "XY_USING_UPDATE_TRIGGER";
	
	//0学生、1教师、2家长、3教育局、5学校信息、6教育局信息
	public static final String XY_TOPIC_VALUE = "";
	/**
	 * 学生
	 */
	public static final String STUDENT_PARTITION_VALUE = "0";
	
	/**
	 * 教师
	 */
	public static final String TEACHER_PARTITION_VALUE = "1";
	
	/**
	 * 家长
	 */
	public static final String FAMILY_PARTITION_VALUE = "2";
	
	/**
	 * 教育局 (用户信息，管理员信息)
	 */
	public static final String EDU_TEACHER_PARTITION_VALUE = "3";
	
	/**
	 * 学校信息 (学校的基础信息)
	 */
	public static final String SCHOOL_PARTITION_VALUE = "5";
	
	/**
	 * 教育局信息 (教育局的基础信息)
	 */
	public static final String EDU_PARTITION_VALUE = "6";
	
	/**
	 * 退出的用户信息 (退出的用户信息)
	 */
	public static final String USER_LOGOUT_VALUE = "7";
	
	public static final String TOPIC_VALUE = "1143051381184397312";
	
	public static final String GROUP_ID_VALUE = "xingyuntestend11";
	
	
	public static final String XINGYUN_LISTEN_START = "xingyun_listen_start";
	//bootstrap.servers
	public static final String XINGYUN_BOOTSTRAP_SERVERS = "xingyun_bootstrap_servers";
	
	public static final String XINGYUN_LOGIN_TICKET = "xingyun.login.ticket";
	
	public static final String XINGYUN_FIRST_LOGIN_TICKET = "xingyun.first.login.ticket";

	/**
	 *
	 * 星云项目-万朋平台单点接入捷成平台
	 */
	public static String XINGYUN_JIECHENG_CLIENT_ID;// = Evn.getString("xingyun_jiecheng_client_id");
	public static String XINGYUN_JIECHENG_CLIENT_SECRET;// = Evn.getString("xingyun_jiecheng_client_secret");
	public static String XINGYUN_JIECHENG_PREFIX;// = Evn.getString("xingyun_jiecheng_prefix");
}
