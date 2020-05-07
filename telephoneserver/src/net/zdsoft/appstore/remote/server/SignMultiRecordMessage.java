package net.zdsoft.appstore.remote.server;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.zdsoft.appstore.entity.AttendanceRecord;
import net.zdsoft.appstore.remote.AbstractMessage;
import net.zdsoft.appstore.remote.SigninServerLocator;
import net.zdsoft.keel.util.DateUtils;
import net.zdsoft.keel.util.StringUtils;

import org.apache.log4j.Logger;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.io.IoSession;

/**
 *
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:34:35 $
 */
public class SignMultiRecordMessage extends SignInMessage {

    private static Logger logger = Logger.getLogger(SignMultiRecordMessage.class);
    private static String REGEX = "\\w+";
    private static String REGEX_CARD = "[0-9a-fA-F]+";
    protected static final String LASTSTARTTIME = "laststarttime";

    private String deviceId;
    private String cardId;
    private Date startTime;
    private int inOutFlag; 
    private boolean error;
    
    protected void processBodyBatch(IoSession session) {
    	deviceId = new String(body, 0, 18).trim();
    	if (deviceId == null || !deviceId.matches(REGEX)) {
            error = true;
            String bodyStr = new String(body);
            logger.error("deviceId error[" + deviceId + "],body[" + bodyStr + "]");
            return;
        }

        cardId = new String(body, 18, 18).trim();
        if (cardId == null || !cardId.matches(REGEX_CARD)) {
            error = true;
            String bodyStr = new String(body);
            logger.error("cardId error[" + cardId + "],body[" + bodyStr + "]");
            return;
        }

        String startTimeString = new String(body, 54, 14);
        startTime = DateUtils.string2Date(startTimeString, "yyyyMMddHHmmss");
        if (startTime == null) {
            error = true;
            String bodyStr = new String(body);
            logger.error("startTimeString error[" + startTimeString + "],body[" + bodyStr + "]");
            return;
        }
        
        inOutFlag = Integer.parseInt(new String(body, 68, 1).trim());
        
        if (logger.isInfoEnabled()) {
            logger.info("deviceId[" + deviceId + "],cardId[" + cardId + "],startTime[" + 
            		startTime + "],inOutFlag[" + inOutFlag + "]");
        }
        
        // 同一分钟内的重发控制，直接返回成功
//        String lastStartTime = startTimeString + cardId;
//        if (lastStartTime.equals(session.getAttribute(LASTSTARTTIME))) {
//        	return;
//        }else {
//        	session.setAttribute(LASTSTARTTIME, lastStartTime);
//        }
        
        AttendanceRecord signRecordInfo = new AttendanceRecord();
        signRecordInfo.setDeviceId(deviceId);
        signRecordInfo.setCardNumber(cardId);
        signRecordInfo.setRecodeTime(startTime);
        signRecordInfo.setIoType(inOutFlag); // 从刷卡记录获得进出标志
        ConcurrentLinkedQueue<AttendanceRecord> queue = SigninServerLocator.getInstance().getSigninRecordQueue();
        SigninServerLocator.getInstance().offerQueue(queue, signRecordInfo, body);
    }


    protected int processResponse(ResponseMessage responseMessage) {

        // 写响应包
        responseMessage.responseLength = AbstractMessage.getHeadMaxLength() + 1;
        responseMessage.writeBuffer = ByteBuffer.allocate(responseMessage.responseLength);
        responseMessage.writeBuffer.put("0011".getBytes());
        responseMessage.writeBuffer.put(getFuncNo().getBytes());
        responseMessage.writeBuffer.put(StringUtils.enoughZero(String.valueOf(getSequence()), 4).getBytes());
        if (error) {
        	responseMessage.writeBuffer.put("0".getBytes());
        } else {
        	responseMessage.writeBuffer.put("1".getBytes());
        }
       
        responseMessage.writeBuffer.flip();
        if (logger.isDebugEnabled()) {
            logger.debug("responseLength[" + responseMessage.responseLength + "]");
        }
        return responseMessage.responseLength;
    }

    public String getCardId() {
    	return cardId;
    }
    
    public String getDeviceId() {
    	return deviceId;
    }
    
    public Date getStartTime() {
    	return startTime;
    }
}
