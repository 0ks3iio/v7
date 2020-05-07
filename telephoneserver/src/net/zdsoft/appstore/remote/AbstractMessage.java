package net.zdsoft.appstore.remote;

import org.apache.log4j.Logger;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:34:54 $
 */
public abstract class AbstractMessage {

    private static Logger logger = Logger.getLogger(AbstractMessage.class);

    private static int headMaxLength;

    protected byte[] head;
    protected byte[] body;

    private int bodyMaxLength;

    private static byte startFlag;

    public AbstractMessage() {
    }

    protected static void setStartFlag(byte startFlag) {
        AbstractMessage.startFlag = startFlag;
    }

    protected static void setHeadMaxLength(int headMaxLength) {
        AbstractMessage.headMaxLength = headMaxLength;
        logger.debug("setHeadMaxLength " + headMaxLength);
    }

    public static int getHeadMaxLength() {
        return headMaxLength;
    }

    public static byte getStartFlag() {
        return startFlag;
    }

    public int getMissingHeadLength() {
        return head == null ? headMaxLength : headMaxLength - head.length;
    }

    public void readHead(byte[] head) {
        if (this.head == null) {
            this.head = head;
            return;
        }

        // 当head已经存在时合并数据
        byte[] bytes = new byte[this.head.length + head.length];
        System.arraycopy(this.head, 0, bytes, 0, this.head.length);
        System.arraycopy(head, 0, bytes, this.head.length, head.length);
        this.head = bytes;
    }

    public abstract boolean processHead();

    public int getBodyMaxLength() {
        return bodyMaxLength;
    }

    public void setBodyMaxLength(int bodyMaxLength) {
        this.bodyMaxLength = bodyMaxLength;
    }

    public int getMissingBodyLength() {
        return body == null ? bodyMaxLength : bodyMaxLength - body.length;
    }

    public void readBody(byte[] body) {
        if (this.body == null) {
            this.body = body;
            return;
        }

        // 当body已经存在时合并数据
        byte[] bytes = new byte[this.body.length + body.length];
        System.arraycopy(this.body, 0, bytes, 0, this.body.length);
        System.arraycopy(body, 0, bytes, this.body.length, body.length);
        this.body = bytes;
    }

    public byte[] getHead() {
        return head;
    }

    public byte[] getBody() {
        return body;
    }

    public String getReceiveMsg() {
        String msg = "";
        if (this.head != null && this.head.length > 0) {
            byte[] head = new byte[this.head.length];
            System.arraycopy(this.head, 0, head, 0, this.head.length);
            msg = new String(head);
        }
        if (this.body != null && this.body.length > 0) {
            byte[] body = new byte[this.body.length];
            System.arraycopy(this.body, 0, body, 0, this.body.length);
            msg = msg + new String(body);
        }
        return msg;
    }

}
