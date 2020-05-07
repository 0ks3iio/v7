package net.zdsoft.teaeaxam.constant;

/**
 * 
 * @author weixh
 * 2018年11月2日	
 */
public class TeaexamConstant {
	
	public static final String TYPE_CENTER_SCHOOL = "0";//中心校
    public static final String TYPE_GROUP_ALLIANCE = "1";//集团联盟
    public static final String TYPE_NORMAL_SCHOOL= "2";//普通学校
	
	/**
	 * 教师报名信息-报名成功
	 */
	public final static int STATUS_PASS = 2;// 报名成功
	/**
	 * 教师报名信息-审核不通过
	 */
	public final static int STATUS_FAIL = -1;// 审核不通过
	/**
	 * 教师报名信息-待审核
	 */
	public final static int STATUS_AUDITTING = 1;// 待审核
	/**
	 * 自动编排-缓存前缀
	 */
	public final static String AUTO_ARRANGE_PREFIX = "teaexam_autoarrange_";
	/**
	 * 自动编排-状态缓存后缀
	 */
	public final static String AUTO_ARRANGE_POSTFIX_STATUS = "_status";
	
	/**
	 * 自动编排-提示信息
	 */
	public final static String AUTO_ARRANGE_POSTFIX_MSG = "_msg";
	/**
	 * 自动编排-考试
	 */
	public final static String AUTO_ARRANGE_TYPE_EXAM = "_exam_";
	/**
	 * 自动编排-科目
	 */
	public final static String AUTO_ARRANGE_TYPE_SUB = "_sub_";
	
	/**
	 * 自动编排-编排中
	 */
	public final static String AUTO_ARRANGE_STATUS_ING = "ing";
	/**
	 * 自动编排-失败
	 */
	public final static String AUTO_ARRANGE_STATUS_FAIL = "fail";
	
	
	public final static String ALL_LIMIT = "00000000000000000000000000000000";
	
	//考试统计状态
	public final static String EXAM_COUNT_STATUS_1 = "1";//未开始考试
	
	public final static String EXAM_COUNT_STATUS_2 = "2";//正在考试中
	
	public final static String EXAM_COUNT_STATUS_3 = "3";//未统计成绩
	
	public final static String EXAM_COUNT_STATUS_4 = "4";//已统计成绩
	
	public final static String SCORE_GRADE_YX = "1";//优秀
	
	public final static String SCORE_GRADE_HG = "2";//合格
	
	public final static String SCORE_GRADE_BHG = "3";//不合格
	
	/**
	 * 考试类型-考试
	 */
	public final static int EXAM_INFOTYPE_0 = 0;
	/**
	 * 考试类型-培训
	 */
	public final static int EXAM_INFOTYPE_1 = 1;

}
