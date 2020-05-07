package net.zdsoft.basedata.constant;

public class JobConstants {
	
	public static final String SERVER_TYPE_7="7";//表示7.0平台
	public static final String TYPE_1="1";//表示耗时任务
	
	public static final int TASK_STATUS_NO_HAND = 0;// 等待执行
    public static final int TASK_STATUS_IN_HAND = 1;// 正在执行
    public static final int TASK_STATUS_SUCCESS = 2;// 执行成功
    public static final int TASK_STATUS_ERROR = 3;// 执行失败
    public static final int TASK_STATUS_PRE_HAND = 4;//预处理，和待执行和正在执行区分，以便于消息提示
    public static final int TASK_STATUS_NOT_NEED_HAND = 9;//不需要处理
    
    public static final String KEY_SYS_SERVICE_NAME="sys_service_name";
    
    /**
     * key前缀
     */
    public static final String CACHE_KEY_TASK_JOB_="TaskJob.";
    
    /**
     * 缓存时间
     */
    public static final int CACHE_SECONDS=3600;
    
    /**
     * 执行状态
     */
    public static final String STATUS_END = "status_end";
}
