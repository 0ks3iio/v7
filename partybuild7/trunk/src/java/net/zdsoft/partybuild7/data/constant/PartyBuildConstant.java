package net.zdsoft.partybuild7.data.constant;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.zdsoft.framework.utils.DateUtils;

/**
 * 党建常量
 * @author weixh
 * @since 2017-9-19 下午2:21:02
 */
public class PartyBuildConstant {
	
	public static final int SUBSYSTEM_ID_PB = 14;
	
	public static final String PB_MODEL_SESSION = "pb.session.model.key.";
	/**
	 * 申请积极分子 被拒绝
	 */
	public static final int PARTY_STATE_DENY = -1;
	/**
	 * partyApplicationState-非党员（已经申请入党积极分子，但是未批准）
	 */
	public static final int PARTY_STATE_NOT = 0; // 非党员（已经申请入党积极分子，但是未批准）
	/**
	 * partyApplicationState-积极分子
	 */
	public static final int PARTY_STATE_ACTIVIST = 1; // 入党积极分子
	/**
	 * partyApplicationState-预备党员
	 */
	public static final int PARTY_STATE_PROBATIONARY = 2;
	/**
	 * partyApplicationState-党员
	 */
	public static final int PARTY_STATE_FORMAL = 3; // 党员
	
	/**
	 * 党员状态正常
	 */
	public static final int PARTY_MEMBER_STATE_NORMAL = 1;

	/**
	 * 党员状态异常
	 */
	public static final int PARTY_MEMBER_STATE_ABNORMAL = 2;
	
	/**
	 * 用户未在党建系统初始化的时候提示信息
	 */
	public static final String USER_NOT_INIT = "此用户未添加到党建系统中，无法访问本系统！";
	
	/**
	 * 活动level-普通党员
	 */
	public static final int PB_ACTIVITY_LEVEL_COMMON = 1;
	/**
	 * 活动level-领导
	 */
	public static final int PB_ACTIVITY_LEVEL_LEADER = 2;
	/**
	 * 页面用-审核状态
	 */
	public static final int MEMBER_AUDIT_STATE_ING = 0;
	public static final int MEMBER_AUDIT_STATE_PASS = 1;
	public static final int MEMBER_AUDIT_STATE_DENY = 2;

	// ---------------------常量---------------------------------------
	public static final int PARTY_MEMBER_TYPE_TEACHER = 1; // 教师类型
	public static final int PARTY_MEMBER_TYPE_STUDENT = 2; // 学生类型

	public static final int PARTY_INIT_POINTS = 0; // 新增党员默认积分
	
	// =======================模块权限判断============================
	public static Set<Integer> dyzzSet = new HashSet<Integer>();
	public static Set<Integer> jjfzkcSet = new HashSet<Integer>();
	public static Set<Integer> rdspSet = new HashSet<Integer>();
	static {
		dyzzSet.add(14023);
		dyzzSet.add(14061);
		
		jjfzkcSet.add(14021);
		jjfzkcSet.add(14059);
		
		rdspSet.add(14022);
		rdspSet.add(14060);
	}
	
	/**
	 * 首页用，获取时间显示
	 * @param creationTime
	 * @return
	 */
	public static String getTimeStr(Date creationTime){
		String str = "";
		if(creationTime == null){
			return str;
		}
		Calendar time = Calendar.getInstance();
		time.setTime(creationTime);
		Calendar now = Calendar.getInstance();
		long nowtimes = now.getTimeInMillis();
		long times = creationTime.getTime();
		long space = nowtimes-times;
		// <1h,显示n分钟前
		long hour = 60*60*1000l;
		if(space < hour){
			long minis = 60*1000l;
			if(space < 0 || space <= minis){
				str = "刚刚";
			} else {
				str = space/minis+" 分钟前";
			}
			return str;
		}
		// <24h,显示n小时前
		long day = 24*hour;
		if(space < day){
			str = space/hour+" 小时前";
			return str;
		}
		// <7d,显示n天前
		long week = 7*day;
		if(space < week){
			str = space/day+" 天前";
			return str;
		}
		// <1m,显示n周前
		long mon = 30*day;
		if(space < mon){
			str = space/week+" 周前";
			return str;
		}
		// <1年,显示n个月前
		long year = 12*mon;
		if(space < year){
			str = space/mon+" 月前";
			return str;
		}
		// 2017-09-21
		str = DateUtils.date2StringByDay(creationTime);
		return str;
	}
}
