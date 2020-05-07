package net.zdsoft.syncdata.custom.yingshuo.constant;

import net.zdsoft.basedata.constant.BaseSaveConstant;

public class YingShuoNewConstant extends BaseSaveConstant{

	private static String YSBeforeUrl = "http://schoolcloud.ys100.com:8080";
	public static final String YS_USER_NAME = "luqiwei";
	public static final String YS_USER_PASSWORD = "123456";
	public static final String YS_REGION_CODE_VAL = "440306"; //默认的地区码
	public static final String YS_EDU_ID = "00000000000000204653389101924352"; //默认的挂载教育局id 
	
	public static final String YS_KEY_VALUE="324245340884566016";
	public static final String YS_SECURITY_VALUE="3b3ad60a-c6e3-4084-b8f7-3be8b70555da";
	
	public static final String YS_DEFAULT_STU_CODE = "0000";
	
	public static final String YS_USERNAME_DEFEULT_BEFORE = "ys_";
	
	public static final String YS_TOKEN_NAME = "x-auth-token";
	
	public static final String YS_AREA_VALUE = "yingshuo";
	
	/**
	 * 楼层类型  1--教学   99 --其他
	 */
	public static final String YS_BUILDING_TYPE_TEACH = "1";
	public static final String YS_BUILDING_TYPE_OTHER = "99";
	
	
	
	/**
	 * 数据的解析
	 */
	public static final String YS_RESOLVE_DATA_NAME = "data";
	public static final String YS_RESULT_CODE_NAME = "code";
	/**
	 * 默认的密码的值
	 */
	public static final String YS_DEFAULT_PASS_WORD_VALUE = "12345678";
	/**
	 * 获取token
	 */
	public static final String YS_GET_TOKEN_URL = "https://proxy.schoolcloud.ys100.com/es/management/out-token/get";
	/**
	 * 获取eduBureauFkCode
	 */
	public static final String YS_GET_FKCODE_URL =  YSBeforeUrl + "/pc/login"; 
	/**
	 * 获取教育局单位
	 */
	public static final String YS_GET_EDU_UNIT_URL = YSBeforeUrl + "/out/api/basic/syn/select-sys-edu-bureau";
	/**
	 * 获取教育局下面的学校外键
	 */
	public static final String YS_GET_SCHOOL_FKCODE_URL = YSBeforeUrl + "/out/api/basic/sys-edu-bureau-open/select-school";
	/**
	 * 获取用户信息
	 */
	public static final String YS_GET_USER_LIST_URL = "https://proxy.schoolcloud.ys100.com/out/api/wp/get/user-account";
	/**
	 * 获取单位
	 */
	public static final String YS_GET_UNIT_URL = YSBeforeUrl + "/out/api/basic/syn/select-schoolinfor-campus-schedule";
	/**
	 * 获取部门
	 */
	public static final String YS_GET_DEPT_URL = YSBeforeUrl + "/out/api/basic/syn/select-school-organization";
	/**
	 * 获取班级
	 */
	public static final String YS_GET_CLAZZ_URL = YSBeforeUrl + "/out/api/basic/syn/select-class-all";
	/**
	 * 获取学生
	 */
	public static final String YS_GET_STUDENT_URL= "https://proxy.schoolcloud.ys100.com/out/api/basic/syn/select-student-lsit";
	/**
	 * 获取教师
	 */
	public static final String YS_GET_TEACHER_URL= YSBeforeUrl + "/out/api/basic/syn/select-workers-all";
	/**
	 * 获取楼层
	 */
	public static final String YS_GET_BUILDING_URL= YSBeforeUrl + "/out/api/basic/syn/select-building-by-campus-fkcode";
	/**
	 * 获取场地
	 */
	public static final String YS_GET_PLACE_URL= YSBeforeUrl + "/out/api/basic/syn/select-classroom-by-building-fkcode";
	/**
	 * 获取学校学年学期
	 */
	public static final String YS_GET_SCHOOL_SEMESTER_URL= YSBeforeUrl + "/out/api/basic/syn/select-school-term-all";
	/**
	 * 获取课程
	 */
	public static final String YS_GET_COURSE_URL= YSBeforeUrl + "/out/api/basic/syn/select-courses-all";
	

	/**
	 * 获取token的缓存key
	 */
	public static final String YS_TOKEN_REDIS_KEY = "ys.token.new.redis.key";
	
}
