package net.zdsoft.appstore.remote.server;

import net.zdsoft.appstore.remote.AbstractMessage;
import net.zdsoft.keel.util.StringUtils;

import org.apache.log4j.Logger;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.io.IoSession;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:32:48 $
 */
public class ConnectStatusMessage extends SignInMessage {

    private static Logger logger = Logger.getLogger(ConnectStatusMessage.class);

    public ConnectStatusMessage() {
    }

    protected void processBodyBatch(IoSession session) {
        // 保存话机的一些信息
        SignInDeviceInfo wirelessPhoneInfo = null;
        wirelessPhoneInfo = (SignInDeviceInfo) session.getAttribute(PHONEINFO);

        if (wirelessPhoneInfo == null) {
            wirelessPhoneInfo = new SignInDeviceInfo();
            session.setAttribute(PHONEINFO, wirelessPhoneInfo);
        }

        wirelessPhoneInfo.connectStateCount++;
        if (logger.isDebugEnabled()) {
            logger.debug("connectStateCount[" + wirelessPhoneInfo.connectStateCount + "],versionInfo["
                    + wirelessPhoneInfo.versionInfo + "]");
        }

        if (wirelessPhoneInfo.connectStateCount % 20 == 1) {
        	// 发送状态查询包
        	ResponseMessage responseMessage = new ResponseMessage();
        	AbtStatusMessageResp abtStatusMessageResp = new AbtStatusMessageResp();
        	abtStatusMessageResp.processResponse(session, responseMessage);
        	session.write(responseMessage.writeBuffer, null);
        }

        if (wirelessPhoneInfo.connectStateCount >= 10000) {
            wirelessPhoneInfo.connectStateCount = 0;
        }
    }

    protected int processResponse(ResponseMessage responseMessage) {
        // 写响应包
        responseMessage.responseLength = AbstractMessage.getHeadMaxLength();
        responseMessage.writeBuffer = ByteBuffer.allocate(responseMessage.responseLength);

        responseMessage.writeBuffer.put("0010".getBytes());
        responseMessage.writeBuffer.put(getFuncNo().getBytes());
        responseMessage.writeBuffer.put(StringUtils.enoughZero(String.valueOf(getSequence()), 4).getBytes());
        
        responseMessage.writeBuffer.flip();
        if (logger.isDebugEnabled()) {
            logger.debug("responseLength[" + responseMessage.responseLength + "]");
        }
        return responseMessage.responseLength;
    }
}
