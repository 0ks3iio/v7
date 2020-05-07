package net.zdsoft.syncdata.custom.guangyuan.constant;

public class GyBaseConstant {
	private static String GyEndUrl = "/Admini/Share/ExternalInterface.asmx?wsdl"; 
	/**
	 * 苍溪平台的地址
	 */
	public static final String GYCX_PLATFORM_URL = "http://218.6.152.186:8570" + GyEndUrl;
	/**
	 * 朝天平台的地址
	 */
	public static final String GYCT_PLATFORM_URL = "http://118.121.232.6:8570" + GyEndUrl;
	
	/**
	 * 登录服务的userName
	 */
	public static final String GY_LOGIN_USERNAME = "210006";
	
	/**
	 * 登录服务的passWord
	 */
	public static final String GY_LOGIN_PASSWORD = "9ED7DB4CB7D0405F96DB7DE7013A8917";
	
	/**
	 * 正确获取到数据
	 */
	public static final String GY_TRUE_GET_RESULT = "0000";
	
	/**
	 * 苍溪单位的deptNo
	 */
	public static final String GYCX_DEPT_NO_VALUE = "510824";
	
	/**
	 * 朝天单位的deptNo
	 */
	public static final String GYCT_DEPT_NO_VALUE = "510812";
	
	/**
	 * 获取单位的moduleNo
	 */
	public static final String GY_UNIT_MODULE_NO_VALUE = "2001";
	
	/**
	 * 获取用户的moduleNo
	 */
	public static final String GY_USER_MODULE_NO_VALUE = "2002";
	
	
	public static final String GY_SCHOOL_REGION_CODE_DEFAULT = "510801";
	
	//朝天教育局的id 
	public static final String GY_CT_JYJ_UNIT_ID = "9e2346c5c08d4f9c9a880a8a6d220f0b";
	//苍溪教育局的id
	public static final String GY_CX_JYJ_UNIT_ID = "89ac2bfba65244cda3e137be53c02940";
	
	/**
	 * 苍溪同步用户的缓存key
	 */
	public static final String CANGXI_USER_REDIS_KEY = "syncdata.cangxi.class.user.time";
	/**
	 * 苍溪同步学校的缓存key
	 */
	public static final String CANGXI_SCHOOL_REDIS_KEY = "syncdata.cangxi.class.school.time";
	
	/**
	 * 朝天同步用户的缓存key
	 */
	public static final String CHAOTIAN_USER_REDIS_KEY = "syncdata.chaotian.class.user.time";
	/**
	 * 朝天同步学校的缓存key
	 */
	public static final String CHAOTIAN_SCHOOL_REDIS_KEY = "syncdata.chaotian.class.school.time";
	
	
	public static final String GY_ADD_TYPE_VAL = "Add";
	public static final String GY_UPDATE_TYPE_VAL = "Modify";
	public static final String GY_DELETE_TYPE_VAL = "Del";
	
	public static final int GY_USER_ADMIN_TYPE_VAL = 2;
	public static final int GY_USER_TEACHER_TYPE_VAL = 3;
	
	
}
