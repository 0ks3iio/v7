package net.zdsoft.basedata.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 代课常量
 *
 */
public class TipsayConstants {
	/**
	 * 代课审核状态
	 * 0：申请中
	 * 1：最终通过（最终管理员同意）
	 * 2：不通过
	 * 3:代课老师同意
	 * 
	 * (代课审核记录中1:通过  2:不通过)
	 */
	public static final String TIPSAY_STATE_0="0";
	public static final String TIPSAY_STATE_1="1";
	public static final String TIPSAY_STATE_2="2";
	public static final String TIPSAY_STATE_3="3";
	
	/**
	 * 审核人类型 
	 * 1：最终管理员老师 2：普通老师 代课老师
	 */
	public static final String AUDITOR_TYPE_1="1";//最终管理员老师
	public static final String AUDITOR_TYPE_2="2";//普通老师 代课老师
	
	public static final int IF_0=0;
	public static final int IF_1=1;
	/**
	 * 管理员code
	 */
	public static final String EDUCATION_CODE ="86_edu_admin";
	/**
	 * 子系统--教务V7
	 */
	public static final String SUBSYSTEM_86="86";
	/**
	 * 代课类型 01：管理员直接安排 02:自主申请  03：自主申请管理员安排
	 */
	public static final String TIPSAY_TYPE_01="01";
	public static final String TIPSAY_TYPE_02="02";
	public static final String TIPSAY_TYPE_03="03";
	
	/**
	 * 审核对象类型 01代课，02调课
	 */
	public static final String TYPE_01="01";
	public static final String TYPE_02="02";
	
	
	
	/**
	 * 类型 1代课，2管课
	 */
	public static final String TYPE_1="1";
	public static final String TYPE_2="2";
	/**
	 * 3 调课
	 */
	public static final String TYPE_3="3";
	
	public static Map<String,String> typeMap=new HashMap<>();
	public static Map<String,String> tipsayTypeMap=new HashMap<>();
	static {
		typeMap.put(TYPE_1, "代课");
		typeMap.put(TYPE_2, "管课");
		tipsayTypeMap.put(TIPSAY_TYPE_01, "直接教务安排");
		tipsayTypeMap.put(TIPSAY_TYPE_02, "自主申请");
		tipsayTypeMap.put(TIPSAY_TYPE_03, "申请教务安排");
	}
	
	public static String SEND_MESSAGE="SYSTEM.TIPSAY.MESSAGE";
}
