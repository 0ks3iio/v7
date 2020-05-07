package net.zdsoft.syncdata.custom.laibin.constant;

public class LaiBinConstant {

	public static final String SQLSERVER_CONNECTION_DRIVE = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String SQLSERVER_CONNECTION_URL = "jdbc:sqlserver://192.168.2.1:1433;DatabaseName=up6";
	public static final String SQLSERVER_CONNECTION_USERNAME = "sa";
	public static final String SQLSERVER_CONNECTION_PASSWORD = "Aa123";
	
	public static final String SQLSERVER_STUDENT_TABLE_NAME = "CARD_T_USERINFO"; 
	
	
	public static final String SQLSERVER_STUDENT_MODIFT_TIME_NAME = "ModifyDate"; 
	public static final String SQLSERVER_STUDENT_ORDER_NAME = "FactoryFixID";
	
	/**
	 * 瑶海同步学生的缓存key
	 */
	public static final String LAIBIN_STUDENT_REDIS_KEY = "syncdata.laibin.class.student.time";
	
	
	public static final int LAIBIN_PAGESIZE_DEFAULT = 1000;
	public static final int LAIBIN_INDEX_DEFAULT = 10;
}
