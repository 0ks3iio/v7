package net.zdsoft.eclasscard.data.constant;

import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.system.entity.config.UnitIni;

public class EccConstants {
	
	//打卡日志 1.学生 2.老师
	public static final String ECC_CLOCK_IN_STUDENT = "1";
	public static final String ECC_CLOCK_IN_TEACHER = "2";
	
	//考勤设置类型，1.上课  2.寝室 3.上下学
	public static final String CLASS_ATTENCE_SET_TYPE1 = "1";
	public static final String DORM_ATTENCE_SET_TYPE2 = "2";
	public static final String IN_OUT_ATTENCE_SET_TYPE3 = "3";
	
	//寝室考勤时间段类型：1.上课日考勤，2.收假日考勤
	public static final Integer DORM_ATTENCE_PERIOD_TYPE1 = 1;
	public static final Integer DORM_ATTENCE_PERIOD_TYPE2 = 2;
	//校门考勤时间段类型：1.放假日离校，2.休假日离校，3.临时离校，4.通校生离校
	public static final Integer GATE_ATTENCE_PERIOD_TYPE1 = 1;
	public static final Integer GATE_ATTENCE_PERIOD_TYPE2 = 2;
	public static final Integer GATE_ATTENCE_PERIOD_TYPE3 = 3;
	public static final Integer GATE_ATTENCE_PERIOD_TYPE4 = 4;
	
	
	public static final String ECC_MCODE_BPYT_1 = "10";//行政班
	public static final String ECC_MCODE_BPYT_2 = "20";//非行政班
	public static final String ECC_MCODE_BPYT_3 = "30";//寝室
	public static final String ECC_MCODE_BPYT_4 = "41";//校门（进）
	public static final String ECC_MCODE_BPYT_5 = "42";//校门（出）
	public static final String ECC_MCODE_BPYT_6 = "50";//签到
	public static final String ECC_MCODE_BPYT_7 = "60";//大厅
	
	//上课考勤 上学 下学
	
	
	public static final String ECC_CLOCK_PAGE_ATTENCE = "ecc_clock_page_attence";
	public static final String ECC_CLOCK_PAGE_LOGIN = "ecc_clock_page_login";
	
    public static final String PERIOD_MORN  = CourseSchedule.PERIOD_INTERVAL_1;
    public static final String PERIOD_AM    = CourseSchedule.PERIOD_INTERVAL_2;
    public static final String PERIOD_PM    = CourseSchedule.PERIOD_INTERVAL_3;
    public static final String PERIOD_NIGHT = CourseSchedule.PERIOD_INTERVAL_4;
    public static final int CLASS_TYPE_NORMAL = CourseSchedule.CLASS_TYPE_NORMAL;
    public static final int CLASS_TYPE_TEACH = CourseSchedule.CLASS_TYPE_TEACH;
//    public static final int CLASS_TYPE_SEVEN = CourseSchedule.CLASS_TYPE_SEVEN;
    public static final int CLASS_TYPE_4 = CourseSchedule.CLASS_TYPE_4;
    public static final int SUBJECT_TYPE_3 = CourseSchedule.SUBJECT_TYPE_3;
    /**
     * '1:未刷卡  2:迟到  3:请假  4:正常  '
     */
    public static final Integer CLASS_ATTENCE_STATUS1 = 1;
    public static final Integer CLASS_ATTENCE_STATUS2 = 2;
    public static final Integer CLASS_ATTENCE_STATUS3 = 3;
    public static final Integer CLASS_ATTENCE_STATUS4 = 4;
    
    /**
     * '0:初始化   1：未刷卡   2.请假  3.正常'
     */
    public static final Integer DORM_ATTENCE_STATUS0 = 0;
    public static final Integer DORM_ATTENCE_STATUS1 = 1;
    public static final Integer DORM_ATTENCE_STATUS2 = 2;
    public static final Integer DORM_ATTENCE_STATUS3 = 3;
    
    /**
     * '1:进校(上学) ２:出校 '(放学);
     */
    public static final Integer GATE_ATTENCE_STATUS1 = 1;
    public static final Integer GATE_ATTENCE_STATUS2 = 2;
    /**
     * '0:当前班牌   9:自定义';
     */
    public static final Integer ECC_SENDTYPE_0 = 0;
    public static final Integer ECC_SENDTYPE_9 = 9;
    
