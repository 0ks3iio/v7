package net.zdsoft.api.openapi.pushjob.constant;

public class BaseOpenapiConstant {

	/**
	 * 公共的地址前缀 V2.0
	 */
	public static final String  BASE_V2_URL= "/remote/openapi/v2.0/";
	
	/**
	 * 更新的时间参数
	 */
	public static final String  BASE_UPDATE_NAME= "dataModifyTime";
	
	/**
	 * 更新的时间参数格式：yyyyMMddHHmmssSSS
	 */
	public static final String  BASE_UPDATE_SIMPLE_DATE_FORMAT= "yyyyMMddHHmmssSSS";
	
	/**
	 * 调用接口的ticketKey
	 */
	public static final String  BASE_OPENAPI_TICKET_NAME= "ticketKey";
	
	/**
	 * get  post
	 */
	public static final String  GET_METHOD= "get";
	public static final String  POST_METHOD= "post";
	
	/**
	 * 推送的最大分页 ,后面可以走配置
	 */
	public static final int MAX_PAGE_SIZE= 1000;
	
	/**
	 * 获取包含软删的数据
	 */
	public static final String IS_DELETED_ALL= "-1";
	
	/**
	 * 分页的参数 ， 每页获取的数量
	 */
	public static final String  PAGE_PARAM_NAME= "page";
	public static final String  LIMIT_PARAM_NAME= "limit";
	public static final String  DEFAULT_IS_DELETED= "isDeleted";
	
	/**
	 * 结果的参数定义
	 */
	public static final String  PUSH_RESULT_COUNT_NAME= "count"; //推送的总数 
	public static final String  PUSH_RESULT_DATA_NAME= "data";   //封装的数据
	public static final String  PUSH_RESULT_UPDATE_STAMP_NAME= "updateStamp"; //推送的时间（更新的时间戳）
	public static final String  PUSH_RESULT_IS_FIRST_NAME= "isFirst"; //是否首次推送
	
	/** 
	 * 返回结果的参数定义
	 */
	public static final String  RETURN_RESULT_CODE_NAME= "code";       //是否成功  1—成功  -1 ---失败
	public static final String  RETURN_RESULT_MESSAGE_NAME= "message"; //返回的信息
	
	/**
	 * 封装结果的类型： 1--fastjson  2--net.sf.json.JSONObject
	 */
	public static final int  FAST_JSON_TYPE = 1; 
	public static final int  SF_JSON_TYPE   = 2;
}
