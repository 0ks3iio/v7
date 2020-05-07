package net.zdsoft.bigdata.data.service.impl;

import com.alibaba.fastjson.JSON;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author:zhujy
 * @since:2019/6/3 13:25
 */
@Service
public class BigLogServiceImpl extends BigdataBaseAction implements BigLogService{

    @Override
    public void insertLog(LogDto logDto) {
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
        bizLog.setBizCode(logDto.getBizCode());
        bizLog.setDescription("新增" + logDto.getDescription());
        bizLog.setNewData(JSON.toJSONString(logDto.getNewData()));
        bizLog.setBizName(logDto.getBizName());
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        BizOperationLogCollector.submitBizOperationLog(bizLog);
    }

    @Override
    public void updateLog(LogDto logDto) {
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
        bizLog.setBizCode(logDto.getBizCode());
        bizLog.setDescription("修改" +logDto.getDescription());
        bizLog.setOldData(JSON.toJSONString(logDto.getOldData()));
        bizLog.setNewData(JSON.toJSONString(logDto.getNewData()));
        bizLog.setBizName(logDto.getBizName());
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        BizOperationLogCollector.submitBizOperationLog(bizLog);
    }

    @Override
    public void deleteLog(LogDto logDto) {
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_DELETE);
        bizLog.setBizCode(logDto.getBizCode());
        bizLog.setDescription("删除" + logDto.getDescription());
        bizLog.setBizName(logDto.getBizName());
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        bizLog.setOldData(JSON.toJSONString(logDto.getOldData()));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
    }
}
