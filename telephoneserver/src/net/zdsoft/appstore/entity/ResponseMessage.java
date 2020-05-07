package net.zdsoft.appstore.entity;

import org.apache.mina.common.ByteBuffer;

/**
 * 响应信息实体类
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2014-6-19 下午03:50:09 $
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
