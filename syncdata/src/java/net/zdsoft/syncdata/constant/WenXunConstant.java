package net.zdsoft.syncdata.constant;

/**
 * @author yangsj  2018年4月23日下午4:28:49
 */
public class WenXunConstant {
	
	private static String wxBeforeUrl = "http://smart.winshareyun.cn/winshare-web-portal";
	/**
	 * 文轩账号的前缀
	 */
	public static final String WENXUN_BEFORE_USERNAME_VALUE = "scwx_";
	/**
	 * 文轩学校的前缀
	 */
	public static final String WENXUN_BEFORE_SCHOOLNAME_VALUE = "(文轩)";
	/**
	 * 数据的解析
	 */
	public static final String RESOLVE_DATA_NAME = "data";
	
	/**
	 * 峨眉二小学校
	 */
	public static final String DEPLOY_EMEIERXIAO = "emex";
	
	/**
	 * 密码的参数
	 */
	public static final String PASSWORD_PARAM_NAME = "password";
	/**
	 * 默认isdeleted 的值
	 */
	public static final int DEFAULT_IS_DELETED_VALUE = 0;
	
	/**
	 * 默认evensource 的值   0 本地，1 订阅消息
	 */ 
	public static final int DEFAULT_EVENT_SOURCE_VALUE = 1;
	
	/**
	 * 默认的密码的值
	 */
	public static final String DEFAULT_PASS_WORD_VALUE = "scwx12345678";
	
	/**
	 * 文轩线程池的最大数
	 */
	public static final int WX_THREAD_POOL_MAX = 5;
	
	
	/**
	 * 文轩请求基础数据的接口
	 */

	public static final String WENXUN_BASE_DATA_URL = "http://smart.winshareyun.cn/winshare-web-portal/winshare/api/um/multi.do"; 
	
	/**
	 * 文轩请求学年学期的接口
	 */

	public static final String WENXUN_BASE_SEMESTER_DATA_URL = wxBeforeUrl + "/winshare/api/basedata/getSchoolYear.do"; 
	
	/**
	 * 文轩请求校区/学部的接口
	 */

	public static final String WENXUN_BASE_TEACH_AREA_DATA_URL = "http://smart.winshareyun.cn/winshare-web-portal/winshare/api/basedata/getSchoolDistricts.do";
	
	/**
	 * 文轩请求 学校教学楼的教室 接口
	 */

	public static final String WENXUN_BASE_TEACH_PLACE_DATA_URL = wxBeforeUrl + "/winshare/api/basedata/getClassRooms.do"; 
	
	/**
	 * 文轩同步结果通知接口
	 */
	public static final String WENXUN_SYN_END_RESULT_URL = "http://smart.winshareyun.cn/winshare-web-portal/winshare/api/syn/synresult.do"; 
		
	/**
	 * 文轩请求appid
	 */
	public static final String WENXUN_APP_ID_VALUE = "461a2248eb42a3444bba7f364d1cb2f1";
	
	
	/**
	 * 文轩请求appkey
	 */
	public static final String WENXUN_APP_KEY_VALUE = "d82f33c59d5dc045cbb8ee42e970dfad";
	
	
	/**
	 * 文轩请求机构code
	 */
	public static final String WENXUN_UNIT_CODE_VALUE = "A25101";
	
	/**
	 * 文轩根据cursor = -1 就代表数据已经拉取完
	 */
	public static final String WENXUN_END_CURSOR_VALUE = "-1";
	
	
	/**
	 * 文轩同步教师的缓存key
	 */
	public static final String WENXUN_BEFORE_TEACHER_REDIS_KEY = "syncdata.wenxun.class.teacher.time";
	/**
	 * 文轩同步学生的缓存key
	 */
	public static final String WENXUN_BEFORE_STUDENT_REDIS_KEY = "syncdata.wenxun.class.student.time";
	/**
	 * 文轩同步家长的缓存key
	 */
	public static final String WENXUN_BEFORE_FAMILY_REDIS_KEY = "syncdata.wenxun.class.family.time";
	/**
	 * 文轩同步学校的缓存key
	 */
	public static final String WENXUN_BEFORE_SCHOOL_REDIS_KEY = "syncdata.wenxun.class.school.time";
	/**
	 * 文轩同步班级的缓存key
	 */
	public static final String WENXUN_BEFORE_CLAZZ_REDIS_KEY = "syncdata.wenxun.class.clazz.time";
	/**
	 * 文轩同步管理员的缓存key
	 */
	public static final String WENXUN_BEFORE_ADMIN_REDIS_KEY = "syncdata.wenxun.class.admin.time";
	
	/**
	 * 文轩同步成功的code值
	 */
	public static final String WX_SYN_SUCCESS_VALUE = "0";
	
	/**
	 * 文轩同步失败的code值
	 */
	public static final String WX_SYN_FAIL_VALUE = "-1";
	
	/**
	 * 文轩username 的截取长度
	 */
	public static final int WX_SUBSTRING_USER_NAEM_LENGTH = 20;
	
	/**
	 * 文轩家长的真实姓名的截取长度
	 */
	public static final int WX_SUBSTRING_REAL_NAEM_LENGTH = 30;
	
	/**
	 * 文轩挂载的regioncode 
	 */
	public static final String WX_UNIT_REGION_CODE_VALUE = "510000";
	
	/**
	 * 文轩学校挂载的unioncode 为 999开头的 
	 */
	public static final String WX_UNIT_UNION_CODE_BEFORE = "999";
	
}
