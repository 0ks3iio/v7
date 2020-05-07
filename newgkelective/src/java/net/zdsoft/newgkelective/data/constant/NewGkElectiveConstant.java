package net.zdsoft.newgkelective.data.constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NewGkElectiveConstant {

	/**
	 * 高中
	 */
	public static final int SECTION_3=3;
	
	/**
	 * 排课参数：01:教室场地02:教师 03:科目周课时04:上课时间
	 */
	public static final String ARRANGE_TYPE_01="01";
	/**
	 * 排课参数：02:教师 丢弃
	 */
	public static final String ARRANGE_TYPE_02="02";
	/**
	 * 排课参数： 03:科目周课时 丢弃
	 */
	public static final String ARRANGE_TYPE_03="03";
	/**
	 * 排课参数：04:上课时间 ;现在是排课特征
	 */
	public static final String ARRANGE_TYPE_04="04";
	
	/**
	 * 选课内关联type 01:选课科目02:禁选组合03:推荐组合04:不参与的学生05:锁定学生06:科目组合
	 */
	public static final String CHOICE_TYPE_01="01";
	/**
	 * 选课内关联type 02:禁选组合
	 */
	public static final String CHOICE_TYPE_02="02";
	/**
	 * 选课内关联type 03:推荐组合
	 */
	public static final String CHOICE_TYPE_03="03";
	/**
	 * 选课内关联type 04:不参与的学生
	 */
	public static final String CHOICE_TYPE_04="04";
	/**
	 * 选课内关联type 05:锁定学生
	 */
	public static final String CHOICE_TYPE_05="05";
	/**
	 * 选课内关联type 06:科目组合
	 * 发布选课科目组合重新启用，移除Deprecated注解
	 */
	public static final String CHOICE_TYPE_06="06";
	/**
	 * 选课内关联type 07教师特征互斥教师
	 */
	public static final String CHOICE_TYPE_07="07";	
	/**
	 * 选课内关联type 08 课程特征互斥课程
	 */
	public static final String CHOICE_TYPE_08="08";	
	/**
	 * 选课内关联type 09 分班方案拆课程信息
	 */
	public static final String CHOICE_TYPE_09="09";	
	
	/**
	 * 班级数据参数：0:组合数据1:行政班2:教学班  
	 * newgkelective_divide_class
	 */
	public static final String CLASS_TYPE_0="0";
	
	/**
	 * 班级数据参数：1:行政班
	 */
	public static final String CLASS_TYPE_1="1";
	/**
	 * 班级数据参数：2:教学班
	 */
	public static final String CLASS_TYPE_2="2";
	/**
	 * 班级数据参数：3:预先设置的教学班(不放在教学班的原因，后面算法开出来的教学班不一定完全按照预先的样子)
	 * 这个下面不存在学生，只关联相应行政班
	 */
	public static final String CLASS_TYPE_3="3";
	/**
	 * 班级数据参数：4:固定组合班级
	 */
	public static final String CLASS_TYPE_4="4";
	
	
	
	/**
	 * 3+0组合班
	 */
	public static final String GROUP_TYPE_3="3";
	/**
	 * 2+X组合班
	 */
	public static final String GROUP_TYPE_2="2";
	/**
	 * 1.7选三 2.理科 3.文科  4.语数英
	 */
	public static final String GROUP_TYPE_1 = "1";
	public static final String GROUP_TYPE_4 = "4";
	
	
	/**
	 * 上课时间设置参数 01:不排课02:必排 03：连堂指定时间  04:教师在其他年级 的上课时间  05: 来自教师组的禁排时间
	 * newgkelective_lesson_time_ex
	 */
	public static final String ARRANGE_TIME_TYPE_01="01";
	/**
	 * 上课时间设置参数 02:必排
	 */
	public static final String ARRANGE_TIME_TYPE_02="02";
	
	/**
	 * 上课时间设置参数03：连堂指定时间
	 */
	public static final String ARRANGE_TIME_TYPE_03="03";
	/**
	 * 上课时间设置参数04：教师在其他年级 的上课时间
	 */
	public static final String ARRANGE_TIME_TYPE_04="04";
	/**
	 * 上课时间设置参数05: 来自教师组的禁排时间
	 */
	public static final String ARRANGE_TIME_TYPE_05="05";
	/**
	 * 班级类型subjectType
	 * A:选考 B:学考 O:行政班 J:教学班(暂时语数英,(或者指分层的行政科目教学班))
	 * 文理用到：A:选考 O:行政班 J:教学班(暂时语数英)
	 * 
	 */
	public static final String SUBJECT_TYPE_A="A";
	public static final String SUBJECT_TYPE_B="B";
	public static final String SUBJECT_TYPE_O="O";
	public static final String SUBJECT_TYPE_J="J";
	
	/**
	 * 科目水平 A/B/C
	 */
	public static final String SUBJECT_LEVEL_A = "A";
	public static final String SUBJECT_LEVEL_B = "B";
	public static final String SUBJECT_LEVEL_C = "C";

	/**
	 * 选课类目级别
	 * 1 : 类别
	 * 2 : 科目组合
	 */
	public static final String CATEGORY_TYPE_1 = "1";
	public static final String CATEGORY_TYPE_2 = "2";
	
	/**
	 * 按照单科成绩分层
	 */
	public static final String DIVIDE_BY_SINGLE_SCORE = "1";
	/**
	 * 按照总分成绩分层
	 */
	public static final String DIVIDE_BY_ALL_SCORE = "2";
	/**
	 * 3科组合
	 */
	public static final String SUBJTCT_TYPE_3="3";
	/**
	 * 2科组合
	 */
	public static final String SUBJTCT_TYPE_2="2";
	/**
	 * 1科组合
	 */
	public static final String SUBJTCT_TYPE_1="1";
	/**
	 * 混合
	 */
	public static final String SUBJTCT_TYPE_0="0";
	
	
	/**
	 * 1:尖子班 2:平行班
	 * newgkelective_divide_class
	 */
	public static final String BEST_TYPE_1="1";
	public static final String BEST_TYPE_2="2";
	/**
	 * 1:手动 0:自动
	 */
	public static final String IS_HAND_1="1";
	public static final String IS_HAND_0="0";
	
	/**
	 * 是否判断参数 1:是 0:否    (完成，未完成)
	 * 0:未排课 1:全部完成排课(老师安排完成) 2:算法排课完成 3:解决完整冲突
	 */
	public static final String IF_1="1";
	public static final String IF_0="0";
	/**
	 * 2:算法排课完成 3:解决完整冲突 4:自动分配老师成功
	 */
	public static final String IF_OTHER_2="2";
	public static final String IF_OTHER_3="3";
	public static final String IF_OTHER_4="4";
	
	/**
	 * 是否判断参数 1:是 0:否   
	 */
	public static final int IF_INT_1=1;
	public static final int IF_INT_0=0;
	
//	/**
//	 * 去使用BaseConstants.SUBJECT_73
//	 */
//	public static final String[] SUBJECT_TYPES =new String[]{
//		"3001",//	政治
//		"3002",//	历史
//		"3003",//	地理
//		"3006",//	物理
//		"3011",//	化学
//		"3020",//	生物
//		"3037"//	技术
//	};
//	public static final String[] SUBJECT_TYPES_YSY =new String[]{
//		"3007",//语文
//		"3008",//数学
//		"3009"//英语
//	};
	public static final String YSY_SUBID="11111111111111111111111111111111";//语数英的subjectId值
	public static final String YSY_SUBNAME="语数英";
	
	public static final String ZCJ_SUBID="22222222222222222222222222222222";//总成绩的subjectId值
	public static final String ZCJ_SUBNAME="总成绩";

	
	public static final String QT_SUBID="99999999999999999999999999999999";//其它的subjectId值
	public static final String QT_SUBNAME="其他";
	/**
	 * 科目缓存前缀  key:choose_subject_choiceId
	 */
	public static final String CHOOSE_SUBJECT="choose_subject";
	/**
	 * 分班缓存前缀  key:divide_class_divideId
	 */
	public static final String DIVIDE_CLASS="divide_class";
	
	/**
	 * 排课缓存前缀  key:array_lesson_arrayId
	 */
	public static final String ARRAY_LESSON="array_lesson";
	
	
	/**
	 * *暂时01与05
	 *01:全固定模式，2+x 2跟行政班，x固定一个批次  B走;3+0 全走行政班 (重组行政班)
	 *02:7选3模式原行政班重新分班（改成半固定模式）
	 *05:选考单科分层模式(A:选考单科分层，B:走批次模式   不重组行政班,需要复制一份原行政班数据)
	 *03:文理科分层教学模式 — 语数外独立分班(重组行政班)
	 *04:文理科分层教学模式 — 语数外跟随文理组合分班(重组行政班)
	 *06:全手动模式
	 *07:行政班预排
	   newgkelective_divide
	*/
	public static final String DIVIDE_TYPE_01="01";
	public static final String DIVIDE_TYPE_02="02";
	@Deprecated
	public static final String DIVIDE_TYPE_03="03";
	@Deprecated
	public static final String DIVIDE_TYPE_04="04";
	public static final String DIVIDE_TYPE_05="05";
	public static final String DIVIDE_TYPE_06="06";
	public static final String DIVIDE_TYPE_07="07";
	/**
	 * 新添加的全手动
	 */
	public static final String DIVIDE_TYPE_08="08";
	
	/**
	 * 3+1+2----单科分层重组
	 */
	public static final String DIVIDE_TYPE_09="09";
	
	/**
	 * 3+1+2----固定重组
	 */
	public static final String DIVIDE_TYPE_10="10";
	
	//下面两种方式隐藏
	/**
	 * 3+1+2----单科分层不重组
	 * 
	 */
	public static final String DIVIDE_TYPE_11="11";
	/**
	 * 3+1+2----固定不重组
	 */
	public static final String DIVIDE_TYPE_12="12";

	
	/**
	 * 0:年级 2:老师 9:科目 7:总课表 5:班级科目  6:教师组
	 * newgkelective_lesson_time
	 */
	
	public static final String LIMIT_GRADE_0="0";
	public static final String LIMIT_TEACHER_2="2";
	public static final String LIMIT_SUBJECT_5="5";
	public static final String LIMIT_TEACHER_GROUP_6="6";
	public static final String LIMIT_SUBJECT_9="9";
	public static final String LIMIT_SUBJECT_7="7";
	
	/**
	 * 01:上课时间(newgkelective_lesson_time) 02:教师安排(newgkelective_teacher_plan)
	 * newgkelective_lesson_time_ex 
	 */
	public static final String SCOURCE_LESSON_01="01";
	public static final String SCOURCE_TEACHER_02="02";
	
	/**
	 * 场地设置参数 1：行政班 2：教学班
	 */
	public static final String SCOURCE_PLACE_TYEP_1="1";
	public static final String SCOURCE_PLACE_TYEP_2="2";
	
	public static final Map<String,String> dayOfWeekMap=new HashMap<String,String>();
	public static final int dayOfWeeks=7;
	
	static{
		dayOfWeekMap.put("0", "星期一");
		dayOfWeekMap.put("1", "星期二");
		dayOfWeekMap.put("2", "星期三");
		dayOfWeekMap.put("3", "星期四");
		dayOfWeekMap.put("4", "星期五");
		dayOfWeekMap.put("5", "星期六");
		dayOfWeekMap.put("6", "星期天");
		
	}
	
	public static final int WEEK_TYPE_ODD = 1; //单周
	public static final int WEEK_TYPE_EVEN = 2; //双周
	public static final int WEEK_TYPE_NORMAL = 3; //正常
	
	public static final Map<Integer,String> WEEK_TYPE_MAP = new HashMap<>(3);
	static {
		WEEK_TYPE_MAP.put(WEEK_TYPE_ODD, "单周");
		WEEK_TYPE_MAP.put(WEEK_TYPE_EVEN, "双周");
		WEEK_TYPE_MAP.put(WEEK_TYPE_NORMAL, "正常");
	}
	
	/**
	 * 1：原始分班结果 2：排课后最终分班结果 divide_id 存放的是array_id
	 */
	public static final String CLASS_SOURCE_TYPE1 = "1"; 
	public static final String CLASS_SOURCE_TYPE2 = "2"; 

	/**
	 * 行政班
	 */
	public static final String SUBJECT_ID_XING_ZHENG="xingzheng";
	
	/**
	 * 开设科目组别  受开班模式影响
	 *  1:7选三
	    2:理科
		3:文科
		4:语数英
		5:选课
		6:学考
		7:组合固定模式中 行政班可排时间点
	 */
	public static final String DIVIDE_GROUP_1="1";
	@Deprecated
	public static final String DIVIDE_GROUP_2="2";
	@Deprecated
	public static final String DIVIDE_GROUP_3="3";
	@Deprecated
	public static final String DIVIDE_GROUP_4="4";
	public static final String DIVIDE_GROUP_5="5";
	public static final String DIVIDE_GROUP_6="6";
	public static final String DIVIDE_GROUP_7="7";
	
	/**
	 * 学生选课调剂类型
	 * 1：不可调剂
	 * 2：可调剂
	 * 3：优先调剂
	 */
	public static final String SUBJECT_TYPE_1="1";
	public static final String SUBJECT_TYPE_2="2";
	public static final String SUBJECT_TYPE_3="3";
	
	/**
	 * 选课结果类型
	 * 01：已选科目
	 * 02：优先调剂到科目
	 * 03：明确不选科目
	 */
	public static final String KIND_TYPE_01="01";
	public static final String KIND_TYPE_02="02";
	public static final String KIND_TYPE_03="03";
	
	
	/**
	 * 单双周
	 * 1：单周
	 * 2：双周
	 * 3：正常
	 */
	public static final int FIRSTSD_WEEK_1=1;
	public static final int FIRSTSD_WEEK_2=2;
	public static final int FIRSTSD_WEEK_3=3;
	
	/**
	 * 微代码
	 * 'DM-GK-KSFP','课时分配'
	 * 'DM-GK-BTKSFP','半天课时分配'
	 * 'DM-GK-BLPFS','不连排方式'
	 */
	public static final String MCODE_KSFP="DM-GK-KSFP";
	public static final String MCODE_BTKSFP="DM-GK-BTKSFP";
	public static final String MCODE_BLPFS="DM-GK-BLPFS";
	
	/**
	 * 合班方式  1.合班 2.同时排课
	 */
	public static final String COMBINE_TYPE_1="1";
	public static final String COMBINE_TYPE_2="2";
	/**
	 * 01:7选3排课  02:行政班排课
	 */
	public static final String ARRANGE_XZB="02";
	public static final String ARRANGE_SEVEN="01";
	
	/**
	 * 科目组合 颜色 类型
	 * 1. 单个科目颜色 2.两科组合颜色  3.三科组合颜色
	 */
	public static final String COLOR_GROUP_TYPE_1="1";
	public static final String COLOR_GROUP_TYPE_2="2";
	public static final String COLOR_GROUP_TYPE_3="3";

    /**
     * 选课结果上报
     * 总人数 01 ，已选 02 ，未选 03 ，3科 04 ，单科 05
     */
    public static final String REPORT_CHOSE_TYPE_01="01";
    public static final String REPORT_CHOSE_TYPE_02="02";
    public static final String REPORT_CHOSE_TYPE_03="03";
    public static final String REPORT_CHOSE_TYPE_04="04";
    public static final String REPORT_CHOSE_TYPE_05="05";

    public static final Map<String, String> EXAM_BATCH = new TreeMap<>();

	static {
		EXAM_BATCH.put("1", "选考一");
		EXAM_BATCH.put("2", "选考二");
		EXAM_BATCH.put("3", "选考三");
	}

	public static final Map<String, String> STUDY_BATCH = new TreeMap<>();

	static {
		STUDY_BATCH.put("1", "学考一");
		STUDY_BATCH.put("2", "学考二");
		STUDY_BATCH.put("3", "学考三");
		STUDY_BATCH.put("4", "学考四");
	}
	//默认一些颜色
	public static final List<String> DEFAULT_COLOR=new ArrayList<>();
	//6选三科目图片名称
	public static final Map<String,String> DEFAULT_PNG=new HashMap<>();
	static {
		DEFAULT_COLOR.add("#47c6a4");
		DEFAULT_COLOR.add("#af89f3");
		DEFAULT_COLOR.add("#f46e97");
		DEFAULT_COLOR.add("#f58a53");
		DEFAULT_COLOR.add("#2b9cfe");
		DEFAULT_COLOR.add("#f8bd48");
		DEFAULT_COLOR.add("#71b0ff");
		
		DEFAULT_PNG.put("3006", "physics");
		DEFAULT_PNG.put("3002", "history");
		DEFAULT_PNG.put("3011", "chemistry");
		DEFAULT_PNG.put("3020", "biology");
		DEFAULT_PNG.put("3003", "geography");
		DEFAULT_PNG.put("3001", "politics");
		//防止杭外
		/**杭外
		 *  "GZ1014",//	政治
			"GZ1020",//	历史
			"GZ1017",//	地理
			"GZ1018",//	物理
			"GZ1019",//	化学
			"GZ1016",//	生物
			"GZ1015"//	技术
		 */
		DEFAULT_PNG.put("GZ1018", "physics");
		DEFAULT_PNG.put("GZ1020", "history");
		DEFAULT_PNG.put("GZ1019", "chemistry");
		DEFAULT_PNG.put("GZ1016", "biology");
		DEFAULT_PNG.put("GZ1017", "geography");
		DEFAULT_PNG.put("GZ1014", "politics");
	}
	//历史
	public static final Set<String> SUBJRCTCODE_LS_SET=new HashSet<>();
	public static final Set<String> SUBJRCTCODE_WL_SET=new HashSet<>();
	static {
		SUBJRCTCODE_LS_SET.add("3002");//历史
		SUBJRCTCODE_LS_SET.add("GZ1020");//杭外 历史
		SUBJRCTCODE_WL_SET.add("3006");//物理
		SUBJRCTCODE_WL_SET.add("GZ1018");
	}
	
	/**
	 * 分班模式：09：3+1+2 单科分层重组  
	 * 	  B-0：相当于物理历史学考跟选考结果无关，独立走班
	 *    B-1:相当于物理历史学考根据选考结果分层,走班
	 * 
	 * 分班模式 11：3+1+2单科分层不重组 
	 * 	 A-2:预先的分配组合结果物理历史班，选考按行政班上课  剩余4门走批次
	 * 	 A-1:相当于物理历史选考根据预先的分配组合结果物理历史班分层
	 * 
	 * 	 B-2:预先的分配组合结果物理历史班，学考按行政班上课  剩余4门走批次
	 * 	 B-1:相当于物理历史学考根据预先的分配组合结果物理历史班分层
	 * 	 B-0：相当于物理历史学考跟选考结果无关，独立走班
	 */
	public static final String FOLLER_TYPE_A1="A-1";
	public static final String FOLLER_TYPE_A2="A-2";
	
	public static final String FOLLER_TYPE_B0="B-0";
	public static final String FOLLER_TYPE_B1="B-1";
	public static final String FOLLER_TYPE_B2="B-2";
}


