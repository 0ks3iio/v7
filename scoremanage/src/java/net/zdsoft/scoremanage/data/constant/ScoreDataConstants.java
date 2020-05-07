package net.zdsoft.scoremanage.data.constant;

import java.util.LinkedHashMap;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;

public class ScoreDataConstants {
	//教育局统考类型
	public static Map<String,String> eduTklx = new LinkedHashMap<String,String>();
	//学校统考类型
	public static Map<String,String> schTklx = new LinkedHashMap<String,String>();
	//用于查询
	public static Map<String,String> allTklx = new LinkedHashMap<String,String>();
	/**
	 * 科目id-高中语文
	 */
	public static final String SUBJECT_TYPE_7 = "3007";
	/**
	 * 科目id-高中数学
	 */
	public static final String SUBJECT_TYPE_8 = "3008";
	/**
	 * 科目id-高中英语
	 */
	public static final String SUBJECT_TYPE_9 = "3009";
	

	
	
	public static String TKLX_0="0";//校内考试
	public static String TKLX_1="1";//直属学校统考
	//public static String TKLX_2="2";//下属学校统考
	public static String TKLX_3="3";//校校联考

	// 考试类型 会考（学考）
	public static final String EXAM_TYPE_GRADUATE = "5";
	// 考试类型 期末考试
	public static final String EXAM_TYPE_FINAL = "12";
	
	static{
		
		allTklx.put(TKLX_0, "校内考试");
		allTklx.put(TKLX_1, "直属学校统考");
		//allTklx.put(TKLX_2, "下属学校统考");
		allTklx.put(TKLX_3, "校校联考");
		
		eduTklx.put(TKLX_1, allTklx.get(TKLX_1));
		//eduTklx.put(TKLX_2, allTklx.get(TKLX_2));
		eduTklx.put(TKLX_3, allTklx.get(TKLX_3));
		
		schTklx.put(TKLX_0, allTklx.get(TKLX_0));
		schTklx.put(TKLX_3, allTklx.get(TKLX_3));
		
	}
	
	public static String CLASS_TYPE1= Clazz.CLASS_TYPE_XZB;//行政班
	public static String CLASS_TYPE2= Clazz.CLASS_TYPE_JXB;//教学班
	
	public static String  FILTER_TYPE1="1";//不排考
	public static String  FILTER_TYPE2="2";//不统分
	
	public static String ZERO32="00000000000000000000000000000000";
	
	//分数线设置
	public static String STAT_TYPE10="10";//分数线
	public static String STAT_METHOD_DO11="11";//分数线百分比
	public static String STAT_METHOD_DO12="12";//分数线名次
	public static String STAT_METHOD_DO13="13";//分数线分值
	public static Map<String,String> statType1 = new LinkedHashMap<String,String>();
	public static Map<String,String> STAT_METHOD_DO_MAP = new LinkedHashMap<String,String>();
	static{
		statType1.put(STAT_TYPE10, "分数线");
		
		STAT_METHOD_DO_MAP.put(STAT_METHOD_DO11, "百分比");
		STAT_METHOD_DO_MAP.put(STAT_METHOD_DO12, "名次");
		STAT_METHOD_DO_MAP.put(STAT_METHOD_DO13, "分值");
	}
	public static String STAT_TYPE21="21";//分数段百分比
	public static String STAT_TYPE22="22";//分数段名次
	public static String STAT_TYPE23="23";//分数段分值
	public static Map<String,String> statType2 = new LinkedHashMap<String,String>();
	static{
		statType2.put(STAT_TYPE21, "百分比");
		statType2.put(STAT_TYPE22, "名次");
		statType2.put(STAT_TYPE23, "分值");
	}
	 /**
     * 成绩类型：分数或等第
     */
    public static final String ACHI_SCORE = "S";// 分数
    public static final String ACHI_GRADE = "G";// 等第
    
    public static final String RANGE_TYPE99 = "99";//考试科目中range_type=99表示教学班
    
    public static final String STATISTIC_TYPE0="0";//统计--班级范围
    public static final String STATISTIC_TYPE1="1";//统计--学校范围
    public static final String STATISTIC_TYPE2="2";//统计--统考范围
    
    public static final String SCORE_STATISTIC_JOB="score_statistic_job";//和宏里面taskBusinessType保持一致
}
