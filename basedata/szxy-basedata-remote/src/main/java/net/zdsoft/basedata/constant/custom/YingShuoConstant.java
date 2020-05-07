package net.zdsoft.basedata.constant.custom;

import net.zdsoft.basedata.constant.BaseSaveConstant;


public class YingShuoConstant extends BaseSaveConstant{
	
	
//    public static final String YINGSHUO_GET_LOGIN_USER_URL = "http://tsuniapi.ys100.com/uniapi/sso/getmgruser";
    public static final String YINGSHUO_GET_LOGIN_USER_URL = "https://proxy.schoolcloud.ys100.com/es/management/signal/check";
	public static final String TICKET_NAME = "ticket";
	public static final String MODULE_ID = "moduleId";
	public static final String UID_NAME = "uid";
	public static final String GOAL_MODEL_PARAM_NAME = "model";
	public static final String NEW_USER_NAME = "newUserName";
	/**
	 * 鹰硕账号的前缀
	 */
	public static final String YINGSHUO_BEFORE_USERNAME_VALUE = "ys_";
	/**
	 * 鹰硕学校的前缀
	 */
	public static final String YINGSHUO_BEFORE_SCHOOLNAME_VALUE = "(鹰硕)";
	/**
	 * 数据的解析
	 */
	public static final String RESOLVE_DATA_NAME = "data";
	
	
	public static final String SUCCESS_CODE_VALUE = "9996";
	
	
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
	public static final String YS_GET_EDU_UNIT_URL = YSBeforeUrl + "/out/api/basic/sys-edu-bureau-open/select";
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
	public static final String YS_GET_UNIT_URL = YSBeforeUrl + "/out/api/basic/schoolinfor-campus-schedule/select";
	/**
	 * 获取部门
	 */
	public static final String YS_GET_DEPT_URL = YSBeforeUrl + "/out/api/basic/department_by_school_fkcode/select";
	/**
	 * 获取班级
	 */
	public static final String YS_GET_CLAZZ_URL = YSBeforeUrl + "/out/api/basic/classes/get-basic-classlist";
	/**
	 * 获取学生
	 */
	public static final String YS_GET_STUDENT_URL= YSBeforeUrl + "/out/api/basic/student-infor/select";
	/**
	 * 获取教师
	 */
	public static final String YS_GET_TEACHER_URL= YSBeforeUrl + "/out/api/basic/worker-infor/select";
	/**
	 * 获取楼层
	 */
	public static final String YS_GET_BUILDING_URL= YSBeforeUrl + "/out/api/basic/selectbuilding-bybuilding_id/select";
	/**
	 * 获取场地
	 */
	public static final String YS_GET_PLACE_URL= YSBeforeUrl + "/out/api/basic/classroom-by-classroom-id/select";
	/**
	 * 获取学校学年学期
	 */
	public static final String YS_GET_SCHOOL_SEMESTER_URL= YSBeforeUrl + "/out/api/basic/school-term/select";
	/**
	 * 获取课程
	 */
	public static final String YS_GET_COURSE_URL= YSBeforeUrl + "/out/api/basic/course/select-by-page";
	
	
	/**
	 * 鹰硕同步学生的缓存key
	 */
	public static final String YS_STUDENT_REDIS_KEY = "syncdata.tianchang.class.student.time";
	/**
	 * 鹰硕同步教育局的缓存key
	 */
	public static final String YS_EDU_REDIS_KEY = "syncdata.tianchang.class.edu.time";
	/**
	 * 鹰硕同步学校的缓存key
	 */
	public static final String YS_SCHOOL_REDIS_KEY = "syncdata.tianchang.class.school.time";
	/**
	 * 鹰硕同步教师的缓存key
	 */
	public static final String YS_TEACHER_REDIS_KEY = "syncdata.tianchang.class.teacher.time";
	/**
	 * 鹰硕同步班级的缓存key
	 */
	public static final String YS_CLAZZ_REDIS_KEY = "syncdata.tianchang.class.clazz.time";
	/**
	 * 鹰硕同步用户的缓存key
	 */
	public static final String YS_USER_REDIS_KEY = "syncdata.tianchang.class.user.time";
	/**
	 * 鹰硕同步课程的缓存key
	 */
	public static final String YS_COURSE_REDIS_KEY = "syncdata.tianchang.class.course.time";
	/**
	 * 鹰硕同步任课信息的缓存key
	 */
	public static final String YS_CLASS_TEACHING_REDIS_KEY = "syncdata.tianchang.class.classTeaching.time";
	/**
	 * 鹰硕同步年级的缓存key
	 */
	public static final String YS_GRADE_REDIS_KEY = "syncdata.tianchang.class.grade.time";
	
	/**
	 * 获取token的缓存key
	 */
	public static final String YS_TOKEN_REDIS_KEY = "ys.token.redis.key";
	
	/**
	 * 获取timestamp的缓存key
	 */
	public static final String YS_APP_TIMESTAMP_REDIS_KEY = "ys.app.timestamp.redis.key.";
}
