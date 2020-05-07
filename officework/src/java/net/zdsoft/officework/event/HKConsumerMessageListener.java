package net.zdsoft.officework.event;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.officework.constant.HKPicTips;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dto.HkDataDto;
import net.zdsoft.officework.mqreceive.AcsEvent;
import net.zdsoft.officework.mqreceive.EventDis;
import net.zdsoft.officework.service.OfficeDataLogHkService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
@Component("hKConsumerMessageListener")
public class HKConsumerMessageListener implements MessageListener {
	private static final Logger log = Logger.getLogger(HKConsumerMessageListener.class);
	private static OfficeDataLogHkService getOfficeDataLogHkService() {
		return Evn.getBean("officeDataLogHkService");
	}
    @Override
	public void onMessage(Message msg) {
		try {
			// cms里发送的消息为BytesMessage，此处不做判断亦可
			if (msg instanceof BytesMessage) {
				BytesMessage bytesMessage = (BytesMessage) msg;
				long length = bytesMessage.getBodyLength();
				byte[] bt = new byte[(int) length];
				bytesMessage.readBytes(bt);
				dealMessage(bt);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	private void dealMessage(byte[] bt) throws Exception {
		EventDis.CommEventLog parseFrom = EventDis.CommEventLog
				.parseFrom(bt);
		boolean saveResult = true;
		ByteString extInfo = parseFrom.getExtInfo();
		// 输出扩展字段
		AcsEvent.AccessEventLog data = AcsEvent.AccessEventLog
				.parseFrom(extInfo);
		if(extInfo == null){
			saveResult = false;
		}
		//目前处理存入日志，
		if(parseFrom.getRsltMsgCount()>0){
			saveResult = false;
			for(int i=0;i<parseFrom.getRsltMsgCount();i++){
				log.info("reult"+i+":"+parseFrom.getRsltMsg(i).toJsonString());
			}
		}
		if(parseFrom.getTrigInfoCount()>0){
			for(int i=0;i<parseFrom.getTrigInfoCount();i++){
				log.info("reult"+i+":"+parseFrom.getTrigInfo(i).toJsonString());
			}
			saveResult = false;
		}
		if(saveResult){
			if(RedisUtils.hasLocked(OfficeConstants.CLOCK_IN_REDIS_LOCK_PREFIX+parseFrom.getLogId())){
				try{
					String logId = RedisUtils.get(OfficeConstants.LOG_ID_REDIS_PREFIX+parseFrom.getLogId());
					if(StringUtils.isNotBlank(logId)){
						saveResult = false;
					}else{
						RedisUtils.set(OfficeConstants.LOG_ID_REDIS_PREFIX+parseFrom.getLogId(), parseFrom.getLogId(),RedisUtils.TIME_ONE_DAY);
					}
				}finally{
					RedisUtils.unLock(OfficeConstants.CLOCK_IN_REDIS_LOCK_PREFIX+parseFrom.getLogId());
				}
			}else{
				saveResult = false;
			}
		}
			
		if(saveResult){
			log.info(parseFrom.toJsonString());
			log.info(data.toJsonString());
			HkDataDto dataDto = new HkDataDto();
			dataDto.setCardNumber(data.getEventCard());
			dataDto.setUnitIdx(parseFrom.getUnitIdx());
			dataDto.setCommInfo(parseFrom.toJsonString());
			dataDto.setDeviceId(data.getDeviceId()+"");
			dataDto.setExtInfo(data.toJsonString());
			dataDto.setInOut(data.getInOut());
			dataDto.setPicUrl(data.getPicUrl());
			dataDto.setEventCode(data.getEventCode()+"");
			dataDto.setPicTips(HKPicTips.DS_K1T600MF.getValue());
			dataDto.setInOutTime(DateUtils.string2DateTime(parseFrom.getStartTime()));
			getOfficeDataLogHkService().saveData(dataDto);
		}
	}
}