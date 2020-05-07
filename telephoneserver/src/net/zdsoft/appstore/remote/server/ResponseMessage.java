package net.zdsoft.appstore.remote.server;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:33:45 $
 */
public class ResponseMessage {

    public ByteBuffer writeBuffer;
    public int responseLength = 0;

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public void setWriteBuffer(ByteBuffer writeBuffer) {
        this.writeBuffer = writeBuffer;
    }

    public int getResponseLength() {
        return responseLength;
    }

    public void setResponseLength(int responseLength) {
        this.responseLength = responseLength;
    }

}
