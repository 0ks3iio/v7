package net.zdsoft.base.constant;

public class BaseDataCenterConstant {
	
	public static final String MAX_NUM_DAY_ERROR_MSG = "今天调用的次数已经超过限制！";
    public static final String APPLY_ERROR_MSG = "该类型的接口没有申请！";
    public static final String URI_PREFIX_SAVE_UPDATE = "/openapi/sync/v3.0/";
    public static final String URI_PREFIX_SAVE = "/openapi/sync/save/v3.0/";
    public static final String URI_PREFIX_UPDATE = "/openapi/sync/update/v3.0/";
    
    public static final String  DATA_IN_REDIS_LOCK_PREFIX = "data.in.redis.lock.prefix";

    
    public static final String IS_SECURITY = "dacacenter.is.security";
    public static final String TICKET_KEY = "ticketKey";
    
    
    public static final int BOOLEAN_TRUE_VAL  = 1;
    public static final int BOOLEAN_FALSE_VAL = 0;
}
