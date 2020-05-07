package net.zdsoft.syncdata.custom.tianchang.constant;

public class TcBaseConstant {

	
	private static String tcBeforeUrl = "http://www.ahedu.cn/SNS/index.php?app=public&mod=Api&areaId=1213"
			+ "&AppName=tianchang&skip=0&limit=1000&act=";
	
	public static final String TC_API_SECRET = "e229d80fca4b8d25D7285F00E4363E00";
	public static final String TC_APP_NAME = "tianchang";
	public static final String TC_SIGH_VAL = "123456";
	public static final String TC_AREA_ID_VAL = "1213";  
	public static final String TC_REGION_CODE_VAL = "341181"; //默认的地区码
	
	public static final String TC_SCHOOL_ADMIN_TYPE_NAME = "schoolmanager";
	public static final String TC_EDU_ADMIN_TYPE_NAME = "countymanage";
	public static final String TC_TEACHER_TYPE_NAME = "teacher";
	public static final String TC_STUDENT_TYPE_NAME = "student";
	
	
	public static final String TC_NORMAL_TYPE_NAME = "teacher";
	
	public static final String TC_DEFAULT_STU_NAME = "tcStudent";
	public static final String TC_DEFAULT_STU_CODE = "0000";
	
	public static final String TC_USERNAME_DEFEULT_BEFORE = "tc_";
	/**
	 * 数据的解析
	 */
	public static final String TC_RESOLVE_DATA_NAME = "data";
	/**
	 * 默认的密码的值
	 */
	public static final String TC_DEFAULT_PASS_WORD_VALUE = "12345678";
	/**
	 * 天长的获取教育局
	 */
	public static final String TC_GET_EDU_UNIT_URL = tcBeforeUrl + "listEduOrgByAreaId";
	/**
	 * 天长的获取教育局教师
	 */
	public static final String TC_GET_EDU_TEACHER_URL= tcBeforeUrl + "listEduOrgUserByOrgId";
	/**
	 * 天长的获取单位
	 */
	public static final String TC_GET_UNIT_URL = tcBeforeUrl + "listSchoolByAreaId";
	/**
	 * 天长的获取班级
	 */
	public static final String TC_GET_CLAZZ_URL = tcBeforeUrl + "listClassBySchoolId";
	/**
	 * 天长的获取学生
	 */
	public static final String TC_GET_STUDENT_URL= tcBeforeUrl + "listStudentBySchoolId";
	/**
	 * 天长的获取教师
	 */
	public static final String TC_GET_TEACHER_URL= tcBeforeUrl + "listTeacher";
	/**
	 * 天长同步学生的缓存key
	 */
	public static final String TC_STUDENT_REDIS_KEY = "syncdata.tianchang.class.student.time";
	/**
	 * 天长同步教育局的缓存key
	 */
	public static final String TC_EDU_REDIS_KEY = "syncdata.tianchang.class.edu.time";
	/**
	 * 天长同步学校的缓存key
	 */
	public static final String TC_SCHOOL_REDIS_KEY = "syncdata.tianchang.class.school.time";
	/**
	 * 天长同步教师的缓存key
	 */
	public static final String TC_TEACHER_REDIS_KEY = "syncdata.tianchang.class.teacher.time";
	/**
	 * 天长同步班级的缓存key
	 */
	public static final String TC_CLAZZ_REDIS_KEY = "syncdata.tianchang.class.clazz.time";
	/**
	 * 天长同步用户的缓存key
	 */
	public static final String TC_USER_REDIS_KEY = "syncdata.tianchang.class.user.time";
	/**
	 * 天长同步课程的缓存key
	 */
	public static final String TC_COURSE_REDIS_KEY = "syncdata.tianchang.class.course.time";
	/**
	 * 天长同步任课信息的缓存key
	 */
	public static final String TC_CLASS_TEACHING_REDIS_KEY = "syncdata.tianchang.class.classTeaching.time";
	/**
	 * 天长同步年级的缓存key
	 */
	public static final String TC_GRADE_REDIS_KEY = "syncdata.tianchang.class.grade.time";
	
	/**
	 * 获取appSecret的缓存key
	 */
	public static final String TC_APP_SECRET_REDIS_KEY = "tc.app.secret.redis.key.";
	
	/**
	 * 获取timestamp的缓存key
	 */
	public static final String TC_APP_TIMESTAMP_REDIS_KEY = "tc.app.timestamp.redis.key.";
}