    public static final String FULL_SCREEN_SOURCE_TYPE_01 = "01";//单个班牌发布
    public static final String FULL_SCREEN_SOURCE_TYPE_02 = "02";//全校班牌发布
    
    public static final Integer ECC_SHOW_STATUS_0 = 0;//未展示
	public static final Integer ECC_SHOW_STATUS_1 = 1;//展示中
	public static final Integer ECC_SHOW_STATUS_2 = 2;//已展示
//	public static final Integer ECC_SHOW_STATUS_9 = 9;//展示中被退出，有其他操作
    
    
    /**
     * 寝室和校门考勤打卡学生显示
     */
    public static final String DORM_RECENT_CLOCK_REDIS = "dorm-recent-clock-redis";
    public static final String GATE_RECENT_CLOCK_REDIS = "gate-recent-clock-redis";
    public static final Integer DORM_RECENT_CLOCK_COUNT = 2;
    public static final Integer GATE_RECENT_CLOCK_COUNT = 3;
    /**
     * 在校离校状态
     */
    public static final Integer GATE_ATT_IN=1;//在校
    public static final Integer GATE_ATT_OUT = 2;//离校
    
    /**
     * 刷卡状态  1：已刷卡 2：未刷卡
     */
    public static final String SWIPE_STATE_1="1";//已刷卡
    public static final String SWIPE_STATE_2="2";//未刷卡
    public static final String SWIPE_STATE_3="3";//请假
    /**
     * 32个0
     */
    public static final String ZERO32 ="00000000000000000000000000000000"; 
    
    /**
     * 屏幕类型 1：横屏 2：竖屏
     */
    public static final String ECC_VIEW_1 = "1";
    public static final String ECC_VIEW_2 = "2";
    
    /**
     * redis锁前缀
     */
    public static final String CLOCK_IN_REDIS_LOCK_PREFIX = "clock-in-redis-prefix";
    /**
     * redis进出校消息前缀
     */
    public static final String GATE_CLOCK_IN_REDIS_MSG = "gate-clock-in-redis-msg";
    public static final String GATE_CLOCK_OUT_REDIS_MSG = "gate-clock-out-redis-msg";
    
    /**
     * 进出校消息
     */
    public static final String ECC_DINGDING_MSG_OUT = "已出校";
    public static final String ECC_DINGDING_MSG_IN = "已进校";
    
    /**
     * 公告类型
     */
    public static final Integer ECC_BULLETIN_TYPE_1 = 1; //普通
    public static final Integer ECC_BULLETIN_TYPE_2 = 2; //顶栏
    public static final Integer ECC_BULLETIN_TYPE_3 = 3; //全屏
    
    /**
     * 公告级别
     */
    public static final Integer ECC_BULLETIN_LEVEL_1 = 1; //校级
    public static final Integer ECC_BULLETIN_LEVEL_2 = 2; //班级
    
    /**
     * 全屏公告类型  templetType
     */
    public static final Integer ECC_BULLETIN_TEMPLETTYPE_1 = 1; //标准公告
    public static final Integer ECC_BULLETIN_TEMPLETTYPE_2 = 2; //喜报
    public static final Integer ECC_BULLETIN_TEMPLETTYPE_3 = 3; //欢迎致辞
    public static final Integer ECC_BULLETIN_TEMPLETTYPE_9 = 9; //欢迎致辞
    /**
     * 相册展示范围：1，单个对象 2，多个对象
     */
    public static final Integer ECC_FOLDER_RANGE_1 = 1;
    public static final Integer ECC_FOLDER_RANGE_2 = 2;
    /**
     * 相册类型：1，相册 2，视频  3，PPT
     */
    public static final Integer ECC_FOLDER_TYPE_1 = 1;
    public static final Integer ECC_FOLDER_TYPE_2 = 2;
    public static final Integer ECC_FOLDER_TYPE_3 = 3;
    
    /**
     * 荣誉类型
     */
    public static final Integer ECC_HONOR_TYPE_1 = 1; //班级
    public static final Integer ECC_HONOR_TYPE_2 = 2; //个人
    
    /**
     * 小图标识
     */
    public static final String ECC_SMALL_IMG = "_small"; 

