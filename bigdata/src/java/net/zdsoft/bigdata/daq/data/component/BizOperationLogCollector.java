package net.zdsoft.bigdata.daq.data.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * 
 * @author 业务日志收集器
 *
 */
public class BizOperationLogCollector {
	
	/**
	 * 提交业务操作日志
	 * 
	 * @param log
	 */
	public static void submitBizOperationLog(BizOperationLog log) {
		//暂不处理
		RedisUtils.lpush("bizOperationLog", JSONObject.toJSONString(log));
	}


	/**
	 * 记录业务操作日志
	 * @param logType 日志类型
	 * @param bizCode 业务代码
	 * @param bizName 业务名称
	 * @param subSystem 子系统
	 * @param description 描述
	 * @param oldData 修改前数据
	 * @param newData 修改后数据
	 */
	public static void saveBizOperationLog(String logType, String bizCode, String bizName, String subSystem, String description, Object oldData, Object newData) {
		LoginInfo loginInfo = getLoginInfo();

		BizOperationLog bizLog = new BizOperationLog();
		bizLog.setLogType(logType);
		bizLog.setBizCode(bizCode);
		bizLog.setBizName(bizName);
		bizLog.setDescription(description);
		bizLog.setSubSystem(subSystem);
		bizLog.setOperator(loginInfo.getRealName() + "(" + loginInfo.getUserName() + ")");
		bizLog.setOperationTime(new Date());

		if (oldData != null) {
			bizLog.setOldData(JSON.toJSONString(oldData));
		}

		if (newData != null) {
			bizLog.setNewData(JSON.toJSONString(newData));
		}
		submitBizOperationLog(bizLog);
	}

	private static LoginInfo getLoginInfo() {
		return (LoginInfo) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("loginInfo");
	}

}
