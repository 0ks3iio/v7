package net.zdsoft.evaluation.data.constants;

public class EvaluationConstants {
	//评价类型
	public final static String EVALUATION_TYPE_TEACH = "10";//教学调查（必修课调查）
	public final static String EVALUATION_TYPE_TEACHER = "11";//班主任调查
	public final static String EVALUATION_TYPE_TOTOR = "12";//导师调查
	public final static String EVALUATION_TYPE_ELECTIVE = "13";//选修课调查
	
	//指标类型
	public final static String ITEM_TYPE_EVALUA = "10";//评教项目
	public final static String ITEM_TYPE_FILL="11";//满意率
	public final static String ITEM_TYPE_ANSWER="12";//解答题
	
	public final static String EVALUATION_STATE_SAVE = "1";//保存
	public final static String EVALUATION_STATE_SUBMIT="2";//提交
	
	public final static String EVALUATION_STATE_NOT_START = "0";//未开始答题
	public final static String EVALUATION_STATE_START="1";//开始答题
	public final static String EVALUATION_STATE_END = "2";//结束答题
	//统计维度
	public final static String STATE__DIMENSION_ONE = "0";//个人
	public final static String STATE__DIMENSION_GRADE = "1";//年级
	public final static String STATE__DIMENSION_SCH = "2";//全校
	public final static String STATE__DIMENSION_CLASS = "3";//班级
	public final static String STATE__DIMENSION_SUBJECT = "4";//学科
	
	public static final String VERIFY_CODE_CACHE_KEY = "desktop_verify_code_key";
	/**
	 * 指标类型
	 */
	public static final String EVA_ITEM_TYPE = "DM-ZBLX";
	/**
	 * 评价类型
	 */
	public static final String EVA_EVA_TYPE = "DM-PJLX";
	
	/**
	 * 单选
	 */
	public static final String EVA_SELECT_ONE="O";
	/**
	 * 多选
	 */
	public static final String EVA_SELECT_MORE="M";
	/**
	 * 只新增项目 无选项
	 */
	public static final String EVA_SELECT_NO="N";
	
	
}