    /**
     * 附件objectType
     */
    public static final String ECC_ATTACHMENT_TYPE = "ECLASSCARD";
    /**
     * 系统使用的班牌版本
     */
    public static final String ECC_USE_VERSION = UnitIni.ECC_USE_VERSION;
    public static final String ECC_USE_VERSION_0 = UnitIni.ECC_USE_VERSION_STANDARD;//标准
    public static final String ECC_USE_VERSION_1 = UnitIni.ECC_USE_VERSION_HW;//德育
    
    public static final String ECC_INDEX_LAYOUT = "ECLASSCARD.INDEX.LAYOUT";
    
    public static final Integer ECC_HONOR_STATUS_1 = 1;//未展示
	public static final Integer ECC_HONOR_STATUS_2 = 2;//展示中
	public static final Integer ECC_HONOR_STATUS_3 = 3;//已展示
	
	/**
	 * 全屏展示对象类型
	 */
    public static final String ECC_FULL_OBJECT_TYPE01 = "01";//全屏公告
    public static final String ECC_FULL_OBJECT_TYPE02 = "02";//考试门贴
    public static final String ECC_FULL_OBJECT_TYPE03 = "03";//相册集
    public static final String ECC_FULL_OBJECT_TYPE04 = "04";//视频集
    public static final String ECC_FULL_OBJECT_TYPE05 = "05";//PPT
    
	//请假信息小红点设置
	public static final Integer ECC_IS_FIRST_0 = 0; //没有小红点
	public static final Integer ECC_IS_FIRST_1 = 1; //有小红点
	
	//启用不启用
	public static final Integer ECC_START_USING_0 = 0; //不启用
	public static final Integer ECC_START_USING_1 = 1; //启用
	
	//其他设置的类型
	public static final Integer ECC_OTHER_SET_1 = 1; //全屏设置
	public static final Integer ECC_OTHER_SET_2 = 2; //视频声音设置
	public static final Integer ECC_OTHER_SET_3 = 3; //是否展示门贴
	public static final Integer ECC_OTHER_SET_4 = 4; //学生空间登录方式
	public static final Integer ECC_OTHER_SET_5 = 5; //PPT、图片播放速度
    public static final Integer ECC_OTHER_SET_6 = 6; //是否开启人脸识别服务
    public static final Integer ECC_OTHER_SET_7 = 7; //是否开启刷卡提示音
    public static final Integer ECC_OTHER_SET_8 = 8;//校园空间设置url
   // public static final Integer ECC_OTHER_SET_9 = 9;//人脸下发范围0全校1班级

	public static final String ECC_OTHER_SET = "ECC_OTHER_SET";
	
	public static final String PPT_CONVERT_TIMESTAMP_SET = "ppt-convert-timestamp-set-value";
	
	//留言 1.学生发送  2.家长发送
	public static final String ECC_LEAVE_WORD_1 = "1";
	public static final String ECC_LEAVE_WORD_2 = "2";
	//设备人脸状态 0正常  1失败  2服务端更新
	public static final Integer ECC_FACE_DEVICE_STATE_0 = 0;
	public static final Integer ECC_FACE_DEVICE_STATE_1 = 1;
	public static final Integer ECC_FACE_DEVICE_STATE_2 = 2;
	
	public static final String LEAVE_WORD_CACHE_HEAD = "eclasscard.leaveword.cache.head.";
	
	//优肯嘉兴市秀洲区印通小学定制不显示班级空间--select * from base_unit where unit_name='嘉兴市秀洲区印通小学';
	public static final String SCHOOL_YKJXSXZQYTXX="D34C890D8C9A40D385D70C2523754FCE";
	
	public static final Integer ECC_CLOCK_CLASS_0=0;//行政班班牌上课考勤
	public static final Integer ECC_CLOCK_CLASS_1=1;//行政班班牌上学放学考勤
	
	//1进出校  2上下学
	public static final Integer ECC_CLASSIFY_1=1;//进出校
	public static final Integer ECC_CLASSIFY_2=2;//上下学
	//对于同一班牌有多种刷卡要求
	public static final Integer ECC_BUSINESS_TYPE_0=0;//刷卡日志--用于上课考勤
	public static final Integer ECC_BUSINESS_TYPE_1=1;//刷卡日志--上下学
	
	public static final String ECC_FACE_SET_TYPE_1="1";//班级

    public static final String ECC_CLASS_TYPE_1="1";  //行政班
    public static final String ECC_CLASS_TYPE_2="2";  //教学班
}
