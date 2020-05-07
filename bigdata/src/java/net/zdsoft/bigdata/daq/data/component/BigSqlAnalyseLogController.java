package net.zdsoft.bigdata.daq.data.component;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.daq.data.entity.BigSqlAnalyseLog;
import net.zdsoft.framework.utils.RedisUtils;

/**
 * @author:zhujy
 * @since:2019/6/11 10:10
 */
public class BigSqlAnalyseLogController {
    /**
     * 提交业务操作日志
     *
     * @param log
     */
    public static void submitBigSqlAnalyseLog(BigSqlAnalyseLog log) {
        //暂不处理
        RedisUtils.lpush("sqlAnalyseLog", JSONObject.toJSONString(log));
    }

}
