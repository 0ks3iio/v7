package net.zdsoft.appstore.remote.server;

import java.util.Date;

import net.zdsoft.appstore.remote.AbstractMessage;
import net.zdsoft.keel.util.DateUtils;
import net.zdsoft.keel.util.StringUtils;

import org.apache.log4j.Logger;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.io.IoSession;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:32:26 $
 */
public class AbtStatusMessageResp extends SignInMessage {

    private static Logger logger = Logger.getLogger(AbtStatusMessageResp.class);

    private static String SEQUENCE = "Sequence";

    private String deviceId;
    private String versionInfo;

    public AbtStatusMessageResp() {
    }

    protected void processBody(IoSession session) {
        deviceId = new String(body, 0, 18).trim();
        versionInfo = new String(body, 18, 18).trim();

        if (logger.isDebugEnabled()) {
            logger.debug("deviceId[" + deviceId + "],versionInfo[" + versionInfo + "]");
        }

        // 保存话机的一些信息
        SignInDeviceInfo wirelessPhoneInfo = null;
        wirelessPhoneInfo = (SignInDeviceInfo) session.getAttribute(PHONEINFO);

        if (wirelessPhoneInfo == null) {
            wirelessPhoneInfo = new SignInDeviceInfo();
            wirelessPhoneInfo.deviceId = deviceId;
            wirelessPhoneInfo.versionInfo = versionInfo;
            session.setAttribute(PHONEINFO, wirelessPhoneInfo);
        }
    }

    protected int processResponse(IoSession session, ResponseMessage responseMessage) {

        // 写响应包
        responseMessage.responseLength = AbstractMessage.getHeadMaxLength() + 14;
        responseMessage.writeBuffer = ByteBuffer.allocate(responseMessage.responseLength);

        responseMessage.writeBuffer.put("0024".getBytes());
        responseMessage.writeBuffer.put("82".getBytes());

        int sequence = ((Integer) session.getAttribute(SEQUENCE)).intValue();
        if (++sequence > 9999) {
            sequence = 0;
        }
        session.setAttribute(SEQUENCE, new Integer(sequence));

        responseMessage.writeBuffer.put(StringUtils.enoughZero(String.valueOf(sequence), 4).getBytes());
        responseMessage.writeBuffer.put(DateUtils.date2String(new Date(), "YYYYMMDDHHMMSS").getBytes());

        responseMessage.writeBuffer.flip();
        if (logger.isDebugEnabled()) {
            logger.debug("responseLength[" + responseMessage.responseLength + "]");
        }
        return responseMessage.responseLength;
    }

    /**
     * @return 返回 deviceId。
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     *            要设置的 deviceId。
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return 返回 versionInfo。
     */
    public String getVersionInfo() {
        return versionInfo;
    }

    /**
     * @param versionInfo
     *            要设置的 versionInfo。
     */
    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

}
