package net.zdsoft.comprehensive.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompreStatisticsConstants {
	
	/**
	 * scoremanage_compre_score scoremanageType
	 * 1、学科 
	 */
	public static final String TYPE_OVERALL="1";
	/**
	 * 2、英语
	 */
	public static final String TYPE_ENGLISH="2";
	/**
	 * 3、英语口试
	 */
	public static final String TYPE_ENG_SPEAK="3";
	/**
	 * 4、体育
	 */
	public static final String TYPE_GYM="4";
	/**
	 *  5、学考
	 */
	public static final String TYPE_XK="5";
	
	
	
	/**
	 * 成绩表类型  01:综合素质
	 */
//	public static final String SCORE_ZHSZ="01";
	/**
	 * 成绩表类型 02成绩分析
	 */
//	public static final String SCORE_CJFX="02";
	/**
	 * 成绩表类型 03学考分数
	 */
//	public static final String SCORE_XKFS="03";
	/**
	 * 一对多表中类型   01:折分值对应relationship_id是科目id
	 */
//	public static final String RELATION_ZFZ="01";
	/**
	 * 一对多表中类型   02:总评参数设置
	 */
//	public static final String RELATION_ZPCS="02";
	/**
	 * 一对多表中类型   03:总评总分  对应relationship_id是科目id
	 */
//	public static final String RELATION_ZPZF="03";
	/**
	 * 一对多表中类型  04: 总评排名对应relationship_id是comprehensive_setup的id
	 */
//	public static final String RELATION_ZFPM="04";
	/**
	 * 初中、高中的口试
	 */
	public static final String SUBJECT_CODE_KS="TY3001";
	/**
	 * 初中英语
	 */
	public static final String SUBJECT_CODE_YY_2="CZ2003";
	
	/**
	 * 高中英语
	 */
	public static final String SUBJECT_CODE_YY_3="GZ1003";
	
	/**
	 * 初中体育
	 */
	public static final String SUBJECT_CODE_TY_2="CZ2011";
	
	/**
	 * 高中体育
	 */
	public static final String SUBJECT_CODE_TY_3="GZ1013";
	
//	/**
//	 *  科目code-高中英语口试
//	 */
//	public static final String SUBJECT_TYPE3_41 = "S3041";
//	/**
//	 *  科目code-高中体育
//	 */
//	public static final String SUBJECT_TYPE3_42 = "S3042";
//	/**
//	 * 科目code-高中英语
//	 */
//	public static final String SUBJECT_TYPE3_9 = "3009";
//	
//	/**
//	 *  科目code-初中英语口试
//	 */
//	public static final String SUBJECT_TYPE2_41 = "S2041";
//	/**
//	 *  科目code-初中体育
//	 */
//	public static final String SUBJECT_TYPE2_42 = "S2042";
//	/**
//	 * 科目code-初中英语
//	 */
//	public static final String SUBJECT_TYPE2_3 = "2003";
//	
	
	
	/**
	 * 学考换算参数info_key
	 */
	public static final String INFO_KEY_5="005";
	
	/**
	 * 英语综合素质info_key
	 */
	public static final String INFO_KEY_2 = "002";
	
	
	/**
	 * 初三下 
	 */
	public static final String THIRD_LOWER = "232";
	
	/**
	 * 高一上
	 */
	public static final String SENIOR_ONE_LOWER = "311";
	
	/**
	 * 高一下
	 */
	public static final String SENIOR_ONE_UPPER = "312";
	
	/**
	 * 高二上
	 */
	public static final String SENIOR_TWO_LOWER = "321";
	
	/**
	 * 高二下
	 */
	public static final String SENIOR_TWO_UPPER = "322";
	
	/**
	 * 高三上
	 */
	public static final String THIRD_UPPER = "331";
	
	
	public static final String CLASS_TYPE_1 = "1";//行政班
	public static final String CLASS_TYPE_2 = "2";//教学班
	
//	public static final Map<String,Set<String>> SUBJECT_TYPES=new HashMap<String, Set<String>>();
//	
//	
//	static{
//		SUBJECT_TYPES.put(TYPE_GYM, new HashSet<String>());
//		SUBJECT_TYPES.get(TYPE_GYM).add(SUBJECT_TYPE2_42);
//		SUBJECT_TYPES.get(TYPE_GYM).add(SUBJECT_TYPE3_42);
//		
//		SUBJECT_TYPES.put(TYPE_ENG_SPEAK, new HashSet<String>());
//		SUBJECT_TYPES.get(TYPE_ENG_SPEAK).add(SUBJECT_TYPE2_41);
//		SUBJECT_TYPES.get(TYPE_ENG_SPEAK).add(SUBJECT_TYPE3_41);
//		
//		SUBJECT_TYPES.put(TYPE_ENGLISH, new HashSet<String>());
//		SUBJECT_TYPES.get(TYPE_ENGLISH).add(SUBJECT_TYPE2_3);
//		SUBJECT_TYPES.get(TYPE_ENGLISH).add(SUBJECT_TYPE3_9);
//		
//	}
	
	
	
	/**
	 * 英语口试换算成绩  10分制换5分制
	 */
	public static final float ENGLISH_SPEAK=0.5f;
	/**
	 * 初中体育  满分值4分
	 */
	public static final float GYM_2=4f;
	/**
	 * 高中体育 满分值15分
	 */
	public static final float GYM_3=15f;
	
	
	public static final Map<String, String> TYPE_NAME_MAP=new HashMap<String, String>();
	static{
		TYPE_NAME_MAP.put(TYPE_OVERALL, "学科成绩");
		TYPE_NAME_MAP.put(TYPE_ENGLISH, "英语");
		TYPE_NAME_MAP.put(TYPE_ENG_SPEAK, "口试");
		TYPE_NAME_MAP.put(TYPE_GYM, "体育");
	}
	
	
	/**
	 * 特殊科目处理  选考科目（直接将杭外环境的课程库中的：GZ1018(高中物理选考)、GZ1019(高中化学选考)、GZ1016(高中生物选考)、
	 * GZ1014(高中政治选考)、GZ1017(高中地理选考)、GZ1015(高中技术选考)、GZ1020(高中历史选考) 需要换算0.75难度
	 */
	public static final List<String> CODE_73=new ArrayList<String>();
	static{
		CODE_73.add("GZ1014");//高中政治选考
		CODE_73.add("GZ1015");//高中技术选考
		CODE_73.add("GZ1016");//高中生物选考
		CODE_73.add("GZ1017");//高中地理选考
		CODE_73.add("GZ1018");//高中物理选考
		CODE_73.add("GZ1019");//高中化学选考
		CODE_73.add("GZ1020");//高中历史选考
	}
	/**
	 * 学考10们
	 */
	public static final List<String> HW_CODE_10=new ArrayList<String>();
	static{
		HW_CODE_10.add("GZ1004");//高中政治
		HW_CODE_10.add("GZ1005");//高中历史
		HW_CODE_10.add("GZ1006");//高中地理
		HW_CODE_10.add("GZ1007");//高中物理
		HW_CODE_10.add("GZ1008");//高中化学
		HW_CODE_10.add("GZ1009");//高中生物
		HW_CODE_10.add("GZ1010");//高中技术
		HW_CODE_10.add("GZ1001");//语
		HW_CODE_10.add("GZ1002");//数
		HW_CODE_10.add("GZ1003");//英
	}
	
}
