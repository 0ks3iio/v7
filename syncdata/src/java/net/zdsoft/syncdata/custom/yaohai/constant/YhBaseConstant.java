package net.zdsoft.syncdata.custom.yaohai.constant;

public class YhBaseConstant {
	public static final String MYSQL_CONNECTION_DRIVE = "com.mysql.jdbc.Driver";
	public static final String MYSQL_CONNECTION_URL = "jdbc:mysql://36.7.123.2:3306/eis_test";
	public static final String MYSQL_CONNECTION_USERNAME = "yaohai_jiaoyuju";
	public static final String MYSQL_CONNECTION_PASSWORD = "zdsoft.net2018";
	
	public static final String MYSQL_SCHOOL_TABLE_NAME = "base_school"; 
	public static final String MYSQL_CLASS_TABLE_NAME = "base_class"; 
	public static final String MYSQL_TEACHER_TABLE_NAME = "base_teacher"; 
	public static final String MYSQL_STUDENT_TABLE_NAME = "base_student"; 
	public static final String MYSQL_USER_TABLE_NAME = "base_user"; 
	
	
	/**
	 * 瑶海同步学生的缓存key
	 */
	public static final String YAOHAI_STUDENT_REDIS_KEY = "syncdata.yaohai.class.student.time";
	/**
	 * 瑶海同步学校的缓存key
	 */
	public static final String YAOHAI_SCHOOL_REDIS_KEY = "syncdata.yaohai.class.school.time";
	/**
	 * 瑶海同步教师的缓存key
	 */
	public static final String YAOHAI_TEACHER_REDIS_KEY = "syncdata.yaohai.class.teacher.time";
	/**
	 * 瑶海同步班级的缓存key
	 */
	public static final String YAOHAI_CLAZZ_REDIS_KEY = "syncdata.yaohai.class.clazz.time";
	/**
	 * 瑶海同步用户的缓存key
	 */
	public static final String YAOHAI_USER_REDIS_KEY = "syncdata.yaohai.class.user.time";
	/**
	 * 瑶海同步课程的缓存key
	 */
	public static final String YAOHAI_COURSE_REDIS_KEY = "syncdata.yaohai.class.course.time";
	/**
	 * 瑶海同步任课信息的缓存key
	 */
	public static final String YAOHAI_CLASS_TEACHING_REDIS_KEY = "syncdata.yaohai.class.classTeaching.time";
	
	
	
	
	
	public static final String YAOHAI_SCHOOL_REGION_CODE_DEFAULT = "340102";
	
	public static final int YAOHAI_PAGESIZE_DEFAULT = 1000;
	public static final int YAOHAI_INDEX_DEFAULT = 1000;
	
	public static final String YAOHAI_USER_PWD_DEFAULT = "123456";
	
	public static final int YAOHAI_ISGRADUATE_DATE_DEFAULT = 715;  //七月15日
	
}
