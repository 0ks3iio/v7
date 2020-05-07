package net.zdsoft.basedata.constant;

/**
 * @author yangsj  2018年7月25日上午11:47:06
 */
public class BaseSaveConstant {
	
	/**
	 * 数据的解析
	 */
	public static final String RESOLVE_DATA_NAME = "data";
	
	/**
	 * 地区的标志
	 */
	public static final String RESOLVE_DATA_AREA_NAME = "area";
	
	/**
	 * 验证非空字段的key
	 */
	public static final String  PROVING_BASE_SAVE_KEY = "errorField";
	
	/**
	 * 验证非空字段出现错误的下标
	 */
	public static final String  PROVING_BASE_SAVE_ERROR_KEY = "errorFieldIndex";
	
	/**
	 * 开启同步基础数据的接口
	 */
	public static final String  BASE_SYN_OPEN_KEY = "base.syn.scheduler.start";
	
	/**
	 * 是否加密
	 */
	public static final String  BASE_SYN_ENABLE_SECURITY = "base.syn.enable.security";
	
	/**
	 * 是否提供32位的主键id
	 */
	public static final String  BASE_SYN_RELATION = "base.syn.relation";
	/**
	 * 基础表的类型值
	 */
	public static final String  BASE_UNIT_TYPE = "unit";
	public static final String  BASE_SCHOOL_TYPE = "school";
	public static final String  BASE_GRADE_TYPE = "grade";
	public static final String  BASE_CLAZZ_TYPE = "clazz";
	public static final String  BASE_STUDENT_TYPE = "student";
	public static final String  BASE_TEACHER_TYPE = "teacher";
	public static final String  BASE_FAMILY_TYPE = "family";
	public static final String  BASE_SUBSCHOOL_TYPE = "subschool";
	public static final String  BASE_DEPT_TYPE = "dept";
	public static final String  BASE_USER_TYPE = "user";
	public static final String  BASE_CHECK_WORK_TYPE = "checkwork";
	public static final String  BASE_COURSE_TYPE = "course";
	public static final String  BASE_TEACH_BUILDING_TYPE = "teachBuliding";
	public static final String  BASE_TEACH_PLACE_TYPE = "teachPlace";
	
	
	/**
	 * 返回结果的code
	 */
	public static final String  BASE_SUCCESS_CODE = "1";
	public static final String  BASE_HALF_SUCCESS_CODE = "-2";
	public static final String  BASE_ERROR_CODE = "-1";
	
	
	/**
	 * 默认isdeleted 的值
	 */
	public static final int DEFAULT_IS_DELETED_VALUE = 0;
	
	public static final int TRUE_IS_DELETED_VALUE = 1;
	
	/**
	 * 默认evensource 的值   0 本地，1 订阅消息
	 */ 
	public static final int DEFAULT_EVENT_SOURCE_VALUE = 1;
	
	/**
	 * DM-BOOLEAN  1 --TRUE, 0--FALSE
	 */
	public static final int BOOLEAN_TRUE_VALUE = 1;
	public static final int BOOLEAN_FALSE_VALUE = 0;
	
	/**
	 * 获取的最大数量
	 */
	public static final int DEFAULT_MAX_PAGE_SIZE = 1000;
	
	/**
	 * 默认每页的获取数量
	 */
	public static final int DEFAULT_PAGE_SIZE = 1000;
	
	/**
	 * 学年学期  1---第一学期   2---第二学期
	 */
	public static final Integer FIRST_SEMESTER_VALUE = 1;
	public static final Integer SECTOND_SEMESTER_VALUE = 2;
	
	/**
	 * 默认密码
	 */
	public static final String DEFAULT_PASS_WORD_VALUE = "zdsoft123";
	
	/**
	 * 默认学号
	 */
	public static final String DEFAULT_STU_CODE = "0000";
}
