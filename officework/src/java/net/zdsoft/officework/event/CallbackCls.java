/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.zdsoft.officework.event;

import java.util.List;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.officework.entity.OfficeHealthData;
import net.zdsoft.officework.service.OfficeHealthDataService;
import net.zdsoft.officework.utils.OfficeHealthUtils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import LovicocoAPParser.Events.APConfig;
import LovicocoAPParser.Events.APStatus;
import LovicocoAPParser.Events.DeviceDataCollect;
import LovicocoAPParser.Events.ILovicocoAPParserEventCallback;

/**
 *
 * @author tel
 */
public class CallbackCls implements ILovicocoAPParserEventCallback {
	private static final Logger log =  LoggerFactory.getLogger(CallbackCls.class);
	private static OfficeHealthDataService getOfficeHealthDataService() {
		return Evn.getBean("officeHealthDataService");
	}

    @Override
    public void OnAction(String ActionName, String Account, String arg2) {
        log.info("OnAction:"+ ActionName+","+Account+","+arg2);
    }

    @Override
    public void OnReceiveData(String Account, DeviceDataCollect Data, String arg2) {
    	//处理接收数据
    	log.info("OnReceiveData:"+ Data.toJsonString()+","+arg2);
    	List<OfficeHealthData> healthDatas = OfficeHealthUtils.convertData(Data, Account);
    	if(CollectionUtils.isNotEmpty(healthDatas)){
    		getOfficeHealthDataService().saveHealthData(healthDatas);
    	}
    }

    @Override
    public APConfig OnHeartPack(String Account, APStatus Status, String arg2) {
    	log.info("OnHeartPack:"+ Status.toJsonString()+","+arg2);
        //固定返回null
        return null;
        
    }

    @Override
    public void OnError(String Account, String message) {
    	log.error("天波AP--OnError:"+ message);
    }

}
