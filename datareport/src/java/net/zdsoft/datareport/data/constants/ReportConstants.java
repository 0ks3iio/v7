package net.zdsoft.datareport.data.constants;

public class ReportConstants {
	
	/**
	 * 是否定时发布	 0:否 1：是
	 */
	public static final Integer IS_TIME_SEND_0 = 0; 
	public static final Integer IS_TIME_SEND_1 = 1; 
	
	/**
	 * 是否添加附件上传	 0:否 1：是
	 */
	public static final Integer IS_ATTACHMENT_0 = 0;
	public static final Integer IS_ATTACHMENT_1 = 1; 
	
	/**
	 * 填报信息任务的状态 1.未发布 2.待发布 3.收集中 4.已完成
	 */
	public static final Integer REPORT_INFO_STATE_1 = 1;
	public static final Integer REPORT_INFO_STATE_2 = 2;
	public static final Integer REPORT_INFO_STATE_3 = 3;
	public static final Integer REPORT_INFO_STATE_4 = 4;
	
	/**
	 * 填报任务的模板类型 1.表格 2.表单
	 */
	public static final Integer REPORT_STRUCT_TYPE_1 = 1; 
	public static final Integer REPORT_STRUCT_TYPE_2 = 2; 
	
	/**
	 * 填报任务的表格类型 1.横列 2.纵列 2.横纵列
	 */
	public static final Integer REPORT_TABLE_TYPE_1 = 1;
	public static final Integer REPORT_TABLE_TYPE_2 = 2;
	public static final Integer REPORT_TABLE_TYPE_3 = 3;
	
	/**
	 * 模板列信息类型 1.横列 2.纵列
	 */
	public static final Integer REPORT_COLUMN_TYPE_1 = 1;
	public static final Integer REPORT_COLUMN_TYPE_2 = 2;
	
	/**
	 * 发布对象的状态  1：未提交 2：待审核 3：已审核 
	 */
	public static final Integer REPORT_TASK_STATE_1 = 1;
	public static final Integer REPORT_TASK_STATE_2 = 2;
	public static final Integer REPORT_TASK_STATE_3 = 3;
	
	/**
	 * 填报对象类型 1：单位 2：老师 3：学生 4：家长
	 */
	public static final Integer OBJECT_TYPE_1 = 1; //单位
	public static final Integer OBJECT_TYPE_2 = 2; //老师
	public static final Integer OBJECT_TYPE_3 = 3; //学生
	public static final Integer OBJECT_TYPE_4 = 4; //家长
	
	/**
	 * 表格标题类型1：表头 2：表尾 3.备注
	 */
	public static final Integer TITLE_TYPE_1 = 1;
	public static final Integer TITLE_TYPE_2 = 2;
	public static final Integer TITLE_TYPE_3 = 3;
	
	/**
	 * 填报上报数据类型 1.普通数据 2.统计数据
	 */
	public static final Integer RESULT_TYPE_1 = 1; 
	public static final Integer RESULT_TYPE_2 = 2; 
	
	public static final Integer LAST_INDEX = 30;
	
	public static final String REPORT_TASK_ACCESSORY = "REPORT_TASK_ACCESSORY"; //附件
	
	public static final String REPORT_TASK_ATT = "REPORT_TASK_ATT";
	
	public static final String REPORT_INFO_TEMPLATE = "REPORT_INFO_TEMPLATE";
	
	public static final String REPORT_INFO_STATS = "REPORT_INFO_STATS";
}
