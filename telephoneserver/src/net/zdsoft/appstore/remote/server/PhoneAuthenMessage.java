package net.zdsoft.appstore.remote.server;

import net.zdsoft.appstore.remote.AbstractMessage;
import net.zdsoft.keel.util.StringUtils;

import org.apache.log4j.Logger;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.io.IoSession;

public class PhoneAuthenMessage extends SignInMessage{
	private static Logger logger = Logger.getLogger(PhoneAuthenMessage.class);

    private String deviceId;
    private String reserved;
    
    protected boolean processBodyBatch(IoSession session) {
        deviceId = new String(body, 0, 18).trim();
        reserved = new String(body, 18, 8).trim();

        if (logger.isInfoEnabled()) {
            logger.info("deviceId[" + deviceId + "],Reserved[" + reserved + "]");
        }
    	return true;
    }

    protected int processResponse(ResponseMessage responseMessage) {
        // 写响应包
        responseMessage.responseLength = AbstractMessage.getHeadMaxLength() + 1;
        responseMessage.writeBuffer = ByteBuffer.allocate(responseMessage.responseLength);

        responseMessage.writeBuffer.put("0011".getBytes());
        responseMessage.writeBuffer.put(getFuncNo().getBytes());
        responseMessage.writeBuffer.put(StringUtils.enoughZero(String.valueOf(getSequence()), 4).getBytes());
        responseMessage.writeBuffer.put("1".getBytes());
        
        responseMessage.writeBuffer.flip();
        if (logger.isDebugEnabled()) {
            logger.debug("responseLength[" + responseMessage.responseLength + "]");
        }
        return responseMessage.responseLength;
    }
    
    
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
}
