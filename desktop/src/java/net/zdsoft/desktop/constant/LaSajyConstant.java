package net.zdsoft.desktop.constant;

import net.zdsoft.framework.config.Evn;

/**
 * 拉萨公共参数
 *
 */
public class LaSajyConstant {
    
	//地址参数的定义
	private static final String LS_START_URL = Evn.getString("sync_data_lasa_ca_url");
	public static final String LS_PROVING_TOKEN_URL = LS_START_URL+ "/am/identity/isTokenValid"; //验证token
	public static final String LS_GET_USERINFO_URL = LS_START_URL+"/am/bjca/random/getAllUserAttributes";  //获得登录用户信息
	public static final String LS_GET_RANDOM_URL = LS_START_URL + "/am/bjca/random/generate";   //得到random
//	public static final String LS_LOGIN_OUT_URL = LS_START_URL + "/am/identity/logout"; //退出的地址
	public static final String LS_LOGIN_OUT_URL = Evn.getString("lasa_login_out_ca_url") + "/sso/outsideLogout"; //退出的地址
	public static final String LS_LOGIN_OUT_INDEX_URL = "http://www.xzeduc.cn"; //退出的地址	
	
	
	//参数名称定义
	public static final String LS_TOKEN_NAME = "tokenid";
	public static final String LS_RANDOM_NAME = "random";
	public static final String LS_USER_NAME = "uid";
	public static final String LS_PROVING_TOKEN_NAME = "boolean=true";
	public static final String LS_IDCARDNUMBER  = "idcardnumber";
	public static final String LS_REAL_NAME  = "cn";
	public static final String LS_MODEL_VALUE = "modelValue";
	public static final String LS_GO_URL = "url";
	public static final String LS_RESULT_CODE = "code";
	//验证tokenid 和退出时 subjectid
	public static final String LS_SUBJECTID_NAME = "subjectid";
	
	//保存tokenid 的key
	public static final String LS_SAVE_TOKENID_KEY = "la.save.tokenid";
	public static final String LS_SAVE_TOKENID_BY_USERID_KEY = "la.save.tokenid.by.userid";
	
	public static final String LS_FAILED_CODE = "500";
	public static final String LS_SUCCESS_CODE = "0";
	
}
