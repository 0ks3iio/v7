package net.zdsoft.studevelop.data.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author weixh
 * @since 2017-4-12 上午10:37:54
 */
public class StuDevelopConstant {
	
	/**
	 * 每个学校、班级、主题活动最多可保存的图片数量
	 */
	public final static int MAX_SIZE_ACTIVITY = 30;
	
	/** 存放成长手册 */
	public final static String DEVDOC_PATH_DIR = "devdoc";
	
	/**
	 * 活动类型-我的校园
	 */
	public final static String ACTIVITY_TYPE_SCHOOL_INFO = "school_info";
	/**
	 * 活动类型-校园活动
	 */
	public final static String ACTIVITY_TYPE_SCHOOL_ACTIVITY = "school_activity";
	/**
	 * 活动类型-班级活动
	 */
	public final static String ACTIVITY_TYPE_CLASS_ACTIVITY = "class_activity";
	/**
	 * 活动类型-主题活动
	 */
	public final static String ACTIVITY_TYPE_THEME_ACTIVITY = "theme_activity";
	/**
	 * 活动类型-校外表现
	 */
	public final static String ACTIVITY_TYPE_OUT_SCHOOL_PERFORMANCE = "out_school_performance";
	/**
	 * 活动类型-我的假期
	 */
	public final static String ACTIVITY_TYPE_KIDS_HOLIDAY = "kids_holiday";
	/**
	 *
	 */
	public final static String  ACTIVITY_TYPE_CLASS_HONOR = "class_honor";
	/**
	 * 学校的
	 */
	public final static String RANGETYPE_SCH = "1";
	/**
	 * 班级的
	 */
	public final static String RANGETYPE_CLASS = "2";
	/**
	 * 学生的
	 */
	public final static String RANGETYPE_STU = "3";
	
	public static final Map<String, List<String>> rangeTypeMap;
	private static String actType = "act";
//	private static String outType = "out";
//	private static String holidayType = "holiday";
//	private static String honorType = "honor";
	/**
	 * 附件表-校长头像
	 */
	public final static String OBJTYPE_MASTER_PIC = "master_pic";
	/**
	 * 附件表-学生头像（自我介绍）
	 */
	public final static String OBJTYPE_STU_PIC = "studevelop_stu_pic";
	/**
	 * 附件表--幸福的一家
	 */
	public final static String OBJTYPE_FALMILY_PIC = "studevelop_family_pic";
	/**
	 * 附件表-成长荣誉
	 */
	public final static String OBJTYPE_STUDEV_HONOR = "studev_honor";
	/**
	 *  附件表-class荣誉
	 */
	public final static String OBJTYPE_CLASS_HONOR = "class_honor";
	
	static {
		rangeTypeMap = new HashMap<>();
		List<String> acts = new ArrayList<>();
		acts.add(ACTIVITY_TYPE_SCHOOL_INFO);
		acts.add(ACTIVITY_TYPE_SCHOOL_ACTIVITY);
		acts.add(ACTIVITY_TYPE_CLASS_ACTIVITY);
		acts.add(ACTIVITY_TYPE_THEME_ACTIVITY);
		rangeTypeMap.put(actType, acts);
	}
	
	public static boolean isActAttachment(String objType) {
		if(StringUtils.isEmpty(objType)) {
			return false;
		}
		return rangeTypeMap.get(actType).contains(objType);
	}
	
	/**
	 * 附件文件夹名称
	 */
	public final static String ATTACHMENT_FILEPATH = "attachment";
	
	/**
	 * 原图名称
	 */
	public final static String PIC_ORIGIN_NAME = "origin";
	/**
	 * 手机显示图名称
	 */
	public final static String PIC_MOBILE_NAME = "mobile";
	public final static String IS_MOBILE_STR = "2"; //手机图

	public final static String DEFAULT_UNIT_ID = "00000000000000000000000000000000"; //默认unitId
	
	public final static String DEFAULT_ACADYEAR = "0000-0000"; //默认学年
	
	public final static String DEFAULT_SEMESTER = "0";  //默认学期
	
	public final static String REPORT_TEMPLATE_1 = "1"; //江北小学/初中模板1
	public final static String REPORT_TEMPLATE_2 = "2"; //滨江小学/初中模板2
	
	public static final String HONOR_TYPE_XJRW = "1"; //星级人物
	public static final String HONOR_TYPE_QCYGK = "2"; //七彩阳光卡
	
	public static final String HONOR_LEVEL_QMFZ = "08"; //全面发展

	/**
	 * 身心健康设置
	 */
	public static final String HEALTH_TYPE_TN = "1"; //体能
	public static final String HEALTH_TYPE_STZB = "2"; //身体指标
	public static final String HEALTH_TYPE_XLSZ = "3"; //心理素质

	public static final String HEALTH_IS_CLOSED = "1"; //心理素质
	public static final String HEALTH_IS_NOT_CLOSED = "0"; //心理素质

	public static final String DEPLOY_BINJIANG          = "BinJiang";
	public static final String DEPLOY_CIXI          = "ucan";
	public static final String PERMISSION_TYPE_GROWTH          = "2";//成长手册
	public static final String PERMISSION_TYPE_REPORT = "1";//素质报告单

	public static final String TEMPLATE_CODE_GRADE = "1";//模板成绩
	public static final String TEMPLATE_CODE_HEALTH = "2";//身心健康
	public static final String TEMPLATE_CODE_THOUGHT = "3";//思想素质

	public static final String TEMPLATE_MAINTAIN_INPUT = "1";//输入
	public static final String TEMPLATE_MAINTAIN_OPTION = "2";//单选

	public static final String PROJECT_STATE_SUB = "2";// 仅对学科显示
	public static final String PROJECT_STATE_CAT = "1";// 仅对学科类别显示
	
	// 成绩
	public static final String TEMPLATE_OBJECT_TYPE_SUBJECT = "12";// 仅对学科显示
	public static final String TEMPLATE_OBJECT_TYPE_CATEGORY = "11";// 仅对学科类别显示
	// 身心项目
	public static final String TEMPLATE_OBJECT_TYPE_TN = "21";// 体能，
	public static final String TEMPLATE_OBJECT_TYPE_STZB = "22";// 身体指标，身高/体重等
	public static final String TEMPLATE_OBJECT_TYPE_XLSZ = "23";// 心理素质，意志力/自主思考等
	// 思想素质
	public static final String TEMPLATE_OBJECT_TYPE_SXSZ = "31";// 思想素质，原综合素质指标等
}
