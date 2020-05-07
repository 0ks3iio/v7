package net.zdsoft.basedata.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseConstants {
	
	private BaseConstants(){
		 throw new IllegalStateException("Utility class");
	}
	
	/**********************学段**********************/
	/** 学段 幼儿园 */
	public static final Integer SECTION_INFANT = 0;
	/** 学段 小学 */
	public static final Integer SECTION_PRIMARY = 1;
	/** 学段 初中 */
	public static final Integer SECTION_JUNIOR = 2;
	/** 学段 高中 */
	public static final Integer SECTION_HIGH_SCHOOL = 3;
	/** 学段 职业初中 */
	public static final Integer SECTION_JUNIOR_CAREER = 4;
	/** 学段 中职 */
	public static final Integer SECTION_SECONDARY_VOCATIONAL = 5;
	/** 学段 特教 */
	public static final Integer SECTION_SPECIAL_EDUCATION = 6;
	/** 学段 成人中学 */
	public static final Integer SECTION_ADULT_MIDDLE_SCHOOL = 7;
	/** 学段 工读学校 */
	public static final Integer SECTION_REFORM_SCHOOL = 8;
	/** 学段 大学专科 */
	public static final Integer SECTION_COLLEGE = 9;
	/** 学段 大学本科 */
	public static final Integer SECTION_UNIVERSITY = 10;
	
	public static final String TEACLSS_TEACHING_LIMIE="SYSTEM.CLASSTEACHING.LIMIT.SWITCH";
	
	public static final String ONE_STR="1";
	
	public static final String ZERO_STR="0";
	
	/**课程库类型  科目 */
	public static final Integer TYPE_COURSE_DISCIPLINE = 0;
	/**
	 * 初始化数据GUID 32个0组成的字符串
	 */
	public static final String ZERO_GUID = "00000000000000000000000000000000";
	
	/**
	 * 男
	 */
	public final static int MALE = 1;
	/**
	 * 女
	 */
	public final static int FEMALE = 2;
	
	/**
	 * 必修课：选考
	 */
	public static final String  SUBJECT_TYPE_A="A";
	/**
	 * 必修课：学考
	 */
	public static final String  SUBJECT_TYPE_B="B";
	/**
	 * 必修课
	 */
	public static final String  SUBJECT_TYPE_BX="1";
	/**
	 * 选修课
	 */
	public static final String  SUBJECT_TYPE_XX="2";
	/**
	 * 选修Ⅰ-A
	 */
	public static final String  SUBJECT_TYPE_XX_4="4";
	/**
	 * 选修Ⅰ-B
	 */
	public static final String  SUBJECT_TYPE_XX_5="5";
	/**
	 * 选修Ⅱ
	 */
	public static final String  SUBJECT_TYPE_XX_6="6";
	/**
	 * 虚拟课程
	 */
	public static final String SUBJECT_TYPE_VIRTUAL ="3";
	
	public static final String PC_KC = "课程";
	
	public static final String PERIOD_INTERVAL_1="1";//早上
	public static final String PERIOD_INTERVAL_2="2";//上午
	public static final String PERIOD_INTERVAL_3="3";//下午
	public static final String PERIOD_INTERVAL_4="4";//晚上
	public static final String PERIOD_INTERVAL_9="9";//特殊时间-中午等
	
	public static final Map<String,String> PERIOD_INTERVAL_Map=new HashMap<String,String>();
	static{
		PERIOD_INTERVAL_Map.put(PERIOD_INTERVAL_1, "早自习");
		PERIOD_INTERVAL_Map.put(PERIOD_INTERVAL_2, "上午");
		PERIOD_INTERVAL_Map.put(PERIOD_INTERVAL_3, "下午");
		PERIOD_INTERVAL_Map.put(PERIOD_INTERVAL_4, "晚自习");
		PERIOD_INTERVAL_Map.put(PERIOD_INTERVAL_9, "特殊时间");
	}
	public static final Map<String,String> PERIOD_INTERVAL_Map2=new HashMap<String,String>();
	static{
		PERIOD_INTERVAL_Map2.put(PERIOD_INTERVAL_1, "早上");
		PERIOD_INTERVAL_Map2.put(PERIOD_INTERVAL_2, "上午");
		PERIOD_INTERVAL_Map2.put(PERIOD_INTERVAL_3, "下午");
		PERIOD_INTERVAL_Map2.put(PERIOD_INTERVAL_4, "晚上");
		PERIOD_INTERVAL_Map2.put(PERIOD_INTERVAL_9, "特殊时间");
	}
	
	public static final Map<String,String> strOfNumberMap=new HashMap<String,String>();
	static{
		strOfNumberMap.put("0", "零");
		strOfNumberMap.put("1", "一");
		strOfNumberMap.put("2", "二");
		strOfNumberMap.put("3", "三");
		strOfNumberMap.put("4", "四");
		strOfNumberMap.put("5", "五");
		strOfNumberMap.put("6", "六");
		strOfNumberMap.put("7", "七");
		strOfNumberMap.put("8", "八");
		strOfNumberMap.put("9", "九");
		
	}
	
	public static final Map<String,String> dayOfWeekMap=new HashMap<String,String>();
	static{
		dayOfWeekMap.put("0", "星期一");
		dayOfWeekMap.put("1", "星期二");
		dayOfWeekMap.put("2", "星期三");
		dayOfWeekMap.put("3", "星期四");
		dayOfWeekMap.put("4", "星期五");
		dayOfWeekMap.put("5", "星期六");
		dayOfWeekMap.put("6", "星期天");
		
	}
	
	public static final Map<String,String> dayOfWeekMap2=new HashMap<String,String>();
	static{
		dayOfWeekMap2.put("0", "周一");
		dayOfWeekMap2.put("1", "周二");
		dayOfWeekMap2.put("2", "周三");
		dayOfWeekMap2.put("3", "周四");
		dayOfWeekMap2.put("4", "周五");
		dayOfWeekMap2.put("5", "周六");
		dayOfWeekMap2.put("6", "周日");
		
	}
	
	public static final String SUBJECT_ZIXI = "7777";
	
	public static final String[] SUBJECT_73 =new String[]{
		"3001",//	政治
		"3002",//	历史
		"3003",//	地理
		"3006",//	物理
		"3011",//	化学
		"3020",//	生物
		"3037"//	技术
	};
	public static final String[] SUBJECT_63 =new String[]{
			"3001",//	政治
			"3002",//	历史
			"3003",//	地理
			"3006",//	物理
			"3011",//	化学
			"3020" //	生物
			//"3037"//	技术
	};
	//技术拆分成： 技术（通用技术）+信息技术
	public static final String[] SUBJECT_73_1 =new String[]{
			"3015",//信息技术
			"3037" //技术(通用技术)
		};
	
	public static final String[] SUBJECT_TYPES_YSY =new String[]{
		"3007",//语文
		"3008",//数学
		"3009"//英语
	};
	
	/**
	 * 理科
	 */
	public static final Set<String> WHS_73 =new HashSet<String>();
	static{
		WHS_73.add("3006");
		WHS_73.add("3011");
		WHS_73.add("3020");
	}
	/**
	 * 文科
	 */
	public static final Set<String> SDZ_73 =new HashSet<String>();
	static{
		SDZ_73.add("3002");
		SDZ_73.add("3003");
		SDZ_73.add("3001");
	}
	
	public static final Map<String, String> SUBJECT_CODE_CLASSNAME = new HashMap<String, String>();
	static{
		SUBJECT_CODE_CLASSNAME.put("3001", "politics");//	政治
		SUBJECT_CODE_CLASSNAME.put("3002", "history");//	历史
		SUBJECT_CODE_CLASSNAME.put("3003", "geography");//	地理
		SUBJECT_CODE_CLASSNAME.put("3006", "physics");//	物理
		SUBJECT_CODE_CLASSNAME.put("3011", "chemistry");//	化学
		SUBJECT_CODE_CLASSNAME.put("3020", "biology");//	生物
		SUBJECT_CODE_CLASSNAME.put("3037", "technology");//	技术
	}
	 public static final String DEPLOY_HANGWAI           = "hangwai";
	/**
	 * 杭州外国语学校 课程码--口试
	 */
	public static final String HW_CODE_KS = "TY3001";
	/**
	 * 杭州外国语学校  课程码集合--语文
	 */
	public static final List<String> HW_CODES_YU = new ArrayList<String>();
	static{
		HW_CODES_YU.add("GZ1001");  // 高中语文
		HW_CODES_YU.add("CZ2001");  // 初中语文
	}
	/**
	 * 杭州外国语学校 课程码--写作
	 */
	public static final String HW_CODE_XZ = "TY3002";
	/**
	 * 杭州外国语学校 课程码集合--外文
	 */
	public static final List<String> HW_CODES_WW = new ArrayList<String>();
	static{
		HW_CODES_WW.add("GZ1003");  // 高中外文
		HW_CODES_WW.add("CZ2003");  // 初中外文
	}
	/**
	 * 1 基础数据课表调整
	 */
	public static final String SCHEDULE_MODIFY_TYPE_1 = "1";
	/**
	 * 2 智能排课子系统 课表调整
	 */
	public static final String SCHEDULE_MODIFY_TYPE_2 = "2";
	
	/**
	 * C 调整班级课表
	 * T 调整老师课表
	 * P 调整场地课表
	 */
	public static final String SCHEDULE_CONTENT_TYPE_C = "C";
	public static final String SCHEDULE_CONTENT_TYPE_T = "T";
	public static final String SCHEDULE_CONTENT_TYPE_P = "P";
	
    public static final String SYS_OPTION_REGION = "SYSTEM.DEPLOY.REGION";
    public static final String SYS_OPTION_DEPLOY_SCHOOL = "SYSTEM.DEPLOY.SCHOOL";
    
    
    /**
     * 虚拟课程 学科类型ID
     */
	public static final String VIRTUAL_COURSE_TYPE = "11111111111111111111111111111111";

	public static final String UNIT_CLASS_EDU="1";
	public static final String UNIT_CLASS_SCH="2";
	
	//杭外定制---------------------start---------------
	public static final String[] SUBJECT_73_HW =new String[]{
			"GZ1014",//	政治
			"GZ1020",//	历史
			"GZ1017",//	地理
			"GZ1018",//	物理
			"GZ1019",//	化学
			"GZ1016",//	生物
			"GZ1015"//	技术
		};
		
		public static final String[] SUBJECT_TYPES_YSY_HW =new String[]{
			"GZ1001",//语文
			"GZ1002",//数学
			"GZ1003"//英语
		};
	//------------------------------end---------------------------
	
//	public static void main(String[] args) {
//		System.out.println(PWD.decode("DRNSFRFDN2RZKNBPGJHWMP47LSSDWRPNVLTHHQ7BRWRXFAWQFK93C2N3U8P3FLY8"));
//	}
}
